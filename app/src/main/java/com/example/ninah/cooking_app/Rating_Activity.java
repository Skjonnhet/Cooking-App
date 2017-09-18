package com.example.ninah.cooking_app;

import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;


public class Rating_Activity extends AppCompatActivity {

    private Button yesButton;
    private Button noButton;
    private RatingBar ratingBar;
    private String recipeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_);

        receiveIntent();

        initButtons();
        initRatingBar();
        setButtonOnClickListener();
    }

    private void receiveIntent(){
        Intent intent=getIntent();
        if(intent!=null){
            if(intent.getStringExtra("RecipeId")!=null){
                recipeId =intent.getStringExtra("RecipeId");            }
            else {
                Toast.makeText(this,"No recipeId-can't rate recipe",Toast.LENGTH_LONG);
                Log.d("Rating_Activity","No recipeId in Intent" );
            }

        }
    }



    private void initButtons(){
        yesButton=(Button) findViewById(R.id.ratingRecipe_changeRecipeButton_yes);
        noButton=(Button) findViewById(R.id.ratingRecipe_changeRecipeButton_no);
    }

    private void initRatingBar(){
        ratingBar=(RatingBar) findViewById(R.id.ratingRecipe_RatingBar);
    }

    private void setButtonOnClickListener(){
        yesButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                commitRatingToDB();
                changeRecipe();

            }
        });

        noButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                commitRatingToDB();
                returnToMainMenu();
            }
        });

    }

    private void changeRecipe(){
        Intent changeRecipeIntent= new Intent(this, RecipeNew.class);
        changeRecipeIntent.putExtra(CookingConstants.NEW_RECIPE_KEY, CookingConstants.NEW_RECIPE_FALSE);
        if(recipeId!=null){
            changeRecipeIntent.putExtra(CookingConstants.RECIPE_ID_KEY, recipeId);
            startActivity(changeRecipeIntent);
        }

        else{Toast.makeText(this,"No recipeId-can't change recipe",Toast.LENGTH_LONG);
            Log.d("Rating_Activity","No recipeId in changeRecipe()" );}
    }

    private void returnToMainMenu(){
        Intent mainMenuIntent=new Intent(this, MainActivity.class);
        startActivity(mainMenuIntent);
    }

    private void commitRatingToDB(){
        if(ratingBar!=null){
            if(ratingBar.getRating()==0){
                Toast.makeText(this,"Please rate your recipe", Toast.LENGTH_LONG);
            }

            else {
                //write Rating in DB
            }
        }
    }



}
