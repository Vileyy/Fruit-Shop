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
import com.example.fruit_shop.Utils.FormatValues;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private Context context;
    private ArrayList<Product> productList;

    // Constructor
    public ProductAdapter(Context context, ArrayList<Product> productList) {
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
        holder.productName.setText(product.getProductName());
        holder.productPrice.setText(FormatValues.formatMoney(product.getPrice()));
        holder.productRating.setText(product.getRating());

        // Use Glide to load image from drawable resource
        Glide.with(context)
                .load(product.getImageUrl())
                .into(holder.productImage);


        holder.itemView.setOnClickListener(v -> {

            Intent intent = new Intent(context, ProductDetailActivity.class);
            intent.putExtra("ProductInfo", product);

            context.startActivity(intent); // Start activity
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    // Method to update the product list when search occurs
    public void updateProductList(ArrayList<Product> newList) {
        this.productList = newList;
        notifyDataSetChanged();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView productName, productPrice, productRating, description;
        ImageView productImage;

        public ProductViewHolder(View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.product_name);
            productPrice = itemView.findViewById(R.id.product_price);
            productRating = itemView.findViewById(R.id.product_rating);
            productImage = itemView.findViewById(R.id.product_image);
            //description = itemView.findViewById(R.id.productDescription);
        }
    }
}
