package com.example.ninah.cooking_app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Jonas on 12.09.2017.
 */

public class CookingTimeReceiver extends BroadcastReceiver {

    /*****************BroadcastReceiver of the timer function of the app***************************/
    /*Logic: onReceives receives time trough intent from CookingTimerService. CookingTimerActivity
     * implements CookingTimeReceiver to receive the time from CookingTimerServices*/
    /*order:onReceive()-> setCurrentTime()->CookingTimerActivity.getCookingTimerActivity().setCurrentTime(currentTime)
    starts starts setCurrentTime(currentTime) in CookingTimerActivity*/

    private String defaultStartValue="0";
    private String currentTime=defaultStartValue;


    //constructor of the CookingTimeReceiver uses super() of the parentClass
    public CookingTimeReceiver(){
        super();
        Log.d("Receiver", "constructed");
    }


    //overwritten onReceive() of BroadcastReceiver
    // if intent.Action equals CookingTimerActivity.CURRENT_TIME_ACTION the currentTime is given to setCurrentTime()
    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("CookingTimeReceiver","onReceive");
        if(intent!=null) {

            if (intent.getAction().equals(CookingConstants.CURRENT_TIME_ACTION)) {
                currentTime = intent.getStringExtra(CookingConstants.KEY_CURRENT_TIME);
                Log.d("CookingTimeReceiver", currentTime);
                setCurrentTime(currentTime);
            } else {
                Log.d("CookingTimeReceiver", "not CURRENT_TIME_ACTION");
            }
        }
    }

    //receives currentTime as a String
    //uses this String to set the time in the GUI of the CookingTimerActivity via setCurrentTime()
    private void setCurrentTime(String currentTime){
        if(CookingTimerActivity.getCookingTimerActivity()!=null){
            try {
                CookingTimerActivity.getCookingTimerActivity().setCurrentTime(currentTime);
            }

            catch (Exception e){
                Log.d("Receiver", "Exception: " +e);
            }
        }
    }
}
