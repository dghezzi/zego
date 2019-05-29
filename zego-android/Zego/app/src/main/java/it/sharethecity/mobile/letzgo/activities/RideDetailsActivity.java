package it.sharethecity.mobile.letzgo.activities;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.RelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import it.sharethecity.mobile.letzgo.R;
import it.sharethecity.mobile.letzgo.application.ApplicationController;
import it.sharethecity.mobile.letzgo.network.request.CompatRequest;
import it.sharethecity.mobile.letzgo.utilities.ZegoConstants;

public class RideDetailsActivity extends ZegoBaseActivity {

    public static final String SHID = "shid";
    public static final String UMODE = "mode";
    public static final String RIDE = "ride";

    @Nullable
    @BindView(R.id.header)
    RelativeLayout headerLayout;

    @Nullable
    @BindView(R.id.support_button)
    Button supportButton;

    @Nullable
    @BindView(R.id.web_view_ride_details)
    WebView rideDetailsWebView;

    private CompatRequest cmpReq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_details);
        ButterKnife.bind(this);
        setUI();
        user = ApplicationController.getInstance().getUserLogged();
        if(getIntent().getExtras() != null && user != null){
            rideDetailsWebView.setWebViewClient(new RideDetailsActivity.MyWebViewClient());
            rideDetailsWebView.getSettings().setJavaScriptEnabled(true);
            cmpReq = (CompatRequest) getIntent().getExtras().getSerializable(RIDE);

            String mode = getIntent().getExtras().getString(UMODE);
            titleTextView.setText(getString(R.string.ride_details_title) + cmpReq.getShortid());
            if(ZegoConstants.DEBUG){
                Log.d("URL_RIDE_DETAILS:", getString(R.string.url_ride_details)  + mode + "/"+ cmpReq.getShid() + "/" + user.getZegotoken());
            }
            rideDetailsWebView.loadUrl(getString(R.string.url_ride_details)  + mode + "/"+ cmpReq.getShid() + "/" + user.getZegotoken());
        }else {
            finish();
            return;
        }

    }

    private void setUI() {
        headerLayout.getLayoutParams().height = (int) ((HEADER_PERCENT_HEIGHT)* ApplicationController.getInstance().getScreenDimension().heightPixels);
        aheadTextView.setVisibility(View.INVISIBLE);
        aheadTextView.setEnabled(false);
    }

    @Optional
    @OnClick(R.id.back_button)
    public void onBackClick(){
        super.onBackPressed();
    }


    @Optional
    @OnClick(R.id.support_button)
    public void onSupportClick(){
        if(cmpReq != null){
            openEmailClient(getString(R.string.zego_support_email),getString(R.string.subject_email_ride_support) + " " + cmpReq.getShortid(), "");
        }
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
