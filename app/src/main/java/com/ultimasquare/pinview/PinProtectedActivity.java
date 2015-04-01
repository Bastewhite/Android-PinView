package com.ultimasquare.pinview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class PinProtectedActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_protected_activity);

        ButterKnife.inject(this);
    }

    @OnClick(R.id.buttonEnterPin)
    public void onClick(View v) {
        startActivity(new Intent(PinProtectedActivity.this, PinCheckerActivity.class));
    }

    @OnClick(R.id.buttonEnterPin2)
    public void onClick2(View v) {
        new PinCheckerDialog(PinProtectedActivity.this, getWindow().getDecorView().findViewById(android.R.id.content)).show();
    }
}
