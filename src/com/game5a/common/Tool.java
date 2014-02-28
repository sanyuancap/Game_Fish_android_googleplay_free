package com.game5a.common;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Random;
import java.util.Vector;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.midlet.MIDlet;

//#if (DrawType == Nokia)
//# import com.nokia.mid.ui.DirectGraphics;
//# import com.nokia.mid.ui.DirectUtils;
//#endif

public class Tool {
	// 旋转常量
	public static final int UI_RATATE_90 = 90; // DirectGraphics.ROTATE_90;
	public static final int UI_RATATE_180 = 180; // DirectGraphics.ROTATE_180;
	public static final int UI_RATATE_270 = 270; // DirectGraphics.ROTATE_270;
	public static final int UI_FLIP_HORIZONTAL = 8192; // DirectGraphics.FLIP_HORIZONTAL;
	public static final int UI_FLIP_VERTICAL = 16384; // DirectGraphics.FLIP_VERTICAL;

	public static final int TRANS_NONE = 0;
	public static final int TRANS_MIRROR_ROT180 = 1;
	public static final int TRANS_MIRROR = 2;
	public static final int TRANS_ROT180 = 3;
	// public static final int TRANS_MIRROR_ROT270 = 4;
	public static final int TRANS_ROT90 = 5;
	public static final int TRANS_ROT270 = 6;
	// public static final int TRANS_MIRROR_ROT90 = 7;

	public static int CENTER = Graphics.HCENTER | Graphics.VCENTER;
	public static int TOP_LEFT = Graphics.TOP | Graphics.LEFT;

	// 文字水平对齐方式
	public static final byte H_CENTER = 0;
	public static final byte H_LEFT = 1;
	public static final byte H_RIGHT = 2;

	//文字绘制类型
	public static final byte TYPE_EDGE = 0;
	public static final byte TYPE_SHADOW = 1;

