package com.example.fruit_shop.User;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fruit_shop.Adapter.ProductAdapter;
import com.example.fruit_shop.Model.Product;
import com.example.fruit_shop.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    // Info
    private View btnExplore;
    private View btnProfile;
    private View btnNotification;
    private TextView productAll;
    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private ImageView menuIcon, imagecat;
    private ArrayList<Product> productList;

    // Firebase
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Ánh xạ các thành phần
        btnExplore = findViewById(R.id.Explore);
        btnProfile = findViewById(R.id.Profile);
        btnNotification = findViewById(R.id.ShoppingCart);
        recyclerView = findViewById(R.id.recyclerViewProducts);
        menuIcon = findViewById(R.id.Menu); // Ánh xạ nút menu
        imagecat = findViewById(R.id.Cart);
        productAll = findViewById(R.id.product_all);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        fetchProductsFromFirebase();

        imagecat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, OrderActivity.class));
            }
        });

        productAll.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, ProductAllActivity.class)));


        // Xử lý sự kiện Explore
        btnExplore.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, ExploreActivity.class)));

        // Xử lý sự kiện Profile
        btnProfile.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, ProfileActivity.class)));

        // Xử lý sự kiện Notification
        btnNotification.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, OrderActivity.class)));

        // Xử lý sự kiện menu
        menuIcon.setOnClickListener(view -> {
            // Tạo PopupMenu
            PopupMenu popupMenu = new PopupMenu(this, view);
            popupMenu.getMenuInflater().inflate(R.menu.menu_items, popupMenu.getMenu()); // Gắn menu resource

            // Xử lý sự kiện chọn item trong menu
            popupMenu.setOnMenuItemClickListener(item -> handleMenuItemClick(item.getItemId()));
            popupMenu.show();
        });
    }

    //lấy dữ liệu từ firebase
    private void fetchProductsFromFirebase() {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Product");
        productList = new ArrayList<>();

        // Fetch data
        databaseReference.limitToFirst(10).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Get each item and add to list
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Product productInfo = dataSnapshot.getValue(Product.class);
                    productList.add(productInfo);
                }

                showProducts();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Load Data", "onCancelled: Failed" + error.getMessage());
            }
        });
    }

    private void showProducts() {
        productAdapter = new ProductAdapter(this, productList);
        recyclerView.setAdapter(productAdapter);
    }


    // Hàm xử lý sự kiện khi chọn item trong menu
    private boolean handleMenuItemClick(int itemId) {
        if (itemId == R.id.order) {
            startActivity(new Intent(HomeActivity.this, OrderActivity.class));
            return true;
        } else if (itemId == R.id.settings) {
            startActivity(new Intent(HomeActivity.this, SettingActivity.class));
            return true;
        } else if (itemId == R.id.help) {
            startActivity(new Intent(HomeActivity.this, HelpActivity.class));
            return true;
        } else if (itemId == R.id.profileuser) {
            startActivity(new Intent(HomeActivity.this, ProfileUser.class));
            return true;
        } else if (itemId == R.id.logout) {
            logoutUser();
            return true;
        } else {
            return false;
        }
    }

    // Phương thức xử lý đăng xuất
    private void logoutUser() {
        // Tạo AlertDialog để xác nhận
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận đăng xuất")
                .setMessage("Bạn có chắc chắn muốn đăng xuất?")
                .setPositiveButton("Có", (dialog, which) -> {
                    // Xóa thông tin đăng nhập trong SharedPreferences
                    SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.apply();

                    // Hiển thị thông báo
                    Toast.makeText(this, "Đã đăng xuất", Toast.LENGTH_SHORT).show();

                    // Chuyển về màn hình đăng nhập
                    startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                    finish();
                })
                .setNegativeButton("Không", (dialog, which) -> dialog.dismiss()) // Đóng dialog nếu chọn "Không"
                .show();
    }

}
