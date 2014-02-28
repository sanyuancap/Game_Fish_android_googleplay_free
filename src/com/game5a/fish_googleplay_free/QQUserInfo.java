package com.game5a.fish_googleplay_free;

//#if (MobileType == Mid)||(MobileType == K700C)||(MobileType == 6230i)||(MobileType == S700)||(MobileType == 6101)||(MobileType == 7260)
//#else

//#endif

public class QQUserInfo {
	public static final int RANK_NUM = 10;

	//#if (MobileType == Mid)||(MobileType == K700C)||(MobileType == 6230i)||(MobileType == S700)||(MobileType == 6101)||(MobileType == 7260)
	//#else
	/** QQ 用户信息 */
	//	public static UserInfo user_info;

	/** QQ 对手用户信息 */
	//	public static OppUserInfo oppUser_info;

	/** QQ Player信息 */
	//	public static PlayerInfo[] PlayerInfo;

	/** QQ 游戏榜单的分数 */
	public static String[] player_Score = new String[RANK_NUM];

	/** QQ 游戏榜单的昵称 */
	public static String[] player_Nicename = new String[RANK_NUM];
	//#endif
}
