package com.example.fruit_shop.User;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.fruit_shop.Adapter.BuyAgainAdapter;
import com.example.fruit_shop.Model.OrderDetail;
import com.example.fruit_shop.Utils.FormatValues;
import com.example.fruit_shop.databinding.ActivityHistoryCartBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

public class HistoryCartActivity extends AppCompatActivity {
    private ActivityHistoryCartBinding binding;
    private ArrayList<OrderDetail> listOfOrderItem;

    private FirebaseDatabase database;
    private FirebaseAuth auth;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHistoryCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.imgBack.setOnClickListener(v -> {
            finish();
        });

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        userID = auth.getCurrentUser().getUid();
        listOfOrderItem = new ArrayList<>();

        retrieveBuyHistory();

        binding.recentBuyItem.setOnClickListener(v -> {
            seeItemsRecentBuyed();
        });

        // Rceived item
        binding.receivedButton.setOnClickListener(v -> {
            updateOrderStatus();
        });

    }

    private void retrieveBuyHistory() {
        binding.recentBuyItem.setVisibility(View.INVISIBLE);
        userID = auth.getCurrentUser().getUid();
        DatabaseReference buyItemRef = database.getReference().child("Users").child(userID).child("BuyHistory");
        listOfOrderItem = new ArrayList<>();

        // Sort by currentTime
        Query shortingQuery = buyItemRef.orderByChild("currentTime");

        shortingQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Get data from firebase and add to list
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    OrderDetail buyHistoryItem = dataSnapshot.getValue(OrderDetail.class);
                    listOfOrderItem.add(buyHistoryItem);
                }

                // Item at the end is the latest order so we need to reverse the list
                Collections.reverse(listOfOrderItem);

                if (!listOfOrderItem.isEmpty()) {
                    setDataInRecentBuyItem();
                    setPreviousBuyItemRecycler();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Load data", "onCancelled: Failed" + error.getMessage());
            }
        });
    }

    private void setDataInRecentBuyItem() {
        binding.recentBuyItem.setVisibility(View.VISIBLE);
        OrderDetail recentOrderItem = listOfOrderItem.get(0);

        binding.buyAgainProductName.setText(recentOrderItem.getOrderItems().get(0).getProductName());
        binding.buyAgainPrice.setText(FormatValues.formatMoney(recentOrderItem.getOrderItems().get(0).getPrice()));

        // Load image using Glide
        Glide.with(this).load(recentOrderItem.getOrderItems().get(0).getImageUrl()).into(binding.buyAgainImage);

        boolean isOrderIsAccepted = listOfOrderItem.get(0).getOrderAccepted();
        boolean isOrderIsReceived = listOfOrderItem.get(0).getPaymentReceived();
        if (isOrderIsAccepted && !isOrderIsReceived) {
            binding.orderStatus.setCardBackgroundColor(Color.GREEN);
            binding.receivedButton.setVisibility(View.VISIBLE);
        } else if (!isOrderIsAccepted) {
            binding.orderStatus.setCardBackgroundColor(Color.RED);
            binding.receivedButton.setVisibility(View.INVISIBLE);
        } else {
            binding.orderStatus.setCardBackgroundColor(Color.GREEN);
            binding.receivedButton.setVisibility(View.INVISIBLE);
        }
    }

    private void setPreviousBuyItemRecycler() {
        ArrayList<String> buyAgainNames = new ArrayList<>();
        ArrayList<Double> buyAgainPrices = new ArrayList<>();
        ArrayList<String> buyAgainImages = new ArrayList<>();

        for (int i = 1; i < listOfOrderItem.size(); i++) {
            buyAgainNames.add(listOfOrderItem.get(i).getOrderItems().get(0).getProductName());
            buyAgainPrices.add(listOfOrderItem.get(i).getOrderItems().get(0).getPrice());
            buyAgainImages.add(listOfOrderItem.get(i).getOrderItems().get(0).getImageUrl());
        }

        // Delete item duplicate
        HashSet<String> uniqueNames = new HashSet<>();
        for (int i = 0; i < buyAgainNames.size(); i++) {
            String name = buyAgainNames.get(i);

            if (!uniqueNames.add(name)) {
                buyAgainNames.remove(i);
                buyAgainPrices.remove(i);
                buyAgainImages.remove(i);
                i--;
            }
        }

        BuyAgainAdapter adapter = new BuyAgainAdapter(buyAgainNames, buyAgainPrices, buyAgainImages, this);
        binding.buyAgainRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.buyAgainRecyclerView.setAdapter(adapter);
    }

    private void seeItemsRecentBuyed() {
        OrderDetail recentOrderItem = listOfOrderItem.get(0);
        Intent intent = new Intent(this, RecentOrderItemsActivity.class);
        intent.putExtra("RecentOrderItem", recentOrderItem);
        startActivity(intent);
    }

    private void updateOrderStatus() {
        String itemPushKey = listOfOrderItem.get(0).getItemPushKey();
        DatabaseReference historyOrderRef = database.getReference().child("Users").child(userID).child("BuyHistory").child(itemPushKey);
        DatabaseReference completeOrderRef = database.getReference().child("CompletedOrders").child(itemPushKey);
        completeOrderRef.child("paymentReceived").setValue(true).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

                historyOrderRef.child("paymentReceived").setValue(true);
                binding.receivedButton.setVisibility(View.INVISIBLE);
                Toast.makeText(HistoryCartActivity.this, "Chúc bạn ngon miệng (●'◡'●)", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Order status", "onFailure: " + e.getMessage());
            }
        });
    }
}
