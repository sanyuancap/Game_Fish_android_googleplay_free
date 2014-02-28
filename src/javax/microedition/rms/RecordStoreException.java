package javax.microedition.rms;

/*
 * �쳣�� ֱ�ӿ�����Phoneme��ʵ��
 */

/**
 * Thrown to indicate a general exception occurred in a record store operation.
 * 
 * @since MIDP 1.0
 */

public class RecordStoreException extends java.lang.Exception {
	/**
	 * Constructs a new <code>RecordStoreException</code> with the specified
	 * detail message.
	 * 
	 * @param message
	 *            the detail message
	 */
	public RecordStoreException(String message) {
		super(message);
	}

	/**
	 * Constructs a new <code>RecordStoreException</code> with no detail
	 * message.
	 */
	public RecordStoreException() {
	}
}
