package com.example.ninah.cooking_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class RecipeNewActivity extends AppCompatActivity implements View.OnClickListener{
    /*****************Main Activity of the recipe function of the app*******************************/
    /*User can write a new recipe or change an old one. Depends on the value of the NEW_RECIPE_ boolean */
    /*which is sended by the activities who start this RecipeNewActivity.class
    * boolean:true: a new recipe is created on the DB and the user fills the recipe witch values*
    * boolean:false: an old recipe is loaded from the DB to fill this recipe, user can than change values*/
    //----------------------------------------------------------------------------------------------------
    /*User has to fill all fields. After that he can add new ingridentsList and worksteps to this recipe
    * by starting other activities
    * startIngridentActivity(): sends intent with recipe id to IngridentActivity.class
    * startWorkSteoActivity(): sends intent with recipe id to WorkStepActivity.class
    * both activities use the recipe id to connect their ingridentsList and worksteps with this recipe
    *
    * user freedom is limited to avoid wrong user input and to allow better usability of the app
    * */



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
    Recipe actitivityRecipe;
    boolean isNewRecipe;
    boolean defaultBoolean;
    Long defaultValue;
    Long oldRecipeID;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_new);
        initDefaultValues();
        initAdapter();
        initTextViews();
        initEditTexts();
        initButtons();
      //  setOnClickListener();
        //---------------------------//keep order!---------------------------------------------------
        setIsNewRecipeBoolean();    //1.checks boolean if recipe is new or old
        setOldRecipeID();           //2.sets oldRecipeID
        createActivityRecipe();     //3.creates the recipe of this activity with boolean and oldRecipeID
                                   //keep order to avoid Exceptions
        //------------------------------------------------------------------------------------------

    }

    //*
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonSave: if(saveRecipeToDB()){startRecipeStartActivity();}
                giveFeedback("onClick", "saveRecipeToDB()");
                break;
            case R.id.newIngridentsButton: startIngridentActivity();
                giveFeedback("onClick", "startIngridentActivity()");
                break;
            case R.id.newWorkStepsButton: startWorkStepActivity();
                giveFeedback("onClick", "startWorkStepActivity()");
                break;


            default:
                break;

        }
    }

    //-----------------------------init methods--------------------------------------------------
    //used in onCreate()

    //inits default values
    private void initDefaultValues(){
        defaultValue=CookingConstants.DEFAULT_RECIPE_ID;
        defaultBoolean=false;
        oldRecipeID=defaultValue;
        isNewRecipe=defaultBoolean;
    }

    //inits adapater
    private void initAdapter(){
        dbAdapter=new DBAdapter(this);
    }

    //inits all textViews
    private void initTextViews(){
        recipeNameTextView = (TextView)findViewById(R.id.recipeName);
        difficultLevelTextView = (TextView)findViewById(R.id.difficultLevel);
        recipePortionsTextView = (TextView)findViewById(R.id.recipePortions);
        recipeTimeTextView = (TextView)findViewById(R.id.recipeTime);
    }

    //inits all editText
    private void initEditTexts(){
        recipeNameInput = (EditText)findViewById(R.id.recipeNameInput);
        difficultInput = (EditText)findViewById(R.id.DifficultInput);

        recipePortionsInput = (EditText)findViewById(R.id.recipeNumberOfPortions);
        recipePortionsInput.setTransformationMethod(null);

        recipeTimeInput = (EditText)findViewById(R.id.recipeTimeInput);
        recipeTimeInput.setTransformationMethod(null);
    }

    //inits all buttons and set clickListener
    private void initButtons(){
        buttonSave = (Button) findViewById(R.id.buttonSave);
        ingridentsButton= (Button) findViewById(R.id.newIngridentsButton);
        workStepsButton=(Button) findViewById(R.id.newWorkStepsButton);
        buttonSave.setOnClickListener(this);
        ingridentsButton.setOnClickListener(this);
        workStepsButton.setOnClickListener(this);
    }

    //checks trough intent if recipe is new
    //depending on that the newactivityy is filled with old values or not
    private void setIsNewRecipeBoolean(){
        isNewRecipe=defaultBoolean;

        try {
            Intent intent=getIntent();
            isNewRecipe=intent.getBooleanExtra(CookingConstants.NEW_RECIPE_KEY, defaultBoolean);
        }

        catch (Exception e){giveFeedback("setIsNewRecipeBoolean", e.toString());}

    }

    //sets recipeID trough intent
    private void setOldRecipeID(){
        oldRecipeID=defaultValue;
        try{
            Intent intent=getIntent();
            if(intent!=null)
            oldRecipeID=intent.getLongExtra(CookingConstants.RECIPE_ID_KEY, defaultValue);
        }
        catch (Exception e){giveFeedback("setOldRecipeID ",e.toString() );}

    }

    //creates a new recipe for this actitivity
    //depending on the value isNewRecipe
    //if false: actitivityRecipe is set to an old recipe by its recipe id+textViews are filled with this recipe
    //if true:  actitivityRecipe is set to a defaultRecipeValue so a new recipeID is generated
    private void createActivityRecipe(){
        if(isNewRecipe)
        {
            actitivityRecipe =dbAdapter.createNewRecipe();
            oldRecipeID= actitivityRecipe.getId();
            giveFeedback("createActivityRecipe","recipe: "+actitivityRecipe.getName()+" is new recipeID:"+oldRecipeID);
        }

        else {
            actitivityRecipe =dbAdapter.getRecipeByID(oldRecipeID);
            actitivityRecipe.getName();
            giveFeedback("createActivityRecipe","recipe:"+actitivityRecipe.getName()+" is old recipeID:"+oldRecipeID);
            updateTextViewsWithOldRecipe();
        }

    }

    //fills textViews with the values of the recipe
    private void updateTextViewsWithOldRecipe(){
        if(actitivityRecipe !=null){
            recipeNameInput.setText("");
            recipeNameInput.setText(actitivityRecipe.getName().toString());
            recipePortionsInput.setText(""+ actitivityRecipe.getPortions());
            recipeTimeInput.setText(""+ actitivityRecipe.getTimeInMinutes());
            difficultInput.setText(actitivityRecipe.getDifficulty().toString());
            giveFeedback("updateTextViewsWithOldRecipe", "updatet");
        }

        else {giveFeedback("updateTextViewsWithOldRecipe", "actitivityRecipe is null");}
    }


    //-----------------------start other activities-------------------------------------------------
    //used in clickListener


    //starts IngridentActivity
    //intent contains recipeID
    //checks if user forgot to fill all fields
    private void startIngridentActivity(){
        if(areAllFieldsFilled()){
            try {

                Intent intent = new Intent(this, IngridentActivity.class);
                Long id = getActivityRecipe().getId();
                giveFeedback("startWorkStepActivity", "recipe-recipeID:" + id);
                intent.putExtra(CookingConstants.RECIPE_ID_KEY, id);
                startActivity(intent);
            }catch (Exception e) {giveFeedback("startIngridentActivity", e.toString());}
        }
        else  Toast.makeText(this, "Eine Rezept Eingabe ist leer: Prüfe Name-, Menge und Zeit", Toast.LENGTH_LONG).show();
    }

    //starts WorkStepActivity
    //intent contains recipeID
    //checks if user forgot to fill all fields
    private void startWorkStepActivity(){
        if(areAllFieldsFilled()){
            try {

                Intent intent = new Intent(this, WorkStepActivity.class);
                Long id = getActivityRecipe().getId();
                giveFeedback("startWorkStepActivity", "recipe-recipeID:" + id);
                intent.putExtra(CookingConstants.RECIPE_ID_KEY, id);
                startActivity(intent);
            }catch (Exception e) {giveFeedback("startWorkStepActivity", e.toString());}

        }
        else  Toast.makeText(this, "Eine Rezept Eingabe ist leer: Prüfe Name-, Menge und Zeit", Toast.LENGTH_LONG).show();
    }

    private void startRecipeStartActivity(){

        if(areAllFieldsFilled()){
            try {

                Intent intent = new Intent(this, RecipeStartActivity.class);
                Long id = getActivityRecipe().getId();
                giveFeedback("startRecipeStartActivity", "recipe-recipeID:" + id);
                intent.putExtra(CookingConstants.RECIPE_ID_KEY, id);
                startActivity(intent);
            }catch (Exception e) {giveFeedback("startRecipeStartActivity", e.toString());}

        }
        else  Toast.makeText(this, "Eine Rezept Eingabe ist leer: Prüfe Name-, Menge und Zeit", Toast.LENGTH_LONG).show();
    }


    //-----------------------save recipe to DB------------------------------------------------------
    //used in ClickListener

    //updates this activitiy recipe
    //MUST be used before saving whole recipe
    private void updateActivityRecipe(Recipe recipe){
        recipe.setName(getRecipeName());
        recipe.setPortions(getPortions());
        recipe.setDifficulty(getDifficulty());
        recipe.setTimeInMinutes(getTimeInMinutes());
        dbAdapter.updateRecipe(recipe);
    }

    //saves whole recipe to DB WITH ingridentsList and worksteps
    //checks if user filled all fields and recipe is new
    private boolean saveRecipeToDB() {
        if (areAllFieldsFilled()) {
            updateActivityRecipe(actitivityRecipe);
            Recipe recipe = getActivityRecipe();
            if(isNewRecipe){
              return   progressNewRecipe(recipe);
            }
            else return updateOldRecipe(recipe);

        }else Toast.makeText(this, "Eine Rezept Eingabe ist leer: Prüfe Name-, Menge und Zeit", Toast.LENGTH_LONG).show();

        return false;
    }

    //checks if the recipeName is new before saving it to DB
    private boolean progressNewRecipe(Recipe recipe){
        if (isRecipeNameNew(recipe)) {
          return   progressRecipeToDB(recipe);
        } else{
            Toast.makeText(this, "Den Rezeptnamen gibt es schon. Bitte neuen aussuchen", Toast.LENGTH_LONG).show();
           return false;
        }
    }

    //just saves the recipe to DB
    private boolean updateOldRecipe(Recipe recipe){
       return progressRecipeToDB(recipe);
    }

    //constructs a new whole recipe and saves it to the db
    private boolean progressRecipeToDB(Recipe recipe){
        List<Ingrident> ingridentList = dbAdapter.getAllIngridentsOfRecipe(recipe);
        List<RecipeWorkStep> workStepList = dbAdapter.getAllWorkStepsOfRecipe(recipe);

        if (recipe != null | ingridentList != null | workStepList != null) {
            dbAdapter.saveRecipeToDB(recipe, ingridentList, workStepList);
            Toast.makeText(this, "Rezept " + recipe.getName() + " gespeichert! ", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            giveFeedback("saveRecipeToDB", "one paramter is null!");
            Toast.makeText(this, "Eine Eingabe ist leer: Prüfe Rezept-, Zutaten und Arbeitsschritteingaben", Toast.LENGTH_LONG).show();
            return false;
        }

    }



    //-----------------------getter methods: return values for recipe from inputs-------------------
    //mainly used in other methofs to read out values from user input

    //returns actitivityRecipe
    private Recipe getActivityRecipe(){
        return actitivityRecipe;
    }

    //returns recipe name
    private String getRecipeName(){
        String name="";
        if(recipeNameInput!=null){
            name=""+recipeNameInput.getText().toString();
        }

        return name;
    }

    //returns recipe difficulty
    private String getDifficulty(){
        String difficulty="";
        if(difficultInput!=null){
            difficulty=difficultInput.getText().toString();
        }


        return difficulty;
    }

    //returns recipe time
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

    //returns recipe portions
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


    //checks if String can be parsed to an integer
    //avoids wrong user input
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

    //checks if user filled all Fields
    //avoids wrong user input
    private boolean areAllFieldsFilled(){
      if(isEditTextEmpty(recipeNameInput)|isEditTextEmpty(difficultInput)|isEditTextEmpty(recipePortionsInput)|isEditTextEmpty(recipeTimeInput)){
          return false;
      }

      else return true;
    }

    //checks if EditText is empty
    //used in areAllFieldsFilled()
    private boolean isEditTextEmpty(EditText editText){
       if (editText.getText().toString().trim().isEmpty()) return true;
        else return false;

    }

    //checks if the recipe name is already in the DB should be max 1
    private boolean isRecipeNameNew(Recipe recipe){
        boolean isNew=false;
        if(recipe!=null) {
            try {
                Log.d("isRecipeNameNew ", "recipe name " +recipe.getName());
                List<Recipe> recipes = dbAdapter.getAllRecipesWithThisName(recipe.getName());
                if (recipes.isEmpty() || recipe == null||recipes.size()==1 ) {
                    isNew = true;
                }
            } catch (Exception e) {
            }
        } else {  Log.d("isRecipeNameNew ", "recipe is null");}

        return isNew;
    }

    private void giveFeedback(String method, String feedback) {
        Log.d("RecipeNewActivity " + method, feedback);
    }

}
