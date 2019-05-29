package it.sharethecity.mobile.letzgo.activities;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.otto.Subscribe;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import it.sharethecity.mobile.letzgo.R;
import it.sharethecity.mobile.letzgo.application.ApplicationController;
import it.sharethecity.mobile.letzgo.bus.BusRequestMessage;
import it.sharethecity.mobile.letzgo.bus.BusResponseMessage;
import it.sharethecity.mobile.letzgo.dao.User;
import it.sharethecity.mobile.letzgo.fragments.RideHistoryFragment;
import it.sharethecity.mobile.letzgo.network.NetworkManager;
import it.sharethecity.mobile.letzgo.network.interfaces.RideRESTInterface;
import it.sharethecity.mobile.letzgo.network.interfaces.UserRESTInterface;
import it.sharethecity.mobile.letzgo.network.request.CompatRequest;
import it.sharethecity.mobile.letzgo.network.utils.NetworkErrorHandler;
import it.sharethecity.mobile.letzgo.utilities.FontManager;
import it.sharethecity.mobile.letzgo.utilities.ZegoConstants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RideHistoryActivity extends ZegoBaseActivity implements RideHistoryFragment.OnRideHistoryListener {

    private static final int PAGINATION = 30;
    @Nullable
    @BindView(R.id.header)
    RelativeLayout headerLayout;


    @Nullable
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;

    @Nullable
    @BindView(R.id.view_pager)
    ViewPager viewPager;

    ViewPagerAdapter viewPagerAdapter;
    boolean isActivityRecreated;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_history);
        ButterKnife.bind(this);
        isActivityRecreated = savedInstanceState != null;
        setUI();
        tabLayout.setupWithViewPager(viewPager);
        setupViewPager(viewPager);
        setTabListener();

    }

    private void setTabListener(){
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        user = ApplicationController.getInstance().getUserLogged();
//        ApplicationController.getInstance().getOttoBus().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
//        ApplicationController.getInstance().getOttoBus().unregister(this);
    }

    private void setUI() {
        headerLayout.getLayoutParams().height = (int) ((HEADER_PERCENT_HEIGHT)* ApplicationController.getInstance().getScreenDimension().heightPixels);
        titleTextView.setText(getString(R.string.ride_history_title));
        aheadTextView.setVisibility(View.INVISIBLE);
        aheadTextView.setEnabled(false);
    }


    @Optional
    @OnClick(R.id.back_button)
    public void onBackClick(){
        super.onBackPressed();
    }

    private void changeTabsFont() {

        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    ((TextView) tabViewChild).setTypeface(FontManager.getInstance(getBaseContext()).getFont("Raleway-Light.ttf"));
                }
            }
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        if(isActivityRecreated){
            List<Fragment> fr = getSupportFragmentManager().getFragments();
            int i = 0;
            String[] titles = new String[]{getString(R.string.passenger),getString(R.string.driver)};
            for(Fragment f : fr){
                viewPagerAdapter.addFragment(f, titles[i]);
                i++;
            }

        }else{
            viewPagerAdapter.addFragment(RideHistoryFragment.newInstance(RideHistoryFragment.PASSENGER),getString(R.string.passenger));
            viewPagerAdapter.addFragment(RideHistoryFragment.newInstance(RideHistoryFragment.DRIVER),getString(R.string.driver));
        }

        isActivityRecreated = false;

        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(1);
        changeTabsFont();
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }


        });
    }



//    //region OTTOBUS RECEIVER
//
//    @Subscribe
//    public void ottoReceiver(BusRequestMessage reqMessage) {
//        if (reqMessage == null) return;
//        Integer page = (Integer) reqMessage.getPayLoad();
//        switch (reqMessage.getCode()){
//            case ZegoConstants.OttoBusConstants.RIDE_HISTORY_DRIVER:
//                getRideHistory(page, User.UMODE_DRIVER);
//                break;
//            case ZegoConstants.OttoBusConstants.RIDE_HISTORY_PASSENGER:
//                getRideHistory(page, User.UMODE_RIDER);
//                break;
//        }
//    }
//
//    //endregion


    private void getRideHistory(final Integer page,final String mode){
        if(user == null){
            user = ApplicationController.getInstance().getUserLogged();
        }
        Call<List<CompatRequest>> call = NetworkManager.getInstance().getRetrofit().create(RideRESTInterface.class).getRideHistory(user.getZegotoken(),user.getId(),mode,page * PAGINATION, (page + 1)* PAGINATION);
        showOrDismissProgressWheelWithTimeOut(SHOW,6);
        call.enqueue(new Callback<List<CompatRequest>>() {
            @Override
            public void onResponse(Call<List<CompatRequest>> call, Response<List<CompatRequest>> response) {
                showOrDismissProgressWheel(DISMISS);
                if(response.isSuccessful()){
//                    int i = viewPager.getCurrentItem();
                    int i = mode.equalsIgnoreCase(User.UMODE_DRIVER) ? 1 : 0;
                    RideHistoryFragment rideHistoryFragment = (RideHistoryFragment) viewPagerAdapter.getItem(i);
                    if(page == 0 && response.body().isEmpty()){
                        rideHistoryFragment.showNoRideView(true);
                    }else{
                        rideHistoryFragment.showNoRideView(false);
                        boolean stopPaging = false;
                        if (response.body().size() < PAGINATION) {
                            stopPaging = true;
                        }


                        rideHistoryFragment.setStopPaging(stopPaging);
                        rideHistoryFragment.addRides(response.body());
                    }
                }else{
                    NetworkErrorHandler.getInstance().errorHandler(response, RideHistoryActivity.this);
                }
            }

            @Override
            public void onFailure(Call<List<CompatRequest>> call, Throwable t) {
                showOrDismissProgressWheel(DISMISS);
                showInfoDialog(getString(R.string.warning),getString(R.string.impossible_to_connetc_to_server));
            }
        });
    }

    @Override
    public void onRideSelected(CompatRequest cmpReq,String umode) {
        if(cmpReq != null && umode != null && !umode.isEmpty()){
            Intent i = new Intent(RideHistoryActivity.this,RideDetailsActivity.class);
            i.putExtra(RideDetailsActivity.RIDE, (Serializable) cmpReq);
            i.putExtra(RideDetailsActivity.UMODE,umode);
            startActivity(i);
        }
    }

    @Override
    public void askRides(int page, String type) {
        if(ZegoConstants.DEBUG){
            Log.d("MODE:",type);
        }
        getRideHistory(page, type.equalsIgnoreCase(RideHistoryFragment.PASSENGER) ? User.UMODE_RIDER : User.UMODE_DRIVER);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
           return mFragmentList.get(position);
        }

        @Override
        public int getItemPosition(Object object) {
            // refresh all fragments when data set changed
            return  PagerAdapter.POSITION_NONE ;
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
//            changeTabsFont();
            return mFragmentTitleList.get(position);
        }

        public List<String> getmFragmentTitleList() {
            return mFragmentTitleList;
        }
    }
}
