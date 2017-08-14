package com.ekosp.bakingapps.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteType;

//Custom code based from library documentation at https://github.com/pushtorefresh/storio
@StorIOSQLiteType(table = IngredientTable.TABLE)
public class IngredientData {

    /**
     * If object was not inserted into db, id will be null
     */
    @Nullable
    @StorIOSQLiteColumn(name = IngredientTable.COLUMN_ID, key = true)
    Long id;

    @NonNull
    @StorIOSQLiteColumn(name = IngredientTable.COLUMN_RECIPE_ID)
    String recipe_id;

    @NonNull
    @StorIOSQLiteColumn(name = IngredientTable.COLUMN_RECIPE_NAME)
    String recipe_name;

    @NonNull
    @StorIOSQLiteColumn(name = IngredientTable.COLUMN_INGREDIENT_CONTENT)
    String ingredient_content;

    IngredientData() {
    }

    private IngredientData(@Nullable Long id, @NonNull String recipeId, @NonNull String recipeName, @NonNull String ingredientContent) {
        this.id = id;
        this.recipe_id = recipeId;
        this.recipe_name = recipeName;
        this.ingredient_content = ingredientContent;
    }

    @NonNull
    public static IngredientData newIngredient(@Nullable Long id, @NonNull String recipeId,@NonNull String recipeName, @NonNull String ingredientContent) {
        return new IngredientData(id, recipeId, recipeName ,ingredientContent);
    }

    @NonNull
    public static IngredientData newIngredient(@NonNull String recipeId, @NonNull String recipeName, @NonNull String ingredientContent) {
        return new IngredientData(null, recipeId, recipeName, ingredientContent);
    }

    @Nullable
    public Long id() {
        return id;
    }

    @NonNull
    public String recipe_id() {
        return recipe_id;
    }

    @NonNull
    public String recipe_name() {
        return recipe_name;
    }

    @NonNull
    public String ingredient_content() {
        return ingredient_content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IngredientData tweet = (IngredientData) o;

        if (id != null ? !id.equals(tweet.id) : tweet.id != null) return false;
        if (!recipe_id.equals(tweet.recipe_id)) return false;
        if (!recipe_name.equals(tweet.recipe_name)) return false;
        return ingredient_content.equals(tweet.ingredient_content);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + recipe_id.hashCode();
        result = 31 * result + recipe_name.hashCode();
        result = 31 * result + ingredient_content.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "IngredientData{" +
                "id=" + id +
                ", recipe_id='" + recipe_id + '\'' +
                ", recipe_name='" + recipe_name + '\'' +
                ", ingredient_content='" + ingredient_content + '\'' +
                '}';
    }
}
