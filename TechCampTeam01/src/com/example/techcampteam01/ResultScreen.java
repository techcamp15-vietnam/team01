package com.example.techcampteam01;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class ResultScreen extends Activity {

	ImageView imgView;
	ImageView retry;
	// ImageView mainMenu;
	ImageView btnShare;
	ImageView btnClose;

	TextView scoreTV;
	TextView highScore;
	private Bitmap resultImage;

	// ImageView retryBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.result_layout);
		// get intent image
		byte[] data = getIntent().getByteArrayExtra("image");
		Bitmap image = BitmapFactory.decodeByteArray(data, 0, data.length);
		resultImage = rotateImage(image, 90);

		int score = getIntent().getIntExtra("score", 0);
		if (score > getHighScore()) {
			AssetManager.playSound(AssetManager.highscoreClapHand);
			saveHighScore(score);
			resultImage = creatHighScoreImage();
		} else {
			resultImage = createMedal();
		}
		scoreTV = (TextView) findViewById(R.id.tv_score);
		scoreTV.setText(scoreTV.getText().toString() + " : " + score);
		highScore = (TextView) findViewById(R.id.high_score);
		highScore.setText(highScore.getText().toString() + " : "
				+ getHighScore());
		makeResultImage(resultImage);

		btnShare = (ImageView) findViewById(R.id.btn_share);
		btnShare.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				shareImage();
			}
		});

		retry = (ImageView) findViewById(R.id.btn_retry);
		retry.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				retry();

			}
		});

		btnClose = (ImageView) findViewById(R.id.btn_mainmenu);
		btnClose.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				gotoMenu();

			}
		});
	}

	private Bitmap createMedal() {
		Bitmap sBitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.medal);
		sBitmap = Bitmap.createScaledBitmap(sBitmap, resultImage.getWidth(),
				resultImage.getHeight(), true);
		return mergeBitmap(resultImage, sBitmap);

	}

	private Bitmap creatHighScoreImage() {
		Bitmap highscoreBitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.highscore);
		highscoreBitmap = Bitmap.createScaledBitmap(highscoreBitmap,
				resultImage.getWidth(), resultImage.getHeight(), true);

		return mergeBitmap(resultImage, highscoreBitmap);
	}

	public Bitmap rotateImage(Bitmap inputImage, int angle) {
		Bitmap mutableBitmap = inputImage.copy(Bitmap.Config.ARGB_8888, true);

		Matrix matrix = new Matrix();
		matrix.postRotate(angle);

		Bitmap scaledBitmap = Bitmap.createScaledBitmap(mutableBitmap,
				mutableBitmap.getWidth(), mutableBitmap.getHeight(), true);

		Bitmap rotatedBitmap = Bitmap
				.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(),
						scaledBitmap.getHeight(), matrix, true);
		return rotatedBitmap;
	}

	/**
	 * RotateImage
	 * 
	 * @param Raw
	 *            Image
	 * 
	 * @author 1-C トゥン
	 */
	public Bitmap mergeBitmap(Bitmap bBitmap, Bitmap sBitmap) {
		Bitmap mergedBitmap = Bitmap.createBitmap(bBitmap.getWidth(),
				bBitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(mergedBitmap);
		canvas.drawBitmap(bBitmap, 0, 0, null);
		canvas.drawBitmap(sBitmap, 0, 0, null);
		return mergedBitmap;
	}

	/**
	 * Make Result Image
	 * 
	 * @param rotated
	 *            Image
	 * 
	 * @author 1-C トゥン
	 */

	private void makeResultImage(Bitmap rotatedBitmap) {

		// Bitmap resultImage = mergeBitmap(rotatedBitmap,
		// AssetManager.splashRaw);

		// resultImage = rotatedBitmap;

		imgView = (ImageView) findViewById(R.id.img_view);
		imgView.setImageBitmap(resultImage);

		// save image to storage
		File path = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
		File imageFile = new File(path, "result.png");

		try {
			FileOutputStream fileOutPutStream;
			fileOutPutStream = new FileOutputStream(imageFile);
			resultImage.compress(Bitmap.CompressFormat.PNG, 80,
					fileOutPutStream);
			try {
				fileOutPutStream.flush();
				fileOutPutStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Post Result Image to FB
	 * 
	 * @param imageName
	 * 
	 * @author 1-C トゥン
	 */

	private void shareImage() {

		Intent share = new Intent(Intent.ACTION_SEND);

		share.setType("image/png");

		String imagePath = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
				+ "/result.png";

		File imageFileToShare = new File(imagePath);

		Uri uri = Uri.fromFile(imageFileToShare);
		share.putExtra(Intent.EXTRA_STREAM, uri);

		startActivity(Intent.createChooser(share, "Share Image!"));
	}

	/**
	 * 　Save HighScore
	 * 
	 * @author ティエップ
	 * @param highScore
	 */

	public void saveHighScore(int highScore) {

		SharedPreferences appPref = PreferenceManager
				.getDefaultSharedPreferences(this);

		SharedPreferences.Editor prefsEditor = appPref.edit();
		prefsEditor.putInt("highscore", highScore);
		prefsEditor.commit();

	}

	/**
	 * Get Saved HighScore
	 * 
	 * @author ティエップ
	 * @param highScore
	 * @return
	 */

	public int getHighScore() {
		//

		SharedPreferences appPref = PreferenceManager
				.getDefaultSharedPreferences(this);

		int saveHighScore = 0;
		saveHighScore = appPref.getInt("highscore", 0);
		return saveHighScore;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			gotoMenu();
			return true;

		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * Retry
	 * 
	 * @author ティエップ
	 * 
	 */

	private void retry() {

		Intent intent = new Intent(this, SinglePlayActivity.class);
		startActivity(intent);

	}

	/**
	 * Goto Main Menu
	 * 
	 * @author ティエップ
	 * 
	 */

	private void gotoMenu() {

		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);

	}

}
