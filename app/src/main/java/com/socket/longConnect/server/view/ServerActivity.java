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
import com.socket.longConnect.model.MsgType;
import com.socket.longConnect.server.service.NettyServerDemo;

public class ServerActivity extends AppCompatActivity {

    private String ip = "127.0.0.1";
    private int port = 8888;

    private NettyServerDemo.NettyServerBootStrap nettyServerBootStrap;
    ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            nettyServerBootStrap = new NettyServerDemo.NettyServerBootStrap();
            nettyServerBootStrap.connect();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            ip = bundle.getString("ip");
            port = bundle.getInt("port");
        }

        TextView txtlocalip = findViewById(R.id.localip);
        txtlocalip.setText(toString());

        NettyServerDemo.setIp(ip);
        NettyServerDemo.setPort(port);
        NettyServerDemo.setHandler(msgHandler);
        Intent intent = new Intent(this, NettyServerDemo.class);
        bindService(intent,connection, Context.BIND_AUTO_CREATE);
    }

    private Handler msgHandler = new Handler(message -> {
        int msgType = message.getData().getInt("type");
        String clientIp = message.getData().getString("clientIp");
        String rcvMsg = message.getData().getString("msg");
        if (msgType == MsgType.CONNECTING){
            Toast.makeText(ServerActivity.this,clientIp + " 连接成功",Toast.LENGTH_LONG).show();
        }
        else if (msgType == MsgType.TEXT && !TextUtils.isEmpty(rcvMsg)) {
            TextView txtRcvMsg = findViewById(R.id.rcvMsg);
            txtRcvMsg.setText(txtRcvMsg.getText().toString() + "\n\n收到消息来自于：" + clientIp + "\n消息内容："+ rcvMsg);
        }
        return false;
    });

    @NonNull
    @Override
    public String toString() {
        return
                "ip='" + ip + '\'' +
                        ", port=" + port;
    }
}