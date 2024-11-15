package com.example.fruit_shop.User;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Kích hoạt Edge-to-Edge cho Activity
        EdgeToEdge.enable(this);

        // Đặt layout cho activity
        setContentView(R.layout.activity_explore);

        // Thiết lập RecyclerView
        recyclerView = findViewById(R.id.recyclerViewSearchPopular);

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
        // Thêm các tìm kiếm phổ biến khác nếu cần

        // Sử dụng GridLayoutManager với 4 cột
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        recyclerView.setLayoutManager(gridLayoutManager);

        // Tạo và thiết lập adapter cho RecyclerView với danh sách tìm kiếm phổ biến
        searchPopularAdapter = new SearchPopularAdapter(popularSearches);
        recyclerView.setAdapter(searchPopularAdapter);

        // Thiết lập padding cho main layout để đảm bảo không bị che khuất bởi thanh trạng thái hoặc thanh điều hướng
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            // Lấy các insets của thanh hệ thống
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());

            // Đặt padding cho view chính
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

            // Trả về insets để chúng không bị tiêu tốn
            return insets;
        });
    }
}
