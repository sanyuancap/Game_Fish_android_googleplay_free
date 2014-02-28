package com.game5a.action;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.InputStream;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import com.game5a.common.GZIP;
import com.game5a.common.Rectangle;
import com.game5a.common.Tool;

/**
 * 动作集合
 * 
 * @author xFu
 * 
 */
public class ActionSet {
	/** 图片数量 */
	public byte imageNum;
	/** 图片矩形块数 */
	public short[] rectNum;
	/** 矩形块数据 */
	public Rectangle[][] imageRects;

	/** 帧数 */
	public short frameNum;
	/** 帧数据 */
	public FrameData[] frameDatas;

	/** 动作数 */
	public short actionNum;
	/** 动作数据 */
	public ActionData[] actionDatas;

	/** 脚底宽 */
	public int footWidth;
	/** 脚底高 */
	public int footHeight;

	/** 动画有关图片 */
	public Image[] images;

	public ActionSet() {

	}

	/**
	 * 创建动作集合
	 * 
	 * @param actionFile
	 *            动作文件
	 * @param imageFiles
	 *            图片文件数组
	 * @return 动作集合对象
	 */
	public static ActionSet createActionSet(String actionFile, String[] imageFiles) {
		Image[] imgs = new Image[imageFiles.length];
		for (int i = 0; i < imageFiles.length; i++) {
			imgs[i] = Tool.createImage(imageFiles[i]); //Common.getImageFromCache(imageFiles[i]);
		}
		return createActionSet(actionFile, imgs);
	}

	/**
	 * 创建动作集合
	 * 
	 * @param gdata
	 *            动作数据
	 * @param imgs
	 *            图片数组
	 * @return 动作集合对象
	 */
	public static ActionSet createActionSet(byte[] gdata, Image[] imgs) {
		ByteArrayInputStream bin;
		try {
			byte[] data = GZIP.inflate(gdata);
			bin = new ByteArrayInputStream(data);
			return createActionSet(bin, imgs);
		} catch (Exception e) {
			Tool.reportException(e);
			return null;
		}
	}

	/**
	 * 创建动作集合
	 * 
	 * @param actionFile
	 *            动作文件
	 * @param imgs
	 *            图片数组
	 * @return 动作集合对象
	 */
	public static ActionSet createActionSet(String actionFile, Image[] imgs) {
		try {
			System.out.println("loading actionFile = " + actionFile);
			InputStream in = Tool.getResourceAsStream(actionFile);
			byte[] gdata = Tool.getBytesFromInput(in);
			return createActionSet(gdata, imgs);
		} catch (Exception e) {
			Tool.reportException(e);
			return null;
		}
	}

	/**
	 * 创建动作集合
	 * 
	 * @param in
	 *            读取数据流
	 * @param imgs
	 *            图片数组
	 * @return 动作集合对象
	 * @throws Exception
	 */
	private static ActionSet createActionSet(InputStream in, Image[] imgs) throws Exception {
		ActionSet actset = new ActionSet();
		DataInputStream din = new DataInputStream(in);

		//读取图片矩形块数据
		actset.imageNum = din.readByte();
		if (actset.imageNum != imgs.length) {
			throw new Exception("Action Image Num Error");
		}
		actset.rectNum = new short[actset.imageNum];
		actset.imageRects = new Rectangle[actset.imageNum][];
		for (int i = 0; i < actset.imageNum; i++) {
			actset.rectNum[i] = din.readShort();
			actset.imageRects[i] = new Rectangle[actset.rectNum[i]];
			for (int j = 0; j < actset.rectNum[i]; j++) {
				actset.imageRects[i][j] = new Rectangle();
				actset.imageRects[i][j].x = din.readShort();
				actset.imageRects[i][j].y = din.readShort();
				actset.imageRects[i][j].width = din.readByte() & 0xff;
				actset.imageRects[i][j].height = din.readByte() & 0xff;
			}
		}

		//读取帧数据
		actset.frameNum = din.readShort();
		actset.frameDatas = new FrameData[actset.frameNum];
		for (int i = 0; i < actset.frameNum; i++) {
			short frameRectNum = (short) (din.readByte() & 0xff);
			actset.frameDatas[i] = new FrameData(frameRectNum);
			FrameData fd = actset.frameDatas[i];
			for (int j = 0; j < fd.rectNum; j++) {
				fd.imgID[j] = din.readByte();
				fd.rectID[j] = din.readShort();
				fd.drawType[j] = din.readByte();
				fd.rectDX[j] = din.readShort();
				fd.rectDY[j] = din.readShort();
			}
			fd.bAtkFrame = din.readBoolean();
			if (fd.bAtkFrame) {
				fd.atkRect.x = din.readShort();
				fd.atkRect.y = din.readShort();
				fd.atkRect.width = din.readShort();
				fd.atkRect.height = din.readShort();
			}
			fd.bodyRect.x = din.readShort();
			fd.bodyRect.y = din.readShort();
			fd.bodyRect.width = din.readShort();
			fd.bodyRect.height = din.readShort();

			fd.edgeRect.x = din.readShort();
			fd.edgeRect.y = din.readShort();
			fd.edgeRect.width = din.readShort();
			fd.edgeRect.height = din.readShort();
		}
		actset.footWidth = din.readByte() & 0xff;
		actset.footHeight = din.readByte() & 0xff;

		//读取动作数据
		actset.actionNum = din.readShort();
		actset.actionDatas = new ActionData[actset.actionNum];
		for (int i = 0; i < actset.actionNum; i++) {
			short fn = (short) (din.readByte() & 0xff);
			actset.actionDatas[i] = new ActionData(fn);
			ActionData ad = actset.actionDatas[i];
			for (int j = 0; j < ad.frameNum; j++) {
				ad.frameID[j] = din.readShort();
				ad.vX[j] = din.readShort();
				ad.vY[j] = din.readShort();
			}
			//				ad.stepSize = din.readByte() & 0xff;
			ad.bAcc = din.readBoolean();
		}

		din.close();
		in.close();
		actset.images = imgs;

		return actset;
	}

