package com.example.techcampteam01;

import java.util.List;
import java.util.Random;

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

	// boolean isAlive;

	public enum FruitType {
		TOMATO, APPLE, EGG
	}

	private FruitType fruitType;

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

		fruitType = getRandomType();

		rawbitmap = getBitmapFromType(fruitType);

		splashRaw = getSplashFromType(fruitType);

		scaledBitmap = Bitmap.createScaledBitmap(rawbitmap, currentTomtatoSize,
				currentTomtatoSize, true);

		splash = new SplashTomato(parent,splashRaw);

		startFlash = false;

		splashDone = false;

		// isAlive = true;

	}

	private Bitmap getBitmapFromType(FruitType type) {
		if (type == FruitType.TOMATO) {
			return AssetManager.rawbitmap;
		}

		else if (type == FruitType.APPLE) {
			return AssetManager.apple;
		}

		else if (type == FruitType.EGG) {

			return AssetManager.egg;
		}

		return AssetManager.rawbitmap;
	}

	private Bitmap getSplashFromType(FruitType type) {

		if (type == FruitType.TOMATO) {
			return AssetManager.splashRaw;
		}

		else if (type == FruitType.APPLE) {
			return AssetManager.splash1;
		}

		else if (type == FruitType.EGG) {

			return AssetManager.splash2;
		}

		return AssetManager.splashRaw;

	}

	private FruitType getRandomType() {

		int rd = new Random().nextInt(FruitType.values().length);

		return FruitType.values()[rd];

	}

	public FruitType getFruitType() {
		return this.fruitType;
	}

	/**
	 * Draw tomato animation onto the screen
	 * 
	 * @param canvas
	 *            canvas from parent container
	 */
	public void draw(final Canvas canvas) {

		// if (!isAlive)
		// return;

		if (splashDone) {
			// removeFromListHolder();
			return;
		}

		if (x < 0)
			return;

		if (x < parent.getWidth() / 2 - currentTomtatoSize / 2) {

			if (!startFlash) {

				if (parent.haveFaceInCenter()) {

					startFlash = true;
					drawSplashTime = 500;
					splash.draw(canvas);
					return;

				}

				canvas.drawBitmap(scaledBitmap, x, y, bPaint);

				x -= velosityX;
				y -= velosityY;

				currentTomtatoSize -= 2;
				if (currentTomtatoSize > 0)
					scaledBitmap = Bitmap.createScaledBitmap(rawbitmap,
							currentTomtatoSize, currentTomtatoSize, true);
			}

			else {

				// isAlive = false;

				splash.draw(canvas);
				new SleepThread().start();

				if (drawSplashTime <= 0) {
					splashDone = true;
					// removeFromListHolder();
					// isAlive = false;

				}
			}

		}

		else {

			if (startFlash)
				return;

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
