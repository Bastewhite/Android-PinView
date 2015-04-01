package com.ultimasquare.pinview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PinEntryView extends LinearLayout implements View.OnClickListener{

    private String userEntered;
    private String userPin;

    private final int PIN_LENGTH = 4;
    private boolean keyPadLockedFlag = false;

    private TextView[] pinBoxArray;

    private TextView statusView;
    private Listener mListener;

    public interface Listener {
        void onFinish();
    }

    public PinEntryView(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(getContext()).inflate(R.layout.view_pin_entry, this, true);

        if (!isInEditMode()) {
            initViews();
        }
    }

    public void init(String userPin, Listener listener) {
        this.userPin = userPin;
        this.mListener = listener;
    }

    private void initViews() {
        userEntered = "";

        Typeface xpressive = Typeface.createFromAsset(getContext().getAssets(), "fonts/XpressiveBold.ttf");

        Button buttonExit = (Button) findViewById(R.id.buttonExit);
        buttonExit.setTypeface(xpressive);
        buttonExit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        pinBoxArray = new TextView[PIN_LENGTH];
        pinBoxArray[0] = (TextView) findViewById(R.id.pinBox0);
        pinBoxArray[1] = (TextView) findViewById(R.id.pinBox1);
        pinBoxArray[2] = (TextView) findViewById(R.id.pinBox2);
        pinBoxArray[3] = (TextView) findViewById(R.id.pinBox3);


        statusView = (TextView) findViewById(R.id.statusMessage);
        statusView.setTypeface(xpressive);

        Button button0 = (Button) findViewById(R.id.button0);
        button0.setTypeface(xpressive);
        button0.setOnClickListener(this);

        Button button1 = (Button) findViewById(R.id.button1);
        button1.setTypeface(xpressive);
        button1.setOnClickListener(this);

        Button button2 = (Button) findViewById(R.id.button2);
        button2.setTypeface(xpressive);
        button2.setOnClickListener(this);

        Button button3 = (Button) findViewById(R.id.button3);
        button3.setTypeface(xpressive);
        button3.setOnClickListener(this);

        Button button4 = (Button) findViewById(R.id.button4);
        button4.setTypeface(xpressive);
        button4.setOnClickListener(this);

        Button button5 = (Button) findViewById(R.id.button5);
        button5.setTypeface(xpressive);
        button5.setOnClickListener(this);

        Button button6 = (Button) findViewById(R.id.button6);
        button6.setTypeface(xpressive);
        button6.setOnClickListener(this);

        Button button7 = (Button) findViewById(R.id.button7);
        button7.setTypeface(xpressive);
        button7.setOnClickListener(this);

        Button button8 = (Button) findViewById(R.id.button8);
        button8.setTypeface(xpressive);
        button8.setOnClickListener(this);

        Button button9 = (Button) findViewById(R.id.button9);
        button9.setTypeface(xpressive);
        button9.setOnClickListener(this);

        Button buttonDelete = (Button) findViewById(R.id.buttonDeleteBack);
        buttonDelete.setTypeface(xpressive);
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (keyPadLockedFlag) {
                    return;
                }

                if (userEntered.length() > 0) {
                    userEntered = userEntered.substring(0, userEntered.length() - 1);
                    pinBoxArray[userEntered.length()].setText("");
                }
            }
        });
    }

    @Override
    public void onClick(View v) {

        if (keyPadLockedFlag) {
            return;
        }

        Button pressedButton = (Button) v;


        if (userEntered.length() < PIN_LENGTH) {
            userEntered = userEntered + pressedButton.getText();
            Log.v("PinView", "User entered=" + userEntered);

            //Update pin boxes
            pinBoxArray[userEntered.length() - 1].setText("*");

            if (userEntered.length() == PIN_LENGTH) {
                //Check if entered PIN is correct
                if (userEntered.equals(userPin)) {
                    statusView.setTextColor(Color.GREEN);
                    statusView.setText("Correct");
                    Log.v("PinView", "Correct PIN");
                    finish();
                } else {
                    statusView.setTextColor(Color.RED);
                    statusView.setText("Wrong PIN. Keypad Locked");
                    keyPadLockedFlag = true;
                    Log.v("PinView", "Wrong PIN");

                    new LockKeyPadOperation().execute("");
                }
            }
        } else {
            //Roll over
            pinBoxArray[0].setText("");
            pinBoxArray[1].setText("");
            pinBoxArray[2].setText("");
            pinBoxArray[3].setText("");

            userEntered = "";

            statusView.setText("");

            userEntered = userEntered + pressedButton.getText();
            Log.v("PinView", "User entered=" + userEntered);

            //Update pin boxes
            pinBoxArray[userEntered.length() - 1].setText("*");

        }
    }

    private void finish() {
        if (mListener != null) {
            mListener.onFinish();
        }
    }

    private class LockKeyPadOperation extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            for (int i = 0; i < 2; i++) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
            statusView.setText("");

            //Roll over
            pinBoxArray[0].setText("");
            pinBoxArray[1].setText("");
            pinBoxArray[2].setText("");
            pinBoxArray[3].setText("");

            userEntered = "";

            keyPadLockedFlag = false;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }
}
