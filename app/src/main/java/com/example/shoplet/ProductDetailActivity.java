package com.example.shoplet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import android.widget.LinearLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProductDetailActivity extends AppCompatActivity {

    private ImageView ivCart;
    private Product product;
    private DatabaseReference mDatabase;

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
        TextView productDiscountedPrice = findViewById(R.id.product_discounted_priceI);
        TextView productSpecifications = findViewById(R.id.product_specificationsI);
        Button buttonBuyNow = findViewById(R.id.button_buy_now);
        Button buttonAddToCart = findViewById(R.id.button_add_to_cart);
        LinearLayout reviewsLayout = findViewById(R.id.reviews_layout);

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

            productName.setText(product.getFullname());
            productRating.setText(String.valueOf(product.getTotalrating() + "/5.0"));

            if (product.isOnsale()) {
                int discountedPrice = (int) (product.getPrice() - (product.getPrice() * product.getDiscount() / 100.0));
                productPrice.setText(String.format("Rs. %,.0f", (double) product.getPrice()));
                productPrice.setPaintFlags(productPrice.getPaintFlags() | android.graphics.Paint.STRIKE_THRU_TEXT_FLAG);
                productDiscountedPrice.setText(String.format("Rs. %,.0f", (double) discountedPrice));
                productDiscountedPrice.setVisibility(View.VISIBLE);
            } else {
                productPrice.setText(String.format("Rs. %,.0f", (double) product.getPrice()));
                productDiscountedPrice.setVisibility(View.GONE);
            }

            // Set product specifications
            String[] specifications = product.getDescription().split(",");
            StringBuilder specsBuilder = new StringBuilder();
            for (String spec : specifications) {
                specsBuilder.append(spec.trim()).append("\n");
            }
            productSpecifications.setText(specsBuilder.toString().trim());

            // Load and display product reviews
            loadProductReviews(product.getId(), reviewsLayout);
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

    private void loadProductReviews(String productId, LinearLayout reviewsLayout) {
        DatabaseReference reviewsRef = FirebaseDatabase.getInstance().getReference("Products").child(productId).child("ratings");
        reviewsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                reviewsLayout.removeAllViews();
                for (DataSnapshot reviewSnapshot : snapshot.getChildren()) {
                    Rating review = reviewSnapshot.getValue(Rating.class);
                    String userId = reviewSnapshot.getKey();
                    if (review != null && userId != null) {
                        fetchReviewerName(userId, review, reviewsLayout);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProductDetailActivity.this, "Failed to load reviews", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchReviewerName(String userId, Rating review, LinearLayout reviewsLayout) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(userId);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String reviewerName = snapshot.child("name").getValue(String.class);
                if (reviewerName != null) {
                    View reviewView = getLayoutInflater().inflate(R.layout.review_item, reviewsLayout, false);
                    TextView reviewerNameTextView = reviewView.findViewById(R.id.reviewer_name);
                    TextView reviewText = reviewView.findViewById(R.id.review_text);

                    reviewerNameTextView.setText(reviewerName);
                    reviewText.setText(review.getReview());

                    reviewsLayout.addView(reviewView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProductDetailActivity.this, "Failed to load reviewer name", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void buyNow(Product product) {
        Intent intent = new Intent(ProductDetailActivity.this, CheckoutActivity.class);
        ArrayList<String> productDetails = new ArrayList<>();
        String detail = product.getId() + ":1:" + product.getName();
        productDetails.add(detail);

        double totalPrice = product.isOnsale() ?
                product.getPrice() - (product.getPrice() * product.getDiscount() / 100.0) :
                product.getPrice();

        intent.putExtra("totalPrice", totalPrice);
        intent.putStringArrayListExtra("productDetails", productDetails);
        startActivity(intent);
    }
}
