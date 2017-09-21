package com.example.ninah.cooking_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;


public class RecipeStartActivity extends AppCompatActivity implements View.OnClickListener {

    TextView title;
    RatingBar ratingBar;
    ListView ingredients;
    TextView servings;
    TextView numberOfServings;
    Button plus;
    Button minus;
    TextView recipe_text;
    CheckBox checkBox;
    Button ready_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        title = (TextView)findViewById(R.id.title);
        ratingBar = (RatingBar)findViewById(R.id.ratingBar);
        ingredients = (ListView)findViewById(R.id.ingredients);
        servings = (TextView)findViewById(R.id.servings);
        numberOfServings = (TextView)findViewById(R.id.numberOfServings);
        plus = (Button)findViewById(R.id.plus);
        minus = (Button)findViewById(R.id.minus);
        recipe_text = (TextView)findViewById(R.id.recipe_text);
        checkBox = (CheckBox)findViewById(R.id.checkBox);
        ready_button = (Button)findViewById(R.id.ready_button);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ratingBar:
                /**
                 * Intent zu Jonas Bewertungs-Activity
                 */

                Intent RatingIntent = new Intent( RecipeStartActivity.this, Rating_Activity.class);
                //RatingIntent.putExtra
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
            case R.id.ready_button:
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
}
