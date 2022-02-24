package controllers;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import logging.*;
import java.sql.*;
import models.*;
import java.util.List;
import java.util.ArrayList;

/** DB Helper function. Controls all interactions with the DB
 *
 * Author: Tyler Wahl
 * Date February 22
 * Course CS-622
 * */

public class DBHelper {
    private Connection connection;
    private final String host = "ubrew2mobile.cyfaiieqbmoj.us-west-2.rds.amazonaws.com";

    private final String database = "postgres";
    private final int port = 5432;
    private final String user = "postgres";
    private final String pass = "Brew4Days";
    private String url = "jdbc:postgresql://%s:%d/%s";
    private boolean status;

    public  DBHelper() {
        this.connection = null;
        this.url = String.format(url, this.host, this.port, this.database);

    }

    private void connect ()
    {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    Class.forName("org.postgresql.Driver");
                    connection = DriverManager.getConnection(url, user, pass);
                    status = true;

                } catch (Exception e) {
                    status = false;
                    System.out.print(e.getMessage());
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (Exception e) {
            e.printStackTrace();
            this.status = false;
        }
    }

    /**Make sure the user table exists */
    public void ensureUserTableExists(){
        connect();
        String tableName = "users";
        try(PreparedStatement statement = this.connection.prepareStatement("Select count(*) From information_schema.tables " +
                "Where table_name = ? Limit 1")){
            statement.setString(1, tableName);
            ResultSet resultset = statement.executeQuery();
            resultset.next();
            if(resultset.getInt(1)==0){
                Statement create = this.connection.createStatement();
                String sql = "CREATE TABLE public.users (" +
                        "\"firstName\" varchar(255) NOT NULL," +
                        "\"lastName\" varchar(255) NULL," +
                        "\"passwordHash\" varchar(255) NOT NULL," +
                        "email varchar(255) NOT NULL," +
                        "\"admin\" bool NOT NULL DEFAULT false," +
                        "disabled bool NOT NULL DEFAULT false," +
                        "\"userId\" uuid NOT NULL DEFAULT uuid_generate_v1()," +
                        "CONSTRAINT \"Users_email_key\" UNIQUE (email)," +
                        "CONSTRAINT \"Users_pkey\" PRIMARY KEY (\"userId\")" +
                        ");";
                create.executeUpdate(sql);
            }
        }
        catch(Exception ex){
            Logger.writeToLog(ex);
        }
    }

