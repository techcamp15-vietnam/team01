package com.example.techcampteam01;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.Bundle;
import android.widget.ImageView;

/**
 * Display result screen of game.
 * 
 * @author ティエプ
 * 
 */

public class ResultScreen extends Activity {

	ImageView imgView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.result_layout);

		// Toast.makeText(this, Integer.toString(intent.getIntExtra("score",
		// 0)),
		// Toast.LENGTH_SHORT).show();

		byte[] data = getIntent().getByteArrayExtra("image");

		Bitmap bmp;
		bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
		Bitmap mutableBitmap = bmp.copy(Bitmap.Config.ARGB_8888, true);

		Matrix matrix = new Matrix();

		matrix.postRotate(90);

		Bitmap scaledBitmap = Bitmap.createScaledBitmap(mutableBitmap,
				mutableBitmap.getWidth(), mutableBitmap.getHeight(), true);

		Bitmap rotatedBitmap = Bitmap
				.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(),
						scaledBitmap.getHeight(), matrix, true);

		Bitmap abcd = mergeBitmap(rotatedBitmap, AssetManager.splashRaw);

		imgView = (ImageView) findViewById(R.id.img_view);
		imgView.setImageBitmap(abcd);

	}

	public Bitmap mergeBitmap(Bitmap bBitmap, Bitmap sBitmap) {
		Bitmap mergedBitmap = Bitmap.createBitmap(bBitmap.getWidth(),
				bBitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(mergedBitmap);
		canvas.drawBitmap(bBitmap, 0, 0, null);
		canvas.drawBitmap(sBitmap,
				mergedBitmap.getWidth() / 2 - sBitmap.getWidth() / 2,
				mergedBitmap.getHeight() / 2 - sBitmap.getHeight() / 2, null);
		return mergedBitmap;
	}

}
