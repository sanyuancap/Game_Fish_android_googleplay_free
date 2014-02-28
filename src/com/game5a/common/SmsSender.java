package com.game5a.common;

//import javax.microedition.io.Connector;
//import javax.wireless.messaging.MessageConnection;
//import javax.wireless.messaging.TextMessage;

public class SmsSender {
	private static boolean bSmsClosed = true;

	/**
	 * 
	 * @param gatewayCode
	 *            关卡编码
	 * @param port
	 *            端口
	 * @param smsContent
	 *            发送内容
	 * @return 是否发送成功
	 */
	public static synchronized boolean sendSMS(String gatewayCode, String port, String smsContent) {
		if (bSmsClosed) {
			return true;
		}

		//		MessageConnection msgconn = null;
		//		boolean isOK = true;
		//		try {
		//			String address = "sms://" + gatewayCode; //+":5000";
		//			if (port != null) {
		//				if (port.length() > 0) {
		//					address += (":" + port);
		//				}
		//			}
		//			System.out.println("SMS address: " + address);
		//
		//			msgconn = (MessageConnection) Connector.open(address);
		//			TextMessage txtmsg = (TextMessage) msgconn.newMessage(MessageConnection.TEXT_MESSAGE);
		//			txtmsg.setAddress(address);
		//			txtmsg.setPayloadText(smsContent);
		//			System.out.println("SMS content: " + smsContent);
		//			msgconn.send(txtmsg);
		//		} catch (Exception e) {
		//			isOK = false;
		//			e.printStackTrace();
		//		}
		//		
		//		if (msgconn != null) {
		//			try {
		//				msgconn.close();
		//			} catch (Exception ioe) {
		//				System.out.println("smsconn.close() error!!!");
		//				return false;
		//			}
		//			return isOK;
		//		}
		return false;
	}
}
