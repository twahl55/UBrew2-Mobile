package controllers;
import models.*;
import views. *;

import java.io.*;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Scanner;
import logging.*;
import java.util.List;
import java.text.DecimalFormat;
import com.google.gson.Gson;
import java.util.concurrent.Callable;
import java.util.concurrent.*;

/**
 * This class is used to tie together the recipe veiw and model
 *
 * Author: Tyler Wahl
 * Date: February 15, 2022
 * Course:CS-622
 * */
public class RecipeController{

    /** this method contains the logic for creating a recipe.*/
    public RecipeModel createRecipe(UserModel user){
        RecipeModel recipe = RecipeView.createRecipe();
        ArrayList<IngredientModel> ingredients = IngredientController.getIngredientsFromDB("");
        recipe.addIngredientAmount(RecipeView.promptForIngredients(ingredients));
        recipe.addNotes(RecipeView.promptForSteps());
        boolean success = addRecipeToDB(recipe,user.getUserId() );
        System.out.println(recipe);
        return recipe;
    }
/** adds recipe to the database*/
    public boolean addRecipeToDB(RecipeModel recipe, String userId){
        boolean success = false;
        int recipeId = 0;
        try{
            recipeId = (new DBHelper()).createRecipe(recipe, userId );
            recipe.setId(recipeId);
            addRecipeIngredients(recipe);
            success = true;
        }
        catch(Exception e){
            Logger.writeToLog(e);
            success = false;
        }
        return success;
    }

    /**Add recipeIngredients appropriately */
    public void addRecipeIngredients(RecipeModel recipe){
        try{
            (new DBHelper()). addRecipeIngredientsToDB(recipe);
        }
        catch(Exception ex){
            Logger.writeToLog(ex);
        }
    }

    /** controls prompts for getting the recipe strike water*/
    public void calculateStrikeWaterPrompts(){
        ArrayList<RecipeModel> recipes = readRecipesFromDB("");
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

    public Double gravity_estimation(List<RecipeIngredient<MaltModel>> malts, double efficiency, double volume){
        double result =  0;
        double sugar_points = 0;
        try {
            if(efficiency > 1){efficiency = efficiency/100;}
            for (RecipeIngredient<MaltModel> m : malts) {
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
   /* public ArrayList<RecipeModel> readRecipesFromFile(){
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
    }*/

    /** reads the recipes from the dtabase */
    public static ArrayList<RecipeModel> readRecipesFromDB(String userId){
        // Gson gson = new Gson();
        Boolean success = false;
        RecipeModel recipe = null;
        ArrayList<RecipeModel>recipes = new ArrayList<RecipeModel>();
        String query = "SELECT \"Name\", \"ABV\", \"IBU\", \"OG\", \"FG\", \"styleId\",  id FROM recipes where public = true OR" +
                " 'userId' ='" + userId + "'";
        try {
            recipes = (new DBHelper()).getRecipes(query);
        }
        catch(Exception ex) {
            recipe = null;
            Logger.writeToLog(ex);
        }
        return recipes;
    }

    /** get recipe by recipeid */
    public RecipeModel getRecipeById(int recipeId){
        String query = "Select \"Name\", \"ABV\", \"IBU\", \"OG\", \"FG\", \"styleId\", public FROM Recipes WHERE id=?";
        RecipeModel recipe = null;
        try{

            DBHelper helper = new DBHelper();
           recipe = (new DBHelper()).getRecipeById(query, recipeId);
            List<RecipeIngredient<MaltModel>> malts = helper.getRecipeMaltsByRecipeId(recipeId);
            List<RecipeIngredient<HopModel>> hops = helper.getRecipeHopsByRecipeId(recipeId);
            recipe.addRecipeHops(hops);
            recipe.addRecipeMalts(malts);
            System.out.println(recipe.toString());
        }
        catch(Exception e){
            Logger.writeToLog(e);
        }
        return recipe;

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

    /** calculate the international bitterness units */
    public Double calculateIbu(List<RecipeIngredient<HopModel>> hops,double originalGravity, double volume){
        double result = 0.0;

        for(RecipeIngredient<HopModel> hop :hops){
            result+=(hop.getAmount()*((HopModel)(hop.getIngredient())).getAlphaAcid() *18.8)/7.25;
        }
        return result;
    }

    /** Caclulate all the calculatable fields for the recipe */
    public RecipeModel calculateAll(RecipeModel recipe){
        double originalGravity = gravity_estimation( recipe.getRecipeMalts(), 65, 5);
        ExecutorService service = Executors.newFixedThreadPool(5);
        Callable<Double> ibuThread =()->{
            return calculateIbu( recipe.getRecipeHops(), originalGravity,5);};
        Future<Double>  future2 = service.submit(ibuThread);
        Callable<Double> abvThread = ()->{
            return (Double)calculateAbv(recipe.getOg(), recipe.getFg());
        };
        Future<Double> future3 = service.submit(abvThread);

        recipe.setOg((float)originalGravity);
        try {
            recipe.setIbu(future2.get().floatValue());
        }catch(Exception ex){
            Logger.writeToLog(ex);
        }
        try {
            recipe.setAbv(future3.get().floatValue());
        }
        catch(Exception ex) {
            Logger.writeToLog((ex));
        }
        return recipe;
    }

    /**Perform all logic around calculations and retrieval or recipe for calculations */
    public void RecipeCalculations(UserModel user){
        ArrayList<RecipeModel> recipes = readRecipesFromDB(user.getUserId());
        RecipeModel recipe = RecipeView.selectRecipe(recipes);
        recipe = getRecipeById(recipe.getId());
        System.out.println("Recipe Before Re Calculations");
        RecipeView.printRecipe(recipe);
        recipe = calculateAll(recipe);
        System.out.println("Recipe After Calcutations");
        RecipeView.printRecipe(recipe);

    }
}
