package com.example.fruit_shop.User;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.fruit_shop.R;

public class ProfileActivity extends AppCompatActivity {
    // Khai báo biến
    private View Address, Order, Help, Setting, ProfileUser, btnExplore, btnProfile, btnNotification, btnHome;
    private LinearLayout Logout;
    private ImageView menuIcon;
    private TextView nameUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Ánh xạ các thành phần
        Address = findViewById(R.id.Address);
        Order = findViewById(R.id.Order);
        Help = findViewById(R.id.Help);
        Setting = findViewById(R.id.Setting);
        ProfileUser = findViewById(R.id.ProfileUser);
        Logout = findViewById(R.id.Logout);
        menuIcon = findViewById(R.id.Menu);
        btnExplore = findViewById(R.id.Explore);
        btnProfile = findViewById(R.id.Profile);
        btnNotification = findViewById(R.id.Notification);
        btnHome = findViewById(R.id.Home);
        nameUser = findViewById(R.id.NameUser);

        // Set the user's name from SharedPreferences
        String userName = getUserName();
        nameUser.setText(userName);

        // Xử lý sự kiện
        Address.setOnClickListener(view -> {
            Intent intent = new Intent(ProfileActivity.this, AddressActivity.class);
            startActivity(intent);
        });

        Order.setOnClickListener(view -> {
            Intent intent = new Intent(ProfileActivity.this, OrderActivity.class);
            startActivity(intent);
        });

        Help.setOnClickListener(view -> {
            Intent intent = new Intent(ProfileActivity.this, HelpActivity.class);
            startActivity(intent);
        });

        Setting.setOnClickListener(view -> {
            Intent intent = new Intent(ProfileActivity.this, SettingActivity.class);
            startActivity(intent);
        });

        ProfileUser.setOnClickListener(view -> {
            Intent intent = new Intent(ProfileActivity.this, ProfileUser.class);
            startActivity(intent);
        });

        // Xử lý sự kiện bottom navigation
        btnExplore.setOnClickListener(v -> startActivity(new Intent(ProfileActivity.this, ExploreActivity.class)));
        btnProfile.setOnClickListener(v -> startActivity(new Intent(ProfileActivity.this, ProfileActivity.class)));
        btnNotification.setOnClickListener(v -> startActivity(new Intent(ProfileActivity.this, NotificationActivity.class)));
        btnHome.setOnClickListener(v -> startActivity(new Intent(ProfileActivity.this, HomeActivity.class)));

        // Xử lý sự kiện Đăng xuất
        Logout.setOnClickListener(view -> {
            new AlertDialog.Builder(ProfileActivity.this)
                    .setTitle("Xác nhận")
                    .setMessage("Bạn có muốn đăng xuất không?")
                    .setCancelable(false)
                    .setPositiveButton("Có", (dialog, id) -> {
                        Toast.makeText(ProfileActivity.this, "Đăng xuất thành công", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    })
                    .setNegativeButton("Không", null)
                    .show();
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

    // Thêm phương thức lấy tên người dùng từ SharedPreferences
    private String getUserName() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        return sharedPreferences.getString("userName", "Người dùng");
    }

    // Hàm xử lý sự kiện khi chọn item trong menu
    private boolean handleMenuItemClick(int itemId) {
        if (itemId == R.id.order) {
            startActivity(new Intent(ProfileActivity.this, OrderActivity.class));
            return true;
        } else if (itemId == R.id.settings) {
            startActivity(new Intent(ProfileActivity.this, SettingActivity.class));
            return true;
        } else if (itemId == R.id.help) {
            startActivity(new Intent(ProfileActivity.this, HelpActivity.class));
            return true;
        } else if (itemId == R.id.profileuser) {
            startActivity(new Intent(ProfileActivity.this, ProfileUser.class));
            return true;
        } else if (itemId == R.id.logout) {
            logoutUser();
            return true;
        } else {
            return false;
        }
    }

    // Phương thức xử lý đăng xuất qua menu
    private void logoutUser() {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận đăng xuất")
                .setMessage("Bạn có chắc chắn muốn đăng xuất?")
                .setPositiveButton("Có", (dialog, which) -> {
                    Toast.makeText(this, "Đã đăng xuất", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
                    finish();
                })
                .setNegativeButton("Không", (dialog, which) -> dialog.dismiss())
                .show();
    }
}
