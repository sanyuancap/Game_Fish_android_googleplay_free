package com.game5a.common;

import java.util.Vector;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

/**
 * 颜色滚动文本
 * 
 */
public class ScrollColorText {
	/** 内容 */
	Vector content = new Vector();

	/** 宽度 */
	int width;
	/** 高度 */
	int height;
	/** 行高 */
	int lineHeight;
	/** 绘制偏移量 */
	int drawDy;
	/** 滚动步长 */
	int scrollStep;

	/** 字图 */
	Font font;

	/** 是否滚动 */
	boolean bScroll;

	/** 停留时间 */
	private int stayTimes;
	/** 停留最大时间 */
	public static final int STAY_TIMES_MAX = 10;

	/**
	 * 构造函数
	 * 
	 * @param w
	 *            宽度
	 * @param h
	 *            高度
	 * @param lh
	 *            行高
	 * @param step
	 *            步长
	 * @param fn
	 *            字体
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
	 * 添加颜色字符串
	 * 
	 * @param color
	 *            颜色
	 * @param str
	 *            字符串
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
	 * 循环处理
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
	 * 开始滚动
	 */
	public void start() {
		bScroll = true;
	}

	/**
	 * 停止滚动
	 */
	public void stop() {
		drawDy = 0;
		stayTimes = 0;
		bScroll = false;
	}

	/**
	 * 暂停滚动
	 */
	public void pause() {
		bScroll = false;
	}

	/**
	 * 获取宽度
	 * 
	 * @return 宽度
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * 获取高度
	 * 
	 * @return 高度
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * 绘制
	 * 
	 * @param g
	 *            Graphics对象
	 * @param x
	 *            位置X
	 * @param y
	 *            位置Y
	 * @param type
	 *            类型
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
	 * 内容数量
	 * 
	 * @return 内容数量
	 */
	public int size() {
		return content.size() >> 1;
	}

	/**
	 * 清空内容
	 */
	public void removeAll() {
		content.removeAllElements();
	}
}
