package it.sharethecity.mobile.letzgo.utilities;

import it.sharethecity.mobile.letzgo.R;

/**
 * Created by lucabellaroba on 10/11/16.
 */

public class ZegoConstants {

  public static final boolean DEBUG = false;

  public static final String BASE_URL = DEBUG ? "http://test.zegoapp.com:8080/zego/" : "https://v2.sharethecity.net/zego/";
  public static final String FACEBOOK_APP = "com.facebook.katana";

  public static final String RALEWAY_ITALIC = "Raleway-italic.ttf";
  public static final String RALEWAY_LIGHT = "Raleway-Light.ttf";
  public static final String RALEWAY_REGULAR = "Raleway-Regular.ttf";
  public static final String RALEWAY_REGULAR_BOLD = "Raleway-SemiBold.ttf";
  public static final int MIN_NUMBER_DIGITS = 5;
  public static final int MAX_NUMBER_DIGITS = 12;
  public static final String SMS_SENDER = "Zego";
  public static final int ERROR_MESSAGE_LIFE_TIME = 3000;
  public static final double MILANO_LAT = 45.463072;
  public static final double MILANO_LNG = 9.185669;
  public static final float DEFAULT_ZOOM = 14;

  public static final int MAX_PASSENGER = 3;

  public static class ApiRestConstants{
    public static final int ERROR_401 = 401;
    public static final int ERROR_403 = 403;
    public static final int ERROR_500 = 500;

    public static final int RESEND_ERROR_MORE_THAN_5_IN_LESS_THAN_5_MIN = 108;
  }

  public static class StripeConstants{
    public static final String TEST_PUBLISHABLE_KEY = "pk_test_kWnzPvkiYyShQzLzLZnjpOZf";
    public static final String PUBLISHABLE_KEY = "pk_live_3ZiPA0QCpHcyi0c0eqGq1LmP";
  }

  public static class AWS3 {
    public static final String BUCKET = "zegoapp";
    public static final String URL_BACKET = "https://s3-eu-west-1.amazonaws.com/"+ BUCKET + "/";
  }

  public static class OttoBusConstants{
    public static final int CONNECTION_STATUS = 3;
    public static final int KILL_POSITION_SERVICE = 4;
    public static final int MY_POSITION = 5;
    public static final int USER_UPDATE_RESPONSE = 6;
    public static final int RIDE_UPDATE_RESPONSE = 7;
    public static final int START_STOP_POLLING = 8;
    public static final int SET_TYPE_OF_POLLING = 9;
    public static final int GPS_AVAILABLE_RESPONSE = 10;
    public static final int KILL_POLLING_RIDE_SERVICE = 11;
    public static final int IS_LOCATION_AVAILABLE = 12;
    public static final int RIDE_HISTORY_DRIVER = 13;
    public static final int RIDE_HISTORY_PASSENGER = 14;
    public static final int UPDATE_USER = 15;
    public static final int SYNCH_STATUS = 16;
  }

  public static class ArrayImages{
    public static final int[] RATING_ARRAY = new int[]{R.drawable.s5, R.drawable.s1, R.drawable.s2, R.drawable.s3, R.drawable.s4, R.drawable.s5};
    public static final int[] PASSENGERS_DRIVER_ARRAY = new int[]{R.drawable.p0driver, R.drawable.p1driver, R.drawable.p2driver, R.drawable.p3driver};
    public static final int[] PASSENGERS_ARRAY = new int[]{R.drawable.p0, R.drawable.p1, R.drawable.p2, R.drawable.p3};

  }

}
