package com.game5a.common;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

/**
 * �б�
 * 
 * @author xFu
 * 
 */
public class XList {
	/** ��ֱ��������: ���� */
	public static final byte V_CENTER = 0;
	/** ��ֱ��������: ���� */
	public static final byte V_TOP = 1;
	/** ��ֱ��������: ���� */
	public static final byte V_BOTTOM = 2;

	/** �б������� */
	private Object[] objects;
	/** ��ʾ�ַ������� */
	private MovingString[] itemStrs;
	/** �б�����ʾ��� */
	private int itemWidth;
	/** �б�����ʾ�߶� */
	private int itemHeight;
	/** �б�����ʾ���� */
	private int itemShowNum;
	/** ��ǰ��ʾ�ĵ�һ����� */
	private int itemShowFirst;
	/** ��ǰ��������� */
	private int focusIndex;

	/** ���� */
	private Font font;

	/** �Ƿ񽹵�任 */
	private boolean bFocusChanged;

	/** �б���Ϊ��ʱ, Ĭ����ʾ���� */
	public static final String STR_NULL = "...";

	/**
	 * ���캯��
	 * 
	 * @param os
	 *            Ԫ������
	 * @param iw
	 *            �б�����ʾ���
	 * @param ih
	 *            �б�����ʾ�߶�
	 * @param sn
	 *            ��ʾ����
	 * @param fn
	 *            ����
	 */
	public XList(Object[] os, int iw, int ih, int sn, Font fn) {
		objects = os;
		itemWidth = iw;
		itemHeight = ih;
		itemShowNum = sn;
		font = fn;
		itemShowFirst = 0;
		focusIndex = 0;

		itemStrs = new MovingString[objects.length];

		for (int i = 0; i < itemStrs.length; i++) {
			if (objects[i] != null) {
				itemStrs[i] = new MovingString(objects[i].toString(), itemWidth, 2);
			} else {
				itemStrs[i] = new MovingString(STR_NULL, itemWidth, 2);
			}
		}
		itemStrs[focusIndex].start();
	}

	/**
	 * ���캯��
	 * 
	 * @param os
	 *            Ԫ������
	 * @param iw
	 *            �б�����ʾ���
	 * @param ih
	 *            �б�����ʾ�߶�
	 * @param sn
	 *            ��ʾ����
	 * @param fn
	 *            ��ͼ
	 * @param bAutoHeight
	 *            �Ƿ������ʾ�߶��Զ�������ʾ����
	 */
	public XList(Object[] os, int iw, int ih, int sn, Font fn, boolean bAutoHeight) {
		this(os, iw, ih, sn, fn);
		if (bAutoHeight && itemShowNum > os.length) {
			itemShowNum = os.length;
		}
	}

	/**
	 * ��ȡ��ʾ���
	 * 
	 * @return ���
	 */
	public int getWidth() {
		return itemWidth;
	}

	/**
	 * ��ȡ��ʾ�߶�
	 * 
	 * @return �߶�
	 */
	public int getHeight() {
		return itemHeight * itemShowNum;
	}

	/**
	 * ��ȡ�б�����ʾ�߶�
	 * 
	 * @return ÿ��߶�
	 */
	public int getItemHeight() {
		return itemHeight;
	}

	/**
	 * �Ƿ񽹵�任
	 * 
	 * @return �Ƿ񽹵�任
	 */
	public boolean focusChanged() {
		return bFocusChanged;
	}

	/**
	 * ��ȡ�б�����
	 * 
	 * @return �б�����
	 */
	public Object getFocusItem() {
		return objects[focusIndex];
	}

	/**
	 * ��ȡ�б��������
	 * 
	 * @return �б��������
	 */
	public int getFocusIndex() {
		return focusIndex;
	}

	/**
	 * ���ý��������
	 * 
	 * @param index
	 *            ���
	 */
	public void setFocusIndex(int index) {
		if (index < 0 || index >= objects.length) {
			return;
		}
		focusIndex = index;
	}

	/**
	 * ��ȡ�������ڵ�ǰҳ������
	 * 
	 * @return �������ڵ�ǰҳ������
	 */
	public int getFocusIndexInPage() {
		return (focusIndex - itemShowFirst);
	}

	/**
	 * ��ȡ��ǰҳ�ĵ�һ�����
	 * 
	 * @return ��ǰҳ�ĵ�һ�����
	 */
	public int getFirstIndexShown() {
		return itemShowFirst;
	}

	/**
	 * ��ȡԪ�ظ���
	 * 
	 * @return Ԫ�ظ���
	 */
	public int getItemNum() {
		return objects.length;
	}

