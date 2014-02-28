package com.game5a.common;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
/**
 * 滚动文本
 * 
 */
public class ScrollText {
	/** 内容 */
	String[] text;
	/** 显示宽度 */
	int width;
	/** 显示高度 */
	int height;
	/** 行高 */
	public int lineHeight;
	/** 显示偏移量Y */
	int drawDy;
	/** 滚动步长 */
	int scrollStep;
	/** 字体 */
	Font font;
	/** 是否滚动 */
	boolean bScroll;
	/** 停留时间 */
	private int stayTimes;
	/** 最大停留时间 */
	public static final int STAY_TIMES_MAX = 10;
	/**
	 * 构造函数
	 * 
	 * @param str
	 *            内容
	 * @param w
	 *            显示宽度
	 * @param h
	 *            显示高度
	 * @param lh
	 *            行高
	 * @param step
	 *            滚动步长
	 * @param fn
	 *            字体
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
	 * 循环处理
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
	 * 半循环处理，播放一遍
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
	 * 获取显示宽度
	 * 
	 * @return 宽度
	 */
	public int getWidth() {
		return width;
	}
	/**
	 * 获取显示高度
	 * 
	 * @return 高度
	 */
	public int getHeight() {
		return height;
	}
	public int getTotalHeight() {
		return lineHeight * text.length;
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
	 *            类型
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
