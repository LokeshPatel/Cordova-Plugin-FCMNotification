package com.lokesh.FCMNotification.plugin;


import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;


/**
 * This class echoes a string called from JavaScript.
 */
public class FCMNotificationPlugin extends CordovaPlugin {

	public static final String PROPERTY_REG_ID_FCM = "registrationFcmId";
	private static final String PROPERTY_APP_VERSION = "appVersion";
	private static final String TAG = "FCM Notification";
	  public static final String PREFS_NAME = "FCMData";
	public CallbackContext getBackContext;
	public Context appContext =null;
	private static CordovaWebView gWebView;
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
    	appContext = cordova.getActivity().getApplicationContext();
    	getBackContext = callbackContext;
    	if (action.equals("FCMTokenID")) {
        	final SharedPreferences prefs = getPushyMePreferences(appContext);
    		String registrationId = prefs.getString(PROPERTY_REG_ID_FCM, "");
            if(registrationId.isEmpty())
            new registerForPushNotificationsAsync().execute();
            else
            getBackContext.success(registrationId);
            return true;
        }
       else if (action.equals("installPlugin")) {
			new registerForPushNotificationsAsync().execute();
            return true;
        }

		else if (action.equals("getAllMessages")) {
            MyFirebaseMessagingService pushMessage = new MyFirebaseMessagingService();
			JSONObject jsonObject = pushMessage.getNotificationOnSharedPreferences(appContext);
			getBackContext.success(jsonObject.toString());
			return true;
		}
		else if (action.equals("clearMessageFromStore")) {
			MyFirebaseMessagingService pushMessage = new MyFirebaseMessagingService();
			pushMessage.removeNotificationOnSharedPreferences(appContext);
			getBackContext.success();
			return true;
		}
		else if (action.equals("clearMessageByIdFromStore")) {
			try {
				String getMsgID = args.getString(0);
				if (getMsgID == null) {

					getBackContext.error("Please insert valid message id");
				} else {
					MyFirebaseMessagingService pushMessage = new MyFirebaseMessagingService();
					pushMessage.removeNotificationWithIDOnSharedPreferences(appContext, getMsgID);
					getBackContext.success();
				}
			}
			catch (Exception e)
			{
				getBackContext.error(e.getMessage());
			}
			return true;
		}

		 return false;
    }

    private class registerForPushNotificationsAsync extends AsyncTask<Void, Void, Exception>
    {
    	protected Exception doInBackground(Void... params)
    	{
    		try
    		{
				FirebaseMessaging.getInstance().subscribeToTopic("test");
                sendRegistrationIdToBackendServer(FirebaseInstanceId.getInstance().getToken(),appContext);
    		}
    		catch( Exception exc )
    		{
    			// Return exc to onPostExecute
    			return exc;
    		}
    		return null;
    	}

    	@Override
    	protected void onPostExecute(Exception exc)
    	{
    		// Failed?
    		if ( exc != null )
    		{
    			getBackContext.error(exc.getMessage().toString());
    			return;
    		}
    		else
    		{
    			getBackContext.success();
    		}
    	}

    	void sendRegistrationIdToBackendServer(String registrationId,Context appContext) throws Exception
    	{
    	   storeRegistrationId(appContext, registrationId, PROPERTY_REG_ID_FCM);
    	   getBackContext.success(registrationId);
    	}
    }

    private void storeRegistrationId(Context context, String regId,String property_Reg_ID) {
	    final SharedPreferences prefs = getPushyMePreferences(context);
	    String appVersion = getVersionID(context);
	    Log.i(TAG, "Saving regId on app" + appVersion);
	    SharedPreferences.Editor editor = prefs.edit();
	    editor.putString(property_Reg_ID, regId);
	    editor.putString(PROPERTY_APP_VERSION, appVersion);
	    editor.commit();
	}

    private SharedPreferences getPushyMePreferences(Context context) {
        return  context.getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE);
	}

    private String getVersionID(Context context)
    {
    	PackageManager manager = context.getPackageManager();
 	    PackageInfo info = null;
 	    String getid ="";
 		try {
 			info = manager.getPackageInfo(
 			    context.getPackageName(), 0);
 			getid = info.versionName;
 		} catch (NameNotFoundException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		}
 	       return getid;
    }

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
    }

	@Override
    public void onPause(boolean multitasking) {
        super.onPause(multitasking);
        final NotificationManager notificationManager = (NotificationManager) cordova.getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }

    @Override
    public void onResume(boolean multitasking) {
        super.onResume(multitasking);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
		gWebView = null;
    }


 public static boolean isActive()
    {
    	return gWebView != null;
    }
}
