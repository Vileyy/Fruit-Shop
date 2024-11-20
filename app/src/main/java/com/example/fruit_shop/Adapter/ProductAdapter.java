package com.example.fruit_shop.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fruit_shop.Model.Product;
import com.example.fruit_shop.R;
import com.example.fruit_shop.User.ProductDetailActivity;

import java.text.DecimalFormat;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private Context context;
    private List<Product> productList;

    // Constructor
    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_menu_item, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.productName.setText(product.getName());

        // Convert price to VND and format it
        String priceInVND = formatCurrency(product.getPrice());
        holder.productPrice.setText(priceInVND);

        holder.productLocation.setText(product.getLocation());

        // Use Glide to load image from drawable resource
        Glide.with(context)
                .load(product.getImageResId())  // Load image from drawable resource ID
                .into(holder.productImage);

        // Handle item click event
        holder.itemView.setOnClickListener(v -> {
            // Create Intent to navigate to Product Detail screen
            Intent intent = new Intent(context, ProductDetailActivity.class);
            intent.putExtra("productImageResId", product.getImageResId()); // Pass image resource ID
            intent.putExtra("productName", product.getName());  // Pass product name
            intent.putExtra("productPrice", priceInVND); // Pass formatted price
            intent.putExtra("productLocation", product.getLocation()); // Pass product location
            context.startActivity(intent); // Start activity
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    // Method to format price as currency in VND
    private String formatCurrency(double price) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        return decimalFormat.format(price) + "đ";  // Append "đ" to denote Vietnamese Dong
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView productName, productPrice, productLocation;
        ImageView productImage;

        public ProductViewHolder(View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.product_name);
            productPrice = itemView.findViewById(R.id.product_price);
            productLocation = itemView.findViewById(R.id.product_location);
            productImage = itemView.findViewById(R.id.product_image);
        }
    }
}
