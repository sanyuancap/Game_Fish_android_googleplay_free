package com.game5a.fish_googleplay_free;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import cn.domob.android.ads.DomobInterstitialAd;
import cn.domob.android.ads.DomobInterstitialAdListener;
import cn.domob.android.ads.DomobAdManager.ErrorCode;
import com.game5a.common.Common;
import com.game5a.common.Tool;
public class Fish extends MapElement {
	/** 每种鱼的分数 */
	public static final int[] FISH_SCORES = { 10, 12, 14, 16, 18,//
			20, 22, 24, 26, 28,//
			50 };
	/** 电鳗 */
	public static final byte FISH_TYPE_NUMB = 9;
	/** 水底金币 */
	public static final byte FISH_TYPE_GOLDCOIN = 100;
	/** 鱼的种类（100以上为水底道具） */
	public byte fishType;
	/** 鱼图片的切块数 */
	public static final int TILE_NUM = 5;
	/** 鱼图片 */
	public Image fishImg;
	/** 切块宽高 */
	public int tileWidth, tileHeight;
	/** 移动步长 */
	int step;
	/** 状态：正常 */
	public static final byte ACT_STATE_NORMAL = 0;
	/** 状态：被钓到 */
	public static final byte ACT_STATE_HOOKED = 1;
	/** 状态：晕 */
	public static final byte ACT_STATE_FAINT = 2;
	/** 状态：电击 */
	public static final byte ACT_STATE_SHOCK = 3;
	/** 当前状态 */
	public byte actState;
	/** 身体矩形偏移量 */
	public int bodyDx, bodyDy;
	/** 身体矩形宽高 */
	public int bodyWidth, bodyHeight;
	/** 是否死亡 */
	public boolean bDead;
	/** 是否跑出地图 */
	public boolean bExitMap;
	/** 是否收获 */
	public boolean bReaped;
	/** 鱼图片切块播放索引 */
	public static final int[] frameIndexs = { 0, 1, 2, 1, 0, 3, 4, 3 };
	/** 鱼切块第一帧索引 */
	int firstFrameIndex;
	/** 鱼分数 */
	int score;
	/** 眩晕状态，星星显示位置总数 */
	static final int STARS_POS_NUM = 9;
	/** 眩晕状态，星星显示位置便宜量X */
	static final int[] STARS_DX = { -16, -11, -4, 3, 9, 9, 3, -5, -12 };
	/** 眩晕状态，星星显示位置便宜量Y */
	static final int[] STARS_DY = { -3, 1, 2, 1, -1, -5, -8, -9, -7 };
	/** 眩晕状态，星星显示的第一帧序号 */
	int starFirstIndex;
	/** 眩晕状态时间 */
	int faintTimes;
	/** 眩晕状态总时间 */
	static final int FAINT_TIME_MAX = 100;
	/** 电击时间（电鳗） */
	int shockTimes;
	/** 是否出现状态 */
	boolean bAppear;
	/** 出现状态时间 */
	int appearTimes;
	/** 出现状态总时间 */
	static final int APPEAR_TIME_MAX = 20;
	public Fish(byte fType, int mx, int my, byte dir, Image img) {
		fishType = fType;
		fishImg = img;
		mapX = mx;
		mapY = my;
		curDir = dir;
		if (isFish()) {
			if (fishType == 10) {
				// #if (MobileType == 6101)||(MobileType ==
				// 7260)||(Series==D508)||(Series==V600)
				// #else
				// whb 如果是鲨鱼的话，切片数为7个
				tileWidth = fishImg.getWidth() / 7;
				tileHeight = fishImg.getHeight();
				// whb 如果是鲨鱼的话，速度要比普通鱼快
				step = 4/* Tool.getNextRnd(4, 5) */;
				// whb 考虑到鲨鱼是吃鱼碰撞，因此，其身体矩形在头部位置
				bodyWidth = (tileWidth >> 2) + 10;
				bodyHeight = 50;
				bodyDy = 15;
				// #endif
			} else {
				tileWidth = fishImg.getWidth() / TILE_NUM;
				tileHeight = fishImg.getHeight();
				step = Tool.getNextRnd(1, 3);
				// 身体宽度=图片宽度*2/3
				bodyWidth = tileWidth * 2 / 3;
				// 身体高度=图片高度*2/3
				bodyHeight = tileHeight * 2 / 3;
				bodyDy = (tileHeight - bodyHeight) >> 1;
			}
			if (curDir == DIR_RIGHT) {
				bMirror = false;
				bodyDx = tileWidth - bodyWidth;
			} else {
				bMirror = true;
				bodyDx = 0;
			}
			firstFrameIndex = Tool.getNextRnd(0, frameIndexs.length);
			score = FISH_SCORES[fishType] / 2; // FISH_SCORES[fishType];
												// //110723
		} else {
			tileWidth = fishImg.getWidth();
			tileHeight = fishImg.getHeight();
			step = 0;
			// 身体宽度=图片宽度*2/3
			bodyWidth = tileWidth;
			// 身体高度=图片高度*2/3
			bodyHeight = tileHeight;
			bMirror = false;
			bodyDx = (tileWidth - bodyWidth) >> 1;
			bodyDy = (tileHeight - bodyHeight) >> 1;
			firstFrameIndex = 0;
			// TODO test
			score = 30;
		}
		bDead = false;
		bExitMap = false;
		bReaped = false;
	}
	public void cycle() {
		if (bAppear) {
			appearTimes++;
			if (appearTimes > APPEAR_TIME_MAX) {
				bAppear = false;
				appearTimes = 0;
			}
		}
		if (actState == ACT_STATE_NORMAL) {
			// if (bFaint) {
			// faintTimes++;
			// if (faintTimes > FAINT_TIME_MAX) {
			// bFaint = false;
			// faintTimes = 0;
			// }
			// } else {
			if (curDir == DIR_RIGHT) {
				mapX += step;
			} else {
				mapX -= step;
			}
			// }
		} else if (actState == ACT_STATE_FAINT) {
			faintTimes++;
			if (faintTimes > FAINT_TIME_MAX) {
				actState = ACT_STATE_NORMAL;
				faintTimes = 0;
			}
		} else if (actState == ACT_STATE_SHOCK) {
			shockTimes++;
			if (shockTimes > FishGame.SHOCKED_TIME_MAX) {
				actState = ACT_STATE_NORMAL;
				shockTimes = 0;
			}
		}
	}
	public void draw(Graphics g, int viewMapX, int viewMapY, int dx, int dy) {
		if (bAppear && (Tool.countTimes & 1) == 0) {
			return;
		}
		int drawX = mapX - viewMapX + dx;
		int drawY = mapY - viewMapY + dy;
		if (isFish()) {
			int drawType = Tool.TRANS_NONE;
			if (actState == ACT_STATE_HOOKED) {
				drawType = Tool.TRANS_ROT270;
			} else if (bMirror) {
				drawType = Tool.TRANS_MIRROR;
			}
			int drawIndex;
			if (fishType == FISH_TYPE_NUMB && actState == ACT_STATE_SHOCK) {
				int fn2 = 3;
				int fw2 = FishGame.numbFishImg2.getWidth() / fn2;
				int fh2 = FishGame.numbFishImg2.getHeight();
				Tool.drawTile(g, FishGame.numbFishImg2, (Tool.countTimes >> 1)
						% fn2, fw2, fh2, drawType, drawX
						+ ((tileWidth - fw2) >> 1), drawY
						+ ((tileHeight - fh2) >> 1));
				int fn1 = 2;
				int fw1 = FishGame.numbFishImg1.getWidth() / fn1;
				int fh1 = FishGame.numbFishImg1.getHeight();
				Tool.drawTile(g, FishGame.numbFishImg1, (Tool.countTimes >> 1)
						% fn1, fw1, fh1, drawType, drawX
						+ ((tileWidth - fw1) >> 1), drawY
						+ ((tileHeight - fh1) >> 1));
			} else {
				if (actState == ACT_STATE_FAINT) {
					// drawIndex = firstFrameIndex;
					drawIndex = frameIndexs[firstFrameIndex];
				} else {
					// drawIndex = ((Tool.countTimes >> 1) +
					// firstFrameIndex)%TILE_NUM;
					drawIndex = frameIndexs[((Tool.countTimes >> 1) + firstFrameIndex)
							% frameIndexs.length];
				}
				if (fishType == 10) {// whb 如果是鲨鱼的话，切片数为9个
					// #if (MobileType == 6101)||(MobileType ==
					// 7260)||(Series==D508)||(Series==V600)
					// #else
					// 鲨鱼有不同的动作，因此，需要根据这些做动作判断
					int drawIndexID = 0;
					if (shark_ActID == SHARK_MOVE) {
						drawIndexID = drawIndex;
					} else if (shark_ActID == SHARK_CATCH) {
						drawIndexID = (((Tool.countTimes / 2) % 2) + 5);
					} else if (shark_ActID == SHARK_CANNOT_CATCH) {
						drawIndexID = ((Tool.countTimes / 2) % 2) + 7;
					}
					Tool.drawTile(g, fishImg, drawIndexID,
							fishImg.getWidth() / 7, fishImg.getHeight(),
							drawType, drawX, drawY);
					// #endif
				} else {
					Tool.drawTile(g, fishImg, drawIndex, fishImg.getWidth()
							/ TILE_NUM, fishImg.getHeight(), drawType, drawX,
							drawY);
				}
				// 绘制“小心”提示
				if (fishType == FISH_TYPE_NUMB && actState == ACT_STATE_NORMAL) {
					Tool.drawImage(g, FishGame.careImg, drawX
							+ ((tileWidth - FishGame.careImg.getWidth()) >> 1),
							drawY - FishGame.careImg.getHeight()
									+ (Tool.countTimes >> 2) % 2,
							Tool.TRANS_NONE);
				}
			}
			if (actState == ACT_STATE_FAINT) {
				int fw = 7;
				int fh = 7;
				int num = FishGame.stars0Img.getWidth() / fw;
				int starCx = drawX + (tileWidth / 3);
				if (curDir == DIR_RIGHT) {
					starCx = drawX + (tileWidth * 2 / 3);
				}
				int starCy = drawY - 5;
				int posIndex = (starFirstIndex + (Tool.countTimes >> 1))
						% STARS_POS_NUM;
				// Tool.drawTile(g, CommonRes.stars0Img, Tool.countTimes % num,
				// fw, fh, Tool.TRANS_NONE, starCx + STARS_DX[posIndex], starCy
				// + STARS_DY[posIndex]);
				for (int i = 0; i < 3; i++) {
					posIndex = (posIndex + 3) % STARS_POS_NUM;
					Tool.drawTile(g, FishGame.stars0Img,
							(Tool.countTimes + i * 2) % num, fw, fh,
							Tool.TRANS_NONE, starCx + STARS_DX[posIndex],
							starCy + STARS_DY[posIndex]);
				}
			}
		} else {
			Tool.drawImage(g, fishImg, drawX, drawY, Tool.TRANS_NONE);
		}
	}
	public boolean isInScreen(int viewMapX, int viewMapY, int viewWidth,
			int viewHeight) {
		return false;
	}
	/**
	 * 判断是否跑出地图
	 * 
	 * @param mapWidth
	 *            地图宽度
	 * @return 是否跑出地图
	 */
	public boolean isExitMap(int mapWidth) {
		if (actState == Fish.ACT_STATE_NORMAL) {
			if (curDir == MapElement.DIR_RIGHT) {
				if (mapX > mapWidth) {
					bExitMap = true;
					return true;
				}
			} else if (curDir == MapElement.DIR_LEFT) {
				if (mapX < -tileWidth) {
					bExitMap = true;
					return true;
				}
			}
		}
		return false;
	}
	/**
	 * 进入被钓起状态
	 */
	public void gotoHooked() {
		clearStatus();
		actState = ACT_STATE_HOOKED;
	}
	/**
	 * 进入眩晕状态
	 */
	public void gotoFaint() {
		if (!isFish()) {
			return;
		}
		if (actState == ACT_STATE_NORMAL) {
			actState = ACT_STATE_FAINT;
			faintTimes = 0;
			starFirstIndex = Tool.getNextRnd(0, STARS_POS_NUM);
		}
	}
	/**
	 * 进入电击状态
	 */
	public void gotoShock() {
		if (actState == ACT_STATE_NORMAL) {
			actState = ACT_STATE_SHOCK;
			shockTimes = 0;
		}
	}
	/**
	 * 进入出现状态
	 */
	public void gotoAppear() {
		bAppear = true;
		appearTimes = 0;
	}
	/**
	 * 清除状态
	 */
	public void clearStatus() {
		// bFaint = false;
	}
	/**
	 * 判断是否鱼类
	 * 
	 * @return 是否鱼类
	 */
	public boolean isFish() {
		return fishType < FISH_TYPE_GOLDCOIN;
	}
	/************************* whb add ******************************/
	// 由于鲨鱼图片做在一张图上，因此，用动作ID的方式来区分鲨鱼动作
	byte shark_ActID;
	static final byte SHARK_MOVE = 0;
	static final byte SHARK_CATCH = 1;
	static final byte SHARK_CANNOT_CATCH = 2;
}
