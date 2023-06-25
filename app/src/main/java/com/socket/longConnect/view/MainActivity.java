package com.socket.longConnect.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.socket.longConnect.R;
import com.socket.longConnect.client.view.ClientStartActivity;
import com.socket.longConnect.server.view.ServerStartActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnServer = findViewById(R.id.btnServer);
        btnServer.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ServerStartActivity.class);
            startActivity(intent);
        });

        Button btnClient = findViewById(R.id.btnClient);
        btnClient.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ClientStartActivity.class);
            startActivity(intent);
        });
    }
}