package com.example.fruit_shop.User;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.fruit_shop.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    // Khai báo biến
    private View Address, Order, Help, Setting, ProfileUser, btnExplore, btnProfile, btnNotification, btnHome;
    private LinearLayout Logout;
    private ImageView menuIcon;
    private TextView nameUser;

    // Firebase
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        // Khởi tạo Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        // Lấy tên người dùng từ Firebase và hiển thị
        fetchUserName();

        // Xử lý sự kiện
        setupEventHandlers();
    }

    // Phương thức lấy tên người dùng từ Firebase
    private void fetchUserName() {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid(); // Lấy UID của người dùng

            // Truy xuất thông tin từ Firebase Realtime Database
            databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        // Lấy giá trị tên từ "name"
                        String userName = snapshot.child("name").getValue(String.class);
                        if (userName != null && !userName.isEmpty()) {
                            nameUser.setText(userName);
                        } else {
                            nameUser.setText("Tên chưa được cập nhật");
                        }
                    } else {
                        nameUser.setText("Người dùng không tồn tại");
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    nameUser.setText("Lỗi: " + error.getMessage());
                }
            });
        } else {
            nameUser.setText("Bạn chưa đăng nhập");
        }
    }

    // Xử lý sự kiện
    private void setupEventHandlers() {
        Address.setOnClickListener(view -> startActivity(new Intent(ProfileActivity.this, AddressActivity.class)));
        Order.setOnClickListener(view -> startActivity(new Intent(ProfileActivity.this, OrderActivity.class)));
        Help.setOnClickListener(view -> startActivity(new Intent(ProfileActivity.this, HelpActivity.class)));
        Setting.setOnClickListener(view -> startActivity(new Intent(ProfileActivity.this, SettingActivity.class)));
        ProfileUser.setOnClickListener(view -> startActivity(new Intent(ProfileActivity.this, ProfileUser.class)));
        btnExplore.setOnClickListener(v -> startActivity(new Intent(ProfileActivity.this, ExploreActivity.class)));
        btnProfile.setOnClickListener(v -> startActivity(new Intent(ProfileActivity.this, ProfileActivity.class)));
        btnNotification.setOnClickListener(v -> startActivity(new Intent(ProfileActivity.this, NotificationActivity.class)));
        btnHome.setOnClickListener(v -> startActivity(new Intent(ProfileActivity.this, HomeActivity.class)));

        // Xử lý sự kiện Đăng xuất
        Logout.setOnClickListener(view -> new AlertDialog.Builder(ProfileActivity.this)
                .setTitle("Xác nhận")
                .setMessage("Bạn có muốn đăng xuất không?")
                .setPositiveButton("Có", (dialog, id) -> {
                    firebaseAuth.signOut(); // Đăng xuất Firebase
                    Toast.makeText(ProfileActivity.this, "Đăng xuất thành công", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
                    finish();
                })
                .setNegativeButton("Không", null)
                .show());

        // Xử lý sự kiện menu
        menuIcon.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(this, view);
            popupMenu.getMenuInflater().inflate(R.menu.menu_items, popupMenu.getMenu());

            popupMenu.setOnMenuItemClickListener(item -> {
                int id = item.getItemId();
                if (id == R.id.order) {
                    startActivity(new Intent(ProfileActivity.this, OrderActivity.class));
                } else if (id == R.id.settings) {
                    startActivity(new Intent(ProfileActivity.this, SettingActivity.class));
                } else if (id == R.id.help) {
                    startActivity(new Intent(ProfileActivity.this, HelpActivity.class));
                } else if (id == R.id.logout) {
                    firebaseAuth.signOut();
                    Toast.makeText(this, "Đã đăng xuất", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
                    finish();
                }
                return true;
            });
            popupMenu.show();
        });
    }
}
