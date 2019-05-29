/*
 * Copyright (C) 2014 ELbuild - Luca Adamo luca@elbuild.it
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package it.sharethecity.mobile.letzgo.gcm;

import android.app.ActivityManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.util.List;

import it.sharethecity.mobile.letzgo.R;
import it.sharethecity.mobile.letzgo.activities.SplashActivity;
import it.sharethecity.mobile.letzgo.application.ApplicationController;
import it.sharethecity.mobile.letzgo.bus.BusRequestMessage;
import it.sharethecity.mobile.letzgo.dao.User;
import it.sharethecity.mobile.letzgo.services.PollingService;
import it.sharethecity.mobile.letzgo.utilities.ZegoConstants;

public class GcmMessageHandler extends IntentService {

    public static final int NOTIFICATION_ID = 12345;
    private static final long CANCEL_TIME = 60 * 1000;  // 1 minuto

    String mes;
    private Handler handler;

    public GcmMessageHandler() {
        super("GcmMessageHandler");
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        handler = new Handler();
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();

        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        mes = extras.getString("zegodata");
        //  showToast();
        if(ZegoConstants.DEBUG)
        {
            Log.i("LETZGO_GCM", "Received : (" +messageType+")  "+extras.getString("zegodata"));
        }

        GcmBroadcastReceiver.completeWakefulIntent(intent);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(GcmMessageHandler.this)
                .setSmallIcon(R.drawable.small_push_icon)
                .setLargeIcon(BitmapFactory.decodeResource(GcmMessageHandler.this.getResources(), R.drawable.notification_big_icon))
                .setContentTitle("Zego")
                .setContentText(mes)
                .setStyle(new NotificationCompat.BigTextStyle()
                .bigText(mes));


        Intent targetIntent = new Intent(GcmMessageHandler.this, SplashActivity.class);
        targetIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent contentIntent = PendingIntent.getActivity(GcmMessageHandler.this, 0, targetIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(contentIntent);

//        if(isAppInBackground(getApplicationContext())){
//            Intent targetIntent = new Intent(GcmMessageHandler.this, SplashActivity.class);
//            targetIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//            PendingIntent contentIntent = PendingIntent.getActivity(GcmMessageHandler.this, 0, targetIntent,
//                    PendingIntent.FLAG_UPDATE_CURRENT);
//
//            builder.setContentIntent(contentIntent);
//        }else {
//            User u = ApplicationController.getInstance().getUserLogged();
//            if(u != null){
//                Intent i = new Intent(this, PollingService.class);
//                i.putExtra(PollingService.USER,u);
//                startService(i);
//            }

//        }




        Notification not = builder.build();
        not.flags = Notification.FLAG_AUTO_CANCEL;
        not.sound = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.short_simple_alarm);
        not.defaults = Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE;


        final NotificationManager nManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nManager.notify(NOTIFICATION_ID, not);

//        try {
//            Thread.sleep(CANCEL_TIME);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        nManager.cancel(NOTIFICATION_ID);


    }

//    public void showToast(){
//        handler.post(new Runnable() {
//            public void run() {
//                Toast.makeText(getApplicationContext(),mes , Toast.LENGTH_LONG).show();
//            }
//         });
//
//    }



    public boolean isAppInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        }else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }

}
