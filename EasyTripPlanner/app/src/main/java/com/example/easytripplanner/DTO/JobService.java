package com.example.easytripplanner.DTO;

import android.content.Context;
import android.content.Intent;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JobService {
  static   private Context context;

    public JobService(Context context) {
        this.context = context;
    }
    static ArrayList<String> result = new ArrayList<>();
 public    static ArrayList<String > autoComplete(String name) {

        RequestQueue queue= Volley.newRequestQueue(context);
        String url = "https://maps.googleapis.com/maps/api/place/autocomplete/json?";

        url+="input="+ name;
        url+="&key=AIzaSyCtfx_tJNE4Zu9np42YYsHsoI9sHwACR3c";
        JsonObjectRequest objectRequest= new JsonObjectRequest(url, null,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    System.out.println(response.toString());
                    JSONArray predictions = response.getJSONArray("predictions");
                    for (int i = 0; i < predictions.length(); i++) {
                        result.add(predictions.getJSONObject(i).getString("description"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
     objectRequest.setRetryPolicy(new DefaultRetryPolicy(
             0,
             DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
             DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                queue.add(objectRequest);
    return result;
    }
}
