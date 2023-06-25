package com.socket.longConnect.client.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.socket.longConnect.R;
import com.socket.longConnect.utils.NetUtils;

import java.net.SocketException;

public class ClientActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        TextView txtlocalip = findViewById(R.id.localip);
        String localip;
        try {
            localip = NetUtils.getInnetIp();
            txtlocalip.setText(localip);
        } catch (SocketException e) {
            e.printStackTrace();
        }

    }
}