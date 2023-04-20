package com.millivalley.qiblaapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.rpc.Code;
import com.millivalley.qiblaapp.R;

import java.util.Locale;

public class LanguageActivity extends AppCompatActivity {
    private ImageView back_arrow;
    private ConstraintLayout arabic_con, turkish_con, english_con, chinese_con, spanish_con, french_con;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);

        connections();
        clicks();
    }

    private void clicks() {
        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LanguageActivity.this, MainActivity.class));
                finish();
            }
        });

        arabic_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLanguage("ar");
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });

        turkish_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLanguage("tr");
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });

        english_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLanguage("en");
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });

        chinese_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLanguage("zh");
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });

        spanish_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLanguage("es");
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });

        french_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLanguage("fr");
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });

    }

    private void connections() {
        back_arrow = findViewById(R.id.back_arrow);
        arabic_con = findViewById(R.id.arabic_con);
        turkish_con = findViewById(R.id.turkish_con);
        english_con = findViewById(R.id.english_con);
        chinese_con = findViewById(R.id.chinese_con);
        spanish_con = findViewById(R.id.spanish_con);
        french_con = findViewById(R.id.french_con);
    }

    public void setLanguage(String languageCode) {
        Log.d("wow123", "setLanguage: " + languageCode);
        Resources resources = this.getResources();
        Configuration configuration = resources.getConfiguration();
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        configuration.setLocale(locale);
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(LanguageActivity.this, MainActivity.class));
        finish();
    }
}