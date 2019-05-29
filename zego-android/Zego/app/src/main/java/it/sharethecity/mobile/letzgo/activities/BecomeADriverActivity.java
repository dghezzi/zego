package it.sharethecity.mobile.letzgo.activities;

import android.content.Context;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import butterknife.Optional;
import it.sharethecity.mobile.letzgo.R;
import it.sharethecity.mobile.letzgo.application.ApplicationController;
import it.sharethecity.mobile.letzgo.customviews.MyFontTextView;
import it.sharethecity.mobile.letzgo.customviews.PopUpDialog;
import it.sharethecity.mobile.letzgo.dao.Area;
import it.sharethecity.mobile.letzgo.dao.Driverdata;
import it.sharethecity.mobile.letzgo.dao.User;
import it.sharethecity.mobile.letzgo.network.NetworkManager;
import it.sharethecity.mobile.letzgo.network.interfaces.UserRESTInterface;
import it.sharethecity.mobile.letzgo.network.request.ErrorObject;
import it.sharethecity.mobile.letzgo.network.utils.NetworkErrorHandler;
import it.sharethecity.mobile.letzgo.utilities.ZegoConstants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BecomeADriverActivity extends ZegoBaseActivity {

    @Nullable
    @BindView(R.id.header)
    RelativeLayout headerLayout;

    @Nullable
    @BindView(R.id.areas_list_view)
    ListView areasListView;

    @Nullable
    @BindView(R.id.driver_data_info_text)
    MyFontTextView driverDataInfoText;

    @Nullable
    @BindView(R.id.become_drive_title_text_view)
    MyFontTextView becomeDriverTitleText;

    @Nullable
    @BindView(R.id.load_documents_button)
    Button loadDocumentsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_become_adriver);
        ButterKnife.bind(this);
        setUI();
        user = ApplicationController.getInstance().getUserLogged();
        areasListView.setAdapter(new AreasAdapter(getBaseContext(),R.layout.area_row,new ArrayList<Area>()));
        getDriverData(user);
    }



    @Optional
    @OnClick(R.id.back_button)
    public void onBackClick(){
        super.onBackPressed();
    }

    @Optional
    @OnClick(R.id.load_documents_button)
    public void onLoadDocumentsClick(){
        onpenUrlLoginGateway(user.getZegotoken());
//        HashMap<String,String> headers = new HashMap<>();
//        headers.put(NetworkManager.ZEGOTOKEN,user.getZegotoken());
//        openUrlWithHeaders(getString(R.string.url_zego_login),headers);
    }

    @Optional
    @OnItemClick(R.id.areas_list_view)
    public void onAreaItemClick(AdapterView<?> parent, int position){
       final Area selectedArea = (Area) parent.getAdapter().getItem(position);
        PopUpDialog.showConfirmPopUpDialog(BecomeADriverActivity.this, getString(R.string.become_driver_dialog_title),getString(R.string.become_driver_msg) + " " + selectedArea.getName() + "?",
                getString(R.string.ok),getString(R.string.no), 0, null, new PopUpDialog.DialogActionListener() {
                    @Override
                    public void actionListener() {
                        Driverdata driverdata = new Driverdata();
                        driverdata.setUid(user.getId());
                        driverdata.setArea(selectedArea.getName());
                        driverdata.setStatus(Driverdata.STATUS_CANDIDATE);
                        postCandicacy(user,driverdata);
                    }

                    @Override
                    public void negativeAction() {

                    }
                });
    }



    private void setUI() {
        headerLayout.getLayoutParams().height = (int) ((HEADER_PERCENT_HEIGHT)* ApplicationController.getInstance().getScreenDimension().heightPixels);
        titleTextView.setText(getString(R.string.become_driver));
        aheadTextView.setVisibility(View.INVISIBLE);
        aheadTextView.setEnabled(false);
    }

    private void showDriverInfoText(boolean show){
        driverDataInfoText.setVisibility(show ? View.VISIBLE : View.GONE);
        becomeDriverTitleText.setVisibility(show ?  View.GONE : View.VISIBLE);
        areasListView.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    private void getDriverData(final User user) {
        Call<Driverdata> call = NetworkManager.getInstance().getRetrofit().create(UserRESTInterface.class).getDriverData(user.getZegotoken(),user.getId());
        showOrDismissProgressWheel(SHOW);
        call.enqueue(new Callback<Driverdata>() {
            @Override
            public void onResponse(Call<Driverdata> call, Response<Driverdata> response) {
                showOrDismissProgressWheel(DISMISS);
                if(response.isSuccessful()){
                    if(response.body() == null){
                        showDriverInfoText(false);
                        getAreas(user);
                    }else{
                        if(response.body().getStatus() == Driverdata.STATUS_CANDIDATE){
                            showDriverInfoText(true);
                            loadDocumentsButton.setVisibility(View.VISIBLE);
                            driverDataInfoText.setText(getString(R.string.driver_candidate));

                        }else if(response.body().getStatus() == Driverdata.STATUS_REJECTED){
                            showDriverInfoText(true);
                            driverDataInfoText.setText(getString(R.string.driver_rejected));

                        }else if(response.body().getStatus() == Driverdata.STATUS_MISSING_DOCUMENTS){
                            showDriverInfoText(true);
                            loadDocumentsButton.setVisibility(View.VISIBLE);
                            driverDataInfoText.setText(getString(R.string.driver_missing_document));
                        }else if(response.body().getStatus() == Driverdata.STATUS_DOCUMENTS_UNDER_EVALUATION){
                            showDriverInfoText(true);
                            loadDocumentsButton.setVisibility(View.VISIBLE);
                            driverDataInfoText.setText(getString(R.string.driver_document_under_evaluation));
                        }else if(response.body().getStatus() == Driverdata.STATUS_DOCUMENTS_EXPIRED){
                            showDriverInfoText(true);
                            loadDocumentsButton.setVisibility(View.VISIBLE);
                            driverDataInfoText.setText(getString(R.string.driver_expired_document));
                        }
                    }

                }else{
                    NetworkErrorHandler.getInstance().errorHandler(response,BecomeADriverActivity.this);
                }
            }

            @Override
            public void onFailure(Call<Driverdata> call, Throwable t) {
                showOrDismissProgressWheel(DISMISS);
                showInfoDialog(getString(R.string.warning),getString(R.string.impossible_to_connetc_to_server));
            }
        });
    }

    private void getAreas(User user) {
        Call<List<Area>> call = NetworkManager.getInstance().getRetrofit().create(UserRESTInterface.class).getAllAreas(user.getZegotoken());
        showOrDismissProgressWheel(SHOW);
        call.enqueue(new Callback<List<Area>>() {
            @Override
            public void onResponse(Call<List<Area>> call, Response<List<Area>> response) {
                showOrDismissProgressWheel(DISMISS);
                if(response.isSuccessful()){
                    ((AreasAdapter)areasListView.getAdapter()).setAreas(response.body());
                }else {
                    NetworkErrorHandler.getInstance().errorHandler(response,BecomeADriverActivity.this);
                }

            }

            @Override
            public void onFailure(Call<List<Area>> call, Throwable t) {
                showOrDismissProgressWheel(DISMISS);
                showInfoDialog(getString(R.string.warning),getString(R.string.impossible_to_connetc_to_server));
            }
        });
    }

    private void postCandicacy(final User user, Driverdata driverdata) {
        Call<Driverdata> call = NetworkManager.getInstance().getRetrofit().create(UserRESTInterface.class).postDriverData(user.getZegotoken(),driverdata);
        showOrDismissProgressWheel(SHOW);
        call.enqueue(new Callback<Driverdata>() {
            @Override
            public void onResponse(Call<Driverdata> call, Response<Driverdata> response) {
                showOrDismissProgressWheel(DISMISS);
                if(response.isSuccessful()){
                    getDriverData(user);
                }
                else if(response.code() == ZegoConstants.ApiRestConstants.ERROR_500){
                    ErrorObject errorObject = NetworkErrorHandler.getInstance().decodeError(response);
                    showInfoDialog(getString(R.string.warning),errorObject != null ? errorObject.getMsg() : getString(R.string.unknown_error));
                }
                else {
                    NetworkErrorHandler.getInstance().errorHandler(response,BecomeADriverActivity.this);
                }
            }

            @Override
            public void onFailure(Call<Driverdata> call, Throwable t) {
                showOrDismissProgressWheel(DISMISS);
                showInfoDialog(getString(R.string.warning),getString(R.string.impossible_to_connetc_to_server));
            }
        });
    }


    public class AreasAdapter extends ArrayAdapter<Area> {
        public AreasAdapter(Context context, int resource, List<Area> objects) {
            super(context, resource, objects);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            ViewHolder holder;
            if(v == null){
                v = LayoutInflater.from(getContext()).inflate(R.layout.area_row,parent,false);
                holder = new ViewHolder(v);
                v.setTag(holder);
            }else{
                holder = (ViewHolder) v.getTag();
            }
            holder.areaText.setText(getItem(position).getName());
            return v;
        }

        public void setAreas(List<Area> areas) {
            clear();
            if(areas != null){
                addAll(areas);
            }

        }

        public class ViewHolder{

            @Nullable
            @BindView(R.id.area_text_view)
            MyFontTextView areaText;

            public ViewHolder(View v){
                ButterKnife.bind(this,v);
            }

        }
    }
}
