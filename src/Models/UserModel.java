package Models;
/** this class represents a user and the needed functions to interact with it */
public class UserModel {
    private String firstName;
    private String lastName;
    private String email;
    private String passwordHash;
    private boolean isAdmin;
    private boolean isEnabled;

    public UserModel(String fname, String lname, String mail, String phash){
        this.firstName = fname;
        this.lastName = lname;
        this.email = mail;
        this.passwordHash = phash;
        this.isAdmin = false;
        this.isEnabled = true;
    }
    public String getFirstName(){
        return this.firstName;
    }
    public void setFirstName(String fn){
        this.firstName = fn;
        return;
    }

    public String getLastName(){
        return this.lastName;
    }
    public void setLastName(String ln){
        this.lastName= ln;
        return;
    }

    public String getEmail(){
        return this.email;
    }
    public void setEmail(String em){
        this.email = em;
        return;
    }

    public String getPasswordHash(){
        return this.passwordHash;
    }
    public void setPasswordHash(String pass){
        this.passwordHash= pass;
        return;
    }

    public boolean getIsAdmin(){
        return this.isAdmin;
    }
    public void setIsAdmin(boolean admin){
        this.isAdmin= admin;
        return;
    }

    public boolean getIsEnabled(){
        return this.isEnabled;
    }
    public void setIsEnabled(boolean enabled){
        this.isEnabled= enabled;
        return;
    }
}
