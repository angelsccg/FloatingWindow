package com.tamic.jswebview.browse.JsWeb;


import android.support.annotation.NonNull;
import android.util.Log;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;

import com.tamic.jswebview.browse.BridgeWebView;
import com.tamic.jswebview.browse.BridgeWebViewClient;

import java.util.Map;

/**
 * Class description
 *
 * @author YEZHENNAN220
 * @date 2016-07-08 13:54
 */
public abstract class CustomWebViewClient extends BridgeWebViewClient {

    private boolean isFile;
    private String fileUrl = null;

    public CustomWebViewClient(BridgeWebView webView) {
        super(webView);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        Log.i("UrlLoading","url-->url:"+url+",isFile:"+isFile+",fileUrl:"+fileUrl);
        if(isFile && fileUrl != null){
            view.loadUrl(fileUrl, onPageHeaders(fileUrl));
            isFile = false;
            url = fileUrl;
            fileUrl = null;
            return super.shouldOverrideUrlLoading(view, url);
        }
        if (onPageHeaders(url) != null) {
            view.loadUrl(url, onPageHeaders(url));
        }
        return super.shouldOverrideUrlLoading(view, url);
    }
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Log.i("UrlLoading","url-->request.getUrl():"+request.getUrl());
            String url = request.getUrl().toString();
            if(isFile && fileUrl != null){
                view.loadUrl(fileUrl, onPageHeaders(fileUrl));
                isFile = false;
                url = fileUrl;
                fileUrl = null;
                return super.shouldOverrideUrlLoading(view, url);
            }
            if (onPageHeaders(url) != null) {
                view.loadUrl(url, onPageHeaders(url));
            }
        }
        return super.shouldOverrideUrlLoading(view, request);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
    }

    @Override
    public void onReceivedError(WebView view, int errorCode,
                                String description, String failingUrl) {
        isFile = true;
        fileUrl = failingUrl;
        Log.i("onReceivedError","url-->"+failingUrl);
        view.loadUrl(onPageError(failingUrl));
    }


    /**
     * return errorUrl
     * @param url
     * @return
     */
    public abstract String onPageError(String url);

    /**
     * HttpHeaders
     * return
     * @return
     */
    @NonNull
    public abstract Map<String, String> onPageHeaders(String url);
    
}
