package com.game5a.common;

import java.util.Hashtable;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public class Common {
	//#if (KeyCodeType == NokiaE62)
	//# public static final int KEY_NUM0 = 109;
	//# public static final int KEY_NUM1 = 114;
	//# public static final int KEY_NUM2 = 116;
	//# public static final int KEY_NUM3 = 121;
	//# public static final int KEY_NUM4 = 102;
	//# public static final int KEY_NUM5 = 103;
	//# public static final int KEY_NUM6 = 104;
	//# public static final int KEY_NUM7 = 118;
	//# public static final int KEY_NUM8 = 98;
	//# public static final int KEY_NUM9 = 110;
	//# public static final int KEY_STAR = 117;
	//# public static final int KEY_POUND = 106;
	//#else	
	public static final int KEY_NUM0 = 48;
	public static final int KEY_NUM1 = 49;
	public static final int KEY_NUM2 = 50;
	public static final int KEY_NUM3 = 51;
	public static final int KEY_NUM4 = 52;
	public static final int KEY_NUM5 = 53;
	public static final int KEY_NUM6 = 54;
	public static final int KEY_NUM7 = 55;
	public static final int KEY_NUM8 = 56;
	public static final int KEY_NUM9 = 57;
	public static final int KEY_STAR = 42;
	public static final int KEY_POUND = 35;
	//#endif

	//	 機型鍵值
	//#if (KeyCodeType == MotorolaE2)
	//# public static final int KEY_UP = 1;
	//# public static final int KEY_DOWN = 2;
	//# public static final int KEY_LEFT = 3;
	//# public static final int KEY_RIGHT = 4;
	//# public static final int KEY_FIRE = 5;
	//# public static final int KEY_LEFT_SOFT = 21;
	//# public static final int KEY_RIGHT_SOFT = 22;
	//#elif (KeyCodeType == Motorola) || (KeyCodeType == MotorolaK1) || (KeyCodeType == SHARP-T71)
	//# public static final int KEY_UP = 1;
	//# public static final int KEY_DOWN = 6;
	//# public static final int KEY_LEFT = 2;
	//# public static final int KEY_RIGHT = 5;
	//# public static final int KEY_FIRE = 20;
	//# public static final int KEY_LEFT_SOFT = 21;
	//# public static final int KEY_RIGHT_SOFT = 22;
	//#elif KeyCodeType == MotorolaE6
	//# public static final int KEY_UP = 1;//E680
	//# public static final int KEY_DOWN = 2;
	//# public static final int KEY_LEFT = 3;
	//# public static final int KEY_RIGHT = 4;
	//# public static final int KEY_FIRE = 5;
	//# public static final int KEY_LEFT_SOFT = 6;
	//# public static final int KEY_RIGHT_SOFT = 7;
	//#elif (KeyCodeType == WINIIF860)
	//# public static final int KEY_UP = 1;
	//# public static final int KEY_DOWN = 2;
	//# public static final int KEY_LEFT = 3;
	//# public static final int KEY_RIGHT = 4;
	//# public static final int KEY_FIRE = 5;
	//# public static final int KEY_LEFT_SOFT = 7;
	//# public static final int KEY_RIGHT_SOFT = 6;
	//#elif (KeyCodeType == LGKG800) 
	//# public static final int KEY_UP = 1;
	//# public static final int KEY_DOWN = 2;
	//# public static final int KEY_LEFT = 3;
	//# public static final int KEY_RIGHT = 4;
	//# public static final int KEY_FIRE = 5;
	//# public static final int KEY_LEFT_SOFT = 202;
	//# public static final int KEY_RIGHT_SOFT = 203;
	//#else
	public static final int KEY_UP = 1;//N7610 N73 K700  S700
	public static final int KEY_DOWN = 2;
	public static final int KEY_LEFT = 3;
	public static final int KEY_RIGHT = 4;
	public static final int KEY_FIRE = 5;
	public static final int KEY_LEFT_SOFT = 6;
	public static final int KEY_RIGHT_SOFT = 7;
	//#endif

	// define constant to handle game key states
	public static final byte UP_PRESSED = 0;
	public static final byte DOWN_PRESSED = 1;
	public static final byte LEFT_PRESSED = 2;
	public static final byte RIGHT_PRESSED = 3;
	public static final byte FIRE_PRESSED = 4;
	public static final byte GAME_A_PRESSED = 5;
	public static final byte GAME_B_PRESSED = 6;
	public static final byte GAME_C_PRESSED = 7;
	public static final byte GAME_D_PRESSED = 8;

	// define constant to handle soft key states
	public static final byte SOFT_FIRST_PRESSED = 9;
	public static final byte SOFT_LAST_PRESSED = 10;

	// define constant to handle number key states
	public static final byte KEY_NUM0_PRESSED = 11;
	public static final byte KEY_NUM1_PRESSED = 12;
	public static final byte KEY_NUM2_PRESSED = 13;
	public static final byte KEY_NUM3_PRESSED = 14;
	public static final byte KEY_NUM4_PRESSED = 15;
	public static final byte KEY_NUM5_PRESSED = 16;
	public static final byte KEY_NUM6_PRESSED = 17;
	public static final byte KEY_NUM7_PRESSED = 18;
	public static final byte KEY_NUM8_PRESSED = 19;
	public static final byte KEY_NUM9_PRESSED = 20;
	public static final byte KEY_POUND_PRESSED = 21;
	public static final byte KEY_STAR_PRESSED = 22;

	public static final byte BUTTON_MENU_PRESSED = SOFT_FIRST_PRESSED;
	public static final byte BUTTON_OK_PRESSED = SOFT_FIRST_PRESSED;
	public static final byte BUTTON_BACK_PRESSED = SOFT_LAST_PRESSED;

	public static final byte KEY_LEFT_SOFT_INIT = -KEY_LEFT_SOFT;
	public static final byte KEY_RIGHT_SOFT_INIT = -KEY_RIGHT_SOFT;

	/*
	 * // define constant to handle menu&ok&back key states //#if KeyCodeType ==
	 * Motorola || KeyCodeType == MotorolaE2 //# public static final byte
	 * BUTTON_MENU_PRESSED = SOFT_FIRST_PRESSED; //# public static final byte
	 * BUTTON_OK_PRESSED = SOFT_LAST_PRESSED; //# public static final byte
	 * BUTTON_BACK_PRESSED = SOFT_FIRST_PRESSED; //#elif KeyCodeType ==
	 * SonyEricsson //# public static final byte BUTTON_MENU_PRESSED =
	 * SOFT_LAST_PRESSED; //# public static final byte BUTTON_OK_PRESSED =
	 * SOFT_FIRST_PRESSED; //# public static final byte BUTTON_BACK_PRESSED =
	 * SOFT_LAST_PRESSED; //#else public static final byte BUTTON_MENU_PRESSED =
	 * SOFT_FIRST_PRESSED; public static final byte BUTTON_OK_PRESSED =
	 * SOFT_FIRST_PRESSED; public static final byte BUTTON_BACK_PRESSED =
	 * SOFT_LAST_PRESSED; //#endif
	 */

	public static int keyPressed = -1;
	public static int keyReleased = -1;

	public static byte pointerEvent;
	public static final byte POINTER_NULL = 0;
	public static final byte POINTER_PRESSED = 1;
	public static final byte POINTER_RELEASED = 2;
	public static final byte POINTER_DRAGGED = 3;

	public static int pointerX, pointerY;

	// 字体
	public static final Font smallFont = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_SMALL);
	public static final Font largeFont = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_LARGE);

	// 窗口显示位置
	/** 窗口X位置 */
	public static int uiX;
	/** 窗口Y位置 */
	public static int uiY;
	/** 窗口宽度 */
	public static int uiWidth;
	/** 窗口高度 */
	public static int uiHeight;

	public static Hashtable resCache = new Hashtable();

	//	public static Display display;
	//	public static Canvas canvas;
	//	public static Graphics graphics;

	/**
	 * 菜单按钮是否在左下角
	 * 
	 * @return 菜单按钮是否在左下角
	 */
	public static boolean isButtonMenuOnLeft() {
		return BUTTON_MENU_PRESSED == SOFT_FIRST_PRESSED;
	}

	/**
	 * OK按钮是否在左下角
	 * 
	 * @return OK按钮是否在左下角
	 */
	public static boolean isButtonOkOnLeft() {
		return BUTTON_OK_PRESSED == SOFT_FIRST_PRESSED;
	}

	/**
	 * Back按钮是否在左下角
	 * 
	 * @return Back按钮是否在左下角
	 */
	public static boolean isButtonBackOnLeft() {
		return BUTTON_BACK_PRESSED == SOFT_FIRST_PRESSED;
	}

	/**
	 * 初始化窗口最大边缘值
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public static void initUIBounds(int x, int y, int width, int height) {
		uiX = x;
		uiY = y;
		uiWidth = width;
		uiHeight = height;
	}

	//	public static void setDisplay(Display dp, Canvas cn) {
	//		display = dp;
	//		canvas = cn;
	//	}

	public static int keyToGame(int keyCode) {
		switch (keyCode) {
			case KEY_UP :
				return UP_PRESSED;
			case KEY_DOWN :
				return DOWN_PRESSED;
			case KEY_LEFT :
				return LEFT_PRESSED;
			case KEY_RIGHT :
				return RIGHT_PRESSED;
			case KEY_FIRE :
				return FIRE_PRESSED;
			default :
				return -1;
		}
	}

	public static int keyToNum(int keyCode) {
		switch (keyCode) {
			case KEY_POUND :
				return KEY_POUND_PRESSED;
			case KEY_STAR :
				return KEY_STAR_PRESSED;
			case KEY_LEFT_SOFT :
				return SOFT_FIRST_PRESSED;
			case KEY_RIGHT_SOFT :
				return SOFT_LAST_PRESSED;

				//#if (KeyCodeType == NokiaE62)
				//# case KEY_NUM0:
				//# 	return KEY_NUM0_PRESSED;
				//# case KEY_NUM1:
				//# 	return KEY_NUM1_PRESSED;
				//# case KEY_NUM2:
				//# 	return KEY_NUM2_PRESSED;
				//# case KEY_NUM3:
				//# 	return KEY_NUM3_PRESSED;
				//# case KEY_NUM4:
				//# 	return KEY_NUM4_PRESSED;
				//# case KEY_NUM5:
				//# 	return KEY_NUM5_PRESSED;
				//# case KEY_NUM6:
				//# 	return KEY_NUM6_PRESSED;
				//# case KEY_NUM7:
				//# 	return KEY_NUM7_PRESSED;
				//# case KEY_NUM8:
				//# 	return KEY_NUM8_PRESSED;
				//# case KEY_NUM9:
				//# 	return KEY_NUM9_PRESSED;
				//# default:
				//#		return -1;
				//#else
			default :
				if (keyCode >= KEY_NUM0 && keyCode <= KEY_NUM9) {
					return KEY_NUM0_PRESSED + (keyCode - KEY_NUM0);
				}
				return -1;
				//#endif
		}
	}

	public static void keyPressed(int keyCode) {
		keyCode = Math.abs(keyCode);
		keyPressed = keyToGame(keyCode);
		if (keyPressed < 0) {
			keyPressed = keyToNum(keyCode);
		}
	}

	public static void keyReleased(int keyCode) {
		keyCode = Math.abs(keyCode);
		keyReleased = keyToGame(keyCode);
		if (keyReleased < 0) {
			keyReleased = keyToNum(keyCode);
		}
	}

	/**
	 * 判断是否有按键按下。
	 * 
	 * @return 有按键按下时返回true，否则返回false。
	 */
	public static final boolean isAnyKeyPressed() {
		//#if (KeyCodeType == NokiaE62)
		//# return keyPressed >= 0 || FishGame.keyCode > 0
		//# || FishGame.keyCode == -50;
		//#else
		return keyPressed >= 0;
		//#endif
	}

	public static final boolean isAnyKeyReleaed() {
		return keyReleased >= 0;
	}

	/**
	 * 判断是指定按键是否被按下。
	 * 
	 * @param key
	 *            指定按键的code。
	 * @param clear
	 *            是否清除按键。
	 * @return 指定按键被按下时返回true，否则返回false。
	 */
	public static final boolean isKeyPressed(int key, boolean clear) {
		boolean ret = keyPressed == key;

		if (clear) {
			if (ret) {
				keyPressed = -1;
			}
		}
		return ret;
	}

	public static final boolean isKeyReleased(int key, boolean clear) {
		boolean ret = keyReleased == key;

		if (clear) {
			if (ret) {
				keyReleased = -1;
			}
		}
		return ret;
	}

	/**
	 * 清除按键状态
	 */
	public static void clearKeyStates() {
		keyPressed = -1;
		keyReleased = -1;
	}

	public static void clearKeyPressedStates() {
		keyPressed = -1;
	}

	public static void clearKeyReleasedStates() {
		keyReleased = -1;
	}

	public static void pointerPressed(int x, int y) {
		pointerEvent = POINTER_PRESSED;
		pointerX = x;
		pointerY = y;
	}

	public static void pointerReleased(int x, int y) {
		pointerEvent = POINTER_RELEASED;
		pointerX = x;
		pointerY = y;
	}

	public static void pointerDragged(int x, int y) {
		pointerEvent = POINTER_DRAGGED;
		pointerX = x;
		pointerY = y;
	}

	public static void clearPointerEvent() {
		pointerEvent = POINTER_NULL;
		pointerX = 0;
		pointerY = 0;
	}

	public static boolean isPointerEvent() {
		return pointerEvent != POINTER_NULL;
	}

	public static boolean isPointerPressed() {
		return pointerEvent == POINTER_PRESSED;
	}

	public static boolean isPointerReleased() {
		return pointerEvent == POINTER_RELEASED;
	}

	public static boolean isPointerDragged() {
		return pointerEvent == POINTER_DRAGGED;
	}

	public static void setUIClip(Graphics g) {
		g.setClip(uiX, uiY, uiWidth, uiHeight);
	}

	public static void fillUIRect(Graphics g, int color) {
		g.setColor(color);
		g.fillRect(uiX, uiY, uiWidth, uiHeight);
	}

	public static Image getImageFromCache(String file) {
		if (resCache.containsKey(file)) {
			return (Image) resCache.get(file);
		} else {
			Image img = Tool.createImage(file);
			resCache.put(file, img);
			return img;
		}
	}
}
