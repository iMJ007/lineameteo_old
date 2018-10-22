package com.LineaMeteoPremium;


import android.appwidget.AppWidgetManager;
import android.content.ComponentName;

import android.os.Bundle;
import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;

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
import java.util.Collection;
import java.util.HashMap;

import static com.robotemplates.kozuza.BaseApplication.getContext;

public class WidgetConfig extends Activity {

    private Spinner spinner;
    ArrayList<String> Regions = new ArrayList<String>();
    HashMap<String, HashMap<String,String>> Locations = new HashMap<String, HashMap<String,String>>();
    HashMap<String,String> activeLocations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget_config);

        loadData("https://api.myjson.com/bins/9oqqc", this);

        AutoCompleteTextView locationView = (AutoCompleteTextView)
                findViewById(R.id.location_list);
        locationView.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String activeId = activeLocations.get(s.toString());
                if(activeId == null) return;

                int[] ids = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(new ComponentName(getApplication(), LineaMeteoPremium.class));
                LineaMeteoPremium lineaMeteoPremium = new LineaMeteoPremium();
                lineaMeteoPremium.src = "http://retemeteo.lineameteo.it/banner/big.php?ID="+activeId;

                Log.i("LineaMeteo", "activityCall " + lineaMeteoPremium.src);
                lineaMeteoPremium.onUpdate(getContext(), AppWidgetManager.getInstance(getContext()), ids);
            }
        });

        Button btn1 = (Button) findViewById(R.id.widget_config_close);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                moveTaskToBack(true);
            }
        });
    }





    private void loadData(String url, final Activity activity) {


        RequestQueue requestQueue=Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override

            public void onResponse(String response) {
                Log.v("regionJson","onResponse");
                try{

                    JSONObject jsonObject=new JSONObject(response);

                    // 1. Get regions, store in array for region auto suggest
                    JSONArray regions = jsonObject.getJSONArray("regions");

                    for(int i=0;i<regions.length();i++) {
                        Regions.add(regions.getString(i));
                    }

                    // 2. Need hashmap for location lookup:
                    //   Map<Region, Map<LocationName, LocationId>
                    JSONObject locations = jsonObject.getJSONObject("locations");
                    for(int i=0;i<locations.names().length(); i++) {
                        JSONObject region = locations.getJSONObject(locations.names().getString(i));
                        HashMap<String, String> temp = new HashMap<String, String>();
                        for(int j = 0; j < region.names().length(); j++) {
                            temp.put(region.names().getString(j), region.getString(region.names().getString(j)));
                            Log.i("regionJson", "key = " + region.names().getString(j) + " value = " + region.getString(region.names().getString(j)));
                        }
                        Locations.put(locations.names().getString(i), temp);
                    }
                    activeLocations = Locations.get(locations.getJSONObject(locations.names().getString(0)));
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity,
                            android.R.layout.simple_dropdown_item_1line, Regions.toArray(new String[Regions.size()]));
                    AutoCompleteTextView regionView = (AutoCompleteTextView)
                            findViewById(R.id.region_list);
                    regionView.setAdapter(adapter);

                    regionView.addTextChangedListener(new TextWatcher() {

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            activeLocations = Locations.get(s.toString());
                            if(activeLocations == null) return;
                            String[] regions = activeLocations.keySet().toArray(new String[activeLocations.size()]);

                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity,
                                    android.R.layout.simple_dropdown_item_1line, regions);
                            AutoCompleteTextView textView = (AutoCompleteTextView)
                                    findViewById(R.id.location_list);
                            textView.setAdapter(adapter);
                        }
                    });

                }catch (JSONException e){
                    e.printStackTrace();
                }

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
