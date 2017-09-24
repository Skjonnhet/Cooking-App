package com.example.ninah.cooking_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class RecipeStartActivity extends AppCompatActivity {
    /*****************Shows recipe to the user so he can cook*****************************/
    /*User sees all fields of the recipe filled.
    * he can work through the single steps of the recipe and
    * start the CookingTimerActivity to start a countdown for his recipe or
    * start the RatingActivity to rate and change the recipe
    * both activities use the recipe id to get time or rating of this recipe
    *
    * */


    ListView ingredientsListView;
    ListView workStepListView;
    TextView titleTextView;
    TextView servingsTextView;
    EditText numberOfServingsEditText;
    RatingBar ratingBar;
    CheckBox checkBox;
    Button rateRecipeButton;
    Button plusButton;
    Button minusButton;
    Button startTimerButton;
    DBAdapter dbAdapter;
    ArrayAdapter arrayAdapterWorkSteps;
    ArrayAdapter arrayAdapterIngridents;
    IngridentListViewAdapter ingridentListViewAdapter;
    ArrayList<String> workstepList;
    ArrayList<String> ingridentStringList;
    List<Ingrident> ingridentsList;
    Long recipeId;
    Long defaultValue;
    double amountPerPerson;
    Recipe activityRecipe;
    static int oldNumberOfServings;
    static int newNumberOfServings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        initListViews();
        ratingBar = (RatingBar)findViewById(R.id.ratingBar);
        initDefaultValues();
        initTextViews();
        initEditTexts();
        initButtons();
        initLists();
        setClickListener();

        //-------------//keep order!---------------------------------------------------------------
        setRecipeId();//1. get recipeID
        initAdapters();//2.initAdapters
        getActivitiyRecipe();//3.get recipe
        fillViewsWithRecipe();//4.fills views with recipeID
        //keep order to avoid exceptions
        //-----------------------------------------------------------------------------------------

        initStartValues();
    }


    //----------------------init part--------------------------------------------------------------
    //methods are used in onCreate()

    //inits default valeus
    private void initDefaultValues(){
        defaultValue= CookingConstants.DEFAULT_RECIPE_ID;
        recipeId=defaultValue;
        oldNumberOfServings=CookingConstants.DEFAULT_RECIPE_PORTIONS;
        newNumberOfServings=CookingConstants.DEFAULT_RECIPE_PORTIONS;
        amountPerPerson =CookingConstants.DEFAULT_INGRIDENT_MENGE/CookingConstants.DEFAULT_RECIPE_PORTIONS;
    }

    //inits start values
    private void initStartValues(){
       oldNumberOfServings=activityRecipe.getPortions();
    }

    //inits the textviews
    private void initTextViews(){
        titleTextView = (TextView)findViewById(R.id.title);
        servingsTextView = (TextView)findViewById(R.id.servings);

          }
    //inits edit texts
    //sets keylistener of numberOfServingsEditText null to avoid user uses it as input
    private void initEditTexts(){
        numberOfServingsEditText = (EditText) findViewById(R.id.act_recipe_numberOfServings);
        numberOfServingsEditText.setInputType(InputType.TYPE_NULL);
        numberOfServingsEditText.setKeyListener(null);
    }

    //inits listviews
    private void initListViews(){
        ingredientsListView = (ListView)findViewById(R.id.ingredients);
        workStepListView=(ListView)findViewById(R.id.workstepText);
        workStepListView.setClickable(true);
        workStepListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }

    //inits the buttons
    private void initButtons(){
        plusButton = (Button)findViewById(R.id.plus);
        minusButton = (Button)findViewById(R.id.minus);
        startTimerButton = (Button)findViewById(R.id.ready_button);
        rateRecipeButton=(Button) findViewById(R.id.reStAc_rateRecipeButton);

    }

    //inits all lists
    private void initLists(){
        workstepList=new ArrayList<>();
        ingridentStringList =new ArrayList<>();
        ingridentsList =new ArrayList<>();
    }



    //inits the adapter for the ingridents- and workstepslistadapters
    private void initAdapters(){
        dbAdapter=new DBAdapter(this);
        arrayAdapterWorkSteps=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, workstepList);
        arrayAdapterIngridents =new ArrayAdapter<String> (this, android.R.layout.simple_list_item_multiple_choice, ingridentStringList);
        ingridentsList= dbAdapter.getAllIngridentsOfRecipe(getActivitiyRecipe());
        ingridentListViewAdapter=new IngridentListViewAdapter(this,this,ingridentsList);
        arrayAdapterWorkSteps=new ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice, workstepList);
        Log.d("RecipeStartActivity","initAdapters : ingridentsList size"+ingridentsList.size());

        ingredientsListView.setAdapter(ingridentListViewAdapter);
        workStepListView.setAdapter(arrayAdapterWorkSteps);


        arrayAdapterWorkSteps.notifyDataSetChanged();
        ingridentListViewAdapter.notifyDataSetChanged();

    }

    //returns activity according to the recipeID
    //sets the activityRecipe
    private Recipe getActivitiyRecipe(){
        this.activityRecipe=dbAdapter.getRecipeByID(recipeId);
        return activityRecipe;
    }


    //get recipeIdFromIntent
    private void setRecipeId(){
        recipeId=defaultValue;
        try{
            Intent intent=getIntent();
            Bundle extras=intent.getExtras();
            recipeId=extras.getLong(CookingConstants.RECIPE_ID_KEY);
            Log.d("RecipeStartActivity","setRecipeId recipeID: "+recipeId);
        }
        catch (Exception e){Log.d("RecipeStartActivity","setRecipeId recipeID: "+e.toString());}
    }

    //sets ClickListener
    private void setClickListener() {
        startTimerButton.setOnClickListener(new View.OnClickListener() {
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

        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    changeNumberOfServings(true);
                    rise();
                    ingridentListViewAdapter.notifyIngridentsChanged();
                } catch (Exception e) {
                }
            }
        });
        minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    changeNumberOfServings(false);
                    lower();

                } catch (Exception e) {
                }
            }
        });


    }



    //fills views with recipe values
    private void fillViewsWithRecipe(){
        if(activityRecipe!=null) {
            try {
                titleTextView.setText(activityRecipe.getName());
                numberOfServingsEditText.setText("" + activityRecipe.getPortions());
                Log.d("RecipeStartActivity", "numberOfServingsEditText " + activityRecipe.getPortions());
                ratingBar.setRating(activityRecipe.getRatingInStars());
                fillIngridentsList(activityRecipe);
                fillWorkStepList(activityRecipe);
            } catch (Exception e) {
                Log.d("RecipeStartActivity", "fillViewsWithRecipe " + e.toString());
            }
        }
        else {Log.d("RecipeStartActivity","fillViewsWithRecipe recipeID is null");}
    }

    //fills lists with ingridents values
    //transform values into one String for ingridentStringList
    private void fillIngridentsList(Recipe recipe){
        try {
            ingridentsList = dbAdapter.getAllIngridentsOfRecipe(recipe);
            for (Ingrident ingrident : ingridentsList) {

                String name = ingrident.getName();
                double menge = ingrident.getMenge();
                String einheit = ingrident.getEinheit();
                String ingridentFormat = "Zutat: " + name + ", Menge: " + menge + " " + einheit;
                this.ingridentStringList.add(ingridentFormat);
            }
        }
        catch (Exception e){Log.d("RecipeStartActivity","fillIngridentsList "+e.toString());}
        arrayAdapterIngridents.notifyDataSetChanged();
    }

    //fills lists with worksteps values
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


    //--------------------------intent part---------------------------------------------------------
    //is used to start other activities

    //starts CookingTimerActivity
    //intent has recipeId
    private void startTimerActivity(){
        Intent intent=new Intent(this, CookingTimerActivity.class);
        intent.putExtra(CookingConstants.RECIPE_ID_KEY,recipeId);
        startActivity(intent);
        Log.d("RecipeStartActivity","startTimerActivity" );
    }

    //starts Rating_Activity
    //intent has recipeId
    private void startRatingActivity(){
        Intent intent=new Intent(this, Rating_Activity.class);
        intent.putExtra(CookingConstants.RECIPE_ID_KEY,recipeId);
        startActivity(intent);
        Log.d("RecipeStartActivity","startRatingActivity" );
    }

    //-------------------calculate part-------------------------------------------------------------
    //methods caculate the amounts of the ingridents

    //calculates the amount with the int/double-values of new number of servings/persons, old amount of recipe, and old number of servings/persons
    private double calculateAmount(int newNumberOfServings, double oldAmount, int oldNumberOfServings ){
        double minValue=1;
        double oldAmountInIngridient=roundDouble(oldAmount);
        if(oldAmountInIngridient<=minValue)oldAmountInIngridient=minValue;
        double factor= (oldAmountInIngridient/oldNumberOfServings);
        factor=roundDouble(factor);
        if(factor<=minValue)factor=minValue;
        double result=factor*newNumberOfServings;
        result=roundDouble(result);
        if(result<=minValue)result=minValue;
        Log.d("calculateAmount","oldAmount :"+oldAmount+  "/oldNumberOfServings: "+oldNumberOfServings+" factor: "+factor );
        Log.d("calculateAmount ","factor"+factor+ " *newNumberOfServings "+newNumberOfServings+ " =result: "+result);
        return result;
    }

    //rounds values two two number after comma
    private double roundDouble(double d){
        return Math.round((d*100))/100;
    }


    //calculates the amount of a single ingrident
    private double calcaulteIngridentAmount(int newNumberOfServings,Ingrident ingrident, int oldNumberOfServings){
        double amountNew=CookingConstants.DEFAULT_INGRIDENT_MENGE;
        try {
             double oldAmount=ingrident.getMenge();
             amountPerPerson =activityRecipe.getPortions();
             amountNew=calculateAmount(newNumberOfServings, oldAmount, oldNumberOfServings);
        }
        catch (Exception e){}

        return amountNew;

    }

    //updates the amount of all IngridentsObjects in the list and the listview
    //tell adapter that ingridentslist has changed
    private void updateAmountOfIngridentObjects(int newNumberOfPersons, int oldNumberOfServings){
        List<Ingrident> ingridentsNew=new ArrayList<>();
        for (Ingrident ingrident:ingridentsList){
            double amount=calcaulteIngridentAmount(newNumberOfPersons,ingrident, oldNumberOfServings);
            if(amount<1){
                amount=1;
            }
            amount=roundDouble(amount);
            ingrident.setMenge(amount);
            ingridentsNew.add(ingrident);
        }
        ingridentsList.clear();
        ingridentsList.addAll(ingridentsNew);


    }


    //changes the number of recipes
    //used in the plus and minus button OnCliListener
    //checks if user rises or lowers the portion
    //than caulcates the new portions through updateAmountOfIngridentObjects()
    private void changeNumberOfServings(boolean isPlus){
        try{
             oldNumberOfServings= getOldNumberOfServings();
            Log.d("changeNumberOfServings1","old"+oldNumberOfServings+"new"+newNumberOfServings);
            if(isPlus){this.newNumberOfServings=oldNumberOfServings;
                 this.newNumberOfServings++;
                Log.d("changeNumberOfServings2","old"+oldNumberOfServings+"new"+newNumberOfServings);}

            else if (!isPlus){
                    if (this.newNumberOfServings > 1)
                    {
                        this.newNumberOfServings = oldNumberOfServings;
                        this.newNumberOfServings--;
                    }

                    else {
                        this.newNumberOfServings = 1;
                    }
                }
                else {
                Toast.makeText(this,"Probleme beim rechnen. Bitte selbst rechnen", Toast.LENGTH_SHORT);

                }


            Log.d("changeNumberOfServings3","old"+oldNumberOfServings+"new"+newNumberOfServings);
            updateAmountOfIngridentObjects(newNumberOfServings, oldNumberOfServings);

        }
        catch (Exception e){Log.d("changeNumberOfServings", numberOfServingsEditText.getText().toString());}
        ingridentListViewAdapter.notifyIngridentsChanged();
        Log.d(" updateAmount","ingridentListViewAdapter data changed");
    }




