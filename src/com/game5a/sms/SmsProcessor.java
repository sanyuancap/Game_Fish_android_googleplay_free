package com.game5a.sms;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsManager;
import android.util.Log;

public class SmsProcessor {

	private static final String ACTION_SMS_SENT = "com.example.android.apis.os.SMS_SENT_ACTION";
	private static Activity activity;
	private static SmsResultProcessor resultProcessor;

	public static void Init(Activity a, SmsResultProcessor callBack) {
		activity = a;
		resultProcessor = callBack;
		activity.registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				String message = null;
				boolean error = true;
				switch (getResultCode()) {
					case Activity.RESULT_OK :
						message = "Message sent!";
						error = false;
						break;
					case SmsManager.RESULT_ERROR_GENERIC_FAILURE :
						message = "Error.";
						break;
					case SmsManager.RESULT_ERROR_NO_SERVICE :
						message = "Error: No service.";
						break;
					case SmsManager.RESULT_ERROR_NULL_PDU :
						message = "Error: Null PDU.";
						break;
					case SmsManager.RESULT_ERROR_RADIO_OFF :
						message = "Error: Radio off.";
						break;
				}

				//Log.v("SMS", message);
				//System.out.println("SMS Send Message: " + message);
				
				int result = SmsResultProcessor.SMS_RESULT_SUCCESS;
				if (error) {
					result = SmsResultProcessor.SMS_RESULT_FAIL;
				}
				resultProcessor.smsCallBack(result, message);
				
			}
		}, new IntentFilter(ACTION_SMS_SENT));
	}

	public static synchronized void sendSMS(String address, String content) {
		//System.out.println("send sms begin...");
		SmsManager sms = SmsManager.getDefault();

		//sms.sendTextMessage(address, null, content, null, null);
		sms.sendTextMessage(address, null, content, PendingIntent.getBroadcast(activity, 0, new Intent(ACTION_SMS_SENT), 0), null);

		//System.out.println("send sms end...");
	}

}
