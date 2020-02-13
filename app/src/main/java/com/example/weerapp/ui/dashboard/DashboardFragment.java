package com.example.weerapp.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import 	java.util.Date;
import 	java.util.Calendar;
import  java.text.SimpleDateFormat;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import java.text.DateFormat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.weerapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    private StringBuilder sb = new StringBuilder();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel = ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);


        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c);

        final TextView txt = (TextView)root.findViewById(R.id.text_worldwide_today);
        RequestQueue rq = Volley.newRequestQueue(getActivity().getApplicationContext());
        String url= "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime="+getYesterdayDateString()+"&endtime="+formattedDate;

        Log.v("date", url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override

                    public void onResponse(String response) {
                        // Do something with the response

                        try{
                            JSONObject o = new JSONObject(response);
                            JSONArray values=o.getJSONArray("features");

                            for ( int i=0; i <= 29; i++) {
                                JSONObject sonuc = values.getJSONObject(i);
                                JSONObject properties = sonuc.getJSONObject("properties");
                                try {
                                    sb.append("Magnitude: " + properties.get("mag") + "\n");
                                    sb.append("Place: " + properties.get("place") + "\n\n");
                                }catch(Exception e){
                                    Log.d("error", e.toString());
                                }
                            }
                            txt.setText(sb.toString());

                        }  catch (JSONException ex){}

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.v("msg", error.toString());
                    }
                });
        rq.add(stringRequest);

        return root;
    }
    private String getYesterdayDateString() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return dateFormat.format(cal.getTime());
    }
}