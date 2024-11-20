package com.example.fruit_shop.User;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.fruit_shop.R;

public class ProductDetailActivity extends AppCompatActivity {

    private ImageView productImage;
    private TextView productName;
    private TextView productPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        // Khởi tạo các thành phần UI
        productImage = findViewById(R.id.productImage);
        productName = findViewById(R.id.productName);
        productPrice = findViewById(R.id.productPrice);

        // Lấy dữ liệu từ Intent
        if (getIntent() != null) {
            int imageResId = getIntent().getIntExtra("productImageResId", 0); // Nhận imageResId (tài nguyên ảnh)
            String name = getIntent().getStringExtra("productName");
            String price = getIntent().getStringExtra("productPrice");

            // Hiển thị dữ liệu lên UI
            if (imageResId != 0) { // Kiểm tra nếu imageResId hợp lệ
                productImage.setImageResource(imageResId); // Sử dụng setImageResource để hiển thị ảnh từ drawable
            }

            if (name != null) {
                productName.setText(name);
            }

            if (price != null) {
                productPrice.setText(price);
            }
        }
    }
}
