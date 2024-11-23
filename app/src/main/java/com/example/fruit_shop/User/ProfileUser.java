package com.example.fruit_shop.User;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fruit_shop.R;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileUser extends AppCompatActivity {

    private ImageView imgBack;
    private EditText nameEditText, phoneEditText, emailEditText;
    private Spinner genderSpinner;
    private Button btnSave;
    private TextView titleTextView;

    private DatabaseReference userDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_user);

        // Ánh xạ các thành phần giao diện
        imgBack = findViewById(R.id.imgBack);
        nameEditText = findViewById(R.id.NameUser);
        phoneEditText = findViewById(R.id.editTextNumberPhone);
        emailEditText = findViewById(R.id.editTextEmail);
        genderSpinner = findViewById(R.id.spinnerGender);
        btnSave = findViewById(R.id.btnSave);
        titleTextView = findViewById(R.id.textView14);

        // Khởi tạo Firebase Database reference
        userDatabaseRef = FirebaseDatabase.getInstance().getReference("Users");

        // Cài đặt Adapter cho Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(adapter);

        // Lấy UID của người dùng đã đăng nhập từ Firebase Authentication
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String userId = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getUid() : null;

        if (userId != null) {
            // Lấy thông tin người dùng từ Firebase
            loadUserInfo(userId);
        } else {
            Toast.makeText(ProfileUser.this, "Không thể xác định người dùng", Toast.LENGTH_SHORT).show();
        }

        // Xử lý sự kiện khi người dùng nhấn nút "Quay lại"
        imgBack.setOnClickListener(v -> finish());

        // Xử lý sự kiện khi người dùng nhấn nút "Lưu"
        btnSave.setOnClickListener(v -> {
            if (userId != null) {
                // Khi người dùng nhấn lưu, bạn cần lưu lại thông tin đã thay đổi
                saveUserInfo(userId);
            } else {
                Toast.makeText(ProfileUser.this, "Lỗi người dùng không hợp lệ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Hàm lấy thông tin người dùng từ Firebase
    private void loadUserInfo(String userId) {
        userDatabaseRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Lấy thông tin từ Firebase
                    String name = dataSnapshot.child("name").getValue(String.class);
                    String phone = dataSnapshot.child("phone").getValue(String.class);
                    String email = dataSnapshot.child("email").getValue(String.class);
                    String gender = dataSnapshot.child("gender").getValue(String.class);

                    // Hiển thị thông tin vào các EditText và Spinner
                    nameEditText.setText(name);
                    phoneEditText.setText(phone);
                    emailEditText.setText(email);

                    // Set giới tính trong Spinner
                    if ("Nam".equals(gender)) {
                        genderSpinner.setSelection(0);
                    } else if ("Nữ".equals(gender)) {
                        genderSpinner.setSelection(1);
                    } else if ("Khác".equals(gender)) {
                        genderSpinner.setSelection(2);
                    }
                } else {
                    Toast.makeText(ProfileUser.this, "Không tìm thấy thông tin người dùng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ProfileUser.this, "Lỗi khi tải dữ liệu: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Hàm lưu thông tin người dùng vào Firebase
    private void saveUserInfo(String userId) {
        String updatedName = nameEditText.getText().toString();
        String updatedPhone = phoneEditText.getText().toString();
        String updatedEmail = emailEditText.getText().toString();
        String updatedGender = genderSpinner.getSelectedItem().toString();

        // Kiểm tra tính hợp lệ của thông tin người dùng
        if (TextUtils.isEmpty(updatedName) || TextUtils.isEmpty(updatedPhone) || TextUtils.isEmpty(updatedEmail)) {
            Toast.makeText(ProfileUser.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
        } else {
            // Cập nhật thông tin người dùng vào Firebase
            userDatabaseRef.child(userId).child("name").setValue(updatedName);
            userDatabaseRef.child(userId).child("phone").setValue(updatedPhone);
            userDatabaseRef.child(userId).child("email").setValue(updatedEmail);
            userDatabaseRef.child(userId).child("gender").setValue(updatedGender);

            Toast.makeText(ProfileUser.this, "Thông tin đã được cập nhật!", Toast.LENGTH_SHORT).show();
        }
    }
}
