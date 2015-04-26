package com.devapps.droids.ui;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Surface;
import android.view.TextureView.SurfaceTextureListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;

import com.devapps.droids.R;

/**
 * An activity which displays the locally saved Video in a form similar to
 * CardboardView. The idea of this is to show the house listing to at any time
 * which is comfortable to the user. Besides the same video can be video again
 * if required.
 * 
 * Created by brijeshmasrani on 26/04/15.
 */
public class LocalVideoActivity extends Activity implements SurfaceTextureListener, PreviewCallback {

	// private Camera mCamera;
	private VideoView mCameraPreview1;
	private VideoView mCameraPreview2;
	private int mCameraRotation;
	private boolean mIsFrontFacing = false;
	MediaController mControl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_video);
		
		mControl = new MediaController(LocalVideoActivity.this);

		mCameraPreview1 = (VideoView) findViewById(R.id.cameraPreview1);
		mCameraPreview2 = (VideoView) findViewById(R.id.cameraPreview2);

		// mCameraPreview1.setVideoPath("rtmp://192.168.43.144:1935/live/test.stream");
		mCameraPreview1.setVideoURI(Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + R.raw.housing));
		mCameraPreview1.setMediaController(mControl);

		// mCameraPreview2.setVideoPath("rtmp://192.168.43.144:1935/live/test.stream");
		mCameraPreview2.setVideoURI(Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + R.raw.housing));
		mCameraPreview2.setMediaController(mControl);

		mCameraPreview1.requestFocus();
		mCameraPreview2.requestFocus();

		new Thread() {
			public void run() {

				try {
					Thread.sleep(200);
					mCameraPreview1.start();
					mCameraPreview2.start();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			};
		}.start();

	}

	private void setCameraDisplayOrientation(Activity activity, int cameraId, android.hardware.Camera camera) {
		android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
		android.hardware.Camera.getCameraInfo(cameraId, info);
		int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
		int degrees = 0;
		switch (rotation) {
		case Surface.ROTATION_0:
			degrees = 0;
			break;
		case Surface.ROTATION_90:
			degrees = 90;
			break;
		case Surface.ROTATION_180:
			degrees = 180;
			break;
		case Surface.ROTATION_270:
			degrees = 270;
			break;
		}

		int result;
		if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
			result = (info.orientation + degrees) % 360;
			result = (360 - result) % 360; // compensate the mirror
			mIsFrontFacing = true;
		} else { // back-facing
			result = (info.orientation - degrees + 360) % 360;
			mIsFrontFacing = false;
		}
		mCameraRotation = result;
		camera.setDisplayOrientation(result);
	}

	private int getCameraId() {
		int cameraCount = 0;
		Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
		cameraCount = Camera.getNumberOfCameras();

		for (int camIdx = 0; camIdx < cameraCount; camIdx++) {

			Camera.getCameraInfo(camIdx, cameraInfo);
			if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
				return camIdx;
			}
		}

		for (int camIdx = 0; camIdx < cameraCount; camIdx++) {

			Camera.getCameraInfo(camIdx, cameraInfo);
			if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
				return camIdx;
			}
		}

		return 0;

	}

	@Override
	public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
		try {
			/*
			 * mCamera = Camera.open(getCameraId()); Camera.Parameters
			 * mParameters = mCamera.getParameters(); List<Camera.Size> sizeList
			 * = mCamera.getParameters().getSupportedPreviewSizes(); int
			 * bestSizeIndex = 0;
			 * 
			 * for (int i = 0; i < sizeList.size(); i++ ) { if
			 * (sizeList.get(i).width < (getScreenWidth(this) / 2)) {
			 * bestSizeIndex = i; break; } }
			 * 
			 * mParameters.setPreviewSize(sizeList.get(bestSizeIndex).width,
			 * sizeList.get(bestSizeIndex).height);
			 * mCamera.setParameters(mParameters);
			 * mCamera.setPreviewTexture(surface);
			 * mCamera.setPreviewCallback((PreviewCallback) this);
			 * 
			 * CameraInfo cameraInfo = new CameraInfo();
			 * Camera.getCameraInfo(getCameraId(), cameraInfo); //
			 * setCameraDisplayOrientation(this, getCameraId(), mCamera);
			 * mCamera.startPreview();
			 * 
			 * mParameters.setPreviewSize(sizeList.get(bestSizeIndex).width,
			 * sizeList.get(bestSizeIndex).height);
			 */

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static int getScreenWidth(Activity activity) {
		DisplayMetrics displayMetrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		int width = displayMetrics.widthPixels;
		return width;
	}

	@Override
	public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
		try {
			/*
			 * mCamera.stopPreview(); mCamera.release();
			 */
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

	}

	@Override
	public void onSurfaceTextureUpdated(SurfaceTexture surface) {

	}

	@Override
	public void onPreviewFrame(final byte[] data, final Camera camera) {

		/*
		 * Thread previewThred = new Thread(new Runnable() {
		 * 
		 * @Override public void run() { try { Camera.Parameters parameters =
		 * camera.getParameters(); int width =
		 * parameters.getPreviewSize().width; int height =
		 * parameters.getPreviewSize().height;
		 * 
		 * //Convert the data to YuVIamge YuvImage yuv = new YuvImage(data,
		 * parameters.getPreviewFormat(), width, height, null);
		 * 
		 * //Get the bitmap from YuvIamge ByteArrayOutputStream out = new
		 * ByteArrayOutputStream(); yuv.compressToJpeg(new Rect(0, 0, width,
		 * height), 50, out); byte[] bytes = out.toByteArray(); Bitmap bitmap =
		 * BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
		 * 
		 * //Rotate the image according to camera orientation calculated earlier
		 * in the code Matrix m = new Matrix(); m.postRotate(mCameraRotation);
		 * 
		 * //If the camera is front facing compensate the mirror if
		 * (mIsFrontFacing) { float[] mirrorY = { -1, 0, 0, 0, 1, 0, 0, 0, 1};
		 * Matrix matrixMirrorY = new Matrix();
		 * matrixMirrorY.setValues(mirrorY); m.postConcat(matrixMirrorY); }
		 * //Apply the matrix to bitmap bitmap = Bitmap.createBitmap(bitmap, 0,
		 * 0, bitmap.getWidth(), bitmap.getHeight(), m, false);
		 * bitmap.setDensity(DisplayMetrics.DENSITY_DEFAULT);
		 * 
		 * setBitmap(bitmap); } catch (Exception e) { e.printStackTrace(); }
		 * 
		 * } });
		 * 
		 * previewThred.start();
		 */

	}

	private void setBitmap(final Bitmap bitmap) {
		runOnUiThread(new Runnable() {
			public void run() {
				if (bitmap != null) {

				}
			}
		});
	}

}