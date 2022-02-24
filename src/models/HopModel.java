package models;
/** this class represents the hop object and methods needed to interact with it
 *
 * Author: Tyler Wahl
 * Date: February 15, 2022
 * Course:CS-622
 * */
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
    public HopModel(String hopname, double alphaacid, int id){
        super( hopname);
        this.alphaAcid = alphaacid;
        this.setId(id);
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
