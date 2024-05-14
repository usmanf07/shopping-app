package com.example.shoplet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowMetrics;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import androidx.recyclerview.widget.GridLayoutManager;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

public class HomeActivity extends AppCompatActivity implements CategoryAdapter.OnCategoryClickListener {

    private static final String AD_UNIT_ID = "ca-app-pub-3940256099942544/1033173712"; // Your interstitial ad unit ID
    private ImageView ivCart;
    private ImageView ivProfile;
    private RecyclerView categoryRecyclerView;
    private CategoryAdapter categoryAdapter;
    private List<CategoryItem> categoryItems;
    private InterstitialAd interstitialAd;

    private AdView adView;
    private FrameLayout adContainerView;
    private final AtomicBoolean initialLayoutComplete = new AtomicBoolean(false);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        adContainerView = findViewById(R.id.ad_view_container);

        MobileAds.initialize(this, initializationStatus -> {});

        adContainerView
                .getViewTreeObserver()
                .addOnGlobalLayoutListener(
                        () -> {
                            if (!initialLayoutComplete.getAndSet(true)) {
                                loadBanner();
                            }
                        });

        categoryRecyclerView = findViewById(R.id.categoryRecyclerView);
        categoryRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        categoryItems = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(this, categoryItems, this);
        categoryRecyclerView.setAdapter(categoryAdapter);

        fetchCategoriesFromFirebase();

        ivCart = findViewById(R.id.ivCart);
        ivCart.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, CartActivity.class);
            startActivity(intent);
        });

        ivProfile = findViewById(R.id.ivProfile);
        ivProfile.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, MyOrdersActivity.class);
            startActivity(intent);
        });

        loadInterstitialAd();
    }

    private void loadInterstitialAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(this, AD_UNIT_ID, adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                HomeActivity.this.interstitialAd = interstitialAd;
                HomeActivity.this.interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        HomeActivity.this.interstitialAd = null;
                        loadInterstitialAd();
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                        HomeActivity.this.interstitialAd = null;
                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                        // Called when ad is shown.
                    }
                });
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                HomeActivity.this.interstitialAd = null;
            }
        });
    }

    @Override
    public void onCategoryClick(CategoryItem categoryItem) {
        if (interstitialAd != null) {
            interstitialAd.show(HomeActivity.this);
        } else {
            proceedToProductActivity(categoryItem);
        }
    }

    private void proceedToProductActivity(CategoryItem categoryItem) {
        Intent intent = new Intent(HomeActivity.this, ProductActivity.class);
        intent.putExtra("categoryID", categoryItem.getId());
        startActivity(intent);
    }

    private void loadBanner() {
        adView = new AdView(this);
        adView.setAdUnitId("ca-app-pub-3940256099942544/6300978111"); // Your banner ad unit ID
        adView.setAdSize(getAdSize());

        adContainerView.removeAllViews();
        adContainerView.addView(adView);

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    public AdSize getAdSize() {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int adWidthPixels = displayMetrics.widthPixels;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowMetrics windowMetrics = this.getWindowManager().getCurrentWindowMetrics();
            adWidthPixels = windowMetrics.getBounds().width();
        }

        float density = displayMetrics.density;
        int adWidth = (int) (adWidthPixels / density);
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth);
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
