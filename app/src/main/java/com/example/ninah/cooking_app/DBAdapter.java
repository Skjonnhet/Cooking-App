package com.example.ninah.cooking_app;

import android.content.Context;
import android.util.Log;

import org.greenrobot.greendao.query.QueryBuilder;
import org.greenrobot.greendao.query.WhereCondition;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Jonas on 19.09.2017.
 */

public class DBAdapter {
    private DaoSession daoSession;
    private QueryBuilder recipeQueryBuilder;
    private QueryBuilder ingridentQueryBuilder;
    private QueryBuilder workStepQueryBuilder;
    //------------------------------------------------------------------------------------------------
    //Constructor: creates a daoSession and queryBuilder with the given context
    //this allows communications between dadapter-implementing class and this class and the DB

    public DBAdapter(Context context) {
        this.daoSession = new DaoMaster(new OpenDBHelper(context, "demoDB3.db").getWritableDb()).newSession();
        if (daoSession == null) Log.d("DBAdapter", "session null!");
        else giveFeedback("constructor","session created");
        recipeQueryBuilder = daoSession.queryBuilder(Recipe.class);
        ingridentQueryBuilder = daoSession.queryBuilder(Ingrident.class);
        workStepQueryBuilder = daoSession.queryBuilder(RecipeWorkStep.class);
    }

    //-------------------Secret Class Part---------------------------------------------------------
    //----------------------------------------------------------------------------------------------
    //this part is capsuled from the user to prevent mistakes----------------------------------
    //----------------------------------------------------------------------------------------------


    //---------------------------------------------------------------------------------------------
    //Default-methods: return a default instance.

    private Recipe getDefaultRecipe() {

        Recipe recipe = new Recipe(null, "DefaultRecipeName", "Beschreibung", "Zutaten", 10, 10);
        daoSession.getRecipeDao().insertOrReplace(recipe);

        Long id = recipe.getId();
        Ingrident ingridient = new Ingrident(null, "namedZutat", "einheit", 2, id);
        IngridentDao ingridentDao = daoSession.getIngridentDao();
        if (id == null) giveFeedback("getDefaultRecipe", "id is null!");
        else giveFeedback("getDefaultRecipe", "id not null");
        daoSession.getRecipeDao().insertOrReplace(recipe);
        return daoSession.getRecipeDao().load(id);
    }

    private Ingrident getDefaultIngrident(Long recipeId) {
        Ingrident ingrident = new Ingrident(null, "default Ingrident", "gramm", 500, recipeId);
        daoSession.getIngridentDao().insertOrReplace(ingrident);
        giveFeedback("getDefaultIngrident", ingrident.getId().toString() + ":" + ingrident.getName());
        return daoSession.getIngridentDao().load(ingrident.getId());
    }

    private RecipeWorkStep getDefaultWorkStep(Long recipeId) {
        RecipeWorkStep workstep = new RecipeWorkStep(null, "default workstepp", recipeId);
        daoSession.getRecipeWorkStepDao().insertOrReplace(workstep);
        giveFeedback("getDefaultWorkStep", workstep.getId().toString() + ":" + workstep.getWorkStepDescribition());
        return workstep;
    }

//-------------------------------------------------------------------------------------------------------#
    //Save-methods: save instances in the dataBase
    //those methods are only for the class
    //methods are used in the public part as a single recipe HAS TO contain recipe,ingridents and worksteps


    private void saveSingleRecipeToDB(Recipe recipe) {
        daoSession.getRecipeDao().insertOrReplace(recipe);
        giveFeedback("setRecipe", "success");
    }

    private void saveSingleIngridentToDB(Ingrident ingrident) {
        Long id = daoSession.getIngridentDao().insertOrReplace(ingrident);
        giveFeedback("saveSingleIngridentToDB", id.toString() + ":" + ingrident.getName());
    }

    private void saveAllIngridentsToDB(List<Ingrident> ingridentList, Long recipeId) {
        for (Ingrident ingrident : ingridentList) {
            ingrident.setRecipeID(recipeId);
            saveSingleIngridentToDB(ingrident);
            Log.d("saveAllIngridentsToDB", "recipeID:" + ingrident.getRecipeID().toString());
        }
    }

