package com.example.shoplet;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Navbar extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navbar);

        ImageView ivCart = findViewById(R.id.ivCart);
        ImageView ivProfile = findViewById(R.id.ivProfile);


        ivCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Navbar.this, CartActivity.class);
                startActivity(intent);
            }
        });


        ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle profile click action
            }
        });
    }
}