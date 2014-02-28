package javax.microedition.lcdui;

import android.view.*;
import android.app.*;
import android.widget.*;
import android.graphics.*;
public class Font {
	public static final int FACE_MONOSPACE = 32;
	public static final int FACE_PROPORTIONAL = 64;
	public static final int FACE_SYSTEM = 0;
	public static final int FONT_INPUT_TEXT = 1;
	public static final int FONT_STATIC_TEXT = 0;
	public static final int SIZE_LARGE = 16;
	public static final int SIZE_MEDIUM = 0;
	public static final int SIZE_SMALL = 8;
	public static final int STYLE_BOLD = 1;
	public static final int STYLE_ITALIC = 2;
	public static final int STYLE_PLAIN = 0;
	public static final int STYLE_UNDERLINED = 4;

	private static Font s_defaultFont;

	private int m_face;
	private int m_style;
	private int m_size;

	protected Paint m_paint = new Paint();
	public static Font getDefaultFont() {
		if (s_defaultFont == null) {
			s_defaultFont = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_MEDIUM);
		}
		return s_defaultFont;
	}

	public int charWidth(char c) {
		return charsWidth(new char[]{c}, 0, 1);
	}

	public int charsWidth(char[] data, int offset, int length) {
		return (int) m_paint.measureText(data, offset, length);
	}

	public int getBaselinePosition() {
		return 10;
	}

	public int getFace() {
		return m_face;
	}

	public static Font getFont(int fontSpecifier) {
		return s_defaultFont;
	}

	public static Font getFont(int face, int style, int size) {
		Font f = new Font();
		f.m_face = face;
		f.m_style = style;
		if ((style & Font.STYLE_BOLD) != 0) {
			//deprecated.
		}
		if ((style & Font.STYLE_ITALIC) != 0) {
			f.m_paint.setTextSkewX(-0.25f);
		}
		if ((style & Font.STYLE_UNDERLINED) != 0) {
			f.m_paint.setUnderlineText(true);
		}

		f.m_size = size;
		switch (size) {
			case Font.SIZE_LARGE :
				f.m_paint.setTextSize(f.m_sizeLarge);
				break;
			case Font.SIZE_MEDIUM :
				f.m_paint.setTextSize(f.m_sizeMedium);
				break;
			case Font.SIZE_SMALL :
				f.m_paint.setTextSize(f.m_sizeSmall);
				break;
		}
		f.m_paint.setAntiAlias(true);
		return f;
	}

	private final static int DEFAULT_FONTSIZE_LARGE = 22;
	private final static int DEFAULT_FONTSIZE_MEDIUM = 18;
	private final static int DEFAULT_FONTSIZE_SMALL = 15;
	private int m_sizeLarge = DEFAULT_FONTSIZE_LARGE;
	private int m_sizeMedium = DEFAULT_FONTSIZE_MEDIUM;
	private int m_sizeSmall = DEFAULT_FONTSIZE_SMALL;
	public void setSize(int key, int value, boolean updateAll) {
		switch (key) {
			case Font.SIZE_LARGE :
				m_sizeLarge = value;
				if (updateAll) {
					m_sizeMedium = DEFAULT_FONTSIZE_MEDIUM * value / DEFAULT_FONTSIZE_LARGE;
					m_sizeSmall = DEFAULT_FONTSIZE_SMALL * value / DEFAULT_FONTSIZE_LARGE;
				}
				break;
			case Font.SIZE_MEDIUM :
				m_sizeMedium = value;
				if (updateAll) {
					m_sizeLarge = DEFAULT_FONTSIZE_LARGE * value / DEFAULT_FONTSIZE_MEDIUM;
					m_sizeSmall = DEFAULT_FONTSIZE_SMALL * value / DEFAULT_FONTSIZE_MEDIUM;
				}
				break;
			case Font.SIZE_SMALL :
				m_sizeSmall = value;
				if (updateAll) {
					m_sizeLarge = DEFAULT_FONTSIZE_LARGE * value / DEFAULT_FONTSIZE_SMALL;
					m_sizeMedium = DEFAULT_FONTSIZE_MEDIUM * value / DEFAULT_FONTSIZE_SMALL;
				}
				break;
		}
	}

	public int getHeight() {
		return (int) (m_paint.getFontMetrics().bottom - m_paint.getFontMetrics().top);
	}

	public int getSize() {
		return m_size;
	}

	public int getStyle() {
		return m_style;
	}

	public boolean isBold() {
		return (m_style & STYLE_BOLD) != 0;
	}

	public boolean isItalic() {
		return (m_style & STYLE_ITALIC) != 0;
	}

	public boolean isPlain() {
		return m_style == 0;
	}

	public boolean isUnderlined() {
		return (m_style & STYLE_UNDERLINED) != 0;
	}

	public int stringWidth(String str) {
		return (int) m_paint.measureText(str);
	}

	public int substringWidth(String str, int offset, int len) {
		return (int) m_paint.measureText(str, offset, offset + len);
	}
}
