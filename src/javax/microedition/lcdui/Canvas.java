package javax.microedition.lcdui;

import android.app.*;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.*;
import android.graphics.*;
import javax.microedition.midlet.*;
import android.widget.*;
import javax.microedition.config.*;
public abstract class Canvas extends Displayable/* implements View.OnKeyListener */
{
	public static final String TAG = "Canvas";

	public static final int DOWN = 6;
	public static final int FIRE = 8;
	public static final int GAME_A = 9;
	public static final int GAME_B = 10;
	public static final int GAME_C = 11;
	public static final int GAME_D = 12;
	public static final int KEY_NUM0 = 48;
	public static final int KEY_NUM1 = 49;
	public static final int KEY_NUM2 = 50;
	public static final int KEY_NUM3 = 51;
	public static final int KEY_NUM4 = 52;
	public static final int KEY_NUM5 = 53;
	public static final int KEY_NUM6 = 54;
	public static final int KEY_NUM7 = 55;
	public static final int KEY_NUM8 = 56;
	public static final int KEY_NUM9 = 57;
	public static final int KEY_POUND = 35;
	public static final int KEY_STAR = 42;
	public static final int LEFT = 2;
	public static final int RIGHT = 5;
	public static final int UP = 1;

	private RefreshHandler m_refreshhandler;
	
//	public static final int SCALE_SRC_X = 640;
//	public static final int SCALE_SRC_Y = 360;
//	public static final int SCALE_DEST_X = 800;
//	public static final int SCALE_DEST_Y = 480;
	
	
	public Canvas() {
		this.m_g = new Graphics();
		m_refreshhandler = new RefreshHandler(this);
	}

	protected abstract void paint(Graphics g);

	private Graphics m_g;

	protected void onDraw(android.graphics.Canvas canvas) {
		super.onDraw(canvas);
		m_g.adCanvas = canvas;
//		canvas.scale((float)SCALE_DEST_X / SCALE_SRC_X, (float)SCALE_DEST_Y /SCALE_SRC_Y);
		if (!this.m_paused) {
			this.paint(m_g);
		}
	}

	public final void repaint() {
		this.m_refreshhandler.refresh();
	}

	public final void repaint(int x, int y, int width, int height) {

	}

	private int m_drawableAreaX;

	private int m_drawableAreaY;

	private int m_drawableAreaW;

	private int m_drawableAreaH;

	private void setDrawableArea(int x, int y, int w, int h) {
		this.m_drawableAreaX = x;
		this.m_drawableAreaY = y;
		this.m_drawableAreaW = w;
		this.m_drawableAreaH = h;
	}

	public boolean isShown() {
		return !m_paused;
	}

	public void setFullScreenMode(boolean fullScreen) {
		//do noting.
	}

	protected void hideNotify() {

	}

	protected void showNotify() {

	}

	private boolean m_paused;
	public void handleMIDletEvent(int event) {
		switch (event) {
			case MIDlet.EVENT_CREATE :
				//this.showNotify();
				break;
			case MIDlet.EVENT_RESTART :
				/*
				 * if(m_paused) { this.showNotify(); m_paused = false; }
				 */
				break;
			case MIDlet.EVENT_START :
				/*
				 * if(m_paused) { this.showNotify(); m_paused = false; }
				 */
				break;
			case MIDlet.EVENT_RESUME :
				if (m_paused) {
					this.showNotify();
					m_paused = false;
				}
				break;
			case MIDlet.EVENT_PAUSE :
				if (!m_paused) {
					this.hideNotify();
					m_paused = true;
				}
				break;
			case MIDlet.EVENT_STOP :
				if (!m_paused) {
					this.hideNotify();
					m_paused = true;
				}
				break;
		}
	}

//	public boolean dispatchKeyEvent(KeyEvent event) {
//		if (DebugConfig.DEBUG_ANDROID_CALLBACK) {
//			Log.v(TAG, DebugConfig.TAG_CALLBACK + "dispatchKeyEvent: action=" + event.getAction() + "\tkeycode=" + event.getKeyCode());
//		}
//		switch (event.getAction()) {
//			case KeyEvent.ACTION_DOWN :
//				this.keyPressed(event.getKeyCode());
//				break;
//			case KeyEvent.ACTION_UP :
//				this.keyReleased(event.getKeyCode());
//				break;
//		}
//		return true;
//	}

