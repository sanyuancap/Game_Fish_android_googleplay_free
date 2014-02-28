package com.game5a.fish_googleplay_free;

import java.util.Vector;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import com.game5a.action.ActionSet;
import com.game5a.common.Common;
import com.game5a.common.Rectangle;
import com.game5a.common.Tool;

public class Fishman extends MapElement {
	/** 动作: 站立 */
	public static final byte ACT_STAND = 0;
	/** 动作: 走动 */
	public static final byte ACT_WALK = 1;
	/** 动作: 电击 */
	public static final byte ACT_SHOCKED = 2;
	/** 动作: 钓鱼交付 */
	public static final byte ACT_HANDUP = 3;
	/** 动作ID */
	public byte actID;

	/** 状态: 正常 */
	public static final byte ACT_STATE_NORMAL = 0;
	/** 状态: 动作 */
	public static final byte ACT_STATE_ACTION = 1;
	/** 动作状态 */
	public byte actState;

	/** 钓鱼者AS */
	public ActionSet fishmanAS;
	/** 渔船AS */
	public ActionSet boatAS;

	/** 脚底初始地图坐标 */
	int mapInitX, mapInitY;

	// static final int HOOK_MOVE_DISTANCE = 225;
	/** 鱼钩移动距离 */
	public static int HOOK_MOVE_DISTANCE;
	/** 鱼钩图片 */
	Image hookImg;
	/** 鱼钩初始偏移量 */
	int hookInitDx, hookInitDy;
	/** 鱼钩偏移量 */
	public int hookDx, hookDy;
	/** 鱼钩宽高 */
	public int hookWidth, hookHeight;
	/** 鱼钩方向 */
	byte hookDir;
	/** 是否有鱼上钩 */
	public boolean bHasFishHooked;

	/** 鱼钩当前角度 */
	byte hookCurAngle;
	/** 鱼钩角度正负类型 */
	byte hookAngleType;
	/** 角度：无 */
	static final byte ANGLE_NULL = 0;
	/** 角度：正 */
	static final byte ANGLE_PLUS = 1;
	/** 角度：负 */
	static final byte ANGLE_MINUS = 2;
	/** 鱼钩摆动次数 */
	int hookWaveCount;
	/** 鱼钩摆动类型 */
	byte hookWaveType;
	/** 鱼钩摆动：无 */
	static final byte HOOK_WAVE_NULL = 0;
	/** 鱼钩摆动：向上 */
	static final byte HOOK_WAVE_UP = 1;
	/** 鱼钩摆动：向下 */
	static final byte HOOK_WAVE_DOWN = 2;

	/** 角度：0 */
	static final byte ANGLE_0 = 0;
	/** 角度：1 */
	static final byte ANGLE_1 = 1;
	/** 角度：2 */
	static final byte ANGLE_2 = 2;
	/** 角度：3 */
	static final byte ANGLE_3 = 3;
	/** 角度：4 */
	static final byte ANGLE_4 = 4;
	/** 角度：5 */
	static final byte ANGLE_5 = 5;
	/** 角度：10 */
	static final byte ANGLE_10 = 6;
	/** 角度正切tan值 */
	static final int[] ANGLE_TAN = {0, 17, 35, 52, 70, 87, 176};

	/** 渔船浮动偏移 */
	static final int[] BOAT_FLOAT_DY = {-1, -1, 0, 1, 2, 2, 1, 0};
	/** 上钩鱼的队列 */
	Vector fishSetHooked = new Vector();

	/** 是否显示Happy状态 */
	boolean bShowHappy;
	/** 已经显示Happy时间 */
	int showHappyTimes;
	/** 显示Happy总时间 */
	static final int SHOW_HAPPY_TIME_MAX = 5;

	/** 显示水花 */
	boolean bShowSpray;
	/** 显示水花时间 */
	int showSprayTimes;

	/** 电击时间 */
	int shockedTimes;

	/** 交付鱼的时间 */
	public int handUpTimes;
	/** 交付鱼的总时间 */
	int handUpTimesTotal;
	/** 交付鱼的时间间隔 */
	static final int HANDUP_TIME_INTERVAL = 10;

	/** 是否显示出现状态 */
	boolean bAppear;
	/** 显示出现时间 */
	int appearTimes;
	/** 显示出现总时间 */
	static final int APPEAR_TIME_MAX = 20;

