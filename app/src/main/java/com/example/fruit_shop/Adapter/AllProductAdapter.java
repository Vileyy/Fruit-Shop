package com.example.fruit_shop.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fruit_shop.Model.CartItem;
import com.example.fruit_shop.Model.Product;
import com.example.fruit_shop.R;
import com.example.fruit_shop.Utils.FormatValues;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AllProductAdapter extends RecyclerView.Adapter<AllProductAdapter.AllProductViewHolder> {
    private Context context;
    private ArrayList<Product> productList;
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;

    public AllProductAdapter(Context context, ArrayList<Product> productList) {
        this.context = context;
        this.productList = productList;
        this.databaseReference = FirebaseDatabase.getInstance().getReference();
        this.auth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public AllProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.all_product_item, parent, false);
        return new AllProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllProductAdapter.AllProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.allProductName.setText(product.getProductName());
        holder.allProductRating.setText(product.getRating());
        holder.allProductPrice.setText(FormatValues.formatMoney(product.getPrice()));


        Glide.with(context)
                .load(product.getImageUrl())
                .into(holder.allProductImage);

        holder.addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItemToCart(product);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class AllProductViewHolder extends RecyclerView.ViewHolder {

        private TextView allProductName, allProductRating, allProductPrice;
        private ImageView allProductImage;
        private Button addToCartButton;
        public AllProductViewHolder(@NonNull View itemView) {
            super(itemView);
            allProductName = itemView.findViewById(R.id.AllProductName);
            allProductRating = itemView.findViewById(R.id.AllProductRating);
            allProductPrice = itemView.findViewById(R.id.AllProductPrice);
            allProductImage = itemView.findViewById(R.id.AllProductImage);
            addToCartButton = itemView.findViewById(R.id.btnAddToCart);
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
