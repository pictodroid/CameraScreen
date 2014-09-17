package com.app.pictolike;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

public class ColorBarDrawable extends Drawable {

	private int[] themeColors;

	public ColorBarDrawable(int[] themeColors) {
		this.themeColors = themeColors;
	}

	@Override
	public void draw(Canvas canvas) {
		final int IMG_OFFSET = 10;
		Rect bounds = getBounds();

		Paint backgroundPaint = new Paint();
		backgroundPaint.setColor(themeColors[1]);
		backgroundPaint.setStyle(Style.STROKE);
		backgroundPaint.setStrokeWidth(20);
		canvas.drawRect(0, 0, bounds.width(), bounds.height(), backgroundPaint);

		backgroundPaint.setColor(themeColors[0]);
		backgroundPaint.setStyle(Style.FILL);
		canvas.drawRect(IMG_OFFSET, IMG_OFFSET, bounds.width() - IMG_OFFSET,
				bounds.height() - IMG_OFFSET, backgroundPaint);
	}

	@Override
	public void setAlpha(int alpha) {
	}

	@Override
	public void setColorFilter(ColorFilter cf) {

	}

	@Override
	public int getOpacity() {
		return PixelFormat.OPAQUE;
	}

}