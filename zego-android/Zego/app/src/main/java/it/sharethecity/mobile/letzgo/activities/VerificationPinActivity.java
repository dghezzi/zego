package it.sharethecity.mobile.letzgo.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import it.sharethecity.mobile.letzgo.R;
import it.sharethecity.mobile.letzgo.application.ApplicationController;
import it.sharethecity.mobile.letzgo.customviews.CellNumber;
import it.sharethecity.mobile.letzgo.customviews.CustomNumberKeyBoard;
import it.sharethecity.mobile.letzgo.customviews.ItalicRelewayTextView;
import it.sharethecity.mobile.letzgo.dao.User;
import it.sharethecity.mobile.letzgo.network.NetworkManager;
import it.sharethecity.mobile.letzgo.network.interfaces.UserRESTInterface;
import it.sharethecity.mobile.letzgo.network.request.ErrorObject;
import it.sharethecity.mobile.letzgo.network.request.PinRequest;
import it.sharethecity.mobile.letzgo.network.request.ResendPinRequest;
import it.sharethecity.mobile.letzgo.network.utils.NetWorkErrorInterface;
import it.sharethecity.mobile.letzgo.network.utils.NetworkErrorHandler;
import it.sharethecity.mobile.letzgo.utilities.ZegoConstants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerificationPinActivity extends ZegoBaseActivity implements NetWorkErrorInterface {

  public static final String USER = "user";
  public static final int CODE_LENGTH = 4;

  @Nullable
  @BindView(R.id.code_layout)
  LinearLayout codeLayout;

  @Nullable
  @BindView(R.id.first_label)
  TextView labelWithNumber;

  @Nullable
  @BindView(R.id.first_digit)
  CellNumber firstDigit;

  @Nullable
  @BindView(R.id.second_digit)
  CellNumber secondDigit;

  @Nullable
  @BindView(R.id.third_digit)
  CellNumber thirdDigit;

  @Nullable
  @BindView(R.id.fourth_digit)
  CellNumber fourthDigit;

  @Nullable
  @BindView(R.id.keyboard)
  CustomNumberKeyBoard keyBoard;

  @Nullable
  @BindView(R.id.send_code_again_button)
  Button sendCodeAgain;

  @Nullable
  @BindView(R.id.avanti_button)
  Button avantiButton;

  @Nullable
  @BindView(R.id.header)
  RelativeLayout headerLayout;

  @Nullable
  @BindView(R.id.hint_error)
  ItalicRelewayTextView hintError;

  private String code = "";
  private View[] codeViewsArray ;

  private User user;
  private BroadcastReceiver smsBrReceiver;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_verification_pin);
    ButterKnife.bind(this);
    registerSmsBrReceiver();
    setUi();
    codeViewsArray = new View[]{firstDigit,secondDigit,thirdDigit,fourthDigit};

    keyBoard.setListener(new CustomNumberKeyBoard.onKeyBoardListener() {
      @Override
      public void onKeyBoardClick(String buttonClick) {
        if(code.length() < CODE_LENGTH){
          code += buttonClick;
          ((CellNumber)codeViewsArray[code.length() - 1]).setText(buttonClick);
        }

        changeAvantiButtonState();

      }

      @Override
      public void onCancelClick() {
        if(code.length() > 0){
          ((CellNumber)codeViewsArray[code.length() - 1]).setText("");
          code = code.substring(0,code.length() - 1);
        }

        changeAvantiButtonState();
      }
    });

    if(getIntent().getExtras() != null){
      user = (User) getIntent().getExtras().getSerializable(USER);
      fromActivity = getIntent().getExtras().getString(FROM_ACTIVITY);
      labelWithNumber.setText(getString(R.string.sms_sent_to) + " " + user.getPrefix() + " " + user.getMobile() + "." + getString(R.string.insert_here_pin));
    }else{
      finish();
    }

  }

  private void changeAvantiButtonState(){

    avantiButton.setEnabled(code.length() == CODE_LENGTH);
    avantiButton.setBackground(ContextCompat.getDrawable(getBaseContext(),code.length() == CODE_LENGTH ? R.drawable.green_button_selector : R.color.gray_button));
    // aheadTextView.setTextColor(ContextCompat.getColor(getBaseContext(),code.length() == CODE_LENGTH ? R.color.green_zego : R.color.gray_button));

    hintError.setTextColor(ContextCompat.getColor(getBaseContext(),R.color.gray_text));
    hintError.setText("");
    if(code.length() == (CODE_LENGTH - 1))
      changePinTextColor(ContextCompat.getColor(getBaseContext(),R.color.gray_text));
  }


  private void changePinTextColor(int color){
    for(int i = 0; i < CODE_LENGTH; i++){
      ((CellNumber)codeViewsArray[i]).setTextColor(color);
    }
  }

  @Optional
  @OnClick({R.id.avanti_button,R.id.right_text_view,R.id.send_code_again_button})
  public void onAvantiClick(View v){

    switch (v.getId()){
      case R.id.avanti_button:
      case R.id.right_text_view:
        avantiAction();
        break;
      case R.id.send_code_again_button:
        hintError.setTextColor(ContextCompat.getColor(getBaseContext(),R.color.gray_text));
        hintError.setText("");
        resendAction();
        break;

    }

  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    if(smsBrReceiver != null)
      unregisterReceiver(smsBrReceiver);
  }

  private void resendAction() {
    ResendPinRequest rp = new ResendPinRequest();
    rp.setUid(user.getId());
    Call<User> call = NetworkManager.getInstance().getRetrofit().create(UserRESTInterface.class).postResend(user.getZegotoken(),rp);
    showOrDismissProgressWheel(SHOW);
    call.enqueue(new Callback<User>() {
      @Override
      public void onResponse(Call<User> call, Response<User> response) {
        showOrDismissProgressWheel(DISMISS);
        if(response.isSuccessful()){
        //  Toast.makeText(getBaseContext(),getString(R.string.pin_sent),Toast.LENGTH_SHORT).show();
        }else{
          if(response.code() == ZegoConstants.ApiRestConstants.ERROR_500){
            ErrorObject errorObject = NetworkErrorHandler.getInstance().decodeError(response);
            if(errorObject.getCode() == ZegoConstants.ApiRestConstants.RESEND_ERROR_MORE_THAN_5_IN_LESS_THAN_5_MIN){
              hintError.setTextColor(ContextCompat.getColor(getBaseContext(),R.color.red_error));
              hintError.setText(errorObject.getMsg());
            }

          }else{
            NetworkErrorHandler.getInstance().errorHandler(response,VerificationPinActivity.this);
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
  @OnClick(R.id.back_button)
  public void onBackClick(){
    super.onBackPressed();
  }

  private void avantiAction() {
    PinRequest pinRequest = new PinRequest();
    pinRequest.setPin(code);
    pinRequest.setUid(user.getId());
    Call<User> call = NetworkManager.getInstance().getRetrofit().create(UserRESTInterface.class).pinVerification(user.getZegotoken(),pinRequest);
    showOrDismissProgressWheel(SHOW);
    call.enqueue(new Callback<User>() {
      @Override
      public void onResponse(Call<User> call, Response<User> response) {
        showOrDismissProgressWheel(DISMISS);
        if(response.isSuccessful()){
          Intent i = null;
          if(user.getStatus() == 1){
            ApplicationController.getInstance().saveUser(response.body());
            if(fromActivity != null && fromActivity.equalsIgnoreCase(EditActivity.class.getSimpleName())){
              VerificationPinActivity.super.onBackPressed();
              return;
            }else{
              i = new Intent(VerificationPinActivity.this,response.body().isDriver() ? DriverMainActivity.class : MainActivity.class);
              i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            }
          }else{
            i = new Intent(VerificationPinActivity.this,UserFormActivity.class);
            i.putExtra(UserFormActivity.USER,response.body());
          }

          startActivity(i);

        }else{
          if(response.code() == ZegoConstants.ApiRestConstants.ERROR_500){
            int errorColor = ContextCompat.getColor(getBaseContext(),R.color.red_error);
            ErrorObject errorObject = NetworkErrorHandler.getInstance().decodeError(response);

            hintError.setText(errorObject != null ? errorObject.getMsg() : getString(R.string.unknown_error));
            hintError.setTextColor(errorColor);
            for(int i = 0; i < CODE_LENGTH; i++){
              ((CellNumber)codeViewsArray[i]).setTextColor(errorColor);
            }
          }
          NetworkErrorHandler.getInstance().errorHandler(response,VerificationPinActivity.this);
        }
      }

      @Override
      public void onFailure(Call<User> call, Throwable t) {
        showOrDismissProgressWheel(DISMISS);
        showInfoDialog(getString(R.string.warning),getString(R.string.impossible_to_connetc_to_server));
      }
    });
  }

  private void setUi() {
    headerLayout.getLayoutParams().height = (int) ((HEADER_PERCENT_HEIGHT)*ApplicationController.getInstance().getScreenDimension().heightPixels);
   // sendCodeAgain.getLayoutParams().width = (int) ((0.36f)*ApplicationController.getInstance().getScreenDimension().widthPixels);
    sendCodeAgain.getLayoutParams().height = (int) ((0.079f)*ApplicationController.getInstance().getScreenDimension().heightPixels);
    codeLayout.getLayoutParams().width = (int) ((0.547f)*ApplicationController.getInstance().getScreenDimension().widthPixels);
    hintError.getLayoutParams().width = (int) ((0.547f)*ApplicationController.getInstance().getScreenDimension().widthPixels);
    //keyBoard.getLayoutParams().width = (int) ((0.80f)*ApplicationController.getInstance().getScreenDimension().widthPixels);
    // codeLayout.getLayoutParams().height = (int) ((0.073f)*ApplicationController.getInstance().getScreenDimension().heightPixels);
    avantiButton.getLayoutParams().height = (int) ((BOTTOM_BUTTON_PERCENT_HEIGHT)*ApplicationController.getInstance().getScreenDimension().heightPixels);

    avantiButton.setEnabled(false);
    aheadTextView.setEnabled(false);

    sendCodeAgain.setTypeface(getFont(ZegoConstants.RALEWAY_REGULAR_BOLD));

    titleTextView.setText(getString(R.string.pin_verification));
    aheadTextView.setVisibility(View.INVISIBLE);

    aheadTextView.setTextColor(ContextCompat.getColor(getBaseContext(),R.color.gray_button));

  }

  private void registerSmsBrReceiver(){
    IntentFilter filter = new IntentFilter();
    filter.addAction("android.provider.Telephony.SMS_RECEIVED");
    filter.addCategory("android.intent.category.DEFAULT");
    // Get the object of SmsManager
    final SmsManager sms = SmsManager.getDefault();

    smsBrReceiver = new BroadcastReceiver() {
      @Override
      public void onReceive(Context context, Intent intent) {
        // Retrieves a map of extended data from the intent.
        final Bundle bundle = intent.getExtras();

        try {

          if (bundle != null) {

            final Object[] pdusObj = (Object[]) bundle.get("pdus");

            for (int i = 0; i < pdusObj.length; i++) {

              SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
              String phoneNumber = currentMessage.getDisplayOriginatingAddress();

              String senderNum = phoneNumber;
              if(senderNum.equalsIgnoreCase(ZegoConstants.SMS_SENDER)){
                String message = currentMessage.getDisplayMessageBody();
                if(message != null && message.length() >= CODE_LENGTH){
                  code = message.substring(message.length() - CODE_LENGTH,message.length());
                  for(int a = 0; a < CODE_LENGTH ; a++){
                    ((CellNumber)codeViewsArray[a]).setText(code.substring(a,a + 1));
                  }

                  changeAvantiButtonState();
                  new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                      avantiAction();
                    }
                  },200);
                }
                if(ZegoConstants.DEBUG){
                  Log.i("SmsReceiver", "senderNum: "+ senderNum + "; message: " + message);
                  // Show Alert
                  Toast.makeText(context,"senderNum: "+ senderNum + ", message: " + message, Toast.LENGTH_LONG).show();

                }
              }




            } // end for loop
          } // bundle is null

        } catch (Exception e) {
          if(ZegoConstants.DEBUG){
            Log.e("SmsReceiver", "Exception smsReceiver" +e);
          }


        }
      }
    };
    registerReceiver(smsBrReceiver,filter);
  }





}