    private void saveSingleRecipeWorkStepToDB(RecipeWorkStep recipeWorkStep) {
        Long id = daoSession.getRecipeWorkStepDao().insertOrReplace(recipeWorkStep);
        giveFeedback("saveSingleRecipeWorkStepToDB", id.toString() + ":" + recipeWorkStep.getWorkStepDescribition().toString());
    }

    private void saveAllRecipeWorkStepsToDB(List<RecipeWorkStep> workStepList, Long recipeId) {
        for (RecipeWorkStep workStep : workStepList) {
            workStep.setRecipeID(recipeId);
            saveSingleRecipeWorkStepToDB(workStep);
            Log.d("saveAllRecWStepsToDB", "recipeID:" + workStep.getRecipeID().toString());
        }
    }

    //-----------------------------------------------------------------------------------------------
    //getter-Methods: return values
    //those methods are only for the class
    //used in the public part of this class


    private Recipe getRecipeByID(Long id) {
        Recipe recipe = daoSession.getRecipeDao().load(id);
        giveFeedback("getRecipeByID", id + " : " + recipe.getName());
        return recipe;
    }

    private Ingrident getIngridentByID(Long id) {
        Ingrident ingrident = daoSession.getIngridentDao().load(id);
        giveFeedback("getRecipeByID", id + " : " + ingrident.getName());
        return ingrident;
    }

    private RecipeWorkStep getWorkStepByID(Long id) {
        RecipeWorkStep workstep = daoSession.getRecipeWorkStepDao().load(id);
        giveFeedback("getRecipeByID", id + " : " + workstep.getWorkStepDescribition());
        return workstep;
    }

    private List<Recipe> getAllRecipesList() {
        List allRecipes = null;

        try {
            allRecipes = daoSession.getRecipeDao().loadAll();
        } catch (Exception e) {
            Log.d("DBAdapter", "getAllRecipesList" + e.toString());
        }

        if (allRecipes == null) {
            Log.d("DBAdapter", "getAllRecipesList returns Null");
        }

        return allRecipes;
    }


    private List<Recipe> getRecipeListByName(String nameX) {
        List recipesWithNameX = null;
        try {
            recipesWithNameX = recipeQueryBuilder.where(RecipeDao.Properties.Name.eq(nameX)).list();
        } catch (Exception e) {
            Log.d("DBAdapter", "getRecipeByName" + e.toString());
        }

        if (recipesWithNameX == null) {
            Log.d("DBAdapter", "getRecipeByName returns Null");
        }


        return recipesWithNameX;
    }

    private List<Ingrident> getIngridentListByRecipe(Recipe recipe) {
        Long id = recipe.getId();
        List recipesWithNameX = null;
        try {
            recipesWithNameX = ingridentQueryBuilder.where(IngridentDao.Properties.RecipeID.eq(id)).list();
        } catch (Exception e) {
            Log.d("DBAdapter", "getIngridentListByRecipe" + e.toString());
        }

        if (recipesWithNameX == null) {
            Log.d("DBAdapter", "getIngridentListByRecipe returns Null");
        }


        return recipesWithNameX;
    }

    private List<RecipeWorkStep> getWorkStepListByRecipe(Recipe recipe) {
        Long id = recipe.getId();
        List recipesWithNameX = null;
        try {
            recipesWithNameX = workStepQueryBuilder.where(RecipeWorkStepDao.Properties.RecipeID.eq(id)).list();
        } catch (Exception e) {
            Log.d("DBAdapter", "getWorkStepListByRecipe" + e.toString());
        }

        if (recipesWithNameX == null) {
            Log.d("DBAdapter", "getWorkStepListByRecipe returns Null");
        }

        return recipesWithNameX;
    }


    private List<Integer> getRecipeIDListByName(String nameX) {
        List idList = null;
        List<Recipe> recipesWithNameX = null;
        try {
            recipesWithNameX = recipeQueryBuilder.where(RecipeDao.Properties.Name.eq(nameX)).list();
            idList = new ArrayList();

        } catch (Exception e) {
            Log.d("DBAdapter", "getRecipeByName" + e.toString());
        }

        for (Recipe recipe : recipesWithNameX) {
            idList.add(recipe.getId());
        }

        if (recipesWithNameX == null) {
            Log.d("DBAdapter", "getRecipeByName returns Null");
        }


        return idList;
    }

    private void ensureIDisEqual() {

    }


//--------------------------------------------------------------------------------------------------
    //setter-methods:allow user of DBAdapter to set Rating-values in the db

