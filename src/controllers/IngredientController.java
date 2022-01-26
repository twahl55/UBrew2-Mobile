package controllers;
import models.*;
import views.*;
import logging.*;

import java.io.*;
import java.util.*;
import java.util.List;

/** This class controls interactions between ingredient model and ingredient view */
public class IngredientController {
    /**
     * List all of the current ingredients. This will eventually fetch from the db.
     */
    public void listAll() {
        ArrayList<IngredientModel> ingredients = readFromFile();
        display(ingredients);
    }

    public void display(ArrayList<IngredientModel> ingredients){
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

    /**
     * Create a new Ingredient for the application
     */
    public void createIngredient(){
        IngredientModel model = IngredientView.createIngredient();
        Boolean suuccess =addToFile (new ArrayList<>(Arrays.asList(model)));

    }

    /**
     * Add an ingredient to the file, at some point this will link to the database instead
     */
    public static  boolean addToFile(ArrayList<IngredientModel> ingredients){
        Boolean success = false;
        try {

            String dir = config.Properties.getProperty("ingredientsFile");
            File file = new File(dir + "/ingredients.txt");
            if (!file.exists()) {
                file.createNewFile();
            }
            PrintWriter pw = new PrintWriter(new FileWriter(file, true));
            for (IngredientModel ingredient : ingredients) {


                pw.format(ingredient.toString());
                pw.println();
            }
            pw.close();
            success = true;
        }catch(Exception ex){
            Logger.writeToLog(ex);
            success = false;
        }
        return success;
    }

    /**
     * Add an ingredient to the file, at some point this will link to the database instead
     */
    public static ArrayList<IngredientModel> readFromFile(){
        Boolean success = false;
        String dir = config.Properties.getProperty("ingredientsFile");
        File file = new File(dir + "/ingredients.txt");
        if (!file.exists()) {
            return null;
        }
        ArrayList<IngredientModel> result = new ArrayList<IngredientModel>();
        try {
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line = null;
            List<String> strings = new ArrayList<String>();
            while((line = br.readLine())!=null){
                strings.add(line);
            }
            br.close();
            fr.close();
            result = stringistToModel(strings);
        }catch(Exception ex){
            Logger.writeToLog(ex);
        }
        return result;
    }

   /* public boolean writeObjectToFIle(ArrayList<IngredientModel> ingredients){
        boolean success = false;
        String dir = config.Properties.getProperty("ingredientsFile");
        File file = new File(dir + "/ingredients.txt");
        try {
            PrintWriter pw = new PrintWriter(new  FileWriter(file));
            for(IngredientModel ingredient :ingredients) {
                pw.println(ingredient.toString());
            }
            pw.close();


            System.out.printf("Data is saved in /testdata/ingredients");
            success = true;
        } catch (IOException i) {
            i.printStackTrace();
            success = false;
        }
        finally{
            System.out.println("Object writing was a success?" + success);
        }
        return success;

    }

    public ArrayList<IngredientModel> readObjectsFromFile(){
       IngredientModel model = null;
       ArrayList<IngredientModel> ingredients = new ArrayList<IngredientModel>();
        String dir = config.Properties.getProperty("ingredientsFile");
        File file = new File(dir + "/ingredients.txt");
        try {
            FileInputStream fileIn = new FileInputStream(file);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            while((model  = (IngredientModel) in.readObject())!=null){
                ingredients.add(model);
            }
            System.out.println(ingredients.size());
            in.close();
            fileIn.close();
        } catch (IOException i) {
            i.printStackTrace();
            return null;
        } catch (ClassNotFoundException c) {
            System.out.println("Employee class not found");
            c.printStackTrace();
            return null;
        }
        return ingredients;
    }*/

    public static ArrayList<IngredientModel> stringistToModel(List<String> list){
        ArrayList<IngredientModel> result = new ArrayList<IngredientModel>();
        for(String model: list){
            try{
                Map<String, String> map = new HashMap<String, String>();
                String[] parts = model.split("[|:?]");
                if(parts.length >0){
                    if(parts[1].equals( "Hop")){
                        HopModel hop  = new HopModel(parts[3], Double.parseDouble(parts[5]));
                        result.add(hop);
                    }
                    else{
                        MaltModel malt = new MaltModel(parts[3], Double.parseDouble(parts[5]));
                        result.add(malt);
                    }
                }
            }
            catch(Exception ex){
                Logger.writeToLog(400, ex.getMessage());
            }
        }
        return result;
    }

}
