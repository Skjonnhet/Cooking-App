package com.example.ninah.cooking_app;

/**
 * Created by Roman on 15.09.2017.
 */
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
public class RecipeListAdapter extends SQLiteOpenHelper {





    private static final String LOG_TAG = RecipeListAdapter.class.getSimpleName();

    public static final String DB_NAME = "RecipeDB.db";
    public static final int DB_VERSION = 1;

    //Columns of the DB add more here
    public static final String RECIPE_DB = "recipe_db";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_RECIPE_NAME = "recipeName";
    public static final String COLUMN_RECIPE_INGREDIENTS = "recipeIngredients";
    public static final String COLUMN_STAR = "stars";


    public static final String SQL_CREATE =
            "CREATE TABLE " + RECIPE_DB + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_RECIPE_NAME + " TEXT NOT NULL, " +
                    COLUMN_STAR  + " INTEGER NOT NULL, " +
                    COLUMN_RECIPE_INGREDIENTS + " INTEGER NOT NULL);";

    //Konstruktor
        public RecipeListAdapter(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
            Log.d(LOG_TAG, "Datenbank: " + getDatabaseName() + " wurde erzeugt.");
        }






        // Die onCreate-Methode wird nur aufgerufen, falls die Datenbank noch nicht existiert
    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            Log.d(LOG_TAG, " Tabelle mit -Befehl: " + SQL_CREATE + " angelegt.");
            db.execSQL(SQL_CREATE);
        }
        catch (Exception ex) {
            Log.e(LOG_TAG, "Error while creating DB: " + ex.getMessage());
        }
    }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }


