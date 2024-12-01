package com.example.fruit_shop.User;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fruit_shop.Adapter.AllProductAdapter;
import com.example.fruit_shop.Model.Product;
import com.example.fruit_shop.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProductAllActivity extends AppCompatActivity {

    private ImageView backimagebutton;
    private RecyclerView recyclerView;
    private AllProductAdapter productAdapter;
    private ArrayList<Product> productList;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_all);

        backimagebutton = findViewById(R.id.backImageButton);
        recyclerView = findViewById(R.id.recyclerViewProducts);

        backimagebutton.setOnClickListener(v -> finish());

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);

        fetchProductsFromFirebase();
    }

    private void fetchProductsFromFirebase() {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Product");
        productList = new ArrayList<>();

        // Fetch data
        databaseReference.limitToFirst(10).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Get each item and add to list
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Product productInfo = dataSnapshot.getValue(Product.class);
                    productList.add(productInfo);
                }

                showProducts();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Load Data", "onCancelled: Failed" + error.getMessage());
            }
        });


    }

    private void showProducts() {
        productAdapter = new AllProductAdapter(this, productList);
        recyclerView.setAdapter(productAdapter);
    }
}