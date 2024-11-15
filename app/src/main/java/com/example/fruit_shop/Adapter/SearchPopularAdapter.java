package com.example.fruit_shop.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fruit_shop.R;

import java.util.List;

public class SearchPopularAdapter extends RecyclerView.Adapter<SearchPopularAdapter.ViewHolder> {

    private List<String> searchItems;  // Danh sách các mục tìm kiếm phổ biến

    // Constructor nhận vào danh sách dữ liệu
    public SearchPopularAdapter(List<String> searchItems) {
        this.searchItems = searchItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_search_popular, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Gán dữ liệu từ danh sách vào TextView
        holder.textSearchPopular.setText(searchItems.get(position));
    }

    @Override
    public int getItemCount() {
        return searchItems.size();  // Số lượng mục trong danh sách
    }

    // ViewHolder giữ các view trong item
    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView textSearchPopular;

        public ViewHolder(View itemView) {
            super(itemView);
            textSearchPopular = itemView.findViewById(R.id.textSearchPopular);
        }
    }
}
