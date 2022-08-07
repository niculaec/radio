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


import java.io.IOException;

public class StreamingService extends Service implements MediaPlayer.OnPreparedListener {
    private static final String stream = "https://edge126.rcs-rds.ro/profm/profm.mp3";
    MediaPlayer mediaPlayer = new MediaPlayer();
    private final IBinder binder = new LocalBinder();

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

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
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

        final String Notification_Channel_ID = "Foreground Service ID";
        NotificationChannel channel = new NotificationChannel(
                Notification_Channel_ID,
                Notification_Channel_ID,
                NotificationManager.IMPORTANCE_DEFAULT);

        getSystemService(NotificationManager.class).createNotificationChannel(channel);
        Notification.Builder notificationBuilder = new Notification.Builder(this, Notification_Channel_ID)
                .setSmallIcon(R.drawable.ic_launcher_radio);

        startForeground(101, notificationBuilder.build());
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onPrepared(MediaPlayer player) {
        mediaPlayer.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayer.release();
        mediaPlayer = null;
    }
}