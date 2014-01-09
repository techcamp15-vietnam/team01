package com.example.techcampteam01;

/***
 * @author　ティエップ
 * Manage resource
 */
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;

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

	public synchronized void playSound(boolean isMute, int idSound,
			MediaPlayer sound, Context context) {
		if (isMute == false) {
			if (sound != null) {
				sound.release();
				sound = null;
			}
			sound = MediaPlayer.create(context, idSound);
			if (sound != null) {
				sound.seekTo(0);
				sound.start();
			}
			Runtime.getRuntime().gc();
		}
	}

	public void releaseSound(boolean isMute, MediaPlayer sound) {
		if (isMute == false && sound != null) {
			sound.release();
			sound = null;
		}
	}

	public void pauseSound(boolean isMute, MediaPlayer sound) {
		if (isMute == false && sound != null) {
			sound.pause();
		}
	}

	public void startSound(boolean isMute, MediaPlayer sound) {
		if (isMute == false && sound != null) {
			sound.start();
		}
	}

	public void playLoopSound(int idSound, boolean isMute, MediaPlayer sound,
			Context context) {
		if (isMute == false) {
			if (sound != null) {
				sound.release();
				sound = null;
			}
			sound = MediaPlayer.create(context, idSound);
			if (sound != null) {
				sound.seekTo(0);
				sound.setLooping(true);
				sound.start();
			}
			Runtime.getRuntime().gc();
		}
	}
}
