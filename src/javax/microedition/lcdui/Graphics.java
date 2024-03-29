/*
 * @(#)Graphics.java	1.84 02/09/10 @(#)
 *
 * Copyright (c) 1999-2002 Sun Microsystems, Inc.  All rights reserved.
 * PROPRIETARY/CONFIDENTIAL
 * Use is subject to license terms.
 */

package javax.microedition.lcdui;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Rect;
import android.graphics.Canvas;
import android.graphics.Bitmap;
import android.graphics.*;
import javax.microedition.lcdui.game.*;
//import java.lang.NullPointerException;

public class Graphics {

	public android.graphics.Canvas adCanvas;
	private Paint adPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private Rect clipRect = new Rect();

	public static final int HCENTER = 1;
	public static final int VCENTER = 2;
	public static final int LEFT = 4;
	public static final int RIGHT = 8;
	public static final int TOP = 16;
	public static final int BOTTOM = 32;
	public static final int BASELINE = 64;
	public static final int SOLID = 0;
	public static final int DOTTED = 1;

	/**
	 * Intialize the peer of this Graphics context
	 */
	//    private void init()
	//    {
	//    	
	//    }

	/**
	 * Create a Graphics object with the given width and height
	 * 
	 * @param w
	 *            The maximum width of the new Graphics object
	 * @param h
	 *            The maximum height of the new Graphics object
	 */
	//    Graphics()
	//    {
	//     	this(HUIConfig.getWidth(),HUIConfig.getHeight());

	//    	if(HUIConfig.getWidth()==0|HUIConfig.getHeight()==0)
	//    		 throw new IllegalArgumentException("Screen Size is Wrong");

	//    }

	//    Graphics(int w, int h) {
	//        destination = null;
	//
	//        maxWidth  = (short) (w & 0x7fff);
	//        maxHeight = (short) (h & 0x7fff);
	//
	////        init();
	//        reset();
	//    }
	//    public void setGraphics()
	//    public void setGraphics(Canvas cs)
	//    {
	//    	Canvas.
	//    }
	//    public Graphics() {
	//    	super(bitmap);
	//	}
	public Graphics() {
		super();
	}
	public Graphics(Bitmap bitmap) {
		//    	super(bitmap);
		adCanvas = new Canvas(bitmap);
	}
	//    public Graphics(Canvas cs)
	//    {
	//    	super(cs);
	//    }

	/**
	 * Translates the origin of the graphics context to the point
	 * <code>(x, y)</code> in the current coordinate system. All coordinates
	 * used in subsequent rendering operations on this graphics context will be
	 * relative to this new origin.
	 * <p>
	 * 
	 * The effect of calls to <code>translate()</code> are cumulative. For
	 * example, calling <code>translate(1, 2)</code> and then <code>translate(3,
	 * 4)</code> results in a translation of <code>(4, 6)</code>.
	 * <p>
	 * 
	 * The application can set an absolute origin <code>(ax,
	 * ay)</code> using the following technique:
	 * <p>
	 * <code>
	 * g.translate(ax - g.getTranslateX(), ay - g.getTranslateY())
	 * </code>
	 * <p>
	 * 
	 * @param x
	 *            the x coordinate of the new translation origin
	 * @param y
	 *            the y coordinate of the new translation origin
	 * @see #getTranslateX()
	 * @see #getTranslateY()
	 */
	private int translateX = 0;
	private int translateY = 0;
	public void translate(int x, int y) {
		//        transX += x;
		//        transY += y;
		//        graphics.translate(transX, transY);
		//        super.translate(x, y);
		//\\translateX=x;
		//\\translateY=y;
		translateX += x;
		translateY += y;
		//    	this.set
		adCanvas.translate(x, y);
	}

	/**
	 * Gets the X coordinate of the translated origin of this graphics context.
	 * 
	 * @return X of current origin
	 */
	public int getTranslateX() {

		return translateX;
	}
	//
	//	 public int stringWidth(String str)
	//	 {
	//		 return str.length()*12;//12为每锟斤拷锟街凤拷锟斤拷锟斤拷幕锟斤拷锟斤拷站锟斤拷锟�
	//	 }
	//    
	/**
	 * Gets the Y coordinate of the translated origin of this graphics context.
	 * 
	 * @return Y of current origin
	 */
	public int getTranslateY() {
		return translateY;
	}

	/**
	 * Gets the current color.
	 * 
	 * @return an integer in form <code>0x00RRGGBB</code>
	 * @see #setColor(int, int, int)
	 */
	public int getColor() {
		return adPaint.getColor();
	}

	/**
	 * Gets the red component of the current color.
	 * 
	 * @return integer value in range <code>0-255</code>
	 * @see #setColor(int, int, int)
	 */
	public int getRedComponent() {
		return (rgbColor >> 16) & 0xff;
	}

	/**
	 * Gets the green component of the current color.
	 * 
	 * @return integer value in range <code>0-255</code>
	 * @see #setColor(int, int, int)
	 */
	public int getGreenComponent() {
		return (rgbColor >> 8) & 0xff;
	}

	/**
	 * Gets the blue component of the current color.
	 * 
	 * @return integer value in range <code>0-255</code>
	 * @see #setColor(int, int, int)
	 */
	public int getBlueComponent() {
		return rgbColor & 0xff;
	}

	/**
	 * Gets the current grayscale value of the color being used for rendering
	 * operations. If the color was set by <code>setGrayScale()</code>, that
	 * value is simply returned. If the color was set by one of the methods that
	 * allows setting of the red, green, and blue components, the value returned
	 * is computed from the RGB color components (possibly in a device-specific
	 * fashion) that best approximates the brightness of that color.
	 * 
	 * @return integer value in range <code>0-255</code>
	 * @see #setGrayScale
	 */
	public int getGrayScale() {
		return gray;
	}

	/**
	 * Sets the current color to the specified RGB values. All subsequent
	 * rendering operations will use this specified color.
	 * 
	 * @param red
	 *            the red component of the color being set in range
	 *            <code>0-255</code>
	 * @param green
	 *            the green component of the color being set in range
	 *            <code>0-255</code>
	 * @param blue
	 *            the blue component of the color being set in range
	 *            <code>0-255</code>
	 * @throws IllegalArgumentException
	 *             if any of the color components are outside of range
	 *             <code>0-255</code>
	 * @see #getColor
	 */
	public void setColor(int red, int green, int blue) {
		if ((red < 0) || (red > 255) || (green < 0) || (green > 255) || (blue < 0) || (blue > 255)) {
			throw new IllegalArgumentException("Value out of range");
		}

		//        rgbColor = (red << 16) | (green << 8) | blue;
		//        paint.setColor(0xff000000 | rgbColor);
		setColor((red << 16) | (green << 8) | blue);

		//        gray = grayVal(red, green, blue);
		//        pixel = getPixel(rgbColor, gray, false);
	}

	/**
	 * Sets the current color to the specified RGB values. All subsequent
	 * rendering operations will use this specified color. The RGB value passed
	 * in is interpreted with the least significant eight bits giving the blue
	 * component, the next eight more significant bits giving the green
	 * component, and the next eight more significant bits giving the red
	 * component. That is to say, the color component is specified in the form
	 * of <code>0x00RRGGBB</code>. The high order byte of this value is ignored.
	 * 
	 * @param RGB
	 *            the color being set
	 * @see #getColor
	 */
	public void setColor(int rgb) {
		//        int red   = (RGB >> 16) & 0xff;
		//        int green = (RGB >> 8)  & 0xff;
		//        int blue  = (RGB)  & 0xff;
		//
		//        rgbColor = RGB & 0x00ffffff;
		//        gray = grayVal(red, green, blue);

		//        pixel = getPixel(rgbColor, gray, false);
		//    	paint.setColor(rgb)
		//    	this.set
		adPaint.setColor(0xff000000 | rgb);
	}

