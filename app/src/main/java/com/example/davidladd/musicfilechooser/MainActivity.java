package com.example.davidladd.musicfilechooser;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    AudioPlayer songAudioPlayer;
    Button buttPause;
    Button buttStop;
    TextView textViewSongPlaying;
    SeekBar seekBarSongPlaying;
    //static dAudioPlayer mydAudioPlayer;
    public Handler sHandler;
    private  static MainActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
// Check whether this app has write external storage permission or not.
        int readExternalStoragePermission = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
// If do not grant write external storage permission.
        if(readExternalStoragePermission!= PackageManager.PERMISSION_GRANTED)
        {
            // Request user to grant write external storage permission.
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 5);
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        buttStop = findViewById(R.id.buttStop);
        buttPause = findViewById(R.id.buttPause);
        seekBarSongPlaying = findViewById(R.id.seekBarSongPlaying);
        buttPause.setEnabled(false);
        textViewSongPlaying= findViewById(R.id.textViewSongPlaying);
        songAudioPlayer = new AudioPlayer(getApplicationContext(), seekBarSongPlaying, textViewSongPlaying, buttPause, buttStop);

        sHandler = new Handler();

        instance = this;
    }

    public static MainActivity getInstance() {
        return instance;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent ScanFilesToDbIntent = new Intent(getApplicationContext(), ScanFilesToDb.class);
            startActivity(ScanFilesToDbIntent);
            return true;
        }

        if (id == R.id.action_chooser) {
            Intent dirMTbrowseIntent = new Intent(getApplicationContext(), dirMTbrowse.class);
            startActivity(dirMTbrowseIntent);
            //dirMTbrowseIntent.
            return true;
        }

        return super.onOptionsItemSelected(item);
    }





}
