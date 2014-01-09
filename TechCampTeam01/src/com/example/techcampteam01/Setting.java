package com.example.techcampteam01;

public class Setting {

	
	/**
	 * @author ティエップ
	 */

	public static boolean soundOn = true;
	public static boolean musicOn = true;

	/**
	 * Set Sound
	 * 
	 * @author ティエップ
	 * @param isOn
	 *            is Sound On or Off
	 */
	public static void setSound(boolean isOn) {

		soundOn = isOn;
	}

	/**
	 * Set Music
	 * 
	 * @author ティエップ
	 * @param isOn
	 *            is Music On or Off
	 */

	public static void setMusic(boolean isOn) {
		musicOn = isOn;

		if (musicOn == true) {
			AssetManager.playMusic(AssetManager.mainMenuSound);
		}

		else {
			AssetManager.pauseSound(AssetManager.mainMenuSound);
		}
	}

}
