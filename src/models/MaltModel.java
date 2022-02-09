package models;

/** Model for representing a malt instance */
public class MaltModel extends IngredientModel {
    private String unit = "lbs";
    private double extractPotential;

    /**
     *Constructor that creates a Malt Model instance
     */
    public MaltModel(String na, double ep){
        super(na);
        this.extractPotential = ep;
    }

    /**
     *returns the Extract Potential Member of an instance
     */
    public double getExtractPotential(){
        return this.extractPotential;
    }

    /**
     *Sets the extractPotential Member of an instance
     */
    public void setExtractPotential(double ep){
        this.extractPotential = ep;
    }

    /**
     *Returns the unit associated with a MaltModel instane
     */
    public String getUnit(){
        return this.unit;
    }
}
