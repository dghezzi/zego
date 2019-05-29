package it.sharethecity.mobile.letzgo.network.interfaces;

import java.util.List;

import it.sharethecity.mobile.letzgo.dao.Conf;
import it.sharethecity.mobile.letzgo.network.request.BootRequest;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by lucabellaroba on 21/11/16.
 */

public interface ConfRESTInterface {

    @POST("v1/conf/global")
    Call<List<Conf>>  postConf(@Body BootRequest btr);
}
