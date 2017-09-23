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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class RecipeStartActivity extends AppCompatActivity implements View.OnClickListener {
    /*****************Shows recipe to the user so he can cook*****************************/
    /*User sees all fields of the recipe filled.
    * he can work through the single steps of the recipe and
    * start the CookingTimerActivity to start a countdown for his recipe or
    * start the RatingActivity to rate and change the recipe
    * both activities use the recipe id to get time or rating of this recipe
    *
    * */

    TextView titleTextView;
    RatingBar ratingBar;
    ListView ingredientsListView;
    TextView servingsTextView;
    EditText numberOfServingsEditText;
    Button rateRecipeButton;
    Button plusButton;
    Button minusButton;
    TextView recipe_text;
    CheckBox checkBox;
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
    private static int numberOfServings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        ingredientsListView = (ListView)findViewById(R.id.ingredients);
        ratingBar = (RatingBar)findViewById(R.id.ratingBar);
        //checkBox = (CheckBox)findViewById(R.id.checkBox);
        initDefaultValues();
        initTextViews();
        initButtons();
        initLists();
        setClickListener();
        //-------------//keep order!---------------------------------------------------------------
        setRecipeId();//1. get recipeID
        initAdapters();//2.initAdapters
        fillViewsWithRecipe();//3.fills views with recipeID
        //keep order to avoid execeptions
        //-----------------------------------------------------------------------------------------




    }

    //-------------------------------overwrite-methods---------------------------------------------
    //overwritten methods

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
            case R.id.plus:morerecipes();
                /**
                 * DB-Funktion einfügren, dass eine Portion dazu gerechnet wird
                 */
                break;
            case R.id.minus:
                /**
                 * DB-Funktion, dass eine Portion abgezogen wird
                 */
                break;
            //case R.id.checkBox:
                /**
                 * hier muss was rein, aber was?
                 * Wenn Timer-Funktion im Rezept hinterlegt ist bei diesem Arbeitsschritt, dann Weiterleitung zu Timer?
                 */
                //break;
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


    //----------------------init part--------------------------------------------------------------
    //methods are used in onCreate()



    private void morerecipes(){
        ingridentsList.containsAll(items());
        ingridentListViewAdapter.notifyDataSetChanged();
    }


    private void initDefaultValues(){
        defaultValue= CookingConstants.DEFAULT_RECIPE_ID;
        recipeId=defaultValue;
        initNumberOfPersons();
    }

    private void initNumberOfPersons(){
        numberOfServings=1;
        try{
            numberOfServings= dbAdapter.getRecipeByID(recipeId).getPortions();
        }
        catch (Exception e){}

    }

    private void initTextViews(){
        titleTextView = (TextView)findViewById(R.id.title);
        servingsTextView = (TextView)findViewById(R.id.servings);
        //recipe_text = (TextView)findViewById(R.id.recipe_text);
        numberOfServingsEditText = (EditText) findViewById(R.id.act_recipe_numberOfServings);

    }

    private void initButtons(){
        plusButton = (Button)findViewById(R.id.plus);
        minusButton = (Button)findViewById(R.id.minus);
        startTimerButton = (Button)findViewById(R.id.ready_button);
        rateRecipeButton=(Button) findViewById(R.id.reStAc_rateRecipeButton);
        plusButton.setOnClickListener(this);
        minusButton.setOnClickListener(this);
        startTimerButton.setOnClickListener(this);
        rateRecipeButton.setOnClickListener(this);


    }

    private void initLists(){
        workstepList=new ArrayList<>();
        ingridentStringList =new ArrayList<>();
        ingridentsList =new ArrayList<>();
    }

    private List<Ingrident> items(){
        List<Ingrident> list=new ArrayList<>();
        for (int i=0;i<4;i++)
        {
            Ingrident ingrident=dbAdapter.createNewIngridentWithRecipeID("z2","2",3,recipeId);
            list.add(ingrident);
        }
        return list;
    }

    private void initAdapters(){
        dbAdapter=new DBAdapter(this);
        arrayAdapterWorkSteps=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, workstepList);
        arrayAdapterIngridents =new ArrayAdapter<String> (this, android.R.layout.simple_list_item_multiple_choice, ingridentStringList);
       // ingredientsListView.setAdapter(arrayAdapterIngridents);
        ingridentsList= items();//dbAdapter.getAllIngridentsOfRecipe(dbAdapter.getRecipeByID(recipeId));
        ingridentListViewAdapter=new IngridentListViewAdapter(this,this,ingridentsList);
        Log.d("RecipeStartActivity","initAdapters : ingridentsList size"+ingridentsList.size());
        ingredientsListView.setAdapter(ingridentListViewAdapter);
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
    //sets setsClickListener
    private void setClickListener(){
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
                try{
                    changeNumberOfServings(true);
                    morerecipes();

                }
                catch (Exception e){}
            }
        });
        minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    changeNumberOfServings(false);
                }
                catch (Exception e){}
            }
        });
    }



    //fills views with recipe values
    private void fillViewsWithRecipe(){
        if(recipeId!=null){
            Recipe activityRecipe= dbAdapter.getRecipeByID(recipeId);
            if(activityRecipe!=null)
            {
                try{
                    titleTextView.setText(activityRecipe.getName());
                    numberOfServingsEditText.setText(""+activityRecipe.getPortions());
                    Log.d("RecipeStartActivity","numberOfServingsEditText "+activityRecipe.getPortions());
                    ratingBar.setNumStars(activityRecipe.getRatingInStars());
                    fillIngridentsList(activityRecipe);
                    fillWorkStepList(activityRecipe);
                }
                catch (Exception e){Log.d("RecipeStartActivity","fillViewsWithRecipe "+e.toString());}

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
        ingridentListViewAdapter.notifyDataSetChanged();



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
    private double calculateAmount(int personsOld, int personsNew, double amountOld, boolean isPlus){
        double factor= amountOld/personsOld;
        Log.d("calculateAmount amnt  :",""+factor*personsNew+ "factor: "+factor);
            return factor;
    }

    private double calcaulteIngridentAmount(Ingrident ingrident, boolean isPlus, int oldNumber, int newNumber){
        int defaultValue=1;
        double amountNew=defaultValue;
        double amountOld=defaultValue;
        int personsNew=defaultValue;
        try {
             amountOld=ingrident.getMenge();
             numberOfServings=dbAdapter.getRecipeByID(recipeId).getPortions();
            amountNew=calculateAmount(oldNumber,newNumber,amountOld, isPlus);
        }
        catch (Exception e){}

        return amountNew;

    }

    private void updateAmountOfIngridentObjects(boolean isPlus, int oldNumber, int newNumber){
        List<Ingrident> ingridentsNew=new ArrayList<>();
        for (Ingrident ingrident:ingridentsList){
            double amount=calcaulteIngridentAmount(ingrident, isPlus, oldNumber, newNumber);
            if(amount<1)amount=1;
            ingrident.setMenge(amount);
            Log.d(" updateAmount","calcIngriAmount" +amount);
            ingridentsNew.add(ingrident);
        }
        ingridentsList.clear();
        ingridentsList.addAll(ingridentsNew);
        arrayAdapterIngridents.notifyDataSetChanged();
        Log.d(" updateAmount","arrayAdapterIngridentsnotifyDataSetChanged");
    }

    private void changeNumberOfServings(boolean isPlus){

        try{
            String numberOfService= numberOfServingsEditText.getText().toString();
            if(numberOfService=="0")numberOfService="1";
            int oldNumber=Integer.parseInt(numberOfService);
            int newNumber=1;
            if(isPlus){newNumber++;}
            else {if(newNumber>1)
                newNumber--;
            }
            updateAmountOfIngridentObjects(isPlus,oldNumber, newNumber);

            Log.d("changeNumberOfServings ","old"+oldNumber+"new"+newNumber);
            numberOfServingsEditText.setText("");
            numberOfServingsEditText.setText(""+newNumber);

        }
        catch (Exception e){Log.d("changeNumberOfServings", numberOfServingsEditText.getText().toString());}

    }




}
