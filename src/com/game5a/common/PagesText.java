package com.game5a.common;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

/**
 * ҳ���ı�
 * 
 */
public class PagesText {
	/** ��������: ҳ��߶� */
	public static final byte SET_PAGE_HEIGHT = 0;
	/** ��������: ҳ����ʾ���� */
	public static final byte SET_PAGE_LINE_NUM = 1;

	/** ����: ���·�ҳ */
	public static final byte STYLE_UP_DOWN = 0;
	/** ����: ���ҷ�ҳ */
	public static final byte STYLE_LEFT_RIGHT = 1;
	/** ���� */
	byte style;

	/** ��ʾ���� */
	String[] text;

	/** ��� */
	int width;
	/** �߶� */
	int height;
	/** �и� */
	int lineHeight;

	/** ���� */
	Font font;

	/** ������ */
	int lineTotalNum;
	/** ÿҳ������ */
	int lineNumInPage;
	/** ҳ�� */
	int pageNum;
	/** ҳ��� */
	int pageIndex;

	/** �Ƿ���Ƽ�ͷ */
	boolean bDrawArrow;
	/** �Ƿ�ط� */
	boolean bRollAble = true;
	/** �Ƿ��ı����� */
	boolean bTextEnd;

	/**
	 * ���캯��
	 * 
	 * @param str
	 *            ��ʾ����
	 * @param w
	 *            ���
	 * @param h
	 *            �߶�
	 * @param lh
	 *            �и�
	 * @param fn
	 *            ����
	 * @param ty
	 *            ����
	 */
	public PagesText(String str, int w, int h, int lh, Font fn, byte ty) {
		this(str, SET_PAGE_HEIGHT, w, h, lh, fn, ty, true);
	}

	/**
	 * ���캯��
	 * 
	 * @param str
	 *            ��ʾ����
	 * @param setType
	 *            ��������
	 * @param w
	 *            ���
	 * @param h
	 *            ��������: ҳ��߶�, Ϊ�߶� / ��������: ҳ����ʾ����, Ϊҳ����ʾ����
	 * @param lh
	 *            �и�
	 * @param fn
	 *            ����
	 * @param ty
	 *            ����
	 * @param bAutoShowArrow
	 *            �Ƿ���ʾ��ҳ��ͷ
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
	 * ��ȡ���
	 * 
	 * @return ���
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * ��ȡ�߶�
	 * 
	 * @return �߶�
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * �Ƿ��һҳ
	 * 
	 * @return �Ƿ��һҳ
	 */
	public boolean isHead() {
		if (pageIndex <= 0) {
			return true;
		}
		return false;
	}

	/**
	 * �Ƿ��ı�����
	 * 
	 * @return �Ƿ��ı�����
	 */
	public boolean isEnd() {
		return bTextEnd;
	}

	/**
	 * �����Ƿ�ط�
	 * 
	 * @param b
	 *            �Ƿ�ط�
	 */
	public void setRoll(boolean b) {
		bRollAble = b;
	}

	/**
	 * ��������
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
