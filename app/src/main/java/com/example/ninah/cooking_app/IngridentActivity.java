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

public class IngridentActivity extends AppCompatActivity {

    private EditText nameTextView;
    private EditText mengeTextView;
    private EditText einheitTextView;
    private Button saveButton;
    private ListView listView;
    ArrayList<Ingrident> ingridentArrayList;
    ArrayList<String> ingridentNames;
    ArrayAdapter arrayAdapter;
    DBAdapter dbAdapter;
    Long recipeID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingrident);

        nameTextView=(EditText) findViewById(R.id.IngrAct_nameEditText);
        mengeTextView=(EditText) findViewById(R.id.IngrAct_mengeEditText);
        einheitTextView=(EditText) findViewById(R.id.IngrAct_einheitEditText);
        einheitTextView.setTransformationMethod(null);
        saveButton=(Button) findViewById(R.id.IngrAct_saveButton);
        listView=(ListView) findViewById(R.id.IngrAct_listView);

        ingridentArrayList=new ArrayList<>();
        ingridentNames=new ArrayList<>();
        arrayAdapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, ingridentNames);
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
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            createNewIngrident();

            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedIngrident=((TextView) view).getText().toString();
                delteIngrident(selectedIngrident);
                return false;
            }
        });

    }

    //-----------------------getter and setter methods---------------------------------------------

    //sets single workstep to list
    private void setIngridentsNameToList( String name){
            nameTextView.setText(CookingConstants.EMPTY_STRING);
            ingridentNames.add(name);
            arrayAdapter.notifyDataSetChanged();
    }

    private String getIngridentName(){
        String name="";
        if(nameTextView!=null){
            name=nameTextView.getText().toString();
        }

        return name;
    }

    private int getIngridentMenge(){
        int amount=0;
        if(mengeTextView!=null){
            String menge=mengeTextView.getText().toString();
            if(isNumber(menge)){
                amount=Integer.parseInt(menge);
            }
        }

        return amount;
    }

    private String getEinheit(){
        String unit="";
        if(einheitTextView!=null){
            unit=einheitTextView.getText().toString();
        }

        return unit;
    }

    //--------------------creater methods----------------------------------------------------------

    private void createNewIngrident(){
        if(recipeID!=null) {
            if(getIngridentName()!=null||getIngridentMenge()!=0||getEinheit()!=null) {
            Ingrident ingrident=dbAdapter.createNewIngridentWithRecipeID(getIngridentName(), getEinheit(),getIngridentMenge(), recipeID);
            setIngridentsNameToList(getIngridentName());
                ingridentArrayList.add(ingrident);
        }
            else {
            Toast.makeText(this, "Ein Feld ist leer. Konnte nicht speichern",Toast.LENGTH_LONG);}
        }
        else {giveFeedback("createNewIngrident", "no recipe created, recipeID is null");}

    }

    //------------------save and delete methods--------------------------------------------------

    private void delteIngrident(String name){
        Long id=getIngridentIDbyName(name);
        deleteIngridentsNamesFromListView(name);
        delteOldIngridentFromDB(id);
    }

    private Long getIngridentIDbyName(String name){
        Long id=Long.valueOf(0);
        for (Ingrident ingrident:ingridentArrayList){
            if(ingrident.getName().equals(name)){
                id=ingrident.getId();
            }
        }
        giveFeedback("getIngridentIDbyName", "id: "+id);

        return id;
    }

    //deletes single workStep from list
    private void deleteIngridentsNamesFromListView(String ingrident){
        if(ingridentNames.contains(ingrident)){
            ingridentNames.remove(ingrident);
            arrayAdapter.notifyDataSetChanged();
        }
    }

    private void delteOldIngridentFromDB(Long ingridentID){
        dbAdapter.deleteIngridentFromDB(ingridentID);
    }


   // ----------------------------helper methods----------------------------------------------------

    private void giveFeedback(String method, String feedback) {
        Log.d("WorkStepActivity " + method, feedback);
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
