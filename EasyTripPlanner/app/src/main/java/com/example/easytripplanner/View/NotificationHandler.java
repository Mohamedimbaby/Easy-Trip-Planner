package com.example.easytripplanner.View;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;

import com.example.easytripplanner.DTO.note;
import com.example.easytripplanner.DTO.trip;
import com.example.easytripplanner.Model.tripModel;
import com.example.easytripplanner.R;
import com.example.easytripplanner.ReceiverManager.AlarmReceiver;
import com.example.easytripplanner.View.AddTripActivity;
import com.example.easytripplanner.ViewModel.TripVM;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.content.Context.NOTIFICATION_SERVICE;

public class NotificationHandler extends AppCompatActivity {
    TripVM tripVM;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tripVM = TripVM.getInstance(this);
        String snooze = getIntent().getExtras().getString("snooze");
        String cancel = getIntent().getExtras().getString("cancel");
        String action = getIntent().getExtras().getString("action");

        int trip_ObjectId = getIntent().getExtras().getInt("trip_ObjectId");
        System.out.println(trip_ObjectId);
        System.out.println(action);
        System.out.println(snooze);
        int NOTIFICATION_ID = (int) getIntent().getExtras().get("trip_alarmId");

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(NOTIFICATION_ID);

        if (action != null) {
            Double trip_start_lat = getIntent().getExtras().getDouble("trip_start_lat");
            Double trip_start_lng = getIntent().getExtras().getDouble("trip_start_lng");
            Double trip_end_lat = getIntent().getExtras().getDouble("trip_end_lat");
            Double trip_end_lng = getIntent().getExtras().getDouble("trip_end_lng");
            String type = getIntent().getExtras().getString("trip_type");
            String trip_repeat = getIntent().getExtras().getString("trip_repeat");

            Intent intent = new Intent(this, tripDetails.class);
            intent.putExtra("trip", trip_ObjectId);
            intent.putExtra("notification", type);
            startActivity(intent);
            Intent in = new Intent(android.content.Intent.ACTION_VIEW,
                    Uri.parse("http://maps.google.com/maps?saddr=" + trip_start_lat + "," + trip_start_lng + "&daddr=" + trip_end_lat + "," + trip_end_lng));
            startActivity(in);
            if (trip_repeat.equals("One time")) {
                tripVM.cancelTrip(trip_ObjectId, "Past");
            }
        } else if (cancel != null) {

            tripVM.cancelTrip(trip_ObjectId, "Cancelled");
            System.out.println("Cancel State");
            tripModel tripModel = tripVM.getTripModel();
            tripModel.getTripCanceled().observe(this, (bool) -> {
                if (bool) Toast.makeText(this, "Canceled ...", Toast.LENGTH_SHORT).show();
            });
            // for example play a music
        } else if (snooze != null) {

            Intent intent = new Intent(this, AlarmReceiver.class);

            String trip_name = getIntent().getExtras().getString("trip_name");
            String trip_start = getIntent().getExtras().getString("trip_start");
            String trip_end = getIntent().getExtras().getString("trip_end");
            String trip_type = getIntent().getExtras().getString("trip_type");

            Double trip_start_lat = getIntent().getExtras().getDouble("trip_start_lat");
            Double trip_start_lng = getIntent().getExtras().getDouble("trip_start_lng");
            Double trip_end_lat = getIntent().getExtras().getDouble("trip_end_lat");
            Double trip_end_lng = getIntent().getExtras().getDouble("trip_end_lng");
            String date_time = getIntent().getExtras().getString("date_time");
            intent.putExtra("trip_ObjectId", trip_ObjectId);
            intent.putExtra("trip_name", trip_name);
            intent.putExtra("trip_start", trip_start);
            intent.putExtra("trip_end", trip_end);
            intent.putExtra("trip_start_lat", trip_start_lat);
            intent.putExtra("trip_start_lng", trip_start_lng);
            intent.putExtra("trip_end_lat", trip_end_lat);
            intent.putExtra("trip_end_lng", trip_end_lng);
            intent.putExtra("date_time", date_time);
            intent.putExtra("trip_alarmId", NOTIFICATION_ID);
            intent.putExtra("trip_type", trip_type);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, NOTIFICATION_ID, intent, 0);
            AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy-H:m");
            try {
                Date date = simpleDateFormat.parse(date_time);
                System.out.println(date.toString());
                date.setTime(date.getTime() + 5 * 60000);
                System.out.println(date);
                calendar.setTime(date);
                manager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            //e current clos notification

        }
        finish();
    }
}

