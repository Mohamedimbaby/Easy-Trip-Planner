package com.example.easytripplanner.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import android.Manifest;
import android.app.AlarmManager;
import android.app.FragmentManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.example.easytripplanner.DTO.note;
import com.example.easytripplanner.DTO.trip;
import com.example.easytripplanner.Model.tripModel;
import com.example.easytripplanner.R;
import com.example.easytripplanner.ReceiverManager.AlarmReceiver;
import com.example.easytripplanner.ViewModel.TripVM;
import com.example.easytripplanner.adapters.PlacesAdapters;
import com.example.easytripplanner.databinding.ActivityAddTripBinding;

import com.example.easytripplanner.ui.Fragments.DatePickerFragment;
import com.example.easytripplanner.ui.Fragments.DialogAlarm;
import com.example.easytripplanner.ui.Fragments.TimePickerFragment;
import com.example.easytripplanner.ui.Fragments.TripsFragment;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.internal.es;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AddTripActivity extends AppCompatActivity {
    ActivityAddTripBinding binding;
    PlacesClient client;
    AutocompleteSupportFragment autocompleteFragment;
    List<Place.Field> placesInfo = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS);
    List<String> Notes;
    com.example.easytripplanner.DTO.trip trip;
    TripVM tripVM;
    com.example.easytripplanner.Model.tripModel tripModel;

    Boolean fromValid = true;
    Boolean toValid = true;
    ProgressDialog progressDialog;

    ArrayAdapter adapter;
    DialogAlarm alram;
    List<String> repetations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_trip);
        List<String> Places = new ArrayList<>();
        repetations = new ArrayList<>();
        Collections.addAll(Places, "cairo", "nasr city", "imbaba", "helwan", "shobra el khema", "kornish el nil", "el maadi", "el salam","El-Mahalla El-Kubra"
                ,"Suez", "Luxor",
                "Port Said","Shubra El-kheima","Luxor","El Mansoura","Tanta","Aswan","Banha","El sharkya","Domyat","El Marg"
                ,"Ain Shams","Hadayk El Kobba", "El matarya","El zamalek","El mohandesen","Ramsis",
                "Abbasya","Ghamra","El Monofya","EL Nozha","Masr el gdeda","West El bald","El sayda Aisha","Attaba","El zawya el Hamra","El bahera");
        Collections.addAll(repetations, "One time", "Daily", "Weekly", "Monthly");
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, Places);
        binding.fromATV.setAdapter(adapter);
        binding.toATV.setAdapter(adapter);
        binding.spinner.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, repetations));
        binding.spinner.setBackgroundColor(Color.parseColor("#ffffff"));
        Notes = new ArrayList<>();
        tripVM = TripVM.getInstance(this);
        tripModel = tripVM.getTripModel();
        trip = new trip();
        adapter = new ArrayAdapter(AddTripActivity.this, R.layout.notes_custom_layout, Notes);
        binding.notesList.setAdapter(adapter);
        setupPlaceAutoComplete();
        initPlaces();
        alram = new DialogAlarm();
        DialogAlarm.onNoteAdded.observe(this, (note) -> {
            if(!note.equals("")) {
                Notes.add(note);
                adapter.notifyDataSetChanged();
  }
            else {
                Toast.makeText(this, "Empty Note ..", Toast.LENGTH_SHORT).show();
            }});
        binding.calenderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment calenderFrag;
                calenderFrag = new DatePickerFragment();
                calenderFrag.show(getSupportFragmentManager(), "Date");
                TimePickerFragment.onTimeChange.observe(AddTripActivity.this, new Observer<String>() {
                    @Override
                    public void onChanged(String s) {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy-H:m");
                        try {
                            Date date = simpleDateFormat.parse(s);
                            if (date.getTime() < System.currentTimeMillis()) {
                                Toast.makeText(AddTripActivity.this, "Choose Valid Date ...", Toast.LENGTH_SHORT).show();
                                TimePickerFragment.onTimeChange = new MutableLiveData<>();
                            } else {
                                binding.dateTripET.setText(s);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        TimePickerFragment.onTimeChange = new MutableLiveData<>();

                    }
                });
            }
        });
        binding.addNotesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alram.show(getSupportFragmentManager(), "Add Notes");


