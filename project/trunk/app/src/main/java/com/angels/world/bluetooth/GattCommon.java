package com.angels.world.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.text.TextUtils;

import com.angels.library.utils.AcLogUtil;
import com.angels.world.constant.BluetoothStateConstant;

import java.lang.reflect.Method;
import java.util.List;
import java.util.UUID;

import static com.angels.world.constant.BluetoothStateConstant.CONNECTING;

/**
 * Created by chencg on 2018/3/1.
 */

public abstract class GattCommon {

    protected BluetoothGatt bluetoothGatt;
    protected BluetoothAdapter bluetoothAdapter;
    protected List<BluetoothGattService> mServiceList;
    protected Context context;
    protected BluetoothDevice device;
    /**
     * 0:未连接
     * 1：连接中
     * 2：已连接
     * 3:绑定成功
     * 4:连接失败、断开连接
     * 5:断开连接
     */
    protected String bnadType = "";

    protected abstract BluetoothGattCallback getBluetoothGattCallback();
    /**启动接收数据*/
    public abstract boolean receive(final UUID serviceUuid, final UUID characteristicUuid);
    /**发送数据*/
    public abstract boolean send(final byte[] data, final UUID serviceUuid, final UUID characteristicUuid);
    /**
     * 全局接口
     */
    public interface GattListener {
        public void state(String state);
        public void receive(byte[] data);
        public void write(byte[] data);
        public void read(byte[] data);
    }
    protected GattListener gattListener;

    public void setGattListener(GattListener gattListener) {
        this.gattListener = gattListener;
    }

    public GattCommon(Context context){
        this.context = context;
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        bnadType = BluetoothStateConstant.CONNECT_NO;
    }

    /***
     * 蓝牙设备的连接
     */
    public void connect(String address) {
        BluetoothDevice device = bluetoothAdapter.getRemoteDevice(address);
        AcLogUtil.i("蓝牙-->bluetoothGatt-->connect-->" + bluetoothGatt + "--" + device.getAddress() + "--" + device.getName());
        bnadType = CONNECTING;
        if (gattListener != null) {
            gattListener.state(bnadType);
        }
        if(device.getName() != null && !TextUtils.isEmpty(device.getName())){
            connect(device);
        }
    }

    public void connect(BluetoothDevice device) {
        refreshDeviceCache();
        if (bluetoothGatt != null) {
            bluetoothGatt.close();
        }

        this.device = device;
        bnadType = CONNECTING;
        if (gattListener != null) {
            gattListener.state(bnadType);
        }
        AcLogUtil.i("蓝牙-->bluetoothGatt-->connect:" + bluetoothGatt + "--" + device.getAddress() + "--" + device.getName());
        bluetoothGatt = device.connectGatt(context, false, getBluetoothGattCallback());
//        bluetoothGatt.connect();
    }

    /**
     * 断开连接
     */
    public void disconnect() {
        bnadType = BluetoothStateConstant.DISCONNECT;
        if (gattListener != null) {
            gattListener.state(bnadType);
        }
        if (bluetoothGatt != null) {
            bluetoothGatt.disconnect();
//            bluetoothGatt.close();
        }
    }

    /**
     * 判断是否连接
     */
    public boolean isConnect() {
//        if (BOUND.equals(bnadType) || CONNECTED.equals(bnadType)) {
        if (BluetoothStateConstant.CONNECTED.equals(bnadType)) {
            return true;
        } else {
            return false;
        }
    }
    /**
     * 判断是否绑定
     */
    public boolean isBound() {
//        if (BOUND.equals(bnadType) || CONNECTED.equals(bnadType)) {
        if (BluetoothStateConstant.BOUND.equals(bnadType)) {
            return true;
        } else {
            return false;
        }
    }

    public void close() {
        disconnect();
        if (bluetoothGatt != null) {
            bluetoothGatt.close();
//            bluetoothGatt = null;
        }
        device = null;
    }


    /**
     * 清理蓝牙缓存
     */
    public boolean refreshDeviceCache() {
        if (bluetoothGatt != null) {
            try {
                Method localMethod = bluetoothGatt.getClass().getMethod(
                        "refresh", new Class[0]);
                if (localMethod != null) {
                    boolean bool = ((Boolean) localMethod.invoke(
                            bluetoothGatt, new Object[0])).booleanValue();
                    return bool;
                }
            } catch (Exception localException) {
                AcLogUtil.i("蓝牙-->An exception occured while refreshing device");
            }
        }
        return false;
    }

    public BluetoothGatt getBluetoothGatt() {
        return bluetoothGatt;
    }

    public List<BluetoothGattService> getmServiceList() {
        return mServiceList;
    }
}
