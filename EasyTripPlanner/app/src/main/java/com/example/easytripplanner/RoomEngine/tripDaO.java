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
public interface tripDaO {
@Insert
    long insert (trip t1);
    @Query("select * from trips where  user_id=:user_id and state='upcoming'")
    List<trip> getAllUpcomingTrips(String user_id);

    @Query("SELECT * FROM trips ORDER BY _id DESC LIMIT 1")
    trip getLasttrip();

    @Query("select * from trips where _id=:id")
    trip getSpecificTrip(int id);
@Update
    int update(trip trip);

    @Delete
    int delete (trip trip);
    @Query("select * from trips where  user_id=:user_id and (state='Past' or state='Cancelled')")
    List<trip> getAllPastTrips(String user_id);

    @Query("update trips SET state=:operation where _id=:id")
    int CancelTrip(int id, String operation);

    @Query("select * from trips")
    List<trip> getAllTrips();

}
