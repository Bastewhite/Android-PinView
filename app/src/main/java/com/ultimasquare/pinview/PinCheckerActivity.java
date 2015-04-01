package com.ultimasquare.pinview;

import android.app.Activity;
import android.os.Bundle;

public class PinCheckerActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_entry_view);

        PinCheckerView pinCheckerView = (PinCheckerView) findViewById(R.id.pinEntryView);
        pinCheckerView.init("8888", new PinCheckerView.Listener() {
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
