package it.sharethecity.mobile.letzgo.utilities;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import it.sharethecity.mobile.letzgo.R;

/**
 * Created by lucabellaroba on 10/11/16.
 */

public class UtilityFunctions {

  public static boolean mailSyntaxCheck(String email)
  {
    // Create the Pattern using the regex
    Pattern p = Pattern.compile(".+@.+\\.[a-z]+");

    // Match the given string with the pattern
    Matcher m = p.matcher(email);

    // check whether match is found
    boolean matchFound = m.matches();

    StringTokenizer st = new StringTokenizer(email, ".");
    String lastToken = null;
    while (st.hasMoreTokens()) {
      lastToken = st.nextToken();
    }

    // validate the country code
    if (matchFound && lastToken.length() >= 2
            && email.length() - 1 != lastToken.length()) {

      return true;
    } else {
      return false;
    }

  }

  public static boolean monthYearCheck(String mmaa){
    if(mmaa == null) return false;
    // Create the Pattern using the regex
    Pattern p = Pattern.compile("^((0[1-9])|(1[0-2]))\\/([1-9][0-9])$");

    // Match the given string with the pattern
    Matcher m = p.matcher(mmaa);

    // check whether match is found
    return m.matches();
  }
  private static String convertToHex(byte[] data) {
    StringBuffer buf = new StringBuffer();
    for (int i = 0; i < data.length; i++) {
      int halfbyte = (data[i] >>> 4) & 0x0F;
      int two_halfs = 0;
      do {
        if ((0 <= halfbyte) && (halfbyte <= 9))
          buf.append((char) ('0' + halfbyte));
        else
          buf.append((char) ('a' + (halfbyte - 10)));
        halfbyte = data[i] & 0x0F;
      } while(two_halfs++ < 1);
    }
    return buf.toString();
  }

  public static String digestMD5(String text)
  {
    try {
      MessageDigest md;
      md = MessageDigest.getInstance("MD5");
      byte[] md5hash = new byte[32];
      md.update(text.getBytes("iso-8859-1"), 0, text.length());
      md5hash = md.digest();
      return convertToHex(md5hash);
    } catch (UnsupportedEncodingException ex) {

    } catch (NoSuchAlgorithmException ex) {

    }
    return null;
  }

  public static boolean areAllPermissionsGranted(int[] grantResults){

    boolean granted = true;

    if(grantResults.length == 0){
      return false;
    }

    for (int grant : grantResults) {
      granted = granted && grant == PackageManager.PERMISSION_GRANTED;
    }
    return granted;

  }


  public static String getDeviceName() {
    String manufacturer = Build.MANUFACTURER;
    String model = Build.MODEL;
    if (model.startsWith(manufacturer)) {
      return capitalize(model);
    } else {
      return capitalize(manufacturer) + " " + model;
    }
  }



  private static String capitalize(String s) {
    if (s == null || s.length() == 0) {
      return "";
    }
    char first = s.charAt(0);
    if (Character.isUpperCase(first)) {
      return s;
    } else {
      return Character.toUpperCase(first) + s.substring(1);
    }
  }

  public static Bitmap decodeSampledBitmapFromStream(File file, int maxWidth) {

    Bitmap b = null;

    // First decode with inJustDecodeBounds=true to check dimensions
    final BitmapFactory.Options options = new BitmapFactory.Options();
    options.inJustDecodeBounds = true;
    try {
      InputStream inputStream = new FileInputStream(file);
      BitmapFactory.decodeStream(inputStream, null, options);
      int newImageWidth = 0;

      if(options.outWidth > maxWidth){
        newImageWidth = maxWidth;
      }
      if (options.outWidth > maxWidth){
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, newImageWidth, newImageWidth);
      }
      inputStream.close();
      inputStream = new FileInputStream(file);
      // Decode bitmap with inSampleSize set
      options.inJustDecodeBounds = false;
      b = BitmapFactory.decodeStream(inputStream, null, options);
      inputStream.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      return null;
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }


    return b;

  }


  public static int calculateInSampleSize(
          BitmapFactory.Options options, int reqWidth, int reqHeight) {
    // Raw height and width of image
    final int height = options.outHeight;
    final int width = options.outWidth;
    int inSampleSize = 1;

    if (height > reqHeight || width > reqWidth) {

      final int halfHeight = height / 2;
      final int halfWidth = width / 2;

      // Calculate the largest inSampleSize value that is a power of 2 and keeps both
      // height and width larger than the requested height and width.
      while ((halfHeight / inSampleSize) > reqHeight
              && (halfWidth / inSampleSize) > reqWidth) {
        inSampleSize *= 2;
      }
    }

    return inSampleSize;
  }

  public static class NetworkUtil {

    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;


    public static int getConnectivityStatus(Context context) {
      ConnectivityManager cm = (ConnectivityManager) context
              .getSystemService(Context.CONNECTIVITY_SERVICE);

      NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
      if (null != activeNetwork) {
        if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
          return TYPE_WIFI;

        if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
          return TYPE_MOBILE;
      }
      return TYPE_NOT_CONNECTED;
    }

    public static String getConnectivityStatusString(Context context) {
      int conn = NetworkUtil.getConnectivityStatus(context);
      String status = null;
      if (conn == NetworkUtil.TYPE_WIFI) {
        status = "Wifi enabled";
      } else if (conn == NetworkUtil.TYPE_MOBILE) {
        status = "Mobile data enabled";
      } else if (conn == NetworkUtil.TYPE_NOT_CONNECTED) {
        status = "Not connected to Internet";
      }
      return status;
    }
  }

  public static Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
    // GET CURRENT SIZE
    int width = bm.getWidth();
    int height = bm.getHeight();
    // GET SCALE SIZE
    float scaleWidth = ((float) newWidth) / width;
    float scaleHeight = ((float) newHeight) / height;
    // CREATE A MATRIX FOR THE MANIPULATION
    Matrix matrix = new Matrix();
    // RESIZE THE BIT MAP
    matrix.postScale(scaleWidth, scaleHeight);
    // "RECREATE" THE NEW BITMAP
    Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
    return resizedBitmap;
  }

  public static int getCardImageByBrand(String cardBrand) {
    int cardImage = R.drawable.cardgrey;

    if(cardBrand == null || cardBrand.isEmpty()){
      return cardImage;
    }
    else if(cardBrand.toLowerCase().contains("visa")){
      cardImage = R.drawable.visa;
    }else if(cardBrand.toLowerCase().contains("mastercard")){
      cardImage = R.drawable.mastercard;
    }else if(cardBrand.toLowerCase().contains("jcb")){
      cardImage = R.drawable.jcb;
    }else if(cardBrand.toLowerCase().contains("american")){
      cardImage = R.drawable.american_express;
    }else if(cardBrand.toLowerCase().contains("diners")){
      cardImage = R.drawable.diners_club;
    }else if(cardBrand.toLowerCase().contains("paypal")){
      cardImage = R.drawable.paypal;
    }

    return cardImage;
  }
}
