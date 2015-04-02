package com.ultimasquare.pinview;

import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.github.orangegangsters.lollipin.lib.views.KeyboardButtonView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.InjectViews;
import butterknife.OnClick;

public class PinCheckerView extends LinearLayout {

    private String userEntered;
    private String userPin;

    private static final int PIN_LENGTH = 4;
    private boolean keyPadLockedFlag = false;

    @InjectViews({ R.id.pin_code_round1, R.id.pin_code_round2, R.id.pin_code_round3, R.id.pin_code_round4 }) List<ImageView> mRoundViews;
    @InjectView(R.id.view_keyboard) View mKeyboardView;

    private Listener mListener;

    public interface Listener {
        void onFinish();
    }

    public PinCheckerView(Context context) {
        this(context, null);
    }

    public PinCheckerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PinCheckerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initializeView(attrs, defStyleAttr);
    }

    private void initializeView(AttributeSet attrs, int defStyleAttr) {
        LayoutInflater.from(getContext()).inflate(R.layout.view_pin_entry, this, true);
        ButterKnife.inject(this, this);
    }

    public void init(String userPin, Listener listener) {
        this.userPin = userPin;
        this.mListener = listener;
        userEntered = "";
    }

    @OnClick(R.id.pin_code_button_clear)
    public void onDeleteClick(View v) {
        if (keyPadLockedFlag) {
            return;
        }

        if (userEntered.length() > 0) {
            userEntered = userEntered.substring(0, userEntered.length() - 1);
            refreshPinCodeRound(userEntered.length());
        }
    }

    @OnClick({R.id.pin_code_button_0, R.id.pin_code_button_1, R.id.pin_code_button_2, R.id.pin_code_button_3,
            R.id.pin_code_button_4, R.id.pin_code_button_5, R.id.pin_code_button_6, R.id.pin_code_button_7, R.id.pin_code_button_8, R.id.pin_code_button_9})
    public void onNumberClick(KeyboardButtonView pressedButton) {

        if (keyPadLockedFlag) {
            return;
        }

        if (userEntered.length() < PIN_LENGTH) {
            userEntered = userEntered + pressedButton.getText();
            Log.v("PinView", "User entered=" + userEntered);

            //Update pin boxes
            refreshPinCodeRound(userEntered.length());

            if (userEntered.length() == PIN_LENGTH) {
                //Check if entered PIN is correct
                if (userEntered.equals(userPin)) {
                    Log.v("PinView", "Correct PIN");
                    finish();
                } else {
                    onPinCodeError();
                }
            }
        }
        else {
            userEntered = "";

            userEntered = userEntered + pressedButton.getText();
            Log.v("PinView", "User entered=" + userEntered);

            //Update pin boxes
            refreshPinCodeRound(userEntered.length());
        }
    }

    @OnClick(R.id.pin_code_button_10)
    public void finish() {
        if (mListener != null) {
            mListener.onFinish();
        }
    }

    /**
     * Run a shake animation when the password is not valid.
     */
    protected void onPinCodeError() {
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.shake);
        mKeyboardView.startAnimation(animation);

        keyPadLockedFlag = true;
        Log.v("PinView", "Wrong PIN");

        new LockKeyPadOperation().execute("");
    }

    /**
     * Refresh the {@link android.widget.ImageView}s to look like what typed the user
     *
     * @param pinLength the current pin code length typed by the user
     */
    public void refreshPinCodeRound(int pinLength) {
        for (int i = 0; i < mRoundViews.size(); i++) {
            if (pinLength - 1 >= i) {
                mRoundViews.get(i).setBackgroundResource(R.drawable.pin_code_round_full);
            } else {
                mRoundViews.get(i).setBackgroundResource(R.drawable.pin_code_round_empty);
            }
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
            //Roll over
            refreshPinCodeRound(0);

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
