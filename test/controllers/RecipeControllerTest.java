package controllers;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RecipeControllerTest {

    @Test
    void calculateAbv() {
        assertEquals(5.25, RecipeController.calculateAbv(1.050, 1.010));
    }

    /*@Test  will use once we have a db to pull a recipe from.
    void gravity_estimation() {
    }*/
}