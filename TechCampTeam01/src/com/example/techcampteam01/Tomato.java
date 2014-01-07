package com.example.techcampteam01;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class Tomato {

	private static final int MAX_PICTURE_SIZE = 180;

	private int currentTomtatoSize = MAX_PICTURE_SIZE;
	private float x;
	private float y;
	private View parent;
	private boolean splashDone = false;

	Paint tPaint;
	Paint bPaint;
	Paint lPaint;

	int deltaX;
	int deltaY;
	private Bitmap bitmap;

	private Bitmap rawbitmap;

	public Tomato(View parent) {

		this.parent = parent;
		x = parent.getWidth() - MAX_PICTURE_SIZE;
		y = parent.getHeight() - MAX_PICTURE_SIZE;

		//

		tPaint = new Paint();
		tPaint.setColor(Color.RED);
		bPaint = new Paint();
		lPaint = new Paint();
		lPaint = new Paint(Color.RED);
		deltaX = parent.getWidth() / 70;
		deltaY = parent.getHeight() / 70;

		rawbitmap = BitmapFactory.decodeResource(parent.getResources(),
				R.drawable.tomato);
		bitmap = Bitmap.createScaledBitmap(rawbitmap, currentTomtatoSize,
				currentTomtatoSize, true);

	}

	public void draw(Canvas canvas) {

		if (x < parent.getWidth() / 2 - currentTomtatoSize / 2) {
			Paint tPaint = new Paint();
			Bitmap splashRaw = BitmapFactory.decodeResource(
					parent.getResources(), R.drawable.splash);
			Bitmap splash = Bitmap.createScaledBitmap(splashRaw,
					currentTomtatoSize, currentTomtatoSize, true);
			canvas.drawBitmap(splash, x, y, tPaint);
			splashDone = true;
		}

		canvas.drawBitmap(bitmap, x, y, bPaint);

		x -= deltaX;
		y -= deltaY;

		currentTomtatoSize -= 2;
		bitmap = Bitmap.createScaledBitmap(rawbitmap, currentTomtatoSize,
				currentTomtatoSize, true);

	}

	public boolean getDoneSplash() {
		return splashDone;
	}

	public void setDoneSplash(boolean splashDone) {
		this.splashDone = splashDone;
	}

}
