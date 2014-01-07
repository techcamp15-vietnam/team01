package com.example.techcampteam01;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.RectF;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Face;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
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

	ImageView tomatoFire;

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

		tomatoFire = (ImageView) findViewById(R.id.tomato_fire);
		tomatoFire.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				fire();
			}
		});
		initButtons();
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
				Toast.makeText(SinglePlayActivity.this, "Target Hit",
						Toast.LENGTH_SHORT).show();
			}

			else {
				Toast.makeText(SinglePlayActivity.this, "Target Miss",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	private void initButtons() {

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
