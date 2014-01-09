package com.example.techcampteam01;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.ImageView;

public class SettingScreen extends Activity {

	/**
	 * @author ティエップ
	 */

	CheckBox musicCB;
	CheckBox soundCB;

	ImageView btnBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.setting);

		musicCB = (CheckBox) findViewById(R.id.checkbox_music);
		soundCB = (CheckBox) findViewById(R.id.checkbox_sound);

		musicCB.setChecked(Setting.musicOn);
		soundCB.setChecked(Setting.soundOn);

		musicCB.setText("Music");
		soundCB.setText("Sound");

		musicCB.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				CheckBox checkbox = (CheckBox) v;

				Setting.setMusic(checkbox.isChecked());
				saveMusicSetting(Setting.musicOn);

			}
		});

		soundCB.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				CheckBox checkbox = (CheckBox) v;

				Setting.setSound(checkbox.isChecked());
				saveSoundSetting(Setting.soundOn);

			}
		});

		btnBack = (ImageView) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				gotoMainMenu();

			}
		});

	}

	public void saveSoundSetting(boolean value) {

		SharedPreferences appPref = PreferenceManager
				.getDefaultSharedPreferences(this);

		SharedPreferences.Editor prefsEditor = appPref.edit();
		prefsEditor.putBoolean("sound", value);
		prefsEditor.commit();

	}

	public void saveMusicSetting(boolean value) {

		SharedPreferences appPref = PreferenceManager
				.getDefaultSharedPreferences(this);

		SharedPreferences.Editor prefsEditor = appPref.edit();
		prefsEditor.putBoolean("music", value);
		prefsEditor.commit();

	}

	/**
	 * Go to main menu
	 * 
	 * @author ティエップ
	 */

	private void gotoMainMenu() {

		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		finish();

	}

}
