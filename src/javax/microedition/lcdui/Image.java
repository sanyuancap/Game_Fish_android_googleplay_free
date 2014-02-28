package javax.microedition.lcdui;

import java.io.InputStream;
import java.io.IOException;
import java.io.*;

//import javax.microedition.lcdui.game.Sprite;
import javax.microedition.midlet.*;

//import com.ea.sport.R;
//import android.content.Resources;
//import android.content.Resources;
//import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Bitmap.Config;
//import android.graphics.drawable.Drawable;
import android.graphics.BitmapFactory;
//import android.util.*;
//import android.graphics.Matrix;

public class Image {
	/**
	 * Construct a new, empty Image
	 */
	//    Image() {}
	// private int width,height;

	//+code android
	Bitmap bitmap = null;
	Graphics imgGraphics;
	public Bitmap getBitmap() {
		return bitmap;
	}

	public static Image createImage(int width, int height) {

		return new Image(width, height);
	}
	public static Image createImage(String name) {

		return new Image(name);
	}

	public static Image createImage(Image source) {

		// SYNC NOTE: Not accessing any shared data, no locking necessary
		return new Image(source);
	}

	public static Image createImage(byte[] imageData, int imageOffset, int imageLength) {

		if (imageOffset < 0 || imageOffset >= imageData.length || imageLength < 0 || imageOffset + imageLength > imageData.length) {
			throw new ArrayIndexOutOfBoundsException();
		}

		// SYNC NOTE: Not accessing any shared data, no locking necessary
		return new Image(imageData, imageOffset, imageLength);

	}

	public static Image createImage(Image image, int x, int y, int width, int height, int transform) {

		if (image == null)

			throw new NullPointerException();

		if (x + width > image.getWidth() || y + height > image.getHeight() || width <= 0 || height <= 0 || x < 0

		|| y < 0)

			throw new IllegalArgumentException("Area out of Image");

		int[] rgbData = new int[height * width];

		int[] rgbTransformedData = new int[height * width];
		image.getRGB(rgbData, 0, width, x, y, width, height);
		//		if (image instanceof AndroidImmutableImage) {
		//
		//			((AndroidImmutableImage) image).getRGB(rgbData, 0, width, x, y, width, height);
		//
		//		} else {
		//
		//			((AndroidMutableImage) image).getRGB(rgbData, 0, width, x, y, width, height);
		//
		//		}
		//

		int colIncr, rowIncr, offset;

		//
		//		switch (transform) {
		//
		//		case Sprite.TRANS_NONE: {
		//
		//			offset = 0;
		//
		//			colIncr = 1;
		//
		//			rowIncr = 0;
		//
		//			break;
		//
		//		}
		//
		//		case Sprite.TRANS_ROT90: {
		//
		//			offset = (height - 1) * width;
		//
		//			colIncr = -width;
		//
		//			rowIncr = (height * width) + 1;
		//
		//			int temp = width;
		//
		//			width = height;
		//
		//			height = temp;
		//
		//			break;
		//
		//		}
		//
		//		case Sprite.TRANS_ROT180: {
		//
		//			offset = (height * width) - 1;
		//
		//			colIncr = -1;
		//
		//			rowIncr = 0;
		//
		//			break;
		//
		//		}
		//
		//		case Sprite.TRANS_ROT270: {
		//
		//			offset = width - 1;
		//
		//			colIncr = width;
		//
		//			rowIncr = -(height * width) - 1;
		//
		//			int temp = width;
		//
		//			width = height;
		//
		//			height = temp;
		//
		//			break;
		//
		//		}
		//
		//		case Sprite.TRANS_MIRROR: {
		//
		//			offset = width - 1;
		//
		//			colIncr = -1;
		//
		//			rowIncr = width << 1;
		//
		//			break;
		//
		//		}
		//
		//		case Sprite.TRANS_MIRROR_ROT90: {
		//
		//			offset = (height * width) - 1;
		//
		//			colIncr = -width;
		//
		//			rowIncr = (height * width) - 1;
		//
		//			int temp = width;
		//
		//			width = height;
		//
		//			height = temp;
		//
		//			break;
		//
		//		}
		//
		//		case Sprite.TRANS_MIRROR_ROT180: {
		//
		//			offset = (height - 1) * width;
		//
		//			colIncr = 1;
		//
		//			rowIncr = -(width << 1);
		//
		//			break;
		//
		//		}
		//
		//		case Sprite.TRANS_MIRROR_ROT270: {
		//
		//			offset = 0;
		//
		//			colIncr = width;
		//
		//			rowIncr = -(height * width) + 1;
		//
		//			int temp = width;
		//
		//			width = height;
		//
		//			height = temp;
		//
		//			break;
		//
		//		}
		//
		//		default:
		//
		//			throw new IllegalArgumentException("Bad transform");
		//
		//		}
		//
		//
		//
		//		// now the loops!
		//
		//		for (int row = 0, i = 0; row < height; row++, offset += rowIncr) {
		//			for (int col = 0; col < width; col++, offset += colIncr, i++) {
		//				rgbTransformedData[i] = rgbData[offset];
		//			}
		//		}
		//
		//		// to aid gc
		//
		//		rgbData = null;
		//
		//		image = null;

		System.gc();
		//		return createRGBImage(rgbTransformedData, width, height, true);

		return createRGBImage(rgbData, width, height, true);
	}