	/**
	 * ��ȡָ����ŵ�Ԫ��
	 * 
	 * @param index
	 *            ָ�����
	 * @return Ԫ��
	 */
	public Object getItemAt(int index) {
		if (index < 0 || index >= objects.length) {
			return null;
		}
		return objects[index];
	}

	/**
	 * ��ȡָ����ŵ���ʾ����
	 * 
	 * @param index
	 *            ָ�����
	 * @return ��ʾ����
	 */
	public MovingString getItemStrAt(int index) {
		if (index < 0 || index >= itemStrs.length) {
			return null;
		}
		return itemStrs[index];
	}

	/**
	 * ��ȡ��ǰ��ʾָ������λ�õ�Ԫ��
	 * 
	 * @param index
	 *            ָ������λ��
	 * @return Ԫ��
	 */
	public Object getItemShownAt(int index) {
		return getItemAt(itemShowFirst + index);
	}

	/**
	 * ��ȡ��ǰ��ʾ��Ԫ�ظ���
	 * 
	 * @return ��ʾ��Ԫ�ظ���
	 */
	public int getItemNumShown() {
		return itemShowNum > objects.length ? objects.length : itemShowNum;
	}

	/**
	 * ��������
	 */
	public void handleKey() {
		bFocusChanged = false;
		if (Common.isAnyKeyPressed()) {
			int lastFocused = focusIndex;
			if (Common.isKeyPressed(Common.UP_PRESSED, true) || Common.isKeyPressed(Common.KEY_NUM2_PRESSED, true)) {
				focusIndex--;
				if (focusIndex < 0) {
					focusIndex = 0;
				}
				if (itemShowFirst > focusIndex) {
					itemShowFirst = focusIndex;
				}
			} else if (Common.isKeyPressed(Common.DOWN_PRESSED, true) || Common.isKeyPressed(Common.KEY_NUM8_PRESSED, true)) {
				focusIndex++;
				if (focusIndex >= objects.length) {
					focusIndex = objects.length - 1;
				}
				if (itemShowFirst < focusIndex - itemShowNum + 1) {
					itemShowFirst = focusIndex - itemShowNum + 1;
				}
			}

			if (lastFocused != focusIndex) {
				itemStrs[lastFocused].stop();
				itemStrs[focusIndex].start();
				bFocusChanged = true;
			}
		}
	}

	/**
	 * ѭ������
	 */
	public void cycle() {
		itemStrs[focusIndex].cycle();
	}

	/**
	 * ��������
	 * 
	 * @param g
	 *            Graphics����
	 * @param listX
	 *            �б�λ��X
	 * @param listY
	 *            �б�λ��Y
	 */
	public void draw(Graphics g, int listX, int listY) {
		g.setFont(font);
		for (int i = 0; i < itemShowNum; i++) {
			int index = i + itemShowFirst;
			if (index >= objects.length) {
				break;
			}
			if (focusIndex == index) {
				itemStrs[index].draw(g, listX, listY + itemHeight * i, 0xffffff, 0x0000ff, Tool.TYPE_EDGE);
			} else {
				itemStrs[index].draw(g, listX, listY + itemHeight * i, 0xffffff, -1, Tool.TYPE_EDGE);
			}
		}
	}

	/**
	 * ��������
	 * 
	 * @param g
	 *            Graphics����
	 * @param listX
	 *            �б�λ��X
	 * @param listY
	 *            �б�λ��Y
	 * @param format
	 *            ��ʽ���� format[0]-������ɫ, format[1]-������ɫ, format[2]-��������
	 * @param focusFormat
	 *            �����ʽ����
	 * @param vType
	 *            ��ֱ��������
	 */
	public void draw(Graphics g, int listX, int listY, int[] format, int[] focusFormat, byte vType) {
		g.setFont(font);
		int dh = 0;
		if (vType == V_CENTER) {
			dh = (itemHeight - font.getHeight()) >> 1;
		} else if (vType == V_BOTTOM) {
			dh = itemHeight - font.getHeight();
		}
		for (int i = 0; i < itemShowNum; i++) {
			int index = i + itemShowFirst;
			if (index >= objects.length) {
				break;
			}
			if (focusIndex == index) {
				itemStrs[index].draw(g, listX, listY + itemHeight * i + dh, focusFormat[0], focusFormat[1], (byte) focusFormat[2]);
			} else {
				itemStrs[index].draw(g, listX, listY + itemHeight * i + dh, format[0], format[1], (byte) format[2]);
				//g.setColor(0);
				//g.drawRect(listX, listY + itemHeight*i, 200, font.getHeight()-1);
			}
		}
	}
}
