package com.example.myfoundationapplication;

import android.content.Intent;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
    import android.os.Bundle;

public class CheckActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);

        Intent intent = getIntent();
        String resFsOver = intent.getStringExtra("fsOver");
        String resFsSlide = intent.getStringExtra("fsSlide");
        String resFsBreak = intent.getStringExtra("fsBreak");

        TextView overTV = findViewById(R.id.overTextView);
        TextView slideTV = findViewById(R.id.slideTextView);
        TextView breakTV = findViewById(R.id.breakTextView);

        overTV.setText(resFsOver);
        slideTV.setText(resFsSlide);
        breakTV.setText(resFsBreak);
    }
}
