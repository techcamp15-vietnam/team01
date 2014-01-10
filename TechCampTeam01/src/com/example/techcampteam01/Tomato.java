package com.example.techcampteam01;

import java.util.List;
import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.techcampteam01.SinglePlayActivity.GameState;

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

	int alphaCount;

	boolean splashPlusStart;

	boolean splashPlusDone;

	int drawSplashPlusTime;

	int delayTime;

	public enum FruitType {
		TOMATO, APPLE, EGG
	}

	private FruitType fruitType;
	private int splashPlusY;
	private int splashPlusX;

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

		SinglePlayActivity context = (SinglePlayActivity) parent.getContext();

		fruitType = context.getFruitType();

		rawbitmap = getBitmapFromType(fruitType);

		splashRaw = getSplashFromType(fruitType);

		scaledBitmap = Bitmap.createScaledBitmap(rawbitmap, currentTomtatoSize,
				currentTomtatoSize, true);

		splash = new SplashTomato(parent, splashRaw);

		startFlash = false;

		splashDone = false;

		alphaCount = 100;

		splashPlusStart = false;

		splashPlusDone = false;

		// isAlive = true;

		drawSplashTime = 3000;

		splashPlusX = (int) (parent.getWidth() * 0.75);
		splashPlusY = (int) (parent.getHeight() * 0.1);

		delayTime = 10;

	}

	/**
	 * Get Bitmap base on Tomato'size
	 * 
	 * @author ティエップ
	 * @param type
	 *            Tomato's type
	 * @return SplashBitmap
	 */

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

	/**
	 * Get Splash from Tomato'size
	 * 
	 * @author ティエップ
	 * @param type
	 *            Tomato's type
	 * @return Splash Bitmap
	 */
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

	/**
	 * get Random Tomato's type
	 * 
	 * @author ティエップ
	 * @return Random Type
	 */
	private FruitType getRandomType() {

		int rd = new Random().nextInt(FruitType.values().length);

		return FruitType.values()[rd];

	}

	/**
	 * Get current Tomato's type
	 * 
	 * @author ティエップ
	 * @return tomato's type
	 */

	public FruitType getFruitType() {
		return this.fruitType;
	}

	/**
	 * Draw tomato animation onto the screen
	 * 
	 * @author ティエップ
	 * 
	 * @param canvas
	 *            canvas from parent container
	 */
	public void draw(final Canvas canvas) {

		// if (!isAlive)
		// return;

		if (splashPlusStart && !splashPlusDone) {

			drawPlusPoint(fruitType, canvas);

			new SleepThreadPlus().start();

			if (drawSplashPlusTime <= 0) {
				// splashPlusDone = true;

			}

		}

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
					splashPlusStart = true;
					SinglePlayActivity context = (SinglePlayActivity) parent
							.getContext();
					context.plusScore(fruitType);
					drawSplashTime = 500;
					splash.draw(canvas);

					AssetManager.playSound(AssetManager.soundHit);
					return;

				}

				canvas.drawBitmap(scaledBitmap, x, y, bPaint);

				SinglePlayActivity context = (SinglePlayActivity) parent
						.getContext();
				if (context.getGameState() == GameState.PLAYING) {
					x -= velosityX;
					y -= velosityY;
					currentTomtatoSize -= 2;
					if (currentTomtatoSize > 0)
						scaledBitmap = Bitmap.createScaledBitmap(rawbitmap,
								currentTomtatoSize, currentTomtatoSize, true);

				}

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

			SinglePlayActivity context = (SinglePlayActivity) parent
					.getContext();

			if (context.getGameState() == GameState.PLAYING) {

				x -= velosityX;
				y -= velosityY;

				currentTomtatoSize -= 2;
				if (currentTomtatoSize > 0)
					scaledBitmap = Bitmap.createScaledBitmap(rawbitmap,
							currentTomtatoSize, currentTomtatoSize, true);
			}

		}

	}

	/**
	 * @author Duc
	 * @param fruitType
	 * @param canvas
	 */

	private void drawPlusPoint(FruitType fruitType, Canvas canvas) {
		// TODO Auto-generated method stub
		Paint paint = new Paint();
		if (alphaCount > 0) {
			paint.setAlpha(alphaCount);
			delayTime -= 1;
			if (delayTime <= 0) {
				alphaCount = alphaCount - 1;
				delayTime = 10;
			}

			switch (fruitType) {
			case EGG:
				canvas.drawBitmap(AssetManager.plusOne, splashPlusX,
						splashPlusY, paint);
				break;
			case APPLE:
				canvas.drawBitmap(AssetManager.plusThree, splashPlusX,
						splashPlusY, paint);
				break;
			case TOMATO:
				canvas.drawBitmap(AssetManager.plusFive, splashPlusX,
						splashPlusY, paint);
				break;
			default:
				break;
			}
			moveThePoint(fruitType);
		}
	}

	/**
	 * @author Duc
	 * @param fruitType
	 */

	protected void moveThePoint(FruitType fruitType) {
		switch (fruitType) {
		case EGG:
			splashPlusX = splashPlusX + 2;
			splashPlusY = splashPlusY + 2;
			break;
		case APPLE:
			splashPlusX = splashPlusX - 2;
			break;
		case TOMATO:
			splashPlusX = splashPlusX - 2;
			splashPlusY = splashPlusY + 2;
			break;
		default:
			break;
		}
		parent.invalidate();
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

	class SleepThreadPlus extends Thread {

		@Override
		public void run() {

			try {
				Thread.sleep(100);
				drawSplashPlusTime -= 100;

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

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
