/*
 * 这是一个RecordStore的基于File的实现用于SE系统，与之相对的可以是基于
 * Android的基于SQLiteRecordStore实现。因为根据API文档RecordStore是基
 * 于MIDletSuite的，每个RecordStore属于一个MIDletSuite，其他的MIDletSuite
 * 的MIDlet想要访问其他MIDletSuite的RecordStore，该RecordStore必须是可见的
 */

package javax.microedition.rms;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;

import javax.microedition.midlet.ActivityMIDletBridge;

import android.util.Log;

/**
 * A class repreSsenting a record store. A record store consists of a collection
 * of records which will remain persistent across multiple invocations of the
 * MIDlet. The platform is responsible for making its best effort to maintain
 * the integrity of the MIDlet's record stores throughout the normal use of the
 * platform, including reboots, battery changes, etc.
 * 
 * <p>
 * Record stores are created in platform-dependent locations, which are not
 * exposed to the MIDlets. The naming space for record stores is controlled at
 * the MIDlet suite granularity. MIDlets within a MIDlet suite are allowed to
 * create multiple record stores, as long as they are each given different
 * names. When a MIDlet suite is removed from a platform all the record stores
 * associated with its MIDlets will also be removed. MIDlets within a MIDlet
 * suite can access each other's record stores directly. New APIs in MIDP allow
 * for the explicit sharing of record stores if the MIDlet creating the
 * RecordStore chooses to give such permission.
 * </p>
 * 
 * <p>
 * Sharing is accomplished through the ability to name a RecordStore created by
 * another MIDlet suite.
 * </p>
 * 
 * <P>
 * RecordStores are uniquely named using the unique name of the MIDlet suite
 * plus the name of the RecordStore. MIDlet suites are identified by the
 * MIDlet-Vendor and MIDlet-Name attributes from the application descriptor.
 * </p>
 * 
 * <p>
 * Access controls are defined when RecordStores to be shared are created.
 * Access controls are enforced when RecordStores are opened. The access modes
 * allow private use or shareable with any other MIDlet suite.
 * </p>
 * 
 * <p>
 * Record store names are case sensitive and may consist of any combination of
 * between one and 32 Unicode characters inclusive. Record store names must be
 * unique within the scope of a given MIDlet suite. In other words, MIDlets
 * within a MIDlet suite are not allowed to create more than one record store
 * with the same name, however a MIDlet in one MIDlet suite is allowed to have a
 * record store with the same name as a MIDlet in another MIDlet suite. In that
 * case, the record stores are still distinct and separate.
 * </p>
 * 
 * <p>
 * No locking operations are provided in this API. Record store implementations
 * ensure that all indAividual record store operations are atomic, synchronous,
 * and serialized, so no corruption will occur with multiple accesses. However,
 * if a MIDlet uses multiple threads to access a record store, it is the
 * MIDlet's responsibility to coordinate this access or unintended consequences
 * may result. Similarly, if a platform performs transparent synchronization of
 * a record store, it is the platform's responsibility to enforce exclusive
 * access to the record store between the MIDlet and synchronization engine.
 * </p>
 * 
 * <p>
 * Records are uniquely identified within a given record store by their
 * recordId, which is an integer value. This recordId is used as the primary key
 * for the records. The first record created in a record store will have
 * recordId equal to one (1). Each subsequent record added to a RecordStore will
 * be assigned a recordId one greater than the record added before it. That is,
 * if two records are added to a record store, and the first has a recordId of
 * 'n', the next will have a recordId of 'n + 1'. MIDlets can create other
 * sequences of the records in the RecordStore by using the
 * <code>RecordEnumeration</code> class.
 * </p>
 * 
 * <p>
 * This record store uses long integers for time/date stamps, in the format used
 * by System.currentTimeMillis(). The record store is time stamped with the last
 * time it was modified. The record store also maintains a <em>version</em>
 * number, which is an integer that is incremented for each operation that
 * modifies the contents of the RecordStore. These are useful for
 * synchronization engines as well as other things.
 * </p>
 * 
 * @since MIDP 1.0
 */

public class RecordStore {

	public class RecordEnumerationImpl implements RecordEnumeration {

		public void destroy() {
			// TODO Auto-generated method stub

		}

		public boolean hasNextElement() {
			// TODO Auto-generated method stub
			return false;
		}

		public boolean hasPreviousElement() {
			// TODO Auto-generated method stub
			return false;
		}

		public boolean isKeptUpdated() {
			// TODO Auto-generated method stub
			return false;
		}

		public void keepUpdated(boolean keepUpdated) {
			// TODO Auto-generated method stub

		}

		public byte[] nextRecord() throws InvalidRecordIDException, RecordStoreNotOpenException, RecordStoreException {
			// TODO Auto-generated method stub
			return null;
		}

		public int nextRecordId() throws InvalidRecordIDException {
			// TODO Auto-generated method stub
			return 0;
		}

		public int numRecords() {
			// TODO Auto-generated method stub
			return 0;
		}

		public byte[] previousRecord() throws InvalidRecordIDException, RecordStoreNotOpenException, RecordStoreException {
			// TODO Auto-generated method stub
			return null;
		}

		public int previousRecordId() throws InvalidRecordIDException {
			// TODO Auto-generated method stub
			return 0;
		}

		public void rebuild() {
			// TODO Auto-generated method stub

		}

		public void reset() {
			// TODO Auto-generated method stub

		}

	}

	/**
	 * RMS文件头结构及OFFSET
	 */

	/**
	 * 签名LONERRMS(8字节)
	 */
	private final static int SIGNATURE = 0;

	/**
	 * 认证模式(4字节)
	 */
	private final static int AUTHMODE_OFFSET = 8;

	/**
	 * 对于其他的MIDlet suite是否可写(1字节)
	 */
	private final static int WRITABLE_OFFSET = 12;

	/**
	 * 时间戳记(8字节)
	 */
	private final static int LAST_MODIFIED_OFFSET = 13;

