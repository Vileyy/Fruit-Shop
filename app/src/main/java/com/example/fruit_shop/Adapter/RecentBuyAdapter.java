package com.example.fruit_shop.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fruit_shop.R;

import java.util.ArrayList;

public class RecentBuyAdapter extends RecyclerView.Adapter<RecentBuyAdapter.RecentBuyViewHolder> {
    private ArrayList<String> productNames;
    private ArrayList<Double> productPrices;
    private ArrayList<String> productImages;
    private ArrayList<Integer> productQuantities;
    private Context context;

    public RecentBuyAdapter(ArrayList<String> productNames, ArrayList<Double> productPrices, ArrayList<String> productImages, ArrayList<Integer> productQuantities, Context context) {
        this.productNames = productNames;
        this.productPrices = productPrices;
        this.productImages = productImages;
        this.productQuantities = productQuantities;
        this.context = context;
    }

    @NonNull
    @Override
    public RecentBuyAdapter.RecentBuyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_buy_again, parent, false);
        return new RecentBuyAdapter.RecentBuyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecentBuyAdapter.RecentBuyViewHolder holder, int position) {
        holder.BuyAgainName.setText(productNames.get(position));
        holder.BuyAgainPrice.setText(String.valueOf(productPrices.get(position)));
        //holder.BuyAgainQuantity.setText(String.valueOf(productQuantities.get(position)));

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class RecentBuyViewHolder extends RecyclerView.ViewHolder {

        private TextView BuyAgainName, BuyAgainPrice, BuyAgainQuantity;
        public RecentBuyViewHolder(@NonNull View itemView) {
            super(itemView);
            BuyAgainName = itemView.findViewById(R.id.buyAgainProductName);
            BuyAgainPrice = itemView.findViewById(R.id.buyAgainPrice);
            //BuyAgainQuantity = itemView.findViewById(R.id.buyAgainQuantity);
        }
    }
}
