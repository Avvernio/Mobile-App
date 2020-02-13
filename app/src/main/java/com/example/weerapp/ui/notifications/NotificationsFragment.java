package com.example.weerapp.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.weerapp.R;

public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;
    private StringBuilder sb = new StringBuilder();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);

        final TextView txt = (TextView)root.findViewById(R.id.text_notifications);
        RequestQueue rq = Volley.newRequestQueue(getActivity().getApplicationContext());
        String url= "http://cdn.knmi.nl/knmi/map/page/seismologie/all_tectonic.json";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override

                    public void onResponse(String response) {
                        // Do something with the response

                        try{
                            JSONObject o = new JSONObject(response);
                            JSONArray values=o.getJSONArray("events");

                            for ( int i=0; i <= 29; i++) {

                                JSONObject sonuc = values.getJSONObject(i);

                                sb.append("Date: " + sonuc.getString("date") + "\n");
                                sb.append("Place: " + sonuc.getString("place") + "\n");
                                sb.append("Magnitude: " + sonuc.getString("mag") + "\n\n");
                            }

                            txt.setText(sb.toString());


                        }  catch (JSONException ex){}

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                    }
                });
        rq.add(stringRequest);
        return root;
    }
}