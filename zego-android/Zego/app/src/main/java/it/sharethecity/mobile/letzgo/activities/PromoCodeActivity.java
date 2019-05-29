package it.sharethecity.mobile.letzgo.activities;

import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import butterknife.Optional;
import it.sharethecity.mobile.letzgo.R;
import it.sharethecity.mobile.letzgo.application.ApplicationController;
import it.sharethecity.mobile.letzgo.customviews.ItalicRelewayEditTextView;
import it.sharethecity.mobile.letzgo.customviews.ItalicRelewayTextView;
import it.sharethecity.mobile.letzgo.customviews.MyFontEditText;
import it.sharethecity.mobile.letzgo.dao.User;
import it.sharethecity.mobile.letzgo.dao.Userpromo;
import it.sharethecity.mobile.letzgo.network.NetworkManager;
import it.sharethecity.mobile.letzgo.network.interfaces.UserRESTInterface;
import it.sharethecity.mobile.letzgo.network.request.ErrorObject;
import it.sharethecity.mobile.letzgo.network.request.RedeemRequest;
import it.sharethecity.mobile.letzgo.network.request.ReferralRequest;
import it.sharethecity.mobile.letzgo.network.utils.NetworkErrorHandler;
import it.sharethecity.mobile.letzgo.utilities.FontManager;
import it.sharethecity.mobile.letzgo.utilities.ZegoConstants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PromoCodeActivity extends ZegoBaseActivity {


    private static final int MIN_PROMO_CODE_LENGTH = 0;
    @Nullable
    @BindView(R.id.avanti_button)
    Button avantiButton;

    @Nullable
    @BindView(R.id.header)
    RelativeLayout headerLayout;

    @Nullable
    @BindView(R.id.promo_layout)
    LinearLayout promoLayout;

    @Nullable
    @BindView(R.id.promo_code_editText)
    EditText promoCodeEditText;


    @Nullable
    @BindView(R.id.hint_error)
    ItalicRelewayTextView hintError;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promo_code);
        ButterKnife.bind(this);
        if(getIntent().getExtras() != null)
            fromActivity = getIntent().getExtras().getString(FROM_ACTIVITY);

        setUI();
    }

    private void setUI() {
        headerLayout.getLayoutParams().height = (int) ((HEADER_PERCENT_HEIGHT)* ApplicationController.getInstance().getScreenDimension().heightPixels);
        avantiButton.getLayoutParams().height = (int) ((BOTTOM_BUTTON_PERCENT_HEIGHT)*ApplicationController.getInstance().getScreenDimension().heightPixels);
        promoLayout.getLayoutParams().height = (int) ((0.1f)*ApplicationController.getInstance().getScreenDimension().heightPixels);
        ((RelativeLayout.LayoutParams)promoLayout.getLayoutParams()).topMargin = (int) ((0.083f)*ApplicationController.getInstance().getScreenDimension().heightPixels);

        avantiButton.setEnabled(false);

        titleTextView.setText(getString(R.string.promo_code));
        aheadTextView.setText(getString(R.string.skip));

        aheadTextView.setVisibility(fromActivity == null ? View.VISIBLE : View.INVISIBLE);
        backButton.setVisibility(fromActivity == null ? View.INVISIBLE : View.VISIBLE);

        promoCodeEditText.setHint(getString(R.string.promo_code_hint));
        promoCodeEditText.setTypeface(FontManager.getInstance(getBaseContext()).getFont("Raleway-Italic.ttf"));

        //aheadTextView.setVisibility(fromActivity != null && fromActivity.equalsIgnoreCase(ZegoNavBaseActivity.class.getSimpleName()) ? View.GONE : View.VISIBLE);
    }


    @Optional
    @OnClick(R.id.back_button)
    public void onBackClick(){
        if(fromActivity != null){
            super.onBackPressed();
        }
    }


    @Override
    public void onBackPressed() {
        if(fromActivity != null){
            super.onBackPressed();
        }
    }

    @Optional
    @OnClick(R.id.right_text_view)
    public void onSkipClick(){
        goToPaymentActivity();
    }

    @Optional
    @OnClick(R.id.avanti_button)
    public void onAvantiClick(){
        User u = ApplicationController.getInstance().getUserLogged();
        if(u != null){
            if(fromActivity ==  null){
                sendPromoCode(u.getZegotoken(),promoCodeEditText.getText().toString());
            }else{
                addPromo(u,promoCodeEditText.getText().toString());
            }

        }

    }

    private void addPromo(User u, String s) {
        RedeemRequest redeemRequest = new RedeemRequest();
        redeemRequest.setCode(s);
        redeemRequest.setUid(u.getId());

        Call<List<Userpromo>> call = NetworkManager.getInstance().getRetrofit().create(UserRESTInterface.class).postAddPromo(u.getZegotoken(),redeemRequest);
        showOrDismissProgressWheel(SHOW);
        call.enqueue(new Callback<List<Userpromo>>() {
            @Override
            public void onResponse(Call<List<Userpromo>> call, Response<List<Userpromo>> response) {
                showOrDismissProgressWheel(DISMISS);
                if(response.isSuccessful()){
                    Intent i = new Intent();
                    i.putExtra(MyPromoActivity.MY_PROMOS, (Serializable) response.body());
                    setResult(RESULT_OK,i);
                    finish();
                }else{
                    if(response.code() == ZegoConstants.ApiRestConstants.ERROR_500){
                        int errorColor = ContextCompat.getColor(getBaseContext(),R.color.red_error);
                        ErrorObject errorObject = NetworkErrorHandler.getInstance().decodeError(response);
                        hintError.setText(errorObject != null ? errorObject.getMsg() : getString(R.string.unknown_error));
                        hintError.setTextColor(errorColor);
                    }else{
                        NetworkErrorHandler.getInstance().errorHandler(response,PromoCodeActivity.this);
                    }

                }
            }

            @Override
            public void onFailure(Call<List<Userpromo>> call, Throwable t) {
                showOrDismissProgressWheel(DISMISS);
                showInfoDialog(getString(R.string.warning),getString(R.string.impossible_to_connetc_to_server));
            }
        });
    }


    private void sendPromoCode(final String token, String promoCode){
        ReferralRequest rfr = new ReferralRequest();
        rfr.setReferral(promoCode);
        Call<User> call = NetworkManager.getInstance().getRetrofit().create(UserRESTInterface.class).promoCode(token,rfr);
        showOrDismissProgressWheel(SHOW);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                showOrDismissProgressWheel(DISMISS);
                if(response.isSuccessful()){
                    goToPaymentActivity();
                }else{
                    if(response.code() == ZegoConstants.ApiRestConstants.ERROR_500){
                        int errorColor = ContextCompat.getColor(getBaseContext(),R.color.red_error);
                        ErrorObject errorObject = NetworkErrorHandler.getInstance().decodeError(response);
                        hintError.setText(errorObject != null ? errorObject.getMsg() : getString(R.string.unknown_error));
                        hintError.setTextColor(errorColor);
                    }else{
                        NetworkErrorHandler.getInstance().errorHandler(response,PromoCodeActivity.this);
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

    @Optional
    @OnTextChanged(R.id.promo_code_editText)
    public void onPromoTextChanged(){
        changeAvantiButtonState();
    }


    private void changeAvantiButtonState() {

        boolean isOK = promoCodeEditText.getText().length() > MIN_PROMO_CODE_LENGTH;
        avantiButton.setEnabled(isOK);
        avantiButton.setBackground(ContextCompat.getDrawable(getBaseContext(), isOK ? R.drawable.green_button_selector :  R.color.gray_button));
//        if (promoCodeEditText.getText().length() > MIN_PROMO_CODE_LENGTH) {
//            avantiButton.setEnabled(true);
//            avantiButton.setBackground(ContextCompat.getDrawable(getBaseContext(), R.drawable.green_button_selector));
//        } else {
//            avantiButton.setEnabled(false);
//            avantiButton.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.gray_button));
//        }

        hintError.setText("");
    }

    private void goToPaymentActivity(){
        Intent i = new Intent(PromoCodeActivity.this,PaymentMethodActivity.class);
        startActivity(i);
    }
}