//---------------------hepler part------------------------------------------------------------------
    //methods support other methods


    private void rise(){
        int number=1;
        try{
            number=Integer.parseInt(numberOfServingsEditText.getText().toString());
        }catch (Exception e) {Log.d("",e.toString());}

        number++;
        numberOfServingsEditText.setText("");
        numberOfServingsEditText.setText(""+number);
    }


    private void lower(){
        int minValue=1;
        int number=1;
        try{
            number=Integer.parseInt(numberOfServingsEditText.getText().toString());
        }
        catch (Exception e) {Log.d("",e.toString());}

        if(number>minValue){number--;}
        else number=minValue;

        numberOfServingsEditText.setText("");
        numberOfServingsEditText.setText(""+number);
    }


    //returns old numberOf Servings
    private int getOldNumberOfServings(){
        int numberOfPersonsInRecipe=1;
        try {
            String numberOfService= numberOfServingsEditText.getText().toString();
            Log.d("getOldNumberOfServings", ""+numberOfService);
            numberOfPersonsInRecipe=Integer.parseInt(numberOfService);
            Log.d("getOldNumberOfServings", ""+numberOfPersonsInRecipe);
        }
        catch (Exception e){Log.d("getOldNumberOfServings",e.toString());}

        return numberOfPersonsInRecipe;

    }





}
