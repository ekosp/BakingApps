package com.ekosp.bakingapps.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import com.ekosp.bakingapps.R;

import java.util.Random;

public class WidgetProvider extends AppWidgetProvider {

	/* 
	 * this method is called every 30 mins as specified on widgetinfo.xml
	 * this method is also called on every phone reboot
	 */
	public static String PARAM_RECIPE_ID = "PARAM_RECIPE_ID";
    private int mRecipeIndex ;

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		final int N = appWidgetIds.length;
        /*int[] appWidgetIds holds ids of multiple instance of your widget
		meaning you are placing more than one widgets on your homescreen*/
		for (int i = 0; i < N; ++i) {
            // update get list
			int appWidgetId = appWidgetIds[i];
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                    R.layout.widget_layout);
          Intent svcIntent = new Intent(context, WidgetService.class);

            svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);

			Random rand = new Random();
            String number = String.valueOf( rand.nextInt(3) + 0);

            mRecipeIndex = 0;
            //svcIntent.putExtra(PARAM_RECIPE_ID, String.valueOf(mRecipeIndex));
            svcIntent.putExtra(PARAM_RECIPE_ID, number);
            svcIntent.setData(Uri.parse(svcIntent.toUri(Intent.URI_INTENT_SCHEME)));

            remoteViews.setRemoteAdapter(appWidgetId, R.id.listViewWidget,
                    svcIntent);

            // update onClick
            int widgetId = appWidgetIds[i];

           remoteViews.setTextViewText(R.id.recipeText, number);
            remoteViews.setEmptyView(R.id.listViewWidget, R.id.empty_view);




            Intent intent = new Intent(context, WidgetProvider.class);


            // coba2 set update list disini
            /*intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            mRecipeIndex = 0;
            intent.putExtra(PARAM_RECIPE_ID, mRecipeIndex);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
            remoteViews.setRemoteAdapter(appWidgetId, R.id.listViewWidget,
                    intent);*/

            // ini aslinya random number
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                    0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            remoteViews.setOnClickPendingIntent(R.id.actionButton, pendingIntent);
            // go update widget view
            appWidgetManager.updateAppWidget(widgetId, remoteViews);

		}
		super.onUpdate(context, appWidgetManager, appWidgetIds);


	}

	private RemoteViews updateWidgetListView(Context context, int appWidgetId) {

		RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
				R.layout.widget_layout);
		Intent svcIntent = new Intent(context, WidgetService.class);
		svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        mRecipeIndex = 0;
        svcIntent.putExtra(PARAM_RECIPE_ID, mRecipeIndex);
		svcIntent.setData(Uri.parse(svcIntent.toUri(Intent.URI_INTENT_SCHEME)));

			remoteViews.setRemoteAdapter(appWidgetId, R.id.listViewWidget,
				svcIntent);
		remoteViews.setEmptyView(R.id.listViewWidget, R.id.empty_view);
		remoteViews.setTextViewText(R.id.recipeText,"Nuttela Pie");
		return remoteViews;
	}

}
