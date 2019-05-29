package it.sharethecity.mobile.letzgo.activities;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import java.io.StringBufferInputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import it.sharethecity.mobile.letzgo.R;
import it.sharethecity.mobile.letzgo.application.ApplicationController;
import it.sharethecity.mobile.letzgo.utilities.ZegoConstants;

public class TutorialActivity extends ZegoBaseActivity {

    @Nullable
    @BindView(R.id.close_tutorial_button)
    Button closeTutorialButton;

    @Nullable
    @BindView(R.id.web_view_tutorial)
    WebView tutorialWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
        ButterKnife.bind(this);

        tutorialWebView.setWebViewClient(new MyWebViewClient());
        tutorialWebView.getSettings().setJavaScriptEnabled(true);
        tutorialWebView.loadUrl(getTutorialUrlByLanguage());

    }

    private String getTutorialUrlByLanguage(){
        String lang = ApplicationController.getInstance().getsDefSystemLanguage();
        if(lang == null || lang.isEmpty() || !lang.equalsIgnoreCase("it")){
            lang = "en";
        }
        if(ZegoConstants.DEBUG){
            Log.d("Tutorial url:", getString(R.string.tutorial_url).replace("{lang}",lang + "and"));
        }
        return getString(R.string.tutorial_url).replace("{lang}",lang + "and");
    }

    @Optional
    @OnClick(R.id.close_tutorial_button)
    public void oncloseTutorial(){
        finish();
    }


    private class MyWebViewClient extends WebViewClient {
        @SuppressWarnings("deprecation")
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @RequiresApi(Build.VERSION_CODES.N)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            final Uri uri = request.getUrl();
            view.loadUrl(uri.toString());
            return true;
        }

//        @Override
//        public WebResourceResponse shouldInterceptRequest(final WebView view, String url) {
//
//
//
//            return super.shouldInterceptRequest(view, url);
//
//        }

        @Override
        public void onPageFinished(WebView view, String url) {
            progressWheel.setVisibility(View.GONE);

            super.onPageFinished(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            progressWheel.setVisibility(View.VISIBLE);
            super.onPageStarted(view, url, favicon);
        }
    }


}
