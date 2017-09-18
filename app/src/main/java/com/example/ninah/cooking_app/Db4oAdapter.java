package com.example.ninah.cooking_app;
import java.util.List;
import android.content.Context;


/**
 * Created by Jonas on 18.09.2017.
 */

public class Db4oAdapter extends Db4oHelper{

    private static Db4oAdapter provider = null;

    //configure Db4oHelper by Passing Context.
    public Db4oAdapter(Context context) {
        super(context);
    }

    public static Db4oAdapter getDb4oAdapater(Context context) {
        if (provider == null){
            provider = new Db4oAdapter(context);
        }

       return provider;
    }

    //This method is used to store the object into the database.
    public void store(Recipe recipe) {
       openDB().store(recipe);
    }

    //This method is used to delete the object into the database.
    public void delete(Recipe recipe) {
        openDB().delete(recipe);
    }

    //This method is used to retrieve all object from database.
    public List<Recipe> getAllRecipes() {
        return openDB().query(Recipe.class);
    }

    //This method is used to retrive matched object from database.
    public List<Recipe> getRecipesByExample(Recipe recipeExample) {
        return openDB().queryByExample(recipeExample);
    }

    public long getRecipeID(Recipe recipe){
       return openDB().ext().getID(recipe);
    }
// public ObjectSet<Student> getAllData() {
//  Student proto = new Student(null, null, 0);
//  ObjectSet result = db().queryByExample(proto);
//  return result;
// }
}
