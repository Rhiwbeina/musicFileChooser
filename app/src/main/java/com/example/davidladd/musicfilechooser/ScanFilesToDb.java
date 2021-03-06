package com.example.davidladd.musicfilechooser;

import android.arch.persistence.room.Room;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;

import static android.media.MediaMetadataRetriever.METADATA_KEY_ALBUM;
import static android.media.MediaMetadataRetriever.METADATA_KEY_ARTIST;
import static android.media.MediaMetadataRetriever.METADATA_KEY_TITLE;
import static android.media.MediaMetadataRetriever.METADATA_KEY_YEAR;

public class ScanFilesToDb extends AppCompatActivity {
    public Handler mainHandler = new Handler();
    Button buttScan, buttWipeDb, buttDbStats, buttAbort, buttId3;
    TextView textViewInfo;
    AppDatabase db;
    private SharedPreferences mPreferences;
    private runableScanDir SDrunable;
    private RunnableId3ToDb Id3runable;
    private static String TAG = "Dave";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_files_to_db);

        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name").build();

        mPreferences = getSharedPreferences("daves", MODE_PRIVATE);
        //path = mPreferences.getString("pathToLibrary", "/");
        Log.d(TAG, mPreferences.getString("pathToLibrary", "/"));

        buttScan = findViewById(R.id.buttScan);
        buttScan.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after Song presses button
                //runableAdd runable = new runableAdd();
                //runableScanDir
                SDrunable = new runableScanDir(mPreferences.getString("pathToLibrary", "/"));
                new Thread(SDrunable).start();
            }
        });

        buttWipeDb = findViewById(R.id.buttWipeDb);
        buttWipeDb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RunnableWipeDb runable = new RunnableWipeDb();
                new Thread(runable).start();
            }
        });


        buttDbStats = findViewById(R.id.buttDbStats);
        buttDbStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RunnableDbStats runable = new RunnableDbStats();
                new Thread(runable).start();
            }
        });

        buttAbort = findViewById(R.id.buttAbort);
        buttAbort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SDrunable != null){
                    SDrunable.setAbort(true);
                    Log.d(TAG, "set abort to true ");
                }

            }
        });

        buttId3 = findViewById(R.id.buttId3);
        buttId3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Id3runable = new RunnableId3ToDb();
                new Thread(Id3runable).start();

            }
        });

        textViewInfo = findViewById(R.id.textViewInfo);

    }

    class RunnableId3ToDb implements Runnable{
        @Override
        public void run() {
            Cursor cursor = db.songDao().getAllToCursor();
            cursor.moveToFirst();
            while (cursor.isAfterLast() == false)
            {
                final String songPath = cursor.getString(cursor.getColumnIndex("song_path"));
                final int sUid = cursor.getInt(cursor.getColumnIndex("uid"));

                MediaMetadataRetriever myId3Reader = new MediaMetadataRetriever();
                myId3Reader.setDataSource(songPath);
                final String sArtist = myId3Reader.extractMetadata(METADATA_KEY_ARTIST);
                final String sTitle = myId3Reader.extractMetadata(METADATA_KEY_TITLE);
                final String sAlbum = myId3Reader.extractMetadata(METADATA_KEY_ALBUM);
                final String sYear = myId3Reader.extractMetadata(METADATA_KEY_YEAR);

                final Song uSong = new Song();
                uSong.setUid(sUid);
                uSong.setSongPath(songPath);
                uSong.setTitle(sTitle);
                uSong.setAttemptId3(false);

                db.songDao().updateId3(uSong);
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        textViewInfo.setText("addded id3  = " + uSong.toString());

                    }
                });
                cursor.moveToNext();
            }
        }
    }

    class RunnableWipeDb implements Runnable{
        @Override
        public void run() {
            try{
                db.songDao().deleteAll();
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        textViewInfo.setText("Wiped maybe ");

                    }
                });
            }
            catch (SQLiteException exept){
                Log.d(TAG,"SQLite caught an error " + exept );
            }
        }
    }

    class RunnableDbStats implements Runnable{
        @Override
        public void run() {
            try{
                final Integer count = db.songDao().rowCount();

                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        textViewInfo.setText("Row count = " + String.valueOf(count));
                    }
                });
            }
            catch (SQLiteException exept){
                Log.d(TAG,"Dave caught an error " + exept );
            }
        }
    }


    class runableScanDir implements Runnable{
        int fileCounter = 0;
        int songCounter = 0;
        String pathToScan;
        volatile boolean Abort;

        public runableScanDir(String pathToScan) {
            this.pathToScan = pathToScan;
            Abort = false;
        }

        public void setAbort(boolean abort){
            Abort = abort;
        }

        @Override
        public void run() {
            Log.d(TAG, "startThread Scan Dir");

            //Uri uri = Uri.parse("/etc");
            Uri uri = Uri.parse(pathToScan);
            Log.d(TAG, "my uri = " + uri.getPath());

            File file = new File(uri.getPath());
            Filewalker fw = new Filewalker();
            fw.walk(file);
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    textViewInfo.setText(String.valueOf(fileCounter));
                    //varmytextbox.append("\n" + f.getName());
                }
            });
            Log.d(TAG, "Finish Scan Dir");
            //addSong(db, Song);
        }


        public class Filewalker  {
            //volatile int fileCounter = 0;
            volatile Long myTimer = System.currentTimeMillis();
            volatile Long myTimerPrevious = Long.valueOf(0);

            public void walk(File root) {
                try {
                    File[] list = root.listFiles();
                    for (final File f : list) {
                        if (Abort == true){
                            Log.d(TAG, "walk: trying to stop");
                            //return;
                            break;

                        }
                        myTimer = System.currentTimeMillis();
                        if((myTimer - myTimerPrevious) >= Long.valueOf(500)){
                            myTimerPrevious = myTimer;
                            mainHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    textViewInfo.setText(String.valueOf(fileCounter));
                                    //varmytextbox.append("\n" + f.getName());
                                }
                            });
                        }
                        if (f.isDirectory()) {
                            Log.d("song", "Dir: " + f.getAbsoluteFile());
                            walk(f);
                        }
                        else {
                            //Log.d("Dave", "File: " + f.getAbsoluteFile());
                            //Log.d("Dave", "counter " + String.valueOf(fileCounter));
                            if (f.getName().toLowerCase().endsWith(".mp3")){
                                Song Song = new Song();
                                Song.setSongPath(f.getAbsolutePath());
                                Song.setTitle(f.getName());
                                Song.setAttemptId3(true);
                                //db.SongDao().insertAll(Song);
                                songCounter++;
                                addaSong(db, Song);
                            }
                            fileCounter++;
                        }
                    }
                }
                catch (Exception s){
                    Log.d(TAG, "walk: fault " + s);
                }


            }

        }

        private Song addaSong(AppDatabase db, Song song) {
            try{
                db.songDao().insertOnlyNew(song);
            }
            catch (SQLiteException exept){
                Log.d(TAG,"Dave caught an error " + exept );
            }
            return song;
        }

    }

}
