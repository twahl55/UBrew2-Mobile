import java.util.Scanner;
import controllers.*;


/**this class controls our main activity*/
public class mainActivity {


    public static void main(String[] args){
        System.out.println("Welcome To UBrew2-Mobile. Please enter " /*1 to login, or*/ + "2 to register or 3 to print " +
                "out a list of our ingredients or 4 to add a new Ingredient or 5 to create a recipe");
        Scanner myObj = new Scanner(System.in);
        Integer decision = Integer.parseInt(myObj.nextLine());

        switch(decision){
            /*case 1:
                new UserController().login();
                break;*/
            case 2:
                new UserController().register();
                break;
            case 3:
                new IngredientController().listAll();
                break;
            case 4:
                new IngredientController().createIngredient();
                break;
            case 5:
                new RecipeController().createRecipe();
                break;
            default:
                System.out.println("You didnt enter in a valid selection try again:");
                break;
        }
        System.out.println("You entered " + decision);

    }
}
