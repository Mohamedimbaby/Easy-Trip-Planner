package com.example.easytripplanner.ui.Fragments;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private final Context mContext;
    private  Location location;

    public SectionsPagerAdapter(Context context, FragmentManager fm, Location location) {
        super(fm);
        mContext = context;
    this.location=location;
    }

    @Override
    public Fragment getItem(int position) {
        if (position==0) {
            Fragment trips= new TripsFragment();
            Bundle bundle = new Bundle();
            bundle.putDouble("lat",location.getLatitude());
            bundle.putDouble("lng",location.getLongitude());
            trips.setArguments(bundle);
            return trips;

        }else
            return new CancelledFragment();

    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
    if (position==0)
        return "UpComing Trips";
    else
        return "Past Trips";
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return 2;
    }
}