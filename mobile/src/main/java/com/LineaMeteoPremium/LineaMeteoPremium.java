package com.LineaMeteoPremium;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RemoteViews;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.robotemplates.webviewapp.R;
import com.bumptech.glide.request.target.AppWidgetTarget;

/**
 * Implementation of App Widget functionality.
 */
public class LineaMeteoPremium extends AppWidgetProvider {

    private AppWidgetTarget appWidgetTarget;
    public String src;
    private SharedPreferences prefs;
    private String srcKey = "com.example.app.datetime";

    static void updateAppWidget(Context context, RemoteViews remoteViews, int appWidgetId) {

        ComponentName myWidget = new ComponentName(context, LineaMeteoPremium.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        manager.updateAppWidget(myWidget, remoteViews);


    }

    private void init(Context context) {

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        prefs = context.getSharedPreferences(
                "com.LineaMeteoPremium", Context.MODE_PRIVATE);

        for (int widgetId : appWidgetIds) {


            setUpAlarm(context, widgetId);

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.linea_meteo_premium);

            Log.e("LineaMeteo Widget", "src " + src);

            if(TextUtils.isEmpty(src)) {
                src = prefs.getString(srcKey, "http://retemeteo.lineameteo.it/banner/big.php?ID=1");
            } else {
                prefs.edit().putString(srcKey, src).apply();
            }

            AppWidgetTarget awt = new AppWidgetTarget(context, R.id.imageView, remoteViews, appWidgetIds);

            RequestOptions options = new RequestOptions().
                    override(300, 300).placeholder(R.drawable.navigation_header_bg).error(R.drawable.ic_navigation_help).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE);


            // Create an Intent to launch Browser
            Intent intent = new Intent(context, WidgetConfig.class);

            PendingIntent pendingIntent =
                    PendingIntent.getActivity(context, 0, intent, 0);


            remoteViews.setOnClickPendingIntent(R.id.imageView, pendingIntent);

            Glide.with(context.getApplicationContext())
                    .asBitmap()
                    .load(src)
                    .apply(options)
                    .into(awt);
        }



    }

    @Override
    public void onEnabled(Context context) {


    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getAction()) {
            case AppWidgetManager.ACTION_APPWIDGET_UPDATE:
                onUpdate(context, AppWidgetManager.getInstance(context), AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context, LineaMeteoPremium.class)));
                break;
            default:
                super.onReceive(context, intent);
        }
    }

    /**
     * Sets up widget refresh alarm
     *
     * @param context     The context of widget
     * @param appWidgetId Widget ID of current widget
     */
    private void setUpAlarm(Context context, int appWidgetId) {
        final AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        long interval = (3 * 60000); // 60000 = 1minute  | Change the formula based on your refresh timing needs

        PendingIntent alarmPendingIntent = getRefreshWidgetPendingIntent(context, appWidgetId);

        alarm.cancel(alarmPendingIntent);

        // SET NEW ALARM
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarm.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + interval, alarmPendingIntent);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarm.setExact(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + interval, alarmPendingIntent);
        } else {
            alarm.set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + interval, alarmPendingIntent);
        }
    }

    /**
     * Get the pending intent object for refreshing the widget
     *
     * @param context  current Context
     * @param widgetId - Current Widget ID
     * @return - Pending intent for refreshing widget
     */
    private PendingIntent getRefreshWidgetPendingIntent(Context context, int widgetId) {
        Intent intent = new Intent(context, LineaMeteoPremium.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
        // Make the pending intent unique...
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        return PendingIntent.getBroadcast(context, 123, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}

