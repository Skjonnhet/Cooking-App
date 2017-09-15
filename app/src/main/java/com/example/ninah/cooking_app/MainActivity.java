package com.example.ninah.cooking_app;

import android.content.ActivityNotFoundException;
import android.content.Intent;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.app.ActionBar;
import com.example.ninah.cooking_app.RecipeActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    TextView recipeList;
    TextView bestRecipe;
    TextView fastRecipe;
    TextView randomRecipe;
    TextView newRecipe;
    Button testTimerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recipeList = (TextView)findViewById(R.id.viewRecipeList);
        randomRecipe = (TextView)findViewById(R.id.viewRandomRecipe);
        bestRecipe = (TextView)findViewById(R.id.viewBestRecipe);
        fastRecipe = (TextView)findViewById(R.id.viewFastRecipe);
        newRecipe = (TextView) findViewById(R.id.viewNewRecipe);
        testTimerButton=(Button) findViewById(R.id.testTimerButton);
        recipeList.setOnClickListener(this);
        bestRecipe.setOnClickListener(this);
        fastRecipe.setOnClickListener(this);
        randomRecipe.setOnClickListener(this);
        newRecipe.setOnClickListener(this);
        onClickForTesting();

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.viewRecipeList:
                Intent recipeListIntent = new Intent(MainActivity.this, RecipeList.class);
                startActivity(recipeListIntent);
                break;
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
            case R.id.viewNewRecipe:
                Intent newRecipeIntent = new Intent(MainActivity.this, RecipeNew.class);
                startActivity(newRecipeIntent);
                break;
            default:
                break;
        }

    }

    private void onClickForTesting(){
        //tests timer
        //timer alarm sound is always the same song in (the raw file) with different names
        //should be changed to different sounds soon
        if(testTimerButton!=null) {
            testTimerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent intent = new Intent(MainActivity.this, Rating_Activity.class);
                        startActivity(intent);
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(MainActivity.this, "Not installed.", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }

        else Log.d("MainActivity", "testTimerButtonIsNull");
    }
}
