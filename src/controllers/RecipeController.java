package controllers;
import models.*;
import views. *;
import java.util.ArrayList;
import java.util.Scanner;
import logging.*;
import java.util.List;
import java.text.DecimalFormat;

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

    public static double calculateAbv(){
        double og = RecipeView.promptForGravity("original");
        double fg = RecipeView.promptForGravity("final");
        double returned =  calculateAbv(og, fg);
        System.out.println("Your abv is : " + returned);
        return returned;
    }

    public static double calculateAbv(double og , double fg ){
        double result = 0.0;

        try{
            DecimalFormat df = new DecimalFormat("###.##");
            result = (og-fg)*131.25;
            result  =Double.valueOf(df.format(result));
        }
        catch(ArithmeticException ae){
            Logger.writeToLog(400, "ArithmeticException " + ae.getMessage());
        }
        catch(Exception ex){
            Logger.writeToLog(ex);
        }
        return  result;
    }

    public double gravity_estimation( double efficiency, double volume){
        ArrayList<IngredientModel> ingredients = IngredientController.readFromFile();
        ArrayList<IngredientModel> malts = new ArrayList<IngredientModel>();
        for(IngredientModel ingr: ingredients){
            if( ingr instanceof MaltModel){
                malts.add(ingr);
            }
        }
        RecipeModel recipe = new RecipeModel();
        recipe.addIngredientAmount(RecipeView.promptForIngredients(malts));
        double result = 0;
        double sugar_points = 0;
        try {
            if(efficiency > 1){efficiency = efficiency/100;}
            for (RecipeIngredient<MaltModel> m : recipe.getRecipeMalts()) {
                double amount = m.getAmount();
                double potential = ((MaltModel)(m.getIngredient())).getExtractPotential();
                sugar_points += amount * (potential - 1) * 1000;
            }
            double efficiency_points = sugar_points * efficiency;
            result = 1 + (efficiency_points / volume) / 1000;
            System.out.println("Your gravity estimation is :" + result);
        }
        catch(Exception ex){
            Logger.writeToLog(ex);
        }
        return result;
    }
}
