package com.game5a.common;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

/**
 * �ƶ��ַ���
 * 
 * @author xFu
 * 
 */
public class MovingString {
	/** ��ʾ���� */
	private String content;
	/** �ƶ�ʱ�� */
	private int movingTimes;
	/** ͣ��ʱ�� */
	private int stayTimes;
	/** ���ͣ��ʱ�� */
	public static final int STAY_TIMES_MAX = 5;

	/** ��ʾ��� */
	private int width;
	/** �ƶ����� */
	private int step;
	/** �Ƿ��ƶ� */
	private boolean bMove;
	/** �Ƿ���� */
	private boolean bEnd;

	boolean bMoveForce;

	/**
	 * ���캯��
	 * 
	 * @param str
	 *            ��ʾ����
	 * @param drawWidth
	 *            ��ʾ���
	 * @param moveStep
	 *            �ƶ�����
	 */
	public MovingString(String str, int drawWidth, int moveStep) {
		content = str;
		movingTimes = 0;
		stayTimes = 0;
		width = drawWidth;
		step = moveStep;
		bMove = false;
		bEnd = false;
		bMoveForce = false;
	}

	/**
	 * ��ʼ�ƶ�
	 */
	public void start() {
		bMove = true;
	}

	/**
	 * ֹͣ�ƶ�
	 */
	public void stop() {
		movingTimes = 0;
		stayTimes = 0;
		bMove = false;
		bEnd = false;
	}

	/**
	 * ��ͣ�ƶ�
	 */
	public void pause() {
		bMove = false;
	}

	/**
	 * �Ƿ��ƶ�����
	 * 
	 * @return �Ƿ��ƶ�����
	 */
	public boolean isEnd() {
		return bEnd;
	}

	/**
	 * ��������
	 * 
	 * @param str
	 *            ����
	 */
	public void setContent(String str) {
		content = str;
		movingTimes = 0;
		bMove = false;
		bEnd = false;
	}

	public void setFromRight() {
		movingTimes = -(width / step + 1);
		bMoveForce = true;
	}

	/**
	 * ��ȡ����
	 * 
	 * @return ����
	 */
	public String getContent() {
		return content;
	}

	/**
	 * ����λ��Ϊ�����
	 */
	public void setPositionLeft() {
		//movingTimes = -(width / step + 1);
	}

	public int getWidth() {
		return width;
	}

	/**
	 * ѭ��
	 */
	public void cycle() {
		if (content == null) {
			return;
		}
		if (bMove) {
			stayTimes++;

			if (stayTimes > STAY_TIMES_MAX) {
				movingTimes++;
			}
		}
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
	 *            ��������
	 */
	public void draw(Graphics g, int x, int y, int frontColor, int bgColor, byte type) {
		if (content == null) {
			return;
		}
		Font font = g.getFont();
		g.setClip(x, y, width, font.getHeight());
		int sw = font.stringWidth(content);
		if (!bMoveForce && sw <= width) {
			Tool.drawString(g, content, x, y, frontColor, bgColor, type);
		} else {
			int dx = movingTimes * step;
			if (dx >= sw) {
				bEnd = true;
				movingTimes = -(width / step + 1);
			}
			Tool.drawString(g, content, x - dx, y, frontColor, bgColor, type);
		}
	}
}
