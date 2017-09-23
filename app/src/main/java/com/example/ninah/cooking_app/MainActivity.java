package com.example.ninah.cooking_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    /******************* MainActivity is the Main Menu of the the app******************************/


    /* -----------------overWrite part--------------------------------------------------------------
    * @OnCreate Implements the Views, Buttons the DBAdapater-Communikation tools
     *@onClick one method for all onClickListener
     * ---------------------------------------------------------------------------------------------
     * -----------------Init part-------------------------------------------------------------------
     * all methods who are used  to init the Activity in onCreate()---------------------------------
     * ---------------------------------------------------------------------------------------------
     /------------------Intent part-----------------------------------------------------------------
     *methods to start other activities trough intents----------------------------------------------
     *----------------------------------------------------------------------------------------------
     *Logic: onCreate implements Activity, after that the user can starts trough buttons other------
     * activities-----------------------------------------------------------------------------------
     */

    TextView recipeList;
    TextView bestRecipe;
    TextView fastRecipe;
    TextView randomRecipe;
    TextView newRecipe;
    Button testTimerButton;
    Button changeRecipeButton;
    Button deleteRecipeButton;
    DBAdapter dbAdapter;

    //-----------------overWrite part---------------------------------------------------------------
    // all methods who are overwritten

    //overwrites onCreate of the ActivityLifecycle
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initTextViews();
        initButtons();
        initClickListener();
        initDBCommunication();
        //cleanDataBase();
        createStartRecipe();
    }


    //overwrites the onClick methods of the buttonClickListener
    //each case starts another method
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.viewRecipeList:
               startRecipeList();
                break;

            case R.id.viewBestRecipe:
              startBestRecipe();
                break;

            case R.id.viewFastRecipe:
               startFastestRecipe();
                break;

            case R.id.viewRandomRecipe:
               startRandomRecipe();
                break;

            case R.id.viewNewRecipe:
               startNewRecipe();
                break;

            default:
                break;
        }

    }


    //-----------------Init part--------------------------------------------------------------------
    // all methods who are used in the onCreate method

    //creates default recipe so db is not empty
    private void createStartRecipe(){
        dbAdapter.createDefaultRecipeAndSaveItToDB();
    }

    //optional: cleans all tables
    //useful if DB has to be reseted in the future to Test DB
    private void cleanAllTables(){
        dbAdapter.cleanAllTables();
    }

    //inits textViews
    private void initTextViews(){
        recipeList = (TextView)findViewById(R.id.viewRecipeList);
        randomRecipe = (TextView)findViewById(R.id.viewRandomRecipe);
        bestRecipe = (TextView)findViewById(R.id.viewBestRecipe);
        fastRecipe = (TextView)findViewById(R.id.viewFastRecipe);
        newRecipe = (TextView) findViewById(R.id.viewNewRecipe);
    }

    //inits buttons
    private void initButtons(){
        //testTimerButton=(Button) findViewById(R.id.testTimerButton);
        //changeRecipeButton=(Button) findViewById(R.id.changeRecipeName);
        deleteRecipeButton=(Button) findViewById(R.id.deleteRecipeButton);
    }

    //inits clickListener
    private void initClickListener(){
        recipeList.setOnClickListener(this);
        bestRecipe.setOnClickListener(this);
        fastRecipe.setOnClickListener(this);
        randomRecipe.setOnClickListener(this);
        newRecipe.setOnClickListener(this);
    }

    //inits DB Communication
    private void initDBCommunication(){
        dbAdapter=new DBAdapter(this);

    }



    //------------------Intent part-----------------------------------------------------------------
    //methods to start other activities trough intents

    //starts the RecipeListActivity.class
    private void startRecipeList(){
        Intent recipeListIntent = new Intent(MainActivity.this, RecipeListActivity.class);
        startActivity(recipeListIntent);
    }

    //starts  highest RateRecipe recipe in DB
    //uses    startOldRecipe(id);
    private void startBestRecipe(){
        Long id=dbAdapter.getHighestRateRecipe().getId();
        startOldRecipe(id);
    }

    //starts  fastest Recipe recipe in DB
    //uses    startOldRecipe(id);
    private void startFastestRecipe(){
        Long id=dbAdapter.getRecipeWithSmallestTime().getId();
        startOldRecipe(id);
    }

    //starts  random Recipe recipe in DB
    //uses    startOldRecipe(id);
    private void startRandomRecipe(){
        Long id=dbAdapter.getRandomRecipe().getId();
        startOldRecipe(id);
    }

    //starts RecipeNewActivity.class
    //in intent the key is NEW_RECIPE_KEY the value is true
    //needs no recipe id
    private void startNewRecipe(){
        Intent newRecipeIntent = new Intent(MainActivity.this, RecipeNewActivity.class);
        newRecipeIntent.putExtra(CookingConstants.NEW_RECIPE_KEY,CookingConstants.NEW_RECIPE_TRUE);
        startActivity(newRecipeIntent);
    }

    //starts RecipeNewActivity.class
    //in intent the 1st key is NEW_RECIPE_KEY the value is false
    //in intent the 2nd key RECIPE_ID_KEY the value is the id of the recipe
    //which is then used in the RecipeStartActivity.class to get the recipe trough his id
    private void startOldRecipe(Long id){
        Intent oldRecipeIntent = new Intent(MainActivity.this, RecipeStartActivity.class);
        oldRecipeIntent.putExtra(CookingConstants.NEW_RECIPE_KEY,CookingConstants.NEW_RECIPE_FALSE);
        oldRecipeIntent.putExtra(CookingConstants.RECIPE_ID_KEY,id);
        startActivity(oldRecipeIntent);
    }

    private void cleanDataBase(){
        dbAdapter.cleanAllTables();
    }




}
