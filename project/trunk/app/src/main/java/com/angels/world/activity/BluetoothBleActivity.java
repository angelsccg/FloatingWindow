package com.angels.world.activity;

import android.app.Dialog;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.angels.library.utils.AcToastUtil;
import com.angels.library.utils.HexStringUtils;
import com.angels.library.widget.AcCustomDialog;
import com.angels.library.widget.AcCustomTitleLayout;
import com.angels.world.R;
import com.angels.world.bluetooth.DeviceGattCommon;
import com.angels.world.bluetooth.GattCommon;
import com.angels.world.constant.BluetoothStateConstant;
import com.angels.world.constant.KeyConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 蓝牙ble
 * */
public class BluetoothBleActivity extends BaseActivity implements View.OnClickListener{

    /**=============测试==================*/
    public static String BLOOD_SERVICE_01 = "0000fff0-0000-1000-8000-00805f9b34fb";
    /**接收CHARACTER*/
    public static String BLOOD_CHARACTER_01 = "0000fff1-0000-1000-8000-00805f9b34fb";
    /**发送CHARACTER*/
    public static String BLOOD_CHARACTER_02 = "0000fff2-0000-1000-8000-00805f9b34fb";
    /**开始指令*/
    public static String START = "-3, -3, -6, 5, 13, 10";




    private static final int STATE = 1;
    private static final int DATA = 2;
    private static final int WRITE = 3;
    private static final int READ = 4;

    private EditText etReceiveService,etReceiveCharacteristic,etSendService,etSendCharacteristic,etSendData;
    private Button btnReceive,btnSend,btnUuid;
    private TextView tvData,tvState,tvWrite,tvRead;
    private ImageView ivReceiveServiceMore,ivReceiveCharacteristicMore,ivSendServiceMore,ivSendCharacteristicMore;
    /**蓝牙设备*/
    private BluetoothDevice device;
    /**蓝牙协议*/
    private GattCommon gattCommon;
    /**标题*/
    private AcCustomTitleLayout titleLayout;
    /**0:  1:  2:  3:*/
    private int etType;

    /**连接状态*/
    private String state;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case STATE:
                    String state = (String) msg.obj;
                    tvState.setText(state);
                    BluetoothBleActivity.this.state = state;
                    break;
                case DATA:{
                    byte[] data = (byte[]) msg.obj;
                    String dataStr = HexStringUtils.bcd2Str16(data);
                    tvData.setText(tvData.getText() + "\n" + dataStr);
                    break;
                }
                case WRITE:{
                    byte[] data = (byte[]) msg.obj;
                    String dataStr = HexStringUtils.bcd2Str16(data);
                    tvWrite.setText(tvWrite.getText() + "\n" + dataStr);
                    break;
                }
                case READ:{
                    byte[] data = (byte[]) msg.obj;
                    String dataStr = HexStringUtils.bcd2Str16(data);
                    tvRead.setText(tvRead.getText() + "\n" + dataStr);
                    break;
                }
            }
        }
    };

    private View.OnClickListener gattListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (etType){
                case 0:
                    etReceiveService.setText(gattCommon.getmServiceList().get(view.getId()).getUuid().toString());
                    break;
                case 1:{
                    UUID uuid = UUID.fromString(etReceiveService.getText().toString());
                    etReceiveCharacteristic.setText(gattCommon.getBluetoothGatt().getService(uuid).getCharacteristics().get(view.getId()).getUuid().toString());
                    break;
                }
                case 2:
                    etSendService.setText(gattCommon.getmServiceList().get(view.getId()).getUuid().toString());
                    break;
                case 3:{
                    UUID uuid = UUID.fromString(etSendService.getText().toString());
                    etSendCharacteristic.setText(gattCommon.getBluetoothGatt().getService(uuid).getCharacteristics().get(view.getId()).getUuid().toString());
                    break;
                }
            }
            Dialog dialog = (Dialog) view.getTag();
            dialog.dismiss();
        }
    };
    /**
     * 设置触摸事件，由于EditView与TextView都处于ScollView中，
     * 所以需要在OnTouch事件中通知父控件不拦截子控件事件
     */
    private View.OnTouchListener touchListener = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if(event.getAction() == MotionEvent.ACTION_DOWN
                    || event.getAction() == MotionEvent.ACTION_MOVE){
                //按下或滑动时请求父节点不拦截子节点
                v.getParent().requestDisallowInterceptTouchEvent(true);
            }
            if(event.getAction() == MotionEvent.ACTION_UP){
                //抬起时请求父节点拦截子节点
                v.getParent().requestDisallowInterceptTouchEvent(false);
            }
            return false;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_ble);

        device = getIntent().getParcelableExtra(KeyConfig.KEY_DEVICE);

        initView();
        initTitleView();
        initData();


