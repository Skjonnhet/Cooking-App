package com.example.ninah.cooking_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class RecipeStartActivity extends AppCompatActivity implements View.OnClickListener {

    TextView title;
    RatingBar ratingBar;
    ListView ingredientsListView;
    TextView servings;
    TextView numberOfServings;
    Button rateRecipeButton;
    Button plus;
    Button minus;
    TextView recipe_text;
    CheckBox checkBox;
    Button startTimer;
    Long recipeId;
    DBAdapter dbAdapter;
    ArrayAdapter arrayAdapterWorkSteps;
    ArrayAdapter arrayAdapterIngridents;
    ArrayList<String> workstepList;
    ArrayList<String>  ingridentList;
    Recipe activityRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        title = (TextView)findViewById(R.id.title);
        ratingBar = (RatingBar)findViewById(R.id.ratingBar);
        ingredientsListView = (ListView)findViewById(R.id.ingredients);
        servings = (TextView)findViewById(R.id.servings);
        numberOfServings = (TextView)findViewById(R.id.numberOfServings);
        plus = (Button)findViewById(R.id.plus);
        minus = (Button)findViewById(R.id.minus);
        recipe_text = (TextView)findViewById(R.id.recipe_text);
        checkBox = (CheckBox)findViewById(R.id.checkBox);
        startTimer = (Button)findViewById(R.id.ready_button);
        rateRecipeButton=(Button) findViewById(R.id.reStAc_rateRecipeButton);

        workstepList=new ArrayList<>();
        ingridentList=new ArrayList<>();
        dbAdapter=new DBAdapter(this);
        arrayAdapterWorkSteps=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, workstepList);
        arrayAdapterIngridents=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, ingridentList);

        ingredientsListView.setAdapter(arrayAdapterIngridents);



        setRecipeId();//1. get recipeID
        fillViewsWithRecipe();//2.fills views with recipeID

        setOnClickListener();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ratingBar:
                /**
                 * Intent zu Jonas Bewertungs-Activity
                 */

                Intent RatingIntent = new Intent( RecipeStartActivity.this, Rating_Activity.class);
                RatingIntent.putExtra(CookingConstants.RECIPE_ID_KEY, recipeId);
                startActivity(RatingIntent);

                break;
            case R.id.plus:
                /**
                 * DB-Funktion einfügren, dass eine Portion dazu gerechnet wird
                 */
                break;
            case R.id.minus:
                /**
                 * DB-Funktion, dass eine Portion abgezogen wird
                 */
                break;
            case R.id.checkBox:
                /**
                 * hier muss was rein, aber was?
                 * Wenn Timer-Funktion im Rezept hinterlegt ist bei diesem Arbeitsschritt, dann Weiterleitung zu Timer?
                 */
                break;
            case R.id.ready_button:startTimerActivity();
                /**
                 * Funktion, dass Kochvorgang gestartet wird
                 */
            default:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actions, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.actionbarEdit:
                /**
                 * hier muss eine DB-Abfrage rein, die das Formular mit vorhandenen Daten füllt
                 * @Jonas: hier wäre auch der Platz für deinen Boolean
                 */
                Intent newRecipeIntent = new Intent(RecipeStartActivity.this, RecipeNewActivity.class);
                startActivity(newRecipeIntent);
                break;
        }
        return super.onOptionsItemSelected(item); //To change body of generated methods, choose Tools | Templates.
    }

    //get recipeIdFromIntent
    private void setRecipeId(){
        Intent intent=getIntent();
        Bundle extras=intent.getExtras();
        recipeId=extras.getLong(CookingConstants.RECIPE_ID_KEY);
        Log.d("RecipeStartActivity","setRecipeId recipeID: "+recipeId);
    }

    private void setOnClickListener(){
        startTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimerActivity();
            }
        });

        rateRecipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRatingActivity();
            }
        });
    }

    private void fillViewsWithRecipe(){
        if(recipeId!=null){
            Recipe activityRecipe= dbAdapter.getRecipeByID(recipeId);
            if(activityRecipe!=null)
            {
                try{
                    title.setText(activityRecipe.getName());
                    numberOfServings.setText(""+activityRecipe.getPortions());
                    ratingBar.setNumStars(activityRecipe.getRatingInStars());
                    fillIngridentsList(activityRecipe);
                    fillWorkStepList(activityRecipe);
                }
                catch (Exception e){Log.d("RecipeStartActivity","fillViewsWithRecipe "+e.toString());}

            }
        }

        else {Log.d("RecipeStartActivity","fillViewsWithRecipe recipeID is null");}
    }

    private void fillIngridentsList(Recipe recipe){
        List<Ingrident> ingridens=dbAdapter.getAllIngridentsOfRecipe(recipe);
        for(Ingrident ingrident:ingridens){
            try{
                String name=ingrident.getName();
                double menge=ingrident.getMenge();
                String einheit=ingrident.getEinheit();
                String ingridentFormat="Zutat: "+name +", Menge: "+menge+" "+einheit;
                this.ingridentList.add(ingridentFormat);
            }
            catch (Exception e){Log.d("RecipeStartActivity","fillIngridentsList "+e.toString());}


        }
        arrayAdapterIngridents.notifyDataSetChanged();

    }

    private void riseNumberPortions(){

    }

    private void lowerNumberOfPortions(){

    }

    private void fillWorkStepList(Recipe recipe){
        List<RecipeWorkStep> workSteps=dbAdapter.getAllWorkStepsOfRecipe(recipe);
        for(RecipeWorkStep workStep:workSteps){
            try{
                String description=workStep.getWorkStepDescribition();
                workstepList.add(description);
            }
            catch (Exception e){Log.d("RecipeStartActivity","fillWorkStepList "+e.toString());
            }
        }
        arrayAdapterIngridents.notifyDataSetChanged();
    }

    private void startTimerActivity(){
        Intent intent=new Intent(this, CookingTimerActivity.class);
        intent.putExtra(CookingConstants.RECIPE_ID_KEY,recipeId);
        startActivity(intent);
        Log.d("RecipeStartActivity","startTimerActivity" );
    }

    private void startRatingActivity(){
        Intent intent=new Intent(this, Rating_Activity.class);
        intent.putExtra(CookingConstants.RECIPE_ID_KEY,recipeId);
        startActivity(intent);
        Log.d("RecipeStartActivity","startRatingActivity" );
    }
}
