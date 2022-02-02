package views;
import models.*;
import logging.*;

import javax.naming.directory.InvalidAttributesException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Scanner;

/**Controls views of the recipe and userinteraction with the recipe*/
public class RecipeView {

    /** View for creating a recipe*/
    public static RecipeModel createRecipe(){
        RecipeModel recipe = new RecipeModel();
        System.out.println("Add a recipe");
        Scanner myScan = new Scanner(System.in).useDelimiter("\\n");
        try{
            System.out.println("Please enter a recipe name: ");
            recipe.setName(myScan.next());
            System.out.println("Enter in the ibus: ");

            recipe.setIbu(myScan.nextFloat());
            myScan.nextLine();
            System.out.println("Enter in the Original Gravity(og): ");
            recipe.setOg(myScan.nextFloat());
            myScan.nextLine();
            System.out.println("Enter in the Final Gravity(fg): ");
            recipe.setFg(myScan.nextFloat());
            myScan.nextLine();
        }
        catch(Exception e){
            Logger.writeToLog(400, e.getMessage());

        }
        finally{
            //myScan.flush();
        }
        return recipe;
    }
    /** prompts users for ingredients to add to a recipe */
    public static HashMap<IngredientModel,Float> promptForIngredients(ArrayList<IngredientModel> ingredientList){
        HashMap<IngredientModel, Float> result = new HashMap<IngredientModel, Float>();
        int index = 1;
        float amount = 0;
        try {
           while(true) {
               System.out.println("Please choose an ingredient by entering the number next to an ingredient(0 to exit)");
               IngredientView.displayWithSelector(ingredientList);
               Scanner scanner = new Scanner(new BufferedReader(new InputStreamReader(System.in)));
               System.out.println("Enter your selection:");
                String line = scanner.nextLine();
               index = Integer.parseInt(line);
               if(index==0){
                   break;
               }
                else index = index-1;
               System.out.println("Input an amount");
               amount = scanner.nextFloat();
               result.put(ingredientList.get(index), amount);
               ingredientList.remove(index);
           }

       }
       catch(Exception e){
           Logger.writeToLog(e);
       }

        return result;
    }

    /**prompts user for steeps to brew */
    public static ArrayList<String> promptForSteps(){
        ArrayList<String> steps = new ArrayList<String>();
        int counter = 0;
        try {
            while(true) {
                if(steps.size()>0){
                    counter =1;
                    for(String note: steps){
                        System.out.println(counter + note);
                        counter++;
                    }
                }
                System.out.println("Please Enter in the steps to brew(0 to exit)");
                Scanner scanner = new Scanner(new BufferedReader(new InputStreamReader(System.in)));
                String step  = scanner.nextLine();
                if(step.equals("0")){
                    break;
                }
                steps.add(step);
            }
            if(steps.size()==0){
                throw new InvalidAttributesException("You must include at least 1 step.");
            }
        }
        catch(Exception e){
            Logger.writeToLog(e);
            System.out.println("There was an error reading in the steps. ");
        }
        return steps;
    }

    public static double promptForGravity(String gravityType){
        Double gravity = 0.0;
        try {
            System.out.println("Please enter in your " + gravityType + " gravity: ");

            Scanner scanner = new Scanner(new BufferedReader(new InputStreamReader(System.in)));
            gravity=scanner.nextDouble();
        }
        catch(Exception ex){
            Logger.writeToLog(ex);
        }
        return gravity;
    }
}
