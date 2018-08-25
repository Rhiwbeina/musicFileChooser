package com.example.davidladd.musicfilechooser;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.database.Cursor;

import java.util.List;

@Dao
public interface SongDao {
    @Query("SELECT * FROM Song")
    List<Song> getAll();

    @Query("SELECT * FROM Song WHERE uid IN (:userIds)")
    List<Song> loadAllByIds(int[] userIds);

    @Query("SELECT COUNT(*) FROM Song WHERE 1")
    Integer rowCount();

    @Query("SELECT * FROM Song")
    Cursor getAllToCursor();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void updateId3(Song song);

    @Insert
    void insertAll(Song... songs);

    @Delete
    void delete(Song song);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertOnlyNew(Song song);

    @Query("DELETE FROM Song")
    void deleteAll();

}