    private void setStarsById(Long id, int starRating) {
        Recipe recipe = daoSession.getRecipeDao().load(id);
        recipe.setRatingInStars(starRating);
    }


    //----------------------------------------------------------------------------------------------
    //helper-methods:help in this class
    private static void giveFeedback(String method, String feedback) {
        Log.d("DBAdapter " + method, feedback);
    }


    //-------------------Public Class Part---------------------------------------------------------
    //----------------------------------------------------------------------------------------------
    //this part is to be used  by the user to control the DB trough the DBAdapter-------------------
    //----------------------------------------------------------------------------------------------


    //------getter-methods-part--------------------------------------------------------------------
    //user can get DB values through those methods

    //returns recipe by ID
    public Recipe getRecipebyRecipeID(Long id) {
        Recipe recipe = null;
        try {
            recipe = getRecipeByID(id);
        } catch (Exception e) {
            giveFeedback("getRecipebyRecipeID", e.toString());
        }

        return recipe;

    }

    //returns all recipes
    public List<Recipe> getAllRecipesFromDB() {
        List<Recipe> recipes = null;
        try {
            recipes = getAllRecipesList();
        } catch (Exception e) {
            giveFeedback("getAllRecipesFromDB", e.toString());
        }

        if (recipes == null) {
            giveFeedback("getAllRecipesFromDB", "recipeList is null");
        }

        return recipes;
    }

    //returns a List of recipes which have the given name
    public List<Recipe> getAllRecipesWithThisName(String recipeName) {
        List<Recipe> recipeList = null;
        try {
            recipeList = getRecipeListByName(recipeName);
        } catch (Exception e) {
            giveFeedback("getAllRecipesWithThisName", e.toString());
        }

        return recipeList;
    }

    //returns a List of ingridents which have the given name
    public List<Ingrident> getAllIngridentsOfRecipe(Recipe recipe) {

        List<Ingrident> ingridentList = null;
        try {
            ingridentList = getIngridentListByRecipe(recipe);
        } catch (Exception e) {
            giveFeedback("getAllIngridentsOfRecipe", e.toString());
        }

        if (ingridentList != null) {
            giveFeedback("getAllIngridentsOfRecipe", "got all ingrident in list: " + ingridentList.toString());
        }

        return ingridentList;
    }

    //returns a List of recipeWorkSteps which have the given name
    public List<RecipeWorkStep> getAllWorkStepsOfRecipe(Recipe recipe) {
        List<RecipeWorkStep> recipeWorkSteps = null;
        try {
            recipeWorkSteps = getWorkStepListByRecipe(recipe);
        } catch (Exception e) {
            giveFeedback("getAllWorkStepsOfRecipe", e.toString());
        }

        if (recipeWorkSteps != null) {
            giveFeedback("getAllWorkStepsOfRecipe", "got all recipeWorkSteps in list: " + recipeWorkSteps.toString());
        }

        return recipeWorkSteps;
    }


    //returns highest rated recipe
    public Recipe getHighestRateRecipe() {
        List<Recipe> allRecipes = null;
        Recipe recipe = null;
        try {
            allRecipes = getAllRecipesList();
            for (Recipe r : allRecipes) {
                if (recipe == null) {
                    recipe = r;
                } else {
                    if (r.getRatingInStars() > recipe.getRatingInStars()) {
                        recipe = r;
                    }
                }

            }
        } catch (Exception e) {
            giveFeedback("getHighestRateRecipe", e.toString());
        }

        return recipe;
    }

    //returns highest rated recipe
    public Recipe getRecipeWithSmallestTime() {
        List<Recipe> allRecipes = null;
        Recipe recipe = null;
        try {
            allRecipes = getAllRecipesList();
            for (Recipe r : allRecipes) {
                if (recipe == null) {
                    recipe = r;
                } else {
                    if (r.getTimeInMinutes() < recipe.getTimeInMinutes()) {
                        recipe = r;
                    }
                }

            }
        } catch (Exception e) {
            giveFeedback("getRecipeWithSmallestTime", e.toString());
        }

        return recipe;
    }

