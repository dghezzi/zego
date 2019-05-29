package it.sharethecity.mobile.letzgo.utilities;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import it.sharethecity.mobile.letzgo.R;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by lucabellaroba on 11/12/16.
 */

public class PolyLineTask extends AsyncTask<Void, Void, List<List<HashMap<String, String>>>> {

    public interface PolyLineTaskListener{
        void onPolyLineReady(PolylineOptions polylineOptions);
    }

    OkHttpClient client = new OkHttpClient();
    String polylineData;
    Context ctx;
    LatLng position1;
    LatLng position2;
    PolyLineTaskListener listener;

    public PolyLineTask(Context ctx, LatLng pos1, LatLng pos2, PolyLineTaskListener listener) {
        this.ctx = ctx;
        position1 = pos1;
        position2 = pos2;
        this.listener = listener;
    }

    String run(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }


    @Override
    protected List<List<HashMap<String, String>>> doInBackground(Void... params) {
        try {

            if (polylineData == null)
                polylineData =  run(getMapsApiDirectionsUrl());
            if (polylineData != null) {
                JSONObject jObject;
                List<List<HashMap<String, String>>> routes = null;

                try {
                    jObject = new JSONObject(polylineData);
                    PathJSONParser parser = new PathJSONParser();
                    routes = parser.parse(jObject);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return routes;
            }
        } catch (Exception e) {
            Log.d("Background Task", e.toString());
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<List<HashMap<String, String>>> routes) {
        ArrayList<LatLng> points = null;
        PolylineOptions polyLineOptions = null;
        if (routes != null && routes.size() > 0) {
            // traversing through routes
            for (int i = 0; i < routes.size(); i++) {
                points = new ArrayList<LatLng>();
                polyLineOptions = new PolylineOptions();
                List<HashMap<String, String>> path = routes.get(i);

                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                polyLineOptions.addAll(points);
                polyLineOptions.width(7);
                polyLineOptions.color(ContextCompat.getColor(ctx, R.color.green_zego));
            }

            listener.onPolyLineReady(polyLineOptions);

        }
    }

    protected String getMapsApiDirectionsUrl() {
        String waypoints = "origin=" + position1.latitude + "," + position1.longitude + "&destination=" + position2.latitude + ","
                + position2.longitude;
        String sensor = "sensor=false";
        String params = waypoints + "&" + sensor;
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + params;
        return url;
    }
}
