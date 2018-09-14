package com.tamic.jswebview.browse.JsWeb;

import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.tamic.jswebview.view.NumberProgressBar;


/**
 * Class description
 *
 * @author YEZHENNAN220
 * @date 2016-07-08 14:05
 */

public class CustomWebChromeClient extends WebChromeClient {

    private NumberProgressBar mProgressBar;
    private final static int DEF = 95;

    public interface CallListener {
        public void showTitle(String title);
    }
    private CallListener listener;

    public CustomWebChromeClient(NumberProgressBar progressBar) {
        this.mProgressBar = progressBar;
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        if (newProgress >= DEF) {
            mProgressBar.setVisibility(View.GONE);
        } else {
            if (mProgressBar.getVisibility() == View.GONE) {
                mProgressBar.setVisibility(View.VISIBLE);
            }

            mProgressBar.setProgress(newProgress);
        }
        super.onProgressChanged(view, newProgress);
    }

    @Override
    public void onReceivedTitle(WebView view, String title) {
        super.onReceivedTitle(view, title);

        if(listener != null){
            listener.showTitle(title);
        }
    }

    public void setOnCallListener(CallListener listener) {
        this.listener = listener;
    }
}
