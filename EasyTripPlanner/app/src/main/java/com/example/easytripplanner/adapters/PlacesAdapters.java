package com.example.easytripplanner.adapters;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.easytripplanner.DTO.JobService;

import java.util.ArrayList;

public class PlacesAdapters  extends  ArrayAdapter implements  Filterable {
ArrayList<String> places= new ArrayList<>();
JobService job ;
Context context;
int resource ;
    public PlacesAdapters(@NonNull Context context, int resource) {
        super(context, resource);
        this.context=context;
        this.resource=resource;
    job = new JobService(context);
    }
    public int getCount()
    {
        return places.size();
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return  places.get(position);
    }

    @NonNull
    @Override
    public Filter getFilter() {
        Filter filter= new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    places = job.autoComplete(constraint.toString());
                    filterResults.values = places;
                    filterResults.count = places.size();
                }
            return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
if(results!=null && results.count>0)
{
    notifyDataSetChanged();
}
else {
    notifyDataSetInvalidated();
}
            }
            };
        return filter;
        }
}
