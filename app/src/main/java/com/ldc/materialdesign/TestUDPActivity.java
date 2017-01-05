package com.ldc.materialdesign;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ldc.materialdesign.udp.ISocketResponse;
import com.ldc.materialdesign.udp.UDPClient;
import com.ldc.materialdesign.udp.UDPServer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by AA on 2017/1/4.
 */

public class TestUDPActivity extends Activity {
    EditText msg_et = null;
    Button send_bt = null;
    TextView info_tv = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.udp_activity);
        msg_et = (EditText) findViewById(R.id.msg_et);
        send_bt = (Button) findViewById(R.id.send_bt);
        info_tv = (TextView) findViewById(R.id.info_tv);
        // 开启服务器
        ExecutorService exec = Executors.newCachedThreadPool();
        UDPServer server = new UDPServer();
        exec.execute(server);
        // 发送消息
        send_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        UDPClient client = new UDPClient(new ISocketResponse() {
                            @Override
                            public void onSocketResponse(byte[] responseData) {
                                Log.e("onSocketResponse", new String(responseData));
                            }
                        });
                        Log.e("client",client.send("localhost",6000,msg_et.getText().toString()));
                    }
                }).start();
            }
        });
    }
}