	/**
	 * 版本号(4字节)
	 */
	private final static int VERSION_OFFSET = 21;

	/**
	 * 下一个ID号(4字节)
	 */
	private final static int NEXT_ID_OFFSET = 25;

	/**
	 * 本record store所占字节数(4字节最大是2G)
	 */
	private final static int SIZE_RECORD_TAKE_OFFSET = 29;

	private final static int SIGN_SIZE = 8;

	private final static int HEAD_SIZE = 25;

	/**
	 * Authorization to allow access only to the current MIDlet suite.
	 * AUTHMODE_PRIVATE has a value of 0.
	 */
	public final static int AUTHMODE_PRIVATE = 0;

	/**
	 * Authorization to allow access to any MIDlet suites. AUTHMODE_ANY has a
	 * value of 1.
	 */
	public final static int AUTHMODE_ANY = 1;

	/**
	 * 系统分隔符
	 */
	private final static String FILE_SEPARATOR = System.getProperty("file.separator");

	private final static String EXT_NAME = "db";

	/**
	 * 存储打开的RecordStore，根据API文档（If this method is called by a MIDlet when the
	 * record store is already open by a MIDlet in the MIDlet suite, this method
	 * returns a reference to the same RecordStore object），对于相同MIDlet
	 * suite的MIDlet调用此方法 如果同名record store已经打开，则只返回其引用。 此容器就是为了保存打开的record
	 * store的引用，以MIDlet suite的名字和recordStoreName做key。
	 */
	private static HashMap<String, RecordStore> openedRecordStores = new HashMap<String, RecordStore>();

	/**
	 * 存储当前record store的每个record的recordId 所在文件中的offset 和 record 的大小
	 */
	private ArrayList<int[]> indexData = new ArrayList<int[]>();

	private ArrayList<RecordListener> recordListeners = new ArrayList<RecordListener>();

	private int getRecordIdIndex(int recordId) {
		for (int a = 0; a < indexData.size(); a++) {
			int[] is = indexData.get(a);
			if (is[0] == recordId) {
				return a;
			}
		}
		return -1;
	}

	/**
	 * 用来实现RecordStore，每个RecordStore存放在一个文件中，同事该文件还存放最后修改时间，
	 * 版本号，是否可见(authmode)，是否可修改(writable)，等信息。
	 */
	private File recordStoreFile;
	private FileChannel recordStoreFileChannel;

	/**
	 * 本文件打开的次数。
	 */
	private int openedNum = 0;

	/**
	 * 属于本record store的midletSuiteName的名字
	 */
	private String midletSuiteName;

	private String vendor;

	private String recordStoreName;

	private long lastModified;

	private int version;

	private int recordStoreSize;// recordStore应战空间

	private int dataSize;// 数据区所占空间

	private int dataOffset;// 数据区所占空间

	private int nextId;

	private int authmode;

	private boolean writable;

	private int headSize;

	/**
	 * Deletes the named record store. MIDlet suites are only allowed to delete
	 * their own record stores. If the named record store is open (by a MIDlet
	 * in this suite or a MIDlet in a different MIDlet suite) when this method
	 * is called, a RecordStoreException will be thrown. If the named record
	 * store does not exist a RecordStoreNotFoundException will be thrown.
	 * Calling this method does NOT result in recordDeleted calls to any
	 * registered listeners of this RecordStore.
	 * 
	 * @param recordStoreName
	 *            the MIDlet suite unique record store to delete
	 * 
	 * @exception RecordStoreException
	 *                if a record store-related exception occurred
	 * @exception RecordStoreNotFoundException
	 *                if the record store could not be found
	 */
	public static void deleteRecordStore(String recordStoreName) throws RecordStoreException, RecordStoreNotFoundException {

	}

	/**
	 * 构造器
	 * 
	 * @param recordStoreFile
	 */
	private RecordStore(File recordStoreFile) {
		this.recordStoreFile = recordStoreFile;
	}

	/**
	 * 通过所给cheatMode不同检测不同的安全性 CHECK_MODE = 1 检测是否可以访问，打开时检测 CHECK_MODE = 2
	 * 检测是否可以addRecord,setRecord, deleteRecord，作如上操作时检测 CHECK_MODE = 3
	 * 检测是否可以setMode，作如上操作时检测
	 * 
	 * @param checkMode
	 */
	private static final int CHECK_MODE_ACCESS = 1;
	private static final int CHECK_MODE_ADD_SET_DELETE = 2;
	private static final int CHECK_MODE_SETMODE = 3;

	private void checkSecurity(int checkMode) {
		switch (checkMode) {
			case CHECK_MODE_ACCESS :
				if ((!getCurrentMIDletSuiteName().equals(midletSuiteName) || !getCurrentVendor().equals(vendor)) && authmode != AUTHMODE_ANY) {
					throw new SecurityException();
				}
				break;
			case CHECK_MODE_ADD_SET_DELETE :
				if ((!getCurrentMIDletSuiteName().equals(midletSuiteName) || !getCurrentVendor().equals(vendor)) && !writable) {
					throw new SecurityException();
				}
				break;
			case CHECK_MODE_SETMODE :
				if ((!getCurrentMIDletSuiteName().equals(midletSuiteName) || !getCurrentVendor().equals(vendor))) {
					throw new SecurityException();
				}
				break;
			default :
				break;
		}
	}