	protected void keyPressed(int keyCode) {

	}

	protected void keyReleased(int keyCode) {

	}

	/*
	 * public boolean onTouchEvent (MotionEvent event) {
	 * //android.util.Log.v("Canvas","onTouchEvent(" + event.getAction() + ")");
	 * 
	 * }
	 */

//	public boolean dispatchTouchEvent(MotionEvent event) {
//		if (DebugConfig.DEBUG_ANDROID_CALLBACK) {
//			Log.v(TAG, DebugConfig.TAG_CALLBACK + "dispatchTouchEvent: action=" + event.getAction() + "\tx=" + event.getX() + "\ty=" + event.getY());
//		}
//
//		int x = (int) event.getX();
//		int y = (int) event.getY();
//		switch (event.getAction()) {
//			case MotionEvent.ACTION_DOWN :
//				this.pointerPressed(x, y);
//				break;
//			case MotionEvent.ACTION_UP :
//				this.pointerReleased(x, y);
//				break;
//			case MotionEvent.ACTION_MOVE :
//				this.pointerDragged(x, y);
//				break;
//		}
//		return true;
//	}
	
	public boolean onTouchEvent (MotionEvent event) {
		int x = (int) event.getX();
		int y = (int) event.getY();
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN :
				this.pointerPressed(x, y);
				break;
			case MotionEvent.ACTION_UP :
				this.pointerReleased(x, y);
				break;
			case MotionEvent.ACTION_MOVE :
				this.pointerDragged(x, y);
				break;
		}
		return true;
	}

	protected void pointerPressed(int x, int y) {

	}

	protected void pointerReleased(int x, int y) {

	}

	protected void pointerDragged(int x, int y) {

	}

	public static String s_debugInfo = "";
	public boolean onTrackballEvent(MotionEvent event) {
		if (DebugConfig.DEBUG_ANDROID_CALLBACK) {
			Log.v(TAG, DebugConfig.TAG_CALLBACK + "onTrackballEvent: action=" + event.getAction());
		}
		return super.onTrackballEvent(event);
	}

	/**
	 * Refresh helper,used to refresh canvas periodically.
	 */
	class RefreshHandler extends Handler {
		private Canvas m_canvas;
		public RefreshHandler(Canvas c) {
			m_canvas = c;
		}

		public void handleMessage(Message msg) {
			//Log.v("Canvas","handled message: " + msg.what);
			m_canvas.invalidate();
		}

		public void refresh() {
			this.removeMessages(0);
			this.sendMessage(obtainMessage(0));
		}
	};

	public int getGameAction(int keyCode) {
		switch (keyCode) {
			case KeyEvent.KEYCODE_DPAD_UP :
			case KeyEvent.KEYCODE_2 :
			case KeyEvent.KEYCODE_W :
				return Canvas.UP;

			case KeyEvent.KEYCODE_DPAD_DOWN :
			case KeyEvent.KEYCODE_8 :
			case KeyEvent.KEYCODE_S :
				return Canvas.DOWN;

			case KeyEvent.KEYCODE_DPAD_LEFT :
			case KeyEvent.KEYCODE_4 :
			case KeyEvent.KEYCODE_A :
				return Canvas.LEFT;

			case KeyEvent.KEYCODE_DPAD_RIGHT :
			case KeyEvent.KEYCODE_6 :
			case KeyEvent.KEYCODE_D :
				return Canvas.RIGHT;

			case KeyEvent.KEYCODE_DPAD_CENTER :
			case KeyEvent.KEYCODE_5 :
			case KeyEvent.KEYCODE_ENTER :
				return Canvas.FIRE;
		}

		return 0;
	}

	public int getKeyCode(int gameAction) {
		return -1;
	}

	public void serviceRepaints() {
		//not implemented yet.
	}
}