    public Recipe getRandomRecipe() {
        List<Recipe> allRecipes = null;
        Recipe recipe = null;
        try {
            allRecipes = getAllRecipesList();
            Random random = new Random();
            if (allRecipes != null) {
                int randomNumber = random.nextInt(allRecipes.size());
                try {
                    recipe = allRecipes.get(randomNumber);
                } catch (Exception e) {
                    giveFeedback("getRecipeWithSmallestTime", e.toString());
                }
            } else {
                giveFeedback("getRecipeWithSmallestTime", "allRecipes is null");
            }


        } catch (Exception e) {
            giveFeedback("getRecipeWithSmallestTime", e.toString());
        }

        return recipe;
    }

    // -------------------create-methods-part-------------------------------------------------------
    //user create new instances through those methods
    //ensures he doesn't set the startID, startRecipeID and ratings by himself

    //creates a new single RecipeInstance without confusing the user with setting the startID and rating
    //should always be used for creating new recipes
    public static Recipe createNewRecipe(String name, String descpription, String difficulty, int timeInMinutes) {
        Long startID = null;
        int startRating = 0;
        Recipe recipe = new Recipe(startID, name, descpription, difficulty, timeInMinutes, startRating);
        giveFeedback("createNewRecipe", "name:" + recipe.getName().toString());
        return recipe;
    }

    //creates a new single Ingrident Instance without confusing the user with setting the startID and rating
    //should always be used for creating new ingrident
    public static Ingrident createNewIngrident(String name, String einheit, double menge) {
        Long startID = null;
        Long startRecipeID = Long.valueOf(1);
        Ingrident ingrident = new Ingrident(startID, name, einheit, menge, startRecipeID);
        giveFeedback("createNewIngrident", "name:" + ingrident.getName().toString());
        return ingrident;
    }

    //creates a new single workstep instance without confusing the user with setting the startID and rating
    //should always be used for creating new workstep
    public static RecipeWorkStep createNewWorkStep(String description) {
        Long startID = null;
        Long startRecipeID = Long.valueOf(1);
        RecipeWorkStep workStep = new RecipeWorkStep(startID, description, startRecipeID);
        return workStep;
    }


    //creates a recipe with default values
    //saves it to db
    //should be used to fill DB in the mainactivity when app starts to avoid nullpointers
    public Recipe createDefaultRecipeAndSaveItToDB() {
        Recipe recipe = getDefaultRecipe();
        Long id = Long.valueOf(1);
        if (recipe != null) {
            id = recipe.getId();
        }
        List<Ingrident> ingridentList = new ArrayList<>();
        List<RecipeWorkStep> workSteps = new ArrayList<>();
        ingridentList.add(getDefaultIngrident(id));
        workSteps.add(getDefaultWorkStep(id));

        saveRecipeToDB(getDefaultRecipe(), ingridentList, workSteps);
        giveFeedback("createDefaultRecipeAndSaveItToDB", "recipe saved");
        return recipe;
    }




    //---------------saveAndDestroy-part---------------------------------
    //most important part: user can save a recipe to DB or destroy it

    //save a recipe to DB: needs, recipe, List of ingridents, list of worksteps
    //each ingrident and worksteps gets the id of "his" recipe
    //therefore recipe, ingridentList and recipeWorkStepList have to be saved together to the DB
    public void saveRecipeToDB(Recipe recipe, List<Ingrident> ingridentList, List<RecipeWorkStep> recipeWorkStepList) {
        try {
            saveSingleRecipeToDB(recipe);
            saveAllIngridentsToDB(ingridentList, recipe.getId());
            saveAllRecipeWorkStepsToDB(recipeWorkStepList, recipe.getId());
        } catch (Exception e) {
            giveFeedback("saveRecipeToDB", "couldnt save recipe: Exception: " + e.toString());
        }

    }

    //destroys whole recipe and all connected ingridents and workSteps
    //should be used carefuly
    public void destroyWholeRecipe(Recipe recipe) {
        Long id = recipe.getId();
        try {


            List<Ingrident> ingridentList = getIngridentListByRecipe(recipe);
            for (Ingrident ingrident : ingridentList) {

                daoSession.delete(ingrident);
            }

            List<RecipeWorkStep> workSteps = getAllWorkStepsOfRecipe(recipe);
            for (RecipeWorkStep workStep : workSteps) {

                daoSession.delete(workStep);
            }

            recipe.delete();
        } catch (Exception e) {
            Log.d("destroyWholeRecipe", e.toString());
        }
        if (id != null) {
            giveFeedback("destroyWholeRecipe", "detroyed id: " + id.toString());
        }

    }


}




    





