package com.example.davidladd.musicfilechooser;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

@Entity(indices = {@Index(value = {"song_path"},
        unique = true)})

public class Song {
    @PrimaryKey(autoGenerate = true)
    private int uid;
    public int getUid() {
        return uid;
    }
    public void setUid(int uid) {
        this.uid = uid;
    }

    @ColumnInfo(name = "song_path")
    private String songPath;
    public String getSongPath() {
        return songPath;
    }
    public void setSongPath(String songPath) {
        this.songPath = songPath;
    }

    @ColumnInfo(name = "attempt_Id3")
    private  boolean attemptId3;
    public boolean getAttemptId3() {return attemptId3;}
    public void setAttemptId3(boolean attemptId3){this.attemptId3 = attemptId3;}

    @ColumnInfo(name = "title")
    private String title;
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    // Getters and setters are ignored for brevity,
    // but they're required for Room to work.

}
