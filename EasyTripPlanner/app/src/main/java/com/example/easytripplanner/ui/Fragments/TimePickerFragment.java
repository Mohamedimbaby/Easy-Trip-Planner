package com.example.easytripplanner.ui.Fragments;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.MutableLiveData;

import com.example.easytripplanner.View.AddTripActivity;

import java.util.Calendar;

public  class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {


    public  static MutableLiveData<String> onTimeChange  = new MutableLiveData<>();  ;
String date ;
    public TimePickerFragment(String date) {

        this.date=date;

    }

    public TimePickerFragment() {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        onTimeChange.setValue(date+"-"+hourOfDay+":"+minute);
        // Do something with the time chosen by the user
    }


}
