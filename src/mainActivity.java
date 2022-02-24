import java.util.Scanner;
import controllers.*;
import logging.*;
import models.*;


/**
 * this class controls our main activity
 *
 * Author: Tyler Wahl
 * Date: February 15, 2022
 * Course:CS-622
 * */
public class mainActivity {


    public static void main(String[] args){
        (new DBHelper()).ensureUserTableExists();
        Scanner myObj = new Scanner(System.in);
        Integer decision = 99;
        boolean authenticated = false;
        while(decision != 0) {


            UserModel user = null;
            while(!authenticated){
                System.out.println("Welcome To UBrew2-Mobile. Please enter 1 to login, or 2 to register ");
                try {
                    decision = Integer.parseInt(myObj.nextLine());
                } catch (Exception ex) {
                    Logger.writeToLog(ex);
                }
                switch(decision){
                    case 0:
                        System.out.println("Exiting. Good Bye");
                        System.exit(0);
                        break;
                    case 1:
                        user = (new UserController()).login();
                        break;
                    case 2:
                        user =  (new UserController()).register();
                        break;
                    default:
                        System.out.println("You didnt enter in a valid selection try again:");
                        break;
                }
                if(user!=null && user.getUserId()!=null && user.getUserId() !="")
                    authenticated = true;
            }
            System.out.println("Enter 3 to print out a list of our ingredients or 4 to add a new Ingredient or 5 to" +
                    " create a recipe or 6 to calculate  abv or 7 to calculate an Original gravity estimate or 8 to " +
                    "calculate Strike Water or 9 to recalculate a recipe");
            try {
                decision = Integer.parseInt(myObj.nextLine());
            } catch (Exception ex) {
                Logger.writeToLog(ex);
            }
            switch (decision) {

                case 3:
                    new IngredientController().listAll();
                    break;
                case 4:
                    new IngredientController().createIngredient();
                    break;
                case 5:
                    new RecipeController().createRecipe(user);
                    break;
                case 6:
                    new RecipeController().calculateAbv();
                    break;
                case 7:
                    new RecipeController().gravity_estimation(65, 5);
                    break;
                case 8:
                    new RecipeController().calculateStrikeWaterPrompts();
                    break;
                case 9:
                    new RecipeController().RecipeCalculations(user);
                    break;
                default:
                    System.out.println("You didnt enter in a valid selection try again:");
                    break;
            }
        }
        System.out.println("You entered " + decision);

    }
}
