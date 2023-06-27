package com.socket.longConnect.client.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.socket.longConnect.R;
import com.socket.longConnect.client.service.NettyClientDemo;
import com.socket.longConnect.model.CMessage;
import com.socket.longConnect.model.Callback;
import com.socket.longConnect.model.MsgType;
import com.socket.longConnect.utils.NetUtils;

import java.net.SocketException;

public class ClientStartActivity extends AppCompatActivity {
    public TextView txtServerIp;
    public TextView txtServerPort;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_start);

        startService(new Intent(this, NettyClientDemo.class));
        NettyClientDemo clientService = NettyClientDemo.getClientService();

        TextView txtlocalIp = findViewById(R.id.localip);
        String localip = "127.0.0.1";
        try {
            localip = NetUtils.getInnetIp();
            txtlocalIp.setText(localip);
        } catch (SocketException e) {
            e.printStackTrace();
        }

        Button btnConnServer = findViewById(R.id.btnConnServer);
        String finalLocalip = localip;
        btnConnServer.setOnClickListener(v -> {
            txtServerIp = findViewById(R.id.server_ip);
            txtServerPort = findViewById(R.id.server_port);

            if (TextUtils.isEmpty(txtServerIp.getText()) || TextUtils.isEmpty(txtServerPort.getText())){
                clientService.closeChannel();
                Toast.makeText(ClientStartActivity.this, "请键入正确的Ip和端口号", Toast.LENGTH_LONG).show();
            }else{
                String serverIp = txtServerIp.getText().toString();
                int serverPort = Integer.parseInt(txtServerPort.getText().toString());

                clientService.setLocalIp(finalLocalip);
                clientService.setServerIp(serverIp);
                clientService.setServerPort(serverPort);

                CMessage connMsg = new CMessage(
                        clientService.getLocalIp(),
                        clientService.getServerIp(),
                        200,
                        MsgType.CONNECT,
                        "connecting");
                try {
                    clientService.sendMsg(connMsg,connMsgCallback);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private Callback<Void> connMsgCallback = new Callback<Void>() {
        @Override
        public void onEvent(String from, int code, int type, String msg, Void unused) {
            if (code == 200){
                Toast.makeText(ClientStartActivity.this, "连接成功", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ClientStartActivity.this,ClientActivity.class);
                startActivity(intent);
                finish();
            }
            else {
                Toast.makeText(ClientStartActivity.this, "连接失败", Toast.LENGTH_SHORT).show();
            }
        }

    };
}
