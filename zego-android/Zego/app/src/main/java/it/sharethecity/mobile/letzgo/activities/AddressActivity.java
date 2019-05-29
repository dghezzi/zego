package it.sharethecity.mobile.letzgo.activities;

import android.app.Activity;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import butterknife.OnTextChanged;
import butterknife.Optional;
import it.sharethecity.mobile.letzgo.R;
import it.sharethecity.mobile.letzgo.application.ApplicationController;
import it.sharethecity.mobile.letzgo.dao.Address;
import it.sharethecity.mobile.letzgo.dao.GeoCodeRequest;
import it.sharethecity.mobile.letzgo.dao.GeoCodeResponse;
import it.sharethecity.mobile.letzgo.dao.GooglePlacePredictions;
import it.sharethecity.mobile.letzgo.dao.GooglePlaceResponse;
import it.sharethecity.mobile.letzgo.dao.GoogleRequest;
import it.sharethecity.mobile.letzgo.network.NetworkManager;
import it.sharethecity.mobile.letzgo.network.interfaces.UserRESTInterface;
import it.sharethecity.mobile.letzgo.network.request.ErrorObject;
import it.sharethecity.mobile.letzgo.network.utils.NetworkErrorHandler;
import it.sharethecity.mobile.letzgo.utilities.AutocompleteResult;
import it.sharethecity.mobile.letzgo.utilities.ZegoConstants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddressActivity extends ZegoBaseActivity {

    public static final String TITLE = "title";
    public static final String TYPE = "type";
    public static final String ADDRESS = "address";
    public static final int HOME_ADDRESS_EDIT = 9;
    public static final int WORK_ADDRESS_EDIT = 8;
    public static final String FROM_ACTIVITY = "fromActivity";

    @Nullable
    @BindView(R.id.suggested_places_list_view)
    ListView listView;

    @Nullable
    @BindView(R.id.autocomplete_text)
    EditText autoCompleteTextView;

    @Nullable
    @BindView(R.id.save_button)
    Button saveButton;

    @Nullable
    @BindView(R.id.header)
    RelativeLayout headerLayout;

    @Nullable
    @BindView(R.id.cancel_button)
    ImageButton cancelSuggestionButon;

    private SearchAdapter mAdapter;
    String title;
    int type;

    Address address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        ButterKnife.bind(this);

        if(getIntent().getExtras() == null){
            finish();
            return;
        }

        title = getIntent().getExtras().getString(TITLE);
        address = (Address) getIntent().getExtras().getSerializable(ADDRESS);
        type = getIntent().getExtras().getInt(TYPE);
        fromActivity = getIntent().getExtras().getString(FROM_ACTIVITY);
        user = ApplicationController.getInstance().getUserLogged();

        setUI();
        autoCompleteTextView.setText(address != null ? address.getAddress() : "");
        autoCompleteTextView.setSelection(address == null || address.getAddress()==null ? 0 : address.getAddress().length());
        mAdapter = new SearchAdapter(this, R.layout.serach_address_row);
        listView.setAdapter(mAdapter);
        autoCompleteTextView.setHint(getString(type == HOME_ADDRESS_EDIT ? R.string.home_address : R.string.address_lavoro));

    }

    private void setUI() {
        headerLayout.getLayoutParams().height = (int) ((HEADER_PERCENT_HEIGHT) * ApplicationController.getInstance().getScreenDimension().heightPixels);
        saveButton.getLayoutParams().height = (int) ((BOTTOM_BUTTON_PERCENT_HEIGHT) * ApplicationController.getInstance().getScreenDimension().heightPixels);
        aheadTextView.setVisibility(View.GONE);
        titleTextView.setText(title);
    }


    @Optional
    @OnTextChanged(R.id.autocomplete_text)
    public void onSearchTextChange(CharSequence text) {
        if(mAdapter != null){
            listView.setVisibility(View.VISIBLE);
            mAdapter.clear();
            mAdapter.getFilter().filter(text);
        }

    }


    @Optional
    @OnClick(R.id.back_button)
    public void onBackClick(){
        super.onBackPressed();
    }

    @Optional
    @OnClick(R.id.save_button)
    public void onSaveButton(){
//        if(address != null){
            postAddress(address);
//        }
    }

    @Optional
    @OnClick(R.id.cancel_button)
    public void onCancelButton(View v){
        autoCompleteTextView.setText("");
        address = null;
//        changeAvantiButtonState();
    }

    private void changeAvantiButtonState(){

        saveButton.setEnabled(address!= null);
        saveButton.setBackground(ContextCompat.getDrawable(getBaseContext(),address!= null ? R.drawable.green_button_selector : R.color.gray_button));
        if(address!= null)  closeKeyBoard(saveButton);
    }

    private void postAddress(Address address){
        if(address == null){
            address = new Address();
            address.setUid(user.getId());
            address.setType(type == HOME_ADDRESS_EDIT ? Address.HOME : Address.WORK);
        }
       Call<Address> call =  NetworkManager.getInstance().getRetrofit().create(UserRESTInterface.class).postAddress(user.getZegotoken(),address);
        showOrDismissProgressWheel(SHOW);
        call.enqueue(new Callback<Address>() {
            @Override
            public void onResponse(Call<Address> call, Response<Address> response) {
                showOrDismissProgressWheel(DISMISS);
                if(response.isSuccessful()){
                    AddressActivity.super.onBackPressed();
                }else if(response.code() == ZegoConstants.ApiRestConstants.ERROR_500){
                    ErrorObject errorObject = NetworkErrorHandler.getInstance().decodeError(response);
                    // TODO: 24/11/16 gestire un eventuale errore
                }
                else {
                    NetworkErrorHandler.getInstance().errorHandler(response,AddressActivity.this);
                }
            }

            @Override
            public void onFailure(Call<Address> call, Throwable t) {
                showOrDismissProgressWheel(DISMISS);
                showInfoDialog(getString(R.string.warning),getString(R.string.impossible_to_connetc_to_server));
            }
        });
    }


    @Optional
    @OnItemClick(R.id.suggested_places_list_view)
    public void onPlaceSelected(int position) {

        final AutocompleteResult location = (AutocompleteResult) listView.getAdapter().getItem(position);
        if (location != null) {
            GeoCodeRequest geoCodeRequest = new GeoCodeRequest();
            geoCodeRequest.setPlaceid(location.getPlaceID());
            geoCodeRequest.setAddress(location.getStreet()+" "+location.getLocation());
            Call<GeoCodeResponse> call = NetworkManager.getInstance().getRetrofit().create(UserRESTInterface.class).postForGeocode(user.getZegotoken(),geoCodeRequest);
            call.enqueue(new Callback<GeoCodeResponse>() {
                @Override
                public void onResponse(Call<GeoCodeResponse> call, Response<GeoCodeResponse> response) {
                    if(response.isSuccessful()){
                        if(response.body() != null){
                            address = new Address(response.body());
                            address.setUid(user.getId());
                            address.setType(type == HOME_ADDRESS_EDIT ? Address.HOME : Address.WORK);
                            closeKeyBoard(saveButton);
//                            changeAvantiButtonState();
                        }
                    }

                }

                @Override
                public void onFailure(Call<GeoCodeResponse> call, Throwable t) {
                   // showInfoDialog(getString(R.string.warning),getString(R.string.impossible_to_connetc_to_server));
                }
            });

            autoCompleteTextView.setText(location.getStreet() + "," + location.getLocation());
        }
        listView.setVisibility(View.GONE);

    }

    private class SearchAdapter extends ArrayAdapter<AutocompleteResult> {
        private Activity activity;
        private GoogleApiClient googleApiClient;

        public SearchAdapter(Activity context, int resource) {
            super(context, resource);
            activity = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View v = convertView;
            SearchViewHolder searchViewHolder;

            if(v ==  null){
                LayoutInflater inflater = activity.getLayoutInflater();
                v = inflater.inflate(R.layout.serach_address_row,parent,false);
                searchViewHolder = new SearchViewHolder();
                searchViewHolder.location = (TextView)v.findViewById(R.id.location_text);
                searchViewHolder.street = (TextView)v.findViewById(R.id.street_text);
                searchViewHolder.location.setTypeface(searchViewHolder.location.getTypeface(), Typeface.BOLD);
                searchViewHolder.street.setTypeface(searchViewHolder.street.getTypeface(),Typeface.BOLD);
                v.setTag(searchViewHolder);
            }else{
                searchViewHolder = (SearchViewHolder) v.getTag();
            }

            if(getItem(position)!= null) {
                searchViewHolder.location.setText(getItem(position).getLocation());
                searchViewHolder.street.setText(getItem(position).getStreet());
            }
            return v;
        }

        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {

//                    if( googleApiClient == null || !googleApiClient.isConnected() ) {
//                        return null;
//                    }
                    String query = "";
                    if(!constraint.toString().isEmpty()){
                        query = constraint.toString();
                    }
                    displayPredictiveResults(query);

                    return null;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    notifyDataSetChanged();
                }
            };
        }

        public void setGoogleApiClient(GoogleApiClient googleApiClient){
            this.googleApiClient = googleApiClient;
        }

        private void displayPredictiveResults( String query )
        {

            GoogleRequest googleRequest = new GoogleRequest();
            googleRequest.setTerm(query);
            googleRequest.setLat(user.getLlat());
            googleRequest.setLng(user.getLlon());
            Call<GooglePlaceResponse> call = NetworkManager.getInstance().getRetrofit().create(UserRESTInterface.class).postForPrediction(user.getZegotoken(),googleRequest);
            call.enqueue(new Callback<GooglePlaceResponse>() {
                @Override
                public void onResponse(Call<GooglePlaceResponse> call, Response<GooglePlaceResponse> response) {
                    if(response.isSuccessful()){
                        for(GooglePlacePredictions pred : response.body().getPredictions()){
                            String full = pred.getDescription();
                            String top = full;
                            String bottom = "";

                            if(full.contains(", ")) {
                                top = full.substring(0,full.indexOf(",")).trim();
                                bottom = full.substring(full.indexOf(","));
                                if(bottom.startsWith(", ")) {
                                    bottom = bottom.substring(2);
                                }
                            }
                            AutocompleteResult ap = new AutocompleteResult(bottom,top);
                            ap.setPlaceID(pred.getPlace_id());

                            add(ap);
                        }

                    }
                }

                @Override
                public void onFailure(Call<GooglePlaceResponse> call, Throwable t) {

                }
            });

        }


        private class SearchViewHolder{
            private TextView street;
            private TextView location;
        }

    }
}
