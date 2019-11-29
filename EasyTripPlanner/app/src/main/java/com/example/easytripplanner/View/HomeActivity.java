package com.example.easytripplanner.View;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.backendless.Backendless;
import com.example.easytripplanner.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Toast;

import com.example.easytripplanner.ui.Fragments.SectionsPagerAdapter;

public class HomeActivity extends AppCompatActivity {
    public static SharedPreferences pref;
    public static SharedPreferences.Editor editor;
    Location locate;

    LocationManager manager;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home2);
        pref = getPreferences(MODE_PRIVATE);
        editor = pref.edit();

        manager = (LocationManager) getSystemService(LOCATION_SERVICE);

        String logined = pref.getString("login", "0");

        if (logined.equals("0")) {
            Intent in = new Intent(this, MainActivity.class);
            startActivity(in);
            finish();
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            String[]perm={Manifest.permission.ACCESS_FINE_LOCATION};
            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("get your Location..");
            progressDialog.setIcon(R.drawable.login);
            progressDialog.show();
            ActivityCompat.requestPermissions( this,perm,1);
        }
        else{
            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("get Your Location ..");
            progressDialog.setIcon(R.drawable.login);
            progressDialog.show();
            manager.requestSingleUpdate(LocationManager.GPS_PROVIDER, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    locate =location;
                    SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(HomeActivity.this, getSupportFragmentManager(),locate);
                    ViewPager viewPager = findViewById(R.id.view_pager);
                    viewPager.setAdapter(sectionsPagerAdapter);
                    TabLayout tabs = findViewById(R.id.tabs);
                    tabs.setupWithViewPager(viewPager);
                    FloatingActionButton fab = findViewById(R.id.fab);

                    fab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Intent in = new Intent(HomeActivity.this, AddTripActivity.class);
                            startActivity(in);


                        }
                    });


progressDialog.cancel();
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            },null);

        }



    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode==1){
            if (grantResults[0]==PackageManager.PERMISSION_GRANTED){
                try {
                    manager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, (LocationListener) this, null);
                } catch (SecurityException e) {
                    e.printStackTrace();
                }
            }
            else{
                Toast.makeText(this, "denied", Toast.LENGTH_SHORT).show();
            }
        }}

    @Override
    public void onBackPressed() {
        finish();
    }





}


