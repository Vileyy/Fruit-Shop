package com.example.fruit_shop.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fruit_shop.R;
import com.example.fruit_shop.Utils.FormatValues;

import java.util.ArrayList;

public class BuyAgainAdapter extends RecyclerView.Adapter<BuyAgainAdapter.ViewHolder>{

    private ArrayList<String> productNames;
    private ArrayList<String> productImages;
    private ArrayList<Double> productPrices;
    private Context context;

    public BuyAgainAdapter(ArrayList<String> productNames, ArrayList<Double> productPrices, ArrayList<String> productImages, Context context) {
        this.productNames = productNames;
        this.productPrices = productPrices;
        this.productImages = productImages;
        this.context = context;
    }
    @NonNull
    @Override
    public BuyAgainAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_buy_again, parent, false);
        return new BuyAgainAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BuyAgainAdapter.ViewHolder holder, int position) {
        holder.BuyAgainName.setText(productNames.get(position));
        holder.BuyAgainPrice.setText(FormatValues.formatMoney(productPrices.get(position)));

        // Load image using Glide
        Glide.with(context).load(productImages.get(position)).into(holder.BuyAgainImage);
    }

    @Override
    public int getItemCount() {
        return productNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView BuyAgainName, BuyAgainPrice;
        private ImageView BuyAgainImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            BuyAgainName = itemView.findViewById(R.id.buyAgainProductName);
            BuyAgainPrice = itemView.findViewById(R.id.buyAgainPrice);
            BuyAgainImage = itemView.findViewById(R.id.buyAgainImage);
        }
    }
}
