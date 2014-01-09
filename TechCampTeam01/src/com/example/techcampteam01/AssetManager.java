package com.example.techcampteam01;

/***
 * @author　ティエップ
 * Manage resource
 */
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class AssetManager {

	public static Bitmap rawbitmap;

	public static Bitmap splashRaw;

	public static Bitmap apple;
	public static Bitmap egg;
	public static Bitmap splash1;
	public static Bitmap splash2;

	MainActivity context;

	public AssetManager(MainActivity context) {

		this.context = context;

	}

	/**
	 * Load Resource.
	 * 
	 * @author ティエップ
	 */

	public void load() {

		rawbitmap = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.tomato);

		splashRaw = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.splash);

		apple = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.apple);
		egg = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.egg);
		splash1 = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.splash1);
		splash2 = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.splash2);

	}

	/**
	 * Dispose resource when don't uses
	 * 
	 * @author ティエップ
	 */

	public void disposeResoure() {

	}

}
