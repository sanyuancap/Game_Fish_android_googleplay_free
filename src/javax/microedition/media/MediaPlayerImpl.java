package javax.microedition.media;

import java.io.File;
import java.util.Vector;

import javax.microedition.config.DebugConfig;
import javax.microedition.media.control.VolumeControl;
import javax.microedition.midlet.MIDlet;

import android.R;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
class MediaPlayerImpl implements Player, MediaPlayer.OnCompletionListener, VolumeControl {
	private final static String TAG = "MediaPlayerImpl";

	private MediaPlayer m_player;

	private Vector m_listener;

	private int m_state;

	public MediaPlayerImpl() {

	}

	private void init() {
		this.m_state = Player.UNREALIZED;
		if (m_player != null) {
			m_player.setOnCompletionListener(this);
		}
	}

	private int m_resId = -1;
	public MediaPlayerImpl(int resId) {
		m_resId = resId;
	}

	private String m_locator;
	public MediaPlayerImpl(String locator) {
		this.m_locator = locator;
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
		if (DebugConfig.DEBUG_ANDROID_MEDIA) {
			Log.v(TAG, DebugConfig.TAG_MEDIA + "close");
		}
		if (m_player != null) {
			m_player.stop();
			m_player.release();
		}
		this.m_state = Player.CLOSED;
		this.triggerListener(PlayerListener.CLOSED, null);
	}

	public void deallocate() {
		if (DebugConfig.DEBUG_ANDROID_MEDIA) {
			Log.v(TAG, DebugConfig.TAG_MEDIA + "deallocate");
		}

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
		if (m_player != null) {
			m_player.getDuration();
		}
		return 0;
	}

	public long getMediaTime() {
		if (m_player != null) {
			return m_player.getCurrentPosition();
		}
		return 0;
	}

	public int getState() {
		return m_state;
	}

	public void prefetch() throws MediaException {
		if (DebugConfig.DEBUG_ANDROID_MEDIA) {
			Log.v(TAG, DebugConfig.TAG_MEDIA + "prefetch");
		}

		if (this.m_state != REALIZED) {
			this.realize();
		}
		m_state = this.PREFETCHED;
	}

	public void realize() throws MediaException {
		if (DebugConfig.DEBUG_ANDROID_MEDIA) {
			Log.v(TAG, DebugConfig.TAG_MEDIA + "realize");
		}

		if (this.m_resId != -1) {
			try {
				this.m_player = MediaPlayer.create(MIDlet.getInstance(), m_resId);
				this.m_player.setLooping(true);
			} catch (Exception e) {
				if (DebugConfig.DEBUG_ANDROID_MEDIA && DebugConfig.DEBUG_ANDROID_EXCEPTION) {
					android.util.Log.e(TAG, "create player failed!", e);
				}
			}
		} else if (this.m_locator != null) {
			try {
				String s = Uri.fromFile(new File(m_locator)).toString();
				this.m_player = MediaPlayer.create(MIDlet.getInstance(), Uri.fromFile(new File(m_locator)));
				this.m_player.setLooping(true);
			} catch (Exception e) {
				if (DebugConfig.DEBUG_ANDROID_MEDIA && DebugConfig.DEBUG_ANDROID_EXCEPTION) {
					android.util.Log.e(TAG, "create player failed!", e);
				}
			}
		}
		this.init();
		m_state = this.REALIZED;
	}

	public void removePlayerListener(PlayerListener playerListener) {
		if (this.m_listener != null && this.m_listener.contains(playerListener)) {
			m_listener.remove(playerListener);
		}
	}

	private int m_loopCount = 1;
	public void setLoopCount(int count) {
		m_loopCount = count;
	}

	public long setMediaTime(long now) throws MediaException {
		if (this.m_player != null) {
			this.m_player.seekTo((int) now);
		}
		return 0;
	}

	public void start() throws MediaException {
		if (DebugConfig.DEBUG_ANDROID_MEDIA) {
			Log.v(TAG, DebugConfig.TAG_MEDIA + "start");
		}

		//now,we always release previous player resources otherwise restart a same player.
		//		if(this.m_player != null)
		//		{
		//			m_player.release();
		//			this.m_player = null;
		//		}

		//reload player resources.
		//		this.prefetch();
		if (m_player != null) {
			this.m_player.setVolume(MAX_VOLUME / 100F, MAX_VOLUME / 100F);
			this.m_player.start();
			this.m_state = Player.STARTED;
			this.triggerListener(PlayerListener.STARTED, null);
		}
	}

	public void stop() throws MediaException {
		if (DebugConfig.DEBUG_ANDROID_MEDIA) {
			Log.v(TAG, DebugConfig.TAG_MEDIA + "stop");
		}

		if (m_player != null) {
			this.m_player.pause();
			//			this.m_state = Player.PREFETCHED;
			this.m_state = Player.REALIZED;
			this.triggerListener(PlayerListener.STOPPED, null);
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

	public void onCompletion(MediaPlayer mp) {
		if (DebugConfig.DEBUG_ANDROID_MEDIA) {
			Log.v(TAG, DebugConfig.TAG_MEDIA + "onCompletion");
		}
		if (mp == this.m_player) {
			triggerListener(PlayerListener.END_OF_MEDIA, null);
			if (this.m_loopCount > 1 || this.m_loopCount == -1) {
				try {
					//android.util.Log.v("MediaPlayer","restart sound");
					this.start();
				} catch (Exception e) {
					if (DebugConfig.DEBUG_ANDROID_MEDIA && DebugConfig.DEBUG_ANDROID_EXCEPTION) {
						android.util.Log.e(TAG, "restart sound failed!", e);
					}
				}
				if (this.m_loopCount != -1) {
					this.m_loopCount--;
				}
			}
		}
	}

	///////////////////////////////////////////////////////////////////////////////
	//////////////////////---- Volume Control ----/////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////
	private final static int MAX_VOLUME = 60;
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
		if (DebugConfig.DEBUG_ANDROID_MEDIA) {
			Log.v(TAG, DebugConfig.TAG_MEDIA + "setLevel: " + level);
		}
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
				this.m_player.setVolume(m_volumeLevel / 100F, m_volumeLevel / 100F);
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
