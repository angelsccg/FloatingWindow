package com.angels.floatwindow.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.angels.floatwindow.R;
import com.angels.library.utils.AcLogUtil;
import com.angels.library.utils.AcToastUtil;

public class MapActivity extends BaseActivity implements AMapLocationListener,AMap.OnMapClickListener{
    public static final LatLng FUZHOU_LATLNG = new LatLng(26.105313,119.257955);

    private MapView mapView = null;
    //地图控制器对象
    private AMap aMap;
    //定位蓝点样式类
    private MyLocationStyle myLocationStyle;
    //定义一个UiSettings对象 (控件交互)
    private UiSettings mUiSettings;
    //声明mlocationClient对象
    private AMapLocationClient mlocationClient;
    //声明mLocationOption对象
    private AMapLocationClientOption mLocationOption = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        initMap(savedInstanceState);

    }
    /**初始化地图*/
    private void initMap(Bundle savedInstanceState) {
        //获取地图控件引用
        mapView = findViewById(R.id.map);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mapView.onCreate(savedInstanceState);
        if (aMap == null) {
            aMap = mapView.getMap();
        }
        /*
        实例化UiSettings类对象
         */
        mUiSettings = aMap.getUiSettings();
        //缩放按钮是提供给 App 端用户控制地图缩放级别的交换按钮，每次点击改变1个级别，此控件默认打开
        mUiSettings.setZoomControlsEnabled(true);
        //设置缩放按钮的位置
//        mUiSettings.setZoomPosition();
        //控制比例尺控件是否显示
        mUiSettings.setScaleControlsEnabled(true);
        //指南针用于向 App 端用户展示地图方向，默认不显示
        mUiSettings.setCompassEnabled(true);
        //设置默认定位按钮是否显示，非必需设置。
        mUiSettings.setMyLocationButtonEnabled(true);

        /*
        *
        * */
        aMap.setOnMapClickListener(this);
        // 显示实时交通状况
//        aMap.setTrafficEnabled(true);
        //地图模式可选类型：MAP_TYPE_NORMAL,MAP_TYPE_SATELLITE,MAP_TYPE_NIGHT
        // 卫星地图模式
//        aMap.setMapType(AMap.MAP_TYPE_SATELLITE);
        moveMap(new LatLng(26.105313,119.257955),16,0,0);
        drawMarker(FUZHOU_LATLNG,"公司","这个世界很美");
        initLocation();
    }
    /**初始化地位*/
    private void initLocation() {

        //初始化定位蓝点样式类
        myLocationStyle = new MyLocationStyle();
        //连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);
        //连续定位、蓝点不会移动到地图中心点，并且蓝点会跟随设备移动。
//        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW_NO_CENTER);
        //连续定位、蓝点不会移动到地图中心点，定位点依照设备方向旋转，并且蓝点会跟随设备移动。
//        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);
        //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        myLocationStyle.interval(2000);
        ////设置是否显示定位小蓝点，用于满足只想使用定位，不想使用定位小蓝点的场景，设置false以后图面上不再有定位蓝点的概念，但是会持续回调位置信息。
        myLocationStyle.showMyLocation(true);
        //如何让这个蓝色的范围圆圈不显示出来，只显示一个marker
        myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));// 设置圆形的边框颜色
        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));// 设置圆形的填充颜色
        //设置定位蓝点的Style
        aMap.setMyLocationStyle(myLocationStyle);
        // 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        aMap.setMyLocationEnabled(true);
        //通过aMap对象设置定位数据源的监听
