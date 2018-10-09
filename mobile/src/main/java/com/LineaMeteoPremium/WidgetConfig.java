package com.LineaMeteoPremium;


import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.robotemplates.webviewapp.R;

import org.alfonz.utility.Logcat;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class WidgetConfig extends Activity {

    private Spinner spinner;
    private HashMap<String, String> LocationIdMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget_config);

        spinner = findViewById(R.id.WidgetConfigSpinner);

        LocationIdMap = new HashMap<String, String>();

        loadSpinnerData("https://api.myjson.com/bins/derf8");

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                // An item was selected. You can retrieve the selected item using
                // parent.getItemAtPosition(pos)

                int[] ids = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(new ComponentName(getApplication(), LineaMeteoPremium.class));
                LineaMeteoPremium lineaMeteoPremium = new LineaMeteoPremium();
                if(LocationIdMap.containsKey(parent.getItemAtPosition(pos))) {
                    lineaMeteoPremium.src = "http://retemeteo.lineameteo.it/banner/big.php?ID="+LocationIdMap.get(parent.getItemAtPosition(pos));
                } else {
                    lineaMeteoPremium.src = "http://retemeteo.lineameteo.it/banner/big.php?ID=1";
                }
                Log.i("LineaMeteo", "activityCall " + lineaMeteoPremium.src);
                lineaMeteoPremium.onUpdate(parent.getContext(), AppWidgetManager.getInstance(parent.getContext()), ids);


            }

            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });
    }





    private void loadSpinnerData(String url) {


        RequestQueue requestQueue=Volley.newRequestQueue(getApplicationContext());

        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            ArrayList<String> Locations = new ArrayList<String>();


            @Override

            public void onResponse(String response) {
                try{

                    JSONObject jsonObject=new JSONObject(response);

                    if(jsonObject.getInt("success")==1){

                        JSONArray jsonArray=jsonObject.getJSONArray("Locations");

                        for(int i=0;i<jsonArray.length();i++){

                            JSONObject jsonObject1=jsonArray.getJSONObject(i);
                            String location=jsonObject1.getString("label");
                            String id=jsonObject1.getString("id");

                            Locations.add(location);

                            LocationIdMap.put(location, id);

                        }

                    }
                    ArrayAdapter<String> spinnerArrayAdapter =
                            new ArrayAdapter<String>(getApplicationContext(),  android.R.layout.simple_spinner_dropdown_item, Locations);

                    spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                            .simple_spinner_dropdown_item);
                    spinner.setAdapter(spinnerArrayAdapter);

                }catch (JSONException e){e.printStackTrace();}

            }

        }, new Response.ErrorListener() {

            @Override

            public void onErrorResponse(VolleyError error) {

                error.printStackTrace();

            }

        });

        int socketTimeout = 30000;

        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        stringRequest.setRetryPolicy(policy);

        requestQueue.add(stringRequest);

    }
}