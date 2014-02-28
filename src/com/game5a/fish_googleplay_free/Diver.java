package com.game5a.fish_googleplay_free;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import com.game5a.common.Common;
import com.game5a.common.Tool;

/**
 * 潜水员类 whb add
 */
public class Diver extends MapElement {
	/** ********************************************************** */
	/** 状态：正常 */
	public static final byte ACT_STATE_NORMAL = 0;
	/** 状态：抓鱼 */
	public static final byte ACT_STATE_CATCH = 1;// 是否控制潜水员
	/** 状态：潜水 */
	public static final byte ACT_STATE_DIVE = 2;

	/** ********************************************************** */
	Image diverImg;
	byte curDir = DIR_RIGHT;
	byte curDirV = DIR_UP;
	static final int TILE_NUM = 5;
	int mapX, mapY;
	public int tileWidth, tileHeight;

	static final int space = 20;

	// 潜水员移动速度
	static int step;
	static final int[] stepType = {1, 1, 1};
	static final int[] stepType2 = {6, 6, 6};

	// 潜水后向下潜水移动速度
	static final int diveStep = 2;

	// 开始时候的潜水速度

	int actState;

	/** 动作: 站立 */
	public static final byte ACT_STAND = 0;
	/** 动作: 走动 */
	public static final byte ACT_WALK = 1;

	byte actID;// 0为行走，1为不动，这个只在控制潜水员有用

	public Diver(byte typeID) {

		diverImg = Tool.createImage("/scubaDiver" + (typeID + 1) + ".png");
		tileWidth = diverImg.getWidth() / TILE_NUM;
		tileHeight = diverImg.getHeight();

		// 初始化
		switch (typeID) {
			case 0 :

				break;
		}
		step = stepType[typeID];

//		mapX = 30;
//		mapY = 180;
		
		mapX = Common.uiWidth >> 1;
		mapY = Common.uiHeight >> 1;
		
		actState = ACT_STATE_CATCH;
		actState = ACT_STATE_NORMAL;
		// DiveSpeed = 40;
		bMirror = false;

		curDir = DIR_RIGHT;// ////////
	}

	void setControlDiverStep(byte typeID) {
		step = stepType2[typeID];
	}

	void setAutoDiverStep(byte typeID) {
		step = stepType[typeID];
	}

	void freeDiverRes() {
		diverImg = null;
	}

	public void cycle() {
		//		if (actState == ACT_STATE_NORMAL) {
		//
		//			// 方向控制
		//			if (mapX < DoMidlet.instance.canvas.viewMapX + space) {
		//				curDir = DIR_LEFT;
		//			} else if (mapX > DoMidlet.instance.canvas.viewMapX + FishGame.SCREEN_WIDTH - tileWidth - space) {
		//				curDir = DIR_RIGHT;
		//			}
		//
		//			if (mapY > DIVER_AUTO_MAX_Y) {
		//				diverDir = 0;
		//			} else if (mapY < DIVER_AUTO_MIN_Y) {
		//				diverDir = -1;
		//			}
		//
		//			// 移动
		//			if (curDir == DIR_LEFT) {
		//				mapX += step;
		//				bMirror = false;
		//			} else {
		//				mapX -= step;
		//				bMirror = true;
		//			}
		//			if (diverDir == 0) {
		//				mapY -= step;
		//			} else {
		//				mapY += step;
		//			}
		//		} else if (actState == ACT_STATE_CATCH) {// 手动捕鱼
		//			//
		//		}
	}

	//	int diverDir = 0;

	public void draw(Graphics g, int viewMapX, int viewMapY, int dx, int dy) {
		int drawX = mapX - viewMapX + dx;
		int drawY = mapY - viewMapY + dy;

		int drawType = Tool.TRANS_NONE;
		if (bMirror) {
			drawType = Tool.TRANS_MIRROR;
		}

		// System.out.println("wave=" + wave);
		Tool.drawTile(g, diverImg, diverActionFrame[(Tool.countTimes >> 2) % TILE_NUM], tileWidth, tileHeight, drawType, drawX, drawY + (Tool.countTimes >> 3) % 2);
	}

	public boolean isInScreen(int viewMapX, int viewMapY, int viewWidth, int viewHeight) {
		return false;
	}

	static final int[] waveFrame = {2, 5, 8, 11, 14, 11, 8, 5, 2};
	static final int[] diverActionFrame = {0, 1, 2, 0, 3, 4};
}
