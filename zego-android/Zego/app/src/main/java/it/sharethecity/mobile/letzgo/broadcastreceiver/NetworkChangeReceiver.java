package it.sharethecity.mobile.letzgo.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import it.sharethecity.mobile.letzgo.R;
import it.sharethecity.mobile.letzgo.application.ApplicationController;
import it.sharethecity.mobile.letzgo.bus.BusRequestMessage;
import it.sharethecity.mobile.letzgo.bus.BusResponseMessage;
import it.sharethecity.mobile.letzgo.utilities.UtilityFunctions;
import it.sharethecity.mobile.letzgo.utilities.ZegoConstants;

/**
 * Created by lucabellaroba on 11/12/16.
 */

public class NetworkChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int status = UtilityFunctions.NetworkUtil.getConnectivityStatus(context);

        BusResponseMessage busRespMessage = new BusResponseMessage();
        busRespMessage.setCode(ZegoConstants.OttoBusConstants.CONNECTION_STATUS);
        busRespMessage.setMessage(new Boolean(status == UtilityFunctions.NetworkUtil.TYPE_NOT_CONNECTED));
        ApplicationController.getInstance().getOttoBus().post(busRespMessage);

    }
}
