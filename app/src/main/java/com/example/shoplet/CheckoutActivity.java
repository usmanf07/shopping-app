package com.example.shoplet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CheckoutActivity extends AppCompatActivity {

    private TextView totalPriceTextView;
    private FirebaseDatabase firebaseDatabase;
    private ImageView imageViewCheckGif;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Orders");

        totalPriceTextView = findViewById(R.id.tvTotalPrice);
        handleIntent();
        setupPlaceOrderButton();
        imageViewCheckGif = findViewById(R.id.imageViewCheckGif);
    }

    private void setupPlaceOrderButton() {
        Button placeOrderButton = findViewById(R.id.btnCheckout);
        RadioGroup paymentMethods = findViewById(R.id.radioGroupPaymentMethods);

        placeOrderButton.setOnClickListener(view -> {
            int selectedId = paymentMethods.getCheckedRadioButtonId();
            String paymentMethod = "";
            if (selectedId == R.id.radioCashOnDelivery) {
                paymentMethod = "Cash on Delivery";
                placeOrder(paymentMethod);
            } else if (selectedId == R.id.radioCreditCard) {
                paymentMethod = "Credit Card";
                // Implement Credit Card payment functionality
            }
        });
    }

    private void placeOrder(String paymentMethod) {
        double totalPrice = getIntent().getDoubleExtra("totalPrice", 0);
        ArrayList<String> productDetails = getIntent().getStringArrayListExtra("productDetails");

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());

        String currentDate = dateFormat.format(calendar.getTime());
        String currentTime = timeFormat.format(calendar.getTime());

        String userId = "u0001"; // Get this from your user management system
        DatabaseReference orderRef = databaseReference.push(); // Create a unique key for the order

        HashMap<String, Object> order = new HashMap<>();
        order.put("id", orderRef.getKey());
        order.put("total", totalPrice);
        order.put("paymentMethod", paymentMethod);
        order.put("userId", userId);
        order.put("date", currentDate);
        order.put("time", currentTime);
        order.put("status", "In Progress");
        HashMap<String, Object> products = new HashMap<>();
        if (productDetails != null) {
            for (String detail : productDetails) {
                String[] parts = detail.split(":");
                String productId = parts[0];
                int quantity = Integer.parseInt(parts[1]);

                HashMap<String, Object> product = new HashMap<>();
                product.put("id", productId);
                product.put("quantity", quantity);

                products.put(productId, product);
            }
        }

        order.put("Products", products);

        orderRef.setValue(order)
                .addOnSuccessListener(aVoid -> {
                    // Handle success
                    Toast.makeText(CheckoutActivity.this, "Order placed successfully", Toast.LENGTH_SHORT).show();
                    loadAndAnimateGif();
                    CartManager.getInstance().clearCart();
                })
                .addOnFailureListener(e -> {
                    // Handle failure
                    Toast.makeText(CheckoutActivity.this, "Failed to place order", Toast.LENGTH_SHORT).show();
                });
    }


    private void loadAndAnimateGif() {
        Glide.with(this)
                .load(R.drawable.orderplaced) // Replace with your GIF resource or URL
                .into(new DrawableImageViewTarget(imageViewCheckGif) {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        super.onResourceReady(resource, transition);
                        imageViewCheckGif.setVisibility(View.VISIBLE);
                        startAnimation();
                    }
                });
    }
    private void startAnimation() {
        Animation slideUpAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        imageViewCheckGif.startAnimation(slideUpAnimation);

        // Delay to navigate after the GIF animation completes
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(CheckoutActivity.this, MyOrdersActivity.class);
            startActivity(intent);
            finish();
        }, 3000); // Assuming the GIF and slide animation take about 3 seconds
    }

    private void handleIntent() {
        Intent intent = getIntent();
        double totalPrice = intent.getDoubleExtra("totalPrice", 0);

        totalPriceTextView.setText(String.format("Total: Rs. %,.0f", totalPrice));

        TextView textViewItemsList = findViewById(R.id.textViewItemsList);
        ArrayList<String> productDetails = getIntent().getStringArrayListExtra("productDetails");
        if (productDetails != null) {
            StringBuilder itemsText = new StringBuilder();
            for (String detail : productDetails) {
                String[] parts = detail.split(":");
                if (parts.length == 3) {;
                    int quantity = Integer.parseInt(parts[1]);
                    String productName = parts[2];
                    itemsText.append(productName).append("    x ").append(quantity).append(")\n");
                }
            }
            textViewItemsList.setText(itemsText.toString());
        }
    }
}