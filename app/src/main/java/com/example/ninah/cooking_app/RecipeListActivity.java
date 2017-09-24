package com.example.ninah.cooking_app;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class RecipeListActivity extends AppCompatActivity {

    /*****************Activity to set show user all recipes on one screen************************************/
    /*reads all recipes from the DB trough database adapter and shows its name in a listView*/
    /*user selects recipe by clicking short on it so it is shown in the RecipeStartActivity*/
     /*user deletes recipe by clicking long on it so dialog is shown*/


    ListView recipeList;
    ArrayList<String> recipeNames;
    ArrayAdapter arrayAdapter;
    DBAdapter dbAdapter;
    List<Recipe> recipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);
        recipeList = (ListView)findViewById(R.id.recipe_list);
        initLists();
        initAdapters();
        setupListView();
        setListViewClickListener();
    }

    //--------------------init part------------------------------------------------------------------
    //methods used in onCreate()

    //inits all ArrayLists
    private void initLists(){
        recipeNames=new ArrayList<>();
        recipes=new ArrayList<>();
    }

    //inits all adapters
    private void initAdapters(){
        try {
            arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice, recipeNames);
            dbAdapter = new DBAdapter(this);
            recipeList.setAdapter(arrayAdapter);
        }
        catch (Exception e){ Log.d("RecipeListActivity", "initAdapters "+e.toString());}
    }


    //setsUpListViews
    private void setupListView(){
        try{
            recipes=dbAdapter.getAllRecipesFromDB();
            for (Recipe recipe:recipes){
                recipeNames.add(recipe.getName());
                Log.d("RecipeListActivity","setupListView recipe name:"+recipe.getName()+" id: "+ recipe.getId());
            }

            arrayAdapter.notifyDataSetChanged();
        }
        catch (Exception e){ Log.d("RecipeListActivity", "setupListView: "+e.toString());}

    }

    //sets ClickListener
    private void setListViewClickListener(){
        //user selects recipe to start it
       recipeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               try {
                   String selectedRecipe = ((TextView) view).getText().toString();
                   Log.d("RecipeListActivity", "onItemLongClick, name: " + selectedRecipe);
                   startRecipe(selectedRecipe);
               }
               catch (Exception e){};

           }
       });

        //long click shows a menu so user can choose to delete the recipe
        recipeList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                try{
                    String selectedRecipe=((TextView) view).getText().toString();
                    startDeleteAlertDialog(selectedRecipe, RecipeListActivity.this);

                }
                catch (Exception e){};
                return true;
            }
        });
    }

    //---------------------------start part---------------------------------------------------------
    //methods to start selected recipe

    //starts RecipeStartActivity.class
    //the intent has the recipeID as extra
    private void startRecipe(String name){
        Long id = getRecipeIdByName(name);
        Log.d("RecipeListActivity", "startRecipe id "+id);
        try {

            Intent intent = new Intent(this, RecipeStartActivity.class);
            intent.putExtra(CookingConstants.RECIPE_ID_KEY, id);
            startActivity(intent);
        }

        catch (Exception e){
            Log.d("RecipeListActivity", "startRecipe "+e.toString());}
    }


    //--------------------delete part--------------------------------------------------------------
    //methods delete recipe from list and db

    //deletes recipe with the given name
    private void deleteThisRecipe(String name){
        try{
            Long id=  getRecipeIdByName(name);
            if(id!=CookingConstants.DEFAULT_RECIPE_ID&&id!=null){
                dbAdapter.destroyWholeRecipeAndAllItsConnectedInstances(dbAdapter.getRecipeByID(id));
                deleteRecipeFromListView(name);
                Toast.makeText(this, "Rezept gelöscht",Toast.LENGTH_SHORT).show();
            }

            else {
                Toast.makeText(this, "Dieses Rezept kann nicht gelöscht werden",Toast.LENGTH_SHORT).show();}
        }
        catch (Exception e){   Log.d("RecipeListActivity", "deleteThisRecipe"+e.toString());}


    }
    private void deleteRecipeFromListView(String name){
        recipeNames.remove(name);
        arrayAdapter.notifyDataSetChanged();
    }


    //------------------------dialog part-----------------------------------------------------------
    //methods show a dialog to the user

    //starts an alertDialog
    //user can choose if he wants to delete a recipe
    private void startDeleteAlertDialog(final String recipeName, Context context){
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setTitle("Rezept löschen");
        alertBuilder.setMessage("Wirklich löschen?");
        alertBuilder.setPositiveButton("Ja, löschen!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteThisRecipe(recipeName);
            }
        });
        alertBuilder.setNegativeButton("Nein, behalten", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertBuilder.show();
    }


    //----------------helper part-------------------------------------------------------------------
    //methods are helper methods


    //returns the id of a recipe by its name
    private Long getRecipeIdByName(String name){

        Long recipeID=Long.valueOf(0);
        try {

            for (Recipe recipe : recipes) {
                if(recipe!=null) {
                    Log.d("RecipeListActivity", "getRecipeIdByName, id: "+recipeID);
                    if (recipe.getName().equals(name)) {
                        recipeID = recipe.getId();
                        Log.d("RecipeListActivity", "getRecipeIdByName, equals id: "+recipeID);
                    }
                }
            }
        }
        catch (Exception e){Log.d("RecipeListActivity", "getRecipeIdByName "+e.toString());}
        return recipeID;
    }




}
