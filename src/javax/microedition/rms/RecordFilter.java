package javax.microedition.rms;

/*
 * 接口，直接拷贝自phoneme的定义
 */

/**
 * An interface defining a filter which examines a record to see if it matches
 * (based on an application-defined criteria). The application implements the
 * match() method to select records to be returned by the RecordEnumeration.
 * Returns true if the candidate record is selected by the RecordFilter. This
 * interface is used in the record store for searching or subsetting records.
 * For example:
 * 
 * <pre>
 * RecordFilter f = new DateRecordFilter(); // class implements RecordFilter
 * if (f.matches(recordStore.getRecord(theRecordID)) == true)
 * 	DoSomethingUseful(theRecordID);
 * </pre>
 * 
 * @since MIDP 1.0
 */

public interface RecordFilter {
	/**
	 * Returns true if the candidate matches the implemented criterion.
	 * 
	 * @param candidate
	 *            the record to consider. Within this method, the application
	 *            must treat this parameter as read-only.
	 * 
	 * @return true if the candidate matches the implemented criterion
	 */
	public abstract boolean matches(byte[] candidate);

}
