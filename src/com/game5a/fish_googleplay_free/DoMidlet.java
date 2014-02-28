package com.game5a.fish_googleplay_free;
import javax.microedition.lcdui.Display;
import javax.microedition.midlet.ActivityMIDletBridge;
import javax.microedition.midlet.MIDlet;
import net.youmi.android.appoffers.YoumiOffersManager;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.KeyEvent;
import cn.domob.android.ads.DomobInterstitialAd;
import cn.domob.android.ads.DomobInterstitialAdListener;
import cn.domob.android.ads.DomobAdManager.ErrorCode;
import com.game5a.common.Common;
import com.game5a.sms.SmsProcessor;
import com.game5a.sms.SmsReceiver;
import com.umeng.analytics.MobclickAgent;
//import com.tencent.qqgame.ui.util.GameToHallBroadcast;
//import com.tencent.webnet.WebNetInterface;
public class DoMidlet extends MIDlet {
	public static DoMidlet instance;
	public static boolean bRunning;
	// public static Display display;
	public static FishGame canvas;
	// public static DataInit gamecenterdata;
	// public static Display display;
	public DoMidlet() {
		instance = this;
	}
	public static String getProperty(String key) {
		return instance.getAppProperty(key);
	}
	protected void startApp() {
		// if (!bRunning) {
		// bRunning = true;
		// //Display display = Display.getDisplay(this);
		// canvas = new FishGame(); // display
		// Display.getDisplay(this).setCurrent(canvas);
		// canvas.start();
		// }
		if (!bRunning) {
			// System.out.println("----------startApp");
			bRunning = true;
			canvas = new FishGame();
			canvas.start();
			// //System.out.println("----------startApp DataInit");
			// gamecenterdata = new DataInit();
			// gamecenterdata.m_CPID = 131; //由腾讯分配的 cp 代号
			// gamecenterdata.m_GameID = 201; //游戏的代号
			// gamecenterdata.m_GameKey =
			// "1900429a-f697-4bb3-aa55-67138d83fc3f"; //游戏密钥，每款游戏对应唯一的密钥，由腾讯分配
			// //System.out.println("----------startApp WebNetInterface Init");
			// WebNetInterface.Init(this, canvas, gamecenterdata);
			// //System.out.println("----------startApp WebNetInterface Init End");
			// SmsProcessor.Init(this, canvas);
			// SmsReceiver.Init(this);
			// WebNetInterface.Init(this, canvas);
			// Intent i = new
			// Intent(GameToHallBroadcast.GAME_NOTIFICATION_ACTION);
			// i.putExtra(GameToHallBroadcast.KEY_ID,
			// GameToHallBroadcast.BROADCAST_ID_START_SUCCESS);
			// i.putExtra(GameToHallBroadcast.KEY_GAME_ID, "10027");
			// instance.sendBroadcast(i);
			// 有米积分墙初始化
			// System.out.println("初始化1111111---------------------------");
			YoumiOffersManager.init(this, "5a422930ae73c382",
					"6f3e3550d243876e");
			// System.out.println("初始化222222---------------------------");
		}
		Display.getDisplay(this).setCurrent(canvas);
	}
	protected void pauseApp() {
		MobclickAgent.onPause(this);
		canvas.hideNotify();
	}
	protected void destroyApp(boolean parm1) {
		// System.out.println("--------------------destoryApp");
		// WebNetInterface.Destroy();
		bRunning = false;
		canvas = null;
		// gamecenterdata = null;
	}
	public void exit() {
		try {
			instance.destroyApp(true);
			instance.notifyDestroyed();
			instance = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void gotoURL(String url) {
		try {
			// instance.pauseApp();
			instance.platformRequest(url);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	protected void onResume() {
		MobclickAgent.onResume(this);
		super.onResume();
		// WebNetInterface.SetCurActivity(this);
	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 返回按钮
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			new AlertDialog.Builder(this)
					.setTitle("游戏提示")
					.setMessage("确定退出游戏?")
					.setNegativeButton("返回",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
								}
							})
					.setPositiveButton("退出",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									bRunning = false;
								}
							}).show();
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}
}
