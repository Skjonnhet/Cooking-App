package com.example.ninah.cooking_app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class RecipeNew extends AppCompatActivity implements View.OnClickListener{

    TextView recipeName;
    TextView recipeIngredients;
    TextView recipeServings;
    TextView recipeWorkstep;
    EditText recipeNameInput;
    EditText recipeIngredientsInput;
    EditText recipeServingsInput;
    EditText TexrecipeWorkstepInput;
    Button buttonSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_new);

        recipeName = (TextView)findViewById(R.id.recipeName);
        recipeIngredients = (TextView)findViewById(R.id.recipeIngredients);
        recipeServings = (TextView)findViewById(R.id.recipeServings);
        recipeWorkstep = (TextView)findViewById(R.id.recipeWorkstep);
        recipeNameInput = (EditText)findViewById(R.id.recipeNameInput);
        recipeIngredientsInput = (EditText)findViewById(R.id.recipeIngredientsInput);
        recipeServingsInput = (EditText)findViewById(R.id.recipeServingsInput);
        TexrecipeWorkstepInput = (EditText)findViewById(R.id.editTexrecipeWorkstepInput);
        buttonSave = (Button) findViewById(R.id.buttonSave);

        buttonSave.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonSave:
                /**
                 * Bitte Funktion einfügen, die dann alles in die DB speichert
                 */
          /*      int recipeIngredients = Integer.parseInt(recipeIngredientsString);
                editTextQuantity.setText("");
                editTextProduct.setText("");

                dbSource.createRecipt(recipeName,  recipeIngredients);

            */
                break;
            default:
                break;

        }
    }
}
