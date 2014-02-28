package com.game5a.common;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

/**
 * 页面文本
 * 
 */
public class PagesText {
	/** 设置类型: 页面高度 */
	public static final byte SET_PAGE_HEIGHT = 0;
	/** 设置类型: 页面显示行数 */
	public static final byte SET_PAGE_LINE_NUM = 1;

	/** 类型: 上下翻页 */
	public static final byte STYLE_UP_DOWN = 0;
	/** 类型: 左右翻页 */
	public static final byte STYLE_LEFT_RIGHT = 1;
	/** 类型 */
	byte style;

	/** 显示内容 */
	String[] text;

	/** 宽度 */
	int width;
	/** 高度 */
	int height;
	/** 行高 */
	int lineHeight;

	/** 字体 */
	Font font;

	/** 总行数 */
	int lineTotalNum;
	/** 每页的行数 */
	int lineNumInPage;
	/** 页数 */
	int pageNum;
	/** 页序号 */
	int pageIndex;

	/** 是否绘制箭头 */
	boolean bDrawArrow;
	/** 是否回翻 */
	boolean bRollAble = true;
	/** 是否文本结束 */
	boolean bTextEnd;

	/**
	 * 构造函数
	 * 
	 * @param str
	 *            显示内容
	 * @param w
	 *            宽度
	 * @param h
	 *            高度
	 * @param lh
	 *            行高
	 * @param fn
	 *            字体
	 * @param ty
	 *            类型
	 */
	public PagesText(String str, int w, int h, int lh, Font fn, byte ty) {
		this(str, SET_PAGE_HEIGHT, w, h, lh, fn, ty, true);
	}

