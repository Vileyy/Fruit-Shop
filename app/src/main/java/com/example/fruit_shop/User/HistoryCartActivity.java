package com.example.fruit_shop.User;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fruit_shop.Adapter.BuyAgainAdapter;
import com.example.fruit_shop.Model.CartItem;
import com.example.fruit_shop.Model.OrderDetail;
import com.example.fruit_shop.R;
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
import java.util.List;

public class HistoryCartActivity extends AppCompatActivity {
    private ActivityHistoryCartBinding binding;
    private ArrayList<OrderDetail> listOfOrderItem;
    private BuyAgainAdapter BuyAgainAdapter;
    private List<OrderDetail> orderDetails;
    private RecyclerView buyAgainRecyclerView;
    private DatabaseReference databaseRefOrderDetails;

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
        // Khởi tạo RecyclerView
        buyAgainRecyclerView = findViewById(R.id.buyAgainRecyclerView);
        buyAgainRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Khởi tạo danh sách
        ArrayList<String> productNames = new ArrayList<>();
        ArrayList<Double> productPrices = new ArrayList<>();
        ArrayList<String> productImages = new ArrayList<>();

        // Khởi tạo Adapter
        BuyAgainAdapter adapter = new BuyAgainAdapter(productNames, productPrices, productImages, this);
        buyAgainRecyclerView.setAdapter(adapter);

        // Khởi tạo Firebase Database Reference
        databaseRefOrderDetails = FirebaseDatabase.getInstance().getReference("OrderDetails");

        // Tải danh sách đơn hàng từ Firebase
        loadOrdersFromFirebase(productNames, productPrices, productImages, adapter);

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
    private void loadOrdersFromFirebase(
            ArrayList<String> productNames,
            ArrayList<Double> productPrices,
            ArrayList<String> productImages,
            BuyAgainAdapter adapter
    ) {
        databaseRefOrderDetails.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Xóa danh sách cũ
                productNames.clear();
                productPrices.clear();
                productImages.clear();

                // Duyệt qua tất cả các đơn hàng trong Firebase
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    OrderDetail orderDetail = snapshot.getValue(OrderDetail.class);
                    if (orderDetail != null &&
                            Boolean.TRUE.equals(orderDetail.getOrderAccepted()) &&
                            Boolean.TRUE.equals(orderDetail.getStatusPending()) &&
                            Boolean.TRUE.equals(orderDetail.getSuccessfulDelivery())) {

                        // Duyệt qua danh sách CartItem bên trong OrderDetail
                        if (orderDetail.getOrderItems() != null) {
                            for (CartItem cartItem : orderDetail.getOrderItems()) {
                                if (cartItem != null) {
                                    // Thêm dữ liệu sản phẩm vào danh sách
                                    productNames.add(cartItem.getProductName());
                                    productPrices.add(cartItem.getPrice());
                                    productImages.add(cartItem.getImageUrl());
                                }
                            }
                        }
                    }
                }

