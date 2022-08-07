package com.appfactory86.radio;

import static com.appfactory86.radio.Constants.PAUSE_ACTION;
import static com.appfactory86.radio.Constants.PLAY_ACTION;
import static com.appfactory86.radio.Constants.RESUME_ACTION;
import static com.appfactory86.radio.Constants.STOP_ACTION;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.MediaPlayer;

import android.os.Binder;
import android.os.IBinder;



import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.io.IOException;
import java.nio.channels.Channel;

public class StreamingService extends Service implements MediaPlayer.OnPreparedListener {
    private static final String stream = "https://edge126.rcs-rds.ro/profm/profm.mp3";
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private final IBinder binder = new LocalBinder();
    private boolean isServiceStarted = false;

    public class LocalBinder extends Binder {
        StreamingService getService() {
            return StreamingService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        intent.getAction();
        return binder;
    }

    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    public void pause() {
        mediaPlayer.pause();
    }

    public void resume() {
        mediaPlayer.start();
    }

    public void stop() {
        mediaPlayer.release();
        mediaPlayer = null;
    }

    private void start() {
        if (isServiceStarted) {
            return;
        }
        mediaPlayer.setOnPreparedListener(this);

        mediaPlayer.setAudioAttributes(new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .build());
        try {
            mediaPlayer.setDataSource(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.prepareAsync();

        final String Notification_Channel_ID = "Foreground Service";
        NotificationChannel channel = new NotificationChannel(
                Notification_Channel_ID,
                Notification_Channel_ID,
                NotificationManager.IMPORTANCE_DEFAULT);

        Intent pauseNotificationIntent = new Intent(this, StreamingService.class);
        Intent resumeNotificationIntent = new Intent(this, StreamingService.class);
        Intent stopNotificationIntent = new Intent(this, StreamingService.class);

        pauseNotificationIntent.setAction(PAUSE_ACTION);
        resumeNotificationIntent.setAction(RESUME_ACTION);
        stopNotificationIntent.setAction(STOP_ACTION);

        PendingIntent pausePendingIntent = PendingIntent.getService(this, 1, pauseNotificationIntent, PendingIntent.FLAG_IMMUTABLE);
        PendingIntent resumePendingIntent = PendingIntent.getService(this, 2, resumeNotificationIntent, PendingIntent.FLAG_IMMUTABLE);
        PendingIntent stopPendingIntent = PendingIntent.getService(this, 0, stopNotificationIntent, PendingIntent.FLAG_IMMUTABLE);

        getSystemService(NotificationManager.class).createNotificationChannel(channel);

        Notification.Builder notificationBuilder = new Notification.Builder(this, Notification_Channel_ID);
        notificationBuilder.setStyle(new Notification.MediaStyle())
                .setSmallIcon(R.drawable.ic_launcher_radio)
                .addAction(R.drawable.ic_launcher_play_foreground, "Play", resumePendingIntent)
                .addAction(R.drawable.ic_launcher_pause_foreground, "Pause", pausePendingIntent)
                .addAction(R.drawable.ic_launcher_stop_foreground, "Close", stopPendingIntent)
                .setContentTitle("Radio")
                .setAutoCancel(false);

        startForeground(101, notificationBuilder.build());
        isServiceStarted = true;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        switch (intent.getAction()) {
            case PLAY_ACTION: {
                start();
            }
            break;
            case PAUSE_ACTION: {
                pause();
            }
            break;
            case RESUME_ACTION: {
                resume();
            }
            break;
            case STOP_ACTION: {
                stopForeground(true);
                stopSelf(startId);
                stop();
            }
            break;

        }

        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onPrepared(MediaPlayer player) {
        // mediaPlayer.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stop();
    }
}