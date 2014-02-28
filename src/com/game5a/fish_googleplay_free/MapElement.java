package com.game5a.fish_googleplay_free;

import javax.microedition.lcdui.Graphics;

/**
 * ��ͼԪ����, ���鳡����ս�������ĵ�ͼԪ���ܸ���, ��ͼԪ���ڻ��Ƶ�ʱ����Ҫ����Y������
 * 
 */
public abstract class MapElement {
	/** ���� */
	public byte type;
	/** ����: NPC */
	public static final byte TYPE_NPC = 0;
	/** ����: ���� */
	public static final byte TYPE_SCENERY = 1;
	/** ����: ���� */
	public static final byte TYPE_ROLE = 2;
	/** ����: ���� */
	public static final byte TYPE_ENEMY = 3;
	/** ����: ���� */
	public static final byte TYPE_PARTICLE = 4;

	/** ����: �� */
	public static final byte DIR_UP = 0;
	/** ����: �� */
	public static final byte DIR_DOWN = 1;
	/** ����: �� */
	public static final byte DIR_LEFT = 2;
	/** ����: �� */
	public static final byte DIR_RIGHT = 3;

	/** ����: ���� */
	public static final byte DIR_RIGHT_UP = 4;
	/** ����: ���� */
	public static final byte DIR_RIGHT_DOWN = 5;
	/** ����: ���� */
	public static final byte DIR_LEFT_DOWN = 6;
	/** ����: ���� */
	public static final byte DIR_LEFT_UP = 7;

	/** ID, Ψһ��ʶ */
	public int ID;

	/** ��ͼX������ */
	public int mapX;
	/** ��ͼY������ */
	public int mapY;
	/** �ŵ׿�, ���ڵ�ͼԪ����ײ��� */
	public int footWidth;
	/** �ŵ׸�, ���ڵ�ͼԪ����ײ��� */
	public int footHeight;
	/** ��ǰ���� */
	public byte curDir;

	/** �Ƿ���ʧ, ��ʧ������ʾ */
	public boolean bDisappear;
	/** �Ƿ����ͼ��ײ */
	public boolean bCheckMapBlock;
	/** �Ƿ����� */
	public boolean bMirror;

	/**
	 * ��ȡ�෴����
	 * 
	 * @param dir
	 *            ����
	 * @return �෴����
	 */
	public static byte getOpDir(byte dir) {
		switch (dir) {
			case DIR_UP :
				return DIR_DOWN;
			case DIR_DOWN :
				return DIR_UP;
			case DIR_LEFT :
				return DIR_RIGHT;
			case DIR_RIGHT :
				return DIR_LEFT;

			case DIR_RIGHT_UP :
				return DIR_LEFT_DOWN;
			case DIR_RIGHT_DOWN :
				return DIR_LEFT_UP;
			case DIR_LEFT_DOWN :
				return DIR_RIGHT_UP;
			case DIR_LEFT_UP :
				return DIR_RIGHT_DOWN;
			default :
				return DIR_UP;
		}
	}

	/**
	 * ѭ������
	 */
	public abstract void cycle();

	/**
	 * ����
	 * 
	 * @param g
	 *            Graphic����
	 * @param viewMapX
	 *            ������Ļ�ڵ�ͼ�ϵ�λ��X
	 * @param viewMapY
	 *            ������Ļ�ڵ�ͼ�ϵ�λ��Y
	 * @param dx
	 *            ƫ����X
	 * @param dy
	 *            ƫ����Y
	 */
	public abstract void draw(Graphics g, int viewMapX, int viewMapY, int dx, int dy);

	/**
	 * �Ƿ�����Ļ��
	 * 
	 * @param viewMapX
	 *            ������Ļ�ڵ�ͼ�ϵ�λ��X
	 * @param viewMapY
	 *            ������Ļ�ڵ�ͼ�ϵ�λ��Y
	 * @param viewWidth
	 *            ������Ļ��
	 * @param viewHeight
	 *            ������Ļ��
	 * @return �Ƿ�����Ļ��
	 */
	public abstract boolean isInScreen(int viewMapX, int viewMapY, int viewWidth, int viewHeight);
}
