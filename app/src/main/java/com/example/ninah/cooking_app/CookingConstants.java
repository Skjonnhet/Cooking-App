package com.example.ninah.cooking_app;

import android.content.IntentFilter;

import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;

/**
 * Created by Jonas on 15.09.2017.
 */

public class CookingConstants {
    /********************************Constants of the app******************************************/
    //DB constants
    public final static String DATA_BASE_FILE_NAME="demoDB6.db";
    public final static String NEW_RECIPE_KEY="NEW_RECIPE_KEY";
    public final static boolean NEW_RECIPE_FALSE=false;
    public final static boolean NEW_RECIPE_TRUE=true;
    public final static String RECIPE_ID_KEY="RECIPE_ID_KEY";

    //default recipe constants
    public final static Long DEFAULT_RECIPE_ID=Long.valueOf(1);
    public final static String DEFAULT_RECIPE_NAME="BeispielRezept";
    public final static int DEFAULT_RECIPE_PORTIONS=2;
    public final static String DEFAULT_RECIPE_DIFFIICULTY="leicht";
    public final static int DEFAULT_RECIPE_TIME=10;
    public final static int DEFAULT_RECIPE_RATING=3;


    //default ingrident constants
    public final static Long DEFAULT_INGRIDENT_ID=DEFAULT_RECIPE_ID;
    public final static String DEFAULT_INGRIDENT_NAME="Bsp.: Mehl";
    public final static String DEFAULT_INGRIDENT_UNIT="gr";
    public final static double DEFAULT_INGRIDENT_MENGE=50;
    public final static Long DEFAULT_INGRIDENT_RECIPE_ID=DEFAULT_RECIPE_ID;

    //default workstep constants
    public final static Long DEFAULT_WORKSTEP_ID=DEFAULT_RECIPE_ID;
    public final static String DEFAULT_WORKSTEP_DECRIPTION="Bsp.: Rühren";
    public final static Long DEFAULT_WORKSTEP_RECIPE_ID=DEFAULT_RECIPE_ID;

    //ringTone constants
    public static final String RING_TONE_CLAPPING="RING_TONE_CLAPPING";
    public static final String RING_TONE_EAGLE="RING_TONE_EAGLE";
    public static final String RING_TONE_TRUMPET="RING_TONE_TRUMPET";
    public static final String RING_TONE_WOLF="RING_TONE_WOLF";
    public static final String CHOSEN_RING_TONE_KEY="CHOSEN_RING_TONE_KEY";
    public static final int RING_TONE_REQUEST_CODE = 1;

    //timer constants
    public final static String KEY_CURRENT_TIME="KEY_CURRENT_TIME";
    public final static String CURRENT_TIME_ACTION="CURRENT_TIME_ACTION";
    public final static String COOKING_TIMER_FINISHED="COOKING_TIMER_FINISHED";

    //other constants
    public final static String EMPTY_STRING="";
    public final static String DIFFICULTY_EASY="leicht";





}
