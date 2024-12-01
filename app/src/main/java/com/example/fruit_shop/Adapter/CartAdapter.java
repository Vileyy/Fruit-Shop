package com.example.fruit_shop.Adapter;

import android.content.Context;
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
import com.example.fruit_shop.R;
import com.example.fruit_shop.Utils.FormatValues;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private Context context;
    private ArrayList<CartItem> cartItemList;
    private int[] itemQuantities;
    private ArrayList<String> uniqueKeys;

    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private String userID;
    private DatabaseReference databaseReference;

    public CartAdapter(Context context, ArrayList<CartItem> cartItemList) {
        this.context = context;
        this.cartItemList = cartItemList;

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        userID = auth.getCurrentUser().getUid();
        databaseReference = database.getReference("Users").child(userID).child("CartItems");
        getUniqueKey();

        itemQuantities = new int[cartItemList.size()];
        for (int i = 0; i < cartItemList.size(); i++) {
            itemQuantities[i] = cartItemList.get(i).getStockQuantity();
        }
    }

    @NonNull
    @Override
    public CartAdapter.CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_list_order, parent, false);
        return new CartAdapter.CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.CartViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return cartItemList.size();
    }

    public ArrayList<Integer> getUpdatedItemsQuantities() {
        ArrayList<Integer> mItemQuantities = new ArrayList<>();
        for (int quantity : itemQuantities) {
            mItemQuantities.add(quantity);
        }
        return mItemQuantities;
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView cartImage;
        TextView cartName, cartPrice, cartQuantity;
        TextView btnMinus, btnPlus;
        ImageView btnDelete;


        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            cartImage = itemView.findViewById(R.id.productAllImage);
            cartName = itemView.findViewById(R.id.cartName);
            cartPrice = itemView.findViewById(R.id.cartPrice);
            cartQuantity = itemView.findViewById(R.id.cartQuantity);
            btnMinus = itemView.findViewById(R.id.btnMinus);
            btnPlus = itemView.findViewById(R.id.btnPlus);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }

        public void bind(int position) {
            CartItem cartItem = cartItemList.get(position);
            cartName.setText(cartItem.getProductName());
            cartPrice.setText(FormatValues.formatMoney(cartItem.getPrice()));
            cartQuantity.setText(String.valueOf(cartItem.getStockQuantity()));

            // Use Glide to load image from drawable resource
            Glide.with(context)
                    .load(cartItem.getImageUrl())
                    .into(cartImage);


            btnMinus.setOnClickListener(view -> {
                if (itemQuantities[position] > 1) {
                    itemQuantities[position]--;
                    cartItemList.get(position).setStockQuantity(itemQuantities[position]);
                    cartQuantity.setText(String.valueOf(itemQuantities[position]));
                }
            });

            btnPlus.setOnClickListener(view -> {
                if (itemQuantities[position] <10){
                    itemQuantities[position]++;
                    cartItemList.get(position).setStockQuantity(itemQuantities[position]);
                    cartQuantity.setText(String.valueOf(itemQuantities[position]));
                }
            });


            btnDelete.setOnClickListener(v -> {
                String uniqueKey = uniqueKeys.get(position);
                if (uniqueKey != null) {
                    // Remove item from Firebase
                    databaseReference.child(uniqueKey).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            cartItemList.remove(position);
                            removeItemAtPosition(0);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, cartItemList.size());
                            Toast.makeText(context, "Xoá thành công", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, "Xoá thất bại", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    }


    private void getUniqueKey() {
        uniqueKeys = new ArrayList<>();

        // Fecth data
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String uniqueKey = null;

                // Get unique key of each item and add to uniqueKeys
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    uniqueKey = dataSnapshot.getKey();
                    uniqueKeys.add(uniqueKey);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        if (cartItemList.isEmpty()) {
            uniqueKeys.clear();
        }
    }

    public void removeItemAtPosition(int position) {
        if (position < 0 || position >= itemQuantities.length) {
            return;
        }

        int[] newItemQuantities = new int[itemQuantities.length - 1];
        for (int i = 0, j = 0; i < itemQuantities.length; i++) {
            if (i != position) {
                newItemQuantities[j++] = itemQuantities[i];
            }
        }

        itemQuantities = newItemQuantities;
    }


}
