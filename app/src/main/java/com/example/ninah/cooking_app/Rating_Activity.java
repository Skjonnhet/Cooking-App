package com.example.ninah.cooking_app;

import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;


public class Rating_Activity extends AppCompatActivity {
    /*****************Activity to set the rating of the recipe************************************/
    /*allows the user to rate a recipe and save the value into the DB
    * asks the user if he wants to change the recipe and starts the fitting activity
    * Logic:Rating_Activity->rate recipe with recipe id,
    *                      ->ask user if he wants to change recipe
    *                                   ->yes: start:RecipeNewActivity.class with boolean: new recipe=false and recipeID as extras
    *                                   ->no:  start: MainActivity.class
    * */


    private Button yesButton;
    private Button noButton;
    private static RatingBar ratingBar;
    private Long recipeId;
    private Long defaultID;
    private DBAdapter dbAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_);
        initDefaultValues();
        initButtons();
        initRatingBar();
        dbAdapter=new DBAdapter(this);
        setButtonOnClickListener();
        setRecipeIdTroughIntent();
        setOldValueToRaingBar();
    }

    private void initDefaultValues(){
        defaultID=CookingConstants.DEFAULT_RECIPE_ID;
        recipeId=defaultID;
    }

    private void initButtons(){
        yesButton=(Button) findViewById(R.id.ratingRecipe_changeRecipeButton_yes);
        noButton=(Button) findViewById(R.id.ratingRecipe_changeRecipeButton_no);
    }

    private void initRatingBar(){
        ratingBar=(RatingBar) findViewById(R.id.ratingRecipe_RatingBar);
        ratingBar.setStepSize(1);
        ratingBar.setRating(5);
    }

    private void setRecipeIdTroughIntent(){
        Intent intent=getIntent();
        if(intent!=null) {
            try {
                Bundle extras = intent.getExtras();
                recipeId = extras.getLong(CookingConstants.RECIPE_ID_KEY);
            } catch (Exception e){Log.d("Rating_Activity","setRecipeIdTroughIntent "+e.toString() );}
        }
        else {
            Toast.makeText(this,"No recipeId-can't rate recipe",Toast.LENGTH_LONG);
            Log.d("Rating_Activity","setRecipeIdTroughIntent:No recipeId in Intent" );
        }
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

    private void setOldValueToRaingBar(){
        try{
            ratingBar.setNumStars(dbAdapter.getRecipeTimeInMinutes(recipeId));
        }
        catch (Exception e){    Log.d("Rating_Activity","setOldValueToRaingBar " +e.toString());}
    }

    //changes rating of the recipe
    private void commitRatingToDB(){
        if(ratingBar!=null){
            try{
                dbAdapter.updateRatingStars(recipeId,ratingBar.getNumStars());
            }

            catch (Exception e){  Log.d("Rating_Activity","commitRatingToDB " +e.toString());}

        }
        else  Log.d("Rating_Activity","commitRatingToDB is null" );
    }



    //start RecipeNewActivity,class trough intent if ratingbar is not empty
    private void changeRecipe(){
        Intent changeRecipeIntent= new Intent(this, RecipeNewActivity.class);
        changeRecipeIntent.putExtra(CookingConstants.NEW_RECIPE_KEY, CookingConstants.NEW_RECIPE_FALSE);
        if(recipeId!=null){
            try {
                if(!isRatingEmpty()){
                    Log.d("Rating_Activity","changeRecipe " +recipeId);
                    changeRecipeIntent.putExtra(CookingConstants.RECIPE_ID_KEY, recipeId);
                    startActivity(changeRecipeIntent);

                }

                else {  Toast.makeText(this,"Bitte bewerten Sie das Rezept",Toast.LENGTH_LONG).show();  }

            }

            catch (Exception e){
                Log.d("Rating Activity ", "changeRecipe "+ e.toString());
            }

        }

        else{Toast.makeText(this,"Rezept konnte nicht geladen werden.Bitte starten Sie die Bewertung neu!",Toast.LENGTH_LONG).show();
            Log.d("Rating_Activity","No recipeId in changeRecipe()" );}
    }

    //start MainActivity,class trough intent if ratingbar is not empty
    private void returnToMainMenu(){
        try {
            if(!isRatingEmpty()){
                Log.d("Rating_Activity","returnToMainMenu " +recipeId);
                Intent mainMenuIntent=new Intent(this, MainActivity.class);
                startActivity(mainMenuIntent);
            }

            else   Toast.makeText(this,"Bitte bewerten Sie das Rezept",Toast.LENGTH_LONG).show();
        }
        catch (Exception e){Log.d("Rating Activity ","returnToMainMenu "+e.toString());}

    }


    //checks if ratingbar is empty
    private boolean isRatingEmpty() {
        if (ratingBar.getRating() == 0)
            return true;

        else return false;
    }





}
