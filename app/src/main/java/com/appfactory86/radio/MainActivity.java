package com.appfactory86.radio;

import static com.appfactory86.radio.Constants.PLAY_ACTION;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {
    Button play;
    boolean mBound = false;
    StreamingService mService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this, StreamingService.class);
        intent.setAction(PLAY_ACTION);
        startForegroundService(intent);

        play = findViewById(R.id.button_play);
        play.setEnabled(true);
        play.setText("Loading");

        play.setOnClickListener(v -> {
            if (mService.isPlaying()) {
                mService.pause();
                play.setText("Play");
            } else {
                mService.resume();
                play.setText("Pause");
            }
        });
    }

    private void bindService() {
        Intent serviceIntent = new Intent(this, StreamingService.class);
        bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE);
    }


    @Override
    protected void onStart() {
        super.onStart();
        bindService();
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(serviceConnection);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            StreamingService.LocalBinder binder = (StreamingService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBound = false;
        }
    };
}