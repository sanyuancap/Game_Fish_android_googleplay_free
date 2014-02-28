package com.tencent.qqgame.ui.util;
/**
 * 游戏到大厅广播的消息。
 * 包括消息ID, 消息内容key等
 * <p>
 * 
 * </p>
 */
public class GameToHallBroadcast {
	//游戏消息通知Action
	public static final String GAME_NOTIFICATION_ACTION = "com.tencent.qqgame.gamenotification";
	
	//登录成功广播消息ID key
	public static final String KEY_ID = "KEY_ID";
	
	//广播消息登录账号key
	public static final String KEY_LOGIN_ACCOUNT = "KEY_LOGIN_ACCOUNT";
	//广播消息登录密码key
	public static final String KEY_LOGIN_PWD = "KEY_LOGIN_PWD";
	
	//广播消息登保存密码状态key
	public static final String KEY_SAVE_PWD = "KEY_SAVE_PWD";
	//广播消息登保存密码状态key
	public static final String KEY_AUTO_LOGIN = "KEY_AUTO_LOGIN";
	
	//游戏ID Key
	public static final String KEY_GAME_ID = "KEY_GAME_ID";
	
	//登录成功广播ID
	public static final int BROADCAST_ID_LOGIN_SUCCESS = 1;
	//更改登录状态广播ID
	public static final int BROADCAST_ID_CHANGE_LOGIN_STATUS = 2;
	//启动游戏成功
	public static final int BROADCAST_ID_START_SUCCESS = 3;
}
