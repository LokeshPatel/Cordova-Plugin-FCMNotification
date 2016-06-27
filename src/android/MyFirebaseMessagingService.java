/**
 * Copyright 2016 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lokesh.FCMNotification.plugin;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static String TAG = "FCM Handler Activity";
    private static String MY_PREFS_NAME = "saveNotification";
    private static int MODE_PRIVATE = 0;
    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO(developer): Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
      //  sendNotification(remoteMessage.getNotification());
        createNotification(this.getApplicationContext(), remoteMessage.getNotification());
    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */


      private static String getAppName(Context context){
        return (String)context.getPackageManager().getApplicationLabel(context.getApplicationInfo());
       }


    public void createNotification(Context context, RemoteMessage.Notification message )
    {
        saveNotificationOnSharedPreferences (message.getBody(),context);
        Intent intent = new Intent(this, FCMNotificationHandlerActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(this.getApplicationInfo().icon)
                .setContentTitle(message.getTitle())
                .setContentText(message.getBody())
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());

    }

    private int saveNotificationOnSharedPreferences (String message,Context context)
    {  int getCountMessage =0;
        try {
            SharedPreferences sharedPreferences =  context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            getCountMessage = sharedPreferences.getInt("msgId", 0);
            getCountMessage = getCountMessage +1;
            String messageKey = "msg"+getCountMessage;
            editor.putString(messageKey, message);
            editor.putInt("msgId",getCountMessage );
            editor.commit();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return getCountMessage;
    }

    public JSONObject getNotificationOnSharedPreferences (Context context)
    {
        JSONObject jsonObject = new JSONObject();
        try {
            SharedPreferences sharedPreferences =  context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            int getCountMessage = sharedPreferences.getInt("msgId",0);
            String messageKey = "msg"+getCountMessage;
            for(int i=1;i<=getCountMessage;i++) {
                jsonObject.put(String.valueOf(i), sharedPreferences.getString(messageKey, null));
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return jsonObject;
    }

    public void removeNotificationOnSharedPreferences (Context context)
    {
        try {
            SharedPreferences sharedPreferences =  context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            sharedPreferences.edit().clear().commit();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void removeNotificationWithIDOnSharedPreferences (Context context,String id)
    {
        try {
            SharedPreferences sharedPreferences =  context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove(id);
            editor.commit();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


}
