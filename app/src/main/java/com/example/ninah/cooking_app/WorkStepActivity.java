package com.example.ninah.cooking_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class WorkStepActivity extends AppCompatActivity {


    private Button addButton;

    private Button saveButton;
    private EditText workStepEditText;
    private ListView listView;
    private ArrayList<String> workstepDescribitions;
    private ArrayAdapter<String> arrayAdapter;
    private Long recipeID;
    private DBAdapter dbAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_step);

        addButton=(Button) findViewById(R.id.saveSingleWorkStepButton);
        saveButton=(Button) findViewById(R.id.saveAllWorkStepsButton);

        workStepEditText=(EditText) findViewById(R.id.workStepEditText);
        listView=(ListView) findViewById(R.id.workStepsListView);

        workstepDescribitions=new ArrayList<>();
        arrayAdapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, workstepDescribitions);
        dbAdapter=new DBAdapter(this);

        listView.setAdapter(arrayAdapter);
        setOnClickListern();
        setRecipeIDThroughIntent();



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
                setWorkStepToList();

            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedWorkStep=((TextView) view).getText().toString();
                deleteWorkStepFromList(selectedWorkStep);
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

    //sets single workstep to list
    private void setWorkStepToList(){
        if(workStepEditText!=null){
            String description=workStepEditText.getText().toString();
            workStepEditText.setText(CookingConstants.EMPTY_STRING);
            workstepDescribitions.add(description);
            arrayAdapter.notifyDataSetChanged();
        }

    }

    //deletes single workStep from list
    private void deleteWorkStepFromList(String workStep){
        if(workstepDescribitions.contains(workStep)){
            workstepDescribitions.remove(workStep);
            arrayAdapter.notifyDataSetChanged();
        }
    }

    //saves all worksteps with the workstepDescribitions to the DB
    private void saveWorkStepListToDB(){

        if(recipeID!=null){
            for (String description:workstepDescribitions){
                RecipeWorkStep recipeWorkStep=dbAdapter.createNewWorkStepWithRecipeID(description,recipeID);
            }
            Toast.makeText(this, "Gespeichert!", Toast.LENGTH_LONG).show();
        }
    }

    private void giveFeedback(String method, String feedback) {
        Log.d("WorkStepActivity " + method, feedback);
    }

    private void returnToRecipeActivity(){
        Intent intent=new Intent(this,RecipeNewActivity.class);
        intent.putExtra(CookingConstants.RECIPE_ID_KEY,recipeID);
        intent.putExtra(CookingConstants.NEW_RECIPE_KEY, CookingConstants.NEW_RECIPE_FALSE);
        String id=recipeID.toString();
        Log.d("returnToRecipeActivity"," id:"+ id);
        intent.putExtra(CookingConstants.NEW_RECIPE_KEY, CookingConstants.NEW_RECIPE_FALSE);
        startActivity(intent);

    }



}
