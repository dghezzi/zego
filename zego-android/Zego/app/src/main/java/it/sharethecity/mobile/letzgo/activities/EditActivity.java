package it.sharethecity.mobile.letzgo.activities;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;

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
import it.sharethecity.mobile.letzgo.customviews.ItalicRelewayEditTextView;
import it.sharethecity.mobile.letzgo.customviews.ItalicRelewayTextView;
import it.sharethecity.mobile.letzgo.customviews.RegularRelewayTextView;
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

public class EditActivity extends ZegoBaseActivity {

    public static final String TITLE = "title";
    public static final String TYPE = "type";

    public static final int NAME_EDIT = 0;
    public static final int LAST_NAME_EDIT = 1;
    public static final int EMAIL_EDIT = 2;
    public static final int EMAIL_WORK_EDIT = 3;
    public static final int MOBILE_EDIT = 4;


    @Nullable
    @BindView(R.id.cancel_number_edit)
    ImageView cancelNumberButton;

    @Nullable
    @BindView(R.id.spinner_country)
    Spinner spinnerCountry;

    @Nullable
    @BindView(R.id.edit_mobile_layout)
    RelativeLayout mobileEditLayout;

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
    @BindView(R.id.hint_error_mobile)
    ItalicRelewayTextView hintErrorMobile;



    @Nullable
    @BindView(R.id.save_button)
    Button saveButton;

    @Nullable
    @BindView(R.id.cancel_edit)
    ImageView cancelButton;

    @Nullable
    @BindView(R.id.header)
    RelativeLayout headerLayout;

    @Nullable
    @BindView(R.id.simple_edit_layout)
    RelativeLayout editLayout;

    @Nullable
    @BindView(R.id.edit_field)
    ItalicRelewayEditTextView editField;

    @Nullable
    @BindView(R.id.hint_error_edit_text)
    ItalicRelewayTextView hintError;



    private String data;
    private String title;
    private int type;
    private String countryId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        ButterKnife.bind(this);
        title = getIntent().getExtras().getString(TITLE);
        user = ApplicationController.getInstance().getUserLogged();
        type = getIntent().getExtras().getInt(TYPE);

        if(type == MOBILE_EDIT) {
            spinnerCountry.setAdapter(new CountryPrefixAdapter(this));
            keyBoard.setListener(new CustomNumberKeyBoard.onKeyBoardListener() {
                @Override
                public void onKeyBoardClick(String buttonClick) {
                    String s = mobileNumberTextView.getText().toString();
                    if (s.length() == 3) {
                        s += " ";
                    }
                    s += buttonClick;
                    mobileNumberTextView.setText(s);

                }

                @Override
                public void onCancelClick() {
                    if (mobileNumberTextView.getText().length() > 0) {
                        String s = mobileNumberTextView.getText().toString();
                        s = s.substring(0, s.length() == 4 ? s.length() - 2 : s.length() - 1);
                        mobileNumberTextView.setText(s);
                    }
                }
            });
        }else{
            mobileEditLayout.setVisibility(View.GONE);
            editLayout.setVisibility(View.VISIBLE);
        }



        //   editLayout.setVisibility(View.VISIBLE);


        setUI();

