package com.example.shoplet;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {
    ImageView ivCart;
    ImageView ivProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ivCart = findViewById(R.id.ivCart);
        ivCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(HomeActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });

        ivProfile = findViewById(R.id.ivProfile);


        ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(HomeActivity.this, MyOrdersActivity.class);
                startActivity(intent);
            }
        });

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
