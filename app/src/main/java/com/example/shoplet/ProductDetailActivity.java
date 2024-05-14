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

import java.util.ArrayList;

public class ProductDetailActivity extends AppCompatActivity {

    private ImageView ivCart;
    private Product product;

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
            product = (Product) intent.getSerializableExtra("product");

            // Set product details to views
            if (product.getImage() != null) {
                Picasso.get()
                        .load(product.getImage())
                        .into(imageViewProduct);
            }

            productName.setText(product.getName());
            productRating.setText(String.valueOf(product.getTotalrating() + "/5.0"));
            productPrice.setText(String.format("Rs. %,.0f", (double)product.getPrice()));
        }

        // Button click listeners
        buttonBuyNow.setOnClickListener(view -> {
            if (product != null) {
                buyNow(product);
            }
        });

        buttonAddToCart.setOnClickListener(view -> {
            if (product != null) {
                CartManager.getInstance().addToCart(product);
                Toast.makeText(ProductDetailActivity.this, "Added to cart", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void buyNow(Product product) {
        Intent intent = new Intent(ProductDetailActivity.this, CheckoutActivity.class);
        ArrayList<String> productDetails = new ArrayList<>();
        String detail = product.getId() + ":1:" + product.getName();
        productDetails.add(detail);

        intent.putExtra("totalPrice", (double) product.getPrice());
        intent.putStringArrayListExtra("productDetails", productDetails);
        startActivity(intent);
    }
}
