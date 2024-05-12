package com.example.shoplet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;


public class ProductDetailActivity extends AppCompatActivity {

    ImageView ivCart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_detail);

        ivCart = findViewById(R.id.ivCart);
        ivCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ProductDetailActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });

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
            if (product.getImage() != null) {
                Picasso.get()
                        .load(product.getImage())
                        .into(imageViewProduct);
            }
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
            if (intent != null && intent.hasExtra("product")) {
                Product product = (Product) intent.getSerializableExtra("product");
                CartManager.getInstance().addToCart(product);
                Toast.makeText(ProductDetailActivity.this, "Added to cart", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
