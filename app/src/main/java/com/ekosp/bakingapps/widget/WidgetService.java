package com.ekosp.bakingapps.widget;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViewsService;

public class WidgetService extends RemoteViewsService {
	/*
	 * So pretty simple just defining the Adapter of the listview
	 * here Adapter is ListProvider
	 * */

	@Override
	public RemoteViewsFactory onGetViewFactory(Intent intent) {
		int appWidgetId = intent.getIntExtra(
				AppWidgetManager.EXTRA_APPWIDGET_ID,
				AppWidgetManager.INVALID_APPWIDGET_ID);

		String indexRecipe = intent.getStringExtra(WidgetProvider.PARAM_RECIPE_ID);
		Log.i("WidgetService", "indexRecipe :"+indexRecipe);

		return (new ListProvider(this.getApplicationContext(), intent, indexRecipe));
	}

}
