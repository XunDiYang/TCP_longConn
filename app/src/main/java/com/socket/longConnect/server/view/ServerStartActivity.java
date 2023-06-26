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
import com.socket.longConnect.client.view.ClientActivity;
import com.socket.longConnect.client.view.ClientStartActivity;
import com.socket.longConnect.model.CMessage;
import com.socket.longConnect.model.Callback;
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
            if (TextUtils.isEmpty(txtServerPort.getText())) {
                Toast.makeText(ServerStartActivity.this, "请键入正确的端口号", Toast.LENGTH_LONG).show();
            } else {
                int serverPort = Integer.parseInt(txtServerPort.getText().toString());
                startService(new Intent(ServerStartActivity.this, NettyServerDemo.class));
                NettyServerDemo serverService = NettyServerDemo.getServerService();
                serverService.setIp(ip);
                serverService.setPort(serverPort);
                try {
                    serverService.connect(connMsgCallback);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

        });
    }

    private Callback<CMessage> connMsgCallback = new Callback<CMessage>() {
        @Override
        public void onEvent(int code, String msg, CMessage cMessage) {
            if (code == 200){
                Toast.makeText(ServerStartActivity.this, "连接成功", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ServerStartActivity.this, ServerActivity.class);
                startActivity(intent);
                finish();
            }
            else {
                Toast.makeText(ServerStartActivity.this, "连接失败", Toast.LENGTH_SHORT).show();
            }
        }
    };
}
