package com.tencent.qqgame.ui.util;
/**
 * ��Ϸ�������㲥����Ϣ��
 * ������ϢID, ��Ϣ����key��
 * <p>
 * 
 * </p>
 */
public class GameToHallBroadcast {
	//��Ϸ��Ϣ֪ͨAction
	public static final String GAME_NOTIFICATION_ACTION = "com.tencent.qqgame.gamenotification";
	
	//��¼�ɹ��㲥��ϢID key
	public static final String KEY_ID = "KEY_ID";
	
	//�㲥��Ϣ��¼�˺�key
	public static final String KEY_LOGIN_ACCOUNT = "KEY_LOGIN_ACCOUNT";
	//�㲥��Ϣ��¼����key
	public static final String KEY_LOGIN_PWD = "KEY_LOGIN_PWD";
	
	//�㲥��Ϣ�Ǳ�������״̬key
	public static final String KEY_SAVE_PWD = "KEY_SAVE_PWD";
	//�㲥��Ϣ�Ǳ�������״̬key
	public static final String KEY_AUTO_LOGIN = "KEY_AUTO_LOGIN";
	
	//��ϷID Key
	public static final String KEY_GAME_ID = "KEY_GAME_ID";
	
	//��¼�ɹ��㲥ID
	public static final int BROADCAST_ID_LOGIN_SUCCESS = 1;
	//���ĵ�¼״̬�㲥ID
	public static final int BROADCAST_ID_CHANGE_LOGIN_STATUS = 2;
	//������Ϸ�ɹ�
	public static final int BROADCAST_ID_START_SUCCESS = 3;
}
