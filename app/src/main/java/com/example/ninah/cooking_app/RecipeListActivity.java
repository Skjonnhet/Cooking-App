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
        recipeNames=new ArrayList<>();
        arrayAdapter=new ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice, recipeNames);
        recipeList.setAdapter(arrayAdapter);
        dbAdapter=new DBAdapter(this);
        recipes=new ArrayList<>();

        setupListView();
        setListViewClickListener();
    }

    /**
     * Wir brauchen hier eine Funktion, die alle Listeneinträge (Überschriften) anzeigt und
     * dann je nachdem, auf welchen man klickt, die Rezept-Activity öffnet und das entsprechende
     * Rezept anzeigt
     *
     * Siehe Übung Todo-List, da haben wir das gemacht
     */


    private void setupListView(){
       List<Recipe> recipes=dbAdapter.getAllRecipesFromDB();
        for (Recipe recipe:recipes){
            recipeNames.add(recipe.getName());
        }

        arrayAdapter.notifyDataSetChanged();
    }

    private void setListViewClickListener(){
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

    private Long getRecipeIdByName(String name){

        Long recipeID=Long.valueOf(0);
        try {

            for (Recipe recipe : recipes) {
                if(recipe!=null) {
                    if (recipe.getName().equals(name)) {
                        recipeID = recipe.getId();
                    }
                }
            }
        }
        catch (Exception e){Log.d("RecipeListActivity", "getRecipeIdByName "+e.toString());}
        return recipeID;
    }

    private void startRecipe(String name){

        try {
            Long id = getRecipeIdByName(name);
            Intent intent = new Intent(this, RecipeStartActivity.class);
            intent.putExtra(CookingConstants.RECIPE_ID_KEY, id);
            startActivity(intent);
        }

        catch (Exception e){
            Log.d("RecipeListActivity", "startRecipe "+e.toString());}
    }



}