	public Graphics getGraphics() {

		// SYNC NOTE: Not accessing any shared data, no locking necessary
		//    	if(imgGraphics==null)
		//    		throw new IllegalStateException();
		//    	imgGraphics.graphics.save(Canvas.CLIP_SAVE_FLAG);
		//    	imgGraphics.graphics.clipRect(0,0,bitmap.getWidth(),bitmap.getHeight());
		;
		return imgGraphics;
		//        throw new IllegalStateException();
	}

	public int getWidth() {
		// SYNC NOTE: return of atomic value, no locking necessary
		return bitmap.getWidth();
	}

	public int getHeight() {
		// SYNC NOTE: return of atomic value, no locking necessary
		return bitmap.getHeight();
	}

	public boolean isMutable() {
		// SYNC NOTE: return of atomic value, no locking necessary

		return bitmap.isMutable();
	}

	public static Image createImage(InputStream stream) throws java.io.IOException {
		try {
			return new Image(stream);
		} catch (IllegalArgumentException e) {
			throw new java.io.IOException();
		}
	}

	public static Image createRGBImage(int rgb[], int width, int height, boolean processAlpha) {

		if (width <= 0 || height <= 0) {
			throw new IllegalArgumentException();
		}

		if ((width * height) > rgb.length) {
			throw new ArrayIndexOutOfBoundsException();
		}

		return new Image(rgb, width, height, processAlpha);
	}

	//    public native void getRGB(int[] rgbData, int offset, int scanlength,
	//                       int x, int y, int width, int height);

	/**
	 * The width, height of this Image
	 */
	//    public int width, height;

	/**
	 * Native image data
	 */
	int imgData;

	/**
	 * Valid transforms possible are 0 - 7
	 */
	private static final int INVALID_TRANSFORM_BITS = 0xFFFFFFF8;

	public void getRGB(final int[] argb, int offset, int scanlength,

	int x, int y, int width, int height) {

		if (width <= 0 || height <= 0)

			return;

		if (x < 0 || y < 0 || x + width > getWidth() || y + height > getHeight())

			throw new IllegalArgumentException("Specified area exceeds bounds of image");

		if ((scanlength < 0 ? -scanlength : scanlength) < width)

			throw new IllegalArgumentException("abs value of scanlength is less than width");

		if (argb == null)

			throw new NullPointerException("null rgbData");

		if (offset < 0 || offset + width > argb.length)

			throw new ArrayIndexOutOfBoundsException();

		if (scanlength < 0) {

			if (offset + scanlength * (height - 1) < 0)

				throw new ArrayIndexOutOfBoundsException();

		} else {

			if (offset + scanlength * (height - 1) + width > argb.length)

				throw new ArrayIndexOutOfBoundsException();

		}

		bitmap.getPixels(argb, offset, scanlength, x, y, width, height);

		/*
		 * for (int i = 0; i < argb.length; i++) {
		 * 
		 * int a = (argb[i] & 0xFF000000);
		 * 
		 * int b = (argb[i] & 0x00FF0000) >>> 16;
		 * 
		 * int g = (argb[i] & 0x0000FF00) >>> 8;
		 * 
		 * int r = (argb[i] & 0x000000FF);
		 * 
		 * 
		 * 
		 * argb[i] = a | (r << 16) | (g << 8) | b;
		 * 
		 * }
		 */

	}