	/** 每种船的装载量（鱼条数） */
	public static final int[] BOAT_FISH_FULL_NUMS = {10, 12, 15, 20};// whb
	// fix
	/** 船的ID */
	int boatID;
	/** 船的装载量（鱼条数） */
	int boatFullFishNum;
	/** 船的当前装载量 */
	int boatCurFishNum;
	/** 船的交付量 */
	int boatHandupFishNum;
	
	//110818
	/** 连续钓鱼成功次数 */
	public static int FishSuccessTime;
	public static boolean IsCleanFishSuccessTime;
	public static boolean IsUpdateAchFishSuccessTime;

	public Fishman(int id) {
		String actionFile = null;
		String[] imageFiles = null;

		actionFile = "/fishman" + id + ".an";
		imageFiles = new String[3];
		imageFiles[0] = "/body" + id + ".png";
		imageFiles[1] = "/head" + id + ".png";

		if (id == 2) {
			imageFiles[2] = "/body" + 0 + "e.png";
		} else {
			imageFiles[2] = "/body" + id + "e.png";
		}
		fishmanAS = ActionSet.createActionSet(actionFile, imageFiles);
		footWidth = fishmanAS.footWidth;
		footHeight = fishmanAS.footHeight;

		actID = ACT_STAND;
		// actID = ACT_SHOCKED;
	}

	public void initPos(int mx, int my) {
		mapX = mapInitX = mx;
		mapY = mapInitY = my;
	}

	/**
	 * 设置渔船
	 * 
	 * @param id
	 *            编号
	 */
	public void setBoat(int id) {
		boatID = id;

		String actionFile = "/boat" + id + ".an";
		String[] imageFiles = new String[1];
		imageFiles[0] = "/boat" + id + ".png";
		boatAS = ActionSet.createActionSet(actionFile, imageFiles);

		boatFullFishNum = BOAT_FISH_FULL_NUMS[boatID];

		boatCurFishNum = 0;

		clearFishSetHooked();
	}

	/**
	 * 设置鱼钩
	 * 
	 * @param id
	 *            编号
	 */
	public void setHook(int id) {
		hookImg = Tool.createImage("/hook" + id + ".png");

		Rectangle rect = fishmanAS.getEdgeRect(fishmanAS.frameDatas[0], false);

		hookWidth = hookImg.getWidth();
		hookHeight = hookImg.getHeight();
		hookDx = hookInitDx = rect.x + rect.width - (hookWidth >> 1);
		hookDy = hookInitDy = rect.y;

		hookDir = DIR_UP;

		bHasFishHooked = false;
	}

	/**
	 * 初始化动作
	 */
	public void initAct() {
		actID = ACT_STAND;
		actState = ACT_STATE_NORMAL;
	}

	/**
	 * 执行动作
	 * 
	 * @param act
	 *            动作ID
	 * @return 是否执行成功
	 */
	public boolean act(byte act) {
		if (actID == act) { // 当前动作
			return false;
		}
		if (actState != ACT_STATE_NORMAL) {
			return false;
		}

		actID = act;
		if (actID == ACT_STAND || actID == ACT_WALK) {
			actState = ACT_STATE_NORMAL;
		} else {
			actState = ACT_STATE_ACTION;
		}

		return true;
	}

	/**
	 * 执行动作
	 * 
	 * @param act
	 *            动作ID
	 * @param bForce
	 *            是否强制执行
	 * @return
	 */
	public boolean act(byte act, boolean bForce) {
		if (bForce) {
			initAct();
		}
		return act(act);
	}

	/**
	 * 设置方向
	 * 
	 * @param dir
	 *            方向
	 */
	public void setDir(byte dir) {
		curDir = dir;
	}

	/**
	 * 改变鱼钩移动方向
	 */
	public void switchHookDir() {
		if (!bHasFishHooked) {
			if (hookDir == DIR_UP) {
				hookDir = DIR_DOWN;
			} else {
				hookDir = DIR_UP;
			}
		}
	}

	/**
	 * 获取渔船边缘矩形
	 * 
	 * @return 渔船边缘矩形
	 */
	public Rectangle getBoatEdgeRect() {
		return boatAS.getEdgeRect(boatAS.frameDatas[0], false);
	}

	/**
	 * 获取渔船身体矩形
	 * 
	 * @return 渔船身体矩形
	 */
	public Rectangle getBoatBodyRect() {
		return boatAS.getBodyRect(boatAS.frameDatas[0], false);
	}

