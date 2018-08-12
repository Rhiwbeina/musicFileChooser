package com.example.davidladd.musicfilechooser;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;

public class AudioPlayer {
    public MediaPlayer amediaplayer;
    private Context myContext;
    private Button buttStop, buttPause;
    static String titleToShow;
    private TextView textViewSongPlaying;
    private SeekBar seekBarSongPlaying;

    public AudioPlayer(Context ctx, SeekBar seekSong,  TextView textv, Button buttp, Button butts){
        myContext = ctx;
        textViewSongPlaying = textv;
        buttPause = buttp;
        //buttPause = MainActivity.getInstance().findViewById(R.id.buttPause);

        buttStop = butts;
        titleToShow = "idle";
        seekBarSongPlaying = seekSong;
        buttPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pause();
            }
        });

    }
    
    public void loadPlay(String songToPlay){
        destroy();
        amediaplayer=MediaPlayer.create(MainActivity.getInstance().getApplicationContext(), Uri.fromFile( new File(songToPlay)));
        //mp=MediaPlayer.create(getApplicationContext(), Uri.fromFile( new File("/storage/extSdCard/mp3/tunes18/Barbera blow - throughout your precious love.mp3")));
        try{
            amediaplayer.start();
            Integer songDuration = amediaplayer.getDuration() / 1000;
            MainActivity.getInstance().seekBarSongPlaying.setMax(songDuration);
        }
        catch (Exception eee){
            Log.d("Dave", "some exepton from MediaPlayer: " + eee);
            return;
        }
        titleToShow = songToPlay;
        textViewSongPlaying.setText(titleToShow);
        buttPause.setEnabled(true);
        buttPause.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_pause_black_24dp, 0, 0, 0);
        amediaplayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                textViewSongPlaying.setText("now finished");
                destroy();
            }
        });

        MainActivity.getInstance().sHandler.post(
                new Runnable() {

                    @Override
                    public void run() {
                        if(amediaplayer != null){
                            int mCurrentPosition = amediaplayer.getCurrentPosition() / 1000;
                            seekBarSongPlaying.setProgress(mCurrentPosition);
                        }
                        MainActivity.getInstance().sHandler.postDelayed(this, 1000);
                    }
                });


    }

    public void pause(){
        if (amediaplayer.isPlaying()){
            amediaplayer.pause();
            buttPause.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_play_arrow_black_24dp, 0, 0, 0);
            textViewSongPlaying.setText("paused");
        }
        else {
            amediaplayer.start();
            buttPause.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_pause_black_24dp, 0, 0, 0);
            textViewSongPlaying.setText(titleToShow);

        }

    }

    public void destroy(){
        if(amediaplayer != null){
            if (amediaplayer.isPlaying()){
                amediaplayer.stop();
            }
            amediaplayer.release();
            amediaplayer = null;

        }
    }

    public int getPosition(){
        if (amediaplayer != null){
            return amediaplayer.getCurrentPosition();
        }
        else
        {
            return (Integer) 0;
        }

    }



}
