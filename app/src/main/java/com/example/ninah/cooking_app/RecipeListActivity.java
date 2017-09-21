package com.example.ninah.cooking_app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import android.widget.AdapterView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class RecipeListActivity extends AppCompatActivity {


    //only for test
    private ArrayList<String> recipe = new ArrayList<String>();
    private ArrayAdapter<String> recipe_List_adapter;
    ListView recipeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);
        recipeList = (ListView)findViewById(R.id.recipe_list);

        setupListView();
    }

    // Datenanbindung geht nicht

    private void setupListView() {
        ListView recipeListView = (ListView) findViewById(R.id.recipe_list);

        List<DBAdapter> reciptList = new ArrayList<>();
        recipe_List_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, recipe);
        //set adapter
        recipeListView.setAdapter(recipe_List_adapter);
        //setOnItemLongClickListener
        recipeListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                //call to open

                return false;
            }
        });
    }

}
