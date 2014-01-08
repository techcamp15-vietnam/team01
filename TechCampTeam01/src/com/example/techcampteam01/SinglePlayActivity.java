package com.example.techcampteam01;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.RectF;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Face;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class SinglePlayActivity extends Activity {

	/**
	 * @author ミン・ドゥック
	 */
	private Preview mPreview;
	private Camera mCamera;
	private int numberOfCameras;
	private int cameraCurrentlyLocked;
	private List<Face> faces;
	private Matrix matrix;
	// The first rear facing camera
	int defaultCameraId;

	private ImageView tomatoFire;
	private ImageButton pauseBtn;

	private TextView scoreTV;
	private TextView timerTV;
	private Button startBT;

	private int countTimePlay;

	private static final int TIME_PLAY_IN_SECOND = 30000;

	private Thread timeCounterThread;

	private Handler handler;

	public enum GameState {
		PLAYING, START, PAUSE;
	}

	private GameState state;

	private int score;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		matrix = new Matrix();
		setContentView(R.layout.single_play);

		FrameLayout previewLayout = (FrameLayout) findViewById(R.id.preview);
		mPreview = new Preview(SinglePlayActivity.this);
		previewLayout.addView(mPreview);
		// Find the total number of cameras available
		numberOfCameras = Camera.getNumberOfCameras();

		// Find the ID of the default camera
		CameraInfo cameraInfo = new CameraInfo();
		for (int i = 0; i < numberOfCameras; i++) {
			Camera.getCameraInfo(i, cameraInfo);
			if (cameraInfo.facing == CameraInfo.CAMERA_FACING_BACK) {
				defaultCameraId = i;
			}
		}

		pauseBtn = (ImageButton) findViewById(R.id.pause_btn);
		tomatoFire = (ImageView) findViewById(R.id.tomato_fire);
		scoreTV = (TextView) findViewById(R.id.textview_score);
		timerTV = (TextView) findViewById(R.id.text_view_timer);
		startBT = (Button) findViewById(R.id.btn_start);

		tomatoFire.setEnabled(false);

		tomatoFire.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				fire();
			}
		});

		pauseBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				pause();
			}
		});

		startBT.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				startGame();

			}
		});

		countTimePlay = TIME_PLAY_IN_SECOND;

		timerTV.setText("Time : " + countTimePlay / 1000 + "");

		timeCounterThread = new TimeCounterThread();
		handler = new Handler();

		state = GameState.START;

		score = 0;
		setScore(score);

	}

	private void setScore(int score) {
		scoreTV.setText("Score : " + score);
	}

	/**
	 * start Game
	 * 
	 * @author ティエップ
	 */

	protected void startGame() {
		tomatoFire.setEnabled(true);
		startBT.setVisibility(View.GONE);
		startTime();
		state = GameState.PLAYING;

	}

	/**
	 * Timer Counter Class
	 * 
	 * @author ティエップ
	 * 
	 */
	class TimeCounterThread extends Thread {

		@Override
		public void run() {

			while (countTimePlay >= 0) {

				try {
					Thread.sleep(500);
					if (state == GameState.PLAYING) {
						countTimePlay -= 500;
						final int displayNumber = (int) (countTimePlay / 1000);

						handler.post(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								timerTV.setText("Time : " + displayNumber + "");

							}
						});

					}

				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

	}

	/**
	 * start Timer
	 * 
	 * @author ティエップ
	 */

	private void startTime() {

		timeCounterThread.start();

	}

	/**
	 * @author Duc
	 */

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * Fire tomato event. Calculate the coordinate, target hit or missed
	 */
	public void fire() {

		mPreview.assignTomatoToPreview();

		faces = mPreview.getFaces();
		RectF rect = new RectF();
		int centerX = mPreview.getWidth() / 2;
		int centerY = mPreview.getHeight() / 2;

		Preview.prepareMatrix(matrix, 90, mPreview.getWidth(),
				mPreview.getHeight());
		for (int i = 0; i < faces.size(); i++)

		{
			Face face = faces.get(i);
			rect.set(face.rect);
			matrix.mapRect(rect);
			int width = (int) rect.width();
			int height = (int) rect.height();
			int x = (int) rect.centerX() - width / 2;
			int y = (int) rect.centerY() - height / 2;

			Rectangle faceRect = new Rectangle(x, y, width, height);

			if (faceRect.checkPointInRectangle(new Point(centerX, centerY))) {
				// Toast.makeText(SinglePlayActivity.this, "Target Hit",
				// Toast.LENGTH_SHORT).show();

				score += 1;
				setScore(score);
			}

			else {
				Toast.makeText(SinglePlayActivity.this, "Target Miss",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	/**
	 * 
	 * Show pause dialog when pause button clicked
	 * 
	 * @author ミン・ドゥック
	 */

	public void pause() {

		state = GameState.PAUSE;
		// TODO Auto-generated method stub
		Toast.makeText(SinglePlayActivity.this, "Pause", Toast.LENGTH_SHORT)
				.show();
		final Dialog dialog = new Dialog(this);
		LayoutInflater inflater = LayoutInflater.from(SinglePlayActivity.this);
		View dialogView = inflater.inflate(R.layout.pause_dialog, null, false);
		dialog.setContentView(dialogView);

		Button resumeBtn = (Button) dialogView.findViewById(R.id.resume_button);
		Button retryBtn = (Button) dialogView.findViewById(R.id.retry_button);
		Button returnMainBtn = (Button) dialogView
				.findViewById(R.id.return_to_main);

		resumeBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (state == GameState.PAUSE)
					state = GameState.PLAYING;
				dialog.dismiss();
			}
		});

		retryBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SinglePlayActivity.this
						.getBaseContext(), SinglePlayActivity.class);
				startActivity(intent);
				finish();
			}
		});

		returnMainBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		dialog.setCanceledOnTouchOutside(false);
		dialog.show();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onPause() {
		// super.onPause();
		// Because the Camera object is a shared resource, it's very
		// important to release it when the activity is paused.

		if (mCamera != null) {
			mPreview.setCamera(null);
			mCamera.release();
			mCamera = null;
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		// Open the default i.e. the first rear facing camera.
		mCamera = Camera.open();
		cameraCurrentlyLocked = defaultCameraId;
		mPreview.setCamera(mCamera);

	}

}
