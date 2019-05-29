package it.sharethecity.mobile.letzgo.application;

import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.multidex.MultiDex;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;

import com.amazonaws.regions.Regions;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

import java.util.List;

import it.sharethecity.mobile.letzgo.dao.Conf;
import it.sharethecity.mobile.letzgo.dao.User;
import it.sharethecity.mobile.letzgo.utilities.ZegoConstants;

/**
 * Created by lucabellaroba on 10/11/16.
 */

public class ApplicationController extends Application {

  private static final String ZEGO_FILE = "zego_file";
  private static final String ZEGO_USER = "zego_user";
  private static final String FIRST_ACCESS = "first_access";
  private static final String LAST_POSITION_SENT = "last_position_sent";
  private static final String PUSH_MESSAGE = "push_notification";
  private static final String CASH = "method_cash";
  private static final String FIRST_CASH = "firstCash";


  private static ApplicationController instance;
  private static Gson gson;
  private String imeiCode;
  private String sDefSystemLanguage;
  private Bus ottoBus;
  private CognitoCachingCredentialsProvider credentialsProvider;
  private Ringtone ringtone;

  @Override
  public void onCreate() {
    super.onCreate();
    FacebookSdk.sdkInitialize(getApplicationContext());
    AppEventsLogger.activateApp(this);
    credentialsProvider = new CognitoCachingCredentialsProvider(
            getApplicationContext(),
            "eu-west-1:a9805a9b-3f6d-4009-9ba7-75e1119a23e4", // Identity Pool ID
            Regions.EU_WEST_1);

    ottoBus = new Bus(ThreadEnforcer.ANY);
    instance = this;
    gson = new Gson();
  }

  @Override
  protected void attachBaseContext(Context base) {
    super.attachBaseContext(base);
    MultiDex.install(this);
  }

  @Override
  public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);

    sDefSystemLanguage = getMobileLanguage();
  }

  public static ApplicationController getInstance() {
    return instance;
  }

  public void saveInPreference(String tag,String toBeSaved){
    SharedPreferences pref = getSharedPreferences(ZEGO_FILE, MODE_PRIVATE);
    if(pref != null){
      SharedPreferences.Editor editor = pref.edit();
      editor.putString(tag,toBeSaved == null?"":toBeSaved);
      editor.commit();
    }
  }

  public void removeFromSharedPreferences(String tag){
    SharedPreferences pref = getSharedPreferences(ZEGO_FILE, MODE_PRIVATE);
    if(pref != null){
      SharedPreferences.Editor editor = pref.edit();
      editor.remove(tag);
      editor.commit();
    }
  }


  public String readFromSharedPreferences(String tag){
    SharedPreferences pref = getSharedPreferences(ZEGO_FILE, MODE_PRIVATE);
    if(pref != null){
      String s =  pref.getString(tag,"");
      return s;
    }
    return "";
  }

  public void logOutsharedPreferences(){
    removeFromSharedPreferences(ZEGO_USER);
  }

  public User getUserLogged(){
    String userString = readFromSharedPreferences(ZEGO_USER);
    return userString.isEmpty() ? null : gson.fromJson(userString,User.class);
  }

  public void saveUser(User user){
    saveInPreference(ZEGO_USER,gson.toJson(user));
  }

  public DisplayMetrics getScreenDimension(){
    DisplayMetrics dm  = getResources().getDisplayMetrics();
    WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
    windowManager.getDefaultDisplay().getMetrics(dm);
    return dm;
  }

  public String getImeiCode() {
    if(imeiCode == null || imeiCode.isEmpty()){
      TelephonyManager mngr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
      imeiCode = mngr.getDeviceId();
    }
    return imeiCode;
  }

  public String getsDefSystemLanguage() {
    if(sDefSystemLanguage == null){
      sDefSystemLanguage = getMobileLanguage();
    }
    return sDefSystemLanguage;
  }

  private String getMobileLanguage(){
    String language= "";
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      language = getResources().getConfiguration().getLocales().get(0).getLanguage();
    } else {
      language = getResources().getConfiguration().locale.getLanguage();
    }
    return language;
  }

  public boolean isFirstAccess(){
    String access = readFromSharedPreferences(FIRST_ACCESS);
    return access.isEmpty() || access.equalsIgnoreCase("true");
  }

  public void setFirstAccess(boolean access){
    saveInPreference(FIRST_ACCESS,"" + access);
  }

  public void saveConf(List<Conf> confs) {
    for(Conf c : confs){
      if(c.getK().equalsIgnoreCase(Conf.PRICING_MAXIMUM_FEE)){
        int valo = 7050;
        if(c.getVal() != null || !c.getVal().isEmpty()){
          valo = (int) ((1175L * Integer.valueOf(c.getVal()))/1000);
        }
        String val = Long.toString(valo,10);
        saveInPreference(c.getK(),val);
      }else{
        saveInPreference(c.getK(),c.getVal());
      }

    }
  }

  public CognitoCachingCredentialsProvider getCredentialsProvider() {
    return credentialsProvider;
  }

  public void setCredentialsProvider(CognitoCachingCredentialsProvider credentialsProvider) {
    this.credentialsProvider = credentialsProvider;
  }

  public void saveLastPositionSent(LatLng ldate) {
    saveInPreference(LAST_POSITION_SENT,gson.toJson(ldate,LatLng.class));
  }

  public LatLng getLastPosition(){
    String pos = readFromSharedPreferences(LAST_POSITION_SENT);
    return pos.isEmpty() ? null : gson.fromJson(pos,LatLng.class);
  }

  public Bus getOttoBus() {
    return ottoBus;
  }

  public String getGlobalConfParam(String tag) {
    String val = "";
    val = readFromSharedPreferences(tag);
    if(ZegoConstants.DEBUG){
      Log.d(tag, "val:" + val);
    }
    return val;
  }

  public void makeSound(int sound){
    Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + sound);
    stopSound();
    ringtone = RingtoneManager.getRingtone(getApplicationContext(), uri);
    ringtone.play();
  }

  public void stopSound(){
    if(ringtone != null) {
      if (ringtone.isPlaying()) {
        ringtone.stop();
      }
    }
  }

  public void savePushMsg(String msg) {
    saveInPreference(PUSH_MESSAGE,msg);
  }

  public String getPushMsg(){
    return readFromSharedPreferences(PUSH_MESSAGE);
  }

  public void removePushNotificationById(int nid){
    NotificationManager nMgr = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
    nMgr.cancel(nid);
  }


  public void saveMethodCash(Integer c){
    saveInPreference(CASH,c.toString());
  }

  public boolean isCashMethod(){
    String s = readFromSharedPreferences(CASH);
    return s.isEmpty() ? false : Integer.valueOf(s).equals(1);
  }

  public boolean isFirstCash() {
    String access = readFromSharedPreferences(FIRST_CASH);
    return access.isEmpty() || access.equalsIgnoreCase("true");
  }



  public void setFirstCash(boolean access){
    saveInPreference(FIRST_CASH,"" + access);
  }
}
