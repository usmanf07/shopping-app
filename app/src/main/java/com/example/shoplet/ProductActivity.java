package com.example.shoplet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private List<Product> productList;
    private DatabaseReference productsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        recyclerView = findViewById(R.id.recyclerViewProducts);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(productList, this, new ProductAdapter.OnProductClickListener() {
            @Override
            public void onProductClick(int position) {
                Product clickedProduct = productList.get(position);
                Intent intent = new Intent(ProductActivity.this, ProductDetailActivity.class);
                intent.putExtra("product", (CharSequence) clickedProduct);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(productAdapter);

        // Initialize Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        productsRef = database.getReference("Products");

        // Retrieve data from Firebase
        productsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productList.clear();
                for (DataSnapshot productSnapshot : dataSnapshot.getChildren()) {
                    Product product = new Product();
                    product.setDescription(productSnapshot.child("description").getValue(String.class));
                    product.setFreedelivery(productSnapshot.child("freedelivery").getValue(Boolean.class));
                    product.setFullname(productSnapshot.child("fullname").getValue(String.class));
                    product.setName(productSnapshot.child("name").getValue(String.class));
                    product.setOnsale(productSnapshot.child("onsale").getValue(Boolean.class));
                    product.setPrice(productSnapshot.child("price").getValue(Integer.class));

                    Map<String, Rating> ratings = new HashMap<>();
                    for (DataSnapshot ratingSnapshot : productSnapshot.child("ratings").getChildren()) {
                        ratings.put(ratingSnapshot.getKey(), ratingSnapshot.getValue(Rating.class));
                    }
                    product.setRatings(ratings);
                    product.setSales(productSnapshot.child("sales").getValue(Integer.class));
                    product.setStock(productSnapshot.child("stock").getValue(Integer.class));
                    product.setCategoryID(productSnapshot.child("categoryID").getValue(String.class));
                    product.setTotalrating(productSnapshot.child("totalrating").getValue(Double.class));

                    productList.add(product);
                }
                productAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ProductActivity.this, "Failed to retrieve products: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
