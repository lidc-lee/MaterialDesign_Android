package com.ldc.bluetoothsdk.base;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.IntentFilter;

import com.ldc.bluetoothsdk.receiver.BluetoothReceiver;

import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Created by AA on 2016/12/16.
 * 广播监听、蓝牙设备的操作、
 */

public abstract class Bluetooth {
    protected BluetoothAdapter mBluetoothAdapter;
    protected BluetoothReceiver mBluetoothReceiver;
    protected BaseListener mBluetoothListener;
    protected Context mContext;

    /**
     * 注册广播
     */
    protected void registerReceiver(){
        if (mBluetoothListener == null||mContext == null){
            return;
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);

        mBluetoothReceiver = new BluetoothReceiver(mBluetoothListener);
        mContext.registerReceiver(mBluetoothReceiver,filter);

    }

    /**
     * 当前蓝牙设备是否可用
     * @return true if current device's bluetooth is available
     */
    public boolean isAvailable(){
        return mBluetoothAdapter != null;
    }

    /**
     * 蓝牙是否打开
     * @return true id current device's bluetooth is enabled,
     */
    public boolean isEnabled(){
        if (isAvailable()){
            return mBluetoothAdapter.isEnabled();
        }else {
            return false;
        }
    }

    /**
     * 打开蓝牙设备
     * @return true if open bluetooth operation success
     */
    public boolean openBluetooth(){
        if (!isAvailable()){
            return false;
        }
        return mBluetoothAdapter.enable();
    }

    /**
     * 关闭设备蓝牙
     */
    public void closeBluetooth(){
        if (!isAvailable() && !isEnabled()){
            return;
        }
        mBluetoothAdapter.disable();
    }

    /**
     * 获取设备当前的蓝牙状态
     * @return
     */
    public int getBluetoothState() {
        if (!isAvailable()){
            return BluetoothAdapter.STATE_OFF;
        }
        return mBluetoothAdapter.getState();
    }

    /**
     * 设置蓝牙的超时时间
     * @param time the time(unit seconds) of the device's bluetooth can be found
     * @return true if set discoverable operation success
     */
    public boolean setDiscoverable(int time){
        return false;
    }

    /**
     * 开始扫瞄蓝牙设备
     * @return if start scan operation success, return true
     */
    public boolean startScan(){
        return false;
    }

    /**
     * Start scan for found bluetooth device with service filter.
     * @param serviceUUIDs the list of possible UUIDs to search
     * @return if start scan operation success, return true
     */
    public boolean startScanByService(List<UUID> serviceUUIDs){
        return false;
    }

    /**
     * 取消扫描
     * @return
     */
    public boolean cancelScan(){
        return false;
    }

    /**
     * 获取与本机蓝牙所有绑定的远程蓝牙信息
     * @return
     */
    public Set<BluetoothDevice> getBondedDevices(){
        if (!isAvailable() || !isEnabled()){
            throw new RuntimeException("Bluetooth is not avaliable!");
        }
        return mBluetoothAdapter.getBondedDevices();
    }
    /**
     * Find a bluetooth device by MAC address.
     * @param mac the bluetooth MAC address
     * @return a remote bluetooth device
     */
    public BluetoothDevice findDeviceByMac(String mac){
        if (!isAvailable() || !isEnabled()){
            throw new RuntimeException("Bluetooth is not avaliable!");
        }
        return mBluetoothAdapter.getRemoteDevice(mac);
    }

    /**
     * 开启一个服务监听客户端连接
     */
    public void startAsServer(){}

    /**
     * Connected a bluetooth device by MAC address.
     * @param mac
     */
    public void connect(String mac){}

    /**
     * Reconnect a bluetooth device by MAC address when the connection is lost.
     *
     */
    public void reConnect(){}

    /**
     * 断开连接
     */
    public void disconnect(){}

    /**
     * 向连接的设备写入数据
     * @param data
     */
    public void write(String data){}

    /**
     * 获取已连接的蓝牙设备
     * @return
     */
    public BluetoothDevice getConnectedDevice(){
        return null;
    }

    /**
     * 释放资源
     */
    public void release(){
        mBluetoothAdapter = null;
        if(mBluetoothReceiver != null) {
            mContext.unregisterReceiver(mBluetoothReceiver);
        }
    }

    /**
     * 获取连接的状态
     * @return
     */
    public int getConnectionState(){
        return State.STATE_UNKNOWN;
    }
}
