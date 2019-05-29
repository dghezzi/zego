package it.sharethecity.mobile.letzgo.adapters;

import android.app.Activity;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import it.sharethecity.mobile.letzgo.R;
import it.sharethecity.mobile.letzgo.application.ApplicationController;


/**
 * Created by lucabellaroba on 12/06/16.
 */
public class CountryPrefixAdapter extends ArrayAdapter<String> {
    private Activity activity;
    private int layout;
    private List<Integer> countryFlags;
    private String[] countryLabel;
    private String[] countryPrefix;


    public CountryPrefixAdapter(Activity context) {
        super(context, R.layout.country_spinner_row);
        this.activity = context;
        this.layout = R.layout.country_spinner_row;
        countryLabel = activity.getBaseContext().getResources().getStringArray(R.array.country_name);
        countryPrefix = activity.getBaseContext().getResources().getStringArray(R.array.country_prefix);
        TypedArray typedArray = activity.getResources().obtainTypedArray(R.array.country_flags);
        for(int i = 0 ; i < typedArray.length(); i++ ){
            if(countryFlags == null) countryFlags = new ArrayList<>();
            countryFlags.add(typedArray.getResourceId(i, 0));
        }
        typedArray.recycle();
        notifyDataSetChanged();

    }

    @Override
    public View getDropDownView(int position, View convertView,ViewGroup parent) {
        View view = convertView;
        ObjectHolder objectHolder;
        if (view == null) {
            LayoutInflater inflater = activity.getLayoutInflater();
            view = inflater.inflate(layout,parent,false);
            objectHolder = new ObjectHolder();
            objectHolder.countryLabel = (TextView) view.findViewById(R.id.country_label);
            objectHolder.countryFlag = (ImageView) view.findViewById(R.id.country_flag);
            view.setTag(objectHolder);
        }else{
            objectHolder = (ObjectHolder) view.getTag();
        }

        objectHolder.countryLabel.setText(countryLabel[position].substring(0,1).toUpperCase() + countryLabel[position].substring(1));
        objectHolder.countryFlag.setImageResource(countryFlags.get(position));
        view.getLayoutParams().width = ApplicationController.getInstance().getScreenDimension().widthPixels/2;
        return view;

    }

    @Override
    public int getCount() {
        return countryLabel.length;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ObjectHolder objectHolder;
        if (view == null) {
            LayoutInflater inflater = activity.getLayoutInflater();
            view = inflater.inflate(layout,parent,false);
            objectHolder = new ObjectHolder();
            objectHolder.countryLabel = (TextView) view.findViewById(R.id.country_label);
            objectHolder.countryFlag = (ImageView) view.findViewById(R.id.country_flag);
            view.setTag(objectHolder);
        }else{
            objectHolder = (ObjectHolder) view.getTag();
        }

        objectHolder.countryLabel.setVisibility(View.GONE);
        objectHolder.countryFlag.setImageResource(countryFlags.get(position));

        return view;
    }

    public String[] getCountryLabel() {
        return countryLabel;
    }

    public String[] getCountryPrefix() {
        return countryPrefix;
    }

    public void setCountryPrefix(String[] countryPrefix) {
        this.countryPrefix = countryPrefix;
    }

    private static class ObjectHolder {
        TextView countryLabel;
        ImageView countryFlag;
    }
}
