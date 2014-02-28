package com.game5a.common;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
/**
 * �����ı�
 * 
 */
public class ScrollText {
	/** ���� */
	String[] text;
	/** ��ʾ��� */
	int width;
	/** ��ʾ�߶� */
	int height;
	/** �и� */
	public int lineHeight;
	/** ��ʾƫ����Y */
	int drawDy;
	/** �������� */
	int scrollStep;
	/** ���� */
	Font font;
	/** �Ƿ���� */
	boolean bScroll;
	/** ͣ��ʱ�� */
	private int stayTimes;
	/** ���ͣ��ʱ�� */
	public static final int STAY_TIMES_MAX = 10;
	/**
	 * ���캯��
	 * 
	 * @param str
	 *            ����
	 * @param w
	 *            ��ʾ���
	 * @param h
	 *            ��ʾ�߶�
	 * @param lh
	 *            �и�
	 * @param step
	 *            ��������
	 * @param fn
	 *            ����
	 */
	public ScrollText(String str, int w, int h, int lh, int step, Font fn) {
		if (str == null) {
			return;
		}
		width = w;
		height = h;
		lineHeight = lh;
		scrollStep = step;
		font = fn;
		text = Tool.getStringArray(str, width - 5, font);
		drawDy = 0;
		stayTimes = 0;
		if (lineHeight * text.length > height) {
			bScroll = true;
			// drawDy = lineHeight;
		}
	}
	public String getTextString() {
		return text[0].toString();
	}
	public int getLineNum() {
		return text.length;
	}
	public void setLineHeight(int lineHeight) {
		this.lineHeight = lineHeight;
		if (lineHeight * text.length > height) {
			bScroll = true;
			// drawDy = lineHeight;
		}
	}
	public void setDrawY() {
		drawDy = height;
	}
	/**
	 * ѭ������
	 */
	public void cycle() {
		if (bScroll) {
			stayTimes++;
			if (stayTimes > STAY_TIMES_MAX) {
				drawDy -= scrollStep;
				if (drawDy <= -lineHeight * text.length) {
					drawDy = height;
				}
			}
		}
	}
	/**
	 * ��ѭ����������һ��
	 */
	public void halfCycle() {
		if (bScroll) {
			stayTimes++;
			if (stayTimes > STAY_TIMES_MAX) {
				drawDy -= scrollStep;
				if (drawDy <= -lineHeight * (text.length - height / lineHeight)) {
					bScroll = false;
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
	 * ��ȡ��ʾ���
	 * 
	 * @return ���
	 */
	public int getWidth() {
		return width;
	}
	/**
	 * ��ȡ��ʾ�߶�
	 * 
	 * @return �߶�
	 */
	public int getHeight() {
		return height;
	}
	public int getTotalHeight() {
		return lineHeight * text.length;
	}
	/**
	 * ��������
	 * 
	 * @param g
	 *            Graphics����
	 * @param x
	 *            λ��X
	 * @param y
	 *            λ��Y
	 * @param frontColor
	 *            ������ɫ
	 * @param bgColor
	 *            ������ɫ
	 * @param type
	 *            ����
	 */
	public void draw(Graphics g, int x, int y, int frontColor, int bgColor,
			byte type) {
		g.setClip(x, y, width, height);
		g.setFont(font);
		int startIndex = -drawDy / lineHeight;
		if (startIndex < 0) {
			startIndex = 0;
		}
		int endIndex = (height - drawDy) / lineHeight + 1;
		if (endIndex > text.length - 1) {
			endIndex = text.length - 1;
		}
		for (int i = startIndex; i <= endIndex; i++) {
			Tool.drawString(g, text[i], x + 1, y + lineHeight * i + drawDy,
					frontColor, bgColor, type);
		}
	}
}
