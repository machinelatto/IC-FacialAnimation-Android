package com.example.teste_cross;

import java.io.IOException;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.*;




public class MainActivity extends Activity {
	private static final String TAG = "UHUL";


	private BaseLoaderCallback  mLoaderCallback = new BaseLoaderCallback(this) {
	        @Override
	        public void onManagerConnected(int status) {
	            switch (status) {
	                case LoaderCallbackInterface.SUCCESS:
	                {
	                    Log.i(TAG, "OpenCV loaded successfully");
	                } break;
	                default:
	                {
	                    super.onManagerConnected(status);
	                } break;
	            }
	        }
	};

	@Override
	public void onResume(){
		super.onResume();
		OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_1_0, this,
				mLoaderCallback);
	}
	
	  
	    
	    
	    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if (!OpenCVLoader.initDebug()) {
	        // Handle initialization error
	    }
		try {
			crossdissolve();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	AnimationDrawable animation = new AnimationDrawable();  
	    
	public void crossdissolve() throws IOException{
		Log.i(TAG, "entrou na make1frame");
		BitmapFactory.Options o = new BitmapFactory.Options();
		o.inScaled = false;
		Bitmap mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.proc_papi_frame_20, o);
		Bitmap mBitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.proc_papi_frame_42, o);	
		
		Mat img1 = new Mat(mBitmap.getHeight(), mBitmap.getWidth(), CvType.CV_8UC3);
		Mat img2 = new Mat(mBitmap2.getHeight(), mBitmap2.getWidth(), CvType.CV_8UC3);
		Mat img3 = new Mat(mBitmap.getHeight(), mBitmap.getWidth(), CvType.CV_8UC3);
		
		Utils.bitmapToMat(mBitmap, img1);
		Utils.bitmapToMat(mBitmap2, img2);
	
		if (img1.empty()){
			 Log.e(TAG, "img1 não lida");
		} 
		
		
		for(int i = 50; i >=0 ; i--){
			double alpha =  (1.0/50)*i;
			double beta = 1 - alpha;
			Log.i(TAG, "alpha vale " + alpha);
			Log.i(TAG, "beta vale " + beta);
			
			Core.addWeighted(img1, alpha, img2, beta, 0, img3);
			
			Bitmap mBitmap3 = Bitmap.createBitmap(img1.cols(),
					img1.rows(),Bitmap.Config.RGB_565);
			Utils.matToBitmap(img3, mBitmap3);
			
			Log.i(TAG, img3.toString());
			
			Drawable frame = new BitmapDrawable(getResources(), mBitmap3);
			
			animation.addFrame(frame, 33);
			
		}
	
		
		Log.i(TAG, "a animação tem " + animation.getNumberOfFrames() + " frames com duração de " + animation.getDuration(1));	
		
		animation.setOneShot(false);
		ImageView ImageView = (ImageView) findViewById(R.id.imageView1);
		
		ImageView.setBackground(animation);
		animation.setVisible(true, true);
	}
	
	public boolean onTouchEvent(MotionEvent event) {
		  if (event.getAction() == MotionEvent.ACTION_DOWN) {
		    animation.start();
		    Log.i(TAG, "deve começar a animação");	
		    return true;
		  }
		  return super.onTouchEvent(event);
		}
	
}
	
	



