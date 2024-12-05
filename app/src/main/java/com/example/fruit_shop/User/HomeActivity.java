package com.example.fruit_shop.User;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private View btnExplore;
    private View btnProfile;
    private View btnNotification;
    private TextView productAll;
    private ImageView start1, start2, start3, start4, start5;
    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private ImageView menuIcon, imagecat;
    private ArrayList<Product> productList, filteredList;
    private ImageSlider imgBanner;
    private EditText searchEditText;// Danh sách cho "Trái Cây Nội"
    private ArrayList<Product> productListNgoaiNhap;  // Danh sách cho "Trái Cây Ngoại Nhập"// Adapter cho sản phẩm "Trái Cây Nội"
    private ProductAdapter productAdapterNgoaiNhap; // Danh sách cho "Trái Cây Ngoại Nhập"
    private ArrayList<Product> productListKhac;  // Danh sách cho "Trái Cây Ngoại Nhập"// Adapter cho sản phẩm "Trái Cây Nội"
    private ProductAdapter productAdapterKhac;
    // Firebase
    private DatabaseReference databaseReference;
    private RecyclerView recyclerViewProductsNgoaiNhap,recyclerViewProductKhac;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Ánh xạ các thành phần
        btnExplore = findViewById(R.id.Explore);
        btnProfile = findViewById(R.id.Profile);
        btnNotification = findViewById(R.id.ShoppingCart);
        recyclerView = findViewById(R.id.recyclerViewProducts);
        menuIcon = findViewById(R.id.Menu);
        imagecat = findViewById(R.id.Cart);
        productAll = findViewById(R.id.product_all);
        imgBanner = findViewById(R.id.imgBanner);
        searchEditText = findViewById(R.id.searchEditText);
        recyclerViewProductsNgoaiNhap = findViewById(R.id.recyclerViewProductsNgoaiNhap);
        recyclerViewProductKhac = findViewById(R.id.recyclerViewProductKhac);

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

        // Thêm chức năng tìm kiếm
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String query = charSequence.toString().trim();
                searchProducts(query);
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    // Hàm loại bỏ dấu
    private String removeAccents(String str) {
        String normalizedStr = Normalizer.normalize(str, Normalizer.Form.NFD);
        return normalizedStr.replaceAll("\\p{M}", ""); // Loại bỏ các dấu (diacritics)
    }

    // Hàm tìm kiếm sản phẩm
    private void searchProducts(String query) {
        filteredList = new ArrayList<>();
        // Loại bỏ dấu trong chuỗi tìm kiếm của người dùng
        String queryWithoutAccents = removeAccents(query.toLowerCase());

        for (Product product : productList) {
            // Loại bỏ dấu trong tên sản phẩm
            String productNameWithoutAccents = removeAccents(product.getProductName().toLowerCase());

            // Kiểm tra nếu tên sản phẩm chứa chuỗi tìm kiếm (không phân biệt dấu)
            if (productNameWithoutAccents.contains(queryWithoutAccents)) {
                filteredList.add(product);
            }
        }
        productAdapter.updateProductList(filteredList);
    }

    private void fetchProductsFromFirebase() {
        // Khởi tạo reference đến Firebase Database
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Product");
        productList = new ArrayList<>();
        productListNgoaiNhap = new ArrayList<>(); // Danh sách cho "Trái Cây Ngoại Nhập"
        productListKhac = new ArrayList<>(); // Danh sách cho "Khác"

        // Fetch dữ liệu từ Firebase
        databaseReference.limitToFirst(20).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Product productInfo = dataSnapshot.getValue(Product.class);

                    // Kiểm tra category là "Trái Cây Nội", "Trái Cây Ngoại Nhập" hoặc "Khác"
                    if (productInfo != null) {
                        if ("Trái Cây Nội".equals(productInfo.getCategory())) {
                            productList.add(productInfo);  // Thêm vào danh sách sản phẩm Nội
                        } else if ("Trái Cây Ngoại Nhập".equals(productInfo.getCategory())) {
                            productListNgoaiNhap.add(productInfo);  // Thêm vào danh sách sản phẩm Ngoại Nhập
                        } else if ("Khác".equals(productInfo.getCategory())) {
                            productListKhac.add(productInfo);  // Thêm vào danh sách sản phẩm Khác
                        }
                    }
                }
                showProducts(); // Hiển thị sản phẩm đã lọc cho "Trái Cây Nội"
                showProductsNgoaiNhap(); // Hiển thị sản phẩm "Trái Cây Ngoại Nhập"
                showProductsKhac(); // Hiển thị sản phẩm "Khác"
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Load Data", "onCancelled: Failed" + error.getMessage());
            }
        });
    }

    private void showProducts() {
        List<Product> limitedProductList = productList.size() > 6 ? productList.subList(0, 6) : productList;
        // Cập nhật RecyclerView cho sản phẩm "Trái Cây Nội"
        productAdapter = new ProductAdapter(this, productList);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));  // Hiển thị 2 sản phẩm mỗi dòng
        recyclerView.setAdapter(productAdapter);
    }
    private void showProductsNgoaiNhap() {
        // Giới hạn số lượng sản phẩm hiển thị tối đa là 6
        List<Product> limitedProductList = productListNgoaiNhap.size() > 6 ? productListNgoaiNhap.subList(0, 6) : productListNgoaiNhap;

        // Cập nhật RecyclerView cho sản phẩm "Trái Cây Ngoại Nhập"
        productAdapterNgoaiNhap = new ProductAdapter(this, new ArrayList<>(limitedProductList));
        recyclerViewProductsNgoaiNhap.setLayoutManager(new GridLayoutManager(this, 2));  // Hiển thị 2 sản phẩm mỗi dòng
        recyclerViewProductsNgoaiNhap.setAdapter(productAdapterNgoaiNhap);
    }

    private void showProductsKhac() {
        // Giới hạn số lượng sản phẩm hiển thị tối đa là 6
        List<Product> limitedProductList = productListKhac.size() > 6 ? productListKhac.subList(0, 6) : productListKhac;
        // Cập nhật RecyclerView cho sản phẩm "Khác"
        productAdapterKhac = new ProductAdapter(this, productListKhac);
        recyclerViewProductKhac.setLayoutManager(new GridLayoutManager(this, 2));  // Hiển thị 2 sản phẩm mỗi dòng
        recyclerViewProductKhac.setAdapter(productAdapterKhac);
    }



    // Lấy dữ liệu Banners từ Firebase
    private void fetchBannersFromFirebase() {
        DatabaseReference bannersRef = FirebaseDatabase.getInstance().getReference("Banners");
        ArrayList<SlideModel> imageList = new ArrayList<>();

        bannersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String imageUrl = dataSnapshot.child("imageUrl").getValue(String.class);
                    if (imageUrl != null) {
                        imageList.add(new SlideModel(imageUrl, ScaleTypes.FIT));
                    }
                }
                imgBanner.setImageList(imageList, ScaleTypes.FIT);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("HomeActivity", "Failed to fetch banners: " + error.getMessage());
            }
        });
    }

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

    private void logoutUser() {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận đăng xuất")
                .setMessage("Bạn có chắc chắn muốn đăng xuất?")
                .setPositiveButton("Có", (dialog, which) -> {
                    SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.apply();
                    Toast.makeText(this, "Đã đăng xuất", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                    finish();
                })
                .setNegativeButton("Không", (dialog, which) -> dialog.dismiss())
                .show();
    }
}
