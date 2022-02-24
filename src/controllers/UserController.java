package controllers;
import logging.Logger;
import models.*;
import views.*;
import org.mindrot.jbcrypt.*;
import java.util.ArrayList;
/** this class handles thes view and the model to interact with users.
 *
 * Author: Tyler Wahl
 * Date: February 15, 2022
 * Course:CS-622
 * */
public class UserController {

    /** log in a user and compare the password hash */
    public UserModel login(){
        String[] params = UserView.login();
        UserModel resultUser = null;
        boolean result = false;
        ArrayList<UserModel> user = new ArrayList<UserModel>();
        try {
            user  = (new DBHelper()).getUser("Select \"firstName\", \"lastName\", \"passwordHash\", \"userId\"" +
                    "from users WHERE email = ? AND disabled = false ", params[0]);
            if(user.size() == 1) {
                result = BCrypt.checkpw(params[1], user.get(0).getPasswordHash());
            }
            else result = false;
        }
        catch(Exception ex){
            Logger.writeToLog(ex);
        }
        if(!result){
            resultUser =  UserView.unsuccessfulLogin();
        }
        else{
            resultUser = user.get(0);
            UserView.successfulLogin();
        }
        return resultUser;

    }

    /** register the user */
    public UserModel  register(){
        String[] params = UserView.register();
        if(params.length == 4){
            System.out.println("Hey that worked.");
            String passHash = BCrypt.hashpw(params[3], BCrypt.gensalt(12));
            if(BCrypt.checkpw(params[3], passHash)){
                System.out.println("Pass hashed");

            }
            else{
                System.out.println("Pass Not Hashed");
            }
            try {
                UserModel user = new UserModel(params[0], params[1], params[2], passHash);
                boolean success = (new DBHelper()).createUser(user);
                UserView.displayName(user.getFirstName(), user.getLastName());
                if(success)
                    return user;
            }
            catch(Exception ex){
                Logger.writeToLog(ex);
            }
        }
        else{
            System.out.println("Looks like you're missing a parameter try again. ");
        }
        return null;
    }
}
