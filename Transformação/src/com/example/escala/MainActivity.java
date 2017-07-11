package com.example.escala;



import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.imgproc.*;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

public class MainActivity extends Activity {
	private static final String TAG = "AQUI";
	
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
			Log.e(TAG, "Erro com a inicialização do OpenCV");
	    }
		rotation();
	}
	
	public void rotation(){
		Log.i(TAG, "Entrou na função");
		Bitmap mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.imagem1);
		Mat img1 = new Mat(mBitmap.getHeight(), mBitmap.getWidth(), CvType.CV_8UC3);
		Mat rot = new Mat();
		Mat M = new Mat();
		Mat img2 = new Mat();
		Utils.bitmapToMat(mBitmap, img1);
		
		
		
		rot = Imgproc.getRotationMatrix2D(new Point(M.cols()/2, M.rows()/2), 90, 1);
		Imgproc.warpAffine(M, img2, rot, img2.size());
		
		Bitmap mBitmap2 = Bitmap.createBitmap(img2.cols(), img2.rows(), Bitmap.Config.ARGB_8888);
		Utils.matToBitmap(img2, mBitmap2);
		ImageView ImageView;
		ImageView = (ImageView) findViewById(R.id.imageView1);
		ImageView ImageView2;
		ImageView2 = (ImageView) findViewById(R.id.imageView2);
		ImageView2.setImageBitmap(mBitmap);
		ImageView.setImageBitmap(mBitmap2);
		
	}
}

