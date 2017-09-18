package com.example.ninah.cooking_app;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Roman on 18.09.2017.
 */

public class Recipe implements Serializable{
    private static String name;
    private List<Ingredient> ingredients;
    private static int ID;


    public Recipe(int ID, String name, List<Ingredient> ingredients){
        this.name=name;
        this.ingredients = ingredients;
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
