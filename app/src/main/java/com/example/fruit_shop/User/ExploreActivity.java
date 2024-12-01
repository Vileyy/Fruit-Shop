package com.example.fruit_shop.User;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fruit_shop.Adapter.SearchPopularAdapter;
import com.example.fruit_shop.R;

import java.util.ArrayList;
import java.util.List;

public class ExploreActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SearchPopularAdapter searchPopularAdapter;
    private ImageView menuIcon; // Tham chiếu đến ImageView nút menu
    private View btnExplore;
    private View btnProfile;
    private View btnNotification;
    private View btnHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Kích hoạt Edge-to-Edge cho Activity
        EdgeToEdge.enable(this);

        // Đặt layout cho activity
        setContentView(R.layout.activity_explore);

        // Ánh xạ các thành phần
        recyclerView = findViewById(R.id.recyclerViewSearchPopular);
        menuIcon = findViewById(R.id.Menu);
        btnExplore = findViewById(R.id.Explore);
        btnProfile = findViewById(R.id.Profile);
        btnNotification = findViewById(R.id.ShoppingCart);
        btnHome = findViewById(R.id.Home);

        //Xử lý sự kiện bottom navigation
        // Xử lý sự kiện Explore
        btnExplore.setOnClickListener(v -> startActivity(new Intent(ExploreActivity.this, ExploreActivity.class)));

        // Xử lý sự kiện Profile
        btnProfile.setOnClickListener(v -> startActivity(new Intent(ExploreActivity.this, ProfileActivity.class)));

        // Xử lý sự kiện Notification
        btnNotification.setOnClickListener(v -> startActivity(new Intent(ExploreActivity.this, OrderActivity.class)));

        // Xử lý sự kiện Home
        btnHome.setOnClickListener(v -> startActivity(new Intent(ExploreActivity.this, HomeActivity.class)));

        // Tạo danh sách các mục tìm kiếm phổ biến
        List<String> popularSearches = new ArrayList<>();
        popularSearches.add("Apple");
        popularSearches.add("Banana");
        popularSearches.add("Orange");
        popularSearches.add("Grapes");
        popularSearches.add("Mango");
        popularSearches.add("Pineapple");
        popularSearches.add("Watermelon");
        popularSearches.add("Strawberry");

        // Sử dụng GridLayoutManager với 4 cột
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        recyclerView.setLayoutManager(gridLayoutManager);

        // Tạo và thiết lập adapter cho RecyclerView với danh sách tìm kiếm phổ biến
        searchPopularAdapter = new SearchPopularAdapter(popularSearches);
        recyclerView.setAdapter(searchPopularAdapter);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

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
            startActivity(new Intent(ExploreActivity.this, OrderActivity.class));
            return true;
        } else if (itemId == R.id.settings) {
            startActivity(new Intent(ExploreActivity.this, SettingActivity.class));
            return true;
        } else if (itemId == R.id.help) {
            startActivity(new Intent(ExploreActivity.this, HelpActivity.class));
            return true;
        } else if (itemId == R.id.profileuser) {
            startActivity(new Intent(ExploreActivity.this, ProfileUser.class));
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
                    startActivity(new Intent(ExploreActivity.this, LoginActivity.class));
                    finish();
                })
                .setNegativeButton("Không", (dialog, which) -> dialog.dismiss()) // Đóng dialog nếu chọn "Không"
                .show();
    }
}
