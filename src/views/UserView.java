package views;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

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
