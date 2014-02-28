package com.game5a.fish_googleplay_free;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import com.game5a.common.Tool;

/**
 * ��������
 * 
 * @author Xavier
 * 
 */
public class FlyNum {
	/** �����������ͣ����� */
	public static final byte NUM_TYPE_SCORE = 0;
	/** �����������ͣ���� */
	public static final byte NUM_TYPE_GOLD = 1;
	/** ���� */
	public byte type;

	public static final int STEP = 8;
	/** Y���򲽳� */
	public static final int STEP_Y = 5;
	/** ��ֵ */
	public int number;
	/** ����ͼƬ */
	public Image numImg;
	/** ��ʼλ�� */
	int initPosX, initPosY;
	/** ��ǰλ�� */
	int posX, posY;
	/** Ŀ��λ�� */
	int desPosX, desPosY;
	/** ����ʱ�� */
	int liveTimes;
	/** �Ƿ����� */
	boolean bDead;
	/** �Ƿ���ʾ */
	private boolean bShown;

	public FlyNum(byte ty, int value, Image img, int px, int py, int desX, int desY) {
		type = ty;
		number = value;
		numImg = img;

		initPosX = posX = px;
		initPosY = posY = py;
		desPosX = desX;
		desPosY = desY;

		// life = (desPosY - initPosY) / STEP_Y + 1;
		liveTimes = 0;
		bDead = false;
		bShown = false;
	}

	public void show() {
		bShown = true;
	}

	//	public void cycle() {
	//		if (!bShown || bDead) {
	//			return;
	//		}
	//
	//		if (desPosY < initPosY) {
	//			posY = initPosY - STEP_Y * liveTimes;
	//			if (posY < desPosY) {
	//				bDead = true;
	//			}
	//		} else {
	//			posY = initPosY + STEP_Y * liveTimes;
	//			if (posY >= desPosY) {
	//				bDead = true;
	//			}
	//		}
	//
	//		if (bDead) {
	//			posY = desPosY;
	//			posX = desPosX;
	//		} else {
	//			posX = initPosX + (desPosX - initPosX) * (posY - initPosY) / (desPosY - initPosY);
	//		}
	//
	//		liveTimes++;
	//	}

	public void cycle() {
		if (!bShown || bDead) {
			return;
		}

		int dx = desPosX - posX;
		int dy = desPosY - posY;
		int dis = (int) Math.sqrt(dx * dx + dy * dy);
		int stepX = 0;
		int stepY = 0;
		if (dis != 0) {
			stepX = STEP * dx / dis;
			stepY = STEP * dy / dis;
		}
		if (posX < desPosX) {
			posX += stepX;
			if (posX > desPosX) {
				posX = desPosX;
			}
		} else if (posX > desPosX) {
			posX += stepX;
			if (posX < desPosX) {
				posX = desPosX;
			}
		}

		if (posY < desPosY) {
			posY += stepY;
			if (posY > desPosY) {
				posY = desPosY;
			}
		} else if (posY > desPosY) {
			posY += stepY;
			if (posY < desPosY) {
				posY = desPosY;
			}
		}

		if (posX == desPosX && posY == desPosY) {
			bDead = true;
		}

		liveTimes++;
	}

	public void draw(Graphics g) {
		if (!bShown || bDead) {
			return;
		}

		if (numImg == null) {
			return;
		}

		// ����С����
		int rx = posX;
		int ry = posY;
		int rw = numImg.getWidth() / 5;
		int rh = numImg.getHeight() * 3 / 2;
		int starTileNum = 3;
		int starNum = 5;
		if (type == NUM_TYPE_GOLD) {
			starNum = 3;
		}

		//#if (MobileType == 6101)||(MobileType == 7260)||(Series==V600)
		//#else
		for (int i = 0; i < starNum; i++) {
			int starIndex = Tool.getNextRnd(0, starTileNum);
			int starX = Tool.getNextRnd(rx, rx + rw);
			int starY = Tool.getNextRnd(ry, ry + rh);
			Tool.drawTile(g, FishGame.stars1Img, starIndex, FishGame.stars1Img.getWidth() / starTileNum, FishGame.stars1Img.getHeight(), Tool.TRANS_NONE, starX, starY);
		}
		//#endif

		// ���Ƶ÷�
		Tool.drawImageNum(g, numImg, String.valueOf(number), posX, posY, numImg.getWidth() / 10, 0);
	}
}
