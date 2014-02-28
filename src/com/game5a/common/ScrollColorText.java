package com.game5a.common;

import java.util.Vector;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

/**
 * ��ɫ�����ı�
 * 
 */
public class ScrollColorText {
	/** ���� */
	Vector content = new Vector();

	/** ��� */
	int width;
	/** �߶� */
	int height;
	/** �и� */
	int lineHeight;
	/** ����ƫ���� */
	int drawDy;
	/** �������� */
	int scrollStep;

	/** ��ͼ */
	Font font;

	/** �Ƿ���� */
	boolean bScroll;

	/** ͣ��ʱ�� */
	private int stayTimes;
	/** ͣ�����ʱ�� */
	public static final int STAY_TIMES_MAX = 10;

	/**
	 * ���캯��
	 * 
	 * @param w
	 *            ���
	 * @param h
	 *            �߶�
	 * @param lh
	 *            �и�
	 * @param step
	 *            ����
	 * @param fn
	 *            ����
	 */
	public ScrollColorText(int w, int h, int lh, int step, Font fn) {
		width = w;
		height = h;
		lineHeight = lh;
		scrollStep = step;
		font = fn;
		drawDy = 0;
		stayTimes = 0;
	}

	/**
	 * �����ɫ�ַ���
	 * 
	 * @param color
	 *            ��ɫ
	 * @param str
	 *            �ַ���
	 */
	public void addColorString(int[] color, String str) {
		String[] strs = Tool.getStringArray(str, width, font);
		for (int i = 0; i < strs.length; i++) {
			content.addElement(color);
			content.addElement(strs[i]);
		}
		if (lineHeight * size() > height) {
			bScroll = true;
		}
	}

	/**
	 * ѭ������
	 */
	public void cycle() {
		if (bScroll) {
			stayTimes++;
			if (stayTimes > STAY_TIMES_MAX) {
				drawDy -= scrollStep;
				if (drawDy <= -lineHeight * size()) {
					drawDy = height;
				}
			}
		}
	}

	/**
	 * ��ʼ����
	 */
	public void start() {
		bScroll = true;
	}

	/**
	 * ֹͣ����
	 */
	public void stop() {
		drawDy = 0;
		stayTimes = 0;
		bScroll = false;
	}

	/**
	 * ��ͣ����
	 */
	public void pause() {
		bScroll = false;
	}

	/**
	 * ��ȡ���
	 * 
	 * @return ���
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * ��ȡ�߶�
	 * 
	 * @return �߶�
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * ����
	 * 
	 * @param g
	 *            Graphics����
	 * @param x
	 *            λ��X
	 * @param y
	 *            λ��Y
	 * @param type
	 *            ����
	 */
	public void draw(Graphics g, int x, int y, int type) {
		g.setClip(x, y, width, height);
		g.setFont(font);
		int startIndex = -drawDy / lineHeight;
		if (startIndex < 0) {
			startIndex = 0;
		}
		int endIndex = (height - drawDy) / lineHeight + 1;
		if (endIndex > size() - 1) {
			endIndex = size() - 1;
		}

		for (int i = startIndex; i <= endIndex; i++) {
			int[] color = (int[]) content.elementAt(i * 2);
			Tool.drawString(g, (String) content.elementAt(i * 2 + 1), x, y + lineHeight * i + drawDy, color[0], color[1], type);
		}
	}

	/**
	 * ��������
	 * 
	 * @return ��������
	 */
	public int size() {
		return content.size() >> 1;
	}

	/**
	 * �������
	 */
	public void removeAll() {
		content.removeAllElements();
	}
}
