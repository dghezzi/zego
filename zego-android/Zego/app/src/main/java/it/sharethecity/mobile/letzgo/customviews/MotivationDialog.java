package it.sharethecity.mobile.letzgo.customviews;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import butterknife.Optional;
import it.sharethecity.mobile.letzgo.R;
import it.sharethecity.mobile.letzgo.application.ApplicationController;
import it.sharethecity.mobile.letzgo.utilities.CircleTransform;
import it.sharethecity.mobile.letzgo.utilities.ZegoConstants;

/**
 * Created by lucabellaroba on 14/12/16.
 */

public class MotivationDialog extends DialogFragment {

    public static final String MOTIVATION_DIALOG = "morivation_dialog";



    @Nullable
    @BindView(R.id.motivation_title)
    MyFontTextView motivationTitleTextView;

    @Nullable
    @BindView(R.id.motivation_question)
    MyFontTextView motivationQuestionTextView;

    @Nullable
    @BindView(R.id.motivation_answers)
    ListView motivationAnswersList;


    @Nullable
    @BindView(R.id.cancel_request_button)
    MyFontTextView cancelButton;


    @Nullable
    @BindView(R.id.main_layout)
    LinearLayout mainLayout;



    private static String titleString;
    private static String closeString;
    private static String question;
    private static List<String> answers;

    private static int titleColor;
    private static int closeColor;
    public static int DEFAULT = 0;
    private static Activity act;
    private static MotivationListener listener;

    public static void showMotivationDialog(Activity ctx, String title, String msg,String closeText, int tColor,int cColor,List<String> options, MotivationListener mListener){
        if(ctx.isDestroyed() || ctx.isFinishing()) return;

        FragmentTransaction ft = ctx.getFragmentManager().beginTransaction();
        Fragment prev = ctx.getFragmentManager().findFragmentByTag(MOTIVATION_DIALOG);
        if (prev != null) {
            ft.remove(prev).commitAllowingStateLoss();
        }

        // Create and show the dialog.
        MotivationDialog motivationDialog = new MotivationDialog();

        act = ctx;
        titleString = title;
        closeString = closeText;
        titleColor = tColor;
        closeColor = cColor;
        answers = options;
        question = msg;
        listener = mListener;
        motivationDialog.show(ctx.getFragmentManager(), MOTIVATION_DIALOG);
    }


    public class MotivationAdapter extends ArrayAdapter<String>{

        public MotivationAdapter(Context context, int resource, List<String> objects) {
            super(context, resource, objects);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;

            if(convertView == null){
                convertView = LayoutInflater.from(act.getBaseContext()).inflate(R.layout.motivation_answer_row_layout,parent,false);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            }else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.answerTextView.setText(getItem(position));

            return convertView;
        }


        public class ViewHolder{

            @Nullable
            @BindView(R.id.answer_text_view)
            MyFontTextView answerTextView;

            ViewHolder(View v){
                ButterKnife.bind(this,v);
            }
        }
    }

    @Optional
    @OnClick(R.id.cancel_request_button)
    public void onCanceClick(){
        dismissAllowingStateLoss();
    }


    @Override
    public void show(FragmentManager manager, String tag) {
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(this, tag);
        ft.commitAllowingStateLoss();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.motivation_layout,container,false);
        ButterKnife.bind(this,v);

        if(titleString == null || titleString.isEmpty()){
            motivationTitleTextView.setText("ttyyyyyyy yyyyuuuu uuuurwre");
            motivationTitleTextView.setTextColor(Color.WHITE);
        }else{
            motivationTitleTextView.setText(titleString);
            motivationTitleTextView.setTextColor(titleColor == 0 ? ContextCompat.getColor(act.getBaseContext(),R.color.black_text) : titleColor);

        }

        cancelButton.setText(closeString);
        cancelButton.setTextColor(closeColor == 0 ? ContextCompat.getColor(act.getBaseContext(),R.color.darker_green_text) : closeColor);
        motivationQuestionTextView.setText(question);
        motivationAnswersList.setAdapter(new MotivationAdapter(act.getBaseContext(),R.layout.motivation_answer_row_layout,answers));

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
    }

    @Optional
    @OnItemClick(R.id.motivation_answers)
    public void onAnswerSelected(int position){

        if(listener != null){
            dismissAllowingStateLoss();
            listener.onAnswerSelected(new Pair<Integer, String>(position,answers.get(position)));
        }

    }

    public interface MotivationListener{
        void onAnswerSelected(Pair<Integer,String> s);
    }
}
