package it.sharethecity.mobile.letzgo.network.utils;


import it.sharethecity.mobile.letzgo.network.request.ErrorObject;

/**
 * Created by lucabellaroba on 24/06/16.
 */
public interface NetWorkErrorInterface {
    void decodeNetWorkErrorListener(Integer responseCode,ErrorObject errorObject);
}
