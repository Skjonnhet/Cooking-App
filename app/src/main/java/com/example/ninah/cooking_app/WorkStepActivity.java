package com.example.ninah.cooking_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class WorkStepActivity extends AppCompatActivity {
    /*****************WorkStepActivity of the recipe function of the app**************************/
    /*saves the user inputs into the listView trough an arrayAdapter*/
    /*receives recipeID from RecipeNewActivity via Intent */
    /*creates new worksteps with user input and tells the worksteps through the recipeID
     *to which recipe the worksteps are connected */
    /*saves worksteps to database*/
    /*deletes workstep if user clicks long on workstep*/


    private Button addButton;
    private Button saveButton;
    private EditText workStepEditText;
    private ListView listView;
    private ArrayList<String> workstepDescribitions;
    ArrayList<RecipeWorkStep> recipeWorkSteps;
    private ArrayAdapter<String> arrayAdapter;
    private Long recipeID;
    private DBAdapter dbAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_step);

        initButtons();
        initViewsAndEditTexts();
        initLists();
        initAdapters();
        setOnClickListern();
        setRecipeIDThroughIntent();
        fillListView();
    }


    //--------------------------init methods--------------------------------------------------------
    //used while OnCreate() of Lifecycle

    //inits all buttons
    private void initButtons(){
        addButton=(Button) findViewById(R.id.saveSingleWorkStepButton);
        saveButton=(Button) findViewById(R.id.saveAllWorkStepsButton);
    }

    //inits views and editTexts
    private void initViewsAndEditTexts(){
        workStepEditText=(EditText) findViewById(R.id.workStepEditText);
        listView=(ListView) findViewById(R.id.workStepsListView);
    }

    //inits all lists
    private void initLists(){
        workstepDescribitions=new ArrayList<>();
        recipeWorkSteps=new ArrayList<>();
    }

    //inits all adapters
    private void initAdapters(){
        arrayAdapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, workstepDescribitions);
        dbAdapter=new DBAdapter(this);
        listView.setAdapter(arrayAdapter);
    }

    //sets recipeID through the intent
    //MUST BE SET ON START OF ACTIVITY!
    private void setRecipeIDThroughIntent(){
        Intent intent= getIntent();
        Bundle extras=intent.getExtras();
        recipeID=extras.getLong(CookingConstants.RECIPE_ID_KEY);
        giveFeedback("setRecipeIDThroughIntent", "recipeID:"+recipeID.toString());
    }

    //sets all onclickListeners of this class
    private void setOnClickListern(){
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isEditTextEmpty(workStepEditText))
                saveWorkStepToList();
                else { tellUserToFillField();}

            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedWorkStep=((TextView) view).getText().toString();
                deleteWorkStep(selectedWorkStep);

                return false;
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveWorkStepListToDB();
                finish();

            }
        });

    }

    //fills listView, keep order of methods in this method or nullException
    private void fillListView(){
        try{
            getOldWorksStepsFromDB();
            fillListViewWithOldValues();
        }
        catch (Exception e){giveFeedback("fillListView ", e.toString());}

    }

    //fills listView with workstep-values from recipe
    private void fillListViewWithOldValues(){
        try{
            for (RecipeWorkStep workStep:recipeWorkSteps){
                workstepDescribitions.add(workStep.getWorkStepDescribition());
            }
        }
        catch (Exception e){giveFeedback("fillListViewWithOldValues ",e.toString());}
        arrayAdapter.notifyDataSetChanged();
    }


    //writes worksteps of this recipe from DB into arrayList
    private void getOldWorksStepsFromDB(){

        try {
            Recipe recipe=dbAdapter.getRecipeByID(recipeID);
            recipeWorkSteps.addAll(dbAdapter.getAllWorkStepsOfRecipe(recipe));
        }
        catch (Exception e){giveFeedback("getOldWorksStepsFromDB ",e.toString() );}
    }

    //--------------------------save and delete methods---------------------------------------------
    //used in the clickListener


    //sets single workstep to list
    private void saveWorkStepToList(){
        if(workStepEditText!=null){
            String description=workStepEditText.getText().toString();
            workStepEditText.setText(CookingConstants.EMPTY_STRING);
            workstepDescribitions.add(description);
            arrayAdapter.notifyDataSetChanged();
        }

    }

    //saves all worksteps with the workstepDescribitions to the DB
    private void saveWorkStepListToDB(){

        if(recipeID!=null){
            for (String description:workstepDescribitions){
                RecipeWorkStep recipeWorkStep=dbAdapter.createNewWorkStepWithRecipeID(description,recipeID);
            }
            tellUserWorkStepSaved();
        }
    }


    private void deleteWorkStep(String workstep){
        try{
            deleteWorkStepFromDB(getWorkstepIDbyDescription(workstep));
            deleteWorkStepFromList(workstep);
            tellUserWorkStepDeleted();
        }
        catch (Exception e){giveFeedback("deleteWorkStep ", e.toString());}

    }

    //deletes single workStep from list
    private void deleteWorkStepFromList(String workStep){
        if(workstepDescribitions.contains(workStep)){
            workstepDescribitions.remove(workStep);
            arrayAdapter.notifyDataSetChanged();
            Toast.makeText(this, "Gelöscht!", Toast.LENGTH_LONG).show();

        }
    }

    //delets workstep from DB
    private void deleteWorkStepFromDB(Long id){
        try {
            dbAdapter.deleteWorkStepFromDB(id);
        }
        catch (Exception e){giveFeedback("deleteWorkStepFromDB ", e.toString());}

    }

    //returns ingrident ID through string name
    //used in to delete worksteps in deleteWorkStep
    private Long getWorkstepIDbyDescription(String description){
        Long id=Long.valueOf(0);
        try{
            for (RecipeWorkStep recipeWorkStep:recipeWorkSteps){
                if(recipeWorkStep.getWorkStepDescribition().equals(description)){
                    id=recipeWorkStep.getId();
                }
            }
            giveFeedback("getWorkstepIDbyDescription", "recipeID: "+id);

        }
        catch (Exception e){giveFeedback("getWorkstepIDbyDescription", e.toString());}

        return id;
    }



    //checks if user forgot to make input
    private boolean isEditTextEmpty(EditText editText){
        if (editText.getText().toString().trim().isEmpty()) return true;
        else return false;
    }

    //-------------------------communication methods------------------------------------------------

    private void giveFeedback(String method, String feedback) {
        Log.d("WorkStepActivity " + method, feedback);
    }


    private void tellUserToFillField(){
        Toast.makeText(WorkStepActivity.this, "Bitte Feld füllen",Toast.LENGTH_SHORT).show();
    }

    private void tellUserWorkStepSaved(){
        Toast.makeText(WorkStepActivity.this, "Gespeichert!",Toast.LENGTH_SHORT).show();
    }

    private void tellUserWorkStepDeleted(){
        Toast.makeText(WorkStepActivity.this, "Gelöscht",Toast.LENGTH_SHORT).show();
    }

    //return to recipeactivity
    //at the moment finished() is used instead of this method
    //for future extensions of this app
    private void returnToRecipeActivity(){
        Intent intent=new Intent(this,RecipeNewActivity.class);
        intent.putExtra(CookingConstants.RECIPE_ID_KEY,recipeID);
        intent.putExtra(CookingConstants.NEW_RECIPE_KEY, CookingConstants.NEW_RECIPE_FALSE);
        String id=recipeID.toString();
        Log.d("returnToRecipeActivity"," recipeID:"+ id);
        intent.putExtra(CookingConstants.NEW_RECIPE_KEY, CookingConstants.NEW_RECIPE_FALSE);
        startActivity(intent);

    }





}
