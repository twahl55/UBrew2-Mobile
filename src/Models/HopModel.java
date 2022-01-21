package Models;
/** this class represents the hop object and methods needed to interact with it*/
public class HopModel extends IngredientModel {
    private String unit="oz";
    //public String Name;
    private double alphaAcid;
/*
    public HopModel(){
        alphaAcid = 0;
    }

    public HopModel(float aa){
        this.alphaAcid = aa;
    }
*/
    public HopModel(String na, double aa){
        super( na);
        this.alphaAcid = aa;
    }

    public double getAlphaAcid(){
        return this.alphaAcid;
    }


    public void setAlphaAcid(double aa){
        this.alphaAcid = aa;
        //Move these to the controller;
        if(aa<100 && aa>0 ) {

        }
        else {
            System.out.println("Invalid AA Percentage. " +
                    "AA must be between 0 and 1");
        }
    }

    public String getUnit(){
        return this.unit;
    }

}
