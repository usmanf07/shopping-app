package com.example.shoplet;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;


public class ProductDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_detail);

        // Initialize views
        ImageView imageViewProduct = findViewById(R.id.imageViewProduct);
        TextView productName = findViewById(R.id.product_nameI);
        TextView productRating = findViewById(R.id.product_ratingI);
        TextView productPrice = findViewById(R.id.product_priceI);
        Button buttonBuyNow = findViewById(R.id.button_buy_now);
        Button buttonAddToCart = findViewById(R.id.button_add_to_cart);

        // Retrieve product data from intent
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("product")) {
            Product product = (Product) intent.getSerializableExtra("product");

            // Set product details to views
            // Assuming you don't have an image resource ID in your Product model
            // imageViewProduct.setImageResource(product.getImageResId());

            // Set other product details
            productName.setText(product.getName());
            productRating.setText(String.valueOf(product.getTotalrating())); // Assuming you want to display total rating
            productPrice.setText(String.valueOf(product.getPrice()));

            // You can set other product details here if needed
        }

        // Button click listeners
        buttonBuyNow.setOnClickListener(view -> {
            // Handle Buy Now button click
        });

        buttonAddToCart.setOnClickListener(view -> {
            // Handle Add to Cart button click
        });
    }
}
