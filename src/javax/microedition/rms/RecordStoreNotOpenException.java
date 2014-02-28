package javax.microedition.rms;

/*
 * �쳣�� ֱ�ӿ�����Phoneme��ʵ��
 */

/**
 * Thrown to indicate that an operation was attempted on a closed record store.
 * 
 * @since MIDP 1.0
 */

public class RecordStoreNotOpenException extends RecordStoreException {
	/**
	 * Constructs a new <code>RecordStoreNotOpenException</code> with the
	 * specified detail message.
	 * 
	 * @param message
	 *            the detail message
	 */
	public RecordStoreNotOpenException(String message) {
		super(message);
	}

	/**
	 * Constructs a new <code>RecordStoreNotOpenException</code> with no detail
	 * message.
	 */
	public RecordStoreNotOpenException() {
	}
}
