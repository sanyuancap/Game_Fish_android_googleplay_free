package com.game5a.common;

import javax.microedition.media.Manager;
import javax.microedition.media.Player;
import javax.microedition.media.control.VolumeControl;

import com.game5a.fish_googleplay_free.R;

public class Sound {
	private Player player;
	public static final String WAVE = "audio/x-wav";
	public static final String MIDI = "audio/midi";
	public static final int INIT_VOLUME = 100;
	public String soundFile;
	
	public static final String[] MID_FILE_NAME = {"/m0.mid", "/m1.mid"};
	public static final int[] MID_FILE_ID = {R.raw.m0, R.raw.m1};
	
	public static int getFileID(String file) {
		for (int i = 0; i < MID_FILE_NAME.length; i++) {
			if (file.compareTo(MID_FILE_NAME[i]) == 0) {
				return MID_FILE_ID[i];
			}
		}
		return -1;
	}

	public Sound(String file, String type, boolean bLoop) {
		setSound(file, type, bLoop);
	}

	public void setSound(String file, String type, boolean bLoop) {
		System.out.println("Load music: " + file);
		soundFile = file;
		deallocate();

		try {
//			InputStream in = Tool.getResourceAsStream(file);
//			player = Manager.createPlayer(in, type);
			//android 110728
			int id = getFileID(file);
			player = Manager.createPlayer(id);
			player.realize();

			if (bLoop) {
				player.setLoopCount(-1);
			} else {
				player.setLoopCount(1);
			}
			player.prefetch(); //N7610
//			in.close();
//			in = null;

		} catch (Exception e) {
			Tool.reportException(e);
		}
	}

	public void start() {
		if (player == null) {
			return;
		}

		try {
			player.start();
		} catch (Exception e) {
			Tool.reportException(e);
		}
	}

	public void stop() {
		if (player == null) {
			return;
		}

		try {
			player.stop();
			//v300
			//player.deallocate();
		} catch (Exception e) {
			Tool.reportException(e);
		}

		setMediaTime(0L);
	}

	public void deallocate() {
		if (player == null) {
			return;
		}
		try {
			if (player.getState() == Player.STARTED) {
				player.stop();
			}
			player.deallocate();
		} catch (Exception e) {
			Tool.reportException(e);
		}
	}

	public void close() {
		if (player == null) {
			return;
		}
		try {
			if (player.getState() == Player.STARTED) {
				player.stop();
			}
			player.deallocate();
			player.close();
			player = null;
		} catch (Exception e) {
			Tool.reportException(e);
		}
	}

	public void setMediaTime(long mt) {
		try {
			if (player.getState() != Player.UNREALIZED && player.getState() != Player.CLOSED)
				player.setMediaTime(mt);
		} catch (Exception e) {
			Tool.reportException(e);
		}
	}

	public boolean isStarted() {
		if (player.getState() == Player.STARTED) {
			return true;
		}
		return false;
	}

	public boolean isPrefetched() {
		if (player.getState() == Player.PREFETCHED) {
			return true;
		}
		return false;
	}

	public boolean isRealized() {
		if (player.getState() == Player.REALIZED) {
			return true;
		}
		return false;
	}

	public void setVolume(int vol) {
		VolumeControl volume = (VolumeControl) player.getControl("VolumeControl");
		if (volume != null) {
			volume.setLevel(vol);
		}
	}
}
