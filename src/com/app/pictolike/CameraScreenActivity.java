package com.app.pictolike;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.widget.SimpleCursorAdapter.ViewBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.Parameters;

@SuppressLint("InlinedApi")
public class CameraScreenActivity extends Fragment implements
		SurfaceHolder.Callback {

	Camera camera;
	SurfaceView surfaceView;
	SurfaceHolder surfaceHolder;
	ImageView captureButton, flipButton, flashButton;
	File pictureFile;
	boolean check = false;
	boolean isFlashOn = false;
	File file;

	String[] camFlashParams = { Parameters.FLASH_MODE_OFF,
			Parameters.FLASH_MODE_ON, Parameters.FLASH_MODE_TORCH };

	int camFlashIndex = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.activity_camerascreen,
				container, false);

		setupViews(rootView);
		return rootView;
	}

	public void setupViews(View rootView) {
		// super.onCreate(savedInstanceState);

		/** Code to change Action Bar Color */
		// ActionBar bar = getActionBar();
		// ColorDrawable cd = new ColorDrawable(0xFFFBAC00);
		// bar.setBackgroundDrawable(cd);

		/** Initializing the View */
		// setContentView(R.layout.activity_camerascreen);
		// getWindow().setFormat(PixelFormat.UNKNOWN);

		/** Mapping the capture and flip button from the xml */
		captureButton = (ImageView) rootView.findViewById(R.id.captureButton);
		flipButton = (ImageView) rootView.findViewById(R.id.btn_selfie);
		flashButton = (ImageView) rootView.findViewById(R.id.btn_flash);
		/** Making the Surface View Holder for the camera Preview */
		surfaceView = (SurfaceView) rootView.findViewById(R.id.surfaceview);
		surfaceHolder = surfaceView.getHolder();
		surfaceHolder.addCallback(this);
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		camera = Camera.open();
		camera.setDisplayOrientation(90);
		final boolean haveFlash = getActivity().getPackageManager()
				.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
		final Parameters p = camera.getParameters();

		if (!haveFlash) {
			flashButton.setImageResource(R.drawable.ic_no_flash);
		}

		flashButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (haveFlash && !check && camera != null) {

					// if we decided to add flash torch mode, this will be
					// useful
					// if(camFlashIndex < camFlashParams.length -1) {
					// camFlashIndex ++;
					// } else {
					// camFlashIndex = 0;
					// }
					// p.setFlashMode(camFlashParams[camFlashIndex]);
					// camera.setParameters(p);
					// camera.startPreview();

					if (isFlashOn) {
						p.setFlashMode(Parameters.FLASH_MODE_OFF);
						flashButton.setImageResource(R.drawable.ic_flash_off);
						camera.setParameters(p);
						camera.startPreview();
						isFlashOn = false;
					} else {
						p.setFlashMode(Parameters.FLASH_MODE_ON);
						flashButton.setImageResource(R.drawable.ic_flash_on);
						camera.setParameters(p);
						camera.startPreview();
						isFlashOn = true;
					}
				}
			}
		});

		/**
		 * Functionality of flip Button onClickListener where we want to flip
		 * the camera
		 */
		flipButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!check) {
					if (camera != null) {
						camera.stopPreview();
						camera.setPreviewCallback(null);
						camera.release();
						camera = null;
						surfaceHolder.removeCallback(CameraScreenActivity.this);
						surfaceHolder = null;
					}

					surfaceHolder = surfaceView.getHolder();
					surfaceHolder.addCallback(CameraScreenActivity.this);
					surfaceHolder
							.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
					camera = Camera.open(1);
					camera.setDisplayOrientation(90);
					try {
						camera.setPreviewDisplay(surfaceHolder);
						flipButton.setImageResource(R.drawable.ic_selfie_on);
						flashButton.setImageResource(R.drawable.ic_no_flash);
						isFlashOn = false;
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					camera.startPreview();
					check = true;
					return;
				} else if (check) {
					if (camera != null) {
						camera.stopPreview();
						camera.setPreviewCallback(null);
						camera.release();
						camera = null;
						surfaceHolder.removeCallback(CameraScreenActivity.this);
						surfaceHolder = null;
					}
					surfaceHolder = surfaceView.getHolder();
					surfaceHolder.addCallback(CameraScreenActivity.this);
					surfaceHolder
							.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
					camera = Camera.open(0);
					camera.setDisplayOrientation(90);

					try {
						camera.setPreviewDisplay(surfaceHolder);
						flipButton.setImageResource(R.drawable.ic_selfie_on);
						flashButton.setImageResource(R.drawable.ic_flash_off);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					camera.startPreview();
					check = false;
					return;
				}
				// TODO Auto-generated method stub

			}
		});

		/** Functionality of Capture Button where we want to save the image */
		captureButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				captureImage();
			}
		});

	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// super.onSaveInstanceState(outState);
		if (outState.isEmpty()) {
			outState.putBoolean("bug:fix", true);
		}
	}

	/** Setting the MenuLayout on Action Bar */
	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// getMenuInflater().inflate(R.menu.activity_camerascreen_menu, menu);
	//
	// return true;
	// }

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		try {
			camera.setPreviewDisplay(surfaceHolder);
			camera.startPreview();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		if (camera != null) {
			camera.stopPreview();
			camera.setPreviewCallback(null);
			camera.release();
			camera = null;
		}
	}

	/** CallBack event we want to execute when user takes an image */
	PictureCallback pictureBack = new PictureCallback() {
		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			pictureFile = getOutputMediaFile();

			if (pictureFile == null) {
				return;
			}
			final Context context = CameraScreenActivity.this.getActivity();
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					context);
			alertDialogBuilder.setTitle("Titel");
			alertDialogBuilder.setMessage("Do you really want to whatever?");
			alertDialogBuilder.setIcon(android.R.drawable.ic_dialog_alert);
			alertDialogBuilder
					.setPositiveButton(android.R.string.yes,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									Toast.makeText(context,
											"Yaay", Toast.LENGTH_SHORT).show();
								}
							}).setNegativeButton(android.R.string.no, null);
			
			alertDialogBuilder.show();

			try {
				FileOutputStream fos = new FileOutputStream(pictureFile);
				fos.write(data);
				fos.close();
				Toast.makeText(getActivity(), "Image Saved!", Toast.LENGTH_LONG)
						.show();
			} catch (FileNotFoundException e) {

			} catch (IOException e) {
			}

		}
	};

	/**
	 * getOutputMediaFile returns the file path on the device where we want to
	 * save the picture
	 */
	private static File getOutputMediaFile() {
		String timeStampDir = new SimpleDateFormat("yyyy-MM-dd")
				.format(new Date());
		File mediaStorageDir = Environment.getExternalStorageDirectory();
		File dir = new File(mediaStorageDir, timeStampDir);
		if (!dir.isDirectory()) {
			dir.mkdirs();
			Log.d("PictoLike", "failed to create directory");
		}

		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(new Date());
		File mediaFile = null;
		try {
			mediaFile = File.createTempFile(
					File.separator + "IMG_" + timeStamp, ".png", dir);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return mediaFile;
	}

	/**
	 * Calls when user pushes the shutter button,this method includes the
	 * callback to the camera api
	 */
	private void captureImage() {

		camera.takePicture(null, null, pictureBack);

		try {

			camera.reconnect();
			camera.startPreview();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
