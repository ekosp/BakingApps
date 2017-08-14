package com.ekosp.bakingapps.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import com.ekosp.bakingapps.R;
import com.ekosp.bakingapps.data.DbOpenHelper;
import com.ekosp.bakingapps.data.IngredientData;
import com.ekosp.bakingapps.data.IngredientDataSQLiteTypeMapping;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.impl.DefaultStorIOSQLite;
import com.pushtorefresh.storio.sqlite.queries.Query;

import java.util.List;
import java.util.Random;

public class WidgetProvider extends AppWidgetProvider {

	/* 
	 * this method is called every 30 mins as specified on widgetinfo.xml
	 * this method is also called on every phone reboot
	 */
	public static String PARAM_RECIPE_ID = "PARAM_RECIPE_ID";
    private int mRecipeIndex ;
	private StorIOSQLite storIOSQLite;

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
            String number = String.valueOf( rand.nextInt(4) + 1);

            mRecipeIndex = 0;
            //svcIntent.putExtra(PARAM_RECIPE_ID, String.valueOf(mRecipeIndex));
            svcIntent.putExtra(PARAM_RECIPE_ID, number);
            svcIntent.setData(Uri.parse(svcIntent.toUri(Intent.URI_INTENT_SCHEME)));

			// only to get recipe name to put on widget title
			storIOSQLite = DefaultStorIOSQLite.builder()
					.sqliteOpenHelper(new DbOpenHelper(context))
					.addTypeMapping(IngredientData.class, new IngredientDataSQLiteTypeMapping())
					.build();

			List<IngredientData> receivedIngredient = storIOSQLite
					.get()
					.listOfObjects(IngredientData.class)
					.withQuery(Query.builder()
							.table("ingredient")
							.where("recipe_id = ?")
							.whereArgs(number)
							.build())
					.prepare()
					.executeAsBlocking();

			Cursor cursor = storIOSQLite
					.get()
					.cursor()
					.withQuery(Query.builder() // Or RawQuery
							.table("ingredient")
							.where("recipe_id = ?")
							.whereArgs(number)
							.build())
					.prepare()
					.executeAsBlocking();

			cursor.moveToFirst();
			String recipeName = cursor.getString(cursor.getColumnIndex("recipe_name"));

            remoteViews.setRemoteAdapter(appWidgetId, R.id.listViewWidget,
                    svcIntent);

            // update onClick
            int widgetId = appWidgetIds[i];

           remoteViews.setTextViewText(R.id.recipeText, recipeName);
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
