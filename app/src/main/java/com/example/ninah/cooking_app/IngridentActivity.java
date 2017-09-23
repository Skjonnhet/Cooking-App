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
import java.util.List;

public class IngridentActivity extends AppCompatActivity {

    /*****************IngridentActivity of the recipe function of the app**************************/
    /*saves the user inputs into the listView trough an arrayAdapter*/
    /*receives recipeID from RecipeNewActivity via Intent */
    /*creates new ingridentsList with user input and tells the ingridentsList through the recipeID
     *to which recipe the ingridentsList are connected */
    /*saves ingridentsList to database*/
     /*deletes ingridentsList if user clicks long on ingridentsList*/

    private EditText nameEditText;
    private EditText mengeEditText;
    private EditText einheitEditText;
    private Button saveButton;
    private ListView listView;
    ArrayList<Ingrident> ingridentArrayList;
    ArrayList<String> ingridentNames;
    ArrayAdapter arrayAdapter;
    DBAdapter dbAdapter;
    Long recipeID;
    Long defaultID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingrident);
        initDefaultValues();
        initEditTexts();
        initAdapters();
        initLists();
        initListView();
        saveButton=(Button) findViewById(R.id.IngrAct_saveButton);
        setRecipeIDThroughIntent();
        setOnClickListern();
        fillListView();


    }

    //-------------------init part-----------------------------------------------------------------
    //all methods are used in onCreate()

    private void initDefaultValues(){
        defaultID=CookingConstants.DEFAULT_RECIPE_ID;
        recipeID=defaultID;
    }

    private void initLists(){
        ingridentArrayList=new ArrayList<>();
        ingridentNames=new ArrayList<>();
    }

    private void initListView(){
        listView=(ListView) findViewById(R.id.IngrAct_listView);
        listView.setAdapter(arrayAdapter);
    }

    private void initEditTexts(){
        nameEditText =(EditText) findViewById(R.id.IngrAct_nameEditText);
        mengeEditText =(EditText) findViewById(R.id.IngrAct_mengeEditText);
        mengeEditText.setTransformationMethod(null);
        einheitEditText =(EditText) findViewById(R.id.IngrAct_einheitEditText);
    }

    private void initAdapters(){
        arrayAdapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, ingridentNames);
        dbAdapter=new DBAdapter(this);
    }

    //sets recipeID through the intent
    //MUST BE SET ON START OF ACTIVITY!
    private void setRecipeIDThroughIntent(){
        try{
            Intent intent= getIntent();
            Bundle extras=intent.getExtras();
            recipeID=extras.getLong(CookingConstants.RECIPE_ID_KEY);
            giveFeedback("setRecipeIDThroughIntent", "recipeID:"+recipeID.toString());
        }
        catch (Exception e){giveFeedback("setRecipeIDThroughIntent ",  e.toString());}
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
                deleteIngrident(selectedIngrident);
                return false;
            }
        });

    }

    //fills listview ith old ingridentsList
    private void fillListView(){
        getOldIngridents();
        for (Ingrident ingrident:ingridentArrayList){
            ingridentNames.add(ingrident.getName());
            giveFeedback("fillListView", ingrident.getName());
        }
        arrayAdapter.notifyDataSetChanged();
    }

    //-----------------------getter and setter methods---------------------------------------------

    //sets single workstep to list
    private void setIngridentsNameToList( String name){
            nameEditText.setText(CookingConstants.EMPTY_STRING);
            ingridentNames.add(name);
            arrayAdapter.notifyDataSetChanged();
    }

    //returns IngridentName
    private String getIngridentName(){
        String name="";
        if(nameEditText !=null){
            name= nameEditText.getText().toString();
        }

        return name;
    }
    //returns IngridentMenge
    private int getIngridentMenge(){
        int amount=0;
        if(mengeEditText !=null){
            String menge= mengeEditText.getText().toString();
            if(isNumber(menge)){
                amount=Integer.parseInt(menge);
            }
        }

        return amount;
    }

    //returns Einheit (unit)
    private String getEinheit(){
        String unit="";
        if(einheitEditText !=null){
            unit= einheitEditText.getText().toString();
        }

        return unit;
    }

    //returns ingrident ID through string name
    private Long getIngridentIDbyName(String name){
        Long id=Long.valueOf(0);
        for (Ingrident ingrident:ingridentArrayList){
            if(ingrident.getName().equals(name)){
                id=ingrident.getId();
            }
        }
        giveFeedback("getIngridentIDbyName", "recipeID: "+id);

        return id;
    }

    private void getOldIngridents(){
        try {
            ingridentArrayList.addAll(dbAdapter.getAllIngridentsOfRecipe(dbAdapter.getRecipeByID(recipeID)));
        }
        catch (Exception e){giveFeedback("getOldIngridents ",e.toString());}
    }


    //--------------------creater methods----------------------------------------------------------
    //------------------save and delete methods--------------------------------------------------

    //creates new ingrident and saves it to DB and ingridentArrayList and ingridentNames
    private void createNewIngrident(){
        if(recipeID!=null) {
            if(areAllFieldsFilled()) {
                Ingrident ingrident=dbAdapter.createNewIngridentWithRecipeID(getIngridentName(), getEinheit(),getIngridentMenge(), recipeID);
                setIngridentsNameToList(getIngridentName());
                ingridentArrayList.add(ingrident);
                giveFeedback("createNewIngrident", "ingriedent created: id:"+ingrident.getId()+" "+getIngridentName()+" recipe id:"+ ingrident.getRecipeID());
                Toast.makeText(this, "Zutat gespeichert",Toast.LENGTH_SHORT).show();}
            else  {  Toast.makeText(this, "Ein Feld ist leer. Bitte füllen",Toast.LENGTH_LONG).show();}
        }
        else {giveFeedback("createNewIngrident", "no recipe created, recipeID is null");}

    }

    //deletes from listView and database
    private void deleteIngrident(String name){
        Long id=getIngridentIDbyName(name);
        deleteIngridentsNamesFromListView(name);
        delteOldIngridentFromDB(id);
        Toast.makeText(this, "Zutat gelöscht",Toast.LENGTH_SHORT).show();
    }


    //deletes single ingrident from list
    private void deleteIngridentsNamesFromListView(String ingrident){
        if(ingridentNames.contains(ingrident)){
            ingridentNames.remove(ingrident);
            arrayAdapter.notifyDataSetChanged();
        }
    }

    //deletes single ingrident from DB
    private void delteOldIngridentFromDB(Long ingridentID){
        dbAdapter.deleteIngridentFromDB(ingridentID);
    }


   // ----------------------------helper methods----------------------------------------------------

    private void giveFeedback(String method, String feedback) {
        Log.d("IngridentActivity " + method, feedback);
    }

    //checks if input is a number
    //is used to avoid wrong user input
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
    private boolean areAllFieldsFilled(){
        if(isEditTextEmpty(nameEditText)|isEditTextEmpty(mengeEditText)|isEditTextEmpty(einheitEditText)){
            return false;
        }

        else return true;
    }

    private boolean isEditTextEmpty(EditText editText){
        if (editText.getText().toString().trim().isEmpty()) return true;
        else return false;

    }
}
