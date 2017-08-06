package com.ekosp.bakingapps.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by eko.purnomo on 28/07/2017.
 * You can contact me at : ekosetyopurnomo@gmail.com
 * or for more detail at  : http://ekosp.com
 */

public class Recipe implements Parcelable {

    @SerializedName("id")
    private long id;
    @SerializedName("name")
    private String name;
    @SerializedName("servings")
    private String servings;
    @SerializedName("image")
    private String image;
    @SerializedName("ingredients")
    private List<Ingredients> ingredientList;
    @SerializedName("steps")
    private List<Steps> stepList;

    public List<Ingredients> getIngredientList() {
        return ingredientList;
    }

    public void setIngredientList(List<Ingredients> ingredientList) {
        this.ingredientList = ingredientList;
    }

    public List<Steps> getStepList() {
        return stepList;
    }

    public void setStepList(List<Steps> stepList) {
        this.stepList = stepList;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getServings() {
        return servings;
    }

    public void setServings(String servings) {
        this.servings = servings;
    }

    protected Recipe(Parcel in) {
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
