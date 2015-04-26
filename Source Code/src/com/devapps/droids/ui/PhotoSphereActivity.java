/*
 * Copyright 2014 Google Inc. All Rights Reserved.

 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.devapps.droids.ui;

import static android.opengl.GLES20.glViewport;
import static android.opengl.Matrix.multiplyMM;

import javax.microedition.khronos.egl.EGLConfig;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.os.Bundle;
import android.util.Log;

import com.devapps.droids.R;
import com.devapps.droids.ui.photosphere.MatrixHelper;
import com.devapps.droids.ui.photosphere.Sphere;
import com.google.vrtoolkit.cardboard.CardboardActivity;
import com.google.vrtoolkit.cardboard.CardboardView;
import com.google.vrtoolkit.cardboard.Eye;
import com.google.vrtoolkit.cardboard.HeadTransform;
import com.google.vrtoolkit.cardboard.Viewport;

/**
 * An activity which provide a 360 View of the house using Cardboard.
 * 
 * Created by mahendraliya on 26/04/15.
 */
public class PhotoSphereActivity extends CardboardActivity implements CardboardView.StereoRenderer {

	private static final String TAG = "MainActivity";

	private Sphere mSphere;
	private final float[] mCamera = new float[16];
	private final float[] mProjectionMatrix = new float[16];
	private final float[] mViewProjectionMatrix = new float[16];

	private float CAMERA_Z = 0.5f;
	private float[] mView = new float[16];

	private static void checkGLError(String label) {
		int error;
		while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
			Log.e(TAG, label + ": glError " + error);
			throw new RuntimeException(label + ": glError " + error);
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_photosphere);
		CardboardView cardboardView = (CardboardView) findViewById(R.id.cardboard_view);
		cardboardView.setRenderer(this);
		setCardboardView(cardboardView);

	}

	@Override
	public void onRendererShutdown() {
		Log.i(TAG, "onRendererShutdown");
	}

	@Override
	public void onSurfaceChanged(int width, int height) {

		glViewport(0, 0, width, height);

		MatrixHelper.perspectiveM(mProjectionMatrix, 90, (float) width / (float) height, 1f, 10f);
		Log.i(TAG, "onSurfaceChanged");
	}

	@Override
	public void onSurfaceCreated(EGLConfig config) {
		Log.i(TAG, "onSurfaceCreated");
		GLES20.glClearColor(1f, 1f, 0f, 1f);// Dark background so text shows up
											// well.

		mSphere = new Sphere(this, 50, 5f);

		checkGLError("onSurfaceCreated");
	}

	@Override
	public void onNewFrame(HeadTransform headTransform) {

		Matrix.setLookAtM(mCamera, 0, 0.0f, 0.0f, CAMERA_Z, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);

		checkGLError("onReadyToDraw");
	}

	@Override
	public void onDrawEye(Eye eye) {

		// float[] scratch = new float[16];
		// long time = SystemClock.uptimeMillis() % 4000L;
		// float angle = 0.090f * ((int) time);
		// Matrix.setRotateM(mRotationMatrix, 0, angle, 0, 1, 0.0f);

		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
		Matrix.multiplyMM(mView, 0, eye.getEyeView(), 0, mCamera, 0);
		multiplyMM(mViewProjectionMatrix, 0, mProjectionMatrix, 0, mView, 0);
		// multiplyMM(mViewProjectionMatrix, 0, mProjectionMatrix, 0, mCamera,
		// 0);
		// multiplyMM(scratch, 0, mViewProjectionMatrix, 0, mRotationMatrix, 0);

		mSphere.draw(mViewProjectionMatrix);

		checkGLError("onDrawEye");

	}

	@Override
	public void onFinishFrame(Viewport viewport) {
	}

	@Override
	public void onCardboardTrigger() {
		Log.i(TAG, "onCardboardTrigger");

	}

}
