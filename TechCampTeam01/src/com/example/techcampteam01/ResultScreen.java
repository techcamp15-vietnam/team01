package com.example.techcampteam01;

import java.io.File;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class ResultScreen extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.result_layout);
		super.onCreate(savedInstanceState);

		Button btnShare = (Button) findViewById(R.id.btn_share);
		btnShare.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				shareImage("abc");
			}
		});
	}

	/**
	 * Post Result Image to FB
	 * 
	 * @param imageName
	 * 
	 * @author 1-C トゥン
	 */

	private void shareImage(String imgName) {

		Intent share = new Intent(Intent.ACTION_SEND);

		share.setType("image/png");

		String imagePath = Environment.getExternalStorageDirectory() + "/"
				+ imgName + ".png";
		Toast.makeText(getBaseContext(), imagePath, Toast.LENGTH_SHORT).show();

		File imageFileToShare = new File(imagePath);

		Uri uri = Uri.fromFile(imageFileToShare);
		share.putExtra(Intent.EXTRA_STREAM, uri);

		startActivity(Intent.createChooser(share, "Share Image!"));
	}

}
