package com.game5a.fish_googleplay_free;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import com.game5a.common.Tool;

/**
 * 飞行数字
 * 
 * @author Xavier
 * 
 */
public class FlyNum {
	/** 飞行数字类型：积分 */
	public static final byte NUM_TYPE_SCORE = 0;
	/** 飞行数字类型：金币 */
	public static final byte NUM_TYPE_GOLD = 1;
	/** 类型 */
	public byte type;

	public static final int STEP = 8;
	/** Y方向步长 */
	public static final int STEP_Y = 5;
	/** 数值 */
	public int number;
	/** 数字图片 */
	public Image numImg;
	/** 初始位置 */
	int initPosX, initPosY;
	/** 当前位置 */
	int posX, posY;
	/** 目的位置 */
	int desPosX, desPosY;
	/** 存在时间 */
	int liveTimes;
	/** 是否死亡 */
	boolean bDead;
	/** 是否显示 */
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

		// 绘制小星星
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

		// 绘制得分
		Tool.drawImageNum(g, numImg, String.valueOf(number), posX, posY, numImg.getWidth() / 10, 0);
	}
}
