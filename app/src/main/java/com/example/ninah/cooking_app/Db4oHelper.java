package com.example.ninah.cooking_app;
import java.io.IOException;

import android.content.Context;
import android.util.Log;


import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.config.EmbeddedConfiguration;


/**
 * Created by Jonas on 18.09.2017.
 */

//http://v4all123.blogspot.de/2013/12/using-db4o-in-android.html

public class Db4oHelper {
    //ObjectContainer is the interface, which encapsulates the database (DB). Is a kind of "ObjectManager"
    private static ObjectContainer objectContainer = null;
    private static ObjectContainer dataBase=null;
    private Context context;
    private static final String DATABASE_NAME = "myDB4o.db4o";
    private static final int DATABASE_MODE = 0;
    private String dbPath;


    //Constructor of Db4oHelper
    public Db4oHelper(Context context) {
        this.context = context;
    }


    //opens the DB
    public ObjectContainer openDB() {

        try {
            if (objectContainer == null || objectContainer.ext().isClosed()) {
                //objectContainer creates connection to the databaseFile
                //objectContainer = Db4oEmbedded.openFile(getDBConfigurations(), getDB4oDBFullPath(context));
                OpenDB4o op=new OpenDB4o();
                op.execute(getDB4oDBFullPath(context)).get();
                Log.d("Db4oHelper", "container created");
                objectContainer=dataBase.ext().openSession();
            }

            //returns the ObjectContainer which directs to the database
            return objectContainer;

        } catch (Exception ie) {
            Log.d("Db04Helper", "objectContainer is null!");
            Log.e(Db4oHelper.class.getName().toString(), ie.toString());
            return null;
        }
    }


    //Sets the activation-depth to eternal, so all related objects will be changed
    private EmbeddedConfiguration getDBConfigurations() throws IOException {
        EmbeddedConfiguration configuration = Db4oEmbedded.newConfiguration();
        configuration.common().objectClass(Recipe.class).cascadeOnActivate(true);
        return configuration;
    }


    //returns path of the DB
    private String getDB4oDBFullPath(Context ctx) {
        String path2= android.os.Environment.getExternalStoragePublicDirectory(android.os.Environment.DIRECTORY_MOVIES)+ "/pu.yap";
        String path=ctx.getDir("data", 0) + "/" + "pu.db4o";
        Log.d("Db4oHelper", path2);
        return path2;

    }

    //closes the DB
    public void close() {
        if (objectContainer != null)
            objectContainer.close();
    }

    public static void setDataBase(ObjectContainer objectContainer){
        dataBase=objectContainer;
    }




}
