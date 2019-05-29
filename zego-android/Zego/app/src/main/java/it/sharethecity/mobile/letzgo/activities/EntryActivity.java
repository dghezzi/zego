package it.sharethecity.mobile.letzgo.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import it.sharethecity.mobile.letzgo.R;
import it.sharethecity.mobile.letzgo.application.ApplicationController;


public class EntryActivity extends ZegoBaseActivity {

  private static final int SMS_PERMISSION_REQUEST_FOR_SIGN_UP = 4;
  private static final int SMS_PERMISSION_REQUEST_DOR_LOGIN = 5;

  @Nullable
  @BindView(R.id.logo)
  ImageView logoImageView;

  @Nullable
  @BindView(R.id.buttons_layout)
  LinearLayout buttonsLayout;

  @Nullable
  @BindView(R.id.login_button)
  Button loginButton;

  @Nullable
  @BindView(R.id.signup_button)
  Button signUpButton;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_entry);
    ButterKnife.bind(this);
    setUI();
    ApplicationController.getInstance().logOutsharedPreferences();
  }

  private void setUI() {
//    logoImageView.getLayoutParams().width = (int) (0.472f * ApplicationController.getInstance().getScreenDimension().widthPixels);
//    logoImageView.getLayoutParams().height = (int) (0.346f * ApplicationController.getInstance().getScreenDimension().heightPixels);
  //  ((RelativeLayout.LayoutParams)logoImageView.getLayoutParams()).topMargin = (int) (0.4f * ApplicationController.getInstance().getScreenDimension().heightPixels);
    ((RelativeLayout.LayoutParams)buttonsLayout.getLayoutParams()).bottomMargin = (int) (0.066f * ApplicationController.getInstance().getScreenDimension().heightPixels);
    signUpButton.getLayoutParams().height = (int) (0.1041f * ApplicationController.getInstance().getScreenDimension().heightPixels);
    loginButton.getLayoutParams().height = (int) (0.1041f * ApplicationController.getInstance().getScreenDimension().heightPixels);
  }

  @Optional
  @OnClick({R.id.signup_button,R.id.login_button})
  public void onClick(View v){
    switch (v.getId()){
      case R.id.signup_button:
        signUpAction();
        break;
      case R.id.login_button:
        loginAction();
        break;
    }
  }

  @Override
  public void onBackPressed() {
  }

  private void loginAction() {
    if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
            (ContextCompat.checkSelfPermission(getBaseContext(),Manifest.permission.RECEIVE_SMS)!=PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(getBaseContext(),Manifest.permission.READ_SMS)!=PackageManager.PERMISSION_GRANTED)){

      ActivityCompat.requestPermissions(EntryActivity.this, new String[]{Manifest.permission.RECEIVE_SMS,Manifest.permission.READ_SMS}, SMS_PERMISSION_REQUEST_DOR_LOGIN);
    }else{
      Intent i = new Intent(EntryActivity.this,LoginActivity.class);
      startActivity(i);
    }

  }

  private void signUpAction() {
    if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
            (ContextCompat.checkSelfPermission(getBaseContext(),Manifest.permission.RECEIVE_SMS)!=PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(getBaseContext(),Manifest.permission.READ_SMS)!=PackageManager.PERMISSION_GRANTED)){
      ActivityCompat.requestPermissions(EntryActivity.this, new String[]{Manifest.permission.RECEIVE_SMS,Manifest.permission.READ_SMS}, SMS_PERMISSION_REQUEST_FOR_SIGN_UP);
    }else{
      Intent i = new Intent(EntryActivity.this,SignUpTelNumberActivity.class);
      startActivity(i);
    }

  }

  @Override
  public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    switch (requestCode) {
      case SMS_PERMISSION_REQUEST_FOR_SIGN_UP:
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
          Intent i = new Intent(EntryActivity.this,SignUpTelNumberActivity.class);
          startActivity(i);
        } else {
         showInfoDialog(getString(R.string.warning),getString(R.string.permission_not_granted));
        }
        break;
      case SMS_PERMISSION_REQUEST_DOR_LOGIN:
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
          Intent i = new Intent(EntryActivity.this,LoginActivity.class);
          startActivity(i);
        } else {
          showInfoDialog(getString(R.string.warning),getString(R.string.permission_not_granted));
        }
        break;
    }
  }
}
