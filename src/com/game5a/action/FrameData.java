package com.game5a.action;

import com.game5a.common.Rectangle;

/**
 * 帧数据
 * 
 */
public class FrameData {
	/** 矩形块数 */
	public short rectNum;
	/** 每个矩形块的图片编号 */
	public byte[] imgID;
	/** 每个矩形块的编号 */
	public short[] rectID;
	/** 每个矩形块的旋转类型 */
	public byte[] drawType;
	/** 每个矩形块相对脚底左上角的X偏移量 */
	public short[] rectDX;
	/** 每个矩形块相对脚底左上角的Y偏移量 */
	public short[] rectDY;
	/** 攻击矩形 */
	public Rectangle atkRect;
	/** 身体矩形 */
	public Rectangle bodyRect;
	/** 攻击帧标识 */
	public boolean bAtkFrame;

	/** 边缘矩形 */
	public Rectangle edgeRect;

	/**
	 * 构造函数
	 * 
	 * @param rn
	 *            图块数量
	 */
	public FrameData(short rn) {
		rectNum = rn;
		imgID = new byte[rn];
		rectID = new short[rn];
		drawType = new byte[rn];
		rectDX = new short[rn];
		rectDY = new short[rn];
		atkRect = new Rectangle();
		bodyRect = new Rectangle();
		edgeRect = new Rectangle();
		bAtkFrame = false;
	}
}
