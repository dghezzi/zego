package it.sharethecity.mobile.letzgo.activities;

import android.content.Intent;
import android.support.annotation.Nullable;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.exception.AuthenticationException;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import butterknife.Optional;
import it.sharethecity.mobile.letzgo.R;
import it.sharethecity.mobile.letzgo.application.ApplicationController;
import it.sharethecity.mobile.letzgo.customviews.ItalicRelewayEditTextView;
import it.sharethecity.mobile.letzgo.customviews.ItalicRelewayTextView;
import it.sharethecity.mobile.letzgo.customviews.PopUpDialog;
import it.sharethecity.mobile.letzgo.customviews.RegularRelewayEditTextView;
import it.sharethecity.mobile.letzgo.dao.User;
import it.sharethecity.mobile.letzgo.network.NetworkManager;
import it.sharethecity.mobile.letzgo.network.interfaces.UserRESTInterface;
import it.sharethecity.mobile.letzgo.network.request.ErrorObject;
import it.sharethecity.mobile.letzgo.network.request.StripeCreateCustomerRequest;
import it.sharethecity.mobile.letzgo.network.utils.NetworkErrorHandler;
import it.sharethecity.mobile.letzgo.utilities.UtilityFunctions;
import it.sharethecity.mobile.letzgo.utilities.ZegoConstants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static java.security.AccessController.getContext;

public class PaymentMethodActivity extends ZegoBaseActivity {


    @Nullable
    @BindView(R.id.avanti_button)
    Button avantiButton;

    @Nullable
    @BindView(R.id.header)
    RelativeLayout headerLayout;

    @Nullable
    @BindView(R.id.cc_number_layout)
    LinearLayout ccLayout;

    @Nullable
    @BindView(R.id.cvv_layout)
    LinearLayout cvvLayout;

    @Nullable
    @BindView(R.id.cvv_edit_text)
    ItalicRelewayEditTextView cvvEditText;

    @Nullable
    @BindView(R.id.month_year_edit_text)
    ItalicRelewayEditTextView mmAAEditText;

    @Nullable
    @BindView(R.id.first_digits_block_edit_text)
    RegularRelewayEditTextView firtsDigitsBlockEditText;

    @Nullable
    @BindView(R.id.second_digits_block_edit_text)
    RegularRelewayEditTextView secondDigitsBlockEditText;

    @Nullable
    @BindView(R.id.third_digits_block_edit_text)
    RegularRelewayEditTextView thirdDigitsBlockEditText;

    @Nullable
    @BindView(R.id.fourth_digits_block_edit_text)
    RegularRelewayEditTextView fourthDigitsBlockEditText;

    @Nullable
    @BindView(R.id.hint_error)
    ItalicRelewayTextView hintError;


