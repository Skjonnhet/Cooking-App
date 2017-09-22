package com.example.ninah.cooking_app;

import android.os.CountDownTimer;
import android.util.Log;

/**
 * Created by Jonas on 12.09.2017.
 */

public class CookingTimer {

    /*****************CountDown-class of the timer function of the app******************************/
    /*logic: CookingTimerServices starts countDownTimer and gives CookingTimer cookingTimerListener
     through constructor of the CookingTimer--------------------------------------------------------
    *countDownTimer counts given time down and sets currentTime-------------------------------------
    *CookingTimerServices gets current time trough getCurrentTime() getter method-------------------
    *countDownTimer counts given time down and tells cookingTimerListener when timer ticks/has finished
    *cookingTimerListener is implemented in CookingTimerServices------------------------------------
    * CookingTimerServices "knows" when cookingTimer has finished and ticks trough  cookingTimerListener
  */

    private CountDownTimer countDownTimer;
    private static String currentTime;
    private CookingTimerListener cookingTimerListener;


    //constructor of the cookingTimer sets cookingTimerListener
    public CookingTimer(CookingTimerListener cookingTimerListener){
                this.cookingTimerListener=cookingTimerListener;
    }


    /*---------------------------------------------------------------------------------------------*/
    /*------------countDownTimer-Part: all methods to control the countDownTimer-------------------*/


    //starts a new countdown timer. All timer functions and logic is here.
    // receives timeInSeconds and transforms them in a String-Time-Format: hh:mm:ss, sets this time in currentTime
    // pattern-oriented from: https://stackoverflow.com/questions/3377688/what-do-these-symbolic-strings-mean-02d-01d, 14.09.2017
    //overwritten onTick() and on Finish() implement methods for communication with the cookingTimerListener
    public void startNewCountDownTimer(int timeInSeconds) {
        final int millis = 1000;
        final String timeFormat = "%02d:%02d:%02d";

        countDownTimer = new CountDownTimer(timeInSeconds * millis, millis) {
            @Override
            public void onTick(long millisUntilFinished) {
                int timeInSecondsLeft = (int) millisUntilFinished / millis;
                int hours = timeInSecondsLeft / 3600;
                int minutes = (timeInSecondsLeft % 360) / 60;
                int seconds = timeInSecondsLeft % 60;

                String currentTime=  String.format(timeFormat, hours, minutes, seconds);
                setCurrentTime( currentTime);
                callCookingTimerListenerThatTick();
                Log.d("Cooking Timer counts", currentTime);
            }

            @Override
            public void onFinish() {
                callCookingTimerListenerThatFinished();
                stopCountDown();
            }
        };

        startCountDown();
    }

    //starts a new countDownTimer
    private void startCountDown(){
        countDownTimer.start();
    }

    //stops the countdowntimer
    public void stopCountDown(){
        countDownTimer.cancel();
    }

     /*-------------------------------------------------------------------------------------------*/
    /*-------------currentTime-Part: all methods to control the currentTime-----------------------*/


    //sets CurrentTime
    private void setCurrentTime(String currentTime){
        this.currentTime=currentTime;
        Log.d("Cooking Timer sets Time", this.currentTime);
    }

    //returns currentTime
    //is used in the CookingTimerService, to send the currentTime to the CookingTimeReceiver
    public String getCurrentTime(){
        if(currentTime!=null){
            Log.d("Cooking Timer returns", currentTime);
        }

        else{
            Log.d("Cooking Timer returns", "null");
        }

        return currentTime;
    }


     /*-------------------------------------------------------------------------------------------*/
    /*-------------Listener-Part: all methods to control the cookingTimerListener-----------------*/

    //calls onCookingTimerTick() of the cookingTimerListener
    //this method is used in onTick() of the countDownTimer
    //every class which implements a CookingTimer(CookingTimerListener cookingTimerListener) receives that onTick() ist activated
    //through the delivered cookingTimerListener and this method
    private void callCookingTimerListenerThatTick(){
        if(this.currentTime!=null){
            Log.d("CookingT alarm Listner", this.currentTime);
            cookingTimerListener.onCookingTimerTick();
        }

        else{
            Log.d("CookingT alarm Listner","no currenTime");
        }
    }

    //calls onCookingTimerFinished() of the cookingTimerListener
    //this method is used in onFinish() of the countDownTimer
    //every class which implements a CookingTimer(CookingTimerListener cookingTimerListener) receives that onFinish() ist activated
    //through the delivered cookingTimerListener and this method
    private void callCookingTimerListenerThatFinished(){
        if(this.currentTime!=null){
            Log.d("CookingT alarm Listner", this.currentTime);
            cookingTimerListener.onCookingTimerFinished();
        }

        else{
            Log.d("CookingT alarm Listner","no currenTime");
        }
    }






}
