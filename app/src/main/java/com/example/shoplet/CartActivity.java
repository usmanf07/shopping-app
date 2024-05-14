package com.example.shoplet;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity implements CartInteractionListener {
    private ListView cartListView;
    private TextView totalTextView, totalItemsTextView;
    private Button checkoutButton;
    private CartItemAdapter adapter;

    ImageView ivClear;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart);

        ivClear = findViewById(R.id.ivClear);
        cartListView = findViewById(R.id.cartListView);
        totalTextView = findViewById(R.id.tvTotalPrice);
        checkoutButton = findViewById(R.id.btnCheckout);
        totalItemsTextView = findViewById(R.id.tvTotalItems);

        // Set up the adapter with the cart items from the CartManager
        adapter = new CartItemAdapter(this, CartManager.getInstance().getCartItems(), this);
        cartListView.setAdapter(adapter);

        // Initially update the total amount
        updateTotal();


        ivClear.setOnClickListener(view -> {
            CartManager.getInstance().clearCart();
            adapter.notifyDataSetChanged();
            updateTotal();
        });
        checkoutButton.setOnClickListener(view -> {
            Intent intent = new Intent(CartActivity.this, CheckoutActivity.class);
            double total = 0;
            ArrayList<String> productDetails = new ArrayList<>();
            for (CartItem item : CartManager.getInstance().getCartItems()) {
                double price = item.getProduct().isOnsale() ?
                        item.getProduct().getPrice() - (item.getProduct().getPrice() * item.getProduct().getDiscount() / 100.0) :
                        item.getProduct().getPrice();
                total += price * item.getQuantity();
                String detail = item.getProduct().getId() + ":" + item.getQuantity() + ":" + item.getProduct().getName();
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
            double price = item.getProduct().isOnsale() ?
                    item.getProduct().getPrice() - (item.getProduct().getPrice() * item.getProduct().getDiscount() / 100.0) :
                    item.getProduct().getPrice();
            total += price * item.getQuantity();
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
