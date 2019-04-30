package com.example.pehchaan;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.puder.highlight.HighlightManager;

public class InsightActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insight);
        ((AnimationDrawable) findViewById(R.id.sensorBtn).getBackground()).start();

        final TextView textView_m1 = findViewById(R.id.tv_m1);
        final TextView textView_m2 = findViewById(R.id.tv_m2);
        final TextView textView_m3 = findViewById(R.id.tv_m3);

        TextView fetch_btn = findViewById(R.id.fetch_btn);

        //region Highlight code
        HighlightManager highlightManager = new HighlightManager(this);
        highlightManager.reshowAllHighlights();

        highlightManager.addView(R.id.tv_m2).setTitle(R.string.title_1)
                .setDescriptionId(R.string.highlight_1);
        highlightManager.addView(R.id.tv_m1).setTitle(R.string.title_2)
                .setDescriptionId(R.string.highlight_2);
        highlightManager.addView(R.id.tv_m3).setTitle(R.string.title_3)
                .setDescriptionId(R.string.highlight_3);
        highlightManager.addView(R.id.fetch_btn).setTitle(R.string.title_4)
                .setDescriptionId(R.string.highlight_4);
        //endregion

        final DatabaseReference database_rf = FirebaseDatabase.getInstance().getReference();
        final Handler handler = new Handler();

        //region Firebase data fetching code
        fetch_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.sensorBtn).setBackgroundResource(R.drawable.animation_gradient_red);

                View img = findViewById(R.id.sensorBtn);
                AnimationDrawable frameAnimation = (AnimationDrawable) img.getBackground();
                frameAnimation.start();

                handler.postDelayed(new Runnable() {
                    public void run() {
                        database_rf.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                int temp_int_value;
                                float temp_double_value;

                                temp_int_value = (int) (Double.parseDouble(dataSnapshot.child("confidence_values").child("material_1").getValue().toString()) * 10000);
                                temp_double_value = (float) temp_int_value / 100;
                                textView_m1.setText(Html.fromHtml("<b>" + temp_double_value + "%</b>" + "<br><small>Ceramics</small>"));

                                temp_int_value = (int) (Double.parseDouble(dataSnapshot.child("confidence_values").child("material_2").getValue().toString()) * 10000);
                                temp_double_value = (float) temp_int_value / 100;
                                textView_m2.setText(Html.fromHtml("<b>" + temp_double_value + "%</b>" + "<br><small>Plastic</small>"));

                                temp_int_value = (int) (Double.parseDouble(dataSnapshot.child("confidence_values").child("material_3").getValue().toString()) * 10000);
                                temp_double_value = (float) temp_int_value / 100;
                                textView_m3.setText(Html.fromHtml("<b>" + temp_double_value + "%</b>" + "<br><small>Wood</small>"));

                                findViewById(R.id.sensorBtn).setBackgroundResource(R.drawable.animation_gradient);

                                View img = findViewById(R.id.sensorBtn);
                                AnimationDrawable frameAnimation = (AnimationDrawable) img.getBackground();
                                frameAnimation.start();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }, 2500);
            }
        });
        //endregion
    }
}
