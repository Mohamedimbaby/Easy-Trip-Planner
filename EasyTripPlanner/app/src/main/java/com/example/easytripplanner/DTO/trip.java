package com.example.easytripplanner.DTO;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.persistence.DataQueryBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;




@Entity(tableName = "trips")
public class trip
{
    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int _id;
    @NonNull
    private Double end_lat;
    @NonNull
    private Double end_lng;
    @NonNull
    private String state;
    @NonNull
    int alarmId;
    @NonNull
    String start;
    @NonNull
    String end;
    @NonNull
    String repeat;

    @NonNull
    public String getRepeat() {
        return repeat;
    }

    public void setRepeat(@NonNull String repeat) {
        this.repeat = repeat;
    }



    @NonNull
    private String user_id;
    @NonNull
    private Double start_lat;
    @NonNull
    private Double start_lng;
    @NonNull
    private String date_time;
    @NonNull
    private String type;
    @NonNull
    private String name;
    @Ignore
List<note> notes;
    @Ignore
String objectId;


    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public List<note> getNotes() {
        return notes;
    }

    public void setNotes(List<note> notes) {
        this.notes = notes;
    }

    @NonNull
    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(@NonNull String user_id) {
        this.user_id = user_id;
    }

    @NonNull
    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(@NonNull String date_time) {
        this.date_time = date_time;
    }

    public int getId()
    {
        return _id;
    }

    public void setId( int id )
    {
        this._id = id;
    }



    @NonNull
    public Double getEnd_lat() {
        return end_lat;
    }

    public void setEnd_lat(@NonNull Double end_lat) {
        this.end_lat = end_lat;
    }

    @NonNull
    public Double getEnd_lng() {
        return end_lng;
    }

    public void setEnd_lng(@NonNull Double end_lng) {
        this.end_lng = end_lng;
    }

    @NonNull
    public Double getStart_lat() {
        return start_lat;
    }

    public void setStart_lat(@NonNull Double start_lat) {
        this.start_lat = start_lat;
    }

    @NonNull
    public Double getStart_lng() {
        return start_lng;
    }

    public void setStart_lng(@NonNull Double start_lng) {
        this.start_lng = start_lng;
    }

    public String getState()
    {
        return state;
    }

    public void setState( String state )
    {
        this.state = state;
    }

    public String getOwnerId()
    {
        return user_id;
    }


    public String getType()
    {
        return type;
    }

    public void setType( String type )
    {
        this.type = type;
    }


    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }



    public int getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(int alarmId) {
        this.alarmId=alarmId;
    }
}