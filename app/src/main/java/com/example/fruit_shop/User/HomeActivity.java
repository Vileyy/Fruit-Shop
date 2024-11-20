package com.example.fruit_shop.User;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fruit_shop.Adapter.ProductAdapter;
import com.example.fruit_shop.Model.Product;
import com.example.fruit_shop.R;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private View btnExplore;
    private View btnProfile;
    private View btnNotification;
    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private List<Product> productList;
    private ImageView menuIcon; // Tham chiếu đến ImageView nút menu

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Ánh xạ các thành phần
        btnExplore = findViewById(R.id.Explore);
        btnProfile = findViewById(R.id.Profile);
        btnNotification = findViewById(R.id.Notification);
        recyclerView = findViewById(R.id.recyclerViewProducts);
        menuIcon = findViewById(R.id.Menu); // Ánh xạ nút menu

        // Khởi tạo danh sách sản phẩm mẫu
        productList = new ArrayList<>();
        productList.add(new Product("Apple", 10000.0, "TP.HCM", R.drawable.item_1));
        productList.add(new Product("Banana", 5000.0, "TP.HCM", R.drawable.item_1));
        productList.add(new Product("Orange", 7000.0, "TP.HCM", R.drawable.item_1));
        productList.add(new Product("Grapes", 12000.0, "TP.HCM", R.drawable.item_1));


        // Thiết lập GridLayoutManager với 2 cột
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        // Tạo và gán Adapter cho RecyclerView
        productAdapter = new ProductAdapter(this, productList);
        recyclerView.setAdapter(productAdapter);

        // Xử lý sự kiện Explore
        btnExplore.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, ExploreActivity.class)));

        // Xử lý sự kiện Profile
        btnProfile.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, ProfileActivity.class)));

        // Xử lý sự kiện Notification
        btnNotification.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, NotificationActivity.class)));

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
