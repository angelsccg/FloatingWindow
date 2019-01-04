package com.angels.world.activity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.angels.library.utils.AcLogUtil;
import com.angels.library.utils.AcToastUtil;
import com.angels.world.R;
import com.angels.world.utils.parser.JsonParser;
import com.angels.world.utils.parser.XmlParser;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.GrammarListener;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.util.ResourceUtil;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Name: SplashAdActivity
 * Comment: 启动页
 */

public class VoiceXFActivity extends BaseActivity implements View.OnClickListener{


    private ImageButton btnSpeak;
    private TextView tvContent;

    // 云端语法文件
    private String mCloudGrammar = null;
    // 语音识别对象
    private SpeechRecognizer mAsr;
    // 引擎类型
    private String mEngineType = SpeechConstant.TYPE_CLOUD;
    // 云端语法id
    private String mCloudGrammarID;
    // 本地语法id
    private String mLocalGrammarID;
    // 本地语法文件
    private String mLocalGrammar = null;
    // 本地语法构建路径
    private String grmPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "world";
    // 返回结果格式，支持：xml,json
    private String mResultType = "json";
    /**
     * 初始化监听器。
     */
    private InitListener mInitListener = new InitListener() {

        @Override
        public void onInit(int code) {
            AcLogUtil.i("SpeechRecognizer init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                AcToastUtil.showShort(VoiceXFActivity.this,"初始化失败,错误码："+code);
            }
        }
    };
    private GrammarListener grammarListener = new GrammarListener() {
        @Override
        public void onBuildFinish(String grammarId, SpeechError error) {
            if (error == null) {
                if (mEngineType.equals(SpeechConstant.TYPE_CLOUD)) {
                    mCloudGrammarID = grammarId;
                } else {
                    mLocalGrammarID = grammarId;
                }
                AcToastUtil.showShort(VoiceXFActivity.this,"语法构建成功：" + grammarId);
            } else {
                AcToastUtil.showShort(VoiceXFActivity.this,"语法构建失败,错误码：" + error.getErrorCode());
            }
        }
    };
    /**
     * 识别监听器。
     */
    private RecognizerListener mRecognizerListener = new RecognizerListener() {

        @Override
        public void onVolumeChanged(int volume, byte[] data) {
            AcToastUtil.showShort(VoiceXFActivity.this,"当前正在说话，音量大小：" + volume);
            AcLogUtil.d( "返回音频数据："+data.length);
        }

        @Override
        public void onResult(final RecognizerResult result, boolean isLast) {
            if (null != result && !TextUtils.isEmpty(result.getResultString())) {
                AcLogUtil.d("recognizer result：" + result.getResultString());
                String text = "";
                if (mResultType.equals("json")) {
                    text = JsonParser.parseGrammarResult(result.getResultString(), mEngineType);
                } else if (mResultType.equals("xml")) {
                    text = XmlParser.parseNluResult(result.getResultString());
                }else{
                    text = result.getResultString();
                }
                // 显示
                ((TextView) findViewById(R.id.tv_content)).setText(text);
            } else {
                AcLogUtil.d("recognizer result : null");
            }
        }

        @Override
        public void onEndOfSpeech() {
            // 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
            AcToastUtil.showShort(VoiceXFActivity.this,"结束说话");
        }

        @Override
        public void onBeginOfSpeech() {
            // 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
            AcToastUtil.showShort(VoiceXFActivity.this,"开始说话");
        }

        @Override
        public void onError(SpeechError error) {
            AcToastUtil.showShort(VoiceXFActivity.this,"onError Code："	+ error.getErrorCode());
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
            //	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            //		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            //		Log.d(TAG, "session id =" + sid);
            //	}
        }

    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_xf);
        initView();
        init();
    }

    private void initView() {
        btnSpeak = findViewById(R.id.btn_speak);
        tvContent = findViewById(R.id.tv_content);

        btnSpeak.setOnClickListener(this);
    }

    private void init() {
        // 初始化识别对象---唤醒+识别,用来构建语法
        mAsr = SpeechRecognizer.createRecognizer(this, mInitListener);
        // 初始化语法文件
        mCloudGrammar = readFile(this, "grammar_sample.abnf", "utf-8");
        mLocalGrammar = readFile(this, "call.bnf", "utf-8");
//        mLocalGrammar = FucUtil.readFile(this,"call.bnf", "utf-8");
//        mCloudGrammar = FucUtil.readFile(this,"grammar_sample.abnf","utf-8");
//        mCloudGrammar = readFile(this, "wake_grammar_sample.abnf", "utf-8");
//        mLocalGrammar = readFile(this, "wake.bnf", "utf-8");

        //构建语法
        initGrammar();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_speak:
                //语法识别
                recognition();
                //离线语音听写
                offlineDictation();
                break;
        }
    }

    /**构建语法*/
    private void initGrammar(){
        //ABNF 和 BNF 格式。前者用于在线语法识别，后者用于离线语法识别。
//        initCloud();
        initLocal();
    }

    /**在线语法识别 初始化   ABNF*/
    private void initCloud(){
        //===============ABNF===============
        // 设置引擎类型
        mAsr.setParameter( SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD );
        /* 其中 "abnf" 指定语法类型为 ABNF,  grammarContent 为语法内容，grammarListener 为构建结果监听器*/
        int ret = mAsr.buildGrammar( "abnf", mCloudGrammar, grammarListener);
        if (ret != ErrorCode.SUCCESS) {
            AcToastUtil.showShort(VoiceXFActivity.this,"语法构建失败,错误码：" + ret);
        }
    }
    private String engineMode = SpeechConstant.MODE_MSC;
    private String engineType = SpeechConstant.TYPE_LOCAL;
    /**本地语法识别 初始化   BNF*/
    private void initLocal(){
        //===============BNF===============
        mAsr.setParameter(SpeechConstant.PARAMS, null);
        // 设置文本编码格式
        mAsr.setParameter(SpeechConstant.TEXT_ENCODING,"utf-8");
        // 设置引擎模式
        mAsr.setParameter( SpeechConstant.ENGINE_MODE, engineMode );
        // 设置引擎类型
        mAsr.setParameter( SpeechConstant.ENGINE_TYPE, engineType );

//        if( SpeechConstant.MODE_MSC.equals(engineMode) ){
            // 设置语法结果文件保存路径，以在本地识别时使用
            mAsr.setParameter( ResourceUtil.GRM_BUILD_PATH, grmPath );
            //设置识别资源路径
            mAsr.setParameter( ResourceUtil.ASR_RES_PATH, getResourcePath());
//        }
        /* 其中 "bnf" 指定语法类型为 BNF,  grammarContent 为语法内容，grammarListener 为构建结果监听器*/
//        int ret = mAsr.buildGrammar( "bnf", mLocalGrammar, grammarListener );
        int ret = mAsr.buildGrammar("bnf", mLocalGrammar, grammarListener);
        if (ret != ErrorCode.SUCCESS) {
            AcToastUtil.showShort(VoiceXFActivity.this,"语法构建失败,错误码：" + ret);
        }
    }

    /***
     * 语法识别
     * */
    private void recognition(){
        //设置引擎类型
        mAsr.setParameter( SpeechConstant.ENGINE_TYPE, engineType );

        if( SpeechConstant.TYPE_LOCAL.equals(engineType) ){
            // 设置本地识别资源
            mAsr.setParameter(ResourceUtil.ASR_RES_PATH, getResourcePath());
            // 设置语法构建路径
            mAsr.setParameter(ResourceUtil.GRM_BUILD_PATH, grmPath);
            // 设置返回结果格式
            mAsr.setParameter(SpeechConstant.RESULT_TYPE, mResultType);
            // 设置本地识别使用语法id
            mAsr.setParameter(SpeechConstant.LOCAL_GRAMMAR, "call" );
        }
        int ret = mAsr.startListening( mRecognizerListener );
        if (ret != ErrorCode.SUCCESS) {
            AcToastUtil.showShort(VoiceXFActivity.this,"识别失败,错误码: " + ret);
        }
    }
    /**离线听写*/
    private void offlineDictation(){
        //此处engineType为“local”
        mAsr.setParameter( SpeechConstant.ENGINE_TYPE, engineType );
        if (mEngineType.equals(SpeechConstant.TYPE_LOCAL)) {
            // 设置本地识别资源
            mAsr.setParameter(ResourceUtil.ASR_RES_PATH, getResourcePath());
        }
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


//    ===============================================================================
    /**
     * 读取asset目录下文件。
     *
     * @return content
     */
    public static String readFile(Context mContext, String file, String code) {
        int len = 0;
        byte[] buf = null;
        String result = "";
        try {
            InputStream in = mContext.getAssets().open(file);
            len = in.available();
            buf = new byte[len];
            in.read(buf, 0, len);

            result = new String(buf, code);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    //获取识别资源路径
    private String getResourcePath(){
        StringBuffer tempBuffer = new StringBuffer();
        //识别通用资源
        tempBuffer.append(ResourceUtil.generateResourcePath(this, ResourceUtil.RESOURCE_TYPE.assets, "asr/common.jet"));
        return tempBuffer.toString();
    }
//    private String getResourcePath(){
//        StringBuffer tempBuffer = new StringBuffer();
//        //识别通用资源
//        tempBuffer.append(ResourceUtil.generateResourcePath(this, ResourceUtil.RESOURCE_TYPE.assets, "iat/common.jet"));
//        tempBuffer.append(";");
//        tempBuffer.append(ResourceUtil.generateResourcePath(this, ResourceUtil.RESOURCE_TYPE.assets, "iat/sms_16k.jet"));
//        //识别8k资源-使用8k的时候请解开注释
//        return tempBuffer.toString();
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if( null != mAsr ){
            // 退出时释放连接
            mAsr.cancel();
            mAsr.destroy();
        }
    }

}
