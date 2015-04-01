package com.ultimasquare.pinview;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.InjectViews;
import butterknife.OnClick;

public class PinEntryView extends LinearLayout {

    private String userEntered;
    private String userPin;

    private final int PIN_LENGTH = 4;
    private boolean keyPadLockedFlag = false;

    @InjectView(R.id.statusMessage) TextView statusView;
    @InjectViews({ R.id.pinBox0, R.id.pinBox1, R.id.pinBox2, R.id.pinBox3 }) TextView[] pinBoxArray;

    private Listener mListener;

    public interface Listener {
        void onFinish();
    }

    public PinEntryView(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(getContext()).inflate(R.layout.view_pin_entry, this, true);
        ButterKnife.inject(this, this);
    }

    public void init(String userPin, Listener listener) {
        this.userPin = userPin;
        this.mListener = listener;
        userEntered = "";
    }

    @OnClick(R.id.buttonDeleteBack)
    public void onDeleteClick(View v) {
        if (keyPadLockedFlag) {
            return;
        }

        if (userEntered.length() > 0) {
            userEntered = userEntered.substring(0, userEntered.length() - 1);
            pinBoxArray[userEntered.length()].setText("");
        }
    }

    @OnClick({R.id.button0, R.id.button1, R.id.button2, R.id.button3, R.id.button4, R.id.button5, R.id.button6, R.id.button7, R.id.button8, R.id.button9})
    public void onNumberClick(Button pressedButton) {

        if (keyPadLockedFlag) {
            return;
        }

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
        }
        else {
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

    @OnClick(R.id.buttonExit)
    public void finish() {
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