// With setInexactRepeating(), you have to use one of the AlarmManager interval
// constants--in this case, AlarmManager.INTERVAL_DAY.

            }
        });

        binding.toATV.setTextColor(Color.parseColor("#E72626"));
        binding.fromATV.setTextColor(Color.parseColor("#E72626"));
    }

    int alarmID = (int) System.currentTimeMillis();

    private void ResetAllFields() {

        binding.dateTripET.setText("");
        binding.tripNameET.setText("");
        binding.fromATV.setText("");
        binding.toATV.setText("");
        binding.roundTripSW.setChecked(false);

   binding.dateTripET.setError(null);
        binding.tripNameET.setError(null);
        binding.fromATV.setError(null);
        binding.toATV.setError(null);




        Notes.clear();
        adapter.notifyDataSetChanged();

    }

    private void setAlarmManager(trip tripObject) {

        Intent intent = new Intent(AddTripActivity.this, AlarmReceiver.class);
        intent.putExtra("trip_ObjectId", tripObject.getId());
        intent.putExtra("trip_name", tripObject.getName());
        intent.putExtra("trip_start", tripObject.getStart());
        intent.putExtra("trip_end", tripObject.getEnd());
        intent.putExtra("trip_start_lat", tripObject.getStart_lat());
        intent.putExtra("trip_start_lng", tripObject.getStart_lng());
        intent.putExtra("date_time", tripObject.getDate_time());
        intent.putExtra("trip_end_lat", tripObject.getEnd_lat());
        intent.putExtra("trip_end_lng", tripObject.getEnd_lng());
        intent.putExtra("trip_alarmId", tripObject.getAlarmId());
        intent.putExtra("trip_type", tripObject.getType());
        intent.putExtra("trip_repeat", tripObject.getRepeat());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(AddTripActivity.this, tripObject.getAlarmId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy-H:m");
        try {
            Date date = simpleDateFormat.parse(tripObject.getDate_time());
            calendar.setTime(date);

            if (tripObject.getRepeat().equals("One time")) {
                System.out.println("done");
                manager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);
            } else if (tripObject.getRepeat().equals("Daily")) {
                long timeInMillis = calendar.getTimeInMillis();
                manager.setRepeating(AlarmManager.RTC, timeInMillis, 24 * 60 * 60 * 1000, pendingIntent);
                System.out.println("done");
            } else if (tripObject.getRepeat().equals("Weekly")) {
                long timeInMillis = calendar.getTimeInMillis();
                if (System.currentTimeMillis() > timeInMillis) {
                    timeInMillis = timeInMillis + 24 * 60 * 60 * 1000 * 7;
                }
                manager.setRepeating(AlarmManager.RTC, timeInMillis, 24 * 60 * 60 * 1000 * 7, pendingIntent);
            } else if (tripObject.getRepeat().equals("Monthly")) {
                long timeInMillis = calendar.getTimeInMillis();
                if (System.currentTimeMillis() > timeInMillis) {
                    timeInMillis = timeInMillis + 30 * 24 * 60 * 60 * 1000;
                }
                manager.setRepeating(AlarmManager.RTC, timeInMillis, 30 * 24 * 60 * 60 * 1000, pendingIntent);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }


// With setInexactRepeating(), you have to use one of the AlarmManager interval
// constants--in this case, AlarmManager.INTERVAL_DAY
    }

    private void setupPlaceAutoComplete() {
        autocompleteFragment = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        autocompleteFragment.setPlaceFields(placesInfo);
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                Toast.makeText(AddTripActivity.this, "" + place.getLatLng(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(@NonNull Status status) {

            }
        });
    }

    private void initPlaces() {
        Places.initialize(this, getString(R.string.place_api_key));
        client = Places.createClient(this);
    }


    public void OnSave(View view) {

        tripModel.getTripAdded().observe(this, (b) -> {
            if (b != null) {
                Toast.makeText(this, "Added successfully ", Toast.LENGTH_SHORT).show();
                setAlarmManager(b);

            } else {
                Toast.makeText(this, "Failed .. ", Toast.LENGTH_SHORT).show();

            }
            ResetAllFields();
            progressDialog.cancel();
            HomeActivity.editor.putString("updated","1");
            HomeActivity.editor.commit();
            tripModel.setTripAdded(new MutableLiveData<>());

        });

        String toplace = binding.toATV.getText().toString();
        String fromPlace = binding.fromATV.getText().toString();
        if (binding.fromATV.getText().toString().equals("") || binding.toATV.getText().toString().equals("") || binding.tripNameET.getText().toString().equals("") ||
                binding.dateTripET.getText().toString().equals("") || toplace.equals("") || fromPlace.equals("")) {
            Toast.makeText(this, "please fill all the fields correctly ", Toast.LENGTH_SHORT).show();
        }
        else if (toplace.trim().equals(fromPlace.trim())){    Toast.makeText(this, "the both places are identical ", Toast.LENGTH_SHORT).show();
        }
        else {

            Geocoder geocoder = new Geocoder(AddTripActivity.this);
            try {
                List<Address> fromLocationName = geocoder.getFromLocationName(toplace, 1);
                if (fromLocationName.size() > 0) {
                    trip.setEnd_lat(fromLocationName.get(0).getLatitude());
                    trip.setEnd_lng(fromLocationName.get(0).getLongitude());
                    toValid = true;
                    binding.toATV.setTextColor(Color.parseColor("#31064B"));
                } else {
                    binding.toATV.setError("the place is not clear try with another spelling ");
                    toValid = false;
                }
            } catch (IOException e) {
                toValid = false;
                e.printStackTrace();
            }

            geocoder = new Geocoder(AddTripActivity.this);
            try {
                List<Address> fromLocationName = geocoder.getFromLocationName(fromPlace, 1);
                if (fromLocationName.size() > 0) {
                    trip.setStart_lat(fromLocationName.get(0).getLatitude());
                    trip.setStart_lng(fromLocationName.get(0).getLongitude());
                    binding.fromATV.setTextColor(Color.parseColor("#31064B"));
                    fromValid = true;
                } else {
                    binding.fromATV.setError("the place is not clear");
                    fromValid = false;

                }
            } catch (IOException e) {
                fromValid = false;
                e.printStackTrace();
            }

            if (!toValid || !fromValid) {
                Toast.makeText(this, "try with another spelling ", Toast.LENGTH_SHORT).show();

                if (!fromValid)
                    binding.fromATV.setError("the spelling is not clear");
                else   binding.fromATV.setError("the spelling is not clear");
            } else {
                progressDialog = new ProgressDialog(this);
                progressDialog.setTitle("Loading Trip ..");
                progressDialog.show();

                trip.setDate_time(binding.dateTripET.getText().toString());
                trip.setName(binding.tripNameET.getText().toString());
                trip.setState("upcoming");

                trip.setStart(fromPlace);
                trip.setEnd(toplace);
                trip.setRepeat(repetations.get(binding.spinner.getSelectedIndex()));
                System.out.println(repetations.get(binding.spinner.getSelectedIndex()));

                if (binding.roundTripSW.isChecked()) {
                    trip.setType("true");
                    Toast.makeText(this, "activated", Toast.LENGTH_SHORT).show();
                } else
                    trip.setType("false");
                trip.setUser_id(HomeActivity.pref.getString("login", "0"));
                List<note> notes = new ArrayList<>();
                for (int i = 0; i < Notes.size(); i++) {
                    note note = new note();
                    note.setText(Notes.get(i));
                    System.out.println(Notes.get(i));
                    note.setChecked(0);
                    notes.add(note);
                    note = null;
                }
                alarmID = (int) System.currentTimeMillis();
                trip.setAlarmId(alarmID);
                tripVM.Add_Trip(AddTripActivity.this.trip, notes);
                fromValid = false;
                toValid = false;
            }
            binding.toATV.setTextColor(Color.parseColor("#E72626"));
            binding.fromATV.setTextColor(Color.parseColor("#E72626"));

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}

