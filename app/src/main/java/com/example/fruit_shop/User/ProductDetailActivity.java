package com.example.fruit_shop.User;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.fruit_shop.Model.CartItem;
import com.example.fruit_shop.Model.Product;
import com.example.fruit_shop.R;
import com.example.fruit_shop.Utils.FormatValues;
import com.example.fruit_shop.databinding.ActivityProductDetailBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProductDetailActivity extends AppCompatActivity {

    private ActivityProductDetailBinding binding;

    private String productCode;      // Mã sản phẩm (dựa trên id sản phẩm Firebase)
    private String productName;      // Tên sản phẩm
    private String category;         // Danh mục
    private String stockQuantity;    // Số lượng tồn kho
    private double price;            // Giá gốc
    private double discountPrice;    // Giá sau giảm giá
    private String discountCode;     // Mã giảm giá
    private String dateIn;           // Ngày nhập
    private String imageUrl;         // URL hình ảnh sản phẩm
    private String edtDescription;   // Mô tả sản phẩm
    private String rating;           // Số sao (đánh giá)

    private Product product;

    private DatabaseReference databaseReference;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityProductDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        auth = FirebaseAuth.getInstance();


        // Lấy dữ liệu từ Intent
        product = (Product) getIntent().getSerializableExtra("ProductInfo");
        if (product != null) {
            productCode = product.getProductCode();
            productName = product.getProductName();
            category = product.getCategory();
            stockQuantity = product.getStockQuantity();
            price = product.getPrice();
            discountPrice = product.getDiscountPrice();
            discountCode = product.getDiscountCode();
            dateIn = product.getDateIn();
            imageUrl = product.getImageUrl();
            edtDescription = product.getEdtDescription();
            rating = product.getRating();
        }

        binding.DetailsName.setText(productName);
        binding.DetailsDescription.setText(edtDescription);
        binding.DetailsPrice.setText(FormatValues.formatMoney(price));
       // binding.DetailsQuantity.setText(stockQuantity);
        binding.DetailsRating.setText("Đánh giá" +" "+ rating);

        Glide.with(this).load(imageUrl).into(binding.DetailsImage);

        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        binding.DetailsCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProductDetailActivity.this, OrderActivity.class));
            }
        });

        binding.btnAddToCart.setOnClickListener(v -> {
            addItemToCart();
        });

        updateStarIcons(rating);
    }

    private void updateStarIcons(String rating) {

        float ratingValue = 0;

        // Kiểm tra và chuyển đổi rating sang số thực
        if (rating != null && !rating.isEmpty()) {
            try {
                ratingValue = Float.parseFloat(rating);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Rating không hợp lệ!", Toast.LENGTH_SHORT).show();
                return; // Thoát nếu không thể parse rating
            }
        }

        // Reset tất cả các sao về trạng thái rỗng
        binding.star1.setImageResource(R.drawable.star_empty);
        binding.star2.setImageResource(R.drawable.star_empty);
        binding.star3.setImageResource(R.drawable.star_empty);
        binding.star4.setImageResource(R.drawable.star_empty);
        binding.star5.setImageResource(R.drawable.star_empty);

        // Cập nhật sao đầy dựa trên ratingValue
        if (ratingValue >= 1) binding.star1.setImageResource(R.drawable.star_full);
        if (ratingValue >= 2) binding.star2.setImageResource(R.drawable.star_full);
        if (ratingValue >= 3) binding.star3.setImageResource(R.drawable.star_full);
        if (ratingValue >= 4) binding.star4.setImageResource(R.drawable.star_full);
        if (ratingValue >= 5) binding.star5.setImageResource(R.drawable.star_full);

        // Cập nhật sao nửa nếu cần
        if (ratingValue > 0 && ratingValue < 1) {
            binding.star1.setImageResource(R.drawable.star_half);
        } else if (ratingValue > 1 && ratingValue < 2) {
            binding.star2.setImageResource(R.drawable.star_half);
        } else if (ratingValue > 2 && ratingValue < 3) {
            binding.star3.setImageResource(R.drawable.star_half);
        } else if (ratingValue > 3 && ratingValue < 4) {
            binding.star4.setImageResource(R.drawable.star_half);
        } else if (ratingValue > 4 && ratingValue < 5) {
            binding.star5.setImageResource(R.drawable.star_half);
        }
    }

    private void addItemToCart() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        String userID = auth.getCurrentUser().getUid();

        CartItem cartItem = new CartItem(productCode, productName, category, 1, price, discountPrice, discountCode, dateIn, imageUrl, edtDescription, rating);

        // Save cart item to Firebase
        databaseReference.child("Users").child(userID).child("CartItems").push().setValue(cartItem).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(ProductDetailActivity.this, "Thêm vào giỏ hàng thành công", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ProductDetailActivity.this, HomeActivity.class));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ProductDetailActivity.this, "Thêm vào giỏ hàng thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }
}



