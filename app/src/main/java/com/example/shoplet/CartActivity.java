package com.example.shoplet;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity implements CartInteractionListener{
    private ListView cartListView;
    private TextView totalTextView, totalItemsTextView;
    private Button checkoutButton;
    private CartItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart);

        cartListView = findViewById(R.id.cartListView);
        totalTextView = findViewById(R.id.tvTotalPrice);
        checkoutButton = findViewById(R.id.btnCheckout);
        totalItemsTextView = findViewById(R.id.tvTotalItems);

        // Set up the adapter with the cart items from the CartManager
        adapter = new CartItemAdapter(this, CartManager.getInstance().getCartItems(), this);
        cartListView.setAdapter(adapter);

        // Initially update the total amount
        updateTotal();

        checkoutButton.setOnClickListener(view -> {
            Intent intent = new Intent(CartActivity.this, CheckoutActivity.class);
            double total = 0;
            ArrayList<String> productDetails = new ArrayList<>();
            for (CartItem item : CartManager.getInstance().getCartItems()) {
                total += item.getProduct().getPrice() * item.getQuantity();
                String detail = item.getProduct().getId() + ":" + item.getQuantity() + ":" + item.getProduct().getName(); // Use colon to separate ID and quantity
                productDetails.add(detail);
            }

            intent.putExtra("totalPrice", total);
            intent.putStringArrayListExtra("productDetails", productDetails);
            startActivity(intent);
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        updateTotal(); // Recalculate totals and update UI
    }

    private void updateTotal() {
        double total = 0;
        int totalItems = 0;
        for (CartItem item : CartManager.getInstance().getCartItems()) {
            total += item.getProduct().getPrice() * item.getQuantity();
            totalItems += item.getQuantity();
        }
        totalTextView.setText(String.format("Total: Rs. %,.0f", total));
        totalItemsTextView.setText("Total Items: " + totalItems);
        checkoutButton.setEnabled(totalItems > 0);
    }

    @Override
    public void onCartUpdated() {
        updateTotal();
    }
}
