package com.example.shoplet;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ResponseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_response);

        TextView responseText = findViewById(R.id.responseText);

        if (getIntent() != null) {
            //azzCashResponse jazzCashResponse = (JazzCashResponse) getIntent().getSerializableExtra("jazzCashResponse");
            //responseText.setText(jazzCashResponse.getPpResponseMessage());
        }
    }
}
