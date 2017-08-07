package com.ekosp.bakingapps.helper;

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


}
