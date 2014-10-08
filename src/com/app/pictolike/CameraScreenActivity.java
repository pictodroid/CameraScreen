package com.app.pictolike;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.app.pictolike.Utils.ConfirmDlg;
import com.app.pictolike.Utils.LocationMgr;
import com.app.pictolike.Utils.UploadWaitDlg;
import com.app.pictolike.mysql.MySQLCommand;
import com.app.pictolike.mysql.MySQLConnect;

@SuppressLint("InlinedApi")
public class CameraScreenActivity extends Fragment implements SurfaceHolder.Callback {

	Camera camera = null;
	SurfaceView surfaceView;
	SurfaceHolder surfaceHolder;
	ImageView captureButton, flipButton, flashButton;
	ImageView locview;

	boolean check = false;
	boolean isFlashOn = false;
	File file;

	String[] camFlashParams = { Parameters.FLASH_MODE_OFF, Parameters.FLASH_MODE_ON, Parameters.FLASH_MODE_TORCH };
	int camFlashIndex = 0;
	
	private UploadWaitDlg waitingDlg;
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.activity_camerascreen, container, false);

		setupViews(rootView);
		return rootView;
	}

	public void setupViews(View rootView) {
		// super.onCreate(savedInstanceState);

		locview = (ImageView)rootView.findViewById(R.id.locview);
		locview.setVisibility(View.INVISIBLE);
		
		/** Mapping the capture and flip button from the xml */
		captureButton = (ImageView) rootView.findViewById(R.id.captureButton);
		flipButton = (ImageView) rootView.findViewById(R.id.btn_selfie);
		flashButton = (ImageView) rootView.findViewById(R.id.btn_flash);
		/** Making the Surface View Holder for the camera Preview */
		surfaceView = (SurfaceView) rootView.findViewById(R.id.surfaceview);
		surfaceHolder = surfaceView.getHolder();
		surfaceHolder.addCallback(this);
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		
		try{
			camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
			camera.setDisplayOrientation(90);
		}catch(Exception e){
			return;
		}
		
		final boolean haveFlash = getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
		final Parameters p = camera.getParameters();

		if (!haveFlash) {
			flashButton.setImageResource(R.drawable.ic_no_flash);
		}

		flashButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (haveFlash && !check && camera != null) {

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
					surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
					camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
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
					surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
					camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
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
	
	public void disableCamera(){
		
		locview.setVisibility(View.VISIBLE);
		
		if (camera != null) {
			camera.stopPreview();
			camera.setPreviewCallback(null);
			camera.release();
			camera = null;
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
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

			final PhotoFile photoFile = getOutputMediaFile();

			if (photoFile.photoFile == null){
				return;
			}

			String title = "Upload";
			String msg = "Do you really want to upload?";
			
			ConfirmDlg confirmlDlg = new ConfirmDlg(CameraScreenActivity.this.getActivity(), title, msg);
			confirmlDlg.setButtonText("Yes", "No");
			confirmlDlg.setConfirmListener(new ConfirmDlg.ConfirmListener() {
				
					@Override
					public void onOkClick() {
		
						uploadPicture(photoFile);
					}
					
					@Override
					public void onCancelClick() {
					}
				}
			);
			
			confirmlDlg.show();
			
			try {
				FileOutputStream fos = new FileOutputStream(photoFile.photoFile);
				fos.write(data);
				fos.close();

				Toast.makeText(getActivity(), "Image Saved : " + photoFile.photoFile.getAbsolutePath(), Toast.LENGTH_LONG).show();
			} catch (FileNotFoundException e) {

			} catch (IOException e) {
			}

		}
	};

	private void uploadPicture(PhotoFile photoFile){

		// show animation dlg.
		waitingDlg = new UploadWaitDlg(this.getActivity());
		waitingDlg.show();
		
		String username = SignInActivity.g_name;
		String filename = photoFile.fileName;
		String datecreated = photoFile.timeStatmp;
		String locationcreated = LocationMgr.getInstance().getLocation();
		
		String deviceID = "";
		String userage = "";
		String gender = "";

		MySQLConnect.savefile(username, filename, datecreated, locationcreated, 
				deviceID, userage, gender, new MySQLCommand.OnCompleteListener() {

			@Override
			public void OnComplete(Object result) {
				// TODO Auto-generated method stub
				waitingDlg.showMessage("Photo upload complete!");
				
				disableCamera();
			}
		});
		
	}
	/**
	 * getOutputMediaFile returns the file path on the device where we want to
	 * save the picture
	 */
	private static PhotoFile getOutputMediaFile() {

		String timeStampDir = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		File mediaStorageDir = Environment.getExternalStorageDirectory();
		File dir = new File(mediaStorageDir, timeStampDir);

		if (!dir.isDirectory()) {
			dir.mkdirs();
			Log.d("PictoLike", "failed to create directory");
		}

		// Create a media file name
		Date now = new Date();
		String timeStamp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(now);
		String fileName = "IMG_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(now) + ".png";

		File mediaFile = null;
		try {
			mediaFile = File.createTempFile(File.separator + "IMG_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(now), ".png", dir);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		PhotoFile photoFile = new PhotoFile();
		photoFile.timeStatmp = timeStamp;
		photoFile.fileName = fileName;
		photoFile.photoFile = mediaFile;

		return photoFile;
	}

	public static class PhotoFile {

		public String timeStatmp;
		public String fileName;
		public File  photoFile;
	}
	/**
	 * Calls when user pushes the shutter button,this method includes the
	 * callback to the camera api
	 */
	private void captureImage() {

		if (camera == null)
			return;
		
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
