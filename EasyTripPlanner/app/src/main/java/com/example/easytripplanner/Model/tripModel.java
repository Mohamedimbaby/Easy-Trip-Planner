package com.example.easytripplanner.Model;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;
import com.example.easytripplanner.DTO.note;
import com.example.easytripplanner.DTO.trip;
import com.example.easytripplanner.RoomEngine.DatabaseEngine;
import com.example.easytripplanner.RoomEngine.tripDaO;
import com.example.easytripplanner.View.MainActivity;
import com.example.easytripplanner.View.tripDetails;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class tripModel {
    private static tripModel tripModel ;
static  Context con;
    public static tripModel getInstance( Context context) {
     con=context;
       if(tripModel==null)
           tripModel=new tripModel();

       return tripModel;
    }

    public MutableLiveData<List<com.example.easytripplanner.DTO.trip>> getAllTripsLiveDateUpcoming() {
        return AllTripsLiveDateUpcoming;
    }
    public MutableLiveData<List<com.example.easytripplanner.DTO.trip>> getAllTripsLiveDatePast() {
        return AllTripsLiveDatePast;
    }

    MutableLiveData <List<trip> > AllTripsLiveDateUpcoming = new MutableLiveData<>();

    public MutableLiveData<List<trip>> getAllTripsLiveDateForSync() {
        return AllTripsLiveDateForSync;
    }

    public void setAllTripsLiveDateForSync(MutableLiveData<List<trip>> allTripsLiveDateForSync) {
        AllTripsLiveDateForSync = allTripsLiveDateForSync;
    }

    MutableLiveData <List<trip> > AllTripsLiveDateForSync = new MutableLiveData<>();

    public MutableLiveData<List<note>> getAllNotesLiveDateForSync() {
        return AllNotesLiveDateForSync;
    }

    public void setAllNotesLiveDateForSync(MutableLiveData<List<note>> allNotesLiveDateForSync) {
        AllNotesLiveDateForSync = allNotesLiveDateForSync;
    }

    MutableLiveData <List<note> > AllNotesLiveDateForSync = new MutableLiveData<>();

    public MutableLiveData<Boolean> getSynced() {
        return Synced;
    }

    public void setSynced(MutableLiveData<Boolean> synced) {
        Synced = synced;
    }

    MutableLiveData <Boolean > Synced = new MutableLiveData<>();
    MutableLiveData <List<trip> > AllTripsLiveDatePast = new MutableLiveData<>();
    MutableLiveData <Boolean > tripDeletedLiveData = new MutableLiveData<>();
    MutableLiveData <Boolean > tripCanceled = new MutableLiveData<>();
    MutableLiveData <trip> tripUpdatedLiveData = new MutableLiveData<>();
    MutableLiveData <trip > getSpecificTripLiveData = new MutableLiveData<>();
    MutableLiveData <trip> TripAdded = new MutableLiveData<>();

    public MutableLiveData<Boolean> getTripCanceled() {
        return tripCanceled;
    }

    public MutableLiveData<trip> getTripUpdatedLiveData() {
        return tripUpdatedLiveData;
    }
    public  MutableLiveData<Boolean> getTripDeletedLiveData() {
        return tripDeletedLiveData;
    }
     public  MutableLiveData<trip> getGetSpecificTripLiveData() {
        return getSpecificTripLiveData;
    }
    public  MutableLiveData<trip> getTripAdded() {
        return TripAdded;
    }
    public  void setTripAdded(MutableLiveData<trip> tripAdded) {
        TripAdded = tripAdded;
    }

    public void setAllTripsLiveDateUpcoming(MutableLiveData<List<trip>> allTripsLiveDateUpcoming) {
        AllTripsLiveDateUpcoming = allTripsLiveDateUpcoming;
    }

    public void setAllTripsLiveDatePast(MutableLiveData<List<trip>> allTripsLiveDatePast) {
        AllTripsLiveDatePast = allTripsLiveDatePast;
    }

    public void setTripDeletedLiveData(MutableLiveData<Boolean> tripDeletedLiveData) {
        this.tripDeletedLiveData = tripDeletedLiveData;
    }

    public void setTripUpdatedLiveData(MutableLiveData<trip> tripUpdatedLiveData) {
        this.tripUpdatedLiveData = tripUpdatedLiveData;
    }

    public void setGetSpecificTripLiveData(MutableLiveData<trip> getSpecificTripLiveData) {
        this.getSpecificTripLiveData = getSpecificTripLiveData;
    }
tripDaO tripDaO ;
    private tripModel() {
    tripDaO= DatabaseEngine.getInstance(con).getTripDaO();
    }
 public void getAllTrips(String user_id,Boolean upcoming) {
        if(upcoming) {
            List<trip> allUpcomingTrips = tripDaO.getAllUpcomingTrips(user_id);
            AllTripsLiveDateUpcoming.setValue(allUpcomingTrips);
        } else {
            List<trip> allPastTrips = tripDaO.getAllPastTrips(user_id);
            AllTripsLiveDatePast.setValue(allPastTrips);
        }
    }
    public  void   Add_Trip (trip trip,List<note> notes) {


        long i = tripDaO.insert(trip);
        if(i>0) {
            trip lasttrip = tripDaO.getLasttrip();

            if (notes.size() > 0) {
                for (note note : notes) {
                    note.setTrip_id(lasttrip.getId());
                    note.setUser_id(lasttrip.getUser_id());
                }
                noteModel model = new noteModel(con);
                 model.Notes_added(notes);
             TripAdded.setValue(lasttrip);
            }
            TripAdded.setValue(lasttrip);
        }
        else TripAdded.setValue(null);
    }
    noteModel    model = new noteModel(con);

    public void getSpecificTrip(int trip_id) {
        trip specificTrip = tripDaO.getSpecificTrip(trip_id);
        List<note> notesOftTrip = model.getNotesOftTrip(specificTrip.getId(), specificTrip.getUser_id());
        if (tripDaO!=null) {
            if (notesOftTrip != null) {
                specificTrip.setNotes(notesOftTrip);

            }
            getSpecificTripLiveData.setValue(specificTrip);

        }
else   getSpecificTripLiveData.setValue(null);

    }

    public void delete(trip trip1) {
        int deleted = tripDaO.delete(trip1);
        if(deleted>0) {
            int b = model.deleteNote(trip1.getId());
            if (b>0)tripDeletedLiveData.setValue(true);
            else tripDeletedLiveData.setValue(false);
        }else tripDeletedLiveData.setValue(false);


            }

    public void update(trip tripObject, LifecycleOwner tripDetails) {


        int update = tripDaO.update(tripObject);
        if(update>0){
        if (tripObject.getNotes()!=null) {
            List<note> notes = model.updateNotesOfTrip(tripObject.getNotes());
            if (notes.size()>0) {
                tripObject.setNotes(notes);
                tripUpdatedLiveData.setValue(tripObject);
            }
            else tripUpdatedLiveData.setValue(tripObject);
        }
        else tripUpdatedLiveData.setValue(tripObject);
        }
        else tripUpdatedLiveData.setValue(null);
    }

    public void cancelTrip(int id,String operation) {
        int cancelTrip = tripDaO.CancelTrip(id, operation);
        if (cancelTrip>0) tripCanceled.setValue(true);
        else tripCanceled.setValue(false);

    }
    public void getAllTrips() {
        List<trip> allTrips = tripDaO.getAllTrips();

        if(allTrips!=null)
        {AllTripsLiveDateForSync.setValue(allTrips);
        }
        else
            AllTripsLiveDateForSync.setValue(null);

    }
    public void getAllNotes() {
        List<note> allNotes = model.noteDaO.getAllNotes();

        if(allNotes!=null)
        {AllNotesLiveDateForSync.setValue(allNotes);
        }
        else
            AllNotesLiveDateForSync.setValue(null);

    }


    public void SyncData(List<trip> trips, List<note> notes) {
        Backendless.Data.of("trip").remove("id > 0", new AsyncCallback<Integer>() {
            @Override
            public void handleResponse(Integer response) {
                if (notes != null) {                   System.out.println(response);


                    System.out.println("remove All trips" + notes.toString());

                    Backendless.Data.of("note").remove("checked =0 or checked =1", new AsyncCallback<Integer>() {
                        @Override
                        public void handleResponse(Integer response) {
                            System.out.println(response);
                            System.out.println("remove All notes " + notes.toString());
                            for (trip trip : trips) {
                                Backendless.Persistence.of(trip.class).save(trip, new AsyncCallback<trip>() {
                                    @Override
                                    public void handleResponse(trip response) {

                                    }

                                    @Override
                                    public void handleFault(BackendlessFault fault) {
                                        Synced.setValue(false);
                                        System.out.println("error save trips"+fault.getMessage());
                                        return;
                                    }
                                });

                            }
                            for (note note : notes) {

                                System.out.println("save All notes" + notes.toString());
                                Backendless.Persistence.of(note.class).save(note,
                                        new AsyncCallback<note>() {
                                    @Override
                                    public void handleResponse(note response) {


                                    }

                                    @Override
                                    public void handleFault(BackendlessFault fault) {
                                        Synced.setValue(false);

                                        return;
                                    }
                                });

                            }

                            Synced.setValue(true);


                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {
                            System.out.println("error remove notes"+fault.getMessage());

                        }
                    });
                } else {

                    for (trip trip : trips) {
                        Backendless.Persistence.of(trip.class).save(trip, new AsyncCallback<trip>() {
                            @Override
                            public void handleResponse(trip response) {

                            }

                            @Override
                            public void handleFault(BackendlessFault fault) {
                                Synced.setValue(false);
                                System.out.println("error save trips"+fault.getMessage());
                                return;
                            }
                        });

                    }
                }


            }
            @Override
            public void handleFault(BackendlessFault fault) {
                System.out.println("error remove trips"+fault.getMessage());
Synced.setValue(false);
            }
        });

    }
}
