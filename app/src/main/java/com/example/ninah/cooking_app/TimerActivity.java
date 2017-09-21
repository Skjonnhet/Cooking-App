package com.example.ninah.cooking_app;

import android.content.Intent;
import android.content.IntentFilter;


import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.ninah.cooking_app.ChooseRingToneActivity;
import com.example.ninah.cooking_app.CookingTimerService;
import com.example.ninah.cooking_app.MovingSensor;
import com.example.ninah.cooking_app.TimeReceiver;

public class TimerActivity extends AppCompatActivity implements SensorEventListener {

    /*MainActivity of the timer function of the app
    *----------------------------------------------------------------------------------
    * --------------Timer Function----------------------------------------------------
    * TimerActivity starts a new timer via the sending an intent to the CookingTimerService
    * CookingTimerService creates a new CookingTimer
     * CookingTimer counts down to zero an sends calculated time via the CookingTimerListener to the CookingTimerService
     * CookingTimerService sends a BroadCast to TimeReceiver
     * TimeReceiver updates the time in the showTimeTextView of the TimerActivity via a runOnUiThread
     * Order: TimerActivity->CookingTimerService->CookingTimer->CookingTimerService (BroadCast)->TimeReceiver(BroadCastReceiver)->TimerActivity(GUI)
     *
     *  --------------SelectTingTone Function----------------------------------------------------
     *  TimerActivity starts the ChooseRingToneActivity via startActivityForResult
     *  user chooses ringTone in ChooseRingToneActivity
     *  ringTone is sent back to TimerActivity
     *  TimerActivity gets new ringTone as Result
     *  Order: TimerActivity->ChooseRingToneActivity->Result(RingTone)->TimerActivity
     *
     *   --------------Alarm Function----------------------------------------------------
     *TimerActivity starts PlayRingToneService if Timer Function receives that time has counted to 00:00
     * PlayRingToneService includes an MediaPlayer and a Vibrator
     * PlayRingToneService is stopped by pressing the resetTimerButton, moving the device so MovingSensor recognize motion
     * or by finishing the TimerActivity
     *  Order: TimerActivity->PlayRingToneService
    * */

    //Edit texts
    private EditText editTextForSeconds;
    private EditText editTextForMinutes;
    private EditText editTextForHour;

    //Buttons
    private Button startCookingButton;
    private Button resetTimerButton;
    private Button selectRingToneButton;

    //TextViews
    private TextView showTimeTextView;

    //TimeReceiver and TimeActivity
    private TimeReceiver timeReceiver;
    private static TimerActivity timerActivity;


    //finals for the communication with the cookingTimerService and the TimReceiver
    public final static String KEY_CURRENT_TIME="KEY_CURRENT_TIME";
    public final static String CURRENT_TIME_ACTION="CURRENT_TIME_ACTION";
    public final static IntentFilter TIME_INTENT_FILTER =new IntentFilter(CURRENT_TIME_ACTION);
    public final static String COOKING_TIMER_FINISHED="COOKING_TIMER_FINISHED";

    //chosen RingTone
    private static String chosenRingTone;
    public static final int RING_TONE_REQUEST_CODE = 1;
    public static final String CHOSEN_RING_TONE_KEY="CHOSEN_RING_TONE_KEY";

    //private MovingSensor movingSensor;
    private MovingSensor movingSensor;

    //DBAdapter for coomunication with DB
    DBAdapter dbAdapter;

    private Long recipeId;


     /*--------------------------------------------------------------------------*/
    /*LifeCycle-Part: all methods of the Activity-LifeCycle which are necessary for this Activity*/


