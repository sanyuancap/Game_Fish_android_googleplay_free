package javax.microedition.media;

import android.media.*;
import android.net.*;
import android.content.*;
import java.util.*;
import java.io.*;
import javax.microedition.midlet.*;
import javax.microedition.media.control.*;
import javax.microedition.config.*;
import android.util.*;
class SoundPoolImpl implements Player, VolumeControl {

	private final static int MAX_STREAMS = 55;

	private final static int DEFAULT_SRC_QUALITY = 100;

	private static SoundPool m_player;

	private Vector m_listener;

	private int m_state;

	private int m_soundID = -1;

	public SoundPoolImpl() {

	}

	private void init() {
		if (m_player == null) {
			m_player = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, DEFAULT_SRC_QUALITY);
		}
		this.m_state = Player.UNREALIZED;
	}

	private int m_resId = -1;
	public SoundPoolImpl(int resId) {
		init();
		try {
			this.m_resId = resId;
			//this.m_soundID = m_player.load(MIDlet.getInstance(), resId, 1);
			android.util.Log.v("SoundPoolImpl", "m_soundID = " + this.m_soundID);
		} catch (Exception e) {
			android.util.Log.e("SoundPoolImpl", "load player failed!", e);
		}

	}

	private String m_locator;
	public SoundPoolImpl(String locator) {
		init();
		this.m_locator = locator;
		//this.m_soundID = m_player.load(locator, 1);
	}

	public void addPlayerListener(PlayerListener playerListener) {
		if (m_listener == null) {
			m_listener = new Vector();
		}
		if (!m_listener.contains(playerListener)) {
			m_listener.addElement(playerListener);
		}
	}

	public void close() {
		if (m_player != null) {
			m_player.unload(this.m_soundID);
		}
	}

	public void deallocate() {
		//do nothing.
		if (this.m_state == Player.PREFETCHED) {
			this.m_state = Player.REALIZED;
		} else if (this.m_state == Player.REALIZED) {
			this.m_state = Player.UNREALIZED;
		} else {
			this.m_state = Player.PREFETCHED;
		}
	}

	public String getContentType() {
		return null;
	}

	public long getDuration() {

		return 0;
	}

	public long getMediaTime() {

		return 0;
	}

	public int getState() {
		return m_state;
	}

	public void prefetch() throws MediaException {
		if (this.m_soundID == -1) {
			if (this.m_resId != -1) {
				m_soundID = this.m_player.load(MIDlet.getInstance(), this.m_resId, 1);
			} else if (this.m_locator != null) {
				m_soundID = this.m_player.load(this.m_locator, 1);
			}
		}
		m_state = this.PREFETCHED;
	}

	public void realize() throws MediaException {
		//do nothing.
		m_state = this.REALIZED;
	}

	public void removePlayerListener(PlayerListener playerListener) {
		if (this.m_listener != null && this.m_listener.contains(playerListener)) {
			m_listener.remove(playerListener);
		}
	}

	private int m_loopCount = 0;
	public void setLoopCount(int count) {
		m_loopCount = count;
	}

	public long setMediaTime(long now) throws MediaException {

		return 0;
	}

	public void start() throws MediaException {
		if (this.m_player != null) {
			//AudioManager mgr = (AudioManager) MIDlet.getInstance().getSystemService(Context.AUDIO_SERVICE); 
			// int streamVolume = mgr.getStreamVolume(AudioManager.STREAM_MUSIC); 
			// android.util.Log.v("SoundPoolImpl","streamVolume = " + streamVolume);
			if (this.m_state != PREFETCHED) {
				this.prefetch();
			}
			this.m_player.play(this.m_soundID, this.m_volumeLevel / 100f, this.m_volumeLevel / 100f/*
																									 * this
																									 * .
																									 * m_volumeLevel
																									 * /
																									 * 100f
																									 * ,
																									 * m_volumeLevel
																									 * /
																									 * 100f
																									 */, 1, this.m_loopCount, 1f);
		}
	}

	public void stop() throws MediaException {
		if (this.m_player != null) {
			this.m_player.pause(this.m_soundID);
		}
	}

	public Control[] getControls() {
		return null;
	}

	private final static String CONTROLTYPE_VOLUMECONTROL = "VolumeControl";
	public Control getControl(String controlType) {
		if (CONTROLTYPE_VOLUMECONTROL.equals(controlType)) {
			return this;
		}
		return null;
	}

	private void triggerListener(String event, Object data) {
		if (this.m_listener != null && this.m_listener.size() > 0) {
			for (int i = 0; i < this.m_listener.size(); i++) {
				((PlayerListener) m_listener.elementAt(i)).playerUpdate(this, event, data);
			}
		}
	}

	///////////////////////////////////////////////////////////////////////////////
	//////////////////////---- Volume Control ----/////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////
	private final static int MAX_VOLUME = 100;
	private final static int MIN_VOLUME = 0;
	private final static int DEFALT_VOLUME = MAX_VOLUME;
	private int m_volumeLevel = DEFALT_VOLUME;
	private boolean m_isMute;
	public int getLevel() {
		return m_volumeLevel;
	}

	public boolean isMuted() {
		return m_isMute;
	}

	private int m_prevVolumeLevel;
	public int setLevel(int level) {
		if (this.m_volumeLevel != level) {
			if (level > MAX_VOLUME) {
				level = MAX_VOLUME;
			}
			if (level < MIN_VOLUME) {
				level = MIN_VOLUME;
			}
			m_prevVolumeLevel = m_volumeLevel;
			m_volumeLevel = level;
			if (m_volumeLevel == 0) {
				this.m_isMute = true;
			}
			if (this.m_player != null) {
				this.m_player.setVolume(this.m_soundID, m_volumeLevel / 100f, m_volumeLevel / 100f);
			}
		}
		return m_volumeLevel;
	}

	public void setMute(boolean mute) {
		if (this.m_isMute != mute) {
			m_isMute = mute;
			if (this.m_player != null) {
				if (m_isMute) {
					this.setLevel(0);
				} else {
					this.setLevel(m_prevVolumeLevel);
				}
			}
		}
	}
}
