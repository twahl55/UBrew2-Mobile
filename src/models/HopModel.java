package models;
/** this class represents the hop object and methods needed to interact with it*/
public class HopModel extends IngredientModel {
    private String unit="oz";
    //public String Name;
    private double alphaAcid;

    /**
     *Constructor that creates a hop Model instance
     */
    public HopModel(String hopname, double alphaacid){
        super( hopname);
        this.alphaAcid = alphaacid;
    }

    /**
     *returns the AlphaAcid Member of an instance
     */
    public double getAlphaAcid(){
        return this.alphaAcid;
    }


    /**
     *Sets the alphaAcid Member of an instance
     */
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

    /**
     *Returns the unit associated with a hopModel instane
     */
    public String getUnit(){
        return this.unit;
    }



}
