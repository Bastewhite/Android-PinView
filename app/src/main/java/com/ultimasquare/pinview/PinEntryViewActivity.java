package com.ultimasquare.pinview;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

/**
 * PinEntryViewActivity
 * Android-PinView
 * <p/>
 * Created by Francisco Aguilar on 01/04/2015.
 * Copyright (c) 2015 Axa Group Solutions. All rights reserved.
 */
public class PinEntryViewActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_entry_view);

        Typeface xpressive = Typeface.createFromAsset(getAssets(), "fonts/XpressiveBold.ttf");

        TextView titleView = (TextView) findViewById(R.id.titleBox);
        titleView.setTypeface(xpressive);

        PinEntryView pinEntryView = (PinEntryView) findViewById(R.id.pinEntryView);
        pinEntryView.init("8888", new PinEntryView.Listener() {
            @Override
            public void onFinish() {
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        //App not allowed to go back to Parent activity until correct pin entered.
        //super.onBackPressed();
    }
}