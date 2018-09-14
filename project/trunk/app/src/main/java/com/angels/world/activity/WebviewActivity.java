package com.angels.world.activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.angels.world.R;
import com.angels.library.widget.AcCustomTitleLayout;
import com.tamic.jswebview.browse.CallBackFunction;
import com.tamic.jswebview.browse.JsWeb.CustomWebChromeClient;
import com.tamic.jswebview.browse.JsWeb.CustomWebViewClient;
import com.tamic.jswebview.browse.JsWeb.JsHandler;
import com.tamic.jswebview.view.ProgressBarWebView;

import java.util.ArrayList;
import java.util.Map;

/**
 * Name: WebviewActivity
 * Author: ccg
 * Comment: 通用webview
 * Date: 2017-08-22 16:46
 */
public class WebviewActivity extends BaseActivity implements View.OnClickListener{
    public static final String DATA_URL = "data_url";
    /**标题栏*/
    private AcCustomTitleLayout titleLayout;
    /**webview*/
    private  ProgressBarWebView webview;
    private String url;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_webview);
        initIntent();
        initView();
        initWebView();
    }

    private void initWebView() {
        // 打开页面，也可以支持网络url
        if(url != null){
            webview.getWebView().clearCache(true);
            webview.loadUrl(url);
        }

        webview.setWebViewClient(new CustomWebViewClient(webview.getWebView()) {
            @Override
            public String onPageError(String url) {
                //指定网络加载失败时的错误页面
                return "file:///android_asset/error.html";
            }
            @Override
            public Map<String, String> onPageHeaders(String url) {
                // 可以加入header
                return null;
            }
        });
        webview.getChromeClient().setOnCallListener(new CustomWebChromeClient.CallListener() {
            @Override
            public void showTitle(String title) {
                titleLayout.setTitle(title);
            }
        });

        ArrayList<String> mHandlerNames = new ArrayList<>();
        mHandlerNames.add("close");
        webview.registerHandlers(mHandlerNames, new JsHandler() {
            @Override
            public void OnHandler(String handlerName, String responseData, CallBackFunction function) {
                Toast.makeText(WebviewActivity.this, handlerName, Toast.LENGTH_SHORT).show();
                if (handlerName.equals("close")) {
                    WebviewActivity.this.finish();
                }
            }
        });
    }

    private void initIntent() {
        Intent intent = getIntent();
        url = intent.getStringExtra(DATA_URL);
        System.out.println("url-->"+url);
    }

    private void initView() {
        /*初始化标题控件*/
        initTitleView();

        webview = findViewById(R.id.webview);
    }

    private void initTitleView() {
        titleLayout = findViewById(R.id.rl_title);
        titleLayout.setTitle("");
        titleLayout.setIvLeft(R.drawable.ic_back_white, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
    }

}
