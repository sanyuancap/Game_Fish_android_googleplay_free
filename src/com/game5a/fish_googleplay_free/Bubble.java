package com.game5a.fish_googleplay_free;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import com.game5a.common.Tool;

/**
 * ˮ��
 * 
 * @author Xavier
 * 
 */
public class Bubble extends MapElement {
	public static final byte BUBBLE_TYPE_RAD = 0;
	public static final byte BUBBLE_TYPE_LINE = 1;
	public byte bubbleType;

	/** ˮ����ʧλ��Y */
	public static int BubbleDeadY = 130;
	/** ˮ��֡��� */
	int frameIndex;
	/** ����X */
	int stepX;
	/** ����Y */
	int stepY;
	/** ��һ�β����任ʱ�� */
	int nextStepTime;
	/** �����任ʱ�� */
	int stepTimes;
	/** �Ƿ����� */
	public boolean bDead;
	/** ����ͼƬ */
	Image bubbleImg;

	public Bubble(int fIndex, int mx, int my, Image img) {
		this(BUBBLE_TYPE_RAD, fIndex, mx, my, img);
	}

	public Bubble(byte bType, int fIndex, int mx, int my, Image img) {
		bubbleType = bType;
		frameIndex = fIndex;
		mapX = mx;
		mapY = my;
		bubbleImg = img;

		getNextStep();
	}

	/**
	 * ��ȡ��һ�β�������
	 */
	public void getNextStep() {
		if (bubbleType == BUBBLE_TYPE_RAD) {
			stepX = Tool.getNextRnd(-1, 2);
			stepY = Tool.getNextRnd(0, 2);
			nextStepTime = Tool.getNextRnd(20, 40);
			stepTimes = 0;
		} else if (bubbleType == BUBBLE_TYPE_LINE) {
			stepX = 0;
			stepY = Tool.getNextRnd(-6, -1);
			nextStepTime = Tool.getNextRnd(20, 40);
			stepTimes = 0;
		}
	}

	public static void setDeadY(int y) {
		BubbleDeadY = y;
	}

	public void cycle() {
		if (bDead) {
			return;
		}

		if (bubbleType == BUBBLE_TYPE_RAD) {
			if (mapY < BubbleDeadY) {
				bDead = true;
				return;
			}

			if (stepY > 0 || (Tool.countTimes % 2 == 0)) {
				mapY--;
			}

			mapX += stepX;

			stepTimes++;
			if (stepTimes > nextStepTime) {
				getNextStep();
			}
		} else if (bubbleType == BUBBLE_TYPE_LINE) {
			if (mapY < 0 || stepTimes > nextStepTime) {
				bDead = true;
				return;
			}
			mapY += stepY;
			stepTimes++;
		}
	}

	public void draw(Graphics g, int viewMapX, int viewMapY, int dx, int dy) {
		if (bDead) {
			return;
		}

		int drawX = mapX - viewMapX + dx;
		int drawY = mapY - viewMapY + dy;
		int fw = bubbleImg.getWidth() / FishGame.BUBBLE_TILE_NUM;
		int fh = bubbleImg.getHeight();

		Tool.drawTile(g, bubbleImg, frameIndex, fw, fh, bubbleType, drawX, drawY);
	}

	public boolean isInScreen(int viewMapX, int viewMapY, int viewWidth, int viewHeight) {
		return false;
	}

}
