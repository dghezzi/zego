package it.sharethecity.mobile.letzgo.fragments;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import it.sharethecity.mobile.letzgo.R;
import it.sharethecity.mobile.letzgo.application.ApplicationController;
import it.sharethecity.mobile.letzgo.bus.BusRequestMessage;
import it.sharethecity.mobile.letzgo.customviews.MyFontTextView;
import it.sharethecity.mobile.letzgo.dao.User;
import it.sharethecity.mobile.letzgo.network.request.CompatRequest;
import it.sharethecity.mobile.letzgo.network.request.Riderequest;
import it.sharethecity.mobile.letzgo.utilities.ZegoConstants;


public class RideHistoryFragment extends Fragment {

    public static final String DRIVER = "driver";
    public static final String PASSENGER = "rider";
    public static final String TYPE_LABEL = "type";


    private String type;
    private String mParam2;

    private boolean stopPaging;
    private boolean activityFirstAccess;
    private int pageLoaded;
    private Integer page;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private int previousTotal;
    private int visibleThreshold;
    private boolean loading;
    private boolean isFirstScroll;
    private OnRideHistoryListener mListener;



    @Nullable
    @BindView(R.id.ride_recycler_view)
    RecyclerView rideRecyclerView;

    @Nullable
    @BindView(R.id.no_ride_text_view)
    MyFontTextView noRideTextView;


    public RideHistoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment RideHistoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RideHistoryFragment newInstance(String param1) {
        RideHistoryFragment fragment = new RideHistoryFragment();
        Bundle args = new Bundle();
        args.putString(TYPE_LABEL, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type = getArguments().getString(TYPE_LABEL);
        }
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        ApplicationController.getInstance().getOttoBus().register(this);
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        ApplicationController.getInstance().getOttoBus().unregister(this);
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_ride_history, container, false);
        ButterKnife.bind(this,v);

        rideRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        rideRecyclerView.setAdapter(new RideHistoryAdapter(type));
        initPagination();
        setRecyclerListener();
        askNewData(page);
        return v;
    }



    private void setRecyclerListener() {
        rideRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) rideRecyclerView.getLayoutManager();
                visibleItemCount = recyclerView.getChildCount();
                totalItemCount = linearLayoutManager.getItemCount();

                int firstVisibleItem = 0;
                firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();


                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false;
                        previousTotal = totalItemCount;
                    }
                }
                if (!loading && !isFirstScroll && !stopPaging && (totalItemCount - visibleItemCount)
                        <= (firstVisibleItem + visibleThreshold)) {
                    // End has been reached

                    askNewData(page);

                    page++;
                    // Do something

                    loading = true;
                }
                isFirstScroll = false;
            }
        });
    }

    private void askNewData(Integer page){
        if(mListener != null){
            mListener.askRides(page,type);
        }
//        BusRequestMessage busRequestMessage = new BusRequestMessage();
//        busRequestMessage.setCode(type.equalsIgnoreCase(PASSENGER) ? ZegoConstants.OttoBusConstants.RIDE_HISTORY_PASSENGER : ZegoConstants.OttoBusConstants.RIDE_HISTORY_DRIVER);
//        busRequestMessage.setPayLoad(page);
//        ApplicationController.getInstance().getOttoBus().post(busRequestMessage);
    }

    private void initPagination() {
        activityFirstAccess = true;
        isFirstScroll = true;
        loading = true;
        stopPaging = false;
        visibleThreshold = 3;
        previousTotal = 0;
        page = 0;
    }

    public boolean isStopPaging() {
        return stopPaging;
    }

    public void setStopPaging(boolean stopPaging) {
        this.stopPaging = stopPaging;
    }

    public void addRides(List<CompatRequest> storeList){

        boolean reset = false;
        if(activityFirstAccess){
            activityFirstAccess = false;
            page = 1;
            reset = true;
        }
        ((RideHistoryAdapter)rideRecyclerView.getAdapter()).addItems(storeList,reset);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnRideHistoryListener) {
            mListener = (OnRideHistoryListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public void showNoRideView(boolean show) {
        noRideTextView.setVisibility(show ? View.VISIBLE : View.GONE);
        rideRecyclerView.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnRideHistoryListener {
        void onRideSelected(CompatRequest cmpRequest,String umode);
        void askRides(int page,String type);
    }



    public class RideHistoryAdapter extends RecyclerView.Adapter<RideHistoryAdapter.RideHistoryViewHolder>{


        List<CompatRequest> rideList;
        String type;

        public RideHistoryAdapter(String tipo) {
            rideList = new ArrayList<>();
            type = tipo;
        }

        @Override
        public RideHistoryAdapter.RideHistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(getContext()).inflate(R.layout.ride_history_layout,parent,false);
            return new RideHistoryViewHolder(v);
        }

        @Override
        public void onBindViewHolder(RideHistoryAdapter.RideHistoryViewHolder holder, int position) {
            CompatRequest ride = rideList.get(position);
            if(ride != null){
                holder.dateTextView.setText(getFormattedDate(Long.valueOf(ride.getReqdate()),"d MMMM yyyy - HH:mm"));
                holder.codeTextView.setText(ride.getShortid());
                holder.puAddressTextView.setText(ride.getPickup());
                holder.doAddressTextView.setText(ride.getDropoff());
                holder.rideCostTextView.setTextColor(ContextCompat.getColor(getContext(),(ride.getStatus() > 11 && type.equalsIgnoreCase(User.UMODE_RIDER)) ? R.color.red_cancel : R.color.darker_green_text));
                holder.rideCostTextView.setText(getFormattedCost(type.equalsIgnoreCase(PASSENGER) ? ride.getPassprice() : ride.getDrivprice()));
                holder.paymentMethodImg.setImageResource(ride.isCard() ? R.drawable.cardgreen : R.drawable.cashgreen);
                if(ride.isControlRide() || ride.isLadyRide()){
                    holder.tipoCorsaView.setVisibility(View.VISIBLE);
                    holder.tipoCorsaView.setBackgroundColor(ContextCompat.getColor(getContext(),ride.getLev() == 2 ? R.color.pink_simbolo : R.color.orange_simbolo));
                }else{
                    holder.tipoCorsaView.setVisibility(View.GONE);
                }

            }

        }

        @Override
        public int getItemCount() {
            return rideList.size();
        }

        public void addItems(List<CompatRequest> body,boolean reset) {
            if(reset){
                rideList.clear();
            }
            int preInsertNumber = rideList.size();
            rideList.addAll(body);

            notifyItemRangeInserted(preInsertNumber, rideList.size());
        }


        private SpannableString getFormattedCost(Integer price) {
            if(price == null) price = 0;

            String s = String.format("%.2f",(price)/100f) + " €";
            s = s.replace(".",",");
            SpannableString spannableString = new SpannableString(s);
            int decimalIndex = s.indexOf(",");
            int currencyIndex = s.indexOf("€");

            spannableString.setSpan(new RelativeSizeSpan(1.7f), 0, decimalIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new StyleSpan(Typeface.BOLD), 0, decimalIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new RelativeSizeSpan(1.2f), currencyIndex, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            return spannableString;
        }

        private String getFormattedDate(Long timeinSecond,String pattern) {
            Date date = new Date();
            date.setTime(timeinSecond * 1000);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, Locale.ITALIAN);
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("CET"));
            return simpleDateFormat.format(date);
        }

        public class RideHistoryViewHolder extends RecyclerView.ViewHolder{

            @Nullable
            @BindView(R.id.card_layout)
            CardView cardView;

            @Nullable
            @BindView(R.id.pu_address_text_view)
            MyFontTextView puAddressTextView;

            @Nullable
            @BindView(R.id.do_address_text_view)
            MyFontTextView doAddressTextView;

            @Nullable
            @BindView(R.id.date_text_view)
            MyFontTextView dateTextView;

            @Nullable
            @BindView(R.id.code_text_view)
            MyFontTextView codeTextView;

            @Nullable
            @BindView(R.id.paymentMethodImg)
            ImageView paymentMethodImg;

            @Nullable
            @BindView(R.id.tipoCorsaView)
            View tipoCorsaView;


            @Nullable
            @BindView(R.id.ride_cost_text_view)
            MyFontTextView rideCostTextView;

            public RideHistoryViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this,itemView);
            }

            @Optional
            @OnClick(R.id.card_layout)
            public void onRideClick(){
                if(mListener != null){
                    mListener.onRideSelected(rideList.get(getAdapterPosition()),type);
                }
            }
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("type",type);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            type = savedInstanceState.getString("type");
        }
    }


}
