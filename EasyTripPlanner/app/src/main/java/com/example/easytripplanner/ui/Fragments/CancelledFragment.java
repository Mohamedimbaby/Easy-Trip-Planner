package com.example.easytripplanner.ui.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.easytripplanner.DTO.trip;
import com.example.easytripplanner.Model.tripModel;
import com.example.easytripplanner.R;
import com.example.easytripplanner.View.HomeActivity;
import com.example.easytripplanner.View.MainActivity;
import com.example.easytripplanner.ViewModel.TripVM;
import com.example.easytripplanner.adapters.PastRecyclerCustomAdapter;
import com.example.easytripplanner.adapters.RecyclerCustomAdapter;
import com.facebook.login.Login;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static android.view.View.VISIBLE;

public class CancelledFragment extends Fragment {

    RecyclerView listView ;
    TripVM tripVM;
    com.example.easytripplanner.Model.tripModel tripModel;
    String Logined;
ProgressBar bar ;
TextView text;
    List<trip> localetrips;
    PastRecyclerCustomAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_cancelled, container, false);
        listView=view.findViewById(R.id.recyclerViewPast);
        bar=view.findViewById(R.id.progress_bar);
        bar.setVisibility(VISIBLE);
        tripVM= TripVM.getInstance(getContext());
        tripModel= tripVM.getTripModel();
        text=view.findViewById(R.id.textView);
        SwipeRefreshLayout swipe = view.findViewById(R.id.swipeCancelled);
        if (localetrips!=null) {
            localetrips.clear();
            adapter.notifyDataSetChanged();
        }

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Logined = HomeActivity.pref.getString("login", "0");

                if (localetrips!=null)
                    localetrips.clear();
                if (adapter!=null)
                    adapter.notifyDataSetChanged();
                bar.setVisibility(View.VISIBLE);

                        TripVM.getInstance(getContext()).getAllTripsUpComing(Logined,false);


            }
        });



        tripModel.getAllTripsLiveDatePast().observe(this, new Observer<List<trip>>() {
            @Override
            public void onChanged(List<trip> trips) {
                bar.setVisibility(View.GONE);
                if (trips.size()>0) {
                    localetrips=trips;
                    adapter = new PastRecyclerCustomAdapter(getContext(), (ArrayList<trip>) localetrips);
                    listView.setAdapter(adapter);
                    listView.setLayoutManager(new LinearLayoutManager(getContext()));
                    swipe.setRefreshing(false);
                    bar.setVisibility(View.GONE);
                    text.setText("");

                }
                else {text.setText("NO Past trips");

               if (localetrips!=null){localetrips.clear();
                adapter.notifyDataSetChanged();}
                swipe.setRefreshing(false);
                    bar.setVisibility(View.GONE);
                }
            }
        });
        Logined = HomeActivity.pref.getString("login", "0");

                tripVM.getAllTripsUpComing(Logined,false);

        return  view;


    }
}
