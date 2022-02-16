package models;

import javax.naming.directory.InvalidAttributesException;
import java.util.*;
import logging.*;
import java.io.Serializable;
/**
 * A class for representing a recipe.
 *
 * Author: Tyler Wahl
 * Date: February 15, 2022
 * Course:CS-622
 * */
public class RecipeModel implements Serializable{

    private String name;
    private float abv;
    private float ibu;
    private float og;
    private float fg;
    //private HashMap<String, Float > ingredientAmounts = new HashMap<String,Float>();
    private List<String> steps = new ArrayList<String>();
    private List<RecipeIngredient<MaltModel>> recipeMalts = new ArrayList<RecipeIngredient<MaltModel>>();
    private List<RecipeIngredient<HopModel>> recipeHops = new ArrayList<RecipeIngredient<HopModel>>();

    /** retrieves the name of the recipe */
    public String getName(){
        return this.name;
    }

    /** set the name of the recipe */
    public void setName(String inputName){
        try {
            if (inputName.isEmpty()) {
                throw new InvalidAttributesException("Must have a name");
            }
            this.name = inputName;
        }
        catch(Exception e){
            System.out.println(e);
            logging.Logger.writeToLog(e);
        }
        return;
    }

    /**retrieves the abv */
    public float getAbv(){
        return this.abv;
    }


    /**sets the abv **/
    public void setAbv(float abv){
        this.abv=  abv;
        return;
    }


    /**retrieves the Ibu rating */
    public float getIbu(){
        return this.ibu;
    }

    /** sets the ibu(international Bitterness unit ) */
    public void setIbu(float ibu){
        this.ibu=  ibu;
        return;
    }

    /** retrieves the orginal gravity(og) */
    public float getOg(){
        return this.og;
    }


    /** set the original gravity(og) */
    public void setOg(float og){
        this.og=  og;
        return;
    }

    /**retrieve the final gravity */
    public float getFg(){
        return this.fg;
    }

    /**set the final gravity(fg) */
    public void setFg(float fg){
        this.fg=  fg;
        return;
    }

    /**retrieves the hashmap of ingredientAmounts */
    public  List<RecipeIngredient<MaltModel>> getRecipeMalts(){

        return this.recipeMalts;
    }
    public  List<RecipeIngredient<HopModel>> getRecipeHops(){

        return this.recipeHops;
    }

    /** Add the ingredient and amount to a recipe */
    public void addMaltAmount(MaltModel ingredient, Float ingredientAmount){
        RecipeIngredient<MaltModel> ingr = new RecipeIngredient<MaltModel>();
        ingr.setIngredient(ingredient);
        ingr.setAmount(ingredientAmount);
        recipeMalts.add(ingr);
        return;
    }

    /** Add the ingredient and amount to a recipe */
    public void addHopAmount(HopModel ingredient, Float ingredientAmount){
        RecipeIngredient<HopModel> ingr = new RecipeIngredient<HopModel>();
        ingr.setIngredient(ingredient);
        ingr.setAmount(ingredientAmount);
        recipeHops.add(ingr);
        return;
    }

    /** Add the ingredient and amount to a recipe */
    public void addIngredientAmount(HashMap<IngredientModel, Float > ingredients){
        try{
            for(IngredientModel key: ingredients.keySet()){
                if(key instanceof MaltModel){
                    addMaltAmount((MaltModel)key, ingredients.get(key));
                }
                if(key instanceof HopModel){
                    addHopAmount((HopModel)key, ingredients.get(key));
                }
            }
        }
        catch(Exception ex){
            Logger.writeToLog(400, ex.getMessage());
        }
        //recipeIngredients.putAll(ingredients);
        return;
    }
    /** Add the notes a recipe */
    public void addNotes(ArrayList<String> new_steps){
        this.steps.addAll(new_steps);
        return;
    }

    @Override
    public String toString(){
        String output = "Name: " + this.name +" | ABV: " + this.abv + " | IBU: " + this.ibu + " | OG: " + this.og +
                " | FG: " + this.fg + " | ingredients: " ;
        int counter = 1;
        output += " Malts:  ";
        for(RecipeIngredient<MaltModel> malt : recipeMalts){
            output+= " " + counter + ". Ingredient: " + malt.getIngredient().getName() + " Amount: " +
                    malt.getAmount() + " ";
            counter ++;
        }
        counter =1;
        output+= " Hops:  ";
        for(RecipeIngredient<HopModel> hop : recipeHops){
            output+= " " + counter + ". Ingredient: " + hop.getIngredient().getName() + " Amount: " +
                    hop.getAmount() + " ";
            counter ++;
        }
        output+= " | Steps: ";
        counter = 1;
        for(String step : steps){
            output+= " " + counter + ". " + step;
            counter ++;
        }
        return output;
    }
}
