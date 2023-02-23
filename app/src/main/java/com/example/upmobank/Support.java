package com.example.upmobank;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class Support extends AppCompatActivity {

    ImageView imageViewBackToMain3;
    TextView textView3, textView5;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);

        imageViewBackToMain3 = findViewById(R.id.imageViewBackToMain3);

        textView3 = findViewById(R.id.textView3);
        textView3.setTextIsSelectable(true);
        textView5 = findViewById(R.id.textView5);
        textView5.setTextIsSelectable(true);


        imageViewBackToMain3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}