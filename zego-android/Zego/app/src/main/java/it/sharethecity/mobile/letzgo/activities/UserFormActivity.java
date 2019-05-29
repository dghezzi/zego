package it.sharethecity.mobile.letzgo.activities;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import butterknife.Optional;
import it.sharethecity.mobile.letzgo.R;
import it.sharethecity.mobile.letzgo.application.ApplicationController;
import it.sharethecity.mobile.letzgo.customviews.ItalicRelewayTextView;
import it.sharethecity.mobile.letzgo.customviews.RegularRelewayEditTextView;
import it.sharethecity.mobile.letzgo.dao.User;
import it.sharethecity.mobile.letzgo.network.NetworkManager;
import it.sharethecity.mobile.letzgo.network.interfaces.UserRESTInterface;
import it.sharethecity.mobile.letzgo.network.request.ErrorObject;
import it.sharethecity.mobile.letzgo.network.utils.NetworkErrorHandler;
import it.sharethecity.mobile.letzgo.utilities.UtilityFunctions;
import it.sharethecity.mobile.letzgo.utilities.ZegoConstants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserFormActivity extends ZegoBaseActivity {

    public static final String USER = "user";
    private static final int PERMISSIONS_REQUEST = 3;
    @Nullable
    @BindView(R.id.header)
    RelativeLayout headerLayout;

    @Nullable
    @BindView(R.id.name_edit_text)
    RegularRelewayEditTextView nameEditTex;

    @Nullable
    @BindView(R.id.surname_edit_text)
    RegularRelewayEditTextView surnameEditTex;

    @Nullable
    @BindView(R.id.email_edit_text)
    RegularRelewayEditTextView emailEditTex;

    @Nullable
    @BindView(R.id.fb_button)
    LinearLayout completeWithFbButton;

    @Nullable
    @BindView(R.id.form_layout)
    LinearLayout formLayout;

    @Nullable
    @BindView(R.id.avanti_button)
    Button avantiButton;

    @Nullable
    @BindView(R.id.hint_error)
    ItalicRelewayTextView hintError;

    private User user;
    private boolean isEmailOk;
    private CallbackManager callbackManager;
    private String fbid = "";
    private String gender = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_form);
        ButterKnife.bind(this);
        setUI();
        setCallbackManagerFacebook();
        user = (User) getIntent().getExtras().getSerializable(USER);
        getUserAccount();

    }

    private void getUserAccount() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(UserFormActivity.this, new String[]{Manifest.permission.GET_ACCOUNTS}, PERMISSIONS_REQUEST);
        }else{
            findAccount();
        }

    }

    private void findAccount(){
        Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
        Account[] accounts = AccountManager.get(this).getAccounts();
        for (Account account : accounts) {
            if (emailPattern.matcher(account.name).matches()) {
                String possibleEmail = account.name;
                if(possibleEmail.contains("@gmail")){
                    emailEditTex.setText(possibleEmail);
                    return;
                }

            }
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSIONS_REQUEST:
                // If request is cancelled, the result arrays are empty.
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    findAccount();
                }
                break;
        }
    }

    private void setUI() {
        headerLayout.getLayoutParams().height = (int) ((HEADER_PERCENT_HEIGHT)* ApplicationController.getInstance().getScreenDimension().heightPixels);
        avantiButton.getLayoutParams().height = (int) ((BOTTOM_BUTTON_PERCENT_HEIGHT)*ApplicationController.getInstance().getScreenDimension().heightPixels);
        completeWithFbButton.getLayoutParams().height = (int) ((0.079f)*ApplicationController.getInstance().getScreenDimension().heightPixels);
        ((LinearLayout.LayoutParams)completeWithFbButton.getLayoutParams()).topMargin = (int) ((0.061f)*ApplicationController.getInstance().getScreenDimension().heightPixels);
        ((RelativeLayout.LayoutParams)formLayout.getLayoutParams()).topMargin = (int) ((0.068f)*ApplicationController.getInstance().getScreenDimension().heightPixels);
        formLayout.getLayoutParams().width = (int) ((0.82f)*ApplicationController.getInstance().getScreenDimension().widthPixels);
        formLayout.getLayoutParams().height = (int) ((0.5187f)*ApplicationController.getInstance().getScreenDimension().heightPixels);


        avantiButton.setEnabled(false);
        titleTextView.setText(getString(R.string.complete_title));
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Optional
    @OnClick(R.id.back_button)
    public void onBackClick(){
        super.onBackPressed();
    }

    @Optional
    @OnTextChanged(R.id.email_edit_text)
    public void onEmailTextChanged(){
        isEmailOk = UtilityFunctions.mailSyntaxCheck(emailEditTex.getText().toString());
        emailEditTex.setTextColor(ContextCompat.getColor(getBaseContext(),isEmailOk ? R.color.gray_keyboard_button : R.color.red_error));
        hintError.setText("");
        changeAvantiButtonState();
    }

    @Optional
    @OnTextChanged(R.id.name_edit_text)
    public void onNameTextChanged(){
        changeAvantiButtonState();
    }


    @Optional
    @OnTextChanged(R.id.surname_edit_text)
    public void onSurNameTextChanged(){

        changeAvantiButtonState();
    }


    @Optional
    @OnClick(R.id.avanti_button)
    public void onAvantiClick(View v){
        if(isFormOk()){
            completeInfo(false);
        }
    }

    private boolean isFormOk(){
       return isEmailOk && nameEditTex.getText().length() > 0 && surnameEditTex.getText().length() > 0;
    }

    @Optional
    @OnClick(R.id.complete_with_fb)
    public void onCompleteFbClick(View v){
        if(appInstalledOrNot(ZegoConstants.FACEBOOK_APP)){
            showOrDismissProgressWheel(SHOW);
            LoginManager.getInstance().logInWithReadPermissions(UserFormActivity.this, Arrays.asList("public_profile", "email"));
        }else{
            showInfoDialog(getString(R.string.warning),getString(R.string.facebook_not_installed));
        }

    }

    private void completeInfo(boolean isFromFB) {
        user.setFname(nameEditTex.getText().toString());
        user.setLname(surnameEditTex.getText().toString());
        user.setEmail(emailEditTex.getText().toString());
        if(isFromFB){
            user.setGender(gender);
            user.setFbid(fbid);
        }

        Call<User> call = NetworkManager.getInstance().getRetrofit().create(UserRESTInterface.class).completeUser(user.getZegotoken(),user);
        showOrDismissProgressWheel(SHOW);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                showOrDismissProgressWheel(DISMISS);
                if(response.isSuccessful()){
                    ApplicationController.getInstance().saveUser(response.body());
                    Intent i = new Intent(UserFormActivity.this,PromoCodeActivity.class);
                    startActivity(i);
                }else{
                    fbid = "";
                    gender = "";
                    if(response.code() == ZegoConstants.ApiRestConstants.ERROR_500){
                        int errorColor = ContextCompat.getColor(getBaseContext(),R.color.red_error);
                        ErrorObject errorObject = NetworkErrorHandler.getInstance().decodeError(response);
                        hintError.setText(errorObject != null ? errorObject.getMsg() : getString(R.string.unknown_error));
                        hintError.setTextColor(errorColor);
                    }else{
                        NetworkErrorHandler.getInstance().errorHandler(response,UserFormActivity.this);
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
        boolean isOk = isFormOk();
        avantiButton.setEnabled(isOk);
        avantiButton.setBackground(ContextCompat.getDrawable(getBaseContext(),isOk ? R.drawable.green_button_selector : R.color.gray_button));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void setCallbackManagerFacebook(){
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                signUpFromFacebook(loginResult);
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

    private void signUpFromFacebook(LoginResult loginResult){
        GraphRequest request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {
                        User u =  readUserFromFacebook(object);
                        LoginManager.getInstance().logOut();
                        setFormFromFacebook(u);
                    }
                });
        Bundle parameters = new Bundle();

        //Add the fields that you need, you dont forget add the right permission
        parameters.putString("fields", "email,id,first_name,last_name,gender");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void setFormFromFacebook(User u) {
        nameEditTex.setText(u.getFname());
        surnameEditTex.setText(u.getLname());
        emailEditTex.setText(u.getEmail());
        fbid = u.getFbid();
        gender = u.getGender();
//        nameEditTex.setEnabled(false);
//        surnameEditTex.setEnabled(false);
//        emailEditTex.setEnabled(false);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showOrDismissProgressWheel(DISMISS);
                completeInfo(true);
            }
        },1000);
    }
//

}
