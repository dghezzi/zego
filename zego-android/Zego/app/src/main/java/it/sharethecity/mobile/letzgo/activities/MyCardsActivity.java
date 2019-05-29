package it.sharethecity.mobile.letzgo.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
import it.sharethecity.mobile.letzgo.dao.StripeCard;
import it.sharethecity.mobile.letzgo.dao.User;
import it.sharethecity.mobile.letzgo.fragments.RideHistoryFragment;
import it.sharethecity.mobile.letzgo.network.NetworkManager;
import it.sharethecity.mobile.letzgo.network.interfaces.UserRESTInterface;
import it.sharethecity.mobile.letzgo.network.request.CompatRequest;
import it.sharethecity.mobile.letzgo.network.request.ErrorObject;
import it.sharethecity.mobile.letzgo.network.utils.NetworkErrorHandler;
import it.sharethecity.mobile.letzgo.utilities.FontManager;
import it.sharethecity.mobile.letzgo.utilities.UtilityFunctions;
import it.sharethecity.mobile.letzgo.utilities.ZegoConstants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyCardsActivity extends ZegoBaseActivity {

    private static final int ADD_CARD_REQUEST = 546;
    private static final String NEW_CARD = "new_card";
    @Nullable
    @BindView(R.id.header)
    RelativeLayout headerLayout;

    @Nullable
    @BindView(R.id.cards_recycler_view)
    RecyclerView myCardsRecyclerView;

    @Nullable
    @BindView(R.id.add_new_card_button)
    Button addNewCardButton;

    @Nullable
    @BindView(R.id.no_cards_text_view)
    MyFontTextView noCardsTextView;

    @Nullable
    @BindView(R.id.debt_layout)
    LinearLayout debtLayout;

    @Nullable
    @BindView(R.id.try_now_text_view)
    Button tryNowTextView;

    @Nullable
    @BindView(R.id.debt_text_view)
    MyFontTextView paymentFailedTextView;

    private Paint p = new Paint();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cards);
        ButterKnife.bind(this);
        setUI();
        myCardsRecyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext(),LinearLayoutManager.VERTICAL,false));
        myCardsRecyclerView.setAdapter(new MyCardsAdapter());
        initSwipe();
        user = ApplicationController.getInstance().getUserLogged();

        getUserById(user);
        getMyCards();
    }

    private void setUI() {
        headerLayout.getLayoutParams().height = (int) ((HEADER_PERCENT_HEIGHT)* ApplicationController.getInstance().getScreenDimension().heightPixels);
        titleTextView.setText(getString(R.string.payment));
        aheadTextView.setVisibility(View.INVISIBLE);
        aheadTextView.setEnabled(false);
    }

    private void showPaymentFailed(Integer debt){
        debtLayout.setVisibility(debt > 0 ? View.VISIBLE : View.GONE);
        String priceString = String.format(" %.2f",debt/100f);
        String s = getString(R.string.payment_failed).replace("{x}",priceString);
        Spannable spannableString = new SpannableString(s);
        spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getBaseContext(),R.color.red_error)),0,s.indexOf("€") + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new RelativeSizeSpan(1.2f), s.indexOf(priceString), s.indexOf("€") + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getBaseContext(),R.color.gray_text_app)),s.indexOf("€") + 1,s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        paymentFailedTextView.setText(spannableString);
    }

    @Optional
    @OnClick(R.id.back_button)
    public void onBackClick(){
        super.onBackPressed();
    }

    @Optional
    @OnClick(R.id.try_now_text_view)
    public void onTryNowClick(){
        payDept(user);
    }

    @Optional
    @OnClick(R.id.add_new_card_button)
    public void onAddCardClick(){
        Intent i = new Intent(MyCardsActivity.this,PaymentMethodActivity.class);
        i.putExtra(FROM_ACTIVITY,MyCardsActivity.class.getSimpleName());
        startActivityForResult(i,ADD_CARD_REQUEST);
    }

    public void showNoCards(boolean show) {
        noCardsTextView.setVisibility(show ? View.VISIBLE : View.GONE);
        myCardsRecyclerView.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK && requestCode == ADD_CARD_REQUEST){
            getMyCards();
//            if(data != null && data.getExtras() != null){
//                StripeCard card = (StripeCard) data.getSerializableExtra(NEW_CARD);
//                ((MyCardsAdapter)myCardsRecyclerView.getAdapter()).addCard(card);
//            }

        }else{
            super.onActivityResult(requestCode, resultCode, data);
        }

    }

    private void getMyCards() {
        Call<List<StripeCard>> call = NetworkManager.getInstance().getRetrofit().create(UserRESTInterface.class).getUserCards(user.getZegotoken(),user.getId());
        showOrDismissProgressWheel(SHOW);
        call.enqueue(new Callback<List<StripeCard>>() {
            @Override
            public void onResponse(Call<List<StripeCard>> call, Response<List<StripeCard>> response) {
                showOrDismissProgressWheel(DISMISS);
                if(response.isSuccessful()){
                   // showNoCards(response.body().isEmpty());
                    ((MyCardsAdapter)myCardsRecyclerView.getAdapter()).setCards(response.body());
                }else if(response.code() == ZegoConstants.ApiRestConstants.ERROR_500){
                    ErrorObject errorObject = NetworkErrorHandler.getInstance().decodeError(response);
                    Toast.makeText(getBaseContext(),errorObject != null ? errorObject.getMsg() : getString(R.string.unknown_error),Toast.LENGTH_SHORT).show();
                }else{
                    NetworkErrorHandler.getInstance().errorHandler(response, MyCardsActivity.this);
                }
            }

            @Override
            public void onFailure(Call<List<StripeCard>> call, Throwable t) {
                showOrDismissProgressWheel(DISMISS);
                showInfoDialog(getString(R.string.warning),getString(R.string.impossible_to_connetc_to_server));
            }
        });
    }

    private void payDept(User u){
        Call<User> call = NetworkManager.getInstance().getRetrofit().create(UserRESTInterface.class).payDebt(u.getZegotoken(),u);
        showOrDismissProgressWheel(SHOW);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                showOrDismissProgressWheel(DISMISS);
                if(response.isSuccessful()){
                    user = response.body();
                    showPaymentFailed(user.getDebt());
                    Toast.makeText(getBaseContext(),getString(R.string.debt_paid),Toast.LENGTH_SHORT).show();
                }else if(response.code() == ZegoConstants.ApiRestConstants.ERROR_500){
                    NetworkErrorHandler.getInstance().errorHandler(response,MyCardsActivity.this);
                    Toast.makeText(getBaseContext(),getString(R.string.debt_payment_failed),Toast.LENGTH_SHORT).show();
                }else{
                    NetworkErrorHandler.getInstance().errorHandler(response,MyCardsActivity.this);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                showOrDismissProgressWheel(DISMISS);
                showInfoDialog(getString(R.string.warning),getString(R.string.impossible_to_connetc_to_server));
            }
        });

    }

    private void postChangeCardPref(StripeCard stripeCard) {
        Call<List<StripeCard>> call = NetworkManager.getInstance().getRetrofit().create(UserRESTInterface.class).postMakeDefault(user.getZegotoken(),user.getId(),stripeCard);
        showOrDismissProgressWheel(SHOW);
        call.enqueue(new Callback<List<StripeCard>>() {
            @Override
            public void onResponse(Call<List<StripeCard>> call, Response<List<StripeCard>> response) {
                showOrDismissProgressWheel(DISMISS);
                if(response.isSuccessful()){
                    ((MyCardsAdapter)myCardsRecyclerView.getAdapter()).setCards(response.body());
                }else if(response.code() == ZegoConstants.ApiRestConstants.ERROR_500){
                    ErrorObject errorObject = NetworkErrorHandler.getInstance().decodeError(response);
                    Toast.makeText(getBaseContext(),errorObject != null ? errorObject.getMsg() : getString(R.string.unknown_error),Toast.LENGTH_SHORT).show();
                }else{
                    NetworkErrorHandler.getInstance().errorHandler(response, MyCardsActivity.this);
                }
            }

            @Override
            public void onFailure(Call<List<StripeCard>> call, Throwable t) {
                showOrDismissProgressWheel(DISMISS);
                showInfoDialog(getString(R.string.warning),getString(R.string.impossible_to_connetc_to_server));
            }
        });
    }

    private void removeCard(StripeCard cardToRemove){
            Call<List<StripeCard>> call = NetworkManager.getInstance().getRetrofit().create(UserRESTInterface.class).postDeleteCard(user.getZegotoken(),user.getId(),cardToRemove);
            showOrDismissProgressWheel(SHOW);
            call.enqueue(new Callback<List<StripeCard>>() {
                @Override
                public void onResponse(Call<List<StripeCard>> call, Response<List<StripeCard>> response) {
                    showOrDismissProgressWheel(DISMISS);
                    if(response.isSuccessful()){
                      //  showNoCards(response.body().isEmpty());

                        ((MyCardsAdapter)myCardsRecyclerView.getAdapter()).setCards(response.body());
                    }else if(response.code() == ZegoConstants.ApiRestConstants.ERROR_500){
                        ErrorObject errorObject = NetworkErrorHandler.getInstance().decodeError(response);
                        Toast.makeText(getBaseContext(),errorObject != null ? errorObject.getMsg() : getString(R.string.unknown_error),Toast.LENGTH_SHORT).show();
                    }else{
                        NetworkErrorHandler.getInstance().errorHandler(response, MyCardsActivity.this);
                    }
                }

                @Override
                public void onFailure(Call<List<StripeCard>> call, Throwable t) {
                    showOrDismissProgressWheel(DISMISS);
                    showInfoDialog(getString(R.string.warning),getString(R.string.impossible_to_connetc_to_server));
                }
            });
    }

    public class MyCardsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        public static final int CARTA = 0;
        List<StripeCard> cardsList;



        public MyCardsAdapter() {
            cardsList = new ArrayList<>();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if(viewType == CARTA){
                View v = LayoutInflater.from(getBaseContext()).inflate(R.layout.my_cards_row_layout,parent,false);
                return new MyCardsAdapterViewHolder(v);
            }else{
                View v = LayoutInflater.from(getBaseContext()).inflate(R.layout.cash_row_layout,parent,false);
                return new CashViewHolder(v);
            }

        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if(getItemViewType(position) == CARTA){
                setCardContent((MyCardsAdapterViewHolder)holder,position);
            }else{
                setPagamentoCashContent((CashViewHolder)holder);
            }

        }

        private void setPagamentoCashContent(CashViewHolder holder) {
            holder.prefCashCheckBox.setChecked(ApplicationController.getInstance().isCashMethod());
            if(holder.prefCashCheckBox.isChecked()){
                holder.cashImg.clearColorFilter();
            }else{
                holder.cashImg.setColorFilter(ContextCompat.getColor(getBaseContext(),R.color.gray_button));
            }
        }

        private void setCardContent(MyCardsAdapterViewHolder holder, int position) {
            StripeCard card = cardsList.get(position - 1);
            if(card != null){
                holder.cardNumberTextView.setText("xx " + card.getLastdigit());
                holder.expDateTextView.setText(String.format("%02d",card.getExpmonth()) + "/" + card.getExpyear());
                holder.prefCardCheckBox.setChecked(!ApplicationController.getInstance().isCashMethod() && card.getPreferred().equals(1));
                holder.typeCardImageView.setImageResource(UtilityFunctions.getCardImageByBrand(card.getBrand()));
                holder.typeCardTextView.setText(card.getBrand());
            }
        }

        public void setCards(List<StripeCard> cards){
            cardsList.clear();
            cardsList.addAll(cards);
            ApplicationController.getInstance().saveMethodCash(cards.isEmpty() ? 1 : 0);
            notifyDataSetChanged();
        }

        public void  addCard(StripeCard newCard){
            cardsList.add(newCard);
            notifyDataSetChanged();
        }


        @Override
        public int getItemViewType(int position) {
            return position == 0 ? 2 : CARTA;
        }

        @Override
        public int getItemCount() {
            return cardsList.size() + 1;
        }

        public void removeItem(int position) {
            if(position < cardsList.size()){
                cardsList.remove(position);
                notifyItemRemoved(position);
            }
        }

        public StripeCard getItem(int position) {
            if(!cardsList.isEmpty() && position <= cardsList.size()){
                return cardsList.get(position - 1);
            }
            return null;
        }


        public class CashViewHolder extends RecyclerView.ViewHolder {


            @Nullable
            @BindView(R.id.cash_preferred_check_box)
            CheckBox prefCashCheckBox;

            @Nullable
            @BindView(R.id.cashImage)
            ImageView cashImg;


            public CashViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this,itemView);
            }


            @Optional
            @OnClick(R.id.cash_preferred_check_box)
            public void onPrefCashClick(){
                ApplicationController.getInstance().saveMethodCash(prefCashCheckBox.isChecked() ? 1 : 0);
                notifyDataSetChanged();
            }
        }


        public class MyCardsAdapterViewHolder extends RecyclerView.ViewHolder{

            @Nullable
            @BindView(R.id.card_layout)
            CardView cardView;

            @Nullable
            @BindView(R.id.type_card_image_view)
            ImageView typeCardImageView;

            @Nullable
            @BindView(R.id.card_type_text_view)
            TextView typeCardTextView;

            @Nullable
            @BindView(R.id.card_number_text_view)
            MyFontTextView cardNumberTextView;

            @Nullable
            @BindView(R.id.exp_date_text_view)
            MyFontTextView expDateTextView;


            @Nullable
            @BindView(R.id.preferred_check_box)
            CheckBox prefCardCheckBox;


            public MyCardsAdapterViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this,itemView);
                typeCardTextView.setTypeface(FontManager.getInstance(getBaseContext()).getFont("Raleway-Regular.ttf"));
            }

            @Optional
            @OnClick(R.id.preferred_check_box)
            public void onPrefCardClick(){
                int pos = getAdapterPosition() - 1;
                StripeCard card = cardsList.get(pos);
                prefCardCheckBox.setChecked(!prefCardCheckBox.isChecked());
                ApplicationController.getInstance().saveMethodCash(0);
                if(card.getPreferred().equals(1)){
                   notifyDataSetChanged();
                }else{
//                    newPreferredPosition = pos;
                    postChangeCardPref(cardsList.get(pos));
                }

            }
        }
    }

    private void initSwipe(){
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT ) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();


                if (direction == ItemTouchHelper.LEFT){
                    if(position == 0){
                        ((MyCardsAdapter) myCardsRecyclerView.getAdapter()).notifyItemChanged(position);
                        return;
                    }

                    PopUpDialog.showConfirmPopUpDialog(MyCardsActivity.this, getString(R.string.warning),getString(R.string.remove_card_msg),
                            getString(R.string.yes),getString(android.R.string.no), 0, null, new PopUpDialog.DialogActionListener() {
                                @Override
                                public void actionListener() {
                                    StripeCard card = ((MyCardsAdapter) myCardsRecyclerView.getAdapter()).getItem(position);
                                    if(card != null){
                                        removeCard(card);
                                    }else{
                                        ((MyCardsAdapter) myCardsRecyclerView.getAdapter()).notifyItemChanged(position);
                                    }


                                }

                                @Override
                                public void negativeAction() {
                                    ((MyCardsAdapter) myCardsRecyclerView.getAdapter()).notifyItemChanged(position);
                                }
                            });

                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                Bitmap icon;
                if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    if(dX > 0){

                    } else if (dX < 0) {
                        p.setColor(Color.parseColor("#00FFFFFF"));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(),(float) itemView.getRight(), (float) itemView.getBottom());
//                        if(ZegoConstants.DEBUG){
//                            Log.d("DX:","" + dX);
//                            Log.d("MYDX:","" + dm.widthPixels/4f);
//                            Log.d("ITEM_RIGHT:","" + itemView.getRight());
//                            Log.d("DIFF:","" + (float) (itemView.getRight()- dm.widthPixels/4f));
//                        }
                       // RectF background = new RectF((float) 500f, (float) itemView.getTop(),(float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background,p);
//                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.cancel_x);
//                        RectF icon_dest = new RectF((float) itemView.getRight() - 2*width ,(float) itemView.getTop() + width,(float) itemView.getRight() - width,(float)itemView.getBottom() - width);
//                        c.drawBitmap(icon,null,icon_dest,p);
                    }
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                }



            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(myCardsRecyclerView);
    }


    private void getUserById(User u) {
        Call<User> call = NetworkManager.getInstance().getRetrofit().create(UserRESTInterface.class).getUserById(u.getZegotoken(),u.getId());
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()){
                    user = response.body();
                    showPaymentFailed(user.getDebt());
                }else if(response.code() == ZegoConstants.ApiRestConstants.ERROR_500){

                }else{
                    NetworkErrorHandler.getInstance().errorHandler(response,MyCardsActivity.this);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                showOrDismissProgressWheel(DISMISS);
                showInfoDialog(getString(R.string.warning),getString(R.string.impossible_to_connetc_to_server));
            }
        });
    }


}
