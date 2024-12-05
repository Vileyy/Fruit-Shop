package com.example.fruit_shop.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fruit_shop.Model.CartItem;
import com.example.fruit_shop.Model.Product;
import com.example.fruit_shop.R;
import com.example.fruit_shop.User.ProductDetailActivity;
import com.example.fruit_shop.Utils.FormatValues;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {


    private Context context;
    private ArrayList<Product> productList;
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;

    // Constructor
    public ProductAdapter(Context context, ArrayList<Product> productList) {
        this.context = context;
        this.productList = productList;
        this.databaseReference = FirebaseDatabase.getInstance().getReference();
        this.auth = FirebaseAuth.getInstance();
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

        holder.addToCartButton.setOnClickListener(v -> addItemToCart(product));
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
        TextView productName, productPrice, productRating;
        ImageView productImage, addToCartButton;

        public ProductViewHolder(View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.product_name);
            productPrice = itemView.findViewById(R.id.product_price);
            productRating = itemView.findViewById(R.id.product_rating);
            productImage = itemView.findViewById(R.id.product_image);
            addToCartButton = itemView.findViewById(R.id.imgCart);
        }
    }

    private void addItemToCart(Product product) {
        String userID = auth.getCurrentUser().getUid();

        CartItem cartItem = new CartItem(
                product.getProductCode(),
                product.getProductName(),
                product.getCategory(),
                1,  // Quantity, can be changed based on user input
                product.getPrice(),
                product.getDiscountPrice(),
                product.getDiscountCode(),
                product.getDateIn(),
                product.getImageUrl(),
                product.getEdtDescription(),
                product.getRating()
        );

        // Save the cart item to Firebase
        databaseReference.child("Users").child(userID).child("CartItems").push().setValue(cartItem)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(context, "Thêm vào giỏ hàng thành công", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Thêm vào giỏ hàng thất bại", Toast.LENGTH_SHORT).show();
                });
    }
}
