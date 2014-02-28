package javax.microedition.rms;

/*
 * �쳣�� ֱ�ӿ�����Phoneme��
 */

/**
 * Thrown to indicate an operation could not be completed because the record ID
 * was invalid.
 * 
 * @since MIDP 1.0
 */

public class InvalidRecordIDException extends RecordStoreException {
	/**
	 * Constructs a new <code>InvalidRecordIDException</code> with the specified
	 * detail message.
	 * 
	 * @param message
	 *            the detail message
	 */
	public InvalidRecordIDException(String message) {
		super(message);
	}

	/**
	 * Constructs a new <code>InvalidRecordIDException</code> with no detail
	 * message.
	 */
	public InvalidRecordIDException() {
	}
}
