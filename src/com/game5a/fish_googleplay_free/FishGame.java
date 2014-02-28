package com.game5a.fish_googleplay_free;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.List;
import java.util.Vector;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.midlet.ActivityMIDletBridge;
import javax.microedition.midlet.MIDlet;
import javax.microedition.rms.RecordStore;
import net.youmi.android.appoffers.CheckStatusNotifier;
import net.youmi.android.appoffers.EarnedPointsNotifier;
import net.youmi.android.appoffers.EarnedPointsOrder;
import net.youmi.android.appoffers.YoumiOffersManager;
import net.youmi.android.appoffers.YoumiPointsManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;
import cn.domob.android.ads.DomobInterstitialAd;
import cn.domob.android.ads.DomobInterstitialAdListener;
import cn.domob.android.ads.DomobAdManager.ErrorCode;
import com.game5a.action.ActionSet;
import com.game5a.common.Common;
import com.game5a.common.MovingString;
import com.game5a.common.Rectangle;
import com.game5a.common.ScrollText;
import com.game5a.common.Sound;
import com.game5a.common.Tool;
import com.game5a.sms.SmsResultProcessor;
import com.game5a.sms.SmsProcessor;
//import com.tencent.webnet.PreSMSReturn;
//import com.tencent.webnet.WebNetEvent;
//import com.tencent.webnet.WebNetInterface;
public class FishGame extends Canvas implements Runnable, SmsResultProcessor, EarnedPointsNotifier { // ,
	// WebNetEvent
	public static int MILLIS_PRE_UPDATE = 50;
	Thread thread;
	FishGame instance;
	public static int SCREEN_WIDTH; // ��Ϸ��Ļ�Ŀ��
	public static int SCREEN_HEIGHT; // ��Ϸ��Ļ�ĸ߶�
	public static int uiWidth;
	public static int uiHeight;
	/** Ĭ������ */
	public static Font font;
	/** Ĭ������� */
	public static int fontH;
	/** ��Ϸ״̬ */
	byte state;
	/** ֮ǰ״̬ */
	byte preState;
	/** �������֮ǰ״̬ */
	byte preNotifyState;
	/** ״̬ʱ�� */
	int stateTimes;
	/** ״̬: ��ʼ�������� */
	public static final byte STATE_START_MUSIC = 0;
	/** ״̬: Logo */
	public static final byte STATE_LOGO = 1;
	/** ״̬: ������ */
	public static final byte STATE_COVER = 2;
	/** ״̬: ���˵� */
	public static final byte STATE_MAIN_MENU = 3;
	/** ״̬: ���� */
	public static final byte STATE_LOAD = 4;
	/** ״̬: ��Ϸ״̬ */
	public static final byte STATE_GAME = 5;
	/** ״̬: ϵͳ�˵� */
	public static final byte STATE_SYSTEM_MENU = 6;
	/** ״̬: ���� */
	public static final byte STATE_HELP = 7;
	/** ״̬: ���� */
	public static final byte STATE_ABOUT = 8;
	/** ״̬: �̵� */
	public static final byte STATE_SHOP = 9;
	/** ״̬: ��ͼ */
	public static final byte STATE_MAP = 10;
	/** ״̬: ����ѡ�� */
	public static final byte STATE_PLAYER_CHOOSE = 11;
	/** ״̬: ��Ϸ��ʼ */
	public static final byte STATE_GAME_BEGIN = 12;
	/** ״̬: ��Ϸʱ����� */
	public static final byte STATE_TIME_OVER = 13;
	/** ״̬: ��Ϸ�ܽ� */
	public static final byte STATE_GAME_SUMMARY = 14;
	/** ״̬: �������˵�ȷ�� */
	public static final byte STATE_BACK_MAINMENU_CONFIRM = 15;
	/** ״̬: ��ʼ����Ϸȷ�� */
	public static final byte STATE_NEWGAME_CONFIRM = 16;
	/** ״̬: �޼�¼������Ϸȷ�� */
	public static final byte STATE_NO_RECORD_CONFIRM = 17;
	/** ״̬: �������˵�ȷ�� */
	public static final byte STATE_PLAYER_INFO_CONFIRM = 18;
	/** ״̬: ǱˮԱ�̵� */
	public static final byte STATE_EMPLOY_SHOP = 19;
	public static final byte STATE_CG = 21;
	public static final byte STATE_LOADING = 22;
	// public static final byte STATE_QQ_SHOWSCORE = 23;
	// public static final byte STATE_QQ_UPLOADSCORE = 24;
	// public static final byte STATE_QQ_UPLOADSCORE_RESULT = 25;
	public static final byte STATE_SMS_UI = 26; // 1116 QQ
	// public static final byte STATE_GAME_OVER = 27;
	public static final byte STATE_SPLOGO = 28;
	public static final byte STATE_MAP_MENU = 29;
	public static final byte STATE_PAUSE = 30;
	public static final byte STATE_RETURN = 31;
	// public static final byte STATE_SHOWSTAGE = 32;
	public static final byte STATE_MORE_GAME = 33; // ��Ϸ�Ƽ�
	public static final byte STATE_EXIT = 34;
	// public static final byte STATE_RETURN_EMPLOY = 35;
	// 0817
	public static final byte STATE_LIVEGAME_CONTINUE = 36;
	public static final byte STATE_LIVEGAME_SUCCESS = 37;
	public static final byte STATE_TUTOR = 40;
	public static final byte STATE_FIRST_TUTOR_OVER = 41;
	/** ״̬: ��ֵ�����ʾ */
	public static final byte STATE_GIFT_HINT = 50;
	/** ״̬: ������ʾ */
	public static final byte STATE_MAGNET_HINT = 51;
	// **********����**********
	Image logoImg, QQlogoImg;
	int logoTimes;
	Image coverImg;
	int coverX, coverY;
	int coverTimes;
	int coverMenuX;
	public static final int MAIN_MENU_X = 490;
	public static final int MAIN_MENU_Y = 1;
	Image menuBgImg; // , menuImg, itemBg0Img, itemBg1Img, itemBg2Img;
	int mainMenuIndex;
	// Image mainMenuBgImg;
	Image[] menuItemImgs;
	int[] menuItemXs, menuItemYs, menuItemWs, menuItemHs;
	int menuItemOffX;
	Image btnBg1Img;
	public static int mainMenuDx, mainMenuDy;
	public static int mainMenuBubbleX, mainMenuBubbleWidth, mainMenuBubbleDX;
	// Image qqBarImg;
	public int qqBarDx, qqBarDy;
	public int[] MainMenuX = new int[MAIN_MENU_NUM];
	public int[] MainMenuY = new int[MAIN_MENU_NUM];
	// static final int MAIN_MENU_NUM = 9;
	static final int MAIN_MENU_NUM = 6;
	static final int MENU_ITEM_H = 40;
	// Image sysMenuImg;
	int sysMenuIndex;
	static final int SYSTEM_MENU_NUM = 4;
	static final int MAP_MENU_NUM = 4;
	public int buttonWidth, buttonHeight;
	public int buttonLeftX, buttonLeftY, buttonRightX, buttonRightY;
	public static byte BUTTON_ALIGN_LEFT = 0;
	public static byte BUTTON_ALIGN_RIGHT = 1;
	Image btnBgImg, btnYesImg, btnNoImg;
	Image btnOkImg, btnBackImg, btnMenuImg, btnBuyImg, btnShopImg, btnMapImg, btnReplayImg;
	// ����ǽ��ť
	Image btnYoumiImg;
	// Image onoffImg;
	// Image mRectImg;
	Image rectTopImg, rectRoundImg;
	Image playerChooseImg;
	static final int PLAYER_NUM = 2;
	Image[] playerHeadImgs;
	int playerIndex;
	int[] playerHeadX = new int[PLAYER_NUM];
	int[] playerHeadY = new int[PLAYER_NUM];
	int playerHeadW, playerHeadH;
	Image shopImg, goldGetImg, boughtImg;
	Image[][] boatInfoImgs;
	Image infoTextImg;
	int shopIndex;// whb �̵������
	static final int BOAT_NUM = 4;
	static final int[] BOAT_PRICE = {100, 200, 500, 1000};
	static final int[] BOAT_FISH_SORT = {3, 5, 7, 9};
	static final int STAGE_NUM = 10;
	static final int[] MAP_PART_X = {29, 146, 31, 147, 148, 152, 104, 7, 10, 65};
	static final int[] MAP_PART_Y = {196, 169, 133, 134, 83, 49, 72, 97, 16, 2};
	static final int[] MAP_POINTER_X = {55, 169, 123, 181, 196, 187, 134, 47, 54, 147};
	static final int[] MAP_POINTER_Y = {235, 226, 168, 151, 126, 87, 100, 120, 68, 51};
	Image mapPointerImg; // mapBgImg,
	Image[] mapPartImgs;
	int mapPartIndex;
	int[] mapPartX = new int[STAGE_NUM];
	int[] mapPartY = new int[STAGE_NUM];
	int mapPartW, mapPartH;
	int mapPartOffX, mapPartOffY;
	Image mapRectImg;
	Image mapFocusImg;
	Image mapLockImg;
	Image medal0Img, medal1Img, medalBig0Img, medalBig1Img;
	Image stageGoalImg, stageScoreImg, stageResultImg, stageGoldImg, stageRecordImg, stageHighScoreImg;
	public static final int BAR_HEIGHT = 36;
	Image numFontImg1, numFontImg2, numFontImgSmall, clockImg, bar0Img, bar1Img, bar2Img; // scoreImg, goalImg, goldBarImg, barImg,
	Image handupImg, fishHandupImg, handupHintImg;
	static final int FISH_HANDUP_NUM = 5;
	static int[] fishHandupYs; // = {95, 80, 60, 70, 80};
	boolean bAccessToGame;
	// **********��Ϸ�й�**********
	static int MAP_WIDTH;
	static int MAP_HEIGHT;
	static final int VIEW_MAP_INIT_Y = 0;
	static int FISHMAN_POSX_INIT;
	static int FISHMAN_POSY_INIT;
	static final int FISHMAN_STEP = 6;
	int viewMapX;
	int viewMapY;
	Image bgImg;
	// Image skyImg;
	Fishman fishman;
	Image waterBackImg, waterFrontImg;
	Image banksideImg, bucketImg;
	/** ˮͰ��� */
	public static final int BUCKET_WIDTH = 55;
	public static int BUCKET_Y; // = 110 - viewMapY
	/** �Ƿ���ʾˮͰ��ʾ */
	boolean bHandupAble;
	static final int FORE_OBJECT_NUM = 2;
	Image[] foreObjImg = new Image[FORE_OBJECT_NUM];
	int[] foreObjMapX = new int[2];
	int[] foreObjNextBubbleTime = new int[2];
	int[] foreObjBubbleTimes = new int[2];
	public static ActionSet sprayAS;
	public static Image stars0Img, stars1Img;
	public static final int BUBBLE_TILE_NUM = 3;
	public static Image bubbleImg;
	public static final byte PROPS_LIGHTNING = 0;
	public static final byte PROPS_GOLD_2 = 1;
	public static final byte PROPS_GOLD_5 = 2;
	public static final byte PROPS_GOLD_10 = 3;
	public static final byte PROPS_GOLD_20 = 4;
	public static final byte PROPS_GOLD_30 = 5;
	public static final byte PROPS_MAGNET = 6;
	public static final int PROPS_TYPE_NUM = 7;
	public static Image[] propsImgs;
	public static Image propsFadeImg;
	// public static final int ITEM_TILE_NUM = 5;
	// public static Image lightningImg;
	public static Image fullFlagImg;
	public static Image[] fullHintImgs;
	Image beginImg, timeoverImg;
	// public static final int FISH_IMAGE_MAX = 10;
	// whb fix add shark image
	public static final int FISH_IMAGE_MAX = 11;
	Image[] fishImgs = new Image[FISH_IMAGE_MAX];
	public static Image numbFishImg1;
	public static Image numbFishImg2;
	public static Image careImg;
	Image goldCoinImg;
	public static int FISH_Y_MIN = 140;
	public static int FISH_Y_MAX = 300;
	Vector fishSet = new Vector();
	// static final int ADD_FISH_INTERVAL = 50;// ˢ���ʱ����
	int addFishTimes;
	public static int SCORE_NUM_FLY_X;
	public static int SCORE_NUM_FLY_Y;
	public static int GOLD_NUM_FLY_X;
	public static int GOLD_NUM_FLY_Y;
	Vector flyNumSet = new Vector();
	Vector bubbleSet = new Vector();
	Vector buttonBubbleSet = new Vector();
	int ADD_ITEM_INTERVAL/* = 200 */;// whb fix
	int addItemTimes;
	PropsBubble propsBubble;
	/** ˮ������ */
	static final int[] WATER_FLOAT_DY = {2, 1, 0, -1, -1, 0, 1, 2};
	static int WATER_FLOAT_Y = 130;
	boolean bShowLightning;
	int showLightningTimes;
	static final int SHOW_LIGHTNING_TIME_MAX = 5;
	public static final int SHOCKED_TIME_MAX = 40;
	// ��Ļ��
	/** ��Ļ��ʱ�� */
	int screenVibrateTimes;
	/** ��Ļ�񶯷��� */
	int screenVibrateScope;
	/** ��Ļ��λ��X */
	int vibrateX;
	/** ��Ļ��λ��Y */
	int vibrateY;
	/**
	 * *************************************************************************
	 * ****
	 */
	// �ؿ����� whb add ���һ��Ϊ��ѧ�ؿ�
	/** �ؿ���ͼID */
	static final int[] STAGE_MAP_ID = {0, 2, 3, 1, 0,// 1-5
			1, 2, 3, 2, 3, // 6-10
			0, 0 // teach live
	};
	/** �ؿ�ʱ�䣬�� */
	static final int[] STAGE_TIME = {120, 120, 120, 120, 120, // 1-5
			120, 120, 120, 120, 120,// 6-10
			90, 120 // teach live //0219
	};
	/** �ؿ�Ŀ�� */
	// static final int[] STAGE_GOAL = {200, 250, 300, 350, 400, // 1-5
	// 450, 550, 600, 650, 700,// 6-10
	// 400, 500 // teach live
	// };
	static final int[][] STAGE_GOAL = {{200, 300, 400}, {300, 400, 500}, {400, 500, 600}, {500, 600, 700}, {600, 700, 800}, // 1-5
			{800, 1000, 1200}, {1000, 1200, 1400}, {1200, 1400, 1600}, {1400, 1600, 1800}, {1600, 1800, 2000}, // 6-10
			{400, 500, 600}, {500, 600, 700} // teach live
	};
	/** �ؿ�������� */
	static final byte[][] STAGE_FISH_TYPE = {{0, 1, 2, 3, 4, 0, 1, 2, 3, 4, 9}, // 1
			// {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, //1
			{5, 1, 2, 3, 0, 5, 1, 2, 3, 4, 9}, // 2
			{5, 6, 2, 3, 1, 0, 1, 2, 3, 4, 9}, // 3
			{0, 4, 7, 3, 4, 1, 6, 7, 3, 4, 9}, // 4
			{5, 4, 3, 4, 0, 1, 6, 7, 8, 4, 9}, // 5
			{5, 3, 4, 8, 2, 5, 6, 7, 8, 0, 9}, // 6
			{4, 5, 7, 8, 2, 3, 6, 7, 8, 0, 9}, // 7
			{2, 5, 7, 8, 2, 1, 6, 4, 8, 0, 9}, // 8
			{2, 3, 7, 8, 4, 5, 6, 7, 8, 4, 9}, // 9
			{5, 6, 7, 8, 4, 5, 6, 7, 8, 4, 9}, // 10
			{1, 1, 2, 3, 4, 5, 6, 7, 8, 2, 9}, // teach
			{5, 6, 7, 8, 4, 5, 6, 7, 8, 4, 9}, // live
	};
	/** ˢ���ʱ���� */
	// static final byte[] ADD_FISH_INTERVAL = {25, 25, 25, 25, 25,//
	// 25, 25, 25, 25, 25, //
	// 20, 25};
	// static final byte[] ADD_FISH_INTERVAL = {
	// 20, 21, 21, 22, 22,//
	// 23, 23, 24, 24, 25, //
	// 16, 20};
	static final byte[] ADD_FISH_INTERVAL = {20, 20, 20, 20, 20, //
			20, 20, 20, 18, 18, //
			16, 20};
	static final short[] ADD_SHARK_INTERVAL = {250, 250, 250, 250, 250,//
			250, 250, 250, 250, 250, //
			200, 350};
	static final short[] ADD_EEL_INTERVAL = {300, 300, 300, 300, 300,//
			300, 300, 300, 300, 300, //
			300, 300};
	/** �ؿ����ŵ�ID */
	int stageOpenID;
	/** ��ǰ�ؿ�ID */
	int stageID;
	/** ������ID */
	int fishmanID;
	/** �洬ID */
	int boatID;
	/** �÷� */
	int score;
	/** Ŀ�� */
	int goal;
	/** ��� */
	int gold;
	/** ���Ƹ��� */
	int medalNum;
	/** ���йؿ���Ʒ���� */
	int[] medalNums = new int[STAGE_NUM + 2];
	/** �ؿ���߷� */
	int highScore;
	/** ���йؿ���߷� */
	int[] highScores = new int[STAGE_NUM + 2];
	// /** ��սģʽ��߷� */
	// int liveGameHighScore;
	/** ��ǰ�ؿ���ý�� */
	int goldGeted;
	/** ��ǰ�ؿ��Ƿ��¼�¼ */
	boolean bNewRecord;
	/** ��Ϸ��ʱ�� */
	int gameTimesTotal;
	/** ʣ����Ϸʱ�� */
	int gameTimesLeft;
	/** �����Ƿ��� */
	boolean bSoundOn;
	/** ���� */
	Sound music;
	/** ��ǰ���������ļ� */
	String curMusicFile;
	/** �����ı� */
	ScrollText scrollText;
	public static final int MESSAGE_WIDTH = 400;
	public static final int MESSAGE_HEIGHT = 120;
	public static int MESSAGE_X;
	public static int MESSAGE_Y;
	public static final int MESSAGE_RECT_WIDTH = 420;
	public static final int MESSAGE_RECT_HEIGHT = 140;
	public static int MESSAGE_RECT_X;
	public static int MESSAGE_RECT_Y;
	/** ��Ϣ���� */
	ScrollText message;
	/** ������Ϣ */
	MovingString hintMessage;
	/** �Ƿ��Ѿ�������Ϸ��Դ */
	boolean bLoadGameRes;
	// static final String STR_HELP =
	// "��Ϸ����\n\n�ƶ�������������洬����\n�����㹳��ȷ����������Ļ��ť\n���������������Ļ��ť\n������������Ļ��ť\n�˵���������Ļ�˵���\n���أ�������Ļ���ؼ�\n\nע��\n��ͬ�ȼ���ֻ��װ�����͹�����������ͬ����ʹ�ý������\nС��ˮ�׵�Σ������\n���պ�ʱ�����õ���";
	// static final String STR_HELP =
	// "��Ϸ����\n\n�ƶ������Ҽ�\n�����㹳��ȷ�������¼�\n���������A��\nǱˮԱ�л���B��\n������C��\n�˵��������\n���أ������\n\nע��\n��ͬ�ȼ���ֻ��װ�����͹�����������ͬ����ʹ�ý������\nС��ˮ�׵�Σ������\n���պ�ʱ�����õ���";
	static final String STR_HELP = "����Ϸ������\n\n��ֻ�ƶ��������ֻ�������б�ֻ�\n�շ��㹳�����ˮ��\n����������������ˮͰ\nǱˮԱ�л���������Ƹ�ͼ��\nǱˮԱ�ƶ������ˮ�����б�ֻ�\nħ�ô������������ͼ��\n\nע��\n��ͬ�ȼ���ֻ��װ�����͹�����������ͬ����ʹ�ý��������\nС��ˮ�׵�Σ�����࣡\n���պ�ʱ�����õ��ߣ�";
	// static final String STR_ABOUT =
	// "��Ϸ����\n\n�����ṩ�̣�\n��ʢ�촴�Ƽ���չ���޹�˾\n\n�ͷ��绰��0510-85387510\n�ͷ����䣺kf@5agame.com";
	static final String STR_ABOUT = "����Ϸ���ڡ�\n\n������\n��Ϸ���ͣ�������Ϸ\n��Ϸ�汾��V1.0.0" + "\n�����ṩ�̣���ʢ�촴�Ƽ���չ���޹�˾\n\n�ͷ��绰��0510-85387510\n�ͷ����䣺kf@5agame.com"; // +
	// "\n\n����������\n����Ϸ�İ�Ȩ����ʢ�촴���У���Ϸ�е����֡�ͼƬ�����ݾ�Ϊ��Ϸ��Ȩ�����ߵĸ���̬�Ȼ��������й����ŶԴ˲��е��κη������Ρ�";
	static final String STR_START_MUSIC = "�Ƿ������֣�";
	static final String STR_YES = "��";
	static final String STR_NO = "��";
	static final String STR_ANYKEY = "������Ļ����...";
	// static final String STR_ANYKEY = "�����������...";
	static final String STR_STAGE_NOT_OPEN = "�ó���δ������\n����Ҫ�����ǰ�泡��Ŀ�ꡣ";
	static final String STR_STAGE_GOAL = "�ؿ�Ŀ�꣺";
	static final String STR_STAGE_SCORE = "�ؿ��÷֣�";
	static final String STR_STAGE_SUCCESS = "Ŀ����ɣ���ϲ���أ�";
	static final String STR_STAGE_FAIL = "Ŀ��δ��ɣ���սʧ�ܣ�";
	static final String STR_BOAT_BUY_SUCCESS = "�����ɹ���\n��������߼���ֻ��ս�����ɣ�";
	static final String STR_BOAT_BUY_FAIL0 = "�˴���ӵ�У�����������\n����ѡ����������ֻ��";
	static final String STR_BOAT_BUY_FAIL1 = "��Ҳ��㣬����ʧ�ܣ�\n���������������㹻�Ľ�ҡ�";
	static final String STR_BOAT_BUY_FAIL2 = "�ϵ͵ȼ���ֻ��δ��������ʧ�ܣ�\n�����������ϵͼ���Ĵ�ֻ��һ��һ�����";
	static final String STR_NEW_GAME = "��ʼ����Ϸ������ʧ������Ϸ��¼��\n�Ƿ������";
	static final String STR_NO_RECORD = "û����Ϸ��¼��\n�Ƿ�ʼ����Ϸ��";
	static final String STR_BACK_MAINMENU = "�������˵�����������ǰ��Ϸ��\n�Ƿ������";
	static final String STR_HOOK_FAIL = "���ܵ����Ʒ�ֵ��㣬��������ֻ��";
	static final String STR_GOLD_LACK = "��Ҳ��㣬����ʧ�ܣ�\n���������������㹻�Ľ�ҡ�";
	static final String STR_BUY_SUCCESS = "����ɹ����뷵�أ�";
	// static final String STR_MOREGAME_URL =
	// "http://gamepie.i139.cn/wap/s.do?j=3channel";
	static final String STR_MOREGAME_URL = "http://wapgame.189.cn";
	Image loadingFish, loadingImg, loadingBackImg, loadingFrontImg;
	int loadingStep;
	// 110817 ����
	public static final int SCORE_GAME_SCORE = 0;
	public static final int SCORE_FISH_NUM = 1;
	public static final int SCORE_GAME_GOLD = 2;
	// public static final int SCORE_GAME_TIME = 3;
	int updateGameScore;
	int updateFishNum, updateFishNumBak;
	int updateGold, updateGoldBak;
	int fishOnceNum, numbAttackTime, magnetUseTime;
	public static final String[] STR_ACH_NAME = {"", "��������", "ҵ�ಶ����", "��ְҵ����", "ְҵ����Ա", "�ռ���������", // 5
			"��������", "����΢��", "���ָ��ָ߸��֣�", "��Ϸ���", "���봹������", // 10
			"����Ѳ����", "���³���׷����", "��������ս��ߣ�", "�����ɹ�5��", "�����ɹ�30��", // 15
			"�����İٷ����У�", "��������", "ģ�������", "�ϸ����Ϸ���", "����һ������", // 20
			"�ﵽ������ǣ�", "������ȫ���ǣ���", "��ʶ������", "���ﻥ���ĺø���", "�ε����յĺ��ֵ�", // 25
			"�ڶ��ֲ��㷽��", "���ɵ��ؼ�", "��ս�ոտ�ʼ", "�ۣ���ս���ˣ�", "��ã���Ů��", // 30
	};
	// 110817 �ɾ�
	public static final int ACH_FISH_TOTAL_50 = 1; // ��������
	public static final int ACH_FISH_TOTAL_100 = 2; // ҵ�ಶ����
	public static final int ACH_FISH_TOTAL_500 = 3; // ��ְҵ����
	public static final int ACH_FISH_TOTAL_1000 = 4; // ְҵ����Ա
	public static final int ACH_FISH_TOTAL_3000 = 5; // �ռ���������
	public static final int ACH_FISH_ONCE_2 = 6; // ��������
	public static final int ACH_FISH_ONCE_3 = 7; // ����΢��
	public static final int ACH_FISH_ONCE_4 = 8; // ���ָ��ָ߸��֣�
	public static final int ACH_BUY_GOLD = 9; // ��Ϸ���
	public static final int ACH_TUTOR_OVER = 10; // ���봹������
	public static final int ACH_BOAT_1 = 11; // ����Ѳ����
	public static final int ACH_BOAT_2 = 12; // ���³���׷����
	public static final int ACH_BOAT_3 = 13; // ��������ս��ߣ�
	public static final int ACH_FISH_SUCCESS_5 = 14; // �����ɹ�5��
	public static final int ACH_FISH_SUCCESS_30 = 15; // �����ɹ�30��
	public static final int ACH_FISH_SUCCESS_100 = 16; // �����İٷ����У�
	public static final int ACH_NUMB_ATTACK_5 = 17; // ��������
	public static final int ACH_MAGNET_USE_5 = 18; // ģ�������
	public static final int ACH_STAGE_ALL_PASS = 19; // �ϸ����Ϸ���
	public static final int ACH_STAGE_MEDAL_1 = 20; // ����һ������
	public static final int ACH_STAGE_MEDAL_5 = 21; // �ﵽ������ǣ�
	public static final int ACH_STAGE_MEDAL_10 = 22; // ������ȫ���ǣ���
	public static final int ACH_DIVER_0 = 23; // ��ʶ������
	public static final int ACH_DIVER_1 = 24; // ���ﻥ���ĺø���
	public static final int ACH_DIVER_2 = 25; // �ε����յĺ��ֵ�
	public static final int ACH_BUY_DIVER_CONTROL = 26; // �ڶ��ֲ��㷽��
	public static final int ACH_BUY_MAGNET = 27; // ���ɵ��ؼ�
	public static final int ACH_LIVEGAME_1 = 28; // ��ս�ոտ�ʼ
	public static final int ACH_LIVEGAME_20 = 29; // �ۣ���ս���ˣ�
	public static final int ACH_GIRL_PLAYER = 30; // ��ã���Ů��
	public static final int ACH_NUM = 30;
	public boolean[] bAchGeted = new boolean[ACH_NUM + 1];
	// 110718
	boolean bTutorStage;
	boolean bTutorOver;
	byte tutorID, preTutorID;
	int tutorTimes, curTutorTimes, nextTutorTimes;
	public static final byte TUTOR_NULL = 0;
	public static final byte TUTOR_BOAT_MOVE = 1;
	public static final byte TUTOR_HOOK = 2;
	public static final byte TUTOR_SENSOR = 3;
	public static final byte TUTOR_SENSOR_BOAT_MOVE = 4;
	public static final byte TUTOR_DIVER_CONTROL = 5;
	public static final byte TUTOR_DIVER_MOVE = 6;
	public static final byte TUTOR_SENSOR_DIVER = 7;
	public static final byte TUTOR_SENSOR_DIVER_MOVE = 8;
	public static final byte TUTOR_MAGNET = 9;
	public static final byte TUTOR_OVER = 10;
	Image[] tutorImgs;
	Image tutorBgImg;
	int tutorRectX, tutorRectY, tutorRectW, tutorRectH;
	boolean bFirstTutorOver;
	public void initTutor() {
		if (!bFirstTutorOver) {
			tutorImgs = new Image[8];
			for (int i = 0; i < tutorImgs.length; i++) {
				tutorImgs[i] = Tool.createImage("/tutor" + (i + 1) + ".png");
			}
			tutorBgImg = Tool.createImage("/tutorBg.png");
			bTutorStage = true;
			bTutorOver = false;
			tutorID = preTutorID = TUTOR_NULL;
			bPadLock = true;
			allFishToScoreNum = 3;
			diver = null;
			diverID = -1;
			isHasControl = false;
			playerScore = 0;
			bSensorAble = false;
		} else {
			bTutorStage = true;
			bTutorOver = true;
			tutorID = preTutorID = TUTOR_NULL;
			bPadLock = false;
			allFishToScoreNum = 3;
			// diver = null;
			// diverID = -1;
			isHasControl = true;
			playerScore = 0;
			// bSensorAble = false;
		}
	}
	// public void freeTutorRes() {
	// tutorImgs = null;
	// bTutorStage = false;
	// }
	public void setTutor(byte id) {
		tutorID = id;
		preTutorID = tutorID;
		curTutorTimes = 0;
		// bPad0Jump = bPad1Jump = bPad2Jump = bPad3Jump = bPad4Jump = bPad5Jump
		// = bPad6Jump = false;
		state = STATE_TUTOR;
	}
	public void tutorOver() {
		tutorImgs = null;
		bTutorOver = true;
		// bPad0Jump = bPad1Jump = bPad2Jump = bPad3Jump = bPad4Jump = bPad5Jump
		// = bPad6Jump = false;
		bPadLock = false;
		diver.actState = Diver.ACT_STATE_NORMAL;
		diver.setAutoDiverStep(diverID);
		bSensorAble = false;
		// 110818
		updateAchievement(ACH_TUTOR_OVER);
	}
	public void cycleTutor() {
		if (message != null) {
			return;
		}
		if (!bTutorStage || bTutorOver) {
			return;
		}
		if (state == STATE_GAME) {
			if (tutorTimes == nextTutorTimes && tutorID == TUTOR_NULL) {
				int tutorInterval = 100;
				if (preTutorID == TUTOR_NULL) {
					setTutor(TUTOR_BOAT_MOVE);
					nextTutorTimes += tutorInterval;
				} else if (preTutorID == TUTOR_BOAT_MOVE) {
					setTutor(TUTOR_HOOK);
					nextTutorTimes += tutorInterval;
				} else if (preTutorID == TUTOR_HOOK) {
					setTutor(TUTOR_SENSOR);
					nextTutorTimes += tutorInterval;
				} else if (preTutorID == TUTOR_SENSOR) {
					setTutor(TUTOR_SENSOR_BOAT_MOVE);
					nextTutorTimes += tutorInterval;
				} else if (preTutorID == TUTOR_SENSOR_BOAT_MOVE) {
					setTutor(TUTOR_DIVER_CONTROL);
					nextTutorTimes += tutorInterval;
				} else if (preTutorID == TUTOR_DIVER_CONTROL) {
					setTutor(TUTOR_DIVER_MOVE);
					nextTutorTimes += tutorInterval;
				} else if (preTutorID == TUTOR_DIVER_MOVE) {
					setTutor(TUTOR_SENSOR_DIVER);
					nextTutorTimes += tutorInterval;
				} else if (preTutorID == TUTOR_SENSOR_DIVER) {
					setTutor(TUTOR_SENSOR_DIVER_MOVE);
					nextTutorTimes += tutorInterval;
				} else if (preTutorID == TUTOR_SENSOR_DIVER_MOVE) {
					setTutor(TUTOR_MAGNET);
					nextTutorTimes += tutorInterval;
				} else if (preTutorID == TUTOR_MAGNET) {
					int step = font.getHeight() / 10;
					if (step == 0) {
						step = 1;
					}
					scrollText = new ScrollText("�ܼ򵥰ɣ����ڷ�����Ϸ����ʼ�����񺣵��ɣ�", MESSAGE_WIDTH, MESSAGE_HEIGHT, fontH, step, font);
					setTutor(TUTOR_OVER);
					nextTutorTimes += tutorInterval;
				}
			}
			tutorTimes++;
		} else if (state == STATE_TUTOR) {
			curTutorTimes++;
		}
	}
	public void tutorHandleEvent() {
		if (message != null) {
			return;
		}
		if (state != STATE_TUTOR) {
			return;
		}
		// TODO
		if (curTutorTimes > 40) {
			sensorY = 11;
		}
		int px = Common.pointerX - realDx;
		int py = Common.pointerY - realDy;
		switch (tutorID) {
			case TUTOR_BOAT_MOVE :
				if (Common.isPointerPressed()) {
					if (Tool.isPointInRect(px, py, tutorRectX, tutorRectY, tutorRectW, tutorRectH)) {
						Common.pointerPressed(px, py);
						state = STATE_GAME;
						tutorID = TUTOR_NULL;
					}
				}
				break;
			case TUTOR_HOOK :
				if (Common.isPointerPressed()) {
					if (Tool.isPointInRect(px, py, tutorRectX, tutorRectY, tutorRectW, tutorRectH)) {
						Common.pointerPressed(px, py);
						state = STATE_GAME;
						tutorID = TUTOR_NULL;
					}
				}
				break;
			case TUTOR_SENSOR :
				if (Common.isPointerReleased()) {
					if (Tool.isPointInRect(px, py, pad4X, pad4Y, pad4W, pad4H)) {
						// Common.pointerReleased(px, py);
						bSensorAble = !bSensorAble;
						state = STATE_GAME;
						tutorID = TUTOR_NULL;
						bPad4Jump = false;
					}
				}
				break;
			case TUTOR_SENSOR_BOAT_MOVE :
				if (curTutorTimes > 40) {
					if (sensorY > 10 || sensorY < -10) {
						state = STATE_GAME;
						tutorID = TUTOR_NULL;
					}
				}
				break;
			case TUTOR_DIVER_CONTROL :
				if (Common.isPointerReleased()) {
					if (Tool.isPointInRect(px, py, pad3X, pad3Y, pad3W, pad3H)) {
						// Common.pointerReleased(px, py);
						diverID = (byte) employShopIndex;
						diver = new Diver(diverID);
						normalGameDiveID = diverID;
						isHasControl = true;
						bSensorAble = false;
						state = STATE_GAME;
						tutorID = TUTOR_NULL;
						bPad3Jump = false;
						Common.keyPressed(Common.KEY_NUM0);
					}
				}
				break;
			case TUTOR_DIVER_MOVE :
				if (Common.isPointerPressed()) {
					if (Tool.isPointInRect(px, py, tutorRectX, tutorRectY, tutorRectW, tutorRectH)) {
						Common.pointerPressed(px, py);
						state = STATE_GAME;
						tutorID = TUTOR_NULL;
					}
				}
				break;
			case TUTOR_SENSOR_DIVER :
				if (Common.isPointerReleased()) {
					if (Tool.isPointInRect(px, py, pad4X, pad4Y, pad4W, pad4H)) {
						// Common.pointerReleased(px, py);
						bSensorAble = !bSensorAble;
						state = STATE_GAME;
						tutorID = TUTOR_NULL;
						bPad4Jump = false;
					}
				}
				break;
			case TUTOR_SENSOR_DIVER_MOVE :
				if (curTutorTimes > 40) {
					if (sensorY > 10 || sensorY < -10 || sensorX > 10 || sensorX < -10) {
						state = STATE_GAME;
						tutorID = TUTOR_NULL;
					}
				}
				break;
			case TUTOR_MAGNET :
				if (Common.isPointerReleased()) {
					if (Tool.isPointInRect(px, py, pad2X, pad2Y, pad2W, pad2H)) {
						// Common.pointerReleased(px, py);
						state = STATE_GAME;
						tutorID = TUTOR_NULL;
						bPad2Jump = false;
						Common.keyPressed(Common.KEY_STAR);
					}
				}
				break;
			case TUTOR_OVER :
				if (Common.isPointerReleased()) {
					if (Tool.isPointInRect(px, py, MESSAGE_RECT_X, MESSAGE_RECT_Y, MESSAGE_RECT_WIDTH, MESSAGE_RECT_HEIGHT)) {
						state = STATE_GAME;
						tutorID = TUTOR_NULL;
						tutorOver();
						// showHintMessage("��Ϸ��ѧ��ϣ������龫�����д�����");
					}
				}
				break;
		}
	}
	public void drawTutorHint(Graphics g, Image img) {
		int hintX, hintY;
		int hintW = tutorBgImg.getWidth();
		int hintH = tutorBgImg.getHeight();
		int hintDrawType;
		if (fishman.mapX > MAP_WIDTH / 2) {
			hintX = fishman.mapX - 30 - hintW;
			hintY = 0; // fishman.mapY - hintH;
			hintDrawType = Tool.TRANS_NONE;
		} else {
			hintX = fishman.mapX + 35;
			hintY = 0; // fishman.mapY - hintH;
			hintDrawType = Tool.TRANS_MIRROR;
		}
		Tool.drawImage(g, tutorBgImg, hintX, hintY, hintDrawType);
		Tool.drawImage(g, img, hintX + ((hintW - img.getWidth()) >> 1), hintY + ((hintH - img.getHeight()) >> 1), Tool.TRANS_NONE);
	}
	public void drawTutor(Graphics g) {
		drawGame(g);
		Image img;
		int dy = Tool.JUMP_RANGE[(Tool.countTimes >> 1) % Tool.JUMP_RANGE.length];
		switch (tutorID) {
			case TUTOR_BOAT_MOVE :
				img = tutorImgs[0];
				// Tool.drawImage(g, tutorBgImg, (uiWidth - tutorBgImg.getWidth())
				// >> 1, ((uiHeight - tutorBgImg.getHeight()) >> 1) + dy,
				// Tool.TRANS_NONE);
				// Tool.drawImage(g, img, (uiWidth - img.getWidth()) >> 1,
				// ((uiHeight - img.getHeight()) >> 1) + dy, Tool.TRANS_NONE);
				drawTutorHint(g, img);
				Common.setUIClip(g);
				tutorRectX = 55;
				tutorRectY = 21;
				tutorRectW = uiWidth - tutorRectX * 2;
				tutorRectH = FISHMAN_TOUCH_Y - tutorRectY - 10; // 141;
				if (Tool.countTimes % 8 < 4) {
					g.setColor(0xff0000);
				} else {
					g.setColor(0xffff00);
				}
				// g.drawArc(tutorRectX, tutorRectY, tutorRectW, tutorRectH, 0,
				// 360);
				// g.drawArc(tutorRectX + 1, tutorRectY + 1, tutorRectW - 2,
				// tutorRectH - 2, 0, 360);
				g.drawRoundRect(tutorRectX, tutorRectY, tutorRectW, tutorRectH, 60, 60);
				g.drawRoundRect(tutorRectX + 1, tutorRectY + 1, tutorRectW - 2, tutorRectH - 2, 58, 58);
				g.drawRoundRect(tutorRectX + 2, tutorRectY + 2, tutorRectW - 4, tutorRectH - 4, 56, 56);
				break;
			case TUTOR_HOOK :
				img = tutorImgs[1];
				// Tool.drawImage(g, tutorBgImg, (uiWidth - tutorBgImg.getWidth())
				// >> 1, ((uiHeight - tutorBgImg.getHeight()) >> 1) + dy,
				// Tool.TRANS_NONE);
				// Tool.drawImage(g, img, (uiWidth - img.getWidth()) >> 1,
				// ((uiHeight - img.getHeight()) >> 1) + dy, Tool.TRANS_NONE);
				drawTutorHint(g, img);
				Common.setUIClip(g);
				tutorRectX = 55;
				tutorRectY = FISHMAN_TOUCH_Y;
				tutorRectW = uiWidth - tutorRectX * 2;
				tutorRectH = uiHeight - FISHMAN_TOUCH_Y - 60; // 130;
				if (Tool.countTimes % 8 < 4) {
					g.setColor(0xff0000);
				} else {
					g.setColor(0xffff00);
				}
				// g.drawArc(tutorRectX, tutorRectY, tutorRectW, tutorRectH, 0,
				// 360);
				// g.drawArc(tutorRectX + 1, tutorRectY + 1, tutorRectW - 2,
				// tutorRectH - 2, 0, 360);
				g.drawRoundRect(tutorRectX, tutorRectY, tutorRectW, tutorRectH, 60, 60);
				g.drawRoundRect(tutorRectX + 1, tutorRectY + 1, tutorRectW - 2, tutorRectH - 2, 58, 58);
				g.drawRoundRect(tutorRectX + 2, tutorRectY + 2, tutorRectW - 4, tutorRectH - 4, 56, 56);
				break;
			case TUTOR_SENSOR :
				img = tutorImgs[2];
				// Tool.drawImage(g, tutorBgImg, (uiWidth - tutorBgImg.getWidth())
				// >> 1, ((uiHeight - tutorBgImg.getHeight()) >> 1) + dy,
				// Tool.TRANS_NONE);
				// Tool.drawImage(g, img, (uiWidth - img.getWidth()) >> 1,
				// ((uiHeight - img.getHeight()) >> 1) + dy, Tool.TRANS_NONE);
				drawTutorHint(g, img);
				bPad4Jump = true;
				if (Tool.countTimes % 16 < 8) {
					Tool.drawImage(g, mapPointerImg, pad4X + ((pad4W - mapPointerImg.getWidth()) >> 1), pad4Y - mapPointerImg.getHeight() - 5, Tool.TRANS_NONE);
				}
				break;
			case TUTOR_SENSOR_BOAT_MOVE :
				img = tutorImgs[3];
				// Tool.drawImage(g, tutorBgImg, (uiWidth - tutorBgImg.getWidth())
				// >> 1, ((uiHeight - tutorBgImg.getHeight()) >> 1) + dy,
				// Tool.TRANS_NONE);
				// Tool.drawImage(g, img, (uiWidth - img.getWidth()) >> 1,
				// ((uiHeight - img.getHeight()) >> 1) + dy, Tool.TRANS_NONE);
				drawTutorHint(g, img);
				break;
			case TUTOR_DIVER_CONTROL :
				img = tutorImgs[4];
				// Tool.drawImage(g, tutorBgImg, (uiWidth - tutorBgImg.getWidth())
				// >> 1, ((uiHeight - tutorBgImg.getHeight()) >> 1) + dy,
				// Tool.TRANS_NONE);
				// Tool.drawImage(g, img, (uiWidth - img.getWidth()) >> 1,
				// ((uiHeight - img.getHeight()) >> 1) + dy, Tool.TRANS_NONE);
				drawTutorHint(g, img);
				bPad3Jump = true;
				if (Tool.countTimes % 16 < 8) {
					Tool.drawImage(g, mapPointerImg, pad3X + ((pad3W - mapPointerImg.getWidth()) >> 1), pad3Y - mapPointerImg.getHeight() - 5, Tool.TRANS_NONE);
				}
				break;
			case TUTOR_DIVER_MOVE :
				img = tutorImgs[5];
				// Tool.drawImage(g, tutorBgImg, (uiWidth - tutorBgImg.getWidth())
				// >> 1, ((uiHeight - tutorBgImg.getHeight()) >> 1) + dy,
				// Tool.TRANS_NONE);
				// Tool.drawImage(g, img, (uiWidth - img.getWidth()) >> 1,
				// ((uiHeight - img.getHeight()) >> 1) + dy, Tool.TRANS_NONE);
				drawTutorHint(g, img);
				Common.setUIClip(g);
				tutorRectX = 30;
				tutorRectY = FISHMAN_TOUCH_Y;
				tutorRectW = uiWidth - tutorRectX * 2; // 580;
				tutorRectH = uiHeight - FISHMAN_TOUCH_Y - 60; // 130;
				if (Tool.countTimes % 8 < 4) {
					g.setColor(0xff0000);
				} else {
					g.setColor(0xffff00);
				}
				// g.drawArc(tutorRectX, tutorRectY, tutorRectW, tutorRectH, 0,
				// 360);
				// g.drawArc(tutorRectX + 1, tutorRectY + 1, tutorRectW - 2,
				// tutorRectH - 2, 0, 360);
				g.drawRoundRect(tutorRectX, tutorRectY, tutorRectW, tutorRectH, 60, 60);
				g.drawRoundRect(tutorRectX + 1, tutorRectY + 1, tutorRectW - 2, tutorRectH - 2, 58, 58);
				g.drawRoundRect(tutorRectX + 2, tutorRectY + 2, tutorRectW - 4, tutorRectH - 4, 56, 56);
				break;
			case TUTOR_SENSOR_DIVER :
				img = tutorImgs[2];
				// Tool.drawImage(g, tutorBgImg, (uiWidth - tutorBgImg.getWidth())
				// >> 1, ((uiHeight - tutorBgImg.getHeight()) >> 1) + dy,
				// Tool.TRANS_NONE);
				// Tool.drawImage(g, img, (uiWidth - img.getWidth()) >> 1,
				// ((uiHeight - img.getHeight()) >> 1) + dy, Tool.TRANS_NONE);
				drawTutorHint(g, img);
				bPad4Jump = true;
				if (Tool.countTimes % 16 < 8) {
					Tool.drawImage(g, mapPointerImg, pad4X + ((pad4W - mapPointerImg.getWidth()) >> 1), pad4Y - mapPointerImg.getHeight() - 5, Tool.TRANS_NONE);
				}
				break;
			case TUTOR_SENSOR_DIVER_MOVE :
				img = tutorImgs[6];
				// Tool.drawImage(g, tutorBgImg, (uiWidth - tutorBgImg.getWidth())
				// >> 1, ((uiHeight - tutorBgImg.getHeight()) >> 1) + dy,
				// Tool.TRANS_NONE);
				// Tool.drawImage(g, img, (uiWidth - img.getWidth()) >> 1,
				// ((uiHeight - img.getHeight()) >> 1) + dy, Tool.TRANS_NONE);
				drawTutorHint(g, img);
				break;
			case TUTOR_MAGNET :
				img = tutorImgs[7];
				// Tool.drawImage(g, tutorBgImg, (uiWidth - tutorBgImg.getWidth())
				// >> 1, ((uiHeight - tutorBgImg.getHeight()) >> 1) + dy,
				// Tool.TRANS_NONE);
				// Tool.drawImage(g, img, (uiWidth - img.getWidth()) >> 1,
				// ((uiHeight - img.getHeight()) >> 1) + dy, Tool.TRANS_NONE);
				drawTutorHint(g, img);
				bPad2Jump = true;
				if (Tool.countTimes % 16 < 8) {
					Tool.drawImage(g, mapPointerImg, pad2X + ((pad2W - mapPointerImg.getWidth()) >> 1), pad2Y - mapPointerImg.getHeight() - 5, Tool.TRANS_NONE);
				}
				break;
			case TUTOR_OVER :
				drawMessage(g, scrollText, true);
				break;
			default :
				break;
		}
	}
	// 0219
	// Image sysMenuGiftImg;
	boolean bGiftHintShown0, bGiftHintShown1;
	boolean bBuyGift;
	Image giftHintImg;
	public static final String STR_GIFT_BOUGHT = "��ֵ����ѹ����뷵�أ�";
	public static final String STR_GIFT_ALREADY_PART = "��ֵ������ֹ����ѿ������뷵�أ�";
	public static final String STR_GIFT_ALREADY_ALL = "��ֵ��������ѿ������뷵�أ�";
	public static final String STR_GIFT_DISABLE = "��ǰ�ؿ�����ʹ�ó�ֵ������뷵�أ�";
	// public void gotoGiftHint(int type) {
	// //#if (MobileType == 6101)||(MobileType == 7260)
	// //#else
	// if (stageID == 10) {
	// return;
	// }
	// if (bBuyGift) {
	// return;
	// }
	// if (boatID == 3 || diverID >= 0) {
	// return;
	// }
	// if (type == 0) {
	// if (bGiftHintShown0) {
	// return;
	// }
	// bGiftHintShown0 = true;
	// } else {
	// if (bGiftHintShown1) {
	// return;
	// }
	// bGiftHintShown1 = true;
	// }
	//
	// giftHintImg = Tool.createImage("/giftHint.png");
	//
	// clearButtonBubbleSet();
	// state = STATE_GIFT_HINT;
	// //#endif
	// }
	// ���׻���ǽ�Ľ��ͼ������
	private int youmi_X = 380;
	private int youmi_Y = 325;
	public void drawImageHint(Graphics g, Image hintImg) {
		if (hintImg == null) {
			return;
		}
		drawGame(g);
		// Common.setUIClip(g);
		// Tool.fillAlphaRect(g, 0xAA000000, 0, 0, uiWidth, uiHeight);
		// int ix = (uiWidth - hintImg.getWidth()) >> 1;
		// int iy = (uiHeight - hintImg.getHeight()) >> 1;
		// //Tool.drawImageRect(g, mRectImg, 0, iy - 10, uiWidth,
		// hintImg.getHeight() + 20, 0x01B7F0);
		// fillBgRect(g, 10, iy - 10, uiWidth - 20, hintImg.getHeight() + 20);
		// Tool.drawImage(g, hintImg, ix, iy, Tool.TRANS_NONE);
		//
		// drawButton(g, btnOkImg, BUTTON_ALIGN_LEFT);
		// drawButton(g, btnBackImg, BUTTON_ALIGN_RIGHT);
		Common.setUIClip(g);
		int rw = 280;
		int rh = 320;
		int rx = (uiWidth - rw) >> 1;
		int ry = (uiHeight - rh) >> 1;
		fillBgRect(g, rx, ry, rw, rh);
		int ix = rx + ((rw - hintImg.getWidth()) >> 1);
		int iy = ry + ((rh - hintImg.getHeight()) >> 1);
		Tool.drawImage(g, hintImg, ix, iy, Tool.TRANS_NONE);
		buttonLeftX = rx - buttonWidth;
		buttonLeftY = uiHeight - buttonHeight;
		buttonRightX = rx + rw;
		buttonRightY = uiHeight - buttonHeight;
		drawButtonBubbleSet(g);
		drawButton(g, btnOkImg, buttonLeftX, buttonLeftY);
		drawButton(g, btnBackImg, buttonRightX, buttonRightY);
	}
	// public void giftHintHandleKey() {
	// //#if (MobileType == 6101)||(MobileType == 7260)
	// //#else
	// if (Common.isKeyPressed(Common.SOFT_LAST_PRESSED, true)) {
	// giftHintImg = null;
	// state = STATE_GAME;
	// } else if (isOkPressed() ||
	// Common.isKeyPressed(Common.SOFT_FIRST_PRESSED, true)) {
	// //giftHintImg = null;
	// //sysMenuIndex = 0;
	// //state = STATE_SYSTEM_MENU;
	// //gotoSystemMenu();
	// gotoSmsUI(SMS_BUY_GIFT);
	// }
	// //#endif
	// }
	Image magnetHintImg;
	boolean bMagnetHintShown;
	boolean bBuyMagnet;
	public void gotoMagnetHint(boolean bProp) {
		// #if (MobileType == 6101)||(MobileType == 7260)
		// #else
		if (stageID == 10) {
			return;
		}
		if (bProp && bBuyMagnet) { // 110715 if (bBuyMagnet)
			return;
		}
		if (bProp) {
			if (bMagnetHintShown) {
				return;
			}
			bMagnetHintShown = true;
		}
		magnetHintImg = Tool.createImage("/magnetHint.png");
		clearButtonBubbleSet();
		state = STATE_MAGNET_HINT;
		// #endif
	}
	public void magnetHintHandleKey() {
		// #if (MobileType == 6101)||(MobileType == 7260)
		// #else
		if (Common.isKeyPressed(Common.SOFT_LAST_PRESSED, true)) {
			magnetHintImg = null;
			state = STATE_GAME;
		} else if (isOkPressed() || Common.isKeyPressed(Common.SOFT_FIRST_PRESSED, true)) {
			// gotoSmsUI(SMS_BUY_MAGNET);
			int price = 1000;
			if (gold >= price) {
				allFishToScoreNum += 10;
				gold -= price;
				saveRecord();
				showMessage(STR_BUY_SUCCESS);
			} else {
				showMessage(STR_GOLD_LACK);
			}
		}
		// #endif
	}
	public static int FISHMAN_TOUCH_Y = 170;
	Image pad0Img, pad1Img, pad2Img, pad3Img, pad3aImg, pad4Img, pad4aImg, pad6Img; // pad5Img, , padXImg
	int pad0X, pad0Y, pad0W, pad0H;
	int pad1X, pad1Y, pad1W, pad1H;
	int pad2X, pad2Y, pad2W, pad2H;
	int pad3X, pad3Y, pad3W, pad3H;
	int pad4X, pad4Y, pad4W, pad4H;
	// �ý�Ǯ��������
	private int pad_X;
	private int pad_Y;
	// int pad5X, pad5Y, pad5W, pad5H;
	int pad6X, pad6Y, pad6W, pad6H;
	int padEdgeY;
	boolean bPad0Jump, bPad1Jump, bPad2Jump, bPad3Jump, bPad4Jump, bPad5Jump, bPad6Jump;
	boolean bPadLock;
	// int padDirW, padDirH;
	// int padLeftX, padLeftY;
	// int padRightX, padRightY;
	Image handImg;
	int handW, handH;
	int[] shopItemX = new int[BOAT_NUM];
	int[] shopItemY = new int[BOAT_NUM];
	int[] shopItemW = new int[BOAT_NUM];
	int[] shopItemH = new int[BOAT_NUM];
	int[] employShopItemX = new int[EMPLOY_SHOP_NUM];
	int[] employShopItemY = new int[EMPLOY_SHOP_NUM];
	int[] employShopItemW = new int[EMPLOY_SHOP_NUM];
	int[] employShopItemH = new int[EMPLOY_SHOP_NUM];
	static String ResDir;
	boolean bSpeedUp;
	// 20120214
	int realWidth;
	int realHeight;
	int realDx;
	int realDy;
	Image frameBg0Img, frameBg1Img, frameTopImg, frameBottomImg, frameLRImg, frameStarImg;
	private static Handler handler;
	public FishGame() {
		handler = new Handler();
		mInterstitialAd = new DomobInterstitialAd(ActivityMIDletBridge.s_instance, PUBLISHER_ID, DomobInterstitialAd.INTERSITIAL_SIZE_300X250);
		mInterstitialAd.setInterstitialAdListener(new DomobInterstitialAdListener() {
			@Override
			public void onInterstitialAdReady() {
				Log.i("DomobSDKDemo", "onAdReady");
			}
			@Override
			public void onLandingPageOpen() {
				Log.i("DomobSDKDemo", "onLandingPageOpen");
			}
			@Override
			public void onLandingPageClose() {
				Log.i("DomobSDKDemo", "onLandingPageClose");
			}
			@Override
			public void onInterstitialAdPresent() {
				Log.i("DomobSDKDemo", "onInterstitialAdPresent");
			}
			@Override
			public void onInterstitialAdDismiss() {
				// ����һ�����ر�ʱ,������һ����档
				mInterstitialAd.loadInterstitialAd();
				gotoSummary();
				Log.i("DomobSDKDemo", "onInterstitialAdDismiss");
			}
			@Override
			public void onInterstitialAdFailed(ErrorCode arg0) {
				// TODO Auto-generated method stub
				loadNUM++;
				if (loadNUM > 5) {
					gotoSummary();
				}
				Log.i("DomobSDKDemo", "onInterstitialAdFailed");
			}
			@Override
			public void onInterstitialAdLeaveApplication() {
				// TODO Auto-generated method stub
				Log.i("DomobSDKDemo", "onInterstitialAdLeaveApplication");
			}
		});
		mInterstitialAd.loadInterstitialAd();
		// SCREEN_WIDTH = getResources().getDisplayMetrics().widthPixels;
		// //getWidth();
		// SCREEN_HEIGHT = getResources().getDisplayMetrics().heightPixels;
		realWidth = SCREEN_WIDTH = getResources().getDisplayMetrics().widthPixels;
		realHeight = SCREEN_HEIGHT = getResources().getDisplayMetrics().heightPixels;
		// realWidth = SCREEN_WIDTH = 800;
		// realHeight = SCREEN_HEIGHT = 480;
		realDx = 0;
		realDy = 0;
		// System.out.println("SCREEN_WIDTH ========== " + SCREEN_WIDTH);
		// System.out.println("SCREEN_HEIGHT ========== " + SCREEN_HEIGHT);
		if (SCREEN_WIDTH > 854) {
			SCREEN_WIDTH = 854;
			realDx = (realWidth - SCREEN_WIDTH) >> 1;
		}
		if (SCREEN_HEIGHT > 480) {
			SCREEN_HEIGHT = 480;
			realDy = (realHeight - SCREEN_HEIGHT) >> 1;
		}
		if (realDx != 0 || realDy != 0) {
			frameBg0Img = Tool.createImage("frame/frameBg0.png");
			frameBg1Img = Tool.createImage("frame/frameBg1.png");
			frameTopImg = Tool.createImage("frame/frameTop.png");
			frameBottomImg = Tool.createImage("frame/frameBottom.png");
			frameLRImg = Tool.createImage("frame/frameLR.png");
			frameStarImg = Tool.createImage("frame/frameStar.png");
		}
		MAP_WIDTH = SCREEN_WIDTH;
		MAP_HEIGHT = SCREEN_HEIGHT;
		FISHMAN_POSX_INIT = SCREEN_WIDTH / 2;
		font = Common.largeFont;
		if (SCREEN_HEIGHT == 480) {
			ResDir = "480800";
			WATER_FLOAT_Y = 170; // 130
			FISHMAN_POSY_INIT = 160;
			BUCKET_Y = 150;
			FISH_Y_MIN = FISHMAN_POSY_INIT + 20; // 180
			FISH_Y_MAX = MAP_HEIGHT - 70; // 410
			FISHMAN_TOUCH_Y = FISHMAN_POSY_INIT + 50; // 210
			DIVER_MIN_Y = FISHMAN_POSY_INIT + 30; // 190;
			DIVER_MAX_Y = MAP_HEIGHT - 60; // 420;
			DIVER_AUTO_MIN_Y = FISHMAN_POSY_INIT + 30; // 190;
			DIVER_AUTO_MAX_Y = MAP_HEIGHT - 80; // 400;
			SCORE_NUM_FLY_Y = 12;
			GOLD_NUM_FLY_Y = 12;
			// ˮͰ
			pad1W = 55;
			pad1H = 40;
			pad1X = 0;
			pad1Y = 125;
			// ����
			// int[] handupYs = {135, 120, 100, 110, 115};
			int[] handupYs = {FISHMAN_POSY_INIT - 25, FISHMAN_POSY_INIT - 40, FISHMAN_POSY_INIT - 60, FISHMAN_POSY_INIT - 50, FISHMAN_POSY_INIT - 45};
			fishHandupYs = handupYs;
			Fishman.HOOK_MOVE_DISTANCE = MAP_HEIGHT - 135;
			PropsBubble.PROPS_FADE_Y = FISHMAN_POSY_INIT - 20; // 140
			menuItemOffX = 50;
			mapPartOffX = 15;
			mapPartOffY = 60;
			padEdgeY = 5;
			font = Common.largeFont;
			if (SCREEN_WIDTH == 800) {
				mainMenuDx = -54;
				mainMenuDy = 0;
				MainMenuX[0] = 572 + mainMenuDx;
				MainMenuY[0] = 14 + mainMenuDy;
				MainMenuX[1] = 712 + mainMenuDx;
				MainMenuY[1] = 41 + mainMenuDy;
				MainMenuX[2] = 624 + mainMenuDx;
				MainMenuY[2] = 143 + mainMenuDy;
				MainMenuX[3] = 720 + mainMenuDx;
				MainMenuY[3] = 214 + mainMenuDy;
				MainMenuX[4] = 612 + mainMenuDx;
				MainMenuY[4] = 280 + mainMenuDy;
				MainMenuX[5] = 715 + mainMenuDx;
				MainMenuY[5] = 362 + mainMenuDy;
				qqBarDx = 20;
				qqBarDy = 420;
				mainMenuBubbleX = 650;
				mainMenuBubbleWidth = 110;
				mainMenuBubbleDX = -54;
				GOLD_NUM_FLY_X = 123; // 192;
				SCORE_NUM_FLY_X = 240; // 216;
				youmi_X = 365;
				youmi_Y = 325;
			} else if (SCREEN_WIDTH == 854) {
				mainMenuDx = 0;
				mainMenuDy = 0;
				MainMenuX[0] = 572 + mainMenuDx;
				MainMenuY[0] = 14 + mainMenuDy;
				MainMenuX[1] = 712 + mainMenuDx;
				MainMenuY[1] = 41 + mainMenuDy;
				MainMenuX[2] = 624 + mainMenuDx;
				MainMenuY[2] = 143 + mainMenuDy;
				MainMenuX[3] = 720 + mainMenuDx;
				MainMenuY[3] = 214 + mainMenuDy;
				MainMenuX[4] = 612 + mainMenuDx;
				MainMenuY[4] = 280 + mainMenuDy;
				MainMenuX[5] = 715 + mainMenuDx;
				MainMenuY[5] = 362 + mainMenuDy;
				qqBarDx = 20;
				qqBarDy = 420;
				mainMenuBubbleX = 650;
				mainMenuBubbleWidth = 110;
				mainMenuBubbleDX = 0;
				GOLD_NUM_FLY_X = 150; // 192;
				SCORE_NUM_FLY_X = 267; // 216;
				youmi_X = 380;
				youmi_Y = 325;
			}
		} else if (SCREEN_HEIGHT == 320) {
			ResDir = "320480";
			WATER_FLOAT_Y = 127; // 130
			FISHMAN_POSY_INIT = 117;
			BUCKET_Y = 107;
			FISH_Y_MIN = FISHMAN_POSY_INIT + 20; // 180
			FISH_Y_MAX = MAP_HEIGHT - 70; // 410
			FISHMAN_TOUCH_Y = FISHMAN_POSY_INIT + 50; // 210
			DIVER_MIN_Y = FISHMAN_POSY_INIT + 30; // 190;
			DIVER_MAX_Y = MAP_HEIGHT - 60; // 420;
			DIVER_AUTO_MIN_Y = FISHMAN_POSY_INIT + 30; // 190;
			DIVER_AUTO_MAX_Y = MAP_HEIGHT - 80; // 400;
			SCORE_NUM_FLY_Y = 8;
			GOLD_NUM_FLY_Y = 8;
			// ˮͰ
			pad1W = 55;
			pad1H = 40;
			pad1X = 0;
			pad1Y = 84;
			// ����
			int[] handupYs = {FISHMAN_POSY_INIT - 25, FISHMAN_POSY_INIT - 40, FISHMAN_POSY_INIT - 60, FISHMAN_POSY_INIT - 50, FISHMAN_POSY_INIT - 45};
			fishHandupYs = handupYs;
			Fishman.HOOK_MOVE_DISTANCE = MAP_HEIGHT - 95;
			PropsBubble.PROPS_FADE_Y = FISHMAN_POSY_INIT - 20;
			menuItemOffX = 10;
			mapPartOffX = 1;
			mapPartOffY = 40;
			padEdgeY = 2;
			font = Common.smallFont;
			if (SCREEN_WIDTH == 480) {
				mainMenuDx = 0;
				mainMenuDy = 0;
				MainMenuX[0] = 290 + mainMenuDx;
				MainMenuY[0] = 7 + mainMenuDy;
				MainMenuX[1] = 382 + mainMenuDx;
				MainMenuY[1] = 26 + mainMenuDy;
				MainMenuX[2] = 323 + mainMenuDx;
				MainMenuY[2] = 92 + mainMenuDy;
				MainMenuX[3] = 388 + mainMenuDx;
				MainMenuY[3] = 140 + mainMenuDy;
				MainMenuX[4] = 315 + mainMenuDx;
				MainMenuY[4] = 185 + mainMenuDy;
				MainMenuX[5] = 383 + mainMenuDx;
				MainMenuY[5] = 237 + mainMenuDy;
				qqBarDx = 5;
				qqBarDy = 265;
				mainMenuBubbleX = 340;
				mainMenuBubbleWidth = 90;
				mainMenuBubbleDX = 0;
				GOLD_NUM_FLY_X = 60;
				SCORE_NUM_FLY_X = 152;
			}
			youmi_X = 196;
			youmi_Y = 246;
		}
		fontH = font.getHeight();
		MESSAGE_X = (SCREEN_WIDTH - MESSAGE_WIDTH) >> 1;
		MESSAGE_Y = ((SCREEN_HEIGHT - MESSAGE_HEIGHT) >> 1) - 20;
		MESSAGE_RECT_X = (SCREEN_WIDTH - MESSAGE_RECT_WIDTH) >> 1;
		MESSAGE_RECT_Y = ((SCREEN_HEIGHT - MESSAGE_RECT_HEIGHT) >> 1) - 20;
		Tool.init();
		setFullScreenMode(true);
		instance = this;
		// Common.setDisplay(dp, this);
		uiWidth = SCREEN_WIDTH;
		uiHeight = SCREEN_HEIGHT;
		Common.initUIBounds(0, 0, uiWidth, uiHeight);
		// initSensor();
		loadRecord();
		logoImg = Tool.createImage("/logo.png");
		QQlogoImg = Tool.createImage("/ctlogo.png");
		bubbleImg = Tool.createImage("/bubble.png");
		logoTimes = 0;
		state = STATE_LOGO;
		// state = STATE_LOGO;
		// 20120412 free
		isOpenGame = true;
	}
	public Image loadResImage(String str) {
		str = Tool.processFileName(str);
		Image img = Tool.createImage(ResDir + "/" + str);
		if (img == null) {
			img = Tool.createImage(str);
		}
		return img;
	}
	public boolean loadRecord() {
		boolean bHasRecord = false;
		RecordStore db = null;
		ByteArrayInputStream bin = null;
		DataInputStream din = null;
		try {
			db = RecordStore.openRecordStore("fishrec", true);
			if (db.getNumRecords() > 0) {
				bHasRecord = true;
				byte[] data = db.getRecord(1);
				bin = new ByteArrayInputStream(data);
				din = new DataInputStream(bin);
				fishmanID = din.readInt();
				boatID = din.readInt();
				stageOpenID = din.readInt();
				// whb gold
				normalGameGold = din.readInt();
				liveGameGold = din.readInt();
				normalGameDiveID = din.readByte();
				// whb add ���߹���
				// noSharkToolNum = din.readInt();
				isHasControl = din.readBoolean();
				// whb add Ů��ɫ����״̬��¼��ȡ
				isOpenGame = din.readBoolean();
				// whb add ��Ϸ��ѧ��¼���ͽ�ѧһ��
				isFirstGame = din.readBoolean();
				// whb add ��������ģʽ
				isLiveGameOpened = din.readBoolean();
				// whb add ����ģʽ�У����Ӽ�¼�ؿ�
				liveGameStageID = din.readInt();
				// whb add ����ɽ�
				// whb add ����ɽ�
				normalGameAllFishToScoreNum = din.readInt();
				liveGameAllFishToScoreNum = din.readInt();
				// whb add ����ģʽ�µĵ���
				liveGameBoatID = din.readInt();
				liveGameDiverID = din.readByte();
				if (isLiveGameOpened) {
					isShowLiveGame = false;
				} else {
					isShowLiveGame = true;
				}
				// 0219
				bBuyGift = din.readBoolean();
				bBuyMagnet = din.readBoolean();
				// 0709
				for (int i = 0; i < highScores.length; i++) {
					highScores[i] = din.readInt();
				}
				for (int i = 0; i < medalNums.length; i++) {
					medalNums[i] = din.readInt();
				}
				// 110817
				updateGameScore = din.readInt();
				updateFishNum = updateFishNumBak = din.readInt();
				updateGold = updateGoldBak = din.readInt();
				// 110818
				for (int i = 0; i < bAchGeted.length; i++) {
					bAchGeted[i] = din.readBoolean();
				}
			}
		} catch (Exception e) {
			Tool.reportException(e);
		} finally {
			try {
				if (din != null) {
					din.close();
				}
			} catch (Exception e) {
				Tool.reportException(e);
			}
			try {
				if (bin != null) {
					bin.close();
				}
			} catch (Exception e) {
				Tool.reportException(e);
			}
			// #if (Series!=L7)
			try {
				if (db != null) {
					db.closeRecordStore();
				}
			} catch (Exception e) {
				Tool.reportException(e);
			}
			// #endif
		}
		return bHasRecord;
	}
	public void saveRecord() {
		RecordStore db = null;
		ByteArrayOutputStream bout = null;
		DataOutputStream dout = null;
		try {
			db = RecordStore.openRecordStore("fishrec", true);
			bout = new ByteArrayOutputStream();
			dout = new DataOutputStream(bout);
			dout.writeInt(fishmanID);
			dout.writeInt(boatID);
			dout.writeInt(stageOpenID);
			// whb gold
			if (stageID == 11) {
				liveGameGold = gold;
			} else {
				normalGameGold = gold;
			}
			dout.writeInt(normalGameGold);
			dout.writeInt(liveGameGold);
			dout.writeByte(normalGameDiveID);
			// whb add ���߹���
			// dout.writeInt(noSharkToolNum);
			dout.writeBoolean(isHasControl);
			// whb add ����״̬��¼
			dout.writeBoolean(isOpenGame);
			// whb add ��Ϸ��ѧ��¼���ͽ�ѧһ��
			dout.writeBoolean(isFirstGame);
			// whb add ��������ģʽ
			dout.writeBoolean(isLiveGameOpened);
			// whb add ����ģʽ�У����Ӽ�¼�ؿ�
			dout.writeInt(liveGameStageID);
			// whb add ħ�ô���
			if (stageID == 11) {
				liveGameAllFishToScoreNum = allFishToScoreNum;
			} else {
				normalGameAllFishToScoreNum = allFishToScoreNum;
			}
			dout.writeInt(normalGameAllFishToScoreNum);
			dout.writeInt(liveGameAllFishToScoreNum);
			// whb add ����ģʽ�µĵ���
			if (boatID > liveGameBoatID) {
				liveGameBoatID = boatID;
			}
			if (diverID > liveGameDiverID) {
				liveGameDiverID = diverID;
			}
			dout.writeInt(liveGameBoatID);
			dout.writeByte(liveGameDiverID);
			// 0219
			dout.writeBoolean(bBuyGift);
			dout.writeBoolean(bBuyMagnet);
			// 0709
			for (int i = 0; i < highScores.length; i++) {
				dout.writeInt(highScores[i]);
			}
			for (int i = 0; i < medalNums.length; i++) {
				dout.writeInt(medalNums[i]);
			}
			// 110817
			dout.writeInt(updateGameScore);
			dout.writeInt(updateFishNum);
			dout.writeInt(updateGold);
			// 110818
			for (int i = 0; i < bAchGeted.length; i++) {
				dout.writeBoolean(bAchGeted[i]);
			}
			dout.flush();
			bout.flush();
			byte[] data = bout.toByteArray();
			if (db.getNumRecords() > 0) {
				db.setRecord(1, data, 0, data.length);
			} else {
				db.addRecord(data, 0, data.length);
			}
		} catch (Exception e) {
			Tool.reportException(e);
		} finally {
			try {
				if (dout != null) {
					dout.close();
				}
			} catch (Exception e) {
				Tool.reportException(e);
			}
			try {
				if (bout != null) {
					bout.close();
				}
			} catch (Exception e) {
				Tool.reportException(e);
			}
			// #if (Series!=L7)
			try {
				if (db != null) {
					db.closeRecordStore();
				}
			} catch (Exception e) {
				Tool.reportException(e);
			}
			// #endif
		}
	}
	public void gotoCover() {
		hintMessage = null;
		if (coverImg == null) {
			// if (SCREEN_WIDTH == 800) {
			// coverImg = Tool.createImage("/cover_800.png");
			// } else {
			// coverImg = Tool.createImage("/cover.png");
			// }
			coverImg = loadResImage("/cover.png");
			coverX = (uiWidth - coverImg.getWidth()) >> 1;
			coverY = (uiHeight - coverImg.getHeight()) >> 1;
		}
		// if (menuImg == null) {
		// menuImg = Tool.createImage("/menu.png");
		// }
		// if (itemBg0Img == null) {
		// itemBg0Img = Tool.createImage("/itembg0.png");
		// }
		// if (itemBg1Img == null) {
		// itemBg1Img = Tool.createImage("/itembg1.png");
		// }
		// if (itemBg2Img == null) {
		// itemBg2Img = Tool.createImage("/itembg2.png");
		// }
		// if (QQMenu == null) {
		// QQMenu = Tool.createImage("/qqmenu.png");
		// }
		// if (mainMenuBgImg == null) {
		// mainMenuBgImg = Tool.createImage("/mainMenuBg.png");
		// }
		if (numFontImg1 == null) {
			numFontImg1 = Tool.createImage("/numFont1.png");
		}
		if (numFontImg2 == null) {
			numFontImg2 = numFontImg1;
			// numFontImg2 = Tool.createImage("/numFont2.png");
		}
		if (numFontImgSmall == null) {
			numFontImgSmall = Tool.createImage("/numFont2.png");
		}
		if (btnOkImg == null) {
			btnOkImg = loadResImage("/btnOk.png");
		}
		if (btnBackImg == null) {
			btnBackImg = loadResImage("/btnBack.png");
		}
		if (btnMenuImg == null) {
			btnMenuImg = loadResImage("/btnMenu.png");
		}
		if (btnBuyImg == null) {
			btnBuyImg = loadResImage("/btnBuy.png");
		}
		if (btnShopImg == null) {
			btnShopImg = loadResImage("/btnShop.png");
		}
		if (btnMapImg == null) {
			btnMapImg = loadResImage("/btnMap.png");
		}
		if (btnYoumiImg == null) {
			btnYoumiImg = loadResImage("/youmi.png");
		}
		if (btnIntoImg == null) {
			btnIntoImg = loadResImage("/btnInto.png");
		}
		if (btnReplayImg == null) {
			btnReplayImg = loadResImage("/btnReplay.png");
		}
		// if (onoffImg == null) {
		// onoffImg = Tool.createImage("/onoff.png");
		// }
		// if (mRectImg == null) {
		// mRectImg = Tool.createImage("/mRect.png");
		// }
		if (rectTopImg == null) {
			rectTopImg = Tool.createImage("/rectTop.png");
		}
		if (rectRoundImg == null) {
			rectRoundImg = Tool.createImage("/rectRound.png");
		}
		if (btnEmployImg == null) {
			btnEmployImg = loadResImage("/btnEmploy.png");
		}
		if (handImg == null) {
			handImg = Tool.createImage("/hand.png");
			handW = handImg.getWidth() >> 1;
			handH = handImg.getHeight();
		}
		if (mapPointerImg == null) {
			mapPointerImg = Tool.createImage("/mapPointer.png");
		}
		startMusic("/m0.mid");
		coverTimes = 0;
		coverMenuX = uiWidth;
		mainMenuIndex = -1;
		state = STATE_COVER;
	}
	public void gotoMainMenu() {
		// if (menuBgImg == null) {
		// menuBgImg = Tool.createImage("/menubg.png");
		// }
		// if (coverImg == null) {
		// coverImg = Tool.createImage("/cover.png");
		// }
		// if (menuImg == null) {
		// menuImg = Tool.createImage("/menu.png");
		// }
		// if (itemBg0Img == null) {
		// itemBg0Img = Tool.createImage("/itembg0.png");
		// }
		// if (itemBg1Img == null) {
		// itemBg1Img = Tool.createImage("/itembg1.png");
		// }
		// if (itemBg2Img == null) {
		// itemBg2Img = Tool.createImage("/itembg2.png");
		// }
		// if (QQMenu == null) {
		// QQMenu = Tool.createImage("/qqmenu.png");
		// }
		// if (mainMenuBgImg == null) {
		// mainMenuBgImg = Tool.createImage("/mainMenuBg.png");
		// }
		menuItemImgs = new Image[MAIN_MENU_NUM];
		menuItemXs = new int[MAIN_MENU_NUM];
		menuItemYs = new int[MAIN_MENU_NUM];
		menuItemWs = new int[MAIN_MENU_NUM];
		menuItemHs = new int[MAIN_MENU_NUM];
		for (int i = 0; i < MAIN_MENU_NUM; i++) {
			menuItemXs[i] = MainMenuX[i];
			menuItemYs[i] = MainMenuY[i];
		}
		menuItemImgs[0] = loadResImage("/m_new.png");
		menuItemWs[0] = btnBg1Img.getWidth();
		menuItemHs[0] = btnBg1Img.getHeight();
		menuItemImgs[1] = loadResImage("/m_continue.png");
		menuItemWs[1] = btnBgImg.getWidth();
		menuItemHs[1] = btnBgImg.getHeight();
		menuItemImgs[2] = loadResImage("/m_mode.png");
		menuItemWs[2] = btnBgImg.getWidth();
		menuItemHs[2] = btnBgImg.getHeight();
		if (bSoundOn) {
			menuItemImgs[3] = loadResImage("/m_soundOn.png");
		} else {
			menuItemImgs[3] = loadResImage("/m_soundOff.png");
		}
		menuItemWs[3] = btnBgImg.getWidth();
		menuItemHs[3] = btnBgImg.getHeight();
		menuItemImgs[4] = loadResImage("/m_helpAbout.png");
		menuItemWs[4] = btnBgImg.getWidth();
		menuItemHs[4] = btnBgImg.getHeight();
		menuItemImgs[5] = loadResImage("/m_exit.png");
		menuItemWs[5] = btnBg1Img.getWidth();
		menuItemHs[5] = btnBg1Img.getHeight();
		// xFu 0815 QQ GameCenter
		// qqBarImg = Tool.createImage("/qqBar.png");
		//
		// menuItemImgs[6] = Tool.createImage("/moregame.png");
		// menuItemXs[6] = qqBarDx;
		// menuItemYs[6] = qqBarDy;
		// menuItemWs[6] = menuItemImgs[6].getWidth();
		// menuItemHs[6] = menuItemImgs[6].getHeight();
		//
		// menuItemImgs[7] = Tool.createImage("/qq1.png");
		// menuItemXs[7] = qqBarDx + ((qqBarImg.getWidth() -
		// menuItemImgs[7].getWidth()) >> 1);
		// menuItemYs[7] = qqBarDy + 1;
		// menuItemWs[7] = menuItemImgs[7].getWidth();
		// menuItemHs[7] = menuItemImgs[7].getHeight();
		//
		// menuItemImgs[8] = Tool.createImage("/qq2.png");
		// menuItemXs[8] = qqBarDx + qqBarImg.getWidth() -
		// menuItemImgs[8].getWidth() - 20;
		// menuItemYs[8] = qqBarDy + 1;
		// menuItemWs[8] = menuItemImgs[8].getWidth();
		// menuItemHs[8] = menuItemImgs[8].getHeight();
		mainMenuIndex = 0;
		state = STATE_MAIN_MENU;
	}
	public void freeMenuRes() {
		menuItemImgs = null;
		menuItemXs = null;
		menuItemYs = null;
		menuItemWs = null;
		menuItemHs = null;
	}
	public void gotoSystemMenu() {
		menuItemImgs = new Image[SYSTEM_MENU_NUM];
		menuItemXs = new int[SYSTEM_MENU_NUM];
		menuItemYs = new int[SYSTEM_MENU_NUM];
		menuItemWs = new int[SYSTEM_MENU_NUM];
		menuItemHs = new int[SYSTEM_MENU_NUM];
		// menuItemImgs[0] = loadResImage("/m_gift.png");
		menuItemImgs[0] = loadResImage("/m_continue.png");
		if (bSoundOn) {
			menuItemImgs[1] = loadResImage("/m_soundOn.png");
		} else {
			menuItemImgs[1] = loadResImage("/m_soundOff.png");
		}
		menuItemImgs[2] = loadResImage("/m_help.png");
		menuItemImgs[3] = loadResImage("/m_mainMenu.png");
		int itemW = btnBgImg.getWidth();
		int itemH = btnBgImg.getHeight();
		int itemX = (uiWidth - itemW * SYSTEM_MENU_NUM - menuItemOffX * (SYSTEM_MENU_NUM - 1)) >> 1;
		int itemY = (uiHeight - itemH) >> 1;
		for (int i = 0; i < SYSTEM_MENU_NUM; i++) {
			menuItemXs[i] = itemX + (itemW + menuItemOffX) * i;
			menuItemYs[i] = itemY;
			menuItemWs[i] = itemW;
			menuItemHs[i] = itemH;
		}
		sysMenuIndex = 0;
		state = STATE_SYSTEM_MENU;
	}
	public void gotoMapMenu() {
		menuItemImgs = new Image[MAP_MENU_NUM];
		menuItemXs = new int[MAP_MENU_NUM];
		menuItemYs = new int[MAP_MENU_NUM];
		menuItemWs = new int[MAP_MENU_NUM];
		menuItemHs = new int[MAP_MENU_NUM];
		menuItemImgs[0] = loadResImage("/m_continue.png");
		if (bSoundOn) {
			menuItemImgs[1] = loadResImage("/m_soundOn.png");
		} else {
			menuItemImgs[1] = loadResImage("/m_soundOff.png");
		}
		menuItemImgs[2] = loadResImage("/m_help.png");
		menuItemImgs[3] = loadResImage("/m_mainMenu.png");
		int itemW = btnBgImg.getWidth();
		int itemH = btnBgImg.getHeight();
		int itemX = (uiWidth - itemW * MAP_MENU_NUM - menuItemOffX * (MAP_MENU_NUM - 1)) >> 1;
		int itemY = (uiHeight - itemH) >> 1;
		for (int i = 0; i < MAP_MENU_NUM; i++) {
			menuItemXs[i] = itemX + (itemW + menuItemOffX) * i;
			menuItemYs[i] = itemY;
			menuItemWs[i] = itemW;
			menuItemHs[i] = itemH;
		}
		sysMenuIndex = 0;
		state = STATE_MAP_MENU;
	}
	public void gotoPlayerChoose(boolean bGame) {
		// tipImg = null;
		allFishToScoreNum = ALL_FISH_NUM_INIT;
		gold = 0;
		normalGameGold = 0;
		normalGameAllFishToScoreNum = ALL_FISH_NUM_INIT;
		// if (menuBgImg == null) {
		// menuBgImg = Tool.createImage("/menubg.png");
		// }
		playerChooseImg = Tool.createImage("/playerChoose.png");
		playerHeadImgs = new Image[PLAYER_NUM];
		for (int i = 0; i < PLAYER_NUM; i++) {
			playerHeadImgs[i] = Tool.createImage("/player" + i + ".png");
		}
		nanshengImg = Tool.createImage("/nansheng.png");
		nvshengImg = Tool.createImage("/nvsheng.png");
		bAccessToGame = bGame;
		playerIndex = 0;
		state = STATE_PLAYER_CHOOSE;
	}
	public void freeShopRes() {
		shopImg = null;
		goldGetImg = null;
		boughtImg = null;
		if (boatInfoImgs != null) {
			for (int i = 0; i < boatInfoImgs.length; i++) {
				for (int j = 0; j < boatInfoImgs[i].length; j++) {
					boatInfoImgs[i][j] = null;
				}
			}
			boatInfoImgs = null;
		}
		infoTextImg = null;
	}
	public void gotoMap() {
		// ��շ�����Դ
		coverImg = null;
		freeMenuRes();
		bgImg = null;
		playerChooseImg = null;
		if (playerHeadImgs != null) {
			for (int i = 0; i < PLAYER_NUM; i++) {
				playerHeadImgs[i] = null;
			}
			playerHeadImgs = null;
		}
		// ����̵���Դ
		freeShopRes();
		mapPartImgs = new Image[STAGE_NUM];
		for (int i = 0; i < STAGE_NUM; i++) {
			mapPartImgs[i] = Tool.createImage("/map" + i + ".png");
		}
		mapRectImg = Tool.createImage("/mapRect.png");
		mapFocusImg = Tool.createImage("/mapFocus.png");
		mapLockImg = Tool.createImage("/mapLock.png");
		medal0Img = Tool.createImage("/medal0.png");
		medal1Img = Tool.createImage("/medal1.png");
		mapPartIndex = stageOpenID;
		state = STATE_MAP;
	}
	public void gotoFirstTutorOver() {
		mapPartImgs = new Image[STAGE_NUM];
		for (int i = 0; i < STAGE_NUM; i++) {
			mapPartImgs[i] = Tool.createImage("/map" + i + ".png");
		}
		mapRectImg = Tool.createImage("/mapRect.png");
		mapFocusImg = Tool.createImage("/mapFocus.png");
		mapLockImg = Tool.createImage("/mapLock.png");
		medal0Img = Tool.createImage("/medal0.png");
		medal1Img = Tool.createImage("/medal1.png");
		stageOpenID = -1;
		mapPartIndex = -1;
		showConfirm("��ϲ����ɽ�ѧ��������Ļ������һ�泡��");
		state = STATE_FIRST_TUTOR_OVER;
	}
	// public void gotoShop() {
	// // ��յ�ͼ��Դ
	// //mapBgImg = null;
	// mapPointerImg = null;
	// if (mapPartImgs != null) {
	// for (int i = 0; i < STAGE_NUM; i++) {
	// mapPartImgs[i] = null;
	// }
	// mapPartImgs = null;
	// }
	// mapRectImg = null;
	// mapFocusImg = null;
	// mapLockImg = null;
	//
	// // �����̵���Դ
	// if (barImg == null) {
	// barImg = Tool.createImage("/bar.png");
	// }
	// shopImg = Tool.createImage("/shop.png");
	// goldGetImg = Tool.createImage("/goldGet.png");
	// boughtImg = Tool.createImage("/bought.png");
	// boatInfoImgs = new Image[BOAT_NUM][2];
	// for (int i = 0; i < boatInfoImgs.length; i++) {
	// for (int j = 0; j < boatInfoImgs[i].length; j++) {
	// boatInfoImgs[i][j] = Tool.createImage("/boatInfo" + i + "" + j + ".png");
	// }
	// }
	//
	// infoTextImg = Tool.createImage("/boatInfo02.png");
	//
	// shopIndex = 0;
	// state = STATE_SHOP;
	// }
	//
	// /**
	// * ����320��Ļ�߰汾�̵�
	// *
	// * @param g
	// */
	// public void drawShop240(Graphics g) {
	// if (shopImg == null) {
	// return;
	// }
	// Tool.drawImage(g, shopImg, 0, 0, Tool.TRANS_NONE);
	//
	// int barX = 10;
	// int barY = 5;
	// //Tool.drawImage(g, barImg, barX, barY, Tool.TRANS_NONE);
	//
	// // ���ƽ��
	// int goldX = barX + 8;
	// int goldY = barY + ((barImg.getHeight() - goldGetImg.getHeight()) >> 1);
	// Tool.drawImage(g, goldGetImg, goldX, goldY, Tool.TRANS_NONE);
	//
	// int numX = goldX + goldGetImg.getWidth() + 3;
	// int numY = barY + ((barImg.getHeight() - numFontImg1.getHeight()) >> 1);
	// Tool.drawImageNum(g, numFontImg1, String.valueOf(gold), numX, numY,
	// numFontImg1.getWidth() / 10, 0);
	//
	// // �����洬
	// int infoPanelH = 0;
	// //int infoTextH = 50;
	// //int infoSpace = 10;
	// int infoTextH = 45;
	// int infoSpace = 5;
	// for (int i = 0; i < BOAT_NUM; i++) {
	// infoPanelH += boatInfoImgs[i][0].getHeight();
	// infoPanelH += infoSpace;
	// }
	// infoPanelH += infoTextH + infoSpace;
	// //int infoPanelX = 30;
	// int infoPanelX = 20;
	//
	// int infoPanelY = barImg.getHeight() + ((uiHeight - barImg.getHeight() -
	// btnBgImg.getHeight() - infoPanelH) >> 1);
	// int infoX = infoPanelX;
	// int infoY = infoPanelY;
	//
	// // ����ѡ�е����沿��
	// int i = 0;
	// //
	// // // whb �˵���������
	// // if (shopDir == 0) {
	// // if (shopIndex > 3) {
	// // i = shopIndex - 3;
	// // oldShopIndex = i;
	// // }
	// // } else {
	// // i = oldShopIndex;
	// // if (i > shopIndex) {
	// // i = shopIndex;
	// // oldShopIndex = i;
	// // }
	// // }
	// // if (shopIndex == 0) {
	// // oldShopIndex = 0;
	// // }
	//
	// for (; i <= shopIndex; i++) {
	// Image[][] img = null;
	// int index = i;
	// // whb fix
	// if (index < 4) {// ����ֻ
	// img = boatInfoImgs;
	// index = i;
	// }
	// shopItemX[index] = infoX;
	// shopItemY[index] = infoY;
	// shopItemW[index] = img[index][0].getWidth();
	// shopItemH[index] = img[index][0].getHeight();
	//
	// if (shopIndex < 7) {
	// // ��˸
	// if (i == shopIndex && (Tool.countTimes % 4) < 2) {
	// Tool.drawImage(g, img[index][1], infoX, infoY, Tool.TRANS_NONE);
	// } else {
	// Tool.drawImage(g, img[index][0], infoX, infoY, Tool.TRANS_NONE);
	// }
	//
	// // �����־
	// if (i <= boatID) {
	// Tool.drawImage(g, boughtImg, infoX + img[index][0].getWidth() -
	// boughtImg.getWidth() - 10, infoY + ((img[index][0].getHeight() -
	// boughtImg.getHeight()) >> 1), Tool.TRANS_NONE);
	// }
	// }
	// // �ۼ�����
	// infoY += (img[index][0].getHeight() + infoSpace);
	// }
	//
	// Tool.fillAlphaRect(g, 0x88000000, 4, infoY, 260, infoTextH);
	// int infoTextX = infoX + 4;
	// int infoTextY = infoY + ((infoTextH - infoTextImg.getHeight()) >> 1);
	// Tool.drawImage(g, infoTextImg, infoTextX, infoTextY, Tool.TRANS_NONE);
	//
	// infoY += infoTextH + infoSpace;
	//
	// // �����°벿��
	// for (i = shopIndex + 1; i < 4; i++) {
	//
	// Image[][] img = null;
	// int index = i;
	// // whb fix
	// if (index < 4) {// ����ֻ
	// img = boatInfoImgs;
	// index = i;
	// } else if (index < 7 && index >= 4) { // ǱˮԱ
	// img = diverInfoImgs;
	// index = i - 4;
	// }
	//
	// shopItemX[index] = infoX;
	// shopItemY[index] = infoY;
	// shopItemW[index] = img[index][0].getWidth();
	// shopItemH[index] = img[index][0].getHeight();
	//
	// Tool.drawImage(g, img[index][0], infoX, infoY, Tool.TRANS_NONE);
	// if (i <= boatID) {
	// Tool.drawImage(g, boughtImg, infoX + img[index][0].getWidth() -
	// boughtImg.getWidth() - 10, infoY + ((img[index][0].getHeight() -
	// boughtImg.getHeight()) >> 1), Tool.TRANS_NONE);
	// }
	// infoY += (img[index][0].getHeight() + infoSpace);
	// }
	// //
	// Image img = null;
	// // if (shopIndex > 3 && shopIndex < 7) {
	// // img = btnEmployImg;
	// // } else {
	// img = btnBuyImg;
	// // }
	// //Tool.drawImage(g, img, 0, uiHeight - btnBuyImg.getHeight(),
	// Tool.TRANS_NONE);
	// drawButton(g, img, BUTTON_ALIGN_LEFT);
	// drawButton(g, btnMapImg, BUTTON_ALIGN_RIGHT);
	// //drawButtonBack(g);
	// }
	public void gotoShop() { // 110717
		goldGetImg = Tool.createImage("/goldGet.png");
		boughtImg = Tool.createImage("/bought.png");
		boatInfoImgs = new Image[BOAT_NUM][2];
		for (int i = 0; i < boatInfoImgs.length; i++) {
			for (int j = 0; j < boatInfoImgs[i].length; j++) {
				boatInfoImgs[i][j] = Tool.createImage("/boatInfo" + i + "" + j + ".png");
			}
		}
		infoTextImg = Tool.createImage("/boatInfo02.png");
		clearButtonBubbleSet();
		shopIndex = 0;
		state = STATE_SHOP;
	}
	/**
	 * ����320��Ļ�߰汾�̵�
	 * 
	 * @param g
	 */
	public void drawShop240(Graphics g) {
		drawGame(g);
		Common.setUIClip(g);
		int rw = 240;
		int rh = 320;
		int rx = (uiWidth - rw) >> 1;
		int ry = (uiHeight - rh) >> 1;
		fillBgRect(g, rx, ry, rw, rh);
		int barX = rx + 5;
		int barY = ry + 5;
		// ���ƽ��
		int goldX = barX + 8;
		int goldY = barY + ((BAR_HEIGHT - goldGetImg.getHeight()));
		Tool.drawImage(g, goldGetImg, goldX, goldY, Tool.TRANS_NONE);
		int numX = goldX + goldGetImg.getWidth() + 3;
		int numY = barY + ((BAR_HEIGHT - numFontImg1.getHeight()));
		Tool.drawImageNum(g, numFontImg1, String.valueOf(gold), numX, numY, numFontImg1.getWidth() / 10, 0);
		// �����洬
		int infoPanelH = 0;
		// int infoTextH = 50;
		// int infoSpace = 10;
		int infoTextH = 45;
		int infoSpace = 5;
		for (int i = 0; i < BOAT_NUM; i++) {
			infoPanelH += boatInfoImgs[i][0].getHeight();
			infoPanelH += infoSpace;
		}
		infoPanelH += infoTextH + infoSpace;
		// int infoPanelX = 30;
		int infoPanelX = rx + 10;
		int infoPanelY = ry + BAR_HEIGHT + ((rh - BAR_HEIGHT - infoPanelH) >> 1); // barImg.getHeight() +
																					// ((uiHeight -
																					// barImg.getHeight()
																					// -
																					// btnBgImg.getHeight()
																					// - infoPanelH) >>
																					// 1);
		int infoX = infoPanelX;
		int infoY = infoPanelY;
		// ����ѡ�е����沿��
		int i = 0;
		//
		// // whb �˵���������
		// if (shopDir == 0) {
		// if (shopIndex > 3) {
		// i = shopIndex - 3;
		// oldShopIndex = i;
		// }
		// } else {
		// i = oldShopIndex;
		// if (i > shopIndex) {
		// i = shopIndex;
		// oldShopIndex = i;
		// }
		// }
		// if (shopIndex == 0) {
		// oldShopIndex = 0;
		// }
		for (; i <= shopIndex; i++) {
			Image[][] img = boatInfoImgs;
			int index = i;
			shopItemX[index] = infoX;
			shopItemY[index] = infoY;
			shopItemW[index] = img[index][0].getWidth();
			shopItemH[index] = img[index][0].getHeight();
			if (shopIndex < 7) {
				// ��˸
				if (i == shopIndex && (Tool.countTimes % 4) < 2) {
					Tool.drawImage(g, img[index][1], infoX, infoY, Tool.TRANS_NONE);
				} else {
					Tool.drawImage(g, img[index][0], infoX, infoY, Tool.TRANS_NONE);
				}
				// �����־
				if (i <= boatID) {
					Tool.drawImage(g, boughtImg, infoX + img[index][0].getWidth() - boughtImg.getWidth() - 10, infoY + ((img[index][0].getHeight() - boughtImg.getHeight()) >> 1), Tool.TRANS_NONE);
				}
			}
			// �ۼ�����
			infoY += (img[index][0].getHeight() + infoSpace);
		}
		Tool.fillAlphaRect(g, 0x88000000, rx + 4, infoY, rw - 8, infoTextH);
		int infoTextX = rx + ((rw - infoTextImg.getWidth()) >> 1); // infoX + 4;
		int infoTextY = infoY + ((infoTextH - infoTextImg.getHeight()) >> 1);
		Tool.drawImage(g, infoTextImg, infoTextX, infoTextY, Tool.TRANS_NONE);
		infoY += infoTextH + infoSpace;
		// �����°벿��
		for (i = shopIndex + 1; i < 4; i++) {
			Image[][] img = null;
			int index = i;
			// whb fix
			if (index < 4) {// ����ֻ
				img = boatInfoImgs;
				index = i;
			} else if (index < 7 && index >= 4) { // ǱˮԱ
				img = diverInfoImgs;
				index = i - 4;
			}
			shopItemX[index] = infoX;
			shopItemY[index] = infoY;
			shopItemW[index] = img[index][0].getWidth();
			shopItemH[index] = img[index][0].getHeight();
			Tool.drawImage(g, img[index][0], infoX, infoY, Tool.TRANS_NONE);
			if (i <= boatID) {
				Tool.drawImage(g, boughtImg, infoX + img[index][0].getWidth() - boughtImg.getWidth() - 10, infoY + ((img[index][0].getHeight() - boughtImg.getHeight()) >> 1), Tool.TRANS_NONE);
			}
			infoY += (img[index][0].getHeight() + infoSpace);
		}
		// drawButton(g, btnBuyImg, BUTTON_ALIGN_LEFT);
		// drawButton(g, btnMapImg, BUTTON_ALIGN_RIGHT);
		buttonLeftX = rx - buttonWidth;
		buttonLeftY = uiHeight - buttonHeight;
		buttonRightX = rx + rw;
		buttonRightY = uiHeight - buttonHeight;
		drawButtonBubbleSet(g);
		drawButton(g, btnBuyImg, buttonLeftX, buttonLeftY);
		drawButton(g, btnBackImg, buttonRightX, buttonRightY);
	}
	/*
	 * //#if (MobileType == N97) //# boolean isSizeChanged = false; //# int
	 * sizew; //# //# protected void sizeChanged(int arg0, int arg1) { //# sizew
	 * = arg0; //# if (arg0 < 350) { //# isSizeChanged = true; //# } else { //#
	 * isSizeChanged = false; //# } //# } //# //#endif
	 */
	public void run() {
		long curTime, sleepTime;
		while (DoMidlet.bRunning) {
			if (!isShown()) {
				continue;
			}
			// if (scriptState == SCRIPT_FINISHED) {
			// setScriptState(SCRIPT_NULL);
			// }
			// if (scriptState == SCRIPT_NULL) {
			// if (scriptFileToRun != null) { //���нű�
			// System.out.println("Run Script: " + scriptFileToRun);
			// if (script == null) {
			// script = new Interpreter(instance);
			// }
			// scriptFileRunning = scriptFileToRun;
			// scriptFileToRun = null;
			// script.startScript("/script/" + scriptFileRunning);
			// continue;
			// }
			// }
			curTime = System.currentTimeMillis();
			try {
				/*
				 * //#if (MobileType == N97) //# if (isSizeChanged) { //#
				 * repaint(); //# continue; //# } //#endif
				 */
				tutorHandleEvent();
				cycleTutor();
				handlePointerEvent();
				handleSensor();
				handleKeyPressed();
				handleKeyReleased();
				Tool.cycle();
				cycle();
				// #if (KeyCodeType==touch)
				// # cycleMoveBoat();
				// #endif
				repaint();
				serviceRepaints();
			} catch (Throwable ex) {
				ex.printStackTrace();
			} finally {
				sleepTime = MILLIS_PRE_UPDATE - (System.currentTimeMillis() - curTime);
				if (sleepTime < 5) {
					sleepTime = 5;
				}
				try {
					if (bSpeedUp) {
						Thread.sleep(1);
					} else {
						Thread.sleep(sleepTime);
					}
				} catch (Exception e) {
				}
			}
		}
		closeMusic();
		DoMidlet.instance.exit();
	}
	public void start() {
		if (thread == null) {
			thread = new Thread(this);
			thread.start();
		}
	}
	public void exit() {
		// closeMusic();
		DoMidlet.bRunning = false;
	}
	public void loadGameRes() {
		if (!bLoadGameRes) {
			bar0Img = loadResImage("/bar0.png");
			bar1Img = loadResImage("/bar1.png");
			bar2Img = loadResImage("/bar2.png");
			clockImg = loadResImage("/clock.png");
			String actionFile = "/spray.an";
			String[] imageFiles = new String[1];
			imageFiles[0] = "/spray.png";
			sprayAS = ActionSet.createActionSet(actionFile, imageFiles);
			stars0Img = Tool.createImage("/stars0.png");
			stars1Img = Tool.createImage("/stars1.png");
			bubbleImg = Tool.createImage("/bubble.png");
			bubble2Img = Tool.createImage("/bubble2.png");
			// sysMenuImg = Tool.createImage("/sysMenu.png");
			// sysMenuGiftImg = Tool.createImage("/sysMenuGift.png"); //0219
			// goldBarImg = Tool.createImage("/goldBar.png");
			// scoreImg = Tool.createImage("/score.png");
			// goalImg = Tool.createImage("/goal.png");
			banksideImg = Tool.createImage("/bankside.png");
			bucketImg = Tool.createImage("/bucket.png");
			handupImg = Tool.createImage("/handup.png");
			fishHandupImg = Tool.createImage("/fishHandup.png");
			handupHintImg = Tool.createImage("/handupHint.png");
			// skyImg = Tool.createImage("/sky0.png");
			// lightningImg = Tool.createImage("/lightning.png");
			propsImgs = new Image[PROPS_TYPE_NUM];
			for (int i = 0; i < PROPS_TYPE_NUM; i++) {
				propsImgs[i] = Tool.createImage("/props" + i + ".png");
			}
			propsFadeImg = Tool.createImage("/propsFade.png");
			fullFlagImg = Tool.createImage("/fullFlag.png");
			fullHintImgs = new Image[2];
			for (int i = 0; i < fullHintImgs.length; i++) {
				fullHintImgs[i] = Tool.createImage("/fullHint" + i + ".png");
			}
			foreObjImg[0] = Tool.createImage("/obj0.png");
			foreObjImg[1] = Tool.createImage("/obj1.png");
			foreObjMapX[0] = 70;
			foreObjMapX[1] = MAP_WIDTH - 70 - foreObjImg[1].getWidth();
			careImg = Tool.createImage("/care.png");
			// goldCoinImg = Tool.createImage("/goldCoin.png");
			numbFishImg1 = Tool.createImage("/fish91.png");
			numbFishImg2 = Tool.createImage("/fish92.png");
			fingerImg = Tool.createImage("/finger.png");
			fishboneImg = Tool.createImage("/fishbones.png");
			// controlTipImg = Tool.createImage("/controlTip.png");
			pad0Img = loadResImage("/button0.png");
			pad2Img = loadResImage("/button2.png");
			pad3Img = loadResImage("/button3.png");
			pad3aImg = loadResImage("/button3a.png");
			pad4Img = loadResImage("/button4.png");
			pad4aImg = loadResImage("/button4a.png");
			pad6Img = loadResImage("/button6.png");
			if (btnYoumiImg == null) {
				btnYoumiImg = loadResImage("/youmi.png");
			}
			// pad0W = pad0Img.getWidth();
			// pad0H = pad0Img.getHeight();
			// pad0X = uiWidth - pad0W;
			// pad0Y = 200; //uiHeight - pad0H;
			// pad0aW = pad0Img.getWidth();
			// pad0aH = pad0Img.getHeight();
			// pad0aX = 0;
			// pad0aY = 200; //uiHeight - pad0H;
			// �˵�
			pad0W = pad0Img.getWidth();
			pad0H = pad0Img.getHeight();
			if (uiWidth >= 800) {
				pad_X = 20;
				pad_Y = 0;
				pad0X = (uiWidth - pad0W) >> 1;
				pad0Y = uiHeight - pad0H - padEdgeY;
			} else {
				pad_X = -30;
				pad_Y = -10;
				pad0X = (uiWidth - pad0W - 80) >> 1;
				pad0Y = uiHeight - pad0H - padEdgeY;
			}
			int padDx = 22;
			int padDy = 0;
			// ��
			pad6W = pad6Img.getWidth();
			pad6H = pad6Img.getHeight();
			padDy = (pad0H - pad6H) >> 1;
			pad6X = pad0X - padDx - pad6W;
			pad6Y = uiHeight - pad6H - padEdgeY - padDy;
			// ����
			pad2W = pad2Img.getWidth();
			pad2H = pad2Img.getHeight();
			pad2X = pad6X - padDx - pad2W;
			pad2Y = uiHeight - pad2H - padEdgeY;
			// ǱˮԱ����
			pad3W = pad3Img.getWidth();
			pad3H = pad3Img.getHeight();
			padDy = (pad0H - pad3H) >> 1;
			pad3X = pad0X + pad0W + padDx;
			pad3Y = uiHeight - pad3H - padEdgeY - padDy;
			// ������Ӧ
			pad4W = pad4Img.getWidth();
			pad4H = pad4Img.getHeight();
			pad4X = pad3X + pad3W + padDx;
			pad4Y = uiHeight - pad4H - padEdgeY;
		}
		bLoadGameRes = true;
	}
	public void freeGameRes() {
		fishSet.removeAllElements();
		sprayAS = null;
		stars0Img = null;
		stars1Img = null;
		bubbleImg = null;
		// sysMenuImg = null;
		// goldBarImg = null;
		// scoreImg = null;
		// goalImg = null;
		clockImg = null;
		banksideImg = null;
		bucketImg = null;
		handupImg = null;
		fishHandupImg = null;
		// skyImg = Tool.createImage("/sky0.png");
		// lightningImg = Tool.createImage("/lightning.png");
		if (propsImgs != null) {
			for (int i = 0; i < PROPS_TYPE_NUM; i++) {
				propsImgs[i] = null;
			}
			propsImgs = null;
		}
		propsFadeImg = null;
		fullFlagImg = null;
		if (fullHintImgs != null) {
			for (int i = 0; i < fullHintImgs.length; i++) {
				fullHintImgs[i] = null;
			}
			fullHintImgs = null;
		}
		foreObjImg[0] = null;
		foreObjImg[1] = null;
		careImg = null;
		goldCoinImg = null;
		numbFishImg1 = null;
		numbFishImg2 = null;
		bgImg = null;
		waterBackImg = null;
		waterFrontImg = null;
		fishman = null;
		for (int i = 0; i < FISH_IMAGE_MAX; i++) {
			fishImgs[i] = null;
		}
		// whb add
		sharkImg = null;
		if (diver != null) {
			diver.freeDiverRes();
			diver = null;
		}
		bLoadGameRes = false;
	}
	public void gotoTimeOver() {
		beginImg = null;
		timeoverImg = Tool.createImage("/timeover.png");
		state = STATE_TIME_OVER;
	}
	// public void gotoSummary() {
	// timeoverImg = null;
	//
	// String str = null;
	//
	// calculateScore();// �������
	//
	// str = STR_STAGE_GOAL + goal;
	//
	// if (score >= goal) {
	// str += "\n" + STR_STAGE_SUCCESS;
	// str += "\n" + STR_ADD_GOLD;
	// gold += 50;
	//
	// if (stageID == stageOpenID) { // �����¹ؿ�
	// stageOpenID++;
	// if (stageOpenID >= STAGE_NUM) {
	// stageOpenID = STAGE_NUM - 1;
	// }
	// }
	// isGameFail = false;
	// } else {
	// str += "\n" + STR_STAGE_FAIL;
	// isGameFail = true;
	// }
	//
	// str += "\n";
	// str += "\n" + STR_STAGE_SCORE + score;
	// str += "\n" + STR_STAGE_PLAYERSCORE + playerScore;
	//
	// if (stageID == 10) {
	// str += "\n";
	// str += "\n" + STR_TEACH_OVER;
	// }
	//
	// scrollText = new ScrollText(str, uiWidth - 60, uiHeight * 2 / 3, fontH,
	// 2, font);
	//
	// saveRecord();
	// state = STATE_GAME_SUMMARY;
	// }
	int moveToX;
	int moveToY;
	public void moveTo(int x, int y) {
		if (diver == null || diver.actState != Diver.ACT_STATE_CATCH) {
			if (fishman.actState == Fishman.ACT_STATE_NORMAL) {
				moveToX = x;
				moveToY = y;
				fishman.act(Fishman.ACT_WALK);
			}
		} else {
			moveToX = x;
			moveToY = y;
			diver.actID = Diver.ACT_WALK;
		}
		// if (actState == ACT_STATE_NORMAL) {
		// moveToX = x;
		// moveToY = y;
		// act(Fishman.ACT_WALK);
		// }
	}
	public void clearMoveTo() {
		moveToX = -1;
		moveToY = -1;
	}
	// public void cycleMoveTo() {
	// if (moveToX < 0 || moveToY < 0) {
	// return;
	// }
	// if (diver == null || diver.actState != Diver.ACT_STATE_CATCH) {
	// if (fishman.mapX < moveToX) {
	// fishman.mapX += FISHMAN_STEP;
	// if (fishman.mapX > moveToX) {
	// fishman.mapX = moveToX;
	// fishman.act(Fishman.ACT_STAND);
	// }
	// } else if (fishman.mapX > moveToX) {
	// fishman.mapX -= FISHMAN_STEP;
	// if (fishman.mapX < moveToX) {
	// fishman.mapX = moveToX;
	// fishman.act(Fishman.ACT_STAND);
	// }
	// }
	// }
	//
	// }
	/**
	 * ������ѭ������
	 */
	public void cycleFishman() {
		Rectangle rect = fishman.getBoatEdgeRect();
		if (diver == null || diver.actState != Diver.ACT_STATE_CATCH) {
			if (fishman.actID == Fishman.ACT_WALK) {
				if (moveToX >= 0 && moveToY >= 0) {
					if (fishman.mapX < moveToX) {
						fishman.mapX += FISHMAN_STEP;
						fishman.setDir(MapElement.DIR_RIGHT);
						if (fishman.mapX > moveToX) {
							fishman.mapX = moveToX;
							fishman.act(Fishman.ACT_STAND);
						}
					} else if (fishman.mapX > moveToX) {
						fishman.mapX -= FISHMAN_STEP;
						fishman.setDir(MapElement.DIR_LEFT);
						if (fishman.mapX < moveToX) {
							fishman.mapX = moveToX;
							fishman.act(Fishman.ACT_STAND);
						}
					}
				} else {
					if (fishman.curDir == MapElement.DIR_LEFT) {
						fishman.mapX -= FISHMAN_STEP;
					} else if (fishman.curDir == MapElement.DIR_RIGHT) {
						fishman.mapX += FISHMAN_STEP;
					}
				}
				if (fishman.mapX + rect.x < BUCKET_WIDTH) {
					fishman.mapX = BUCKET_WIDTH - rect.x;
					fishman.act(Fishman.ACT_STAND);
				} else if (fishman.mapX + rect.x + rect.width > MAP_WIDTH) {
					fishman.mapX = MAP_WIDTH - rect.x - rect.width;
					fishman.act(Fishman.ACT_STAND);
				}
			}
		}
		if (fishman.actState == Fishman.ACT_STATE_NORMAL && fishman.mapX < BUCKET_WIDTH - rect.x + 15 && fishman.boatCurFishNum > 0) { // !(fishman.mapX >=
																																		// BUCKET_WIDTH - rect.x &&
																																		// fishman.mapX >=
																																		// BUCKET_WIDTH - rect.x +
																																		// 15)
			bHandupAble = true;
		} else {
			bHandupAble = false;
		}
		fishman.cycle();
		if (fishman.isBoatFull() && boatID < BOAT_NUM - 1) {
			showHintMessage(STR_BOAT_BUY_HINT);
		}
		// 110818
		if (Fishman.IsUpdateAchFishSuccessTime) {
			updateAchFishSuccessTime();
			Fishman.IsUpdateAchFishSuccessTime = false;
		}
	}
	void cycleDiver() {
		if (diver == null) {
			return;
		}
		diver.cycle();
		if (diver.actState == Diver.ACT_STATE_NORMAL) {
			// �������
			if (diver.mapX < viewMapX + Diver.space) {
				diver.curDir = MapElement.DIR_RIGHT;
			} else if (diver.mapX > viewMapX + FishGame.SCREEN_WIDTH - diver.tileWidth - Diver.space) {
				diver.curDir = MapElement.DIR_LEFT;
			}
			if (diver.mapY > DIVER_AUTO_MAX_Y) {
				diver.curDirV = MapElement.DIR_UP;
			} else if (diver.mapY < DIVER_AUTO_MIN_Y) {
				diver.curDirV = MapElement.DIR_DOWN;
			}
			// �ƶ�
			if (diver.curDir == MapElement.DIR_LEFT) {
				diver.mapX -= Diver.step;
				diver.bMirror = true;
			} else if (diver.curDir == MapElement.DIR_RIGHT) {
				diver.mapX += Diver.step;
				diver.bMirror = false;
			}
			if (diver.curDirV == MapElement.DIR_UP) {
				diver.mapY -= Diver.step;
			} else if (diver.curDirV == MapElement.DIR_DOWN) {
				diver.mapY += Diver.step;
			}
		} else if (diver.actState == Diver.ACT_STATE_CATCH && diver.actID == Diver.ACT_WALK) {
			if (moveToX >= 0 && moveToY >= 0) {
				int mx = moveToX - (diver.tileWidth >> 1);
				int my = moveToY - (diver.tileHeight >> 1);
				// if (diver.mapX < mx) {
				// diver.mapX += Diver.step;
				// diver.curDir = MapElement.DIR_RIGHT;
				// diver.bMirror = false;
				// if (diver.mapX > mx) {
				// diver.mapX = mx;
				// }
				// } else if (diver.mapX > mx) {
				// diver.mapX -= Diver.step;
				// diver.curDir = MapElement.DIR_LEFT;
				// diver.bMirror = true;
				// if (diver.mapX < mx) {
				// diver.mapX = mx;
				// }
				// }
				//
				// if (diver.mapY < my) {
				// diver.mapY += Diver.step;
				// diver.curDirV = MapElement.DIR_DOWN;
				// if (diver.mapY > my) {
				// diver.mapY = my;
				// }
				// } else if (diver.mapY > my) {
				// diver.mapY -= Diver.step;
				// diver.curDirV = MapElement.DIR_UP;
				// if (diver.mapY < my) {
				// diver.mapY = my;
				// }
				// }
				int dx = mx - diver.mapX;
				int dy = my - diver.mapY;
				int dis = (int) Math.sqrt(dx * dx + dy * dy);
				int stepX = 0;
				int stepY = 0;
				if (dis != 0) {
					stepX = Diver.step * dx / dis;
					stepY = Diver.step * dy / dis;
				}
				if (diver.mapX < mx) {
					diver.mapX += stepX;
					diver.curDir = MapElement.DIR_RIGHT;
					diver.bMirror = false;
					if (diver.mapX > mx) {
						diver.mapX = mx;
					}
				} else if (diver.mapX > mx) {
					diver.mapX += stepX;
					diver.curDir = MapElement.DIR_LEFT;
					diver.bMirror = true;
					if (diver.mapX < mx) {
						diver.mapX = mx;
					}
				}
				if (diver.mapY < my) {
					diver.mapY += stepY;
					diver.curDirV = MapElement.DIR_DOWN;
					if (diver.mapY > my) {
						diver.mapY = my;
					}
				} else if (diver.mapY > my) {
					diver.mapY += stepY;
					diver.curDirV = MapElement.DIR_UP;
					if (diver.mapY < my) {
						diver.mapY = my;
					}
				}
			} else {
				if (diver.curDir == MapElement.DIR_LEFT) {
					diver.mapX -= Diver.step;
					diver.bMirror = true;
				} else if (diver.curDir == MapElement.DIR_RIGHT) {
					diver.mapX += Diver.step;
					diver.bMirror = false;
				} else if (diver.curDir == MapElement.DIR_DOWN) {
					diver.mapY += Diver.step;
				} else if (diver.curDir == MapElement.DIR_UP) {
					diver.mapY -= Diver.step;
				}
			}
			if (diver.mapX < viewMapX + Diver.space) {
				diver.mapX = viewMapX + Diver.space;
			} else if (diver.mapX > viewMapX + SCREEN_WIDTH - diver.tileWidth - Diver.space) {
				diver.mapX = viewMapX + SCREEN_WIDTH - diver.tileWidth - Diver.space;
			}
			if (diver.mapY > DIVER_MAX_Y) {
				diver.mapY = DIVER_MAX_Y;
			} else if (diver.mapY < DIVER_MIN_Y) {
				diver.mapY = DIVER_MIN_Y;
			}
		}
		// ץ��
		for (int j = 0; j < fishSet.size(); j++) {
			// �������ǵ�������ģ��K�Ҳ��Ǳ�����ģ����ܴ��ڸ�ǱˮԱ����������Ҫ�Ե�����
			Fish fish = (Fish) fishSet.elementAt(j);
			if (stageID == 10) {
				if (fish.fishType != SHARK_TYPE//
						&& fish.fishType != 9 && fish.actState != Fish.ACT_STATE_HOOKED && (fish.fishType <= 9 || fish.fishType >= Fish.FISH_TYPE_GOLDCOIN)) {
					if (Tool.isRectIntersected(diver.mapX, diver.mapY, diver.tileWidth, diver.tileHeight,//
							fish.mapX, fish.mapY, fish.bodyWidth, fish.bodyHeight)) {
						fishSet.removeElementAt(j);
						FlyNum flyNum = new FlyNum(FlyNum.NUM_TYPE_SCORE, fish.score, numFontImg1, diver.mapX - viewMapX, diver.mapY - viewMapY, SCORE_NUM_FLY_X, SCORE_NUM_FLY_Y);
						flyNumSet.addElement(flyNum);
					}
				}
			} else {
				if (fish.fishType != SHARK_TYPE//
						&& fish.actState != Fish.ACT_STATE_HOOKED && (fish.fishType <= DIVER_FISH_SORT[diverID] || fish.fishType >= Fish.FISH_TYPE_GOLDCOIN)) {
					if (Tool.isRectIntersected(diver.mapX, diver.mapY, diver.tileWidth, diver.tileHeight,//
							fish.mapX, fish.mapY, fish.bodyWidth, fish.bodyHeight)) {
						fishSet.removeElementAt(j);
						FlyNum flyNum = new FlyNum(FlyNum.NUM_TYPE_SCORE, fish.score, numFontImg1, diver.mapX - viewMapX, diver.mapY - viewMapY, SCORE_NUM_FLY_X, SCORE_NUM_FLY_Y);
						flyNumSet.addElement(flyNum);
					}
				}
			}
		}
		if (propsBubble == null) {
			return;
		}
		if (propsBubble.actState == PropsBubble.ACT_STATE_NORMAL && fishman.actState == Fishman.ACT_STATE_NORMAL) {
			// Rectangle rect = fishman.getBoatBodyRect();
			// if (Tool.isRectIntersected(fishman.mapX + rect.x,
			// fishman.mapY +
			// rect.y, rect.width, rect.height, itemBubble.mapX,
			// itemBubble.mapY, itemBubble.tileWidth,
			// itemBubble.tileHeight)) {
			if (Tool.isPointInRect(propsBubble.mapX + (propsBubble.tileWidth >> 1), propsBubble.mapY + (propsBubble.tileHeight >> 1), diver.mapX, diver.mapY, diver.tileWidth, diver.tileHeight)) {
				propsBubble.gotoFade();
				getPropsBubble(propsBubble);
			}
		}
	}
	/**
	 * �ƶ���ͼ
	 */
	public void moveMap() {
		int fishmanCX = fishman.mapX + (fishman.footWidth >> 1);
		Rectangle rect = fishman.getBoatEdgeRect();
		int viewStep = 2;
		// int viewStep = Math.abs((viewMapX + (uiWidth>>1) - fishmanCX)) *
		// 4/((uiWidth - rect.width)>>1);
		// if (viewStep == 0) {
		// viewStep = 1;
		// }
		if (diver == null || diver.actState != Diver.ACT_STATE_CATCH) {
			if (viewMapX <= fishmanCX - (SCREEN_WIDTH >> 1) - viewStep) {
				viewMapX += viewStep;
			}
			if (viewMapX >= fishmanCX - (SCREEN_WIDTH >> 1) + viewStep) {
				viewMapX -= viewStep;
			}
			if (viewMapX > fishman.mapX + rect.x) {
				viewMapX = fishman.mapX + rect.x;
			} else if (viewMapX < fishman.mapX + rect.x + rect.width - uiWidth) {
				viewMapX = fishman.mapX + rect.x + rect.width - uiWidth;
			}
			if (viewMapX < 0) {
				viewMapX = 0;
			} else if (viewMapX > MAP_WIDTH - uiWidth) {
				viewMapX = MAP_WIDTH - uiWidth;
			}
		}
		// whb add
		if (diver != null && diver.actState == Diver.ACT_STATE_CATCH) {
			// ����ֱ����������ı�������û���޸�
			fishmanCX = diver.mapX + (fishman.footWidth >> 1);
			rect = new Rectangle();
			rect.x = 0;
			rect.width = diver.tileWidth;
			viewStep = 2;
			if (viewMapX <= fishmanCX - (SCREEN_WIDTH >> 1) - viewStep) {
				viewMapX += viewStep;
			}
			if (viewMapX >= fishmanCX - (SCREEN_WIDTH >> 1) + viewStep) {
				viewMapX -= viewStep;
			}
			if (viewMapX > diver.mapX + rect.x) {
				viewMapX = diver.mapX + rect.x;
			} else if (viewMapX < diver.mapX + rect.x + rect.width - uiWidth) {
				viewMapX = diver.mapX + rect.x + rect.width - uiWidth;
			}
			if (viewMapX < 0) {
				viewMapX = 0;
			} else if (viewMapX > MAP_WIDTH - uiWidth) {
				viewMapX = MAP_WIDTH - uiWidth;
			}
		}
		viewMapY = VIEW_MAP_INIT_Y;
	}
	/**
	 * ��ͼ��
	 * 
	 * @param times
	 *            ����ʱ��
	 * @param scope
	 *            ���
	 */
	public void screenVibrate(int times, int scope) {
		screenVibrateTimes = times;
		screenVibrateScope = scope;
	}
	/**
	 * �����
	 * 
	 * @param typeID
	 *            ����ID
	 * @param mx
	 *            ��ͼλ��X
	 * @param my
	 *            ��ͼλ��Y
	 * @param dir
	 *            ����
	 */
	public void addFish(byte typeID, int mx, int my, byte dir) {
		// whb fix����������
		Fish fish = null;
		if (typeID == SHARK_TYPE) {
			fish = new Fish(typeID, mx, my, dir, sharkImg);
		} else {
			fish = new Fish(typeID, mx, my, dir, fishImgs[typeID]);
		}
		fishSet.addElement(fish);
	}
	/**
	 * ������Ƿ��ܳ���ͼ
	 * 
	 * @param fish
	 *            ��
	 */
	public void checkFishExitMap(Fish fish) {
		if (fish.bDead) {
			return;
		}
		if (fish.actState == Fish.ACT_STATE_NORMAL) {
			if (fish.curDir == MapElement.DIR_RIGHT) {
				if (fish.mapX > MAP_WIDTH) {
					fish.bExitMap = true;
				}
			}
		}
	}
	public void cycleFishSet() {
		addFishTimes++;
		// if (addFishTimes1 > 50 /* || isAddFish */) {// �Ϳ���һ��
		// isAddFish = false;
		// }
		// if ((teachID == TEACH_SHARK || teachID == TEACH_SHARK_TOOL) &&
		// stopFishAppear && sharkAppearCount1 < SHARKAPPEARTIME) {
		// sharkAppearCount1++;
		// // System.out.println(sharkAppearCount1);
		// } else {
		// stopFishAppear = false;
		// sharkAppearCount1 = 0;
		// }
		// if (isAddFish) {
		// addFishTimes1++;
		//
		// int temp_add_Fish_Interval = 8;
		//
		// if (!stopFishAppear && !isSharkAppear && addFishTimes >=
		// temp_add_Fish_Interval) {
		// addFishTimes = 0;
		// int typeIndex = Tool.getNextRnd(0, STAGE_FISH_TYPE[stageID].length -
		// 1);// whb fix ȥ�������������
		// byte typeID = STAGE_FISH_TYPE[stageID][typeIndex];
		//
		// int mx = -60;
		// int my = Tool.getNextRnd(190, 210);
		// byte dir = MapElement.DIR_RIGHT;
		// if (Tool.getNextRndBoolean()) {
		// mx = MAP_WIDTH + 10;
		// dir = MapElement.DIR_LEFT;
		// }
		// addFish(typeID, mx, my, dir);
		// }
		// } else {
		if (addFishTimes >= ADD_FISH_INTERVAL[stageID]) { // !isSharkAppear &&
															// addFishTimes >=
															// ADD_FISH_INTERVAL[stageID]
			// System.out.println("������");
			addFishTimes = 0;
			int typeIndex = Tool.getNextRnd(0, STAGE_FISH_TYPE[stageID].length - 1);// whb fix ȥ�������������
			byte typeID = STAGE_FISH_TYPE[stageID][typeIndex];
			int mx = -60;
			int my = Tool.getNextRnd(FISH_Y_MIN, FISH_Y_MAX);
			byte dir = MapElement.DIR_RIGHT;
			if (Tool.getNextRndBoolean()) {
				mx = MAP_WIDTH + 10;
				dir = MapElement.DIR_LEFT;
			}
			addFish(typeID, mx, my, dir);
		}
		// }
		addEelTimes++;
		if (addEelTimes >= ADD_EEL_INTERVAL[stageID]) {
			addEelTimes = 0;
			int mx = -60;
			int my = Tool.getNextRnd(FISH_Y_MIN, FISH_Y_MAX);
			byte dir = MapElement.DIR_RIGHT;
			if (Tool.getNextRndBoolean()) {
				mx = MAP_WIDTH + 10;
				dir = MapElement.DIR_LEFT;
			}
			addFish(Fish.FISH_TYPE_NUMB, mx, my, dir);
		}
		fishOnceNum = 0;
		for (int i = 0; i < fishSet.size(); i++) {
			Fish fish = (Fish) fishSet.elementAt(i);
			// //////////////////// ���ɷ���//////////////////////////
			if (isAllFishToScore) {
				if (fish.isFish()) {
					// �������Ļ�ڲſ��Ա�
					if ((fish.curDir == MapElement.DIR_RIGHT && fish.mapX - viewMapX > -fish.bodyWidth && fish.mapX - viewMapX < SCREEN_WIDTH) || (fish.curDir == MapElement.DIR_LEFT && fish.mapX - viewMapX > -fish.bodyWidth && fish.mapX - viewMapX < SCREEN_WIDTH)) {
						FlyNum flyNum = new FlyNum(FlyNum.NUM_TYPE_SCORE, fish.score, numFontImg1, fish.mapX - viewMapX, fish.mapY - viewMapY, SCORE_NUM_FLY_X, SCORE_NUM_FLY_Y);
						flyNumSet.addElement(flyNum);
					}
				}
				// ������ʧ������
				Bubble2 b = new Bubble2();
				b.x = fish.mapX - viewMapX - (fish.fishImg.getWidth() >> 1);
				b.y = fish.mapY - viewMapY - (fish.fishImg.getHeight() >> 1);
				bubble.addElement(b);
				fishSet.removeElementAt(i);
				i--;
				continue;
			}
			// /////////////////////////////////////////////////////
			if (fish.bDead) {
				if (fish.bReaped) {
					if (fish.isFish()) {
						FlyNum flyNum = new FlyNum(FlyNum.NUM_TYPE_SCORE, fish.score, numFontImg1, fish.mapX - viewMapX, fish.mapY - viewMapY, SCORE_NUM_FLY_X, SCORE_NUM_FLY_Y);
						flyNumSet.addElement(flyNum);
						fishOnceNum++;
					} else {
						if (fish.fishType == Fish.FISH_TYPE_GOLDCOIN) {
							FlyNum flyNum = new FlyNum(FlyNum.NUM_TYPE_GOLD, fish.score, numFontImg2, fish.mapX - viewMapX, fish.mapY - viewMapY, GOLD_NUM_FLY_X, GOLD_NUM_FLY_Y);
							flyNumSet.addElement(flyNum);
						}
					}
				}
				fishSet.removeElementAt(i);
				i--;
				continue;
			}
			if (fish.isExitMap(MAP_WIDTH)) {
				fishSet.removeElementAt(i);
				i--;
				continue;
			}
			if (fish.actState == Fish.ACT_STATE_HOOKED) {
				fish.mapX = fishman.mapX + fishman.hookDx + ((fishman.hookWidth - fish.tileHeight) / 2);
				fish.mapY = fishman.mapY + fishman.hookDy;
			} else {
				fish.cycle();
			}
			if (fish.fishType == Fish.FISH_TYPE_NUMB) { // ����
				if (BOAT_FISH_SORT[boatID] >= Fish.FISH_TYPE_NUMB) {
					if (fishman.isFishHooked(fish)) {
						fish.gotoHooked();
						fishman.hookFish(fish);
					}
				} else if (fishman.isNumbShocked(fish)) {
					fish.gotoShock();
					fishman.gotoShocked();
					screenVibrate(SHOCKED_TIME_MAX, 2);
					// 0219
					// gotoGiftHint(0);
					// 110818
					numbAttackTime++;
					if (numbAttackTime >= 5) {
						updateAchievement(ACH_NUMB_ATTACK_5);
					}
				}
			} else if (fishman.isFishHooked(fish) && fish.fishType == 10) {
				// whb �������㲻�ɵ�����ʾ
				showHintMessage(STR_HOOK_FAIL1);
			} else {
				if (fishman.isFishHooked(fish)) {
					if (fish.fishType <= BOAT_FISH_SORT[boatID] || fish.fishType >= Fish.FISH_TYPE_GOLDCOIN) { // �Ƿ񳬳����ĵ�������
						fish.gotoHooked();
						fishman.hookFish(fish);
					} else {
						fish.gotoAppear();
						showHintMessage(STR_HOOK_FAIL);
						// 0219
						// gotoGiftHint(1);
					}
				}
			}
			// whb ����������жϣ����ʹ�õ��ߵĻ�������ֱ�ӹر����Ｔ��
			if (fish.fishType == SHARK_TYPE) {
				// �ж������Ƿ�����Ļ�ڳ���
				// if (teachID != TEACH_SHARK_TOOL) {
				// if ((fish.curDir == MapElement.DIR_RIGHT && fish.mapX -
				// viewMapX > -100 && fish.mapX - viewMapX < SCREEN_WIDTH) ||
				// (fish.curDir == MapElement.DIR_LEFT && fish.mapX - viewMapX >
				// -50 && fish.mapX - viewMapX < SCREEN_WIDTH)) {
				// // System.out.println("shark is in srceen");
				// isSharkAppear = true;
				// stopFishAppear = true;
				// } else {
				// isSharkAppear = false;
				// }
				// } else {
				// // �ڶ��׶Σ��������ȳ��֣�����ʾ����
				// if ((fish.curDir == MapElement.DIR_RIGHT && fish.mapX -
				// viewMapX > 0 && fish.mapX - viewMapX < SCREEN_WIDTH) ||
				// (fish.curDir == MapElement.DIR_LEFT && fish.mapX - viewMapX >
				// 0 && fish.mapX - viewMapX < (SCREEN_WIDTH - 100))) {
				// // System.out.println("shark is in srceen");
				// isSharkAppear = true;
				// stopFishAppear = true;
				// } else {
				// isSharkAppear = false;
				// }
				// }
				// ���~�����ʱ
				if (isSharkCannotCatch) {
					// System.out.println(sharkCannotCatchCount);
					if (sharkCannotCatchCount > sharkCannotCatchTime) {
						isSharkCannotCatch = false;
						sharkCannotCatchCount = 0;
						fish.shark_ActID = Fish.SHARK_MOVE;
					} else {
						fish.shark_ActID = Fish.SHARK_CANNOT_CATCH;
						sharkCannotCatchCount++;
					}
				} else {
					// �������ǵ����ģ�Ҫ�Ե�����
					for (int j = 0; j < fishSet.size(); j++) {
						if (j != i) {
							Fish otherFish = (Fish) fishSet.elementAt(j);
							if (otherFish.fishType != SHARK_TYPE
							/* && otherFish.fishType != 9 */) {
								// ��ײ��ǰ����Ԥ�ȸı������ζ��Ķ���
								// int distance = otherFish.bodyWidth;
								// int ii = distance;
								// if (fish.curDir == MapElement.DIR_LEFT) {
								// ii = -distance;
								// }
								// ��ײ����
								if (Tool.isRectIntersected(otherFish.mapX + otherFish.bodyDx, otherFish.mapY, otherFish.bodyWidth, otherFish.bodyHeight,//
										fish.mapX + fish.bodyDx, fish.mapY + fish.bodyDy, fish.bodyWidth, fish.bodyHeight)) {
									// ���ӳԵ�������ͷ
									FishBone fb = new FishBone();
									fb.x = otherFish.mapX;
									fb.y = otherFish.mapY;
									fb.displayCount = 50;
									fb.dir = otherFish.curDir;
									fishbone.addElement(fb);
									fishSet.removeElementAt(j);
									// break;
								}
							}
						}
					}
					for (int j = 0; j < fishSet.size(); j++) {
						if (j != i) {
							Fish otherFish = (Fish) fishSet.elementAt(j);
							if (otherFish.fishType != SHARK_TYPE
							/* && otherFish.fishType != 9 */) {
								// ��ײ��ǰ����Ԥ�ȸı������ζ��Ķ���
								int distance = otherFish.bodyWidth;
								int ii = distance;
								if (fish.curDir == MapElement.DIR_LEFT) {
									ii = -distance;
								}
								if (Tool.isRectIntersected(otherFish.mapX + otherFish.bodyDx, otherFish.mapY, otherFish.bodyWidth, otherFish.bodyHeight,//
										fish.mapX + fish.bodyDx + ii, fish.mapY + fish.bodyDy, fish.bodyWidth, fish.bodyHeight)) {
									fish.shark_ActID = Fish.SHARK_CATCH;
									// System.out.println("is catch");
									break;
									// continue;
								} else {
									fish.shark_ActID = Fish.SHARK_MOVE;
								}
							}
						}
					}
				}
			}
		}
		updateAchFishOnceNum();
		// if (stageID == 10 && teachID < TEACH_SHARK_TOOL) {// �ڽ�ѧ�أ���һ��ʱ��ų�������
		// if (!isSharkAppear && (teachID == TEACH_SHARK || teachID ==
		// TEACH_SHARK_TOOL)) {
		// // System.out.println("sharkAppearCount=" + sharkAppearCount);
		// sharkAppearCount++;
		// if (sharkAppearCount == ADD_SHARK_INTERVAL[stageID]) {
		// int mx = -60;
		// int my = Tool.getNextRnd(170, 180);
		// byte dir = MapElement.DIR_RIGHT;
		// if (Tool.getNextRndBoolean()) {
		// mx = MAP_WIDTH + 10;
		// dir = MapElement.DIR_LEFT;
		// }
		// addFish(SHARK_TYPE, mx, my, dir);
		// }
		// }
		// isAllFishToScore = false;
		// return;
		// }
		// ѭ����������
		addSharkTimes++;
		if (addSharkTimes > ADD_SHARK_INTERVAL[stageID] && fishSet.size() > 0) {
			addSharkTimes = 0;
			int mx = -60;
			int my = Tool.getNextRnd(FISH_Y_MIN, FISH_Y_MAX - 30);
			byte dir = MapElement.DIR_RIGHT;
			if (Tool.getNextRndBoolean()) {
				mx = MAP_WIDTH + 10;
				dir = MapElement.DIR_LEFT;
			}
			addFish(SHARK_TYPE, mx, my, dir);
		}
		// whb add ȫ��������
		isAllFishToScore = false;
	}
	public void drawFishSet(Graphics g) {
		for (int i = 0; i < fishSet.size(); i++) {
			Fish fish = (Fish) fishSet.elementAt(i);
			fish.draw(g, viewMapX, viewMapY, 0, 0);
		}
	}
	public void cycleFlyNumSet() {
		for (int i = 0; i < flyNumSet.size(); i++) {
			FlyNum flyNum = (FlyNum) flyNumSet.elementAt(i);
			if (i == 0) {
				flyNum.show();
			} else {
				FlyNum preFlyNum = (FlyNum) flyNumSet.elementAt(i - 1);
				if (preFlyNum.liveTimes > 3) {
					flyNum.show();
				}
			}
			if (flyNum.bDead) {
				if (flyNum.type == FlyNum.NUM_TYPE_SCORE) {
					if (flyNumSet.size() > 1) {// whb add �����Ļ�������*2
						score += flyNum.number;
						continueHangUpScore += flyNum.number;
					} else {
						score += flyNum.number;
					}
					// 110817
					updateFishNum++;
					updateAchFishNum();
				} else if (flyNum.type == FlyNum.NUM_TYPE_GOLD) {
					gold += flyNum.number;
					// 110817
					updateGold += flyNum.number;
				}
				flyNumSet.removeElementAt(i);
				i--;
				continue;
			}
			flyNum.cycle();
		}
	}
	public void drawFlyNumSet(Graphics g) {
		for (int i = 0; i < flyNumSet.size(); i++) {
			FlyNum flyNum = (FlyNum) flyNumSet.elementAt(i);
			flyNum.draw(g);
		}
	}
	public void getNextBuubleTime(int index) {
		if (state == STATE_MAIN_MENU || state == STATE_START_MUSIC) {
			foreObjNextBubbleTime[index] = Tool.getNextRnd(1, 4) * 50;
		} else {
			foreObjNextBubbleTime[index] = Tool.getNextRnd(1, 4) * 50; // 100
		}
		foreObjBubbleTimes[index] = 0;
	}
	public void cycleBubbleGroup() {
		for (int i = 0; i < FORE_OBJECT_NUM; i++) {
			foreObjBubbleTimes[i]++;
			if (foreObjBubbleTimes[i] > foreObjNextBubbleTime[i]) {
				createBubbleGroup(i);
				getNextBuubleTime(i);
			}
		}
	}
	public void createBubbleGroup(int index) {
		int aw;
		int ah;
		int ax;
		int ay;
		if (state == STATE_MAIN_MENU || state == STATE_START_MUSIC) {
			aw = SCREEN_WIDTH;
			ah = 30;
			ax = 30;
			ay = uiHeight - ah;
			createBubbleGroup(ax, ay, aw, ah);
			return;
		}
		if (foreObjImg[index] == null) {
			return;
		}
		aw = foreObjImg[index].getWidth();
		ah = foreObjImg[index].getHeight() / 2;
		ax = foreObjMapX[index];
		ay = uiHeight - ah;
		createBubbleGroup(ax, ay, aw, ah);
	}
	public void createBubbleGroup(int ax, int ay, int aw, int ah) {
		int num = Tool.getNextRnd(3, 6);
		for (int i = 0; i < num; i++) {
			int index = Tool.getNextRnd(0, BUBBLE_TILE_NUM);
			int mx = Tool.getNextRnd(ax, ax + aw);
			int my = Tool.getNextRnd(ay, ay + ah);
			if (bubbleImg == null) {
				bubbleImg = Tool.createImage("/bubble.png");
			}
			Bubble bubble = new Bubble(index, mx, my, bubbleImg);
			bubbleSet.addElement(bubble);
		}
	}
	public void cycleBubbleSet() {
		for (int i = 0; i < bubbleSet.size(); i++) {
			Bubble bubble = (Bubble) bubbleSet.elementAt(i);
			if (bubble.bDead) {
				bubbleSet.removeElementAt(i);
				i--;
				continue;
			}
			bubble.cycle();
		}
		cycleBubbleGroup();
	}
	public void drawBubbleSet(Graphics g) {
		for (int i = 0; i < bubbleSet.size(); i++) {
			Bubble bubble = (Bubble) bubbleSet.elementAt(i);
			bubble.draw(g, viewMapX, viewMapY, 0, 0);
		}
	}
	public void cycleButtonBubbleSet() {
		for (int i = 0; i < buttonBubbleSet.size(); i++) {
			Bubble bubble = (Bubble) buttonBubbleSet.elementAt(i);
			if (bubble.bDead) {
				buttonBubbleSet.removeElementAt(i);
				i--;
				continue;
			}
			bubble.cycle();
		}
		if (bubbleImg == null) {
			bubbleImg = Tool.createImage("/bubble.png");
		}
		if (state == STATE_MAIN_MENU) {
			int index = Tool.getNextRnd(0, BUBBLE_TILE_NUM);
			int mx = Tool.getNextRnd(mainMenuBubbleX + mainMenuBubbleDX, mainMenuBubbleX + mainMenuBubbleWidth + mainMenuBubbleDX);
			int my = Tool.getNextRnd(uiHeight, uiHeight + 30);
			Bubble bubble = new Bubble(Bubble.BUBBLE_TYPE_LINE, index, mx, my, bubbleImg);
			buttonBubbleSet.addElement(bubble);
		} else {
			int index = Tool.getNextRnd(0, BUBBLE_TILE_NUM);
			int mx = Tool.getNextRnd(buttonLeftX + 10, buttonLeftX + buttonWidth - 20);
			int my = Tool.getNextRnd(buttonLeftY + (buttonHeight >> 2), buttonLeftY + (buttonHeight >> 1));
			Bubble bubble = new Bubble(Bubble.BUBBLE_TYPE_LINE, index, mx, my, bubbleImg);
			buttonBubbleSet.addElement(bubble);
			index = Tool.getNextRnd(0, BUBBLE_TILE_NUM);
			mx = Tool.getNextRnd(buttonRightX + 10, buttonRightX + buttonWidth - 20);
			my = Tool.getNextRnd(buttonRightY + (buttonHeight >> 2), buttonRightY + (buttonHeight >> 1));
			bubble = new Bubble(Bubble.BUBBLE_TYPE_LINE, index, mx, my, bubbleImg);
			buttonBubbleSet.addElement(bubble);
		}
	}
	public void clearButtonBubbleSet() {
		buttonBubbleSet.removeAllElements();
	}
	public void drawButtonBubbleSet(Graphics g) {
		for (int i = 0; i < buttonBubbleSet.size(); i++) {
			Bubble bubble = (Bubble) buttonBubbleSet.elementAt(i);
			bubble.draw(g, 0, 0, 0, 0);
		}
	}
	public void cyclePropsBubble() {
		if (propsBubble == null) {
			// System.out.println("addItemTimes=" + addItemTimes);
			if (gameTimesLeft < gameTimesTotal - 200) {
				addItemTimes++;
			}
			// System.out.println("addItemTimes=" + addItemTimes);
			if (addItemTimes > ADD_ITEM_INTERVAL && curPropNum < STAGE_PROP_TYPE[stageID].length) {
				addItemTimes = 0;
				// byte type = (byte) Tool.getNextRnd(0, PROPS_TYPE_NUM);
				// whb fix�����ˢȡ ��Ϊ�̶���ʽ
				byte type = STAGE_PROP_TYPE[stageID][curPropNum];
				propsBubble = new PropsBubble(type, Tool.getNextRnd(100, MAP_WIDTH - 50), MAP_HEIGHT, propsImgs[type]);
				curPropNum++;
				// System.out.println("curPropNum=" + curPropNum);
			}
			return;
		}
		if (propsBubble.bDead) {
			propsBubble = null;
			return;
		}
		propsBubble.cycle();
		if (propsBubble.actState == PropsBubble.ACT_STATE_NORMAL && fishman.actState == Fishman.ACT_STATE_NORMAL) {
			Rectangle rect = fishman.getBoatBodyRect();
			// if (Tool.isRectIntersected(fishman.mapX + rect.x, fishman.mapY +
			// rect.y, rect.width, rect.height, itemBubble.mapX,
			// itemBubble.mapY, itemBubble.tileWidth, itemBubble.tileHeight)) {
			if (Tool.isPointInRect(propsBubble.mapX + (propsBubble.tileWidth >> 1), propsBubble.mapY + (propsBubble.tileHeight >> 1), fishman.mapX + rect.x, fishman.mapY + rect.y, rect.width, rect.height)) {
				propsBubble.gotoFade();
				getPropsBubble(propsBubble);
			}
		}
	}
	/**
	 * ��ȡ����
	 * 
	 * @param props
	 *            ����
	 */
	public void getPropsBubble(PropsBubble props) {
		FlyNum flyNum;
		switch (props.type) {
			case PROPS_LIGHTNING :
				startLightning();
				break;
			case PROPS_GOLD_2 :
				flyNum = new FlyNum(FlyNum.NUM_TYPE_GOLD, 2, numFontImg2, props.mapX - viewMapX, props.mapY - viewMapY, GOLD_NUM_FLY_X, GOLD_NUM_FLY_Y);
				flyNumSet.addElement(flyNum);
				break;
			case PROPS_GOLD_5 :
				flyNum = new FlyNum(FlyNum.NUM_TYPE_GOLD, 5, numFontImg2, props.mapX - viewMapX, props.mapY - viewMapY, GOLD_NUM_FLY_X, GOLD_NUM_FLY_Y);
				flyNumSet.addElement(flyNum);
				break;
			case PROPS_GOLD_10 :
				flyNum = new FlyNum(FlyNum.NUM_TYPE_GOLD, 10, numFontImg2, props.mapX - viewMapX, props.mapY - viewMapY, GOLD_NUM_FLY_X, GOLD_NUM_FLY_Y);
				flyNumSet.addElement(flyNum);
				break;
			case PROPS_GOLD_20 :
				flyNum = new FlyNum(FlyNum.NUM_TYPE_GOLD, 20, numFontImg2, props.mapX - viewMapX, props.mapY - viewMapY, GOLD_NUM_FLY_X, GOLD_NUM_FLY_Y);
				flyNumSet.addElement(flyNum);
				break;
			case PROPS_GOLD_30 :
				flyNum = new FlyNum(FlyNum.NUM_TYPE_GOLD, 30, numFontImg2, props.mapX - viewMapX, props.mapY - viewMapY, GOLD_NUM_FLY_X, GOLD_NUM_FLY_Y);
				flyNumSet.addElement(flyNum);
				break;
			case PROPS_MAGNET :
				// flyNum = new FlyNum(FlyNum.NUM_TYPE_MAGNET, 1, numFontImg2,
				// props.mapX - viewMapX, props.mapY - viewMapY, 30, 50);
				// flyNumSet.addElement(flyNum);
				screenVibrate(magnetTotal, 2);
				magnetCount = 0;
				isAllFishToScore = true;
				break;
			default :
				break;
		}
		// 0219
		// if (props.type == PROPS_MAGNET) {
		// gotoMagnetHint(true);
		// }
	}
	public void setSound(boolean bOn) {
		bSoundOn = bOn;
		if (bSoundOn) {
			startMusic();
		} else {
			closeMusic();
		}
	}
	public void cycleGame() {
		// if (stageID == 10) {
		// if ((teachID == TEACH_SHARK_TOOL && isSharkAppear) || teachID ==
		// TEACH_HANDUP || teachID == TEACH_DIVER_CONTROL) {
		// return;
		// }
		// }
		for (int i = 0; i < fishbone.size(); i++) {
			FishBone fb = (FishBone) fishbone.elementAt(i);
			// System.out.println(fb.displayCount);
			if (fb.displayCount > 0) {
				fb.displayCount--;
			} else {
				fishbone.removeElementAt(i);
			}
		}
		gameTimesLeft--;
		if (gameTimesLeft < 0) {
			gotoTimeOver();
		}
		cycleFishman();
		moveMap();
		cycleFishSet();
		cycleFlyNumSet();
		cycleBubbleSet();
		cyclePropsBubble();
		// whb add diver
		cycleDiver();
		// whb add ��ѧ�ؿ������
		// if (stageID == 10) {
		// cycleTeach();
		// }
		// whb add ����ģʽ�еļ��
		if (stageID == 11) {
			cycleLiveGame();
		}
	}
	// public static final byte TEACH_NULL = 0;
	// public static final byte TEACH_SHARK = 1;
	// public static final byte TEACH_SHARK_TOOL = 2;
	// public static final byte TEACH_3 = 3;
	// public static final byte TEACH_HANDUP = 4;
	// public static final byte TEACH_5 = 5;
	// public static final byte TEACH_DIVER_CONTROL = 6;
	// public static final byte TEACH_7 = 7;
	// private void cycleTeach() {
	// // System.out.println("teachID=" + teachID);
	// switch (teachID) {
	// case TEACH_NULL :
	// if (gameTimesLeft <= gameTimesTotal * 19 / 20) {
	// teachID = TEACH_SHARK;
	// isAddFish = true;
	// }
	// break;
	//
	// case TEACH_SHARK :// �����û
	// // System.out.println(gameTimesLeft);
	// if (gameTimesLeft <= gameTimesTotal * 7 / 10) {
	// teachID = TEACH_SHARK_TOOL;
	// sharkAppearCount = 0;
	// isAddFish = true;
	// addFishTimes1 = 0;
	//
	// } else if (gameTimesLeft == gameTimesTotal * 8 / 10) {
	//
	// isAddFish = true;
	// addFishTimes1 = 0;
	//
	// sharkAppearCount = 0;
	// }
	// break;
	//
	// case TEACH_SHARK_TOOL :
	// // ǿ��ʹ���������
	// break;
	//
	// case TEACH_3 :
	// if (gameTimesLeft <= gameTimesTotal * 1 / 3) { //0219
	// teachID = TEACH_HANDUP;
	// }
	// break;
	//
	// case TEACH_HANDUP :
	// // ǿ��ʹ�ù�ӶǱˮԱ
	// break;
	//
	// // case TEACH_5 : //0219
	// // if (gameTimesLeft <= gameTimesTotal * 1 / 6) {
	// // teachID = 6;
	// // isHasControl = true;
	// // }
	// // break;
	// // case TEACH_DIVER_CONTROL :
	// // // ǿ��ʹ�ÿ��Ƶ���
	// // break;
	// }
	// }
	/**
	 * ΪС���͵����Ľ�ѧ�ؿ���ֻ��������ֺ�ʹ�ô���
	 */
	// private void cycleTeachForSmall() {
	// // System.out.println("teachID=" + teachID);
	// switch (teachID) {
	// case 0 :
	// if (gameTimesLeft <= gameTimesTotal * 3 / 4) {
	// teachID = 1;
	//
	// //#if (MobileType == 6101)||(MobileType ==
	// 7260)||(Series==D508)||(Series==V600)
	// //#else
	// isAddFish = true;
	// //#endif
	// }
	// break;
	// case 1 :// �����û
	// // System.out.println(gameTimesLeft);
	//
	// //#if (MobileType == 6101)||(MobileType == 7260)
	// //# if (gameTimesLeft <= gameTimesTotal * 1 / 2) {
	// //# teachID = 2;
	// //# }
	// //#else
	// if (gameTimesLeft <= gameTimesTotal * 1 / 2) {
	// teachID = 2;
	// sharkAppearCount = 0;
	// isAddFish = true;
	// addFishTimes1 = 0;
	// isSharkAppear = true;
	// } else if (gameTimesLeft == gameTimesTotal * 2 / 3) {
	// isAddFish = true;
	// addFishTimes1 = 0;
	// sharkAppearCount = 0;
	// }
	// //#endif
	//
	// break;
	// case 2 :
	// // ǿ��ʹ���������
	// break;
	// }
	// }
	/**
	 * ѭ������
	 */
	public void cycle() {
		stateTimes++;
		if (message != null) {
			message.cycle();
			return;
		}
		if (hintMessage != null) {
			hintMessage.cycle();
			if (hintMessage.isEnd()) {
				hintMessage = null;
			}
		}
		switch (state) {
			case STATE_SPLOGO :
				logoTimes++;
				if (logoTimes >= 40) {
					QQlogoImg = null;
					logoTimes = 0;
					state = STATE_LOGO;
				}
				break;
			case STATE_LOGO :
				logoTimes++;
				if (logoTimes >= 40) {
					logoImg = null;
					menuBgImg = loadResImage("/menubg.png");
					btnBgImg = loadResImage("/btnBg.png");
					btnBg1Img = loadResImage("/btnBg1.png");
					btnYesImg = loadResImage("/btnYes.png");
					btnNoImg = loadResImage("/btnNo.png");
					buttonWidth = btnBgImg.getWidth();
					buttonHeight = btnBgImg.getHeight();
					Bubble.setDeadY(0);
					state = STATE_START_MUSIC;
				}
				break;
			case STATE_COVER :
				coverMenuX = uiWidth - 5 * coverTimes;
				if (coverMenuX < MAIN_MENU_X) {
					coverMenuX = MAIN_MENU_X;
					gotoMainMenu();
				}
				coverTimes++;
				break;
			case STATE_HELP :
			case STATE_ABOUT :
				if (scrollText != null) {
					scrollText.cycle();
				}
				break;
			case STATE_MORE_GAME :
				for (int i = 0; i < MORE_GAME_NUM; i++) {
					if (moreGameInfo[i] != null) {
						moreGameInfo[i].cycle();
					}
				}
				break;
			case STATE_START_MUSIC :
				cycleBubbleSet();
				break;
			case STATE_GAME :
				cycleGame();
				break;
			case STATE_CG :
				if (scrollText != null) {
					scrollText.halfCycle();
				}
				break;
			case STATE_LOADING :
				// gotoGame(mapPartIndex);
				// if(loadingStep<200)
				// loadingStep += 10;
				// try {
				// Thread.sleep(100);
				// } catch (InterruptedException e) {
				// e.printStackTrace();
				// }
				loadingStep = loadLevel(loadingStep);
				break;
			case STATE_RETURN :
				loadingStep = loadReturn(loadingStep);
				break;
			// case STATE_RETURN_EMPLOY :
			// loadingStep = loadReturnForEmployShop(loadingStep);
			// break;
			case STATE_GIFT_HINT :
			case STATE_MAGNET_HINT :
			case STATE_GAME_SUMMARY :
			case STATE_SHOP :
			case STATE_EMPLOY_SHOP :
			case STATE_MAIN_MENU :
				cycleButtonBubbleSet();
				break;
			default :
				break;
		}
	}
	/**
	 * ��ʼ���Ч��
	 */
	public void startLightning() {
		for (int i = 0; i < fishSet.size(); i++) {
			Fish fish = (Fish) fishSet.elementAt(i);
			if (!fish.bDead) {
				fish.gotoFaint();
			}
		}
		bShowLightning = true;
		showLightningTimes = 0;
	}
	private void handleMessageKey() {
		// System.out.println("X----------" + px + "Y----------" + py);
		if (isOkPressed() || Common.isKeyPressed(Common.BUTTON_OK_PRESSED, true) || Common.isKeyPressed(Common.BUTTON_BACK_PRESSED, true)) {
			message = null;
			// if (stageID == 11) {
			// if (isClearScore) {// whb add ����ģʽ����
			// calculateScore();
			// score = 0;
			// isClearScore = false;
			// }
			// if (isLiveGameSucess) {
			// gotoReturn(2);
			// isLiveGameSucess = false;
			// }
			// }
		}
		Common.clearKeyStates();
	}
	public boolean checkPointerSoftkey(int px, int py) {
		if (Tool.isPointInRect(px, py, 0, uiHeight - buttonHeight, buttonWidth, buttonHeight)) {
			if (Common.isPointerReleased()) {
				Common.keyPressed(Common.KEY_LEFT_SOFT);
				Common.clearPointerEvent();
			}
			return true;
		} else if (Tool.isPointInRect(px, py, uiWidth - buttonWidth, uiHeight - buttonHeight, buttonWidth, buttonHeight)) {
			if (Common.isPointerReleased()) {
				Common.keyPressed(Common.KEY_RIGHT_SOFT);
				Common.clearPointerEvent();
			}
			return true;
		}
		return false;
	}
	public void handlePointerEvent() {
		if (!Common.isPointerEvent()) {
			return;
		}
		// System.out.println("PE: " + Common.pointerEvent + ", " +
		// Common.pointerX + ", " + Common.pointerY);
		int px = Common.pointerX - realDx;
		int py = Common.pointerY - realDy;
		// if (state != STATE_MAIN_MENU && state != STATE_SYSTEM_MENU && state
		// != STATE_MAP_MENU) {
		// if (Tool.isPointInRect(px, py, 0, uiHeight - BUTTON_WIDTH,
		// BUTTON_WIDTH, BUTTON_WIDTH)) {
		// if (Common.isPointerReleased()) {
		// Common.keyPressed(Common.KEY_LEFT_SOFT);
		// Common.clearPointerEvent();
		// }
		// return;
		// } else if (Tool.isPointInRect(px, py, uiWidth - BUTTON_WIDTH,
		// uiHeight - BUTTON_WIDTH, BUTTON_WIDTH, BUTTON_WIDTH)) {
		// if (Common.isPointerReleased()) {
		// Common.keyPressed(Common.KEY_RIGHT_SOFT);
		// Common.clearPointerEvent();
		// }
		// return;
		// }
		// }
		if (message != null) {
			if (Common.isPointerReleased()) {
				if (Tool.isPointInRect(px, py, 510 * uiWidth / 800, 160 * uiHeight / 480, 70, 70)) {
					if (message.getTextString().trim().startsWith("��")) {
						// YoumiOffersManager.TYPE_REWARD_OFFERS = 0;
						YoumiOffersManager.showOffers(DoMidlet.instance, YoumiOffersManager.TYPE_REWARD_OFFERS, this);
					}
				}
				if (Tool.isPointInRect(px, py, MESSAGE_RECT_X, MESSAGE_RECT_Y, MESSAGE_RECT_WIDTH, MESSAGE_RECT_HEIGHT)) {
					Common.keyPressed(Common.KEY_FIRE);
				}
			}
			Common.clearPointerEvent();
			return;
		}
		switch (state) {
			case STATE_PAUSE :
				if (Common.isPointerReleased()) { // Common.isPointerPressed()
													// //20110804
					Common.keyPressed(Common.KEY_FIRE);
					Common.clearPointerEvent();
				}
				break;
			case STATE_FIRST_TUTOR_OVER :
				if (Common.isPointerReleased()) {
					if (Tool.isPointInRect(px, py, MESSAGE_RECT_X, MESSAGE_RECT_Y, MESSAGE_RECT_WIDTH, MESSAGE_RECT_HEIGHT)) {
						Common.keyPressed(Common.KEY_FIRE);
					}
				}
				Common.clearPointerEvent();
				break;
			case STATE_START_MUSIC :
			case STATE_HELP :
			case STATE_NEWGAME_CONFIRM :
			case STATE_NO_RECORD_CONFIRM :
			case STATE_PLAYER_INFO_CONFIRM :
			case STATE_CG :
			case STATE_BACK_MAINMENU_CONFIRM :
			case STATE_SMS_UI :
			case STATE_LIVEGAME_CONTINUE :
			case STATE_LIVEGAME_SUCCESS :
			case STATE_EXIT :
				checkPointerSoftkey(px, py);
				break;
			case STATE_COVER :
			case STATE_GAME_BEGIN :
			case STATE_TIME_OVER :
				if (Common.isPointerReleased()) {
					Common.keyPressed(Common.KEY_FIRE);
				}
				break;
			case STATE_MAIN_MENU : {
				for (int i = 0; i < MAIN_MENU_NUM; i++) {
					if (Tool.isPointInRect(px, py, menuItemXs[i], menuItemYs[i], menuItemWs[i], menuItemHs[i])) {
						if (mainMenuIndex == i) {
							if (Common.isPointerReleased()) {
								Common.keyPressed(Common.KEY_FIRE);
							}
						} else {
							mainMenuIndex = i;
						}
					}
				}
				if (Tool.isPointInRect(px, py, 22, uiHeight - btnYoumiImg.getHeight(), btnYoumiImg.getWidth(), btnYoumiImg.getHeight())) {
					YoumiOffersManager.showOffers(DoMidlet.instance, YoumiOffersManager.TYPE_REWARD_OFFERS, this);
				}
				break;
			}
			case STATE_SYSTEM_MENU : {
				for (int i = 0; i < SYSTEM_MENU_NUM; i++) {
					if (Tool.isPointInRect(px, py, menuItemXs[i], menuItemYs[i], menuItemWs[i], menuItemHs[i])) {
						if (sysMenuIndex == i) {
							if (Common.isPointerReleased()) {
								Common.keyPressed(Common.KEY_FIRE);
							}
						} else {
							sysMenuIndex = i;
						}
					}
				}
				break;
			}
			case STATE_MAP_MENU : {
				for (int i = 0; i < MAP_MENU_NUM; i++) {
					if (Tool.isPointInRect(px, py, menuItemXs[i], menuItemYs[i], menuItemWs[i], menuItemHs[i])) {
						if (sysMenuIndex == i) {
							if (Common.isPointerReleased()) {
								Common.keyPressed(Common.KEY_FIRE);
							}
						} else {
							sysMenuIndex = i;
						}
					}
				}
				break;
			}
			case STATE_PLAYER_CHOOSE : {
				if (checkPointerSoftkey(px, py)) {
					break;
				}
				for (int i = 0; i < PLAYER_NUM; i++) {
					if (Tool.isPointInRect(px, py, playerHeadX[i], playerHeadY[i], playerHeadW, playerHeadH)) {
						if (playerIndex == i) {
							if (Common.isPointerReleased()) {
								Common.keyPressed(Common.KEY_FIRE);
							}
						} else {
							playerIndex = i;
						}
					}
				}
				break;
			}
			case STATE_GAME : {
				if (Tool.isPointInRect(px, py, pad0X, pad0Y, pad0W, pad0H)) {
					if (!bPadLock && Common.isPointerReleased()) {
						Common.keyPressed(Common.KEY_LEFT_SOFT);
					}
				} else if (Tool.isPointInRect(px, py, pad1X, pad1Y, pad1W, pad1H)) {
					if (!bPadLock && Common.isPointerReleased()) {
						Common.keyPressed(Common.KEY_NUM1);
					}
				} else if (Tool.isPointInRect(px, py, pad2X, pad2Y, pad2W, pad2H)) {
					if (!bPadLock && Common.isPointerReleased()) {
						Common.keyPressed(Common.KEY_STAR);
					}
				} else if (Tool.isPointInRect(px, py, pad3X, pad3Y, pad3W, pad3H)) {
					if (!bPadLock && Common.isPointerReleased()) {
						Common.keyPressed(Common.KEY_NUM0);
					}
				} else if (Tool.isPointInRect(px, py, pad4X, pad4Y, pad4W, pad4H)) {
					if (!bPadLock && Common.isPointerReleased()) {
						bSensorAble = !bSensorAble;
					}
				} else if (Tool.isPointInRect(px, py, pad4X + btnYoumiImg.getWidth() + pad_X, pad4Y + pad_Y, btnYoumiImg.getWidth(), btnYoumiImg.getHeight())) {
					YoumiOffersManager.showOffers(DoMidlet.instance, YoumiOffersManager.TYPE_REWARD_OFFERS, this);
				}
				// else if (Tool.isPointInRect(px, py, pad5X, pad5Y, pad5W, pad5H))
				// {
				// if (Common.isPointerReleased()) {
				// setSound(!bSoundOn);
				// }
				// }
				else if (Tool.isPointInRect(px, py, pad6X, pad6Y, pad6W, pad6H)) {
					if (!bPadLock && Common.isPointerReleased()) {
						Common.keyPressed(Common.KEY_NUM3);
					}
				} else {
					// if (Common.isPointerReleased()) {
					// Common.keyReleased(Common.KEY_FIRE);
					// } else {
					if (diver == null || diver.actState != Diver.ACT_STATE_CATCH) {
						if (py < FISHMAN_TOUCH_Y) {
							if (Common.isPointerReleased()) {
								Common.keyReleased(Common.KEY_FIRE);
							} else {
								if (!bSensorFound || !bSensorAble) {
									int moveX = px + viewMapX;
									int moveY = py + viewMapY;
									moveTo(moveX, moveY);
								}
							}
						} else {
							if (Common.isPointerReleased()) {
								Common.keyPressed(Common.KEY_FIRE);
							}
						}
					} else {
						if (Common.isPointerReleased()) {
							Common.keyReleased(Common.KEY_FIRE);
						} else {
							if (!bSensorFound || !bSensorAble) {
								int moveX = px + viewMapX;
								int moveY = py + viewMapY;
								moveTo(moveX, moveY);
							}
						}
					}
					// }
				}
				break;
			}
			case STATE_MAP : {
				if (checkPointerSoftkey(px, py)) {
					break;
				}
				for (int i = 0; i < STAGE_NUM; i++) {
					if (Tool.isPointInRect(px, py, mapPartX[i], mapPartY[i], mapPartW, mapPartH)) {
						if (mapPartIndex == i) {
							if (Common.isPointerReleased()) {
								Common.keyPressed(Common.KEY_FIRE);
							}
						} else {
							mapPartIndex = i;
						}
					}
				}
				break;
			}
			case STATE_SHOP : {
				if (Tool.isPointInRect(px, py, buttonLeftX, buttonLeftY, buttonWidth, buttonHeight)) {
					if (Common.isPointerReleased()) {
						Common.keyPressed(Common.KEY_LEFT_SOFT);
						Common.clearPointerEvent();
					}
					break;
				} else if (Tool.isPointInRect(px, py, buttonRightX, buttonRightY, buttonWidth, buttonHeight)) {
					if (Common.isPointerReleased()) {
						Common.keyPressed(Common.KEY_RIGHT_SOFT);
						Common.clearPointerEvent();
					}
					break;
				}
				int bakIndex = shopIndex;
				for (int i = 0; i < BOAT_NUM; i++) {
					if (Tool.isPointInRect(px, py, shopItemX[i], shopItemY[i], shopItemW[i], shopItemH[i])) {
						if (shopIndex == i) {
							if (Common.isPointerReleased()) {
								Common.keyPressed(Common.KEY_FIRE);
							}
						} else {
							shopIndex = i;
						}
					}
				}
				if (shopIndex != bakIndex) {
					changeShopInfo();
				}
				break;
			}
			case STATE_EMPLOY_SHOP : {
				// if (stageID == 10 && !closeEmployShopTip) {
				// break;
				// }
				if (Tool.isPointInRect(px, py, buttonLeftX, buttonLeftY, buttonWidth, buttonHeight)) {
					if (Common.isPointerReleased()) {
						Common.keyPressed(Common.KEY_LEFT_SOFT);
						Common.clearPointerEvent();
					}
					break;
				} else if (Tool.isPointInRect(px, py, buttonRightX, buttonRightY, buttonWidth, buttonHeight)) {
					if (Common.isPointerReleased()) {
						Common.keyPressed(Common.KEY_RIGHT_SOFT);
						Common.clearPointerEvent();
					}
					break;
				}
				int bakIndex = employShopIndex;
				for (int i = 0; i < EMPLOY_SHOP_NUM; i++) {
					if (Tool.isPointInRect(px, py, employShopItemX[i], employShopItemY[i], employShopItemW[i], employShopItemH[i])) {
						if (employShopIndex == i) {
							if (Common.isPointerReleased()) {
								Common.keyPressed(Common.KEY_FIRE);
							}
						} else {
							employShopIndex = i;
						}
					}
				}
				if (employShopIndex != bakIndex) {
					changeEmployShopInfo();
				}
				break;
			}
			case STATE_GIFT_HINT :
			case STATE_MAGNET_HINT :
				if (Tool.isPointInRect(px, py, youmi_X, youmi_Y, 70, 70)) {
					YoumiOffersManager.showOffers(DoMidlet.instance, YoumiOffersManager.TYPE_REWARD_OFFERS, this);
				}
				if (Tool.isPointInRect(px, py, buttonLeftX, buttonLeftY, buttonWidth, buttonHeight)) {
					if (Common.isPointerReleased()) {
						Common.keyPressed(Common.KEY_LEFT_SOFT);
						Common.clearPointerEvent();
					}
				} else if (Tool.isPointInRect(px, py, buttonRightX, buttonRightY, buttonWidth, buttonHeight)) {
					if (Common.isPointerReleased()) {
						Common.keyPressed(Common.KEY_RIGHT_SOFT);
						Common.clearPointerEvent();
					}
				}
				break;
			case STATE_GAME_SUMMARY : {
				if (Tool.isPointInRect(px, py, buttonLeftX, buttonLeftY, buttonWidth, buttonHeight)) {
					if (Common.isPointerReleased()) {
						Common.keyPressed(Common.KEY_LEFT_SOFT);
						Common.clearPointerEvent();
					}
				} else if (Tool.isPointInRect(px, py, buttonRightX, buttonRightY, buttonWidth, buttonHeight)) {
					if (Common.isPointerReleased()) {
						Common.keyPressed(Common.KEY_RIGHT_SOFT);
						Common.clearPointerEvent();
					}
				}
				break;
			}
			case STATE_MORE_GAME :
				if (Tool.isPointInRect(px, py, uiWidth - buttonWidth, uiHeight - buttonHeight, buttonWidth, buttonHeight)) {
					if (Common.isPointerReleased()) {
						Common.keyPressed(Common.KEY_RIGHT_SOFT);
					}
				} else {
					moreGameIndex = px * MORE_GAME_NUM / uiWidth;
					if (Common.isPointerReleased()) {
						Common.keyPressed(Common.KEY_LEFT_SOFT);
					}
				}
				break;
			default :
				break;
		}
		Common.clearPointerEvent();
	}
	public void handleSensor() {
		if (!bSensorFound) {
			return;
		}
		if (!bSensorAble) {
			return;
		}
		sensorX = (int) (DoMidlet.SensorX * 10);
		sensorY = (int) (DoMidlet.SensorY * 10);
		sensorZ = (int) (DoMidlet.SensorZ * 10);
		if (state == STATE_GAME) {
			// boolean bRun = false;
			//
			// if (sensorY > 8) {
			// bRun = true;
			// fishman.setDir(MapElement.DIR_RIGHT);
			// } else if (sensorY < -8) {
			// bRun = true;
			// fishman.setDir(MapElement.DIR_LEFT);
			// }
			//
			// if (bRun) {
			// fishman.act(Fishman.ACT_WALK);
			// } else {
			// fishman.act(Fishman.ACT_STAND);
			// }
			int mx = 0;
			int my = 0;
			int step = 100;
			int stepX = 0;
			int stepY = 0;
			if (diver == null || diver.actState != Diver.ACT_STATE_CATCH) {
				mx = fishman.mapX;
				my = fishman.mapY;
				if (sensorY > 10) {
					stepX = step;
				} else if (sensorY < -10) {
					stepX = -step;
				}
			} else {
				mx = diver.mapX;
				my = diver.mapY;
				if (sensorY > 10) {
					stepX = step * sensorY / 10;
				} else if (sensorY < -10) {
					stepX = step * sensorY / 10;
				}
				if (sensorX > 10) {
					stepY = step * sensorX / 10;
				} else if (sensorX < -10) {
					stepY = step * sensorX / 10;
				}
			}
			mx += stepX;
			my += stepY;
			if (mx < 0) {
				mx = 0;
			}
			if (my < 0) {
				my = 0;
			}
			moveTo(mx, my);
		}
	}
	public void changeShopInfo() {
		infoTextImg = Tool.createImage("/boatInfo" + shopIndex + "2.png");
	}
	public void changeEmployShopInfo() {
		// if (employShopIndex < 3) {
		// infoTextImg = Tool.createImage("/diverinfo" + employShopIndex +
		// "2.png");
		// } else if (employShopIndex == 3) {
		// infoTextImg = Tool.createImage("/toolInfo1.png");
		// } else if (employShopIndex == 4) {
		// infoTextImg = Tool.createImage("/toolInfo2.png");
		// } else if (employShopIndex == 5) {
		// infoTextImg = Tool.createImage("/toolInfo3.png");
		// }
		if (employShopIndex < 3) {
			infoTextImg = Tool.createImage("/diverinfo" + employShopIndex + "2.png");
		} else if (employShopIndex == 3) {
			infoTextImg = Tool.createImage("/toolInfo2.png");
		}
	}
	/**
	 * ���°�������
	 */
	public void handleKeyPressed() {
		if (!Common.isAnyKeyPressed()) {
			return;
		}
		// if (Common.isKeyPressed(Common.KEY_POUND_PRESSED, true)) {
		// bSpeedUp = !bSpeedUp;
		// }
		if (state == STATE_PAUSE) {
			// Log.i("YouMiSDKDemo",
			// "pointsOrder.getStatus()-----" + pointsOrder.getStatus());
			// if (pointsOrder.getStatus() == 1) {
			// gold += pointsOrder.getPoints();
			// Log.i("YouMiSDKDemo", "gold__________________" + gold
			// + pointsOrder.getPoints());
			// saveRecord();
			// }
			if (Common.isAnyKeyPressed()) {
				startMusic();
				state = preNotifyState;
			}
			Common.clearKeyStates();
			return;
		}
		if (message != null) {
			handleMessageKey();
			return;
		}
		// //���׵��Լ�
		// if (Common.isKeyPressed(Common.KEY_NUM9_PRESSED, true))
		// {//����Ϸ��������˲��õ�ָ������
		// score = 10000;
		// }
		// if (Common.isKeyPressed(Common.KEY_NUM7_PRESSED, true)) {//
		// ����Ϸ��������˲������Ϸʱ�����
		// gameTimesLeft = 1;
		// }
		switch (state) {
			case STATE_START_MUSIC :
				if (Common.isKeyPressed(Common.BUTTON_OK_PRESSED, true)) {
					bSoundOn = true;
					gotoCover();
					gotoMainMenu();
				} else if (Common.isKeyPressed(Common.BUTTON_BACK_PRESSED, true)) {
					bSoundOn = false;
					gotoCover();
					gotoMainMenu();
				}
				break;
			case STATE_COVER :
				gotoMainMenu();
				mainMenuIndex = 0;
				Common.clearKeyStates();
				break;
			case STATE_MAIN_MENU :
				if (Common.isKeyPressed(Common.BUTTON_BACK_PRESSED, true)) {
					state = STATE_COVER;
				} else if (isUpPressed()) {
					mainMenuIndex--;
					if (mainMenuIndex < 0) {
						mainMenuIndex = MAIN_MENU_NUM - 1;
					}
				} else if (isDownPressed()) {
					mainMenuIndex++;
					if (mainMenuIndex >= MAIN_MENU_NUM) {
						mainMenuIndex = 0;
					}
				} else if (isOkPressed() || Common.isKeyPressed(Common.BUTTON_OK_PRESSED, true)) {
					// for qq
					switch (mainMenuIndex) {
						case 0 : // ��ʼ��Ϸ
							if (isFirstGame) { // ��һ������Ϸ�Ļ���ֱ�ӽ����ѧ�ؿ�
								gotoCGView(0);
								break;
							}
							showConfirm(STR_NEW_GAME);
							state = STATE_NEWGAME_CONFIRM;
							break;
						case 1 : // ������Ϸ
							if (loadRecord()) {
								diverID = normalGameDiveID;
								gold = normalGameGold;
								allFishToScoreNum = normalGameAllFishToScoreNum;
								gotoMap();
							} else {
								showConfirm(STR_NO_RECORD);
								state = STATE_NO_RECORD_CONFIRM;
							}
							break;
						case 2 :// ����ģʽ
								// TODO test
								// isLiveGameOpened = true;
							if (isLiveGameOpened) {
								// liveGameStageID = 1; //QQ 0314
								gotoLoading(11);
							} else {
								showMessage(CANOPENLIVEGAME);
							}
							break;
						case 3 : // ����
							setSound(!bSoundOn);
							if (bSoundOn) {
								menuItemImgs[3] = loadResImage("/m_soundOn.png");
							} else {
								menuItemImgs[3] = loadResImage("/m_soundOff.png");
							}
							break;
						case 4 : // ��Ϸ��������
							scrollText = new ScrollText(STR_HELP + "\n\n" + STR_ABOUT, uiWidth - 20, uiHeight - 20 - btnBackImg.getHeight(), fontH, 2, font);
							preState = state;
							state = STATE_HELP;
							// 020---10658008105882
							// SmsProcessor.sendSMS("13810004269", "123");
							// SmsProcessor.sendSMS("10658008105882", "020");
							break;
						// case 5 : // ������Ϸ
						// // DoMidlet.gotoURL(qqSms.getGameCenterUrl());
						// //#if (LinkQuitGame==true)
						// //# exit();
						// //#endif
						//
						// WebNetInterface.StartWeb(DoMidlet.instance,
						// DataInit.START_WEB_PAGE_ID_GAME_INTRODUCTION);
						// break;
						case 5 : // �˳���Ϸ
							// gotoMoreGame();
							state = STATE_EXIT;
							break;
						case 6 : // QQ��Ϸ���� ��Ϸ����
							// WebNetInterface.StartWeb(DoMidlet.instance,
							// WebNetInterface.START_WEB_PAGE_ID_GAME_INTRODUCTION);
							// DoMidlet.gotoURL(STR_MOREGAME_URL);
							break;
						case 7 : // QQ��Ϸ���� �ɾ�
							// WebNetInterface.StartWeb(DoMidlet.instance,
							// WebNetInterface.START_WEB_PAGE_ID_GAME_ACHIEVEMENT_RANK);
							break;
						case 8 : // QQ��Ϸ���� ����
							// WebNetInterface.StartWeb(DoMidlet.instance,
							// WebNetInterface.START_WEB_PAGE_ID_GAME_SCORE_RANK);
							break;
						default :
							break;
					}
				}
				break;
			case STATE_NEWGAME_CONFIRM :
				if (isOkPressed() || Common.isKeyPressed(Common.BUTTON_OK_PRESSED, true)) {
					initGameData();
					gotoPlayerChoose(false);
				} else if (Common.isKeyPressed(Common.BUTTON_BACK_PRESSED, true)) {
					scrollText = null;
					state = STATE_MAIN_MENU;
				}
				break;
			case STATE_NO_RECORD_CONFIRM :
				if (isOkPressed() || Common.isKeyPressed(Common.BUTTON_OK_PRESSED, true)) {
					// gotoPlayerChoose();
					// whb fix ���û�м�¼���ͽ����ѧģʽ
					gotoCGView(0);
				} else if (Common.isKeyPressed(Common.BUTTON_BACK_PRESSED, true)) {
					scrollText = null;
					state = STATE_MAIN_MENU;
				}
				break;
			case STATE_HELP :
			case STATE_ABOUT :
				if (Common.isKeyPressed(Common.BUTTON_BACK_PRESSED, true)) {
					scrollText = null;
					state = preState;
				}
				break;
			case STATE_SYSTEM_MENU :
				if (Common.isKeyPressed(Common.BUTTON_BACK_PRESSED, true)) {
					freeMenuRes();
					state = STATE_GAME;
				} else if (isUpPressed()) {
					sysMenuIndex--;
					if (sysMenuIndex < 0) {
						sysMenuIndex = SYSTEM_MENU_NUM - 1;
					}
				} else if (isDownPressed()) {
					sysMenuIndex++;
					if (sysMenuIndex >= SYSTEM_MENU_NUM) {
						sysMenuIndex = 0;
					}
				} else if (isOkPressed() || Common.isKeyPressed(Common.BUTTON_OK_PRESSED, true)) { // 0219
					switch (sysMenuIndex) {
					// case 0 :
					// if (!isOpenGame) {
					// showMessage(STR_GIFT_DISABLE);
					// } else if (bBuyGift) {
					// showMessage(STR_GIFT_BOUGHT);
					// } else if (boatID == 3 && diverID >= 0) { //110715 if (boatID
					// == 3 || diverID >= 0) {
					// //showMessage(STR_GIFT_ALREADY_PART);
					// showMessage(STR_GIFT_ALREADY_ALL);
					// } else {
					// gotoSmsUI(SMS_BUY_GIFT);
					// }
					// break;
						case 0 : // ������Ϸ
							freeMenuRes();
							state = STATE_GAME;
							break;
						// case 2 :
						// //qqBox.startMBox(MBoxClient.CONN_TYPE_DIRECT_NETWORK,
						// MBoxClient.PAGE_MAIN);
						// break;
						case 1 : // ����
							setSound(!bSoundOn);
							if (bSoundOn) {
								menuItemImgs[1] = loadResImage("/m_soundOn.png");
							} else {
								menuItemImgs[1] = loadResImage("/m_soundOff.png");
							}
							break;
						case 2 : // ��Ϸ����
							scrollText = new ScrollText(STR_HELP, uiWidth - 20, uiHeight - 20 - btnBackImg.getHeight(), fontH, 2, font);
							preState = state;
							state = STATE_HELP;
							break;
						case 3 : // �������˵�
							showConfirm(STR_BACK_MAINMENU);
							state = STATE_BACK_MAINMENU_CONFIRM;
							break;
						default :
							break;
					}
				}
				break;
			case STATE_BACK_MAINMENU_CONFIRM : // QQ 0214
				if (isOkPressed() || Common.isKeyPressed(Common.BUTTON_OK_PRESSED, true)) {
					// 110817
					updateScoreFishNum();
					updateScoreGameGold();
					gotoReturn(2);
					// if (stageID != 10) {
					// calculateScore();
					// gotoUpLoadScore(true);
					// } else {
					// gotoReturn(2);
					// }
				} else if (Common.isKeyPressed(Common.BUTTON_BACK_PRESSED, true)) {
					sysMenuIndex = 0;
					state = STATE_SYSTEM_MENU;
				}
				break;
			case STATE_PLAYER_CHOOSE :
				if (Common.isKeyPressed(Common.BUTTON_BACK_PRESSED, true)) {
					gotoCover();
					gotoMainMenu();
				} else if (isLeftPressed()) {
					playerIndex--;
					if (playerIndex < 0) {
						playerIndex = PLAYER_NUM - 1;
					}
				} else if (isRightPressed()) {
					playerIndex++;
					if (playerIndex >= PLAYER_NUM) {
						playerIndex = 0;
					}
				} else if (isOkPressed() || Common.isKeyPressed(Common.BUTTON_OK_PRESSED, true)) {
					fishmanID = playerIndex;
					stageOpenID = 0;
					if (fishmanID == 1 && !isOpenGame) {
						// showPlayerConfirm();
						showMessage(CHOOSE_GRIL_INFO);
						return;
					}
					saveRecord();
					if (bAccessToGame) {
						gotoLoading(0);
					} else {
						gotoCGView(1);
					}
					// 110818
					if (fishmanID == 1) {
						updateAchievement(ACH_GIRL_PLAYER);
					}
				}
				break;
			case STATE_SHOP :
				int bakIndex = shopIndex;
				if (Common.isKeyPressed(Common.BUTTON_BACK_PRESSED, true)) {
					// gotoMap();
					freeShopRes(); // 110717
					state = STATE_GAME;
				} else if (isUpPressed()) {
					shopIndex--;
					if (shopIndex < 0) {
						shopIndex = BOAT_NUM - 1;
					}
				} else if (isDownPressed()) {
					shopIndex++;
					if (shopIndex >= BOAT_NUM) {
						shopIndex = 0;
					}
				} else if (isOkPressed() || Common.isKeyPressed(Common.BUTTON_OK_PRESSED, true)) {
					if (shopIndex <= boatID) {
						showMessage(STR_BOAT_BUY_FAIL0);
					} else if (gold < BOAT_PRICE[shopIndex]) {
						// if (!isOpenGame) {
						// showMessage(STR_BOAT_BUY_FAIL1);
						// } else {
						// gotoSmsUI(SMS_BUY_GOLD);
						// }
						showMessage(STR_GOLD_LACK);
					} else if (shopIndex > boatID + 1) {
						showMessage(STR_BOAT_BUY_FAIL2);
					} else {
						gold -= BOAT_PRICE[shopIndex];
						boatID = shopIndex;
						saveRecord();
						fishman.setBoat(boatID);
						fishman.setHook(boatID);
						fishman.initAct();
						showMessage(STR_BOAT_BUY_SUCCESS);
						updateAchBoat();
					}
				}
				if (shopIndex != bakIndex) {
					changeShopInfo();
				}
				// if (shopIndex < 4) {
				// infoTextImg = Tool.createImage("/boatInfo" + shopIndex +
				// "2.png");
				// } else if (shopIndex < 7 && shopIndex >= 4) {
				// infoTextImg = Tool.createImage("/diverinfo" + (shopIndex - 4) +
				// "2.png");
				// } else {
				// infoTextImg = Tool.createImage("/toolInfo" + (shopIndex - 6) +
				// ".png");
				// }
				break;
			case STATE_EMPLOY_SHOP :
				handleEmployShopKey();
				break;
			case STATE_MAP :
				if (Common.isKeyPressed(Common.SOFT_FIRST_PRESSED, true)) {
					sysMenuIndex = 0;
					gotoMapMenu();
				} else if (Common.isKeyPressed(Common.SOFT_LAST_PRESSED, true)) {
					// gotoShop();
				} else if (isOkPressed()) {
					if (isOpenGame) {
						if (mapPartIndex <= stageOpenID) {
							gotoLoading(mapPartIndex);
						} else {
							showMessage(STR_STAGE_NOT_OPEN);
						}
					} else {
						// gotoSmsUI(SMS_BUY_GAME);
					}
				}
				break;
			// case STATE_GAME_BEGIN:
			// if (Common.isKeyPressed(Common.BUTTON_BACK_PRESSED, true)
			// || isOkPressed()) {
			// state = STATE_GAME;
			// }
			// break;
			case STATE_TIME_OVER :
				if (Common.isKeyPressed(Common.BUTTON_BACK_PRESSED, true) || isOkPressed()) {
					handler.post(new Runnable() {
						public void run() {
							domobAd();
						}
					});
					// if (stageID == 10 || stageID == 11 || score >= goal) {
					// ;
					// } else {
					// // gotoSmsUI(SMS_BUY_TIME);
					// gotoSummary();
					// }
				}
				Common.clearKeyStates();
				break;
			case STATE_GAME_SUMMARY :
				// if (Common.isKeyPressed(Common.BUTTON_BACK_PRESSED, true) ||
				// Common.isKeyPressed(Common.BUTTON_OK_PRESSED, true) ||
				// isOkPressed()) {
				// scrollText = null;
				// if (stageID == 10) {// whb add ��ѧ�ز������ͼ�����ǽ��뵽����ѡ��
				// if (menuBgImg == null) {
				// menuBgImg = Tool.createImage("/menubg.png");
				// }
				// clearTeachData();
				// gotoPlayerChoose();
				// } else if (stageID == 11) {// whb add ����ģʽ�������ͼ�����ǽ��뵽���˵�
				// gotoReturn(2);
				// liveGameStageID = 1;// ��ԭ�ؿ�
				// } else {
				// gotoReturn(1);
				// }
				// }
				if (Common.isKeyPressed(Common.BUTTON_BACK_PRESSED, true)) {
					scrollText = null;
					if (stageID == 10) {// whb add ��ѧ�ز������ͼ�����ǽ��뵽����ѡ��
						if (bFirstTutorOver) {
							// if (menuBgImg == null) {
							// menuBgImg = Tool.createImage("/menubg.png");
							// }
							clearTeachData();
							gotoPlayerChoose(false);
						} else {
							gotoFirstTutorOver();
						}
					} else if (stageID == 11) {// whb add ����ģʽ�������ͼ�����ǽ��뵽���˵�
						gotoReturn(2);
						liveGameStageID = 1;// ��ԭ�ؿ�
					} else {
						gotoReturn(1);
					}
				} else if (Common.isKeyPressed(Common.BUTTON_OK_PRESSED, true)) {
					gotoLoading(stageID);
				}
				break;
			case STATE_FIRST_TUTOR_OVER :
				if (isOkPressed()) {
					bFirstTutorOver = true;
					gotoLoading(10);
				}
				break;
			case STATE_GAME_BEGIN :
				if (Common.isAnyKeyPressed()) {
					state = STATE_GAME;
					beginImg = null;
				}
				Common.clearKeyStates();
				break;
			case STATE_GAME :
				if (Common.isKeyPressed(Common.BUTTON_MENU_PRESSED, true)) {
					// sysMenuIndex = 0;
					// state = STATE_SYSTEM_MENU;
					gotoSystemMenu();
				}
				// whb add ��Ӷ�̵�
				else if (Common.isKeyPressed(Common.BUTTON_BACK_PRESSED, true)) {
					// ���ǽ�ѧ�ؿ������Խ���
					if (stageID != 10) {
						gotoEmployShop();
					}
					// else if (teachID >= TEACH_HANDUP) {
					// gotoEmployShop();
					// employShopIndex = 0;
					// }
				}
				if (fishman.actState == Fishman.ACT_STATE_NORMAL
				/* && diver.actState != Diver.ACT_STATE_CATCH */) {
					if (diver == null || diver.actState != Diver.ACT_STATE_CATCH) {
						boolean bRun = false;
						if (isLeftPressed()) {
							bRun = true;
							fishman.setDir(MapElement.DIR_LEFT);
						} else if (isRightPressed()) {
							bRun = true;
							fishman.setDir(MapElement.DIR_RIGHT);
						} else if (isOkPressed() || isDownPressed()) {
							fishman.switchHookDir();
						}
						if (bRun) {
							fishman.act(Fishman.ACT_WALK);
						} else {
							fishman.act(Fishman.ACT_STAND);
						}
					}
				}
				// whb add ǱˮԱ�ƶ�
				if (diver != null && diver.actState == Diver.ACT_STATE_CATCH) {
					if (isLeftPressed()) {
						diver.curDir = MapElement.DIR_LEFT;
						diver.actID = Diver.ACT_WALK;
					} else if (isRightPressed()) {
						diver.curDir = MapElement.DIR_RIGHT;
						diver.actID = Diver.ACT_WALK;
					} else if (isUpPressed()) {
						diver.curDir = MapElement.DIR_UP;
						diver.actID = Diver.ACT_WALK;
					} else if (isDownPressed()) {
						diver.curDir = MapElement.DIR_DOWN;
						diver.actID = Diver.ACT_WALK;
					}
				}
				/*************************** ǱˮԱ�л����� *****************************/
				if (Common.isKeyPressed(Common.KEY_NUM0_PRESSED, true)) {
					// if (stageID == 10) {
					// if (teachID >= TEACH_DIVER_CONTROL) {
					// if (diver.actState != Diver.ACT_STATE_CATCH) {
					// showMessage("���Ѿ�����ǱˮԱ���ƹ��ܣ��������Ϸ��");
					// diver.actState = Diver.ACT_STATE_CATCH;
					// diver.setControlDiverStep(diverID);
					// teachID = TEACH_7;
					// } else {
					// showMessage("���Ѿ��ر�ǱˮԱ���ƹ��ܣ��������Ϸ��");
					// diver.actState = Diver.ACT_STATE_NORMAL;
					// diver.setAutoDiverStep(diverID);
					// }
					// } else {
					// // showMessage("����δ����ǱˮԱ���ƹ��ܣ��������������Ϸ��");
					// }
					// break;
					// }
					if (bTutorStage) {
						if (isHasControl && diver != null) {
							if (diver.actState != Diver.ACT_STATE_CATCH) {
								showMessage("���Ѿ�����ǱˮԱ���ƹ��ܣ��������Ϸ��");
								diver.actState = Diver.ACT_STATE_CATCH;
								diver.setControlDiverStep(diverID);
							} else {
								showMessage("���Ѿ��ر�ǱˮԱ���ƹ��ܣ��������Ϸ��");
								diver.actState = Diver.ACT_STATE_NORMAL;
								diver.setAutoDiverStep(diverID);
							}
						}
					} else {
						if (isHasControl) {
							if (diver != null) {
								if (diver.actState != Diver.ACT_STATE_CATCH) {
									showMessage("���Ѿ�����ǱˮԱ���ƹ��ܣ��������Ϸ��");
									diver.actState = Diver.ACT_STATE_CATCH;
									diver.setControlDiverStep(diverID);
									if (diverID < 2) { // 110720
										gotoEmployShop();
									}
								} else {
									showMessage("���Ѿ��ر�ǱˮԱ���ƹ��ܣ��������Ϸ��");
									diver.actState = Diver.ACT_STATE_NORMAL;
									diver.setAutoDiverStep(diverID);
								}
							} else {
								gotoEmployShop(); // 110116
							}
						} else { // QQ 0214
							gotoEmployShop(); // 110116
						}
					}
					// if (isHasControl) {
					// if (diver != null) {
					// if (diver.actState != Diver.ACT_STATE_CATCH) {
					// showMessage("���Ѿ�����ǱˮԱ���ƹ��ܣ��������Ϸ��");
					// diver.actState = Diver.ACT_STATE_CATCH;
					// diver.setControlDiverStep(diverID);
					// } else {
					// showMessage("���Ѿ��ر�ǱˮԱ���ƹ��ܣ��������Ϸ��");
					// diver.actState = Diver.ACT_STATE_NORMAL;
					// diver.setAutoDiverStep(diverID);
					// }
					// } else {
					// //showMessage("����δ��ӶǱˮԱ����������̵��Ӷ��");
					// gotoEmployShop(); //110116
					// }
					// } else { //QQ 0214
					// //showMessage("��δ����ǱˮԱ���ƹ��ܣ�������̵꣬����ǱˮԱ���ƹ��ܡ�");
					// gotoEmployShop(); //110116
					// }
				}
				/**
				 * *************************************************************
				 * **** ****
				 */
				if (bHandupAble && Common.isKeyPressed(Common.KEY_NUM1_PRESSED, true)) {
					fishman.gotoHandup();
					flag1_Tip = true;
					isCloseMessage = false;
				}
				// �������--> ��Ϊʹ��ȫ�����ҵ���
				if (Common.isKeyPressed(Common.KEY_STAR_PRESSED, true)) {
					// if (noSharkToolNum > 0) {
					// isSharkCannotCatch = true;
					// toolNum++;
					// } else if (stageID == 10 && teachID >= 2) {
					// isSharkCannotCatch = true;
					// teachID = 3;
					// }
					// if (bTutorStage) {
					// if (teachID == TEACH_SHARK_TOOL) {
					// screenVibrate(magnetTotal, 2);
					// magnetCount = 0;
					// isAllFishToScore = true;
					// allFishToScoreNum--;
					// isSharkAppear = false;
					// teachID = TEACH_3;
					// } else if (teachID > TEACH_SHARK_TOOL && teachID !=
					// TEACH_HANDUP) {
					// if (allFishToScoreNum > 0) {
					// screenVibrate(magnetTotal, 2);
					// magnetCount = 0;
					// isAllFishToScore = true;
					// allFishToScoreNum--;
					// } else {
					// showMessage("���Ѿ�û��ħ�ô������߿���ʹ�ã�");
					// }
					// }
					// }
					// else {
					if (allFishToScoreNum > 0) {
						screenVibrate(magnetTotal, 2);
						magnetCount = 0;
						isAllFishToScore = true;
						allFishToScoreNum--;
						// isSharkAppear = false;
						// 110818
						magnetUseTime++;
						if (magnetUseTime >= 5) {
							updateAchievement(ACH_MAGNET_USE_5);
						}
					} else {
						// gotoSmsUI(SMS_BUY_MAGNET);
						// showMessage("���Ѿ�û��ħ�ô������߿���ʹ�ã�");
						if (bTutorStage) {
							showMessage("���Ѿ�û��ħ�ô�������ʹ�ã�");
						} else {
							gotoMagnetHint(false); // 0219
						}
					}
					// }
				}
				if (Common.isKeyPressed(Common.KEY_NUM3_PRESSED, true)) {
					if (bTutorStage) {
						showMessage("��ǰ�ؿ�����������ֻ��\n�뷵�ء�");
					} else if (boatID < BOAT_NUM - 1) {
						gotoShop();
					} else {
						showMessage("���Ѿ�ӵ����߼��洬��\n�뷵�ء�");
					}
				}
				break;
			// whb add
			case STATE_PLAYER_INFO_CONFIRM :
				if (Common.isKeyPressed(Common.BUTTON_BACK_PRESSED, true)) {
					state = STATE_PLAYER_CHOOSE;
				} else if (isLeftPressed()) {
				} else if (isRightPressed()) {
				} else if (isOkPressed() || Common.isKeyPressed(Common.BUTTON_OK_PRESSED, true)) {
					// fishmanID = playerIndex;
					// stageOpenID = 0;
					// isMaySelectGirl = true;
					// saveRecord();
					// gotoMap();
					state = STATE_PLAYER_CHOOSE;
				}
				break;
			// whb add
			// case STATE_SMS:
			// handleKeyPressed_SMS();
			// break;
			case STATE_CG :
				if (Common.isKeyPressed(Common.BUTTON_BACK_PRESSED, true))
					if (state_CGID == 0) {
						// allFishToScoreNum = 3;
						bFirstTutorOver = false;
						gotoLoading(10);
					} else if (state_CGID == 1) {
						gotoMap();
					}
				break;
			// case STATE_QQ_SHOWSCORE :// QQ ���ְ�
			// if (Common.isKeyPressed(Common.BUTTON_BACK_PRESSED, true)) {
			// state = STATE_MAIN_MENU;
			// }
			// if (Common.isKeyPressed(Common.BUTTON_OK_PRESSED, true)) {
			// //qqBox.startMBox(MBoxClient.CONN_TYPE_DIRECT_NETWORK,
			// MBoxClient.PAGE_MAIN);
			// }
			// break;
			// case STATE_QQ_UPLOADSCORE :
			// if (Common.isKeyPressed(Common.BUTTON_BACK_PRESSED, true)) {
			// if (bBackToMain) {
			// gotoReturn(2);
			// } else if (stageID == 9) {
			// if (isGameFail) {
			// //gotoReturn(2);
			// gotoReturn(1); //0219
			// } else {
			// gotoGameOver();
			// }
			// } else if (stageID == 11) {
			// // gotoMainMenu();
			// gotoReturn(2);
			// } else {
			// // gotoShop();
			// if (isGameFail) {
			// //gotoReturn(2);
			// gotoReturn(1); //0219
			// } else {
			// gotoReturn(1);
			// }
			// }
			// }
			//
			// if (Common.isKeyPressed(Common.BUTTON_OK_PRESSED, true)) {
			// bRequestNotified = false;
			// //qqBox.uploadUserData(playerScore, "", 0, 0, "", "", "");
			// state = STATE_QQ_UPLOADSCORE_RESULT;
			// }
			// break;
			// case STATE_QQ_UPLOADSCORE_RESULT :
			// if (Common.isKeyPressed(Common.BUTTON_BACK_PRESSED, true)) {
			// if (bBackToMain) { //QQ 0407
			// gotoReturn(2);
			// } else if (stageID == 9) {
			// if (isGameFail) {
			// //gotoReturn(2);
			// gotoReturn(1); //0219
			// } else {
			// gotoGameOver();
			// }
			// } else if (stageID == 11) {
			// gotoReturn(2);
			// } else {
			// if (isGameFail) {
			// //gotoReturn(2);
			// gotoReturn(1); //0219
			// } else {
			// gotoReturn(1);
			// }
			// }
			// }
			//
			// if (Common.isKeyPressed(Common.BUTTON_OK_PRESSED, true)) {
			// try {
			// //qqBox.startMBox(MBoxClient.CONN_TYPE_DIRECT_NETWORK,
			// MBoxClient.PAGE_MAIN);
			// } catch (Exception e) {
			// }
			// }
			// break;
			case STATE_SMS_UI :
				handleSMSKey();
				break;
			// case STATE_GAME_OVER :
			// if (Common.isKeyPressed(Common.BUTTON_BACK_PRESSED, true)) {
			// isShowLiveGame = false;
			// gotoReturn(2);
			// }
			// break;
			case STATE_MAP_MENU :
				if (Common.isKeyPressed(Common.BUTTON_BACK_PRESSED, true)) {
					freeMenuRes();
					state = STATE_MAP;
				} else if (isUpPressed()) {
					sysMenuIndex--;
					if (sysMenuIndex < 0) {
						sysMenuIndex = MAP_MENU_NUM - 1;
					}
				} else if (isDownPressed()) {
					sysMenuIndex++;
					if (sysMenuIndex >= MAP_MENU_NUM) {
						sysMenuIndex = 0;
					}
				} else if (isOkPressed() || Common.isKeyPressed(Common.BUTTON_OK_PRESSED, true)) {
					// whb for qq
					switch (sysMenuIndex) {
						case 0 : // ������Ϸ
							freeMenuRes();
							state = STATE_MAP;
							break;
						// case 1 :
						// gotoShop();
						// break;
						case 1 : // ����
							setSound(!bSoundOn);
							if (bSoundOn) {
								menuItemImgs[1] = loadResImage("/m_soundOn.png");
							} else {
								menuItemImgs[1] = loadResImage("/m_soundOff.png");
							}
							break;
						case 2 : // ��Ϸ����
							scrollText = new ScrollText(STR_HELP, uiWidth - 20, uiHeight - 20 - btnBackImg.getHeight(), fontH, 2, font);
							preState = state;
							state = STATE_HELP;
							break;
						case 3 : // �������˵�+
							gotoCover();
							gotoMainMenu();
							break;
						default :
							break;
					}
				}
				break;
			case STATE_MORE_GAME :
				if (isUpPressed() || isDownPressed() || isLeftPressed() || isRightPressed()) {
					// scrollText = null;
					moreGameIndex = ++moreGameIndex % MORE_GAME_NUM;
				} else if (Common.isKeyPressed(Common.BUTTON_OK_PRESSED, true)) {
					DoMidlet.gotoURL(DoMidlet.instance.getAppProperty(MOREGAME_URL[moreGameIndex]));
					exit();
				} else if ((Common.isKeyPressed(Common.BUTTON_BACK_PRESSED, true))) {
					exit();
				}
				break;
			case STATE_EXIT :
				if (Common.isKeyPressed(Common.BUTTON_OK_PRESSED, true)) {
					exit();
				} else if ((Common.isKeyPressed(Common.BUTTON_BACK_PRESSED, true))) {
					state = STATE_MAIN_MENU;
				}
				break;
			// 0219
			// case STATE_GIFT_HINT :
			// giftHintHandleKey();
			// break;
			case STATE_MAGNET_HINT :
				magnetHintHandleKey();
				break;
			case STATE_LIVEGAME_CONTINUE :
				if (Common.isKeyPressed(Common.BUTTON_OK_PRESSED, true) || Common.isKeyPressed(Common.BUTTON_BACK_PRESSED, true)) {
					calculateScore();
					// score = 0;
					state = STATE_GAME;
				}
				break;
			case STATE_LIVEGAME_SUCCESS :
				if (Common.isKeyPressed(Common.BUTTON_OK_PRESSED, true) || Common.isKeyPressed(Common.BUTTON_BACK_PRESSED, true)) {
					gotoReturn(2);
				}
				break;
			default :
				break;
		}
	}
	private void showPlayerConfirm() {
		scrollText = null;
		// int height = uiHeight / 3;
		// if (height < (fontH << 2) + 5) {
		// height = (fontH << 2) + 5;
		// }
		scrollText = new ScrollText(CHOOSE_GRIL_INFO, MESSAGE_WIDTH, MESSAGE_HEIGHT, fontH, 2, font);
		state = STATE_PLAYER_INFO_CONFIRM;
	}
	/**
	 * �ͷŰ�������
	 */
	public void handleKeyReleased() {
		if (!Common.isAnyKeyReleaed()) {
			return;
		}
		if (state == STATE_GAME) {
			if (fishman.actID == Fishman.ACT_WALK) {
				fishman.act(Fishman.ACT_STAND);
			}
			// whb add
			if (diver != null && diver.actState == Diver.ACT_STATE_CATCH) {
				diver.actID = Diver.ACT_STAND;
			}
			clearMoveTo();
			Common.clearKeyStates();
		}
	}
	public void drawGame(Graphics g) { // 0219
		// if (skyImg != null) {
		// Tool.drawImage(g, skyImg, -viewMapX, viewMapY, Tool.TRANS_NONE);
		// }
		// if (bgImg != null) {
		// Tool.drawImage(g, bgImg, -viewMapX, viewMapY + skyImg.getHeight() -
		// 20, Tool.TRANS_NONE);
		// }
		Common.fillUIRect(g, 0x0000ff);
		if (screenVibrateTimes > 0) {
			vibrateX = (-1 + (screenVibrateTimes & 1) * 2) * screenVibrateScope;
			vibrateY = (-1 + (screenVibrateTimes & 1) * 2) * screenVibrateScope;
			screenVibrateTimes--;
		} else {
			vibrateX = 0;
			vibrateY = 0;
		}
		viewMapX += vibrateX;
		viewMapY += vibrateY;
		if (bgImg != null) {
			Tool.drawImage(g, bgImg, -viewMapX, -viewMapY, Tool.TRANS_NONE);
		}
		Tool.drawImage(g, banksideImg, -viewMapX, BUCKET_Y, Tool.TRANS_NONE);
		int bucketID = 0;
		if (bHandupAble || fishman.actID == Fishman.ACT_HANDUP) {
			bucketID = 1;
		}
		int yy = 55;
		Tool.drawTile(g, bucketImg, bucketID, bucketImg.getWidth() >> 1, bucketImg.getHeight(), Tool.TRANS_NONE, -viewMapX, BUCKET_Y - yy);
		pad1X = -viewMapX;
		if (waterBackImg != null) {
			int imgW = waterBackImg.getWidth();
			int mapX = -(Tool.countTimes >> 1) % imgW;
			int drawX = -(viewMapX - mapX) % imgW;
			int num = uiWidth / imgW + 1;
			for (int i = 0; i <= num; i++) {
				Tool.drawImage(g, waterBackImg, drawX + imgW * i, WATER_FLOAT_Y - 10 - viewMapY, Tool.TRANS_NONE);
			}
		}
		if (fishman != null) {
			fishman.draw(g, viewMapX, viewMapY, 0, 0);
			int y = FISHMAN_POSY_INIT - 70;
			if (diver == null || diver.actState != Diver.ACT_STATE_CATCH) {// ���ǿ���ǱˮԱ�Ļ���������������ָʾ
				Tool.drawTile(g, fingerImg, (Tool.countTimes >> 2) % 2, fingerImg.getWidth() / 2, fingerImg.getHeight(), 0, fishman.mapX - viewMapX, y);
			}
			// whb add ָʾ��ʾ
			// if (fishman.boatCurFishNum >= fishman.boatFullFishNum
			// && !bHandupAble) {
			// if ((Tool.countTimes >> 1) % 2 == 1) {
			// g.setClip(0, 150, SCREEN_WIDTH, 40);
			// g.drawImage(tipImg[6], SCREEN_WIDTH >> 1, 150, 17);
			// Tool.drawImage(g, mapPointerImg, 5, 50, Tool.TRANS_ROT90);
			// }
			// }
			// �����㽻��ʱЧ��
			if (fishman.actID == Fishman.ACT_HANDUP) {
				int sx = fishman.mapX;
				int dx = 20;
				int index = (fishman.handUpTimes >> 1) % FISH_HANDUP_NUM;
				Tool.drawTile(g, fishHandupImg, index, fishHandupImg.getWidth() / FISH_HANDUP_NUM, fishHandupImg.getHeight(), Tool.TRANS_NONE, sx - (sx - dx) * index / (FISH_HANDUP_NUM - 1), fishHandupYs[index] - viewMapY);
			}
		}
		// ���ƽ�����ʾ
		if (bHandupAble) { // 110720
			if (!bTutorStage || bTutorOver) {
				drawHand(g, pad1X + ((pad1W - handW) >> 1), pad1Y + (pad1H >> 1));
			}
			if ((Tool.countTimes & 4) < 2) {
				Tool.drawImage(g, handupImg, 0, BUCKET_Y - 25 - handupImg.getHeight(), Tool.TRANS_NONE);
			}
		} else if (fishman.isBoatFull() && fishman.actID != Fishman.ACT_HANDUP) {
			int dy = Tool.JUMP_RANGE[(Tool.countTimes >> 1) % Tool.JUMP_RANGE.length];
			Tool.drawImage(g, handupHintImg, 0, BUCKET_Y - 30 - handupHintImg.getHeight() + dy, Tool.TRANS_NONE);
		}
		// whb ����ħ�ô���
		if (magnetCount < magnetTotal) {
			magnetCount++;
			int y = 15;
			Tool.drawTile(g, magnetImg, (Tool.countTimes >> 2) % 4, magnetImg.getWidth() / 4, magnetImg.getHeight(), 0, fishman.mapX - viewMapX - 5, fishman.mapY - viewMapY + y);
		}
		int s = bubble.size();
		if (s > 0) {
			for (int i = 0; i < s; i++) {
				Bubble2 b = (Bubble2) bubble.elementAt(i);
				Tool.drawTile(g, bubble2Img, (bubble2Count >> 1) % 3, bubble2Img.getWidth() / 3, bubble2Img.getHeight(), 0, b.x, b.y);
			}
			bubble2Count++;
			if (bubble2Count == 6) {
				bubble.removeAllElements();
				bubble2Count = 0;
			}
		}
		// ��ʧ�����ͷ
		for (int i = 0; i < fishbone.size(); i++) {
			FishBone fb = (FishBone) fishbone.elementAt(i);
			if (fb.displayCount > 0) {
				if (fb.displayCount < 40 && (Tool.countTimes >> 1) % 3 != 0) {
					if (fb.dir == MapElement.DIR_RIGHT) {
						Tool.drawImage(g, fishboneImg, fb.x - viewMapX, fb.y - viewMapY, 0);
					} else {
						Tool.drawImage(g, fishboneImg, fb.x - viewMapX, fb.y - viewMapY, Tool.TRANS_MIRROR);
					}
				}
			}
		}
		drawFishSet(g);
		if (waterFrontImg != null) {
			int imgW = waterFrontImg.getWidth();
			int mapX = -(Tool.countTimes) % imgW;
			int drawX = -(viewMapX - mapX) % imgW;
			int num = uiWidth / imgW + 1;
			for (int i = 0; i <= num; i++) {
				Tool.drawImage(g, waterFrontImg, drawX + imgW * i, WATER_FLOAT_Y + WATER_FLOAT_DY[(Tool.countTimes >> 1) % WATER_FLOAT_DY.length] - viewMapY, Tool.TRANS_NONE);
			}
			// Tool.drawImage(g, waterFrontImg, 0, 130, Tool.TRANS_NONE);
		}
		drawBubbleSet(g);
		if (propsBubble != null) {
			propsBubble.draw(g, viewMapX, viewMapY, 0, 0);
		}
		for (int i = 0; i < FORE_OBJECT_NUM; i++) {
			Tool.drawImage(g, foreObjImg[i], foreObjMapX[i] - viewMapX, uiHeight - foreObjImg[i].getHeight(), Tool.TRANS_NONE);
		}
		Common.setUIClip(g);
		// whb add
		drawDiver(g);
		if (diver != null && diver.actState == Diver.ACT_STATE_CATCH) {// �ǿ���ǱˮԱ�Ļ���������������ָʾ
			Tool.drawTile(g, fingerImg, (Tool.countTimes >> 2) % 2, fingerImg.getWidth() / 2, fingerImg.getHeight(), 0, diver.mapX - viewMapX + 25, diver.mapY - viewMapY - 20);
		}
		drawGameUI(g);
		drawFlyNumSet(g);
		// drawButtonMenu(g);
		// if (stageID != 10) {
		// drawButton(g, btnShopImg, BUTTON_ALIGN_RIGHT);
		// } else if (stageID == 10 && teachID >= TEACH_DIVER) {
		// drawButton(g, btnShopImg, BUTTON_ALIGN_RIGHT);
		// }
		if (state == STATE_GIFT_HINT || state == STATE_MAGNET_HINT || state == STATE_EMPLOY_SHOP || state == STATE_SHOP || state == STATE_GAME_SUMMARY) {
			// ...
		} else {
			drawTouchPad(g);
		}
		// whb add
		if (stageID == 10) {
			// Common.setUIClip(g);
			// int tipImgY = 150;
			// switch (teachID) {
			// case TEACH_SHARK :
			// if (isSharkAppear && (Tool.countTimes >> 2) % 4 != 3) {
			// // System.out.println("�����û");
			// // Tool.drawString(g, "�����û",
			// // (SCREEN_WIDTH - font.stringWidth("�����û")) >> 1,
			// // 150, 0xff0000, 0, 0);
			// g.drawImage(tipImg[0], SCREEN_WIDTH >> 1, tipImgY, 17);
			// }
			// break;
			// case TEACH_SHARK_TOOL :
			// if (isSharkAppear && (Tool.countTimes >> 2) % 4 != 3) {
			// // Tool.drawString(g, "�����û",
			// // (SCREEN_WIDTH - font.stringWidth("�����û")) >> 1,
			// // 150, 0xff0000, 0, 0);
			// // Tool.drawString(g, "��*����ס������",
			// // (SCREEN_WIDTH - font.stringWidth("��*����ס������")) >> 1,
			// // 175, 0xff0000, 0, 0);
			// Common.setUIClip(g);
			// g.drawImage(tipImg[1], SCREEN_WIDTH >> 1, tipImgY, 17);
			// // g.drawImage(tipImg[1], SCREEN_WIDTH >> 1, 170, 17);
			//
			// drawHand(g, pad2X + ((pad2W - handW) >> 1), pad2Y + (pad2H >>
			// 2));
			// }
			// break;
			// case TEACH_HANDUP :
			// if ((Tool.countTimes >> 2) % 4 != 3) {
			// // Tool.drawString(g, "�����Ļ�����",
			// // (SCREEN_WIDTH - font.stringWidth("�����Ļ�����")) >> 1,
			// // 150, 0xff0000, 0, 0);
			// // Tool.drawString(
			// // g,
			// // "�����̵��ӶǱˮԱ",
			// // (SCREEN_WIDTH - font.stringWidth("�����̵��ӶǱˮԱ")) >> 1,
			// // 175, 0xff0000, 0, 0);
			// g.drawImage(tipImg[2], SCREEN_WIDTH >> 1, tipImgY, 17);
			// }
			// // ָʾ��ʾ
			//
			// Tool.drawImage(g, mapPointerImg, SCREEN_WIDTH - 40, SCREEN_HEIGHT
			// - 48 - (((Tool.countTimes >> 2) % 3) << 1), 0);
			// break;
			// case TEACH_DIVER_CONTROL :
			// if ((Tool.countTimes >> 2) % 4 != 3) {
			// // Tool.drawString(
			// // g,
			// // "���0�����ɿ���ǱˮԱ�ƶ�",
			// // (SCREEN_WIDTH - font.stringWidth("���0�����ɿ���ǱˮԱ�ƶ�")) >> 1,
			// // 150, 0xff0000, 0, 0);
			// g.drawImage(tipImg[5], SCREEN_WIDTH >> 1, tipImgY, 17);
			// }
			// break;
			// }
		}
		// whb add ָʾ��ʾ
		// if (fishman.boatCurFishNum >= fishman.boatFullFishNum && !bHandupAble
		// && !flag1_Tip//
		// && !(isSharkAppear && teachID == TEACH_SHARK_TOOL)//
		// && teachID != TEACH_HANDUP && teachID != TEACH_DIVER_CONTROL//
		// ) {
		// if (diver != null) {
		// if (diver.actState != Diver.ACT_STATE_CATCH) {
		// if (stageID == 10 && (Tool.countTimes >> 2) % 4 != 3) {
		// g.setClip(0, 150, SCREEN_WIDTH, 40);
		// g.drawImage(tipImg[6], SCREEN_WIDTH >> 1, 150, 17);
		// }
		// }
		// } else {
		// if (stageID == 10 && (Tool.countTimes >> 2) % 4 != 3) {
		// g.setClip(0, 150, SCREEN_WIDTH, 40);
		// g.drawImage(tipImg[6], SCREEN_WIDTH >> 1, 150, 17);
		// }
		// }
		//
		// int y = 50;
		// Tool.drawImage(g, mapPointerImg, 5 - (Tool.countTimes >> 2) % 3, y,
		// Tool.TRANS_ROT90);
		// }
		// whb add ����ģʽ����ÿ�׶ε���ʾ
		if (stageID == 11) {
			// if (liveGameStageID < 10) {
			// Tool.drawImage(g, stageIDImg, x, y, Tool.TRANS_NONE);
			// Tool.drawImageNum(g, numFontImg1,
			// String.valueOf(liveGameStageID), x + stageIDImg.getWidth() / 3 +
			// 5, y + 2, numFontImg1.getWidth() / 10, 0);
			// } else {
			// Tool.drawTile(g, stageIDImg, 0, stageIDImg.getWidth() / 3,
			// stageIDImg.getHeight(), 0, x, 33);
			// Tool.drawTile(g, stageIDImg, 2, stageIDImg.getWidth() / 3,
			// stageIDImg.getHeight(), 0, x + 10 + stageIDImg.getWidth() * 2 /
			// 3, y);
			// Tool.drawImageNum(g, numFontImg1,
			// String.valueOf(liveGameStageID), x + stageIDImg.getWidth() / 3 +
			// 2, y + 2, numFontImg1.getWidth() / 10, 0);
			// }
			String str = String.valueOf(liveGameStageID);
			int x = SCREEN_WIDTH - stageIDImg.getWidth() - (str.length() - 1) * numFontImg1.getWidth() / 10 - 38;
			int y = 50;
			Tool.drawTile(g, stageIDImg, 0, stageIDImg.getWidth() / 3, stageIDImg.getHeight(), 0, x, y);
			Tool.drawImageNum(g, numFontImg1, str, x + stageIDImg.getWidth() / 3 + 2, y + 2, numFontImg1.getWidth() / 10, 0);
			Tool.drawTile(g, stageIDImg, 2, stageIDImg.getWidth() / 3, stageIDImg.getHeight(), 0, x + stageIDImg.getWidth() / 3 + numFontImg1.getWidth() / 10 * str.length() + 4, y);
		}
	}
	public void drawTouchPad(Graphics g) {
		// if (pad0Img != null) {
		// Tool.drawImage(g, pad0Img, pad0X, pad0Y, Tool.TRANS_NONE);
		// Tool.drawImage(g, pad0Img, pad0aX, pad0aY, Tool.TRANS_NONE);
		// }
		int dy = Tool.JUMP_RANGE[(Tool.countTimes >> 1) % Tool.JUMP_RANGE.length];
		if (pad0Img != null) {
			// Tool.drawImage(g, btnMenuImg, pad0X + ((pad0W -
			// btnMenuImg.getWidth()) >> 1), pad0Y + ((pad0H -
			// btnMenuImg.getHeight()) >> 1), Tool.TRANS_NONE);
			// Tool.drawImage(g, btnBgImg, pad0X, pad0Y, Tool.TRANS_NONE);
			Tool.drawImage(g, pad0Img, pad0X, pad0Y, Tool.TRANS_NONE);
		}
		if (pad2Img != null) {
			int py = pad2Y;
			if (bPad2Jump) {
				py += dy;
			}
			Tool.drawImage(g, pad2Img, pad2X, py, Tool.TRANS_NONE);
			// if (!bTutorStage) {
			Tool.drawImageNum(g, numFontImgSmall, String.valueOf(allFishToScoreNum), pad2X + pad2W - 2, pad2Y + pad2H - numFontImgSmall.getHeight() - 2, numFontImgSmall.getWidth() / 10, 0);
			// }
		}
		if (pad3Img != null) {
			// if (stageID == 10) {
			// if (teachID >= 6) {
			// Tool.drawImage(g, pad3Img, pad3X, pad3Y, Tool.TRANS_NONE);
			// }
			// } else {
			int py = pad3Y;
			if (bPad3Jump) {
				py += dy;
			}
			// else if (diver != null && diver.actState ==
			// Diver.ACT_STATE_CATCH) {
			// py += dy;
			// }
			// if (diver != null && isHasControl) {
			// Tool.drawImage(g, pad3Img, pad3X, py, Tool.TRANS_NONE);
			// } else {
			// Tool.drawImage(g, pad3aImg, pad3X, py, Tool.TRANS_NONE);
			// }
			if (diver != null && diver.actState == Diver.ACT_STATE_CATCH) {
				Tool.drawImage(g, pad3Img, pad3X, py, Tool.TRANS_NONE);
			} else {
				Tool.drawImage(g, pad3aImg, pad3X, py, Tool.TRANS_NONE);
			}
			// }
		}
		if (pad4Img != null) {
			if (!bSensorAble) {
				Tool.drawImage(g, pad4aImg, pad4X, pad4Y, Tool.TRANS_NONE);
			} else {
				int py = pad4Y;
				if (bPad4Jump) {
					py += dy;
				} else if (bSensorAble) {
					py += dy;
				}
				Tool.drawImage(g, pad4Img, pad4X, py, Tool.TRANS_NONE);
			}
		}
		// �Ľ��
		Tool.drawImage(g, btnYoumiImg, pad4X + btnYoumiImg.getWidth() + pad_X, pad4Y + pad_Y, Tool.TRANS_NONE);
		// if (pad5Img != null) {
		// Tool.drawImage(g, pad5Img, pad5X, pad5Y, Tool.TRANS_NONE);
		// if (!bSoundOn) {
		// //Tool.drawImage(g, padXImg, pad5X + ((pad5W - padXImg.getWidth()) >>
		// 1), pad5Y + ((pad5H - padXImg.getHeight()) >> 1), Tool.TRANS_NONE);
		// Tool.drawImage(g, padXImg, pad5X + ((pad5W - padXImg.getWidth())) -
		// 3, pad5Y + ((pad5H - padXImg.getHeight()))- 3, Tool.TRANS_NONE);
		// }
		// }
		if (pad6Img != null) {
			Tool.drawImage(g, pad6Img, pad6X, pad6Y, Tool.TRANS_NONE);
		}
		// Common.setUIClip(g);
		// g.setColor(0xff0000);
		// g.drawRect(padLeftX, padLeftY, padDirW - 1, padDirH - 1);
		// g.drawRect(padRightX, padRightY, padDirW - 1, padDirH - 1);
		// ujoho
	}
	public void drawHand(Graphics g, int x, int y) {
		Tool.drawTile(g, handImg, (Tool.countTimes >> 2) % 2, handW, handH, Tool.TRANS_NONE, x, y);
	}
	public void drawButton(Graphics g, Image img, int x, int y) {
		int bgX = x;
		int bgY = y;
		int imgX = bgX + ((btnBgImg.getWidth() - img.getWidth()) >> 1);
		int imgY = bgY + ((btnBgImg.getHeight() - img.getHeight()) >> 1);
		Tool.drawImage(g, img, imgX, imgY, Tool.TRANS_NONE);
		Tool.drawImage(g, btnBgImg, bgX, bgY, Tool.TRANS_NONE);
	}
	public void drawButton(Graphics g, Image img, byte align) {
		int bgX = 0;
		int bgY = uiHeight - btnBgImg.getHeight();
		if (align == BUTTON_ALIGN_RIGHT) {
			bgX = uiWidth - btnBgImg.getWidth();
		}
		int imgX = bgX + ((btnBgImg.getWidth() - img.getWidth()) >> 1);
		int imgY = bgY + ((btnBgImg.getHeight() - img.getHeight()) >> 1);
		Tool.drawImage(g, img, imgX, imgY, Tool.TRANS_NONE);
		Tool.drawImage(g, btnBgImg, bgX, bgY, Tool.TRANS_NONE);
	}
	public void drawButtonOk(Graphics g) {
		drawButton(g, btnOkImg, BUTTON_ALIGN_LEFT);
		// Tool.drawImage(g, btnOkImg, 0, uiHeight - btnOkImg.getHeight(),
		// Tool.TRANS_NONE);
	}
	public void drawButtonBack(Graphics g) {
		drawButton(g, btnBackImg, BUTTON_ALIGN_RIGHT);
		// Tool.drawImage(g, btnBackImg, uiWidth - btnBackImg.getWidth(),
		// uiHeight - btnBackImg.getHeight(), Tool.TRANS_NONE);
	}
	public void drawButtonMenu(Graphics g) {
		drawButton(g, btnMenuImg, BUTTON_ALIGN_LEFT);
		// Tool.drawImage(g, btnMenuImg, 0, uiHeight - btnMenuImg.getHeight(),
		// Tool.TRANS_NONE);
	}
	public void drawGameUI(Graphics g) {
		if (SCREEN_HEIGHT == 480) {
			drawGameUILarge(g);
		} else if (SCREEN_HEIGHT == 320) {
			drawGameUIMiddle(g);
		}
	}
	public void drawGameUIMiddle(Graphics g) {
		int barX = 0;
		int barY = 0;
		// Tool.drawImage(g, barImg, barX, barY, Tool.TRANS_NONE);
		// ʱ�ӱ���
		int bar2X = (uiWidth - bar2Img.getWidth()) >> 1;
		int bar2Y = barY;
		Tool.drawImage(g, bar2Img, bar2X, bar2Y, Tool.TRANS_NONE);
		// ʱ��
		int clockX = bar2X + 26;
		int clockY = barY - 2;
		int angle = gameTimesLeft * 360 / gameTimesTotal;
		Common.setUIClip(g);
		g.setColor(0xffffff);
		int w = 36;
		int xx = 5;
		int yy = 7;
		if (angle > 0) {
			if (gameTimesLeft * 4 > gameTimesTotal * 3) {
				g.setColor(0x5B7900);
			} else if (gameTimesLeft * 2 > gameTimesTotal) {
				g.setColor(0xE2E25A);
			} else if (gameTimesLeft * 4 > gameTimesTotal) {
				g.setColor(0xDB8300);
			} else {
				g.setColor(0xDB3400);
			}
			g.fillArc(clockX + xx, clockY + yy, w, w, 90, angle);
		}
		Tool.drawImage(g, clockImg, clockX, clockY, Tool.TRANS_NONE);
		int spaceW = 2;
		int bar0X = bar2X - bar0Img.getWidth() + 22;
		int bar0Y = clockY;
		Tool.drawImage(g, bar0Img, bar0X, bar0Y, Tool.TRANS_NONE);
		// ���
		int numX = bar0X + 45 + spaceW;
		int numY = bar0Y + 8;
		Tool.drawImageNum(g, numFontImg1, String.valueOf(gold), numX, numY, numFontImg1.getWidth() / 10, 0);
		// �÷�
		numX = bar0X + 137 + spaceW; // bar0X + 72 + spaceW; //185 + spaceW;
		numY = bar0Y + 8;
		Tool.drawImageNum(g, numFontImg1, String.valueOf(score), numX, numY, numFontImg1.getWidth() / 10, 0);
		int bar1X = bar2X + bar2Img.getWidth() - 22;
		int bar1Y = clockY;
		Tool.drawImage(g, bar1Img, bar1X, bar1Y, Tool.TRANS_NONE);
		// Ŀ��
		numX = bar1X + 56 + spaceW;
		numY = bar1Y + 8;
		Tool.drawImageNum(g, numFontImg1, String.valueOf(goal), numX, numY, numFontImg1.getWidth() / 10, 0);
		// ��߷�
		numX = bar1X + 153 + spaceW;
		numY = bar1Y + 8;
		Tool.drawImageNum(g, numFontImg1, String.valueOf(highScore), numX, numY, numFontImg1.getWidth() / 10, 0);
	}
	public void drawGameUILarge(Graphics g) {
		int barX = 0;
		int barY = 0;
		// Tool.drawImage(g, barImg, barX, barY, Tool.TRANS_NONE);
		// ʱ�ӱ���
		int bar2X = (uiWidth - bar2Img.getWidth()) >> 1;
		int bar2Y = barY;
		Tool.drawImage(g, bar2Img, bar2X, bar2Y, Tool.TRANS_NONE);
		// ʱ��
		int clockX = bar2X + 36; // (uiWidth - clockImg.getWidth()) >> 1; //295
									// + spaceW;
		int clockY = barY - 2;
		int angle = gameTimesLeft * 360 / gameTimesTotal;
		Common.setUIClip(g);
		g.setColor(0xffffff);
		int w = 50;
		int xx = 5;
		int yy = 7;
		// g.fillArc(clockX + xx, clockY + yy, w, w, 0, 360);
		if (angle > 0) {
			if (gameTimesLeft * 4 > gameTimesTotal * 3) {
				g.setColor(0x5B7900);
			} else if (gameTimesLeft * 2 > gameTimesTotal) {
				g.setColor(0xE2E25A);
			} else if (gameTimesLeft * 4 > gameTimesTotal) {
				g.setColor(0xDB8300);
			} else {
				g.setColor(0xDB3400);
			}
			g.fillArc(clockX + xx, clockY + yy, w, w, 90, angle);
		}
		Tool.drawImage(g, clockImg, clockX, clockY, Tool.TRANS_NONE);
		int spaceW = 2;
		int bar0X = bar2X - bar0Img.getWidth() - 23; // clockX -
														// bar0Img.getWidth();
		int bar0Y = clockY;
		Tool.drawImage(g, bar0Img, bar0X, bar0Y, Tool.TRANS_NONE);
		// ���
		int numX = bar0X + 64 + spaceW; // bar0X + 48 + spaceW;
		int numY = bar0Y + 12; // barY + ((barImg.getHeight() -
								// numFontImg1.getHeight()) >> 1) + 1;
		Tool.drawImageNum(g, numFontImg1, String.valueOf(gold), numX, numY, numFontImg1.getWidth() / 10, 0);
		// �÷�
		numX = bar0X + 182 + spaceW; // bar0X + 72 + spaceW; //185 + spaceW;
		numY = bar0Y + 12;
		Tool.drawImageNum(g, numFontImg1, String.valueOf(score), numX, numY, numFontImg1.getWidth() / 10, 0);
		int bar1X = bar2X + bar2Img.getWidth() + 23; // clockX +
														// clockImg.getWidth();
		int bar1Y = clockY;
		Tool.drawImage(g, bar1Img, bar1X, bar1Y, Tool.TRANS_NONE);
		// Ŀ��
		numX = bar1X + 75 + spaceW; // 437 + spaceW;
		numY = bar1Y + 12;
		Tool.drawImageNum(g, numFontImg1, String.valueOf(goal), numX, numY, numFontImg1.getWidth() / 10, 0);
		// ��߷�
		numX = bar1X + 199 + spaceW; // 556 + spaceW;
		numY = bar1Y + 12;
		Tool.drawImageNum(g, numFontImg1, String.valueOf(highScore), numX, numY, numFontImg1.getWidth() / 10, 0);
	}
	// private void drawShop320(Graphics g) {
	// if (shopImg == null) {
	// return;
	// }
	// int barX = 0;
	// int barY = 0;
	// Tool.drawImage(g, shopImg, barX, barY, Tool.TRANS_NONE);
	// Tool.drawImage(g, barImg, barX, barY, Tool.TRANS_NONE);
	//
	// // ���ƽ��
	// int goldX = barX + 8;
	// int goldY = barY + ((barImg.getHeight() - goldGetImg.getHeight()) >> 1);
	// Tool.drawImage(g, goldGetImg, goldX, goldY, Tool.TRANS_NONE);
	//
	// int numX = goldX + goldGetImg.getWidth() + 3;
	// int numY = barY + ((barImg.getHeight() - numFontImg1.getHeight()) >> 1);
	// Tool.drawImageNum(g, numFontImg1, String.valueOf(gold), numX, numY,
	// numFontImg1.getWidth() / 10, 0);
	//
	// // �����洬
	// int infoH = 50;
	// int infoSpace = 5;
	// int infoTextH = 45;
	// int infoPanelH = infoH * 2 + infoTextH + infoSpace;
	//
	// int infoPanelX = 2;
	// int infoPanelY = barY + barImg.getHeight() + 20;
	//
	// int infoX, infoY;
	// for (int i = 0; i < BOAT_NUM; i++) {
	// if ((i & 1) == 0) {
	// infoX = (uiWidth >> 1) - boatInfoImgs[i][0].getWidth();
	// } else {
	// infoX = (uiWidth >> 1);
	// }
	// infoY = infoPanelY + (i >> 1) * infoH;
	//
	// if (i == shopIndex && (Tool.countTimes % 4) < 2) {
	// Tool.drawImage(g, boatInfoImgs[i][1], infoX, infoY, Tool.TRANS_NONE);
	// } else {
	// Tool.drawImage(g, boatInfoImgs[i][0], infoX, infoY, Tool.TRANS_NONE);
	// }
	//
	// if (i <= boatID) {
	// Tool.drawImage(g, boughtImg, infoX + boatInfoImgs[i][0].getWidth() -
	// boughtImg.getWidth() - 10, infoY + ((boatInfoImgs[i][0].getHeight() -
	// boughtImg.getHeight()) >> 1), Tool.TRANS_NONE);
	// }
	// }
	//
	// int infoTextX = infoPanelX + 4;
	// int infoTextY = infoPanelY + infoPanelH - infoTextH;
	// Tool.fillAlphaRect(g, 0x88000000, 0, infoTextY - 2, uiWidth, infoTextH +
	// 4);
	// Tool.drawImage(g, infoTextImg, infoTextX, infoTextY + ((infoTextH -
	// infoTextImg.getHeight()) >> 1), Tool.TRANS_NONE);
	//
	// Tool.drawImage(g, btnBuyImg, 0, uiHeight - btnBuyImg.getHeight(),
	// Tool.TRANS_NONE);
	// drawButtonBack(g);
	// }
	// public void drawMap(Graphics g) {
	// if (mapBgImg == null) {
	// return;
	// }
	//
	// int mapDx = 0;
	// int mapDy = 0;
	// int scale = 10;
	//
	// Tool.drawImage(g, mapBgImg, 0, 0, Tool.TRANS_NONE);
	//
	// for (int i = stageOpenID; i >= 0; i--) {
	// Tool.drawImage(g, mapPartImgs[i], mapDx + (MAP_PART_X[i] * scale / 10),
	// mapDy + (MAP_PART_Y[i] * scale / 10), Tool.TRANS_NONE);
	// }
	//
	// int mapPx = mapDx + (MAP_POINTER_X[mapPartIndex] * scale / 10) -
	// (mapPointerImg.getWidth() >> 1);
	// int mapPy = mapDy + (MAP_POINTER_Y[mapPartIndex] * scale / 10) -
	// mapPointerImg.getHeight();
	// Tool.drawImage(g, mapPointerImg, mapPx, mapPy, Tool.TRANS_NONE);
	//
	// Tool.drawImage(g, btnIntoImg, 0, uiHeight - btnMenuImg.getHeight(),
	// Tool.TRANS_NONE);
	// Tool.drawImage(g, btnMenuImg, uiWidth - btnMenuImg.getWidth(), uiHeight -
	// btnMenuImg.getHeight(), Tool.TRANS_NONE);
	// }
	public void drawMap(Graphics g) {
		Tool.drawImage(g, menuBgImg, 0, 0, Tool.TRANS_NONE);
		mapPartW = mapPartImgs[0].getWidth();
		mapPartH = mapPartImgs[0].getHeight();
		int lineNum = 5;
		int panelX = (uiWidth - (mapPartW * lineNum + mapPartOffX * (lineNum - 1))) >> 1;
		int panelY = (uiHeight - buttonHeight - mapPartH * 2 - mapPartOffY) / 2; // 60;
		int rectW = mapRectImg.getWidth();
		int rectH = mapRectImg.getHeight();
		int rectDx = 12; // (imgW - mapRectImg.getWidth()) >> 1;
		int rectDy = 66;
		int medalW = medal0Img.getWidth();
		int medalH = medal1Img.getHeight();
		int medalX = rectDx + ((rectW - medalW * 3) >> 1);
		int medalY = rectDy + ((rectH - medalH) >> 1);
		for (int i = 0; i < STAGE_NUM; i++) {
			int dx = panelX + (mapPartW + mapPartOffX) * (i % lineNum);
			int dy = panelY + (mapPartH + mapPartOffY) * (i / lineNum);
			mapPartX[i] = dx;
			mapPartY[i] = dy;
			Tool.drawImage(g, mapPartImgs[i], dx, dy, Tool.TRANS_NONE);
			if (i > stageOpenID) {
				Tool.drawImage(g, mapLockImg, dx, dy, Tool.TRANS_NONE);
			}
			if (i == mapPartIndex && (Tool.countTimes % 4) < 2) {
				Tool.drawImage(g, mapFocusImg, dx - 2, dy - 3, Tool.TRANS_NONE);
			}
			Tool.drawImage(g, mapRectImg, dx + rectDx, dy + rectDy, Tool.TRANS_NONE);
			for (int j = 0; j < 3; j++) {
				// if (i > stageOpenID) {
				// Tool.drawImage(g, medal0Img, dx + medalX + medalW * j, dy +
				// medalY, Tool.TRANS_NONE);
				// } else {
				// Tool.drawImage(g, medal1Img, dx + medalX + medalW * j, dy +
				// medalY, Tool.TRANS_NONE);
				// }
				int mx = dx + medalX + medalW * j;
				int my = dy + medalY;
				if (j < medalNums[i]) {
					Tool.drawImage(g, medal1Img, mx, my, Tool.TRANS_NONE);
				} else {
					Tool.drawImage(g, medal0Img, mx, my, Tool.TRANS_NONE);
				}
			}
		}
		// Tool.drawImage(g, btnIntoImg, 0, uiHeight - btnMenuImg.getHeight(),
		// Tool.TRANS_NONE);
		// Tool.drawImage(g, btnMenuImg, uiWidth - btnMenuImg.getWidth(),
		// uiHeight - btnMenuImg.getHeight(), Tool.TRANS_NONE);
		if (state == STATE_MAP) {
			drawButton(g, btnMenuImg, BUTTON_ALIGN_LEFT);
			// drawButton(g, btnShopImg, BUTTON_ALIGN_RIGHT);
		}
	}
	// 0219
	public void drawSysMenuBig(Graphics g) {
		// Tool.drawImage(g, mainMenuBgImg, (uiWidth - mainMenuBgImg.getWidth())
		// >> 1, (uiHeight - mainMenuBgImg.getHeight()) >> 1, Tool.TRANS_NONE);
		Common.setUIClip(g);
		Tool.fillAlphaRect(g, 0xAA000000, 0, 0, uiWidth, uiHeight);
		for (int i = 0; i < SYSTEM_MENU_NUM; i++) {
			Image itemB = btnBgImg;
			// if (i == 0) {
			// itemB = btnBg1Img;
			// }
			int itemX = menuItemXs[i] + ((menuItemWs[i] - menuItemImgs[i].getWidth()) >> 1);
			int itemY = menuItemYs[i] + ((menuItemHs[i] - menuItemImgs[i].getHeight()) >> 1);
			// if (i == 0) {
			// itemY += Tool.JUMP_RANGE[(Tool.countTimes >> 1) %
			// Tool.JUMP_RANGE.length]; //((Tool.countTimes >> 1) & 1);
			// }
			Tool.drawImage(g, menuItemImgs[i], itemX, itemY, Tool.TRANS_NONE);
			Tool.drawImage(g, itemB, menuItemXs[i] + (menuItemWs[i] - itemB.getWidth()) / 2, menuItemYs[i] + (menuItemHs[i] - itemB.getHeight()) / 2, Tool.TRANS_NONE);
		}
		// drawButtonOk(g);
		// drawButtonBack(g);
	}
	public void drawMapMenu(Graphics g) {
		Common.setUIClip(g);
		Tool.fillAlphaRect(g, 0xAA000000, 0, 0, uiWidth, uiHeight);
		// int itemH = MENU_ITEM_H;
		// int itemY = (uiHeight - itemH * MAP_MENU_NUM) >> 1;
		// int menuW = sysMenuImg.getWidth();
		// int menuH = sysMenuImg.getHeight() / MAP_MENU_NUM;
		// int menuX = (uiWidth - menuW) >> 1;
		// int menuY;
		// int itembW = itemBg0Img.getWidth();
		// int itembH = itemBg0Img.getHeight();
		// int itembX = (uiWidth - itembW) >> 1;
		// for (int i = 0; i < MAP_MENU_NUM; i++) {
		// int curItemY = itemY + itemH * i;
		// if (i == sysMenuIndex) {
		// Tool.drawImage(g, itemBg1Img, itembX, curItemY + ((itemH - itembH) >>
		// 1), Tool.TRANS_NONE);
		// } else {
		// Tool.drawImage(g, itemBg0Img, itembX, curItemY + ((itemH - itembH) >>
		// 1), Tool.TRANS_NONE);
		// }
		// menuY = curItemY + ((itemH - menuH) >> 1);
		// if (i == 1) {
		// Tool.drawTile(g, sysMenuShopImg, 0, menuW, menuH, Tool.TRANS_NONE,
		// menuX, menuY);
		// } else {
		// Tool.drawTile(g, sysMenuImg, i, menuW, menuH, Tool.TRANS_NONE, menuX,
		// menuY);
		// }
		// if (i == 2) { // ����
		// int fid = 1;
		// if (bSoundOn) {
		// fid = 0;
		// }
		// Tool.drawTile(g, onoffImg, fid, (onoffImg.getWidth() >> 1),
		// onoffImg.getHeight(), Tool.TRANS_NONE, menuX + (menuW >> 1), menuY);
		// }
		// }
		for (int i = 0; i < MAP_MENU_NUM; i++) {
			Image itemB = btnBgImg;
			// if (i == 0 || i == 5) {
			// itemB = btnBg1Img;
			// }
			int itemX = menuItemXs[i] + ((menuItemWs[i] - menuItemImgs[i].getWidth()) >> 1);
			int itemY = menuItemYs[i] + ((menuItemHs[i] - menuItemImgs[i].getHeight()) >> 1);
			// if (i == 0) {
			// itemY += Tool.JUMP_RANGE[(Tool.countTimes >> 1) %
			// Tool.JUMP_RANGE.length]; //((Tool.countTimes >> 1) & 1);
			// }
			Tool.drawImage(g, menuItemImgs[i], itemX, itemY, Tool.TRANS_NONE);
			Tool.drawImage(g, itemB, menuItemXs[i], menuItemYs[i], Tool.TRANS_NONE);
		}
		// drawButtonOk(g);
		// drawButtonBack(g);
	}
	public void updataScoreGameScore() {
		int temp = 0;
		for (int i = 0; i < highScores.length; i++) {
			temp += highScores[i];
		}
		if (temp > updateGameScore) {
			updateGameScore = temp;
			// WebNetInterface.UpdateScore(SCORE_GAME_SCORE, updateGameScore,
			// "�ؿ��ܵ÷�");
		}
	}
	public void updateScoreFishNum() {
		if (updateFishNum > updateFishNumBak) {
			updateFishNumBak = updateFishNum;
			// WebNetInterface.UpdateScore(SCORE_FISH_NUM, updateFishNum,
			// "�������");
		}
	}
	public void updateScoreGameGold() {
		if (updateGold > updateGoldBak) {
			updateGoldBak = updateGold;
			// WebNetInterface.UpdateScore(SCORE_GAME_GOLD, updateGold, "�󸻺�");
		}
	}
	public void updateAchievement(int AchievementId) {
		if (!bAchGeted[AchievementId]) {
			System.out.println("Update Achievement: " + AchievementId);
			// WebNetInterface.UpdateAchievement(AchievementId);
			bAchGeted[AchievementId] = true;
			saveRecord();
			// TODO test
			// final String str = "��óɾͣ�" + STR_ACH_NAME[AchievementId];
			// Thread action = new Thread() {
			// public void run() {
			// Looper.prepare();
			// Toast toast = Toast.makeText(DoMidlet.instance, str,
			// Toast.LENGTH_LONG);
			// toast.setGravity(Gravity.TOP, 0, 0);
			// toast.show();
			// //Toast.makeText(DoMidlet.instance, "Ĭ��Toast��ʽ",
			// Toast.LENGTH_SHORT).show();
			// Looper.loop();
			// }
			// };
			// action.start();
		}
	}
	public void updateAchFishNum() {
		if (updateFishNum >= 50) {
			updateAchievement(ACH_FISH_TOTAL_50);
		}
		if (updateFishNum >= 100) {
			updateAchievement(ACH_FISH_TOTAL_100);
		}
		if (updateFishNum >= 500) {
			updateAchievement(ACH_FISH_TOTAL_500);
		}
		if (updateFishNum >= 1000) {
			updateAchievement(ACH_FISH_TOTAL_1000);
		}
		if (updateFishNum >= 3000) {
			updateAchievement(ACH_FISH_TOTAL_3000);
		}
	}
	public void updateAchFishOnceNum() {
		if (fishOnceNum == 2) {
			updateAchievement(ACH_FISH_ONCE_2);
		} else if (fishOnceNum == 3) {
			updateAchievement(ACH_FISH_ONCE_3);
		} else if (fishOnceNum >= 4) {
			updateAchievement(ACH_FISH_ONCE_4);
		}
	}
	public void updateAchFishSuccessTime() {
		if (Fishman.FishSuccessTime >= 5) {
			updateAchievement(ACH_FISH_SUCCESS_5);
		}
		if (Fishman.FishSuccessTime >= 30) {
			updateAchievement(ACH_FISH_SUCCESS_30);
		}
		if (Fishman.FishSuccessTime >= 100) {
			updateAchievement(ACH_FISH_SUCCESS_100);
		}
	}
	public void updateAchBoat() {
		if (boatID >= 1) {
			updateAchievement(ACH_BOAT_1);
		}
		if (boatID >= 2) {
			updateAchievement(ACH_BOAT_2);
		}
		if (boatID == 3) {
			updateAchievement(ACH_BOAT_3);
		}
	}
	public void updateAchDiver() {
		if (diverID >= 0) {
			updateAchievement(ACH_DIVER_0);
		}
		if (diverID >= 1) {
			updateAchievement(ACH_DIVER_1);
		}
		if (diverID == 2) {
			updateAchievement(ACH_DIVER_2);
		}
	}
	public void updateAchStageMedal() {
		int allMedalNum = 0;
		for (int i = 0; i < STAGE_NUM; i++) {
			if (medalNums[i] == 3) {
				allMedalNum++;
			}
		}
		if (allMedalNum >= 1) {
			updateAchievement(ACH_STAGE_MEDAL_1);
		}
		if (allMedalNum >= 5) {
			updateAchievement(ACH_STAGE_MEDAL_5);
		}
		if (allMedalNum == 10) {
			updateAchievement(ACH_STAGE_MEDAL_10);
		}
	}
	public void updateAchLiveGame() {
		if (liveGameStageID >= 1) {
			updateAchievement(ACH_LIVEGAME_1);
		}
		if (liveGameStageID >= 20) {
			updateAchievement(ACH_LIVEGAME_20);
		}
	}
	public void gotoSummary() {
		loadNUM = 0;
		timeoverImg = null;
		// String str = null;
		calculateScore();// �������
		// str = STR_STAGE_GOAL + goal;
		goldGeted = 0;
		if (score >= goal) {
			// str += "\n" + STR_STAGE_SUCCESS;
			// str += "\n" + STR_ADD_GOLD;
			// if (medalNums[stageID] == 0) {
			// goldGeted = 50;
			// gold += goldGeted;
			// }
			// 20120412
			if (score >= STAGE_GOAL[stageID][2]) {
				goldGeted = 50;
			} else if (score >= STAGE_GOAL[stageID][1]) {
				goldGeted = 30;
			} else if (score >= STAGE_GOAL[stageID][0]) {
				goldGeted = 20;
			}
			gold += goldGeted;
			if (stageID == stageOpenID) { // �����¹ؿ�
				stageOpenID++;
				if (stageOpenID >= STAGE_NUM) {
					stageOpenID = STAGE_NUM - 1;
				}
			}
			bGameFail = false;
		} else {
			// str += "\n" + STR_STAGE_FAIL;
			bGameFail = true;
		}
		// ��߷�
		bNewRecord = false;
		// if (stageID < STAGE_NUM) {
		if (score > highScores[stageID]) {
			highScores[stageID] = score;
			bNewRecord = true;
		}
		// }
		// ���¸���
		if (score >= STAGE_GOAL[stageID][2]) {
			medalNum = 3;
		} else if (score >= STAGE_GOAL[stageID][1]) {
			medalNum = 2;
		} else if (score >= STAGE_GOAL[stageID][0]) {
			medalNum = 1;
		} else {
			medalNum = 0;
		}
		if (medalNum > medalNums[stageID]) {
			medalNums[stageID] = medalNum;
			// 110818
			if (medalNums[stageID] == 3) {
				updateAchStageMedal();
			}
		}
		// str += "\n";
		// str += "\n" + STR_STAGE_SCORE + score;
		// str += "\n" + STR_STAGE_PLAYERSCORE + playerScore;
		// if (stageID == 10) {
		// str += "\n";
		// str += "\n" + STR_TEACH_OVER;
		// }
		// scrollText = new ScrollText(str, uiWidth - 60, uiHeight * 2 / 3,
		// fontH, 2, font);
		updataScoreGameScore();
		updateScoreFishNum();
		updateScoreGameGold();
		saveRecord();
		medalBig0Img = Tool.createImage("/medalBig0.png");
		medalBig1Img = Tool.createImage("/medalBig1.png");
		stageGoalImg = Tool.createImage("/stageGoal.png");
		stageScoreImg = Tool.createImage("/stageScore.png");
		if (bGameFail) {
			stageResultImg = Tool.createImage("/stageFail.png");
		} else {
			stageResultImg = Tool.createImage("/stageSuccess.png");
		}
		stageGoldImg = Tool.createImage("/stageGold.png");
		stageRecordImg = Tool.createImage("/stageRecord.png");
		stageHighScoreImg = Tool.createImage("/stageHighScore.png");
		clearButtonBubbleSet();
		state = STATE_GAME_SUMMARY;
	}
	// public void drawSummary(Graphics g) {
	// Common.setUIClip(g);
	// int panelW = 338;
	// int panelH = uiHeight;
	// int panelX = (uiWidth - panelW) >> 1;
	// int panelY = 0;
	// Tool.fillAlphaRect(g, 0xAA000000, panelX, panelY, panelW, panelH);
	//
	// //����
	// int medalW = medalBig1Img.getWidth();
	// int medalH = medalBig1Img.getHeight();
	// int medalSpace = 6;
	// int medalX = panelX + ((panelW - medalW * 3 - medalSpace * 2) >> 1);
	// int medalY = panelY + 70;
	// for (int i = 0; i < 3; i++) {
	// int mx = medalX + (medalW + medalSpace) * i;
	// int my = medalY - (i & 1) * 11;
	// if (i < medalNum) {
	// Tool.drawImage(g, medalBig1Img, mx, my, Tool.TRANS_NONE);
	// } else {
	// Tool.drawImage(g, medalBig0Img, mx, my, Tool.TRANS_NONE);
	// }
	// }
	//
	// //Ŀ��
	// int goalX = panelX + 80;
	// int goalY = medalY + medalH + 33;
	// Tool.drawImage(g, stageGoalImg, goalX, goalY, Tool.TRANS_NONE);
	// int goalNumX = goalX + stageGoalImg.getWidth() + 6;
	// int goalNumY = goalY + ((stageGoalImg.getHeight() -
	// numFontImg1.getHeight()) >> 1);
	// Tool.drawImageNum(g, numFontImg1, String.valueOf(goal), goalNumX,
	// goalNumY, numFontImg1.getWidth() / 10, 0);
	//
	// //�÷�
	// int scoreX = goalX;
	// int scoreY = goalY + stageGoalImg.getHeight() + 10;
	// Tool.drawImage(g, stageScoreImg, scoreX, scoreY, Tool.TRANS_NONE);
	// int scoreNumX = scoreX + stageScoreImg.getWidth() + 6;
	// int scoreNumY = scoreY + ((stageScoreImg.getHeight() -
	// numFontImg1.getHeight()) >> 1);
	// Tool.drawImageNum(g, numFontImg1, String.valueOf(score), scoreNumX,
	// scoreNumY, numFontImg1.getWidth() / 10, 0);
	//
	// //�¼�¼
	// if (bNewRecord) {
	// Tool.drawImage(g, stageRecordImg, panelX + 263, panelY + 190,
	// Tool.TRANS_NONE);
	// }
	//
	// //���
	// int resultX = panelX + ((panelW - stageResultImg.getWidth()) >> 1);
	// int resultY = scoreY + stageScoreImg.getHeight() + 12;
	// Tool.drawImage(g, stageResultImg, resultX, resultY, Tool.TRANS_NONE);
	//
	// //���
	// //if (!bGameFail) {
	// if (goldGeted > 0) {
	// int goldX = scoreX;
	// int goldY = resultY + stageResultImg.getHeight() + 14;
	// Tool.drawImage(g, stageGoldImg, goldX, goldY, Tool.TRANS_NONE);
	// int goldNumX = goldX + stageGoldImg.getWidth() + 6;
	// int goldNumY = goldY + ((stageGoldImg.getHeight() -
	// numFontImg1.getHeight()) >> 1);
	// Tool.drawImageNum(g, numFontImg1, String.valueOf(50), goldNumX, goldNumY,
	// numFontImg1.getWidth() / 10, 0);
	// }
	//
	// drawButtonOk(g);
	// drawButtonBack(g);
	// }
	public void drawSummary(Graphics g) {
		drawGame(g);
		Common.setUIClip(g);
		int rw = 240;
		int rh = 320;
		int rx = (uiWidth - rw) >> 1;
		int ry = (uiHeight - rh) >> 1;
		fillBgRect(g, rx, ry, rw, rh);
		// Common.setUIClip(g);
		int panelW = rw;
		int panelH = rh;
		int panelX = rx;
		int panelY = ry;
		// Tool.fillAlphaRect(g, 0xAA000000, panelX, panelY, panelW, panelH);
		// ����
		int medalW = medalBig1Img.getWidth();
		int medalH = medalBig1Img.getHeight();
		int medalSpace = 6;
		int medalX = panelX + ((panelW - medalW * 3 - medalSpace * 2) >> 1);
		int medalY = panelY + 50;
		for (int i = 0; i < 3; i++) {
			int mx = medalX + (medalW + medalSpace) * i;
			int my = medalY - (i & 1) * 11;
			if (i < medalNum) {
				Tool.drawImage(g, medalBig1Img, mx, my, Tool.TRANS_NONE);
			} else {
				Tool.drawImage(g, medalBig0Img, mx, my, Tool.TRANS_NONE);
			}
		}
		// ���
		int resultX = panelX + ((panelW - stageResultImg.getWidth()) >> 1);
		int resultY = medalY + medalH + 12; // scoreY +
											// stageScoreImg.getHeight() + 12;
		Tool.drawImage(g, stageResultImg, resultX, resultY, Tool.TRANS_NONE);
		// Ŀ��
		int goalX = medalX; // panelX + 80;
		int goalY = resultY + stageResultImg.getHeight() + 12; // medalY +
																// medalH + 33;
		Tool.drawImage(g, stageGoalImg, goalX, goalY, Tool.TRANS_NONE);
		int goalNumX = goalX + stageGoalImg.getWidth() + 6;
		int goalNumY = goalY + ((stageGoalImg.getHeight() - numFontImg1.getHeight()) >> 1);
		Tool.drawImageNum(g, numFontImg1, String.valueOf(goal), goalNumX, goalNumY, numFontImg1.getWidth() / 10, 0);
		// �÷�
		int scoreX = goalX;
		int scoreY = goalY + stageGoalImg.getHeight() + 10;
		Tool.drawImage(g, stageScoreImg, scoreX, scoreY, Tool.TRANS_NONE);
		int scoreNumX = scoreX + stageScoreImg.getWidth() + 6;
		int scoreNumY = scoreY + ((stageScoreImg.getHeight() - numFontImg1.getHeight()) >> 1);
		Tool.drawImageNum(g, numFontImg1, String.valueOf(score), scoreNumX, scoreNumY, numFontImg1.getWidth() / 10, 0);
		// ��߷�
		int highScoreX = scoreX;
		int highScoreY = scoreY + stageScoreImg.getHeight() + 10;
		Tool.drawImage(g, stageHighScoreImg, highScoreX, highScoreY, Tool.TRANS_NONE);
		int highScoreNumX = highScoreX + stageHighScoreImg.getWidth() + 6;
		int highScoreNumY = highScoreY + ((stageHighScoreImg.getHeight() - numFontImg1.getHeight()) >> 1);
		Tool.drawImageNum(g, numFontImg1, String.valueOf(highScores[stageID]), highScoreNumX, highScoreNumY, numFontImg1.getWidth() / 10, 0);
		// ���
		// if (!bGameFail) {
		if (goldGeted > 0) {
			int goldX = highScoreX; // scoreX;
			int goldY = highScoreY + stageHighScoreImg.getHeight() + 10; // resultY
																			// +
																			// stageResultImg.getHeight()
																			// +
																			// 14;
			Tool.drawImage(g, stageGoldImg, goldX, goldY, Tool.TRANS_NONE);
			int goldNumX = goldX + stageGoldImg.getWidth() + 6;
			int goldNumY = goldY + ((stageGoldImg.getHeight() - numFontImg1.getHeight()) >> 1);
			Tool.drawImageNum(g, numFontImg1, String.valueOf(goldGeted), goldNumX, goldNumY, numFontImg1.getWidth() / 10, 0);
		}
		// �¼�¼
		if (bNewRecord) {
			// Tool.drawImage(g, stageRecordImg, panelX + 263, panelY + 190,
			// Tool.TRANS_NONE);
		}
		// drawButtonOk(g);
		// drawButtonBack(g);
		buttonLeftX = rx - buttonWidth;
		buttonLeftY = uiHeight - buttonHeight;
		buttonRightX = rx + rw;
		buttonRightY = uiHeight - buttonHeight;
		drawButtonBubbleSet(g);
		// drawButton(g, btnOkImg, buttonLeftX, buttonLeftY);
		// drawButton(g, btnBackImg, buttonRightX, buttonRightY);
		drawButton(g, btnReplayImg, buttonLeftX, buttonLeftY);
		if (stageID == 11) {
			drawButton(g, btnBackImg, buttonRightX, buttonRightY);
		} else {
			drawButton(g, btnMapImg, buttonRightX, buttonRightY);
		}
	}
	// public void drawSysMenu(Graphics g) {
	// int itemH = 28;
	// int itemY = (uiHeight - itemH * SYSTEM_MENU_NUM) >> 1;
	//
	// int menuW = sysMenuImg.getWidth();
	//
	// int menuH = sysMenuImg.getHeight() / SYSTEM_MENU_NUM;
	// int menuX = (uiWidth - menuW) >> 1;
	// int menuY;
	//
	// int itembW = itemBg0Img.getWidth();
	// int itembH = itemBg0Img.getHeight();
	// int itembX = (uiWidth - itembW) >> 1;
	//
	// for (int i = 0; i < SYSTEM_MENU_NUM; i++) {
	// int curItemY = itemY + itemH * i;
	// if (i == sysMenuIndex) {
	// Tool.drawImage(g, itemBg1Img, itembX, curItemY + ((itemH - itembH) >> 1),
	// Tool.TRANS_NONE);
	// } else {
	// Tool.drawImage(g, itemBg0Img, itembX, curItemY + ((itemH - itembH) >> 1),
	// Tool.TRANS_NONE);
	// }
	// menuY = curItemY + ((itemH - menuH) >> 1);
	// Tool.drawTile(g, sysMenuImg, i, menuW, menuH, Tool.TRANS_NONE, menuX,
	// menuY);
	//
	// if (i == 2) { // ����
	// int fid = 1;
	// if (bSoundOn) {
	// fid = 0;
	// }
	// Tool.drawTile(g, onoffImg, fid, (onoffImg.getWidth() >> 1),
	// onoffImg.getHeight(), Tool.TRANS_NONE, menuX + (menuW >> 1), menuY);
	// }
	// }
	// }
	public void draw(Graphics g) { // 0219
		/*
		 * //#if (MobileType == N97) //# if (isSizeChanged) { //#
		 * Common.fillUIRect(g, 0x0); //# g.setColor(-1); //#
		 * g.drawString("����Ϸ��֧�ֺ���", SCREEN_WIDTH >> 1, (SCREEN_HEIGHT >> 1) -
		 * fontH - 5, 17); //# return; //# } //#endif
		 */
		switch (state) {
			case STATE_PAUSE : {
				Common.fillUIRect(g, 0x0);
				// g.setColor(-1);
				// g.drawString("��Ϸ��ͣ", SCREEN_WIDTH >> 1, (SCREEN_HEIGHT >> 1) -
				// fontH - 5, 17);
				// g.drawString("������Ļ������Ϸ", SCREEN_WIDTH >> 1, (SCREEN_HEIGHT >> 1),
				// 17);
				String str = "��Ϸ��ͣ";
				Tool.drawString(g, str, (SCREEN_WIDTH - font.stringWidth(str)) >> 1, (SCREEN_HEIGHT >> 1) - fontH * 2, 0xffffff, 0);
				str = "������Ļ������Ϸ";
				Tool.drawString(g, str, (SCREEN_WIDTH - font.stringWidth(str)) >> 1, (SCREEN_HEIGHT >> 1) - fontH * 1, 0xffffff, 0);
				break;
			}
			case STATE_SPLOGO :
				if (QQlogoImg != null) {
					// Common.fillUIRect(g, 0xFF9000);
					Common.fillUIRect(g, 0xffffff);
					Tool.drawImage(g, QQlogoImg, (uiWidth - QQlogoImg.getWidth()) >> 1, (uiHeight - QQlogoImg.getHeight()) >> 1, Tool.TRANS_NONE);
				}
				break;
			case STATE_LOGO :
				if (logoImg != null) {
					Common.fillUIRect(g, 0xffffff);
					Tool.drawImage(g, logoImg, (uiWidth - logoImg.getWidth()) >> 1, (uiHeight - logoImg.getHeight()) >> 1, Tool.TRANS_NONE);
				}
				break;
			case STATE_START_MUSIC :
				Tool.drawImage(g, menuBgImg, 0, 0, Tool.TRANS_NONE);
				g.setClip(0, 0, 240, 320);
				drawBubbleSet(g);
				Tool.fillAlphaRect(g, 0xAA000000, 0, 0, uiWidth, uiHeight);
				Tool.drawString(g, STR_START_MUSIC, (uiWidth - font.stringWidth(STR_START_MUSIC)) >> 1, uiHeight * 2 / 5, 0xffff00, 0);
				// Tool.drawString(g, STR_YES, 5, uiHeight - fontH - 5, 0xffffff, 0,
				// Tool.TYPE_SHADOW);
				// Tool.drawString(g, STR_NO, uiWidth - font.stringWidth(STR_NO) -
				// 5, uiHeight - fontH - 5, 0xffffff, 0, Tool.TYPE_SHADOW);
				drawButton(g, btnYesImg, BUTTON_ALIGN_LEFT);
				drawButton(g, btnNoImg, BUTTON_ALIGN_RIGHT);
				break;
			case STATE_COVER :
				if (coverImg != null) {
					Tool.drawImage(g, coverImg, coverX, coverY, Tool.TRANS_NONE);
				}
				drawMainMenu(g, coverMenuX, MAIN_MENU_Y);
				// Tool.drawString(g, STR_ANYKEY, (uiWidth -
				// font.stringWidth(STR_ANYKEY)) >> 1, uiHeight - fontH - 5,
				// 0xffffff, 0, 1);
				break;
			case STATE_MAIN_MENU :
				// if (menuBgImg != null) {
				// Tool.drawImage(g, menuBgImg, 0, 0, Tool.TRANS_NONE);
				// }
				// drawBubbleSet(g);
				// drawMainMenu240(g);
				//
				// drawButtonOk(g);
				// drawButtonBack(g);
				if (coverImg != null) {
					Tool.drawImage(g, coverImg, coverX, coverY, Tool.TRANS_NONE);
				}
				drawButtonBubbleSet(g);
				drawMainMenu(g, MAIN_MENU_X, MAIN_MENU_Y);
				// g.setClip(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
				break;
			case STATE_NEWGAME_CONFIRM :
			case STATE_NO_RECORD_CONFIRM :
				if (menuBgImg != null) {
					Tool.drawImage(g, menuBgImg, 0, 0, Tool.TRANS_NONE);
				}
				drawConfirm(g);
				break;
			case STATE_HELP :
			case STATE_ABOUT :
				if (menuBgImg != null) {
					Tool.drawImage(g, menuBgImg, 0, 0, Tool.TRANS_NONE);
				}
				if (preState == STATE_SYSTEM_MENU) {
					drawGame(g);
				} else if (preState == STATE_MAP_MENU) {
					drawMap(g);
				}
				Tool.fillAlphaRect(g, 0xAA000000, 0, 0, uiWidth, uiHeight);
				if (scrollText != null) {
					scrollText.draw(g, 10, 10, 0xffffff, 0, Tool.TYPE_SHADOW);
				}
				drawButtonBack(g);
				break;
			case STATE_PLAYER_CHOOSE :
				drawPlayerChoose(g);
				break;
			case STATE_SHOP :
				drawShop240(g);
				break;
			case STATE_EMPLOY_SHOP :
				drawEmployShop240(g);
				break;
			case STATE_MAP :
			case STATE_MAP_MENU :
				drawMap(g);
				if (state == STATE_MAP_MENU) {
					drawMapMenu(g);
				}
				break;
			case STATE_FIRST_TUTOR_OVER :
				drawMap(g);
				drawMessage(g, scrollText, false);
				break;
			case STATE_GAME_BEGIN :
			case STATE_GAME :
			case STATE_SYSTEM_MENU :
			case STATE_TIME_OVER :
			case STATE_GAME_SUMMARY :
			case STATE_BACK_MAINMENU_CONFIRM :
				drawGame(g);
				if (state == STATE_GAME_BEGIN) {
					int dy = Tool.JUMP_RANGE[(Tool.countTimes >> 1) % Tool.JUMP_RANGE.length];
					if (beginImg != null) {
						Tool.drawImage(g, beginImg, (uiWidth - beginImg.getWidth()) >> 1, ((uiHeight - beginImg.getHeight()) >> 1) + dy, Tool.TRANS_NONE);
					}
					// if (beginImg != null && (Tool.countTimes >> 1) % 8 != 3) {
					// Tool.drawImage(g, beginImg, (uiWidth - beginImg.getWidth())
					// >> 1, (uiHeight - beginImg.getHeight()) >> 1,
					// Tool.TRANS_NONE);
					// }
				} else if (state == STATE_SYSTEM_MENU) { // 0219
					drawSysMenuBig(g);
				} else if (state == STATE_TIME_OVER) {
					if (Tool.countTimes % 16 < 8) {
						Tool.drawImage(g, timeoverImg, (uiWidth - timeoverImg.getWidth()) >> 1, (uiHeight - timeoverImg.getHeight()) >> 1, Tool.TRANS_NONE);
					}
				} else if (state == STATE_GAME_SUMMARY) {
					// domobAd();
					drawSummary(g);
				} else if (state == STATE_BACK_MAINMENU_CONFIRM) {
					drawConfirm(g);
				}
				break;
			case STATE_LIVEGAME_CONTINUE :
			case STATE_LIVEGAME_SUCCESS :
				drawGame(g);
				drawConfirm(g);
				break;
			// whb add
			case STATE_PLAYER_INFO_CONFIRM :
				drawPlayerChoose(g);
				drawConfirm(g);
				break;
			// case STATE_SMS:
			// drawSMS(g);
			// break;
			case STATE_CG :
				drawCGView(g);
				break;
			case STATE_LOADING :
			case STATE_RETURN :
				// case STATE_RETURN_EMPLOY :
				drawLoading(g);
				break;
			// case STATE_QQ_SHOWSCORE :// QQ ���ְ�
			// drawQQScore(g);
			// break;
			// case STATE_QQ_UPLOADSCORE :
			// Common.fillUIRect(g, 0);
			// scrollText.draw(g, (uiWidth - scrollText.getWidth()) >> 1, 40,
			// 0xffffff, 0, Tool.TYPE_EDGE);
			// g.setClip(10, SCREEN_HEIGHT - fontH - 5, SCREEN_WIDTH - 20, fontH);
			// Tool.drawString(g, "�ϴ�����", 10, SCREEN_HEIGHT - fontH - 5, 0xffffff,
			// 0);
			// Tool.drawString(g, "����", SCREEN_WIDTH - 10 - font.stringWidth("����"),
			// SCREEN_HEIGHT - fontH - 5, 0xffffff, 0);
			// break;
			// case STATE_QQ_UPLOADSCORE_RESULT :
			// Common.fillUIRect(g, 0);
			// if (!bRequestNotified || qqInfoStrs == null) {
			// String str = "�����ϴ����֣����Ժ�";
			// Tool.drawString(g, str, (SCREEN_WIDTH - font.stringWidth(str)) >> 1,
			// (SCREEN_HEIGHT - fontH) >> 1, 0xffffff, 0);
			// } else {
			// int infoDY = (SCREEN_HEIGHT - fontH * qqInfoStrs.length) >> 1;
			// for (int i = 0; i < qqInfoStrs.length; i++) {
			// Tool.drawString(g, qqInfoStrs[i], (SCREEN_WIDTH -
			// font.stringWidth(qqInfoStrs[i])) >> 1, infoDY + fontH * i, 0xffffff,
			// 0);
			// }
			// }
			//
			// Tool.drawString(g, "QQ����", 10, SCREEN_HEIGHT - fontH - 5, 0xffffff,
			// 0);
			// Tool.drawString(g, "����", SCREEN_WIDTH - 10 - font.stringWidth("����"),
			// SCREEN_HEIGHT - fontH - 5, 0xffffff, 0);
			// break;
			case STATE_SMS_UI :
				drawSmsUI(g);
				break;
			// case STATE_GAME_OVER :
			// Common.fillUIRect(g, 0);
			// // ��ϲͨ��\n����ģʽ�ѿ�����
			// g.setColor(-1);
			// g.drawString("��ϲͨ��", SCREEN_WIDTH >> 1, (SCREEN_HEIGHT >> 1) - fontH
			// - 10, 17);
			// if (isShowLiveGame) {
			// isLiveGameOpened = true;
			// liveGameGold = gold;
			// liveGameAllFishToScoreNum = allFishToScoreNum;
			// saveRecord();
			// g.drawString("����ģʽ�ѿ���", SCREEN_WIDTH >> 1, (SCREEN_HEIGHT >> 1), 17);
			// }
			// g.setColor(0xffff00);
			// g.drawString("�������˵�", SCREEN_WIDTH - 10, SCREEN_HEIGHT - 10, 40);
			// break;
			case STATE_MORE_GAME :
				drawMoreGame(g);
				break;
			case STATE_EXIT : {
				Tool.drawImage(g, coverImg, coverX, coverY, Tool.TRANS_NONE);
				Tool.fillAlphaRect(g, 0xAA000000, 0, 0, uiWidth, uiHeight);
				String str = "�Ƿ��˳���Ϸ��";
				Tool.drawString(g, str, (SCREEN_WIDTH - font.stringWidth(str)) >> 1, (SCREEN_HEIGHT >> 1) - fontH * 2, 0xffff00, 0, Tool.TYPE_EDGE);
				drawButton(g, btnOkImg, BUTTON_ALIGN_LEFT);
				drawButton(g, btnBackImg, BUTTON_ALIGN_RIGHT);
				break;
			}
			// 0219
			// case STATE_GIFT_HINT :
			// drawImageHint(g, giftHintImg);
			// break;
			case STATE_MAGNET_HINT :
				drawImageHint(g, magnetHintImg);
				Tool.drawImage(g, btnYoumiImg, youmi_X, youmi_Y, Tool.TRANS_NONE);
				break;
			case STATE_TUTOR :
				drawTutor(g);
				break;
			default :
				break;
		}
		if (state != STATE_PAUSE) {
			drawMessage(g, message, true);
			drawHintMessage(g);
			// drawButton(g, btnYoumiImg, BUTTON_ALIGN_RIGHT);
		}
	}
	private void drawPlayerChoose(Graphics g) {
		if (menuBgImg != null) {
			Tool.drawImage(g, menuBgImg, 0, 0, Tool.TRANS_NONE);
		}
		Tool.drawImage(g, playerChooseImg, (uiWidth - playerChooseImg.getWidth()) >> 1, 30, Tool.TRANS_NONE);
		playerHeadW = playerHeadImgs[0].getWidth() >> 1;
		playerHeadH = playerHeadImgs[0].getHeight();
		int headX = (uiWidth - playerHeadW * 2) / 4;
		int headY = uiHeight / 3;
		int headTileIndex0 = 0;
		int headTileIndex1 = 1;
		if (playerIndex == 1) {
			headTileIndex0 = 1;
			headTileIndex1 = 0;
		}
		playerHeadX[0] = headX;
		playerHeadY[0] = headY;
		playerHeadX[1] = uiWidth - playerHeadW - headX;
		playerHeadY[1] = headY;
		Tool.drawTile(g, playerHeadImgs[0], headTileIndex0, playerHeadW, playerHeadH, Tool.TRANS_NONE, headX, headY);
		Tool.drawTile(g, playerHeadImgs[1], headTileIndex1, playerHeadW, playerHeadH, Tool.TRANS_NONE, uiWidth - playerHeadW - headX, headY);
		Tool.drawImage(g, nanshengImg, playerHeadX[0] + (playerHeadW - nanshengImg.getWidth()) / 2, headY + playerHeadH + 10, 0);
		Tool.drawImage(g, nvshengImg, playerHeadX[1] + (playerHeadW - nvshengImg.getWidth()) / 2, headY + playerHeadH + 10, 0);
		drawButtonOk(g);
		drawButtonBack(g);
	}
	// public void drawQQScore(Graphics g) {
	// g.drawImage(menuBgImg, 0, 0, 0);
	// Tool.fillAlphaRect(g, 0x88000000, 0, 0, uiWidth, uiHeight);
	//
	// int x = 5;
	// int y = 5;
	// int width = uiWidth - x * 2;
	// int height = uiHeight - y * 2;
	//
	// String str = "QQ����";
	// Tool.drawString(g, str, x, y + height - fontH, 0xffffff, -1);
	// str = "����";
	// Tool.drawString(g, str, x + width - font.stringWidth(str), y + height -
	// fontH, 0xffffff, -1);
	//
	// if (bRequestNotified) {
	// if (QQUserInfo.player_Nicename == null || QQUserInfo.player_Score ==
	// null) {
	// str = "û��������ݣ��뷵��";
	// Tool.drawString(g, str, (SCREEN_WIDTH - font.stringWidth(str)) >> 1,
	// (SCREEN_HEIGHT - fontH) >> 1, 0xffff00, 0);
	// } else {
	// str = "���ְ�";
	// Tool.drawString(g, str, x + (width - font.stringWidth(str)) >> 1, y,
	// 0xffff00, -1);
	//
	// String rankStr = "����";
	// String scoreStr = "����";
	// String nameStr = "�ǳ�";
	// int dy = y + fontH + 5;
	// int rankX = x;
	// int scoreX = rankX + font.stringWidth(rankStr) + 15;
	// int nameX = scoreX + font.stringWidth(scoreStr) * 2;
	// Tool.drawString(g, rankStr, rankX, dy, 0x0000ff, 0xffffff,
	// Tool.TYPE_EDGE);
	// Tool.drawString(g, scoreStr, scoreX, dy, 0x0000ff, 0xffffff,
	// Tool.TYPE_EDGE);
	// Tool.drawString(g, nameStr, nameX, dy, 0x0000ff, 0xffffff,
	// Tool.TYPE_EDGE);
	//
	// dy += (fontH + 5);
	// int RANK_NUM = 10;
	// for (int i = 0; i < RANK_NUM; i++) {
	// if (QQUserInfo.player_Score[i] == null || QQUserInfo.player_Nicename[i]
	// == null) {
	// break;
	// }
	// Tool.drawString(g, String.valueOf(i + 1), rankX, dy, 0xffffff, -1);
	// Tool.drawString(g, QQUserInfo.player_Score[i], scoreX, dy, 0xffffff, -1);
	// Tool.drawString(g, QQUserInfo.player_Nicename[i], nameX, dy, 0xffffff,
	// -1);
	// dy += fontH;
	// }
	// }
	// } else {
	// str = "���ڶ�ȡ�����Ժ�";
	// Tool.drawString(g, str, x + (width - font.stringWidth(str)) >> 1, y +
	// (height - fontH) >> 1, 0x0000ff, 0xffffff);
	// }
	// }
	public void drawMainMenu(Graphics g, int x, int y) {
		// Tool.drawImage(g, qqBarImg, qqBarDx, qqBarDy, Tool.TRANS_NONE);
		for (int i = 0; i < MAIN_MENU_NUM; i++) {
			Image itemB = btnBgImg;
			if (i == 0 || i == 5) {
				itemB = btnBg1Img;
			}
			int itemX = menuItemXs[i] + ((menuItemWs[i] - menuItemImgs[i].getWidth()) >> 1);
			int itemY = menuItemYs[i] + ((menuItemHs[i] - menuItemImgs[i].getHeight()) >> 1);
			if (i == 0) {
				itemY += Tool.JUMP_RANGE[(Tool.countTimes >> 1) % Tool.JUMP_RANGE.length]; // ((Tool.countTimes >> 1) &
																							// 1);
			}
			Tool.drawImage(g, menuItemImgs[i], itemX, itemY, Tool.TRANS_NONE);
			if (i < 6) {
				Tool.drawImage(g, itemB, menuItemXs[i], menuItemYs[i], Tool.TRANS_NONE);
			}
		}
		Tool.drawImage(g, btnYoumiImg, 22, uiHeight - btnYoumiImg.getHeight(), Tool.TRANS_NONE);
	}
	// private void drawMainMenu240(Graphics g) {
	// //int itemH = 33;
	// int itemH = MENU_ITEM_H;
	// int itemY = (uiHeight - itemH * MAIN_MENU_NUM) >> 1;
	//
	// int menuW = menuImg.getWidth();
	// int menuH = menuImg.getHeight() / 7;
	// int menuX = (uiWidth - menuW) >> 1;
	// int menuY;
	//
	// int itembW = itemBg0Img.getWidth();
	// int itembH = itemBg0Img.getHeight();
	// int itembX = (uiWidth - itembW) >> 1;
	// for (int i = 0; i < MAIN_MENU_NUM; i++) {
	// int curItemY = itemY + itemH * i;
	// if (i == mainMenuIndex) {
	// Tool.drawImage(g, itemBg1Img, itembX, curItemY + ((itemH - itembH) >> 1),
	// Tool.TRANS_NONE);
	// } else {
	// if (i == 2 && !isOpenLiveGame) {
	// Tool.drawImage(g, itemBg2Img, itembX, curItemY + ((itemH - itembH) >> 1),
	// Tool.TRANS_NONE);
	// } else {
	// Tool.drawImage(g, itemBg0Img, itembX, curItemY + ((itemH - itembH) >> 1),
	// Tool.TRANS_NONE);
	// }
	// }
	// menuY = curItemY + ((itemH - menuH) >> 1);
	//
	// // =============for qq===========
	// int id = i;
	// // if (i > 4 && i < 8) {
	// // id = i - 2;
	// // } else if (i >= 8) {
	// // id = i - 2;
	// // }
	// // if (i < 3) {
	// // Tool.drawTile(g, menuImg, id, menuW, menuH, Tool.TRANS_NONE, menuX,
	// menuY);
	// // } else if (i >= 3 && i < 5) {// for QQ
	// // Tool.drawTile(g, QQMenu, i - 3, QQMenu.getWidth(), menuH,
	// Tool.TRANS_NONE, (uiWidth - QQMenu.getWidth()) >> 1, menuY);
	// // } else if (i > 4 && i < 7) {
	// // Tool.drawTile(g, menuImg, id, menuW, menuH, Tool.TRANS_NONE, menuX,
	// menuY);
	// // } else if (i == 7) {
	// // Tool.drawTile(g, QQMenu, 2, QQMenu.getWidth(), menuH, Tool.TRANS_NONE,
	// (uiWidth - QQMenu.getWidth()) >> 1, menuY);
	// // } else if (i == 8) {
	// // Tool.drawTile(g, menuImg, id, menuW, menuH, Tool.TRANS_NONE, menuX,
	// menuY);
	// // }
	//
	// if (i == 5) { //QQ��Ϸ����
	// Tool.drawTile(g, QQMenu, 2, QQMenu.getWidth(), menuH, Tool.TRANS_NONE,
	// (uiWidth - QQMenu.getWidth()) >> 1, menuY);
	// } else {
	// Tool.drawTile(g, menuImg, id, menuW, menuH, Tool.TRANS_NONE, menuX,
	// menuY);
	// }
	//
	// // ==============================
	//
	// if (i == 3) { // ����
	// //if (i == 5) { // ���� for qq
	// int fid = 1;
	// if (bSoundOn) {
	// fid = 0;
	// }
	// Tool.drawTile(g, onoffImg, fid, (onoffImg.getWidth() >> 1),
	// onoffImg.getHeight(), Tool.TRANS_NONE, menuX + (menuW >> 1), menuY);
	// }
	// }
	// }
	public void showMessage(String str) { // , byte type
		int step = font.getHeight() / 10;
		if (step == 0) {
			step = 1;
		}
		// int height = uiHeight / 3;
		// if (height < (fontH << 2) + 5) {
		// height = (fontH << 2) + 5;
		// }
		message = new ScrollText(str, MESSAGE_WIDTH, MESSAGE_HEIGHT, font.getHeight(), step, font);
	}
	public void fillBgRect(Graphics g, int rx, int ry, int rw, int rh) {
		Common.setUIClip(g);
		// g.setColor(0x082D47);
		// g.drawRect(rx, ry, rw - 1, rh - 1);
		// g.setColor(0x56FFF8);
		// g.drawRect(rx + 1, ry + 1, rw - 3, rh - 3);
		// g.setColor(0x02A59E);
		// g.drawRect(rx + 2, ry + 2, rw - 5, rh - 5);
		// Tool.fillAlphaRect(g, 0xAA000000, rx + 3, ry + 3, rw - 6, rh - 6);
		Tool.fillAlphaRect(g, 0xAA000000, rx + 4, ry + 4, rw - 8, rh - 8);
		Common.setUIClip(g);
		int roundW = rectRoundImg.getWidth();
		int roundH = rectRoundImg.getHeight();
		g.setColor(0x003CFF);
		g.fillRect(rx + roundW, ry, rw - roundW * 2, 2);
		g.fillRect(rx + roundW, ry + rh - 2, rw - roundW * 2, 2);
		g.fillRect(rx, ry + roundH, 2, rh - roundH * 2);
		g.fillRect(rx + rw - 2, ry + roundH, 2, rh - roundH * 2);
		g.setColor(0x76F1FF);
		g.fillRect(rx + roundW, ry + 2, rw - roundW * 2, 2);
		g.fillRect(rx + roundW, ry + rh - 4, rw - roundW * 2, 2);
		g.fillRect(rx + 2, ry + roundH, 2, rh - roundH * 2);
		g.fillRect(rx + rw - 4, ry + roundH, 2, rh - roundH * 2);
		Tool.drawImage(g, rectRoundImg, rx, ry, Tool.TRANS_MIRROR_ROT180);
		Tool.drawImage(g, rectRoundImg, rx + rw - roundW, ry, Tool.TRANS_ROT180);
		Tool.drawImage(g, rectRoundImg, rx, ry + rh - roundH, Tool.TRANS_NONE);
		Tool.drawImage(g, rectRoundImg, rx + rw - roundW, ry + rh - roundH, Tool.TRANS_MIRROR);
		int topX = -10;
		int topY = -17;
		int leftX = rx + topX;
		Tool.drawImage(g, rectTopImg, leftX, ry + topY, Tool.TRANS_NONE);
		int rightX = topX = rx + rw - topX - rectTopImg.getWidth();
		Tool.drawImage(g, rectTopImg, rightX, ry + topY, Tool.TRANS_MIRROR);
	}
	public void drawMessage(Graphics g, ScrollText text, boolean bBack) {
		if (text != null) {
			fillBgRect(g, MESSAGE_RECT_X, MESSAGE_RECT_Y, MESSAGE_RECT_WIDTH, MESSAGE_RECT_HEIGHT);
			Common.setUIClip(g);
			int mdy = 0;
			if (text.getTotalHeight() < MESSAGE_HEIGHT) {
				mdy = (MESSAGE_HEIGHT - text.getTotalHeight()) >> 1;
			}
			text.draw(g, MESSAGE_X, MESSAGE_Y + mdy, 0xffff00, 0, Tool.TYPE_EDGE);
			if (bBack) {
				Tool.drawImage(g, btnBackImg, MESSAGE_X + MESSAGE_WIDTH - btnBackImg.getWidth(), MESSAGE_Y + MESSAGE_HEIGHT - btnBackImg.getHeight(), Tool.TRANS_NONE);
			}
			if (text.getTextString().trim().startsWith("��")) {
				Tool.drawImage(g, btnYoumiImg, 520 * uiWidth / 800, 164 * uiHeight / 480, Tool.TRANS_NONE);
			}
		}
	}
	public void showHintMessage(String str) {
		if (hintMessage != null) {
			return;
		}
		int width = uiWidth - 4;
		hintMessage = new MovingString(str, width, 4);
		hintMessage.setFromRight();
		hintMessage.start();
	}
	public void drawHintMessage(Graphics g) {
		if (hintMessage != null) {
			int mx = (uiWidth - hintMessage.getWidth()) >> 1;
			int my = uiHeight - fontH - 2; // uiHeight - btnBgImg.getHeight() -
											// 2; //uiHeight - fontH - 5;
											// //uiHeight - btnOkImg.getHeight()
											// - fontH - 5;
			Tool.fillAlphaRect(g, 0x88000000, mx - 10, my - 2, hintMessage.getWidth() + 20, fontH + 4);
			hintMessage.draw(g, mx, my, 0xffffff, 0, Tool.TYPE_SHADOW);
		}
	}
	public void showConfirm(String str) {
		int step = font.getHeight() / 10;
		if (step == 0) {
			step = 1;
		}
		scrollText = new ScrollText(str, MESSAGE_WIDTH, MESSAGE_HEIGHT, fontH, step, font);
	}
	// public void drawConfirm(Graphics g) {
	// if (scrollText != null) {
	// Common.setUIClip(g);
	// Tool.fillAlphaRect(g, 0xAA000000, 0, 0, uiWidth, uiHeight);
	//
	// int mx = (uiWidth - scrollText.getWidth()) >> 1;
	// int my = ((uiHeight - scrollText.getHeight()) >> 1) - 20;
	//
	// //Tool.drawImageRect(g, mRectImg, mx - 10, my - 5, scrollText.getWidth()
	// + 20, scrollText.getHeight() + 10, 0x01B7F0);
	// fillBgRect(g, mx - 10, my - 10, scrollText.getWidth() + 20,
	// scrollText.getHeight() + 20);
	//
	// int mdy = 0;
	// if (scrollText.getTotalHeight() < scrollText.getHeight()) {
	// mdy = (scrollText.getHeight() - scrollText.getTotalHeight()) >> 1;
	// }
	//
	// scrollText.draw(g, mx, my + mdy, 0xffff00, 0, Tool.TYPE_EDGE);
	//
	// drawButtonOk(g);
	// drawButtonBack(g);
	// }
	// }
	public void drawConfirm(Graphics g) {
		// System.out.println("scrollText--------------------" + scrollText);
		if (scrollText != null) {
			Common.setUIClip(g);
			Tool.fillAlphaRect(g, 0xAA000000, 0, 0, uiWidth, uiHeight);
			// fillBgRect(g, MESSAGE_RECT_X, MESSAGE_RECT_Y, MESSAGE_RECT_WIDTH,
			// MESSAGE_RECT_HEIGHT);
			//
			// int mdy = 0;
			// if (scrollText.getTotalHeight() < MESSAGE_HEIGHT) {
			// mdy = (MESSAGE_HEIGHT - scrollText.getTotalHeight()) >> 1;
			// }
			//
			// scrollText.draw(g, MESSAGE_X, MESSAGE_Y + mdy, 0xffff00, 0,
			// Tool.TYPE_EDGE);
			drawMessage(g, scrollText, false);
			// Common.setUIClip(g);
			drawButtonOk(g);
			drawButtonBack(g);
			// System.out.println("��ť���ꡪ��X" + "��ť���ꡪ��y");
		}
	}
	protected void paint(Graphics g) {
		g.setClip(0, 0, realWidth, realHeight);
		g.setColor(0);
		g.fillRect(0, 0, realWidth, realHeight);
		g.translate(realDx, realDy);
		Common.setUIClip(g);
		g.setFont(font);
		try {
			draw(g);
		} catch (Exception e) {
		}
		// g.setColor(0xff0000);
		// g.setClip(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
		// g.drawString(debugStr, 0, 0, 0);
		// g.setColor(0xff0000);
		// g.setClip(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
		// g.drawString(String.valueOf(loadingStep), 0, 0, 0);
		// if (bSensorAble) {
		// Common.setUIClip(g);
		// g.setColor(0xffffff);
		// g.fillRect(0, 0, uiWidth, fontH + 10);
		// Tool.drawString(g, "X:" + DoMidlet.SensorX + " Y:" + DoMidlet.SensorY
		// + " Z:" + DoMidlet.SensorZ + " moveToX:" + moveToX + " moveToY:" +
		// moveToY, 5, 5, 0, 0xffffff);
		// //System.out.println("X:" + x_int + " Y:" + y_int + " Z:" + z_int);
		// }
		// #if (clearUpMemory==freeMemoryAndGC)
		// # System.gc();
		// #endif
		g.drawString(String.valueOf(Runtime.getRuntime().freeMemory()), 0, -50, 0);
		// if (btnOkImg != null) {
		// Tool.drawImageScale(g, btnYesImg, 200, 150, 15, 15);
		// Tool.drawImageScale(g, btnOkImg, 25, 10, 26, 16, 200, 150, 50, 50);
		// }
		if (realDx != 0 || realDy != 0) {
			g.translate(-realDx, -realDy);
			g.setClip(0, 0, realWidth, realHeight);
			g.setColor(0);
			g.fillRect(0, 0, realWidth, realDy);
			g.fillRect(0, realDy, realDx, SCREEN_HEIGHT);
			g.fillRect(realDx + SCREEN_WIDTH, realDy, realWidth - realDx - SCREEN_WIDTH, SCREEN_HEIGHT);
			g.fillRect(0, realDy + SCREEN_HEIGHT, realWidth, realHeight - realDy - SCREEN_HEIGHT);
			// int bg1Num = 14;
			// int bg1Height = frameBg1Img.getHeight() * bg1Num;
			Tool.drawImage(g, frameTopImg, realDx, realDy - frameTopImg.getHeight(), Tool.TRANS_NONE);
			Tool.drawImage(g, frameLRImg, realDx - frameLRImg.getWidth(), realDy, Tool.TRANS_NONE);
			Tool.drawImage(g, frameLRImg, realDx + SCREEN_WIDTH, realDy, Tool.TRANS_MIRROR);
			Tool.drawImage(g, frameBottomImg, realDx - ((frameBottomImg.getWidth() - SCREEN_WIDTH) >> 1), realDy + SCREEN_HEIGHT - 29, Tool.TRANS_NONE);
			final int starDx = 30;
			final int starDy = 30;
			Tool.drawImage(g, frameStarImg, realDx - starDx, realDy - starDy, Tool.TRANS_NONE);
			Tool.drawImage(g, frameStarImg, realDx + SCREEN_WIDTH + starDx - frameStarImg.getWidth(), realDy - starDy, Tool.TRANS_MIRROR);
		}
	}
	protected void keyPressed(int keyCode) {
		/*
		 * //#if (MobileType == N97) //# if (isSizeChanged) { //# return; //# }
		 * //#endif
		 */
		if (keyLock) {
			return;
		}
		Common.keyPressed(keyCode);
		Common.clearKeyReleasedStates();
	}
	protected void keyReleased(int keyCode) {
		Common.keyReleased(keyCode);
	}
	protected void pointerPressed(int x, int y) {
		Common.pointerPressed(x, y);
	}
	protected void pointerReleased(int x, int y) {
		if (Common.isPointerPressed()) {
			return;
		}
		Common.pointerReleased(x, y);
	}
	protected void pointerDragged(int x, int y) {
		if (Common.isPointerPressed()) {
			return;
		}
		Common.pointerDragged(x, y);
	}
	/**
	 * �Ƿ񴥷�ȷ����
	 * 
	 * @return �Ƿ񴥷�ȷ����
	 */
	public static boolean isOkPressed() {
		if (Common.isKeyPressed(Common.FIRE_PRESSED, true) || Common.isKeyPressed(Common.KEY_NUM5_PRESSED, true)) {
			return true;
		}
		return false;
	}
	/**
	 * �Ƿ񴥷��ϼ�
	 * 
	 * @return �Ƿ񴥷��ϼ�
	 */
	public static boolean isUpPressed() {
		if (Common.isKeyPressed(Common.UP_PRESSED, true) || Common.isKeyPressed(Common.KEY_NUM2_PRESSED, true)) {
			return true;
		}
		return false;
	}
	/**
	 * �Ƿ񴥷��¼�
	 * 
	 * @return �Ƿ񴥷��¼�
	 */
	public static boolean isDownPressed() {
		if (Common.isKeyPressed(Common.DOWN_PRESSED, true) || Common.isKeyPressed(Common.KEY_NUM8_PRESSED, true)) {
			return true;
		}
		return false;
	}
	/**
	 * �Ƿ񴥷����
	 * 
	 * @return �Ƿ񴥷����
	 */
	public static boolean isLeftPressed() {
		if (Common.isKeyPressed(Common.LEFT_PRESSED, true) || Common.isKeyPressed(Common.KEY_NUM4_PRESSED, true)) {
			return true;
		}
		return false;
	}
	/**
	 * �Ƿ񴥷��Ҽ�
	 * 
	 * @return �Ƿ񴥷��Ҽ�
	 */
	public static boolean isRightPressed() {
		if (Common.isKeyPressed(Common.RIGHT_PRESSED, true) || Common.isKeyPressed(Common.KEY_NUM6_PRESSED, true)) {
			return true;
		}
		return false;
	}
	/**
	 * ��ʼ����
	 * 
	 * @param file
	 *            �����ļ�
	 */
	public void startMusic(String file) {
		curMusicFile = file;
		if (!bSoundOn) {
			return;
		}
		if (music == null) {
			music = new Sound(curMusicFile, Sound.MIDI, true);
		} else {
			if (music.soundFile.compareTo(curMusicFile) != 0) {
				music.setSound(curMusicFile, Sound.MIDI, true);
			}
		}
		music.start();
	}
	/**
	 * ��ʼ��ǰ����
	 */
	public void startMusic() {
		if (curMusicFile != null) {
			startMusic(curMusicFile);
		}
	}
	/**
	 * �ر�����
	 */
	public void closeMusic() {
		if (music != null) {
			music.close();
			music = null;
		}
	}
	protected void hideNotify() {
		// if ((qqBox.isInForum())) {
		// return;
		// }
		closeMusic();
		if (state != STATE_PAUSE) {
			preNotifyState = state;
		}
		state = STATE_PAUSE;
	}
	protected void showNotify() {
	}
	/** ********************************************************************* */
	/** *************************** whb add ********************************* */
	/** ********************************************************************* */
	// ���ӽ�ѧ�ؿ���idΪ10����11������
	// ��������ؿ���idΪ11����12������
	// �������㣬����ֵ����Ϊ10
	static final byte SHARK_TYPE = 10;
	Image sharkImg;
	int addSharkTimes;
	// ���ӵ���
	int addEelTimes;
	// ����ѭ�����ֵļ��ʱ��
	// static final int ADD_SHARK_INTERVAL = 100;
	// ǱˮԱ
	Diver diver;
	// ǱˮԱ�Ļ��Χ��
	public static int DIVER_MAX_Y = 300;
	public static int DIVER_MIN_Y = 150;
	public static int DIVER_AUTO_MAX_Y = 280;
	public static int DIVER_AUTO_MIN_Y = 150;
	// ��ɫ����״̬�ж�
	boolean isMaySelectGirl;
	/* ѡ��Ů�Խ�ɫʱ�����ʾ���� */
	// static final String CHOOSE_GRIL_INFO = "����δ����������ѡ��ý�ɫ������Ϸ���봥����Ļ���أ�";
	static final String CHOOSE_GRIL_INFO = "����δ����������ѡ��ý�ɫ������Ϸ���뷵�أ�";
	/* ǱˮԱ�����������ݼ������� */
	static final int[] DIVER_FISH_SORT = {3, 6, 9};
	/* ǱˮԱ�ļ�Ǯ */
	static final int[] DIVER_PRICE = {400, 800, 1200};
	/* ǱˮԱ�̵����ϢͼƬ */
	Image[][] diverInfoImgs;
	/* ���ߵ���ϢͼƬ */
	Image[] toolImg;
	// byte shopDir;
	// int oldShopIndex;
	/* ��Ӷ��ͼƬ */
	Image btnEmployImg;
	public static final int EMPLOY_SHOP_NUM = 4;
	int employShopIndex;
	static final String STR_DIVER_EMPLOY_SUCCESS = "��Ӷ�ɹ���\n������������ǱˮԱ��ս�����ɣ�";
	static final String STR_DIVER_EMPLOY_FAIL0 = "��ǱˮԱ��ӵ�У������Ӷ��\n����ѡ���Ӷ����ǱˮԱ��";
	static final String STR_DIVER_EMPLOY_FAIL1 = "��Ҳ��㣬��Ӷʧ�ܣ�\n���������������㹻�Ľ�ҡ�";
	static final String STR_DIVER_EMPLOY_FAIL2 = "��δ��Ӷ�͵ȼ�ǱˮԱ��\n�����ȹ�Ӷ�ͼ����ǱˮԱ��һ��һ�����";
	static final String STR_BUY_NO_SHARK_FAIL = "���Ѿ�ӵ�����������ߣ���ʹ�������������";
	static final String STR_BUY_NO_SHARK_SUCCESS = "����ɹ���\n���Ѿ�ӵ��10�����������ߡ�";
	static final String STR_BUY_CONTROL_FAIL = "���Ѿ�ӵ��ǱˮԱ���Ƶ��ߣ����蹺��";
	static final String STR_BUY_CONTROL_SUCCESS = "����ɹ���\n���Ѿ�ӵ��ǱˮԱ���Ƶ��ߣ�";
	static final String STR_HOOK_FAIL1 = "���ܵ������㣡";
	static final String STR_BOAT_BUY_HINT = "������������������������߳�������";
	static final String STR_TEACH_OVER = "���ؽ�����";
	/* ��ӶǱˮԱ��ID */
	byte diverID = -1;
	/* ����������� */
	// int noSharkToolNum = 0;
	/* ǱˮԱ���Ƶ��� */
	boolean isHasControl;
	/* ������� */
	boolean isSharkCannotCatch;
	/* �������ʱ������ */
	int sharkCannotCatchCount;
	static final int sharkCannotCatchTime = 400;// ��ʱ����ʱ��
	/* �жϵ�һ�ν�����Ϸ�󣬽����ѧ�ؿ� */
	boolean isFirstGame = true;
	/* ��ѧλ�ã������û */
	// byte teachID;
	// boolean isSharkAppear;
	// int sharkAppearCount;
	// int sharkAppearCount1;// ������ֵ���ʧһ��ʱ�� �㲻������
	// static final int SHARKAPPEARTIME = 90;// ����������ֶ�����֮���ٳ�����
	// boolean stopFishAppear;
	/* ��һ��������ֵ�ʱ�򣬴��������� */
	// boolean isAddFish;
	// int addFishTimes1;
	/* ��ѧʱ�����ù��� */
	static final String CANNOTBUY = "����δ�������������ܹ���õ��ߣ��뷵�أ�";
	static final String CANNOTEMPLOY = "���Ľ�ҳ��㣬��ֱ�ӹ�Ӷ��ɫǱˮԱ��";
	static final String CANNOTRETURN = "����δ��ӶǱˮԱ";
	static final String CANNOTCONTIUNEEMPLOY = "���ѹ�ӶǱˮԱ���뷵�أ�";
	Image fingerImg;
	// Image[] tipImg;
	Image employedImg;
	Image nanshengImg, nvshengImg;
	boolean flag1_Tip;// �����ʾ1��������bug����
	boolean isOpenGame;
	boolean isLiveGameOpened;
	int liveGameBoatID;
	byte liveGameDiverID = -1;
	byte normalGameDiveID = -1;
	// #if (KeyCodeType==touch)
	// # static final String CANOPENLIVEGAME = "����ģʽδ��������Ϸͨ�غ��Զ�������\n�봥����Ļ���أ�";
	// #else
	static final String CANOPENLIVEGAME = "����ģʽδ��������Ϸͨ�غ��Զ�������\n�뷵�أ�";
	// #endif
	boolean isShowLiveGame;
	// boolean isLiveGameSucess;
	static final byte[][] STAGE_PROP_TYPE = {
			//
			// { 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6 },// test
			// {PROPS_MAGNET, PROPS_MAGNET, PROPS_MAGNET, PROPS_MAGNET,
			// PROPS_MAGNET, PROPS_MAGNET},// 1
			{PROPS_GOLD_10, PROPS_MAGNET, PROPS_GOLD_30, PROPS_MAGNET, PROPS_LIGHTNING, PROPS_GOLD_20, PROPS_GOLD_20},// 1
			{PROPS_GOLD_10, PROPS_MAGNET, PROPS_GOLD_30, PROPS_GOLD_20, PROPS_MAGNET, PROPS_GOLD_10, PROPS_LIGHTNING, PROPS_GOLD_5, PROPS_GOLD_5},// 2
			// {PROPS_GOLD_30, PROPS_GOLD_5, PROPS_MAGNET, PROPS_GOLD_5,
			// PROPS_LIGHTNING, PROPS_GOLD_20, PROPS_GOLD_20},// 3
			{PROPS_MAGNET, PROPS_MAGNET, PROPS_MAGNET, PROPS_MAGNET, PROPS_MAGNET},// 3 //0219
			{PROPS_GOLD_30, PROPS_GOLD_10, PROPS_MAGNET, PROPS_GOLD_30, PROPS_LIGHTNING, PROPS_GOLD_10, PROPS_GOLD_20},// 4
			{PROPS_GOLD_20, PROPS_GOLD_20, PROPS_MAGNET, PROPS_GOLD_30, PROPS_LIGHTNING, PROPS_GOLD_10, PROPS_GOLD_20},// 5
			{PROPS_GOLD_20, PROPS_GOLD_20, PROPS_MAGNET, PROPS_GOLD_30, PROPS_LIGHTNING, PROPS_GOLD_10, PROPS_GOLD_20},// 6
			{PROPS_GOLD_20, PROPS_GOLD_20, PROPS_GOLD_5, PROPS_MAGNET, PROPS_GOLD_30, PROPS_LIGHTNING, PROPS_GOLD_30, PROPS_GOLD_10, PROPS_GOLD_5},// 7
			{PROPS_GOLD_20, PROPS_GOLD_10, PROPS_GOLD_10, PROPS_MAGNET, PROPS_GOLD_30, PROPS_LIGHTNING, PROPS_GOLD_30, PROPS_GOLD_10, PROPS_GOLD_10},// 8
			{PROPS_GOLD_20, PROPS_GOLD_20, PROPS_GOLD_5, PROPS_LIGHTNING, PROPS_GOLD_30, PROPS_LIGHTNING, PROPS_GOLD_10, PROPS_GOLD_20, PROPS_LIGHTNING, PROPS_GOLD_5, PROPS_GOLD_10, PROPS_GOLD_30},// 9
			{PROPS_GOLD_20, PROPS_GOLD_20, PROPS_GOLD_5, PROPS_LIGHTNING, PROPS_GOLD_30, PROPS_LIGHTNING, PROPS_GOLD_10, PROPS_GOLD_20, PROPS_GOLD_5, PROPS_GOLD_10, PROPS_GOLD_30},// 10
			{PROPS_MAGNET, PROPS_GOLD_20, PROPS_LIGHTNING, PROPS_MAGNET, PROPS_GOLD_5, PROPS_GOLD_30, PROPS_MAGNET, PROPS_GOLD_5, PROPS_GOLD_10, PROPS_GOLD_30},// teach
			{PROPS_GOLD_10, PROPS_GOLD_5, PROPS_GOLD_10, PROPS_LIGHTNING, PROPS_GOLD_10, PROPS_MAGNET, PROPS_GOLD_10, PROPS_GOLD_5},// live
	};
	int curPropNum;
	// ����ģʽ
	int liveGameStageID = 1;
	Image stageIDImg;
	Image magnetImg;// ħ�ô��� , magnetTipImg
	static final int magnetTotal = 30;
	int magnetCount = magnetTotal;
	Vector bubble = new Vector();
	Image bubble2Img;
	int bubble2Count;
	// boolean isClearScore;// ���������Ļ����
	Image fishboneImg;
	Vector fishbone = new Vector();
	// Image controlTipImg;
	// boolean closeEmployShopTip;
	boolean bGameFail;
	Image btnIntoImg, sysMenuShopImg;
	boolean isShowMessage;// ����������С��Ļ������ʾ
	int isShowMessageCountTime;
	static final String STR_HOOK_FAIL2 = "���ܵ����Ʒ�ֵ���";
	static final String STR_HOOK_FAIL3 = "��������ֻ��";
	Image flagImg;
	boolean isCloseMessage;
	// Image moreGameImg[] = new Image[3];
	// byte moreGameIndex;
	// static final String STR_MORE_GAME_TITLE1 = "���ι����ҳ�Ʒ";
	// static final String[] STR_MORE_GAME_TITLE = {"--����ս��2012--",
	// "--��֮ŭ-ʥ����ʿ--", "--������֮�¹ⱦ��--"};
	// static final String[] STR_MORE_GAME_INFO =
	// {"δ���ռ�ս�����㽫��Ϊ����ϵ���Ͻ����е�һԱ�����־����׵�ս������Ԧ���������ˣ�������ʵ�ı�ըЧ�������ɵĲٿظо�����ˬ�Ŀ��и񶷡�", //
	// "�ڰ���ŵ��½�����������������壬��ʥ�ĵ��������а��Ķ�ħ�壬�����˳�����ǧ���ʥս��ֱ�����ħ������˹����ӡ������ŵ��½������һ˿��Ϣ��ʱ�䡣",
	// "ԭ֭ԭζ����ͷ���ಽ��������Ц����Ӱ������Ȩ��������Ⱦ��ף�ֻΪ��Զ�ľ��䡶�����Ρ���"};
	// String[] moreGameInfoStr;
	// static final String[] MOREGAME_URL = {"Tj1Url", "Tj2Url", "Tj3Url"};
	public static final int MORE_GAME_NUM = 2;
	Image[] moreGameImg;
	int moreGameIndex;
	static final String STUDIO_TITLE = "���ι����ҳ�Ʒ";
	static final String[] STR_MORE_GAME_TITLE = {"������ս��2012��", "����Ե-�λ����ɡ�"};
	static final String[] STR_MORE_GAME_INFO = {"    δ���ռ�ս�����㽫��Ϊ����ϵ���Ͻ����е�һԱ�����־����׵�ս������Ԧ���������ˣ�������ʵ�ı�ըЧ�������ɵĲٿظо�����ˬ�Ŀ��и񶷡�", //
			"    �ǹ¶�һ�����������ɣ����ǰ��޲��࣬�����˼䣿��ȴ�����쳾����������֮�Σ����������ظ�����ն���������������ɵĴ�Խ֮��������������������㳩���λ��ɾ���", //
	// "ԭ֭ԭζ����ͷ���ಽ��������Ц����Ӱ������Ȩ��������Ⱦ��ף�ֻΪ��Զ�ľ��䡶�����Ρ���"//
	};
	// String[] moreGameInfoStr;
	static final String[] MOREGAME_URL = {"Tj1Url", "Tj2Url"}; // , "Tj3Url"
	ScrollText[] moreGameInfo;
	Image btnExitImg; // btnDownImg,
	public void gotoMoreGame() {
		moreGameImg = new Image[MORE_GAME_NUM];
		moreGameInfo = new ScrollText[MORE_GAME_NUM];
		for (int i = 0; i < MORE_GAME_NUM; i++) {
			moreGameImg[i] = Tool.createImage("/mg" + i + ".png");
			moreGameInfo[i] = new ScrollText(STR_MORE_GAME_INFO[i], (uiWidth / 2) - 10, uiHeight - btnBgImg.getHeight() - moreGameImg[i].getHeight() - ((fontH + 3) * 2) - 30, fontH, 1, font);
		}
		// btnDownImg = loadResImage("/btnDown.png");
		btnExitImg = loadResImage("/m_exit.png");
		moreGameIndex = -1;
		state = STATE_MORE_GAME;
	}
	public void drawMoreGame(Graphics g) {
		Tool.drawImage(g, menuBgImg, 0, 0, Tool.TRANS_NONE);
		Tool.fillAlphaRect(g, 0xAA000000, 0, 0, uiWidth, uiHeight);
		int panelX;
		int panelW = uiWidth / MORE_GAME_NUM;
		int imageX;
		int imageY = 20;
		for (int i = 0; i < MORE_GAME_NUM; i++) {
			panelX = (panelW * i);
			imageX = panelX + ((panelW - moreGameImg[i].getWidth()) >> 1);
			Tool.drawImage(g, moreGameImg[i], imageX, imageY, Tool.TRANS_NONE);
			Common.setUIClip(g);
			int studioY = imageY + moreGameImg[i].getHeight() + 3;
			Tool.drawString(g, STUDIO_TITLE, panelX + ((panelW - font.stringWidth(STUDIO_TITLE)) >> 1), studioY, 0xffffff, 0x0000ff, Tool.TYPE_SHADOW);
			int nameY = studioY + fontH + 3;
			int jump = 0;
			if (i == moreGameIndex) {
				jump = 1;
			}
			Tool.drawString(g, STR_MORE_GAME_TITLE[i], panelX + ((panelW - font.stringWidth(STR_MORE_GAME_TITLE[i])) >> 1), nameY, 0xffff00, 0x0, jump, Tool.TYPE_EDGE);
			int infoY = nameY + fontH + 3;
			if (moreGameInfo[i] != null) {
				moreGameInfo[i].draw(g, panelX + ((panelW - moreGameInfo[i].getWidth()) >> 1), infoY, 0xffffff, -1, Tool.TYPE_SHADOW);
			}
		}
		// drawButton(g, btnDownImg, 0, uiHeight - buttonHeight);
		// drawButton(g, btnDownImg, uiWidth / 2, uiHeight - buttonHeight);
		drawButton(g, btnExitImg, uiWidth - buttonWidth, uiHeight - buttonHeight);
	}
	// void initMoreGame() {
	// for (int i = 0; i < 2; i++) {
	// moreGameImg[i] = Tool.createImage("/mgi"+i+".png");
	// }
	//
	// moreGameInfoStr=Tool.getStringArray(STR_MORE_GAME_INFO, drawWidth, font)
	// }
	/** ******************************************************************** */
	private void gotoLiveGameContinue() {
		// 110818
		updateAchLiveGame();
		liveGameStageID++;
		liveGameGold = gold;
		liveGameAllFishToScoreNum = allFishToScoreNum;
		saveRecord();
		// showMessage("���ѴﵽĿ�������������Ļ������һ����ս��");
		// showMessage("���ѴﵽĿ��������������һ����ս��");
		// goal += 50;// ��ս�ɹ������ӵ�Ŀ�����
		goal += (goal + 50);// ��ս�ɹ������ӵ�Ŀ�����
		gameTimesLeft = gameTimesTotal;
		curPropNum = 0;// ���ݻ�ԭ
		// isClearScore = true;
		showConfirm("���ѴﵽĿ��������������һ����ս��");
		state = STATE_LIVEGAME_CONTINUE;
	}
	private void gotoLiveGameSuccess() {
		// isLiveGameSucess = true;
		// showMessage("���Ѵﵽ��߷�������ϲ����ս�ɹ���������Ļ������Ϸ���˵���");
		// showMessage("���Ѵﵽ��߷�������ϲ����ս�ɹ����뷵����Ϸ���˵���");
		liveGameGold = gold;
		liveGameAllFishToScoreNum = allFishToScoreNum;
		saveRecord();
		curPropNum = 0;// ���ݻ�ԭ
		// isClearScore = true;
		showConfirm("���Ѵﵽ��߷�������ϲ����ս�ɹ����뷵����Ϸ���˵���");
		state = STATE_LIVEGAME_SUCCESS;
	}
	void cycleLiveGame() {
		// System.out.println(score);
		if (score > 99999) {
			gotoLiveGameSuccess();
		} else if (score >= goal) {
			gotoLiveGameContinue();
		}
	}
	// void clearTeachRes() {
	// setArrayAsNull(tipImg);
	// }
	void clearTeachData() {
		// isFirstGame = false; //xFu 0113
		diver = null;
		diverID = -1;
		isHasControl = false;
		boatID = 0;
		playerScore = 0;
	}
	void drawDiver(Graphics g) {
		if (diver != null) {
			diver.draw(g, viewMapX, viewMapY, 0, 0);
		}
	}
	/**
	 * �����Ӷ�̵�
	 */
	// public void gotoEmployShop() {
	// // �����̵���Դ
	// shopImg = Tool.createImage("/shop.png");
	// goldGetImg = Tool.createImage("/goldGet.png");
	// boughtImg = Tool.createImage("/bought.png");
	//
	// // whb add �ѹ�ӶͼƬ
	// employedImg = Tool.createImage("/employ.png");
	// // whb add ����ǱˮԱ��������ߵȵ���Դ
	// diverInfoImgs = new Image[3][2];
	// for (int i = 0; i < diverInfoImgs.length; i++) {
	// for (int j = 0; j < diverInfoImgs[i].length; j++) {
	// diverInfoImgs[i][j] = Tool.createImage("/diverinfo" + i + "" + j +
	// ".png");
	// }
	// }
	//
	// toolImg = new Image[2];
	// toolImg[0] = Tool.createImage("/tool1.png");
	// toolImg[1] = Tool.createImage("/tool2.png");
	//
	// infoTextImg = Tool.createImage("/diverinfo02.png");
	// employShopIndex = 0;
	// state = STATE_EMPLOY_SHOP;
	//
	// }
	//
	// private void handleEmployShopKey() {
	// //boolean changeInfo = false;
	// int bakIndex = employShopIndex;
	//
	// if (Common.isKeyPressed(Common.BUTTON_BACK_PRESSED, true)) {
	// if (stageID == 10) {
	// if (diverID == 0) {
	// state = STATE_GAME;
	// closeEmployShopTip = true;
	// }
	// return;
	// }
	// /** ********************************* */
	// state = STATE_GAME;
	//
	// } else if (isUpPressed()) {
	// if (stageID != 10)
	// employShopIndex--;
	// else {
	// if (closeEmployShopTip) {
	// employShopIndex--;
	// }
	// }
	// if (employShopIndex < 0) {
	// employShopIndex = 5;
	// }
	// //changeInfo = true;
	// } else if (isDownPressed()) {
	// if (stageID != 10)
	// employShopIndex++;
	// else {
	// if (closeEmployShopTip) {
	// employShopIndex++;
	// }
	// }
	// if (employShopIndex > 5) {
	// employShopIndex = 0;
	// }
	// //changeInfo = true;
	// } else if (isLeftPressed()) {
	// if (stageID != 10) {
	// if (employShopIndex == 4) {
	// employShopIndex = 3;
	// } else if (employShopIndex == 5) {
	// employShopIndex = 4;
	// }
	// } else {
	// if (closeEmployShopTip) {
	// if (employShopIndex == 4) {
	// employShopIndex = 3;
	// } else if (employShopIndex == 5) {
	// employShopIndex = 4;
	// }
	// }
	// }
	// //changeInfo = true;
	// } else if (isRightPressed()) {
	// if (stageID != 10) {
	// if (employShopIndex == 3) {
	// employShopIndex = 4;
	// } else if (employShopIndex == 4) {
	// employShopIndex = 5;
	// }
	// } else {
	// if (employShopIndex == 3) {
	// employShopIndex = 4;
	// } else if (employShopIndex == 4) {
	// employShopIndex = 5;
	// }
	// }
	// //changeInfo = true;
	// } else if (isOkPressed() || Common.isKeyPressed(Common.BUTTON_OK_PRESSED,
	// true)) {
	//
	// if (employShopIndex < 3) {
	// if (stageID == 10) {// ��ѧ�Ĺ�Ӷ
	// if (teachID == TEACH_DIVER) {// ��ѧ�Ĺ�Ӷ
	// // if (diverID == 2) {// ֻ���Թ�Ӷ��ߵȼ�ǱˮԱ
	// gold -= DIVER_PRICE[employShopIndex];
	// diverID = (byte) employShopIndex;
	// diver = new Diver(diverID);
	// showMessage(STR_DIVER_EMPLOY_SUCCESS);
	// teachID = TEACH_5;
	// } else {
	// if (employShopIndex == 0) {
	// showMessage(STR_DIVER_EMPLOY_FAIL0);
	// } else {
	// showMessage(STR_DIVER_EMPLOY_FAIL1);
	// }
	// }
	// /** ********************************************* */
	// } else if (employShopIndex <= diverID) {
	// showMessage(STR_DIVER_EMPLOY_FAIL0);
	// } else if (employShopIndex > diverID + 1) {
	// showMessage(STR_DIVER_EMPLOY_FAIL2);
	// } else if (gold < DIVER_PRICE[employShopIndex]) {
	// if (!isOpenGame)
	// showMessage(STR_DIVER_EMPLOY_FAIL1);
	// else
	// gotoSmsUI(SMS_BUY_GOLD2);
	// } else {
	// gold -= DIVER_PRICE[employShopIndex];
	// diverID = (byte) employShopIndex;
	// diver = new Diver(diverID);
	// normalGameDiveID = diverID;
	// saveRecord();
	// showMessage(STR_DIVER_EMPLOY_SUCCESS);
	// }
	// } else if (employShopIndex == 3) {
	// // if (noSharkToolNum == 0) {
	// // // ��ʾ�̴�
	// // // gotoSMS((byte) 0);
	// // } else {
	// // showMessage(STR_BUY_NO_SHARK_FAIL);
	// // }
	// if (stageID == 10) {
	// showMessage("����δ�������������ܹ���õ��ߣ�");
	// } else {
	// gotoSmsUI(SMS_BUY_MAGNET);
	// }
	// } else if (employShopIndex == 4) {
	// if (!isHasControl) {
	// // ��ʾ�̴�
	// // gotoSMS((byte) 1);
	// if (stageID == 10) {
	// showMessage("����δ�������������ܹ���õ��ߣ�");
	// } else { //QQ 0214
	// if (diverID == 2) {
	// gotoSmsUI(SMS_BUY_DIVER_CONTROL);
	// } else {
	// showMessage("��������������߼�ǱˮԱ���ſɿ����ù��ܣ�");
	// }
	// }
	// } else {
	// showMessage(STR_BUY_CONTROL_FAIL);
	// }
	// } else if (employShopIndex == 5) {
	// // ������
	// if (stageID == 10) {
	// showMessage("����δ�������������ܹ���õ��ߣ�");
	// } else {
	// gotoSmsUI(SMS_BUY_GOLD);
	// }
	// }
	// }
	//
	// if (employShopIndex != bakIndex) {
	// changeEmployShopInfo();
	// }
	//
	// // if (changeInfo) {
	// // if (employShopIndex < 3) {
	// // infoTextImg = Tool.createImage("/diverinfo" + employShopIndex +
	// "2.png");
	// // } else if (employShopIndex == 3) {
	// // infoTextImg = Tool.createImage("/toolInfo1.png");
	// // } else if (employShopIndex == 4) {
	// // infoTextImg = Tool.createImage("/toolInfo2.png");
	// // } else if (employShopIndex == 5) {
	// // infoTextImg = Tool.createImage("/toolInfo3.png");
	// // }
	// // }
	// }
	//
	// /**
	// * ���ƹ�Ӷ�̵�
	// */
	// public void drawEmployShop240(Graphics g) {
	// if (shopImg == null) {
	// return;
	// }
	// Tool.drawImage(g, shopImg, 0, 0, Tool.TRANS_NONE);
	//
	// int barX = 10;
	// int barY = 5;
	// //Tool.drawImage(g, barImg, barX, barY, Tool.TRANS_NONE);
	//
	// // ���ƽ��
	// int goldX = barX + 8;
	// int goldY = barY + ((barImg.getHeight() - goldGetImg.getHeight()) >> 1);
	// Tool.drawImage(g, goldGetImg, goldX, goldY, Tool.TRANS_NONE);
	//
	// int numX = goldX + goldGetImg.getWidth() + 3;
	// int numY = barY + ((barImg.getHeight() - numFontImg1.getHeight()) >> 1);
	// Tool.drawImageNum(g, numFontImg1, String.valueOf(gold), numX, numY,
	// numFontImg1.getWidth() / 10, 0);
	//
	// // �����洬
	// int infoPanelH = 0;
	// //int infoTextH = 50;
	// //int infoSpace = 10;
	// int infoTextH = 45;
	// int infoSpace = 5;
	// for (int i = 0; i < 4; i++) {
	// if (i < 3) {
	// infoPanelH += diverInfoImgs[i][0].getHeight();
	// infoPanelH += infoSpace;
	// } else {
	// infoPanelH += toolImg[0].getHeight();
	// infoPanelH += infoSpace;
	// }
	// }
	// infoPanelH += infoTextH + infoSpace;
	// //int infoPanelX = 30;
	// int infoPanelX = 20;
	//
	// int infoPanelY = barImg.getHeight() + ((uiHeight - barImg.getHeight() -
	// btnBgImg.getHeight() - infoPanelH) >> 1);
	// int infoX = infoPanelX;
	// int infoY = infoPanelY;
	//
	// // ����ѡ�е����沿��
	// int i = 0;
	//
	// int posY = infoY;
	// for (i = 0; i < 6; i++) {
	// if (i < 3) {
	// employShopItemX[i] = infoX;
	// employShopItemY[i] = posY;
	// employShopItemW[i] = diverInfoImgs[i][0].getWidth();
	// employShopItemH[i] = diverInfoImgs[i][0].getHeight();
	// } else if (i == 3) {
	// employShopItemX[i] = infoX;
	// employShopItemY[i] = posY;
	// employShopItemW[i] = 60;
	// employShopItemH[i] = 48;
	// } else if (i == 4) {
	// employShopItemX[i] = infoX + 60;
	// employShopItemY[i] = posY;
	// employShopItemW[i] = 88;
	// employShopItemH[i] = 48;
	// } else if (i == 5) {
	// employShopItemX[i] = infoX + 148;
	// employShopItemY[i] = posY;
	// employShopItemW[i] = 61;
	// employShopItemH[i] = 48;
	// }
	//
	// if (i < 3) {
	// posY += (diverInfoImgs[i][0].getHeight() + infoSpace);
	// if (i == employShopIndex) {
	// posY += (infoTextH + infoSpace);
	// }
	// }
	// }
	//
	// for (i = 0; i <= employShopIndex; i++) {
	// // ��˸
	// Image img = null;
	//
	// if (i == employShopIndex && (Tool.countTimes % 4) < 2) {
	// if (i < 3) {
	// img = diverInfoImgs[i][1];
	// } else {
	// img = toolImg[1];
	// }
	// } else {
	// if (i < 3) {
	// img = diverInfoImgs[i][0];
	// } else {
	// img = toolImg[0];
	// }
	// }
	//
	// if (i == 3) {
	// g.setClip(0, 0, 240, 320);
	// g.drawImage(toolImg[0], infoX, infoY, 0);
	// g.setClip(infoX, infoY, 60, 48);
	// g.drawImage(img, infoX, infoY, 0);
	// } else if (i == 4) {
	// g.setClip(0, 0, 240, 320);
	// g.drawImage(toolImg[0], infoX, infoY, 0);
	// g.setClip(infoX + 60, infoY, 88, 48);
	// g.drawImage(img, infoX, infoY, 0);
	// } else if (i == 5) {
	// g.setClip(0, 0, 240, 320);
	// g.drawImage(toolImg[0], infoX, infoY, 0);
	// g.setClip(infoX + 148, infoY, 61, 48);
	// g.drawImage(img, infoX, infoY, 0);
	// } else {
	// Tool.drawImage(g, img, infoX, infoY, Tool.TRANS_NONE);
	// }
	//
	// // �����־
	// if (i <= diverID) {
	// Tool.drawImage(g, employedImg, infoX + diverInfoImgs[i][0].getWidth() -
	// employedImg.getWidth() - 10, infoY + ((diverInfoImgs[i][0].getHeight() -
	// employedImg.getHeight()) >> 1), Tool.TRANS_NONE);
	// } else if (i >= 3 && isHasControl) {
	// int x = 70;
	// Tool.drawImage(g, boughtImg, infoX + diverInfoImgs[0][0].getWidth() - x,
	// infoY + ((diverInfoImgs[0][0].getHeight() - boughtImg.getHeight()) >> 1),
	// Tool.TRANS_NONE);
	// }
	// // �ۼ�����
	// if (i < 3) {
	// infoY += (diverInfoImgs[i][0].getHeight() + infoSpace);
	// // } else if (i == 3) {
	// // infoY += (toolImg[0].getHeight() + infoSpace);
	// }
	// }
	//
	// if (employShopIndex >= 3 && employShopIndex <= 5) {
	// infoY += (toolImg[0].getHeight() + infoSpace);
	// }
	//
	// Tool.fillAlphaRect(g, 0x88000000, 4, infoY, 260, infoTextH);
	// int infoTextX = infoX + 4;
	// int infoTextY = infoY + ((infoTextH - infoTextImg.getHeight()) >> 1);
	// Tool.drawImage(g, infoTextImg, infoTextX, infoTextY, Tool.TRANS_NONE);
	//
	// infoY += infoTextH + infoSpace;
	//
	// // �����°벿��
	// for (i = employShopIndex + 1; i < 4; i++) {
	//
	// Image img = null;
	// if (i < 3) {
	// img = diverInfoImgs[i][0];
	// } else {
	// img = toolImg[0];
	// }
	//
	// Tool.drawImage(g, img, infoX, infoY, Tool.TRANS_NONE);
	// if (i <= diverID) {
	// Tool.drawImage(g, employedImg, infoX + diverInfoImgs[i][0].getWidth() -
	// employedImg.getWidth() - 10, infoY + ((diverInfoImgs[i][0].getHeight() -
	// employedImg.getHeight()) >> 1), Tool.TRANS_NONE);
	// } else if (i >= 3 && isHasControl) {
	// int x = 70;
	// Tool.drawImage(g, boughtImg, infoX + diverInfoImgs[0][0].getWidth() - x,
	// infoY + ((diverInfoImgs[0][0].getHeight() - boughtImg.getHeight()) >> 1),
	// Tool.TRANS_NONE);
	// }
	//
	// // �ۼ�����
	// if (i < 3) {
	// infoY += (diverInfoImgs[i][0].getHeight() + infoSpace);
	// } else {
	// infoY += (toolImg[0].getHeight() + infoSpace);
	// }
	// }
	// //
	// Image img = null;
	// if (employShopIndex < 3) {
	// img = btnEmployImg;
	// } else {
	// img = btnBuyImg;
	// }
	// //Tool.drawImage(g, img, 0, uiHeight - btnBuyImg.getHeight(),
	// Tool.TRANS_NONE);
	// drawButton(g, img, BUTTON_ALIGN_LEFT);
	// drawButtonBack(g);
	//
	// if (!closeEmployShopTip) {
	// int x = 20;
	// int y = 135;
	// int yy = 48;
	// if (teachID == TEACH_DIVER) {// ������ʾ���
	// //Tool.drawImageRect(g, mRectImg, (uiWidth - MESSAGE_WIDTH) >> 1, y,
	// MESSAGE_WIDTH, 80 + 10, 0x01B7F0);
	// fillBgRect(g, (uiWidth - MESSAGE_WIDTH) >> 1, y, MESSAGE_WIDTH, 80 + 10);
	// g.setClip(0, y + 25, SCREEN_WIDTH, 40);
	// g.drawImage(tipImg[3], SCREEN_WIDTH >> 1, y + 25, 17);
	// Tool.drawImage(g, mapPointerImg, x, SCREEN_HEIGHT - yy -
	// (((Tool.countTimes >> 2) % 3) << 1), 0);
	// } else if (teachID == TEACH_5 && message == null) {// ������ʾ���
	// //Tool.drawImageRect(g, mRectImg, (uiWidth - MESSAGE_WIDTH) >> 1, y,
	// MESSAGE_WIDTH, 80 + 10, 0x01B7F0);
	// fillBgRect(g, (uiWidth - MESSAGE_WIDTH) >> 1, y, MESSAGE_WIDTH, 80 + 10);
	// g.setClip(0, y + 25, SCREEN_WIDTH, 40);
	// g.drawImage(tipImg[4], SCREEN_WIDTH >> 1, y + 25, 17);
	// Tool.drawImage(g, mapPointerImg, SCREEN_WIDTH - x -
	// mapPointerImg.getWidth(), SCREEN_HEIGHT - yy - (((Tool.countTimes >> 2) %
	// 3) << 1), 0);
	// }
	// }
	// }
	// 110716
	public void gotoEmployShop() {
		// �����̵���Դ
		goldGetImg = Tool.createImage("/goldGet.png");
		boughtImg = Tool.createImage("/bought.png");
		employedImg = Tool.createImage("/employ.png");
		diverInfoImgs = new Image[3][2];
		for (int i = 0; i < diverInfoImgs.length; i++) {
			for (int j = 0; j < diverInfoImgs[i].length; j++) {
				diverInfoImgs[i][j] = Tool.createImage("/diverinfo" + i + "" + j + ".png");
			}
		}
		toolImg = new Image[2];
		toolImg[0] = Tool.createImage("/too11.png");
		toolImg[1] = Tool.createImage("/too12.png");
		infoTextImg = Tool.createImage("/diverinfo02.png");
		clearButtonBubbleSet();
		employShopIndex = 0;
		state = STATE_EMPLOY_SHOP;
	}
	// private void handleEmployShopKey() {
	// int bakIndex = employShopIndex;
	// if (Common.isKeyPressed(Common.BUTTON_BACK_PRESSED, true)) {
	// if (stageID == 10) {
	// if (diverID == 0) {
	// state = STATE_GAME;
	// closeEmployShopTip = true;
	// }
	// return;
	// }
	// state = STATE_GAME;
	// } else if (isUpPressed()) {
	// if (stageID != 10) {
	// employShopIndex--;
	// } else {
	// if (closeEmployShopTip) {
	// employShopIndex--;
	// }
	// }
	// if (employShopIndex < 0) {
	// employShopIndex = EMPLOY_SHOP_NUM - 1;
	// }
	// } else if (isDownPressed()) {
	// if (stageID != 10) {
	// employShopIndex++;
	// } else {
	// if (closeEmployShopTip) {
	// employShopIndex++;
	// }
	// }
	// if (employShopIndex >= EMPLOY_SHOP_NUM) {
	// employShopIndex = 0;
	// }
	// } else if (isOkPressed() || Common.isKeyPressed(Common.BUTTON_OK_PRESSED,
	// true)) {
	// if (employShopIndex < 3) {
	// if (stageID == 10) {// ��ѧ�Ĺ�Ӷ
	// // if (teachID == TEACH_HANDUP) {// ��ѧ�Ĺ�Ӷ
	// // // if (diverID == 2) {// ֻ���Թ�Ӷ��ߵȼ�ǱˮԱ
	// // gold -= DIVER_PRICE[employShopIndex];
	// // diverID = (byte) employShopIndex;
	// // diver = new Diver(diverID);
	// // showMessage(STR_DIVER_EMPLOY_SUCCESS);
	// // teachID = TEACH_5;
	// // } else {
	// if (employShopIndex == 0) {
	// showMessage(STR_DIVER_EMPLOY_FAIL0);
	// } else {
	// showMessage(STR_DIVER_EMPLOY_FAIL1);
	// }
	// // }
	// } else if (employShopIndex <= diverID) {
	// showMessage(STR_DIVER_EMPLOY_FAIL0);
	// } else if (employShopIndex > diverID + 1) {
	// showMessage(STR_DIVER_EMPLOY_FAIL2);
	// } else if (gold < DIVER_PRICE[employShopIndex]) {
	// if (!isOpenGame) {
	// showMessage(STR_DIVER_EMPLOY_FAIL1);
	// } else {
	// gotoSmsUI(SMS_BUY_GOLD2);
	// }
	// } else {
	// gold -= DIVER_PRICE[employShopIndex];
	// diverID = (byte) employShopIndex;
	// diver = new Diver(diverID);
	// normalGameDiveID = diverID;
	// saveRecord();
	// showMessage(STR_DIVER_EMPLOY_SUCCESS);
	// }
	// } else if (employShopIndex == 3) {
	// if (!isHasControl) {
	// // ��ʾ�̴�
	// // gotoSMS((byte) 1);
	// if (stageID == 10) {
	// showMessage("����δ�������������ܹ���õ��ߣ�");
	// } else { //QQ 0214
	// if (diverID == 2) {
	// gotoSmsUI(SMS_BUY_DIVER_CONTROL);
	// } else {
	// showMessage("��������������߼�ǱˮԱ���ſɿ����ù��ܣ�");
	// }
	// }
	// } else {
	// showMessage(STR_BUY_CONTROL_FAIL);
	// }
	// }
	// }
	//
	// if (employShopIndex != bakIndex) {
	// changeEmployShopInfo();
	// }
	// }
	private void handleEmployShopKey() {
		int bakIndex = employShopIndex;
		if (Common.isKeyPressed(Common.BUTTON_BACK_PRESSED, true)) {
			state = STATE_GAME;
		} else if (isUpPressed()) {
			employShopIndex--;
			if (employShopIndex < 0) {
				employShopIndex = EMPLOY_SHOP_NUM - 1;
			}
		} else if (isDownPressed()) {
			employShopIndex++;
			if (employShopIndex >= EMPLOY_SHOP_NUM) {
				employShopIndex = 0;
			}
		} else if (isOkPressed() || Common.isKeyPressed(Common.BUTTON_OK_PRESSED, true)) {
			if (employShopIndex < 3) {
				// if (stageID == 10) {// ��ѧ�Ĺ�Ӷ
				// if (employShopIndex == 0) {
				// showMessage(STR_DIVER_EMPLOY_FAIL0);
				// } else {
				// showMessage(STR_DIVER_EMPLOY_FAIL1);
				// }
				// } else
				if (employShopIndex <= diverID) {
					showMessage(STR_DIVER_EMPLOY_FAIL0);
				} else if (employShopIndex > diverID + 1) {
					showMessage(STR_DIVER_EMPLOY_FAIL2);
				} else if (gold < DIVER_PRICE[employShopIndex]) {
					// if (!isOpenGame) {
					// showMessage(STR_DIVER_EMPLOY_FAIL1);
					// } else {
					// gotoSmsUI(SMS_BUY_GOLD);
					// }
					showMessage(STR_GOLD_LACK);
				} else {
					gold -= DIVER_PRICE[employShopIndex];
					diverID = (byte) employShopIndex;
					diver = new Diver(diverID);
					normalGameDiveID = diverID;
					saveRecord();
					showMessage(STR_DIVER_EMPLOY_SUCCESS);
					updateAchDiver();
				}
			} else if (employShopIndex == 3) {
				if (!isHasControl) {
					// ��ʾ�̴�
					// gotoSMS((byte) 1);
					if (stageID == 10) {
						showMessage("����δ�������������ܹ���õ��ߣ�");
					} else { // QQ 0214
						if (diverID == 2) {
							// gotoSmsUI(SMS_BUY_DIVER_CONTROL);
							int price = 1500;
							if (gold >= price) {
								isHasControl = true;
								gold -= price;
								saveRecord();
								showMessage(STR_BUY_SUCCESS);
							} else {
								showMessage(STR_GOLD_LACK);
							}
						} else {
							showMessage("��������������߼�ǱˮԱ���ſɿ����ù��ܣ�");
						}
					}
				} else {
					showMessage(STR_BUY_CONTROL_FAIL);
				}
			}
		}
		if (employShopIndex != bakIndex) {
			changeEmployShopInfo();
		}
	}
	/**
	 * ���ƹ�Ӷ�̵�
	 */
	public void drawEmployShop240(Graphics g) {
		drawGame(g);
		Common.setUIClip(g);
		int rw = 240;
		int rh = 320;
		int rx = (uiWidth - rw) >> 1;
		int ry = (uiHeight - rh) >> 1;
		fillBgRect(g, rx, ry, rw, rh);
		int barX = rx + 5;
		int barY = ry + 5;
		// ���ƽ��
		int goldX = barX + 8;
		int goldY = barY + ((BAR_HEIGHT - goldGetImg.getHeight()));
		Tool.drawImage(g, goldGetImg, goldX, goldY, Tool.TRANS_NONE);
		int numX = goldX + goldGetImg.getWidth() + 3;
		int numY = barY + ((BAR_HEIGHT - numFontImg1.getHeight()));
		Tool.drawImageNum(g, numFontImg1, String.valueOf(gold), numX, numY, numFontImg1.getWidth() / 10, 0);
		// �����洬
		int infoPanelH = 0;
		// int infoTextH = 50;
		// int infoSpace = 10;
		int infoTextH = 45;
		int infoSpace = 5;
		for (int i = 0; i < 4; i++) {
			if (i < 3) {
				infoPanelH += diverInfoImgs[i][0].getHeight();
				infoPanelH += infoSpace;
			} else {
				infoPanelH += toolImg[0].getHeight();
				infoPanelH += infoSpace;
			}
		}
		infoPanelH += infoTextH + infoSpace;
		// int infoPanelX = 30;
		int infoPanelX = rx + 10;
		int infoPanelY = ry + BAR_HEIGHT + ((rh - BAR_HEIGHT - infoPanelH) >> 1); // barImg.getHeight() +
																					// ((uiHeight -
																					// barImg.getHeight()
																					// -
																					// btnBgImg.getHeight()
																					// - infoPanelH) >>
																					// 1);
		int infoX = infoPanelX;
		int infoY = infoPanelY;
		// ����ѡ�е����沿��
		int i = 0;
		int posY = infoY;
		for (i = 0; i < EMPLOY_SHOP_NUM; i++) {
			if (i < 3) {
				employShopItemX[i] = infoX;
				employShopItemY[i] = posY;
				employShopItemW[i] = diverInfoImgs[i][0].getWidth();
				employShopItemH[i] = diverInfoImgs[i][0].getHeight();
			} else if (i == 3) {
				employShopItemX[i] = infoX;
				employShopItemY[i] = posY;
				employShopItemW[i] = toolImg[0].getWidth();
				employShopItemH[i] = toolImg[0].getHeight();
			}
			if (i < 3) {
				posY += (diverInfoImgs[i][0].getHeight() + infoSpace);
				if (i == employShopIndex) {
					posY += (infoTextH + infoSpace);
				}
			}
		}
		for (i = 0; i <= employShopIndex; i++) {
			// ��˸
			Image img = null;
			if (i == employShopIndex && (Tool.countTimes % 4) < 2) {
				if (i < 3) {
					img = diverInfoImgs[i][1];
				} else {
					img = toolImg[1];
				}
			} else {
				if (i < 3) {
					img = diverInfoImgs[i][0];
				} else {
					img = toolImg[0];
				}
			}
			if (i == 3) {
				Tool.drawImage(g, img, infoX, infoY, Tool.TRANS_NONE);
			} else {
				Tool.drawImage(g, img, infoX, infoY, Tool.TRANS_NONE);
			}
			// �����־
			if (i <= diverID) {
				Tool.drawImage(g, employedImg, infoX + diverInfoImgs[i][0].getWidth() - employedImg.getWidth() - 10, infoY + ((diverInfoImgs[i][0].getHeight() - employedImg.getHeight()) >> 1), Tool.TRANS_NONE);
			} else if (i >= 3 && isHasControl) {
				Tool.drawImage(g, boughtImg, infoX + diverInfoImgs[0][0].getWidth() - boughtImg.getWidth() - 10, infoY + ((diverInfoImgs[0][0].getHeight() - boughtImg.getHeight()) >> 1), Tool.TRANS_NONE);
			}
			// �ۼ�����
			if (i < 3) {
				infoY += (diverInfoImgs[i][0].getHeight() + infoSpace);
				// } else if (i == 3) {
				// infoY += (toolImg[0].getHeight() + infoSpace);
			}
		}
		if (employShopIndex >= 3 && employShopIndex <= 5) {
			infoY += (toolImg[0].getHeight() + infoSpace);
		}
		Tool.fillAlphaRect(g, 0x88000000, rx + 4, infoY, rw - 8, infoTextH);
		int infoTextX = rx + ((rw - infoTextImg.getWidth()) >> 1); // infoX + 4;
		int infoTextY = infoY + ((infoTextH - infoTextImg.getHeight()) >> 1);
		Tool.drawImage(g, infoTextImg, infoTextX, infoTextY, Tool.TRANS_NONE);
		infoY += infoTextH + infoSpace;
		// �����°벿��
		for (i = employShopIndex + 1; i < 4; i++) {
			Image img = null;
			if (i < 3) {
				img = diverInfoImgs[i][0];
			} else {
				img = toolImg[0];
			}
			Tool.drawImage(g, img, infoX, infoY, Tool.TRANS_NONE);
			if (i <= diverID) {
				Tool.drawImage(g, employedImg, infoX + diverInfoImgs[i][0].getWidth() - employedImg.getWidth() - 10, infoY + ((diverInfoImgs[i][0].getHeight() - employedImg.getHeight()) >> 1), Tool.TRANS_NONE);
			} else if (i >= 3 && isHasControl) {
				Tool.drawImage(g, boughtImg, infoX + diverInfoImgs[0][0].getWidth() - boughtImg.getWidth() - 10, infoY + ((diverInfoImgs[0][0].getHeight() - boughtImg.getHeight()) >> 1), Tool.TRANS_NONE);
			}
			// �ۼ�����
			if (i < 3) {
				infoY += (diverInfoImgs[i][0].getHeight() + infoSpace);
			} else {
				infoY += (toolImg[0].getHeight() + infoSpace);
			}
		}
		//
		Image img = null;
		if (employShopIndex < 3) {
			img = btnEmployImg;
		} else {
			img = btnBuyImg;
		}
		// drawButton(g, img, BUTTON_ALIGN_LEFT);
		// drawButtonBack(g);
		buttonLeftX = rx - buttonWidth;
		buttonLeftY = uiHeight - buttonHeight;
		buttonRightX = rx + rw;
		buttonRightY = uiHeight - buttonHeight;
		drawButtonBubbleSet(g);
		drawButton(g, img, buttonLeftX, buttonLeftY);
		drawButton(g, btnBackImg, buttonRightX, buttonRightY);
		// if (!closeEmployShopTip) {
		// int x = 20;
		// int y = 135;
		// int yy = 48;
		// if (teachID == TEACH_HANDUP) {// ������ʾ���
		// //Tool.drawImageRect(g, mRectImg, (uiWidth - MESSAGE_WIDTH) >> 1, y,
		// MESSAGE_WIDTH, 80 + 10, 0x01B7F0);
		// fillBgRect(g, (uiWidth - MESSAGE_WIDTH) >> 1, y, MESSAGE_WIDTH, 80 +
		// 10);
		// g.setClip(0, y + 25, SCREEN_WIDTH, 40);
		// g.drawImage(tipImg[3], SCREEN_WIDTH >> 1, y + 25, 17);
		// Tool.drawImage(g, mapPointerImg, x, SCREEN_HEIGHT - yy -
		// (((Tool.countTimes >> 2) % 3) << 1), 0);
		// } else if (teachID == TEACH_5 && message == null) {// ������ʾ���
		// //Tool.drawImageRect(g, mRectImg, (uiWidth - MESSAGE_WIDTH) >> 1, y,
		// MESSAGE_WIDTH, 80 + 10, 0x01B7F0);
		// fillBgRect(g, (uiWidth - MESSAGE_WIDTH) >> 1, y, MESSAGE_WIDTH, 80 +
		// 10);
		// g.setClip(0, y + 25, SCREEN_WIDTH, 40);
		// g.drawImage(tipImg[4], SCREEN_WIDTH >> 1, y + 25, 17);
		// Tool.drawImage(g, mapPointerImg, SCREEN_WIDTH - x -
		// mapPointerImg.getWidth(), SCREEN_HEIGHT - yy - (((Tool.countTimes >>
		// 2) % 3) << 1), 0);
		// }
		// }
	}
	/** ********************************** CG ******************************* */
	static int CgTextHeight;
	static final String STR_CG_1 = "\n\n\n³�˳�����Ӣ���ۿ�С���ϰ��նأ����ڼҾ�ƶ��������10���Ҫȥ��ͷ�򹤣�����С����Ľ����ҵ��ҵĸ����޽�˹��ÿ���������޽�˹ָ�������洬ͣ����ͷ�����ջ�ĺ���Ʒ���۸�Ӣ���ĸ߼����ꡣ�������������������³����־Ҫ��Ϊ���޽�˹һ�����ˣ��ܹ�ͨ���Լ���Ŭ�����ü��˹��ϸ�ԣ�����";
	static final String STR_CG_2 = "\n\n\n������³�˵ĺ��ѣ���������³����ɸ��������ڿ��׵İ����£�³��ͨ���Լ���˫�֣����ڹ����˸�ԣ���������³��û���뵽���ǣ������޽�˹���Լ��������ִ�����Ů���޸���³�ˣ�������Ů����������³�˳�Ϧ�ദ�Ŀ��ס�";
	int state_CGID;
	Image CGImg;
	void gotoCGView(int id) {
		state_CGID = id;
		String str = null;
		coverImg = null;
		// mainMenuBgImg = null;
		freeMenuRes();
		// menuBgImg = null;
		// menuImg = null;
		// itemBg2Img = null;
		// teachID = TEACH_NULL;
		System.gc();
		if (id == 0) {
			str = STR_CG_1;
			isHasControl = false;
			// closeEmployShopTip = false; //xFu 0113
			CGImg = loadResImage("/cg0.png");
		} else if (id == 1) {
			// isSharkAppear = false;
			str = STR_CG_2;
			CGImg = loadResImage("/cg1.png");
		}
		CgTextHeight = SCREEN_HEIGHT - CGImg.getHeight() - fontH - 30;
		scrollText = new ScrollText(str, CGImg.getWidth(), CgTextHeight, fontH, 1, font);
		state = STATE_CG;
	}
	void drawCGView(Graphics g) {
		Common.fillUIRect(g, 0);
		// g.drawImage(CGImg, 0, 8, 0);
		Tool.drawImage(g, CGImg, (uiWidth - CGImg.getWidth()) >> 1, 8, Tool.TRANS_NONE);
		if (scrollText != null) {
			int space = 5;
			scrollText.draw(g, (uiWidth - CGImg.getWidth()) >> 1, SCREEN_HEIGHT - CgTextHeight - (fontH + space), 0xffffff, 188, Tool.TYPE_SHADOW);
		}
		// String str = null;
		// if (state_CGID == 0) {
		// str = "����";
		// } else if (state_CGID == 1) {
		// str = "������Ϸ";
		// }
		// g.setColor(-1);
		// g.setClip(SCREEN_WIDTH >> 1, SCREEN_HEIGHT - 5 - fontH, SCREEN_WIDTH
		// >> 1, fontH);
		// g.drawString(str, SCREEN_WIDTH - 5, SCREEN_HEIGHT - 5, 40);
		drawButton(g, btnIntoImg, BUTTON_ALIGN_RIGHT);
	}
	void initGameData() {
		playerScore = 0;// ������Ϸ����
		stageOpenID = 0;
		stageID = 0;
		score = 0;
		playerIndex = 0;
		if (bBuyGift) { // QQ 0407
			boatID = 3;
			diverID = 0;
			normalGameDiveID = 0;
		} else {
			boatID = 0;
			diverID = -1; // QQ 0214
			normalGameDiveID = -1;
		}
		// 110721
		for (int i = 0; i < highScores.length; i++) {
			highScores[i] = 0;
		}
		for (int i = 0; i < medalNums.length; i++) {
			medalNums[i] = 0;
		}
		saveRecord();
	}
	private void initLoadingRes() {
		int i = Tool.getNextRnd(0, 9);
		loadingFish = Tool.createImage("/fish" + i + ".png");
		if (loadingImg == null) {
			loadingImg = Tool.createImage("/loading.png");
			loadingBackImg = Tool.createImage("/loadingBack.png");
			loadingFrontImg = Tool.createImage("/loadingFront.png");
		}
	}
	void gotoLoading(int id) {
		stageID = id;
		if (stageID == 11) { // 110722
			liveGameStageID = 1;
		}
		initLoadingRes();
		clearMoveTo();
		Bubble.setDeadY(WATER_FLOAT_Y);
		state = STATE_LOADING;
	}
	// void drawLoading(Graphics g) {
	// Common.fillUIRect(g, 0);
	// g.setColor(0xffff00);
	// int w = 100;
	// g.setClip((SCREEN_WIDTH >> 1) - w, (SCREEN_HEIGHT >> 1) + 10, (w << 1) +
	// 2, 15);
	// g.drawRect((SCREEN_WIDTH >> 1) - w, (SCREEN_HEIGHT >> 1) + 10, w << 1,
	// 6);
	// g.setColor(0xff0000);
	// g.setClip((SCREEN_WIDTH >> 1) - w + 1, (SCREEN_HEIGHT >> 1) + 11, (w <<
	// 1) - 1, 5);
	// g.fillRect((SCREEN_WIDTH >> 1) - w - 1, (SCREEN_HEIGHT >> 1) + 11,
	// loadingStep, 5);
	// Tool.drawTile(g, loadFish, (Tool.countTimes >> 2) % 5,
	// loadFish.getWidth() / 5, loadFish.getHeight(), 0, (SCREEN_WIDTH >> 1) - w
	// + loadingStep, (SCREEN_HEIGHT >> 1) + 10 - (loadFish.getHeight() / 3));
	// }
	void drawLoading(Graphics g) {
		// if (menuBgImg != null) {
		// Tool.drawImage(g, menuBgImg, 0, 0, Tool.TRANS_NONE);
		// Tool.fillAlphaRect(g, 0xAA000000, 0, 0, uiWidth, uiHeight);
		// }
		//
		// g.setColor(0xffff00);
		// int w = 100;
		// g.setClip((SCREEN_WIDTH >> 1) - w, (SCREEN_HEIGHT >> 1) + 10, (w <<
		// 1) + 2, 15);
		// g.drawRect((SCREEN_WIDTH >> 1) - w, (SCREEN_HEIGHT >> 1) + 10, w <<
		// 1, 6);
		// g.setColor(0xff0000);
		// g.setClip((SCREEN_WIDTH >> 1) - w + 1, (SCREEN_HEIGHT >> 1) + 11, (w
		// << 1) - 1, 5);
		// g.fillRect((SCREEN_WIDTH >> 1) - w - 1, (SCREEN_HEIGHT >> 1) + 11,
		// loadingStep, 5);
		// Tool.drawTile(g, loadingFish, (Tool.countTimes >> 2) % 5,
		// loadingFish.getWidth() / 5, loadingFish.getHeight(), 0, (SCREEN_WIDTH
		// >> 1) - w + loadingStep, (SCREEN_HEIGHT >> 1) + 10 -
		// (loadingFish.getHeight() / 3));
		Common.fillUIRect(g, 0);
		int lbw = loadingBackImg.getWidth();
		int lbh = loadingBackImg.getHeight();
		int lbx = (uiWidth - lbw) >> 1;
		int lby = (uiHeight - lbh) >> 1;
		Tool.drawImage(g, loadingBackImg, lbx, lby, Tool.TRANS_NONE);
		int lfw = loadingStep * lbw / 200;
		Tool.drawRegion(g, loadingFrontImg, 0, 0, lfw, lbh, Tool.TRANS_NONE, lbx, lby);
		int fw = loadingFish.getWidth() / 5;
		int fh = loadingFish.getHeight();
		Tool.drawTile(g, loadingFish, (Tool.countTimes >> 2) % 5, fw, fh, Tool.TRANS_NONE, lbx + lfw - fw / 2, lby + (lbh - fh) / 2);
		int lx = (uiWidth - loadingImg.getWidth()) >> 1;
		int ly = lby + lbh + 10;
		Tool.drawImage(g, loadingImg, lx, ly, Tool.TRANS_NONE);
	}
	public void loadBgImg(int bgID) {
		// if (SCREEN_WIDTH == 800) {
		// bgImg = Tool.createImage("/bg" + STAGE_MAP_ID[stageID] + "_800.png");
		// } else {
		// bgImg = Tool.createImage("/bg" + STAGE_MAP_ID[stageID] + ".png");
		// }
		bgImg = loadResImage("/bg" + bgID + ".png");
	}
	public int loadLevel(int step) {
		try {
			if (step == -1) {
				return 0;
			} else if (step == 0) {// �����Դ
				coverImg = null;
				// mainMenuBgImg = null;
				freeMenuRes();
				// menuBgImg = null;
				// menuImg = null;
				CGImg = null;
				// QQMenu = null;
				// itemBg2Img = null;
				nanshengImg = null;
				nvshengImg = null;
				// ��յ�ͼ��Դ
				// mapBgImg = null;
				if (mapPartImgs != null) {
					for (int i = 0; i < STAGE_NUM; i++) {
						mapPartImgs[i] = null;
					}
					mapPartImgs = null;
				}
				mapRectImg = null;
				mapFocusImg = null;
				mapLockImg = null;
				fishSet.removeAllElements();
				flyNumSet.removeAllElements();
				bubbleSet.removeAllElements();
				fishbone.removeAllElements();
				propsBubble = null;
				System.gc();
				return 10;
			} else if (step == 10) {
				int bgID = STAGE_MAP_ID[stageID];
				if (stageID == 10 && bFirstTutorOver) {
					bgID = 3;
				}
				loadBgImg(bgID);
				waterBackImg = Tool.createImage("/waterBack" + bgID + ".png");
				waterFrontImg = Tool.createImage("/waterFront" + bgID + ".png");
				return 20;
			} else if (step == 20) {// ��ʼ��������
				// fishman��ʼ��
				// whb fix ���ӽ�ѧ�ؿ�
				// if (stageID == 10) {
				// tipImg = new Image[7];
				// for (int i = 0; i < 7; i++) {
				// tipImg[i] = Tool.createImage("/tip" + (i + 1) + ".png");
				// }
				// }
				fishman = new Fishman(fishmanID);
				return 40;
			} else if (step == 40) {// ���õ����ߴ�
				if (stageID == 10) {// whb add��ѧ�Ļ� �����Ӵ�ֻ2
					if (bFirstTutorOver) {
						boatID = 3;
						gold = 400;// ֱ�Ӹ�����
					} else {
						boatID = 2;
						gold = 400;// ֱ�Ӹ�����
					}
				}
				// 0219
				// else if (stageID == 0) {
				// boatID = 0;
				// }
				/** ************* test ***************** */
				// if (stageID == 11) {// whb test
				// boatID = 3;
				// }
				/** ************* test ***************** */
				return 60;
			} else if (step == 60) {// ���õ����ߴ� //ǱˮԱ
				if (stageID == 11) {
					boatID = liveGameBoatID;
					fishman.setBoat(boatID);
					fishman.setHook(boatID);
					fishman.initPos(FISHMAN_POSX_INIT - (fishman.footWidth >> 1), FISHMAN_POSY_INIT);
					if (liveGameStageID == 1) {
						gold = liveGameGold;
						if (liveGameAllFishToScoreNum < ALL_FISH_NUM_INIT) {
							allFishToScoreNum = ALL_FISH_NUM_INIT;
						} else {
							allFishToScoreNum = liveGameAllFishToScoreNum;
						}
					} else {
						gold = liveGameGold;
						allFishToScoreNum = liveGameAllFishToScoreNum;
					}
					diverID = liveGameDiverID;
					if (diverID >= 0) { // ǱˮԱ
						diver = new Diver(diverID);
					} else {
						diver = null;
					}
				} else {
					fishman.setBoat(boatID);
					fishman.setHook(boatID);
					fishman.initPos(FISHMAN_POSX_INIT - (fishman.footWidth >> 1), FISHMAN_POSY_INIT);
					diverID = normalGameDiveID;
					if (diverID >= 0) { // ǱˮԱ
						diver = new Diver(diverID);
					} else {
						diver = null;
					}
				}
				return 80;
			} else if (step == 80) {// ����ÿ�ص���ֵ
				if (stageID == 10) {// whb add ��ѧ�Ļ� �����������
					fishman.boatFullFishNum = 20;
					// fishman.boatFullFishNum = 1;// ///////////////
				}
				for (int i = 0; i < FISH_IMAGE_MAX; i++) {
					fishImgs[i] = null;
				}
				for (int i = 0; i < STAGE_FISH_TYPE[stageID].length; i++) {
					int fid = STAGE_FISH_TYPE[stageID][i];
					if (fishImgs[fid] == null) {
						fishImgs[fid] = Tool.createImage("/fish" + fid + ".png");
					}
				}
				// whb add initialize driver
				// if (stageID != 10) {// ��ѧ�ص�ʱ�򣬲�����ǱˮԱ
				// diver = new Diver(diverID);
				// if (isHasControl) {
				// diver.actState = Diver.ACT_STATE_CATCH;
				// }
				// }
				// whb initialize shark image
				sharkImg = Tool.createImage("/shark.png");
				addSharkTimes = 0;
				return 100;
			} else if (step == 100) {// ������Դ
				loadGameRes();
				magnetImg = Tool.createImage("/magnet.png");
				// magnetTipImg = Tool.createImage("/magnetTip.png");
				return 120;
			} else if (step == 120) {
				for (int i = 0; i < FORE_OBJECT_NUM; i++) {
					if (foreObjImg[i] == null) {
						continue;
					}
					getNextBuubleTime(i);
				}
				return 150;
			} else if (step == 150) {// ��ʼ���ؿ�����
				viewMapX = 0;
				viewMapY = VIEW_MAP_INIT_Y;
				gameTimesLeft = gameTimesTotal = STAGE_TIME[stageID] * 1000 / MILLIS_PRE_UPDATE;
				// goal = STAGE_GOAL[stageID];
				goal = STAGE_GOAL[stageID][0];
				// if (stageID < STAGE_NUM) {
				highScore = highScores[stageID];
				// } else {
				// highScore = 0;
				// }
				/** **************** ��ʼ������ ***************** */
				score = 0;
				addFishTimes = 0;
				addItemTimes = 0;
				bShowLightning = false;
				isSharkCannotCatch = false;
				toolNum = 0;// ������߷���
				continueHangUpScore = 0;
				/** ******************************************** */
				// ���ˮ�׽��
				// if (stageID != 10) {// ��ѧ��û�н��
				// Fish fish = new Fish(Fish.FISH_TYPE_GOLDCOIN, MAP_WIDTH >> 1,
				// MAP_HEIGHT - goldCoinImg.getHeight(), MapElement.DIR_RIGHT,
				// goldCoinImg);
				// fishSet.addElement(fish);
				// }
				// whb add ���ݽ��
				ADD_ITEM_INTERVAL = gameTimesTotal / (STAGE_PROP_TYPE[stageID].length * 4);
				curPropNum = 0;
				if (stageID == 10) { // ��ѧ��
					initTutor();
				} else {
					bTutorStage = false;
				}
				if (stageID == 11) {// ����ģʽ��ÿ�����ӵķ���
					goal += (liveGameStageID - 1) * 50;
					stageIDImg = Tool.createImage("/stage.png");
				}
				beginImg = Tool.createImage("/begin.png");
				return 180;
			} else if (step == 180) {
				closeMusic();
				return 199;
			} else if (step == 199) {
				Thread.sleep(100);
				startMusic("/m1.mid");
				initStageFishset();
				// 110818
				numbAttackTime = 0;
				magnetUseTime = 0;
				state = STATE_GAME_BEGIN;
				//
				// allFishToScoreNum = 1;
				// isHasControl = false;
				// diver = null;
				// diverID = -1;
				// normalGameDiveID = -1;
				// liveGameDiverID = -1;
				// bBuyGift = false;
			} else {
				return step;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	public void initStageFishset() {
		int fishNum = 15;
		int gridNum = 10;
		for (int i = 0; i < fishNum; i++) {
			int typeIndex = Tool.getNextRnd(0, STAGE_FISH_TYPE[stageID].length - 1);
			byte typeID = STAGE_FISH_TYPE[stageID][typeIndex];
			// int mx = Tool.getNextRnd(0, gridNum) * MAP_WIDTH / gridNum;
			// int my = Tool.getNextRnd(FISH_Y_MIN, FISH_Y_MAX);
			// byte dir = MapElement.DIR_RIGHT;
			// if (Tool.getNextRndBoolean()) {
			// dir = MapElement.DIR_LEFT;
			// }
			int mx;
			int my = Tool.getNextRnd(FISH_Y_MIN, FISH_Y_MAX);
			byte dir;
			if (i < fishNum / 2) {
				mx = (Tool.getNextRnd(0, gridNum / 2) * MAP_WIDTH / gridNum) + Tool.getNextRnd(-10, 10);
				dir = MapElement.DIR_RIGHT;
			} else {
				mx = (Tool.getNextRnd(gridNum / 2, gridNum) * MAP_WIDTH / gridNum) + Tool.getNextRnd(-10, 10);
				dir = MapElement.DIR_LEFT;
			}
			addFish(typeID, mx, my, dir);
		}
	}
	int returnState;
	/**
	 * @param state
	 *            1:shop 2:mainmenu
	 */
	void gotoReturn(int state) {
		initLoadingRes();
		returnState = state;
		this.state = STATE_RETURN;
	}
	private int loadReturn(int step) {
		if (step == -1) {
			return 0;
		} else if (step == 20) {// �����Դ
			closeMusic();
		} else if (step == 40) {// �����Դ
			freeGameRes();
		} else if (step == 100) {
			System.gc();
		} else if (step == 200) {
			if (returnState == 1) {
				// gotoShop();
				gotoMap(); // 110718
				if (stageID == 9) {
					if (!bGameFail && !isLiveGameOpened) {
						isLiveGameOpened = true;
						liveGameGold = gold;
						liveGameAllFishToScoreNum = allFishToScoreNum;
						saveRecord();
						showMessage("��ϲͨ�أ�\n����ģʽ�ѿ������������˵����룡\n��ս999������ģʽ��������������");
						updateAchievement(ACH_STAGE_ALL_PASS);
					}
				}
			} else if (returnState == 2) {
				gotoCover();
				gotoMainMenu();
				// startMusic("/m0.mid");
			}
			try {
				Thread.sleep(1L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return 0;
		}
		return step + 20;
	}
	/** ***************************** �������ֵ ******************************* */
	// 1��ʱ�������ڣ�������Ļ����ۼӡ�����ķ�ֵ�ۼ�
	// 2����;ʹ�õ��ߣ����������ߣ�ÿʹ��һ�μ�50
	// 3��һ�͵���2�������3������߸��࣬���ּ���N��2
	int playerScore;// ���������ϴ�ʹ��
	int toolNum;// ʹ�õ��ߺ����ӵķ���
	int continueHangUpScore;// ����������Ĵ���
	static final String STR_STAGE_PLAYERSCORE = "��Ϸ���֣�";
	static final String STR_ADD_GOLD = "����50�����";
	void calculateScore() {
		// playerScore += score;
		// playerScore += toolNum * 50;
		// playerScore += continueHangUpScore;
		// playerScore += 50;
		int n = 5;
		playerScore += (score / n);
		playerScore += (continueHangUpScore / n);
		// playerScore += 10; //QQ 0407
		if (playerScore < 10) {
			playerScore = 10;
		}
	}
	/** ***************************** ��Ļ������ ****************************** */
	boolean isAllFishToScore;
	int allFishToScoreNum;
	int normalGameAllFishToScoreNum;
	int liveGameAllFishToScoreNum;
	public static final int ALL_FISH_NUM_INIT = 3; // QQ 0314
	int normalGameGold;
	int liveGameGold;
	void allFishToScore() {
		for (int i = 0; i < fishSet.size(); i++) {
			Fish fish = (Fish) fishSet.elementAt(i);
			if (fish.isFish()) {
				// �������Ļ�ڲſ��Ա�
				if ((fish.curDir == MapElement.DIR_RIGHT && fish.mapX - viewMapX > -fish.bodyWidth && fish.mapX - viewMapX < 240) || (fish.curDir == MapElement.DIR_LEFT && fish.mapX - viewMapX > -fish.bodyWidth && fish.mapX - viewMapX < 240)) {
					FlyNum flyNum = new FlyNum(FlyNum.NUM_TYPE_SCORE, fish.score, numFontImg1, fish.mapX - viewMapX, fish.mapY - viewMapY, SCORE_NUM_FLY_X, SCORE_NUM_FLY_Y);
					flyNumSet.addElement(flyNum);
				}
			}
			fishSet.removeElementAt(i);
			i--;
			// continue;
		}
	}
	// void gotoGameOver() {
	// if (isLiveGameOpened) {
	// isShowLiveGame = false;
	// } else {
	// isShowLiveGame = true;
	// }
	// state = STATE_GAME_OVER;
	// }
	/********************************* QQ SMS *********************************/
	/** ������Ϸ */
	public static final int SMS_BUY_GAME = 1;
	/** ������� */
	public static final int SMS_BUY_MAGNET = 2;
	/** ����ǱˮԱ���� */
	public static final int SMS_BUY_DIVER_CONTROL = 3;
	/** ������ */
	public static final int SMS_BUY_GOLD = 4;
	/** ����ʱ�� */
	public static final int SMS_BUY_TIME = 5;
	/** ����ֵ��� */
	public static final int SMS_BUY_GIFT = 6;
	int smsType;
	int doIndex;
	// ������� 1671587 2 10658077100416 YX,205036,1,2bc7,30501
	// ���⼼�� 1671589 2 10658077100416 YX,205036,2,e0fa,30501
	// �ؿ� 1671591 4 10658077100416 YX,205036,3,cf28,30501
	// ��Ǯ 1671593 2 10658077100416 YX,205036,4,e323,30501
	byte preSmsState;
	ScrollText smsST;
	String smsContent;
	// PreSMSReturn preSmsRetrun;
	boolean bSmsWaitBack;
	ScrollText smsWaitST;
	// void gotoSmsUI(int type) {
	// smsType = type;
	// switch (smsType) {
	// case SMS_BUY_GAME :
	// smsContent = "��10���泡����̽�ա���999�����漫�޵�����ս����������Ů��ڡ�������3��ħ�ô�������ֵ���龫�����д�����";
	// doIndex = 3;
	// break;
	//
	// case SMS_BUY_MAGNET :
	// smsContent = "���10��ħ�ô�����˲���ջ���Ļ�������㣬����Ҳ���Ź���";
	// doIndex = 1;
	// break;
	//
	// case SMS_BUY_DIVER_CONTROL :
	// smsContent = "����ǱˮԱ���ƹ��ܣ��ɹ����������¿�ʼ��Ϸ�˹��ܲ���Ҫ�󸶷ѡ�";
	// doIndex = 2;
	// break;
	//
	// case SMS_BUY_GOLD :
	// smsContent = "���Ľ�Ҳ��㣬��ȡ1000��ң�����ͧ����ӶǱˮԱ������������Ȥ��";
	// doIndex = 4;
	// break;
	//
	// case SMS_BUY_TIME :
	// smsContent = "��û����ɱ���Ŀ�꣬�ӳ���Ϸʱ�䣬��ʱ���㣬���ֱ�����������ս������������Ȥ��";
	// doIndex = 2;
	// break;
	//
	// case SMS_BUY_GIFT : //0219
	// smsContent = "��ֵ�����ֱ���������������㡢�߼��㾡���������ǱˮԱ�����ɹ����������¿�ʼ��Ϸ�˹��ܲ���Ҫ�󸶷ѡ�";
	// doIndex = 1;
	// break;
	//
	// default :
	// break;
	// }
	// //preSmsRetrun = WebNetInterface.PreSMSBillingPoint(doIndex);
	//
	// if (smsType == SMS_BUY_GAME) {
	// smsContent = smsContent + "\n����Ϣ��4Ԫ\nֻ�跢��1������\n4Ԫ/��������ͨ�ŷѣ�\n�Ƿ��ͣ�";
	// } else {
	// smsContent = smsContent + "\n����Ϣ��2Ԫ\nֻ�跢��1������\n2Ԫ/��������ͨ�ŷѣ�\n�Ƿ��ͣ�";
	// }
	// smsUpdateInfo();
	// preSmsState = state;
	// state = STATE_SMS_UI;
	//
	// // if (preSmsRetrun.m_bSuccess) {
	// // smsContent = smsContent + "\n" + preSmsRetrun.m_contents;
	// // smsUpdateInfo();
	// //
	// // bSmsWaitBack = false;
	// // preSmsState = state;
	// // state = STATE_SMS_UI;
	// // } else {
	// // showMessage("��ȡ�Ʒ���Ϣʧ�ܣ��뷵�أ�");
	// //
	// // //xFu 20110914
	// // if (smsType == SMS_BUY_TIME) {
	// // gotoSummary();
	// // }
	// // }
	//
	// //TODO test
	// // if (!preSmsRetrun.m_bSuccess) {
	// // message = null;
	// //
	// // smsContent = smsContent + preSmsRetrun.m_contents;
	// // smsUpdateInfo();
	// // preSmsState = state;
	// // state = STATE_SMS_UI;
	// // }
	// }
	void smsUpdateInfo() {
		String str = null;
		str = new String(smsContent);
		smsST = new ScrollText(str, MESSAGE_WIDTH, uiHeight - buttonHeight - 50, font.getHeight(), 2, font);
	}
	void handleSMSKey() {
		if (bSmsWaitBack) {
			Common.clearKeyStates();
			Common.clearPointerEvent();
			return;
		}
		if (Common.isKeyPressed(Common.SOFT_LAST_PRESSED, true)) { // ȡ��
			state = preSmsState;
			// xFu 0113
			if (smsType == SMS_BUY_TIME) {
				gotoSummary();
			}
		} else if (Common.isKeyPressed(Common.SOFT_FIRST_PRESSED, true)) { // ����
			//
			bSmsWaitBack = true;
			if (smsWaitST == null) {
				int step = font.getHeight() / 10;
				if (step == 0) {
					step = 1;
				}
				smsWaitST = new ScrollText("���ŷ����У����Ժ򡭡�", MESSAGE_WIDTH, MESSAGE_HEIGHT, fontH, step, font);
			}
			// WebNetInterface.SMSBillingPoint(doIndex, String.valueOf(smsType)
			// + System.currentTimeMillis());
			// SendSMSCB(SendSMS_Event_OK, "Test SmsType��" + smsType + "��");
			// SmsProcessor.sendSMS("10658008105882", "020"); //10658008105882
			// SmsProcessor.sendSMS("13810004269", "020");
			// TODO delete
			smsCallBack(SMS_RESULT_SUCCESS, "");
		}
	}
	/**
	 * �ɹ��󣬶����ݵĴ���
	 */
	void processBuySuccess() {
		switch (smsType) {
			case SMS_BUY_GAME :
				isFirstGame = false; // xFu 0113
				isOpenGame = true;
				boatID = 0; // QQ 0407
				diverID = -1; // QQ 0214
				normalGameDiveID = -1;
				liveGameDiverID = -1;
				// isOpenLiveGame = true;
				gotoPlayerChoose(true); // xFu 0113
				break;
			case SMS_BUY_MAGNET :
				bBuyMagnet = true;
				allFishToScoreNum += 10; // 0219
				// 110818
				updateAchievement(ACH_BUY_MAGNET);
				state = STATE_GAME; // 110721
				break;
			case SMS_BUY_DIVER_CONTROL : // QQ 0214
				isHasControl = true;
				// diverID = 2;
				// diver = new Diver(diverID);
				// normalGameDiveID = diverID;
				// 110818
				updateAchievement(ACH_BUY_DIVER_CONTROL);
				state = STATE_GAME;
				break;
			case SMS_BUY_GOLD :
				gold += 1000;
				// 110817
				updateGold += 1000;
				updateScoreGameGold();
				// 110818
				updateAchievement(ACH_BUY_GOLD);
				break;
			case SMS_BUY_TIME :
				gameTimesLeft = gameTimesTotal;
				state = STATE_GAME;
				break;
			case SMS_BUY_GIFT : // 0219
				bBuyGift = true;
				boatID = 3;
				fishman.setBoat(boatID);
				fishman.setHook(boatID);
				fishman.initAct();
				if (diverID < 0) {
					diverID = 0;
					diver = new Diver(diverID);
					normalGameDiveID = diverID;
				}
				// 110818 gift
				updateAchBoat();
				updateAchDiver();
				state = STATE_GAME; // 110721
				break;
		}
		saveRecord();
	}
	public void smsCallBack(int result, String message) {
		System.out.println("SMS Send: " + result + " | " + message);
		boolean bSuccess = false;
		if (result == SMS_RESULT_SUCCESS) {
			bSuccess = true;
		}
		if (bSuccess) {
			state = preSmsState;
			processBuySuccess();
			showMessage("����ɹ���");
		} else {
			showMessage("���ŷ���ʧ�ܣ�");
			// xFu 0113
			if (smsType == SMS_BUY_TIME) {
				gotoSummary();
			}
		}
		bSmsWaitBack = false;
		Common.clearKeyStates();
		Common.clearPointerEvent();
	}
	public boolean SendSMSCB(int event, String mark) {
		System.out.println("SendSMSCB: " + event + " " + mark);
		boolean bSuccess = false;
		// if (event == SendSMS_Event_OK) {
		// bSuccess = true;
		// }
		if (bSuccess) {
			state = preSmsState;
			processBuySuccess();
			showMessage("����ɹ���");
		} else {
			showMessage("���ŷ���ʧ�ܣ�");
			// xFu 0113
			if (smsType == SMS_BUY_TIME) {
				gotoSummary();
			}
		}
		bSmsWaitBack = false;
		Common.clearKeyStates();
		Common.clearPointerEvent();
		return true;
	}
	public void drawSmsUI(Graphics g) {
		if (menuBgImg != null) {
			Tool.drawImage(g, menuBgImg, 0, 0, Tool.TRANS_NONE);
			Tool.fillAlphaRect(g, 0xAA000000, 0, 0, uiWidth, uiHeight);
		}
		smsST.draw(g, (uiWidth - smsST.getWidth()) >> 1, (uiHeight - smsST.getHeight()) >> 1, 0xffffff, 0, Tool.TYPE_SHADOW);
		smsST.cycle();
		if (bSmsWaitBack) {
			drawMessage(g, smsWaitST, false);
		}
		drawButtonOk(g);
		drawButtonBack(g);
		// String str = "����";
		// Tool.drawString(g, str, 10, uiHeight - fontH - 5, 0xffff00, 0);
		// str = "����";
		// Tool.drawString(g, str, uiWidth - font.stringWidth(str) - 10,
		// uiHeight - fontH - 5, 0xffff00, 0);
	}
	// void setArrayAsNull(Object ob[]) {
	// if (ob != null) {
	// for (int i = 0; i < ob.length; i++) {
	// ob[i] = null;
	// }
	// }
	// }
	byte returnEmployShopState;
	boolean keyLock;
	// void gotoReturnEmployShopForSmall(byte state) {
	// keyLock = true;
	// int i = Tool.getNextRnd(0, 9);
	// loadingFish = Tool.createImage("/fish" + i + ".png");
	// returnEmployShopState = state;
	// this.state = STATE_RETURN_EMPLOY;
	// }
	// int loadReturnForEmployShop(int step) {
	// if (step == -1) {
	// return 0;
	// } else if (step == 20) {// �����Դ
	//
	// } else if (step == 40) {// �����Դ
	// if (returnEmployShopState == 0) {//�����̵�
	// sprayAS = null;
	//
	// stars0Img = null;
	// stars1Img = null;
	// bubbleImg = null;
	//
	// // sysMenuImg = null;
	// //goldBarImg = null;
	// //scoreImg = null;
	// //goalImg = null;
	// clockImg = null;
	//
	// banksideImg = null;
	// bucketImg = null;
	//
	// handupImg = null;
	// fishHandupImg = null;
	//
	// if (propsImgs != null) {
	// for (int i = 0; i < PROPS_TYPE_NUM; i++) {
	// propsImgs[i] = null;
	// }
	// propsImgs = null;
	// }
	//
	// propsFadeImg = null;
	//
	// fullFlagImg = null;
	// if (fullHintImgs != null) {
	// for (int i = 0; i < fullHintImgs.length; i++) {
	// fullHintImgs[i] = null;
	// }
	// fullHintImgs = null;
	// }
	//
	// foreObjImg[0] = null;
	// foreObjImg[1] = null;
	//
	// careImg = null;
	// goldCoinImg = null;
	//
	// numbFishImg1 = null;
	// numbFishImg2 = null;
	// bgImg = null;
	// waterBackImg = null;
	// waterFrontImg = null;
	//
	// for (int i = 0; i < FISH_IMAGE_MAX; i++) {
	// fishImgs[i] = null;
	// }
	// fishImgs = new Image[FISH_IMAGE_MAX];
	// sharkImg = null;
	//
	// for (int i = 0; i < fishSet.size(); i++) { //QQ 0413
	// ((Fish) fishSet.elementAt(i)).fishImg = null;
	// }
	//
	// bLoadGameRes = false;
	// } else {
	// shopImg = null;
	// goldGetImg = null;
	// boughtImg = null;
	// toolImg = null;
	// infoTextImg = null;
	// }
	// System.gc();
	// } else if (step == 100) {
	//
	// if (returnEmployShopState == 0) {//�����̵�
	//
	// } else {
	// loadBgImg();
	// waterBackImg = Tool.createImage("/waterBack" + STAGE_MAP_ID[stageID] +
	// ".png");
	// waterFrontImg = Tool.createImage("/waterFront" + STAGE_MAP_ID[stageID] +
	// ".png");
	//
	// // if (stageID == 10) {
	// // tipImg = new Image[3];
	// // tipImg[0] = Tool.createImage("/tip1.png");
	// // tipImg[1] = Tool.createImage("/tip2.png");
	// // tipImg[2] = Tool.createImage("/tip7.png");
	// // }
	// for (int i = 0; i < STAGE_FISH_TYPE[stageID].length; i++) {
	// int fid = STAGE_FISH_TYPE[stageID][i];
	// if (fishImgs[fid] == null) {
	// fishImgs[fid] = Tool.createImage("/fish" + fid + ".png");
	// }
	// }
	// sharkImg = Tool.createImage("/shark.png");
	// loadGameRes();
	//
	// for (int i = 0; i < fishSet.size(); i++) { //QQ 0413
	// Fish fish = (Fish) fishSet.elementAt(i);
	// if (fish.fishType == SHARK_TYPE) {
	// fish.fishImg = sharkImg;
	// } else {
	// fish.fishImg = fishImgs[fish.fishType];
	// }
	// }
	// }
	// } else if (step == 120) {
	// if (returnEmployShopState == 0) {//�����̵�
	//
	// } else {
	// if (bgImg == null) {
	// loadBgImg();
	// return 120;
	// }
	// }
	// } else if (step == 140) {
	// if (returnEmployShopState == 0) {//�����̵�
	// gotoEmployShop();
	// } else {
	// state = STATE_GAME;
	// }
	// keyLock = false;
	// return 0;
	// }
	// try {
	// Thread.sleep(1L);
	// } catch (InterruptedException e) {
	// e.printStackTrace();
	// }
	//
	// return step + 20;
	// }
	public boolean bSensorAble = false;
	// �������ϵ��������ٶ�
	public int sensorX, sensorY, sensorZ;
	// //������
	// private static SensorConnection sensor = null;
	// private static SensorInfo infos[];
	// //�Ƿ�����ȷ�Ĵ�����
	private static boolean bSensorFound = true;
	// //�������Ļ�������С
	// private static final int BUFFER_SIZE = 3;
	//
	// public void dataReceived(SensorConnection sensor, Data[] data, boolean
	// isDataLost) {
	// if (!bSensorAble) {
	// return;
	// }
	// int[] directions = getIntegerDirections(data);
	// sensorX = directions[0];
	// sensorY = directions[1];
	// sensorZ = directions[2];
	// System.out.println("X:" + sensorX + " Y:" + sensorY + " Z:" + sensorZ);
	// }
	//
	// /**
	// * ��ø������ϵ��������ٶ�
	// *
	// * @param data
	// * @return
	// */
	// private static int[] getIntegerDirections(Data[] data) {
	// //int[][] intValues = new int[3][BUFFER_SIZE];
	// int[] intValues = new int[BUFFER_SIZE];
	// int[] directions = new int[3];
	// if (data != null) {
	// for (int i = 0; i < 3; i++) {
	// if (data[i] == null) {
	// continue;
	// }
	// intValues = data[i].getIntValues();
	// int temp = 0;
	// for (int j = 0; j < BUFFER_SIZE; j++) {
	// temp = temp + intValues[j];
	// }
	// directions[i] = temp / BUFFER_SIZE;
	// }
	// }
	//
	// return directions;
	// }
	//
	// /**
	// * �������ĳ�ʼ��
	// */
	// private void initSensor() {
	// // if (!bSensorAble) {
	// // return;
	// // }
	// sensor = openSensor();
	// if (sensor != null) {
	// System.out.println("-------------------sensor found");
	// bSensorFound = true;
	// } else {
	// bSensorFound = false;
	// System.out.println("-------------------sensor not found");
	// }
	// sensor.setDataListener(this, BUFFER_SIZE);
	// }
	//
	// /**
	// * �򿪴������������Ҫ�Ĵ�����
	// *
	// * @return INT �������͵Ĵ�����
	// */
	// private SensorConnection openSensor() {
	// infos = SensorManager.findSensors("acceleration", null);
	// if (infos.length == 0) {
	// return null;
	// }
	// int datatypes; //[] = new int[infos.length];
	// String sensor_url = "";
	// try {
	// for (int i = 0; i < infos.length; i++) {
	// ChannelInfo[] channelInfo = infos[i].getChannelInfos();
	// datatypes = channelInfo[0].getDataType();
	// if (datatypes == ChannelInfo.TYPE_INT) {
	// sensor_url = infos[i].getUrl();
	// //sensor_found = true;
	// return (SensorConnection) Connector.open(sensor_url);
	// //break;
	// }
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return null;
	//
	// // while (!sensor_found) {
	// // ChannelInfo[] channelInfo = infos[i].getChannelInfos();
	// // datatypes = channelInfo[0].getDataType();
	// // if (datatypes == ChannelInfo.TYPE_INT) {
	// // sensor_url = infos[i].getUrl();
	// // sensor_found = true;
	// // } else {
	// // i++;
	// // }
	// // }
	//
	// // try {
	// // return (SensorConnection) Connector.open(sensor_url);
	// // } catch (IOException ioe) {
	// // ioe.printStackTrace();
	// // return null;
	// // }
	// }
	/************************* ���ȹ�� ******************************/
	public static final String PUBLISHER_ID = "56OJzuw4uNd/3t8hh+";
	public static DomobInterstitialAd mInterstitialAd;
	int loadNUM;
	public void domobAd() {
		// ���ò������λ�ļ�����
		// �״����������档
		// �������������ʱ����ʱ����չ�ֹ�档ֻ�е������ߵ���showInterstitialAd�ӿ�ʱ�Ż�չʾ��档
		// ��չ�ֹ��ʱ�������жϹ���Ƿ�����ɹ���
		if (mInterstitialAd.isInterstitialAdReady()) {
			// �������ɹ�����չ�ֹ�档
			mInterstitialAd.showInterstitialAd(DoMidlet.instance);
		} else {
			Log.i("DomobSDKDemo", "û�гɹ���");
			// ��ʼ��һ�������������
			mInterstitialAd.loadInterstitialAd();
		}
	}
	/************************* ���׻���ǽ ******************************/
	EarnedPointsOrder pointsOrder = new EarnedPointsOrder();
	private static final String KEY_FILE_POINTS = "Points";
	private static final String KEY_POINTS = "points";
	private static final String KEY_FILE_ORDERS = "Orders";
	@Override
	public void onEarnedPoints(Context context, List pointsList) {
		// TODO Auto-generated method stub
		Log.i("YouMiSDKDemo", "pointsOrder.getPoints()-----" + pointsOrder.getPoints() + "gold------------" + gold);
		if (pointsList != null) {
			// saveRecord();
			for (int i = 0; i < pointsList.size(); i++) {
				// �����ִ洢���Զ�������˻���
				storePoints(context, (EarnedPointsOrder) pointsList.get(i));
				// (��ѡ)�����洢���ֻ�ȡ��¼
				recordOrder(context, (EarnedPointsOrder) pointsList.get(i));
			}
		} else {
			infoMsg("onPullPoints:pointsList is null");
		}
		// if (pointsList != null) {
		// for (int i = 0; i < pointsList.size(); i++) {
		// // �����ִ洢���Զ�������˻���
		// storePoints(context, (EarnedPointsOrder) pointsList.get(i));
		// // (��ѡ)�����洢���ֻ�ȡ��¼
		// recordOrder(context, (EarnedPointsOrder) pointsList.get(i));
		// }
		// // Log.i("YouMiSDKDemo",
		// // "pointsOrder.getStatus()-----" + pointsOrder.getStatus());
		// // if (pointsOrder.getStatus() == 1) {
		// // gold += pointsOrder.getPoints();
		// // Log.i("YouMiSDKDemo", "gold__________________" + gold
		// // + pointsOrder.getPoints());
		// // // saveRecord();
		// // }
		// } else {
		// Log.i("YouMiSDKDemo", "onPullPoints:pointsList is null");
		// }
	}
	private void recordOrder(Context context, EarnedPointsOrder order) {
		try {
			if (order != null) {
				// ���Դ�����Щ������ϸ��Ϣ������ֻ����Ϊ�򵥵ļ�¼.
				StringBuilder stringBuilder = new StringBuilder(256);
				gold += order.getPoints();
				saveRecord();
				Log.i("YouMiSDKDemo", "order.getPoints()" + order.getPoints() + "gold------------" + gold);
				Toast.makeText(context, "��ý��" + order.getPoints(), Toast.LENGTH_SHORT).show();
				stringBuilder.append("[").append("������ => ").append(order.getOrderId()).append("]\t[").append("������ => ").append(order.getChannelId()).append("]\t[").append("���õ��û�Id(md5) => ").append(order.getUserId()).append("]\t[").append("��õĻ��� => ").append(order.getPoints()).append("]\t[").append("��û��ֵ�����(1Ϊ������Ļ��֣�2Ϊ������Ļ���) => ").append(order.getStatus()).append("]\t[").append("���ֵĽ���ʱ��(��������ʱ�䣬��λ��) => ").append(order.getTime()).append("]\t[").append("���λ�û��ֵ�������Ϣ => ").append(order.getMessage()).append("]");
				String msg = stringBuilder.toString();
				// SharedPreferences sp = context.getSharedPreferences(
				// KEY_FILE_ORDERS, Context.MODE_PRIVATE);
				// Editor editor = sp.edit();
				// editor.putString(
				// order.getOrderId() != null ? order.getOrderId() : Long
				// .toString(System.currentTimeMillis()), msg);
				// editor.commit();
				infoMsg(msg);
			}
		} catch (Exception e) {
		}
	}
	private void infoMsg(String msg) {
		Log.i("MyPointsManager", msg);
	}
	/**
	 * �洢����
	 * 
	 * @param context
	 * @param order
	 */
	private void storePoints(Context context, EarnedPointsOrder order) {
		try {
			if (order != null) {
				if (order.getPoints() > 0) {
					// �����ּ�������˻��У������������˻��Ǵ洢�ڱ���
					SharedPreferences sp = context.getSharedPreferences(KEY_FILE_POINTS, Context.MODE_PRIVATE);
					int p = sp.getInt(KEY_POINTS, 0);
					p += order.getPoints();
					sp.edit().putInt(KEY_POINTS, p).commit();
				}
			}
		} catch (Exception e) {
		}
	}
}
