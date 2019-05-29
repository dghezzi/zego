package it.sharethecity.mobile.letzgo.customviews;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import it.sharethecity.mobile.letzgo.R;
import it.sharethecity.mobile.letzgo.utilities.CircleTransform;


/**
 * Created by lucabellaroba on 28/07/16.
 */
public class PopUpDialog extends DialogFragment {

    private static final String POP_UP_DIALOG = "popup_dialog";
    private static final String CONFIRM_POP_UP_DIALOG = "confirm_popup_dialog";
    private static int imageResourceId;


    private Button button;
    private TextView body;
    private TextView title;
    private ImageView image;
    private static String titleString;
    private static String bodyString;
    private static String textButton;
    private static String imageUrl;
    private static int color;
    private static String cancelButtonString;
    private static DialogActionListener actionListener;
    private Button cancelButton;


    public PopUpDialog(){

    }


    public static void removeDialog(Activity ctx){
        if (ctx.isDestroyed() || ctx.isFinishing()) return;

        FragmentTransaction ft = ctx.getFragmentManager().beginTransaction();
        Fragment prev = ctx.getFragmentManager().findFragmentByTag(POP_UP_DIALOG);
        if (prev != null) {
            ft.remove(prev).commitAllowingStateLoss();
        }
    }
    public static void showPopUpDialog(Activity ctx, String title, String body, String textB, int backgroundColorImage, String url, DialogActionListener listener){

        if (ctx.isDestroyed() || ctx.isFinishing()) return;

        FragmentTransaction ft = ctx.getFragmentManager().beginTransaction();
        Fragment prev = ctx.getFragmentManager().findFragmentByTag(POP_UP_DIALOG);
        if (prev != null) {
            ft.remove(prev).commitAllowingStateLoss();
        }
        // Create and show the dialog.
        PopUpDialog popUpDialog = new PopUpDialog();
        titleString = title;
        bodyString = body;
        textButton = textB;
        cancelButtonString = null;
        actionListener = listener;
        color = backgroundColorImage;
        imageUrl = url;
        imageResourceId =  R.drawable.car_pop_up ;
        popUpDialog.show(ctx.getFragmentManager(), POP_UP_DIALOG);
    }

    public static void showPopUpDialog(Activity ctx, String title, String body, String textB, int backgroundColorImage, int resourceId, DialogActionListener listener){

        if (ctx.isDestroyed() || ctx.isFinishing()) return;

        FragmentTransaction ft = ctx.getFragmentManager().beginTransaction();
        Fragment prev = ctx.getFragmentManager().findFragmentByTag(POP_UP_DIALOG);
        if (prev != null) {
            ft.remove(prev).commitAllowingStateLoss();
        }
        // Create and show the dialog.
        PopUpDialog popUpDialog = new PopUpDialog();
        titleString = title;
        bodyString = body;
        textButton = textB;
        cancelButtonString = null;
        actionListener = listener;
        color = backgroundColorImage;
        imageUrl = null;
        imageResourceId = (resourceId != 0) ? resourceId : R.drawable.car_pop_up ;
        popUpDialog.show(ctx.getFragmentManager(), POP_UP_DIALOG);
    }

    public static void showConfirmPopUpDialog(Activity ctx, String title, String body, String confirmButton, String cancelButton, int backgroundColorImage, String url, DialogActionListener listener){

        if (ctx.isDestroyed() || ctx.isFinishing()) return;

        FragmentTransaction ft = ctx.getFragmentManager().beginTransaction();
        Fragment prev = ctx.getFragmentManager().findFragmentByTag(POP_UP_DIALOG);
        if (prev != null) {
            ft.remove(prev).commitAllowingStateLoss();
        }
        // Create and show the dialog.
        PopUpDialog popUpDialog = new PopUpDialog();
        titleString = title;
        bodyString = body;
        textButton = confirmButton;
        cancelButtonString = cancelButton;
        actionListener = listener;
        color = backgroundColorImage;
        imageUrl = url;
        imageResourceId =  R.drawable.car_pop_up ;
        popUpDialog.show(ctx.getFragmentManager(), POP_UP_DIALOG);
    }

    public static void showConfirmPopUpDialog(Activity ctx, String title, String body, String confirmButton, String cancelButton, int backgroundColorImage, int resourceId, DialogActionListener listener){

        if (ctx.isDestroyed() || ctx.isFinishing()) return;

        FragmentTransaction ft = ctx.getFragmentManager().beginTransaction();
        Fragment prev = ctx.getFragmentManager().findFragmentByTag(POP_UP_DIALOG);
        if (prev != null) {
            ft.remove(prev).commitAllowingStateLoss();
        }
        // Create and show the dialog.
        PopUpDialog popUpDialog = new PopUpDialog();
        titleString = title;
        bodyString = body;
        textButton = confirmButton;
        cancelButtonString = cancelButton;
        actionListener = listener;
        color = backgroundColorImage;
        imageUrl = null;
        imageResourceId = (resourceId != 0) ? resourceId : R.drawable.car_pop_up ;
        popUpDialog.show(ctx.getFragmentManager(), POP_UP_DIALOG);
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

        View v = inflater.inflate(cancelButtonString == null ? R.layout.custom_pop_up_layout : R.layout.custom_confirm_pop_up_layout,container,false);
        bind(v);

        title.setText(titleString);
        body.setText(bodyString);
        button.setText(textButton);

        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(getResources().getColor(color != 0 ? color : R.color.gray_circle));
        drawable.setShape(GradientDrawable.OVAL);
        image.setBackground(drawable);

        if(imageUrl != null){
            Picasso.with(getActivity().getBaseContext())
                    .load(imageUrl)
                    .transform(new CircleTransform())
                    .into(image);
        }else{
            Picasso.with(getActivity().getBaseContext())
                    .load(imageResourceId)
                    .transform(new CircleTransform())
                    .into(image);
        }


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (actionListener!=null){
                    actionListener.actionListener();
                }
                dismissAllowingStateLoss();
            }
        });

        if(cancelButtonString != null){
            cancelButton.setText(cancelButtonString);
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (actionListener!=null){
                        actionListener.negativeAction();
                    }
                    dismissAllowingStateLoss();
                }
            });
        }

        return v;
    }


    private void bind(View v){
        button = (Button) v.findViewById(R.id.pop_up_button);
        body = (TextView)v.findViewById(R.id.body_pop_up);
        title = (TextView)v.findViewById(R.id.title_pop_up);
        image = (ImageView)v.findViewById(R.id.image_pop_up);
        if(cancelButtonString != null){
            cancelButton = (Button) v.findViewById(R.id.pop_up_cancel_button);
        }
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

    public interface DialogActionListener {
        void actionListener();
        void negativeAction();
    }



}
