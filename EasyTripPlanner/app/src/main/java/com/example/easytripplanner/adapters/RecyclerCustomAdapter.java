package com.example.easytripplanner.adapters;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easytripplanner.R;
import com.example.easytripplanner.DTO.trip;
import com.example.easytripplanner.View.HomeActivity;
import com.example.easytripplanner.View.tripDetails;
import com.example.easytripplanner.ViewModel.TripVM;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class RecyclerCustomAdapter extends RecyclerView.Adapter<RecyclerCustomAdapter.ViewHolder> {
ArrayList<trip> trips;
Context context;
TripVM tripVM  ;
Location location ;
    public RecyclerCustomAdapter(Context context, ArrayList<trip> trips,Location location) {
        this.trips = trips;
        this.context= context;
        this.location=location;
        tripVM=TripVM.getInstance(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view,parent,false);
        ViewHolder viewHolder= new ViewHolder (view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tripName.setText(trips.get(position).getName());

        String[] data = timeslice(trips.get(position).getDate_time());
        holder.repeatTV.setText(trips.get(position).getRepeat());
        if (trips.get(position).getType().equals("true"))
        holder.roundTV.setText("--->\n<---");
        else    holder.roundTV.setText("--->");

        holder.tripDate.setText(data[0]);
        holder.tripTime.setText(data[1]);
        holder.tripStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(trips.get(position).getRepeat()+"hi");

                if(trips.get(position).getRepeat().equals("One time"))
                {System.out.println(trips.get(position).getRepeat());
                    tripVM.cancelTrip(trips.get(position).getId(),"Past");
                }
                Intent in = new Intent(context, tripDetails.class);
                in.putExtra("trip", trips.get(position).getId());
                in.putExtra("notification", trips.get(position).getType());
                context.startActivity(in);

                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?saddr="+location.getLatitude()+","+location.getLongitude()+"&daddr="+trips.get(position).getEnd_lat()+","+trips.get(position).getEnd_lng()));
                context.startActivity(intent);
            }
        });
        holder.tripCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            tripVM.cancelTrip(trips.get(position).getId(),"Cancelled");
                Toast.makeText(context, "Cancelled", Toast.LENGTH_SHORT).show();
            }
        });
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(context, tripDetails.class);
                in.putExtra("trip", trips.get(position).getId());
                context.startActivity(in);
            }
        });

    }

    @Override
    public int getItemCount() {
        return trips.size();
    }
String[] timeslice (String time)
{
    String[] split = time.split("-");

    return split;
}







    class ViewHolder extends RecyclerView.ViewHolder {
Button tripTime,tripStart,tripCancel;
TextView tripName,tripDate,repeatTV,roundTV;
CardView cardView ;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tripTime= itemView.findViewById(R.id.tripDate);
            tripName= itemView.findViewById(R.id.tripNameTV);
            tripDate= itemView.findViewById(R.id.tripNotesTV);
            tripStart= itemView.findViewById(R.id.tripstartBtn);
            tripCancel= itemView.findViewById(R.id.tripCancelBtn);
            repeatTV= itemView.findViewById(R.id.repeatTV);
            roundTV= itemView.findViewById(R.id.tripIsRounded);
            cardView= (CardView) itemView;
        }

    }
}
