package javax.microedition.midlet;
import java.io.InputStream;
import java.util.Vector;
import javax.microedition.config.DebugConfig;
import javax.microedition.lcdui.Displayable;
import net.youmi.android.appoffers.YoumiOffersManager;
import cn.domob.android.ads.DomobActivity;
import cn.domob.android.ads.DomobInterstitialAd;
import android.R;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.Button;
public abstract class MIDlet extends ActivityMIDletBridge implements
		SensorEventListener {
	public final static String TAG = "MIDlet";
	// public static MIDlet s_instance;
	private static KeyguardManager s_keyguardManager;
	private boolean bSensorEnable = true;
	private SensorManager mSensorManager;
	private Sensor mAccelerometer;
	public static float SensorX, SensorY, SensorZ;
	// public MIDlet()
	// {
	// super();
	// s_instance = this;
	// }
	// public static MIDlet getInstance()
	// {
	// return s_instance;
	// }
	protected abstract void startApp() throws MIDletStateChangeException;
	protected abstract void pauseApp();
	protected abstract void destroyApp(boolean unconditional)
			throws MIDletStateChangeException;
	public final void notifyDestroyed() {
		if (!this.isFinishing()) {
			try {
				// this.destroyApp(true);
				this.finish();
			} catch (Exception e) {
				if (DebugConfig.DEBUG_ANDROID_MIDLET
						&& DebugConfig.DEBUG_ANDROID_EXCEPTION) {
					android.util.Log.e(TAG, DebugConfig.TAG_EXCEPTION
							+ "destroy application failed!", e);
				}
			}
		}
		android.os.Process.killProcess(android.os.Process.myPid());
		System.exit(0);
	}
	public final void notifyPaused() {
	}
	public final String getAppProperty(String key) {
		try {
			PackageManager pm = this.getPackageManager();
			// get property from Activity.
			ActivityInfo info1 = pm.getActivityInfo(this.getComponentName(),
					PackageManager.GET_META_DATA);
			if (info1 != null) {
				Object o = info1.metaData.get(key);
				if (o != null) {
					return o.toString();
				}
			}
			// get property from Application.
			ApplicationInfo info2 = pm.getApplicationInfo(
					this.getPackageName(), PackageManager.GET_META_DATA);
			if (info2 != null) {
				Object o = info1.metaData.get(key);
				if (o != null) {
					return o.toString();
				}
			}
		} catch (Exception e) {
			if (DebugConfig.DEBUG_ANDROID_MIDLET
					&& DebugConfig.DEBUG_ANDROID_EXCEPTION) {
				android.util.Log.e(TAG, DebugConfig.TAG_EXCEPTION
						+ "get property \"" + key + "\" failed!", e);
			}
		}
		return null;
	}
	private static boolean s_paused;
	private static boolean s_initialized;
	protected void onCreate(Bundle savedInstanceState) {
		if (DebugConfig.DEBUG_ANDROID_MIDLET
				&& DebugConfig.DEBUG_ANDROID_CALLBACK) {
			Log.v(TAG, DebugConfig.TAG_CALLBACK
					+ "onCreate: savedInstanceState=" + savedInstanceState);
		}
		super.onCreate(savedInstanceState);
		s_instance = this;
		if (s_keyguardManager == null) {
			s_keyguardManager = (KeyguardManager) this
					.getSystemService(Context.KEYGUARD_SERVICE);
		}
		// this.m_bundle = savedInstanceState;
		try {
			this.startApp();
			s_initialized = true;
		} catch (Exception e) {
			if (DebugConfig.DEBUG_ANDROID_MIDLET
					&& DebugConfig.DEBUG_ANDROID_EXCEPTION) {
				Log.e(TAG, DebugConfig.TAG_EXCEPTION
						+ "exception on MIDlet.onCreate", e);
			}
		}
		deliverEvent(EVENT_CREATE);
		if (bSensorEnable) {
			mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
			mAccelerometer = mSensorManager
					.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		}
	}
	// protected void onRestart() {
	// if (DebugConfig.DEBUG_ANDROID_MIDLET &&
	// DebugConfig.DEBUG_ANDROID_CALLBACK) {
	// Log.v(TAG, DebugConfig.TAG_CALLBACK + "onRestart");
	// }
	//
	// super.onRestart();
	// if (s_paused) {
	// try {
	// this.startApp();
	// } catch (Exception e) {
	// if (DebugConfig.DEBUG_ANDROID_EXCEPTION) {
	// Log.e(TAG, DebugConfig.TAG_EXCEPTION + "exception on MIDlet.onRestart",
	// e);
	// }
	//
	// }
	// s_paused = false;
	// }
	// deliverEvent(EVENT_RESTART);
	// }
	// protected void onStart() {
	// if (DebugConfig.DEBUG_ANDROID_MIDLET &&
	// DebugConfig.DEBUG_ANDROID_CALLBACK) {
	// Log.v(TAG, DebugConfig.TAG_CALLBACK + "onStart");
	// }
	// super.onStart();
	// if (s_paused) {
	// try {
	// this.startApp();
	// } catch (Exception e) {
	// if (DebugConfig.DEBUG_ANDROID_EXCEPTION) {
	// Log.e(TAG, DebugConfig.TAG_EXCEPTION + "exception on MIDlet.onStart", e);
	// }
	// }
	// s_paused = false;
	// }
	// deliverEvent(EVENT_START);
	// }
	private static boolean s_doResumeRequest;
	protected void onResume() {
		if (DebugConfig.DEBUG_ANDROID_MIDLET
				&& DebugConfig.DEBUG_ANDROID_CALLBACK) {
			Log.v(TAG, DebugConfig.TAG_CALLBACK + "onResume");
		}
		super.onResume();
		if (bSensorEnable) {
			mSensorManager.registerListener(this, mAccelerometer,
					SensorManager.SENSOR_DELAY_GAME);
		}
		// check if keyguard is presented,if so,block the application.
		if (s_keyguardManager != null) {
			while (s_keyguardManager.inKeyguardRestrictedInputMode()) {
				Thread.yield();
			}
		}
		// if (!this.isFinishing()) {
		// if (s_initialized) {
		// if (s_focusedDisplayable != null) {
		// Log.v(TAG, "reset display");
		// javax.microedition.lcdui.Display.getDisplay(this).setCurrent(s_focusedDisplayable);
		// Log.v(TAG, "s_focusedDisplayable: " + s_focusedDisplayable);
		// Log.v(TAG, "displayable: " + this.getCurrentFocus());
		// }
		// }
		//
		// if (s_paused) {
		try {
			this.startApp();
		} catch (Exception e) {
			if (DebugConfig.DEBUG_ANDROID_EXCEPTION) {
				Log.e(TAG, DebugConfig.TAG_EXCEPTION
						+ "exception on MIDlet.onResume", e);
			}
		}
		// s_paused = false;
		// }
		// deliverEvent(EVENT_RESUME);
		// }
	}
	protected void onPause() {
		if (DebugConfig.DEBUG_ANDROID_MIDLET
				&& DebugConfig.DEBUG_ANDROID_CALLBACK) {
			Log.v(TAG, DebugConfig.TAG_CALLBACK + "onPause");
		}
		super.onPause();
		if (bSensorEnable) {
			mSensorManager.unregisterListener(this);
		}
		s_paused = true;
		if (!this.isFinishing()) {
			try {
				this.pauseApp();
			} catch (Exception e) {
				if (DebugConfig.DEBUG_ANDROID_MIDLET
						&& DebugConfig.DEBUG_ANDROID_EXCEPTION) {
					Log.e(TAG, DebugConfig.TAG_EXCEPTION
							+ "exception on MIDlet.onPause", e);
				}
			}
			deliverEvent(EVENT_PAUSE);
		}
	}
	// protected void onStop() {
	// if (DebugConfig.DEBUG_ANDROID_MIDLET &&
	// DebugConfig.DEBUG_ANDROID_CALLBACK) {
	// Log.v(TAG, DebugConfig.TAG_CALLBACK + "onStop");
	// }
	// super.onStop();
	// s_paused = true;
	// if (!this.isFinishing()) {
	// try {
	// this.pauseApp();
	// } catch (Exception e) {
	//
	// }
	// }
	// deliverEvent(EVENT_STOP);
	// }
	// protected void onDestroy() {
	// if (DebugConfig.DEBUG_ANDROID_MIDLET &&
	// DebugConfig.DEBUG_ANDROID_CALLBACK) {
	// Log.v(TAG, DebugConfig.TAG_CALLBACK + "onDestroy");
	// }
	// super.onDestroy();
	// if (s_focusedDisplayable != null) {
	// ((ViewGroup)
	// s_focusedDisplayable.getParent()).removeView(s_focusedDisplayable);
	// }
	//
	// try {
	//
	// } catch (Exception e) {
	// if (DebugConfig.DEBUG_ANDROID_MIDLET &&
	// DebugConfig.DEBUG_ANDROID_EXCEPTION) {
	// Log.e(TAG, DebugConfig.TAG_EXCEPTION + "exception on MIDlet.onDestroy",
	// e);
	// }
	// }
	// //deliverEvent(EVENT_DESTROY);
	// }
	public static final int EVENT_CREATE = 0;
	public static final int EVENT_RESTART = 1;
	public static final int EVENT_START = 2;
	public static final int EVENT_PAUSE = 3;
	public static final int EVENT_RESUME = 4;
	public static final int EVENT_STOP = 5;
	public static final int EVENT_DESTROY = 6;
	private static Vector s_eventHandlers = new Vector();
	private static void deliverEvent(int event) {
		for (int i = 0; i < s_eventHandlers.size(); i++) {
			((MIDletEventHandler) s_eventHandlers.elementAt(i))
					.handleMIDletEvent(event);
		}
	}
	public void addEventHandler(MIDletEventHandler handler) {
		if (!s_eventHandlers.contains(handler)) {
			s_eventHandlers.add(handler);
		}
	}
	public void removeEventHandler(MIDletEventHandler handler) {
		if (s_eventHandlers.contains(handler)) {
			s_eventHandlers.remove(handler);
		}
	}
	public void removeAllHandler() {
		s_eventHandlers.removeAllElements();
	}
	public InputStream openInputStreamFromAsset(String name) {
		try {
			return this.getResources().getAssets().open(name);
		} catch (Exception e) {
			if (DebugConfig.DEBUG_ANDROID_MIDLET
					&& DebugConfig.DEBUG_ANDROID_EXCEPTION) {
				Log.e(TAG, DebugConfig.TAG_EXCEPTION
						+ "get input stream from \"" + name + "\" failed!", e);
			}
			return null;
		}
	}
	private static Displayable s_focusedDisplayable;
	public Displayable getFocusedDisplayable() {
		return s_focusedDisplayable;
	}
	public void setFoucsedDisplayable(Displayable d) {
		s_focusedDisplayable = d;
	}
	/*
	 * public boolean onKeyDown (int keyCode, KeyEvent event) {
	 * if(s_focusedDisplayable != null) {
	 * s_focusedDisplayable.onKeyDown(keyCode, event); } return
	 * super.onKeyDown(keyCode, event); }
	 * 
	 * public boolean onKeyUp (int keyCode, KeyEvent event) {
	 * if(s_focusedDisplayable != null) { s_focusedDisplayable.onKeyUp(keyCode,
	 * event); } return super.onKeyUp(keyCode, event); }
	 */
	protected void onSaveInstanceState(Bundle outState) {
		if (DebugConfig.DEBUG_ANDROID_MIDLET
				&& DebugConfig.DEBUG_ANDROID_CALLBACK) {
			Log.v(TAG, DebugConfig.TAG_CALLBACK
					+ "onSaveInstanceState: outState=" + outState);
		}
		super.onSaveInstanceState(outState);
	}
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		if (DebugConfig.DEBUG_ANDROID_MIDLET
				&& DebugConfig.DEBUG_ANDROID_CALLBACK) {
			Log.v(TAG, DebugConfig.TAG_CALLBACK
					+ "onRestoreInstanceState: savedInstanceState="
					+ savedInstanceState);
		}
		super.onRestoreInstanceState(savedInstanceState);
	}
	// public boolean dispatchTouchEvent(MotionEvent ev) {
	// if (DebugConfig.DEBUG_ANDROID_MIDLET &&
	// DebugConfig.DEBUG_ANDROID_CALLBACK) {
	// Log.v(TAG, DebugConfig.TAG_CALLBACK + "dispatchTouchEvent: action=" +
	// ev.getAction() + "\tx=" + ev.getX() + "\ty=" + ev.getY());
	// }
	// super.dispatchTouchEvent(ev);
	// if (s_focusedDisplayable != null) {
	// s_focusedDisplayable.dispatchTouchEvent(ev);
	// }
	// return true;
	// }
	// public boolean dispatchKeyEvent(KeyEvent event) {
	// if (DebugConfig.DEBUG_ANDROID_MIDLET &&
	// DebugConfig.DEBUG_ANDROID_CALLBACK) {
	// Log.v(TAG, DebugConfig.TAG_CALLBACK + "dispatchKeyEvent: action=" +
	// event.getAction() + "\tkeycode=" + event.getAction());
	// }
	// super.dispatchKeyEvent(event);
	// if (s_focusedDisplayable != null) {
	// s_focusedDisplayable.dispatchKeyEvent(event);
	// }
	// return true;
	// }
	public void onConfigurationChanged(
			android.content.res.Configuration newConfig) {
		if (s_focusedDisplayable != null) {
			// s_focusedDisplayable.on(event);
		}
	}
	/*
	 * private static Vector s_keyguardListeners = new Vector(); public void
	 * addKeyguardListener(KeyguardListener listener) { if(listener != null &&
	 * !s_keyguardListeners.contains(listener)) {
	 * s_keyguardListeners.addElement(listener); } }
	 */
	/*
	 * public void removeKeyguardListner(KeyguardListener listener) {
	 * if(listener != null && s_keyguardListeners.contains(listener)) {
	 * s_keyguardListeners.removeElement(listener); } }
	 */
	/*
	 * public void onKeyguardStateChanged(int newState) { if(s_doResumeRequest)
	 * { if(newState == KeyguardListener.STATE_UNLOCKED) { //this.doResume();
	 * s_doResumeRequest = false; } } }
	 */
	private final static int PERMISSION_UNKNOWN = -1;
	private final static int PERMISSION_DENIED = 0;
	private final static int PERMISSION_ALLOWED = 1;
	public final int checkPermission(String permission) {
		return PERMISSION_ALLOWED;
	}
	public final void resumeRequest() {
		this.onResume();
	}
	public final boolean platformRequest(String URL)/*
													 * throws
													 * ConnectionNotFoundException
													 */
	{
		Uri uri = Uri.parse(URL);
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		startActivity(intent);
		return true;
	}
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}
	public void onSensorChanged(SensorEvent event) {
		if (event != null) {
			SensorX = event.values[SensorManager.DATA_X];
			SensorY = event.values[SensorManager.DATA_Y];
			SensorZ = event.values[SensorManager.DATA_Z];
			// System.out.println("Sensor: " + SensorX + " " + SensorY + " " +
			// SensorZ);
		}
	}
}
