package com.ultimasquare.pinview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PinProtectedActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_protected_activity);

        Button enterPin = (Button) findViewById(R.id.buttonEnterPin);
        enterPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PinProtectedActivity.this, PinEntryViewActivity.class));
            }
        });
    }
}
