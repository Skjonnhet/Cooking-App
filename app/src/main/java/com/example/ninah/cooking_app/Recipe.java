package com.example.ninah.cooking_app;

import java.util.List;

/**
 * Created by Roman on 18.09.2017.
 */

public class Recipe {
    private static String name;
    private List<RecipeListDatabase.Ingridient> ingridients;
    private static int ID;


    public Recipe(int ID, String name, List<RecipeListDatabase.Ingridient> ingridients ){
        this.name=name;
        this.ingridients=ingridients;
        this.ID=ID;
    }

    public String getName(){
        return name;
    }

    public void setID(int ID){
        this.ID=ID;
    }

    public int returnName(){
        return this.ID;
    }
}
