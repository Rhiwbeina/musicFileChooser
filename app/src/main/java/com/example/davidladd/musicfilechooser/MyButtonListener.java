package com.example.davidladd.musicfilechooser;

import android.content.Context;
import android.media.MediaPlayer;

import android.util.Log;
import android.view.View;
import android.widget.Toast;


//public class MainActivity extends AppCompatActivity {
public class MyButtonListener implements View.OnClickListener {
    private Context Activity;
    AudioPlayer sap;
    //private static MediaPlayer amediaplayer;
    //Context myContext;
    //TextView textSongTitle;
    //static Button buttPause, buttStop;

    public  MyButtonListener(){
        super();
    }


    public  MyButtonListener(Context ctx, AudioPlayer mp){
        Activity = ctx;
        sap = mp;
    }

    @Override
    public void onClick(View clipcodes) {
        //textSongTitle = MainActivity.getInstance().findViewById(R.id.teve3);
        Log.d("Dave", "onclick by" + clipcodes.getId());
        Toast.makeText(Activity, "Some button was predded",
                Toast.LENGTH_LONG).show();
        if (clipcodes.getId() == R.id.buttPause){
            sap.pause();

        }

    }

}