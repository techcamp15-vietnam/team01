package com.example.techcampteam01;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class Tomato {
/**
 * Tomato object
 * @author ミン・ドゥック 
 * @author ティエプ
 */
	private static final int MAX_PICTURE_SIZE = 180; //max size of tomato's picture will be loaded

	private int currentTomtatoSize = MAX_PICTURE_SIZE;
	// tomato picture's top-left point
	private float x;
	private float y;
	//parent container
	private View parent;
	private boolean splashDone = false;

	Paint tPaint;
	Paint bPaint;
	Paint lPaint;

	// moving speed
	int velosityX;
	int velosityY;
	
	private Bitmap scaledBitmap;

	private Bitmap rawbitmap;

	private Bitmap splashRaw;

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
		velosityX = parent.getWidth() / 50;
		velosityY = parent.getHeight() / 50;

		rawbitmap = BitmapFactory.decodeResource(parent.getResources(),
				R.drawable.tomato);
		
		
		scaledBitmap = Bitmap.createScaledBitmap(rawbitmap, currentTomtatoSize,
				currentTomtatoSize, true);
		
		 splashRaw = BitmapFactory.decodeResource(
				parent.getResources(), R.drawable.splash);

	}

	/**
	 * Draw tomato animation onto the screen
	 * @param canvas canvas from parent container
	 */
	public void draw(Canvas canvas) {

		if (x < parent.getWidth() / 2 - currentTomtatoSize / 2) {
			Paint tPaint = new Paint();
			
			Bitmap splash = Bitmap.createScaledBitmap(splashRaw,
					currentTomtatoSize, currentTomtatoSize, true);
			canvas.drawBitmap(splash, x, y, tPaint);
			splashDone = true;
		}

		canvas.drawBitmap(scaledBitmap, x, y, bPaint);

		x -= velosityX;
		y -= velosityY;

		currentTomtatoSize -= 2;
		scaledBitmap = Bitmap.createScaledBitmap(rawbitmap, currentTomtatoSize,
				currentTomtatoSize, true);

	}
	
	/**
	 * @author ミン・ドゥック 
	 * @return true if Tomato Splash done.
	 */

	public boolean getDoneSplash() {
		return splashDone;
	}

	public void setDoneSplash(boolean splashDone) {
		this.splashDone = splashDone;
	}

}
