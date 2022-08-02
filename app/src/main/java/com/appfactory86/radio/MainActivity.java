package com.appfactory86.radio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {
    Button play;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this, StreamingService.class);
        startForegroundService(intent);

        play = findViewById(R.id.button_play);
        play.setEnabled(false);
        play.setText("Loading");

//        play.setOnClickListener(v -> {
//            if (streamingService.started) {
//                streamingService.started = false;
//                streamingService.mediaPlayer.pause();
//                play.setText("Play");
//            } else {
//                streamingService.started = true;
//                streamingService.mediaPlayer.start();
//                play.setText("Pause");
//            }
//        });
    }
}