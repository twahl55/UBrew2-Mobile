package controllers;
import models.*;
import views. *;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import logging.*;
import java.util.List;
import java.text.DecimalFormat;
import com.google.gson.Gson;

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
        writeRecipeToFile(recipe);
        System.out.println(recipe.toString());
        return recipe;
    }

    /** controls prompts for getting the recipe strike water*/
    public void calculateStrikeWaterPrompts(){
        ArrayList<RecipeModel> recipes = readRecipesFromFile();
        RecipeModel recipe = RecipeView.selectRecipe(recipes);
        double strikeWater = calculateStrikeWaterEstimation(recipe);
        RecipeView.displayStrikeWater(strikeWater);
    }

    /** this method calculates the abv*/
    public static double calculateAbv(){
        double og = RecipeView.promptForGravity("original");
        double fg = RecipeView.promptForGravity("final");
        double returned =  calculateAbv(og, fg);
        System.out.println("Your abv is : " + returned);
        return returned;
    }

    /* this method calculates the abv based on og and fg*/
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

    /** this method estimates the gravity */
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

    /** write the recipe to a file */
    public boolean writeRecipeToFile(RecipeModel recipe){
        Gson gson = new Gson();
        Boolean success = false;
        String dir = config.Properties.getProperty("recipeFile");
        try(PrintWriter oos =new PrintWriter(new FileOutputStream(dir+"/recipes.dat", true))){
            oos.println(gson.toJson(recipe));
            success=true;
        }
        catch(Exception ex) {
            success = false;
            Logger.writeToLog(ex);
        }
        return success;
    }

    /** Read the recipes from a file */
    public ArrayList<RecipeModel> readRecipesFromFile(){
        Gson gson = new Gson();
        Boolean success = false;
        String dir = config.Properties.getProperty("recipeFile");
        RecipeModel recipe = null;
        ArrayList<RecipeModel>recipes = new ArrayList<RecipeModel>();
        try(Scanner oos =new Scanner(new File(dir+"/recipes.dat"))){
            while(oos.hasNextLine()) {
                recipe = gson.fromJson(oos.nextLine(), RecipeModel.class);
                recipes.add(recipe);
            }
            success=true;
        }
        catch(Exception ex) {
            recipe = null;
            Logger.writeToLog(ex);
        }
        return recipes;
    }

    /** calculate the graing weight */
    public double calculateGrainWeight(RecipeModel recipe){
        double grainWeight = recipe.getRecipeMalts()
                .stream()
                .mapToDouble( r -> r.getAmount())
                .sum();
        System.out.println("Your total Grain Weight is: " + grainWeight);
        return grainWeight;
    }

    /** calculates the strikewater needed with a standard mashthickness of 1.5 */
    public double calculateStrikeWaterEstimation(RecipeModel recipe){
        double mashThickness = 1.5;
        double grainWeight = calculateGrainWeight(recipe);
        double amountOfWaterQuarts = grainWeight * mashThickness;
        double amountOfWaterGallons = amountOfWaterQuarts/4;
        return amountOfWaterGallons;
    }
}
