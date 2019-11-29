package com.example.easytripplanner.ViewModel;

import android.content.Context;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.example.easytripplanner.DTO.note;
import com.example.easytripplanner.Model.tripModel;
import com.example.easytripplanner.DTO.trip;
import com.example.easytripplanner.View.tripDetails;

import java.util.List;

public class TripVM extends ViewModel {

private static TripVM tripViewModel;

    public  void SyncData(List<trip> trips, List<note> notes) {
        tripModel.SyncData(trips,notes);
    }

    public com.example.easytripplanner.Model.tripModel getTripModel() {
        return tripModel;
    }

    public void setTripModel(com.example.easytripplanner.Model.tripModel tripModel) {
        this.tripModel = tripModel;
    }

    public tripModel tripModel = com.example.easytripplanner.Model.tripModel.getInstance(con);
static Context con;


    private TripVM() {
        }
 public static TripVM getInstance (Context context)
{
    con=context;
    if(tripViewModel==null)
         tripViewModel = new TripVM();
    return tripViewModel;
}

public void getAllTripsUpComing(String user_id,Boolean upcoming)
     {
      tripModel.getAllTrips(user_id,upcoming);
     }
public void getAllTrips()
     {
      tripModel.getAllTrips();
     }
public void getAllNotes()
     {
      tripModel.getAllNotes();
     }
    public  void   Add_Trip (trip trip,List<note>notes)
    {
        tripModel.Add_Trip(trip,notes);

    }

    public void getSpecificTrip(int trip) {
        tripModel.getSpecificTrip(trip);
    }

    public void delete(trip trip) {
        tripModel.delete(trip);
    }

    public void update(trip tripObject, LifecycleOwner tripDetails) {
        tripModel.update(tripObject,tripDetails);
    }
    public void cancelTrip(int tripObject,String operation) {
        tripModel.cancelTrip(tripObject,operation);
    }
}
