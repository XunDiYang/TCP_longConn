package com.socket.longConnect.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.socket.longConnect.R;
import com.socket.longConnect.utils.NetUtils;

import java.net.SocketException;

public class ClientStartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_start);

        TextView txtlocalip = findViewById(R.id.localip);
        String localip = null;
        try {
            localip = NetUtils.getInnetIp();
            txtlocalip.setText(localip);
        } catch (SocketException e) {
            e.printStackTrace();
        }



        Button btnConnServer = findViewById(R.id.btnConnServer);
        btnConnServer.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClientStartActivity.this,ClientActivity.class);
                startActivity(intent);
            }
        });
    }
}