package com.game5a.action;

/**
 * ��������
 * 
 * @author xFu
 * 
 */
public class ActionData {
	/** ������֡�� */
	public short frameNum;
	/** ÿ֡ID */
	public short[] frameID;
	/** ÿ֡Vx */
	public short[] vX;
	/** ÿ֡Vy */
	public short[] vY;

	/** �Ƿ���� */
	public boolean bAcc;

	/**
	 * ���캯��
	 * 
	 * @param fn
	 *            ����֡��
	 */
	public ActionData(short fn) {
		frameNum = fn;
		frameID = new short[fn];
		vX = new short[fn];
		vY = new short[fn];

		//		stepSize = 0;
		bAcc = false;
	}
}
