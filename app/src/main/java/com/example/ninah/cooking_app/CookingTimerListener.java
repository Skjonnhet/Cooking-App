package com.example.ninah.cooking_app;

/**
 * Created by Jonas on 12.09.2017.
 */

public interface CookingTimerListener {

    /*Interface so services and other classes can communicate with the CookingTimer*/

    public void onCookingTimerTick();
    public void onCookingTimerFinished();
}
