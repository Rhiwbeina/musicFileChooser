package com.example.davidladd.musicfilechooser;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;


import com.example.davidladd.musicfilechooser.Song;

@Database(entities = {Song.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract SongDao songDao();


}
