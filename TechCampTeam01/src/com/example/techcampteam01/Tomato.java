package com.example.techcampteam01;

import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Tomato {
	/**
	 * Tomato object
	 * 
	 * @author ミン・ドゥック
	 * @author ティエップ
	 */
	private static final int MAX_PICTURE_SIZE = 180; // max size of tomato's
														// picture will be
														// loaded

	private int currentTomtatoSize;
	// tomato picture's top-left point
	private float x;
	private float y;
	// parent container
	private Preview parent;
	private boolean splashDone;

	Paint tPaint;
	Paint bPaint;
	Paint lPaint;

	// moving speed
	int velosityX;
	int velosityY;

	private Bitmap scaledBitmap;

	private Bitmap rawbitmap;

	private Bitmap splashRaw;

	SplashTomato splash;

	boolean startFlash;

	int drawSplashTime;

	public Tomato(Preview parent) {

		this.parent = parent;
		x = parent.getWidth() - MAX_PICTURE_SIZE;
		y = parent.getHeight() - MAX_PICTURE_SIZE;

		currentTomtatoSize = MAX_PICTURE_SIZE;
		//

		tPaint = new Paint();
		tPaint.setColor(Color.RED);
		bPaint = new Paint();
		lPaint = new Paint();
		lPaint = new Paint(Color.RED);
		velosityX = parent.getWidth() / 30;
		velosityY = parent.getHeight() / 30 + 3;

		rawbitmap = AssetManager.rawbitmap;

		splashRaw = AssetManager.splashRaw;

		scaledBitmap = Bitmap.createScaledBitmap(rawbitmap, currentTomtatoSize,
				currentTomtatoSize, true);

		splash = new SplashTomato(parent);

		startFlash = false;

		splashDone = false;

	}

	/**
	 * Draw tomato animation onto the screen
	 * 
	 * @param canvas
	 *            canvas from parent container
	 */
	public void draw(final Canvas canvas) {

		if (splashDone) {
			// removeFromListHolder();
			return;
		}

		if (x < parent.getWidth() / 2 - currentTomtatoSize / 2) {

			if (!startFlash) {
				startFlash = true;
				drawSplashTime = 500;
			}

			if (startFlash) {

				splash.draw(canvas);
				new SleepThread().start();

				if (drawSplashTime <= 0)
					splashDone = true;
			}

		}

		else {
			canvas.drawBitmap(scaledBitmap, x, y, bPaint);

			x -= velosityX;
			y -= velosityY;

			currentTomtatoSize -= 2;
			if (currentTomtatoSize > 0)
				scaledBitmap = Bitmap.createScaledBitmap(rawbitmap,
						currentTomtatoSize, currentTomtatoSize, true);

		}

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

	/**
	 * @author ティエップ Remove this Tomato from list Holder in Preview.
	 */

	private void removeFromListHolder() {
		List<Tomato> listHolder = ((Preview) parent).getListHolder();
		listHolder.remove(this);
	}

	class SleepThread extends Thread {
		@Override
		public void run() {

			try {
				Thread.sleep(100);
				drawSplashTime -= 100;

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

}
