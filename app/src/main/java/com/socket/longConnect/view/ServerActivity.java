package com.socket.longConnect.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.socket.longConnect.R;
import com.socket.longConnect.utils.NetUtils;

import java.net.SocketException;

public class ServerActivity extends AppCompatActivity {

    private String ip = "127.0.0.1";
    private int port = 8888;

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

         // new NettyServerDemo(port).start();

    }

    @Override
    public String toString() {
        return
                "ip='" + ip + '\'' +
                        ", port=" + port;
    }
}
