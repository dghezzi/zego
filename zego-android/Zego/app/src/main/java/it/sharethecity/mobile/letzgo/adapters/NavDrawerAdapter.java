package it.sharethecity.mobile.letzgo.adapters;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import it.sharethecity.mobile.letzgo.R;
import it.sharethecity.mobile.letzgo.activities.ZegoNavBaseActivity;
import it.sharethecity.mobile.letzgo.application.ApplicationController;
import it.sharethecity.mobile.letzgo.customviews.RegularRelewayTextView;
import it.sharethecity.mobile.letzgo.dao.User;

/**
 * Created by lucabellaroba on 15/11/16.
 */

public class NavDrawerAdapter extends RecyclerView.Adapter<NavDrawerAdapter.ViewHolder> {


    public static final float ROW_HEIGTH_PERCENT_DIMEN = 0.0833f;
    public static final int PROFILE = 0;
    public static final int PAYMENT = 1;
    public static final int MY_LIFTS = 2;
    public static final int PROMO = 3;
    public static final int INFO = 4;
    public static final int GIVE_LIFTS = 5;

    private Context ctx;
    private List<Integer> icons;
    private String[] labels;
    private NavBarListener listener;
    private User u;

    public NavDrawerAdapter(Context ctx,NavBarListener navBarListener) {
        this.ctx = ctx;
        listener = navBarListener;
        u = ApplicationController.getInstance().getUserLogged();
        labels = ctx.getResources().getStringArray(R.array.nav_drawer_labels);
        TypedArray typedArray;
        typedArray = ctx.getResources().obtainTypedArray(R.array.nav_drawer_icons);

        icons = new ArrayList<>();
        for(int i = 0; i < typedArray.length(); i++ ){
            icons.add(typedArray.getResourceId(i,0));
        }
        typedArray.recycle();


    }

    @Override
    public NavDrawerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(ctx).inflate(R.layout.row_nav_bar,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(NavDrawerAdapter.ViewHolder holder, int position) {
        boolean isRowWithSwitch = (position == labels.length - 1);
        holder.giveLift.setVisibility(isRowWithSwitch  && u.getCandrive().equals(1) ? View.VISIBLE : View.GONE);
        holder.giveLift.setChecked(u.getCandrive().equals(1) && u.isDriver());
        holder.leftIconImageView.setImageResource(icons.get(position));
        holder.labelTextView.setText(labels[position]);
    }


    @Override
    public int getItemCount() {
        int count = u.getCandrive() == 0 ? labels.length - 1 : labels.length;
        return count;
    }

    public void setListener(NavBarListener listener) {
        this.listener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        @Nullable
        @BindView(R.id.left_icon_image_view)
        ImageView leftIconImageView;

        @Nullable
        @BindView(R.id.give_lift_checkbox)
        CheckBox giveLift;

        @Nullable
        @BindView(R.id.nav_label_text_view)
        RegularRelewayTextView labelTextView;



        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

            itemView.getLayoutParams().height = (int) (ROW_HEIGTH_PERCENT_DIMEN* ApplicationController.getInstance().getScreenDimension().heightPixels);
            leftIconImageView.getLayoutParams().height = (int) (ZegoNavBaseActivity.LEFT_ICON_PERCENT_DIMEN * ApplicationController.getInstance().getScreenDimension().widthPixels);
            leftIconImageView.getLayoutParams().width = (int) (ZegoNavBaseActivity.LEFT_ICON_PERCENT_DIMEN * ApplicationController.getInstance().getScreenDimension().widthPixels);
            giveLift.getLayoutParams().width = (int) (0.1259f * ApplicationController.getInstance().getScreenDimension().widthPixels);
            giveLift.getLayoutParams().height = (int) ( 0.55f* giveLift.getLayoutParams().width);

        }

        @Optional
        @OnClick(R.id.nav_row)
        public void onRowClick(){
            if(listener != null){
                listener.positionSelected(getAdapterPosition());
            }
        }

        @Optional
        @OnClick(R.id.give_lift_checkbox)
        public void onchangeModeClick(){
            if(listener != null){
                listener.onchangeMode(giveLift);
            }
        }
    }

    public interface NavBarListener{
        void positionSelected(int position);
        void onchangeMode(CheckBox checkBox);
    }
}
