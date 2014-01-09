package com.example.techcampteam01;

import java.io.ByteArrayOutputStream;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Face;
import android.hardware.Camera.PictureCallback;
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

import com.example.techcampteam01.Tomato.FruitType;

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
	private TextView highscoreTV;
	private Button startBT;

	private ImageView timeUpImg;

	private int countTimePlay;

	private static final int TIME_PLAY_IN_SECOND = 30000;

	private Thread timeCounterThread;

	private Handler handler;

	public enum GameState {
		PLAYING, START, PAUSE, STOP;
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
		highscoreTV = (TextView) findViewById(R.id.text_view_highscore);
		startBT = (Button) findViewById(R.id.btn_start);
		timeUpImg = (ImageView) findViewById(R.id.timeup_img);

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

		setGameState(GameState.START);

		score = 0;
		setScore(score);

	}

	/**
	 * Display score in Screen
	 * 
	 * @param score
	 *            User's score
	 */

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
		setGameState(GameState.PLAYING);

	}

	/**
	 * @author ティエップ
	 * @param state
	 *            game's state
	 */

	private void setGameState(GameState state) {
		this.state = state;
		if (this.state == GameState.STOP)

		{
			handler.post(new Runnable() {

				@Override
				public void run() {

					timeUpImg.setVisibility(View.VISIBLE);
					handler.post(new Runnable() {

						@Override
						public void run() {

						}
					});
					tomatoFire.setEnabled(false);
					takePicture();

				}
			});

		}
	}

	/**
	 * Call take picture' function of Camera.
	 * 
	 * @author ティエップ
	 */

	private void takePicture() {

		mPreview.getCamera().takePicture(null, null, new PictureCallback() {

			@Override
			public void onPictureTaken(byte[] data, Camera camera) {

				// Toast.makeText(getBaseContext(), "Chup hinh",
				// Toast.LENGTH_SHORT).show();
				gotoResultScreen(data);
			}
		});

	}

	/**
	 * @author ティエップ
	 * @param imageByte
	 *            Byte array taken pictrue from Camera
	 */

	private void gotoResultScreen(byte[] imageByte) {
		Intent intent = new Intent(SinglePlayActivity.this, ResultScreen.class);
		Bitmap image = BitmapFactory.decodeByteArray(imageByte, 0,
				imageByte.length);
		image = mergeBitmap(image, AssetManager.splashRaw);
		image = Bitmap.createScaledBitmap(image, 250, 250, true);

		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.PNG, 100, stream);
		byte[] byteArray = stream.toByteArray();

		intent.putExtra("score", score);
		intent.putExtra("image", byteArray);
		startActivity(intent);
		finish();
	}

	/**
	 * @author ドゥック Merge two Bitmaps
	 * 
	 * @param bBitmap
	 *            Bitmap 1
	 * @param sBitmap
	 *            Bitmap 2
	 * @return
	 */

	public Bitmap mergeBitmap(Bitmap bBitmap, Bitmap sBitmap) {
		Bitmap mergedBitmap = Bitmap.createBitmap(bBitmap.getWidth(),
				bBitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(mergedBitmap);
		sBitmap = Bitmap.createScaledBitmap(sBitmap, bBitmap.getWidth() / 4,
				bBitmap.getHeight() / 4, true);
		canvas.drawBitmap(bBitmap, 0, 0, null);
		canvas.drawBitmap(sBitmap,
				mergedBitmap.getWidth() / 2 - sBitmap.getWidth() / 2,
				mergedBitmap.getHeight() / 2 - sBitmap.getHeight() / 2, null);
		return mergedBitmap;
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
								if (displayNumber <= 5) {
									timerTV.setTextColor(Color.RED);
								}
								timerTV.setText("Time : " + displayNumber + "");
							}
						});

					}

				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			setGameState(GameState.STOP);

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

			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
			finish();

			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * Fire tomato event. Calculate the coordinate, target hit or missed
	 */
	public void fire() {

		mPreview.assignTomatoToPreview();

	}

	public void plusScore(FruitType type) {

		if (type == FruitType.EGG) {
			score += 1;
		}

		else if (type == FruitType.APPLE) {
			score += 3;
		}

		else {
			score += 5;
		}

		setScore(score);
	}

	/**
	 * 
	 * Show pause dialog when pause button clicked
	 * 
	 * @author ミン・ドゥック
	 */

	public void pause() {

		setGameState(GameState.PAUSE);

		showDialog(0);

	}

	@Override
	@Deprecated
	protected Dialog onCreateDialog(int id) {

		if (id == 0) {

			setGameState(GameState.PAUSE);
			Toast.makeText(SinglePlayActivity.this, "Pause", Toast.LENGTH_SHORT)
					.show();
			final Dialog dialog = new Dialog(this);
			LayoutInflater inflater = LayoutInflater
					.from(SinglePlayActivity.this);
			View dialogView = inflater.inflate(R.layout.pause_dialog, null,
					false);
			dialog.setContentView(dialogView);

			Button resumeBtn = (Button) dialogView
					.findViewById(R.id.resume_button);
			Button retryBtn = (Button) dialogView
					.findViewById(R.id.retry_button);
			Button returnMainBtn = (Button) dialogView
					.findViewById(R.id.return_to_main);

			resumeBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					if (state == GameState.PAUSE) {

						setGameState(GameState.PLAYING);

					}
					dialog.dismiss();
				}
			});

			retryBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					dialog.dismiss();
					Intent intent = new Intent(SinglePlayActivity.this,
							SinglePlayActivity.class);
					startActivity(intent);
					finish();
				}
			});

			returnMainBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					Intent intent = new Intent(getBaseContext(),
							MainActivity.class);
					startActivity(intent);
					finish();
				}
			});

			dialog.setCanceledOnTouchOutside(false);
			dialog.show();

			return dialog;

		}

		else
			return super.onCreateDialog(id);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onPause() {
		super.onPause();
		// Because the Camera object is a shared resource, it's very
		// important to release it when the activity is paused.

		if (state == GameState.PLAYING)
			pause();

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
