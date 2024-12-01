package com.example.fruit_shop.User;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.fruit_shop.Adapter.RecentBuyAdapter;
import com.example.fruit_shop.Model.CartItem;
import com.example.fruit_shop.Model.OrderDetail;
import com.example.fruit_shop.R;
import com.example.fruit_shop.databinding.ActivityRecentOrderItemsBinding;

import java.util.ArrayList;

public class RecentOrderItemsActivity extends AppCompatActivity {

    private ActivityRecentOrderItemsBinding binding;
    private OrderDetail recentOrderItems;
    private ArrayList<String> productNames,productImages;
    private ArrayList<Double> productPrices;
    private ArrayList<Integer> productQuantities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRecentOrderItemsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setContentView(R.layout.activity_recent_order_items);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recentOrderItems = (OrderDetail) getIntent().getSerializableExtra("RecentOrderItem");

        if (recentOrderItems != null) {
            productNames = new ArrayList<>();
            productPrices = new ArrayList<>();
            productQuantities = new ArrayList<>();
            productImages = new ArrayList<>();

            for (CartItem cartItem : recentOrderItems.getOrderItems()) {

                productNames.add(cartItem.getProductName());
                //productPrices.add(FormatValues.formatMoney(cartItem.getPrice()));
                productQuantities.add(cartItem.getStockQuantity());
                productImages.add(cartItem.getImageUrl());
            }
        }

        setAdapter();

        binding.backButton.setOnClickListener(v -> {
            finish();
        });
    }

    private void setAdapter() {
        RecentBuyAdapter recentBuyAdapter = new RecentBuyAdapter(productNames, productPrices, productImages, productQuantities, this);
        binding.recentBuyRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recentBuyRecyclerView.setAdapter(recentBuyAdapter);
    }
}