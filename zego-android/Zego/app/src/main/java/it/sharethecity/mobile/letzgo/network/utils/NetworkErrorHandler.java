package it.sharethecity.mobile.letzgo.network.utils;

import java.io.IOException;
import java.lang.annotation.Annotation;


import it.sharethecity.mobile.letzgo.network.NetworkManager;
import it.sharethecity.mobile.letzgo.network.request.ErrorObject;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;

/**
 * Created by lucabellaroba on 24/06/16.
 */
public class NetworkErrorHandler<T> {
     private static NetworkErrorHandler instance;

    private NetworkErrorHandler(){

    }

    public static NetworkErrorHandler getInstance() {
        if(instance == null){
            instance = new NetworkErrorHandler();
        }
        return instance;
    }


    public void errorHandler(Response<T> response, NetWorkErrorInterface listener){
        ErrorObject error = null;
        Converter<ResponseBody, ErrorObject> errorConverter = NetworkManager.getInstance()
                .getRetrofit().responseBodyConverter(ErrorObject.class, new Annotation[0]);
        try {
            error = errorConverter.convert(response.errorBody());
            listener.decodeNetWorkErrorListener(Integer.valueOf(response.code()),error);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public ErrorObject decodeError(Response<T> response){
        ErrorObject error = null;
        Converter<ResponseBody, ErrorObject> errorConverter = NetworkManager.getInstance()
                .getRetrofit().responseBodyConverter(ErrorObject.class, new Annotation[0]);
        try {
            return error = errorConverter.convert(response.errorBody());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
