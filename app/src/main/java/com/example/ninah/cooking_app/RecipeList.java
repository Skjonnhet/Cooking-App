package com.example.ninah.cooking_app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

public class RecipeList extends AppCompatActivity {

    ListView recipeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);
        recipeList = (ListView)findViewById(R.id.recipe_list);
    }

    /**
     * Wir brauchen hier eine Funktion, die alle Listeneinträge (Überschriften) anzeigt und
     * dann je nachdem, auf welchen man klickt, die Rezept-Activity öffnet und das entsprechende
     * Rezept anzeigt
     *
     * Siehe Übung Todo-List, da haben wir das gemacht
     */
}
