package com.example.pehchaan;

import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.puder.highlight.HighlightManager;

public class InsightActivity extends FragmentActivity {

    private boolean help_displayed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insight);
        ((AnimationDrawable) findViewById(R.id.sensorBtn).getBackground()).start();

        final TextView textView_m1 = findViewById(R.id.tv_m1);
        final TextView textView_m2 = findViewById(R.id.tv_m2);
        final TextView textView_m3 = findViewById(R.id.tv_m3);

        TextView help_btn = findViewById(R.id.help_btn);
        final LinearLayout help_view = findViewById(R.id.help_values);

        HighlightManager highlightManager = new HighlightManager(this);
        highlightManager.reshowAllHighlights();

        highlightManager.addView(R.id.tv_m2).setTitle(R.string.title_1)
                .setDescriptionId(R.string.highlight_1);
        highlightManager.addView(R.id.tv_m1).setTitle(R.string.title_2)
                .setDescriptionId(R.string.highlight_2);
        highlightManager.addView(R.id.tv_m3).setTitle(R.string.title_3)
                .setDescriptionId(R.string.highlight_3);
        highlightManager.addView(R.id.help_btn).setTitle(R.string.title_4)
                .setDescriptionId(R.string.highlight_4);

        help_btn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                if (help_displayed) {
                    help_view.setVisibility(View.INVISIBLE);
                    help_displayed = !help_displayed;
                    findViewById(R.id.sensorBtn).setBackgroundResource(R.drawable.animation_gradient);
                    View img = (View)findViewById(R.id.sensorBtn);
                    AnimationDrawable frameAnimation = (AnimationDrawable) img.getBackground();
                    frameAnimation.start();
                } else {
                    help_view.setVisibility(View.VISIBLE);
                    help_displayed = !help_displayed;
                    findViewById(R.id.sensorBtn).setBackgroundResource(R.drawable.animation_gradient_red);
                    View img = (View) findViewById(R.id.sensorBtn);
                    AnimationDrawable frameAnimation = (AnimationDrawable) img.getBackground();
                    frameAnimation.start();
                }
            }
        });

        DatabaseReference database_rf = FirebaseDatabase.getInstance().getReference();
        database_rf.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int temp_int_value;
                float temp_double_value;

                temp_int_value = (int) (Double.parseDouble(dataSnapshot.child("confidence_values").child("material_1").getValue().toString()) * 1000);
                temp_double_value = (float) temp_int_value / 10;
                textView_m1.setText(temp_double_value + "%");

                temp_int_value = (int) (Double.parseDouble(dataSnapshot.child("confidence_values").child("material_2").getValue().toString()) * 1000);
                temp_double_value = (float) temp_int_value / 10;
                textView_m2.setText(temp_double_value + "%");

                temp_int_value = (int) (Double.parseDouble(dataSnapshot.child("confidence_values").child("material_3").getValue().toString()) * 1000);
                temp_double_value = (float) temp_int_value / 10;
                textView_m3.setText(temp_double_value + "%");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