//        aMap.setLocationSource(this);
        mlocationClient = new AMapLocationClient(this);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置返回地址信息，默认为true
        mLocationOption.setNeedAddress(true);
        //设置定位监听
        mlocationClient.setLocationListener(this);
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
        //设置定位参数
        mlocationClient.setLocationOption(mLocationOption);
        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（最小间隔支持为1000ms），并且在合适时间调用stopLocation()方法来取消定位请求
        // 在定位结束后，在合适的生命周期调用onDestroy()方法
        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
        //启动定位
        mlocationClient.startLocation();

    }

    /**移动*/
    /**
     *
     * @param latLng  视角调整区域的中心点坐标
     * @param zoomLevel 希望调整到的缩放级别
     * @param angle1 俯仰角0°~45°（垂直与地图时为0）
     * @param angle2 偏航角 0~360° (正北方为0)
     */
    private void moveMap(LatLng latLng,int zoomLevel, int angle1, int angle2){
        //参数依次是：视角调整区域的中心点坐标、希望调整到的缩放级别、俯仰角0°~45°（垂直与地图时为0）、偏航角 0~360° (正北方为0)
        CameraUpdate cameraupdate = CameraUpdateFactory.newCameraPosition(new CameraPosition(latLng,zoomLevel,angle1,angle2));
        //AMap类中提供，直接移动过去，不带移动过程动画
        aMap.moveCamera(cameraupdate);
        //AMap类中提供，带有移动过程的动画
        aMap.animateCamera(cameraupdate,1000,null);
    }

    /**缩放
     * @param  zoomLevel 缩放基本
     * ZoomTo
     缩放地图到指定的缩放级别
     AMap.moveCamera(CameraUpdateFactory.zoomTo(17))
     ZoomIn
     缩放地图到当前缩放级别的上一级
     AMap.moveCamera(CameraUpdateFactory.zoomIn())
     * */
    private void zoomMap(int zoomLevel){
        //设置希望展示的地图缩放级别
        CameraUpdate cameraupdate = CameraUpdateFactory.zoomTo(zoomLevel);
        //AMap类中提供，直接移动过去，不带移动过程动画
        aMap.moveCamera(cameraupdate);
        //AMap类中提供，带有移动过程的动画
        aMap.animateCamera(cameraupdate,1000,null);
    }

    /**绘制点坐标
     *
     * Marker 常用属性
     position 在地图上标记位置的经纬度值。必填参数
     title 点标记的标题
     snippet 点标记的内容
     draggable 点标记是否可拖拽
     visible 点标记是否可见
     anchor 点标记的锚点
     alpha 点的透明度
     * */
    private void drawMarker(LatLng latLng, String title, String snippet){
        //默认样式
//        final Marker marker = aMap.addMarker(new MarkerOptions().position(latLng).title(title).snippet(snippet));
        //自定义样式
        MarkerOptions markerOption = new MarkerOptions();
        markerOption.position(latLng).title(title).snippet(snippet);
        //设置Marker可拖动
        markerOption.draggable(true);
//        markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.ic_phone)));
        // 将Marker设置为贴地显示，可以双指下拉地图查看效果
//        markerOption.setFlat(true);//设置marker平贴地图效果
        final Marker marker = aMap.addMarker(markerOption);

    }
    private void drawMarker2(LatLng latLng, String title, String snippet){
        //默认样式
//        final Marker marker = aMap.addMarker(new MarkerOptions().position(latLng).title(title).snippet(snippet));
        //自定义样式
        MarkerOptions markerOption = new MarkerOptions();
        markerOption.position(latLng).title(title).snippet(snippet);
        //设置Marker可拖动
        markerOption.draggable(true);
//        markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.ic_phone)));
        // 将Marker设置为贴地显示，可以双指下拉地图查看效果
//        markerOption.setFlat(true);//设置marker平贴地图效果
        final Marker marker = aMap.addMarker(markerOption);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        AcLogUtil.i("map-->onMapClick:" + latLng);
        AcToastUtil.showShort(MapActivity.this,latLng + "");
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        AcLogUtil.i("map-->onLocationChanged:" + aMapLocation);

        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
                aMapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                aMapLocation.getLatitude();//获取纬度
                aMapLocation.getLongitude();//获取经度
                aMapLocation.getAccuracy();//获取精度信息
                aMapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                aMapLocation.getCountry();//国家信息
                aMapLocation.getProvince();//省信息
                aMapLocation.getCity();//城市信息
                aMapLocation.getDistrict();//城区信息
                aMapLocation.getStreet();//街道信息
                aMapLocation.getStreetNum();//街道门牌号信息
                aMapLocation.getCityCode();//城市编码
                aMapLocation.getAdCode();//地区编码
                aMapLocation.getAoiName();//获取当前定位点的AOI信息
                AcLogUtil.i("map-->onLocationChanged-->" +
                        "来源:" + aMapLocation.getLocationType() + ", " +
                        "纬度:" + aMapLocation.getLatitude() + ", " +
                        "经度:" + aMapLocation.getLongitude() + ", " +
                        "精度信息:" + aMapLocation.getAccuracy() + ", " +
                        "地址:" + aMapLocation.getAddress() + ", " +
                        "国家:" + aMapLocation.getCountry() + ", " +
                        "省:" + aMapLocation.getProvince() + ", " +
                        "城市:" + aMapLocation.getCity() + ", " +
                        "城区:" + aMapLocation.getDistrict() + ", " +
                        "街道:" + aMapLocation.getStreet() + ", " +
                        "街道门牌号:" + aMapLocation.getStreetNum() + ", " +
                        "城市编码:" + aMapLocation.getCityCode() + ", " +
                        "地区编码:" + aMapLocation.getAdCode() + ", " +
                        "当前定位点的AOI信息:" + aMapLocation.getAoiName()
                );

                //连续定位、蓝点不会移动到地图中心点，定位点依照设备方向旋转，并且蓝点会跟随设备移动。
                myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);
                aMap.setMyLocationStyle(myLocationStyle);
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                AcLogUtil.e("map-->onLocationChanged-->定位错误, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        if(mapView != null){
            mapView.onDestroy();
        }
        if(mlocationClient != null){
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mapView.onPause();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mapView.onSaveInstanceState(outState);
    }

}
