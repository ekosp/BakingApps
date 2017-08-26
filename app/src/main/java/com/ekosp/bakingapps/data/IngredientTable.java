package com.ekosp.bakingapps.data;

import android.support.annotation.NonNull;

import com.pushtorefresh.storio.sqlite.queries.Query;

//Custom code based from library documentation at https://github.com/pushtorefresh/storio
public class IngredientTable {

    @NonNull
    public static final String TABLE = "ingredient";
    @NonNull
    public static final String COLUMN_ID = "_id";
    @NonNull
    public static final String COLUMN_RECIPE_ID = "recipe_id";
    @NonNull
    public static final String COLUMN_RECIPE_NAME = "recipe_name";
    @NonNull
    public static final String COLUMN_INGREDIENT_CONTENT = "ingredient_content";

    // Yep, with StorIO you can safely store queries as objects and reuse them, they are immutable
    @NonNull
    public static final Query QUERY_ALL = Query.builder()
            .table(TABLE)
            .build();

    // This is just class with Meta Data, we don't need instances
    private IngredientTable() {
        throw new IllegalStateException("No instances please");
    }

    @NonNull
    public static String getCreateTableQuery() {
        return "CREATE TABLE " + TABLE + "("
                + COLUMN_ID + " INTEGER NOT NULL PRIMARY KEY, "
                + COLUMN_RECIPE_ID + " TEXT NOT NULL, "
                + COLUMN_RECIPE_NAME + " TEXT NOT NULL, "
                + COLUMN_INGREDIENT_CONTENT + " TEXT NOT NULL"
                + ");";
    }
}