	/**
	 * Sets the current grayscale to be used for all subsequent rendering
	 * operations. For monochrome displays, the behavior is clear. For color
	 * displays, this sets the color for all subsequent drawing operations to be
	 * a gray color equivalent to the value passed in. The value must be in the
	 * range <code>0-255</code>.
	 * 
	 * @param value
	 *            the desired grayscale value
	 * @throws IllegalArgumentException
	 *             if the gray value is out of range
	 * @see #getGrayScale
	 */
	public void setGrayScale(int value) {
		if ((value < 0) || (value > 255)) {
			throw new IllegalArgumentException("Gray value out of range");
		}

		rgbColor = (value << 16) | (value << 8) | value;
		gray = value;
		pixel = getPixel(rgbColor, gray, true);
	}

	/**
	 * Gets the current font.
	 * 
	 * @return current font
	 * @see javax.microedition.lcdui.Font
	 * @see #setFont(javax.microedition.lcdui.Font)
	 */
	public Font getFont() {
		return font;
	}

	/**
	 * Sets the stroke style used for drawing lines, arcs, rectangles, and
	 * rounded rectangles. This does not affect fill, text, and Image3
	 * operations.
	 * 
	 * @param style
	 *            can be <code>SOLID</code> or <code>DOTTED</code>
	 * @throws IllegalArgumentException
	 *             if the <code>style</code> is illegal
	 * @see #getStrokeStyle
	 */
	public void setStrokeStyle(int style) {
		if ((style != SOLID) && (style != DOTTED)) {
			throw new IllegalArgumentException("Invalid line style");
		}

		this.style = style;
	}

	/**
	 * Gets the stroke style used for drawing operations.
	 * 
	 * @return stroke style, <code>SOLID</code> or <code>DOTTED</code>
	 * @see #setStrokeStyle
	 */
	public int getStrokeStyle() {
		return style;
	}

	/**
	 * Sets the font for all subsequent text rendering operations. If font is
	 * <code>null</code>, it is equivalent to
	 * <code>setFont(Font.getDefaultFont())</code>.
	 * 
	 * @param font
	 *            the specified font
	 * @see javax.microedition.lcdui.Font
	 * @see #getFont()
	 * @see #drawString(java.lang.String, int, int, int)
	 * @see #drawChars(char[], int, int, int, int, int)
	 */
	public void setFont(Font font) {
		this.font = ((font == null) ? Font.getDefaultFont() : font);
	}

	/**
	 * Gets the X offset of the current clipping area, relative to the
	 * coordinate system origin of this graphics context. Separating the
	 * <code>getClip</code> operation into two methods returning integers is
	 * more performance and memory efficient than one <code>getClip()</code>
	 * call returning an object.
	 * 
	 * @return X offset of the current clipping area
	 * @see #clipRect(int, int, int, int)
	 * @see #setClip(int, int, int, int)
	 */
	public int getClipX() {
		return clipRect.left;
	}

	/**
	 * Gets the Y offset of the current clipping area, relative to the
	 * coordinate system origin of this graphics context. Separating the
	 * <code>getClip</code> operation into two methods returning integers is
	 * more performance and memory efficient than one <code>getClip()</code>
	 * call returning an object.
	 * 
	 * @return Y offset of the current clipping area
	 * @see #clipRect(int, int, int, int)
	 * @see #setClip(int, int, int, int)
	 */
	public int getClipY() {
		return clipRect.top;
	}

	/**
	 * Gets the width of the current clipping area.
	 * 
	 * @return width of the current clipping area.
	 * @see #clipRect(int, int, int, int)
	 * @see #setClip(int, int, int, int)
	 */
	public int getClipWidth() {
		return clipRect.right - clipRect.left;
	}

	public int getClipHeight() {
		return clipRect.bottom - clipRect.top;
	}

	/**
	 * Intersects the current clip with the specified rectangle. The resulting
	 * clipping area is the intersection of the current clipping area and the
	 * specified rectangle. This method can only be used to make the current
	 * clip smaller. To set the current clip larger, use the
	 * <code>setClip</code> method. Rendering operations have no effect outside
	 * of the clipping area.
	 * 
	 * @param x
	 *            the x coordinate of the rectangle to intersect the clip with
	 * @param y
	 *            the y coordinate of the rectangle to intersect the clip with
	 * @param width
	 *            the width of the rectangle to intersect the clip with
	 * @param height
	 *            the height of the rectangle to intersect the clip with
	 * @see #setClip(int, int, int, int)
	 */
	public boolean clipRect(int x, int y, int width, int height) {
		//		graphics.clipRect(x, y, x + width, y + height);
		return adCanvas.clipRect(x, y, x + width, y + height);
		//		clipRect = graphics.getClipBounds();
	}

	/**
	 * Sets the current clip to the rectangle specified by the given
	 * coordinates. Rendering operations have no effect outside of the clipping
	 * area.
	 * 
	 * @param x
	 *            the x coordinate of the new clip rectangle
	 * @param y
	 *            the y coordinate of the new clip rectangle
	 * @param width
	 *            the width of the new clip rectangle
	 * @param height
	 *            the height of the new clip rectangle
	 * @see #clipRect(int, int, int, int)
	 */
	public void setClip(int x, int y, int width, int height) {

		//    	if(graphics!=null)
		//    	{
		/*
		 * if(graphics.getSaveCount()==graphics.CLIP_SAVE_FLAG) {
		 * graphics.restore(); }
		 *///\\

		adCanvas.save(Canvas.CLIP_SAVE_FLAG);
		clipRect.set(x, y, x + width, y + height);
		//            clipRect.left = x;
		//            clipRect.top = y;
		//            clipRect.right = x + width;
		//            clipRect.bottom = y + height;
		adCanvas.clipRect(clipRect, Region.Op.REPLACE);

		//    	}

	}

	/**
	 * Draws a line between the coordinates <code>(x1,y1)</code> and
	 * <code>(x2,y2)</code> using the current color and stroke style.
	 * 
	 * @param x1
	 *            the x coordinate of the start of the line
	 * @param y1
	 *            the y coordinate of the start of the line
	 * @param x2
	 *            the x coordinate of the end of the line
	 * @param y2
	 *            the y coordinate of the end of the line
	 */
	public void drawLine(int x1, int y1, int x2, int y2) {
		adPaint.setStyle(Paint.Style.FILL);
		//   paint.setColor(rgbColor);
		adPaint.setAlpha(0xff);
		adCanvas.drawLine(x1, y1, x2, y2, adPaint);
	}

	/**
	 * Fills the specified rectangle with the current color. If either width or
	 * height is zero or less, nothing is drawn.
	 * 
	 * @param x
	 *            the x coordinate of the rectangle to be filled
	 * @param y
	 *            the y coordinate of the rectangle to be filled
	 * @param width
	 *            the width of the rectangle to be filled
	 * @param height
	 *            the height of the rectangle to be filled
	 * @see #drawRect(int, int, int, int)
	 */
	public void fillRect(int x, int y, int width, int height) {
		adPaint.setStyle(Paint.Style.FILL);
		drawRect.set(x, y, x + width, y + height);
		adCanvas.drawRect(drawRect, adPaint);
	}
	
	//android 110727
	public void fillAlphaRect(int argb, int x, int y, int width, int height) {
		adPaint.setStyle(Paint.Style.FILL);
		drawRect.set(x, y, x + width, y + height);
		adPaint.setColor(argb);
		adCanvas.drawRect(drawRect, adPaint);
		adPaint.setAlpha(255);
	}

