package Models;

public class MaltModel extends IngredientModel {
    public String unit = "lbs";
    private double extractPotential;

    /*public MaltModel(){
        extractPotential  = 0;
    }*/


    /*public MaltModel(float ep){
        extractPotential = ep;
    }*/
    public MaltModel(String na, double ep){
        super(na);
        this.extractPotential = ep;
    }
    public double getExtractPotential(){
        return this.extractPotential;
    }

    public void setExtractPotential(double ep){
        this.extractPotential = ep;
    }

    public String getUnit(){
        return this.unit;
    }
}