                // Cập nhật adapter
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(HistoryCartActivity.this, "Firebase Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void retrieveBuyHistory() {
        binding.recentBuyItem.setVisibility(View.INVISIBLE);
        userID = auth.getCurrentUser().getUid();
        DatabaseReference buyItemRef = database.getReference().child("OrderDetails");
        listOfOrderItem = new ArrayList<>();

        // Sort by currentTime
        Query shortingQuery = buyItemRef.orderByChild("currentTime");

        shortingQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Get data from firebase and add to list
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    OrderDetail buyHistoryItem = dataSnapshot.getValue(OrderDetail.class);
                    if (buyHistoryItem != null && buyHistoryItem.getUserUid() != null) {
                        // Check if the user UID matches and the delivery is not successful
                        if (buyHistoryItem.getUserUid().equals(userID) && !buyHistoryItem.getSuccessfulDelivery()) {
                            listOfOrderItem.add(buyHistoryItem);
                        }
                    }
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
    //



    private void setDataInRecentBuyItem() {
        binding.recentBuyItem.setVisibility(View.VISIBLE);
        OrderDetail recentOrderItem = listOfOrderItem.get(0);

        binding.buyAgainProductName.setText(recentOrderItem.getOrderItems().get(0).getProductName());
        binding.buyAgainPrice.setText(FormatValues.formatMoney(recentOrderItem.getOrderItems().get(0).getPrice()));

        // Load image using Glide
        Glide.with(this).load(recentOrderItem.getOrderItems().get(0).getImageUrl()).into(binding.buyAgainImage);

        boolean isOrderIsAccepted = listOfOrderItem.get(0).getOrderAccepted();
        boolean isOrderIsReceived = listOfOrderItem.get(0).getPaymentReceived();
        Boolean isStatusPending = recentOrderItem.getStatusPending(); // Lấy giá trị từ đối tượng OrderDetail
        if (!isOrderIsAccepted) {
            // Chưa xác nhận: hiển thị "Chờ xác nhận" với màu đỏ
            binding.orderStatus.setCardBackgroundColor(Color.RED);
            binding.statusText.setText("Chờ xác nhận");
        } else if (isOrderIsAccepted && !isStatusPending) {
            // Đã xác nhận và không đang chờ (orderAccepted = true và statusPending = false): hiển thị "Đã xác nhận" với màu xanh
            binding.orderStatus.setCardBackgroundColor(Color.BLACK);
            binding.statusText.setText("Đã xác nhận");
        } else if (isOrderIsAccepted && isStatusPending) {
            // Đã xác nhận và đang giao hàng (orderAccepted = true và statusPending = true): hiển thị "Đang giao hàng" với màu cam
            binding.orderStatus.setCardBackgroundColor(Color.parseColor("#0000FF")); // Màu cam cho "Đang giao hàng"
            binding.statusText.setText("Đang giao hàng");
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
        // Giả sử bạn đã lấy thông tin từ OrderDetail
        OrderDetail orderDetail = listOfOrderItem.get(0); // Chọn item đầu tiên trong list
        Boolean isOrderAccepted = orderDetail.getOrderAccepted();
        Boolean isStatusPending = orderDetail.getStatusPending();
        Boolean isSuccessfulDelivery = orderDetail.getSuccessfulDelivery(); // Lấy giá trị successfulDelivery

        // Log giá trị của isStatusPending, isOrderAccepted và isSuccessfulDelivery để kiểm tra
        Log.d("Order Status", "isStatusPending: " + isStatusPending + ", isOrderAccepted: " + isOrderAccepted + ", isSuccessfulDelivery: " + isSuccessfulDelivery);

        // Kiểm tra điều kiện khi nút 'receivedButton' sẽ được hiển thị
        if (isStatusPending != null && isOrderAccepted != null && isSuccessfulDelivery != null &&
                isStatusPending && isOrderAccepted && !isSuccessfulDelivery) {
            // Hiển thị nút với nền màu đỏ khi điều kiện thỏa mãn
            binding.receivedButton.setVisibility(View.VISIBLE);  // Hiển thị nút
            binding.receivedButton.setBackgroundColor(ContextCompat.getColor(this, R.color.red)); // Màu đỏ
        } else {
            // Hiển thị nút với nền màu xám khi không thỏa mãn điều kiện
            binding.receivedButton.setVisibility(View.VISIBLE);  // Hiển thị nút
            binding.receivedButton.setBackgroundColor(ContextCompat.getColor(this, R.color.gray)); // Màu xám
        }

        // Xử lý sự kiện khi nhấn vào nút 'receivedButton'
        binding.receivedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isStatusPending != null && isOrderAccepted != null && isSuccessfulDelivery != null &&
                        isStatusPending && isOrderAccepted && !isSuccessfulDelivery) {
                    // Cập nhật giá trị 'successfulDelivery' thành true khi nhấn
                    String itemPushKey = listOfOrderItem.get(0).getItemPushKey();
                    DatabaseReference completeOrderRef = database.getReference().child("OrderDetails").child(itemPushKey);

                    // Cập nhật giá trị successfulDelivery trong Firebase
                    completeOrderRef.child("successfulDelivery").setValue(true).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            // Ẩn nút và hiển thị thông báo
                            binding.receivedButton.setVisibility(View.INVISIBLE); // Ẩn nút sau khi nhấn
                            Toast.makeText(HistoryCartActivity.this, "Cảm Ơn Bạn Đã Đặt Hàng !!!", Toast.LENGTH_SHORT).show();

                            // Gọi lại phương thức retrieveBuyHistory() để tải lại dữ liệu và cập nhật UI
                            retrieveBuyHistory();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Xử lý khi có lỗi
                            Log.d("Order status", "onFailure: " + e.getMessage());
                            Toast.makeText(HistoryCartActivity.this, "Đã có lỗi xảy ra. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    // Hiển thị thông báo khi đơn hàng chưa xử lý xong
                    Toast.makeText(HistoryCartActivity.this, "Đơn hàng chưa xử lý xong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }







}
