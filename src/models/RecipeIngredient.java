package models;
/** Class that houses RecipeIngredients */
public class RecipeIngredient<T extends IngredientModel> {

    private T ingredient;
    private double amount ;

    /** gets the ingredient Model*/
    public IngredientModel getIngredient(){
        return this.ingredient;
    }

    /** sets the ingredientModel */
    public void setIngredient(T ingr){
        this.ingredient = ingr;
    }

    /**sets the amount of this ingredient in the recipe */
    public void setAmount(double amount){
        this.amount = amount;
    }

    /** returns the amount associated with this recipeIngredient*/
    public double getAmount(){
        return this.amount;
    }

    /** returns the name*/
    public String getName(){
        return getIngredient().getName();
    }
}
