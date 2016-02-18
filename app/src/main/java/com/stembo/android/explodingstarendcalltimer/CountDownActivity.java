package com.stembo.android.explodingstarendcalltimer;

import java.util.Calendar;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class CountDownActivity extends Activity {
    /** Private members of the class */
    private EditText displayTime;
    private Button pickTime;
    private Button startTimer;
    private TextView timeRemaining;
    private Button explode;

    private int pMinutesLeft;
    private int pSecondsLeft;
    /** This integer will uniquely define the
     * dialog to be used for displaying time picker.*/
    static final int TIME_DIALOG_ID = 0;

    /** Callback received when the user "picks" a time in the dialog */
    private TimePickerDialog.OnTimeSetListener mTimeSetListener =
            new TimePickerDialog.OnTimeSetListener() {
                public void onTimeSet(TimePicker view, int minLeft, int secLeft) {
                    pMinutesLeft = minLeft;
                    pSecondsLeft = secLeft;
                    updateDisplay();
                    displayToast();
                }
            };

    /** Updates the time in the TextView */
    private void updateDisplay() {
        /**displayTime.setText(
         new StringBuilder()
         .append(pad(pMinutesLeft)).append(":")
         .append(pad(pSecondsLeft)));*/
        displayTime.setText(Integer.toString(pSecondsLeft));
    }

    /** Displays a notification when the time is updated */
    private void displayToast() {
        Toast.makeText(this, new StringBuilder().append("Time choosen is ")
                .append(displayTime.getText()),   Toast.LENGTH_SHORT).show();

    }

    /** Add padding to numbers less than ten */
    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.stembo.android.explodingstarendcalltimer.R.layout.main);

        /** Capture our View elements */
        displayTime = (EditText) findViewById(R.id.timeDisplay);
        pickTime = (Button) findViewById(R.id.pickTime);
        startTimer = (Button) findViewById(R.id.startTimer);
        //timeRemaining = (TextView) findViewById(R.id.timeRemaining);
        explode = (Button) findViewById(R.id.explodeButton);

        /** Listener for click event of the pick button */
        pickTime.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startTimer.setEnabled(true);
                showDialog(TIME_DIALOG_ID);
            }
        });

        displayTime.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //nothing yet
            }
        });

        /**Listener for click event of the start button */
        startTimer.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startTimer.setEnabled(false);
                displayTime.setEnabled(false);
                /**StringTokenizer st = new StringTokenizer(displayTime.getText().toString(), ":");
                 while (st.hasMoreElements()){
                 st.nextElement();
                 long pSecondsTimer = Long.parseLong(st.nextToken());
                 }*/
                //timeRemaining.setText(displayTime.getText()+" Token="+ pSecondsLeft);
                long pSecondsLeft = Long.parseLong(displayTime.getText().toString());
                long oneSecondInterval = 1000;
                MyCount counter = new MyCount(1000*pSecondsLeft, oneSecondInterval);
                counter.start();
            }
        });

        explode.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Toast.makeText(getApplicationContext(), R.string.explode_text, Toast.LENGTH_SHORT).show();
                new CountDownTimer(3000, 1000){
                    public void onTick(long millisUntilFinished){
                        //do nothing
                    }

                    public void onFinish(){
                        //do nothing
                        System.exit(0);
                    }
                }.start();
            }
        });

        /** Get the current time */
        final Calendar cal = Calendar.getInstance();
        pMinutesLeft = cal.get(Calendar.HOUR_OF_DAY);
        pSecondsLeft = cal.get(Calendar.MINUTE);

        /** Display the current time in the TextView */
        updateDisplay();
    }

    /** Create a new dialog for time picker */

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case TIME_DIALOG_ID:
                return new TimePickerDialog(this,
                        mTimeSetListener, pMinutesLeft, pSecondsLeft, true);
        }
        return null;
    }

    public class MyCount extends CountDownTimer {
        public MyCount(long pSecondsLeft, long countDownInterval){
            super(pSecondsLeft, countDownInterval);
        }

        @Override
        public void onTick(long pSecondsLeft){
            displayTime.setText("Time remaining: " + pSecondsLeft/1000);
        }

        @Override
        public void onFinish(){
            displayTime.setText("Countdown Complete!");
            startTimer.setEnabled(true);
            displayTime.setEnabled(true);
            disconnectCall();
        }

    }

    public void disconnectCall(){
        PhoneStateReceiver psr = new PhoneStateReceiver();
        psr.killCall(getApplicationContext());

    }
}