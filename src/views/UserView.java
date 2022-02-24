package views;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import controllers.UserController;
import logging.Logger;
import models.UserModel;

/**this class handles the view functionality for a user object
 *
 * Author: Tyler Wahl
 * Date: February 15, 2022
 * Course:CS-622
 * */
public class UserView {

    /**
     *
     * returns a printed out view of the login
     */
    public static String[] login(){
        String[] result =new String[2];

        System.out.print("Email:");
        if(System.console() == null){
            Scanner scanner = new Scanner(new BufferedReader(new InputStreamReader(System.in)));
            result[0] = scanner.next();
            System.out.print("Password:");
            result[1] = scanner.next();

        }
        else {
            result[0] = System.console().readLine();
            System.out.print("Password:");
            result[1] = new String(System.console().readPassword());
        }
        return result;
    }

    /**
     * handles an unsuccesful login
     */
    public static UserModel unsuccessfulLogin(){
        UserModel user = null;
        System.out.println("You've entered incorrect credentials. Would you like to try again? 1 for yes 0 for no");
        try {
            while(true) {
                Scanner scanner = new Scanner(new BufferedReader(new InputStreamReader(System.in)));
                int choice = scanner.nextInt();
                if(choice == 0){
                    System.out.println("Ok. Thank you for your time. GoodBye. ");
                    break;
                }
                if(choice == 1){
                    user = (new UserController()).login();
                    break;
                }
                else{
                    System.out.println("Invalid Selection please try again. ");
                }

            }
        }catch(Exception ex){
            Logger.writeToLog(ex);
        }
        return user;
    }
    /**
     * Handles a succesful login
     */
    public static void successfulLogin(){
        System.out.println("You have successfully logged in.");
    }

    /**
     * Returns a printed out view for registering
     *
     */
    public static String[] register(){
        ArrayList<String> input = new ArrayList<String>();
        Scanner scanner = new Scanner(new BufferedReader(new InputStreamReader(System.in)));
        System.out.print("First Name:");
        input.add(scanner.next());
        System.out.print("Last Name:");
        input.add( scanner.next());
        System.out.print("Email:");
        input.add(scanner.next());
        System.out.print("Password:");
        input.add(scanner.next());

        String[] result =input.toArray(new String[0]);
        return result;
    }

    /**
     * displays the name of a user
     * @param firstname
     * @param lastname
     */
    public static void displayName( String firstname,String lastname){
        System.out.println("Name: " +firstname + " " + lastname);
    }


}
