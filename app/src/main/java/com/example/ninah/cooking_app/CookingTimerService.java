package com.example.ninah.cooking_app;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

/**
 * Created by Jonas on 13.09.2017.
 */

public class CookingTimerService extends Service implements CookingTimerListener {
    private CookingTimer cookingTimer;
    private Intent broadcastIntent;


     /*--------------------------------------------------------------------------*/
    /*Constructor-part: (all) constructor(s) of the class*/

    //constructor of the CookingTimerService
    public CookingTimerService() {
        super();
    }

      /*--------------------------------------------------------------------------*/
    /*LifeCycle-Part: all methods of the Activity-LifeCycle which are necessary for this service*/

    @Override
    public void onCreate() {
        super.onCreate();
        initBroadCastIntent();
        Log.d("CTService", "onCreate()");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        int currentTime=getValuesFromIntent(intent);
        Log.d("CTService", "onStartCommand: "+currentTime);
        initCookingTimer(currentTime);
        return Service.START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        cookingTimer.stopCountDown();
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

      /*--------------------------------------------------------------------------*/
    /*Service-Part: all methods to control service*/

    private void stopThisService(){
        stopSelf();
    }


    /*--------------------------------------------------------------------------*/
    /*init part: inits broadcastIntent, cookingTimer*/

    //inits the broadCatIntent
    private void initBroadCastIntent(){
        broadcastIntent=new Intent();
    }

    //inits the cookingTimer
    private void initCookingTimer(int currentTime){
        cookingTimer=new CookingTimer(this);
        cookingTimer.startNewCountDownTimer(currentTime);
        Log.d("CTService","new CountDownTimer started: "+currentTime);
    }

      /*--------------------------------------------------------------------------*/
    /*intent part: methods to getValues from received intents and to prepare a new BroadCastIntent*/

    //returns the int value currentTime of the given intent
    private int getValuesFromIntent(Intent intent){
        int currentTime=0;
        if(intent!=null ){
            currentTime=intent.getExtras().getInt(TimerActivity.KEY_CURRENT_TIME);
        }

        Log.d("CTService","getValuesFromIntent: "+currentTime);
        return currentTime;
    }

    //prepares a new broadCastIntent
    //puts the currentTimeFromTimer as an Extra
    // gets the currentTimeFromTimer via the public getCurrenTime() of the cookingTimer
    private void prepareBroadCastIntent(){
        if(cookingTimer!=null) {
            String currentTimeFromTimer = cookingTimer.getCurrentTime();
            if(currentTimeFromTimer!=null) {
                if (broadcastIntent != null) {

                    Log.d("prepare broadcast time", currentTimeFromTimer);
                    broadcastIntent.setAction(TimerActivity.CURRENT_TIME_ACTION);
                    broadcastIntent.putExtra(TimerActivity.KEY_CURRENT_TIME, currentTimeFromTimer);
                }
                else {Log.d("CTService","no broadcast to prepare");}
            }
            else {Log.d("CTService","no cookingTimer");}
        }
        else {Log.d("CTService","no currentTimeFromTimer");}

    }

      /*--------------------------------------------------------------------------*/
    /*listener part: overwritten methods of the CookingTimerListener*/

    //prepares and sends a broadCastIntent via sendBroadcast() of LocalBroadcastManager
    //LocalBroadcastManager is used as it has advantages over sending a global broadcast: https://developer.android.com/reference/android/support/v4/content/LocalBroadcastManager.html, 14.09.17
    //is activated in onTick() of the CookingTimer
    @Override
    public void onCookingTimerTick() {
        prepareBroadCastIntent();
        if(broadcastIntent!=null) {

            LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);

            {Log.d("CTService","broadCastIntent sended "+broadcastIntent.getAction());}
        }

        else {Log.d("CTService","no broadCastIntent");}

    }

    //prepares a broadCastIntent with constant TimerActivity.COOKING_TIMER_FINISHED as extra
    //is activated in onFinish() of the CookingTimer
    //sends the broadCastIntent via sendBroadcast() of LocalBroadcastManager
    // receiver "knows" than that the cookingTimer has finished running();
    //LocalBroadcastManager is used as it has advantages over sending a global broadcast: https://developer.android.com/reference/android/support/v4/content/LocalBroadcastManager.html, 14.09.17
    // stops this CookingTimerService at the end;

    @Override
    public void onCookingTimerFinished() {

        broadcastIntent.setAction(TimerActivity.CURRENT_TIME_ACTION);
        broadcastIntent.putExtra( TimerActivity.KEY_CURRENT_TIME, TimerActivity.COOKING_TIMER_FINISHED);
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);
        Log.d("CTService", "onCookingTimerFinished: "+TimerActivity.COOKING_TIMER_FINISHED);


        stopThisService(); //stops this service
    }


}
