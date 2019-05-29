package it.sharethecity.mobile.letzgo.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import butterknife.OnTextChanged;
import butterknife.Optional;
import it.sharethecity.mobile.letzgo.R;
import it.sharethecity.mobile.letzgo.adapters.CountryPrefixAdapter;
import it.sharethecity.mobile.letzgo.application.ApplicationController;
import it.sharethecity.mobile.letzgo.customviews.CellNumber;
import it.sharethecity.mobile.letzgo.customviews.CustomNumberKeyBoard;
import it.sharethecity.mobile.letzgo.customviews.ItalicRelewayTextView;
import it.sharethecity.mobile.letzgo.customviews.MyFontTextView;
import it.sharethecity.mobile.letzgo.customviews.RegularRelewayTextView;
import it.sharethecity.mobile.letzgo.dao.User;
import it.sharethecity.mobile.letzgo.network.NetworkManager;
import it.sharethecity.mobile.letzgo.network.interfaces.UserRESTInterface;
import it.sharethecity.mobile.letzgo.network.request.BootRequest;
import it.sharethecity.mobile.letzgo.network.request.ErrorObject;
import it.sharethecity.mobile.letzgo.network.utils.NetworkErrorHandler;
import it.sharethecity.mobile.letzgo.utilities.ZegoConstants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpTelNumberActivity extends ZegoBaseActivity {


    @Nullable
    @BindView(R.id.spinner_country)
    Spinner spinnerCountry;

    @Nullable
    @BindView(R.id.country_prefix_field)
    RegularRelewayTextView prefixTextView;

    @Nullable
    @BindView(R.id.cell_field)
    RegularRelewayTextView mobileNumberTextView;

    @Nullable
    @BindView(R.id.term_condition_text_view)
    RegularRelewayTextView termCondTextView;

    @Nullable
    @BindView(R.id.keyboard)
    CustomNumberKeyBoard keyBoard;

    @Nullable
    @BindView(R.id.avanti_button)
    Button avantiButton;

    @Nullable
    @BindView(R.id.header)
    RelativeLayout headerLayout;

    @Nullable
    @BindView(R.id.term_cond_checkbox)
    CheckBox termCondCheckBox;

    @Nullable
    @BindView(R.id.hint_error)
    MyFontTextView hintError;

    private String countryId = "it";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_tel_number);
        ButterKnife.bind(this);
        setUi();

        keyBoard.setListener(new CustomNumberKeyBoard.onKeyBoardListener() {
            @Override
            public void onKeyBoardClick(String buttonClick) {
                String s = mobileNumberTextView.getText().toString();
                if(s.length() == 3){
                    s += " ";
                }
                s +=  buttonClick;
                mobileNumberTextView.setText(s);

            }

            @Override
            public void onCancelClick() {
                if(mobileNumberTextView.getText().length() > 0){
                    String s = mobileNumberTextView.getText().toString();
                    s = s.substring(0,s.length() == 4 ? s.length() - 2 : s.length() - 1);
                    mobileNumberTextView.setText(s);
                }

            }
        });

        spinnerCountry.setAdapter(new CountryPrefixAdapter(this));
        spinnerCountry.setSelection(getCountryPositionByLanguage(ApplicationController.getInstance().getsDefSystemLanguage()));
        setTermCond();
    }

    private void setTermCond() {
        String wholeString = getString(R.string.term_condition);
        String clickableString = getString(R.string.service_term);
        String privacyPolicyString = getString(R.string.privacy_policy);
        Log.d("clickableString:",clickableString);
        Log.d("privacyPolicyString:",privacyPolicyString);
        SpannableString termSpannable = new SpannableString(getString(R.string.term_condition));

        int startIndex = wholeString.indexOf(clickableString);
        Log.d("privacyPolicyString:",privacyPolicyString);
        int policyStartIndex = wholeString.indexOf(privacyPolicyString);
        termSpannable.setSpan(new MyClickableSpan(),startIndex,startIndex + clickableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        termSpannable.setSpan(new MyClickableSpan(),policyStartIndex,policyStartIndex + privacyPolicyString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

//        termSpannable.setSpan(new MyClickableSpan() {
//            public void updateDrawState(TextPaint ds) {
////                ds.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
//                //ds.setColor(ContextCompat.getColor(getBaseContext(),R.color.darker_green_text));
//                ds.setUnderlineText(true);
//            }
//            @Override
//            public void onClick(View view) {
//                TextView tv = (TextView) view;
//                Spanned s = (Spanned) tv.getText();
//                int start = s.getSpanStart(this);
//                int end = s.getSpanEnd(this);
//                String theWord = s.subSequence(start + 1, end).toString();
//                if(theWord.equalsIgnoreCase(getString(R.string.privacy_policy))){
//                    openUrl(getString(R.string.url_privacy_policy));
//                }else{
//                    openUrl(getString(R.string.url_term_cond));
//                }
//
//            }
//        },startIndex,wholeString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);



        termCondTextView.setText(termSpannable);
        termCondTextView.setMovementMethod(LinkMovementMethod.getInstance());
        termCondTextView.setHighlightColor(Color.TRANSPARENT);
    }

    private void setUi() {
        headerLayout.getLayoutParams().height = (int) ((HEADER_PERCENT_HEIGHT)* ApplicationController.getInstance().getScreenDimension().heightPixels);
        avantiButton.getLayoutParams().height = (int) ((BOTTOM_BUTTON_PERCENT_HEIGHT)*ApplicationController.getInstance().getScreenDimension().heightPixels);

        avantiButton.setEnabled(false);
        titleTextView.setText(getString(R.string.signup));

    }

    @Optional
    @OnTextChanged(R.id.cell_field)
    public void onTextChanged(){
        changeAvantiButtonState();
    }

    @Optional
    @OnCheckedChanged(R.id.term_cond_checkbox)
    public void onTermCheck(){
        changeAvantiButtonState();
    }

    @Optional
    @OnClick(R.id.back_button)
    public void onBackClick(){
        super.onBackPressed();
    }

    @Optional
    @OnClick(R.id.avanti_button)
    public void onAvantiClick(){
        BootRequest bt = new BootRequest();
        bt.setMobile(mobileNumberTextView.getText().toString());
        bt.setPrefix( prefixTextView.getText().toString());
//        bt.setCountry(getResources().getStringArray(R.array.country_id)[getCountryPosition(prefix)]);
        bt.setCountry(countryId);
       Call<User> call =  NetworkManager.getInstance().getRetrofit().create(UserRESTInterface.class).signUp(bt);
        showOrDismissProgressWheel(SHOW);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                showOrDismissProgressWheel(DISMISS);
                if(response.isSuccessful()){
                    Intent i = new Intent(SignUpTelNumberActivity.this,VerificationPinActivity.class);
                    i.putExtra(VerificationPinActivity.USER,response.body());
                    startActivity(i);
                }else{
                    if(response.code() == ZegoConstants.ApiRestConstants.ERROR_500){
                        int errorColor = ContextCompat.getColor(getBaseContext(),R.color.red_error);
                        ErrorObject errorObject = NetworkErrorHandler.getInstance().decodeError(response);
                        hintError.setText(errorObject != null ? errorObject.getMsg() : getString(R.string.unknown_error));
                        hintError.setTextColor(errorColor);
                    }else{
                        NetworkErrorHandler.getInstance().errorHandler(response,SignUpTelNumberActivity.this);
                    }

                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                showOrDismissProgressWheel(DISMISS);
                showInfoDialog(getString(R.string.warning),getString(R.string.impossible_to_connetc_to_server));
            }
        });
    }

    private void changeAvantiButtonState(){
        boolean isNumberOk = false;
        if(mobileNumberTextView.getText().length() >= (ZegoConstants.MIN_NUMBER_DIGITS + 1)  &&
                mobileNumberTextView.getText().length() <= (ZegoConstants.MAX_NUMBER_DIGITS + 1) ){
            isNumberOk = true;

        }
//        else{
//            avantiButton.setEnabled(false);
//            avantiButton.setBackgroundColor(ContextCompat.getColor(getBaseContext(),R.color.gray_button));
//        }

        hintError.setTextColor(ContextCompat.getColor(getBaseContext(),isNumberOk ? R.color.gray_text : R.color.red_error));
        hintError.setText(getString(isNumberOk ? R.string.hint_sms : R.string.hint_wrong_number));

        avantiButton.setEnabled(isNumberOk && termCondCheckBox.isChecked());
        avantiButton.setBackground(ContextCompat.getDrawable(getBaseContext(),(isNumberOk && termCondCheckBox.isChecked()) ? R.drawable.green_button_selector : R.color.gray_button));
    }


    @Optional
    @OnItemSelected(R.id.spinner_country)
    public void spinnerItemSelected(Spinner spinner, int position) {
        prefixTextView.setText(((CountryPrefixAdapter) spinner.getAdapter()).getCountryPrefix()[position]);
        countryId = getResources().getStringArray(R.array.country_id)[position];
    }


    private class MyClickableSpan extends ClickableSpan{

        MyClickableSpan(){

        }

        @Override
        public void onClick(View view) {
            TextView tv = (TextView) view;
            Spanned s = (Spanned) tv.getText();
            int start = s.getSpanStart(this);
            int end = s.getSpanEnd(this);
            String theWord = s.subSequence(start , end).toString();
            Log.d("Clicked:",theWord);
            if(theWord.equalsIgnoreCase(getString(R.string.privacy_policy))){

               // openUrl(getString(R.string.url_privacy_policy));
                openUrl(getPrivacyPolicyByLanguage());
            }else{
//                openUrl(getString(R.string.url_term_cond));
                openUrl(getTCurlByLanguage());
            }
        }

        public void updateDrawState(TextPaint ds) {
//                ds.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            //ds.setColor(ContextCompat.getColor(getBaseContext(),R.color.darker_green_text));
            ds.setUnderlineText(true);
        }
    }
}
