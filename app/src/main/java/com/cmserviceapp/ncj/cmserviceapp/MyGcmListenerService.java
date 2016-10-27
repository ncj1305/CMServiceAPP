package com.cmserviceapp.ncj.cmserviceapp;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

public class MyGcmListenerService extends GcmListenerService {

    private static final String TAG = "MyGcmListenerService";

    /**
     *
     * @param from SenderID 값을 받아온다.
     * @param data Set형태로 GCM으로 받은 데이터 payload이다.
     */
    @Override
    public void onMessageReceived(String from, Bundle data) {
        String phonenum = data.getString("phonenum");
        String latitude = data.getString("latitude");
        String longitude = data.getString("longitude");

        Log.d(TAG, "From: " + from);
        Log.d(TAG, "phonenum: " + phonenum);
        Log.d(TAG, "latitude: " + latitude);
        Log.d(TAG, "longitude: " + longitude);

        // GCM으로 받은 메세지를 디바이스에 알려주는 sendNotification()을 호출한다.
        sendNotification(phonenum, latitude, longitude);
    }


    /**
     * 실제 디바에스에 GCM으로부터 받은 메세지를 알려주는 함수이다. 디바이스 Notification Center에 나타난다.
     */
    private void sendNotification(String phonenum, String latitude, String longitude) {
        Intent intent = new Intent(this, MainActivity.class);
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("phonenum", phonenum);
        intent.putExtra("latitude", latitude);
        intent.putExtra("longitude", longitude);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setContentTitle("사고가 접수 되었습니다.")
                .setContentText("Service APP 으로 확인하세요.")
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setSmallIcon(R.drawable.emmergency)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
