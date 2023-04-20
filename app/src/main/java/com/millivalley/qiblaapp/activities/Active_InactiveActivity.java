package com.millivalley.qiblaapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;
import com.millivalley.qiblaapp.R;

public class Active_InactiveActivity extends AppCompatActivity {
    private ImageView back_arrow;
    private MaterialCardView circle_cardview;
    private TextView inactive_text;
    private int a = 0;
    long[] p = {1000};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_inactive);

        connections();
        clicks();

    }

    private void clicks() {
        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        circle_cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//vibration
                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
//                Vibrator mVibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);

// Vibrate for 500 milliseconds
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                    v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
//                    Log.d("wow123", "onClick: O");
                    v.vibrate(VibrationEffect.createWaveform(p, 1),
                            new AudioAttributes.Builder()
                                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                                    .setUsage(AudioAttributes.USAGE_ALARM)
                                    .build());
//                    long[] pattern = {100, 1000, 100, 1000};
//                    if (v != null) {
//                        AudioAttributes audioAttributes = new AudioAttributes.Builder()
//                                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
//                                .setUsage(AudioAttributes.USAGE_ALARM) //key
//                                .build();
//                        v.vibrate(pattern, 2, audioAttributes);
//                    }
                } else {
                    //deprecated in API 26
                    Log.d("wow123", "onClick: 26");
                    v.vibrate(500);
                }
//
                if(a == 0){
                    circle_cardview.setCardBackgroundColor(Color.rgb(0,200,0));
                    inactive_text.setText(R.string.active);
                    a = 1;
                }
                else{
                    circle_cardview.setCardBackgroundColor(Color.rgb(224,121,59));
                    inactive_text.setText(R.string.inactive);
                    a = 0;
                }

            }
        });
    }

    private void connections() {
        back_arrow = findViewById(R.id.back_arrow);
        circle_cardview = findViewById(R.id.circle_cardview);
        inactive_text = findViewById(R.id.inactive_text);

    }
}