	/**
	 * Draws the outline of the specified rectangle using the current color and
	 * stroke style. The resulting rectangle will cover an area
	 * <code>(width + 1)</code> pixels wide by <code>(height + 1)</code> pixels
	 * tall. If either width or height is less than zero, nothing is drawn.
	 * 
	 * @param x
	 *            the x coordinate of the rectangle to be drawn
	 * @param y
	 *            the y coordinate of the rectangle to be drawn
	 * @param width
	 *            the width of the rectangle to be drawn
	 * @param height
	 *            the height of the rectangle to be drawn
	 * @see #fillRect(int, int, int, int)
	 */
	public void drawRect(int x, int y, int width, int height) {
		// paint.setColor(rgbColor);
		//    	  paint.setAlpha(0xff);

		adPaint.setStyle(Paint.Style.STROKE);
		drawRect.set(x, y, x + width, y + height);
		adCanvas.drawRect(drawRect, adPaint);
	}

	/**
	 * Draws the outline of the specified rounded corner rectangle using the
	 * current color and stroke style. The resulting rectangle will cover an
	 * area <code>(width +
	 * 1)</code> pixels wide by <code>(height + 1)</code> pixels tall. If either
	 * <code>width</code> or <code>height</code> is less than zero, nothing is
	 * drawn.
	 * 
	 * @param x
	 *            the x coordinate of the rectangle to be drawn
	 * @param y
	 *            the y coordinate of the rectangle to be drawn
	 * @param width
	 *            the width of the rectangle to be drawn
	 * @param height
	 *            the height of the rectangle to be drawn
	 * @param arcWidth
	 *            the horizontal diameter of the arc at the four corners
	 * @param arcHeight
	 *            the vertical diameter of the arc at the four corners
	 * @see #fillRoundRect(int, int, int, int, int, int)
	 */
	public void drawRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
		//    	paint.setAntiAlias(true);
		//    	paint.setColor(rgbColor);
		//    	paint.setAlpha(0xff);
		//    	RectF drawRect = new RectF();
		adPaint.setStyle(Paint.Style.STROKE);
		drawRect.set(x, y, x + width, y + height);
		adCanvas.drawRoundRect(drawRect, arcWidth, arcHeight, adPaint);
	}

	/**
	 * Fills the specified rounded corner rectangle with the current color. If
	 * either <code>width</code> or <code>height</code> is zero or less, nothing
	 * is drawn.
	 * 
	 * @param x
	 *            the x coordinate of the rectangle to be filled
	 * @param y
	 *            the y coordinate of the rectangle to be filled
	 * @param width
	 *            the width of the rectangle to be filled
	 * @param height
	 *            the height of the rectangle to be filled
	 * @param arcWidth
	 *            the horizontal diameter of the arc at the four corners
	 * @param arcHeight
	 *            the vertical diameter of the arc at the four corners
	 * @see #drawRoundRect(int, int, int, int, int, int)
	 */
	RectF drawRect = new RectF();
	public void fillRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
		//    	 paint.setAntiAlias(true);
		//  paint.setColor(rgbColor);
		//         paint.setAlpha(0xff);
		//         RectF drawRect = new RectF();
		//    	 drawRect.set
		adPaint.setStyle(Paint.Style.FILL);
		drawRect.set(x, y, x + width, y + height);
		adCanvas.drawRoundRect(drawRect, arcWidth, arcHeight, adPaint);
	}

	/**
	 * Fills a circular or elliptical arc covering the specified rectangle.
	 * <p>
	 * The resulting arc begins at <code>startAngle</code> and extends for
	 * <code>arcAngle</code> degrees. Angles are interpreted such that
	 * <code>0</code> degrees is at the <code>3</code> o'clock position. A
	 * positive value indicates a counter-clockwise rotation while a negative
	 * value indicates a clockwise rotation.
	 * <p>
	 * The center of the arc is the center of the rectangle whose origin is (
	 * <em>x</em>,&nbsp;<em>y</em>) and whose size is specified by the
	 * <code>width</code> and <code>height</code> arguments.
	 * <p>
	 * If either <code>width</code> or <code>height</code> is zero or less,
	 * nothing is drawn.
	 * 
	 * <p>
	 * The filled region consists of the &quot;pie wedge&quot; region bounded by
	 * the arc segment as if drawn by <code>drawArc()</code>, the radius
	 * extending from the center to this arc at <code>startAngle</code> degrees,
	 * and radius extending from the center to this arc at
	 * <code>startAngle + arcAngle</code> degrees.
	 * </p>
	 * 
	 * <p>
	 * The angles are specified relative to the non-square extents of the
	 * bounding rectangle such that <code>45</code> degrees always falls on the
	 * line from the center of the ellipse to the upper right corner of the
	 * bounding rectangle. As a result, if the bounding rectangle is noticeably
	 * longer in one axis than the other, the angles to the start and end of the
	 * arc segment will be skewed farther along the longer axis of the bounds.
	 * </p>
	 * 
	 * @param x
	 *            the <em>x</em> coordinate of the upper-left corner of the arc
	 *            to be filled.
	 * @param y
	 *            the <em>y</em> coordinate of the upper-left corner of the arc
	 *            to be filled.
	 * @param width
	 *            the width of the arc to be filled
	 * @param height
	 *            the height of the arc to be filled
	 * @param startAngle
	 *            the beginning angle.
	 * @param arcAngle
	 *            the angular extent of the arc, relative to the start angle.
	 * @see #drawArc(int, int, int, int, int, int)
	 */
	public void fillArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
		adPaint.setStyle(Paint.Style.FILL);
		drawRect.set(x, y, x + width, y + height);
		adCanvas.drawArc(drawRect, startAngle - 180, -arcAngle, true, adPaint);
	}

	/**
	 * Draws the outline of a circular or elliptical arc covering the specified
	 * rectangle, using the current color and stroke style.
	 * <p>
	 * The resulting arc begins at <code>startAngle</code> and extends for
	 * <code>arcAngle</code> degrees, using the current color. Angles are
	 * interpreted such that <code>0</code>&nbsp;degrees is at the
	 * <code>3</code>&nbsp;o'clock position. A positive value indicates a
	 * counter-clockwise rotation while a negative value indicates a clockwise
	 * rotation.
	 * <p>
	 * The center of the arc is the center of the rectangle whose origin is (
	 * <em>x</em>,&nbsp;<em>y</em>) and whose size is specified by the
	 * <code>width</code> and <code>height</code> arguments.
	 * <p>
	 * The resulting arc covers an area <code>width&nbsp;+&nbsp;1</code> pixels
	 * wide by <code>height&nbsp;+&nbsp;1</code> pixels tall. If either
	 * <code>width</code> or <code>height</code> is less than zero, nothing is
	 * drawn.
	 * 
	 * <p>
	 * The angles are specified relative to the non-square extents of the
	 * bounding rectangle such that <code>45</code> degrees always falls on the
	 * line from the center of the ellipse to the upper right corner of the
	 * bounding rectangle. As a result, if the bounding rectangle is noticeably
	 * longer in one axis than the other, the angles to the start and end of the
	 * arc segment will be skewed farther along the longer axis of the bounds.
	 * </p>
	 * 
	 * @param x
	 *            the <em>x</em> coordinate of the upper-left corner of the arc
	 *            to be drawn
	 * @param y
	 *            the <em>y</em> coordinate of the upper-left corner of the arc
	 *            to be drawn
	 * @param width
	 *            the width of the arc to be drawn
	 * @param height
	 *            the height of the arc to be drawn
	 * @param startAngle
	 *            the beginning angle
	 * @param arcAngle
	 *            the angular extent of the arc, relative to the start angle
	 * @see #fillArc(int, int, int, int, int, int)
	 */
	public void drawArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
		adPaint.setStyle(Paint.Style.STROKE);
		drawRect.set(x, y, x + width, y + height);
		//        super.drawRoundRect(drawRect, arc, arcHeight, paint);
		adCanvas.drawArc(drawRect, startAngle, arcAngle, false, adPaint);
	}

	/**
	 * Draws the specified <code>String</code> using the current FontR and
	 * color. The <code>x,y</code> position is the position of the anchor point.
	 * See <a href="#anchor">anchor points</a>.
	 * 
	 * @param str
	 *            the <code>String</code> to be drawn
	 * @param x
	 *            the x coordinate of the anchor point
	 * @param y
	 *            the y coordinate of the anchor point
	 * @param anchor
	 *            the anchor point for positioning the text
	 * @throws NullPointerException
	 *             if <code>str</code> is <code>null</code>
	 * @throws IllegalArgumentException
	 *             if anchor is not a legal value
	 * @see #drawChars(char[], int, int, int, int, int)
	 */
	public void drawString(java.lang.String str, int x, int y, int anchor) {
		int newx = x;
		int newy = y;

		if (anchor == 0) {
			anchor = javax.microedition.lcdui.Graphics.TOP | javax.microedition.lcdui.Graphics.LEFT;
		}
		if ((anchor & javax.microedition.lcdui.Graphics.TOP) != 0) {

			newy -= font.m_paint.getFontMetricsInt().ascent;

		} else if ((anchor & javax.microedition.lcdui.Graphics.BOTTOM) != 0) {

			newy -= font.m_paint.getFontMetricsInt().descent;

		}

		if ((anchor & javax.microedition.lcdui.Graphics.HCENTER) != 0) {

			newx -= font.m_paint.measureText(str) / 2;

		} else if ((anchor & javax.microedition.lcdui.Graphics.RIGHT) != 0) {

			newx -= font.m_paint.measureText(str);

		}
		font.m_paint.setColor(adPaint.getColor());
		adCanvas.drawText(str, newx, newy, font.m_paint);
	}

	/**
	 * Draws the specified <code>String</code> using the current FontR and
	 * color. The <code>x,y</code> position is the position of the anchor point.
	 * See <a href="#anchor">anchor points</a>.
	 * 
	 * <p>
	 * The <code>offset</code> and <code>len</code> parameters must specify a
	 * valid range of characters within the string <code>str</code>. The
	 * <code>offset</code> parameter must be within the range
	 * <code>[0..(str.length())]</code>, inclusive. The <code>len</code>
	 * parameter must be a non-negative integer such that
	 * <code>(offset + len) &lt;= str.length()</code>.
	 * </p>
	 * 
	 * @param str
	 *            the <code>String</code> to be drawn
	 * @param offset
	 *            zero-based index of first character in the substring
	 * @param len
	 *            length of the substring
	 * @param x
	 *            the x coordinate of the anchor point
	 * @param y
	 *            the y coordinate of the anchor point
	 * @param anchor
	 *            the anchor point for positioning the text
	 * @see #drawString(String, int, int, int).
	 * @throws StringIndexOutOfBoundsException
	 *             if <code>offset</code> and <code>length</code> do not specify
	 *             a valid range within the <code>String</code> <code>str</code>
	 * @throws IllegalArgumentException
	 *             if <code>anchor</code> is not a legal value
	 * @throws NullPointerException
	 *             if <code>str</code> is <code>null</code>
	 */
	public void drawSubstring(String str, int offset, int len, int x, int y, int anchor) {
		drawString(str.substring(offset, offset + len), x, y, anchor);
	}

	/**
	 * Draws the specified character using the current FontR and color.
	 * 
	 * @param character
	 *            the character to be drawn
	 * @param x
	 *            the x coordinate of the anchor point
	 * @param y
	 *            the y coordinate of the anchor point
	 * @param anchor
	 *            the anchor point for positioning the text; see <a
	 *            href="#anchor">anchor points</a>
	 * 
	 * @throws IllegalArgumentException
	 *             if <code>anchor</code> is not a legal value
	 * 
	 * @see #drawString(java.lang.String, int, int, int)
	 * @see #drawChars(char[], int, int, int, int, int)
	 */
	public void drawChar(char character, int x, int y, int anchor) {
		// 	   paint.setAntiAlias(true);
		//  paint.setColor(rgbColor);
		//       paint.setAlpha(255);
		int newx = x;
		int newy = y;

		if (anchor == 0) {
			anchor = javax.microedition.lcdui.Graphics.TOP | javax.microedition.lcdui.Graphics.LEFT;
		}
		if ((anchor & javax.microedition.lcdui.Graphics.TOP) != 0) {

			newy -= font.m_paint.getFontMetricsInt().ascent;

		} else if ((anchor & javax.microedition.lcdui.Graphics.BOTTOM) != 0) {

			newy -= font.m_paint.getFontMetricsInt().descent;

		}

		if ((anchor & javax.microedition.lcdui.Graphics.HCENTER) != 0) {

			newx -= font.charWidth(character) / 2;

		} else if ((anchor & javax.microedition.lcdui.Graphics.RIGHT) != 0) {

			newx -= font.charWidth(character);

		}
		font.m_paint.setColor(adPaint.getColor());
		//       font.paint.setColor(paint.getColor());

		adCanvas.drawText("" + character, newx, newy, font.m_paint);
		//       graphics.draw
		//  graphics.draw
	}
	//    public static final int HCENTER = 1;
	//    public static final int VCENTER = 2;
	//    public static final int LEFT = 4;
	//    public static final int RIGHT = 8;
	//    public static final int TOP = 16;
	//    public static final int BOTTOM = 32;

	/**
	 * Draws the specified characters using the current FontR and color.
	 * 
	 * <p>
	 * The <code>offset</code> and <code>length</code> parameters must specify a
	 * valid range of characters within the character array <code>data</code>.
	 * The <code>offset</code> parameter must be within the range
	 * <code>[0..(data.length)]</code>, inclusive. The <code>length</code>
	 * parameter must be a non-negative integer such that
	 * <code>(offset + length) &lt;= data.length</code>.
	 * </p>
	 * 
	 * @param data
	 *            the array of characters to be drawn
	 * @param offset
	 *            the start offset in the data
	 * @param length
	 *            the number of characters to be drawn
	 * @param x
	 *            the x coordinate of the anchor point
	 * @param y
	 *            the y coordinate of the anchor point
	 * @param anchor
	 *            the anchor point for positioning the text; see <a
	 *            href="#anchor">anchor points</a>
	 * 
	 * @throws ArrayIndexOutOfBoundsException
	 *             if <code>offset</code> and <code>length</code> do not specify
	 *             a valid range within the data array
	 * @throws IllegalArgumentException
	 *             if anchor is not a legal value
	 * @throws NullPointerException
	 *             if <code>data</code> is <code>null</code>
	 * 
	 * @see #drawString(java.lang.String, int, int, int)
	 */
	public void drawChars(char[] data, int offset, int length, int x, int y, int anchor) {
		//    	   paint.setAntiAlias(true);
		//           paint.setColor(rgbColor);
		//           paint.setAlpha(255);
		//           x=anchorX(x,anchor,currentFont.charsWidth(data,offset,length));
		//           y=anchorY(y,anchor,currentFont.getHeight())+10;
		int newx = x;
		int newy = y;

		if (anchor == 0) {
			anchor = javax.microedition.lcdui.Graphics.TOP | javax.microedition.lcdui.Graphics.LEFT;
		}
		if ((anchor & javax.microedition.lcdui.Graphics.TOP) != 0) {

			newy -= font.m_paint.getFontMetricsInt().ascent;

		} else if ((anchor & javax.microedition.lcdui.Graphics.BOTTOM) != 0) {

			newy -= font.m_paint.getFontMetricsInt().descent;

		}

		if ((anchor & javax.microedition.lcdui.Graphics.HCENTER) != 0) {

			newx -= font.charsWidth(data, offset, length) / 2;

		} else if ((anchor & javax.microedition.lcdui.Graphics.RIGHT) != 0) {

			newx -= font.charsWidth(data, offset, length);

		}
		font.m_paint.setColor(adPaint.getColor());
		adCanvas.drawText(data, offset, length, newx, newy, font.m_paint);
	}

	/**
	 * Draws the specified Image3 by using the anchor point. The Image3 can be
	 * drawn in different positions relative to the anchor point by passing the
	 * appropriate position constants. See <a href="#anchor">anchor points</a>.
	 * 
	 * <p>
	 * If the source Image3 contains transparent pixels, the corresponding
	 * pixels in the destination Image3 must be left untouched. If the source
	 * Image3 contains partially transparent pixels, a compositing operation
	 * must be performed with the destination pixels, leaving all pixels of the
	 * destination Image3 fully opaque.
	 * </p>
	 * 
	 * <p>
	 * If <code>img</code> is the same as the destination of this Graphics
	 * object, the result is undefined. For copying areas within an
	 * <code>Image3</code>, {@link #copyArea copyArea} should be used instead.
	 * </p>
	 * 
	 * @param img
	 *            the specified Image3 to be drawn
	 * @param x
	 *            the x coordinate of the anchor point
	 * @param y
	 *            the y coordinate of the anchor point
	 * @param anchor
	 *            the anchor point for positioning the Image3
	 * @throws IllegalArgumentException
	 *             if <code>anchor</code> is not a legal value
	 * @throws NullPointerException
	 *             if <code>img</code> is <code>null</code>
	 * @see Image
	 */
	public void drawImage(Image image, int x, int y, int anchor) {
		if (image == null)
			throw new NullPointerException("Image Object is Null");

		if (image.getBitmap() == null)
			throw new NullPointerException("OMG!! Maybe you create a wrong Bitmap!");
		if (anchor == 0) {
			anchor = javax.microedition.lcdui.Graphics.TOP | javax.microedition.lcdui.Graphics.LEFT;

		}

		if ((anchor & javax.microedition.lcdui.Graphics.RIGHT) != 0) {

			x -= image.getWidth();

		} else if ((anchor & javax.microedition.lcdui.Graphics.HCENTER) != 0) {

			x -= image.getWidth() >> 1;

		}

		if ((anchor & javax.microedition.lcdui.Graphics.BOTTOM) != 0) {

			y -= image.getHeight();

		} else if ((anchor & javax.microedition.lcdui.Graphics.VCENTER) != 0) {

			y -= image.getHeight() >> 1;

		}
		//paint.setAntiAlias(true);
		adCanvas.drawBitmap(image.getBitmap(), x, y, adPaint);
	}

	private Matrix m_transMatrix = new Matrix();

	private final static float[] MATRIX_FLIPX = new float[]{-1, 0, 0, 0, 1, 0, 0, 0, 1};

	private final static float[] MATRIX_FLIPY = new float[]{1, 0, 0, 0, -1, 0, 0, 0, 1};

	private Matrix m_flipXMatrix = new Matrix();
	private Matrix getFlipXMatrix(int w) {
		MATRIX_FLIPX[2] = w;
		m_flipXMatrix.setValues(MATRIX_FLIPX);
		return m_flipXMatrix;
	}

	private Matrix m_flipYMatrix = new Matrix();
	private Matrix getFlipYMatrix(int h) {
		MATRIX_FLIPY[5] = h;
		m_flipYMatrix.setValues(MATRIX_FLIPY);
		return m_flipYMatrix;
	}

	/**
	 * Copies a region of the specified source Image3 to a location within the
	 * destination, possibly transforming (rotating and reflecting) the Image3
	 * data using the chosen transform function.
	 * 
	 * <p>
	 * The destination, if it is an Image3, must not be the same Image3 as the
	 * source Image3. If it is, an exception is thrown. This restriction is
	 * present in order to avoid ill-defined behaviors that might occur if
	 * overlapped, transformed copies were permitted.
	 * </p>
	 * 
	 * <p>
	 * The transform function used must be one of the following, as defined in
	 * the {@link javax.microedition.lcdui.game.Sprite Sprite} class:<br>
	 * 
	 * <code>Sprite.TRANS_NONE</code> - causes the specified Image3 region to be
	 * copied unchanged<br>
	 * <code>Sprite.TRANS_ROT90</code> - causes the specified Image3 region to
	 * be rotated clockwise by 90 degrees.<br>
	 * <code>Sprite.TRANS_ROT180</code> - causes the specified Image3 region to
	 * be rotated clockwise by 180 degrees.<br>
	 * <code>Sprite.TRANS_ROT270</code> - causes the specified Image3 region to
	 * be rotated clockwise by 270 degrees.<br>
	 * <code>Sprite.TRANS_MIRROR</code> - causes the specified Image3 region to
	 * be reflected about its vertical center.<br>
	 * <code>Sprite.TRANS_MIRROR_ROT90</code> - causes the specified Image3
	 * region to be reflected about its vertical center and then rotated
	 * clockwise by 90 degrees.<br>
	 * <code>Sprite.TRANS_MIRROR_ROT180</code> - causes the specified Image3
	 * region to be reflected about its vertical center and then rotated
	 * clockwise by 180 degrees.<br>
	 * <code>Sprite.TRANS_MIRROR_ROT270</code> - causes the specified Image3
	 * region to be reflected about its vertical center and then rotated
	 * clockwise by 270 degrees.<br>
	 * </p>
	 * 
	 * <p>
	 * If the source region contains transparent pixels, the corresponding
	 * pixels in the destination region must be left untouched. If the source
	 * region contains partially transparent pixels, a compositing operation
	 * must be performed with the destination pixels, leaving all pixels of the
	 * destination region fully opaque.
	 * </p>
	 * 
	 * <p>
	 * The <code>(x_src, y_src)</code> coordinates are relative to the upper
	 * left corner of the source Image3. The <code>x_src</code>,
	 * <code>y_src</code>, <code>width</code>, and <code>height</code>
	 * parameters specify a rectangular region of the source Image3. It is
	 * illegal for this region to extend beyond the bounds of the source Image3.
	 * This requires that:
	 * </P>
	 * <TABLE BORDER="2">
	 * <TR>
	 * <TD ROWSPAN="1" COLSPAN="1">
	 * 
	 * <pre>
	 * <code>
	 *   x_src &gt;= 0
	 *   y_src &gt;= 0
	 *   x_src + width &lt;= source width
	 *   y_src + height &lt;= source height    </code>
	 * </pre>
	 * 
	 * </TD>
	 * </TR>
	 * </TABLE>
	 * <P>
	 * The <code>(x_dest, y_dest)</code> coordinates are relative to the
	 * coordinate system of this Graphics object. It is legal for the
	 * destination area to extend beyond the bounds of the <code>Graphics</code>
	 * object. Pixels outside of the bounds of the <code>Graphics</code> object
	 * will not be drawn.
	 * </p>
	 * 
	 * <p>
	 * The transform is applied to the Image3 data from the region of the source
	 * Image3, and the result is rendered with its anchor point positioned at
	 * location <code>(x_dest, y_dest)</code> in the destination.
	 * </p>
	 * 
	 * @param src
	 *            the source Image3 to copy from
	 * @param x_src
	 *            the x coordinate of the upper left corner of the region within
	 *            the source Image3 to copy
	 * @param y_src
	 *            the y coordinate of the upper left corner of the region within
	 *            the source Image3 to copy
	 * @param width
	 *            the width of the region to copy
	 * @param height
	 *            the height of the region to copy
	 * @param transform
	 *            the desired transformation for the selected region being
	 *            copied
	 * @param x_dest
	 *            the x coordinate of the anchor point in the destination
	 *            drawing area
	 * @param y_dest
	 *            the y coordinate of the anchor point in the destination
	 *            drawing area
	 * @param anchor
	 *            the anchor point for positioning the region within the
	 *            destination Image3
	 * 
	 * @throws IllegalArgumentException
	 *             if <code>src</code> is the same Image3 as the destination of
	 *             this <code>Graphics</code> object
	 * @throws NullPointerException
	 *             if <code>src</code> is <code>null</code>
	 * @throws IllegalArgumentException
	 *             if <code>transform</code> is invalid
	 * @throws IllegalArgumentException
	 *             if <code>anchor</code> is invalid
	 * @throws IllegalArgumentException
	 *             if the region to be copied exceeds the bounds of the source
	 *             Image3
	 * @since MIDP 2.0
	 */
	public void drawRegion(Image src, int x_src, int y_src, int width, int height, int transform, int x_dest, int y_dest, int anchor) {
		if (x_src + width > src.getWidth() || y_src + height > src.getHeight() || width < 0 || height < 0 || x_src < 0

		|| y_src < 0)

			throw new IllegalArgumentException("Area out of Image");

		if (src.isMutable() && src.getGraphics() == this)

			throw new IllegalArgumentException("Image is source and target");

		boolean rotated = transform == Sprite.TRANS_ROT90 || transform == Sprite.TRANS_ROT270 || transform == Sprite.TRANS_MIRROR_ROT90 || transform == Sprite.TRANS_MIRROR_ROT270;
		if ((anchor & Graphics.HCENTER) != 0) {
			x_dest -= (rotated ? height : width) >> 1;
		} else if ((anchor & Graphics.RIGHT) != 0) {
			x_dest -= rotated ? height : width;
		}

		if ((anchor & Graphics.VCENTER) != 0) {
			y_dest -= (rotated ? width : height) >> 1;
		} else if ((anchor & Graphics.BOTTOM) != 0) {
			y_dest -= (rotated ? width : height);
		}

		Bitmap img = src.getBitmap();
		if (transform == Sprite.TRANS_NONE) {
			//if no transforms are presented,draw the src image directly in a clipped area.
			adCanvas.save(Canvas.CLIP_SAVE_FLAG);
			adCanvas.clipRect(x_dest, y_dest, x_dest + width, y_dest + height);
			adCanvas.drawBitmap(img, x_dest - x_src, y_dest - y_src, adPaint);
			adCanvas.restore();
			return;
		}

		int srcW = img.getWidth();
		int srcH = img.getHeight();
		int drawX = x_dest;
		int drawY = y_dest;

		m_transMatrix.reset();

		switch (transform) {
			case Sprite.TRANS_ROT180 :
				m_transMatrix.setRotate(180);
				m_transMatrix.postTranslate(srcW, srcH);
				x_src = srcW - x_src - width;
				drawX -= x_src;
				y_src = srcH - y_src - height;
				drawY -= y_src;
				break;
			case Sprite.TRANS_ROT90 :
				m_transMatrix.setRotate(90);
				m_transMatrix.postTranslate(srcH, 0);
				drawX -= y_src;
				//x_src = srcW - x_src - width;
				drawY -= x_src;
				break;
			case Sprite.TRANS_MIRROR :
				m_transMatrix.postConcat(this.getFlipXMatrix(srcW));
				//m_transMatrix.postTranslate(srcW, srcH);
				x_src = srcW - x_src - width;
				drawX -= x_src;
				drawY -= y_src;
				break;

			case Sprite.TRANS_MIRROR_ROT180 :
				m_transMatrix.postConcat(this.getFlipYMatrix(srcH));
				//m_transMatrix.postTranslate(srcW, srcH);
				//x_src = srcW - x_src - width;
				drawX -= x_src;
				y_src = srcH - y_src - height;
				drawY -= y_src;
				break;

			case Sprite.TRANS_ROT270 :
				m_transMatrix.setRotate(270);
				m_transMatrix.postTranslate(0, srcW);
				x_src = srcW - x_src - width;
				//y_src = srcH - y_src - height;
				drawX -= y_src;
				drawY -= x_src;
				break;
			case Sprite.TRANS_MIRROR_ROT90 :
				m_transMatrix.postConcat(this.getFlipXMatrix(srcW));
				m_transMatrix.postRotate(90);
				m_transMatrix.postTranslate(srcH, 0);
				x_src = srcW - x_src - width;
				y_src = srcH - y_src - height;
				drawX -= y_src;
				drawY -= x_src;
				break;
			case Sprite.TRANS_MIRROR_ROT270 :
				m_transMatrix.postConcat(this.getFlipXMatrix(srcW));
				m_transMatrix.postRotate(270);
				m_transMatrix.postTranslate(0, srcW);
				drawX -= y_src;
				drawY -= x_src;
				break;

		}
		m_transMatrix.postTranslate(drawX, drawY);

		adCanvas.save(Canvas.CLIP_SAVE_FLAG);
		adCanvas.clipRect(x_dest, y_dest, x_dest + (rotated ? height : width), y_dest + (rotated ? width : height));
		adCanvas.drawBitmap(img, m_transMatrix, adPaint);
		adCanvas.restore();

	}

	/**
	 * Copies the contents of a rectangular area
	 * <code>(x_src, y_src, width, height)</code> to a destination area, whose
	 * anchor point identified by anchor is located at
	 * <code>(x_dest, y_dest)</code>. The effect must be that the destination
	 * area contains an exact copy of the contents of the source area
	 * immediately prior to the invocation of this method. This result must
	 * occur even if the source and destination areas overlap.
	 * 
	 * <p>
	 * The points <code>(x_src, y_src)</code> and <code>(x_dest,
	 * y_dest)</code> are both specified relative to the coordinate system of
	 * the <code>Graphics</code> object. It is illegal for the source region to
	 * extend beyond the bounds of the graphic object. This requires that:
	 * </P>
	 * <TABLE BORDER="2">
	 * <TR>
	 * <TD ROWSPAN="1" COLSPAN="1">
	 * 
	 * <pre>
	 * <code>
	 *   x_src + tx &gt;= 0
	 *   y_src + ty &gt;= 0
	 *   x_src + tx + width &lt;= width of Graphics object's destination
	 *   y_src + ty + height &lt;= height of Graphics object's destination      </code>
	 * </pre>
	 * 
	 * </TD>
	 * </TR>
	 * </TABLE>
	 * 
	 * <p>
	 * where <code>tx</code> and <code>ty</code> represent the X and Y
	 * coordinates of the translated origin of this graphics object, as returned
	 * by <code>getTranslateX()</code> and <code>getTranslateY()</code>,
	 * respectively.
	 * </p>
	 * 
	 * <P>
	 * However, it is legal for the destination area to extend beyond the bounds
	 * of the <code>Graphics</code> object. Pixels outside of the bounds of the
	 * <code>Graphics</code> object will not be drawn.
	 * </p>
	 * 
	 * <p>
	 * The <code>copyArea</code> method is allowed on all <code>Graphics</code>
	 * objects except those whose destination is the actual display device. This
	 * restriction is necessary because allowing a <code>copyArea</code> method
	 * on the display would adversely impact certain techniques for implementing
	 * double-buffering.
	 * </p>
	 * 
	 * <p>
	 * Like other graphics operations, the <code>copyArea</code> method uses the
	 * Source Over Destination rule for combining pixels. However, since it is
	 * defined only for mutable images, which can contain only fully opaque
	 * pixels, this is effectively the same as pixel replacement.
	 * </p>
	 * 
	 * @param x_src
	 *            the x coordinate of upper left corner of source area
	 * @param y_src
	 *            the y coordinate of upper left corner of source area
	 * @param width
	 *            the width of the source area
	 * @param height
	 *            the height of the source area
	 * @param x_dest
	 *            the x coordinate of the destination anchor point
	 * @param y_dest
	 *            the y coordinate of the destination anchor point
	 * @param anchor
	 *            the anchor point for positioning the region within the
	 *            destination Image3
	 * 
	 * @throws IllegalStateException
	 *             if the destination of this <code>Graphics</code> object is
	 *             the display device
	 * @throws IllegalArgumentException
	 *             if the region to be copied exceeds the bounds of the source
	 *             Image3
	 * 
	 * @since MIDP 2.0
	 */
	public void copyArea(int x_src, int y_src, int width, int height, int x_dest, int y_dest, int anchor) {

	}

	/**
	 * Fills the specified triangle will the current color. The lines connecting
	 * each pair of points are included in the filled triangle.
	 * 
	 * @param x1
	 *            the x coordinate of the first vertex of the triangle
	 * @param y1
	 *            the y coordinate of the first vertex of the triangle
	 * @param x2
	 *            the x coordinate of the second vertex of the triangle
	 * @param y2
	 *            the y coordinate of the second vertex of the triangle
	 * @param x3
	 *            the x coordinate of the third vertex of the triangle
	 * @param y3
	 *            the y coordinate of the third vertex of the triangle
	 * 
	 * @since MIDP 2.0
	 */
	public void fillTriangle(int x1, int y1, int x2, int y2, int x3, int y3) {

	}

	/**
	 * implementation of CopyArea method.
	 * 
	 * @param x_src
	 *            the x coordinate of upper left corner of source area
	 * @param y_src
	 *            the y coordinate of upper left corner of source area
	 * @param width
	 *            the width of the source area
	 * @param height
	 *            the height of the source area
	 * @param x_dest
	 *            the x coordinate of the destination anchor point
	 * @param y_dest
	 *            the y coordinate of the destination anchor point
	 * @param anchor
	 *            the anchor point for positioning the region within the
	 *            destination Image3
	 * 
	 * @throws IllegalArgumentException
	 *             if the region to be copied exceeds the bounds of the source
	 *             Image3
	 */
	//    private   void doCopyArea(int x_src, int y_src, 
	//				   int width, int height, 
	//				   int x_dest, int y_dest, int anchor)
	//    {
	////    	graphics.draw
	//    }

	/**
	 * Renders a series of device-independent RGB+transparency values in a
	 * specified region. The values are stored in <code>rgbData</code> in a
	 * format with <code>24</code> bits of RGB and an eight-bit alpha value (
	 * <code>0xAARRGGBB</code>), with the first value stored at the specified
	 * offset. The <code>scanlength</code> specifies the relative offset within
	 * the array between the corresponding pixels of consecutive rows. Any value
	 * for <code>scanlength</code> is acceptable (even negative values) provided
	 * that all resulting references are within the bounds of the
	 * <code>rgbData</code> array. The ARGB data is rasterized horizontally from
	 * left to right within each row. The ARGB values are rendered in the region
	 * specified by <code>x</code>, <code>y</code>, <code>width</code> and
	 * <code>height</code>, and the operation is subject to the current clip
	 * region and translation for this <code>Graphics</code> object.
	 * 
	 * <p>
	 * Consider <code>P(a,b)</code> to be the value of the pixel located at
	 * column <code>a</code> and row <code>b</code> of the Image3, where rows
	 * and columns are numbered downward from the top starting at zero, and
	 * columns are numbered rightward from the left starting at zero. This
	 * operation can then be defined as:
	 * </p>
	 * 
	 * <TABLE BORDER="2">
	 * <TR>
	 * <TD ROWSPAN="1" COLSPAN="1">
	 * 
	 * <pre>
	 * <code>
	 *    P(a, b) = rgbData[offset + (a - x) + (b - y) * scanlength]       </code>
	 * </pre>
	 * 
	 * </TD>
	 * </TR>
	 * </TABLE>
	 * 
	 * <p>
	 * for
	 * </p>
	 * 
	 * <TABLE BORDER="2">
	 * <TR>
	 * <TD ROWSPAN="1" COLSPAN="1">
	 * 
	 * <pre>
	 * <code>
	 *     x &lt;= a &lt; x + width
	 *     y &lt;= b &lt; y + height    </code>
	 * </pre>
	 * 
	 * </TD>
	 * </TR>
	 * </TABLE>
	 * <p>
	 * This capability is provided in the <code>Graphics</code> class so that it
	 * can be used to render both to the screen and to offscreen
	 * <code>Image3</code> objects. The ability to retrieve ARGB values is
	 * provided by the {@link Image#getRGB} method.
	 * </p>
	 * 
	 * <p>
	 * If <code>processAlpha</code> is <code>true</code>, the high-order byte of
	 * the ARGB format specifies opacity; that is, <code>0x00RRGGBB</code>
	 * specifies a fully transparent pixel and <code>0xFFRRGGBB</code> specifies
	 * a fully opaque pixel. Intermediate alpha values specify semitransparency.
	 * If the implementation does not support alpha blending for Image3
	 * rendering operations, it must remove any semitransparency from the source
	 * data prior to performing any rendering. (See <a
	 * href="Image3.html#alpha">Alpha Processing</a> for further discussion.) If
	 * <code>processAlpha</code> is <code>false</code>, the alpha values are
	 * ignored and all pixels must be treated as completely opaque.
	 * </p>
	 * 
	 * <p>
	 * The mapping from ARGB values to the device-dependent pixels is
	 * platform-specific and may require significant computation.
	 * </p>
	 * 
	 * @param rgbData
	 *            an array of ARGB values in the format <code>0xAARRGGBB</code>
	 * @param offset
	 *            the array index of the first ARGB value
	 * @param scanlength
	 *            the relative array offset between the corresponding pixels in
	 *            consecutive rows in the <code>rgbData</code> array
	 * @param x
	 *            the horizontal location of the region to be rendered
	 * @param y
	 *            the vertical location of the region to be rendered
	 * @param width
	 *            the width of the region to be rendered
	 * @param height
	 *            the height of the region to be rendered
	 * @param processAlpha
	 *            <code>true</code> if <code>rgbData</code> has an alpha
	 *            channel, false if all pixels are fully opaque
	 * 
	 * @throws ArrayIndexOutOfBoundsException
	 *             if the requested operation will attempt to access an element
	 *             of <code>rgbData</code> whose index is either negative or
	 *             beyond its length
	 * @throws NullPointerException
	 *             if <code>rgbData</code> is <code>null</code>
	 * 
	 * @since MIDP 2.0
	 */
	public void drawRGB(int[] rgbData, int offset, int scanlength, int x, int y, int width, int height, boolean processAlpha) {
		adCanvas.drawBitmap(rgbData, offset, scanlength, x, y, width, height, processAlpha, adPaint);
	}

	/**
	 * Gets the color that will be displayed if the specified color is
	 * requested. This method enables the developer to check the manner in which
	 * RGB values are mapped to the set of distinct colors that the device can
	 * actually display. For example, with a monochrome device, this method will
	 * return either <code>0xFFFFFF</code> (white) or <code>0x000000</code>
	 * (black) depending on the brightness of the specified color.
	 * 
	 * @param color
	 *            the desired color (in <code>0x00RRGGBB</code> format, the
	 *            high-order byte is ignored)
	 * @return the corresponding color that will be displayed on the device's
	 *         screen (in <code>0x00RRGGBB</code> format)
	 * 
	 * @since MIDP 2.0
	 */
	public int getDisplayColor(int color) {
		return 0;
	}

	// private implementation //

	/** Translated x,y coordinates */
	//    private int transX, transY;
	//    /** top/left clip bounds */
	//    private short clipX1, clipY1;
	//    /** right/bottom clip bounds */
	//    private int clipX2, clipY2;
	/**
	 * temporary variable for top/left clip bounds use a private data member
	 * instead of a new variable for every invocation of clipRect
	 */
	//    private int translatedX1, translatedY1;
	/**
	 * temporary variable for right/bottom clip bounds use a private data member
	 * instead of a new variable for every invocation of clipRect
	 */
	//    private int translatedX2, translatedY2;
	/** Array to hold the clip values */
	//    private short clip[] = new short[4];     /* format is -- x y w h */
	/** A flag indicating the clipping state */
	//    private boolean clipped = false;
	/** Pixel values */
	private int rgbColor, gray, pixel;
	/** Line stroke style */
	private int style; // line stroke style
	/** The current FontR */
	private Font font = Font.getDefaultFont();
	/** The maximum width and height */
	//    private short maxWidth, maxHeight;
	/** The 'destination' Image3 to paint to */
	//    Image destination;

	/**
	 * Retrieve the Graphics context for the given Image3
	 * 
	 * @param img
	 *            The Image3 to get a Graphics context for
	 * @return Graphics Will return a new ImageGraphics object if the Image3 is
	 *         non-null, otherwise will return a new Graphics object with
	 *         Display.WIDTH maximum width and Display.HEIGHT maximum height
	 */
	//    public static Graphics getGraphics(Image img) {
	//        if (img == null) {
	//            return new Graphics(HUIConfig.getWidth(), HUIConfig.getHeight());
	//        } else {
	//            return img.getGraphics();//modified by yan 
	//        }
	//    }

	/**
	 * Reset this Graphics context with the given coordinates
	 * 
	 * @param x1
	 *            The upper left x coordinate
	 * @param y1
	 *            The upper left y coordinate
	 * @param x2
	 *            The lower right x coordinate
	 * @param y2
	 *            The lower right y coordinate
	 */
	void reset(int x1, int y1, int x2, int y2) {
		//        transX = transY = 0;
		font = Font.getDefaultFont();
		style = SOLID;
		rgbColor = gray = 0;
		pixel = getPixel(rgbColor, gray, true);

		setClip(x1, y1, x2 - x1, y2 - y1);
	}

	/**
	 * Reset this Graphics context to its default dimensions (same as reset(0,
	 * 0, maxWidth, maxHeight)
	 */
	//    void reset() {
	//        reset(0, 0, maxWidth, maxHeight);
	//    }

	/**
	 * Get a gray value given the RGB values
	 * 
	 * @param red
	 *            The Red pixel value
	 * @param green
	 *            The Green pixel value
	 * @param blue
	 *            The Blue pixel value
	 * @return int The grayscale value corresponding to the RGB color
	 */
	//    private static int grayVal(int red, int green, int blue) {
	//        /* CCIR Rec 601 luma (nonlinear rgb to nonlinear "gray") */
	//        return (red*76 + green*150 + blue*29) >> 8;
	//    }

	/**
	 * Get a specific pixel value
	 * 
	 * @param rgb
	 * @param gray
	 * @param isGray
	 * @return int
	 */
	private int getPixel(int rgb, int gray, boolean isGray) {
		return 0;
	}

} // class Graphics