	public static final byte[] PNG_HEAD = {(byte) 0x89, (byte) 0x50, (byte) 0x4E, (byte) 0x47, (byte) 0x0D, (byte) 0x0A, (byte) 0x1A, (byte) 0x0A, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x0D, (byte) 0x49, (byte) 0x48, (byte) 0x44, (byte) 0x52};
	public static final byte[] PNG_END = {(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x49, (byte) 0x45, (byte) 0x4E, (byte) 0x44, (byte) 0xAE, (byte) 0x42, (byte) 0x60, (byte) 0x82};
	public static final int P_CHUNK_SIZE = 100;

	public static int countTimes = 0;
	public static Random rnd = new Random(System.currentTimeMillis());

	private static int[] alphaColors;
	private static final String FREE_MEMORY = "FreeMemory = ";
	//#if AlphaMethod == alphaImage
	//# private static Image alphaImage;
	//#endif

	public static int imageCreatedColor;

	public static boolean bChinese = true;

	public static String marks = ",.!\"'?";

	public static String IMAGE_NUM_STYLE0 = "0123456789";
	public static String IMAGE_NUM_STYLE1 = "+0123456789";
	public static String IMAGE_NUM_STYLE2 = "-0123456789";

	public static final int[] JUMP_RANGE = {-1, -1, 0, 1, 2, 2, 1, 0}; //{1, 1, 0, -1, -1, 0};

	/**
	 * Tool类有关参数初始化
	 */
	public static void init() {
		//#if (MobileType == 6101)||(MobileType == 7260)||(MobileType == 6230i)
		//#else
		Image img = Image.createImage(1, 1);
		int[] rgbData = getImageRGB(img, 0, 0, 1, 1);
		imageCreatedColor = rgbData[0];
		//#endif
	}

	public static void cycle() {
		countTimes++;
	}
	
	//android 110728
	public static String processFileName(String fileName) {
		if (fileName.startsWith("/")) {
			fileName = fileName.substring(1);
		}
		return fileName;
	}

	/**
	 * 获取镜像翻转类型
	 * 
	 * @param dt
	 * @return 翻转类型
	 */
	public static int getMirrorTransType(int dt) {
		int mdt = TRANS_NONE;
		switch (dt) {
			case TRANS_NONE :
				mdt = TRANS_MIRROR;
				break;

			case TRANS_MIRROR :
				mdt = TRANS_NONE;
				break;

			case TRANS_MIRROR_ROT180 :
				mdt = TRANS_ROT180;
				break;

			case TRANS_ROT180 :
				mdt = TRANS_MIRROR_ROT180;
				break;

			//      case TRANS_ROT270:
			//        mdt = TRANS_MIRROR;
			//        break;
			//
			//      case TRANS_ROT90:
			//        mdt = TRANS_MIRROR;
			//        break;

			default :
				break;
		}
		return mdt;
	}

	/**
	 * 绘制图片的一部分
	 * 
	 * @param g
	 *            Graphics对象
	 * @param image
	 *            Image图片对象
	 * @param x_src
	 *            图片源位置X
	 * @param y_src
	 *            图片源位置Y
	 * @param width
	 *            图片源宽度X
	 * @param height
	 *            图片源高度X
	 * @param transType
	 *            翻转类型
	 * @param x_dest
	 *            绘制位置X
	 * @param y_dest
	 *            绘制位置Y
	 */
	public static void drawRegion(Graphics g, Image image, int x_src, int y_src, int width, int height, int transType, int x_dest, int y_dest) {
		//#if (Series==E258)||(Series==D508)||(Series==D608)
		//# Common.setUIClip(g);
		//#else
		if (transType < 4) {
			g.setClip(x_dest, y_dest, width, height);
		} else {
			g.setClip(x_dest, y_dest, height, width);
		}
		//#endif

		//#if (DrawType == Nokia)
		//# DirectGraphics dg = DirectUtils.getDirectGraphics(g);
		//# switch (transType) {
		//# case TRANS_MIRROR_ROT180 :
		//# 	dg.drawImage(image, x_dest - x_src, y_dest
		//# 			- (image.getHeight() - height - y_src), TOP_LEFT,
		//# 			UI_FLIP_VERTICAL);
		//# 	break;
		//# case TRANS_MIRROR :
		//# 	dg.drawImage(image,
		//# 			x_dest - (image.getWidth() - width - x_src), y_dest
		//# 					- y_src, TOP_LEFT, UI_FLIP_HORIZONTAL);
		//# 	break;
		//# case TRANS_ROT180 :
		//# 	dg.drawImage(image,
		//# 			x_dest - (image.getWidth() - width - x_src), y_dest
		//# 					- (image.getHeight() - height - y_src), TOP_LEFT,
		//# 			UI_RATATE_180);
		//# 	break;
		//# case TRANS_ROT90 :
		//# 	dg.drawImage(image, x_dest
		//# 			- (image.getHeight() - height - y_src), y_dest - x_src,
		//# 			TOP_LEFT, UI_RATATE_270);
		//# 	break;
		//# case TRANS_ROT270 :
		//# 	dg.drawImage(image, x_dest - y_src, y_dest
		//# 			- (image.getWidth() - width - x_src), TOP_LEFT, UI_RATATE_90);
		//# 	break;
		//# default :
		//# 	//dg.drawImage(image, x_dest - x_src, y_dest - y_src, TOP_LEFT, 0);
		//# 	g.drawImage(image, x_dest - x_src, y_dest - y_src, TOP_LEFT);
		//# 	break;
		//# }
		//# dg = null;
		//#else
		// Midp 2.0方法
		g.drawRegion(image, x_src, y_src, width, height, transType, x_dest, y_dest, TOP_LEFT);
		//#endif
	}

	/**
	 * 获取图片ARGB值
	 * 
	 * @param image
	 *            图片
	 * @param x_src
	 *            图片源位置X
	 * @param y_src
	 *            图片源位置Y
	 * @param width
	 *            图片源宽度
	 * @param height
	 *            图片源高度
	 * @return 图片ARGB值
	 */
	public static int[] getImageRGB(Image image, int x_src, int y_src, int width, int height) {
		int length = width * height;
		int[] rgbData = new int[length];
		//#if (DrawType == Nokia)
		//#		Image img = Image.createImage(width, height);
		//#		Graphics g = img.getGraphics();
		//#		g.drawImage(image, -x_src, -y_src, TOP_LEFT);
		//#		DirectGraphics dg = DirectUtils.getDirectGraphics(g);
		//#		dg.getPixels(rgbData, 0, width, 0, 0, width, height, DirectGraphics.TYPE_INT_8888_ARGB);
		//#		img = null;
		//#else
		image.getRGB(rgbData, 0, width, x_src, y_src, width, height);
		//#endif
		return rgbData;
	}

	/**
	 * 绘制图块Alpha混合颜色
	 * 
	 * @param g
	 *            Graphics对象
	 * @param image
	 *            图片
	 * @param x_src
	 *            图片源位置X
	 * @param y_src
	 *            图片源位置Y
	 * @param width
	 *            图片源宽度
	 * @param height
	 *            图片源高度
	 * @param transType
	 *            翻转类型
	 * @param x_dest
	 *            绘制位置X
	 * @param y_dest
	 *            绘制位置Y
	 * @param alphaColor
	 *            alpha混合颜色
	 */
	public static void drawRegionAlphaColor(Graphics g, Image image, int x_src, int y_src, int width, int height, int transType, int x_dest, int y_dest, int alphaColor) {
		int length = width * height;
		int[] rgbData = new int[length];
		image.getRGB(rgbData, 0, width, x_src, y_src, width, height);

		int alpha = alphaColor >>> 24;
		int alphaR = (alphaColor >>> 16) & 0xff;
		int alphaG = (alphaColor >>> 8) & 0xff;
		int alphaB = alphaColor & 0xff;

		Hashtable colorHT = new Hashtable();

		int count = 0;
		for (int i = 0; i < length; i++) {
			if (rgbData[i] == imageCreatedColor) {
				rgbData[i] = 0;
				continue;
			}
			if ((rgbData[i] >>> 24) == 0) {
				continue;
			}

			String rgbKey = Integer.toString(rgbData[i]);
			if (colorHT.containsKey(rgbKey)) {
				rgbData[i] = Integer.parseInt((String) colorHT.get(rgbKey));
				continue;
			}

			int red = (rgbData[i] >>> 16) & 0xff;
			int green = (rgbData[i] >>> 8) & 0xff;
			int blue = rgbData[i] & 0xff;

			red = red * (255 - alpha) / 255 + alphaR * alpha / 255;
			green = green * (255 - alpha) / 255 + alphaG * alpha / 255;
			blue = blue * (255 - alpha) / 255 + alphaB * alpha / 255;

			rgbData[i] = (red << 16) | (green << 8) | blue | 0xff000000;

			colorHT.put(rgbKey, Integer.toString(rgbData[i]));
			count++;
		}

		drawRGB(g, rgbData, x_dest, y_dest, width, height, transType, true);
	}

	/**
	 * 绘制图片Alpha混合颜色
	 * 
	 * @param g
	 *            Graphics对象
	 * @param i
	 *            图片
	 * @param x
	 *            绘制位置X
	 * @param y
	 *            绘制位置Y
	 * @param transType
	 *            翻转类型
	 * @param alphaColor
	 *            alpha混合颜色
	 */
	public static void drawImageAlphaColor(Graphics g, Image i, int x, int y, int transType, int alphaColor) {
		drawRegionAlphaColor(g, i, 0, 0, i.getWidth(), i.getHeight(), transType, x, y, alphaColor);
	}

	/**
	 * 绘制图块半透明
	 * 
	 * @param g
	 *            Graphics对象
	 * @param image
	 *            图片
	 * @param x_src
	 *            图片源位置X
	 * @param y_src
	 *            图片源位置Y
	 * @param width
	 *            图片源宽度
	 * @param height
	 *            图片源高度
	 * @param transType
	 *            翻转类型
	 * @param x_dest
	 *            绘制位置X
	 * @param y_dest
	 *            绘制位置Y
	 * @param alphaValue
	 *            透明值, 取值范围0~255
	 */
	public static void drawRegionAlpha(Graphics g, Image image, int x_src, int y_src, int width, int height, int transType, int x_dest, int y_dest, int alphaValue) {
		if (alphaValue <= 0) {
			return;
		}
		if (alphaValue >= 255) {
			drawRegion(g, image, x_src, y_src, width, height, transType, x_dest, y_dest);
			return;
		}

		int alpha = alphaValue << 24;
		int length = width * height;
		int[] rgbData = new int[length];
		image.getRGB(rgbData, 0, width, x_src, y_src, width, height);

		for (int i = 0; i < length; i++) {
			if (rgbData[i] == imageCreatedColor) {
				rgbData[i] = 0;
				continue;
			}
			if ((rgbData[i] >>> 24) == 0) {
				continue;
			}

			rgbData[i] = ((rgbData[i] & 0xffffff) | alpha);
		}

		drawRGB(g, rgbData, x_dest, y_dest, width, height, transType, true);
	}

	/**
	 * 绘制图片半透明
	 * 
	 * @param g
	 *            Graphics对象
	 * @param i
	 *            图片
	 * @param x
	 *            绘制位置X
	 * @param y
	 *            绘制位置Y
	 * @param transType
	 *            翻转类型
	 * @param alphaValue
	 *            透明值, 取值范围0~255
	 */
	public static void drawImageAlpha(Graphics g, Image i, int x, int y, int transType, int alphaValue) {
		drawRegionAlpha(g, i, 0, 0, i.getWidth(), i.getHeight(), transType, x, y, alphaValue);
	}

	/**
	 * 绘制RGB数组
	 * 
	 * @param g
	 *            Graphics对象
	 * @param rgbData
	 *            RGB数组
	 * @param x_dest
	 *            绘制位置X
	 * @param y_dest
	 *            绘制位置Y
	 * @param width
	 *            宽度
	 * @param height
	 *            高度
	 * @param transType
	 *            翻转类型
	 * @param processAlpha
	 *            是否半透明
	 */
	public static void drawRGB(Graphics g, int[] rgbData, int x_dest, int y_dest, int width, int height, int transType, boolean processAlpha) {
		if (transType < 4) {
			g.setClip(x_dest, y_dest, width, height);
		} else {
			g.setClip(x_dest, y_dest, height, width);
		}
		int length = width * height;
		int rgb[];
		switch (transType) {
			case TRANS_MIRROR_ROT180 :
				rgb = new int[length];
				for (int i = 0; i < height; i++) {
					System.arraycopy(rgbData, i * width, rgb, (height - i - 1) * width, width);
				}
				g.drawRGB(rgb, 0, width, x_dest, y_dest, width, height, processAlpha);
				break;

			case TRANS_MIRROR :
				rgb = new int[length];
				for (int i = 0; i < height; i++) {
					for (int j = 0; j < width; j++) {
						rgb[i * width + j] = rgbData[i * width + width - j - 1];
					}
				}
				g.drawRGB(rgb, 0, width, x_dest, y_dest, width, height, processAlpha);
				break;

			case TRANS_ROT180 :
				rgb = new int[length];
				for (int i = 0; i < height; i++) {
					for (int j = 0; j < width; j++) {
						rgb[i * width + j] = rgbData[(height - i - 1) * width + width - j - 1];
					}
				}
				g.drawRGB(rgb, 0, width, x_dest, y_dest, width, height, processAlpha);
				break;

			case TRANS_ROT90 :
				rgb = new int[length];
				for (int i = 0; i < width; i++) {
					for (int j = 0; j < height; j++) {
						rgb[i * height + j] = rgbData[(height - j - 1) * width + i];
					}
				}
				g.drawRGB(rgb, 0, height, x_dest, y_dest, height, width, processAlpha);
				break;

			case TRANS_ROT270 :
				rgb = new int[length];
				for (int i = 0; i < width; i++) {
					for (int j = 0; j < height; j++) {
						rgb[i * height + j] = rgbData[j * width + width - i - 1];
					}
				}
				g.drawRGB(rgb, 0, height, x_dest, y_dest, height, width, processAlpha);
				break;

			default :
				g.drawRGB(rgbData, 0, width, x_dest, y_dest, width, height, processAlpha);
				break;
		}
	}

	/**
	 * 绘制切块
	 * 
	 * @param g
	 *            Graphics对象
	 * @param image
	 *            图片
	 * @param id
	 *            切块ID
	 * @param fwidth
	 *            切块宽度
	 * @param fheight
	 *            切块高度
	 * @param type
	 *            翻转类型
	 * @param dx
	 *            位置X
	 * @param dy
	 *            位置Y
	 */
	public static void drawTile(Graphics g, Image image, int id, int fwidth, int fheight, int type, int dx, int dy) {
		int cols = image.getWidth() / fwidth;
		drawRegion(g, image, (id % cols) * fwidth, (id / cols) * fheight, fwidth, fheight, type, dx, dy);
	}

	/**
	 * 绘制图片
	 * 
	 * @param g
	 *            Graphics对象
	 * @param i
	 *            图片
	 * @param x
	 *            位置X
	 * @param y
	 *            位置Y
	 * @param transType
	 *            翻转类型
	 */
	public static void drawImage(Graphics g, Image i, int x, int y, int transType) {
		if (transType == TRANS_NONE) {
			g.setClip(x, y, i.getWidth(), i.getHeight());
			g.drawImage(i, x, y, TOP_LEFT);
		} else {
			drawRegion(g, i, 0, 0, i.getWidth(), i.getHeight(), transType, x, y);
		}
	}

	public static void drawImageScale(Graphics g, Image srcImg, int dx, int dy, int dw, int dh) {
		drawImageScale(g, srcImg, 0, 0, srcImg.getWidth(), srcImg.getHeight(), dx, dy, dw, dh);
	}

	/*
	 * 调整图片大小 destW 调整后的宽，destH调整后的高
	 */
	public static void drawImageScale(Graphics g, Image srcImg, int sx, int sy, int sw, int sh, int dx, int dy, int dw, int dh) {
		int[] destRGB = new int[dw * dh];
		int[] srcRGB = getImageRGB(srcImg, sx, sy, sw, sh);
		for (int destY = 0; destY < dh; ++destY) {
			for (int destX = 0; destX < dw; ++destX) {
				int srcX = (destX * sw) / dw;
				int srcY = (destY * sh) / dh;
				destRGB[destX + destY * dw] = srcRGB[srcX + srcY * sw];
			}
		}
		g.setClip(dx, dy, dw, dh);
		g.drawRGB(destRGB, 0, dw, dx, dy, dw, dh, true);
	}

	/**
	 * 获取随机Bool值
	 * 
	 * @return 随机Bool值
	 */
	public static boolean getNextRndBoolean() {
		return Math.abs(rnd.nextInt()) % 2 == 0;
	}

	/**
	 * 获取随机整数
	 * 
	 * @param min
	 *            最小值
	 * @param max
	 *            最大值
	 * @return 随机整数
	 */
	public static int getNextRnd(int min, int max) {
		if (max == min) {
			return min;
		}

		if (max < min) {
			return min - Math.abs(rnd.nextInt()) % (min - max);
		}
		return min + Math.abs(rnd.nextInt()) % (max - min);
	}

	/**
	 * 获取随机整数
	 * 
	 * @return 随机整数
	 */
	public static int getNextRnd() {
		return rnd.nextInt();
	}

	/**
	 * 获取随机几率值
	 * 
	 * @return 随机几率值
	 */
	public static int getNextOdds() {
		return Math.abs(rnd.nextInt()) % 100;
	}

	/**
	 * 创建图片
	 * 
	 * @param str
	 *            图片文件
	 * @return 图片
	 */
	public static Image createImage(String str) {
		try {
			//android 110727
			str = processFileName(str);

			return Image.createImage(str);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 幂计算
	 * 
	 * @param base
	 *            底数
	 * @param exp
	 *            基数
	 * @return 幂计算结果
	 */
	public static int power(int base, int exp) {
		if (exp < 0) {
			return 0;
		}
		int result = 1;
		for (int i = 0; i < exp; i++) {
			result *= base;
		}
		return result;
	}

	/**
	 * 分割字符串
	 * 
	 * @param s
	 *            字符串
	 * @param drawWidth
	 *            分割宽度
	 * @param font
	 *            字体
	 * @return 字符串数组
	 */
	public static String[] getStringArray(String s, int drawWidth, Font font) {
		if (bChinese) {
			return getStringArrayCn(s, drawWidth, font);
		} else {
			return getStringArrayFrn(s, drawWidth, font);
		}
	}

	/**
	 * 分割中文字符串
	 * 
	 * @param s
	 *            字符串
	 * @param drawWidth
	 *            分割宽度
	 * @param font
	 *            字体
	 * @return 字符串数组
	 */
	public static String[] getStringArrayCn(String s, int drawWidth, Font font) {
		int begin = 0;
		int lineWidth = 0;
		Vector vS = new Vector();
		s += '\n';

		boolean blineEnd;
		for (int i = 0; i < s.length(); i++) {
			blineEnd = false;
			if (s.charAt(i) == '\n') {
				blineEnd = true;
			} else {
				lineWidth = font.substringWidth(s, begin, i - begin + 1);
				if (lineWidth >= drawWidth) {
					blineEnd = true;
				}
			}
			if (blineEnd) {
				vS.addElement((Object) (s.substring(begin, i)));
				begin = i;
				if (s.charAt(i) == '\n') {
					begin++;
				}
			}
		}

		String[] strs = new String[vS.size()];
		vS.copyInto(strs);
		return strs;
	}

	/**
	 * 判断字符是否符号
	 * 
	 * @param ch
	 *            字符
	 * @return 是否符合
	 */
	public static boolean isMark(char ch) {
		if (marks == null) {
			return false;
		}
		if (marks.indexOf(ch) >= 0) {
			return true;
		}
		return false;
	}

	/**
	 * 分割外文
	 * 
	 * @param s
	 *            字符串
	 * @param drawWidth
	 *            分割宽度
	 * @param fnt
	 *            字体
	 * @return 字符串数组
	 */
	public static String[] getStringArrayFrn(String s, int drawWidth, Font fnt) {
		int sl = s.length();
		int beginID = 0;
		int endID = 0;
		int lineWidth = 0;
		int spaceID = 0;
		char ch;
		String tstr;
		Vector vS = new Vector();

		boolean bLineEnd;
		boolean bLink;
		boolean bEndSymbol;
		for (int i = 0; i < sl; i++) {
			bLineEnd = false;
			bLink = false;
			bEndSymbol = false;
			ch = s.charAt(i);
			if (i == sl - 1) {
				endID = i + 1;
				bLineEnd = true;
			} else if (s.charAt(i) == '\n') {
				endID = i + 1;
				bLineEnd = true;
				bEndSymbol = true;
			} else {
				if (ch == ' ') {
					spaceID = i;
				}
				lineWidth = fnt.substringWidth(s, beginID, i - beginID + 1);
				if (lineWidth >= drawWidth) {
					if (ch != ' ') {
						if (spaceID <= beginID) {
							endID = i;
							bLink = true;
						} else {
							endID = spaceID + 1;
						}
					} else if (isMark(ch)) {
						endID = i - 1;
						bLink = true;
					} else {
						endID = i + 1;
					}
					bLineEnd = true;
				}
			}
			if (bLineEnd) {
				if (bEndSymbol) {
					tstr = s.substring(beginID, endID - 1);
				} else {
					tstr = s.substring(beginID, endID);
				}

				if (bLink) {
					tstr += "-";
				}
				vS.addElement((Object) tstr);
				beginID = endID;
			}
		}

		String[] strs = new String[vS.size()];
		vS.copyInto(strs);
		return strs;
	}

	/**
	 * 分割字符串
	 * 
	 * @param s
	 *            字符串
	 * @param ch
	 *            分割字符
	 * @return 字符串数组
	 */
	public static String[] getStringArray(String s, char ch) {
		if (s.length() == 0) {
			return null;
		}
		int startIndex = 0;
		int endIndex = 0;
		Vector vS = new Vector();
		while (true) {
			endIndex = s.indexOf(ch, startIndex);
			if (endIndex == -1) {
				vS.addElement(s.substring(startIndex));
				break;
			} else {
				vS.addElement(s.substring(startIndex, endIndex));
				startIndex = endIndex + 1;
			}
		}
		String[] strs = new String[vS.size()];
		vS.copyInto(strs);
		return strs;
	}

	public static String[] getStringArray(String s, String sep) {
		if (s.length() == 0) {
			return null;
		}
		int startIndex = 0;
		int endIndex = 0;
		Vector vS = new Vector();
		while (true) {
			endIndex = s.indexOf(sep, startIndex);
			if (endIndex == -1) {
				vS.addElement(s.substring(startIndex));
				break;
			} else {
				vS.addElement(s.substring(startIndex, endIndex));
				startIndex = endIndex + sep.length();
			}
		}
		String[] strs = new String[vS.size()];
		vS.copyInto(strs);
		return strs;
	}

	/**
	 * 分割字符串
	 * 
	 * @param s
	 *            字符串
	 * @return 字符串数组
	 */
	public static String[] getStringArray(String s) {
		return getStringArray(s, '\n');
	}

	/**
	 * 绘制字符串
	 * 
	 * @param g
	 *            Graphics对象
	 * @param s
	 *            字符串
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
	public static void drawString(Graphics g, String s, int x, int y, int frontColor, int bgColor, byte type) {
		if (bgColor >= 0) {
			g.setColor(bgColor);
			if (type == TYPE_EDGE) {
				//				g.drawString(s, x + 1, y - 1, TOP_LEFT);
				//				g.drawString(s, x - 1, y + 1, TOP_LEFT);
				//				g.drawString(s, x - 1, y - 1, TOP_LEFT);
				//				g.drawString(s, x + 1, y + 1, TOP_LEFT);

				g.drawString(s, x, y - 1, TOP_LEFT);
				g.drawString(s, x, y + 1, TOP_LEFT);
				g.drawString(s, x - 1, y, TOP_LEFT);
				g.drawString(s, x + 1, y, TOP_LEFT);
			} else if (type == TYPE_SHADOW) {
				g.drawString(s, x + 1, y + 1, TOP_LEFT);
			}
		}
		g.setColor(frontColor);
		g.drawString(s, x, y, TOP_LEFT);
	}

	/**
	 * 绘制字符串
	 * 
	 * @param g
	 *            Graphics对象
	 * @param s
	 *            字符串
	 * @param x
	 *            位置X
	 * @param y
	 *            位置Y
	 * @param frontColor
	 *            字体颜色
	 * @param bgColor
	 *            背景颜色
	 */
	public static void drawString(Graphics g, String s, int x, int y, int frontColor, int bgColor) {
		drawString(g, s, x, y, frontColor, bgColor, TYPE_EDGE);
	}

	/**
	 * 绘制字符串
	 * 
	 * @param g
	 *            Graphics对象
	 * @param s
	 *            字符串
	 * @param x
	 *            位置X
	 * @param y
	 *            位置Y
	 * @param frontColor
	 *            字体颜色
	 * @param bgColor
	 *            背景颜色
	 * @param jrange
	 *            跳动幅度
	 * @param type
	 *            类型
	 */
	public static void drawString(Graphics g, String s, int x, int y, int frontColor, int bgColor, int jrange, byte type) {
		if (jrange == 0) {
			drawString(g, s, x, y, frontColor, bgColor, type);
		} else {
			Font font = g.getFont();
			int w = 0;
			int[] f = {-jrange, 0, jrange, 0};
			for (int i = 0; i < s.length(); i++) {
				w = font.substringWidth(s, 0, i);
				drawString(g, s.substring(i, i + 1), x + w, y + f[(i + (countTimes & 3)) & 3], frontColor, bgColor, type);
			}
		}
	}

	/**
	 * 绘制字符串
	 * 
	 * @param g
	 *            Graphics对象
	 * @param s
	 *            字符串
	 * @param x
	 *            位置X
	 * @param y
	 *            位置Y
	 * @param frontColor
	 *            字体颜色
	 * @param bgColor
	 *            背景颜色
	 * @param jrange
	 *            跳动幅度
	 */
	public static void drawString(Graphics g, String s, int x, int y, int frontColor, int bgColor, int jrange) {
		drawString(g, s, x, y, frontColor, bgColor, jrange, TYPE_EDGE);
	}

	/**
	 * 绘制图片数字
	 * 
	 * @param g
	 *            Graphics对象
	 * @param img
	 *            图片, 图片顺序(0123456789)
	 * @param numStr
	 *            数字字符串
	 * @param dx
	 *            位置X
	 * @param dy
	 *            位置Y
	 * @param fontWidth
	 *            图片字体宽度
	 * @param offSet
	 *            偏移量
	 */
	public static void drawImageNum(Graphics g, Image img, String numStr, int dx, int dy, int fontWidth, int offSet) {
		int ih = img.getHeight();
		for (int i = 0; i < numStr.length(); i++) {
			char ch = numStr.charAt(i);
			int id = ch - '0';
			int x = dx + i * (fontWidth + offSet);
			drawTile(g, img, id, fontWidth, ih, Tool.TRANS_NONE, x, dy);
		}
	}

	/**
	 * 绘制图片数字
	 * 
	 * @param g
	 *            Graphics对象
	 * @param img
	 *            图片
	 * @param numStr
	 *            数字字符串
	 * @param dx
	 *            位置X
	 * @param dy
	 *            位置Y
	 * @param fontWidth
	 *            图片字体宽度
	 * @param offSet
	 *            偏移量
	 * @param imgDes
	 *            图片字符串描述
	 */
	public static void drawImageNum(Graphics g, Image img, String numStr, int dx, int dy, int fontWidth, int offSet, String imgDes) {
		int ih = img.getHeight();
		for (int i = 0; i < numStr.length(); i++) {
			char ch = numStr.charAt(i);
			int id = imgDes.indexOf(ch);
			if (id < 0) {
				continue;
			}
			int x = dx + i * (fontWidth + offSet);
			drawTile(g, img, id, fontWidth, ih, Tool.TRANS_NONE, x, dy);
		}
	}

	/**
	 * 绘制图片矩形
	 * 
	 * @param g
	 *            Graphics对象
	 * @param rImage
	 *            图片
	 * @param rx
	 *            位置X
	 * @param ry
	 *            位置Y
	 * @param rw
	 *            矩形宽度
	 * @param rh
	 *            矩形高度
	 * @param bgColor
	 *            背景色
	 */
	public static void drawImageRect(Graphics g, Image rImage, int rx, int ry, int rw, int rh, int bgColor) {
		int fw = rImage.getWidth() / 3;
		int fh = rImage.getHeight() / 3;

		// /ft 修正rw, rh值为fw, fh的整数倍,并调整rx,ry值
		if (rh < fh) {
			ry += ((rh - fh * 2) >> 1);
			rh = fh * 2;
		}

		int rwb = rw;
		int rhb = rh;
		int w = rw / fw;
		int h = rh / fh;
		if (w * fw < rw) {
			rw = fw * (w + 1);
			rx += ((rwb - rw) >> 1);
		}
		if (h * fh < rh) {
			rh = fh * (h + 1);
			ry += ((rhb - rh) >> 1);
		}

		if (bgColor >= 0) {
			g.setClip(rx, ry, rw, rh);
			g.setColor(bgColor);
			g.fillRect(rx + fw, ry + fh, rw - fw * 2, rh - fh * 2);
		}
		for (int i = 0; i < rw / fw - 2; i++) {
			// Top
			drawTile(g, rImage, 1, fw, fh, TRANS_NONE, rx + fw * (i + 1), ry);
			// Bottom
			drawTile(g, rImage, 7, fw, fh, TRANS_NONE, rx + fw * (i + 1), ry + rh - fh);
		}

		for (int i = 0; i < rh / fh - 2; i++) {
			// Left
			drawTile(g, rImage, 3, fw, fh, TRANS_NONE, rx, ry + fh * (i + 1));
			// Right
			drawTile(g, rImage, 5, fw, fh, TRANS_NONE, rx + rw - fw, ry + fh * (i + 1));
		}

		drawTile(g, rImage, 0, fw, fh, TRANS_NONE, rx, ry);
		drawTile(g, rImage, 2, fw, fh, TRANS_NONE, rx + rw - fw, ry);
		drawTile(g, rImage, 6, fw, fh, TRANS_NONE, rx, ry + rh - fh);
		drawTile(g, rImage, 8, fw, fh, TRANS_NONE, rx + rw - fw, ry + rh - fh);
	}

	/**
	 * 填充半透明矩形
	 * 
	 * @param g
	 *            Graphics对象
	 * @param argb
	 *            半透明色
	 * @param x
	 *            位置X
	 * @param y
	 *            位置Y
	 * @param width
	 *            宽度
	 * @param height
	 *            高度
	 */
//	public static void fillAlphaRect(Graphics g, int argb, int x, int y, int width, int height) {
//		g.setClip(x, y, width, height);
//
//		//#if (Series==E258)||(Series==D608)||(MobileType == Mid)
//		//# g.setColor(0);
//		//# g.fillRect(x, y, width, height);
//		//#else
//		int alpha = argb >>> 24;
//		if (alpha == 0) {
//			return;
//		} else if (alpha == 0xff) {
//			g.setColor(argb & 0xffffff);
//			g.fillRect(x, y, width, height);
//			return;
//		}
//
//		//#if (DrawType == Nokia)
//		//# DirectGraphics dg = DirectUtils.getDirectGraphics(g);
//		//# int[] xPoints = {x, x+width, x+width, x};
//		//# int[] yPoints = {y, y, y+height, y+height};
//		//# dg.fillPolygon(xPoints, 0, yPoints, 0, 4, argb);
//		//#else
//		boolean bnew = false;
//		if (alphaColors == null) {
//			bnew = true;
//		} else if (width != alphaColors.length || argb != alphaColors[0]) {
//			bnew = true;
//		}
//
//		if (bnew == true) {
//			alphaColors = new int[width];
//			for (int i = 0; i < alphaColors.length; i++) {
//				alphaColors[i] = argb;
//			}
//
//			//#if AlphaMethod == alphaImage
//			//# alphaImage = Image.createRGBImage(alphaColors, width, 1, true);
//			//#endif
//		}
//		//#if AlphaMethod == alphaImage
//		//# for(int j = 0; j < height; j++){
//		//# g.drawImage(alphaImage, x, y + j, TOP_LEFT);
//		//# }
//		//#else
//		g.drawRGB(alphaColors, 0, 0, x, y, width, height, true);
//		//#endif
//		//#endif
//		//#endif
//	}
	public static void fillAlphaRect(Graphics g, int argb, int x, int y, int width, int height) {
		g.setClip(x, y, width, height);
		g.fillAlphaRect(argb, x, y, width, height);
	}

	/**
	 * 清理内存
	 */
	public static void clearUpMemory() {
		// System.gc();
		System.out.println(FREE_MEMORY + Runtime.getRuntime().freeMemory());
	}

	/**
	 * 检测矩形碰撞
	 * 
	 * @param ax
	 *            矩形a左上角X
	 * @param ay
	 *            矩形a左上角Y
	 * @param aw
	 *            矩形a宽度
	 * @param ah
	 *            矩形a高度
	 * @param bx
	 *            矩形b左上角X
	 * @param by
	 *            矩形b左上角Y
	 * @param bw
	 *            矩形b宽度
	 * @param bh
	 *            矩形b高度
	 * @return 是否碰撞
	 */
	public static boolean isRectIntersected(int ax, int ay, int aw, int ah, int bx, int by, int bw, int bh) {
		if (by + bh < ay || by > ay + ah || bx + bw < ax || bx > ax + aw) {
			return false;
		}
		return true;
	}

	/**
	 * 检测矩形碰撞
	 * 
	 * @param ax
	 *            矩形a左上角X
	 * @param ay
	 *            矩形a左上角Y
	 * @param aRect
	 *            矩形a
	 * @param bx
	 *            矩形b左上角X
	 * @param by
	 *            矩形b左上角Y
	 * @param bRect
	 *            矩形b
	 * @return 是否碰撞
	 */
	public static boolean isRectIntersected(int ax, int ay, Rectangle aRect, int bx, int by, Rectangle bRect) {
		return isRectIntersected(ax + aRect.x, ay + aRect.y, aRect.width, aRect.height, bx + bRect.x, by + bRect.y, bRect.width, bRect.height);
	}

	public static boolean isPointInRect(int px, int py, int bx, int by, int bw, int bh) {
		if (by + bh < py || by > py || bx + bw < px || bx > px) {
			return false;
		}
		return true;
	}

	public static InputStream getResourceAsStream(String fileName) throws IOException {
		//android 110727
		fileName = processFileName(fileName);
		return MIDlet.getResourceManager().getAssets().open(fileName);
		//return "".getClass().getResourceAsStream(fileName);
	}

	/**
	 * 从输入流获取数据
	 * 
	 * @param in
	 *            输入流
	 * @return byte数组
	 * @throws IOException
	 */
	public static byte[] getBytesFromInput(InputStream in) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int rd = 0;
		int len = 0;
		byte[] buf = new byte[64];
		while ((rd = in.read(buf)) != -1) {
			len += rd;
			out.write(buf, 0, rd);
		}
		byte[] rt = out.toByteArray();
		out.close();
		in.close();
		return rt;
	}

	/**
	 * byte数组转换UTF编码字符串
	 * 
	 * @param data
	 *            byte数组
	 * @param off
	 *            开始位置
	 * @param len
	 *            长度
	 * @return 字符串
	 */
	public static String toUTF(byte data[], int off, int len) {
		char infoStr[] = new char[len * 2];
		int i = 0;
		int charCount = 0;
		while (i < len) {
			try {
				char ch;
				if ((data[i + off] & 0x80) != 0) {
					if ((data[i + off] & 0x20) != 0) {
						ch = (char) ((data[i + off] & 0xf) << 12);
						ch += (char) (data[i + 1 + off] & 0x3f) << 6;
						ch += data[i + 2 + off] & 0x3f;
						i += 3;
					} else {
						ch = (char) ((data[i + off] & 0x1f) << 6);
						ch += data[i + 1 + off] & 0x3f;
						i += 2;
					}
				} else {
					ch = (char) data[i + off];
					i++;
				}
				infoStr[charCount++] = ch;
			} catch (Exception _ex) {
			}
		}
		return new String(infoStr, 0, charCount);
	}

	public static void reportException(Exception e) {
		e.printStackTrace();
	}

	/**
	 * 解密字符串
	 * 
	 * @param srcStr
	 *            源字符串
	 * @param version
	 *            加密版本
	 * @return 解密字符串
	 */
	public static String decryptString(String srcStr, int version) {
		String desStr = srcStr;
		if (version == 1) {
			int length = srcStr.length() / 2;
			char[] chs = new char[length];
			for (int i = 0; i < length; i++) {
				char ch0 = srcStr.charAt(i * 2);
				char ch1 = srcStr.charAt(i * 2 + 1);
				chs[i] = (char) ((ch0 - 'A') / 8 * 24 + (ch1 - 'a'));
			}
			desStr = new String(chs);
		}

		return desStr;
	}

	/**
	 * 读取P图片文件
	 * 
	 * @param file
	 *            P文件
	 * @return 图片对象
	 * @throws Exception
	 */
	public static Image loadPImage(String file) throws Exception {
		Image img = null;
		InputStream in = getResourceAsStream(file);
		byte[] lens = new byte[4];
		in.read(lens);
		int len = ((lens[0] & 0xff) << 24) + ((lens[1] & 0xff) << 16) + ((lens[2] & 0xff) << 8) + (lens[3] & 0xff);

		byte[] pngData = new byte[PNG_HEAD.length + len + PNG_END.length];
		System.arraycopy(PNG_HEAD, 0, pngData, 0, PNG_HEAD.length);
		System.arraycopy(PNG_END, 0, pngData, pngData.length - PNG_END.length, PNG_END.length);

		int blockNum = len / P_CHUNK_SIZE;
		int oddSize = len % P_CHUNK_SIZE;

		int off = PNG_HEAD.length + len;

		if (oddSize != 0) {
			off -= oddSize;
			in.read(pngData, off, oddSize);
		}
		for (int i = blockNum - 1; i >= 0; i--) {
			off -= P_CHUNK_SIZE;
			in.read(pngData, off, P_CHUNK_SIZE);
		}

		in.close();
		in = null;

		img = Image.createImage(pngData, 0, pngData.length);

		return img;
	}

	/**
	 * 调色板换色
	 * 
	 * @param file
	 *            .p文件
	 * @param palette
	 *            .pt调色板
	 * @return 图片对象
	 * @throws Exception
	 */
	public static Image loadPPImage(String file, String palette) throws Exception {
		Image img = null;
		InputStream in = getResourceAsStream(file);
		byte[] lens = new byte[4];
		in.read(lens);
		int len = ((lens[0] & 0xff) << 24) + ((lens[1] & 0xff) << 16) + ((lens[2] & 0xff) << 8) + (lens[3] & 0xff);

		byte[] pngData = new byte[PNG_HEAD.length + len + PNG_END.length];
		System.arraycopy(PNG_HEAD, 0, pngData, 0, PNG_HEAD.length);
		System.arraycopy(PNG_END, 0, pngData, pngData.length - PNG_END.length, PNG_END.length);

		int blockNum = len / P_CHUNK_SIZE;
		int oddSize = len % P_CHUNK_SIZE;

		int off = PNG_HEAD.length + len;

		if (oddSize != 0) {
			off -= oddSize;
			in.read(pngData, off, oddSize);
		}
		for (int i = blockNum - 1; i >= 0; i--) {
			off -= P_CHUNK_SIZE;
			in.read(pngData, off, P_CHUNK_SIZE);
		}

		in.close();
		in = null;

		//更换调色板
		byte[] ptData = getBytesFromInput(getResourceAsStream(palette));
		//要拷贝的源文件  从多少开始  要拷贝到的文件  从多少开始   拷贝多少
		System.arraycopy(ptData, 4, pngData, 41, ptData.length - 4);
		img = Image.createImage(pngData, 0, pngData.length);

		return img;
	}

	/**
	 * 填充矩形
	 * 
	 * @param g
	 *            Graphics对象
	 * @param x
	 *            位置X
	 * @param y
	 *            位置Y
	 * @param width
	 *            宽度
	 * @param height
	 *            高度
	 * @param bgClr
	 *            背景色
	 * @param edgeClr
	 *            边缘色
	 * @param hasVertex
	 *            是否绘制顶点
	 */
	public static void fillRect(Graphics g, int x, int y, int width, int height, int bgClr, int edgeClr, boolean hasVertex) {
		if (edgeClr < 0) {
			g.setColor(bgClr);
			g.fillRect(x, y, width, height);
		} else {
			g.setColor(bgClr);
			g.fillRect(x + 1, y + 1, width - 2, height - 2);

			g.setColor(edgeClr);
			if (hasVertex) {
				g.drawRect(x, y, width - 1, height - 1);
			} else {
				g.fillRect(x + 1, y, width - 2, 1);
				g.fillRect(x + 1, y + height - 1, width - 2, 1);
				g.fillRect(x, y + 1, 1, height - 2);
				g.fillRect(x + width - 1, y + 1, 1, height - 2);
			}
		}
	}
}
