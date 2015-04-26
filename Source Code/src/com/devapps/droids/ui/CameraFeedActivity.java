package com.devapps.droids.ui;

import java.io.ByteArrayOutputStream;
import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.PreviewCallback;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Surface;
import android.view.TextureView;
import android.view.TextureView.SurfaceTextureListener;
import android.widget.ImageView;

import com.devapps.droids.R;

/**
 * An activity which displays the live feeds from the Camera inside
 * CardboardView. The idea behind this is to enable a broker or any other
 * representative to show the house listing to the remote user in a way that
 * ensures that they both synchronize what they view across their devices while
 * also enabling them to highlight attributes they want each other to notice.
 * 
 * Created by mahendraliya on 26/04/15.
 */
public class CameraFeedActivity extends Activity implements SurfaceTextureListener, PreviewCallback {

	private Camera mCamera;
	private TextureView mTextureView1;
	private ImageView mCameraPreview1;
	private ImageView mCameraPreview2;
	private int mCameraRotation;
	private boolean mIsFrontFacing = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera_feed);

		mTextureView1 = (TextureView) findViewById(R.id.textureView);
		mCameraPreview1 = (ImageView) findViewById(R.id.cameraPreview1);
		mCameraPreview2 = (ImageView) findViewById(R.id.cameraPreview2);
		mTextureView1.setSurfaceTextureListener(this);
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
			mCamera = Camera.open(getCameraId());
			Camera.Parameters mParameters = mCamera.getParameters();
			List<Camera.Size> sizeList = mCamera.getParameters().getSupportedPreviewSizes();
			int bestSizeIndex = 0;

			for (int i = 0; i < sizeList.size(); i++) {
				if (sizeList.get(i).width < (getScreenWidth(this) / 2)) {
					bestSizeIndex = i;
					break;
				}
			}

			mParameters.setPreviewSize(sizeList.get(bestSizeIndex).width, sizeList.get(bestSizeIndex).height);
			mCamera.setParameters(mParameters);
			mCamera.setPreviewTexture(surface);
			mCamera.setPreviewCallback((PreviewCallback) this);

			CameraInfo cameraInfo = new CameraInfo();
			Camera.getCameraInfo(getCameraId(), cameraInfo);
			setCameraDisplayOrientation(this, getCameraId(), mCamera);
			mCamera.startPreview();

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
			mCamera.stopPreview();
			mCamera.release();
		} catch (Throwable e) {
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

		Thread previewThred = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Camera.Parameters parameters = camera.getParameters();
					int width = parameters.getPreviewSize().width;
					int height = parameters.getPreviewSize().height;

					// Convert the data to YuVIamge
					YuvImage yuv = new YuvImage(data, parameters.getPreviewFormat(), width, height, null);

					// Get the bitmap from YuvIamge
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					yuv.compressToJpeg(new Rect(0, 0, width, height), 50, out);
					byte[] bytes = out.toByteArray();
					Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

					// Rotate the image according to camera orientation
					// calculated earlier in the code
					Matrix m = new Matrix();
					m.postRotate(mCameraRotation);

					// If the camera is front facing compensate the mirror
					if (mIsFrontFacing) {
						float[] mirrorY = { -1, 0, 0, 0, 1, 0, 0, 0, 1 };
						Matrix matrixMirrorY = new Matrix();
						matrixMirrorY.setValues(mirrorY);
						m.postConcat(matrixMirrorY);
					}
					// Apply the matrix to bitmap
					bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, false);
					bitmap.setDensity(DisplayMetrics.DENSITY_DEFAULT);

					setBitmap(bitmap);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});

		previewThred.start();

	}

	private void setBitmap(final Bitmap bitmap) {
		runOnUiThread(new Runnable() {
			public void run() {
				if (bitmap != null) {
					mCameraPreview1.setImageBitmap(bitmap);
					mCameraPreview2.setImageBitmap(bitmap);
				}
			}
		});
	}

}