	/**
	 * 鱼钩循环处理
	 */
	public void cycleHook() {
		if (bHasFishHooked) {
			hookDir = DIR_UP;
			hookDy -= 12;
			if (hookDy < hookInitDy) {
				hookDy = hookInitDy;
				reapFishSetHooked();
			}
			
			//110818
			IsCleanFishSuccessTime = false;
		} else {
			if (hookDir == DIR_UP) {
				hookDy -= 6;
				if (hookDy < hookInitDy) {
					hookDy = hookInitDy;
					
					//110818
					if (IsCleanFishSuccessTime) {
						FishSuccessTime = 0;
						IsCleanFishSuccessTime = false;
					}
				}
			} else if (hookDir == DIR_DOWN) {
				hookDy += 6;
				if (hookDy > hookInitDy + HOOK_MOVE_DISTANCE) {
					hookDy = hookInitDy + HOOK_MOVE_DISTANCE;
					hookDir = DIR_UP;
				}
				
				//110818
				IsCleanFishSuccessTime = true;
			}
		}

		if (hookDy < hookInitDy + HOOK_MOVE_DISTANCE / 4) {
			hookWaveType = HOOK_WAVE_NULL;
			hookAngleType = ANGLE_NULL;
		}

		if (actID == ACT_WALK) {
			hookWaveCount = 0;

			if (curDir == DIR_LEFT) {
				if (hookAngleType == ANGLE_NULL) {
					hookAngleType = ANGLE_PLUS;
					hookWaveType = HOOK_WAVE_UP;
				} else if (hookAngleType == ANGLE_PLUS) {
					hookWaveType = HOOK_WAVE_UP;
				}
			} else if (curDir == DIR_RIGHT) {
				if (hookAngleType == ANGLE_NULL) {
					hookAngleType = ANGLE_MINUS;
					hookWaveType = HOOK_WAVE_UP;
				} else if (hookAngleType == ANGLE_MINUS) {
					hookWaveType = HOOK_WAVE_UP;
				}
			}
		}

		if (hookWaveCount < 5) {
			if (hookWaveType == HOOK_WAVE_UP) {
				hookCurAngle++;
				if (hookCurAngle > ANGLE_5) {
					hookCurAngle = ANGLE_5;
					hookWaveType = HOOK_WAVE_DOWN;
				}
			} else if (hookWaveType == HOOK_WAVE_DOWN) {
				hookCurAngle--;
				if (hookCurAngle <= ANGLE_0) {
					hookCurAngle = ANGLE_0;
					hookWaveCount++;
					if (actID == ACT_WALK) {
						if (curDir == DIR_LEFT) {
							hookAngleType = ANGLE_PLUS;
						} else if (curDir == DIR_RIGHT) {
							hookAngleType = ANGLE_MINUS;
						}
					} else {
						if (hookAngleType == ANGLE_PLUS) {
							hookAngleType = ANGLE_MINUS;
						} else if (hookAngleType == ANGLE_MINUS) {
							hookAngleType = ANGLE_PLUS;
						}
					}
					hookWaveType = HOOK_WAVE_UP;
				}
			} else {
				hookCurAngle = ANGLE_0;
			}
		}

		if (hookCurAngle > ANGLE_0) {
			int dx = (hookDy - hookInitDy) * ANGLE_TAN[hookCurAngle] / 1000;
			if (hookAngleType == ANGLE_MINUS) {
				hookDx = hookInitDx - dx;
			} else if (hookAngleType == ANGLE_PLUS) {
				hookDx = hookInitDx + dx;
			}
		} else {
			hookDx = hookInitDx;
		}
	}

