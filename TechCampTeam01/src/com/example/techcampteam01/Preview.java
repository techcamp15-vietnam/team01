package com.example.techcampteam01;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Face;
import android.hardware.Camera.FaceDetectionListener;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.hardware.Camera.Size;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

@SuppressLint("NewApi")
public class Preview extends SurfaceView implements SurfaceHolder.Callback,
		FaceDetectionListener {
	private Camera mCamera;
	private SurfaceHolder mHolder;
	// Size mPreviewSize;
	List<Size> mSupportedPreviewSizes;
	private final String TAG = "PreviewCamera";

	List<Face> faces;
	Paint painter;
	Matrix matrix;
	RectF rect;

	Handler handler;

	Rectangle targetRect;

	public Preview(Context context) {
		super(context);
		mHolder = getHolder();
		mHolder.addCallback(this);
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

		faces = new ArrayList<Face>();
		matrix = new Matrix();
		rect = new RectF();
		setWillNotDraw(false);

		painter = new Paint(Paint.ANTI_ALIAS_FLAG);
		painter.setStyle(Paint.Style.STROKE);
		painter.setColor(Color.RED);
		painter.setStrokeWidth(3);

		handler = new Handler();
	}

	public void setFaces(List<Face> faces) {

		this.faces = faces;
		invalidate();

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		final int width = resolveSize(getSuggestedMinimumWidth(),
				widthMeasureSpec);
		final int height = resolveSize(getSuggestedMinimumHeight(),
				heightMeasureSpec);
		setMeasuredDimension(width, height);

		if (mSupportedPreviewSizes != null) {
			// mPreviewSize = getOptimalPreviewSize(mSupportedPreviewSizes,
			// width,
			// height);
		}

	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		Camera.Parameters parameters = mCamera.getParameters();
		// parameters.setPreviewSize(mPreviewSize.width, mPreviewSize.height);
		requestLayout();

		mCamera.setParameters(parameters);
		mCamera.startPreview();

		startDetection();
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		try {
			if (mCamera != null) {
				mCamera.setPreviewDisplay(holder);
				mCamera.setFaceDetectionListener(this);
			}
		} catch (IOException exception) {
			Log.e(TAG, "IOException caused by setPreviewDisplay()", exception);
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		if (mCamera != null) {
			mCamera.stopPreview();
			mCamera.stopFaceDetection();
		}
	}

	public void setCamera(Camera camera) {
		mCamera = camera;
		if (mCamera != null) {
			mCamera.setDisplayOrientation(90);
			mSupportedPreviewSizes = mCamera.getParameters()
					.getSupportedPreviewSizes();
			requestLayout();

		}

	}

	PictureCallback pictureCallback = new PictureCallback() {

		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			camera.startPreview();
		}
	};

	PictureCallback rawCallback = new PictureCallback() {

		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			Log.v(TAG, "onPictureTaken");
		}
	};

	ShutterCallback shutterCallback = new ShutterCallback() {

		@Override
		public void onShutter() {
			Log.i(TAG, "onShutter");
		}
	};

	public void takePicture() {
		if (mCamera == null) {
			return;
		}

		handler.post(new Runnable() {

			@Override
			public void run() {

				// mCamera.takePicture(shutterCallback, rawCallback,
				// pictureCallback);

			}
		});

	}

	private String createFileName() {

		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			String sFileName = Environment.getExternalStorageDirectory()
					+ File.separator + System.currentTimeMillis() + ".jpg";
			return sFileName;
		}
		return "";
	}

	@SuppressLint("NewApi")
	public void startDetection() {

		// Try starting Face Detection
		Camera.Parameters params = mCamera.getParameters();

		// start face detection only *after* preview has started
		if (params.getMaxNumDetectedFaces() > 0) {
			// camera supports face detection, so can start it:
			mCamera.startFaceDetection();
		}

	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {

		drawTarget(canvas);
		prepareMatrix(matrix, 90, getWidth(), getHeight());

		for (int i = 0; i < faces.size(); i++)

		{

			Face face = faces.get(i);

			rect.set(face.rect);
			matrix.mapRect(rect);
			canvas.drawRect(rect, painter);
			Activity activity = (Activity) getContext();
			TextView textView = (TextView) activity
					.findViewById(R.id.text_view);

			int width = (int) rect.width();
			int height = (int) rect.height();
			int x = (int) rect.centerX() - width / 2;
			int y = (int) rect.centerY() - height / 2;

			textView.setText(x + "  " + y + " Width: " + width + "  Height : "
					+ height + " Target : " + targetRect.getX() + ", "
					+ targetRect.getY());

			Rectangle faceRect = new Rectangle(x, y, width, height);

			// Toast.makeText(getContext(), "Detected",
			// Toast.LENGTH_SHORT).show();

			if (faceRect.checkPointInRectangle(new Point(getWidth() / 2,
					getHeight() / 2))) {

				painter.setColor(Color.GREEN);
			}

			else {

				painter.setColor(Color.RED);

			}
		}

	}

	private void drawTarget(Canvas canvas) {

		int centerX = getWidth() / 2;
		int centerY = getHeight() / 2;

		int width = 10;
		int height = 10;

		targetRect = new Rectangle(centerX - width / 2, centerY - height / 2,
				width, height);

		canvas.drawRect(new RectF(targetRect.getX(), targetRect.getY(),
				targetRect.getX() + width, targetRect.getY() + height), painter);

	}

	public void prepareMatrix(Matrix matrix, int displayOrientation,
			int viewWidth, int viewHeight) {

		CameraInfo info = new CameraInfo();
		Camera.getCameraInfo(0, info);
		// Need mirror for front camera.
		boolean mirror = (info.facing == CameraInfo.CAMERA_FACING_FRONT);
		matrix.setScale(mirror ? -1 : 1, 1);
		// This is the value for android.hardware.Camera.setDisplayOrientation.
		matrix.postRotate(displayOrientation);
		// Camera driver coordinates range from (-1000, -1000) to (1000, 1000).
		// UI coordinates range from (0, 0) to (width, height).
		matrix.postScale(viewWidth / 2000f, viewHeight / 2000f);
		matrix.postTranslate(viewWidth / 2f, viewHeight / 2f);

	}

	@Override
	public void onFaceDetection(android.hardware.Camera.Face[] arg0, Camera arg1) {

		setFaces(Arrays.asList(arg0));

	}

}

class Rectangle {

	private int x, y;
	private int width, height;

	public Rectangle(int x, int y, int width, int height) {

		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;

	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getWith() {
		return width;
	}

	public void setWith(int with) {
		this.width = with;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public boolean checkOverlap(Rectangle rect2) {
		Point P1 = new Point(x, y);
		Point P2 = new Point(x + width, y + height);
		Point P3 = new Point(x + width, y);
		Point P4 = new Point(x, y + height);

		if (checkPointInRectangle(P1)) {

			return true;
		}

		return false;

	}

	public boolean checkPointInRectangle(Point p) {

		if (p.x > x && p.x < x + width && p.y > y && p.y < y + height)
			return true;

		return false;
	}
}