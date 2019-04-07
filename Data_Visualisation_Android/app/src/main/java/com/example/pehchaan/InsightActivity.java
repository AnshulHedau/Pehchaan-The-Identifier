package com.example.pehchaan;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class InsightActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insight);
        ((AnimationDrawable) findViewById(R.id.sensorBtn).getBackground()).start();

        final TextView textView_m1 = findViewById(R.id.tv_m1);
        final TextView textView_m2 = findViewById(R.id.tv_m2);
        final TextView textView_m3 = findViewById(R.id.tv_m3);
        final TextView textView_m4 = findViewById(R.id.tv_m4);

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

                temp_int_value = (int) (Double.parseDouble(dataSnapshot.child("confidence_values").child("material_4").getValue().toString()) * 1000);
                temp_double_value = (float) temp_int_value / 10;
                textView_m4.setText(temp_double_value + "%");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
