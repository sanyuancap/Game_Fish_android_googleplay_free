package com.game5a.fish_googleplay_free;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import com.game5a.common.Common;
import com.game5a.common.Tool;

/**
 * ǱˮԱ�� whb add
 */
public class Diver extends MapElement {
	/** ********************************************************** */
	/** ״̬������ */
	public static final byte ACT_STATE_NORMAL = 0;
	/** ״̬��ץ�� */
	public static final byte ACT_STATE_CATCH = 1;// �Ƿ����ǱˮԱ
	/** ״̬��Ǳˮ */
	public static final byte ACT_STATE_DIVE = 2;

	/** ********************************************************** */
	Image diverImg;
	byte curDir = DIR_RIGHT;
	byte curDirV = DIR_UP;
	static final int TILE_NUM = 5;
	int mapX, mapY;
	public int tileWidth, tileHeight;

	static final int space = 20;

	// ǱˮԱ�ƶ��ٶ�
	static int step;
	static final int[] stepType = {1, 1, 1};
	static final int[] stepType2 = {6, 6, 6};

	// Ǳˮ������Ǳˮ�ƶ��ٶ�
	static final int diveStep = 2;

	// ��ʼʱ���Ǳˮ�ٶ�

	int actState;

	/** ����: վ�� */
	public static final byte ACT_STAND = 0;
	/** ����: �߶� */
	public static final byte ACT_WALK = 1;

	byte actID;// 0Ϊ���ߣ�1Ϊ���������ֻ�ڿ���ǱˮԱ����

	public Diver(byte typeID) {

		diverImg = Tool.createImage("/scubaDiver" + (typeID + 1) + ".png");
		tileWidth = diverImg.getWidth() / TILE_NUM;
		tileHeight = diverImg.getHeight();

		// ��ʼ��
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
		//			// �������
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
		//			// �ƶ�
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
		//		} else if (actState == ACT_STATE_CATCH) {// �ֶ�����
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
