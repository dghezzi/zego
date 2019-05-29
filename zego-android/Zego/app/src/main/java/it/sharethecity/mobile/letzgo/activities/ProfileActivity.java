package it.sharethecity.mobile.letzgo.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import it.sharethecity.mobile.letzgo.R;
import it.sharethecity.mobile.letzgo.adapters.CountryPrefixAdapter;
import it.sharethecity.mobile.letzgo.application.ApplicationController;
import it.sharethecity.mobile.letzgo.bus.BusRequestMessage;
import it.sharethecity.mobile.letzgo.customviews.ItalicRelewayTextView;
import it.sharethecity.mobile.letzgo.customviews.MyFontTextView;
import it.sharethecity.mobile.letzgo.customviews.PopUpDialog;
import it.sharethecity.mobile.letzgo.customviews.PopUpThreeActionDialog;
import it.sharethecity.mobile.letzgo.dao.Address;
import it.sharethecity.mobile.letzgo.dao.User;
import it.sharethecity.mobile.letzgo.network.NetworkManager;
import it.sharethecity.mobile.letzgo.network.interfaces.UserRESTInterface;
import it.sharethecity.mobile.letzgo.network.request.ErrorObject;
import it.sharethecity.mobile.letzgo.network.utils.NetworkErrorHandler;
import it.sharethecity.mobile.letzgo.utilities.CircleTransform;
import it.sharethecity.mobile.letzgo.utilities.ZegoConstants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class ProfileActivity extends ZegoBaseActivity {


    private static final int FROM_CAMERA = 1;
    private static final int FROM_GALLERY = 2;
    private static final int WRITE_EXTERNAL_PERMISSION_REQUEST = 58;
    private static final int FROM_FACEBOOK_CONNECTION = 23;

    @Nullable
    @BindView(R.id.save_profile_button)
    Button saveProfileButton;

    @Nullable
    @BindView(R.id.profile_image)
    ImageView profileImageView;

    @Nullable
    @BindView(R.id.header)
    RelativeLayout headerLayout;

    @Nullable
    @BindView(R.id.name_edit_text)
    ItalicRelewayTextView nameTextView;

    @Nullable
    @BindView(R.id.lastname_edit_text)
    ItalicRelewayTextView lnameTextView;

    @Nullable
    @BindView(R.id.email_edit_text)
    ItalicRelewayTextView emailTextView;

    @Nullable
    @BindView(R.id.address_lavoro_edit_text)
    ItalicRelewayTextView addressLavoroTextView;

    @Nullable
    @BindView(R.id.address_edit_text)
    ItalicRelewayTextView addressHomeTextView;

    @Nullable
    @BindView(R.id.email_lavoro_edit_text)
    ItalicRelewayTextView emailLavoroTextView;


    @Nullable
    @BindView(R.id.country_prefix_field)
    ItalicRelewayTextView prefixTextView;

    @Nullable
    @BindView(R.id.cell_field)
    ItalicRelewayTextView mobileTextView;

    @Nullable
    @BindView(R.id.fb_button)
    LinearLayout fbButton;

    @Nullable
    @BindView(R.id.edit_layout_mobile)
    RelativeLayout mobileLayout;

    @Nullable
    @BindView(R.id.profile_image_layout)
    LinearLayout profileImageButton;


    @Nullable
    @BindView(R.id.edit_name_button)
    ImageView nameEditButton;

    @Nullable
    @BindView(R.id.edit_last_name_button)
    ImageView lnameEditButton;

    @Nullable
    @BindView(R.id.edit_email_button)
    ImageView emailEditButton;

    @Nullable
    @BindView(R.id.edit_email_lavoro_button)
    ImageView emailLavoroEditButton;

    @Nullable
    @BindView(R.id.edit_home_address_button)
    ImageView homeAddressEditButton;

    @Nullable
    @BindView(R.id.edit_address_lavoro_button)
    ImageView addressLavoroEditButton;


    @Nullable
    @BindView(R.id.p_spinner_country)
    Spinner spinnerCountry;

    @Nullable
    @BindView(R.id.logout_text_view)
    MyFontTextView logoutTextView;

    @Nullable
    @BindView(R.id.log_out_div)
    View logOutDividerView;

    @Nullable
    @BindView(R.id.edit_mobile_button)
    ImageView mobileEditButton;


    private boolean isFromOncreate;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        setUI();
        setCallbackManagerFacebook();
        isFromOncreate = true;
    }

    private void getUserById(User u) {
        Call<User> call = NetworkManager.getInstance().getRetrofit().create(UserRESTInterface.class).getUserById(u.getZegotoken(),u.getId());
        showOrDismissProgressWheel(SHOW);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                showOrDismissProgressWheel(DISMISS);
                if(response.isSuccessful()){
                    user = response.body();
                    fillForm(response.body());
                }else if(response.code() == ZegoConstants.ApiRestConstants.ERROR_500){
                    // TODO: 25/11/16
                }else{
                    NetworkErrorHandler.getInstance().errorHandler(response,ProfileActivity.this);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                showOrDismissProgressWheel(DISMISS);
                showInfoDialog(getString(R.string.warning),getString(R.string.impossible_to_connetc_to_server));
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        user = ApplicationController.getInstance().getUserLogged();
        if(user != null){
            if(isFromOncreate){
                getUserById(user);
            }else{
                fillForm(user);
            }

            isFromOncreate = false;

            getUserAddress(user.getZegotoken(),user.getId());
        }else {
            // TODO: 22/11/16 l'utente non risulta loggato decidere cosa fare
        }

    }



    @Override
    protected void onResume() {
        super.onResume();
    }


    @Optional
    @OnClick(R.id.profile_image_layout)
    public void onImageClick(){
        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            ActivityCompat.requestPermissions(ProfileActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_PERMISSION_REQUEST);
        }else{
            showChangeProfileImageDialog();
        }


    }


    private void showChangeProfileImageDialog(){

        PopUpThreeActionDialog.ThreeActionPopUpBuilder v = new PopUpThreeActionDialog.ThreeActionPopUpBuilder(this);
        v.setMsg(getString(R.string.load_photo))
                .setFirstTextButton(getString(R.string.camera))
                .setSecondTextButton(getString(R.string.gallery))
                .setThirdTextButton(getString(R.string.annulla))
                .setListener(new PopUpThreeActionDialog.DialogThreeActionListener() {
                    @Override
                    public void firstActionListener() {
                        Intent intent = new Intent(ProfileActivity.this, CroppingImageActivity.class);
                        intent.putExtra("ACTION", CroppingImageActivity.ACTION_CAMERA);
                        intent.putExtra(CroppingImageActivity.TYPE_OF_PHOTO, FROM_CAMERA);
                        startActivityForResult(intent,FROM_CAMERA);
                    }

                    @Override
                    public void secondActionListener() {
                        Intent intent = new Intent(ProfileActivity.this, CroppingImageActivity.class);
                        intent.putExtra("ACTION", CroppingImageActivity.ACTION_GALLERY);
                        startActivityForResult(intent,FROM_GALLERY);
                    }

                    @Override
                    public void thirdActionListener() {

                    }
                });
        v.createDialog();

//        PopUpDialog.showConfirmPopUpDialog(ProfileActivity.this, "", getString(R.string.load_photo),
//                getString(R.string.camera), getString(R.string.gallery), 0, null, new PopUpDialog.DialogActionListener() {
//                    @Override
//                    public void actionListener(){
//                        Intent intent = new Intent(ProfileActivity.this, CroppingImageActivity.class);
//                        intent.putExtra("ACTION", CroppingImageActivity.ACTION_CAMERA);
//                        intent.putExtra(CroppingImageActivity.TYPE_OF_PHOTO, FROM_CAMERA);
//                        startActivityForResult(intent,FROM_CAMERA);
//                    }
//
//                    @Override
//                    public void negativeAction() {
//                        Intent intent = new Intent(ProfileActivity.this, CroppingImageActivity.class);
//                        intent.putExtra("ACTION", CroppingImageActivity.ACTION_GALLERY);
//                        startActivityForResult(intent,FROM_GALLERY);
//
//
//                    }
//                });
    }

    private void getUserAddress(String token,Integer uid){
        Call<List<Address>> call = NetworkManager.getInstance().getRetrofit().create(UserRESTInterface.class).getUserAddress(token,uid);
        //showOrDismissProgressWheel(SHOW);
        call.enqueue(new Callback<List<Address>>() {
            @Override
            public void onResponse(Call<List<Address>> call, Response<List<Address>> response) {
             //   showOrDismissProgressWheel(DISMISS);
                if(response.isSuccessful()){
                    if(response.body() == null || response.body().isEmpty()) return;

                    for(Address address : response.body()){
                        if(address.getType() != null){
                            if(address.getType().equalsIgnoreCase(Address.HOME)){
                                addressHomeTextView.setText(address.getAddress());
                                addressHomeTextView.setTag(address);
                            }else if (address.getType().equalsIgnoreCase(Address.WORK)){
                                addressLavoroTextView.setText(address.getAddress());
                                addressLavoroTextView.setTag(address);
                            }
                        }
                    }
                }else if(response.code() == ZegoConstants.ApiRestConstants.ERROR_500){
                    // TODO: 25/11/16  decidere cosa fare
                }else{
                    NetworkErrorHandler.getInstance().errorHandler(response,ProfileActivity.this);
                }
            }

            @Override
            public void onFailure(Call<List<Address>> call, Throwable t) {
               // showOrDismissProgressWheel(DISMISS);
                showInfoDialog(getString(R.string.warning),getString(R.string.impossible_to_connetc_to_server));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if((requestCode == FROM_CAMERA || requestCode == FROM_GALLERY) && resultCode == RESULT_OK) {
            String profileImageName = data.getExtras().getString(CroppingImageActivity.PROFILE_IMAGE_NAME);
            if (!profileImageName.isEmpty()) {
                user.setImg(ZegoConstants.AWS3.URL_BACKET+profileImageName);
                postProfile(user,requestCode);
            }
        } else if((requestCode == FROM_CAMERA || requestCode == FROM_GALLERY)){

        }
        else{
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }


    }

    @Optional
    @OnClick(R.id.fb_button)
    public void onFbButton(){
        showOrDismissProgressWheel(SHOW);
        LoginManager.getInstance().logInWithReadPermissions(ProfileActivity.this, Arrays.asList("public_profile", "email"));
    }

    @Optional
    @OnClick(R.id.logout_text_view)
    public void onLogOutClick(){

        if(user.getRtstatus() % 100 != 0){
           Toast.makeText(getBaseContext(),getString(R.string.logout_not_allowed),Toast.LENGTH_SHORT).show();
        }else{
            PopUpDialog.showConfirmPopUpDialog(ProfileActivity.this, getString(R.string.logout), getString(R.string.logout_msg),
                    getString(android.R.string.yes), getString(android.R.string.no), 0, null, new PopUpDialog.DialogActionListener() {
                        @Override
                        public void actionListener() {
                            killPollingRideService();
                            killPositionService();
                            sendLogOut(user);
                            goToEntryActivity();
                        }

                        @Override
                        public void negativeAction() {

                        }
                    });
        }



    }

    private void setCallbackManagerFacebook(){
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                showOrDismissProgressWheel(DISMISS);
               // String fbId = loginResult.getAccessToken().getUserId();
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

    @Optional
    @OnClick({R.id.name_edit_text,R.id.edit_name_button,R.id.lastname_edit_text,R.id.edit_last_name_button,R.id.email_edit_text,R.id.edit_email_button,R.id.email_lavoro_edit_text,R.id.edit_email_lavoro_button, R.id.edit_layout_mobile,R.id.edit_mobile_button})
   // @OnClick({R.id.name_edit_text,R.id.edit_name_button,R.id.lastname_edit_text,R.id.edit_last_name_button,R.id.email_edit_text,R.id.edit_email_button,R.id.email_lavoro_edit_text,R.id.edit_email_lavoro_button})
    public void onEditsButton(View v){
        String title = "";
        int type = 0;
        switch (v.getId()){
            case R.id.edit_name_button:
            case R.id.name_edit_text:
                title = getString(R.string.edit_name);
                type = EditActivity.NAME_EDIT;
                break;
            case R.id.edit_last_name_button:
            case R.id.lastname_edit_text:
                title = getString(R.string.edit_surname);
                type = EditActivity.LAST_NAME_EDIT;
                break;
            case R.id.edit_email_button:
            case R.id.email_edit_text:
                title = getString(R.string.edit_email);
                type = EditActivity.EMAIL_EDIT;
                break;
            case R.id.edit_email_lavoro_button:
            case R.id.email_lavoro_edit_text:
                title = getString(R.string.edit_wemail);
                type = EditActivity.EMAIL_WORK_EDIT;
                break;
            case R.id.edit_mobile_button:
            case R.id.edit_layout_mobile:
                title = getString(R.string.edit_mobile);
                type = EditActivity.MOBILE_EDIT;
                break;


        }


        Intent  i = new Intent(ProfileActivity.this,EditActivity.class);

        i.putExtra(EditActivity.TITLE,title);
        i.putExtra(EditActivity.TYPE,type);

        startActivity(i);
    }

    @Optional
    @OnClick({R.id.address_edit_text,R.id.edit_address_lavoro_button,R.id.address_lavoro_edit_text,R.id.edit_home_address_button})
    public void onEditAddressesButton(View v){
        String title = "";
        int type = 0;
        Address address = null;
        switch (v.getId()){
            case R.id.edit_home_address_button:
            case R.id.address_edit_text:
                title = getString(R.string.edit_home_address);
                type = AddressActivity.HOME_ADDRESS_EDIT;
                address = (Address) addressHomeTextView.getTag();
                break;
            case R.id.edit_address_lavoro_button:
            case R.id.address_lavoro_edit_text:
                title = getString(R.string.edit_work_address);
                type = AddressActivity.WORK_ADDRESS_EDIT;
                address = (Address) addressLavoroTextView.getTag();
                break;
        }
        Intent i = new Intent(ProfileActivity.this,AddressActivity.class);
        i.putExtra(AddressActivity.TITLE,title);
        i.putExtra(AddressActivity.ADDRESS,address);
        i.putExtra(AddressActivity.TYPE,type);

        startActivity(i);
    }

    @Optional
    @OnClick(R.id.back_button)
    public void onBackClick(){
        super.onBackPressed();
    }



    private void fillForm(User u) {


        nameTextView.setText(u.getFname());
        lnameTextView.setText(u.getLname());
        emailTextView.setText(u.getEmail());
        emailLavoroTextView.setText(u.getWemail());
        spinnerCountry.setSelection(getCountryPosition(u.getPrefix()));
        prefixTextView.setText(u.getPrefix());
        mobileTextView.setText(u.getFormattedMobile());

        lnameTextView.setClickable(!(u.isFB()|| u.getCandrive() == 1));
        nameTextView.setClickable(!(u.isFB()|| u.getCandrive() == 1));

        nameEditButton.setEnabled(!(u.isFB() || u.getCandrive() == 1));
        lnameEditButton.setEnabled(!(u.isFB()|| u.getCandrive() == 1));
        nameEditButton.setVisibility(u.isFB()|| u.getCandrive() == 1 ? INVISIBLE : VISIBLE);
        lnameEditButton.setVisibility(u.isFB()|| u.getCandrive() == 1 ? INVISIBLE : VISIBLE);
        fbButton.setVisibility(u.isFB() ?  View.GONE : View.VISIBLE );
        logOutDividerView.setVisibility(user.isFB() ? View.INVISIBLE : VISIBLE);

//        int imageSize = getResources().getDimensionPixelSize(R.dimen.profile_image_dimen);
        if(user.hasImage()) {
            Picasso.with(getBaseContext())
                    .load(u.getImg())
                    .transform(new CircleTransform())
                    .placeholder(R.drawable.user_placeholder)
                    .into(profileImageView);
        }
    }

    private void setUI() {
        headerLayout.getLayoutParams().height = (int) ((HEADER_PERCENT_HEIGHT)* ApplicationController.getInstance().getScreenDimension().heightPixels);
        saveProfileButton.getLayoutParams().height = (int) ((BOTTOM_BUTTON_PERCENT_HEIGHT)*ApplicationController.getInstance().getScreenDimension().heightPixels);
        titleTextView.setText(getString(R.string.profile_title));
        spinnerCountry.setAdapter(new CountryPrefixAdapter(this));
        spinnerCountry.setEnabled(false);
    }

    private void postProfile(User u,final int fromRequest){
        Call<User> call = NetworkManager.getInstance().getRetrofit().create(UserRESTInterface.class).postUser(u.getZegotoken(),u);
        showOrDismissProgressWheel(SHOW);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                showOrDismissProgressWheel(DISMISS);
                if(response.isSuccessful()){
                    ApplicationController.getInstance().saveUser(response.body());
                    if(fromRequest == FROM_FACEBOOK_CONNECTION){
                        Toast.makeText(getBaseContext(),getString(R.string.now_connected_with_fb),Toast.LENGTH_LONG).show();
                    }

                    user = response.body();
                    fillForm(response.body());

                }else if(response.code() == ZegoConstants.ApiRestConstants.ERROR_500){
                    ErrorObject errorObject = NetworkErrorHandler.getInstance().decodeError(response);
                    showInfoDialog(getString(R.string.warning),errorObject != null ? errorObject.getMsg() : getString(R.string.unknown_error));
                }else{
                    NetworkErrorHandler.getInstance().errorHandler(response,ProfileActivity.this);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                showOrDismissProgressWheel(DISMISS);
                showInfoDialog(getString(R.string.warning),getString(R.string.impossible_to_connetc_to_server));
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case WRITE_EXTERNAL_PERMISSION_REQUEST:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    showChangeProfileImageDialog();
                } else {
                    showInfoDialog(getString(R.string.warning),getString(R.string.permission_not_granted));
                }
                break;
        }
    }


//    private void setCallbackManagerFacebook(){
//        callbackManager = CallbackManager.Factory.create();
//        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//                signUpFromFacebook(loginResult);
//            }
//
//            @Override
//            public void onCancel() {
//                showOrDismissProgressWheel(DISMISS);
//            }
//
//            @Override
//            public void onError(FacebookException e) {
//                showOrDismissProgressWheel(DISMISS);
//            }
//        });
//
//    }

    private void signUpFromFacebook(LoginResult loginResult){
        GraphRequest request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {
                        User u =  readUserFromFacebook(object);
                        user.setFbid(u.getFbid());
                        user.setFname(u.getFname());
                        user.setLname(u.getLname());
                        user.setGender(u.getGender());
                        user.setBirthdate(u.getBirthdate());
                        LoginManager.getInstance().logOut();
                        postProfile(user,FROM_FACEBOOK_CONNECTION);

//                      showInfoDialog(getString(R.string.warning),getString(R.string.impossible_to_connect_with_fb));

                    }
                });
        Bundle parameters = new Bundle();

        //Add the fields that you need, you dont forget add the right permission
        parameters.putString("fields", "email,id,first_name,last_name,gender");
        request.setParameters(parameters);
        request.executeAsync();
    }



}
