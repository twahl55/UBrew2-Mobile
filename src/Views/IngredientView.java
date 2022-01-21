package Views;
import Models.*;

import java.util.ArrayList;
/**This class handles the views associated with ingredients */
public class IngredientView {
    public static void display(ArrayList<IngredientModel> ingredients, String ingredeintType) {
        System.out.println(ingredeintType + ": ");
        for(IngredientModel ingredient: ingredients) {

            String output = ingredient.getName();
            if (ingredeintType == "Hops") {
                output += " | Alpha Acid: " + ((HopModel) ingredient).getAlphaAcid();
            } else {
                output += " | Extract Potential: " + ((MaltModel) ingredient).getExtractPotential();
            }
            output+= " | Unit of Measure: " + ingredient.getUnit();
            System.out.println(output);
        }
    }
}