	public void cycle() {
		mapY = mapInitY + BOAT_FLOAT_DY[((Tool.countTimes + 1) >> 1) % BOAT_FLOAT_DY.length];

		if (bAppear) {
			appearTimes++;
			if (appearTimes > APPEAR_TIME_MAX) {
				bAppear = false;
				appearTimes = 0;
			}
		}

		if (bShowHappy) {
			showHappyTimes++;
			if (showHappyTimes > SHOW_HAPPY_TIME_MAX) {
				bShowHappy = false;
				showHappyTimes = 0;
			}
		}

		if (bShowSpray) {
			if (FishGame.sprayAS == null) {
				bShowSpray = false;
			} else {
				if ((Tool.countTimes & 1) == 0) {
					showSprayTimes++;
				}
				if (showSprayTimes >= FishGame.sprayAS.frameNum) {
					bShowSpray = false;
					showSprayTimes = 0;
				}
			}
		}

		if (actState == ACT_STATE_NORMAL) {
			if (actID == ACT_WALK) {
				showSpray();
			}
			cycleHook();
		} else if (actID == ACT_SHOCKED) {
			shockedTimes++;
			if (shockedTimes > FishGame.SHOCKED_TIME_MAX) {
				shockedTimes = 0;
				gotoAppear();
			}
		} else if (actID == ACT_HANDUP) {
			handUpTimes++;
			if (handUpTimes % HANDUP_TIME_INTERVAL == 0) {
				System.out.println(handUpTimes / HANDUP_TIME_INTERVAL);
				// if (DoMidlet.instance.canvas.stageID == 10
				// /*&& boatCurFishNum > 10*/) {
				if (boatCurFishNum <= (10 - handUpTimes / HANDUP_TIME_INTERVAL)) {
					boatCurFishNum--;
				} else {
					boatCurFishNum -= 2;
				}
				// } else {
				// boatCurFishNum--;
				// }
			}

			if (boatCurFishNum <= 0) {
				boatCurFishNum = 0;
				handUpTimes = 0;
				DoMidlet.instance.canvas.flag1_Tip = false;
				initAct();
			}
			cycleHook();
		}
	}

	public boolean isBoatFull() {
		return boatCurFishNum >= boatFullFishNum;
	}

	public void draw(Graphics g, int viewMapX, int viewMapY, int dx, int dy) {
		if (bAppear && (Tool.countTimes & 1) == 0) {
			return;
		}

		int drawX = mapX - viewMapX + dx;
		int drawY = mapY - viewMapY + dy;

		// 绘制渔夫
		if (fishmanAS != null) {
			int frameId = 0;
			if (bShowHappy) {
				frameId = 1;
			}
			if (actID == ACT_SHOCKED) {
				frameId = 2 + (Tool.countTimes >> 1) % 2;;
			}
			fishmanAS.drawFrame(g, frameId, drawX, drawY, false);
		}

		// 绘制船
		if (boatAS != null) {
			boatAS.drawFrame(g, 0, drawX, drawY, false);

			Rectangle rect = getBoatEdgeRect();
			int fdx = drawX + rect.x - FishGame.fullFlagImg.getWidth();
			int fdy = drawY - FishGame.fullFlagImg.getHeight() + 10;
			Tool.drawImage(g, FishGame.fullFlagImg, fdx, fdy, Tool.TRANS_NONE);

			// 绘制船装载量标识
			//#if (MobileType == 6101)||(MobileType == 7260)
			//# int frdx = fdx + 3;
			//# int frdy = fdy + 7;
			//# int frw = 4;
			//# int frht = 13;
			//# int frh = 0;
			//#else
			int frdx = fdx + 4;
			int frdy = fdy + 8;
			int frw = 5;
			int frht = 21;
			int frh = 0;
			//#endif

			if (actID == ACT_HANDUP) {// whb fix 修改船的卸载量
				if (/*
					 * DoMidlet.instance.canvas.stageID == 10 &&
					 */handUpTimesTotal >= 10) {
					frh = frht * boatCurFishNum / boatFullFishNum;
				} /*
				 * else { frh = frht * (handUpTimesTotal - handUpTimes)
				 * boatHandupFishNum / (handUpTimesTotal * boatFullFishNum); }
				 */
			} else {
				frh = frht * boatCurFishNum / boatFullFishNum;
			}

			if (frh > frht) {
				frh = frht;
			} else if (frh < 0) {
				frh = 0;
			}
			g.setColor(0xff0000);
			g.fillRect(frdx, frdy + frht - frh, frw, frh);

			// 绘制船已满提示
			if (isBoatFull()) {
				Image img = FishGame.fullHintImgs[(Tool.countTimes >> 1) % FishGame.fullHintImgs.length];
				int idx = fdx - ((img.getWidth() - FishGame.fullFlagImg.getWidth()) >> 1);
				int idy = fdy - img.getHeight();
				Tool.drawImage(g, img, idx, idy, Tool.TRANS_NONE);
			}
		}

		// 绘制水花
		//#if (MobileType == 6101)||(MobileType == 7260)
		//# if (bShowSpray && FishGame.sprayAS != null) {
		//# Rectangle rect = getBoatBodyRect();
		//# if (curDir == DIR_RIGHT) {
		//# FishGame.sprayAS.drawFrame(g, showSprayTimes, drawX + rect.x
		//# + rect.width, drawY, false);
		//# } else if (curDir == DIR_LEFT) {
		//# FishGame.sprayAS.drawFrame(g, showSprayTimes, drawX + rect.x,
		//# drawY, true);
		//# }
		//# }
		//#else
		//#endif

		// 绘制鱼钩
		if (hookImg != null) {
			Common.setUIClip(g);
			g.setColor(0xffffff);
			g.drawLine(drawX + hookInitDx + (hookWidth >> 1), drawY + hookInitDy, drawX + hookDx + (hookWidth >> 1), drawY + hookDy);
			Tool.drawImage(g, hookImg, drawX + hookDx, drawY + hookDy, Tool.TRANS_NONE);
		}
	}

