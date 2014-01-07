package com.example.techcampteam01;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

public class Tomato {

	private static final int TOMATO_FLYING_TIME = 1000;
	private static final int DELAY_TIME = 50;
	private static final int MAX_PICTURE_SIZE = 180;

	private int currentTomtatoSize = MAX_PICTURE_SIZE;
	public float x = 0;
	public float y = 0;
	private float Vx;
	private float Vy;
	private int count = TOMATO_FLYING_TIME;

	View parent;

	public Tomato(View parent) {

		this.parent = parent;

		x = parent.getWidth() - MAX_PICTURE_SIZE;
		y = parent.getHeight() - MAX_PICTURE_SIZE;

		//
		Vx = -(float) (parent.getWidth() / 100f);
		Vy = -(float) (parent.getHeight() / 90f);

		x = 0;
		y = 0;

	}

	public void draw(Canvas canvas) {

		// y -= 10;
		Paint bPaint = new Paint();
		Bitmap rawbitmap = BitmapFactory.decodeResource(parent.getResources(),
				R.drawable.tomato);

		Bitmap bitmap = Bitmap.createScaledBitmap(rawbitmap,
				currentTomtatoSize, currentTomtatoSize, true);
		System.gc();
		canvas.drawBitmap(bitmap, x, y, bPaint);

		// if (y >= parent.getHeight() / 2 - currentTomtatoSize / 2)
		//
		// {
		// Paint tPaint = new Paint();
		// tPaint.setColor(Color.RED);
		// canvas.drawText("Time: " + count, 5, 20, tPaint);
		// Paint bPaint = new Paint();
		// Bitmap rawbitmap = BitmapFactory.decodeResource(
		// parent.getResources(), R.drawable.tomato);
		// currentTomtatoSize = currentTomtatoSize - 2;
		// Bitmap bitmap = Bitmap.createScaledBitmap(rawbitmap,
		// currentTomtatoSize, currentTomtatoSize, true);
		// System.gc();
		// canvas.drawBitmap(bitmap, x, y, bPaint);
		// moveTheTomato();
		// Paint lPaint = new Paint();
		// lPaint.setColor(Color.RED);
		// canvas.drawLine(parent.getWidth() / 2, 0,
		// parent.getWidth() / 2 + 2, parent.getHeight(), bPaint);
		// canvas.drawLine(0, parent.getHeight() / 2, parent.getWidth(),
		// parent.getHeight() / 2 + 2, lPaint);
		// } else
		//
		// {
		// Paint tPaint = new Paint();
		// Bitmap splashRaw = BitmapFactory.decodeResource(
		// parent.getResources(), R.drawable.splash);
		// Bitmap splash = Bitmap.createScaledBitmap(splashRaw,
		// currentTomtatoSize, currentTomtatoSize, true);
		// canvas.drawBitmap(splash, x, y, tPaint);
		// }

	}

	protected void moveTheTomato() {
		x = x + Vx;
		y = y + Vy;
	}

}
