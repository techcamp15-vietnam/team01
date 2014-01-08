package com.example.techcampteam01;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.Toast;

public class SettingScreen extends Activity {
	
	/**
	 * @author ティエップ
	 */

	CheckBox musicCB;
	CheckBox soundCB;

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

			}
		});

		soundCB.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				CheckBox checkbox = (CheckBox) v;

				Setting.setSound(checkbox.isChecked());

			}
		});

	}

}
