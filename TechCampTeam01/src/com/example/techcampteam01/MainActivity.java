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

import com.facebook.Session;
import com.facebook.SessionState;

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
		final EditText name = (EditText) findViewById(R.id.name);

		btnStart.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (name.getText().toString().equals("")) {
					Toast.makeText(getBaseContext(), "Please Enter Your Name",
							Toast.LENGTH_SHORT).show();
				} else {
					callSinglePlay();
				}
			}
		});
		btnOption.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (name.getText().toString().equals("")) {
					Toast.makeText(getBaseContext(), "Please Enter Your Name",
							Toast.LENGTH_SHORT).show();
				} else {
					callShare();
				}
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

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		showToat(Setting.musicOn);
	}
	
	/**Go to Setting Screen
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
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			Process.killProcess(Process.myPid());
		}
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
