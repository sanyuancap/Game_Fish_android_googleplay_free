package com.game5a.action;

import com.game5a.common.Rectangle;

/**
 * ֡����
 * 
 */
public class FrameData {
	/** ���ο��� */
	public short rectNum;
	/** ÿ�����ο��ͼƬ��� */
	public byte[] imgID;
	/** ÿ�����ο�ı�� */
	public short[] rectID;
	/** ÿ�����ο����ת���� */
	public byte[] drawType;
	/** ÿ�����ο���Խŵ����Ͻǵ�Xƫ���� */
	public short[] rectDX;
	/** ÿ�����ο���Խŵ����Ͻǵ�Yƫ���� */
	public short[] rectDY;
	/** �������� */
	public Rectangle atkRect;
	/** ������� */
	public Rectangle bodyRect;
	/** ����֡��ʶ */
	public boolean bAtkFrame;

	/** ��Ե���� */
	public Rectangle edgeRect;

	/**
	 * ���캯��
	 * 
	 * @param rn
	 *            ͼ������
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
