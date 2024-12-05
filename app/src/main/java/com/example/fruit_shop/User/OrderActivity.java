package com.example.fruit_shop.User;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.fruit_shop.Adapter.CartAdapter;
import com.example.fruit_shop.Model.CartItem;
import com.example.fruit_shop.R;
import com.example.fruit_shop.Utils.FormatValues;
import com.example.fruit_shop.databinding.ActivityOrderBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OrderActivity extends AppCompatActivity {
    private ActivityOrderBinding binding;
    private ArrayList<CartItem> cartItemList;
    private CartAdapter cartAdapter;

    // Firebase
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private String userID;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize Firebase
        auth = FirebaseAuth.getInstance();

        retrieveCartItems();

        // Move to PayOutActivity
        binding.btnBuy.setOnClickListener(v -> {
            getOrderItemsDetail();
        });

        //Xu ly su kien
        binding.imgBack.setOnClickListener(v -> finish());

        binding.Home.setOnClickListener(v -> startActivity(new Intent(OrderActivity.this, HomeActivity.class)));
        binding.Explore.setOnClickListener(v -> startActivity(new Intent(OrderActivity.this, ExploreActivity.class)));
        binding.Profile.setOnClickListener(v -> startActivity(new Intent(OrderActivity.this, ProfileActivity.class)));

    }

    private void getOrderItemsDetail() {
        DatabaseReference orderItemsRef = database.getReference().child("Users").child(userID).child("CartItems");
        cartItemList = new ArrayList<>();
        ArrayList<Integer> itemQuantities = cartAdapter.getUpdatedItemsQuantities();

        // Fetch data
        orderItemsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Get each item and add to cartItems
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        CartItem orderItem = dataSnapshot.getValue(CartItem.class);
                        cartItemList.add(orderItem);
                    }

                    joinQuantitiesToOrderItems(cartItemList, itemQuantities);

                    Intent intent = new Intent(OrderActivity.this, PayActivity.class);
                    intent.putExtra("cartItems", cartItemList);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void joinQuantitiesToOrderItems(ArrayList<CartItem> cartItems, ArrayList<Integer> itemQuantities) {
        for (int i = 0; i < cartItems.size(); i++) {
            CartItem cartItem = cartItems.get(i);
            cartItem.setStockQuantity(itemQuantities.get(i));
        }
    }

    private void retrieveCartItems() {
        database = FirebaseDatabase.getInstance();
        userID = auth.getCurrentUser().getUid();
        databaseReference = database.getReference().child("Users").child(userID).child("CartItems");
        cartItemList = new ArrayList<>();

        // Fetch data
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Get each item and add to cartItems
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    CartItem cartItem = dataSnapshot.getValue(CartItem.class);
                    cartItemList.add(cartItem);
                }

                showCartItems();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Load data", "onCancelled: Failed" + error.getMessage());
            }
        });
    }

    private void showCartItems() {
        cartAdapter = new CartAdapter(this, cartItemList);
        binding.recyclerViewOrder.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewOrder.setAdapter(cartAdapter);

        int totalAmount = getTotalAmount();
        binding.totalMoney.setText(FormatValues.formatMoney(totalAmount));
    }

    private int getTotalAmount() {
        int total = 0;
        for (CartItem item : cartItemList) {
            total += item.getPrice() * item.getStockQuantity();
        }
        return total;
    }


}