package com.game5a.common;

import java.io.IOException;

import javax.microedition.lcdui.Image;

import com.game5a.action.ActionSet;

public class ResManager {
	public static String IMAGE_TYPE = ".p";
	public static String IMG_PATH = "/pic/";
	public static String ACTION_PATH = "/action/";

	public static Image arImg;
	public static Image addHpImg;
	public static Image dropHpImg0;
	public static Image dropHpImg1;

	public static ActionSet faceAs;

	public static ActionSet buffAs;
	public static ActionSet fightLightAs;

	public static void loadRes() {
		arImg = Tool.createImage(IMG_PATH + "ar" + IMAGE_TYPE);
		addHpImg = Tool.createImage(IMG_PATH + "ui_2_13" + IMAGE_TYPE);
		dropHpImg0 = Tool.createImage(IMG_PATH + "ui_2_14" + IMAGE_TYPE);
		dropHpImg1 = Tool.createImage(IMG_PATH + "ui_2_15" + IMAGE_TYPE);

		Image[] imgs = new Image[1];
		imgs[0] = Tool.createImage(ACTION_PATH + "face" + IMAGE_TYPE);
		faceAs = ActionSet.createActionSet(ACTION_PATH + "face.an", imgs);

		imgs = new Image[1];
		imgs[0] = Tool.createImage(ACTION_PATH + "ui_2_16" + IMAGE_TYPE);
		buffAs = ActionSet.createActionSet(ACTION_PATH + "ui_2_16.an", imgs);

		imgs = new Image[1];
		imgs[0] = Tool.createImage(ACTION_PATH + "attack" + IMAGE_TYPE);
		fightLightAs = ActionSet.createActionSet(ACTION_PATH + "attack.an", imgs);
	}
}