//        etReceiveService.setText(BLOOD_SERVICE_01);
//        etSendService.setText(BLOOD_SERVICE_01);
//        etReceiveCharacteristic.setText(BLOOD_CHARACTER_01);
//        etSendCharacteristic.setText(BLOOD_CHARACTER_02);
        etSendData.setText(START);
    }

    private void initView() {
        tvState = findViewById(R.id.tv_state);
        etReceiveService = findViewById(R.id.et_receive_service);
        etReceiveCharacteristic = findViewById(R.id.et_receive_characteristic);
        etSendService = findViewById(R.id.et_send_service);
        etSendCharacteristic = findViewById(R.id.et_send_characteristic);
        btnReceive = findViewById(R.id.btn_receive);
        btnSend = findViewById(R.id.btn_send);
        tvData = findViewById(R.id.tv_data);
        etSendData = findViewById(R.id.et_send_data);
        btnUuid = findViewById(R.id.btn_uuid);
        tvWrite = findViewById(R.id.tv_write);
        tvRead = findViewById(R.id.tv_read);

        ivReceiveServiceMore = findViewById(R.id.iv_receive_service_more);
        ivReceiveCharacteristicMore = findViewById(R.id.iv_receive_characteristic_more);
        ivSendServiceMore = findViewById(R.id.iv_send_service_more);
        ivSendCharacteristicMore = findViewById(R.id.iv_send_characteristic_more);

        tvState.setOnClickListener(this);
        btnReceive.setOnClickListener(this);
        btnSend.setOnClickListener(this);
        btnUuid.setOnClickListener(this);
        ivReceiveServiceMore.setOnClickListener(this);
        ivReceiveCharacteristicMore.setOnClickListener(this);
        ivSendServiceMore.setOnClickListener(this);
        ivSendCharacteristicMore.setOnClickListener(this);

        tvData.setMovementMethod(ScrollingMovementMethod.getInstance());
        tvData.setOnTouchListener(touchListener);
        tvWrite.setMovementMethod(ScrollingMovementMethod.getInstance());
        tvWrite.setOnTouchListener(touchListener);
        tvRead.setMovementMethod(ScrollingMovementMethod.getInstance());
        tvRead.setOnTouchListener(touchListener);
    }

    private void initTitleView() {
        titleLayout = findViewById(R.id.rl_title);
        titleLayout.setTitle(device.getName()+"(" + device.getAddress() + ")");
        titleLayout.setIvLeft(R.drawable.ic_back_white, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleLayout.setTvRight("清空", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvData.setText("");
            }
        });
    }

    private void initData() {
        gattCommon = new DeviceGattCommon(this);
        gattCommon.setGattListener(new GattCommon.GattListener() {
            @Override
            public void state(String state) {
                Message message = new Message();
                message.what = STATE;
                message.obj = state;
                handler.handleMessage(message);
            }

            @Override
            public void receive(byte[] data) {
                Message message = new Message();
                message.what = DATA;
                message.obj = data;
                handler.handleMessage(message);
            }

            @Override
            public void write(byte[] data) {
                Message message = new Message();
                message.what = WRITE;
                message.obj = data;
                handler.handleMessage(message);
            }

            @Override
            public void read(byte[] data) {
                Message message = new Message();
                message.what = READ;
                message.obj = data;
                handler.handleMessage(message);
            }
        });
        gattCommon.connect(device);
    }

    /**连接*/
    private void connect(){

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_receive:
                if(gattCommon.isConnect()){
                    String receiveService = etReceiveService.getText().toString();
                    String receiveCharacteristic = etReceiveCharacteristic.getText().toString();
                    if(!TextUtils.isEmpty(receiveService) && !TextUtils.isEmpty(receiveCharacteristic)){
                        UUID serviceUUID = UUID.fromString(receiveService);
                        UUID characteristicUUID = UUID.fromString(receiveCharacteristic);
                        boolean success = gattCommon.receive(serviceUUID,characteristicUUID);
                        AcToastUtil.showShort(this,success+"");
                    }
                }else {
                    AcToastUtil.showShort(this,"未连接上设备");
                }
                break;
            case R.id.btn_send:
                if(gattCommon.isConnect()){
                    String sendService = etSendService.getText().toString();
                    String sendCharacteristic = etSendCharacteristic.getText().toString();
                    String sendData = etSendData.getText().toString();
                    byte[] data = stringToByte(sendData);
                    if(!TextUtils.isEmpty(sendService) && !TextUtils.isEmpty(sendCharacteristic) && data != null && data.length > 0){
                        UUID serviceUUID = UUID.fromString(sendService);
                        UUID characteristicUUID = UUID.fromString(sendCharacteristic);
                        boolean success = gattCommon.send(data,serviceUUID,characteristicUUID);
                        AcToastUtil.showShort(this,success+"");
                    }
                }else {
                    AcToastUtil.showShort(this,"未连接上设备");
                }

                break;
            case R.id.btn_uuid:
                if(gattCommon.isConnect()){
//                    List<BluetoothGattService> services = gattCommon.getBluetoothGatt().getServices();
                    List<BluetoothGattService> services = gattCommon.getmServiceList();
                    tvData.setText("");
                    for (int i = 0;services != null && i < services.size(); i++) {
                        BluetoothGattService service = services.get(i);
                        tvData.setText(tvData.getText()+ "服务:" + service.getUuid().toString() + "\n");
                        for (int j = 0; j < service.getCharacteristics().size(); j++) {
                            BluetoothGattCharacteristic characteristic = service.getCharacteristics().get(j);
                            tvData.setText(tvData.getText()+"--特征:" + characteristic.getUuid().toString() + "\n");
                            for (int k = 0; k < characteristic.getDescriptors().size(); k++) {
                                BluetoothGattDescriptor descriptor = characteristic.getDescriptors().get(k);
                                tvData.setText(tvData.getText()+"--##描述:" + descriptor.getUuid().toString() + "\n");
                            }
                        }
                    }
                }else {
                    AcToastUtil.showShort(this,"未连接上设备");
                }
                break;
            case R.id.tv_state:
                if(BluetoothStateConstant.CONNECT_NO.equals(state) || BluetoothStateConstant.CONNECTION_BREAK.equals(state) || BluetoothStateConstant.DISCONNECT.equals(state)){
                    gattCommon.connect(device);
                }
                break;
            case R.id.iv_receive_service_more:
                etType = 0;
                showServiceDialog();
                break;
            case R.id.iv_receive_characteristic_more:
                etType = 1;
                showCharacteristicDialog(etReceiveService.getText().toString());
                break;
            case R.id.iv_send_service_more:
                etType = 2;
                showServiceDialog();
                break;
            case R.id.iv_send_characteristic_more:
                etType = 3;
                showCharacteristicDialog(etSendService.getText().toString());
                break;
        }
    }

    private void showServiceDialog(){
        if(gattCommon.isConnect()){
            List<BluetoothGattService> gattServices = gattCommon.getmServiceList();
            if(gattServices != null && gattServices.size() > 0){
                List<String> services = new ArrayList<>();
                for(BluetoothGattService bluetoothGattService : gattServices){
                    services.add(bluetoothGattService.getUuid().toString());
                }
                Dialog dialog = AcCustomDialog.createListButtonDialog(this,services, gattListener);
                dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
            }
        }else {
            AcToastUtil.showShort(this,"未连接上设备");
        }
    }
    private void showCharacteristicDialog(String serviceUuidStr){
        if(gattCommon.isConnect()){
            if(serviceUuidStr == null || TextUtils.isEmpty(serviceUuidStr)){
                AcToastUtil.showShort(this,"请先选择上一个uuid");
                return;
            }
            UUID serviceUuid = UUID.fromString(serviceUuidStr);
            List<BluetoothGattCharacteristic> characteristics = gattCommon.getBluetoothGatt().getService(serviceUuid).getCharacteristics();
            if(characteristics != null && characteristics.size() > 0){
                List<String> data = new ArrayList<>();
                for(BluetoothGattCharacteristic characteristic : characteristics){
                    data.add(characteristic.getUuid().toString());
                }
                Dialog dialog = AcCustomDialog.createListButtonDialog(this,data, gattListener);
                dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
            }
        }else {
            AcToastUtil.showShort(this,"未连接上设备");
        }
    }

    private byte[] stringToByte(String str){
        if(str == null || TextUtils.isEmpty(str)){
            return null;
        }
        String[] strArray = str.replace(" ","").split(",");
        byte[] data = new byte[strArray.length];
        for (int i = 0; i < strArray.length; i++) {
//            data[i] = (byte) Integer.parseInt(strArray[i]);
            data[i] = Byte.parseByte(strArray[i]);
        }
        return data;
    }

    @Override
    protected void onDestroy() {
        if(gattCommon != null){
            gattCommon.disconnect();
        }
        super.onDestroy();
    }
}
