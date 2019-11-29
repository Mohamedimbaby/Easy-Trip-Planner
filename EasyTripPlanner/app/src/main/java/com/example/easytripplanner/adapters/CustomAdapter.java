package com.example.easytripplanner.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.easytripplanner.DTO.note;
import com.example.easytripplanner.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<note> implements View.OnClickListener{

    private ArrayList<note> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView txtName;
        CheckBox checkBox ;

    }

    public CustomAdapter(ArrayList<note> data, Context context) {
        super(context, R.layout.custom_list_view_notes, data);
        this.dataSet = data;
        this.mContext=context;

    }
    public ArrayList<note> getDataSet(){return this.dataSet;}

    @Override
    public void onClick(View v) {
        View parentRow = (View) v.getParent();

        ListView listView = (ListView) parentRow.getParent();

        final int position = listView.getPositionForView(parentRow);

        Object object= getItem(position);
        note dataModel=(note) object;
        if(this.dataSet.get(position).getChecked()==0)
        Toast.makeText(mContext, "this note is done ", Toast.LENGTH_SHORT).show();
        else         Toast.makeText(mContext, "this note is not done yet ", Toast.LENGTH_SHORT).show();
        System.out.println(this.dataSet.get(position).getText());


this.dataSet.get(position).setChecked(1^this.dataSet.get(position).getChecked());

    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        note dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.custom_list_view_notes, parent, false);

            viewHolder.checkBox =  convertView.findViewById(R.id.checkBox);
            viewHolder.txtName =  convertView.findViewById(R.id.textNote);


            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        lastPosition = position;

        viewHolder.txtName.setText(dataModel.getText());
        if (dataModel.getChecked()==0)
        viewHolder.checkBox.setChecked(false);
        else viewHolder.checkBox.setChecked(true);
        viewHolder.checkBox.setOnClickListener(this);
        // Return the completed view to render on screen
        return convertView;
    }
}