	/**
	 * 绘制帧
	 * 
	 * @param g
	 *            Graphics对象
	 * @param fd
	 *            帧对象
	 * @param footX
	 *            脚底位置X
	 * @param footY
	 *            脚底位置Y
	 * @param bMirror
	 *            是否镜像
	 */
	public void drawFrame(Graphics g, FrameData fd, int footX, int footY, boolean bMirror) {
		if (fd != null) {
			for (int i = 0; i < fd.rectNum; i++) {
				int iId = fd.imgID[i];
				int rId = fd.rectID[i];
				if (iId >= imageNum) {
					continue;
				}
				if (rId >= rectNum[iId]) {
					continue;
				}
				Rectangle rct = imageRects[iId][rId];
				int rdx = fd.rectDX[i];
				int drawType = fd.drawType[i];
				if (bMirror) {
					rdx = -fd.rectDX[i] - rct.width + footWidth;
					drawType = Tool.getMirrorTransType(fd.drawType[i]);

				}
				Tool.drawRegion(g, images[iId], rct.x, rct.y, rct.width, rct.height, drawType, footX + rdx, footY + fd.rectDY[i]);
			}
		}
	}

	/**
	 * 绘制帧
	 * 
	 * @param g
	 *            Graphics对象
	 * @param frameID
	 *            帧ID
	 * @param footX
	 *            脚底位置X
	 * @param footY
	 *            脚底位置Y
	 * @param bMirror
	 *            是否镜像
	 */
	public void drawFrame(Graphics g, int frameID, int footX, int footY, boolean bMirror) {
		FrameData fd = frameDatas[frameID];
		drawFrame(g, fd, footX, footY, bMirror);
	}

	/**
	 * 绘制帧
	 * 
	 * @param g
	 *            Graphics对象
	 * @param actIndex
	 *            动作序号
	 * @param frameIndex
	 *            帧序号
	 * @param footX
	 *            脚底位置X
	 * @param footY
	 *            脚底位置Y
	 * @param bMirror
	 *            是否镜像
	 */
	public void drawFrame(Graphics g, int actIndex, int frameIndex, int footX, int footY, boolean bMirror) {
		if (actIndex < 0) {
			actIndex = 0;
		} else if (actIndex >= actionDatas.length) {
			actIndex = actionDatas.length - 1;
		}
		if (frameIndex < 0) {
			frameIndex = 0;
		} else if (frameIndex >= actionDatas[actIndex].frameID.length) {
			frameIndex = actionDatas[actIndex].frameID.length - 1;
		}
		FrameData fd = frameDatas[actionDatas[actIndex].frameID[frameIndex]];
		drawFrame(g, fd, footX, footY, bMirror);
	}

	/**
	 * 循环绘制动作帧
	 * 
	 * @param g
	 *            Graphics对象
	 * @param actIndex
	 *            动作序号
	 * @param drawTimes
	 *            绘制时间
	 * @param footX
	 *            脚底位置X
	 * @param footY
	 *            脚底位置Y
	 * @param bMirror
	 *            是否镜像
	 */
	public void drawFrameCycle(Graphics g, int actIndex, int drawTimes, int footX, int footY, boolean bMirror) {
		if (actIndex < 0) {
			actIndex = 0;
		} else if (actIndex >= actionDatas.length) {
			actIndex = actionDatas.length - 1;
		}
		drawTimes = Math.abs(drawTimes) % actionDatas[actIndex].frameID.length;

		FrameData fd = frameDatas[actionDatas[actIndex].frameID[drawTimes]];
		drawFrame(g, fd, footX, footY, bMirror);
	}

	/**
	 * 获取身体矩形
	 * 
	 * @param fd
	 *            帧对象
	 * @param bMirror
	 *            是否镜像
	 * @return 身体矩形
	 */
	public Rectangle getBodyRect(FrameData fd, boolean bMirror) {
		Rectangle rect = new Rectangle(fd.bodyRect);
		if (bMirror) {
			rect.x = -rect.x - rect.width + footWidth;
		}
		return rect;
	}

	/**
	 * 获取攻击矩形
	 * 
	 * @param fd
	 *            帧对象
	 * @param bMirror
	 *            是否镜像
	 * @return 攻击矩形
	 */
	public Rectangle getAttackRect(FrameData fd, boolean bMirror) {
		Rectangle rect = new Rectangle(fd.atkRect);
		if (bMirror) {
			rect.x = -rect.x - rect.width + footWidth;
		}
		return rect;
	}

	/**
	 * 获取边缘矩形
	 * 
	 * @param fd
	 *            帧对象
	 * @param bMirror
	 *            是否镜像
	 * @return 边缘矩形
	 */
	public Rectangle getEdgeRect(FrameData fd, boolean bMirror) {
		Rectangle rect = new Rectangle(fd.edgeRect);
		if (bMirror) {
			rect.x = -rect.x - rect.width + footWidth;
		}
		return rect;
	}
}
