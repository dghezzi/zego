package it.sharethecity.mobile.letzgo.customviews;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import it.sharethecity.mobile.letzgo.R;
import it.sharethecity.mobile.letzgo.utilities.CircleTransform;


/**
 * Created by lucabellaroba on 28/07/16.
 */
public class PopUpThreeActionDialog extends DialogFragment {


    private static final String THREE_ACTION_POP_UP_DIALOG = "three_action_popup_dialog";


    @Nullable
    @BindView(R.id.button1)
    Button firstButton;

    @Nullable
    @BindView(R.id.button2)
    Button secondButton;

    @Nullable
    @BindView(R.id.button3)
    Button thirdButton;

    @Nullable
    @BindView(R.id.body_pop_up)
    TextView body;

    @Nullable
    @BindView(R.id.title_pop_up)
    TextView title;

    @Nullable
    @BindView(R.id.image_pop_up)
    ImageView image;

    private static String titleString;
    private static String bodyString;
    private static String firstTextButton;
    private static String secondTextButton;
    private static String thirdTextButton;
    private static Integer imageToLoad;
    private static Integer thirdSelector;

    private static DialogThreeActionListener actionListener;


    private static Activity activity;

    public PopUpThreeActionDialog(){
        activity = null;
        titleString = "";
        bodyString = "";
        firstTextButton = "";
        secondTextButton = "";
        thirdTextButton = "";
        actionListener = null;
        imageToLoad = null;
        thirdSelector = null;
    }

    public static class ThreeActionPopUpBuilder{
        private  Activity activity;
        private  String titleString;
        private  String bodyString;
        private  String firstTextButton;
        private  String secondTextButton;
        private  String thirdTextButton;
        private  Integer imageToLoad;
        private  Integer thirdButtonSelector;

        private DialogThreeActionListener listener;

        public ThreeActionPopUpBuilder(Activity act){
            activity = act;
        }

        public ThreeActionPopUpBuilder setTitle(String title){
            titleString = title;
            return this;
        }

        public ThreeActionPopUpBuilder setMsg(String msg){
            bodyString = msg;
            return this;
        }

        public ThreeActionPopUpBuilder setFirstTextButton(String firstTB){
            firstTextButton = firstTB;
            return this;
        }

        public ThreeActionPopUpBuilder setSecondTextButton(String secondTB){
            secondTextButton = secondTB;
            return this;
        }

        public ThreeActionPopUpBuilder setThirdTextButton(String thirdTb){
            thirdTextButton = thirdTb;
            return this;
        }

        public ThreeActionPopUpBuilder setImageToLoad(Integer imageRef){
            imageToLoad = imageRef;
            return this;
        }

        public void setListener(DialogThreeActionListener l){
            listener = l;
            return ;
        }

        public ThreeActionPopUpBuilder setThirdButtonBackGroundColor(Integer selector){
            thirdButtonSelector = selector;
            return this;
        }

        public void createDialog(){
            showThreectionPopUpDialog(activity,titleString,bodyString,firstTextButton,secondTextButton,thirdTextButton,imageToLoad,listener,thirdButtonSelector);
        }

    }


    private static void showThreectionPopUpDialog(Activity ctx,
                                                  String title,
                                                  String body,
                                                  String textB1,
                                                  String textB2,
                                                  String textB3,
                                                  Integer imageRef,
                                                  DialogThreeActionListener listener,
                                                  Integer thirdButtonSelector){

        if (ctx.isDestroyed() || ctx.isFinishing()) return;

        FragmentTransaction ft = ctx.getFragmentManager().beginTransaction();
        Fragment prev = ctx.getFragmentManager().findFragmentByTag(THREE_ACTION_POP_UP_DIALOG);
        if (prev != null) {
            ft.remove(prev).commitAllowingStateLoss();
        }
        // Create and show the dialog.
        PopUpThreeActionDialog popUpDialog = new PopUpThreeActionDialog();
        activity = ctx;
        titleString = title;
        bodyString = body;
        firstTextButton = textB1;
        secondTextButton = textB2;
        thirdTextButton = textB3;
        actionListener = listener;
        imageToLoad = imageRef;
        thirdSelector = thirdButtonSelector;

        popUpDialog.show(ctx.getFragmentManager(), THREE_ACTION_POP_UP_DIALOG);
    }




    @Override
    public void show(FragmentManager manager, String tag) {
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(this, tag);
        ft.commitAllowingStateLoss();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public void onSaveInstanceState(Bundle arg0) {
        //super.onSaveInstanceState(arg0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.custom_three_action_pop_up_layout,container,false);
        ButterKnife.bind(this,v);

        title.setText(titleString);
        body.setText(bodyString);
        title.setVisibility(titleString == null || titleString.isEmpty() ? View.GONE : View.VISIBLE);
        firstButton.setText(firstTextButton);
        thirdButton.setText(thirdTextButton);
        secondButton.setText(secondTextButton);

        if(thirdSelector != null){
            thirdButton.setBackground(ContextCompat.getDrawable(activity.getBaseContext(),thirdSelector));
        }

        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(getResources().getColor(R.color.gray_circle));
        drawable.setShape(GradientDrawable.OVAL);
        image.setBackground(drawable);

        Picasso.with(getActivity().getBaseContext())
                .load(imageToLoad == null ? R.drawable.car_pop_up : imageToLoad)
                .transform(new CircleTransform())
                .into(image);

        return v;
    }


    @Optional
    @OnClick(R.id.button1)
    public void onButton1Click(){
        actionListener.firstActionListener();
        dismissAllowingStateLoss();
    }

    @Optional
    @OnClick(R.id.button2)
    public void onButton2Click(){
        actionListener.secondActionListener();
        dismissAllowingStateLoss();
    }

    @Optional
    @OnClick(R.id.button3)
    public void onButton3Click(){
        actionListener.thirdActionListener();
        dismissAllowingStateLoss();

    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog dialog = super.onCreateDialog(savedInstanceState);
        if (dialog == null) {
            setShowsDialog(false);
        }
        // request a window without the title
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        return dialog;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    public interface DialogThreeActionListener{
        void firstActionListener();
        void secondActionListener();
        void thirdActionListener();
    }

}
