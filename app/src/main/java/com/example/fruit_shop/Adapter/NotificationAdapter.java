package com.example.fruit_shop.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fruit_shop.Model.Notification;
import com.example.fruit_shop.R;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private List<Notification> notificationList;

    // Constructor nhận vào danh sách thông báo
    public NotificationAdapter(List<Notification> notificationList) {
        this.notificationList = notificationList;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate layout cho mỗi item trong RecyclerView
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_list_notification, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        Notification notification = notificationList.get(position);
        holder.title.setText(notification.getTitle());
        holder.content.setText(notification.getContent());
        holder.timestamp.setText(notification.getTimestamp());
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public static class NotificationViewHolder extends RecyclerView.ViewHolder {

        TextView title, content, timestamp;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            content = itemView.findViewById(R.id.content);
            timestamp = itemView.findViewById(R.id.time_stamp);
        }
    }
}
