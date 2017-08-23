package com.example.ninah.cooking_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.ninah.cooking_app.RecipeActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    TextView bestRecipe;
    TextView fastRecipe;
    TextView randomRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        randomRecipe = (TextView)findViewById(R.id.viewRandomRecipe);
        bestRecipe = (TextView)findViewById(R.id.viewBestRecipe);
        fastRecipe = (TextView)findViewById(R.id.viewFastRecipe);

        bestRecipe.setOnClickListener(this);
        fastRecipe.setOnClickListener(this);
        randomRecipe.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.viewBestRecipe:
                /** hier gehört eine Logik rein, die das beste Rezept aufruft**/
                Intent bestRecipeIntent = new Intent(MainActivity.this, RecipeActivity.class);
                startActivity(bestRecipeIntent);
                break;
            case R.id.viewFastRecipe:
                /** hier gehört eine Logik rein, die das schnellste Rezept aufruft**/
                Intent fastRecipeIntent = new Intent(MainActivity.this, RecipeActivity.class);
                startActivity(fastRecipeIntent);
                break;
            case R.id.viewRandomRecipe:
                /** hier gehört eine Logik rein, die ein zufälliges Rezept aufruft**/
                Intent randomRecipeIntent = new Intent(MainActivity.this, RecipeActivity.class);
                startActivity(randomRecipeIntent);
                break;
            default:
                break;
        }
    }
}
