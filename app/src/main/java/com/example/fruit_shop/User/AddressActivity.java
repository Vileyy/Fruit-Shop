package com.example.fruit_shop.User;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.fruit_shop.R;

public class AddressActivity extends AppCompatActivity {

    private ImageView imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Enable edge-to-edge mode
        EdgeToEdge.enable(this);
        // Set the content view
        setContentView(R.layout.activity_address);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Ánh xạ
        Spinner provinceSpinner = findViewById(R.id.Province);
        Spinner citySpinner = findViewById(R.id.City);
        imgBack = findViewById(R.id.imgBack);

        // Xử lý sự kiện
        imgBack.setOnClickListener(v -> finish());

        // Create an array of provinces and cities (these can be dynamic or come from an API)
        String[] provinces = {"Hà Nội", "Hồ Chí Minh", "Đà Nẵng", "Cần Thơ", "Bình Dương"};
        String[] hanoiCities = {"Ba Đình", "Hoàn Kiếm", "Hai Bà Trưng"};
        String[] hcmCities = {"Quận 1", "Quận 2", "Quận 3"};
        String[] daNangCities = {"Sơn Trà", "Cẩm Lệ", "Liên Chiểu"};

        // Create ArrayAdapters for provinces and cities
        ArrayAdapter<String> provinceAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, provinces);
        provinceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Set the adapter for the province spinner
        provinceSpinner.setAdapter(provinceAdapter);

        // Set listener to update city spinner based on selected province
        provinceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedProvince = parentView.getItemAtPosition(position).toString();
                String[] cities;
                switch (selectedProvince) {
                    case "Hà Nội":
                        cities = hanoiCities;
                        break;
                    case "Hồ Chí Minh":
                        cities = hcmCities;
                        break;
                    case "Đà Nẵng":
                        cities = daNangCities;
                        break;
                    default:
                        cities = new String[]{};
                        break;
                }

                // Update the city spinner based on the selected province
                ArrayAdapter<String> cityAdapter = new ArrayAdapter<>(AddressActivity.this, android.R.layout.simple_spinner_item, cities);
                cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                citySpinner.setAdapter(cityAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Optional: You can set a default city list or do nothing
            }
        });
    }
}
