package com.LineaMeteoPremium;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.RemoteViews;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.transition.Transition;
import com.robotemplates.webviewapp.R;
import com.bumptech.glide.request.target.AppWidgetTarget;

/**
 * Implementation of App Widget functionality.
 */
public class LineaMeteoPremium extends AppWidgetProvider {

    private AppWidgetTarget appWidgetTarget;

    static void updateAppWidget(Context context, RemoteViews remoteViews) {

        ComponentName myWidget = new ComponentName(context, LineaMeteoPremium.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        manager.updateAppWidget(myWidget, remoteViews);


    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),R.layout.linea_meteo_premium);

        AppWidgetTarget awt = new AppWidgetTarget(context, R.id.imageView, remoteViews, appWidgetIds) {
            @Override
            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                super.onResourceReady(resource, transition);
            }
        };

        RequestOptions options = new RequestOptions().
                override(300, 300).placeholder(R.drawable.navigation_header_bg).error(R.drawable.ic_navigation_help);


        // Create an Intent to launch Browser
        Intent intent =
                new Intent(
                        Intent.ACTION_VIEW, Uri.parse("http://google.com")
                );
        PendingIntent pendingIntent =
                PendingIntent.getActivity(context, 0, intent, 0);

        remoteViews.setOnClickPendingIntent(R.id.imageView, pendingIntent);

        Glide.with(context.getApplicationContext())
                .asBitmap()
                .load("http://retemeteo.lineameteo.it/banner/big.php?ID=1")
                .apply(options)
                .into(awt);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

