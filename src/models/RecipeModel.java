package models;

import javax.naming.directory.InvalidAttributesException;
import java.util.*;
import logging.*;

/**
 * A class for representing a recipe.
 */
public class RecipeModel {

    private String name;
    private float abv;
    private float ibu;
    private float og;
    private float fg;
    private HashMap<String, Float > ingredientAmounts = new HashMap<String,Float>();
    private List<String> steps = new ArrayList<String>();

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
    public  HashMap<String, Float> getIngredientAmounts(){
        return this.ingredientAmounts;
    }

    /** Add the ingredient and amount to a recipe */
    public void addIngredientAmount(String ingredientName, Float ingredientAmount){
        ingredientAmounts.put(ingredientName, ingredientAmount);
        return;
    }

    /** Add the ingredient and amount to a recipe */
    public void addIngredientAmount(HashMap<String, Float > ingredients){
        ingredientAmounts.putAll(ingredients);
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
        for(String key : ingredientAmounts.keySet()){
            output+= " " + counter + ". Ingredient: " + key + " Amount: " + ingredientAmounts.get(key) + " ";
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
