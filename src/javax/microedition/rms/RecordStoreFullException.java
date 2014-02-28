package javax.microedition.rms;

/*
 * �쳣�� ֱ�ӿ�����Phoneme��ʵ��
 */

/**
 * Thrown to indicate an operation could not be completed because the record
 * store system storage is full.
 * 
 * @since MIDP 1.0
 */

public class RecordStoreFullException extends RecordStoreException {
	/**
	 * Constructs a new <code>RecordStoreFullException</code> with the specified
	 * detail message.
	 * 
	 * @param message
	 *            the detail message
	 */
	public RecordStoreFullException(String message) {
		super(message);
	}

	/**
	 * Constructs a new <code>RecordStoreFullException</code> with no detail
	 * message.
	 */
	public RecordStoreFullException() {
	}
}
