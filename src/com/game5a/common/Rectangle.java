package com.game5a.common;

/**
 * ������
 * 
 */
public class Rectangle {
	/** ���Ͻ�X */
	public int x;
	/** ���Ͻ�Y */
	public int y;
	/** ��� */
	public int width;
	/** �߶� */
	public int height;

	public Rectangle() {
	}

	/**
	 * ���캯��
	 * 
	 * @param xx
	 *            ���Ͻ�X
	 * @param yy
	 *            ���Ͻ�Y
	 * @param ww
	 *            ���
	 * @param hh
	 *            �߶�
	 */
	public Rectangle(int xx, int yy, int ww, int hh) {
		x = xx;
		y = yy;
		width = ww;
		height = hh;
	}

	/**
	 * ���캯��
	 * 
	 * @param rect
	 *            ���ζ���
	 */
	public Rectangle(Rectangle rect) {
		x = rect.x;
		y = rect.y;
		width = rect.width;
		height = rect.height;
	}

}
