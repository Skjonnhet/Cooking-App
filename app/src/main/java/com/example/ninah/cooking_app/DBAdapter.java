package com.example.ninah.cooking_app;

import android.content.Context;
import android.util.Log;

import org.greenrobot.greendao.annotation.Entity;
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
    /*****************Adapter of the database function of the app**********************************/
    /*translates the input which are done in the activities of the app into greenDao 3 code
    to encapsulate the sensible database from users who are  working in the activities
    greenDao 3 is an SQLite based Android ORM, source: http://greenrobot.org/greendao/ 22.09.17
    */

    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private QueryBuilder recipeQueryBuilder;
    private QueryBuilder ingridentQueryBuilder;
    private QueryBuilder workStepQueryBuilder;

    //------------------------------------------------------------------------------------------------
    //Constructor: creates a daoSession and queryBuilder with the given context
    //this allows communications between dadapter-implementing class and this class and the DB

    public DBAdapter(Context context) {
        daoMaster= new DaoMaster(new OpenDBHelper(context, CookingConstants.DATA_BASE_FILE_NAME).getWritableDb());
        this.daoSession = daoMaster.newSession();
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

        Recipe recipe = new Recipe(null, "DefaultRecipeName", 2, "Zutaten", 10, 10);
        daoSession.getRecipeDao().insertOrReplace(recipe);

        Long id = recipe.getId();
        Ingrident ingridient = new Ingrident(null, "namedZutat", "einheit", 2, id);
        IngridentDao ingridentDao = daoSession.getIngridentDao();
        if (id == null) giveFeedback("getDefaultRecipe", "recipeID is null!");
        else giveFeedback("getDefaultRecipe", "recipeID not null");
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
        try {
           Long id= recipe.getId();
            daoSession.getRecipeDao().insertOrReplace(recipe);
            giveFeedback("saveSingleRecipeToDB", "success" +"recipeID: "+id );
        }

        catch (Exception e){giveFeedback("saveSingleRecipeToDB", e.toString());}


    }

    private void saveSingleIngridentToDB(Ingrident ingrident) {
        try {
            Long id =ingrident.getId();
            Long recipeID=ingrident.getRecipeID();
            daoSession.getIngridentDao().insertOrReplace(ingrident);
            giveFeedback("saveSingleIngridentToDB success"+"recipeID. "+recipeID+ " ingridentID: ", id.toString() + ":" + ingrident.getName());
        }
        catch (Exception e){giveFeedback("saveSingleIngridentToDB", e.toString());}
    }

    private void saveSingleRecipeWorkStepToDB(RecipeWorkStep recipeWorkStep) {
        try {
            Long id = recipeWorkStep.getId();
            Long recipeID=recipeWorkStep.getRecipeID();
            daoSession.getRecipeWorkStepDao().insertOrReplace(recipeWorkStep);
            giveFeedback("saveSingleRecipeWorkStepToDB success"+"recipeID: "+recipeID+ " workstepiID: ", id.toString() + ":" + recipeWorkStep.getWorkStepDescribition().toString());
        }

        catch (Exception e){giveFeedback("saveSingleRecipeWorkStepToDB", e.toString());}

    }


    private void saveAllIngridentsToDB(List<Ingrident> ingridentList, Long recipeId) {
        for (Ingrident ingrident : ingridentList) {
            ingrident.setRecipeID(recipeId);
            saveSingleIngridentToDB(ingrident);
            Log.d("saveAllIngridentsToDB", "recipeID:" + ingrident.getRecipeID().toString());
        }
    }


    private void saveAllRecipeWorkStepsToDB(List<RecipeWorkStep> workStepList, Long recipeId) {
        for (RecipeWorkStep workStep : workStepList) {
            workStep.setRecipeID(recipeId);
            saveSingleRecipeWorkStepToDB(workStep);
            Log.d("saveAllRecWStepsToDB", "recipeID:" + workStep.getRecipeID().toString());
        }
    }

    //-----------------------------------------------------------------------------
    //destroy methods: destroy all specified instances
    //should be used carefully

    //destroys whole recipe and all connected ingridents and workSteps
    //should be used carefuly
    private void destroyWholeRecipe(Recipe recipe) {
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
            giveFeedback("destroyWholeRecipe", "detroyed recipeID: " + id.toString());
        }

    }

    private void destroyAllIngridientsWithThisRecipeID(Long id){
        List<Ingrident> ingridentList=getIngridentListByRecipeID(id);

        try {
            for (Ingrident ingrident : ingridentList) {
                if(ingrident.getId()!=null)
                giveFeedback("destroyAllIngridientsWithThisRecipeID","recipe recipeID:"+id+  " ingrident recipeID:"+ ingrident.getId().toString()+":"+ingrident.getName());
                daoSession.delete(ingrident);


            }
        }
        catch (Exception e){giveFeedback("destroyAllIngridientsWithThisRecipeID: ", e.toString());}
    }

    private void destroyAllWorkStepsWithThisRecipeID(Long id){
        List<RecipeWorkStep> worksteps=getWorkStepListByRecipeID(id);

        try {
            for (RecipeWorkStep workstep : worksteps) {
                if(workstep.getId()!=null)
                giveFeedback("destroyAllWorkStepsWithThisRecipeID", "recipe recipeID :"+id+" workstep recipeID:"+ workstep.getId().toString()+" "+workstep.getWorkStepDescribition());
                daoSession.delete(workstep);
            }
        }
        catch (Exception e){giveFeedback("destroyAllWorkStepsWithThisRecipeID: ", e.toString());}
    }

    //-----------------------------------------------------------------------------------------------
    //getter-Methods: return values
    //those methods are only for the class
    //used in the public part of this class


    public Recipe getRecipeByID(Long id) {
        Recipe recipe = daoSession.getRecipeDao().load(id);
        if(recipe!=null)
        giveFeedback("getRecipeByID", id + " : " + recipe.getName());
        else giveFeedback("getRecipeByID", id + " : recipe is null" );
        return recipe;
    }

    private Ingrident getIngridentByID(Long id) {
        Ingrident ingrident = daoSession.getIngridentDao().load(id);
        if(ingrident!=null)
        giveFeedback("getRecipeByID", id + " : " + ingrident.getName());
        else giveFeedback("getRecipeByID", id + " : ingrident is null" );
        return ingrident;
    }

    private RecipeWorkStep getWorkStepByID(Long id) {
        RecipeWorkStep workstep = daoSession.getRecipeWorkStepDao().load(id);
        if(workstep!=null)
        giveFeedback("getRecipeByID", id + " : " + workstep.getWorkStepDescribition());
        else giveFeedback("getRecipeByID", id + " : workstep is null" );
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
            giveFeedback("getRecipeListByName", "Size: "+ recipesWithNameX.size());
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

    private List<Ingrident> getIngridentListByRecipeID(Long id) {

        List recipesWithNameX = null;
        try {
            recipesWithNameX = ingridentQueryBuilder.where(IngridentDao.Properties.RecipeID.eq(id)).list();
        } catch (Exception e) {
            Log.d("DBAdapter", "getIngridentListByRecipeID" + e.toString());
        }

        if (recipesWithNameX == null) {
            Log.d("DBAdapter", "getIngridentListByRecipeID returns Null");
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

    private List<RecipeWorkStep> getWorkStepListByRecipeID(Long id) {

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

    private Long getHighestRecipeID() {
        List<Recipe> allRecipes = null;
        Recipe recipe = null;
        Long id=Long.valueOf(0);
        try {
            allRecipes = getAllRecipesList();
            for (Recipe r : allRecipes) {
                if (recipe == null) {
                    recipe = r;
                } else {
                    if (r.getId() > recipe.getId()) {
                        recipe = r;
                    }
                }

            }
        } catch (Exception e) {
            giveFeedback("getHighestRecipeID", e.toString());
        }

        if(recipe!=null)
        {
            giveFeedback("getHighestRecipeID", "recipeID:" +recipe.getId());
            id=recipe.getId();
        }

        else{ giveFeedback("getHighestRecipeID","recipe is null! return: "+ id);}

        return id;
    }

    private Long getHighestIngridentID() {
        List<Ingrident> ingridentList = null;
        Ingrident ingrident = null;
        Long id=Long.valueOf(0);
        try {
            ingridentList = daoSession.getIngridentDao().loadAll();
            for (Ingrident ing : ingridentList) {
                if (ingrident == null) {
                    ingrident = ing;
                } else {
                    if (ing.getId() > ingrident.getId()) {
                        ingrident = ing;
                    }
                }

            }
        } catch (Exception e) {
            giveFeedback("getHighestIngridentID", e.toString());
        }

        if(ingrident!=null)
        {
            giveFeedback("getHighestIngridentID", "recipeID:" +ingrident.getId());
            id=ingrident.getId();
        }

        else{ giveFeedback("getHighestIngridentID","ingrident is null! return: "+ id);}

        return id;
    }

    private Long getHighestWorkStepID() {
        List<RecipeWorkStep> workSteps = null;
        RecipeWorkStep workStep = null;
        Long id=Long.valueOf(0);
        try {
            workSteps = daoSession.getRecipeWorkStepDao().loadAll();
            for (RecipeWorkStep ws : workSteps) {
                if (workStep == null) {
                    workStep = ws;
                } else {
                    if (ws.getId() > workStep.getId()) {
                        workStep = ws;
                    }
                }

            }
        } catch (Exception e) {
            giveFeedback("getHighestWorkStepID", e.toString());
        }

        if(workStep!=null)
        {
            giveFeedback("getHighestWorkStepID", "recipeID:" +workStep.getId());
            id=workStep.getId();
        }

        else{ giveFeedback("getHighestWorkStepID","workstep is null! return: "+ id);}

        return id;
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

    public int getRecipeTimeInMinutes(Long id){
       int time=0;
        try{
            time=getRecipeByID(id).getTimeInMinutes();
        }

        catch (Exception e){giveFeedback("getRecipeTimeInMinutes", e.toString());}
        giveFeedback("getRecipeTimeInMinutes", "recipeId:" +id+ " time:"+time );
        return time;
    }

    // -------------------create-methods-part-------------------------------------------------------
    //user create new instances through those methods
    //ensures he doesn't set the startID, startRecipeID and ratings by himself

    //creates a new single RecipeInstance without confusing the user with setting the startID and rating
    //should always be used for creating new recipes
    public Recipe createNewRecipe(String name, int portions, String difficulty, int timeInMinutes) {
        Long startID = getHighestRecipeID();
        startID++;
        int startRating = 0;
        Recipe recipe = new Recipe(startID, name, portions, difficulty, timeInMinutes, startRating);
        giveFeedback("createNewRecipe", "name:" + recipe.getName().toString());
        saveSingleRecipeToDB(recipe);
        return recipe;
    }

    //creates a new single Ingrident Instance without confusing the user with setting the startID and rating
    //should always be used for creating new ingrident
    public Ingrident createNewIngrident(String name, String einheit, double menge) {
        Long startID = getHighestIngridentID();
        startID++;
        Long startRecipeID = Long.valueOf(1);
        Ingrident ingrident = new Ingrident(startID, name, einheit, menge, startRecipeID);
        giveFeedback("createNewIngrident", "name:" + ingrident.getName().toString()+" recipeID: "+startID);
        saveSingleIngridentToDB(ingrident);
        return ingrident;
    }

    //creates a new single workstep instance without confusing the user with setting the startID and rating
    //should always be used for creating new workstep
    public RecipeWorkStep createNewWorkStep(String description) {
        Long startID = getHighestWorkStepID();
        startID++;
        Long startRecipeID = Long.valueOf(1);
        RecipeWorkStep workStep = new RecipeWorkStep(startID, description, startRecipeID);
        saveSingleRecipeWorkStepToDB(workStep);
        return workStep;
    }


    //creates a recipe with default values
    //saves it to db
    //should be used to fill DB in the mainactivity when app starts to avoid nullpointers
    public Recipe createDefaultRecipeAndSaveItToDB() {
        Recipe recipe = getDefaultRecipe();
        Long id = getHighestRecipeID();
        id++;

        List<Ingrident> ingridentList = new ArrayList<>();
        List<RecipeWorkStep> workSteps = new ArrayList<>();
        ingridentList.add(getDefaultIngrident(id));
        workSteps.add(getDefaultWorkStep(id));
        saveRecipeToDB(recipe, ingridentList, workSteps);
        giveFeedback("createDefaultRecipeAndSaveItToDB", "recipe saved");
        return recipe;
    }

    //creates a new single Ingrident Instance without confusing the user with setting the startID and rating
    //should always be used for creating new ingrident
    public Ingrident createNewIngridentWithRecipeID(String name, String einheit, double menge, Long recipeID) {
        Long startID = getHighestIngridentID();
        startID++;
        Long startRecipeID = recipeID;
        Ingrident ingrident = new Ingrident(startID, name, einheit, menge, startRecipeID);
        giveFeedback("createNewIngridentWithRecipeID", "name:" + ingrident.getName().toString()+" recipeID: "+startID);
        saveSingleIngridentToDB(ingrident);
        return ingrident;
    }

    //creates a new single workstep instance without confusing the user with setting the startID and rating
    //should always be used for creating new workstep
    public RecipeWorkStep createNewWorkStepWithRecipeID(String description, Long recipeID) {
        Long startID = getHighestWorkStepID();
        startID++;
        Long startRecipeID = recipeID;
        RecipeWorkStep workStep = new RecipeWorkStep(startID, description, startRecipeID);
        saveSingleRecipeWorkStepToDB(workStep);
        return workStep;
    }






    //---------------saveAndDestroy-part---------------------------------
    //most important part: user can save a whole recipe to DB or destroy it

    //save a recipe to DB: needs, recipe, List of ingridents, list of worksteps
    //each ingrident and worksteps gets the recipeID of "his" recipe
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


    public void updateRecipe(Recipe recipe){

        daoSession.update(recipe);
    }

    public void destroyWholeRecipeAndAllItsConnectedInstances(Recipe recipe){
        if(recipe!=null) {
            try {
                destroyWholeRecipe(recipe);
            } catch (Exception e) {
                giveFeedback("destroyWholeRecipeAndAllItsConnectedInstances : ", e.toString());
            }
        }
        else giveFeedback("destroyWholeRecipeAndAllItsConnectedInstances : ","recipe is null");

    }

    public void deleteIngridentFromDB(Long ingridentID){
        daoSession.getIngridentDao().deleteByKey(ingridentID);
        giveFeedback("deleteIngridentFromDB", " ingrident-ID: "+ingridentID);
    }

    public void deleteWorkStepFromDB(Long workStepID){
        daoSession.getRecipeWorkStepDao().deleteByKey(workStepID);
        giveFeedback("deleteWorkStepFromDB", " workStep-ID: "+workStepID);
    }


   // ----------update-part------------------------------------------------------------------------
    //all update methods

    //destroys old worksteps and writes new in DB
    public void updateAllWorkStepsOfSingleRecipeByRecipeID(Long recipeId, List<RecipeWorkStep> worksteps){
        destroyAllWorkStepsWithThisRecipeID(recipeId);
        saveAllRecipeWorkStepsToDB(worksteps,recipeId);

    }

    public void updateAllIngridentsOfSingleRecipeByRecipeID(Long recipeId, List<Ingrident> ingridents){
        destroyAllIngridientsWithThisRecipeID(recipeId);
        saveAllIngridentsToDB(ingridents, recipeId);
    }

    public void updateSingleRecipeByID(Long oldRecipeId, Recipe newRecipe){
        //destroy old recipe
        Recipe oldRecipe=getRecipeByID(oldRecipeId);
        daoSession.delete(oldRecipe);

        //create new recipe
        newRecipe.setId(oldRecipeId);
        saveSingleRecipeToDB(newRecipe);

    }

    public void cleanAllTables(){
        daoMaster.dropAllTables(daoSession.getDatabase(), true);
        daoMaster.createAllTables(daoSession.getDatabase(), true);
    }

    //allows user of DBAdapter to set  rating values in the db
    public void updateRatingStars(Long id, int starRating) {

        try {
            Recipe recipe = daoSession.getRecipeDao().load(id);
            recipe.setRatingInStars(starRating);
            daoSession.update(recipe);
        }
        catch (Exception e){giveFeedback("updateRatingStars ", e.toString());}

    }












}




    





