package com.suxsx.firstservicev3;

import com.suxsx.firstservice.R;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;

public class DimmeWidgetProvider extends AppWidgetProvider {

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int N = appWidgetIds.length;

        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int i=0; i<N; i++) {
            int appWidgetId = appWidgetIds[i];

            // Create an Intent to launch ExampleActivity
            //Intent intent = new Intent(context, ExampleActivity.class);
            //PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            // Get the layout for the App Widget and attach an on-click listener
            // to the button
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.dim_appwidget);
            DimmeCalc exCalcu = new DimmeCalc();            
    		
    		String DIM = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext()).getString("dimmedag", "nodim");
    		int diffInDaysD = 0;
    		
    		if(DIM == "nodim")
    		{
    			views.setTextViewText(R.id.days_to_text_dimWig, "SETT DIMME DATO"); 
    			views.setTextViewText(R.id.days_to__dimWig, "KLIKK HER"); 
    			
    		}
    		
    		else
    		{
    			diffInDaysD = exCalcu.getDiff(DIM);
    			views.setTextViewText(R.id.days_to_text_dimWig, "ANTALL DAGER TIL DIM"); 
    			views.setTextViewText(R.id.days_to__dimWig, String.valueOf(diffInDaysD)); 
    		}
    		
    		Intent intent = new Intent(context, MainActivity.class);
    	    PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
    	    // Get the layout for the App Widget and attach an on-click listener to the button
    	    views.setOnClickPendingIntent(R.id.days_to__dimWig, pendingIntent);

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }
}

