package com.ldc.bluetoothsdk;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.ParcelUuid;

import com.ldc.bluetoothsdk.base.Bluetooth;
import com.ldc.bluetoothsdk.base.BluetoothLEListener;
import com.ldc.bluetoothsdk.base.State;
import com.ldc.bluetoothsdk.service.BluetoothLEService;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Created by AA on 2016/12/16.
 */

public class BluetoothLEController extends Bluetooth{
    private BluetoothLeScanner BLEScanner;
    private BluetoothLEService BLEService;
    private BluetoothDevice BLEDevice;
    private ScanSettings scanSettings;
    private List<ScanFilter> BLEFilters;
    private Handler handler;

    private int mScanTime = 120000;  //120s
    private static BluetoothLEController leController;

    public static BluetoothLEController getInstance(){
        if (leController == null){
            synchronized (BluetoothLEController.class){
                if (leController == null){
                    leController = new BluetoothLEController();
                }
            }
        }
        return leController;
    }

    public BluetoothLEController build(Context context){
        mContext = context;
        handler = new Handler();

        //1. 获取BluetoothAdapter
        BluetoothManager bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        //
        BLEService = new BluetoothLEService();
        return this;
    }

    /**
     * Set bluetooth listener, you can check all bluetooth status and read data with this listener's callback.
     * @param listener
     */
    public void setBluetoothListener(BluetoothLEListener listener){
        this.mBluetoothListener = listener;
//        registerReceiver();
        if (BLEService != null) {
            BLEService.setBluetoothListener(mBluetoothListener);
        }
    }

    /**
     * 是否支持BLE
     * @return
     */
    public boolean isSupportBLE(){
        return mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE);

    }

    public void setReadCharacteristicUUID(String readCharacteristicUUID) {
        BLEService.setReadCharacteristicUUID(readCharacteristicUUID);
    }

    public void setWriteCharacteristicUUID(String writeCharacteristicUUID) {
        BLEService.setWriteCharacteristicUUID(writeCharacteristicUUID);
    }

    public void setServiceID(String SERVICE_UUID) {
        BLEService.setServiceID(SERVICE_UUID);
    }


    //region 重写Bluetooth方法
    @Override
    protected void registerReceiver() {
        super.registerReceiver();
    }

    @Override
    public boolean isAvailable() {
        return super.isAvailable();
    }

    //
    @Override
    public boolean isEnabled() {
        return super.isEnabled();
    }

    @Override
    public boolean openBluetooth() {
        return super.openBluetooth();
    }

    @Override
    public void closeBluetooth() {
        super.closeBluetooth();
    }

    @Override
    public int getBluetoothState() {
        return super.getBluetoothState();
    }

    @Override
    public boolean setDiscoverable(int time) {
        return super.setDiscoverable(time);
    }

    @Override
    public boolean startScan() {
        if (!isAvailable() && !isEnabled()){
            return false;
        }
        //sdk版本大于android-21
        if (Build.VERSION.SDK_INT >= 21){
            BLEScanner = mBluetoothAdapter.getBluetoothLeScanner();
            scanSettings = new ScanSettings.Builder()
                    .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                    .build();
            BLEFilters = new ArrayList<>();
        }
        //扫描设备
        scanLeDevice();
        return true;
    }

    //通过UUID服务搜索设备
    @Override
    public boolean startScanByService(List<UUID> serviceUUIDs) {
        if (!isAvailable() && !isEnabled()){
            return false;
        }
        if (Build.VERSION.SDK_INT >= 21){
            BLEScanner = mBluetoothAdapter.getBluetoothLeScanner();
            scanSettings = new ScanSettings.Builder()
                    .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                    .build();
            BLEFilters = scanFilters(serviceUUIDs);
        }
        scanLeDevice();
        return true;
    }



    @Override
    public boolean cancelScan() {
        if (!isAvailable() && !isEnabled()){
            return false;
        }
        if (Build.VERSION.SDK_INT < 21) {
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }else {
            BLEScanner.stopScan(mCbtScanCallback);
        }
        if (mBluetoothListener != null){
            mBluetoothListener.onActionDiscoveryStateChanged(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        }
        return true;
    }
    //获取已配对的蓝牙设备
    @Override
    public Set<BluetoothDevice> getBondedDevices() {
        return super.getBondedDevices();
    }

    @Override
    public BluetoothDevice findDeviceByMac(String mac) {
        return super.findDeviceByMac(mac);
    }

    @Override
    public void startAsServer() {
        super.startAsServer();
    }

    @Override
    public void connect(String mac) {
        if (BLEService != null) {
            BLEDevice = mBluetoothAdapter.getRemoteDevice(mac);
            BLEService.connect(mContext, BLEDevice);
        }
    }

    @Override
    public void reConnect() {
        if (BLEService != null) {
            BLEService.reConnect();
        }
    }

    @Override
    public void disconnect() {
        if (BLEService != null) {
            BLEService.disConnect();
        }
    }

    @Override
    public void write(String data) {
        if (BLEService != null){
            BLEService.write(data);
        }
    }

    @Override
    public void read() {
        if (BLEService != null){
            BLEService.read();
        }
    }

    @Override
    public BluetoothDevice getConnectedDevice() {
        return BLEDevice;
    }

    @Override
    public void release() {
        BLEService.close();
        BLEService = null;
        super.release();
    }

    @Override
    public int getConnectionState() {
        if (BLEService != null){
            return BLEService.getState();
        }
        return State.STATE_UNKNOWN;

    }
    //endregion

    ////////////////////////////////////////////////////////////////////////////////


    public int getScanTime() {
        return mScanTime;
    }

    public void setScanTime(int mScanTime) {
        this.mScanTime = mScanTime;
    }


    //region private
    private void scanLeDevice(){
        //超时取消扫描
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                cancelScan();
            }
        },mScanTime);
        if (mBluetoothListener != null){
            mBluetoothListener.onActionDiscoveryStateChanged(BluetoothAdapter.ACTION_DISCOVERY_STARTED);

        }
        if (Build.VERSION.SDK_INT < 21){
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        }else {
            if (mCbtScanCallback == null){
                mCbtScanCallback = new CBTScanCallback();
            }
            BLEScanner.startScan(BLEFilters,scanSettings,mCbtScanCallback);
        }
    }
    private List<ScanFilter> scanFilters(List<UUID> serviceUUIDs) {
        List<ScanFilter> list = new ArrayList<>();
        for (UUID uuid : serviceUUIDs){
            ScanFilter filter = new ScanFilter.Builder()
                    .setServiceUuid(ParcelUuid.fromString(uuid.toString()))
                    .build();
            list.add(filter);
        }
        return list;
    }
    private CBTScanCallback mCbtScanCallback;

    private class CBTScanCallback extends ScanCallback {

        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            if (mBluetoothListener != null)
            {
                mBluetoothListener.onActionDeviceFound(result.getDevice(), (short)result.getRssi());
            }
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            super.onBatchScanResults(results);
        }

        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
        }
    }

    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
                    if (mBluetoothListener != null) {
                        mBluetoothListener.onActionDeviceFound(device, (short)rssi);
                    }
                }

            };

    //endregion

}
