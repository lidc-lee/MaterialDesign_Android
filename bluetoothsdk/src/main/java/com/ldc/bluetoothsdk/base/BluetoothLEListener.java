package com.ldc.bluetoothsdk.base;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;

import java.util.List;

/**
 * Created by AA on 2016/12/16.
 */

public interface BluetoothLEListener extends BaseListener{
    /**
     * Read data from BLE device.
     * @param characteristic the characteristic
     */
    void onReadData(BluetoothGattCharacteristic characteristic);

    /**
     * When write data to remote BLE device, the notification will send to here.
     * @param characteristic the characteristic
     */
    void onWriteData(BluetoothGattCharacteristic characteristic);

    /**
     * When data changed, the notification will send to here.
     * @param characteristic the characteristic
     */
    void onDataChanged(BluetoothGattCharacteristic characteristic);

    /**
     * When a service is discovered, send the list of characteristics
     * @param characteristics the list of characteristics
     */
    void onDiscoveringCharacteristics(List<BluetoothGattCharacteristic> characteristics);


    /**
     * When a device is connected, send the list of services
     * @param services the list of services
     */
    void onDiscoveringServices(List<BluetoothGattService> services);
}
