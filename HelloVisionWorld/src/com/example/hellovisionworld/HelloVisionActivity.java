package com.example.hellovisionworld;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.WindowManager;

public class HelloVisionActivity extends Activity implements CvCameraViewListener2{

	private static final String TAG = "OLHA PRA MIM PF";
	private CameraBridgeViewBase mOpenCvCameraView;
	
	private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
		//This is the callback method called once the OpenCV manageR is connected
		public void onManagerConnected(int status) {
			switch (status) {
				//Once the OpenCV manager is successfully connected we can enable the camera interaction with the defined OpenCV camera view
				case LoaderCallbackInterface.SUCCESS:{
					Log.i(TAG, "OpenCV loaded successfully");
					mOpenCvCameraView.enableView();
				} break;
			default:{
				super.onManagerConnected(status);
			} break;
			}
		}
	};
	
	@Override
	public void onResume(){
		super.onResume();
		//Call the async initialization and pass the callback object we
		//created later, and chose which version of OpenCV library to load.Just make sure that the OpenCV manager you installed //supports the
		//version you are trying to load.
		OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_1_0, this, mLoaderCallback);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hello_vision);
		
		Log.i(TAG, "called onCreate");
		
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		mOpenCvCameraView = (CameraBridgeViewBase)
		findViewById(R.id.HelloVisionView);
		//Set the view as visible
		mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
		//Register your activity as the callback object to handle camera frames
		mOpenCvCameraView.setCvCameraViewListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.hello_vision, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onCameraViewStarted(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCameraViewStopped() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
		Mat cameraFrame=inputFrame.rgba();
		double [] pixelValue=cameraFrame.get(0, 0);
		double redChannelValue=pixelValue[0];
		double greenChannelValue=pixelValue[1];
		double blueChannelValue=pixelValue[2];
		Log.i(TAG, "red channel value: "+redChannelValue);
		Log.i(TAG, "green channel value: "+greenChannelValue);
		Log.i(TAG, "blue channel value: "+blueChannelValue);
		return inputFrame.rgba();
	}
}
