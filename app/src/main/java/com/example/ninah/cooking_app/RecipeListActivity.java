package com.example.ninah.cooking_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class RecipeListActivity extends AppCompatActivity {

    /*****************Activity to set show user all recipes on one screen************************************/
    /*reads all recipes from the DB trough database adapter and shows its name in a listView*/
    /*user selects recipe by long clicking on it*/


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

    //inits all ArrayLists
    private void initLists(){
        recipeNames=new ArrayList<>();
        recipes=new ArrayList<>();

    }

    //inits all adapters
    private void initAdapters(){
        arrayAdapter=new ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice, recipeNames);
        dbAdapter=new DBAdapter(this);
        recipeList.setAdapter(arrayAdapter);
    }


    //setsUpListViews
    private void setupListView(){
        recipes=dbAdapter.getAllRecipesFromDB();
        for (Recipe recipe:recipes){
            recipeNames.add(recipe.getName());
            Log.d("RecipeListActivity","setupListView recipe name:"+recipe.getName()+" id: "+ recipe.getId());
        }

        arrayAdapter.notifyDataSetChanged();
    }

    //sets ClickListener
    private void setListViewClickListener(){
        //user selects recipe to start it
        recipeList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedRecipe=((TextView) view).getText().toString();
                Log.d("RecipeListActivity", "onItemLongClick, name: "+selectedRecipe);
                startRecipe(selectedRecipe);
                return false;
            }
        });
    }

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



}
