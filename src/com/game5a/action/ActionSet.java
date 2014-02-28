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
 * ��������
 * 
 * @author xFu
 * 
 */
public class ActionSet {
	/** ͼƬ���� */
	public byte imageNum;
	/** ͼƬ���ο��� */
	public short[] rectNum;
	/** ���ο����� */
	public Rectangle[][] imageRects;

	/** ֡�� */
	public short frameNum;
	/** ֡���� */
	public FrameData[] frameDatas;

	/** ������ */
	public short actionNum;
	/** �������� */
	public ActionData[] actionDatas;

	/** �ŵ׿� */
	public int footWidth;
	/** �ŵ׸� */
	public int footHeight;

	/** �����й�ͼƬ */
	public Image[] images;

	public ActionSet() {

	}

	/**
	 * ������������
	 * 
	 * @param actionFile
	 *            �����ļ�
	 * @param imageFiles
	 *            ͼƬ�ļ�����
	 * @return �������϶���
	 */
	public static ActionSet createActionSet(String actionFile, String[] imageFiles) {
		Image[] imgs = new Image[imageFiles.length];
		for (int i = 0; i < imageFiles.length; i++) {
			imgs[i] = Tool.createImage(imageFiles[i]); //Common.getImageFromCache(imageFiles[i]);
		}
		return createActionSet(actionFile, imgs);
	}

	/**
	 * ������������
	 * 
	 * @param gdata
	 *            ��������
	 * @param imgs
	 *            ͼƬ����
	 * @return �������϶���
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
	 * ������������
	 * 
	 * @param actionFile
	 *            �����ļ�
	 * @param imgs
	 *            ͼƬ����
	 * @return �������϶���
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
	 * ������������
	 * 
	 * @param in
	 *            ��ȡ������
	 * @param imgs
	 *            ͼƬ����
	 * @return �������϶���
	 * @throws Exception
	 */
	private static ActionSet createActionSet(InputStream in, Image[] imgs) throws Exception {
		ActionSet actset = new ActionSet();
		DataInputStream din = new DataInputStream(in);

		//��ȡͼƬ���ο�����
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

		//��ȡ֡����
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

		//��ȡ��������
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
	 * ����֡
	 * 
	 * @param g
	 *            Graphics����
	 * @param fd
	 *            ֡����
	 * @param footX
	 *            �ŵ�λ��X
	 * @param footY
	 *            �ŵ�λ��Y
	 * @param bMirror
	 *            �Ƿ���
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
	 * ����֡
	 * 
	 * @param g
	 *            Graphics����
	 * @param frameID
	 *            ֡ID
	 * @param footX
	 *            �ŵ�λ��X
	 * @param footY
	 *            �ŵ�λ��Y
	 * @param bMirror
	 *            �Ƿ���
	 */
	public void drawFrame(Graphics g, int frameID, int footX, int footY, boolean bMirror) {
		FrameData fd = frameDatas[frameID];
		drawFrame(g, fd, footX, footY, bMirror);
	}

	/**
	 * ����֡
	 * 
	 * @param g
	 *            Graphics����
	 * @param actIndex
	 *            �������
	 * @param frameIndex
	 *            ֡���
	 * @param footX
	 *            �ŵ�λ��X
	 * @param footY
	 *            �ŵ�λ��Y
	 * @param bMirror
	 *            �Ƿ���
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
	 * ѭ�����ƶ���֡
	 * 
	 * @param g
	 *            Graphics����
	 * @param actIndex
	 *            �������
	 * @param drawTimes
	 *            ����ʱ��
	 * @param footX
	 *            �ŵ�λ��X
	 * @param footY
	 *            �ŵ�λ��Y
	 * @param bMirror
	 *            �Ƿ���
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
	 * ��ȡ�������
	 * 
	 * @param fd
	 *            ֡����
	 * @param bMirror
	 *            �Ƿ���
	 * @return �������
	 */
	public Rectangle getBodyRect(FrameData fd, boolean bMirror) {
		Rectangle rect = new Rectangle(fd.bodyRect);
		if (bMirror) {
			rect.x = -rect.x - rect.width + footWidth;
		}
		return rect;
	}

	/**
	 * ��ȡ��������
	 * 
	 * @param fd
	 *            ֡����
	 * @param bMirror
	 *            �Ƿ���
	 * @return ��������
	 */
	public Rectangle getAttackRect(FrameData fd, boolean bMirror) {
		Rectangle rect = new Rectangle(fd.atkRect);
		if (bMirror) {
			rect.x = -rect.x - rect.width + footWidth;
		}
		return rect;
	}

	/**
	 * ��ȡ��Ե����
	 * 
	 * @param fd
	 *            ֡����
	 * @param bMirror
	 *            �Ƿ���
	 * @return ��Ե����
	 */
	public Rectangle getEdgeRect(FrameData fd, boolean bMirror) {
		Rectangle rect = new Rectangle(fd.edgeRect);
		if (bMirror) {
			rect.x = -rect.x - rect.width + footWidth;
		}
		return rect;
	}
}
