package com.example.shoplet;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private ImageView ivCart;
    private ImageView ivProfile;
    private RecyclerView categoryRecyclerView;
    private RecyclerView flashSaleRecyclerView;

    private CategoryAdapter categoryAdapter;
    private FlashSaleAdapter flashSaleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        categoryRecyclerView = findViewById(R.id.categoryRecyclerView);
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        flashSaleRecyclerView = findViewById(R.id.flashSaleRecyclerView);
        flashSaleRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        List<CategoryItem> categoryItems = new ArrayList<>();
        categoryItems.add(new CategoryItem("c0001", "Phones"));
        categoryItems.add(new CategoryItem("c0002", "Pets"));
        categoryItems.add(new CategoryItem("c0003", "Laptops"));
        categoryItems.add(new CategoryItem("c0004", "Books"));
        categoryItems.add(new CategoryItem("c0005", "Home Appliances"));
        categoryItems.add(new CategoryItem("c0006", "Fitness"));
        categoryItems.add(new CategoryItem("c0007", "Fashion"));
        categoryItems.add(new CategoryItem("c0008", "Beauty Products"));
        categoryItems.add(new CategoryItem("c0009", "Electronics"));
        categoryItems.add(new CategoryItem("c0010", "Furniture"));

        List<FlashSaleItem> flashSaleItems = new ArrayList<>();
        flashSaleItems.add(new FlashSaleItem(R.drawable.phone, "Iphone 15 pro - 256 GB", "4.1/5", "Rs 340,000"));
        flashSaleItems.add(new FlashSaleItem(R.drawable.phone, "Samsung Galaxy S22 Ultra - 512 GB", "4.5/5", "Rs 300,000"));
        flashSaleItems.add(new FlashSaleItem(R.drawable.phone, "Google Pixel 7 Pro - 128 GB", "4.3/5", "Rs 250,000"));
        flashSaleItems.add(new FlashSaleItem(R.drawable.phone, "OnePlus 10 - 256 GB", "4.2/5", "Rs 280,000"));

        categoryAdapter = new CategoryAdapter(this, categoryItems);
        categoryRecyclerView.setAdapter(categoryAdapter);

        flashSaleAdapter = new FlashSaleAdapter(this, flashSaleItems);
        flashSaleRecyclerView.setAdapter(flashSaleAdapter);

        ivCart = findViewById(R.id.ivCart);
        ivCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });

        ivProfile = findViewById(R.id.ivProfile);
        ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, MyOrdersActivity.class);
                startActivity(intent);
            }
        });
    }

    private void openProductActivity() {
        Intent intent = new Intent(this, ProductActivity.class);
        startActivity(intent);
    }
}
