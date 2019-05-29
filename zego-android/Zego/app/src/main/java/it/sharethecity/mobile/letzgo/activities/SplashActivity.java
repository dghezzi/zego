package it.sharethecity.mobile.letzgo.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.appsflyer.AppsFlyerLib;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;
import java.util.List;

import it.sharethecity.mobile.letzgo.R;
import it.sharethecity.mobile.letzgo.application.ApplicationController;
import it.sharethecity.mobile.letzgo.customviews.PopUpDialog;
import it.sharethecity.mobile.letzgo.dao.Conf;
import it.sharethecity.mobile.letzgo.dao.User;
import it.sharethecity.mobile.letzgo.network.NetworkManager;
import it.sharethecity.mobile.letzgo.network.interfaces.ConfRESTInterface;
import it.sharethecity.mobile.letzgo.network.interfaces.UserRESTInterface;
import it.sharethecity.mobile.letzgo.network.request.BootRequest;
import it.sharethecity.mobile.letzgo.network.request.ErrorObject;
import it.sharethecity.mobile.letzgo.network.utils.NetworkErrorHandler;
import it.sharethecity.mobile.letzgo.utilities.ZegoConstants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends ZegoBaseActivity {

  private static final int PERMISSIONS_REQUEST = 2;
  private static final int TUTORIAL_REQUEST = 43;
  public static final int TIME_DELAY_SPLASH = 2000;
  GoogleCloudMessaging gcm;
  String regid;
  String PROJECT_NUMBER = "493924624911";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_splash);
    AppsFlyerLib.getInstance().startTracking(this.getApplication(),"iaduS6foKokLycBsuNojri");

    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        launchZego();
//        if(ApplicationController.getInstance().isFirstAccess()){
//          Intent i = new Intent(SplashActivity.this,TutorialActivity.class);
//          startActivityForResult(i,TUTORIAL_REQUEST);
//        }else{
//          launchZego();
//        }
      }
    },TIME_DELAY_SPLASH);

  }


  private void launchZego(){
    ApplicationController.getInstance().saveMethodCash(0);
    if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(getBaseContext(),Manifest.permission.READ_PHONE_STATE)!=PackageManager.PERMISSION_GRANTED){
      ActivityCompat.requestPermissions(SplashActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, PERMISSIONS_REQUEST);
    }else{
      postConf();
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if(requestCode == TUTORIAL_REQUEST){
      ApplicationController.getInstance().setFirstAccess(false);
      launchZego();
    }
  }

  @Override
  public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    switch (requestCode) {
      case PERMISSIONS_REQUEST:
        // If request is cancelled, the result arrays are empty.
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
          postConf();
//          checkForSilentLogin();
        } else {
          finish();
        }
        break;
    }
  }

  private void checkForSilentLogin(){
    User u = ApplicationController.getInstance().getUserLogged();
    if(u != null){
      silentLogin(u);
    }else{
      Intent i  = new Intent(SplashActivity.this,EntryActivity.class);
      startActivity(i);
    }
  }

  private void silentLogin(User u) {
    BootRequest br = new BootRequest();
    if(ZegoConstants.DEBUG){
      Log.d("SilentLogin:",u.toString());
    }
    Call<User> call = NetworkManager.getInstance().getRetrofit().create(UserRESTInterface.class).silentLogin(u.getZegotoken(),br);
    call.enqueue(new Callback<User>() {
      @Override
      public void onResponse(Call<User> call, Response<User> response) {
        if(response.isSuccessful()){
          if(ZegoConstants.DEBUG){
            Log.d("SilentLoginUser:",response.body().toString());
          }
          ApplicationController.getInstance().saveUser(response.body());
          Intent i;
          if(response.body().getMobok() == 1){
            i = new Intent(SplashActivity.this,response.body().isDriver() ? DriverMainActivity.class: MainActivity.class);
          }
          else{
            i = new Intent(SplashActivity.this,VerificationPinActivity.class);
            i.putExtra(VerificationPinActivity.USER,response.body());
          }

          startActivity(i);
          finish();
        }else{
          ApplicationController.getInstance().logOutsharedPreferences();
          Intent i  = new Intent(SplashActivity.this,EntryActivity.class);
          startActivity(i);
          finish();
//          NetworkErrorHandler.getInstance().errorHandler(response,SplashActivity.this);
        }
      }

      @Override
      public void onFailure(Call<User> call, Throwable t) {
        showInfoDialog(getString(R.string.warning),getString(R.string.impossible_to_connetc_to_server));
      }
    });
  }

  private void postConf(){
    BootRequest btr = new BootRequest();
    Call<List<Conf>> call = NetworkManager.getInstance().getRetrofit().create(ConfRESTInterface.class).postConf(btr);
    call.enqueue(new Callback<List<Conf>>() {
      @Override
      public void onResponse(Call<List<Conf>> call, Response<List<Conf>> response) {
        if(response.isSuccessful()){
          ApplicationController.getInstance().saveConf(response.body());
          getRegId();
//          checkForSilentLogin();
        }else{
          if(response.code() == ZegoConstants.ApiRestConstants.ERROR_500){
            ErrorObject errorObject = NetworkErrorHandler.getInstance().decodeError(response);
            if(errorObject != null && errorObject.getCode() == ErrorObject.ERROR_CODE_DEVICE_BLACKLISTED){
              Intent i = new Intent(SplashActivity.this,EntryActivity.class);
              startActivity(i);
              finish();
            }else if(errorObject != null && errorObject.getCode() == ErrorObject.NEW_APP_AVAILABLE){
              PopUpDialog.showPopUpDialog(SplashActivity.this, getString(R.string.title_conf_dialog), getString(R.string.body_conf_dialog), getString(R.string.ok), 0, null, new PopUpDialog.DialogActionListener() {
                @Override
                public void actionListener() {
                  openGooglePlayMarket();
                  finish();
                }

                @Override
                public void negativeAction() {

                }
              });
            }
          }else{
            NetworkErrorHandler.getInstance().errorHandler(response,SplashActivity.this);
          }

        }
      }

      @Override
      public void onFailure(Call<List<Conf>> call, Throwable t) {
        showInfoDialogWithAction(getString(R.string.warning), getString(R.string.impossible_to_connetc_to_server), new InfoAction() {
          @Override
          public void onAction() {
            finish();
          }
        });
      }
    });
  }

  private void openGooglePlayMarket() {
    final String appPackageName = getPackageName();
    try {

      startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
    } catch (android.content.ActivityNotFoundException anfe) {
      startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
    }
  }

  public void getRegId(){
    new AsyncTask<Void, Void, String>() {
      @Override
      protected String doInBackground(Void... params) {
        String msg = "";
        try {
          if (gcm == null) {
            gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
          }
          regid = gcm.register(PROJECT_NUMBER);
          msg = "Device registered, registration ID=" + regid;
          Log.i("GCM",  msg);

        } catch (IOException ex) {
          msg = "Error :" + ex.getMessage();

        }
        return regid;
      }

      @Override
      protected void onPostExecute(String msg) {

        if(msg!=null)
        {
          ApplicationController.getInstance().savePushMsg(msg);
        }
        checkForSilentLogin();
      }
    }.execute(null, null, null);
  }
}
