package com.example.techcampteam01;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

public class TomatoAnimation extends View {
	private static final int TOMATO_FLYING_TIME = 1000;
	private static final int DELAY_TIME = 50;
	private static final int MAX_PICTURE_SIZE = 180;
	private int currentTomtatoSize = MAX_PICTURE_SIZE;
	public float x = 0;
	public float y = 0;
	private float Vx;
	private float Vy;
	private int count = TOMATO_FLYING_TIME;
	TimeCountThread timeCount = new TimeCountThread();

	public TomatoAnimation(Context context) {
		super(context);
		timeCount.start();
	}

	public TomatoAnimation(Context context, AttributeSet attr) {
		super(context, attr);
		timeCount.start();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (count == TOMATO_FLYING_TIME - DELAY_TIME) {
			float velosRatio = (float) (getHeight() / 2) / (getWidth() / 2);
			x = getWidth() - MAX_PICTURE_SIZE;
			y = getHeight() - MAX_PICTURE_SIZE;
			Toast.makeText(getContext(), "Ratio:" + velosRatio,
					Toast.LENGTH_SHORT).show();
			Vx = -(float) (getWidth() / 100f);
			Vy = -(float) (getHeight() / 90f);
			Toast.makeText(getContext(), "Vy:" + Vy, Toast.LENGTH_SHORT).show();
		}
		if (y >= getHeight() / 2 - currentTomtatoSize / 2) {
			Paint tPaint = new Paint();
			tPaint.setColor(Color.RED);
			canvas.drawText("Time: " + count, 5, 20, tPaint);
			Paint bPaint = new Paint();
			Bitmap rawbitmap = BitmapFactory.decodeResource(getResources(),
					R.drawable.tomato);
			currentTomtatoSize = currentTomtatoSize - 2;
			Bitmap bitmap = Bitmap.createScaledBitmap(rawbitmap,
					currentTomtatoSize, currentTomtatoSize, true);
			System.gc();
			canvas.drawBitmap(bitmap, x, y, bPaint);
			moveTheTomato();
			Paint lPaint = new Paint();
			lPaint.setColor(Color.RED);
			canvas.drawLine(getWidth() / 2, 0, getWidth() / 2 + 2, getHeight(),
					bPaint);
			canvas.drawLine(0, getHeight() / 2, getWidth(),
					getHeight() / 2 + 2, lPaint);
		} else {
			Paint tPaint = new Paint();
			Bitmap splashRaw = BitmapFactory.decodeResource(getResources(),
					R.drawable.splash);
			Bitmap splash = Bitmap.createScaledBitmap(splashRaw,
					currentTomtatoSize, currentTomtatoSize, true);
			canvas.drawBitmap(splash, x, y, tPaint);
		}
	}

	protected void moveTheTomato() {
		x = x + Vx;
		y = y + Vy;
		invalidate();
	}

	private class TimeCountThread extends Thread {
		public void run() {
			while (count > 0) {
				count = count - DELAY_TIME;
				try {
					sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
