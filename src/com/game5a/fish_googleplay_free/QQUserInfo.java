package com.game5a.fish_googleplay_free;

//#if (MobileType == Mid)||(MobileType == K700C)||(MobileType == 6230i)||(MobileType == S700)||(MobileType == 6101)||(MobileType == 7260)
//#else

//#endif

public class QQUserInfo {
	public static final int RANK_NUM = 10;

	//#if (MobileType == Mid)||(MobileType == K700C)||(MobileType == 6230i)||(MobileType == S700)||(MobileType == 6101)||(MobileType == 7260)
	//#else
	/** QQ �û���Ϣ */
	//	public static UserInfo user_info;

	/** QQ �����û���Ϣ */
	//	public static OppUserInfo oppUser_info;

	/** QQ Player��Ϣ */
	//	public static PlayerInfo[] PlayerInfo;

	/** QQ ��Ϸ�񵥵ķ��� */
	public static String[] player_Score = new String[RANK_NUM];

	/** QQ ��Ϸ�񵥵��ǳ� */
	public static String[] player_Nicename = new String[RANK_NUM];
	//#endif
}
