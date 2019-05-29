package it.sharethecity.mobile.letzgo.customviews;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.pnikosis.materialishprogress.ProgressWheel;
import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import it.sharethecity.mobile.letzgo.R;
import it.sharethecity.mobile.letzgo.utilities.CircleTransform;


/**
 * Created by lucabellaroba on 28/07/16.
 */
public class ProgressHud extends DialogFragment {

    private static final String PROGRESS_WHEEL_DIALOG = "ProgressHud_dialog";


    private ProgressWheel progressWheel;
    private static Handler handler;

    public ProgressHud(){

    }


    public static void showProgressWheel(final Activity ctx,Integer timeOutInSec){
        if (ctx.isDestroyed() || ctx.isFinishing()) return;

        FragmentTransaction ft = ctx.getFragmentManager().beginTransaction();
        Fragment prev = ctx.getFragmentManager().findFragmentByTag(PROGRESS_WHEEL_DIALOG);
        if (prev != null) {
            ft.remove(prev).commitAllowingStateLoss();
        }
        // Create and show the dialog.
        ProgressHud popUpDialog = new ProgressHud();
        popUpDialog.show(ctx.getFragmentManager(), PROGRESS_WHEEL_DIALOG);
        if(timeOutInSec != null){
             new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    FragmentTransaction ft = ctx.getFragmentManager().beginTransaction();
                    Fragment prev = ctx.getFragmentManager().findFragmentByTag(PROGRESS_WHEEL_DIALOG);
                    if (prev != null) {
                        ft.remove(prev).commitAllowingStateLoss();
                    }
                }
            },timeOutInSec*1000);
        }

    }

    public static void dismissProgressWheel(Activity ctx){

        if (ctx.isDestroyed() || ctx.isFinishing()) return;

        FragmentTransaction ft = ctx.getFragmentManager().beginTransaction();
        Fragment prev = ctx.getFragmentManager().findFragmentByTag(PROGRESS_WHEEL_DIALOG);
        if(handler != null){
            handler.removeCallbacksAndMessages(null);
        }
        if (prev != null) {
            ft.remove(prev).commitAllowingStateLoss();
        }
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

        View v = inflater.inflate(R.layout.progress_wheel_layout ,container,false);
       // ButterKnife.bind(this,v);

        return v;
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
        if(handler != null){
            handler.removeCallbacksAndMessages(null);
        }
    }

    public interface DialogActionListener {
        void actionListener();
        void negativeAction();
    }



}
