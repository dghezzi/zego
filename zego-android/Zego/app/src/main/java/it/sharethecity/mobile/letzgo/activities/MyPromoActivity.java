package it.sharethecity.mobile.letzgo.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import it.sharethecity.mobile.letzgo.R;
import it.sharethecity.mobile.letzgo.application.ApplicationController;
import it.sharethecity.mobile.letzgo.customviews.MyFontTextView;
import it.sharethecity.mobile.letzgo.customviews.PopUpDialog;
import it.sharethecity.mobile.letzgo.dao.Promo;
import it.sharethecity.mobile.letzgo.dao.StripeCard;
import it.sharethecity.mobile.letzgo.dao.User;
import it.sharethecity.mobile.letzgo.dao.Userpromo;
import it.sharethecity.mobile.letzgo.network.NetworkManager;
import it.sharethecity.mobile.letzgo.network.interfaces.UserRESTInterface;
import it.sharethecity.mobile.letzgo.network.request.ErrorObject;
import it.sharethecity.mobile.letzgo.network.utils.NetworkErrorHandler;
import it.sharethecity.mobile.letzgo.utilities.UtilityFunctions;
import it.sharethecity.mobile.letzgo.utilities.ZegoConstants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyPromoActivity extends ZegoBaseActivity {

    private static final int ADD_PROMO_REQUEST = 33;
    public static final String MY_PROMOS = "my_promo";
    @Nullable
    @BindView(R.id.header)
    RelativeLayout headerLayout;

    @Nullable
    @BindView(R.id.promos_recycler_view)
    RecyclerView myPromosRecyclerView;

    @Nullable
    @BindView(R.id.add_new_promo_button)
    Button addNewPromoButton;

    @Nullable
    @BindView(R.id.no_promo_text_view)
    MyFontTextView noPromoTextView;

    private Paint p = new Paint();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_promo);
        ButterKnife.bind(this);
        setUI();
        myPromosRecyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext(),LinearLayoutManager.VERTICAL,false));
        myPromosRecyclerView.setAdapter(new MyPromosAdapter());
      //  initSwipe();
        user = ApplicationController.getInstance().getUserLogged();
        getAllMyPromo(user);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == ADD_PROMO_REQUEST && resultCode == RESULT_OK){
            if(data != null){
                List<Userpromo> promos = (List<Userpromo>) data.getSerializableExtra(MY_PROMOS);
                noPromoTextView.setVisibility(promos == null || promos.isEmpty() ?  View.VISIBLE : View.GONE);
                myPromosRecyclerView.removeAllViewsInLayout();
                ((MyPromosAdapter)myPromosRecyclerView.getAdapter()).setPromos(promos == null ? new ArrayList<Userpromo>() : promos);
            }

        }else{
            super.onActivityResult(requestCode, resultCode, data);
        }

    }


    @Optional
    @OnClick(R.id.add_new_promo_button)
    public void onAddPromoClick(){
        Intent i = new Intent(MyPromoActivity.this,PromoCodeActivity.class);
        i.putExtra(FROM_ACTIVITY,MyPromoActivity.class.getSimpleName());
        startActivityForResult(i,ADD_PROMO_REQUEST);
    }


    private void getAllMyPromo(User u) {
        Call<List<Userpromo>> call = NetworkManager.getInstance().getRetrofit().create(UserRESTInterface.class).getAllMyPromo(u.getZegotoken(),u.getId());
        showOrDismissProgressWheel(SHOW);
        call.enqueue(new Callback<List<Userpromo>>() {
            @Override
            public void onResponse(Call<List<Userpromo>> call, Response<List<Userpromo>> response) {
                showOrDismissProgressWheel(DISMISS);
                if(response.isSuccessful()){
                    noPromoTextView.setVisibility(response.body().isEmpty() ?  View.VISIBLE : View.GONE);
                    ((MyPromosAdapter)myPromosRecyclerView.getAdapter()).setPromos(response.body());
                }else{
                    if(response.code() == ZegoConstants.ApiRestConstants.ERROR_500){
                        ErrorObject errorObject = NetworkErrorHandler.getInstance().decodeError(response);
                        Toast.makeText(getBaseContext(),errorObject != null ? errorObject.getMsg() : getString(R.string.unknown_error),Toast.LENGTH_SHORT).show();
                    }else{
                        NetworkErrorHandler.getInstance().errorHandler(response,MyPromoActivity.this);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Userpromo>> call, Throwable t) {
                showOrDismissProgressWheel(DISMISS);
                showInfoDialog(getString(R.string.warning),getString(R.string.impossible_to_connetc_to_server));
            }
        });
    }


    private String getFormattedPromoDate(long timeinSecond){
        Date date = new Date();
        date.setTime(timeinSecond * 1000);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yy");
        return simpleDateFormat.format(date);
    }

    @Optional
    @OnClick(R.id.back_button)
    public void onBackClick(){
        super.onBackPressed();
    }

    private void setUI() {
        headerLayout.getLayoutParams().height = (int) ((HEADER_PERCENT_HEIGHT)* ApplicationController.getInstance().getScreenDimension().heightPixels);
        titleTextView.setText(getString(R.string.promo));
        aheadTextView.setVisibility(View.INVISIBLE);
        aheadTextView.setEnabled(false);
    }

//    private void initSwipe(){
//        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT ) {
//
//            @Override
//            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
//                return false;
//            }
//
//            @Override
//            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
//                final int position = viewHolder.getAdapterPosition();
//
//                if (direction == ItemTouchHelper.LEFT){
//                    PopUpDialog.showConfirmPopUpDialog(MyPromoActivity.this, getString(R.string.warning),getString(R.string.remove_card_msg),
//                            getString(R.string.yes),getString(android.R.string.no), 0, null, new PopUpDialog.DialogActionListener() {
//                                @Override
//                                public void actionListener() {
//                                    StripeCard card = ((MyCardsActivity.MyCardsAdapter) myPromosRecyclerView.getAdapter()).getItem(position);
//                                    if(card != null){
//                                        removePromo(card);
//                                    }else{
//                                        myPromosRecyclerView.getAdapter().notifyItemChanged(position);
//                                    }
//
//
//                                }
//
//                                @Override
//                                public void negativeAction() {
//                                    myPromosRecyclerView.getAdapter().notifyItemChanged(position);
//                                }
//                            });
//
//                }
//            }
//
//            @Override
//            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
//
//                Bitmap icon;
//                if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){
//
//                    View itemView = viewHolder.itemView;
//                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
//                    float width = height / 3;
//
//                    if(dX > 0){
//
//                    } else if (dX < 0) {
//                        p.setColor(Color.parseColor("#00FFFFFF"));
//                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(),(float) itemView.getRight(), (float) itemView.getBottom());
//                        c.drawRect(background,p);
//                    }
//                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
//                }
//
//
//
//            }
//        };
//        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
//        itemTouchHelper.attachToRecyclerView(myPromosRecyclerView);
//    }


    private void removePromo(Object promo){

    }


    public class MyPromosAdapter extends RecyclerView.Adapter<MyPromosAdapter.PromoViewHolder> {

        List<Userpromo> promosList;



        public MyPromosAdapter() {
            promosList = new ArrayList<>();
        }
        @Override
        public PromoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(getBaseContext()).inflate(R.layout.promo_layout_row,parent,false);
            return new PromoViewHolder(v);
        }

        @Override
        public void onBindViewHolder(PromoViewHolder holder, int position) {
            if(position < promosList.size()){
                Userpromo userpromo = promosList.get(position);
                if(userpromo != null && userpromo.getPromo() != null){
                    holder.promoName.setText(userpromo.getPromo().getPromotitle());
                    holder.codeValue.setText(userpromo.getPromo().getCode());
                    holder.expiryDate.setText(getFormattedPromoDate(Long.valueOf(userpromo.getExpirydate())));
                    holder.promoTypeLabel.setText(getTypeString(userpromo.getPromo().getType()));
                    holder.promoTypeValue.setText(getValueType(userpromo));
                }
            }

        }

        @Override
        public int getItemCount() {
            return promosList.size();
        }

        public void setPromos(List<Userpromo> cards){
            promosList.clear();
            promosList.addAll(cards);
            notifyDataSetChanged();
        }

        public Object getItem(int position) {
            if(!promosList.isEmpty() && position < promosList.size()){
                return promosList.get(position);
            }
            return null;
        }

        public class PromoViewHolder extends RecyclerView.ViewHolder{

            @Nullable
            @BindView(R.id.card_view_layout)
            CardView cardViewLayout;

//            @Nullable
//            @BindView(R.id.main_linear_layout)
//            LinearLayout linearLayout;

            @Nullable
            @BindView(R.id.promo_name_text_view)
            MyFontTextView promoName;

            @Nullable
            @BindView(R.id.promo_type_label)
            MyFontTextView promoTypeLabel;

            @Nullable
            @BindView(R.id.promo_type_text_view)
            MyFontTextView promoTypeValue;

            @Nullable
            @BindView(R.id.expiry_date_text_view)
            MyFontTextView expiryDate;

            @Nullable
            @BindView(R.id.code_value_text_view)
            MyFontTextView codeValue;


            public PromoViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this,itemView);
            }


            @Optional
            @OnClick(R.id.card_view_layout)
            public void onPromoClick(){
                int position = getAdapterPosition();
                if(position < promosList.size()){
                    showInfoDialog(getString(R.string.details),promosList.get(position).getPromo().getPromodesc());
                }
            }
        }
    }

    private String getValueType(Userpromo userpromo) {
        if(userpromo == null) return "";

        if(userpromo.getPromo().getType().equals(Promo.PERCENT) || userpromo.getPromo().getType().equals(Promo.MGM)){
            return  "-" + userpromo.getPromo().getValue() + "%";
        }else if(userpromo.getPromo().getType().equals(Promo.FREERIDE)){
            return userpromo.getPromo().getValue().toString();
        }else if(userpromo.getPromo().getType().equals(Promo.WALLET)){
            return String.format("%.2f €",userpromo.getValueleft()/100f);
        }else if(userpromo.getPromo().getType().equals(Promo.EURO)){
            return String.format("%.2f €",userpromo.getPromo().getValue()/100f) ;
        }
        else{
            return "";
        }
    }

    public String getTypeString(String type) {
        String typeS = "";
        switch (type){
            case Promo.PERCENT:
                typeS = "";//getString(R.string.promo_percent_label);
                break;
            case Promo.EURO:
                typeS = "";//getString(R.string.promo_euro_label);
                break;
            case Promo.FREERIDE:
                typeS = getString(R.string.promo_freeride_label);
                break;
            case Promo.WALLET:
                typeS = getString(R.string.promo_wallet_label);
                break;
            case Promo.MGM:
                typeS = getString(R.string.promo_mgm_label);
                break;
            default:
                typeS = getString(R.string.unknown_promo_label);
                break;
        }
        return typeS;
    }
}
