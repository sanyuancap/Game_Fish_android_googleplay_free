package javax.microedition.media;

import java.io.*;
public class Manager {
	public static final String TONE_DEVICE_LOCATOR = "device://tone";

	public static final int IMPLTYPE_MEDIAPLAYER = 0;

	public static final int IMPLTYPE_SOUNDPOOL = 1;

	private static int s_implType = IMPLTYPE_MEDIAPLAYER;

	public static Player createPlayer(InputStream stream, String type) throws IOException, MediaException {
		if (s_implType == IMPLTYPE_MEDIAPLAYER) {
			return new MediaPlayerImpl();
		} else if (s_implType == IMPLTYPE_SOUNDPOOL) {
			return new SoundPoolImpl();
		}
		return null;
	}

	public static Player createPlayer(int resId) throws IOException, MediaException {
		if (s_implType == IMPLTYPE_MEDIAPLAYER) {
			return new MediaPlayerImpl(resId);
		} else if (s_implType == IMPLTYPE_SOUNDPOOL) {
			return new SoundPoolImpl(resId);
		}
		return null;
	}

	public static Player createPlayer(int resId, int implType) throws IOException, MediaException {
		if (implType == IMPLTYPE_MEDIAPLAYER) {
			return new MediaPlayerImpl(resId);
		} else if (implType == IMPLTYPE_SOUNDPOOL) {
			return new SoundPoolImpl(resId);
		}
		return null;
	}

	public static Player createPlayer(String locator) throws IOException, MediaException {
		if (s_implType == IMPLTYPE_MEDIAPLAYER) {
			return new MediaPlayerImpl(locator);
		} else if (s_implType == IMPLTYPE_SOUNDPOOL) {
			return new SoundPoolImpl(locator);
		}
		return null;
	}

	public static String[] getSupportedContentTypes(String protocol) {
		return null;
	}

	public static String[] getSupportedProtocols(String content_type) {
		return null;
	}

	public static void playTone(int note, int duration, int volume) throws MediaException {

	}
}
