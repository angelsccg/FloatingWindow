package com.angels.world.activity;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.angels.library.utils.AcLogUtil;
import com.angels.library.widget.AcCustomDialog;
import com.angels.library.widget.AcCustomTitleLayout;
import com.angels.world.R;
import com.angels.world.adapter.BluetoothAdapter;
import com.angels.world.bluetooth.drive.DeviceConstant;
import com.angels.world.constant.KeyConfig;

import java.util.ArrayList;
import java.util.List;

import static android.bluetooth.le.ScanSettings.MATCH_MODE_STICKY;
import static android.bluetooth.le.ScanSettings.SCAN_MODE_LOW_LATENCY;

/**
 * Created by chencg on 2018/11/26.
 */

public class BluetoothListActivity extends BaseActivity implements AdapterView.OnItemClickListener{

    private BluetoothAdapter adapter;
    private List<BluetoothDevice> datas;
    private ListView listView;
    private AcCustomTitleLayout titleLayout;
    private String bluetoothType;
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            // 要做的事情
            super.handleMessage(msg);
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_list);

        bluetoothType = getIntent().getStringExtra(KeyConfig.KEY_BLUETOOTH_TYPE);
        initView();
        initTitleView();

        searchBand();
        handler.postDelayed(runnableHeart,30*1000);
    }

    private void initView(){
        listView = (ListView) findViewById(R.id.lv_bluetooth);
        listView.setOnItemClickListener(this);

        datas = new ArrayList<>();
        adapter = new BluetoothAdapter(datas,this);
        listView.setAdapter(adapter);
    }

    private void initTitleView() {
        titleLayout = (AcCustomTitleLayout) findViewById(R.id.rl_title);
        titleLayout.setTitle("蓝牙设备列表" + bluetoothType);
        titleLayout.setIvLeft(R.drawable.ic_back_white, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BluetoothListActivity.this.finish();
            }
        });
        titleLayout.setTvRight("搜索", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchBand();
                handler.postDelayed(runnableHeart,30*1000);
                datas.clear();
                titleLayout.setTvRightVisibility(View.INVISIBLE);
                titleLayout.setRightProgress(View.VISIBLE);
            }
        });
        titleLayout.setTvRightVisibility(View.INVISIBLE);
        titleLayout.setRightProgress(View.VISIBLE);
//        titleLayout.setRightProgressClick(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                searchBand();
//            }
//        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        BluetoothDevice device = datas.get(position);
        if(KeyConfig.KEY_BLUETOOTH_BLE.equals(bluetoothType)){
            Intent intent = new Intent(this,BluetoothBleActivity.class);
            intent.putExtra(KeyConfig.KEY_DEVICE,device);
            startActivity(intent);
        }else if(KeyConfig.KEY_BLUETOOTH_SPP.equals(bluetoothType)){
            Intent intent = new Intent(this,BluetoothSppActivity.class);
            intent.putExtra(KeyConfig.KEY_DEVICE,device);
            startActivity(intent);
        }
        finish();
    }

    /**搜索*/
    Runnable runnableHeart = new Runnable() {
        @Override
        public void run() {
            stopSearch();
            titleLayout.setTvRightVisibility(View.VISIBLE);
            titleLayout.setRightProgress(View.GONE);
        }
    };


      /*==========================================搜索===========================================*/
    //     private BleScanCallback scanCallback;
    private ScanCallback scanCallback;

    /***
     * 搜索周围的蓝牙设备(手环)
     */
    private void searchBand() {
        //判断定位服务是否开启
        if (!isLocationEnabled()) {
            Dialog dialog = AcCustomDialog.createMessageDialog(this, "需要开启位置服务，是否前去开启", "是", "否", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                    ((Dialog) v.getTag()).dismiss();
                    finish();
                }
            }, null);
            dialog.show();
            return;
        }


        AcLogUtil.i("蓝牙-->searchBD2扫描-->版本判断" + Build.VERSION.SDK_INT + "--" + Build.VERSION_CODES.LOLLIPOP);
        android.bluetooth.BluetoothAdapter bluetoothAdapter = android.bluetooth.BluetoothAdapter.getDefaultAdapter();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //2. Android 5.0以上，扫描的结果在mScanCallback中进行处理
//            scanCallback = new BleScanCallback();
            BluetoothLeScanner bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();
            //搜索的时候可添加个过滤器
            bluetoothLeScanner.startScan(getScanCallback());
            AcLogUtil.i("蓝牙-->searchBD2扫描-->开始搜索");
        } else {
            //1. Android 4.3以上，Android 5.0以下
            bluetoothAdapter.startLeScan(leScanCallback);
        }
//        if (!bluetoothAdapter.isDiscovering()) {
//            bluetoothAdapter.startDiscovery();//开始搜索 
//        }
    }

    private android.bluetooth.BluetoothAdapter.LeScanCallback leScanCallback = new android.bluetooth.BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            AcLogUtil.i("蓝牙-->search扫描-->onLeScan-->" + rssi + "--" + device.getName() + "--" + device.getType() + "--" + device.getAddress());
            if ((device.getName()!= null && !TextUtils.isEmpty(device.getName().trim())) && !datas.contains(device)) {
                if(device.getType() == 2 || device.getType() == 3){
                    datas.add(device);
                    adapter.setData(datas);
                }
            }
        }
    };

    private ScanCallback getScanCallback() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if(scanCallback != null){
                return scanCallback;
            }
            scanCallback = new ScanCallback() {
                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onScanResult(int callbackType, ScanResult result) {
                    super.onScanResult(callbackType, result);
                    AcLogUtil.i("蓝牙-->searchBD2扫描-->onScanResult-->" + callbackType + "--" + result.getDevice().getName() + "--" + result.getDevice().getType() + "--" + result.getDevice().getAddress() + "---Rssi：" + result.getRssi());
                    if ((result.getDevice().getName()!= null && !TextUtils.isEmpty(result.getDevice().getName().trim())) && !datas.contains(result.getDevice())) {
                        if(result.getDevice().getType() == 2 || result.getDevice().getType() == 3){
                            datas.add(result.getDevice());
                            adapter.setData(datas);
                        }
                    }
                }
            };
        }
        return scanCallback;
    }

    /**
     * 判断定位无法是否开启
     */
    public boolean isLocationEnabled() {
        int locationMode = 0;
        String locationProviders;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(getContentResolver(), Settings.Secure.LOCATION_MODE);
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }
            return locationMode != Settings.Secure.LOCATION_MODE_OFF;
        } else {
            locationProviders = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
    }

    private void stopSearch(){
        android.bluetooth.BluetoothAdapter bluetoothAdapter = android.bluetooth.BluetoothAdapter.getDefaultAdapter();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if(scanCallback != null){
                bluetoothAdapter.getBluetoothLeScanner().stopScan(scanCallback);
            }
        }else{
            if(leScanCallback != null){
                bluetoothAdapter.stopLeScan(leScanCallback);
            }
        }
    }
    /*=============================end====================================*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(handler != null){
            handler.removeCallbacks(runnableHeart);
            handler = null;
        }
        stopSearch();
    }

}
