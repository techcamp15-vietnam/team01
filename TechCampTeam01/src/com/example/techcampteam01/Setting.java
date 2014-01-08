package com.example.techcampteam01;

public class Setting {

	/**
	 * @author ティエップ
	 */

	public static boolean soundOn = true;
	public static boolean musicOn = true;

	/**
	 * @author ティエップ Set Sound
	 * @param isOn
	 *            is Sound On or Off
	 */
	public static void setSound(boolean isOn) {

		soundOn = isOn;
	}

	/**
	 * @author ティエップ Set Music
	 * @param isOn
	 *            is Music On or Off
	 */

	public static void setMusic(boolean isOn) {
		musicOn = isOn;
	}

}
