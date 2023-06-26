package com.socket.longConnect.server.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.socket.longConnect.R;
import com.socket.longConnect.model.CMessage;
import com.socket.longConnect.model.Callback;
import com.socket.longConnect.model.MsgType;
import com.socket.longConnect.server.service.NettyServerDemo;

public class ServerActivity extends AppCompatActivity {

    private String ip = "127.0.0.1";
    private int port = 8888;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);

        NettyServerDemo serverService = NettyServerDemo.getServerService();
        ip = serverService.getIp();
        port = serverService.getPort();
        TextView txtlocalip = findViewById(R.id.localip);
        txtlocalip.setText(toString());

        serverService.connect(connMsgCallback);
    }

    private Callback<CMessage> connMsgCallback = new Callback<CMessage>() {
        @Override
        public void onEvent(int code, String msg, CMessage cMessage) {
            if (code == 100){
                Toast.makeText(ServerActivity.this, "服务器启动成功", Toast.LENGTH_LONG).show();
            }
            else if (code == 200){
                if (cMessage.getType() == MsgType.CONNECT) {
                    Toast.makeText(ServerActivity.this, cMessage.getFrom() + " 连接成功", Toast.LENGTH_LONG).show();
                } else if (cMessage.getType() == MsgType.TEXT && !TextUtils.isEmpty(cMessage.getMsg())) {
                    TextView txtRcvMsg = findViewById(R.id.rcvMsg);
                    txtRcvMsg.setText(txtRcvMsg.getText().toString() + "\n\n收到消息来自于：" + cMessage.getFrom() + "\n消息内容：" + cMessage.getMsg());
                }
                finish();
            }
            else {
                Toast.makeText(ServerActivity.this, "服务器启动失败", Toast.LENGTH_LONG).show();

            }
        }
    };

    @NonNull
    @Override
    public String toString() {
        return
                "ip='" + ip + '\'' +
                        ", port=" + port;
    }
}
