package models;

public class RecipeIngredient<T extends IngredientModel> {

    private T ingredient;
    private double amount ;

    public IngredientModel getIngredient(){
        return this.ingredient;
    }

    public void setIngredient(T ingr){
        this.ingredient = ingr;
    }

    public void setAmount(double amount){
        this.amount = amount;
    }

    public double getAmount(){
        return this.amount;
    }

    public String getName(){
        return getIngredient().getName();
    }
}
