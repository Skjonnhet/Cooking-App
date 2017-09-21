package com.example.ninah.cooking_app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;


import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class RecipeListActivity extends AppCompatActivity {

    ListView recipeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);
        recipeList = (ListView)findViewById(R.id.recipe_list);

        setupListView();
    }

    /**
     * Wir brauchen hier eine Funktion, die alle Listeneinträge (Überschriften) anzeigt und
     * dann je nachdem, auf welchen man klickt, die Rezept-Activity öffnet und das entsprechende
     * Rezept anzeigt
     *
     * Siehe Übung Todo-List, da haben wir das gemacht
     */

    private void setupListView() {
        ListView recipeListView = (ListView) findViewById(R.id.recipe_list);
        //Create Adapter

        /*List<RecipeListDatabase.Recipt> reciptList = new ArrayList<>();

        //set adapter
        recipeListView.setAdapter(todo_items_adapter);

        //setOnItemLongClickListener
        recipeListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                //call to remove
                itemLongClicked(position);
                return false;
            }
        });
        */
    }

}
