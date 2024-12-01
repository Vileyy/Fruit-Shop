package com.example.fruit_shop.User;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.fruit_shop.CongratsBottomSheetFragment;
import com.example.fruit_shop.Model.CartItem;
import com.example.fruit_shop.Model.OrderDetail;
import com.example.fruit_shop.R;
import com.example.fruit_shop.Utils.FormatValues;
import com.example.fruit_shop.databinding.ActivityPayBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PayActivity extends AppCompatActivity {

    private ActivityPayBinding binding;
    private ArrayList<CartItem> cartItems;
    private ArrayList<CartItem> orderItems;

    // Order info
    private String name;
    private String address;
    private String phone;
    private String totalAmount;

    // Firebase
    private FirebaseAuth auth;
    private DatabaseReference databaseRef;
    private String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPayBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        auth = FirebaseAuth.getInstance();
        databaseRef = FirebaseDatabase.getInstance().getReference();

        setUserData();

        orderItems = (ArrayList<CartItem>) getIntent().getSerializableExtra("cartItems");

        // Total amount
        totalAmount = String.valueOf(getTotalAmount());
        binding.totalAmount.setText(FormatValues.formatMoney(Integer.parseInt(totalAmount)));

        binding.buttonPay.setOnClickListener(v -> {
            name = binding.name.getText().toString();
            address = binding.address.getText().toString();
            phone = binding.phone.getText().toString();

            if (name.isEmpty() || address.isEmpty() || phone.isEmpty()) {
                Toast.makeText(PayActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            } else {
                placeOrder();
            }
        });

        binding.backButton.setOnClickListener(v -> {
            finish();
        });

    }

    private void placeOrder() {
        userID = auth.getCurrentUser().getUid();
        Long time = System.currentTimeMillis();
        String itemPushKey = databaseRef.child("OrderDetails").push().getKey();
        OrderDetail orderDetail = new OrderDetail(userID, name, orderItems, address, phone, totalAmount, false, false, itemPushKey, time);

        DatabaseReference orderRef = databaseRef.child("OrderDetails").child(itemPushKey);
        orderRef.setValue(orderDetail).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(PayActivity.this, "Đặt hàng thành công", Toast.LENGTH_SHORT).show();
                BottomSheetDialogFragment congratsBottomSheet = new CongratsBottomSheetFragment();
                congratsBottomSheet.show(getSupportFragmentManager(), "CongratsBottomSheet");
                removeItemFromCart();
                addOrderToHistory(orderDetail);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(PayActivity.this, "Đặt hàng thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addOrderToHistory(OrderDetail orderDetail) {
        DatabaseReference historyRef = databaseRef.child("Users").child(userID).child("BuyHistory").child(orderDetail.getItemPushKey());
        historyRef.setValue(orderDetail).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Update data", "onFailure: Failed" + e.getMessage());
            }
        });
    }

    private void removeItemFromCart() {
        DatabaseReference cartItemRef = databaseRef.child("Users").child(userID).child("CartItems");
        cartItemRef.removeValue();
    }

    // Có nên lưu luôn là 10.000đ hay 10000
    private int getTotalAmount() {
        int total = 0;
        if (orderItems != null) {
            for (CartItem item : orderItems) {
                try {
                    double price = item.getPrice();
                    int quantity = item.getStockQuantity();
                    total += price * quantity;
                } catch (NumberFormatException e) {
                    Log.e("getTotalAmount", "Invalid price format: " + item.getPrice(), e);
                }
            }
        }

        return total;
    }

    private void setUserData() {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            userID = user.getUid();
            DatabaseReference userRef = databaseRef.child("Users").child(userID);

            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        name = snapshot.child("name").getValue(String.class);
                        address = snapshot.child("address").getValue(String.class);
                        phone = snapshot.child("phone").getValue(String.class);
                    }

                    binding.name.setText(name);
                    binding.address.setText(address);
                    binding.phone.setText(phone);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
    }
}