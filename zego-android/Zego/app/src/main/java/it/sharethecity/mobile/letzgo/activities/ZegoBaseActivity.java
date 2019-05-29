package it.sharethecity.mobile.letzgo.activities;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.Browser;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.google.android.gms.maps.model.LatLng;
import com.pnikosis.materialishprogress.ProgressWheel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import it.sharethecity.mobile.letzgo.R;
import it.sharethecity.mobile.letzgo.application.ApplicationController;
import it.sharethecity.mobile.letzgo.bus.BusRequestMessage;
import it.sharethecity.mobile.letzgo.customviews.PopUpDialog;
import it.sharethecity.mobile.letzgo.customviews.ProgressHud;
import it.sharethecity.mobile.letzgo.dao.User;
import it.sharethecity.mobile.letzgo.network.NetworkManager;
import it.sharethecity.mobile.letzgo.network.interfaces.UserRESTInterface;
import it.sharethecity.mobile.letzgo.network.request.ErrorObject;
import it.sharethecity.mobile.letzgo.network.request.Riderequest;
import it.sharethecity.mobile.letzgo.network.utils.NetWorkErrorInterface;
import it.sharethecity.mobile.letzgo.services.PollingService;
import it.sharethecity.mobile.letzgo.utilities.ZegoConstants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ZegoBaseActivity extends AppCompatActivity implements NetWorkErrorInterface {

  public static final float HEADER_PERCENT_HEIGHT = 0.104f;
  public static final float BOTTOM_BUTTON_PERCENT_HEIGHT = 0.081f;
  public static final String FROM_ACTIVITY = "from_activity";
  public static final boolean SHOW = true;
  public static final boolean DISMISS = false;
  public static final int LOW_RATE_THRESHOULD = 2;

  @Nullable
  @BindView(R.id.title_text_view)
  TextView titleTextView;

  @Nullable
  @BindView(R.id.back_button)
  ImageButton backButton;

  @Nullable
  @BindView(R.id.right_text_view)
  TextView aheadTextView;

  @Nullable
  @BindView(R.id.progress_wheel)
  ProgressWheel progressWheel;

  protected String fromActivity;
  protected User user;
  private AlertDialog dialog;

  protected  Riderequest currentRide;
  protected DisplayMetrics dm;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    //compute screen dimensions
    dm  = getResources().getDisplayMetrics();
    WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
    windowManager.getDefaultDisplay().getMetrics(dm);
  }


  @Override
  protected void onStart() {
    super.onStart();
//    if(this instanceof MainActivity || this instanceof DriverMainActivity)
//      ApplicationController.getInstance().getOttoBus().register(this);
  }

  @Override
  protected void onStop() {
    super.onStop();
//    if(this instanceof MainActivity || this instanceof DriverMainActivity)
//      ApplicationController.getInstance().getOttoBus().unregister(this);
  }

  public  Typeface getFont(String fontName){
    String font = "fonts/" + fontName;
    Typeface tf = Typeface.createFromAsset(getResources().getAssets(), font);
    return tf;
  }

  protected void openUrl(String url){
    if(url!=null){

      Intent i = new Intent(Intent.ACTION_VIEW);
      i.setData(Uri.parse(!url.startsWith("http://") ? "http://" + url : url));
      startActivity(i);
    }
  }

  protected void openUrlWithHeaders(String url, HashMap<String,String> mExtraHeader){
    if(url!=null){

      Intent i = new Intent(Intent.ACTION_VIEW);
      i.setData(Uri.parse(!url.startsWith("http://") ? "http://" + url : url));
      Bundle bundle = new Bundle();
      if(mExtraHeader!=null){
        for(String key: mExtraHeader.keySet()){
          bundle.putString(key, mExtraHeader.get(key));
        }
      }
      i.putExtra(Browser.EXTRA_HEADERS, bundle);
      startActivity(i);
    }
  }


  protected void openEmailClient(String address,String object,String content) {
    Intent i = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
            "mailto", address, null));
    i.putExtra(android.content.Intent.EXTRA_SUBJECT,object);
    i.putExtra(Intent.EXTRA_TEXT,content);
    startActivity(Intent.createChooser(i, getString(R.string.send_email)));
  }

  protected int getCountryPosition(String prefix){
    int index = 0;
    if(prefix != null){
      index = Arrays.asList(getResources().getStringArray(R.array.country_prefix)).indexOf(prefix);
    }

    return index;
  }

  protected void startPollingService(User u){
    Intent i = new Intent(this, PollingService.class);
    i.putExtra(PollingService.USER,u);
    startService(i);
  }

  protected int getCountryPositionByLanguage(String lang){
    int index = 0;
    if(lang != null){
      index = Arrays.asList(getResources().getStringArray(R.array.country_id)).indexOf(lang);
    }

    return index;
  }


  protected void killPositionService()
  {
    BusRequestMessage killServiceReq = new BusRequestMessage();
    killServiceReq.setCode(ZegoConstants.OttoBusConstants.KILL_POSITION_SERVICE);
    ApplicationController.getInstance().getOttoBus().post(killServiceReq);
  }

  protected void killPollingRideService()
  {
    BusRequestMessage killServiceReq = new BusRequestMessage();
    killServiceReq.setCode(ZegoConstants.OttoBusConstants.KILL_POLLING_RIDE_SERVICE);
    ApplicationController.getInstance().getOttoBus().post(killServiceReq);
  }

  protected void startStopPolling(boolean isStart){
    BusRequestMessage stopPollingReq = new BusRequestMessage();
    stopPollingReq.setCode(ZegoConstants.OttoBusConstants.START_STOP_POLLING);
    stopPollingReq.setPayLoad(isStart);
    ApplicationController.getInstance().getOttoBus().post(stopPollingReq);
  }

  protected void synchWithRightStatus(User user) {
    if(user == null) return;
    BusRequestMessage stopPollingReq = new BusRequestMessage();
    stopPollingReq.setCode(ZegoConstants.OttoBusConstants.SYNCH_STATUS);
    stopPollingReq.setPayLoad(user);
    ApplicationController.getInstance().getOttoBus().post(stopPollingReq);
  }

  protected void updateUserPolling(User u){
    BusRequestMessage userUpdate = new BusRequestMessage();
    userUpdate.setCode(ZegoConstants.OttoBusConstants.UPDATE_USER);
    userUpdate.setPayLoad(u);
    ApplicationController.getInstance().getOttoBus().post(userUpdate);
  }

  @Override
  public void decodeNetWorkErrorListener(Integer responseCode,ErrorObject errorObject) {
    switch (responseCode){
      case ZegoConstants.ApiRestConstants.ERROR_401:
        break;
      case ZegoConstants.ApiRestConstants.ERROR_403:
        PopUpDialog.showPopUpDialog(this, getString(R.string.warning), errorObject.getMsg(), getString(R.string.logout), 0, null, new PopUpDialog.DialogActionListener() {
          @Override
          public void actionListener() {
            killPositionService();
            killPollingRideService();
            ApplicationController.getInstance().logOutsharedPreferences();
            goToEntryActivity();
          }

          @Override
          public void negativeAction() {

          }
        });
        break;
      default:
        break;
    }
  }

  protected void closeKeyBoard(View v){
    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
  }

  protected void showOrDismissProgressWheel(boolean show){
//    progressWheel.setVisibility(show ? View.VISIBLE : View.GONE);
    if(show){
      ProgressHud.showProgressWheel(this,null);
    }else{
      ProgressHud.dismissProgressWheel(this);
    }
  }

  protected void showOrDismissProgressWheelWithTimeOut(boolean show,Integer timeOut){
//    progressWheel.setVisibility(show ? View.VISIBLE : View.GONE);
    if(show){
      ProgressHud.showProgressWheel(this,timeOut);
    }else{
      ProgressHud.dismissProgressWheel(this);
    }
  }


  protected boolean isThereConnectivity(){
    ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
    return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
  }

  protected void showInfoDialog(String title,String msg){
    if(this.isDestroyed() || this.isFinishing()) return;

    if(dialog != null && dialog.isShowing()){
      dialog.dismiss();
    }

    dialog =  new AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(msg)
            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int which) {
                // continue with delete
              }
            })
            .show();
  }

  protected void onpenUrlLoginGateway(String token){
    HashMap<String,String> headers = new HashMap<>();
    headers.put(NetworkManager.ZEGOTOKEN,token);
    openUrlWithHeaders(getString(R.string.url_zego_login),headers);
  }


  protected void showInfoDialogWithAction(String title, String msg, final InfoAction actionListener){

    if(this.isDestroyed() || this.isFinishing()) return;

    if(dialog != null && dialog.isShowing()){
      dialog.dismiss();
    }

    dialog =  new AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(msg)
            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int which) {
                if(actionListener != null){
                  actionListener.onAction();
                }
              }
            })
            .create();

    dialog.show();
  }


  protected boolean appInstalledOrNot(String uri)
  {
    PackageManager pm = getPackageManager();
    boolean app_installed = false;
    try
    {
      pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
      app_installed = true;
    }
    catch (PackageManager.NameNotFoundException e)
    {
      app_installed = false;
    }
    return app_installed ;
  }

  public  AmazonS3 getS3Client(){

    // Create an S3 client
    AmazonS3 s3 = new AmazonS3Client(ApplicationController.getInstance().getCredentialsProvider());

    // Set the region of your S3 bucket
    s3.setRegion(Region.getRegion(Regions.EU_WEST_1));
    return s3;

  }

  protected User readUserFromFacebook(JSONObject object){
    User u = new User();
    try {
      if(object.getString("first_name")!= null){
        u.setFname(object.getString("first_name"));
      }
      if(object.getString("last_name")!= null){
        u.setLname(object.getString("last_name"));
      }

      if(object.getString("id")!=null){
        u.setFbid(object.getString("id"));
//                u.setImg("https://graph.facebook.com/" + object.getString("id") + "/picture?type=large");
      }
      if(object.getString("email")!=null){
        u.setEmail(object.getString("email"));
      }
      if(object.getString("gender")!=null){
        u.setGender(object.getString("gender"));
      }

      if(object.getString("birthday")!=null){
        u.setBirthdate(object.getString("birthday"));
      }


    } catch (JSONException e) {
      e.printStackTrace();
    }

    return u;
  }

  protected void callPhoneNumber(String numberToCall) {
    Intent intent = new Intent(Intent.ACTION_CALL);
    intent.setData(Uri.parse("tel:" + numberToCall));
    startActivity(intent);
  }
  protected void startNavigation(LatLng latLng)
  {
    Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
            Uri.parse("http://maps.google.com/maps?daddr="+latLng.latitude+","+latLng.longitude));
    startActivity(intent);
  }


  protected void openShareTextChooser(String url){
    Intent shareIntent = new Intent();
    shareIntent.setAction(Intent.ACTION_SEND);
    shareIntent.putExtra(Intent.EXTRA_TEXT, url);
    shareIntent.setType("text/plain");
    startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.share_to)));
  }


  protected Bitmap getBitmapMarker(int width,int height,int resource){
    BitmapFactory.Options op = new BitmapFactory.Options();
    op.outWidth = width;
    op.outHeight = height;
    Bitmap marker = BitmapFactory.decodeResource(getResources(),resource,op);
    return marker;
  }

  protected void disableHeaderRightIcon(boolean disable){
    aheadTextView.setEnabled(disable);
    aheadTextView.setVisibility(disable ? View.INVISIBLE : View.VISIBLE);
  }

  protected String getTCurlByLanguage(){
    return getString(R.string.zego_web_site_url) + "/" + getWebSiteSupportedLanguage() + "/termini-e-condizioni";
  }

  protected String getPrivacyPolicyByLanguage(){
    return getString(R.string.zego_web_site_url) + "/" + getWebSiteSupportedLanguage() +"/privacy";
  }

  protected String getFaqByLanguage(){
    return getString(R.string.zego_web_site_url) + "/" + getWebSiteSupportedLanguage() +"/faq";
  }


  protected String getWebSiteSupportedLanguage(){
    String lang = ApplicationController.getInstance().getsDefSystemLanguage();

    if(ZegoConstants.DEBUG){
      Log.d("Lang:",lang);
    }
    List<String> array = Arrays.asList(getResources().getStringArray(R.array.lang_supported));

    if(lang == null || lang.isEmpty()) {
      lang = "en";
    }
    else{
      for(String s : array){
        if(lang.contains(s)){
          lang = s;
          return  lang;
        }
      }
      lang = "en";
    }

    return lang;
  }



  public interface InfoAction{
    void onAction();
  }


  protected void sendLogOut(User u){

    if(ZegoConstants.DEBUG){
      Log.d("LogOut:",u.toString());
    }
    Call<User> call = NetworkManager.getInstance().getRetrofit().create(UserRESTInterface.class).logout(u.getZegotoken(),u);
    call.enqueue(new Callback<User>() {
      @Override
      public void onResponse(Call<User> call, Response<User> response) {
//        goToEntryActivity();
      }

      @Override
      public void onFailure(Call<User> call, Throwable t) {
//        goToEntryActivity();
      }
    });
  }



  protected void goToEntryActivity(){
    Intent i = new Intent(ZegoBaseActivity.this,EntryActivity.class);
    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
    startActivity(i);
    finish();
  }


}
