package com.game5a.common;

/**
 * 矩形类
 * 
 */
public class Rectangle {
	/** 左上角X */
	public int x;
	/** 左上角Y */
	public int y;
	/** 宽度 */
	public int width;
	/** 高度 */
	public int height;

	public Rectangle() {
	}

	/**
	 * 构造函数
	 * 
	 * @param xx
	 *            左上角X
	 * @param yy
	 *            左上角Y
	 * @param ww
	 *            宽度
	 * @param hh
	 *            高度
	 */
	public Rectangle(int xx, int yy, int ww, int hh) {
		x = xx;
		y = yy;
		width = ww;
		height = hh;
	}

	/**
	 * 构造函数
	 * 
	 * @param rect
	 *            矩形对象
	 */
	public Rectangle(Rectangle rect) {
		x = rect.x;
		y = rect.y;
		width = rect.width;
		height = rect.height;
	}

}
