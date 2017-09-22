package com.example.ninah.cooking_app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


public class ChooseRingToneActivity extends AppCompatActivity {
    /*****************Acitvity of the timer function of the app************************************/
    /*----------------------------------------------------------------------------------------------
    * Logic: CookingTimerActivity starts this service------------------------------------------------------
    * user selects ringTone through radioGroup------------------------------------------------------
    * ringTone-string is send back in returnIntent through setResult(Activity.RESULT_OK,returnIntent);-----
    * Timer Activity receives ringTone-string in onActivityResult()-method*/

    //Buttons and radioGroup
    RadioGroup radioGroup;
    RadioButton radioButton;
    Button okButton;
    Button cancelButton;

    //constants of ringToneNames are copied from constant class
    private static final String RING_TONE_CLAPPING=CookingConstants.RING_TONE_CLAPPING;
    private static final String RING_TONE_EAGLE=CookingConstants.RING_TONE_EAGLE;
    private static final String RING_TONE_TRUMPET=CookingConstants.RING_TONE_TRUMPET;
    private static final String RING_TONE_WOLF=CookingConstants.RING_TONE_WOLF;

    //array of ringToneNames
    private String allTones[]={RING_TONE_CLAPPING, RING_TONE_EAGLE, RING_TONE_TRUMPET, RING_TONE_WOLF};

    //selected RingTone
    private static String selectedRingTone;

    /*---------------------------------------------------------------------------------------------*/
    /*----LifeCycle-Part:all methods of the Activity-LifeCycle which are necessary for this Activity*/

    //overwritten method of the Activity-Lifecycle
    //inits buttons, the radioGroup and sets the onClickListener
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ring_tone);
        initButtons();
        initRadioGroup();
        setButtonOnClickListener();
    }

     /*---------------------------------------------------------------------------------------------*/
     /*--------------clickListener-Part: all methods for the clickListener-------------------------*/

     //sets ClickListener on the buttons
     private void setButtonOnClickListener(){
         okButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 ensureThatRingToneIsNotNull();
                 returnToTimerActivity();
             }
         });

         cancelButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 cancelThisActivity();
             }
         });
     }

    //method for communication with the different radio buttons. Is used in the .xml-file
    //onClick-listener are in the .xml-file
    //pattern-oriented from: https://www.youtube.com/watch?v=n7c3bIWcgZo, 14.09.17
    public void radioButtonClick(View v){
        int radioButtonID= radioGroup.getCheckedRadioButtonId();
        radioButton=(RadioButton) findViewById(radioButtonID);
        String buttonName=radioButton.getResources().getResourceEntryName(radioButtonID);
        selectRingTone(buttonName);
    }

    /*---------------------------------------------------------------------------------------------*/
    /*-------init part: inits all editTexts, showTimeTextView and the buttons via the view ids-----*/

    //inits buttons
    private void initButtons(){
        okButton=(Button) findViewById(R.id.okButton);
        cancelButton=(Button) findViewById(R.id.cancelButton);
    }

    //inits RadioGroup
    private void initRadioGroup(){
        radioGroup= (RadioGroup) findViewById(R.id.radioGroup);
    }

     /*--------------------------------------------------------------------------------------------*/
    /*-------select part: all methods so user can select ringTone----------------------------------*/

    //sets selectedRingTone to clapping, if selectedRingTone is null
    private void ensureThatRingToneIsNotNull(){
        if(selectedRingTone.isEmpty()){
            selectedRingTone=RING_TONE_CLAPPING;
        }
    }

    //sets selectedRingTone via the parameter
    private void selectRingTone(String buttonName){
        for (String ringToneName:allTones ){
            if(ringToneName.equals(buttonName)){
                selectedRingTone=ringToneName;
                Toast.makeText(getBaseContext(), "Selected: "+selectedRingTone, Toast.LENGTH_SHORT).show();
            }
        }
    }


    /*--------------------------------------------------------------------------------------------*/
    /*----------return part: all methods so user can return to CookingTimerActivity----------------------*/

    //returns to CookingTimerActivity. Intent has selectedRingTone as extra. Finishes this activity.
    private void returnToTimerActivity(){
        Intent returnIntent = new Intent();
        returnIntent.putExtra(CookingTimerActivity.CHOSEN_RING_TONE_KEY,selectedRingTone);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }

    //returns to CookingTimerActivity. Intent has no selectedRingTone and no extra. Finishes this activity.
    private void cancelThisActivity(){
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED,returnIntent);
        finish();

    }


}
