package com.example.ninah.cooking_app;

import java.io.Serializable;

/**
 * Created by Roman on 18.09.2017.
 */

public class Ingredient implements Serializable{
    private String name;
    private String norm;
    private double amount;

    public Ingredient(String name, String norm, double amount){
        this.name=name;
        this.norm=norm;
        this.amount=amount;
    }

    public void setName(String name){
        this.name=name;
    }

    public void setNorm(String norm){
        this.norm=norm;
    }

    public void setAmount(double amount){
        this.amount=amount;
    }

    public String getName() {
        return name;
    }

    public String getNorm(){
        return norm;
    }

    public double getAmount(){
        return amount;
    }

}

