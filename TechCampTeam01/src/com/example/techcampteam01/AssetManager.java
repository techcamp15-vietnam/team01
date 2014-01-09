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
	public static MediaPlayer mainMenuSound;
	MainActivity context;

	public static Bitmap plusOne, plusThree, plusFive;

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
		mainMenuSound = MediaPlayer.create(context, R.raw.main_menu_sound);

		plusOne = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.plus1);
		plusThree = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.plus3);
		plusFive = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.plus5);
	}

	/**
	 * Dispose resource when don't uses
	 * 
	 * @author ティエップ
	 */

	public void disposeResoure() {

	}

	public static synchronized void playMusic(MediaPlayer music) {

		if (Setting.musicOn) {

			if (music != null) {
				music.seekTo(0);
				music.setLooping(true);
				music.start();
			}
			Runtime.getRuntime().gc();
		}

	}

	public static synchronized void playSound(MediaPlayer sound) {

		if (Setting.soundOn) {

			if (sound != null) {
				sound.seekTo(0);
				sound.start();
			}
			Runtime.getRuntime().gc();
		}
	}

	public void releaseSound(MediaPlayer sound) {
		if (sound != null) {
			sound.release();
			sound = null;
		}
	}

	public static void pauseSound(MediaPlayer sound) {
		if (sound != null) {
			sound.pause();
		}
	}

	public void startSound(boolean isOn, MediaPlayer sound) {
		if (isOn == true && sound != null) {
			sound.start();
		}
	}

	public void playLoopSound(int idSound, boolean isOn, MediaPlayer sound,
			Context context) {
		if (isOn == true) {
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
