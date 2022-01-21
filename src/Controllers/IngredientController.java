package Controllers;
import Models.*;
import Views.*;

import java.util.ArrayList;
/** This class controls interactions between ingredient model and ingredient view */
public class IngredientController {
    public void listAll(){
        ArrayList<IngredientModel> ingredients = new ArrayList<IngredientModel>();
        ingredients.add(new HopModel("Cascade Hops", 5.2));
        ingredients.add( new MaltModel("Munich Malt", 75.0));
        ingredients.add(new MaltModel("Special B", 68 ));
        ingredients.add(new MaltModel("Wheat Malt" , 79));
        ingredients.add(new HopModel("Citra" , 3.1));
        ArrayList<IngredientModel> hops = new ArrayList<IngredientModel>();
        ArrayList<IngredientModel> malts = new ArrayList<IngredientModel>();
        for(IngredientModel ingredient: ingredients) {
            if (ingredient instanceof HopModel) {
                hops.add((HopModel) ingredient);
            } else {
                malts.add((MaltModel) ingredient);
            }
        }
        IngredientView.display(malts, "Malts");
        IngredientView.display(hops, "Hops");

    }
}
