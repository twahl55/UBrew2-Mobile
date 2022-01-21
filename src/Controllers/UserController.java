package Controllers;
import Models.*;
import Views.*;
/** this class handles thes view and the model to interact with users. **/
public class UserController {

    public void login(){
        String[] params = UserView.login();
        if(params.length == 2){
            System.out.print("Hey that worked.");
        }

    }

    public void register(){
        String[] params = UserView.register();
        if(params.length == 4){
            System.out.println("Hey that worked.");
            UserModel user = new UserModel(params[0], params[1], params[2], params[3]);
            UserView.displayName(user.getFirstName(), user.getLastName());
        }
        else{
            System.out.println("Looks like you're missing a parameter try again. ");
        }
    }

    /* Need to add password hashging and checking
    private String hashPassword(String pss){
        BCrypt
    }*/
}
