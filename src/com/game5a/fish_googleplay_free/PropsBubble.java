package com.game5a.fish_googleplay_free;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import com.game5a.common.Tool;

/**
 * 道具水泡
 * 
 * @author Xavier
 * 
 */
public class PropsBubble extends MapElement {
	/** 道具消失位置Y */

	public static int PROPS_FADE_Y;
	/** 道具类型 */
	byte type;

	/** 状态：正常 */
	public static final byte ACT_STATE_NORMAL = 0;
	/** 状态：消失 */
	public static final byte ACT_STATE_FADE = 1;
	/** 当前状态 */
	public byte actState;

	/** 消失状态总时间 */
	static final int FADE_TIME_MAX = 8;
	/** 消失状态时间 */
	int fadeTimes;

	/** 是否死亡 */
	public boolean bDead;

	/** 道具图片 */
	Image itemImg;

	/** 图片切块宽高 */
	public int tileWidth, tileHeight;

	public PropsBubble(byte ty, int mx, int my, Image img) {
		type = ty;
		mapX = mx;
		mapY = my;
		itemImg = img;

		tileWidth = itemImg.getWidth();
		tileHeight = itemImg.getHeight();

		bDead = false;
		actState = ACT_STATE_NORMAL;
	}

	/**
	 * 进入消失状态
	 */
	public void gotoFade() {
		fadeTimes = 0;
		actState = ACT_STATE_FADE;
	}

	public void cycle() {
		if (bDead) {
			return;
		}

		if (actState == ACT_STATE_FADE) {
			fadeTimes++;
			if (fadeTimes >= FADE_TIME_MAX) {
				bDead = true;
			}
			return;
		}

		if (mapY < PROPS_FADE_Y) {
			gotoFade();
			return;
		}

		mapY--;
	}

	public void draw(Graphics g, int viewMapX, int viewMapY, int dx, int dy) {
		if (bDead) {
			return;
		}

		int drawX = mapX - viewMapX + dx;
		int drawY = mapY - viewMapY + dy;

		if (actState == ACT_STATE_NORMAL) {
			//Tool.drawTile(g, itemImg, 0, tileWidth, tileHeight, Tool.TRANS_NONE, drawX, drawY);
			Tool.drawImage(g, itemImg, drawX, drawY, Tool.TRANS_NONE);
		} else if (actState == ACT_STATE_FADE) {
			//Tool.drawTile(g, itemImg, (fadeTimes >> 1) + 1, tileWidth, tileHeight, Tool.TRANS_NONE, drawX, drawY);
			Tool.drawTile(g, FishGame.propsFadeImg, fadeTimes >> 1, FishGame.propsFadeImg.getWidth() / 4, FishGame.propsFadeImg.getHeight(), Tool.TRANS_NONE, drawX, drawY);
		}
	}

	public boolean isInScreen(int viewMapX, int viewMapY, int viewWidth, int viewHeight) {
		return false;
	}

}
