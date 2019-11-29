package com.example.easytripplanner.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easytripplanner.DTO.trip;
import com.example.easytripplanner.R;
import com.example.easytripplanner.View.tripDetails;
import com.example.easytripplanner.ViewModel.TripVM;

import java.util.ArrayList;

public class PastRecyclerCustomAdapter extends RecyclerView.Adapter<PastRecyclerCustomAdapter.ViewHolder> {
ArrayList<trip> trips;
Context context;

    public PastRecyclerCustomAdapter(Context context, ArrayList<trip> trips) {
        this.trips = trips;
        this.context= context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_past,parent,false);
        ViewHolder viewHolder= new ViewHolder (view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tripName.setText(trips.get(position).getName());
        String state = trips.get(position).getState();
        if (state.equals("Past"))
        holder.tripState.setTextColor(Color.parseColor("#226910"));
        else  if (state.equals("Cancelled"))
            holder.tripState.setTextColor(Color.parseColor("#E72626"));
        holder.tripState.setText(state);
        String[] data = timeslice(trips.get(position).getDate_time());

        holder.tripDate.setText(data[0]);
        holder.tripTime.setText(data[1]);
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
Button tripTime;
TextView tripName,tripDate,tripState;
CardView cardView ;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tripTime= itemView.findViewById(R.id.pasttripDate);
            tripName= itemView.findViewById(R.id.pasttripNameTV);
            tripDate= itemView.findViewById(R.id.pasttripDateTV);
            tripState= itemView.findViewById(R.id.tripState);

            cardView= (CardView) itemView;
        }

    }
}
