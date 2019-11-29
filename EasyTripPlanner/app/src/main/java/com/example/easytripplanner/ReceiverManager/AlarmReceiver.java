package com.example.easytripplanner.ReceiverManager;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.easytripplanner.R;
import com.example.easytripplanner.View.NotificationHandler;

public class AlarmReceiver extends BroadcastReceiver {
    private static final String CHANNEL_ID = "1";

    @Override
    public void onReceive(Context context, Intent intent) {
       /* Toast.makeText(context, "Hello World", Toast.LENGTH_LONG).show();
        Intent intent1 = new Intent(context, MyIntentService.class);
        context.startService(intent1);*/
        String trip_name= intent.getExtras().getString("trip_name");
        String date_time= intent.getExtras().getString("date_time");
        String trip_start= intent.getExtras().getString("trip_start");
        String trip_end= intent.getExtras().getString("trip_end");
        int trip_ObjectId= intent.getExtras().getInt("trip_ObjectId");
        Double trip_start_lat= intent.getExtras().getDouble("trip_start_lat");
        Double trip_start_lng= intent.getExtras().getDouble("trip_start_lng");
        Double trip_end_lat= intent.getExtras().getDouble("trip_end_lat");
        Double trip_end_lng= intent.getExtras().getDouble("trip_end_lng");
        String  trip_type= intent.getExtras().getString("trip_type");
        String  trip_repeat= intent.getExtras().getString("trip_repeat");
        int trip_alarmId= intent.getExtras().getInt("trip_alarmId");







         Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    long[] vibrate = new long[] { 1000, 1000, 1000, 1000, 1000 };
//               Intent in = new Intent(android.content.Intent.ACTION_VIEW,
//                Uri.parse("http://maps.google.com/maps?saddr="+trip_start_lat+","+trip_start_lng+"&daddr="+trip_end_lat+","+trip_end_lng));
//        PendingIntent pendingIntent = PendingIntent.getActivity(context,trip_alarmId,in,PendingIntent.FLAG_UPDATE_CURRENT);

        Intent in = new Intent(context, NotificationHandler.class);
        in.putExtra("action","start");
         in.putExtra("trip_ObjectId",trip_ObjectId);

        in.putExtra("trip_start_lat",trip_start_lat);
        in.putExtra("trip_start_lng",trip_start_lng);
        in.putExtra("trip_end_lat",trip_end_lat);
        in.putExtra("trip_end_lng",trip_end_lng);
        in.putExtra("trip_alarmId",trip_alarmId);
        in.putExtra("trip_type",trip_type);
        in.putExtra("trip_repeat",trip_repeat);


        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,in,PendingIntent.FLAG_UPDATE_CURRENT);






        Intent CustomIntentCancel = new Intent(context, NotificationHandler.class);
        CustomIntentCancel.putExtra("cancel","cancel");
        CustomIntentCancel.putExtra("trip_ObjectId",trip_ObjectId);
        CustomIntentCancel.putExtra("trip_alarmId",trip_alarmId);
        PendingIntent CustomPendingIntentCancel = PendingIntent.getActivity(context,2,CustomIntentCancel,PendingIntent.FLAG_UPDATE_CURRENT);




        Intent CustomIntentSnooze = new Intent(context,NotificationHandler.class);
        CustomIntentSnooze.putExtra("trip_ObjectId",trip_ObjectId);
        CustomIntentSnooze.putExtra("trip_name",trip_name);
        CustomIntentSnooze.putExtra("trip_start",trip_start);
        CustomIntentSnooze.putExtra("trip_end",trip_end);
        CustomIntentSnooze.putExtra("trip_start_lat",trip_start_lat);
        CustomIntentSnooze.putExtra("trip_start_lng",trip_start_lng);
        CustomIntentSnooze.putExtra("trip_end_lat",trip_end_lat);
        CustomIntentSnooze.putExtra("trip_end_lng",trip_end_lng);
        CustomIntentSnooze.putExtra("date_time",date_time);
        CustomIntentSnooze.putExtra("snooze","snooze");
        CustomIntentSnooze.putExtra("trip_alarmId",trip_alarmId);
        CustomIntentSnooze.putExtra("trip_type",trip_type);
        PendingIntent CustomPendingIntentSnooze = PendingIntent.getActivity(context,1,CustomIntentSnooze,PendingIntent.FLAG_UPDATE_CURRENT);
        Toast.makeText(context, trip_start, Toast.LENGTH_SHORT).show();
//================================ Building Notification    ================
                    createNotificationChannel(context);
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                            .setSmallIcon(R.drawable.icon)
                            .addAction(0,"Start",pendingIntent)
                            .addAction(0,"Snooze",CustomPendingIntentSnooze)
                            .addAction(0,"Cancel",CustomPendingIntentCancel)
                            .setTicker(trip_name+ " it is the time  to to take ")
                            .setContentTitle(trip_name)
                            .setContentText("From : " + trip_start  +" To : "+trip_end)
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT).setSound(uri).setVibrate(vibrate);

                    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

// notificationId is a unique int for each notification that you must define
                    notificationManager.notify(trip_alarmId, builder.build());


    }

    private void createNotificationChannel(Context context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String description = "Channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "1", importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
