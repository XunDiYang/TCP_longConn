package com.socket.longConnect.client.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.socket.longConnect.R;
import com.socket.longConnect.client.service.NettyClientDemo;
import com.socket.longConnect.utils.NetUtils;

import java.net.SocketException;

public class ClientStartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_start);

        TextView txtlocalIp = findViewById(R.id.localip);
        String localip = "127.0.0.1";
        try {
            localip = NetUtils.getInnetIp();
            txtlocalIp.setText(localip);
        } catch (SocketException e) {
            e.printStackTrace();
        }

        TextView txtServerIp = findViewById(R.id.server_ip);
        String serverIp = txtServerIp.toString();
        TextView txtServerPort = findViewById(R.id.server_port);
        int serverPort = Integer.parseInt(txtServerPort.toString());

        Button btnConnServer = findViewById(R.id.btnConnServer);
        String finalLocalip = localip;
        btnConnServer.setOnClickListener(v -> {
            NettyClientDemo.setLocalIp(finalLocalip);
            NettyClientDemo.setServerIp(serverIp);
            NettyClientDemo.setServerPort(serverPort);

            Intent intent = new Intent(ClientStartActivity.this,ClientActivity.class);
            startActivity(intent);
        });
    }
}