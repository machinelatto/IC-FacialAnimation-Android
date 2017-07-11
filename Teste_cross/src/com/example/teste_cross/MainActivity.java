package com.example.teste_cross;

import java.io.File;
import java.io.IOException;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import org.jcodec.api.SequenceEncoder;
import org.jcodec.common.model.ColorSpace;
import org.jcodec.common.model.Picture;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.*;






public class MainActivity extends Activity {
	private static final String TAG = "UHUL";

	 @SuppressWarnings("unused")
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
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if (!OpenCVLoader.initDebug()) {
	        // Handle initialization error
	    }
		try {
			makeframes();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public void makeframes() throws IOException{
		Log.i(TAG, "entrou na make1frame");
		Bitmap mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.proc_papi_frame_20);
		Bitmap mBitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.proc_papi_frame_42);
		Bitmap mBitmap3 = BitmapFactory.decodeResource(getResources(), R.drawable.proc_papi_frame_42);		
		
		Mat img1 = new Mat(mBitmap.getHeight(), mBitmap.getWidth(), CvType.CV_8UC3);
		Mat img2 = new Mat(mBitmap2.getHeight(), mBitmap2.getWidth(), CvType.CV_8UC3);
		Mat img3 = new Mat(mBitmap.getHeight(), mBitmap.getWidth(), CvType.CV_8UC3);
		Utils.bitmapToMat(mBitmap, img1);
		Utils.bitmapToMat(mBitmap2, img2);
	
		 if (img1.empty()){
			 Log.e(TAG, "img1 não lida");
		 }
		
		File file = new File(getExternalFilesDir(null), "Video.mp4"); 		 
		SequenceEncoder encoder = new SequenceEncoder(file);
		
		for(int i = 150; i > 0; i--){
			double alpha = (1.0 / 150) * i;
			double beta = 1 - alpha;
			
			Core.addWeighted(img1, alpha, img2, beta, 0, img3);
		
			if (img3.empty()){
				Log.e(TAG, "img3 não calculada");
			}
		 
			Utils.matToBitmap(img3, mBitmap3);
			
			encoder.encodeNativeFrame(this.fromBitmap(mBitmap3));
		}
		
		Log.i(TAG, "saiu do for");
		
		encoder.finish();
		
		
	}
	
	public Picture fromBitmap(Bitmap src) {
	    Picture dst = Picture.create((int)src.getWidth(), (int)src.getHeight(), ColorSpace.RGB);
	    fromBitmap(src, dst);
	    return dst;
	}

	public void fromBitmap(Bitmap src, Picture dst) {
	    int[] dstData = dst.getPlaneData(0);
	    int[] packed = new int[src.getWidth() * src.getHeight()];

	    src.getPixels(packed, 0, src.getWidth(), 0, 0, src.getWidth(), src.getHeight());

	    for (int i = 0, srcOff = 0, dstOff = 0; i < src.getHeight(); i++) {
	        for (int j = 0; j < src.getWidth(); j++, srcOff++, dstOff += 3) {
	            int rgb = packed[srcOff];
	            dstData[dstOff]     = (rgb >> 16) & 0xff;
	            dstData[dstOff + 1] = (rgb >> 8) & 0xff;
	            dstData[dstOff + 2] = rgb & 0xff;
	        }
	    }
	}
	
}
	
	



