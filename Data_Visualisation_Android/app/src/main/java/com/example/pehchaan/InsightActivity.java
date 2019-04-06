package com.example.pehchaan;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;

public class InsightActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insight);
        ((AnimationDrawable)findViewById(R.id.sensorBtn).getBackground()).start();
    }
}
