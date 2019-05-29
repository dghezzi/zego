package it.sharethecity.mobile.letzgo.customviews;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.percent.PercentRelativeLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import it.sharethecity.mobile.letzgo.R;

/**
 * Created by lucabellaroba on 10/12/16.
 */

public class StartStopView extends PercentRelativeLayout {



    @Nullable
    @BindView(R.id.backgound_view)
    RelativeLayout backGround;

    @Nullable
    @BindView(R.id.person_profile_image_layout)
    RelativeLayout personProfileImageLayout;

    @Nullable
    @BindView(R.id.pickup_addr_text_view)
    MyFontTextView pickUpaddr;


    @Nullable
    @BindView(R.id.dropoff_add_text_view)
    MyFontTextView dropoffAddr;

    @Nullable
    @BindView(R.id.pickup_cancel_img)
    ImageView pickUpCancelButton;

    @Nullable
    @BindView(R.id.dropoff_cancel_img)
    ImageView dropOffCancelButton;

    @Nullable
    @BindView(R.id.pickup_indicator)
    ImageView pickUpIndicator;

    @Nullable
    @BindView(R.id.dropoff_indicator)
    ImageView dropOffindicator;

    int[] idicatorsImg = new int[]{R.drawable.puntogrigio,R.drawable.puntoverde};

    private PickUpDropoffListeners listeners;


    public StartStopView(Context context) {
        super(context);
        init(true);
    }

    public StartStopView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(false);
    }


    public StartStopView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(false);

    }



    private void init(boolean b) {
        setBackgroundColor(Color.WHITE);
        inflate(getContext(), R.layout.pickup_dropoff_layout,this);
        if(b){
            ButterKnife.bind(this);
            setUi();
        }

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
        setUi();
    }

    private void setUi(){
        dropOffCancelButton.setEnabled(false);
    }

    public void setPickUpaddr(String pickUpaddrString){
        pickUpaddr.setText(pickUpaddrString);
    }

    public void setDropOffaddr(String dropoffaddrString){
        dropoffAddr.setText(dropoffaddrString);
       // enableDoAddrCancelButton(true);
    }

    public void setSimpleDoAddress(String dropoffaddrString){
        dropoffAddr.setText(dropoffaddrString);
    }

    @Optional
    @OnClick({R.id.pickup_cancel_img,R.id.dropoff_cancel_img,R.id.pickup_addr_text_view,R.id.dropoff_add_text_view})
    public void onCancelClick(View v){
        switch (v.getId()){
            case R.id.pickup_cancel_img:
                onPickUpCancelAction();
                break;
            case R.id.dropoff_cancel_img:
                onDropffCancelAction();
                break;
            case R.id.pickup_addr_text_view:
                onPickUpClickAction(v.getId());
                break;
            case R.id.dropoff_add_text_view:
                onDropOffClickAction(v.getId());
                break;
        }
    }

    private void onPickUpCancelAction(){
        pickUpaddr.setText("");
        resetView();
        if(listeners != null){
            listeners.onPickUpCancel();
        }
    }

    private void onDropffCancelAction(){
        dropoffAddr.setText("");
        enableDoAddrCancelButton(false);
        if(listeners != null){
            listeners.onDropOffpCancel();
        }
    }

    private  void onPickUpClickAction(int viewId){
      //  switchIndicators(viewId);
        if(listeners != null){
            listeners.onPickUpClick();
        }
    }

    private  void onDropOffClickAction(int viewId){
       // switchIndicators(viewId);
        if(listeners != null){
            listeners.onDropOffClick();
        }
    }



    public void setListeners(PickUpDropoffListeners listeners) {
        this.listeners = listeners;
    }

    public void resetView(){
        pickUpaddr.setText("");
        dropoffAddr.setText("");
        enableDoAddrCancelButton(false);
    }


    public void enableDoAddrCancelButton(boolean enable){
        dropOffCancelButton.setEnabled(enable);
        dropOffCancelButton.setVisibility(enable ? VISIBLE : INVISIBLE);
    }

    private void switchIndicators(int viewId){
        pickUpIndicator.setImageResource(viewId == R.id.pickup_addr_text_view ? idicatorsImg[1] : idicatorsImg[0]);
        dropOffindicator.setImageResource(viewId == R.id.dropoff_add_text_view ? idicatorsImg[1] : idicatorsImg[0]);
    }

    public void resetDropOff(){
        dropoffAddr.setText("");
    }

    public interface PickUpDropoffListeners{
        void onPickUpCancel();
        void onDropOffpCancel();
        void onPickUpClick();
        void onDropOffClick();
    }


}
