package controllers;
import models.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;

class IngredientControllerTest {

    @org.junit.jupiter.api.Test
    void addToFile() {
        IngredientModel ingredient = new HopModel("Test", 7.2);
        ArrayList<IngredientModel> ingredientList = new ArrayList<IngredientModel>(Arrays.asList(ingredient));
        assertEquals(true, IngredientController.addToFile(ingredientList));
    }

    @Test
    void stringistToModel() {
        ArrayList<String> list = new ArrayList<String>(Arrays.asList("Type:Hop|Name:Cascade Hops|SpecialParam:5.2|Unit:oz",
        "Type:Malt|Name:Munich Malt|SpecialParam:75.0|Unit:lbs","Type:Malt|Name:Special B|SpecialParam:68.0|Unit:lbs",
        "Type:Malt|Name:Wheat Malt|SpecialParam:79.0|Unit:lbs", "Type:Hop|Name:Citra|SpecialParam:3.1|Unit:oz"
        ));
        ArrayList<IngredientModel> models = IngredientController.stringistToModel(list);
        assertEquals(list.size(), models.size());
    }
}