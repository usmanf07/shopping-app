package com.example.shoplet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private List<Product> productList;
    private List<Product> filteredProductList; // List to hold filtered products
    private DatabaseReference productsRef;
    private ProgressBar progressBar;
    private ImageView ivCart;
    private EditText editTextSearch;
    private Button btnSort, btnOnSale, btnFreeDel;
    private boolean isPriceAscending = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        ivCart = findViewById(R.id.ivCart);
        ivCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });

        editTextSearch = findViewById(R.id.editTextSearch);
        btnSort = findViewById(R.id.btnSort);
        btnOnSale = findViewById(R.id.btnOnSale);
        btnFreeDel = findViewById(R.id.btnFreeDel);
        recyclerView = findViewById(R.id.recyclerViewProducts);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        progressBar = findViewById(R.id.progressBar);

        productList = new ArrayList<>();
        filteredProductList = new ArrayList<>();
        productAdapter = new ProductAdapter(filteredProductList, this, new ProductAdapter.OnProductClickListener() {
            @Override
            public void onProductClick(int position) {
                Product clickedProduct = filteredProductList.get(position);
                Intent intent = new Intent(ProductActivity.this, ProductDetailActivity.class);
                intent.putExtra("product", clickedProduct);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(productAdapter);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        productsRef = database.getReference("Products");

        progressBar.setVisibility(View.VISIBLE);

        productsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productList.clear();
                for (DataSnapshot productSnapshot : dataSnapshot.getChildren()) {
                    // Log.d("FirebaseData", "Product Snapshot: " + productSnapshot.getValue());
                    Product product = new Product();
                    product.setId(productSnapshot.child("id").getValue(String.class));
                    product.setDescription(productSnapshot.child("description").getValue(String.class));
                    product.setFreedelivery(productSnapshot.child("freedelivery").getValue(Boolean.class));
                    product.setFullname(productSnapshot.child("fullname").getValue(String.class));
                    product.setName(productSnapshot.child("name").getValue(String.class));
                    product.setOnsale(productSnapshot.child("onsale").getValue(Boolean.class));
                    product.setPrice(productSnapshot.child("price").getValue(Integer.class));
                    product.setImage(productSnapshot.child("image").getValue(String.class));
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

                filteredProductList.clear();
                filteredProductList.addAll(productList);
                productAdapter.notifyDataSetChanged();
                // Hide progress bar when data is loaded
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("FirebaseData", "Failed to retrieve products: " + databaseError.getMessage(), databaseError.toException());
                Toast.makeText(ProductActivity.this, "Failed to retrieve products: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                // Hide progress bar on error
                progressBar.setVisibility(View.GONE);
            }
        });

        // Set up search functionality
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // Set up button click listeners
        btnSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortProductsByPrice();
            }
        });

        btnOnSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterOnSaleProducts();
            }
        });

        btnFreeDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterFreeDeliveryProducts();
            }
        });
    }

    private void filter(String text) {
        filteredProductList.clear();
        if (text.isEmpty()) {
            filteredProductList.addAll(productList);
        } else {
            for (Product product : productList) {
                if (product.getName().toLowerCase().contains(text.toLowerCase())) {
                    filteredProductList.add(product);
                }
            }
        }
        productAdapter.notifyDataSetChanged();
    }

    private void sortProductsByPrice() {
        if (isPriceAscending) {
            Collections.sort(filteredProductList, new Comparator<Product>() {
                @Override
                public int compare(Product p1, Product p2) {
                    return Integer.compare(p1.getPrice(), p2.getPrice());
                }
            });
        } else {
            Collections.sort(filteredProductList, new Comparator<Product>() {
                @Override
                public int compare(Product p1, Product p2) {
                    return Integer.compare(p2.getPrice(), p1.getPrice());
                }
            });
        }
        isPriceAscending = !isPriceAscending;
        productAdapter.notifyDataSetChanged();
    }

    private void filterOnSaleProducts() {
        filteredProductList.clear();
        for (Product product : productList) {
            if (product.isOnsale()) {
                filteredProductList.add(product);
            }
        }
        productAdapter.notifyDataSetChanged();
    }

    private void filterFreeDeliveryProducts() {
        filteredProductList.clear();
        for (Product product : productList) {
            if (product.isFreedelivery()) {
                filteredProductList.add(product);
            }
        }
        productAdapter.notifyDataSetChanged();
    }
}
