package com.ekosp.bakingapps.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService.RemoteViewsFactory;

import com.ekosp.bakingapps.R;

import java.util.ArrayList;
import java.util.Random;

/**
 * If you are familiar with Adapter of ListView,this is the same as adapter
 * with few changes
 * 
 */
public class ListProvider implements RemoteViewsFactory {
	private ArrayList<ListItem> listItemList = new ArrayList<ListItem>();
	private Context context = null;
	private int appWidgetId;
    private String mRecipeId;

	public ListProvider(Context context, Intent intent, String indexRecipeId) {
		this.context = context;
		appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
				AppWidgetManager.INVALID_APPWIDGET_ID);
        mRecipeId = indexRecipeId;

		populateListItem(mRecipeId);
	}

	private void populateListItem(String recipeId) {

        // load ingredient recipe from recipe woth id = recipeId

		for (int i = 0; i < 10; i++) {
			ListItem listItem = new ListItem();
			listItem.quantity = recipeId; //String.format("%03d", (new Random().nextInt(900) + 100));;
            listItem.measure = "CUP";
			listItem.ingredient = "Graham Cracker crumbs";
			listItemList.add(listItem);
		}

	}

	@Override
	public int getCount() {
		return listItemList.size();
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	/*
	 *Similar to getView of Adapter where instead of View
	 *we return RemoteViews 
	 * 
	 */
	@Override
	public RemoteViews getViewAt(int position) {
		final RemoteViews remoteView = new RemoteViews(
				context.getPackageName(), R.layout.list_row);
		ListItem listItem = listItemList.get(position);
		remoteView.setTextViewText(R.id.quantityText, listItem.quantity);
        remoteView.setTextViewText(R.id.measureText, listItem.measure);
		remoteView.setTextViewText(R.id.ingredientText, listItem.ingredient);

		return remoteView;
	}
	

	@Override
	public RemoteViews getLoadingView() {
		return null;
	}

	@Override
	public int getViewTypeCount() {
		return 1;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public void onCreate() {
	}

	@Override
	public void onDataSetChanged() {
	}

	@Override
	public void onDestroy() {
	}

}
