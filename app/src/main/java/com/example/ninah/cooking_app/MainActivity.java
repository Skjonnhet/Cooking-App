package com.example.ninah.cooking_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    TextView recipeList;
    TextView bestRecipe;
    TextView fastRecipe;
    TextView randomRecipe;
    TextView newRecipe;
    Button testTimerButton;
    DBAdapter dbAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recipeList = (TextView)findViewById(R.id.viewRecipeList);
        randomRecipe = (TextView)findViewById(R.id.viewRandomRecipe);
        bestRecipe = (TextView)findViewById(R.id.viewBestRecipe);
        fastRecipe = (TextView)findViewById(R.id.viewFastRecipe);
        newRecipe = (TextView) findViewById(R.id.viewNewRecipe);
        testTimerButton=(Button) findViewById(R.id.testTimerButton);
        recipeList.setOnClickListener(this);
        bestRecipe.setOnClickListener(this);
        fastRecipe.setOnClickListener(this);
        randomRecipe.setOnClickListener(this);
        newRecipe.setOnClickListener(this);
        dbAdapter=new DBAdapter(this);
        onClickForTesting();



    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.viewRecipeList:
                Intent recipeListIntent = new Intent(MainActivity.this, RecipeList.class);
                startActivity(recipeListIntent);
                break;
            case R.id.viewBestRecipe:
                /** hier gehört eine Logik rein, die das beste Rezept aufruft**/
                Intent bestRecipeIntent = new Intent(MainActivity.this, RecipeActivity.class);
                startActivity(bestRecipeIntent);
                break;
            case R.id.viewFastRecipe:
                /** hier gehört eine Logik rein, die das schnellste Rezept aufruft**/
                Intent fastRecipeIntent = new Intent(MainActivity.this, RecipeActivity.class);
                startActivity(fastRecipeIntent);
                break;
            case R.id.viewRandomRecipe:
                /** hier gehört eine Logik rein, die ein zufälliges Rezept aufruft**/
                Intent randomRecipeIntent = new Intent(MainActivity.this, RecipeActivity.class);
                startActivity(randomRecipeIntent);
                break;
            case R.id.viewNewRecipe:
                Intent newRecipeIntent = new Intent(MainActivity.this, RecipeNew.class);
                startActivity(newRecipeIntent);
                break;
            default:
                break;
        }

    }

    private void createNewRecipeInDataBase(){

        //Rezpet in db schreiben:
        // 1.recipe erstellen
        // 2. liste von zutaten erstellen
        //3.Liste von Arbeitsschritten erstellen
        // 4. mit der Methode  dbAdapter.saveRecipeToDB(recipe,ingridentList,workStepList)
        //in die DB schreibem

        //Erst das Rezept erstellen

        String RezeptName="Kuchen";
        String Beschreibung="leckerer Schockokuchen";
        String Schwierigkeit="leicht";
        int ZeitInMinuten=45;

        //Bitte Adapter verwendenen um Rezepte, Zutaten und Arbeitsschritte zu erstellen
        //Ansonsten funktioniert DB nicht mehr
        Recipe recipe=dbAdapter.createNewRecipe(RezeptName,Beschreibung,Schwierigkeit,ZeitInMinuten);

        //Zweitens Zutatenliste erstellen
        String ZutatenName="Zucker";
        String ZuatetnEinheit="gramm";
        double Menge=500;
        String ZutatenName2="Mehl";

        //Bitte Adapter verwendenen um Rezepte, Zutaten und Arbeitsschritte zu erstellen
        //Ansonsten funktioniert DB nicht mehr
        Ingrident ingrident1=dbAdapter.createNewIngrident(ZutatenName, ZuatetnEinheit,Menge);
        Ingrident ingrident2=dbAdapter.createNewIngrident(ZutatenName2, ZuatetnEinheit,Menge);


        List<Ingrident> ingridentList=new ArrayList<>();
        ingridentList.add(ingrident1);
        ingridentList.add(ingrident2);

        //ArbeitsschrittListe erstellen
        String ruhren="Rühren";
        String essen="Essen";

        //Bitte Adapter verwendenen um Rezepte, Zutaten und Arbeitsschritte zu erstellen
        //Ansonsten funktioniert DB nicht mehr
        RecipeWorkStep workStep1=dbAdapter.createNewWorkStep(ruhren);
        RecipeWorkStep workStep2=dbAdapter.createNewWorkStep(essen);



        List<RecipeWorkStep> workStepList=new ArrayList<>();
        workStepList.add(workStep1);
        workStepList.add(workStep2);

        //IN DATENBANK EINFÜGEN
        //Rezpet, Liste mit Zutaten und Arbeitsschritte
        dbAdapter.saveRecipeToDB(recipe,ingridentList,workStepList);
    }

    //holte Rezept mit name recipeName
    public List<Recipe> getRecipeWithName(String recipeName){

        //holt immer eine Liste zurück, da ja Rezepte gleich heißen können
        //alles läuft über den dbAdapter
        List<Recipe> list=dbAdapter.getAllRecipesWithThisName(recipeName);
        return list;
    }

    public void createDefaultRecipeInDB(){
        //erstellt das standardRezept
       dbAdapter.createDefaultRecipeAndSaveItToDB();
    }




    private void onClickForTesting(){

        //tests timer
        //timer alarm sound is always the same song in (the raw file) with different names
        //should be changed to different sounds soon
        if(testTimerButton!=null) {


            testTimerButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    createNewRecipeInDataBase();
                    //hole eine Liste von Rezepten mit dem namen "Kuchen" aus der DB
                    //nehme da erste Objekt aus der Liste
                    Recipe kuchenRezept=getRecipeWithName("Kuchen").get(0);

                    //hole dir die id des kuchenRezeptes, ID ist (Long)
                    //die getter und setter kommen ohne Adapter aus
                    Long id=kuchenRezept.getId();

                    //neuen Namen setzten und updaten
                    //kuchenRezept.setName("donut");
                    //kuchenRezept.update();

                    String name=kuchenRezept.getName();
                    //String schwierigkeit=kuchenRezept.getDifficulty();


                    //erstelle das StandardZufallsRezept
                    createDefaultRecipeInDB();

                    //hole ds rezept mit der höchten Bewertung (Kuchen oder StandardZufallsRezept?)
                    Recipe recipeWithHighestRating=dbAdapter.getHighestRateRecipe();


                    //schreibe den namen auf dem Button
                    testTimerButton.setText(recipeWithHighestRating.getName());

                    }

                }


            );}
        else Log.d("MainActivity", "testTimerButtonIsNull");
        }



}