        // user = ApplicationController.getInstance().getUserLogged();

    }

    private void setUI() {
        headerLayout.getLayoutParams().height = (int) ((HEADER_PERCENT_HEIGHT) * ApplicationController.getInstance().getScreenDimension().heightPixels);
        saveButton.getLayoutParams().height = (int) ((BOTTOM_BUTTON_PERCENT_HEIGHT) * ApplicationController.getInstance().getScreenDimension().heightPixels);

        titleTextView.setText(title);
        aheadTextView.setVisibility(View.GONE);

        editField.setText(data);

        switch (type) {
            case NAME_EDIT:
                editField.setText(user.getFname());
                editField.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS|InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
                break;
            case LAST_NAME_EDIT:
                editField.setText(user.getLname());
                editField.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS|InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
                break;
            case EMAIL_EDIT:
                editField.setText(user.getEmail());
                break;
            case EMAIL_WORK_EDIT:
                editField.setText(user.getWemail());
                break;
            case MOBILE_EDIT:
                spinnerCountry.setSelection(user.getPrefix() == null || user.getPrefix().isEmpty() ? 0 : getCountryPosition(user.getPrefix()));
                mobileNumberTextView.setText(user.getFormattedMobile());
                prefixTextView.setText(user.getPrefix());
                mobileEditLayout.setVisibility(View.VISIBLE);
                editLayout.setVisibility(View.GONE);
                break;


        }
    }


    @Optional
    @OnClick(R.id.cancel_edit)
    public void onCancelButton(){
        editField.setText("");
    }

    @Optional
    @OnClick(R.id.save_button)
    public void onSaveButton(){
        Call<User> call = NetworkManager.getInstance().getRetrofit().create(UserRESTInterface.class).getUserById(user.getZegotoken(),user.getId());
        showOrDismissProgressWheel(SHOW);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                showOrDismissProgressWheel(DISMISS);
                if(response.isSuccessful()){
                    switch (type){
                        case NAME_EDIT:
                            response.body().setFname(editField.getText().toString());
                            break;
                        case LAST_NAME_EDIT:
                            response.body().setLname(editField.getText().toString());
                            break;
                        case EMAIL_EDIT:
                            response.body().setEmail(editField.getText().toString());
                            break;
                        case EMAIL_WORK_EDIT:
                            response.body().setWemail(editField.getText().toString());
                            break;
                        case MOBILE_EDIT:
                            response.body().setPrefix(prefixTextView.getText().toString());
                            response.body().setCountry(countryId);
                            response.body().setMobile(mobileNumberTextView.getText().toString());
                            break;


                    }
                    postProfile(response.body());

                }else{
                    NetworkErrorHandler.getInstance().errorHandler(response,EditActivity.this);
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
    @OnTextChanged({R.id.edit_field,R.id.cell_field})
    public void TextChanged(){
        if(type == EMAIL_EDIT || type == EMAIL_WORK_EDIT){
            boolean isEmailOk = UtilityFunctions.mailSyntaxCheck(editField.getText().toString());
            hintError.setText("");
            editField.setTextColor(ContextCompat.getColor(getBaseContext(),isEmailOk ? R.color.gray_keyboard_button : R.color.red_error));
            changeAvantiButtonState(isEmailOk || type == EMAIL_WORK_EDIT);
        }
        else if(type == MOBILE_EDIT){
            hintErrorMobile.setText("");
            boolean isOk = mobileNumberTextView.getText().length() >= (ZegoConstants.MIN_NUMBER_DIGITS + 1)  &&
                    mobileNumberTextView.getText().length() <= (ZegoConstants.MAX_NUMBER_DIGITS + 1);
            changeAvantiButtonState(isOk);
        }
        else if(type == NAME_EDIT || type == LAST_NAME_EDIT){
                hintError.setText("");
                changeAvantiButtonState(editField.getText().length() > 0);
            }else{
                hintError.setText("");
                changeAvantiButtonState(true);
            }

        }

    private void changeAvantiButtonState(boolean isOk){
        saveButton.setEnabled(isOk);
        saveButton.setBackground(ContextCompat.getDrawable(getBaseContext(),isOk ? R.drawable.green_button_selector : R.color.gray_button));
    }

    @Optional
    @OnClick(R.id.back_button)
    public void onBackClick(){
        super.onBackPressed();
    }

    @Optional
    @OnItemSelected(R.id.spinner_country)
    public void spinnerItemSelected(Spinner spinner, int position) {
        prefixTextView.setText(((CountryPrefixAdapter) spinner.getAdapter()).getCountryPrefix()[position]);
        countryId = getResources().getStringArray(R.array.country_id)[position];
    }



    private void postProfile(User u){
        Call<User> call = NetworkManager.getInstance().getRetrofit().create(UserRESTInterface.class).postUser(u.getZegotoken(),u);
        showOrDismissProgressWheel(SHOW);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                showOrDismissProgressWheel(DISMISS);
                if(response.isSuccessful()){
                    if(ZegoConstants.DEBUG){
                        Log.d("EditProfile:",user.toString());
                    }
                    ApplicationController.getInstance().saveUser(response.body());
                    if(response.body().getMobok() != 1){
                        Intent i = new Intent(EditActivity.this,VerificationPinActivity.class);
                        i.putExtra(VerificationPinActivity.USER,response.body());
                        i.putExtra(FROM_ACTIVITY,EditActivity.class.getSimpleName());
                        startActivity(i);
                    }
                    finish();
                }else if(response.code() == ZegoConstants.ApiRestConstants.ERROR_500){
                    ErrorObject errorObject = NetworkErrorHandler.getInstance().decodeError(response);
                    int errorColor = ContextCompat.getColor(getBaseContext(),R.color.red_error);
                    if(type == MOBILE_EDIT){
                        hintErrorMobile.setText(errorObject!= null ? errorObject.getMsg() : getString(R.string.unknown_error));
                        hintErrorMobile.setTextColor(errorColor);
                    }else {
                        hintError.setTextColor(errorColor);
                        hintError.setText(errorObject!= null ? errorObject.getMsg() : getString(R.string.unknown_error));
                    }


                }else{
                    NetworkErrorHandler.getInstance().errorHandler(response,EditActivity.this);
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
    @OnClick({R.id.cancel_edit,R.id.cancel_number_edit})
    public void onCancelButton(View v){
        switch (v.getId()){
            case R.id.cancel_number_edit:
                mobileNumberTextView.setText("");
                break;
            case R.id.cancel_edit:
                editField.setText("");
                break;

        }

    }

}
