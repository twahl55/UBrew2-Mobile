package models;
/** this class represents an ingredient and necesary functions for interacting with it*/
public abstract class IngredientModel implements java.io.Serializable{

    private  String name;

    private String unit;

    /**
     *IngredientModel Constructor
     */
    public IngredientModel(String name){
        this.name = name;
    }

    /**
     * abstract method to return the unit for an ingredient
     */
    public abstract String getUnit();

    /**
     * Sets the name of the ingredient
     */
    public void setName(String n){
        this.name = n;
    }

    /**
     * get the name of the ingredient
     */
    public String getName(){
        return this.name;
    }

    /**
     * override the toString() functionality to appropriately stringify an ingredient
     */
    @Override
    public String toString(){
        String type = (this instanceof HopModel)? "Hop": "Malt";
        double special = (this instanceof HopModel)?((HopModel)this).getAlphaAcid():
                ((MaltModel)this).getExtractPotential();
        String result = "Type:" + type + "|Name:" + this.getName() + "|SpecialParam:" + special + "|Unit:" + getUnit();
        return result;
    }
}
