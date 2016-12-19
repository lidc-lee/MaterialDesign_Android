package com.ldc.bluetoothsdk.service;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.ldc.bluetoothsdk.base.BaseListener;
import com.ldc.bluetoothsdk.base.BluetoothLEListener;
import com.ldc.bluetoothsdk.base.State;

import java.util.List;
import java.util.UUID;

/**
 * Created by AA on 2016/12/16.
 */

public class BluetoothLEService {

    private BaseListener mBluetoothListener;
    private BluetoothGatt mBluetoothGatt;
    private BluetoothGattCharacteristic mWriteCharacteristic, mNotifyCharacteristic;
    private BluetoothGattCharacteristic mReadCharacteristic;
    private String writeCharacteristicUUID;
    private String readCharacteristicUUID;
    private String SERVICE_UUID;
    private int mState;

    public BluetoothLEService() {
        this.mState = State.STATE_NONE;
    }

    /**
     * 设置 蓝牙监听
     * @param mBluetoothListener
     */
    public void setBluetoothListener(BaseListener mBluetoothListener) {
        this.mBluetoothListener = mBluetoothListener;
    }

    public void setState(int mState) {
        this.mState = mState;
        if (mBluetoothListener != null){
            mBluetoothListener.onBluetoothServiceStateChanged(mState);
        }
    }

    public int getState() {
        return mState;
    }

    public void connect(Context context, BluetoothDevice device){
        setState(State.STATE_CONNECTED);
        mBluetoothGatt = device.connectGatt(context,false,mBTGattCallback);
    }

    public void reConnect(){
        if (mBluetoothGatt != null){
            mBluetoothGatt.connect();
        }
    }

    public void disConnect(){
        if (mBluetoothGatt != null){
            mBluetoothGatt.disconnect();
        }
    }

    public void close(){
        disConnect();
        if (mBluetoothGatt != null){
            mBluetoothGatt.close();
        }
        mBluetoothGatt = null;
    }
    public void write(String data){
        if (mBluetoothGatt != null){
            if (mWriteCharacteristic == null){
                Log.e("write error","该设备下没有找到该特性");
                return;
            }
            mWriteCharacteristic.setValue(data);
            mBluetoothGatt.writeCharacteristic(mWriteCharacteristic);
        }
    }

    public void setReadCharacteristicUUID(String readCharacteristicUUID) {
        this.readCharacteristicUUID = readCharacteristicUUID;
    }

    public void setWriteCharacteristicUUID(String writeCharacteristicUUID) {
        this.writeCharacteristicUUID = writeCharacteristicUUID;
    }

    public void setSERVICE_UUID(String SERVICE_UUID) {
        this.SERVICE_UUID = SERVICE_UUID;
    }

    private BluetoothGattCallback mBTGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            switch (newState){
                case BluetoothProfile.STATE_CONNECTED:
                    setState(State.STATE_CONNECTED);
                    gatt.discoverServices();
                    break;

                case BluetoothProfile.STATE_DISCONNECTED:
                    setState(State.STATE_DISCONNECTED);
                    break;

                default:break;
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                List<BluetoothGattService> services = gatt.getServices();
                if(mBluetoothListener != null){
                    ((BluetoothLEListener)mBluetoothListener).onDiscoveringServices(services);
                }
                for (BluetoothGattService service : services) {

                    Log.e("BluetoothGattService","UUID_services--"+service.getUuid());
                    List<BluetoothGattCharacteristic> characteristics = service.getCharacteristics();
                    for (BluetoothGattCharacteristic characteristic : characteristics) {
                        final int charaProp = characteristic.getProperties();
                        final String charaUUID = characteristic.getUuid().toString();

                        //读
                        if ((charaProp | BluetoothGattCharacteristic.PERMISSION_READ) > 0){
//                            if(readCharacteristicUUID.isEmpty()){
//                                if (mNotifyCharacteristic != null){
//                                    mBluetoothGatt.setCharacteristicNotification(mNotifyCharacteristic, false);
//                                    mNotifyCharacteristic = null;
//                                }
//                                gatt.readCharacteristic(characteristic);
//                            }
                            if (UUID.fromString(readCharacteristicUUID).equals(characteristic.getUuid())) {
                                if (mNotifyCharacteristic != null){
                                    mBluetoothGatt.setCharacteristicNotification(mNotifyCharacteristic,false);
                                    mNotifyCharacteristic = null;
                                }
                                gatt.readCharacteristic(characteristic);
                                Log.d("LMBluetoothSdk", "Assigning read characteristic : " + characteristic.getUuid());
                            }
                        }

                        //接受Characteristic被读的通知,收到蓝牙模块的数据后会触发mOnDataAvailable.onCharacteristicWrite()
                        if ((charaProp | BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
                            if(readCharacteristicUUID.isEmpty()){
                                mNotifyCharacteristic = characteristic;
                                mBluetoothGatt.setCharacteristicNotification(characteristic, true);
                            }else if(charaUUID.equalsIgnoreCase(readCharacteristicUUID)){
                                mNotifyCharacteristic = characteristic;
                                if( mBluetoothGatt.setCharacteristicNotification(characteristic, true) ) {
                                    Log.d("LMBluetoothSdk", "Subscribing to characteristic : " + characteristic.getUuid());
                                }
                            }
                        }

//                        if(writeCharacteristicUUID.isEmpty()){
//                            if (((charaProp & BluetoothGattCharacteristic.PERMISSION_WRITE)
//                                    | (charaProp & BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE)) > 0){
//                                mWriteCharacteristic = characteristic;
//                            }
//                        }else{
                            if (((charaProp & BluetoothGattCharacteristic.PROPERTY_WRITE)
                                    | (charaProp & BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE)) > 0) {
                                if (charaUUID.equalsIgnoreCase(writeCharacteristicUUID)){
                                    Log.e("LMBluetoothSdk","write service" +service);
                                    Log.e("LMBluetoothSdk", "Assigning write characteristic : " + characteristic.getUuid());
                                    mWriteCharacteristic = characteristic;
                                    mBluetoothGatt.setCharacteristicNotification(characteristic, true);
//                                    mWriteCharacteristic.setValue("send data");
//                                    mBluetoothGatt.writeCharacteristic(mWriteCharacteristic);

                            }
                        }
                    }
                    if(mBluetoothListener != null){
                        ((BluetoothLEListener)mBluetoothListener).onDiscoveringCharacteristics(characteristics);
                    }
                }
                setState(State.STATE_GOT_CHARACTERISTICS);
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);
            if (mBluetoothListener != null){
                ((BluetoothLEListener)mBluetoothListener).onReadData(characteristic);
            }
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);
            if (mBluetoothListener != null){
                ((BluetoothLEListener)mBluetoothListener).onWriteData(characteristic);
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
            if (mBluetoothListener != null){
                ((BluetoothLEListener)mBluetoothListener).onDataChanged(characteristic);
            }
        }
    };
}
