package com.example.techcampteam01;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
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

	private List<Face> faces;
	private Paint painter;
	private Matrix matrix;
	private RectF rect;

	Handler handler; // Synchronize Ui

	Rectangle targetRect;

	Tomato flyingTomato;

	private List<Tomato> listTomatoOnScreen; // Hold tomatos on screen.

	/**
	 * Camera Preview Constructor
	 * 
	 * @author ティエプ
	 * @param context
	 *            the parent context
	 */
	public Preview(SinglePlayActivity context) {
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
		listTomatoOnScreen = new ArrayList<Tomato>();

	}

	/**
	 * set faces listed from the camera to Preview's Class attribute
	 * 
	 * @param faces
	 *            The list of detected faces returned from camera
	 */
	public void setFaces(List<Face> faces) {

		this.faces = faces;
		invalidate();

	}

	public List<Face> getFaces() {
		return this.faces;
	}

	/**
	 * Put image from the camera to device's screen for previewing. Called
	 * immediately after any structural changes (format or size) have been made
	 * to the surface. Start faces detection from here
	 * 
	 * @author ティエプ
	 * 
	 */
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		Camera.Parameters parameters = mCamera.getParameters();
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

	/**
	 * Set camera for processing. In this application we use facing back camera
	 * only, no switching camera
	 * 
	 * @param camera
	 */
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

	/**
	 * Create file's name for captured picture. File's name is depend on taken
	 * time.
	 * 
	 * @return file's name
	 */
	private String createFileName() {

		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			String sFileName = Environment.getExternalStorageDirectory()
					+ File.separator + System.currentTimeMillis() + ".jpg";
			return sFileName;
		}
		return "";
	}

	/**
	 * Start detecting faces
	 * 
	 * @author ティエプ
	 */
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

	/**
	 * Draw detected faces's rectangle area onto the screen. Call drawTomato
	 * method.
	 * 
	 * @author ティエプ
	 */
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

			if (faceRect.checkPointInRectangle(new Point(getWidth() / 2,
					getHeight() / 2))) {

				painter.setColor(Color.GREEN);
			}

			else {

				painter.setColor(Color.RED);

			}
		}

		drawTomato(canvas);

	}

	/**
	 * Draw the flying tomato
	 * 
	 * @param canvas
	 *            Canvas from surface view 's onDraw method
	 * @author ティエプ
	 */
	private void drawTomato(Canvas canvas) {

		for (Tomato flyingTomato : listTomatoOnScreen) {

			if (flyingTomato != null)
				flyingTomato.draw(canvas);
		}

		invalidate();

	}

	/**
	 * Get a new Tomato when fire button called and add to listTomatoOnScreen
	 * 
	 * @author ティエプ
	 */
	public void assignTomatoToPreview() {

		// handler.post(new Runnable() {
		//
		// @Override
		// public void run() {
		// // TODO Auto-generated method stub
		//
		// // invalidate();
		//
		// }
		// });

		Tomato tomato = new Tomato(Preview.this);

		listTomatoOnScreen.add(tomato);
		// invalidate();
	}
	/**
	 * @author ティエップ
	 * @return List Tomato in Screen
	 */

	public List<Tomato> getListHolder() {
		return this.listTomatoOnScreen;
	}

	public Handler getHandler() {
		return this.handler;
	}

	/**
	 * Draw the aim point at the middle of device's screen.
	 * 
	 * @author ティエプ
	 */
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

	/**
	 * Prepare a matrix to translate camera coordinate to screen coordinate
	 * 
	 * @author developer.android.com ^^
	 * @param matrix
	 * @param displayOrientation
	 * @param viewWidth
	 * @param viewHeight
	 */
	public static void prepareMatrix(Matrix matrix, int displayOrientation,
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
