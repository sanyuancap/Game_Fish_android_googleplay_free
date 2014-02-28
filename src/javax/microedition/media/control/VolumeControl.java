package javax.microedition.media.control;

import javax.microedition.media.*;
public interface VolumeControl extends Control {
	public int getLevel();

	public boolean isMuted();

	public int setLevel(int level);

	public void setMute(boolean mute);
}
