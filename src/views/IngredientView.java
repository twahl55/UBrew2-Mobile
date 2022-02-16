package views;

import java.util.ArrayList;
import java.util.Scanner;
import models.IngredientModel;
import models.HopModel;
import models.MaltModel;

/**
 * This class handles the views associated with ingredients
 *
 * Author: Tyler Wahl
 * Date: February 15, 2022
 * Course:CS-622
 * */
public class IngredientView {
    /**
     * displays an arrayList of ingredients
     */
    public static void display(ArrayList<IngredientModel> ingredients, String ingredientType) {
        System.out.println(ingredientType + ": ");
        for(IngredientModel ingredient: ingredients) {

            String output = ingredient.getName();
            if (ingredientType == "Hops") {
                output += " | Alpha Acid: " + ((HopModel) ingredient).getAlphaAcid();
            } else {
                output += " | Extract Potential: " + ((MaltModel) ingredient).getExtractPotential();
            }
            output+= " | Unit of Measure: " + ingredient.getUnit();
            System.out.println(output);
        }
    }

    /** Displays a list of ingredients with the addition of a number you can use to choose it */
    public static void displayWithSelector(ArrayList<IngredientModel> ingredients) {
        //System.out.println(ingredientType + ": ");
        int selector = 0;
        for(IngredientModel ingredient: ingredients) {
            selector++;
            String output = selector + " " + ingredient.getName();
            if (ingredient instanceof HopModel) {
                output += " | Alpha Acid: " + ((HopModel) ingredient).getAlphaAcid();
            } else {
                output += " | Extract Potential: " + ((MaltModel) ingredient).getExtractPotential();
            }
            output+= " | Unit of Measure: " + ingredient.getUnit();
            System.out.println(output);
        }
    }

    /**
     * creates the output for creating a new output and returns the appropriate ingredient model.
     */
    public static IngredientModel createIngredient(){
        System.out.println("Add an ingredient:");
        System.out.println("\t Type(h for Hop, M for Malt): ");
        Scanner myScan = new Scanner(System.in).useDelimiter("\\n");
        String type = myScan.next();
        Object model = null;
        System.out.println("\t Name: ");
        String name = myScan.next();
        if(type.toLowerCase().equals("h") || type.toLowerCase().equals("hop")){
            System.out.println("\t Alpha Acid %(between 0 and 100): ");
            double aa = Double.parseDouble(myScan.next());
            model = new HopModel(name, aa);
        }
        else{
            System.out.println("\t Extract Potential: ");
            double potential = Double.parseDouble(myScan.next());
            model = new MaltModel(name, potential);
        }
        return (IngredientModel) model;

    }
}
