package com.example.shoplet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.toolbox.JsonObjectRequest;
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
import java.util.Map;

import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class CheckoutActivity extends AppCompatActivity {

    private TextView totalPriceTextView;
    private FirebaseDatabase firebaseDatabase;
    private ImageView imageViewCheckGif;
    private DatabaseReference databaseReference;

    private TextView usernameTextView;
    private TextView userAddressTextView;
    private TextView userPhoneTextView;

    private User user;
    private double totalPrice;
    private ArrayList<String> productDetails;

    private PaymentSheet paymentSheet;
    private PaymentSheet.Configuration paymentSheetConfiguration;
    private String paymentIntentClientSecret;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        PaymentConfiguration.init(
                getApplicationContext(),
                "pk_test_51PGdR6P1pmJbill8ushP5HyD7RLhVAMTzla7WCXYjPJoqVyUSgQGOpbpi8ck3ljJvF6NrR1oZYzVnEnjxxIclq1O0021JW6BZQ"
        );

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Orders");

        totalPriceTextView = findViewById(R.id.tvTotalPrice);
        usernameTextView = findViewById(R.id.username);
        userAddressTextView = findViewById(R.id.useraddress);
        userPhoneTextView = findViewById(R.id.userphone);

        imageViewCheckGif = findViewById(R.id.imageViewCheckGif);

        user = CurrentUser.getInstance().getUser();

        if (user != null) {

            usernameTextView.setText(user.getName());
            userAddressTextView.setText(user.getAddress());
            userPhoneTextView.setText(String.valueOf(user.getPhone()));
        }

        paymentSheet = new PaymentSheet(this, this::onPaymentSheetResult);

        handleIntent();
        setupPlaceOrderButton();
    }

    private void handleIntent() {
        Intent intent = getIntent();
        totalPrice = intent.getDoubleExtra("totalPrice", 0);
        totalPriceTextView.setText(String.format("Total: Rs. %,.0f", totalPrice));

        TextView textViewItemsList = findViewById(R.id.textViewItemsList);
        productDetails = intent.getStringArrayListExtra("productDetails");
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
                startStripePayment();
            }
            else if (selectedId == R.id.radioJazzCash) {
                paymentMethod = "Jazz Cash";
                makeJazzCashPayment();
            }
        });
    }

    private void makeJazzCashPayment() {
        double totalPrice = getIntent().getDoubleExtra("totalPrice", 0);
        Intent intent = new Intent(CheckoutActivity.this, PaymentActivity.class);
        intent.putExtra("price", String.valueOf(totalPrice));
        startActivityForResult(intent, 0);
    }

    private void startStripePayment() {
        fetchPaymentIntent();
    }

    private void fetchPaymentIntent() {
        double amount = totalPrice * 100;
        String url = "https://api.stripe.com/v1/payment_intents";

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null,
                response -> {
                    try {
                        paymentIntentClientSecret = response.getString("client_secret");

                        paymentSheetConfiguration = new PaymentSheet.Configuration(
                                "Shoplet, Inc."
                        );

                        paymentSheet.presentWithPaymentIntent(
                                paymentIntentClientSecret,
                                paymentSheetConfiguration
                        );
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Failed to parse payment intent response", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Toast.makeText(this, "Failed to start payment: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer sk_test_51PGdR6P1pmJbill89Z72Oayc2QlyW4wLddiud2t9ph5QPl3A5aktYUTfQPGWbuQa8G46eh5BEhTz0lAY56Gb0Iv700YaSRpyMW");
                return headers;
            }

            @Override
            public byte[] getBody() {
                String requestBody = "amount=" + (int) amount + "&currency=pkr";
                return requestBody.getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded";
            }
        };

        queue.add(jsonObjectRequest);
    }

    private void onPaymentSheetResult(PaymentSheetResult paymentSheetResult) {
        if (paymentSheetResult instanceof PaymentSheetResult.Completed) {
            Toast.makeText(this, "Payment Successful", Toast.LENGTH_SHORT).show();
            placeOrder("Credit Card");
        } else if (paymentSheetResult instanceof PaymentSheetResult.Failed) {
            Toast.makeText(this, "Payment Failed", Toast.LENGTH_SHORT).show();
        } else if (paymentSheetResult instanceof PaymentSheetResult.Canceled) {
            Toast.makeText(this, "Payment Canceled", Toast.LENGTH_SHORT).show();
        }
    }

    private void placeOrder(String paymentMethod) {
        if (user == null) {
            Toast.makeText(this, "User not found. Please log in again.", Toast.LENGTH_SHORT).show();
            return;
        }

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

                    Toast.makeText(CheckoutActivity.this, "Order placed successfully", Toast.LENGTH_SHORT).show();
                    loadAndAnimateGif();
                    CartManager.getInstance().clearCart();

                    emailContent.append("\nTotal: Rs. ").append(totalPrice).append("\n");
                    emailContent.append("Payment Method: ").append(paymentMethod).append("\n");
                    new EmailSender(user.getEmail(), "Thanks for Order!", emailContent.toString()).execute();

                })
                .addOnFailureListener(e -> {

                    Toast.makeText(CheckoutActivity.this, "Failed to place order", Toast.LENGTH_SHORT).show();
                });
    }

    private void loadAndAnimateGif() {
        Glide.with(this)
                .load(R.drawable.orderplaced)
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

        new Handler().postDelayed(() -> {
            Intent intent = new Intent(CheckoutActivity.this, MyOrdersActivity.class);
            startActivity(intent);
            finish();
        }, 3000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {
            String responseCode = data.getStringExtra("pp_ResponseCode");
            if (responseCode.equals("000")) {
                Toast.makeText(getApplicationContext(), "Payment Success", Toast.LENGTH_SHORT).show();
                placeOrder("JazzCash");
            } else {
                Toast.makeText(getApplicationContext(), "Payment Failed", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
