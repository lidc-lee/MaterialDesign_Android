package com.ldc.bluetoothsdk.receiver;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.ldc.bluetoothsdk.base.BaseListener;

/**
 * Created by AA on 2016/12/16.
 * 蓝牙广播接收类
 */

public class BluetoothReceiver extends BroadcastReceiver {

    private BaseListener mBluetoothListener;

    public BluetoothReceiver(BaseListener listener) {
        this.mBluetoothListener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null || mBluetoothListener == null){
            return;
        }
        String action = intent.getAction();
        switch (action){
            //蓝牙状态值发生改变
            case BluetoothAdapter.ACTION_STATE_CHANGED:
                int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0);
                int preState = intent.getIntExtra(BluetoothAdapter.EXTRA_PREVIOUS_STATE, 0);
                mBluetoothListener.onActionStateChanged(preState, state);
                break;
            //蓝牙扫描过程开始
            case BluetoothAdapter.ACTION_DISCOVERY_STARTED:
                mBluetoothListener.onActionDiscoveryStateChanged(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
                break;
            //蓝牙扫描过程结束
            case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
                mBluetoothListener.onActionDiscoveryStateChanged(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
                break;
            //该常量字段位于BluetoothDevice类中
            //说明：蓝牙扫描时，扫描到任一远程蓝牙设备时，会发送此广播。
            case BluetoothDevice.ACTION_FOUND:
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                short rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, (short) 0);
                mBluetoothListener.onActionDeviceFound(device, rssi);
                break;
            //蓝牙扫描状态(SCAN_MODE)发生改变
            case BluetoothAdapter.ACTION_SCAN_MODE_CHANGED:
                int scanMode = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, 0);
                int preScanMode = intent.getIntExtra(BluetoothAdapter.EXTRA_PREVIOUS_SCAN_MODE, 0);
                mBluetoothListener.onActionScanModeChanged(preScanMode, scanMode);
                break;
        }
    }
}
