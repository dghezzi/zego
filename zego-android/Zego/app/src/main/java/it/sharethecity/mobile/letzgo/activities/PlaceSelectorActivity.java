package it.sharethecity.mobile.letzgo.activities;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import butterknife.Optional;
import it.sharethecity.mobile.letzgo.R;
import it.sharethecity.mobile.letzgo.application.ApplicationController;
import it.sharethecity.mobile.letzgo.customviews.MyFontEditText;
import it.sharethecity.mobile.letzgo.customviews.MyFontTextView;
import it.sharethecity.mobile.letzgo.dao.Address;
import it.sharethecity.mobile.letzgo.dao.GeoCodeRequest;
import it.sharethecity.mobile.letzgo.dao.GeoCodeResponse;
import it.sharethecity.mobile.letzgo.dao.GooglePlacePredictions;
import it.sharethecity.mobile.letzgo.dao.GooglePlaceResponse;
import it.sharethecity.mobile.letzgo.dao.GoogleRequest;
import it.sharethecity.mobile.letzgo.network.NetworkManager;
import it.sharethecity.mobile.letzgo.network.interfaces.UserRESTInterface;
import it.sharethecity.mobile.letzgo.utilities.AutocompleteResult;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PlaceSelectorActivity extends ZegoBaseActivity {

    private static final int EDIT_ADDRESS_REQUEST = 4235;
    public static final String PICKUP = "pickup";
    public static final String DROPOFF = "dropoff";



    @Nullable
    @BindView(R.id.places_recycler_view)
    RecyclerView placesRecyclerView;


    @Nullable
    @BindView(R.id.cancel_place_button)
    ImageView cancelPlaceButton;

    @Nullable
    @BindView(R.id.place_edit_text)
    MyFontEditText placeEditText;

    @Nullable
    @BindView(R.id.confirm_place_button)
    Button confirmButton;

    boolean isPickUp;
    private List<Address> myAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_selector);
        ButterKnife.bind(this);
        myAddress = new ArrayList<>();
        placesRecyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext(),LinearLayoutManager.VERTICAL,false));
        placesRecyclerView.setAdapter(new PlacesSuggestionsAdapter());

        user = ApplicationController.getInstance().getUserLogged();

        isPickUp = getIntent().getExtras().getBoolean(PICKUP);

        getMyAddress();
    }


    private void getMyAddress() {
        Call<List<Address>> call = NetworkManager.getInstance().getRetrofit().create(UserRESTInterface.class).getAddress(user.getZegotoken(),user.getId(),isPickUp ? Address.PICKUP_TYPE : Address.DROPOFF_TYPE);
        call.enqueue(new Callback<List<Address>>() {
            @Override
            public void onResponse(Call<List<Address>> call, Response<List<Address>> response) {
                if(response.isSuccessful()){
                    myAddress = response.body();
                    ((PlacesSuggestionsAdapter)placesRecyclerView.getAdapter()).setOldPosition(true);
                    ((PlacesSuggestionsAdapter)placesRecyclerView.getAdapter()).setSuggestion(response.body());
                }else{

                }
            }

            @Override
            public void onFailure(Call<List<Address>> call, Throwable t) {
                showInfoDialog(getString(R.string.warning),getString(R.string.impossible_to_connetc_to_server));
            }
        });
    }


    @Optional
    @OnTextChanged(R.id.place_edit_text)
    public void onSearchTextChange(CharSequence text) {
        displayPredictiveResults(text.toString());
    }

    @Optional
    @OnClick(R.id.cancel_place_button)
    public void onCancelButton(){
        placeEditText.setText("");
    }


    @Optional
    @OnClick(R.id.confirm_place_button)
    public void onConfirmButton(){
        Address addreSeelcted = (Address) placeEditText.getTag();
        if(addreSeelcted != null){
            onPlaceSelected(addreSeelcted);
        }
    }


    @Optional
    @OnClick(R.id.back_button)
    public void onBackClick(){
        super.onBackPressed();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if((resultCode == RESULT_OK || resultCode == RESULT_CANCELED) && requestCode == EDIT_ADDRESS_REQUEST){
            getMyAddress();
        }else{
            super.onActivityResult(requestCode, resultCode, data);
        }

    }


    private void displayPredictiveResults( String query )
    {
        if(query.isEmpty()){
            ((PlacesSuggestionsAdapter)placesRecyclerView.getAdapter()).setOldPosition(true);
            ((PlacesSuggestionsAdapter)placesRecyclerView.getAdapter()).setSuggestion(myAddress);
            return;
        }
        ((PlacesSuggestionsAdapter)placesRecyclerView.getAdapter()).setOldPosition(false);
        GoogleRequest googleRequest = new GoogleRequest();
        googleRequest.setTerm(query);
        googleRequest.setLat(user.getLlat());
        googleRequest.setLng(user.getLlon());
        Call<List<Address>> call = NetworkManager.getInstance().getRetrofit().create(UserRESTInterface.class).postForPredictionRideAddress(user.getZegotoken(),googleRequest);
        call.enqueue(new Callback<List<Address>>() {
            @Override
            public void onResponse(Call<List<Address>> call, Response<List<Address>> response) {
                if(response.isSuccessful()){
                    ArrayList<Address> ad = new ArrayList<Address>();
                    for(Address pred : response.body()){
                        String full = pred.getAddress();
                        String top = full;
                        String bottom = "";

                        if(full.contains(", ")) {
                            top = full.substring(0,full.indexOf(",")).trim();
                            bottom = full.substring(full.indexOf(","));
                            if(bottom.startsWith(", ")) {
                                bottom = bottom.substring(2);
                            }
                        }

                        pred.setBottomTerm(bottom);
                        pred.setTopTerm(top);

                        ad.add(pred);

                    }

                    ((PlacesSuggestionsAdapter)placesRecyclerView.getAdapter()).setSuggestion(ad);
                }
            }

            @Override
            public void onFailure(Call<List<Address>> call, Throwable t) {
              //  showInfoDialog(getString(R.string.warning),getString(R.string.impossible_to_connetc_to_server));
            }
        });

    }

    private void onPlaceSelected(Address adressSelected){
        GeoCodeRequest geoCodeRequest = new GeoCodeRequest();
        geoCodeRequest.setPlaceid(adressSelected.getPlaceid());
        geoCodeRequest.setAddress(adressSelected.getAddress());
        Call<GeoCodeResponse> call = NetworkManager.getInstance().getRetrofit().create(UserRESTInterface.class).postForGeocode(user.getZegotoken(),geoCodeRequest);
        call.enqueue(new Callback<GeoCodeResponse>() {
            @Override
            public void onResponse(Call<GeoCodeResponse> call, Response<GeoCodeResponse> response) {
                if(response.isSuccessful()){
                    if(response.body() != null){
                        Address sd = new Address();
                        sd.setAddress(response.body().getAddress());
                        sd.setLng(response.body().getLng());
                        sd.setLat(response.body().getLat());
                        Intent i = new Intent();
                        i.putExtra(isPickUp ? MainActivity.PICK_UP_ADDRESS : MainActivity.DROP_OFF_ADDRESS, (Serializable) sd);
                        setResult(RESULT_OK,i);
                        finish();

                    }
                }

            }

            @Override
            public void onFailure(Call<GeoCodeResponse> call, Throwable t) {
                 showInfoDialog(getString(R.string.warning),getString(R.string.impossible_to_connetc_to_server));
            }
        });
    }

    public class PlacesSuggestionsAdapter extends RecyclerView.Adapter<PlacesSuggestionsAdapter.PlacesSuggestions>{


        List<Address> suggestions;
        private boolean isOldPosition;

        public PlacesSuggestionsAdapter(){
            suggestions = new ArrayList<>();
        }
        @Override
        public PlacesSuggestionsAdapter.PlacesSuggestions onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = getLayoutInflater().inflate(R.layout.suggestions_row_layout,parent,false);
            return new PlacesSuggestions(v);
        }

        @Override
        public void onBindViewHolder(PlacesSuggestionsAdapter.PlacesSuggestions holder, int position) {

//            if(position < suggestions.size()){
            Address address = suggestions.get(position);
            if(position == 0 && address.getType().equalsIgnoreCase(Address.HOME)){
                holder.indicatorImgView.setImageResource(R.drawable.home_icon);
                holder.secondLabelTextView.setVisibility(View.GONE);
                holder.secondLabelTextView.setText( "" );
                holder.editImgView.setVisibility(View.VISIBLE) ;
                holder.firstLabelTextView.setText(address.getAddress());
                holder.firstLabelTextView.setHint(getString(R.string.home_address));

            }else if(position == 1 && address.getType().equalsIgnoreCase(Address.WORK)){
                holder.indicatorImgView.setImageResource(R.drawable.work_icon );
                holder.secondLabelTextView.setVisibility(View.GONE);
                holder.secondLabelTextView.setText( "" );
                holder.firstLabelTextView.setText(address.getAddress());
                holder.editImgView.setVisibility(View.VISIBLE) ;
                holder.firstLabelTextView.setText(address.getAddress());
                holder.firstLabelTextView.setHint(getString(R.string.address_lavoro));
            }else{
                boolean isSearchType = isOldPosition && address.isSearch();
                holder.editImgView.setVisibility(isSearchType ? View.VISIBLE : View.GONE) ;
                holder.indicatorImgView.setImageResource(isSearchType ? R.drawable.puntoverde : R.drawable.puntogrigio );
                holder.firstLabelTextView.setText(isOldPosition ? address.getAddress() : address.getTopTerm());
                holder.secondLabelTextView.setVisibility(isOldPosition ? View.GONE : View.VISIBLE);
                holder.secondLabelTextView.setText(isOldPosition ? "" : address.getBottomTerm());
            }

//            }
        }

        @Override
        public int getItemCount() {
            return suggestions.size();
        }

        public void setSuggestion(List<Address> addresses) {
            suggestions.clear();
            suggestions.addAll(addresses);

            if(isOldPosition){
                Address home = null;
                Address work = null;

                if(suggestions.isEmpty()){
                    home = new Address();
                    home.setType(Address.HOME);
                    suggestions.add(0,home);

                    work =  new Address();
                    work.setType(Address.WORK);
                    suggestions.add(1,work);
                }else{
                    for (int i = 0 ; i < suggestions.size(); i++) {
                        if(i == 0 && !suggestions.get(i).getType().equalsIgnoreCase(Address.HOME)){
                            home = new Address();
                            home.setType(Address.HOME);
                        }

                        if(i == 1 && !suggestions.get(i).getType().equalsIgnoreCase(Address.WORK)){
                            work =  new Address();
                            work.setType(Address.WORK);
                        }
                    }

                    if(home != null){
                        suggestions.add(0,home);
                    }

                    if(work != null){
                        suggestions.add(1,work);
                    }
                }


            }


            notifyDataSetChanged();
        }


        public boolean isOldPosition() {
            return isOldPosition;
        }

        public void setOldPosition(boolean oldPosition) {
            isOldPosition = oldPosition;
        }

        public class PlacesSuggestions extends RecyclerView.ViewHolder{


            @Nullable
            @BindView(R.id.indicator_img_view)
            ImageView indicatorImgView;

            @Nullable
            @BindView(R.id.row_suggestion)
            RelativeLayout rowSuggestionView;

            @Nullable
            @BindView(R.id.edit_img_view)
            ImageView editImgView;

            @Nullable
            @BindView(R.id.first_label_text_view)
            MyFontTextView firstLabelTextView;

            @Nullable
            @BindView(R.id.second_label_text_view)
            MyFontTextView secondLabelTextView;

            public PlacesSuggestions(View itemView) {
                super(itemView);
                ButterKnife.bind(this,itemView);
            }


            @Optional
            @OnClick(R.id.row_suggestion)
            public void onRowClick(){
                Address addr = suggestions.get(getAdapterPosition());
                if(addr != null){
                    placeEditText.setTag(addr);
                    placeEditText.setText(addr.getAddress());
                    if(addr.isAddressOk() && (!addr.isSearch() || (addr.getType().equalsIgnoreCase(Address.WORK) || addr.getType().equalsIgnoreCase(Address.HOME)))){
                        Intent i = new Intent();
                        i.putExtra(isPickUp ? MainActivity.PICK_UP_ADDRESS : MainActivity.DROP_OFF_ADDRESS, (Serializable) addr);
                        setResult(RESULT_OK,i);
                        finish();
                    }
                }


            }

            @Optional
            @OnClick(R.id.edit_img_view)
            public void onEditRowClick(){
                int position = getAdapterPosition();
                Address addr = suggestions.get(position);
                if(addr != null && (position == 0 || position == 1)){
                    Intent i  = new Intent(PlaceSelectorActivity.this,AddressActivity.class);
                    i.putExtra(AddressActivity.TITLE,getString(position == 0 ? R.string.edit_home_address : R.string.edit_work_address));
                    i.putExtra(AddressActivity.ADDRESS,addr);
                    i.putExtra(AddressActivity.FROM_ACTIVITY,PlaceSelectorActivity.class.getSimpleName());
                    i.putExtra(AddressActivity.TYPE,position == 0 ? AddressActivity.HOME_ADDRESS_EDIT : AddressActivity.WORK_ADDRESS_EDIT);
                    startActivityForResult(i,EDIT_ADDRESS_REQUEST);
                }

            }
        }
    }
}
