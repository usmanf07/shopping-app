package com.example.shoplet;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import androidx.annotation.NonNull;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import androidx.recyclerview.widget.GridLayoutManager;


public class HomeActivity extends AppCompatActivity {
    private ImageView ivCart;
    private ImageView ivProfile;
    private RecyclerView categoryRecyclerView;
    private CategoryAdapter categoryAdapter;
    private List<CategoryItem> categoryItems;
    private TextView viewAllProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

//        viewAllProducts = findViewById(R.id.viewAllProducts);
//        viewAllProducts.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(HomeActivity.this, ProductActivity.class);
//                startActivity(intent);
//            }
//        });

        categoryRecyclerView = findViewById(R.id.categoryRecyclerView);
        categoryRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        categoryItems = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(this, categoryItems);
        categoryRecyclerView.setAdapter(categoryAdapter);

        fetchCategoriesFromFirebase();

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

    private void fetchCategoriesFromFirebase() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Categories");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                categoryItems.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    CategoryItem categoryItem = dataSnapshot.getValue(CategoryItem.class);
                    categoryItems.add(categoryItem);
                }
                categoryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle possible errors.
            }
        });
    }
}
