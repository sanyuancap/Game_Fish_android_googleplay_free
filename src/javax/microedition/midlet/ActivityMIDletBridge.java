package javax.microedition.midlet;

import android.app.*;
import android.content.*;
import android.os.*;
import android.content.res.*;
import android.util.*;
import android.view.Window;
import android.view.WindowManager;

import java.util.*;
import java.io.*;

import android.view.*;

public class ActivityMIDletBridge extends Activity {
	public static ActivityMIDletBridge s_instance;

	public static String rootPath;

	//	public ActivityMIDletBridge() {
	//		super();
	//		s_instance = this;
	//	}

	public static ActivityMIDletBridge getInstance() {
		/*
		 * if(s_instance == null) { s_instance = new ActivityMIDletBridge(); }
		 */
		return s_instance;
	}

	private static Resources s_resourcesManager;

	public static Resources getResourceManager() {
		if (s_resourcesManager == null) {
			s_resourcesManager = getInstance().getResources();
		}
		return s_resourcesManager;
	}

	private static boolean s_paused;

	protected void onCreate(Bundle savedInstanceState) {
		initPath();
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,

		WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		super.onCreate(savedInstanceState);
		s_instance = this;
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		if (s_resourcesManager == null) {
			s_resourcesManager = this.getResources();
		}

	}

	void initPath() {
		rootPath = this.getFilesDir().getAbsolutePath();
	}

//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//
//		// 按下键盘上返回按钮
//		if (keyCode == KeyEvent.KEYCODE_BACK) {
//
//			new AlertDialog.Builder(this)
//			.setTitle("游戏提示")
//			.setMessage("确定退出游戏?")
//
//			.setNegativeButton("返回", new DialogInterface.OnClickListener() {
//				public void onClick(DialogInterface dialog, int which) {
//
//				}
//			})
//
//			.setPositiveButton("退出", new DialogInterface.OnClickListener() {
//				public void onClick(DialogInterface dialog, int whichButton) {
//					finish();
//					//android.os.Process.killProcess(android.os.Process.myPid());
//				}
//
//			}).show();
//
//			return true;
//		} else {
//			return super.onKeyDown(keyCode, event);
//		}
//	}

	//	public Resources getResources()
	//    {
	//		System.out.println("3333333333333333333333333");
	//		return null;
	//        //return super.getResources();
	//    }

	/*
	 * protected void onRestart() { super.onRestart();
	 * 
	 * }
	 * 
	 * protected void onStart() { super.onStart();
	 * 
	 * }
	 * 
	 * protected void onResume() { super.onResume();
	 * 
	 * }
	 * 
	 * protected void onPause() { super.onPause(); s_paused = true;
	 * 
	 * }
	 * 
	 * 
	 * protected void onStop() { super.onStop();
	 * 
	 * }
	 * 
	 * 
	 * protected void onDestroy() { super.onDestroy();
	 * 
	 * }
	 */
}
