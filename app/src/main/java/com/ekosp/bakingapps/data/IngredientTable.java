package com.ekosp.bakingapps.data;

import android.support.annotation.NonNull;

import com.pushtorefresh.storio.sqlite.queries.Query;


// We suggest to store table meta such as table name, columns names, queries, etc in separate class
// Because it makes code of the Entity itself cleaner and easier to read/understand/support
public class IngredientTable {

    @NonNull
    public static final String TABLE = "ingredient";

    @NonNull
    public static final String COLUMN_ID = "_id";

    /**
     * For example: "artem_zin" without "@"
     */
    @NonNull
    public static final String COLUMN_RECIPE_ID = "recipe_id";

    @NonNull
    public static final String COLUMN_RECIPE_NAME = "recipe_name";

    /**
     * For example: "Check out StorIO — modern API for SQLiteDatabase & ContentResolver #androiddev"
     */
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