	/**
	 * 初始化所需数据 构造索引表格
	 */
	private void init() throws RecordStoreException {
		try {
			DataInputStream dataInputStream = new DataInputStream(new FileInputStream(recordStoreFile));
			dataInputStream.skip(AUTHMODE_OFFSET);
			headSize = dataInputStream.readInt();
			authmode = dataInputStream.readInt();
			writable = dataInputStream.readBoolean();
			lastModified = dataInputStream.readLong();
			version = dataInputStream.readInt();
			nextId = dataInputStream.readInt();
			midletSuiteName = dataInputStream.readUTF();
			vendor = dataInputStream.readUTF();
			recordStoreName = dataInputStream.readUTF();
			dataSize = dataInputStream.readInt();
			dataOffset = headSize + AUTHMODE_OFFSET + 4;
			dataInputStream.skip(dataSize);
			int num = dataInputStream.readInt();
			for (int i = 0; i < num; i++) {
				indexData.add(new int[]{dataInputStream.readInt(), dataInputStream.readInt(), dataInputStream.readInt()});
			}
			dataInputStream.close();
			recordStoreFileChannel = new RandomAccessFile(recordStoreFile, "rw").getChannel();
			openedNum++;
		} catch (Exception e) {
			throw new RecordStoreException(e.toString());
		}
	}

