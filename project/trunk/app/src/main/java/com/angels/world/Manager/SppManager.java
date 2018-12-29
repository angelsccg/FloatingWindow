package com.angels.world.Manager;

///**
// * Created by chencg on 2018/1/30.
// */
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.util.UUID;
//
//import android.bluetooth.BluetoothDevice;
//import android.bluetooth.BluetoothSocket;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//
//import com.angels.library.utils.AcLogUtil;
//import com.angels.library.utils.HexStringUtils;
//
//public class SppManager {
//    /**初始状态*/
//    public static final int STATE_NO = 0;
//    /**连接中。。*/
//    public static final int STATE_CONNECTING = 1;
//    /**连接成功*/
//    public static final int STATE_CONNECTED = 2;
//    /**连接失败*/
//    public static final int STATE_CONNECT_FAIL = 3;
//    /**断开连接（传输数据时候 失败）*/
//    public static final int STATE_FAIL = 4;
//    /**主动断开连接*/
//    public static final int STATE_STOP = 5;
//
//
//
//    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
//    /*
//     * private static final UUID MY_UUID = UUID
//     * .fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");
//     */
//    private BluetoothSocket socket;
//    private BluetoothDevice device;
//    private InputStream mmInStream;
//    private OutputStream mmOutStream;
//    private Handler mHandler;
//    private ConnectedThread mConnectedThread;
//    private boolean isRuning = false;
//    private int mState;
//
//    /*自定义回调接口*/
//    public interface SppListener {
//        /* 1：连接成功 0：连接失败*/
//        void state(int state);
//        void receive(int[] bytes);
//        void error(int[] bytes);
//    }
//    public SppListener listener;
//
//    public SppManager() {
//        mState = STATE_NO;
//    }
//
//    public int getmState() {
//        return mState;
//    }
//
//    public boolean isSucced(){
//        return STATE_CONNECTED == mState;
//    }
//
//    public void setHandler(Handler handler) {
//        mHandler = handler;
//    }
//
//    public void setDevice(BluetoothDevice device) {
//        this.device = device;
//    }
//
//    /**
//     * 连接方法
//     */
//    public void connect() {
//        isRuning = true;
//        new Thread(){
//            @Override
//            public void run() {
//                super.run();
//                AcLogUtil.i("蓝牙-->connect()" );
//                if (device != null) {
//                    try {
//                        mState = STATE_CONNECTING;
//                        if(listener != null){
//                            listener.state(mState);
//                        }
//                        socket = device.createRfcommSocketToServiceRecord(MY_UUID);
//                        socket.connect();
//                        mState = STATE_CONNECTED;
//                        if(listener != null){
//                            listener.state(mState);
//                        }
//                        connected();
//                        AcLogUtil.i("蓝牙-->血压-->socket.connect()-->connected()" );
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                        connectionFailed();
//                    }
//                }
//            }
//        }.start();
//    }
//
//    /**
//     * 发送数据
//     *
//     * @param f
//     */
//    public void write(byte[] f) {
//        AcLogUtil.i("蓝牙-->血压-->write()--> " + HexStringUtils.bcd2Str16(f));
//        if (mmOutStream != null)
//            try {
//                mmOutStream.write(f);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//    }
//
//    /**
//     * 连接成功后启动接收数据的线程
//     */
//    private void connected() {
//        if (socket != null) {
//            AcLogUtil.i("蓝牙-->血压-->连接成功 启动线程");
//            mConnectedThread = new ConnectedThread();
//            mConnectedThread.start();
//
//            mState = STATE_CONNECTED;
//            if(listener != null){
//                listener.state(STATE_CONNECTED);
//            }
//        }
//
//    }
//
//    /**
//     * 停止
//     */
//    public void stop() {
//        AcLogUtil.i("蓝牙-->血压-->stop() ");
//        try {
//            isRuning = false;
//            if (mConnectedThread != null) {
//                mConnectedThread.interrupt();
//                //   mConnectedThread.destroy();
//            }
//            if (socket != null) {
//                socket.close();
//                mState = STATE_CONNECT_FAIL;
//                if(listener != null){
//                    listener.state(STATE_CONNECT_FAIL);
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    private void connectionFailed() {
//        mState = STATE_CONNECT_FAIL;
//        if(listener != null){
//            listener.state(STATE_CONNECT_FAIL);
//        }
//    }
//
//    private void connectionLost() {
//        send(IBean.ERROR, new Error(Error.ERROR_CONNECTION_LOST));
//        if(listener != null){
//            listener.connect(STATE_FAIL);
//        }
//    }
//    private class ConnectedThread extends Thread {
//        public ConnectedThread() {
//            try {
//                mmInStream = socket.getInputStream();
//                mmOutStream = socket.getOutputStream();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        public void run() {
//            byte[] buffer = new byte[16];
//            while (isRuning) {
//                try {
//                    if (mmInStream != null && mmInStream.available() > 0) {// 如果流中有数据就进行解析
//                        Head head = new Head();
//                        mmInStream.read(buffer);
//                        LogUtil.i("蓝牙-->血压-->收到的数据" + HexStringUtils.bcd2Str16(buffer));
//                        int[] f = CodeFormat.bytesToHexStringTwo(buffer, 6);
//                        head.analysis(f);
//                        if (head.getType() == Head.TYPE_ERROR) {
//                            // APP接收到血压仪的错误信息
//                            Error error = new Error();
//                            error.analysis(f);
//                            error.setHead(head);
//                            // 前台根据错误编码显示相应的提示
//                            send(IBean.ERROR, error);
//                            if(listener != null){
//                                listener.error(f);
//                            }
//                        }
//                        if (head.getType() == Head.TYPE_RESULT) {
//                            // APP接收到血压仪的测量结果
//                            Data data = new Data();
//                            data.analysis(f);
//                            data.setHead(head);
//                            // 前台根据测试结果来画线性图
//                            send(IBean.DATA, data);
//                            if(listener != null){
//                                listener.bloodEnd(f);
//                            }
//                        }
//
//                        if (head.getType() == Head.TYPE_MESSAGE) {
//                            // APP接收到血压仪开始测量的通知
//                            Msg msg = new Msg();
//                            msg.analysis(f);
//                            msg.setHead(head);
//                            send(IBean.MESSAGE, msg);
//                            if(listener != null){
//                                listener.bloodStart(f);
//                            }
//                        }
//                        if (head.getType() == Head.TYPE_PRESSURE) {
//                            // APP接受到血压仪测量的压力数据
//                            Pressure pressure = new Pressure();
//                            pressure.analysis(f);
//                            pressure.setHead(head);
//                            // 每接收到一条数据就发送到前台，以改变进度条的显示
//                            send(IBean.DATA, pressure);
//                            if(listener != null){
//                                listener.blooding(f);
//                            }
//                        }
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    connectionLost();
//                    interrupt();
//                    break;
//                }
//            }
//        }
//    }
//
//    public void setListener(BloodListener listener) {
//        this.listener = listener;
//    }
//
//    public boolean isConnect(){
//        if(socket != null){
//            return socket.isConnected();
//        }else{
//            return false;
//        }
//    }
//}
