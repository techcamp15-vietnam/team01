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

	MainActivity context;

	public AssetManager(MainActivity context) {

		this.context = context;

	}

	/**
	 * @author ティエップ Load Resource.
	 */

	public void load() {

		rawbitmap = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.tomato);

		splashRaw = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.splash);

	}

	/**
	 * Dispose resource when don't uses
	 * 
	 * @author ティエップ
	 */

	public void disposeResoure() {

	}

}