/**
 * <p>
 * SYNC NOTE: Unlike the Graphics class, multiple threads may issue calls on
 * ImageGraphics instances at any time. It is therefore the responsibility of
 * the ImageGraphics class to ensure that all public methods are MT-safe.
 * </p>
 * 
 * <p>
 * Some methods are implicitly safe already, namely those that are methods and
 * those that make a single atomic access to instance state. Those method
 * implementations are simply inherited from the superclass. The inherited
 * methods are included below, within comments, to indicate this fact. If the
 * superclass' implementation of a method changes, this class may need to
 * override it and provide locking.
 * </p>
 * 
 * <p>
 * Any method that has multiple accesses to shared state must be protected with
 * a synchronized block. As for the Graphics class, all access is to the state
 * of this instance, and so locking is done on 'this' instead of on
 * Display.LCDUILock.
 * </p>
 */
//class ImageGraphics extends GraphicsR {
//
//    /**
//     * Constuct a new ImageGraphics
//     *
//     * @param img The Image3 to use to construct the ImageGraphics
//     */
//    ImageGraphics(ImageR img) {
//        super(img.getWidth(), img.getHeight());
//        destination = img;
//    }
//
//    /**
//     * Translate this Graphics context by the given offset
//     *
//     * @param x The x coordinate offset
//     * @param y The y coordinate offset
//     */
//    public void translate(int x, int y) {
//        synchronized (this) {
//            super.translate(x, y);
//        }
//    }
//
//    /**
//     * Set the Color of this Graphics context
//     *
//     * @param red The Red color component
//     * @param green The Green color component
//     * @param blue The Blue color component
//     * @see #getColor
//     */
//    public void setColor(int red, int green, int blue) {
//        synchronized (this) {
//            super.setColor(red, green, blue);
//        }
//    }
//
//    /**
//     * Set the Color of this Graphics context
//     *
//     * @param RGB The RGB composite color value
//     * @see #getColor
//     */
//    public void setColor(int RGB) {
//        synchronized (this) {
//            super.setColor(RGB);
//        }
//    }
//
//    /**
//     * Set the gray scale value of this Graphics context
//     *
//     * @param value The gray scale value
//     */
//    public void setGrayScale(int value) {
//        synchronized (this) {
//            super.setGrayScale(value);
//        }
//    }
//
//    /**
//     * Get the x coordinate of the clip
//     *
//     * @return int The x coordinate of the clip
//     */
//    public int getClipX() {
//        synchronized (this) {
//            return super.getClipX();
//        }
//    }
//
//    /**
//     * Get the y coordinate of the clip
//     *
//     * @return int The y coordinate of the clip
//     */
//    public int getClipY() {
//        synchronized (this) {
//            return super.getClipY();
//        }
//    }
//
//    // SYNC NOTE: the superclass implementations of getClipWidth() and
//    // getClipHeight() read a single value atomically, so no locking is 
//    // necessary.  We simply inherit the implementations from the superclass.
//
//    // public int getClipWidth();
//    // public int getClipHeight();
//
//    /**
//     * Add the clip rectangle of this Graphics context
//     *
//     * @param x The x coordinate of the rectangle
//     * @param y The y coordinate of the rectangle
//     * @param w The width of the rectangle
//     * @param h The height of the rectangle
//     */
//    public void clipRect(int x, int y, int w, int h) {
//        synchronized (this) {
//            super.clipRect(x, y, w, h);
//        }
//    }
//
//    /**
//     * Set the clip of this Graphics context
//     *
//     * @param x The x coordinate of the clip
//     * @param y The y coordinate of the clip
//     * @param w The width of the clip
//     * @param h The height of the clip
//     */
//    public void setClip(int x, int y, int w, int h) {
//        synchronized (this) {
//            super.setClip(x, y, w, h);
//        }
//    }
//
//    // SYNC NOTE: all of the following are   methods, which run
//    // atomically, so no synchronization is necessary.  We therefore simply
//    // inherit them from the superclass.
//
//    // public   void drawLine(int x1, int y1, int x2, int y2);
//
//    // public   void fillRect(int x, int y, int width, int height);
//
//    // public   void drawRect(int x, int y, int width, int height);
//
//    // public   void drawRoundRect(int x, int y, int width, int height,
//    //                                  int arcWidth, int arcHeight);
//
//    // public   void fillRoundRect(int x, int y, int width, int height,
//    //                                  int arcWidth, int arcHeight);
//
//    // public   void fillArc(int x, int y, int width, int height,
//    //                            int startAngle, int arcAngle);
//
//    // public   void drawArc(int x, int y, int width, int height,
//    //                            int startAngle, int arcAngle);
//
//    // public   void fillTriangle(int x1, int y1, int x2, int y2,
//    //                                 int x3, int y3);
//
//    // public   void drawString(java.lang.String str,
//    //                               int x, int y, int anchor);
//
//    // public   void drawSubstring(String str, int offset, int len,
//    //                                  int x, int y, int anchor);
//
//    // public   void drawChar(char character, int x, int y, int anchor);
//
//    // public   void drawChars(char[] data, int offset, int length,
//    //                              int x, int y, int anchor);
//
//    // public   void drawImage(Image3 img, int x, int y, int anchor);
//    
//    // public   void drawRGB(int[] rgbData, int offset, int scanlength,
//    //	 		          int x, int y, int width, int height,
//    //		                  boolean processAlpha);
//
//    // public   int getDisplayColor(int color);
//} // class ImageGraphics
