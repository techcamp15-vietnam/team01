package com.example.techcampteam01;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity {
	ImageView btnStart, btnOption, btnExit;
	private static final int SINGLE = 1;
	private static final int MULTI = 2;

	AssetManager assetMN;

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
					finish();
				}
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

	/**
	 * call single player mode activity
	 * 
	 * @param null
	 * @author 1-A トゥン
	 */
	private void callSinglePlay() {
		Intent intent = new Intent(getBaseContext(), SinglePlayActivity.class);
		intent.putExtra("ABC", "data");
		startActivity(intent);
	}

	/**
	 * Exit
	 * 
	 * @param null
	 * @author 1-A トゥン
	 */
	private void exit() {
		System.exit(0);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	/**
	 * Dispose Resoure when not using.
	 * @author ティエップ
	 */

	@Override
	protected void onDestroy() {

		assetMN.disposeResoure();
		super.onDestroy();
	}

}
