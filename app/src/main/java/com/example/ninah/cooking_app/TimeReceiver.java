package com.example.ninah.cooking_app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Jonas on 12.09.2017.
 */

public class TimeReceiver extends BroadcastReceiver {

    private String currentTime="0";


    //constructor of the TimeReceiver uses super() of the parentClass
    public TimeReceiver(){
        super();
        Log.d("Receiver", "constructed");
    }


    //overwritten onReceive() of BroadcastReceiver
    // if intent.Action equals TimerActivity.CURRENT_TIME_ACTION the currentTime is given to setCurrentTime()
    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("TimeReceiver","onReceive");
        if(intent!=null) {

            if (intent.getAction().equals(TimerActivity.CURRENT_TIME_ACTION)) {
                currentTime = intent.getStringExtra(TimerActivity.KEY_CURRENT_TIME);
                Log.d("TimeReceiver", currentTime);
                setCurrentTime(currentTime);
            } else {
                Log.d("TimeReceiver", "not CURRENT_TIME_ACTION");
            }
        }
    }

    //receives currentTime as a String
    //uses this String to set the time in the GUI of the TimerActivity via setCurrentTime()
    private void setCurrentTime(String currentTime){
        if(TimerActivity.getTimerActivity()!=null){
            try {
                TimerActivity.getTimerActivity().setCurrentTime(currentTime);
            }

            catch (Exception e){
                Log.d("Receiver", "Exception: " +e);
            }
        }
    }
}
