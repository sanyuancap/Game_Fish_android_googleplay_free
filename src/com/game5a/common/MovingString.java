package com.game5a.common;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

/**
 * 移动字符串
 * 
 * @author xFu
 * 
 */
public class MovingString {
	/** 显示内容 */
	private String content;
	/** 移动时间 */
	private int movingTimes;
	/** 停顿时间 */
	private int stayTimes;
	/** 最大停顿时间 */
	public static final int STAY_TIMES_MAX = 5;

	/** 显示宽度 */
	private int width;
	/** 移动步长 */
	private int step;
	/** 是否移动 */
	private boolean bMove;
	/** 是否结束 */
	private boolean bEnd;

	boolean bMoveForce;

	/**
	 * 构造函数
	 * 
	 * @param str
	 *            显示内容
	 * @param drawWidth
	 *            显示宽度
	 * @param moveStep
	 *            移动步长
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
	 * 开始移动
	 */
	public void start() {
		bMove = true;
	}

	/**
	 * 停止移动
	 */
	public void stop() {
		movingTimes = 0;
		stayTimes = 0;
		bMove = false;
		bEnd = false;
	}

	/**
	 * 暂停移动
	 */
	public void pause() {
		bMove = false;
	}

	/**
	 * 是否移动结束
	 * 
	 * @return 是否移动结束
	 */
	public boolean isEnd() {
		return bEnd;
	}

	/**
	 * 设置内容
	 * 
	 * @param str
	 *            内容
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
	 * 获取内容
	 * 
	 * @return 内容
	 */
	public String getContent() {
		return content;
	}

	/**
	 * 设置位置为最左端
	 */
	public void setPositionLeft() {
		//movingTimes = -(width / step + 1);
	}

	public int getWidth() {
		return width;
	}

	/**
	 * 循环
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
	 * 绘制内容
	 * 
	 * @param g
	 *            Graphics对象
	 * @param x
	 *            位置X
	 * @param y
	 *            位置Y
	 * @param frontColor
	 *            字体颜色
	 * @param bgColor
	 *            背景颜色
	 * @param type
	 *            绘制类型
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
