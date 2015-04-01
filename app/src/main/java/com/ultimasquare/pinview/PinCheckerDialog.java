package com.ultimasquare.pinview;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class PinCheckerDialog extends Dialog {

    @InjectView(R.id.container) View container;
    @InjectView(R.id.pinEntryView) PinCheckerView pinCheckerView;

    private View blurBackground;

    public PinCheckerDialog(Context context, View background) {
        super(context, android.R.style.Theme_Holo_Light_NoActionBar);
        blurBackground = background;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_entry_view);
        ButterKnife.inject(this, this);

        container.setBackgroundDrawable(new BlurDrawable(getContext(), blurBackground));

        pinCheckerView.init("8888", new PinCheckerView.Listener() {
            @Override
            public void onFinish() {
                dismiss();
            }
        });
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }
}
