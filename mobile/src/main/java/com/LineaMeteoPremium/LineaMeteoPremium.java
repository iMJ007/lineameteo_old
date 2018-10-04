package com.LineaMeteoPremium;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.ImageView;
import android.widget.RemoteViews;
import com.robotemplates.webviewapp.R;
import com.squareup.picasso.Picasso;

/**
 * Implementation of App Widget functionality.
 */
public class LineaMeteoPremium extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.linea_meteo_premium);
        ImageView imageView;
        imageView = new ImageView(context);
        Picasso.get().load("http://retemeteo.lineameteo.it/banner/big.php?ID=1").into(imageView);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
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

