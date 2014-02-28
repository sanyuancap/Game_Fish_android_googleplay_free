package javax.microedition.lcdui;

import android.app.*;
import android.view.*;
import android.graphics.*;
import android.content.*;
import android.util.*;
import javax.microedition.midlet.*;
import javax.microedition.config.*;
public abstract class Displayable extends View implements MIDletEventHandler {
	public final static String TAG = "Displayable";
	protected Context m_context;

	public Displayable(Context context) {
		super(context);
		setKeepScreenOn(true);
	}

	public Displayable() {
		this(MIDlet.getInstance());
		setKeepScreenOn(true);
	}

	public void onSizeChanged(int w, int h, int oldw, int oldh) {
		if (DebugConfig.DEBUG_ANDROID_CALLBACK) {
			Log.v(TAG, DebugConfig.TAG_CALLBACK + "onSizeChanged: w=" + w + "\th=" + h + "\toldw=" + oldw + "\toldh=" + oldh);
		}
		super.onSizeChanged(w, h, oldw, oldh);
		this.sizeChanged(w, h);
	}

	protected void sizeChanged(int w, int h) {

	}

	public boolean isShown() {
		return this.isFocused();
	}
}
