package com.angels.world.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.angels.library.utils.AcAppUtil;
import com.angels.world.R;
import com.angels.world.constant.RequestUrl;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Name: SplashAdActivity
 * Comment: 启动页
 */

public class VoiceActivity extends BaseActivity implements TextToSpeech.OnInitListener{

    private EditText et_text;
    private TextToSpeech tts;

    protected static final int RESULT_SPEECH = 1;
    private ImageButton btnSpeak;
    private TextView txtText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice);
        init();
    }

    private void init() {
        et_text =  findViewById(R.id.et_text);
        //创建tts对象
        tts = new TextToSpeech(this, this);


        txtText = (TextView) findViewById(R.id.txtText);

        btnSpeak = (ImageButton) findViewById(R.id.btnSpeak);

        btnSpeak.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                start();
            }
        });
    }

    private void start(){
        Intent intent = new Intent(
                RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

//                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");
//                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "zh-cn");
        //语言模式和自由模式的语音识别
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        //提示语音开始
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "开始语音");
        //语音搜索结果最大值设定
//                intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 5);

        intent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS,true);

        try {
            startActivityForResult(intent, RESULT_SPEECH);
//            txtText.setText("");
        } catch (ActivityNotFoundException a) {
            Toast t = Toast.makeText(getApplicationContext(),
                    "你的手机不支持 Speech to Text",
                    Toast.LENGTH_SHORT);
            t.show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RESULT_SPEECH: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> text = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
//                    txtText.setText("");
                    for (String str:text){
                        txtText.setText(txtText.getText().toString() + "\n==========\n" + str);
                    }
                    start();

                }
                break;
            }

        }
    }

    public void play(View view){
        String str = et_text.getText().toString().trim();
        if (!TextUtils.isEmpty(str)){
            // 设置音调，值越大声音越尖（女生），值越小则变成男声,1.0是常规
            tts.setPitch(1.0f);
            // 设置语速
            tts.setSpeechRate(1.0f);
            //播放语音
            tts.speak(str, TextToSpeech.QUEUE_ADD, null,null);
        }
    }
    @Override
    public void onInit(int status) {
        //判断tts回调是否成功
        if (status == TextToSpeech.SUCCESS) {
            int result1 = tts.setLanguage(Locale.US);
            int result2 = tts.setLanguage(Locale.CHINESE);
            if (result1 == TextToSpeech.LANG_MISSING_DATA || result1 == TextToSpeech.LANG_NOT_SUPPORTED || result2 == TextToSpeech.LANG_MISSING_DATA || result2 == TextToSpeech.LANG_NOT_SUPPORTED){
                Toast.makeText(this, "数据丢失或不支持", Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
    }
}