    //overwritten method of the Activity-LifeCycle
    //inits all components and sets the ClickListener
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        initAdapter();
        initEditTexts();
        initButtons();
        initTextView();
        initTimeReceiver();
        initMovingSensor();
        initButtonStatus();
        //context=getApplicationContext();
        initTimerActitivty();
        setCookingTimerClickListener();
        setRingToneClickListener();
        setRecipeId();//1.get recipeID
        setStartTimeToTextView();//2.set time trough recipeID
    }

    //overwritten method of the Activity-LifeCycle
    //registers TimeReceiver
    @Override
    protected void onStart() {
        registerTimeReceiver();
        Log.d("TimerActivty","Receiver registered "+timeReceiver);
        super.onStart();
    }

    @Override
    protected void onResume(){
        super.onResume();
    }

    //overwritten method of the Activity-LifeCycle

    @Override
    protected void onStop() {
        Log.d("TimerActivity","Receiver unregistered");
        super.onStop();
    }

    //destroys all services if this activity is destroyed and unregisters TimeReceiver
    @Override
    protected void onDestroy() {
        unregisterTimeReceiver();
        stopAllServices();
        super.onDestroy();
    }

    /*--------------------------------------------------------------------------*/
    /*clickListener-Part: all methods for the clickListener*/

    //setsListener on the start- and resetButton
    private void setCookingTimerClickListener(){
        startCookingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startCooking();
            }
        });

        resetTimerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetTimer();
                stopAllServices();
            }
        });

    }

    //setsListener on selectRingToneButton
    private void setRingToneClickListener(){
        selectRingToneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectRingTone();
            }
        });
    }



    /*--------------------------------------------------------------------------*/
    /*init part: all editTexts, showTimeTextView and the buttons via the view ids
     *the status (enabled, disabled) of the buttons are set, inits a new timeReceiver-object and a new movingSensor-Object
     * this part is used in the on createMethod();
      */

    //inits all editTexts
    private void initEditTexts() {
        editTextForSeconds = (EditText) findViewById(R.id.editTextForSeconds);
        editTextForMinutes = (EditText) findViewById(R.id.editTextForMinutes);
        editTextForHour = (EditText) findViewById(R.id.editTextForHours);
    }

    private void initAdapter(){
        dbAdapter=new DBAdapter(this);
    }

    //inits all showTimeTextView
    private void initTextView() {
        showTimeTextView = (TextView) findViewById(R.id.textViewTimer);
    }

    //inits Buttons
    private void initButtons() {
        startCookingButton = (Button) findViewById(R.id.startCookingButton);
        resetTimerButton = (Button) findViewById(R.id.resetButton);
        selectRingToneButton = (Button) findViewById(R.id.ringtoneButton);
    }

    //sets buttons en -and disabled at the start, so user can only use the cooking-button
    private void initButtonStatus() {
        startCookingButton.setEnabled(true);
        resetTimerButton.setEnabled(false);
    }

    //inits time Receiver
    private void initTimeReceiver(){
        timeReceiver=new TimeReceiver();
        Log.d("TimerActivity","Receiver constructed "+timeReceiver);
    }

    //inits MovingSensor
    private void initMovingSensor(){
        movingSensor=new MovingSensor(this,this);
    }

    private void initTimerActitivty(){
        timerActivity=this;
    }

    private void setRecipeId(){
        try{
            Intent intent=getIntent();
            Bundle extras=intent.getExtras();
            recipeId=extras.getLong(CookingConstants.RECIPE_ID_KEY);
            Log.d("TimerActivity","setRecipeId recipeID: "+recipeId);
        }

        catch (Exception e){giveFeedback("setRecipeId", e.toString());}

    }

    private void setStartTimeToTextView(){
        try{
            String time=calculateRecipeTime(getRecipeTime());
            showTimeTextView.setText(time);
        }
        catch (Exception e){giveFeedback("setStartTimeToTextView ",e.toString());}

    }

    private void setStartTimeToEdiTexts(int hours, int minutes, int seconds){
       editTextForSeconds.setText(""+seconds);
       editTextForMinutes.setText(""+minutes);
       editTextForHour.setText(""+hours);
    }

    private int getRecipeTime(){
        int timeInMinutes=0;
        if(recipeId!=null){
            try{
                timeInMinutes=dbAdapter.getRecipeByID(recipeId).getTimeInMinutes();
            }
            catch (Exception e){giveFeedback("getRecipeTime()",e.toString());}

        }

        return timeInMinutes;
    }

    private String calculateRecipeTime(int timeInMinutes){
        final String timeFormat = "%02d:%02d:%02d";
        int timeInSecondsLeft = timeInMinutes*60;
        int hours = timeInSecondsLeft / 3600;
        int minutes = (timeInSecondsLeft % 360) / 60;
        int seconds = timeInSecondsLeft % 60;

        String currentTime=  String.format(timeFormat, hours, minutes, seconds);
        setStartTimeToEdiTexts(hours,minutes,seconds);

        return currentTime;
    }



    /*--------------------------------------------------------------------------*/
    /*changing-part:all-methods to change the status of the UI-control-panels

     */
    //changes button from dis- to enabled and reverse, each time it is called
    private void switchButtonsStatus() {
        boolean isCooking = startCookingButton.isEnabled();
        boolean isReset = resetTimerButton.isEnabled();
        startCookingButton.setEnabled(!isCooking);
        resetTimerButton.setEnabled(!isReset);
    }

    //sets EditTexts invisible, so user can't enter new times. this protects the work of the cookingTimerService
    private void setEditTextsInvisible() {
        editTextForSeconds.setVisibility(View.GONE);
        editTextForMinutes.setVisibility(View.GONE);
        editTextForHour.setVisibility(View.GONE);
    }

    //sets EditTexts visible, so user can enter new times.
    private void setEditTextsVisible() {
        editTextForSeconds.setVisibility(View.VISIBLE);
        editTextForMinutes.setVisibility(View.VISIBLE);
        editTextForHour.setVisibility(View.VISIBLE);
    }

    //resets edit Texts
    private void resetEditTexts() {
        editTextForSeconds.setText("");
        editTextForMinutes.setText("");
        editTextForHour.setText("");
    }

    /*--------------------------------------------------------------------------*/
    /*cooking-part: all-methods to control the program if the user wants to start the
     * cooking by setting and starting a new  timer or wants to reset the timer */

    //starts cooking: gets the time, which the user put in the editTexts, changes the status of the buttons, gives the startCookingTimerService-method the time, which starts a new cookingTimerService
    private void startCooking() {
        int cookingTime=getTimeInSeconds();
        switchButtonsStatus();
        startCookingTimerService(cookingTime);
        Log.d("TimerActivty","cooking started "+ cookingTime);
    }

    //returns the values of the entered time in seconds. therefore calculates the hours and minutes into seconds and sums them up, sets edit texts invisible.
    private int getTimeInSeconds(){
        String enteredSeconds = editTextForSeconds.getText().toString();
        String enteredMinutes = editTextForMinutes.getText().toString();
        String enteredHours = editTextForHour.getText().toString();
        setEditTextsInvisible();

        int seconds = 0;
        int minutes = 0;
        int hours = 0;


        if (!enteredSeconds.equalsIgnoreCase("")) {
            seconds = Integer.valueOf(enteredSeconds);
        }

        if (!enteredMinutes.equalsIgnoreCase("")) {
            minutes = Integer.valueOf(enteredMinutes);
        }

        if (!enteredHours.equalsIgnoreCase("")) {
            hours = Integer.valueOf(enteredHours);
        }

        int timeInSeconds = seconds + minutes * 60 + hours * 3600;
        return timeInSeconds;
    }

    //starts a new cooking timer service. the intent has the given parameter timeInSeconds as an extra.
    private void startCookingTimerService(int timeInSeconds){
        Intent intent=new Intent(this, CookingTimerService.class);
        intent.putExtra(KEY_CURRENT_TIME, timeInSeconds);
        startService(intent);
        Log.d("TimerActivity","startService(cookingTimerService)");
    }

    //stops the cooking timer service by sending an intent.
    private void stopTimerService(){
        Intent intent =new Intent(this, CookingTimerService.class);
        stopService(intent);
    }

    //resets timer by using the methods of the changing-part
    private void resetTimer() {
        setEditTextsVisible();
        resetEditTexts();
        switchButtonsStatus();
    }

    //reports if the timer has finished. sets the showTimeTextView and starts the RingToneService and the MovingSensor
    private void reportTimerHasFinished() {
        showTimeTextView.setText("00:00:00");
        startRingToneService();
        startMovingSensor();
        Log.d("TimerActivity", "timerHasFinished");
    }

    /*--------------------------------------------------------------------------*/
    /*RingTone-part: all methods regarding the PlayRingToneService(ringToneService)
    * this part is important after the timer has counted to 00:00
    * */

    //starts a new Activity (chooseRingToneActivity) with startActivityForResult(intent, code) so the user can select the ringtone in a new activity
    private void selectRingTone(){
        Log.d("TimerActivity", "RingTone selected");
        Intent selectRingToneIntent= new Intent(this, ChooseRingToneActivity.class);
        startActivityForResult(selectRingToneIntent, RING_TONE_REQUEST_CODE);
    }

    //Receives the values of the chooseRingToneActivity after the user has finished it. Sets the chosenRingTone-variable
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == RING_TONE_REQUEST_CODE) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                if(data!=null){
                    if(data.getStringExtra(CHOSEN_RING_TONE_KEY)!=null){
                        chosenRingTone=data.getStringExtra(CHOSEN_RING_TONE_KEY);
                        Log.d("TimerActivity", "chosenRingTone: "+chosenRingTone);
                    }
                }
            }
        }
    }


    //starts a new RingToneService. the intent has the chosenRingTone-variable as an extra.
    //trough the service the ringtone is played and the device begins to vibrate.
    private void startRingToneService(){
        Intent intent =new Intent(this, PlayRingToneService.class);
        intent.putExtra(CHOSEN_RING_TONE_KEY,chosenRingTone);
        startService(intent);
    }

    //stops the RingToneService. the device stops vibrating abd playing the chosen ringtone.
    private void stopRingToneService(){
        Intent intent =new Intent(this, PlayRingToneService.class);
        stopService(intent);
    }

    /*--------------------------------------------------------------------------*/
    /*TimeReceiver-part: all methods to control the TimeReceiver
    * this receiver is necessary so this Activity (TimerActivity) can communicate with the CookingTimerService
    * the TimeReceiver receives the values of that service and updates the GUI of the TimerActivity */

    //registers the TimeReceiver via the LocalBroadcastManager at this Activity(TimerActivity)
    //it is used in onStart()-method of the ActivityLifecycle so each time the user switches to another activity
    //and back it is ensured the receiver is registered
    private void registerTimeReceiver(){
        LocalBroadcastManager.getInstance(this).registerReceiver(timeReceiver, TIME_INTENT_FILTER);
    }

    //unregisters the TimeReceiver via the LocalBroadcastManager at this Activity(TimerActivity)
    //it is used in onStop()-method of the ActivityLifecycle so each time the user switches to another activity
    //it is ensured the receiver is unregistered even if the Activity is not killed via onDestroy()
    //this safes memory
    private void unregisterTimeReceiver(){
        LocalBroadcastManager.getInstance(this).unregisterReceiver(timeReceiver);
    }

    //getTimerActivity  and setCurrentTimer are for the communication with the receiver

    //returns the running TimerActivity(this)
    //it is used by the TimeReceiver for setting the time in this TimerActivity
    public static TimerActivity getTimerActivity() {
        if (timerActivity != null) {
            return timerActivity;
        }

        else {
            Log.d("TimerActivity", "timerActivity is null");
            return null;
        }
    }

    //sets currentTime in the showTimeTextView
    //updates the GUI through the non-UI-thread receiver (TimeReceiver), therefore uses runOnUiThread(action)
    // checks if the cookingTime is unequal the time when the timer has finished
    //if so updates the showTimeTextView, otherwise starts reportTimerHasFinished()
    //is is used in the TimeReceiver to update the time
    public void setCurrentTime(final String currentTime) {
        TimerActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                if(currentTime!=COOKING_TIMER_FINISHED) {

                    showTimeTextView.setText(currentTime);
                }

                else {

                    reportTimerHasFinished();
                }
            }
        });
    }


    /*--------------------------------------------------------------------------*/
    /*SensorEventListener-part: starts and stops the moving sensor and notices sensorChanges */


    //starts the movingSensor. It is used after timer has reached 00:00
    private void startMovingSensor(){
        movingSensor.start();
    }

    //stops the movingSensor. It is used after the SensorRegistered had notice changes.
    private void stopMovingSensor(){
        movingSensor.stop();
    }


    //overwritten method of the SensorEventListener
    // if the moving sensor notices changes(=motion of the device) the RingToneService will be stopped
    //and the sensorListener MovingSensor stops.
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            if(movingSensor!=null) {
                if (movingSensor.hasBeenMoved(event.values.clone())) {
                    //beende Alarm-Service
                    stopRingToneService();
                    stopMovingSensor();
                }
            }

            else {Log.d("TimerActivity", "movingsensor is null");}

        }
    }

    //overwritten method of the SensorEventListener.
    //not used in this Activity.
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    /*-------------------------------------------------------------------------------------*/
    /*Destroy-part:all methods which are necessary to ensure a safe finishing of TimerActivity*/

    //stops all services
    //used in the onDestroy-method
    private void stopAllServices(){
        stopTimerService();
        stopRingToneService();

    }



    //helper-methods:help in this class
    private static void giveFeedback(String method, String feedback) {
        Log.d("TimerActvitiy " + method, feedback);
    }






}