    /** for troubleshooting multiple connection errors*/
    public Connection getExtraConnection()
    {
        Connection c = null;
        try
        {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection(url, user, pass);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return c;
    }

    /** pull the recipes back from the database*/
    public ArrayList<RecipeModel> getRecipes(String query)throws SQLException{
        ResultSet result = null;
        connect();
        ArrayList<RecipeModel> recipes = new ArrayList<RecipeModel>();
        try(Statement statement = this.connection.createStatement()){
            result = statement.executeQuery(query);
            while(result.next()){
                RecipeModel recipe =new RecipeModel(result.getString(1), result.getFloat(2),
                        result.getFloat(3), result.getFloat(4), result.getFloat(5),
                        result.getString(6), result.getInt(7));
                recipes.add(recipe);
            }
        }
        return recipes;
    }

    /*get recipe by id */
    public RecipeModel getRecipeById(String query, int id)throws SQLException{
        ResultSet result = null;
        connect();
        RecipeModel recipe = new RecipeModel();
        try(PreparedStatement statement = this.connection.prepareStatement(query)){
            statement.setInt(1, id);
            result = statement.executeQuery();
            while(result.next()){
                 recipe =new RecipeModel(result.getString(1), result.getFloat(2),
                        result.getFloat(3), result.getFloat(4), result.getFloat(5),
                        result.getString(6), id);

            }
        }
        return recipe;
    }

    /**get malts back for a recipe */
    public  List<RecipeIngredient<MaltModel>> getRecipeMaltsByRecipeId(int id){
        List<RecipeIngredient> result  = new ArrayList<RecipeIngredient>();
        List<RecipeIngredient<MaltModel>> finalResult = new ArrayList<RecipeIngredient<MaltModel>>();
        try{
            result =  getRecipeIngredientsByRecipeId(id, "malt");
            for(RecipeIngredient ri : result) {
                finalResult.add((RecipeIngredient<MaltModel>)ri);
            }
        }
       catch(Exception ex){
           Logger.writeToLog(ex);
       }
       return finalResult;
    }

    /** get hops back from a recipe */
    public  List<RecipeIngredient<HopModel>> getRecipeHopsByRecipeId(int id){
        List<RecipeIngredient> result  = new ArrayList<RecipeIngredient>();
        List<RecipeIngredient<HopModel>> finalResult= new ArrayList<RecipeIngredient<HopModel>>();
        try{
            result =  getRecipeIngredientsByRecipeId(id, "hop");
            for(RecipeIngredient ri : result) {
                finalResult.add((RecipeIngredient<HopModel>)ri);
            }
        }
        catch(Exception ex){
            Logger.writeToLog(ex);
        }
        return finalResult;
    }

    /** get recipe ingredients back */
    public List<RecipeIngredient> getRecipeIngredientsByRecipeId( int id, String type)throws SQLException{
        String query = "SELECT ri.quantity, i.\"Name\", i.specialparam, i.type, i.id FROM recipeingredients ri " +
                "JOIN ingredients i on i.id = ri.\"ingredientId\" WHERE ri.\"recipeId\" = ?  AND i.type = ?";
        ResultSet result = null;
        connect();
        List<RecipeIngredient> results = new ArrayList<RecipeIngredient>();
        RecipeModel recipe = new RecipeModel();
        try(PreparedStatement statement = this.connection.prepareStatement(query)){
            statement.setInt(1, id);
            statement.setString(2, type);
            result = statement.executeQuery();
            while(result.next()){
                IngredientModel ingredient = null;
                if(result.getString(4).equalsIgnoreCase("hop")) {
                    ingredient = new HopModel(result.getString(2), result.getDouble(3), result.getInt(5));
                }
                else{
                    ingredient = new MaltModel(result.getString(2), result.getDouble(3), result.getInt(5));
                }
                RecipeIngredient ri =new RecipeIngredient();
                ri.setIngredient( ingredient);
                ri.setAmount(result.getDouble(1));
                results.add(ri);
            }
        }
        catch(Exception e){
            Logger.writeToLog(e);
        }
        return results;
    }

    /** creates a user entry */
    public boolean createUser(UserModel user)throws SQLException{
        String query = "INSERT into users (\"firstName\", \"lastName\", \"passwordHash\", \"email\", \"admin\", \"disabled\""+
                ") values ( ?, ?, ? ,? , ?,  ?)";

        boolean result = false;
        connect();
        PreparedStatement statement  = this.connection.prepareStatement(query);
        ArrayList<RecipeModel> recipes = new ArrayList<RecipeModel>();
        try{
            statement.setString(1, user.getFirstName());
            statement.setString(2, user.getLastName());
            statement.setString(3, user.getPasswordHash());
            statement.setString(4, user.getEmail());
            statement.setBoolean(5, false);
            statement.setBoolean(6,false);
            int row  = statement.executeUpdate();
            System.out.println("Updated Rows: " + row);
            result = true;
        }
        catch(Exception ex){
            Logger.writeToLog(ex);
        }
        return result;
    }

    /**retrieves a user record from the db */
    public ArrayList<UserModel> getUser(String query, String email)throws SQLException{
        ResultSet result = null;
        connect();
        ArrayList<UserModel> users = new ArrayList<UserModel>();
        try(PreparedStatement statement = this.connection.prepareStatement(query)){
            statement.setString(1, email);
            result = statement.executeQuery();
            while(result.next()){
                UserModel user =new UserModel();
                user.setFirstName(result.getString(1));
                user.setLastName(result.getString(2));
                user.setPasswordHash(result.getString(3));
                user.setUserId(result.getString(4));

                users.add(user);
            }
        }
        return users;
    }

    /** creates ingredient records in the database. */
    public boolean createIngredients(ArrayList<IngredientModel> ingredients)throws SQLException{
        String query = "INSERT into ingredients (\"Name\", \"specialparam\", \"type\""+
                ") values ( ?, ?, ? )";

        boolean result = false;
        PreparedStatement statement  = this.connection.prepareStatement(query);
        ArrayList<RecipeModel> recipes = new ArrayList<RecipeModel>();
        try{
            for(IngredientModel ingredient : ingredients) {
                String type = "";
                double specialparam =(double) 0;
                if(ingredient instanceof HopModel){ type= "hop"; specialparam = ((HopModel) ingredient).getAlphaAcid(); }
                else if(ingredient instanceof MaltModel){type = "malt"; specialparam = ((MaltModel) ingredient).getExtractPotential();}
                statement.setString(1, ingredient.getName());
                statement.setDouble(2, specialparam);
                statement.setString(3, type);
                statement.addBatch();

            }

            int[] rows  = statement.executeBatch();
            System.out.println("Updated Rows: " + rows.length);
            result = true;
        }
        catch(Exception ex){
            Logger.writeToLog(ex);
        }
        return result;
    }

    /** gets the ingredeints from the db */
    public ArrayList<IngredientModel> getIngredients(String query)throws SQLException{
        ResultSet result = null;
        connect();
        ArrayList<IngredientModel> ingredients = new ArrayList<IngredientModel>();
        try(Statement statement = this.connection.createStatement()){
            result = statement.executeQuery(query);
            while(result.next()){
                IngredientModel ingredient = null;
                if(result.getString(3).toLowerCase().equalsIgnoreCase("hop")) {
                    ingredient = new HopModel(result.getString(1), result.getDouble(2), result.getInt(4));
                }
                else if(result.getString(3).toLowerCase().equalsIgnoreCase("malt")){
                    ingredient = new MaltModel(result.getString(1), result.getDouble(2), result.getInt(4));
                }
                ingredients.add(ingredient);
            }
        }
        return ingredients;
    }

    /** creates a recipe record and returns the recipe id*/
    public int createRecipe(RecipeModel recipe, String userId){
        String query = "INSERT into recipes (\"Name\", \"ABV\", \"OG\", \"FG\", \"IBU\", public, instructions, \"userId\""+
                ") values ( ?, ?, ? ,? , ?,  ?,?,?)";
        int id = 0;
        connect();
        try(PreparedStatement statement = this.connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)){
            statement.setString(1, recipe.getName());
            statement.setFloat(2, recipe.getAbv());
            statement.setFloat(3, recipe.getOg());
            statement.setFloat(4, recipe.getFg());
            statement.setFloat(5, recipe.getIbu());
            statement.setBoolean(6, true);
            statement.setString(7, recipe.stepsToString(true));
            statement.setObject(8, java.util.UUID.fromString(userId));
            statement.executeUpdate();
            ResultSet rs = statement.getGeneratedKeys();
            if (rs != null && rs.next()) {
                id = rs.getInt(1);
            }
        }
        catch(Exception ex){
            Logger.writeToLog(ex);
        }
        return id;
    }

    /** adds instnaces to the mapping table for recipes and ingredients */
    public void addRecipeIngredientsToDB(RecipeModel recipe){
        String query = "Insert into recipeingredients(\"ingredientId\", \"recipeId\", quantity) Values (?, ?, ?)";
        connect();
        try{
            PreparedStatement statement  = this.connection.prepareStatement(query);
            for(RecipeIngredient<MaltModel>  malt: recipe.getRecipeMalts())
            {
                statement.setInt(1, malt.getIngredient().getId());
                statement.setInt(2, recipe.getId());
                statement.setDouble(3, malt.getAmount());
                statement.addBatch();
            }
            for(RecipeIngredient<HopModel>  hop: recipe.getRecipeHops())
            {
                statement.setInt(1, hop.getIngredient().getId());
                statement.setInt(2, recipe.getId());
                statement.setDouble(3, hop.getAmount());
                statement.addBatch();
            }
            statement.executeBatch();
        }
        catch(Exception e){
            Logger.writeToLog(e);
        }
    }

}