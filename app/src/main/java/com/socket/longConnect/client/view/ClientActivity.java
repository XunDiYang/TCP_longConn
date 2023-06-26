package com.socket.longConnect.client.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.socket.longConnect.R;
import com.socket.longConnect.client.service.NettyClientDemo;

public class ClientActivity extends AppCompatActivity {
    public TextView txtlocalip;
    public TextView txtSndMsg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        txtlocalip = findViewById(R.id.localip);
        txtlocalip.setText(ipPortToString());

        txtSndMsg = findViewById(R.id.sndMsg);
        String sndMsg = txtSndMsg.toString();

        Button btnSndMsg = findViewById(R.id.btnSndMsg);
        btnSndMsg.setOnClickListener(v->{

        });

    }


    public String ipPortToString() {
        return
                "client ip='" + NettyClientDemo.getLocalIp() + '\'' +
                        "\nserver ip='" + NettyClientDemo.getServerIp() + '\'' +
                        ", port=" + NettyClientDemo.getServerPort();
    }
}
