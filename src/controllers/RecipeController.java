package controllers;
import models.*;
import views. *;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * This class is used to tie together the recipe veiw and model
 */
public class RecipeController {

    /** this method contains the logic for creating a recipe.*/
    public RecipeModel createRecipe(){
        RecipeModel recipe = RecipeView.createRecipe();
        //Scanner myscan = new Scanner(System.in);
        ArrayList<IngredientModel> ingredients = IngredientController.readFromFile();
        recipe.addIngredientAmount(RecipeView.promptForIngredients(ingredients));
        recipe.addNotes(RecipeView.promptForSteps());
        System.out.println(recipe.toString());
        return recipe;
    }
}
