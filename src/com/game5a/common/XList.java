package com.game5a.common;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

/**
 * 列表
 * 
 * @author xFu
 * 
 */
public class XList {
	/** 竖直对齐类型: 居中 */
	public static final byte V_CENTER = 0;
	/** 竖直对齐类型: 居上 */
	public static final byte V_TOP = 1;
	/** 竖直对齐类型: 居下 */
	public static final byte V_BOTTOM = 2;

	/** 列表项数组 */
	private Object[] objects;
	/** 显示字符串数组 */
	private MovingString[] itemStrs;
	/** 列表项显示宽度 */
	private int itemWidth;
	/** 列表项显示高度 */
	private int itemHeight;
	/** 列表项显示行数 */
	private int itemShowNum;
	/** 当前显示的第一项序号 */
	private int itemShowFirst;
	/** 当前焦点项序号 */
	private int focusIndex;

	/** 字体 */
	private Font font;

	/** 是否焦点变换 */
	private boolean bFocusChanged;

	/** 列表项为空时, 默认显示内容 */
	public static final String STR_NULL = "...";

	/**
	 * 构造函数
	 * 
	 * @param os
	 *            元素数组
	 * @param iw
	 *            列表项显示宽度
	 * @param ih
	 *            列表项显示高度
	 * @param sn
	 *            显示个数
	 * @param fn
	 *            字体
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
	 * 构造函数
	 * 
	 * @param os
	 *            元素数组
	 * @param iw
	 *            列表项显示宽度
	 * @param ih
	 *            列表项显示高度
	 * @param sn
	 *            显示个数
	 * @param fn
	 *            字图
	 * @param bAutoHeight
	 *            是否根据显示高度自动设置显示个数
	 */
	public XList(Object[] os, int iw, int ih, int sn, Font fn, boolean bAutoHeight) {
		this(os, iw, ih, sn, fn);
		if (bAutoHeight && itemShowNum > os.length) {
			itemShowNum = os.length;
		}
	}

	/**
	 * 获取显示宽度
	 * 
	 * @return 宽度
	 */
	public int getWidth() {
		return itemWidth;
	}

	/**
	 * 获取显示高度
	 * 
	 * @return 高度
	 */
	public int getHeight() {
		return itemHeight * itemShowNum;
	}

	/**
	 * 获取列表项显示高度
	 * 
	 * @return 每项高度
	 */
	public int getItemHeight() {
		return itemHeight;
	}

	/**
	 * 是否焦点变换
	 * 
	 * @return 是否焦点变换
	 */
	public boolean focusChanged() {
		return bFocusChanged;
	}

	/**
	 * 获取列表焦点项
	 * 
	 * @return 列表焦点项
	 */
	public Object getFocusItem() {
		return objects[focusIndex];
	}

	/**
	 * 获取列表焦点项序号
	 * 
	 * @return 列表焦点项序号
	 */
	public int getFocusIndex() {
		return focusIndex;
	}

	/**
	 * 设置焦点项序号
	 * 
	 * @param index
	 *            序号
	 */
	public void setFocusIndex(int index) {
		if (index < 0 || index >= objects.length) {
			return;
		}
		focusIndex = index;
	}

	/**
	 * 获取焦点项在当前页的排序
	 * 
	 * @return 焦点项在当前页的排序
	 */
	public int getFocusIndexInPage() {
		return (focusIndex - itemShowFirst);
	}

	/**
	 * 获取当前页的第一项序号
	 * 
	 * @return 当前页的第一项序号
	 */
	public int getFirstIndexShown() {
		return itemShowFirst;
	}

	/**
	 * 获取元素个数
	 * 
	 * @return 元素个数
	 */
	public int getItemNum() {
		return objects.length;
	}

	/**
	 * 获取指定序号的元素
	 * 
	 * @param index
	 *            指定序号
	 * @return 元素
	 */
	public Object getItemAt(int index) {
		if (index < 0 || index >= objects.length) {
			return null;
		}
		return objects[index];
	}

	/**
	 * 获取指定序号的显示内容
	 * 
	 * @param index
	 *            指定序号
	 * @return 显示内容
	 */
	public MovingString getItemStrAt(int index) {
		if (index < 0 || index >= itemStrs.length) {
			return null;
		}
		return itemStrs[index];
	}

	/**
	 * 获取当前显示指定排序位置的元素
	 * 
	 * @param index
	 *            指定排序位置
	 * @return 元素
	 */
	public Object getItemShownAt(int index) {
		return getItemAt(itemShowFirst + index);
	}

	/**
	 * 获取当前显示的元素个数
	 * 
	 * @return 显示的元素个数
	 */
	public int getItemNumShown() {
		return itemShowNum > objects.length ? objects.length : itemShowNum;
	}

	/**
	 * 按键处理
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
	 * 循环处理
	 */
	public void cycle() {
		itemStrs[focusIndex].cycle();
	}

	/**
	 * 绘制内容
	 * 
	 * @param g
	 *            Graphics对象
	 * @param listX
	 *            列表位置X
	 * @param listY
	 *            列表位置Y
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
	 * 绘制内容
	 * 
	 * @param g
	 *            Graphics对象
	 * @param listX
	 *            列表位置X
	 * @param listY
	 *            列表位置Y
	 * @param format
	 *            格式参数 format[0]-字体颜色, format[1]-背景颜色, format[2]-绘制类型
	 * @param focusFormat
	 *            焦点格式参数
	 * @param vType
	 *            竖直对齐类型
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
