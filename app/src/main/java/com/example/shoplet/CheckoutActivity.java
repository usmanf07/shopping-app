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

    private TextView usernameTextView;
    private TextView userAddressTextView;
    private TextView userPhoneTextView;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Orders");

        totalPriceTextView = findViewById(R.id.tvTotalPrice);
        usernameTextView = findViewById(R.id.username);
        userAddressTextView = findViewById(R.id.useraddress);
        userPhoneTextView = findViewById(R.id.userphone);

        imageViewCheckGif = findViewById(R.id.imageViewCheckGif);

        // Retrieve the current user details
        user = CurrentUser.getInstance().getUser();

        if (user != null) {
            // Set user details in the TextViews
            usernameTextView.setText(user.getName());
            userAddressTextView.setText(user.getAddress());
            userPhoneTextView.setText(String.valueOf(user.getPhone()));
        }

        handleIntent();
        setupPlaceOrderButton();
    }

    private void handleIntent() {
        Intent intent = getIntent();
        double totalPrice = intent.getDoubleExtra("totalPrice", 0);
        totalPriceTextView.setText(String.format("Total: Rs. %,.0f", totalPrice));

        TextView textViewItemsList = findViewById(R.id.textViewItemsList);
        ArrayList<String> productDetails = intent.getStringArrayListExtra("productDetails");
        if (productDetails != null) {
            StringBuilder itemsText = new StringBuilder();
            for (String detail : productDetails) {
                String[] parts = detail.split(":");
                if (parts.length == 3) {
                    int quantity = Integer.parseInt(parts[1]);
                    String productName = parts[2];
                    itemsText.append(productName).append("    x ").append(quantity).append(")\n");
                }
            }
            textViewItemsList.setText(itemsText.toString());
        }
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
        if (user == null) {
            Toast.makeText(this, "User not found. Please log in again.", Toast.LENGTH_SHORT).show();
            return;
        }
        double totalPrice = getIntent().getDoubleExtra("totalPrice", 0);
        ArrayList<String> productDetails = getIntent().getStringArrayListExtra("productDetails");

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());

        String currentDate = dateFormat.format(calendar.getTime());
        String currentTime = timeFormat.format(calendar.getTime());

        String userId = user.getId();
        DatabaseReference orderRef = databaseReference.push();

        HashMap<String, Object> order = new HashMap<>();
        order.put("id", orderRef.getKey());
        order.put("total", totalPrice);
        order.put("paymentMethod", paymentMethod);
        order.put("userId", userId);
        order.put("date", currentDate);
        order.put("time", currentTime);
        order.put("status", "In Progress");
        HashMap<String, Object> products = new HashMap<>();
        StringBuilder emailContent = new StringBuilder();
        emailContent.append("Order Details:\n\n");
        emailContent.append("Order ID: ").append(orderRef.getKey()).append("\n");
        emailContent.append("Name: ").append(user.getName()).append("\n");
        emailContent.append("Address: ").append(user.getAddress()).append("\n");
        emailContent.append("Phone: ").append(user.getPhone()).append("\n");
        emailContent.append("Email: ").append(user.getEmail()).append("\n\n");
        emailContent.append("Products:\n");

        if (productDetails != null) {
            for (String detail : productDetails) {
                String[] parts = detail.split(":");
                String productId = parts[0];
                int quantity = Integer.parseInt(parts[1]);
                String productName = parts[2];

                HashMap<String, Object> product = new HashMap<>();
                product.put("id", productId);
                product.put("quantity", quantity);

                products.put(productId, product);
                emailContent.append(productName).append("    x ").append(quantity).append("\n");
            }
        }

        order.put("Products", products);

        orderRef.setValue(order)
                .addOnSuccessListener(aVoid -> {
                    // Handle success
                    Toast.makeText(CheckoutActivity.this, "Order placed successfully", Toast.LENGTH_SHORT).show();
                    loadAndAnimateGif();
                    CartManager.getInstance().clearCart();

                    // Send email to the user
                    emailContent.append("\nTotal: Rs. ").append(totalPrice).append("\n");
                    emailContent.append("Payment Method: ").append(paymentMethod).append("\n");
                    new EmailSender(user.getEmail(), "Thanks for Order!", emailContent.toString()).execute();

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
}
