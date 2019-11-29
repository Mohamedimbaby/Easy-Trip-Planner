package com.example.easytripplanner.Model;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;
import com.example.easytripplanner.DTO.note;
import com.example.easytripplanner.DTO.trip;
import com.example.easytripplanner.RoomEngine.DatabaseEngine;
import com.example.easytripplanner.RoomEngine.noteDaO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class noteModel {
MutableLiveData <List<note>> NoteLiveData ;
MutableLiveData <Boolean> BooleanLiveData ;
noteDaO noteDaO;
    public noteModel(Context context) {
        NoteLiveData = new MutableLiveData<>();
        BooleanLiveData = new MutableLiveData<>();
        noteDaO= DatabaseEngine.getInstance(context).getNoteDaO();

    }
    Boolean Saved =new Boolean(false);

    public  int deleteNote(int trip_id) {
        int b = noteDaO.deleteNotes(trip_id);
return b;
    }
    public  List<note> getNotesOftTrip(int objectId,String userID) {
        List<note> notesOfSomeTrip = noteDaO.getNotesOfSomeTrip(objectId, userID);
        if (notesOfSomeTrip.size()>0)
           return notesOfSomeTrip;
        else return null;
    }

    public Boolean Notes_added(List<note> notes)
   {
        noteDaO.insertAll(notes);
  return true;
   }

    public List<note> updateNotesOfTrip(List<note> notes) {

        for (note note : notes) {
            int updated= noteDaO.update(note);

        }
        return notes;
    }

    public List<note> getAllNotes() {
        List<note> allNotes = noteDaO.getAllNotes();
        return allNotes;
    }
}