	/**
	 * 构造函数
	 * 
	 * @param str
	 *            显示内容
	 * @param setType
	 *            设置类型
	 * @param w
	 *            宽度
	 * @param h
	 *            设置类型: 页面高度, 为高度 / 设置类型: 页面显示行数, 为页面显示行数
	 * @param lh
	 *            行高
	 * @param fn
	 *            字体
	 * @param ty
	 *            类型
	 * @param bAutoShowArrow
	 *            是否显示翻页箭头
	 */
	public PagesText(String str, byte setType, int w, int h, int lh, Font fn, byte ty, boolean bAutoShowArrow) {
		if (str == null) {
			return;
		}

		style = ty;
		width = w;

		lineHeight = lh;
		font = fn;

		if (setType == SET_PAGE_HEIGHT) {
			height = h;

			lineNumInPage = height / lineHeight;
			if (lineNumInPage == 0) {
				lineNumInPage = 1;
			}

			text = Tool.getStringArray(str, width, font); //width - 15
			lineTotalNum = text.length;

			if (lineTotalNum > lineNumInPage && bAutoShowArrow) {
				bDrawArrow = true;
				if (style == STYLE_UP_DOWN) {
					lineNumInPage = (height - ResManager.arImg.getHeight() * 2) / lineHeight;
					if (lineNumInPage == 0) {
						lineNumInPage = 1;
					}
				} else if (style == STYLE_LEFT_RIGHT) {
					text = Tool.getStringArray(str, width - ResManager.arImg.getHeight() * 2, font);
					lineTotalNum = text.length;
				}
			}
		} else if (setType == SET_PAGE_LINE_NUM) {
			lineNumInPage = h;
			height = lineNumInPage * lineHeight;

			text = Tool.getStringArray(str, width, font); //width - 15
			lineTotalNum = text.length;

			if (lineTotalNum > lineNumInPage && bAutoShowArrow) {
				bDrawArrow = true;
				if (style == STYLE_UP_DOWN) {
					height += ResManager.arImg.getHeight() * 2;
				} else if (style == STYLE_LEFT_RIGHT) {
					text = Tool.getStringArray(str, width - ResManager.arImg.getHeight() * 2, font);
					lineTotalNum = text.length;
				}
			}
		}

		pageNum = lineTotalNum / lineNumInPage;
		if (lineTotalNum % lineNumInPage != 0) {
			pageNum++;
		}
		pageIndex = 0;
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
	 * 是否第一页
	 * 
	 * @return 是否第一页
	 */
	public boolean isHead() {
		if (pageIndex <= 0) {
			return true;
		}
		return false;
	}

	/**
	 * 是否文本结束
	 * 
	 * @return 是否文本结束
	 */
	public boolean isEnd() {
		return bTextEnd;
	}

	/**
	 * 设置是否回翻
	 * 
	 * @param b
	 *            是否回翻
	 */
	public void setRoll(boolean b) {
		bRollAble = b;
	}

	/**
	 * 按键处理
	 */
	public void handleKey() {
		boolean nextPage = false;
		boolean prePage = false;
		if (style == STYLE_UP_DOWN) {
			if (Common.isKeyPressed(Common.DOWN_PRESSED, true) || Common.isKeyPressed(Common.KEY_NUM8_PRESSED, true)) {
				nextPage = true;
			} else if (Common.isKeyPressed(Common.UP_PRESSED, true) || Common.isKeyPressed(Common.KEY_NUM2_PRESSED, true)) {
				prePage = true;
			}
		} else {
			if (Common.isKeyPressed(Common.RIGHT_PRESSED, true) || Common.isKeyPressed(Common.KEY_NUM6_PRESSED, true)) {
				nextPage = true;
			} else if (Common.isKeyPressed(Common.LEFT_PRESSED, true) || Common.isKeyPressed(Common.KEY_NUM4_PRESSED, true)) {
				prePage = true;
			}
		}

		bTextEnd = false;

		if (nextPage) {
			nextPage();
		} else if (prePage) {
			prePage();
		}

		//		if (nextPage) {
		//			pageIndex++;
		//			if (pageIndex >= pageNum) {
		//				if (bRollAble) {
		//					pageIndex = 0;
		//				} else {
		//					pageIndex = pageNum - 1;
		//					bTextEnd = true;
		//				}
		//			}
		//		} else if (prePage) {
		//			pageIndex--;
		//			if (pageIndex < 0) {
		//				if (bRollAble) {
		//					pageIndex = pageNum - 1;
		//				} else {
		//					pageIndex = 0;
		//				}
		//			}
		//		}
	}

	public void nextPage() {
		pageIndex++;
		if (pageIndex >= pageNum) {
			if (bRollAble) {
				pageIndex = 0;
			} else {
				pageIndex = pageNum - 1;
				bTextEnd = true;
			}
		}
	}

	public void prePage() {
		pageIndex--;
		if (pageIndex < 0) {
			if (bRollAble) {
				pageIndex = pageNum - 1;
			} else {
				pageIndex = 0;
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
		g.setClip(x, y, width, height);
		g.setFont(font);
		int startIndex = pageIndex * lineNumInPage;

		int tx = x;
		int ty = y;
		if (bDrawArrow) {
			if (style == STYLE_UP_DOWN) {
				ty += ResManager.arImg.getHeight();
				int ax = x + (width - ResManager.arImg.getWidth()) / 2;
				Tool.drawImage(g, ResManager.arImg, ax, y, Tool.TRANS_MIRROR_ROT180);
				Tool.drawImage(g, ResManager.arImg, ax, y + height - ResManager.arImg.getHeight(), Tool.TRANS_NONE);
			} else {
				tx += ResManager.arImg.getHeight();
				int ay = y + (height - ResManager.arImg.getWidth()) / 2;
				Tool.drawImage(g, ResManager.arImg, x, ay, Tool.TRANS_ROT90);
				Tool.drawImage(g, ResManager.arImg, x + width - ResManager.arImg.getHeight(), ay, Tool.TRANS_ROT270);
			}
			Common.setUIClip(g);
		}

		for (int i = 0; i < lineNumInPage; i++) {
			if (startIndex + i >= lineTotalNum) {
				break;
			}
			Tool.drawString(g, text[startIndex + i], tx, ty + lineHeight * i, frontColor, bgColor, type);
		}
	}
}
