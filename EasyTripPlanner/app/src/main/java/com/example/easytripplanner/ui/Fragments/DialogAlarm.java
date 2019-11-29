package com.example.easytripplanner.ui.Fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.easytripplanner.R;

public class DialogAlarm extends DialogFragment {
    EditText edit;
 public static MutableLiveData<String> onNoteAdded = new MutableLiveData<>();

    @SuppressLint("ResourceAsColor")
    @Override    public Dialog onCreateDialog(Bundle savedInstanceState) {

        edit = new EditText(getActivity());
        edit.setTextColor(Color.parseColor("#68DA44"));
        AlertDialog.Builder   builder=   new AlertDialog.Builder(getActivity()).setView(edit)
                .setTitle("Add Note").setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whih) {
                dialog.cancel();
                String s = edit.getText().toString();

                onNoteAdded.setValue(s);

            }
        }) .setNegativeButton(android.R.string.cancel,null);
        builder.getContext().setTheme(R.style.AppTheme_Dialog);

        return builder.create();
    }
}
