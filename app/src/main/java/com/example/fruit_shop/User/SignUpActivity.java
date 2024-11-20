package com.example.fruit_shop.User;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.fruit_shop.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button signUpButton;
    private EditText nameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        // Setup window insets for Edge-to-Edge display
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Initialize views
        emailEditText = findViewById(R.id.Email);
        passwordEditText = findViewById(R.id.Password);
        signUpButton = findViewById(R.id.btnSignUp);
        nameEditText = findViewById(R.id.NameUser);

        // Set onClick listener for the signup button
        signUpButton.setOnClickListener(view -> signUpUser());
    }

    private void signUpUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String userName = nameEditText.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty() || userName.isEmpty()) {
            Toast.makeText(SignUpActivity.this, "Vui lòng nhập Email, mật khẩu và tên người dùng", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign up success, update UI with the signed-in user's information
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            saveUserName(userName); // Lưu tên người dùng vào SharedPreferences
                        }

                        Toast.makeText(SignUpActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();

                        new Handler().postDelayed(() -> {
                            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }, 1000); // Delay 1 giây
                    } else {
                        // If sign up fails, display a message to the user.
                        Toast.makeText(SignUpActivity.this, "Đăng ký thất bại! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveUserName(String userName) {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userName", userName); // Lưu tên người dùng vào SharedPreferences
        editor.apply();
    }
}
