package com.example.cv.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.RingtoneManager;
import android.net.Uri;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FCM_Messaging extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if(remoteMessage.getData().size()>0){
            String title,msg,img_url;
            title=remoteMessage.getData().get("title");
            msg= remoteMessage.getData().get("message");
            img_url=remoteMessage.getData().get("img_url");

            Intent intent= new Intent(this,MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent=PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);
            Uri sounduri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            final android.support.v4.app.NotificationCompat.Builder builder= new android.support.v4.app.NotificationCompat.Builder(this,"default");
            builder.setContentTitle(title);
            builder.setContentText(msg);
            builder.setContentIntent(pendingIntent);
            builder.setSound(sounduri);
            builder.setSmallIcon(R.drawable.ic_notification);

            ImageRequest imageRequest= new ImageRequest(img_url, new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap response) {
                    builder.setStyle(new android.support.v4.app.NotificationCompat.BigPictureStyle().bigPicture(response));
                    NotificationManager notificationManager=(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    notificationManager.notify(0,builder.build());
                }
            },0, 0, null, Bitmap.Config.RGB_565, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            MySingleton.getmInstance(this).addToRquestQueue(imageRequest);
        }
    }

}
