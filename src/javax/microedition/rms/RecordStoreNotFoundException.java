package javax.microedition.rms;

/*
 * 异常类 直接拷贝自Phoneme的实现
 */

/**
 * Thrown to indicate an operation could not be completed because the record
 * store could not be found.
 * 
 * @since MIDP 1.0
 */

public class RecordStoreNotFoundException extends RecordStoreException {
	/**
	 * Constructs a new <code>RecordStoreNotFoundException</code> with the
	 * specified detail message.
	 * 
	 * @param message
	 *            the detail message
	 */
	public RecordStoreNotFoundException(String message) {
		super(message);
	}

	/**
	 * Constructs a new <code>RecordStoreNotFoundException</code> with no detail
	 * message.
	 */
	public RecordStoreNotFoundException() {
	}
}
