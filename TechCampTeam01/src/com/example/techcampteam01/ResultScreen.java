package com.example.techcampteam01;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class ResultScreen extends Activity {

	ImageView imgView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.result_layout);
		// get intent image
		byte[] data = getIntent().getByteArrayExtra("image");
		Bitmap image = BitmapFactory.decodeByteArray(data, 0, data.length);
		Bitmap rotateImage = rotateImage(image);
		makeResultImage(rotateImage);

		Button btnShare = (Button) findViewById(R.id.btn_share);
		btnShare.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				shareImage();
			}
		});

	}

	/**
	 * RotateImage
	 * 
	 * @param Raw
	 *            Image
	 * 
	 * @author 1-C トゥン
	 */
	public Bitmap rotateImage(Bitmap inputImage) {
		Bitmap mutableBitmap = inputImage.copy(Bitmap.Config.ARGB_8888, true);

		Matrix matrix = new Matrix();
		matrix.postRotate(90);

		Bitmap scaledBitmap = Bitmap.createScaledBitmap(mutableBitmap,
				mutableBitmap.getWidth(), mutableBitmap.getHeight(), true);

		Bitmap rotatedBitmap = Bitmap
				.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(),
						scaledBitmap.getHeight(), matrix, true);
		return rotatedBitmap;
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

		Bitmap resultImage = mergeBitmap(rotatedBitmap, AssetManager.splashRaw);

		imgView = (ImageView) findViewById(R.id.img_view);
		imgView.setImageBitmap(resultImage);

		//save image to storage 
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
		canvas.drawBitmap(bBitmap, 0, 0, null);
		canvas.drawBitmap(sBitmap,
				mergedBitmap.getWidth() / 2 - sBitmap.getWidth() / 2,
				mergedBitmap.getHeight() / 2 - sBitmap.getHeight() / 2, null);
		return mergedBitmap;
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

}
