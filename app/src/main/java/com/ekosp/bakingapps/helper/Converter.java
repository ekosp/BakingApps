package com.ekosp.bakingapps.helper;

import android.text.Html;
import android.text.Spanned;

import com.ekosp.bakingapps.models.Ingredients;

import java.util.List;

/**
 * Created by eko.purnomo on 07/08/2017.
 * You can contact me at : ekosetyopurnomo@gmail.com
 * or for more detail at  : http://ekosp.com
 */

public class Converter {

    public static String IngredientToString(List<Ingredients> ingredients){
        StringBuilder sb = new StringBuilder();
        for (Ingredients igr : ingredients){
            sb.append(igr.getQuantity()+" "+igr.getMeasure()+" "+igr.getIngredient()+"\n");
        }
        return sb.toString();
    }

    public static Spanned IngredientToHtmlAsString(List<Ingredients> ingredients){
        StringBuilder sb = new StringBuilder();
        for (Ingredients igr : ingredients){
            sb.append(igr.getQuantity()+" "+igr.getMeasure()+" "+igr.getIngredient()+"\n");
        }

        // get our html content
        String htmlAsString = sb.toString();
        Spanned htmlAsSpanned = Html.fromHtml(htmlAsString); // used by TextView

        return htmlAsSpanned;
    }







}
