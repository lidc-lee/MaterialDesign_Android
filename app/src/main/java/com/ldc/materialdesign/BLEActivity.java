package com.ldc.materialdesign;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ldc.bluetoothsdk.BluetoothLEController;
import com.ldc.bluetoothsdk.base.BluetoothLEListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by AA on 2016/12/17.
 */

public class BLEActivity extends AppCompatActivity{

    private static final String TAG = "BLEActivity";

    private BluetoothLEController mBLEController;
    private static final String SERVICE_ID = "0000ffe0-0000-1000-8000-00805f9b34fb";
    private static final String READ_CHARACTERISTIC_ID = "0000ffe1-0000-1000-8000-00805f9b34fb";
    private static final String WRITE_CHARACTERISTIC_ID = "0000ffe1-0000-1000-8000-00805f9b34fb";

    private List<String> mList;
    private ArrayAdapter<String> mFoundAdapter;
    private ListView lvDevices;
    private Button btnScan;
    private Button btnDisconnect;
    private Button btnReconnect;
    private Button btnSend;
    private TextView tvConnState;
    private TextView tvContent;
    private EditText etSendContent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ble);
        getSupportActionBar().setTitle("BLE Sample");
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBLEController.release();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    private void init(){
        mBLEController = BluetoothLEController.getInstance().build(this);
        mBLEController.setBluetoothListener(mBluetoothLEListener);
        mList = new ArrayList<String>();
        mFoundAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mList);

        lvDevices = (ListView) findViewById(R.id.lv_ble_devices);
        btnScan = (Button) findViewById(R.id.btn_ble_scan);
        btnDisconnect = (Button) findViewById(R.id.btn_ble_disconnect);
        btnReconnect = (Button) findViewById(R.id.btn_ble_reconnect);
        btnSend = (Button) findViewById(R.id.btn_ble_send);
        tvConnState = (TextView) findViewById(R.id.tv_ble_conn_state);
        tvContent = (TextView) findViewById(R.id.tv_ble_chat_content);
        etSendContent = (EditText) findViewById(R.id.et_ble_send_content);

        lvDevices.setAdapter(mFoundAdapter);

        mBLEController.setServiceID(SERVICE_ID);
        mBLEController.setReadCharacteristicUUID(READ_CHARACTERISTIC_ID);
        mBLEController.setWriteCharacteristicUUID(WRITE_CHARACTERISTIC_ID);
        //扫描
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mList.clear();
                mFoundAdapter.notifyDataSetChanged();

                if( mBLEController.startScan() ){
                    Toast.makeText(BLEActivity.this, "Scanning!", Toast.LENGTH_SHORT).show();
                }

////                You can scan by service using the following code:
//                List<UUID> uuids = new ArrayList<UUID>();
//                uuids.add(UUID.fromString(SERVICE_ID));

//                if( mBLEController.startScanByService(uuids) ){
//                    Toast.makeText(BLEActivity.this, "Scanning!", Toast.LENGTH_SHORT).show();
//                }
            }
        });
        //断开连接
        btnDisconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBLEController.disconnect();
            }
        });
        //重新连接
        btnReconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBLEController.reConnect();
            }
        });
        //发送数据
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = "Hello world";
                if (!TextUtils.isEmpty(msg)) {
                    mBLEController.write(msg);
                    mBLEController.read();
                }
            }
        });
        //点击列表连接
        lvDevices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String itemStr = mList.get(position);
                mBLEController.connect(itemStr.substring(itemStr.length() - 17));
            }
        });
        //是否支持BLE
        if (!mBLEController.isSupportBLE()){
            Toast.makeText(this, "Unsupport BLE!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private BluetoothLEListener mBluetoothLEListener = new BluetoothLEListener() {
        // 读特性
        @Override
        public void onReadData(final BluetoothGattCharacteristic characteristic) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tvContent.append("Read from " + characteristic.getUuid()+"\n");
                }
            });
        }
        //写特性
        @Override
        public void onWriteData(final BluetoothGattCharacteristic characteristic) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tvContent.append("Me" + ": " + parseData(characteristic) + "\n");
                }
            });
        }
        //数据改变
        @Override
        public void onDataChanged(BluetoothGattCharacteristic characteristic) {
            final String dataValue = characteristic.getStringValue(0);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tvContent.append( dataValue );
                }
            });
        }
        //发现特性
        @Override
        public void onDiscoveringCharacteristics(final List<BluetoothGattCharacteristic> characteristics) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    for(BluetoothGattCharacteristic characteristic : characteristics){
                        Log.d(TAG, "onDiscoveringCharacteristics - characteristic : " + characteristic.getUuid());
                    }
                }
            });
        }
        //发现服务
        @Override
        public void onDiscoveringServices(final List<BluetoothGattService> services) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    for(BluetoothGattService service : services){
                        Log.d(TAG, "onDiscoveringServices - service : " + service.getUuid());
                    }

                }
            });

        }
        //状态改变
        @Override
        public void onActionStateChanged(int preState, int state) {
            Log.d(TAG, "onActionStateChanged: " + state);
        }
        //
        @Override
        public void onActionDiscoveryStateChanged(String discoveryState) {
            if (discoveryState.equals(BluetoothAdapter.ACTION_DISCOVERY_STARTED)) {
                Toast.makeText(BLEActivity.this, "scanning!", Toast.LENGTH_SHORT).show();
            } else if (discoveryState.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) {
                Toast.makeText(BLEActivity.this, "scan finished!", Toast.LENGTH_SHORT).show();
            }
        }
        //扫描模式改变
        @Override
        public void onActionScanModeChanged(int preScanMode, int scanMode) {
            Log.d(TAG, "onActionScanModeChanged:  " + scanMode);
        }

        @Override
        public void onBluetoothServiceStateChanged(final int state) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tvConnState.setText("Conn state: " +state);
                }
            });
        }

        @Override
        public void onActionDeviceFound(final BluetoothDevice device, short rssi) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mList.add(device.getName() + "@" + device.getAddress());
                    mFoundAdapter.notifyDataSetChanged();
                }
            });
        }
    };

    private String parseData(BluetoothGattCharacteristic characteristic){

        String result = characteristic.getStringValue(0);
        return result;
    }
}
