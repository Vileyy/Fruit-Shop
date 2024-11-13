package com.example.fruit_shop.User;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.fruit_shop.R;

public class SettingActivity extends AppCompatActivity {

    // Khai báo biến
    private ImageView btnBack;
    private View LanguageSetting;;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_setting);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Ánh xạ các thành phần
        btnBack = findViewById(R.id.imgBack);
        LanguageSetting = findViewById(R.id.languageSetting);

        // Xu lý sự kiện
        btnBack.setOnClickListener(v -> finish());

        LanguageSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent intent = new Intent(SettingActivity.this, LanguageActivity.class);
                startActivity(intent);
            }
        });
    }
}