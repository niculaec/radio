package com.appfactory86.radio;

import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    Button play;
    String stream = "https://live5uk.antenaplay.ro/zurtv/zurtv/chunklist.m3u8?version=1&session=2yWAQ6LwhooeaRkvPiTz&starttime=1658239079&endtime=1658253509&source=web&token=_wIuPttkafXXjN2LB0atTrtCnaE=";
    MediaPlayer mediaPlayer;
    boolean prepared = false;
    boolean started = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        play = findViewById(R.id.button_play);
        play.setEnabled(false);
        play.setText("Loading");

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        new PlayTask().execute(stream);

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (started){
                    started = false;
                    mediaPlayer.pause();
                    play.setText("Play");
                }else {
                    started = true;
                    mediaPlayer.start();
                    play.setText("Pause");
                }
            }
        });
        
    }

    private class PlayTask extends AsyncTask<String,Void,Boolean>{

        @Override
        protected Boolean doInBackground(String... strings) {
            try {
                mediaPlayer.setDataSource(strings[0]);
                mediaPlayer.prepare();
                prepared = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return prepared;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            play.setEnabled(true);
            play.setText("Play");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(started){
            mediaPlayer.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(started) {
            mediaPlayer.start();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(prepared) {
            mediaPlayer.release();
        }
    }
}