	private static void createRecordStoreFile(File file, String midletSuiteName, String vendor, String recordStoreName, int authmode, boolean writable) throws RecordStoreFullException {
		try {
			if (!file.exists()) {
				file.getParentFile().mkdirs();
				file.createNewFile();
			}
			DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream(file));
			dataOutputStream.write("LONERRMS".getBytes("utf-8"));
			dataOutputStream.writeInt(HEAD_SIZE + (midletSuiteName + vendor + recordStoreName).getBytes("utf-8").length + 6);
			dataOutputStream.writeInt(authmode);
			dataOutputStream.writeBoolean(writable);
			dataOutputStream.writeLong(System.currentTimeMillis());
			dataOutputStream.writeInt(0);// version
			dataOutputStream.writeInt(1);// next id
			dataOutputStream.writeUTF(midletSuiteName);
			dataOutputStream.writeUTF(vendor);
			dataOutputStream.writeUTF(recordStoreName);// end of head
			dataOutputStream.writeInt(0);// record data size
			dataOutputStream.writeInt(0);// index number
			dataOutputStream.close();
		} catch (Exception e) {
			throw new RecordStoreFullException(e.toString());
		}
	}

	/**
	 * @return RecordStore所应该存放的路径
	 */
	private static String getPath() {
		return getRoot() + FILE_SEPARATOR + getCurrentMIDletSuiteName() + "_" + getCurrentVendor() + FILE_SEPARATOR;
	}

	/**
	 * @return 通过AMS获得存放record store的根目录
	 */
	private static String getRoot() {
		return ActivityMIDletBridge.rootPath;
	}

	/**
	 * @return 当前MIDletSuite的名称(通过AMS获得)：非常重要
	 */
	private static String getCurrentMIDletSuiteName() {
		return "TEST";
	}

	/**
	 * @return 当前的MIDlet所属的MIDlet suite的vendor名字(通过AMS获得)
	 */
	private static String getCurrentVendor() {
		return "GLU";
	}

	/**
	 * @return 当前的MIDlet所属的MIDlet suite的vendor名字(通过AMS获得)
	 */
	private static int getFullSize() {
		return 2 << 20;
	}

	/**
	 * 如果RecordStore已经关闭，则抛出RecordStoreNotOpenException。通过检测
	 * 计数器是否小于1判断。如果小于1则说明RecordStore打开的次数小于关闭的次数。
	 * 
	 * @exception RecordStoreNotOpenException
	 */
	private void checkOpen() throws RecordStoreNotOpenException {
		if (!isOpened()) {
			throw new RecordStoreNotOpenException();
		}
	}

	private void checkRecordStoreFull(int numBytes) throws RecordStoreFullException, RecordStoreNotOpenException {
		int sizeAvailable = getSizeAvailableIn();
		if (sizeAvailable < numBytes) {
			throw new RecordStoreFullException("sizeAvailable < the record size you need.you can only restore:" + sizeAvailable);
		}
	}

	/**
	 * @return RecordStore是否打开
	 */
	private boolean isOpened() {
		return openedNum > 0;
	}

	/**
	 * 返回属于当前MIDlet所属MIDletSuite的所有RecordStore的名字的String数组。
	 * 需要注意的是如果MIDletSuite没有RecordStore，本方法将返回null。 返回数组的顺序有相关实现决定。<br>
	 * 
	 * Returns an array of the names of record stores owned by the MIDlet suite.
	 * Note that if the MIDlet suite does not have any record stores, this
	 * function will return null.
	 * 
	 * The order of RecordStore names returned is implementation dependent.
	 * 
	 * @return array of the names of record stores owned by the MIDlet suite.
	 *         Note that if the MIDlet suite does not have any record stores,
	 *         this function will return null.
	 */
	public static String[] listRecordStores() {
		synchronized (openedRecordStores) {
			File file = new File(getPath());
			return file.list(new FilenameFilter() {
				public boolean accept(File dir, String name) {
					// TODO Auto-generated method stub
					return name.endsWith(EXT_NAME);
				}
			});
		}
	}

	/**
	 * @param recordStoreName
	 * @param vendorName
	 * @param suiteName
	 * @param createIfNecessary
	 * @param authmode
	 * @param writable
	 * @return
	 * @throws RecordStoreException
	 * @throws RecordStoreFullException
	 * @throws RecordStoreNotFoundException
	 */
	private static RecordStore getRecordStore(String recordStoreName, String vendorName, String suiteName, boolean createIfNecessary, int authmode, boolean writable) throws RecordStoreException, RecordStoreFullException, RecordStoreNotFoundException {
		if (recordStoreName.length() > 32 || recordStoreName.length() == 0) {
			throw new IllegalArgumentException(recordStoreName);
		}
		RecordStore recordStore = null;
		Log.v("getRecordStore", "" + recordStoreName);
		synchronized (openedRecordStores) {// 以openedRecordStores为锁
			recordStore = openedRecordStores.get(getCurrentMIDletSuiteName() + getCurrentVendor() + recordStoreName);
			if (recordStore != null) {
				recordStore.openedNum++;
				return recordStore;
			}

			File file;
			if (vendorName == null || suiteName == null) {
				file = new File(getPath() + recordStoreName + "." + EXT_NAME);
			} else {
				file = new File(getRoot() + FILE_SEPARATOR + suiteName + "_" + vendorName + FILE_SEPARATOR + recordStoreName + "." + EXT_NAME);
			}
			if (!file.exists() && !createIfNecessary) {
				throw new RecordStoreNotFoundException();
			}
			if (!file.exists() && createIfNecessary) {
				createRecordStoreFile(file, getCurrentMIDletSuiteName(), getCurrentVendor(), recordStoreName, authmode, writable);
			}
			recordStore = new RecordStore(file);
			recordStore.init();
			recordStore.checkSecurity(CHECK_MODE_ACCESS);
			openedRecordStores.put(getCurrentMIDletSuiteName() + recordStoreName, recordStore);
		}
		return recordStore;
	}

	/**
	 * 打开 （或者创建）一个与当前MIDlet suite相关联的 record store。 如果本方法调用时， 同名record
	 * store已被与调用本方法的MIDlet同属一个MIDlet suite的MIDlet调用并打开。 则返回该record store的引用。<br>
	 * Open (and possibly create) a record store associated with the given
	 * MIDlet suite. If this method is called by a MIDlet when the record store
	 * is already open by a MIDlet in the MIDlet suite, this method returns a
	 * reference to the same RecordStore object.
	 * 
	 * @param recordStoreName
	 *            同一MIDlet suite中唯一的record store的名字，由1至32个字母组成。 the MIDlet suite
	 *            unique name for the record store, consisting of between one
	 *            and 32 Unicode characters inclusive.
	 * @param createIfNecessary
	 *            如果为真，当需要时(通常是record store不存在)则创建record store。 if true, the
	 *            record store will be created if necessary
	 * 
	 * @return <code>RecordStore</code> record store对象。 object for the record
	 *         store
	 * 
	 * @exception RecordStoreException
	 *                当出现一个record store相关的异常时抛出。 if a record store-related
	 *                exception occurred
	 * @exception RecordStoreNotFoundException
	 *                当未发现record store时抛出。 if the record store could not be
	 *                found
	 * @exception RecordStoreFullException
	 *                当record store已满导致无法完成操作时抛出。 if the operation cannot be
	 *                completed because the record store is full
	 * @exception IllegalArgumentException
	 *                当参数recordStoreName非法时抛出。 if recordStoreName is invalid
	 */
	public static RecordStore openRecordStore(String recordStoreName, boolean createIfNecessary) throws RecordStoreException, RecordStoreFullException, RecordStoreNotFoundException {

		Log.v("openRecordStore", recordStoreName);

		return getRecordStore(recordStoreName, null, null, createIfNecessary, AUTHMODE_PRIVATE, false);
	}

	/**
	 * Open (and possibly create) a record store that can be shared with other
	 * MIDlet suites. The RecordStore is owned by the current MIDlet suite. The
	 * authorization mode is set when the record store is created, as follows:
	 * 
	 * <ul>
	 * <li><code>AUTHMODE_PRIVATE</code> - Only allows the MIDlet suite that
	 * created the RecordStore to access it. This case behaves identically to
	 * <code>openRecordStore(recordStoreName,
	 *          createIfNecessary)</code>.</li>
	 * <li><code>AUTHMODE_ANY</code> - Allows any MIDlet to access the
	 * RecordStore. Note that this makes your recordStore accessible by any
	 * other MIDlet on the device. This could have privacy and security issues
	 * depending on the data being shared. Please use carefully.</li>
	 * </ul>
	 * 
	 * <p>
	 * The owning MIDlet suite may always access the RecordStore and always has
	 * access to write and update the store.
	 * </p>
	 * 
	 * <p>
	 * If this method is called by a MIDlet when the record store is already
	 * open by a MIDlet in the MIDlet suite, this method returns a reference to
	 * the same RecordStore object.
	 * </p>
	 * 
	 * @param recordStoreName
	 *            the MIDlet suite unique name for the record store, consisting
	 *            of between one and 32 Unicode characters inclusive.
	 * @param createIfNecessary
	 *            if true, the record store will be created if necessary
	 * @param authmode
	 *            the mode under which to check or create access. Must be one of
	 *            AUTHMODE_PRIVATE or AUTHMODE_ANY. This argument is ignored if
	 *            the RecordStore exists.
	 * @param writable
	 *            true if the RecordStore is to be writable by other MIDlet
	 *            suites that are granted access. This argument is ignored if
	 *            the RecordStore exists.
	 * 
	 * @return <code>RecordStore</code> object for the record store
	 * 
	 * @exception RecordStoreException
	 *                if a record store-related exception occurred
	 * @exception RecordStoreNotFoundException
	 *                if the record store could not be found
	 * @exception RecordStoreFullException
	 *                if the operation cannot be completed because the record
	 *                store is full
	 * @exception IllegalArgumentException
	 *                if authmode or recordStoreName is invalid
	 */
	public static RecordStore openRecordStore(String recordStoreName, boolean createIfNecessary, int authmode, boolean writable) throws RecordStoreException, RecordStoreFullException, RecordStoreNotFoundException {
		Log.v("openRecordStore", recordStoreName);
		return getRecordStore(recordStoreName, null, null, createIfNecessary, authmode, writable);
	}

	/**
	 * Open a record store associated with the named MIDlet suite. The MIDlet
	 * suite is identified by MIDlet vendor and MIDlet name. Access is granted
	 * only if the authorization mode of the RecordStore allows access by the
	 * current MIDlet suite. Access is limited by the authorization mode set
	 * when the record store was created:
	 * 
	 * <ul>
	 * <li><code>AUTHMODE_PRIVATE</code> - Succeeds only if vendorName and
	 * suiteName identify the current MIDlet suite; this case behaves
	 * identically to <code>openRecordStore(recordStoreName,
	 *          createIfNecessary)</code>.</li>
	 * <li><code>AUTHMODE_ANY</code> - Always succeeds. Note that this makes
	 * your recordStore accessible by any other MIDlet on the device. This could
	 * have privacy and security issues depending on the data being shared.
	 * Please use carefully. Untrusted MIDlet suites are allowed to share data
	 * but this is not recommended. The authenticity of the origin of untrusted
	 * MIDlet suites cannot be verified so shared data may be used
	 * unscrupulously.</li>
	 * </ul>
	 * 
	 * <p>
	 * If this method is called by a MIDlet when the record store is already
	 * open by a MIDlet in the MIDlet suite, this method returns a reference to
	 * the same RecordStore object.
	 * </p>
	 * 
	 * <p>
	 * If a MIDlet calls this method to open a record store from its own suite,
	 * the behavior is identical to calling:
	 * <code>{@link #openRecordStore(String, boolean)
	 * openRecordStore(recordStoreName, false)}</code>
	 * </p>
	 * 
	 * @param recordStoreName
	 *            the MIDlet suite unique name for the record store, consisting
	 *            of between one and 32 Unicode characters inclusive.
	 * @param vendorName
	 *            the vendor of the owning MIDlet suite
	 * @param suiteName
	 *            the name of the MIDlet suite
	 * 
	 * @return <code>RecordStore</code> object for the record store
	 * 
	 * @exception RecordStoreException
	 *                if a record store-related exception occurred
	 * @exception RecordStoreNotFoundException
	 *                if the record store could not be found
	 * @exception SecurityException
	 *                if this MIDlet Suite is not allowed to open the specified
	 *                RecordStore.
	 * @exception IllegalArgumentException
	 *                if recordStoreName is invalid
	 */
	public static RecordStore openRecordStore(String recordStoreName, String vendorName, String suiteName) throws RecordStoreException, RecordStoreNotFoundException {
		if (vendorName == null || suiteName == null) {
			throw new IllegalArgumentException();
		}
		Log.v("openRecordStore", recordStoreName);
		return getRecordStore(recordStoreName, vendorName, suiteName, false, AUTHMODE_PRIVATE, false);
	}

	/**
	 * Adds a new record to the record store. The recordId for this new record
	 * is returned. This is a blocking atomic operation. The record is written
	 * to persistent storage before the method returns.
	 * 
	 * @param data
	 *            the data to be stored in this record. If the record is to have
	 *            zero-length data (no data), this parameter may be null.
	 * @param offset
	 *            the index into the data buffer of the first relevant byte for
	 *            this record
	 * @param numBytes
	 *            the number of bytes of the data buffer to use for this record
	 *            (may be zero)
	 * 
	 * @return the recordId for the new record
	 * 
	 * @exception RecordStoreNotOpenException
	 *                if the record store is not open
	 * @exception RecordStoreException
	 *                if a different record store-related exception occurred
	 * @exception RecordStoreFullException
	 *                if the operation cannot be completed because the record
	 *                store has no more room
	 * @exception SecurityException
	 *                if the MIDlet has read-only access to the RecordStore
	 */
	public int addRecord(byte[] data, int offset, int numBytes) throws RecordStoreNotOpenException, RecordStoreException, RecordStoreFullException {
		Log.v("addRecord", nextId + " " + recordStoreName);

		if (data == null || numBytes == 0)
			throw new RecordStoreException();

		synchronized (indexData) {
			checkRecordStoreFull(numBytes);// 检测open 和 sizeAvailable
			checkSecurity(CHECK_MODE_ADD_SET_DELETE);// 检测可写
			try {
				MappedByteBuffer mappedByteBuffer = recordStoreFileChannel.map(FileChannel.MapMode.READ_WRITE, dataOffset + dataSize, numBytes);
				mappedByteBuffer.put(data, offset, numBytes);
				indexData.add(new int[]{nextId, dataOffset + dataSize, numBytes});
				dataSize += numBytes;
			} catch (Exception ex) {
				throw new RecordStoreException();
			}
			updateData();
			for (RecordListener listener : recordListeners) {
				listener.recordAdded(this, nextId);
			}
			return nextId++;
		}
	}

	/**
	 * 更新version lastModified等数据
	 */
	private void updateData() {
		version++;
		lastModified = System.currentTimeMillis();
	}

	/**
	 * Adds the specified RecordListener. If the specified listener is already
	 * registered, it will not be added a second time. When a record store is
	 * closed, all listeners are removed.
	 * 
	 * @param listener
	 *            the RecordChangedListener
	 * @see #removeRecordListener
	 */
	public void addRecordListener(RecordListener listener) {
		if (!recordListeners.contains(listener)) {
			recordListeners.add(listener);
		}
	}

	/**
	 * 本方法当MIDlet请求关闭record store时调用。当closeRecordStore()被调用的次数
	 * 小于openRecordStore()被调用的次数时，record store并不会真正被关闭。也就是说 当record
	 * store打开的次数同关闭的次数相等时，record store才会真正关闭。 This method is called when the
	 * MIDlet requests to have the record store closed. Note that the record
	 * store will not actually be closed until closeRecordStore() is called as
	 * many times as openRecordStore() was called. In other words, the MIDlet
	 * needs to make a balanced number of close calls as open calls before the
	 * record store is closed.
	 * 
	 * <p>
	 * 当一个record store关闭时，所有的监听器(recordListener)都会被移除， When the record store is
	 * closed, all listeners are removed and all RecordEnumerations associated
	 * with it become invalid. If the MIDlet attempts to perform operations on
	 * the RecordStore object after it has been closed, the methods will throw a
	 * RecordStoreNotOpenException.
	 * 
	 * @exception RecordStoreNotOpenException
	 *                if the record store is not open
	 * @exception RecordStoreException
	 *                if a different record store-related exception occurred
	 * @throws IOException
	 */
	public void closeRecordStore() throws RecordStoreNotOpenException, RecordStoreException, IOException {
		Log.v("closeRecordStore", recordStoreName + "");
		synchronized (indexData) {
			checkOpen();
			if (--openedNum == 0) {
				ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
				DataOutputStream dataOutputStream = new DataOutputStream(arrayOutputStream);
				try {
					dataOutputStream.writeInt(headSize);
					dataOutputStream.writeInt(authmode);
					dataOutputStream.writeBoolean(writable);
					dataOutputStream.writeLong(lastModified);
					dataOutputStream.writeInt(version);// version
					dataOutputStream.writeInt(nextId);// next id
					dataOutputStream.writeUTF(midletSuiteName);
					dataOutputStream.writeUTF(vendor);
					dataOutputStream.writeUTF(recordStoreName);// end of head
					dataOutputStream.writeInt(dataSize);// record data size
					dataOutputStream.flush();
					byte[] bs = arrayOutputStream.toByteArray();
					//dataOutputStream.close();
					MappedByteBuffer mappedByteBuffer = recordStoreFileChannel.map(FileChannel.MapMode.READ_WRITE, AUTHMODE_OFFSET, bs.length);
					mappedByteBuffer.put(bs);
					arrayOutputStream.reset();
					dataOutputStream.writeInt(indexData.size());
					for (int i = 0; i < indexData.size(); i++) {
						int[] js = indexData.get(i);
						dataOutputStream.writeInt(js[0]);
						dataOutputStream.writeInt(js[1]);
						dataOutputStream.writeInt(js[2]);
					}
					bs = arrayOutputStream.toByteArray();
					dataOutputStream.close();
					mappedByteBuffer = recordStoreFileChannel.map(FileChannel.MapMode.READ_WRITE, dataOffset + dataSize, bs.length);
					mappedByteBuffer.put(bs);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				recordStoreFileChannel.force(true);
				recordStoreFileChannel.close();
				openedRecordStores.remove(getCurrentMIDletSuiteName() + getCurrentVendor() + recordStoreName);
			}
		}
	}

	/**
	 * The record is deleted from the record store. The recordId for this record
	 * is NOT reused.
	 * 
	 * @param recordId
	 *            the ID of the record to delete
	 * 
	 * @exception RecordStoreNotOpenException
	 *                if the record store is not open
	 * @exception InvalidRecordIDException
	 *                if the recordId is invalid
	 * @exception RecordStoreException
	 *                if a general record store exception occurs
	 * @exception SecurityException
	 *                if the MIDlet has read-only access to the RecordStore
	 */
	public void deleteRecord(int recordId) throws RecordStoreNotOpenException, InvalidRecordIDException, RecordStoreException {
		synchronized (indexData) {
			checkOpen();
			checkSecurity(CHECK_MODE_ADD_SET_DELETE);
			Object object = indexData.remove(getRecordIdIndex(recordId));
			if (object == null) {
				throw new InvalidRecordIDException();
			}
			updateData();
			for (RecordListener listener : recordListeners) {
				listener.recordDeleted(this, recordId);
			}
		}
	}

	/**
	 * Returns an enumeration for traversing a set of records in the record
	 * store in an optionally specified order.
	 * <p>
	 * 
	 * The filter, if non-null, will be used to determine what subset of the
	 * record store records will be used.
	 * <p>
	 * 
	 * The comparator, if non-null, will be used to determine the order in which
	 * the records are returned.
	 * <p>
	 * 
	 * If both the filter and comparator is null, the enumeration will traverse
	 * all records in the record store in an undefined order. This is the most
	 * efficient way to traverse all of the records in a record store. If a
	 * filter is used with a null comparator, the enumeration will traverse the
	 * filtered records in an undefined order.
	 * 
	 * The first call to <code>RecordEnumeration.nextRecord()</code> returns the
	 * record data from the first record in the sequence. Subsequent calls to
	 * <code>RecordEnumeration.nextRecord()</code> return the next consecutive
	 * record's data. To return the record data from the previous consecutive
	 * from any given point in the enumeration, call
	 * <code>previousRecord()</code>. On the other hand, if after creation the
	 * first call is to <code>previousRecord()</code>, the record data of the
	 * last element of the enumeration will be returned. Each subsequent call to
	 * <code>previousRecord()</code> will step backwards through the sequence.
	 * 
	 * @param filter
	 *            if non-null, will be used to determine what subset of the
	 *            record store records will be used
	 * @param comparator
	 *            if non-null, will be used to determine the order in which the
	 *            records are returned
	 * @param keepUpdated
	 *            if true, the enumerator will keep its enumeration current with
	 *            any changes in the records of the record store. Use with
	 *            caution as there are possible performance consequences. If
	 *            false the enumeration will not be kept current and may return
	 *            recordIds for records that have been deleted or miss records
	 *            that are added later. It may also return records out of order
	 *            that have been modified after the enumeration was built. Note
	 *            that any changes to records in the record store are accurately
	 *            reflected when the record is later retrieved, either directly
	 *            or through the enumeration. The thing that is risked by
	 *            setting this parameter false is the filtering and sorting
	 *            order of the enumeration when records are modified, added, or
	 *            deleted.
	 * 
	 * @exception RecordStoreNotOpenException
	 *                if the record store is not open
	 * 
	 * @see RecordEnumeration#rebuild
	 * 
	 * @return an enumeration for traversing a set of records in the record
	 *         store in an optionally specified order
	 */
	public RecordEnumeration enumerateRecords(RecordFilter filter, RecordComparator comparator, boolean keepUpdated) throws RecordStoreNotOpenException {
		return null;
	}

	/**
	 * Returns the last time the record store was modified, in the format used
	 * by System.currentTimeMillis().
	 * 
	 * @return the last time the record store was modified, in the format used
	 *         by System.currentTimeMillis()
	 * 
	 * @exception RecordStoreNotOpenException
	 *                if the record store is not open
	 */
	public long getLastModified() throws RecordStoreNotOpenException {
		checkOpen();
		return lastModified;
	}

	/**
	 * Returns the name of this RecordStore.
	 * 
	 * @return the name of this RecordStore
	 * @exception RecordStoreNotOpenException
	 *                if the record store is not open
	 */
	public String getName() throws RecordStoreNotOpenException {
		checkOpen();
		return recordStoreName;
	}

	/**
	 * Returns the recordId of the next record to be added to the record store.
	 * This can be useful for setting up pseudo-relational relationships. That
	 * is, if you have two or more record stores whose records need to refer to
	 * one another, you can predetermine the recordIds of the records that will
	 * be created in one record store, before populating the fields and
	 * allocating the record in another record store. Note that the recordId
	 * returned is only valid while the record store remains open and until a
	 * call to <code>addRecord()</code>.
	 * 
	 * @return the recordId of the next record to be added to the record store
	 * @exception RecordStoreNotOpenException
	 *                if the record store is not open
	 * @exception RecordStoreException
	 *                if a different record store-related exception occurred
	 */
	public int getNextRecordID() throws RecordStoreNotOpenException, RecordStoreException {
		synchronized (indexData) {
			checkOpen();
			return nextId;
		}
	}

	/**
	 * Returns the number of records currently in the record store.
	 * 
	 * @return the number of records currently in the record store
	 * @exception RecordStoreNotOpenException
	 *                if the record store is not open
	 */
	public int getNumRecords() throws RecordStoreNotOpenException {
		checkOpen();
		Log.v("getNumRecords", indexData.size() + "" + recordStoreName);
		return indexData.size();
	}

	/**
	 * Returns a copy of the data stored in the given record.
	 * 
	 * @param recordId
	 *            the ID of the record to use in this operation
	 * 
	 * @exception RecordStoreNotOpenException
	 *                if the record store is not open
	 * @exception InvalidRecordIDException
	 *                if the recordId is invalid
	 * @exception RecordStoreException
	 *                if a general record store exception occurs
	 * 
	 * @return the data stored in the given record. Note that if the record has
	 *         no data, this method will return null.
	 * @see #setRecord
	 */
	public byte[] getRecord(int recordId) throws RecordStoreNotOpenException, InvalidRecordIDException, RecordStoreException {
		Log.v("getRecord", recordId + " " + recordStoreName);
		synchronized (indexData) {
			checkOpen();
			int recordIdIndex = getRecordIdIndex(recordId);
			if (recordIdIndex == -1) {
				throw new InvalidRecordIDException();
			}
			int[] is = indexData.get(recordIdIndex);
			try {
				MappedByteBuffer mappedByteBuffer = recordStoreFileChannel.map(FileChannel.MapMode.READ_ONLY, is[1], is[2]);
				byte[] dst = new byte[is[2]];
				mappedByteBuffer.get(dst, 0, dst.length);
				return dst;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * Returns the data stored in the given record.
	 * 
	 * @param recordId
	 *            the ID of the record to use in this operation
	 * @param buffer
	 *            the byte array in which to copy the data
	 * @param offset
	 *            the index into the buffer in which to start copying
	 * 
	 * @exception RecordStoreNotOpenException
	 *                if the record store is not open
	 * @exception InvalidRecordIDException
	 *                if the recordId is invalid
	 * @exception RecordStoreException
	 *                if a general record store exception occurs
	 * @exception ArrayIndexOutOfBoundsException
	 *                if the record is larger than the buffer supplied
	 * 
	 * @return the number of bytes copied into the buffer, starting at index
	 *         <code>offset</code>
	 * @see #setRecord
	 */
	public int getRecord(int recordId, byte[] buffer, int offset) throws RecordStoreNotOpenException, InvalidRecordIDException, RecordStoreException {
		Log.v("getRecord", recordId + " " + recordStoreName);
		synchronized (indexData) {
			checkOpen();
			int recordIdIndex = getRecordIdIndex(recordId);
			if (recordIdIndex == -1) {
				throw new InvalidRecordIDException();
			}
			int[] is = indexData.get(recordIdIndex);
			try {
				MappedByteBuffer mappedByteBuffer = recordStoreFileChannel.map(FileChannel.MapMode.READ_ONLY, is[1], is[2]);
				mappedByteBuffer.get(buffer, offset, is[2]);
				return is[2];
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return -1;
	}

	/**
	 * Returns the size (in bytes) of the MIDlet data available in the given
	 * record.
	 * 
	 * @param recordId
	 *            the ID of the record to use in this operation
	 * 
	 * @return the size (in bytes) of the MIDlet data available in the given
	 *         record
	 * 
	 * @exception RecordStoreNotOpenException
	 *                if the record store is not open
	 * @exception InvalidRecordIDException
	 *                if the recordId is invalid
	 * @exception RecordStoreException
	 *                if a general record store exception occurs
	 */
	public int getRecordSize(int recordId) throws RecordStoreNotOpenException, InvalidRecordIDException, RecordStoreException {
		synchronized (indexData) {
			checkOpen();
			int[] is = indexData.get(recordId);
			return is[2];
		}
	}

	/**
	 * Returns the amount of space, in bytes, that the record store occupies.
	 * The size returned includes any overhead associated with the
	 * implementation, such as the data structures used to hold the state of the
	 * record store, etc.
	 * 
	 * @exception RecordStoreNotOpenException
	 *                if the record store is not open
	 * @return the size of the record store in bytes
	 */
	public int getSize() throws RecordStoreNotOpenException {
		return -1;
	}

	/**
	 * Returns the amount of additional room (in bytes) available for this
	 * record store to grow. Note that this is not necessarily the amount of
	 * extra MIDlet-level data which can be stored, as implementations may store
	 * additional data structures with each record to support integration with
	 * native applications, synchronization, etc.
	 * 
	 * @exception RecordStoreNotOpenException
	 *                if the record store is not open
	 * 
	 * @return the amount of additional room (in bytes) available for this
	 *         record store to grow
	 */

	private int getSizeAvailableIn() throws RecordStoreNotOpenException {
		checkOpen();
		int total = getFullSize();
		for (int[] is : indexData) {
			total -= is[2];
		}
		return total;
	}

	public int getSizeAvailable() throws RecordStoreNotOpenException {
		synchronized (indexData) {
			return getSizeAvailableIn();
		}
	}

	/**
	 * Each time a record store is modified (by <code>addRecord</code>,
	 * <code>setRecord</code>, or <code>deleteRecord</code> methods) its
	 * <em>version</em> is incremented. This can be used by MIDlets to quickly
	 * tell if anything has been modified.
	 * 
	 * The initial version number is implementation dependent. The increment is
	 * a positive integer greater than 0. The version number increases only when
	 * the RecordStore is updated.
	 * 
	 * The increment value need not be constant and may vary with each update.
	 * 
	 * @return the current record store version
	 * @exception RecordStoreNotOpenException
	 *                if the record store is not open
	 */
	public int getVersion() throws RecordStoreNotOpenException {
		return version;
	}

	/**
	 * Removes the specified RecordListener. If the specified listener is not
	 * registered, this method does nothing.
	 * 
	 * @param listener
	 *            the RecordChangedListener
	 * @see #addRecordListener
	 */
	public void removeRecordListener(RecordListener listener) {
		recordListeners.remove(listener);
	}

	/**
	 * Changes the access mode for this RecordStore. The authorization mode
	 * choices are:
	 * 
	 * <ul>
	 * <li><code>AUTHMODE_PRIVATE</code> - Only allows the MIDlet suite that
	 * created the RecordStore to access it. This case behaves identically to
	 * <code>openRecordStore(recordStoreName,
	 *          createIfNecessary)</code>.</li>
	 * <li><code>AUTHMODE_ANY</code> - Allows any MIDlet to access the
	 * RecordStore. Note that this makes your recordStore accessible by any
	 * other MIDlet on the device. This could have privacy and security issues
	 * depending on the data being shared. Please use carefully.</li>
	 * </ul>
	 * 
	 * <p>
	 * The owning MIDlet suite may always access the RecordStore and always has
	 * access to write and update the store. Only the owning MIDlet suite can
	 * change the mode of a RecordStore.
	 * </p>
	 * 
	 * @param authmode
	 *            the mode under which to check or create access. Must be one of
	 *            AUTHMODE_PRIVATE or AUTHMODE_ANY.
	 * @param writable
	 *            true if the RecordStore is to be writable by other MIDlet
	 *            suites that are granted access
	 * 
	 * @exception RecordStoreException
	 *                if a record store-related exception occurred
	 * @exception SecurityException
	 *                if this MIDlet Suite is not allowed to change the mode of
	 *                the RecordStore
	 * @exception IllegalArgumentException
	 *                if authmode is invalid
	 */
	public void setMode(int authmode, boolean writable) throws RecordStoreException {
		synchronized (indexData) {
			if (authmode != AUTHMODE_ANY && authmode != AUTHMODE_PRIVATE) {
				throw new IllegalArgumentException();
			}
			checkSecurity(CHECK_MODE_SETMODE);
			this.authmode = authmode;
			this.writable = writable;
		}
	}

	/**
	 * Sets the data in the given record to that passed in. After this method
	 * returns, a call to <code>getRecord(int recordId)</code> will return an
	 * array of numBytes size containing the data supplied here.
	 * 
	 * @param recordId
	 *            the ID of the record to use in this operation
	 * @param newData
	 *            the new data to store in the record
	 * @param offset
	 *            the index into the data buffer of the first relevant byte for
	 *            this record
	 * @param numBytes
	 *            the number of bytes of the data buffer to use for this record
	 * 
	 * @exception RecordStoreNotOpenException
	 *                if the record store is not open
	 * @exception InvalidRecordIDException
	 *                if the recordId is invalid
	 * @exception RecordStoreException
	 *                if a general record store exception occurs
	 * @exception RecordStoreFullException
	 *                if the operation cannot be completed because the record
	 *                store has no more room
	 * @exception SecurityException
	 *                if the MIDlet has read-only access to the RecordStore
	 * @see #getRecord
	 */
	public void setRecord(int recordId, byte[] newData, int offset, int numBytes) throws RecordStoreNotOpenException, InvalidRecordIDException, RecordStoreException, RecordStoreFullException {

		Log.v("setRecord", recordId + " " + recordStoreName);
		synchronized (indexData) {
			checkOpen();
			checkSecurity(CHECK_MODE_ADD_SET_DELETE);
			int recordIdIndex = getRecordIdIndex(recordId);
			if (recordIdIndex == -1) {
				throw new InvalidRecordIDException();
			}

			if (numBytes <= 0 || newData == null) {
				throw new RecordStoreException();
			}

			// wp: 2010-07-01
			// add size support more large than the original
			int[] is = indexData.get(recordIdIndex);
			int startOffset = is[1];

			if (is[2] < numBytes) {
				// enlarge if needed
				checkRecordStoreFull(numBytes);// 检测open 和 sizeAvailable
				startOffset = dataSize + dataOffset;
				try {
					MappedByteBuffer buffer = recordStoreFileChannel.map(FileChannel.MapMode.READ_WRITE, startOffset, numBytes);
					buffer.put(newData, offset, numBytes);
					is[1] = startOffset;
					is[2] = numBytes;
					dataSize += numBytes;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					throw new RecordStoreException();
				}
			} else {
				// update directly
				startOffset = is[1];
				try {
					MappedByteBuffer buffer = recordStoreFileChannel.map(FileChannel.MapMode.READ_WRITE, startOffset, numBytes);
					buffer.put(newData, offset, numBytes);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					throw new RecordStoreException();
				}
			}

			updateData();
			for (RecordListener listener : recordListeners) {
				listener.recordChanged(this, recordId);

			}
		}
	}
}
