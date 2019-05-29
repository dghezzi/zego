package it.sharethecity.mobile.letzgo.activities;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import butterknife.OnTextChanged;
import butterknife.Optional;
import it.sharethecity.mobile.letzgo.R;
import it.sharethecity.mobile.letzgo.adapters.CountryPrefixAdapter;
import it.sharethecity.mobile.letzgo.application.ApplicationController;
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

public class LoginActivity extends ZegoBaseActivity {


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
    @BindView(R.id.keyboard)
    CustomNumberKeyBoard keyBoard;

    @Nullable
    @BindView(R.id.avanti_button)
    Button avantiButton;

    @Nullable
    @BindView(R.id.header)
    RelativeLayout headerLayout;

    @Nullable
    @BindView(R.id.fb_button)
    LinearLayout fbButton;

    @Nullable
    @BindView(R.id.hint_error)
    MyFontTextView hintError;

    private String countryId = "it";
    private String fbId;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        setUI();
        setCallbackManagerFacebook();
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
                if (mobileNumberTextView.getText().length() > 0) {
                    String s = mobileNumberTextView.getText().toString();
                    s = s.substring(0,s.length() == 4 ? s.length() - 2 : s.length() - 1);
                    mobileNumberTextView.setText(s);
                }

            }
        });

        spinnerCountry.setAdapter(new CountryPrefixAdapter(this));
        spinnerCountry.setSelection(getCountryPositionByLanguage(ApplicationController.getInstance().getsDefSystemLanguage()));

    }


    @Nullable
    @OnTextChanged(R.id.cell_field)
    public void onTextChanged() {
        changeAvantiButtonState();
    }

    @Optional
    @OnClick(R.id.avanti_button)
    public void onAvantiClick(){
        login(false);
    }

    @Optional
    @OnClick(R.id.fb_button)
    public void onFbButtonClick(){
        showOrDismissProgressWheel(SHOW);
        LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile", "email"));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Optional
    @OnItemSelected(R.id.spinner_country)
    public void spinnerItemSelected(Spinner spinner, int position) {
        prefixTextView.setText(((CountryPrefixAdapter) spinner.getAdapter()).getCountryPrefix()[position]);
        countryId = getResources().getStringArray(R.array.country_id)[position];
    }

    private void setUI() {
        headerLayout.getLayoutParams().height = (int) ((HEADER_PERCENT_HEIGHT) * ApplicationController.getInstance().getScreenDimension().heightPixels);
        avantiButton.getLayoutParams().height = (int) ((BOTTOM_BUTTON_PERCENT_HEIGHT) * ApplicationController.getInstance().getScreenDimension().heightPixels);
        avantiButton.setEnabled(false);
        titleTextView.setText(getString(R.string.login));
    }


    private void changeAvantiButtonState() {
        boolean isNumberOk = false;
        if(mobileNumberTextView.getText().length() >= (ZegoConstants.MIN_NUMBER_DIGITS + 1 ) &&
                mobileNumberTextView.getText().length() <= (ZegoConstants.MAX_NUMBER_DIGITS + 1) ){
            avantiButton.setEnabled(true);
            isNumberOk = true;
            avantiButton.setBackground(ContextCompat.getDrawable(getBaseContext(),R.drawable.green_button_selector));
        }else{
            avantiButton.setEnabled(false);
            avantiButton.setBackgroundColor(ContextCompat.getColor(getBaseContext(),R.color.gray_button));
        }

        hintError.setTextColor(ContextCompat.getColor(getBaseContext(),isNumberOk ? R.color.gray_text : R.color.red_error));
        hintError.setText(getString(isNumberOk ? R.string.hint_sms : R.string.hint_wrong_number));
    }

    @Optional
    @OnClick(R.id.back_button)
    public void onBackClick(){
        super.onBackPressed();
    }

    private void login(final boolean isFromFb){
        BootRequest btr = new BootRequest();
        if(fbId != null && !fbId.isEmpty()){
            btr.setFbid(fbId);
        }else{
            btr.setMobile(mobileNumberTextView.getText().toString());
            btr.setPrefix(prefixTextView.getText().toString());
            btr.setCountry(countryId);
        }


        Call<User> call = NetworkManager.getInstance().getRetrofit().create(UserRESTInterface.class).login(btr);
        showOrDismissProgressWheel(SHOW);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                showOrDismissProgressWheel(DISMISS);
                if(response.isSuccessful()){
                    Intent i = null;
                    if(isFromFb){
                        ApplicationController.getInstance().saveUser(response.body());
                        i = new Intent(LoginActivity.this,response.body().isDriver() ? DriverMainActivity.class : MainActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    }else{
                        i = new Intent(LoginActivity.this,VerificationPinActivity.class);
                        i.putExtra(VerificationPinActivity.USER,response.body());
                    }
                    startActivity(i);
                    finish();
                }else{
                    fbId = null;
                    if(response.code() == ZegoConstants.ApiRestConstants.ERROR_500){
                        int errorColor = ContextCompat.getColor(getBaseContext(),R.color.red_error);
                        ErrorObject errorObject = NetworkErrorHandler.getInstance().decodeError(response);
                        hintError.setText(errorObject != null ? errorObject.getMsg() : getString(R.string.unknown_error));
                        hintError.setTextColor(errorColor);
                    }else{
                        NetworkErrorHandler.getInstance().errorHandler(response,LoginActivity.this);
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

    private void setCallbackManagerFacebook(){
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                showOrDismissProgressWheel(DISMISS);
                 fbId = loginResult.getAccessToken().getUserId();
                if(fbId != null){
                    login(true);
                }else{
                    // TODO: 15/11/16 mostra dialog fallimento login fb
                }
            }

            @Override
            public void onCancel() {
                showOrDismissProgressWheel(DISMISS);
            }

            @Override
            public void onError(FacebookException e) {
                showOrDismissProgressWheel(DISMISS);
            }
        });

    }


}