	public boolean isInScreen(int viewMapX, int viewMapY, int viewWidth, int viewHeight) {
		return false;
	}

	/**
	 * 判断鱼是否上钩
	 * 
	 * @param fish
	 *            鱼
	 * @return 是否上钩
	 */
	public boolean isFishHooked(Fish fish) {
		if (isBoatFull()) {
			return false;
		}
		if ((fish.actState == Fish.ACT_STATE_NORMAL || fish.actState == Fish.ACT_STATE_FAINT) && actState == ACT_STATE_NORMAL) {
			// if (Tool.isRectIntersected(mapX + hookDx, mapY + hookDy,
			// hookWidth, hookHeight, fish.mapX + fish.bodyDx, fish.mapY +
			// fish.bodyDy, fish.bodyWidth, fish.bodyHeight)) {
			// return true;
			// }
			if (Tool.isPointInRect(mapX + hookDx + (hookWidth >> 1), mapY + hookDy + (hookHeight >> 1), fish.mapX + fish.bodyDx, fish.mapY + fish.bodyDy, fish.bodyWidth, fish.bodyHeight)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断是否电鳗电击
	 * 
	 * @param fish
	 * @return
	 */
	public boolean isNumbShocked(Fish fish) {
		if (fish.actState == Fish.ACT_STATE_NORMAL && actState == ACT_STATE_NORMAL) {
			if (mapY + hookDy > fish.mapY && mapX + hookDx > fish.mapX && mapX + hookDx < fish.mapX + fish.tileWidth) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 进入电击状态
	 */
	public void gotoShocked() {
		shockedTimes = 0;
		act(ACT_SHOCKED);
	}

	/**
	 * 进入交付状态
	 */
	public void gotoHandup() {
		if (boatCurFishNum > 0) {
			handUpTimes = 0;
			boatHandupFishNum = boatCurFishNum;
			handUpTimesTotal = HANDUP_TIME_INTERVAL * boatHandupFishNum;
			act(ACT_HANDUP);
		}
	}

	/**
	 * 进入出现状态
	 */
	public void gotoAppear() {
		bAppear = true;
		appearTimes = 0;

		// act(ACT_STAND);
		initAct();
		hookDx = hookInitDx;
		hookDy = hookInitDy;
		hookDir = DIR_UP;

		clearFishSetHooked();
	}

	/**
	 * 清空钓起鱼的集合
	 */
	public void clearFishSetHooked() {
		for (int i = 0; i < fishSetHooked.size(); i++) {
			Fish fish = (Fish) fishSetHooked.elementAt(i);
			fish.bDead = true;
		}
		fishSetHooked.removeAllElements();
		bHasFishHooked = false;
	}

	/**
	 * 把鱼钓起
	 * 
	 * @param fish
	 *            鱼
	 */
	public void hookFish(Fish fish) {
		fishSetHooked.addElement(fish);
		bHasFishHooked = true;
	}

	/**
	 * 捕获钓起的鱼
	 */
	public void reapFishSetHooked() {
		for (int i = 0; i < fishSetHooked.size(); i++) {
			Fish fish = (Fish) fishSetHooked.elementAt(i);
			fish.bDead = true;
			fish.bReaped = true;

			if (fish.isFish()) {
				boatCurFishNum++;
			}
		}
		fishSetHooked.removeAllElements();
		bHasFishHooked = false;
		showHappy();
		
		//110818
		FishSuccessTime++;
		IsUpdateAchFishSuccessTime = true;
	}

	/**
	 * 显示Happy头像
	 */
	public void showHappy() {
		bShowHappy = true;
		showHappyTimes = 0;
	}

	/**
	 * 显示水花
	 */
	public void showSpray() {
		if (bShowSpray) {
			return;
		}
		bShowSpray = true;
		showSprayTimes = 0;
	}

	/**
	 * 清空状态
	 */
	public void clearStatus() {
		bShowHappy = false;
		bShowSpray = false;
	}
}
