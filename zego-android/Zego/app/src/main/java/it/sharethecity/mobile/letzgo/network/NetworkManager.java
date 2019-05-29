package it.sharethecity.mobile.letzgo.network;

import java.io.IOException;

import it.sharethecity.mobile.letzgo.utilities.ZegoConstants;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by lucabellaroba on 23/06/16.
 */
public class NetworkManager {

    private static NetworkManager instance;
    public static final String ZEGOTOKEN = "zegotoken";
    private  OkHttpClient client;
    private  Retrofit retrofit;
    private  Interceptor myInterceptor;

    private NetworkManager() {
        setClient();
    }

    public static NetworkManager getInstance() {
        if(instance == null){
            instance = new NetworkManager();
        }
        return instance;
    }

    public  OkHttpClient getClient() {
        return client;
    }

    public  Retrofit getRetrofit() {
        return retrofit;
    }


    public void setClient() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {
                Request original = chain.request();
                Request request = original.newBuilder()
                        .header("Content-Type", "application/json")
                        .method(original.method(), original.body())
                        .build();

                return chain.proceed(request);
            }
        });

        if(ZegoConstants.DEBUG){
            HttpLoggingInterceptor logginInterceptor = new HttpLoggingInterceptor();
            logginInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClient.addInterceptor(logginInterceptor);
        }



        client = httpClient.build();


         retrofit = new Retrofit.Builder()
                .baseUrl(ZegoConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

    }

    public Retrofit getRetrofitWithTokens(final String token){
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {
                Request original = chain.request();
                Request request = original.newBuilder()
                        .header("Content-Type", "application/json")
                        .header("zegotoken", token)
                        .method(original.method(), original.body())
                        .build();

                return chain.proceed(request);
            }
        });
        client = httpClient.build();
        retrofit = new Retrofit.Builder()
                .baseUrl(ZegoConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        return retrofit;
    }

    public Interceptor getMyInterceptor() {
        return myInterceptor;
    }

    public void setMyInterceptor(Interceptor myInterceptor) {
        this.myInterceptor = myInterceptor;
    }
}
