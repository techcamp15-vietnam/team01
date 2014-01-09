package com.example.techcampteam01;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Process;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity {
	ImageView btnStart, btnOption, btnExit;
	private AssetManager assetMN;
	private static final int SINGLE = 1;
	private static final int MULTI = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		btnStart = (ImageView) findViewById(R.id.btnStart);
		btnOption = (ImageView) findViewById(R.id.btnOption);
		btnExit = (ImageView) findViewById(R.id.btnExit);

		btnStart.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				callSinglePlay();
			}
		});

		btnOption.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				gotoSetting();

			}
		});

		btnExit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				exit();

			}
		});

		assetMN = new AssetManager(this);
		assetMN.load();
		playMainMenuSound();
	}

	private void playMainMenuSound() {
		// TODO Auto-generated method stub
		AssetManager.playSound(AssetManager.mainMenuSound);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (Setting.musicOn) {
			playMainMenuSound();
		}
		// showToat(Setting.musicOn);
	}

	/**
	 * Go to Setting Screen
	 * 
	 * @author ティエップ
	 */

	protected void gotoSetting() {

		Intent intent = new Intent(this, SettingScreen.class);
		startActivity(intent);

	}

	protected void callShare() {
		Intent intent = new Intent(this, ResultScreen.class);
		startActivity(intent);
	}

	@Override
	protected void onDestroy() {

		assetMN.disposeResoure();
		super.onDestroy();
	}

	/**
	 * call single player mode activity
	 * 
	 * @param null
	 * @author 1-A トゥン
	 */
	private void callSinglePlay() {
		Intent intent = new Intent(this, SinglePlayActivity.class);
		// intent.putExtra("ABC", "data");
		AssetManager.pauseSound(AssetManager.mainMenuSound);
		startActivity(intent);
	}

	/**
	 * Exit
	 * 
	 * @param null
	 * @author 1-A トゥン
	 */
	private void exit() {
		Process.killProcess(Process.myPid());
	}

	/**
	 * Kill application when touching back button on the screen
	 * 
	 * @author ミン・ドゥック
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		AssetManager.pauseSound(AssetManager.mainMenuSound);
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void showToat(boolean value) {
		Toast.makeText(this, value + "", Toast.LENGTH_SHORT).show();
	}

}
