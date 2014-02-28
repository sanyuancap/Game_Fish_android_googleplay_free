package javax.microedition.lcdui;
import javax.microedition.midlet.*;
import java.util.*;
import android.util.*;
import android.view.*;
public class Display {
	public final static String TAG = "Display";
	private static Hashtable s_displays;

	private MIDlet m_midlet;
	public static Display getDisplay(MIDlet m) {
		if (s_displays == null) {
			s_displays = new Hashtable();
		}
		String key = "" + m.hashCode();
		if (s_displays.containsKey(key)) {
			return (Display) s_displays.get(key);
		} else {
			Display display = new Display();
			display.m_midlet = m;
			s_displays.put(key, display);
			return display;
		}
	}

	public void setCurrent(Displayable d) {
		if (d != null) {
			if (d.getParent() != null) {
				((ViewGroup) d.getParent()).removeView(d);
			}

//			if (d.getParent() == null) {
//				m_midlet.addEventHandler(d);
				m_midlet.setContentView(d);
//				m_midlet.setFoucsedDisplayable(d);
//				d.setVisibility(View.VISIBLE);
//				d.requestFocus();
//			}
		}
	}
}
