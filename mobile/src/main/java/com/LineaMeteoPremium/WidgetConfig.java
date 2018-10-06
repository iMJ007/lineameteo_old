package com.LineaMeteoPremium;


import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.robotemplates.webviewapp.R;

public class WidgetConfig extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget_config);

        Spinner spinner = findViewById(R.id.WidgetConfigSpinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                // An item was selected. You can retrieve the selected item using
                // parent.getItemAtPosition(pos)

                int[] ids = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(new ComponentName(getApplication(), LineaMeteoPremium.class));
                LineaMeteoPremium lineaMeteoPremium = new LineaMeteoPremium();
                lineaMeteoPremium.src = "http://retemeteo.lineameteo.it/banner/big.php?ID=1543";
                Log.i("LineaMeteo", "activityCall " + lineaMeteoPremium.src);
                lineaMeteoPremium.onUpdate(parent.getContext(), AppWidgetManager.getInstance(parent.getContext()), ids);


            }

            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });
    }
}