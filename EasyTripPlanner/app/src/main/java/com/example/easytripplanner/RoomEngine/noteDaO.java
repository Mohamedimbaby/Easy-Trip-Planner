package com.example.easytripplanner.RoomEngine;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.easytripplanner.DTO.note;
import com.example.easytripplanner.DTO.trip;

import java.util.List;

@Dao
public interface noteDaO {
    @Insert
    long insert(note note);
    @Query("select * from notes where trip_id=:trip_id and user_id=:user_id")
    List<note> getNotesOfSomeTrip(int trip_id,String user_id);

    @Insert
    void insertAll(List <note> notes);

    @Query("Delete from notes where trip_id=:trip_id")
    int deleteNotes(int trip_id );
    @Update
    int update(note note);
    @Query("select * from notes")
    List<note> getAllNotes();
}


