package com.suxsx.firstservicev3;

import com.suxsx.firstservice.R;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.widget.ListView;
import android.widget.RemoteViews;

public class ForkoWidgetProvider extends AppWidgetProvider {
	
	// List view
    private ListView lv;

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int N = appWidgetIds.length;

        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int i=0; i<N; i++) {
            int appWidgetId = appWidgetIds[i];


            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.forko_appwidget);
 
    		Intent intent = new Intent(context, MainActivity.class);
    	    PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
    	    // Get the layout for the App Widget and attach an on-click listener to the button
    	    views.setOnClickPendingIntent(R.id.forkoIMGWIG, pendingIntent);

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }
}

