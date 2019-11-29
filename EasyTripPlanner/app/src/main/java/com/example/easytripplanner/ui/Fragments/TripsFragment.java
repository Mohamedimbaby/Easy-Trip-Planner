package com.example.easytripplanner.ui.Fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.easytripplanner.DTO.note;
import com.example.easytripplanner.DTO.trip;
import com.example.easytripplanner.R;
import com.example.easytripplanner.View.HomeActivity;
import com.example.easytripplanner.ViewModel.TripVM;
import com.example.easytripplanner.adapters.RecyclerCustomAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TripsFragment extends Fragment {
    RecyclerView listView;

    com.example.easytripplanner.Model.tripModel tripModel;
    String Logined;
    ProgressBar bar;
    TextView text, LoginedName, Logout;
    SwipeRefreshLayout swipe;

    ProgressDialog progressDialog;
    List<trip> localetrips;
    ImageView sync;
    RecyclerCustomAdapter adapter;
    Location location;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_trips, container, false);
        listView = view.findViewById(R.id.recyclerViewUpcoming);
        bar = view.findViewById(R.id.progress_bar);

        double lat = getArguments().getDouble("lat");
        double lng = getArguments().getDouble("lng");
        location = new Location("");
        location.setLatitude(lat);
        location.setLongitude(lng);
        bar.setVisibility(View.VISIBLE);
        text = view.findViewById(R.id.textView);
        LoginedName = view.findViewById(R.id.LoginedName);
        Logout = view.findViewById(R.id.logout);
        sync = view.findViewById(R.id.Sync);
        sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()).setMessage("Are You sure you Sync Now ? ")
                        .setTitle("Maybe take a few Seconds .. ").setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int whih) {
                                dialog.cancel();
                                String updated = HomeActivity.pref.getString("updated", "0");
                                System.out.println("before" + updated);
                                if (updated.equals("1")) {
                                    System.out.println("before" + updated);
                                    progressDialog = new ProgressDialog(getContext());
                                    progressDialog.setTitle("Synchronization ..");
                                    progressDialog.show();

                                    TripVM.getInstance(getContext()).getAllTrips();
                                    tripModel.getAllTripsLiveDateForSync().observe(TripsFragment.this, new Observer<List<trip>>() {
                                        @Override
                                        public void onChanged(List<trip> trips) {
                                            if (trips != null) {
                                                System.out.println("before" + updated);
                                                TripVM.getInstance(getContext()).getAllNotes();
                                                tripModel.getAllNotesLiveDateForSync().observe(TripsFragment.this, new Observer<List<note>>() {
                                                    @Override
                                                    public void onChanged(List<note> notes) {

                                                        TripVM.getInstance(getContext()).SyncData(trips, notes);
                                                        System.out.println("get All Notes " + notes.toString());

                                                        tripModel.getSynced().observe(TripsFragment.this, new Observer<Boolean>() {
                                                            @Override
                                                            public void onChanged(Boolean aBoolean) {
                                                                if (aBoolean) {
                                                                    Toast.makeText(getContext(), "Synced Successfully ..", Toast.LENGTH_SHORT).show();
                                                                    HomeActivity.editor.putString("updated", "0");
                                                                    HomeActivity.editor.commit();

                                                                } else
                                                                    Toast.makeText(getContext(), "there is something wrong ..", Toast.LENGTH_SHORT).show();
                                                                progressDialog.cancel();
                                                            }
                                                        });
                                                    }
                                                });
                                            } else {
                                                Toast.makeText(getContext(), "no trips to sync...", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                    });
                                } else {
                                    Toast.makeText(getContext(), "No updated to Sync with ", Toast.LENGTH_SHORT).show();
                                }
                                tripModel.setAllTripsLiveDateForSync(new MutableLiveData<>());
                                tripModel.setAllNotesLiveDateForSync(new MutableLiveData<>());
                            }
                        }).setNegativeButton(android.R.string.cancel, null);
                builder.getContext().setTheme(R.style.AppTheme_Dialog);
                builder.create().show();

            }
        });
        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()).setMessage("Are You sure you want to log out ?")
                        .setTitle("Alert").setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int whih) {
                                dialog.cancel();
                                HomeActivity.editor.putString("login", "0");
                                HomeActivity.editor.putString("name", "0");
                                HomeActivity.editor.commit();
                                Intent in = new Intent(getContext(), HomeActivity.class);
                                startActivity(in);
                                getActivity().finish();
                            }
                        }).setNegativeButton(android.R.string.cancel, null);
                builder.getContext().setTheme(R.style.AppTheme_Dialog);
                builder.create().show();
            }
        });
        String name = HomeActivity.pref.getString("name", "0");
        LoginedName.append(name);
        swipe = view.findViewById(R.id.swipe);
        if (localetrips != null) {
            localetrips.clear();
            adapter.notifyDataSetChanged();
        }

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                Logined = HomeActivity.pref.getString("login", "0");
                if (localetrips != null)
                    localetrips.clear();
                if (adapter != null)
                    adapter.notifyDataSetChanged();
                bar.setVisibility(View.VISIBLE);
                if (isNetworkConnected()) {
                    TripVM.getInstance(getContext()).getAllTripsUpComing(Logined, true);
                } else
                {text.setText("there is no internet connection");

                    bar.setVisibility(View.GONE);}

                swipe.setRefreshing(false);
            }
        });
        tripModel = TripVM.getInstance(getContext()).getTripModel();
        tripModel.getAllTripsLiveDateUpcoming().observe(TripsFragment.this, new Observer<List<trip>>() {
            @Override
            public void onChanged(List<trip> trips) {

                if (trips.size() > 0) {
                    localetrips = trips;
                    Collections.reverse(localetrips);
                    new DownloadFilesTask().execute();


                    listView.setLayoutManager(new LinearLayoutManager(getContext()));
                    swipe.setRefreshing(false);
                    text.setText("");


                } else {
                    text.setText("NO UpComing trips");
                    bar.setVisibility(View.INVISIBLE);
                    if (localetrips != null) {
                        localetrips.clear();
                        adapter.notifyDataSetChanged();
                    }
                    swipe.setRefreshing(false);
                }
            }
        });


        Logined = HomeActivity.pref.getString("login", "0");

        if (isNetworkConnected()) {
            TripVM.getInstance(getContext()).getAllTripsUpComing(Logined, true);
        } else {text.setText("there is no internet connection");
        bar.setVisibility(View.GONE);}


        return view;


    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    private class DownloadFilesTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            adapter = new RecyclerCustomAdapter(getContext(), (ArrayList<trip>) localetrips, location);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            listView.setAdapter(adapter);
            bar.setVisibility(View.GONE);
            super.onPostExecute(aVoid);
        }
    }


}