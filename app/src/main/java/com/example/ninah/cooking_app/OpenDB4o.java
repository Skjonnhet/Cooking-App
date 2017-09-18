package com.example.ninah.cooking_app;

import android.os.AsyncTask;
import android.util.Log;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;

/**
 * Created by Jonas on 18.09.2017.
 */

public class OpenDB4o extends AsyncTask<String, Void, ObjectContainer> {
    @Override
    protected ObjectContainer doInBackground(String... params) {

        String dbPath = params[0];
        Log.d("OpenDB4o",dbPath);
        ObjectContainer db = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), dbPath);
        Db4oHelper.setDataBase(db);
        Log.d("OpenDB4o","containerSet");
        Log.d("OpenDB4o",db.toString());
        return db;
    }

    @Override
    protected void onPostExecute(ObjectContainer objectContainer) {

        super.onPostExecute(objectContainer);


    }
}
