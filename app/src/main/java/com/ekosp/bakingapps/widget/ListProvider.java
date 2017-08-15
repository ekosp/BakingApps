package com.ekosp.bakingapps.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService.RemoteViewsFactory;

import com.ekosp.bakingapps.R;
import com.ekosp.bakingapps.data.DbOpenHelper;
import com.ekosp.bakingapps.data.IngredientData;
import com.ekosp.bakingapps.data.IngredientDataSQLiteTypeMapping;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.impl.DefaultStorIOSQLite;
import com.pushtorefresh.storio.sqlite.queries.Query;

import java.util.ArrayList;
import java.util.List;

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
	private StorIOSQLite storIOSQLite;

	public ListProvider(Context context, Intent intent, String indexRecipeId) {
		this.context = context;
		appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
				AppWidgetManager.INVALID_APPWIDGET_ID);
        mRecipeId = indexRecipeId;

		populateListItem(mRecipeId);
	}

	private void populateListItem(String recipeId) {

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
                        .whereArgs(recipeId)
                        .build())
                .prepare()
                .executeAsBlocking();

        // set selected recipe list to widget list
        for (int i = 0; i < receivedIngredient.size(); i++) {
            ListItem listItem = new ListItem();
            listItem.ingredient = receivedIngredient.get(i).ingredient_content();
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
				context.getPackageName(), R.layout.list_widget_row);
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
