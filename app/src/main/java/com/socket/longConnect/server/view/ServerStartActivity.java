package com.socket.longConnect.server.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.socket.longConnect.R;
import com.socket.longConnect.server.service.NettyServerDemo;
import com.socket.longConnect.utils.NetUtils;

import java.net.SocketException;

public class ServerStartActivity extends AppCompatActivity {
    NettyServerDemo serverNettyUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_start);

        TextView txtlocalip = findViewById(R.id.localip);
        String localip = null;
        try {
            localip = NetUtils.getInnetIp();
            txtlocalip.setText(localip);
        } catch (SocketException e) {
            e.printStackTrace();
        }

        EditText txtServerPort = findViewById(R.id.serverPort);

        Button btnStartServer = findViewById(R.id.btnStartServer);
        String ip = localip;
        btnStartServer.setOnClickListener(v -> {
//                Intent intent = new Intent(ServerStartActivity.this,ServerActivity.class);
//                startActivity(intent);
            if(TextUtils.isEmpty(txtlocalip.getText())){
                Toast.makeText(ServerStartActivity.this,"请键入正确的ip地址",Toast.LENGTH_LONG).show();
                return;
            }
            int serverPort = Integer.parseInt(txtServerPort.getText().toString());
            Intent intent = new Intent(ServerStartActivity.this,ServerActivity.class);
            intent.putExtra("ip", ip);
            intent.putExtra("port",serverPort);
            startActivity(intent);
        });
    }
}