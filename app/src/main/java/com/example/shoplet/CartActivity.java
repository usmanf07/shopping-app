package com.example.shoplet;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    private ListView cartListView;
    private TextView totalTextView;
    private Button checkoutButton;
    private CartItemAdapter adapter;
    private ArrayList<CartItem> itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart);

        cartListView = findViewById(R.id.cartListView);
        totalTextView = findViewById(R.id.totalTextView);
        checkoutButton = findViewById(R.id.checkoutButton);


        itemList = new ArrayList<>();

        itemList.add(new CartItem(R.drawable.phone, "Item 1", 10.99, 2));
        itemList.add(new CartItem(R.drawable.phone, "Item 2", 5.99, 1));
        itemList.add(new CartItem(R.drawable.phone, "Item 3", 7.49, 3));


        adapter = new CartItemAdapter(this, itemList);
        cartListView.setAdapter(adapter);


        double total = calculateTotal(itemList);
        totalTextView.setText("Total: $" + total);


        checkoutButton.setOnClickListener(view -> {
            // Handle checkout process
        });
    }

    private double calculateTotal(ArrayList<CartItem> itemList) {
        double total = 0;
        for (CartItem item : itemList) {
            total += item.getPrice() * item.getQuantity();
        }
        return total;
    }
}
