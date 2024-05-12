package com.example.shoplet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class ProductActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private List<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        recyclerView = findViewById(R.id.recyclerViewProducts);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));


        productList = new ArrayList<>();
        productList.add(new Product("Iphone 15 pro", 4.1, "Rs 340,000", R.drawable.phone));
        productList.add(new Product("Samsung Galaxy S22", 4.3, "Rs 300,000", R.drawable.phone));
        productList.add(new Product("Google Pixel 7", 4.5, "Rs 280,000", R.drawable.phone));
        productList.add(new Product("OnePlus 10", 4.0, "Rs 320,000", R.drawable.phone));
        productList.add(new Product("Xiaomi Mi 12", 4.2, "Rs 260,000", R.drawable.phone));
        productList.add(new Product("Huawei P50", 4.4, "Rs 290,000", R.drawable.phone));


        productAdapter = new ProductAdapter(productList, this, new ProductAdapter.OnProductClickListener() {
            @Override
            public void onProductClick(int position) {
                Product clickedProduct = productList.get(position);
                Intent intent = new Intent(ProductActivity.this, ProductDetailActivity.class);
                intent.putExtra("product", clickedProduct);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(productAdapter);
    }

}

