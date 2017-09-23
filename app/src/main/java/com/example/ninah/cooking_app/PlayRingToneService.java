package com.example.ninah.cooking_app;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Vibrator;
import android.util.Log;

public class PlayRingToneService extends Service implements MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener, MediaPlayer.OnSeekCompleteListener, MediaPlayer.OnInfoListener, MediaPlayer.OnBufferingUpdateListener {
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
    * mediaPlayer is very sensitive to its lifecyclestate. therefore it's behaviour is controlled trough booleans and MediaPlayer Listeners
    * source https://developer.android.com/reference/android/media/MediaPlayer.html, 23.09.17
    * */


    private static MediaPlayer mediaPlayer;
    private Vibrator vibrator;
    private static String chosenRingtone;
    private static String CHOSEN_RING_TONE_KEY;
    private static String defaultRingTone;
    private boolean isMediaPlayerStopped ;
    private boolean isMediaPlayerPrepared;


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
        setDefaultValues();
        setRingToneTroughIntent(intent);
        vibrate();
        playMusic();
        Log.d("RingToneService","started");
        return START_STICKY;
    }

    //service can only be destroyed if mediaplayer has been stopped
    //avoids that mediplayer runs in the background while ringTone-Service has been shut down
    //mediaplayer can only be stopped if mediplayer is prepared avoids mediaplayer exceptions
    @Override
    public void onDestroy() {
        if(isMediaPlayerPrepared) {
            stopMusic();
            Log.d("RingToneService", "destroyed");
            stopVibrator();
            if (isMediaPlayerStopped)
                super.onDestroy();
        }
    }



    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /*-------------------------------------------------------------------------------------------*/
    /*--------------Intent-Part: all methods to progress received intent-------------------------*/

    //sets default booleans
    //isMediaPlayerStopped and isMediaPlayerPrepared protect mediaplayer from acting in the wrong lifecyclestate
    private void setDefaultValues(){
        isMediaPlayerStopped =false;
        isMediaPlayerPrepared=false;
        defaultRingTone="clapping";
        CHOSEN_RING_TONE_KEY=CookingConstants.CHOSEN_RING_TONE_KEY;
    }

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
    //waits 3 seconds in handler to avoid exception because media player is in the idlestate https://developer.android.com/reference/android/media/MediaPlayer.html
    private void playMusic(){
        try{
            if(chosenRingtone!=null){
                mediaPlayer= MediaPlayer.create(this, getChosenRingtoneID(chosenRingtone));

            }
            else{
                mediaPlayer= MediaPlayer.create(this, R.raw.clapping);
                Log.d("RingToneService","chosenRingTone is null");
            }

           setMPListeners();
           prepareMediaPlayer();


        }catch(Exception e){Log.d("RingToneService","playMusic "+e.toString());}
    }



    //stops the MediaPlayer according to https://developer.android.com/reference/android/media/MediaPlayer.html#setLooping(boolean) to avoid execptions
    //is used in onDestroy() of this LifeCycle
    private void stopMusic(){
        if(isMediaPlayerPrepared){
            try
            {
                mediaPlayer.stop();
                isMediaPlayerStopped =true;
                mediaPlayer.release();
                mediaPlayer=null;
                Log.d("RingToneService", "stopMusic mediaPlayer stopped");
            }
            catch (Exception e){Log.d("RingToneService", "stopMusic "+e.toString());}
        }



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


    //-------------------------control media player part-------------------------------------------
    //mediaplayer is very sensitive needs to be controlled
    //overwritten methods to handle mediaplayer errors

    //listener controll the mediaplayer to avoid execeptions
    private void setMPListeners(){
        mediaPlayer.setOnErrorListener(this);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnBufferingUpdateListener(this);
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnBufferingUpdateListener(this);
        mediaPlayer.setOnSeekCompleteListener(this);
        mediaPlayer.setOnInfoListener(this);

    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        resetMediaPlayer();
        Log.d("RingToneService", "onError mediaplayer created error" +"what :"+what +"extra: "+extra);
        return false;
    }

    //prepares mediaplayer and sets boolean isMediaPlayerPrepared true
    private void prepareMediaPlayer() {
        try {

            if(!mediaPlayer.isLooping())
                mediaPlayer.setLooping(true);
            if(!mediaPlayer.isPlaying())
                mediaPlayer.start();
            isMediaPlayerPrepared = true;
        }
        catch (Exception e) {
            {
                Log.d("RingToneService", "playMusic " + e.toString());
            }

        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (mediaPlayer!=null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            stopSelf();
        }
        else return;
    }


    private void resetMediaPlayer(){
        mediaPlayer.stop();
        mediaPlayer.reset();
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {

    }



    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        prepareMediaPlayer();
    }


    @Override
    public void onSeekComplete(MediaPlayer mp) {

    }




}