	Image(int width, int height) {
		bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		imgGraphics = new Graphics(bitmap);
	}
	Image(Image img) {
		//      this.width   = img.width;
		//      this.height  = img.height;

		//createImmutableCopy(width, height, img);
		bitmap = img.bitmap.copy(img.bitmap.getConfig(), false);
	}

	/**
	 * Create a new immutable Image with the region of the given Image applying
	 * the given transform
	 * 
	 * @param img
	 *            The Image to use to create an immutable copy
	 * @param x
	 *            The x-offset of the top-left of the region
	 * @param y
	 *            The y-offset of the top-left of the region
	 * @param width
	 *            The width of the region
	 * @param height
	 *            The height of the region
	 * @param transform
	 *            The transform to apply to the region
	 */

	/**
	 * Create an immutable Image with the given byte data
	 * 
	 * @param imageData
	 *            The byte[] image data
	 * @param imageOffset
	 *            The offset in the array marking the start of the image data
	 * @param imageLength
	 *            The length of the image data in the array
	 */
	Image(byte[] imageData, int imageOffset, int imageLength) {
		//decodeImage(imageData, imageOffset, imageLength);
		// byte[] data = new byte[imageLength];
		// System.arraycopy(imageData, imageOffset, data, 0, imageLength);
		android.util.Log.v("Image", "created bitmap: data = " + imageData.length + "\toffset=" + imageOffset + "\tlength = " + imageLength);
		String s = "";
		for (int i = 0; i < 16; i++) {
			s += "  " + Integer.toHexString(imageData[imageOffset + i]);
		}
		android.util.Log.v("Image", "header: " + s);
		bitmap = BitmapFactory.decodeByteArray(imageData, imageOffset, imageLength);
		android.util.Log.v("Image", "created bitmap: " + bitmap);
		//InputStream is = new ByteArrayInputStream(data);
		// bitmap = BitmapFactory.decodeStream(is);
		//is.close();
		// bitmap=BitmapFactory.decodeByteArray(data, 0, data.length);
		//  	width=bitmap.getWidth();
		//  	height=bitmap.getHeight();
	}

	/**
	 * Create an immutable Image with the given rgb data
	 * 
	 * @param rgbImageData
	 *            an array of ARGB values that composes the image.
	 * @param width
	 *            the width of the image
	 * @param height
	 *            the height of the image
	 * @param parseAlpha
	 *            true if rgb has an alpha channel, false if all pixels are
	 *            fully opaque
	 */

	Image(int[] rgbImageData, int width, int height, boolean parseAlpha) {
		// decodeRGBImage(rgbImageData, width, height, parseAlpha);
		//      bitmap=Bitmap.createScaledBitmap(rgbImageData, width, height, parseAlpha);
		if (rgbImageData == null)

			throw new NullPointerException();

		if (width <= 0 || height <= 0)

			throw new IllegalArgumentException();

		// TODO processAlpha is not handled natively, check whether we need to create copy of rgb

		int[] newrgb = rgbImageData;

		if (!parseAlpha) {

			newrgb = new int[rgbImageData.length];

			for (int i = 0; i < rgbImageData.length; i++) {

				newrgb[i] = (0x00ffffff & rgbImageData[i]) | 0xff000000;

			}

		}
		bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		Paint paint = new Paint();
		canvas.drawBitmap(rgbImageData, 0, width, 0, 0, width, height, parseAlpha, paint);

	}

	public Image(InputStream stream) {
		// TODO Auto-generated constructor stub
		bitmap = BitmapFactory.decodeStream(stream);
		// 	 width=bitmap.getWidth();
		// 	 	height=bitmap.getHeight();	
	}

	public Image(String name) {
		/*
		 * try { bitmap = BitmapFactory.decodeFile(name); } catch(Exception e) {
		 * android.util.Log.e("Image", "create image from \"" + name +
		 * "\" failed!", e); } android.util.Log.v("Image", "Image.bitmap = " +
		 * bitmap);
		 */
		try {
			InputStream is = MIDlet.getResourceManager().getAssets().open(name);
			bitmap = BitmapFactory.decodeStream(is);
			is.close();
		} catch (Exception e) {
			android.util.Log.e("Image", "create image from \"" + name + "\" failed!", e);
			//e.printStackTrace();
		}
		android.util.Log.v("Image", "Image.bitmap = " + bitmap);

	}

}
