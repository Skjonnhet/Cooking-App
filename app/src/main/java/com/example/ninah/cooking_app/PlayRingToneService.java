package com.example.ninah.cooking_app;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.Vibrator;
import android.util.Log;

public class PlayRingToneService extends Service {
    /*****************Service of the CookingTimerActivity of the app**************************************/
    /*----------------------------------------------------------------------------------------------
    * Logic:
    * CookingTimerActivity starts this service-------------------------------------------------------------
    * onStartCommand reads chosenRingToneFrom Intent------------------------------------------------
    * mediaPlayer starts playing chosenRing---------------------------------------------------------
    * vibrator starts vibrating---------------------------------------------------------------------
    *
    *CookingTimerActivity ends this service----------------------------------------------------------------
    * mediaPlayer and vibrator are stopped in onDestroy()-------------------------------------------
    * */


    private static MediaPlayer mediaPlayer;
    private Vibrator vibrator;
    private static String chosenRingtone;
    private static String CHOSEN_RING_TONE_KEY=CookingConstants.CHOSEN_RING_TONE_KEY;
    private static String defaultRingTone="clapping";


    //constructor calls SuperClass
    public PlayRingToneService() {
        super();
    }

     /*-------------------------------------------------------------------------------------------*/
    /*LifeCycle-Part: all methods of the Service-LifeCycle which are necessary for this Service----*/

    //receives the Intent sent by the CookingTimerActivity
    //selects the ringTone according to the extra in the Intent
    //starts Vibrator and MediaPlayer
    //START_STICKY: Constant to return from onStartCommand(Intent, int, int): if this service's process is killed while it is started (after returning from onStartCommand(Intent, int, int)), then leave it in the started state but don't retain this delivered intent.
    //from: https://developer.android.com/reference/android/app/Service.html, 14.09.17
    //START_STICKY is returned as the service should alarm the user, even if the device has run out of memory before
    @Override
    public int onStartCommand(Intent intent,int flags, int startId) {
        setRingToneTroughIntent(intent);
        vibrate();
        playMusic();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        stopMusic();
        stopVibrator();
        Log.d("RingToneService","destroyed");
        super.onDestroy();
    }



    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /*-------------------------------------------------------------------------------------------*/
    /*--------------Intent-Part: all methods to progress received intent-------------------------*/

    private void setRingToneTroughIntent(Intent intent){
        if(intent!=null)
        {
            String sendedRingTone=intent.getStringExtra(CHOSEN_RING_TONE_KEY);
            if(sendedRingTone!=null)
            {
                selectRingTone(sendedRingTone);
            }

            else
            {
                chosenRingtone=defaultRingTone;
                Log.d("RingToneService", "sendedRingTone is null");
            }

        }

    }


    /*-------------------------------------------------------------------------------------------*/
    /*--------------Music-Part: all methods to control the music of this service*----------------*/

    //creates a new MediaPlayer and plays the sound in looping
    //is used in onStartCommand() of this LifeCycle
    private void playMusic(){
        try{
            if(chosenRingtone!=null){
                mediaPlayer= MediaPlayer.create(this, getChosenRingtoneID(chosenRingtone));
            }
            else{
                mediaPlayer= MediaPlayer.create(this, R.raw.clapping);
                Log.d("RingToneService","chosenRingTone is null");
            }

            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        }

        catch (Exception e){Log.d("RingToneService","playMusic "+e.toString());}

    }

    //stops the MediaPlayer
    //is used in onDestroy() of this LifeCycle
    private void stopMusic(){
        if(mediaPlayer!=null)
        {
            mediaPlayer.stop();
        }

        else {Log.d("RingToneService", "mediaPlayer is null");}

    }

    //receives a String and selects the ringtone according to this String
    //is used in onStartCommand() of this LifeCycle
    private void selectRingTone(String ringtone){

        switch (ringtone){
            case CookingConstants.RING_TONE_CLAPPING:chosenRingtone="clapping";
                break;
            case CookingConstants.RING_TONE_EAGLE:chosenRingtone="eagle";
                break;
            case CookingConstants.RING_TONE_TRUMPET:chosenRingtone="trumpet";
                break;
            case CookingConstants.RING_TONE_WOLF:chosenRingtone="wolf";
                break;
            default:chosenRingtone="clapping";
                Log.d("RingToneService","default Ring Tone");
                break;
        }
    }

    //transforms the given String into an ID which is an int
    //this is necessary an MediaPlayer needs an int ID-value in his create(context, ID) method
    //returns the ID int value
    private int getChosenRingtoneID(String chosenRingtone){
        int ring_tone_id = this.getResources().getIdentifier(chosenRingtone, "raw",
                this.getPackageName());

        return ring_tone_id;
    }

    /*-------------------------------------------------------------------------------------------*/
    /*---------Vibration-Part: all methods to control the vibrator of this service---------------*/


    //creates a new vibrator and starts vibrating for 5 seconds
    //has a try catch pattern to avoid problems as vibrator needs permission
    //is used in onStartCommand() of this LifeCycle
    private void vibrate(){
        vibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        int vibrateTime = 5000;
        try{
            vibrator.vibrate(vibrateTime);
            Log.d("RingToneService","vibrates");
        } catch (Exception e)        {
            Log.d("RingToneService","can't vibrate: "+e);
        }
    }


    //stops the vibrator
    // is used in onDestroy() of this LifeCycle
    private void stopVibrator(){
        if(vibrator!=null)
        {
            vibrator.cancel();
        }

        else {Log.d("RingToneService", "vibrator is null");}

    }






}
