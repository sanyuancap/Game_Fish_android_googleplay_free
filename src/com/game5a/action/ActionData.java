package com.game5a.action;

/**
 * 动作数据
 * 
 * @author xFu
 * 
 */
public class ActionData {
	/** 动作总帧数 */
	public short frameNum;
	/** 每帧ID */
	public short[] frameID;
	/** 每帧Vx */
	public short[] vX;
	/** 每帧Vy */
	public short[] vY;

	/** 是否加速 */
	public boolean bAcc;

	/**
	 * 构造函数
	 * 
	 * @param fn
	 *            动作帧数
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
