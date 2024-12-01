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

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
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

    private View btnExplore;
    private View btnProfile;
    private View btnNotification;
    private TextView productAll;
    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private ImageView menuIcon, imagecat;
    private ArrayList<Product> productList;
    private ImageSlider imgBanner;

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
        imgBanner = findViewById(R.id.imgBanner);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        fetchProductsFromFirebase();

        // Xử lý sự kiện các nút
        imagecat.setOnClickListener(view -> startActivity(new Intent(HomeActivity.this, OrderActivity.class)));
        productAll.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, ProductAllActivity.class)));
        btnExplore.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, ExploreActivity.class)));
        btnProfile.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, ProfileActivity.class)));
        btnNotification.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, OrderActivity.class)));

        // Xử lý sự kiện menu
        menuIcon.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(this, view);
            popupMenu.getMenuInflater().inflate(R.menu.menu_items, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(item -> handleMenuItemClick(item.getItemId()));
            popupMenu.show();
        });

        // Lấy dữ liệu Banner từ Firebase
        fetchBannersFromFirebase();
    }

    // Hàm lấy dữ liệu sản phẩm từ Firebase
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

    // Lấy dữ liệu Banners từ Firebase
    private void fetchBannersFromFirebase() {
        DatabaseReference bannersRef = FirebaseDatabase.getInstance().getReference("Banners");
        ArrayList<SlideModel> imageList = new ArrayList<>();

        // Fetch Banner data
        bannersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    // Lấy dữ liệu Banner
                    String imageUrl = dataSnapshot.child("imageUrl").getValue(String.class);
                    if (imageUrl != null) {
                        imageList.add(new SlideModel(imageUrl, ScaleTypes.FIT));
                    }
                }

                // Cập nhật ảnh vào ImageSlider
                imgBanner.setImageList(imageList, ScaleTypes.FIT);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("HomeActivity", "Failed to fetch banners: " + error.getMessage());
            }
        });
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
                .setNegativeButton("Không", (dialog, which) -> dialog.dismiss())
                .show();
    }
}
