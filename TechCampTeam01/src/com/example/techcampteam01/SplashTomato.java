package com.example.techcampteam01;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

/**
 * 
 * @author ティエップ
 * 
 */

public class SplashTomato {

	Preview parent;

	float x;
	float y;

	Bitmap splashRaw;
	Paint painter;

	public SplashTomato(View parent2, Bitmap splash) {
		splashRaw = splash;
		x = 0;
		y = 0;
		this.parent = (Preview) parent2;
		painter = new Paint();

	}

	/**
	 * Set Splash Size to compatible with Tomato'size
	 * 
	 * @author ティエップ
	 * @param currentTomatoSize
	 *            : Tomato'size
	 */

	public void setSizeBaseTomatoSize(int currentTomatoSize) {

		splashRaw = Bitmap.createScaledBitmap(splashRaw, currentTomatoSize,
				currentTomatoSize, true);
	}

	/**
	 * Draw Splash
	 * 
	 * @author ティエップ
	 * 
	 * @param canvas
	 *            canvas to draw Bitmap
	 */

	public void draw(Canvas canvas) {

		x = parent.getWidth() / 2 - 90;
		y = parent.getHeight() / 2 - 90;
		canvas.drawBitmap(splashRaw, x, y, painter);

	}
}
