package com.example.shoplet;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Button openProductsButton = findViewById(R.id.openProducts);
        openProductsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openProductActivity();
            }
        });
    }

    private void openProductActivity() {
        Intent intent = new Intent(this, ProductActivity.class);
        startActivity(intent);
    }
}
