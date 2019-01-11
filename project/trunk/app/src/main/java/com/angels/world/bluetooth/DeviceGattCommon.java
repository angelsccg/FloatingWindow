package com.angels.world.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.Context;

import com.angels.library.utils.AcLogUtil;
import com.angels.library.utils.AcHexStringUtils;
import com.angels.world.bluetooth.drive.DeviceCommandConstant;
import com.angels.world.constant.BluetoothStateConstant;

import java.util.UUID;

/**
 * Created by chencg on 2018/1/10.
 */

public class DeviceGattCommon extends GattCommon {


    public DeviceGattCommon(Context context) {
        super(context);
        initData();
    }

    private void initData(){

    }

    @Override
    public void connect(BluetoothDevice device) {
        AcLogUtil.i("蓝牙-->bluetoothGatt-->2333-->"+device.getAddress()+"--"+device.getName());
        super.connect(device);
    }

    @Override
    protected BluetoothGattCallback getBluetoothGattCallback() {
        return new BluetoothGattCallback() {
            @Override
            public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
                super.onConnectionStateChange(gatt, status, newState);
                //status 表示相应的连接或断开操作是否完成，而不是指连接状态
                AcLogUtil.i("蓝牙-->gatt-->onConnectionStateChange-->"+status+"--"+newState);
                if (BluetoothGatt.GATT_SUCCESS == status) {
                    AcLogUtil.i("蓝牙-->onConnectionStateChange:status ---> success");
                } else {
                    AcLogUtil.i("蓝牙-->onConnectionStateChange:status ---> failed");
                    bnadType = BluetoothStateConstant.CONNECTION_BREAK;
                    if(gattListener != null){
                        gattListener.state(bnadType);
                    }
                    close(); // 防止出现status 133
                    return;
                }
//                gatt.discoverServices();
                if (newState == BluetoothProfile.STATE_CONNECTED) {//连接着
                    AcLogUtil.i("蓝牙-->onConnectionStateChange:newState ---> 连接着");
                    gatt.discoverServices();
                    bnadType = BluetoothStateConstant.CONNECTED;
                    if(gattListener != null){
                        gattListener.state(bnadType);
                    }
                } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {//断开
                    AcLogUtil.i("蓝牙-->onConnectionStateChange:newState ---> 断开");
                    bnadType = BluetoothStateConstant.CONNECTION_BREAK;
                    if(gattListener != null){
                        gattListener.state(bnadType);
                    }
                    AcLogUtil.i("蓝牙-->onConnectionStateChange:newState ---> close");
                    close(); // 防止出现status 133
//                gatt.connect();
//                connect(address);
//                ACLogUtil.i("蓝牙-->onConnectionStateChange:newState ---> 重新开始连接");
                }else{
                    close(); // 防止出现status 133
                }
            }

            @Override
            public void onServicesDiscovered(BluetoothGatt gatt, int status) {
                super.onServicesDiscovered(gatt, status);
                AcLogUtil.i("蓝牙-->onServicesDiscovered status" + status);
                mServiceList = gatt.getServices();
            }
            @Override
            public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                //读取到值，在这里读数据
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    byte[] data = characteristic.getValue();
                    gattListener.read(data);
                    AcLogUtil.i( "蓝牙-->读取结果onCharacteristicRead UUID : " + status + "--" + characteristic.getUuid()+"----"+ AcHexStringUtils.bcd2Str16(data) );
                }
            }

            @Override
            public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                super.onCharacteristicWrite(gatt, characteristic, status);
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    byte[] data = characteristic.getValue();
                    gattListener.write(data);
                    AcLogUtil.i("蓝牙-->onCharacteristicWrite写入结果 uuid:"+characteristic.getUuid() + "---" + status + "---" + AcHexStringUtils.bcd2Str16(data));
                }
            }

            @Override
            public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
                super.onCharacteristicChanged(gatt, characteristic);
                byte[] data = characteristic.getValue();
                AcLogUtil.i("蓝牙-->onCharacteristicChanged 变化了 uuid:"+characteristic.getUuid() +"#####"+ AcHexStringUtils.bcd2Str16(data));
                BluetoothGattService service = characteristic.getService();
                if(gattListener != null){
                    gattListener.receive(data);
                }
            }

            @Override
            public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
                super.onDescriptorRead(gatt, descriptor, status);
                byte[] data = descriptor.getValue();
                AcLogUtil.i("蓝牙-->onDescriptorRead 读取 uuid:"+ AcHexStringUtils.bcd2Str16(data) +"----"+status);
            }

            @Override
            public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
                super.onDescriptorWrite(gatt, descriptor, status);
                byte[] data = descriptor.getValue();
                AcLogUtil.i("蓝牙-->onDescriptorWrite 写入:"+ AcHexStringUtils.bcd2Str16(data) +"----"+status);
                BluetoothGattCharacteristic characteristic = descriptor.getCharacteristic();
                BluetoothGattService service = descriptor.getCharacteristic().getService();
            }

            @Override
            public void onReliableWriteCompleted(BluetoothGatt gatt, int status) {
                super.onReliableWriteCompleted(gatt, status);
                AcLogUtil.i("蓝牙-->onReliableWriteCompleted" +"----"+status);

            }

            @Override
            public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
                super.onReadRemoteRssi(gatt, rssi, status);
                AcLogUtil.i("蓝牙-->onReadRemoteRssi" +"----"+status + "---"+rssi);
            }

            @Override
            public void onMtuChanged(BluetoothGatt gatt, int mtu, int status) {
                super.onMtuChanged(gatt, mtu, status);
                AcLogUtil.i("蓝牙-->onMtuChanged" +"----"+status + "---"+mtu);
            }
        };
    }


    /**===================================交互===================================*/
    /**启动接收数据*/
    public boolean receive(final UUID serviceUuid, final UUID characteristicUuid){
        AcLogUtil.i("蓝牙-->receive-->启动接收数据");
        BluetoothGattService service01 = bluetoothGatt.getService(serviceUuid);
        BluetoothGattCharacteristic characteristic01 = service01.getCharacteristic(characteristicUuid);
        BluetoothGattDescriptor dp02 = characteristic01.getDescriptor(UUID.fromString(DeviceCommandConstant.DESCRIPTOR));
        boolean notification01 = bluetoothGatt.setCharacteristicNotification(characteristic01, true);
        AcLogUtil.i("蓝牙-->receive-->通知 notification01=" + notification01 );
        dp02.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
        boolean resBd02 = bluetoothGatt.writeDescriptor(dp02);
        AcLogUtil.i("蓝牙-->receive-->descriptors resBd02: " + resBd02  + " ,Value=" + AcHexStringUtils.bcd2Str16(dp02.getValue()) + ",UUID:" + dp02.getUuid());
        return resBd02;
    }
    /**发送数据*/
    public boolean send(final byte[] data, final UUID serviceUuid, final UUID characteristicUuid){
        AcLogUtil.i("蓝牙-->send-->发送数据");
        BluetoothGattService service01 = bluetoothGatt.getService(serviceUuid);
        BluetoothGattCharacteristic characteristicHeart = service01.getCharacteristic(characteristicUuid);
        characteristicHeart.setValue(data);
        boolean res1 = bluetoothGatt.writeCharacteristic(characteristicHeart);//开始写入
        AcLogUtil.i("蓝牙-->send-->开始发送数据 res1=" + res1 + ",uuid:" + characteristicHeart.getUuid() + "--" + AcHexStringUtils.bcd2Str16(characteristicHeart.getValue()));
        return res1;
    }

}
