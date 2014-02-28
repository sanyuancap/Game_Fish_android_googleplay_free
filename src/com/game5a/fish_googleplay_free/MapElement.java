package com.game5a.fish_googleplay_free;

import javax.microedition.lcdui.Graphics;

/**
 * 地图元素类, 剧情场景与战斗场景的地图元素总父类, 地图元素在绘制的时候需要进行Y轴排序
 * 
 */
public abstract class MapElement {
	/** 类型 */
	public byte type;
	/** 类型: NPC */
	public static final byte TYPE_NPC = 0;
	/** 类型: 景物 */
	public static final byte TYPE_SCENERY = 1;
	/** 类型: 主角 */
	public static final byte TYPE_ROLE = 2;
	/** 类型: 敌人 */
	public static final byte TYPE_ENEMY = 3;
	/** 类型: 粒子 */
	public static final byte TYPE_PARTICLE = 4;

	/** 方向: 上 */
	public static final byte DIR_UP = 0;
	/** 方向: 下 */
	public static final byte DIR_DOWN = 1;
	/** 方向: 左 */
	public static final byte DIR_LEFT = 2;
	/** 方向: 右 */
	public static final byte DIR_RIGHT = 3;

	/** 方向: 右上 */
	public static final byte DIR_RIGHT_UP = 4;
	/** 方向: 右下 */
	public static final byte DIR_RIGHT_DOWN = 5;
	/** 方向: 左下 */
	public static final byte DIR_LEFT_DOWN = 6;
	/** 方向: 左上 */
	public static final byte DIR_LEFT_UP = 7;

	/** ID, 唯一标识 */
	public int ID;

	/** 地图X轴坐标 */
	public int mapX;
	/** 地图Y轴坐标 */
	public int mapY;
	/** 脚底宽, 用于地图元素碰撞检测 */
	public int footWidth;
	/** 脚底高, 用于地图元素碰撞检测 */
	public int footHeight;
	/** 当前方向 */
	public byte curDir;

	/** 是否消失, 消失即不显示 */
	public boolean bDisappear;
	/** 是否检测地图碰撞 */
	public boolean bCheckMapBlock;
	/** 是否镜像处理 */
	public boolean bMirror;

	/**
	 * 获取相反方向
	 * 
	 * @param dir
	 *            方向
	 * @return 相反方向
	 */
	public static byte getOpDir(byte dir) {
		switch (dir) {
			case DIR_UP :
				return DIR_DOWN;
			case DIR_DOWN :
				return DIR_UP;
			case DIR_LEFT :
				return DIR_RIGHT;
			case DIR_RIGHT :
				return DIR_LEFT;

			case DIR_RIGHT_UP :
				return DIR_LEFT_DOWN;
			case DIR_RIGHT_DOWN :
				return DIR_LEFT_UP;
			case DIR_LEFT_DOWN :
				return DIR_RIGHT_UP;
			case DIR_LEFT_UP :
				return DIR_RIGHT_DOWN;
			default :
				return DIR_UP;
		}
	}

	/**
	 * 循环处理
	 */
	public abstract void cycle();

	/**
	 * 绘制
	 * 
	 * @param g
	 *            Graphic对象
	 * @param viewMapX
	 *            绘制屏幕在地图上的位置X
	 * @param viewMapY
	 *            绘制屏幕在地图上的位置Y
	 * @param dx
	 *            偏移量X
	 * @param dy
	 *            偏移量Y
	 */
	public abstract void draw(Graphics g, int viewMapX, int viewMapY, int dx, int dy);

	/**
	 * 是否在屏幕内
	 * 
	 * @param viewMapX
	 *            绘制屏幕在地图上的位置X
	 * @param viewMapY
	 *            绘制屏幕在地图上的位置Y
	 * @param viewWidth
	 *            绘制屏幕宽
	 * @param viewHeight
	 *            绘制屏幕高
	 * @return 是否在屏幕内
	 */
	public abstract boolean isInScreen(int viewMapX, int viewMapY, int viewWidth, int viewHeight);
}
