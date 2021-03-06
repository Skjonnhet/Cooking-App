package com.example.ninah.cooking_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class RecipeNewActivity extends AppCompatActivity implements View.OnClickListener{

    TextView recipeNameTextView;
    TextView difficultLevelTextView;
    TextView recipePortionsTextView;
    TextView recipeTimeTextView;
    EditText recipeNameInput;
    EditText difficultInput;
    EditText recipePortionsInput;
    EditText recipeTimeInput;
    Button buttonSave;
    Button ingridentsButton;
    Button workStepsButton;
    DBAdapter dbAdapter;
    Recipe actiticityRecipe;
    boolean isNewRecipe;
    Long oldRecipeID;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_new);

        dbAdapter=new DBAdapter(this);

        recipeNameTextView = (TextView)findViewById(R.id.recipeName);
        difficultLevelTextView = (TextView)findViewById(R.id.difficultLevel);
        recipePortionsTextView = (TextView)findViewById(R.id.recipePortions);
        recipeTimeTextView = (TextView)findViewById(R.id.recipeTime);

        recipeNameInput = (EditText)findViewById(R.id.recipeNameInput);
        difficultInput = (EditText)findViewById(R.id.DifficultInput);

        recipePortionsInput = (EditText)findViewById(R.id.recipeNumberOfPortions);
        recipePortionsInput.setTransformationMethod(null);

        recipeTimeInput = (EditText)findViewById(R.id.recipeTimeInput);
        recipeTimeInput.setTransformationMethod(null);

        buttonSave = (Button) findViewById(R.id.buttonSave);
        ingridentsButton= (Button) findViewById(R.id.newIngridentsButton);
        workStepsButton=(Button) findViewById(R.id.newWorkStepsButton);
        buttonSave.setOnClickListener(this);

                                    //keep order!
        setIsNewRecipeBoolean();    //1.checks boolean if recipe is new or old
        setOldRecipeID();           //2.sets oldRecipeID
        createActivityRecipe();     //3.creates the recipe of this activity with boolean and oldRecipeID
        setOnClickListener();       //4.sets ClickListener

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonSave: saveRecipeToDB();
                giveFeedback("onClick", "saveRecipeToDB()");
                break;
            case R.id.newIngridentsButton: startIngridentActivity();
                giveFeedback("onClick", "startIngridentActivity()");
                break;
            case R.id.newWorkStepsButton: startWorkStepActivity();
                giveFeedback("onClick", "startWorkStepActivity()");
                break;
                /**
             * speichern oder activites aufrufen
             */


            default:
                break;

        }
    }

    //-----------------------------init methods--------------------------------------------------

    private void setIsNewRecipeBoolean(){
        Intent intent=getIntent();
        Bundle extras=intent.getExtras();
        isNewRecipe=extras.getBoolean(CookingConstants.NEW_RECIPE_KEY);
    }

    private void setOldRecipeID(){
        Intent intent=getIntent();
        Bundle extras=intent.getExtras();
        oldRecipeID=extras.getLong(CookingConstants.RECIPE_ID_KEY);
    }

    //creates a new recipe for this actitivity
    private void createActivityRecipe(){
        if(isNewRecipe)
        {
            actiticityRecipe =dbAdapter.createDefaultRecipeAndSaveItToDB();
            oldRecipeID=actiticityRecipe.getId();
            giveFeedback("createActivityRecipe","recipe is new id:"+oldRecipeID);
        }

        else {
            actiticityRecipe=dbAdapter.getRecipeByID(oldRecipeID);
            giveFeedback("createActivityRecipe","recipe is old id:"+oldRecipeID);
            updateTextViewsWithOldRecipe();
        }

    }

    private void updateTextViewsWithOldRecipe(){
        if(actiticityRecipe!=null){
            recipeNameInput.setText(actiticityRecipe.getName().toString());
            recipePortionsInput.setText(""+actiticityRecipe.getPortions());
            recipeTimeInput.setText(""+actiticityRecipe.getTimeInMinutes());
            difficultInput.setText(actiticityRecipe.getDifficulty().toString());
        }
    }


    private void setOnClickListener(){
       ingridentsButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               startIngridentActivity();
           }
       });

        workStepsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startWorkStepActivity();
            }
        });
    }


    //-----------------------start other activities-------------------------------------------------

    private void startIngridentActivity(){
        Intent intent=new Intent(this, IngridentActivity.class);
        Long id=getActivityRecipe().getId();
        giveFeedback("startIngridentActivity", "recipe-id:"+id);
        intent.putExtra(CookingConstants.RECIPE_ID_KEY,id);
        startActivity(intent);
    }

    private void startWorkStepActivity(){
        Intent intent=new Intent(this, WorkStepActivity.class);
        Long id=getActivityRecipe().getId();
        giveFeedback("startWorkStepActivity", "recipe-id:"+id);
        intent.putExtra(CookingConstants.RECIPE_ID_KEY,id);
        startActivity(intent);
    }


    //-----------------------save recipe to DB------------------------------------------------------




    //updates this activitiy recipe
    //MUST be used before saving whole recipe
    private void updateActivityRecipe(Recipe recipe){
        recipe.setName(getRecipeName());
        recipe.setPortions(getPortions());
        recipe.setDifficulty(getDifficulty());
        recipe.setTimeInMinutes(getTimeInMinutes());
        dbAdapter.updateRecipe(recipe);
    }




    //saves whole recipe to DB WITH ingridents and worksteps
    private void saveRecipeToDB(){
        updateActivityRecipe(actiticityRecipe);
        Recipe recipe=getActivityRecipe();
        List<Ingrident> ingridentList=dbAdapter.getAllIngridentsOfRecipe(recipe);
        List<RecipeWorkStep> workStepList=dbAdapter.getAllWorkStepsOfRecipe(recipe);

        if(recipe!=null| ingridentList!=null | workStepList!=null){
                dbAdapter.saveRecipeToDB(recipe,ingridentList,workStepList);
        }

        else {
            giveFeedback("saveRecipeToDB", "one paramter is null!");
            Toast.makeText(this,"Eine Eingabe ist leer: Prüfe Rezept-, Zutaten und Arbeitsschritteingaben", Toast.LENGTH_LONG).show();
        }
    }

    //-----------------------getter methods: return values for recipe from inputs------------------------------------------------------


    private Recipe getActivityRecipe(){
        return actiticityRecipe;
    }

    private String getRecipeName(){
        String name="";
        if(recipeNameInput!=null){
            name=recipeNameInput.toString();
        }

        return name;
    }

    private String getDifficulty(){
        String difficulty="";
        if(difficultInput!=null){
            difficulty=difficultInput.toString();
        }


        return difficulty;
    }

    private int getTimeInMinutes(){
        int time=0;
        if(recipeTimeInput!=null){
            try {
              String number=  recipeTimeInput.getText().toString();
                if(isNumber(number)){
                    time=Integer.parseInt(number);
                }
                else {
                    time=0;
                }
            }
            catch (Exception e){giveFeedback("getTimeInMinutes",e.toString());}
        }

        return time;
    }

    private int getPortions(){
        int portions=1;
        try {
            String number=  recipePortionsInput.getText().toString();

            if(isNumber(number)){
                portions=Integer.parseInt(number);
            }

            else {
                portions=1;
            }
        }

        catch (Exception e){giveFeedback("getPortions",e.toString());}

        return portions;
    }



  //  ---------------------------helper-methods---------------------------------------------

    private void giveFeedback(String method, String feedback) {
        Log.d("RecipeNewActivity " + method, feedback);
    }

    private boolean isNumber(String s){
        try{
            Integer.parseInt(s);
        }

        catch (NumberFormatException e)
        {
            giveFeedback("isNumber ",s+ " is NotANumber");
            return false;
        }

        return true;

    }
}
