package com.game5a.sms;

public interface SmsResultProcessor {
	public static final int SMS_RESULT_SUCCESS = 1;
	public static final int SMS_RESULT_FAIL = 0;
	
	public abstract void smsCallBack(int result, String message);
}