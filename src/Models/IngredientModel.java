package Models;
/** this class represents an ingredient and necesary functions for interacting with it*/
public abstract class IngredientModel {

    private  String name;

    private String unit;

    public IngredientModel(String name){
        this.name = name;
    }

    public abstract String getUnit();

    public void setName(String n){
        this.name = n;
    }
    public String getName(){
        return this.name;
    }
}
