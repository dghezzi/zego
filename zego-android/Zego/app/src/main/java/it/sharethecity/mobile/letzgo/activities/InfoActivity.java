package it.sharethecity.mobile.letzgo.activities;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import it.sharethecity.mobile.letzgo.BuildConfig;
import it.sharethecity.mobile.letzgo.R;
import it.sharethecity.mobile.letzgo.application.ApplicationController;
import it.sharethecity.mobile.letzgo.customviews.MyFontTextView;

public class InfoActivity extends ZegoBaseActivity {

    @Nullable
    @BindView(R.id.header)
    RelativeLayout headerLayout;

    @Nullable
    @BindView(R.id.app_version_text_view)
    MyFontTextView appVersionTextView;

    @Nullable
    @BindView(R.id.tutorial_button)
    MyFontTextView tutorialButton;

    @Nullable
    @BindView(R.id.faq_button)
    MyFontTextView faqButton;

    @Nullable
    @BindView(R.id.web_site_button)
    MyFontTextView webSiteButton;

    @Nullable
    @BindView(R.id.info_legali_button)
    MyFontTextView termConditionButton;

    @Nullable
    @BindView(R.id.privacy)
    MyFontTextView privacyButton;

    @Nullable
    @BindView(R.id.contact_us)
    MyFontTextView contactUsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        ButterKnife.bind(this);
        setUI();
        appVersionTextView.setText("Zego v. " + BuildConfig.VERSION_NAME);

    }

    private void setUI() {
        headerLayout.getLayoutParams().height = (int) ((HEADER_PERCENT_HEIGHT)* ApplicationController.getInstance().getScreenDimension().heightPixels);
        titleTextView.setText(getString(R.string.info));
        aheadTextView.setVisibility(View.INVISIBLE);
        aheadTextView.setEnabled(false);
    }

    @Optional
    @OnClick(R.id.back_button)
    public void onBackClick(){
        super.onBackPressed();
    }

    @Optional
    @OnClick(R.id.web_site_button)
     public void onWebSiteClick(){
        openUrl(getString(R.string.zego_web_site_url) + "/" + getWebSiteSupportedLanguage());
    }

    @Optional
    @OnClick(R.id.info_legali_button)
    public void onInfoLegaliClick(){
//        openUrl(getString(R.string.url_term_cond));
        openUrl(getTCurlByLanguage());
    }

    @Optional
    @OnClick(R.id.tutorial_button)
    public void onTutorialClick(){
        Intent i = new Intent(InfoActivity.this,TutorialActivity.class);
        startActivity(i);
    }

    @Optional
    @OnClick(R.id.faq_button)
    public void onFaqClick(){
        openUrl(getFaqByLanguage());
    }

    @Optional
    @OnClick(R.id.privacy)
    public void onPrivacyClick(){
//        openUrl(getString(R.string.url_privacy_policy));
        openUrl(getPrivacyPolicyByLanguage());
    }

    @Optional
    @OnClick(R.id.contact_us)
    public void onContactUsClick(){
        openEmailClient(getString(R.string.zego_support_email),"","");
    }


}
