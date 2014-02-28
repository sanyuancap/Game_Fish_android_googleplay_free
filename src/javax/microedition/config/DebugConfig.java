package javax.microedition.config;

public interface DebugConfig {
	public final static boolean DEBUG_ON = true;

	public final static boolean DEBUG_ANDROID = DEBUG_ON && true;

	public final static boolean DEBUG_ANDROID_CALLBACK = DEBUG_ANDROID && true;

	public final static boolean DEBUG_ANDROID_EXCEPTION = DEBUG_ANDROID && true;

	public final static boolean DEBUG_ANDROID_MIDLET = DEBUG_ANDROID && true;

	public final static boolean DEBUG_ANDROID_MEDIA = DEBUG_ANDROID && !false;

	public final static String TAG_CALLBACK = "[callback] ";

	public final static String TAG_EXCEPTION = "[exception] ";

	public final static String TAG_MEDIA = "[media] ";
}
