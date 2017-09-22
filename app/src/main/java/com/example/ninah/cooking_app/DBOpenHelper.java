package com.example.ninah.cooking_app;

import android.content.Context;
import android.util.Log;

import org.greenrobot.greendao.database.Database;

/**
 * Created by Jonas on 19.09.2017.
 */

public class DBOpenHelper extends DaoMaster.OpenHelper {
    /*****************Helper class for building a new database(DB)*******************************/
    //Helper class to allow modify the database schema in greenDao 3
    //source: https://mindorks.com/blog/powerful-android-orm-greendao-3-tutorial, 22.09.17



    public DBOpenHelper(Context context, String name) {
        super(context, name);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);
        Log.d("DBOpenHelper", "old_db, version: : " + oldVersion + ", db_new, version: " + newVersion);
        switch (oldVersion) {
            case 1:
            case 2:
                //db.execSQL("ALTER TABLE " + UserDao.TABLENAME + " ADD COLUMN " + UserDao.Properties.Name.columnName + " TEXT DEFAULT 'DEFAULT_VAL'");
        }
    }
}