    private boolean isFormOk;
    private User user;
    private String mmaaPrev = "";
    private Card card;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_method);
        ButterKnife.bind(this);
        setUI();

        if(getIntent().getExtras() != null){
            fromActivity = getIntent().getExtras().getString(FROM_ACTIVITY);
            aheadTextView.setVisibility(View.INVISIBLE);
        }else{
            fromActivity = null;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        user = ApplicationController.getInstance().getUserLogged();
        if (user == null) {
            // TODO: 15/11/16 gestire il caso
        }
    }

    private void setUI() {
        headerLayout.getLayoutParams().height = (int) ((HEADER_PERCENT_HEIGHT) * ApplicationController.getInstance().getScreenDimension().heightPixels);
        avantiButton.getLayoutParams().height = (int) ((BOTTOM_BUTTON_PERCENT_HEIGHT) * ApplicationController.getInstance().getScreenDimension().heightPixels);
        ccLayout.getLayoutParams().height = (int) ((0.057f) * ApplicationController.getInstance().getScreenDimension().heightPixels);
        cvvLayout.getLayoutParams().height = (int) ((0.057f) * ApplicationController.getInstance().getScreenDimension().heightPixels);
        ((RelativeLayout.LayoutParams) cvvLayout.getLayoutParams()).topMargin = (int) ((0.057f) * ApplicationController.getInstance().getScreenDimension().heightPixels);
        ((RelativeLayout.LayoutParams) ccLayout.getLayoutParams()).topMargin = (int) ((0.057f) * ApplicationController.getInstance().getScreenDimension().heightPixels);

        titleTextView.setText(getString(R.string.payment_method));
        aheadTextView.setText(getString(R.string.skip));

        firtsDigitsBlockEditText.requestFocus();
    }


    @Optional
    @OnTextChanged(R.id.first_digits_block_edit_text)
    public void onFirstDigitBlockTextchanged() {
        if (firtsDigitsBlockEditText.getText().length() >= 4) {
            firtsDigitsBlockEditText.clearFocus();
            secondDigitsBlockEditText.requestFocus();
        }
        setEditTextMaxLength(cvvEditText,firtsDigitsBlockEditText.getText().toString().startsWith("3") ? 4 : 3);
        isFormOk = isFormOk();
        changeAvantiButtonState();
    }

    @Optional
    @OnTextChanged(R.id.second_digits_block_edit_text)
    public void onSecondDigitBlockTextchanged() {
        if (secondDigitsBlockEditText.getText().length() >= 4) {
            secondDigitsBlockEditText.clearFocus();
            thirdDigitsBlockEditText.requestFocus();
        }
        isFormOk = isFormOk();
        changeAvantiButtonState();
    }

    @Optional
    @OnTextChanged(R.id.third_digits_block_edit_text)
    public void onThirdDigitBlockTextchanged() {
        if (thirdDigitsBlockEditText.getText().length() >= 4) {
            thirdDigitsBlockEditText.clearFocus();
            fourthDigitsBlockEditText.requestFocus();
        }

        isFormOk = isFormOk();
        changeAvantiButtonState();
    }

    @Optional
    @OnTextChanged(R.id.fourth_digits_block_edit_text)
    public void onFourthDigitBlockTextchanged() {
        if (fourthDigitsBlockEditText.getText().length() >= 4) {
            fourthDigitsBlockEditText.clearFocus();
            mmAAEditText.requestFocus();
        }
        isFormOk = isFormOk();
        changeAvantiButtonState();
    }

    @Optional
    @OnTextChanged(R.id.cvv_edit_text)
    public void onCvvDigitBlockTextchanged() {
        isFormOk = isFormOk();
        changeAvantiButtonState();
    }

    @Optional
    @OnTextChanged(R.id.month_year_edit_text)
    public void onMMAADigitBlockTextchanged() {
        String s = mmAAEditText.getText().toString();

        if (mmaaPrev.length() < s.length() && (mmAAEditText.getText().length() == 2 && !s.contains("/"))) {
            s = s.substring(0, 2) + "/"; //+ s.substring(2,3);
            mmAAEditText.setText(s);
            mmAAEditText.setSelection(s.length());
        }
        if (mmAAEditText.getText().length() >= 5) {
            mmAAEditText.clearFocus();
            cvvEditText.requestFocus();
        }
        isFormOk = isFormOk();
        changeAvantiButtonState();
        mmaaPrev = s;
    }


    private boolean isFormOk() {

        String mm = "";
        String aa = "";
        String cvv = "";
        String cardNumber = "";

//        if (cvvEditText.getText().length() != 3) {
        if (cvvEditText.getText().length() <  3) {
            return false;
        }else{
            cvv = cvvEditText.getText().toString();
        }

        String monthYear = mmAAEditText.getText().toString();
        String[] ms = monthYear.split("/");
        if (ms.length != 2){
            return false;
        }else {
            mm = ms[0];
            aa = ms[1];
        }


         cardNumber = firtsDigitsBlockEditText.getText().toString() +
                    "-" + secondDigitsBlockEditText.getText().toString() +
                    "-" + thirdDigitsBlockEditText.getText().toString() +
                    "-" + fourthDigitsBlockEditText.getText().toString();

        card = new Card(cardNumber,Integer.valueOf(ms[0]),Integer.valueOf(ms[1]),cvv);
        return card.validateCard();

//        if (firtsDigitsBlockEditText.getText().length() != 4) {
//            return false;
//        }
//        if (secondDigitsBlockEditText.getText().length() != 4) {
//            return false;
//        }
//        if (thirdDigitsBlockEditText.getText().length() != 4) {
//            return false;
//        }
//        if (fourthDigitsBlockEditText.getText().length() != 4) {
//            return false;
//        }
//
//        if (mmAAEditText.getText().length() < 5) {
//            return false;
//        }
//
//        if (cvvEditText.getText().length() != 3) {
//            return false;
//        }


    }


    public void setEditTextMaxLength(EditText editText, int length) {
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(length);
        editText.setFilters(FilterArray);
    }

    @Optional
    @OnClick(R.id.right_text_view)
    public void onSkipClick(){
        if(fromActivity ==  null){
            goToMainActivity();
        }

    }

    @Optional
    @OnClick(R.id.back_button)
    public void onBackClick(){
        if(fromActivity == null){
            super.onBackPressed();
        }else{
            setResult(RESULT_OK);
            finish();
        }

    }

    @Optional
    @OnClick(R.id.avanti_button)
    public void onAvantiClick(){
        String cardNumber = firtsDigitsBlockEditText.getText().toString() +
                "-" + secondDigitsBlockEditText.getText().toString() +
                "-" + thirdDigitsBlockEditText.getText().toString() +
                "-" + fourthDigitsBlockEditText.getText().toString();

        String monthYear = mmAAEditText.getText().toString();
        String[] ms = monthYear.split("/");
        if(ms.length == 2){

            Card card = new Card(cardNumber, Integer.valueOf(ms[0]),Integer.valueOf(ms[1])  , cvvEditText.getText().toString());
            if (!card.validateCard()) {
                // TODO: 15/11/16 dialog per carta non valida
            }else{
                sendInfoCard(card);
            }
        }

    }

    private void changeAvantiButtonState(){
        avantiButton.setEnabled(isFormOk);
        avantiButton.setBackground(ContextCompat.getDrawable(getBaseContext(),isFormOk ? R.drawable.green_button_selector : R.color.gray_button));
        hintError.setText("");
        if(isFormOk){
            closeKeyBoard(avantiButton);
        }
    }

    private void goToMainActivity(){
        Intent i = new Intent(PaymentMethodActivity.this,user.isDriver() ? DriverMainActivity.class : MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
    }

    private void sendInfoCard(Card card){
        Stripe stripe = null;
        try {
            stripe = new Stripe(getBaseContext(),ZegoConstants.DEBUG ? ZegoConstants.StripeConstants.TEST_PUBLISHABLE_KEY : ZegoConstants.StripeConstants.PUBLISHABLE_KEY);
            showOrDismissProgressWheel(SHOW);
            stripe.createToken(
                    card,
                    new TokenCallback() {
                        public void onSuccess(Token token) {
                            showOrDismissProgressWheel(DISMISS);
                            StripeCreateCustomerRequest stripeCreateCustomerRequest = new StripeCreateCustomerRequest();
                            stripeCreateCustomerRequest.setUid(user.getId());
                            stripeCreateCustomerRequest.setToken(token.getId());
                            Call<User> call = NetworkManager.getInstance().getRetrofit().create(UserRESTInterface.class).sendCardInfo(user.getZegotoken(),stripeCreateCustomerRequest);
                            showOrDismissProgressWheel(SHOW);
                            call.enqueue(new Callback<User>() {
                                @Override
                                public void onResponse(Call<User> call, Response<User> response) {
                                    showOrDismissProgressWheel(DISMISS);
                                    if(response.isSuccessful()){
                                        ApplicationController.getInstance().saveUser(response.body());
                                        ApplicationController.getInstance().saveMethodCash(0);
                                        if(fromActivity == null) {

                                            goToMainActivity();
//                                            PopUpDialog.showPopUpDialog(PaymentMethodActivity.this, getString(R.string.title_dialog_card_ok), getString(R.string.body_welcome_dialog), getString(R.string.ok), 0, null, new PopUpDialog.DialogActionListener() {
//                                                @Override
//                                                public void actionListener() {
//                                                        goToMainActivity();
//                                                    }
//                                                @Override
//                                                public void negativeAction() {
//
//                                                }
//                                            });
                                        }else {
                                                setResult(RESULT_OK);
                                            finish();
                                        }


                                    }else{
                                        if(response.code() == ZegoConstants.ApiRestConstants.ERROR_500){
                                            int errorColor = ContextCompat.getColor(getBaseContext(),R.color.red_error);
                                            ErrorObject errorObject = NetworkErrorHandler.getInstance().decodeError(response);
                                            hintError.setText(errorObject != null ? errorObject.getMsg() : getString(R.string.unknown_error));
                                            hintError.setTextColor(errorColor);
                                        }else{
                                            NetworkErrorHandler.getInstance().errorHandler(response,PaymentMethodActivity.this);
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
                        public void onError(Exception error) {
                            showOrDismissProgressWheel(DISMISS);
                            // Show localized error message
                            showInfoDialog(getString(R.string.warning),error.getMessage());
                            if(ZegoConstants.DEBUG)
                                 Log.e("STRIPE_ERROR:",error.getMessage());
                        }
                    }
            );
        } catch (AuthenticationException e) {
            e.printStackTrace();
        }
    }


}
