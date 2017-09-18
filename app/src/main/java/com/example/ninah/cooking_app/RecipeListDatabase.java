package com.example.ninah.cooking_app;

/**
 * Created by Roman on 15.09.2017.
 */
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import android.content.ContentValues;
import android.database.Cursor;
import java.util.ArrayList;
import java.util.List;


public class RecipeListDatabase {




    private SQLiteDatabase Database;
    private RecipeListAdapter DBHelper;

    private String[] columns = {
            RecipeListAdapter.COLUMN_ID,
            RecipeListAdapter.COLUMN_RECIPE_NAME,
            RecipeListAdapter.COLUMN_RECIPE_INGREDIENTS,
            RecipeListAdapter.COLUMN_STAR
    };


    public RecipeListDatabase(Context context) {
        DBHelper = new RecipeListAdapter(context);
    }


    public void open() {

        Database = DBHelper.getWritableDatabase();

    }

    public void close() {
        DBHelper.close();

    }

    //Create the database
    public Recipe createNewRecipeInDB(String recipeName, int recipeIngredients, int stars) {
           ContentValues values = new ContentValues();
        values.put(RecipeListAdapter.COLUMN_RECIPE_NAME, recipeName);
        values.put(RecipeListAdapter.COLUMN_RECIPE_INGREDIENTS, recipeIngredients);
        values.put(RecipeListAdapter.COLUMN_STAR, stars);

        //Values
        long insertId = Database.insert(RecipeListAdapter.RECIPE_DB, null, values);

        Cursor cursor = Database.query(RecipeListAdapter.RECIPE_DB,
                columns, RecipeListAdapter.COLUMN_ID + "=" + insertId,
                null, null, null, null);

        cursor.moveToFirst();
        Recipe recipt = cursorToRecipe(cursor);
        cursor.close();

        return recipt;

    }

    private Recipe cursorToRecipe(Cursor cursor) {
        int idIndex = cursor.getColumnIndex(RecipeListAdapter.COLUMN_ID);
        int idrecipeName = cursor.getColumnIndex(RecipeListAdapter.COLUMN_RECIPE_NAME);
        int idrecipeIngredients = cursor.getColumnIndex(RecipeListAdapter.COLUMN_RECIPE_INGREDIENTS);
        int idstars = cursor.getColumnIndex(RecipeListAdapter.COLUMN_STAR);

        String recipeName = cursor.getString(idrecipeName);
        int recipeIngredients = cursor.getInt(idrecipeIngredients);
        int stars = cursor.getInt(idstars);
        long id = cursor.getLong(idIndex);

        Recipe recipt = new Recipe(recipeName, recipeIngredients, stars,id);

        return recipt;
    }

    public List<Recipe> getRecipts() {
        List<Recipe> reciptList = new ArrayList<>();

        Cursor cursor = Database.query(RecipeListAdapter.RECIPE_DB,
                columns, null, null, null, null, null);

        cursor.moveToFirst();
        Recipe recipt;

        while(!cursor.isAfterLast()) {
            recipt = cursorToRecipe(cursor);
            reciptList.add(recipt);

            cursor.moveToNext();
        }

        cursor.close();

        return reciptList;
    }



}