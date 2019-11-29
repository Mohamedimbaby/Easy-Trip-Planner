package com.example.easytripplanner.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.easytripplanner.DTO.note;
import com.example.easytripplanner.DTO.trip;
import com.example.easytripplanner.Model.tripModel;
import com.example.easytripplanner.R;
import com.example.easytripplanner.ReceiverManager.AlarmReceiver;
import com.example.easytripplanner.ViewModel.TripVM;
import com.example.easytripplanner.adapters.CustomAdapter;
import com.example.easytripplanner.databinding.ActivityTripDetailsBinding;
import com.example.easytripplanner.ui.Fragments.DatePickerFragment;
import com.example.easytripplanner.ui.Fragments.TimePickerFragment;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class tripDetails extends AppCompatActivity {

    ActivityTripDetailsBinding binding;
    CustomAdapter adapter;
    trip tripObject;
    TripVM tripVM ;
    com.example.easytripplanner.Model.tripModel tripModel ;
   ProgressDialog progressDialog;
    String date_time;
    Boolean isDateChange=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_trip_details);
        tripVM= TripVM.getInstance(this);
        tripModel=tripVM.getTripModel();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading Trip ..");
        progressDialog.show();
        int trip= getIntent().getExtras().getInt("trip");
        String trip_type= getIntent().getExtras().getString("notification");
        if (trip_type!=null){
        if(trip_type.equals("true"))
        {
            AlertDialog.Builder   builder=   new AlertDialog.Builder(this).setMessage("do you want to Go back ?")
                    .setTitle("Return Journey ").setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int whih) {
                            dialog.cancel();
                            Intent in = new Intent(android.content.Intent.ACTION_VIEW,
                                    Uri.parse("http://maps.google.com/maps?saddr=" + tripObject.getEnd_lat() + "," + tripObject.getEnd_lng()+ "&daddr=" + tripObject.getStart_lat() + "," + tripObject.getStart_lng()));
                            startActivity(in);
                        }
                    }) .setNegativeButton(android.R.string.cancel,null);
            builder.getContext().setTheme(R.style.AppTheme_Dialog);
builder.create().show();
        }
        }
        tripVM.getSpecificTrip(trip);
        System.out.println(trip_type+"asxasasasasasas");
        tripModel.getGetSpecificTripLiveData().observe(this, new Observer<trip>() {
            @Override
            public void onChanged(trip trips) {


                if (trips!=null) {
                    tripObject = trips;
                    System.out.println("alarm id is : "+tripObject.getAlarmId());
                    date_time = tripObject.getDate_time();
                    if(tripObject.getNotes()!=null) {
                        System.out.println(tripObject.getNotes().size());
                        adapter = new CustomAdapter((ArrayList<note>) tripObject.getNotes(),tripDetails.this);
                        binding.notesCheckListDet.setAdapter(adapter);
                    }
                    setViewsWithData(tripObject);
                }
                progressDialog.cancel();
            tripModel.setGetSpecificTripLiveData(new MutableLiveData<>());
            }

        });
        tripModel.getTripDeletedLiveData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean tripsDeleted) {
                if (tripsDeleted)
                    Toast.makeText(tripDetails.this, "Deleted ..", Toast.LENGTH_SHORT).show();
            tripModel.setTripDeletedLiveData(new MutableLiveData<>());
            HomeActivity.editor.putString("updated","1");
            HomeActivity.editor.commit();

            }
        });


        binding.tapBarMenu.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                binding.tapBarMenu.toggle();
            }
        });
        binding.dateGetterImg.setOnClickListener((v)->{
        DialogFragment calenderFrag;
        calenderFrag = new DatePickerFragment();
        calenderFrag.show(getSupportFragmentManager(), "Date");
        TimePickerFragment.onTimeChange.observe(tripDetails.this, new Observer<String>() {
            @Override
            public void onChanged(String date_time) {


                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy-H:m");
                try {
                    Date date = simpleDateFormat.parse(date_time);
                    if(date.getTime()<System.currentTimeMillis()){
                        Toast.makeText(tripDetails.this, "Choose Valid Date ...", Toast.LENGTH_SHORT).show();
                        TimePickerFragment.onTimeChange=new MutableLiveData<>();
                    }
                    else {
                        binding.DateDetET.setText(date_time);

                        isDateChange=true;


                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                TimePickerFragment.onTimeChange=new MutableLiveData<>();

            }
        });
    });

    }



    private void ResetAllFields() {

        binding.toDetET.setText("");
        binding.froDetmET.setText("");
        binding.tripNameDet.setText("");
        binding.DateDetET.setText("");
        onBackPressed();
}
    private void setViewsWithData(trip tripObject) {
        binding.DateDetET.setText(tripObject.getDate_time());
        binding.tripNameDet.setText(tripObject.getName());

        binding.froDetmET.setText(tripObject.getStart());

        binding.toDetET.setText(tripObject.getEnd());


        binding.DateDetET.setText(tripObject.getDate_time());
    }

    public void OndeletePressed(View view) {

        binding.tapBarMenu.close();

        AlertDialog.Builder   builder=   new AlertDialog.Builder(this).setMessage("Are You Sure You want to delete this trip ? ")
                .setTitle("Alert ").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whih) {
                        dialog.cancel();

                        tripVM.delete(tripObject);
                        Intent intent=new Intent(tripDetails.this, AlarmReceiver.class);
                        AlarmManager manager= (AlarmManager) getSystemService(ALARM_SERVICE);
                        PendingIntent pendingIntent=PendingIntent.getBroadcast(tripDetails.this, tripObject.getAlarmId(),intent,PendingIntent.FLAG_CANCEL_CURRENT);
                        manager.cancel(pendingIntent);
                        ResetAllFields();

                    }
                }) .setNegativeButton("No" ,null);
        builder.getContext().setTheme(R.style.AppTheme_Dialog);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }


    boolean toValid=false,fromValid=false;

    public void onUpdatePressed(View view) {
        AlertDialog.Builder   builder=   new AlertDialog.Builder(this).setMessage("Are you sure you want to update this trip ? ")
                .setTitle("Alert ").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whih) {
                    dialog.cancel();

                        tripModel.getTripUpdatedLiveData().observe(tripDetails.this, new Observer<trip>() {
                            @Override
                            public void onChanged(trip tripsUpdated) {
                                if (tripsUpdated!=null) {
                                    Toast.makeText(tripDetails.this, "Updated ..", Toast.LENGTH_SHORT).show();
                                    setViewsWithData(tripObject);
                                    System.out.println("After Updated "+ tripObject.getStart());
                                    setAlarmManager();
                                    HomeActivity.editor.putString("updated","1");
                                    HomeActivity.editor.commit();


                                }
                                tripModel.setTripUpdatedLiveData(new MutableLiveData<>());
                            }
                        });
                        binding.tapBarMenu.close();
                        String fromPlace = binding.froDetmET.getText().toString();
                        String toPlace = binding.toDetET.getText().toString();
                        String date_time = binding.DateDetET.getText().toString();
                        if (fromPlace.equals("")||toPlace.equals("")||date_time.equals(""))
                        {
                            Toast.makeText(tripDetails.this, "Please fill All the fields ..", Toast.LENGTH_SHORT).show();
                        }
                        else if (fromPlace.trim().equals(toPlace.trim())){
                            Toast.makeText(tripDetails.this, "the both places are identical ", Toast.LENGTH_SHORT).show();

                        }
                        else {
                            Geocoder geocoder = new Geocoder(tripDetails.this);
                            try {
                                List<Address> toLocationName = geocoder.getFromLocationName(toPlace, 1);
                                if (toLocationName.size() > 0) {
                                    tripObject.setEnd_lat(toLocationName.get(0).getLatitude());
                                    tripObject.setEnd_lng(toLocationName.get(0).getLongitude());
                                    toValid = true;
                                } else {
                                    binding.toDetET.setError("the place is not clear try with another spelling ");
                                    toValid = false;
                                }
                            } catch (IOException e) {
                                toValid = false;
                                e.printStackTrace();
                            }
                            Geocoder geocoderfrom = new Geocoder(tripDetails.this);
                            geocoderfrom = new Geocoder(tripDetails.this);
                            try {
                                List<Address> fromLocationName = geocoderfrom.getFromLocationName(fromPlace, 1);
                                if (fromLocationName.size() > 0) {
                                    tripObject.setStart_lat(fromLocationName.get(0).getLatitude());
                                    tripObject.setStart_lng(fromLocationName.get(0).getLongitude());
                                    fromValid = true;
                                } else {
                                    binding.froDetmET.setError("the place is not clear try with another spelling ");
                                    fromValid = false;
                                }
                            } catch (IOException e) {
                                toValid = false;
                                e.printStackTrace();
                            }
                            System.out.println(toValid + "   " + fromValid);
                            if (toValid && fromValid) {
                                tripObject.setStart(fromPlace);
                                tripObject.setEnd(toPlace);
                                tripObject.setDate_time(date_time);

                                if (adapter != null) {
                                    tripObject.setNotes(adapter.getDataSet());
                                }
                     if (isDateChange) {
                         if (!tripObject.getState().equals("upcoming"))
                             tripObject.setState("upcoming");
                     }
                     tripVM.update(tripObject, tripDetails.this);
                            } else {
                                Toast.makeText(tripDetails.this, "the places are invalid ", Toast.LENGTH_SHORT).show();
                            }

                        }
                        }
                }) .setNegativeButton("No" ,null);
        builder.getContext().setTheme(R.style.AppTheme_Dialog);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();


    }



    private void setAlarmManager() {

        Intent intent=new Intent(tripDetails.this,AlarmReceiver.class);
        intent.putExtra("trip_ObjectId",tripObject.getId());
        intent.putExtra("trip_name",tripObject.getName());
        intent.putExtra("trip_start",tripObject.getStart());
        intent.putExtra("trip_end",tripObject.getEnd());
        intent.putExtra("trip_start_lat",tripObject.getStart_lat());
        intent.putExtra("trip_start_lng",tripObject.getStart_lng());
        intent.putExtra("date_time",tripObject.getDate_time());
        intent.putExtra("trip_end_lat",tripObject.getEnd_lat());
        intent.putExtra("trip_end_lng",tripObject.getEnd_lng());
        intent.putExtra("trip_alarmId",tripObject.getAlarmId());
        intent.putExtra("trip_type",tripObject.getType());
        intent.putExtra("trip_repeat", tripObject.getRepeat());

        PendingIntent pendingIntent=PendingIntent.getBroadcast(tripDetails.this, tripObject.getAlarmId(),intent,PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager manager= (AlarmManager) getSystemService(ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy-H:m");
        try {
            Date date = simpleDateFormat.parse(tripObject.getDate_time());
            System.out.println(date.toString());

            if(date.getTime()>System.currentTimeMillis()) {
                System.out.println("previous "+tripObject.getAlarmId());
                calendar.setTime(date);
            manager.set(AlarmManager.RTC, calendar.getTimeInMillis(),pendingIntent);
            }
} catch (ParseException e) {
            e.printStackTrace();
        }



    }
}
