package com.example.easytripplanner.RoomEngine;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import com.example.easytripplanner.DTO.note;
import com.example.easytripplanner.DTO.trip;

@Database(entities = {note.class, trip.class},version = 1)
public abstract class  DatabaseEngine extends RoomDatabase {

   private static DatabaseEngine instance ;
    private static String DATABASE_NAME="etpDB.db";
    public abstract tripDaO getTripDaO();
    public abstract noteDaO getNoteDaO();

    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {

        }
    };
    public static DatabaseEngine getInstance(Context context)
    {
        if(instance==null)
        {
            instance = Room.databaseBuilder(context, DatabaseEngine.class, DATABASE_NAME).
                    createFromAsset(DATABASE_NAME).addMigrations(MIGRATION_1_2).allowMainThreadQueries().build();
        }
        return instance;
    }

    }

