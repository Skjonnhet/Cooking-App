package com.example.ninah.cooking_app;

/**
 * Created by Jonas on 12.09.2017.
 */

public interface CookingTimerListener {
    /*****************Listener of the timer function of the app************************************/
    /*Interface so CookingTimerService and other classes can communicate with the CookingTimer.class*/

    public void onCookingTimerTick();
    public void onCookingTimerFinished();
}
