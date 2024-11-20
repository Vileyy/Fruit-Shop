package com.example.fruit_shop.User;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fruit_shop.Adapter.NotificationAdapter;
import com.example.fruit_shop.Model.Notification;
import com.example.fruit_shop.R;

import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {

    private ImageView imgBack;
    private RecyclerView notificationRecyclerView;
    private NotificationAdapter notificationAdapter;
    private List<Notification> notificationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        // Ánh xạ các thành phần
        imgBack = findViewById(R.id.imgBack);
        notificationRecyclerView = findViewById(R.id.recyclerViewNotification);

        // Xử lý sự kiện khi nhấn nút Back
        imgBack.setOnClickListener(v -> onBackPressed());

        // Khởi tạo RecyclerView
        notificationRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Tạo danh sách thông báo mẫu
        notificationList = new ArrayList<>();
        notificationList.add(new Notification("New Update", "A new update is available.", "January 1, 2024"));
        notificationList.add(new Notification("Reminder", "Don't forget the meeting at 10AM.", "January 2, 2024"));

        // Gắn adapter vào RecyclerView
        notificationAdapter = new NotificationAdapter(notificationList);
        notificationRecyclerView.setAdapter(notificationAdapter);
    }
}
