package it.sharethecity.mobile.letzgo.customviews;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import it.sharethecity.mobile.letzgo.R;
import it.sharethecity.mobile.letzgo.dao.Service;
import it.sharethecity.mobile.letzgo.network.response.EtaResponse;

/**
 * Created by lucabellaroba on 12/12/16.
 */

public class PickUpStatusView extends RelativeLayout {


    @Nullable
    @BindView(R.id.pick_up_console)
    PickUpView pickUpConsole;

    @Nullable
    @BindView(R.id.my_position_button)
    ImageButton myPositionButton;

    @Nullable
    @BindView(R.id.confirm_starting_point_button)
    Button confirmStartingPoint;

    @Nullable
    @BindView(R.id.femaleTypeButton)
    View pinkRideButton;

    @Nullable
    @BindView(R.id.zegoStandardButton)
    View standardRideButton;

    @Nullable
    @BindView(R.id.premiumTypeButton)
    View premiumRideButton;


    @Nullable
    @BindView(R.id.female_driver_layout)
    View pinkRideButtonLayout;

    @Nullable
    @BindView(R.id.zego_driver_layout)
    View standardRideButtonLayout;

    @Nullable
    @BindView(R.id.premium_driver_layout)
    View controlRideButtonLayout;

    @Nullable
    @BindView(R.id.service)
    RelativeLayout serviceLayout;

    @Nullable
    @BindView(R.id.controlZego)
    MyFontTextView controlZego;

    @Nullable
    @BindView(R.id.controlZegoRide)
    MyFontTextView controlZegoRide;

    @Nullable
    @BindView(R.id.pinkZego)
    MyFontTextView pinkZego;

    @Nullable
    @BindView(R.id.pinkZegoRide)
    MyFontTextView pinkZegoRide;




    private PickUpViewStatusListener listener;

    public PickUpStatusView(Context context) {
        super(context);
        init(true);
    }

    public PickUpStatusView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(false);
    }

    public PickUpStatusView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(false);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public PickUpStatusView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(false);
    }


    private void  init(boolean b){
        inflate(getContext(), R.layout.pickup_status_view,this);
        if(b){
            ButterKnife.bind(this);
        }
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
    }

    @Nullable
    public PickUpView getPickUpConsole() {
        return pickUpConsole;
    }

    public void setPickUpConsole(@Nullable PickUpView pickUpConsole) {
        this.pickUpConsole = pickUpConsole;
    }

    public void updateData(EtaResponse etaResponse){
        if(etaResponse != null && etaResponse.getAddress()!= null && !etaResponse.getAddress().isEmpty()){
            pickUpConsole.setEta(etaResponse.getEta());
            pickUpConsole.setPickUpAddress(etaResponse.getAddress());
          //  pickUpConsole.enableCancelButton(true);
            enableButton(true);
        }
    }

    @Optional
    @OnClick(R.id.confirm_starting_point_button)
    public void onConfirmButtonClick(){
        if(listener != null){
            listener.onCofirmStartingPoint();
        }
    }


    @Optional
    @OnClick({R.id.femaleTypeButton,R.id.premiumTypeButton,R.id.zegoStandardButton})
    public void onTypeRideButtonClick(View v){
        if(listener != null){
            int level = 0;
            switch (v.getId()){
                case R.id.femaleTypeButton:
                    Service s = (Service)pinkRideButton.getTag();
                    level = s != null ? s.getLevel() : 0;
                    pickUpConsole.setTypeRideImage(R.drawable.tondo_pink);
                    break;
                case R.id.premiumTypeButton:
                    Service s2 = (Service)premiumRideButton.getTag();
                    level = s2 != null ? s2.getLevel() : 0;
                    pickUpConsole.setTypeRideImage(R.drawable.tondo_control);
                    break;
                default:
                    level = 0;
                    pickUpConsole.setTypeRideImage(R.drawable.tondo_standard);
                    break;
            }

            serviceLayout.setVisibility(GONE);
            listener.onRideType(level);
        }
    }


    @Optional
    @OnClick(R.id.my_position_button)
    public void onMyPositionButtonClick(){
        if(listener != null){
            listener.onMyPositionClick();
        }
    }

    public void setListener(PickUpViewStatusListener listener) {
        this.listener = listener;
        pickUpConsole.setListener(listener);
    }

    public void resetView(){
        pickUpConsole.resetView();
        updateTypeOfRide(0,null);
        enableButton(false);
    }

    public void enableButton(boolean enable){
        confirmStartingPoint.setEnabled(enable);
        confirmStartingPoint.setBackground(ContextCompat.getDrawable(getContext(), enable ? R.drawable.green_button_selector :  R.color.gray_button));
    }


    public void showTypeOfRideSelector(boolean show){
        serviceLayout.setVisibility(show ? VISIBLE : GONE);
    }

    public void toggleTypeOfRideSelector(){
        boolean isGone = serviceLayout.getVisibility() == GONE;
        if(premiumRideButton.getTag() == null && pinkRideButton.getTag() == null) {
            isGone = false;
        }

        showTypeOfRideSelector(isGone);
    }

    public void updateTypeOfRide(Integer typeOfRideSelected,List<Service> services) {
        pinkRideButton.setTag(null);
        premiumRideButton.setTag(null);
        pinkRideButtonLayout.setVisibility(GONE);
        controlRideButtonLayout.setVisibility(GONE);
        pickUpConsole.setTypeRideImage(R.drawable.tondo_standard);


        if(services == null || services.isEmpty()){
            pinkRideButton.setTag(null);
            premiumRideButton.setTag(null);
        }else{
            for(Service s : services){
                if (s == null) continue;
                if(s.getName().toLowerCase().contains("rosa")){
                    pinkZego.setText(s.getNameByLang());
                    pinkZegoRide.setText(s.getDetailsByLang());
                    pinkRideButtonLayout.setVisibility(VISIBLE);
                    pinkRideButton.setTag(new Service(s));
                    if(typeOfRideSelected.equals(s.getLevel())){
                        pickUpConsole.setTypeRideImage(R.drawable.tondo_pink);
                    }
                }

                if(s.getName().toLowerCase().contains("control")){
                    controlRideButtonLayout.setVisibility(VISIBLE);
                    premiumRideButton.setTag(new Service(s));
                    controlZego.setText(s.getNameByLang());
                    controlZegoRide.setText(s.getDetailsByLang());
                    if(typeOfRideSelected.equals(s.getLevel())){
                        pickUpConsole.setTypeRideImage(R.drawable.tondo_control);
                    }

                }
            }

        }


    }



    public interface PickUpViewStatusListener extends PickUpView.PickUpViewListener{
        void onCofirmStartingPoint();
        void onMyPositionClick();
        void onRideType(Integer level);
    }
}
