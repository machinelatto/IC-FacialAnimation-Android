package com.example.operations;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jcodec.api.SequenceEncoder;
import org.jcodec.common.model.ColorSpace;
import org.jcodec.common.model.Picture;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Canvas;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	protected static final String TAG = "DEBUG";
	
	//inicia o opencv manager
	private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
		@Override
		public void onManagerConnected(int status) {
			switch (status) {
				case LoaderCallbackInterface.SUCCESS:{
					Log.i(TAG, "OpenCV loaded successfully");
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
		OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_2_0, this,
				mLoaderCallback);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private Mat M;
	private Mat M2;
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_abrirImagem) {
			M = loadImage(1);
			M2 = loadImage(2);
			//showImage(M);
		}
		else if (id == R.id.action_desenhar) {
			if(M == null){
				Context context = getApplicationContext();
				CharSequence text = "Abra a imagem primeiro!";
				int duration = Toast.LENGTH_SHORT;
				Toast toast = Toast.makeText(context, text, duration);
				toast.show();
				return true;
			}
			
			double[] x = {104, 105, 66, 139, 106};
			double[] y = {53,70, 60, 59, 103};
			drawPointsOnImage(M, x, y ,5 , 1);
		}
		else if (id == R.id.action_warp){
			ArrayList<Point> sap = new ArrayList<Point>();
			ArrayList<Point> tap = new ArrayList<Point>();
			
			sap.add(new Point(208,106));
			sap.add(new Point(210,140));
			sap.add(new Point(132,120));
			sap.add(new Point(278,118));
			sap.add(new Point(212,206));

			tap.add(new Point(208,104));
			tap.add(new Point(208,146));
			tap.add(new Point(130,116));
			tap.add(new Point(281,108));
			tap.add(new Point(206,206));
			int width = M.cols();
			int height = M.rows();
			//Adiciona os pontos nas bordas		
			sap.add(new Point(0,0));
			sap.add(new Point(width/2,0));
			sap.add(new Point(width-1,0));
			sap.add(new Point(0,(height-1)));
			sap.add(new Point(width/2,(height-1)));
			sap.add(new Point((width-1),(height-1)));
			sap.add(new Point(0,height/2));
			sap.add(new Point((width-1),height/2));
			
			tap.add(new Point(0,0));
			tap.add(new Point(width/2,0));
			tap.add(new Point(width-1,0));
			tap.add(new Point(0,(height-1)));
			tap.add(new Point(width/2,(height-1)));
			tap.add(new Point((width-1),(height-1)));
			tap.add(new Point(0,height/2));
			tap.add(new Point((width-1),height/2));
			warpDelaunay(M, sap, tap);
		}
		else if (id == R.id.action_morph){
			ArrayList<Point> sap = new ArrayList<Point>();
			ArrayList<Point> tap = new ArrayList<Point>();
			
			sap.add(new Point(208,106));
			sap.add(new Point(210,140));
			sap.add(new Point(132,120));
			sap.add(new Point(278,118));
			sap.add(new Point(212,206));

			tap.add(new Point(208,104));
			tap.add(new Point(208,146));
			tap.add(new Point(130,116));
			tap.add(new Point(281,108));
			tap.add(new Point(206,206));
			
			int width = M.cols();
			int height = M.rows();
			//Adiciona os pontos nas bordas		
			sap.add(new Point(0,0));
			sap.add(new Point(width/2,0));
			sap.add(new Point(width-1,0));
			sap.add(new Point(0,(height-1)));
			sap.add(new Point(width/2,(height-1)));
			sap.add(new Point((width-1),(height-1)));
			sap.add(new Point(0,height/2));
			sap.add(new Point((width-1),height/2));
			
			tap.add(new Point(0,0));
			tap.add(new Point(width/2,0));
			tap.add(new Point(width-1,0));
			tap.add(new Point(0,(height-1)));
			tap.add(new Point(width/2,(height-1)));
			tap.add(new Point((width-1),(height-1)));
			tap.add(new Point(0,height/2));
			tap.add(new Point((width-1),height/2));
			//crossdissolve(M2, M);
			double[] ftvector = getftvector(0, 3, 1.0/30);
			
			List<List<Point>> trajectory = getTrajectory( 0, ftvector, sap, tap);
			
			morphTrajectory(M, M2,  sap, tap, trajectory);
			
			/*animation.start();
			if(animation.isRunning())
				Log.i(TAG, "ta rodando");*/
			
		}else if (id == R.id.action_morph_save){
			ArrayList<Point> sap = new ArrayList<Point>();
			ArrayList<Point> tap = new ArrayList<Point>();
			
			sap.add(new Point(208,106));
			sap.add(new Point(210,140));
			sap.add(new Point(132,120));
			sap.add(new Point(278,118));
			sap.add(new Point(212,206));

			tap.add(new Point(208,104));
			tap.add(new Point(208,146));
			tap.add(new Point(130,116));
			tap.add(new Point(281,108));
			tap.add(new Point(206,206));
			
			int width = M.cols();
			int height = M.rows();
			//Adiciona os pontos nas bordas		
			sap.add(new Point(0,0));
			sap.add(new Point(width/2,0));
			sap.add(new Point(width-1,0));
			sap.add(new Point(0,(height-1)));
			sap.add(new Point(width/2,(height-1)));
			sap.add(new Point((width-1),(height-1)));
			sap.add(new Point(0,height/2));
			sap.add(new Point((width-1),height/2));
			
			tap.add(new Point(0,0));
			tap.add(new Point(width/2,0));
			tap.add(new Point(width-1,0));
			tap.add(new Point(0,(height-1)));
			tap.add(new Point(width/2,(height-1)));
			tap.add(new Point((width-1),(height-1)));
			tap.add(new Point(0,height/2));
			tap.add(new Point((width-1),height/2));
			//crossdissolve(M2, M);
			double[] ftvector = getftvector(0, 3, 1.0/30);
			
			List<List<Point>> trajectory = getTrajectory(0, ftvector, sap, tap);
			
			try {
				morphTrajectoryAndSave(M, M2,  sap, tap, trajectory);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return super.onOptionsItemSelected(item);
	}
	
	public boolean onTouchEvent(MotionEvent event) {
		  if (event.getAction() == MotionEvent.ACTION_DOWN) {
		    animation.start();
		    Log.i(TAG, "deve começar a animação");	
		    return true;
		  }
		  return super.onTouchEvent(event);
		}
	
	private Mat loadImage(int sel){
		BitmapFactory.Options o = new BitmapFactory.Options();
		o.inScaled = false;
		Bitmap mBitmap;
		if(sel == 1){
			mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.proc_papi_frame_20, o);
		}
		else{
			mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.proc_papi_frame_42, o);
		}
		//Display display = getWindowManager().getDefaultDisplay();
		//This is "android graphics Point" class
		//Point size = new Point();
		//display.getSize(size);
		//int width = size.x;
		//int height = size.y;
		Mat rgbImage = new Mat();
		Utils.bitmapToMat(mBitmap, rgbImage);
		Log.i(TAG,rgbImage.size().toString());
		Size size = new Size(rgbImage.cols()*2, rgbImage.rows()*2);
		Mat sampledImage = new Mat();
		//double downSampleRatio= calculateSubSampleSize(rgbImage,width,height);
		Imgproc.resize(rgbImage, sampledImage, size);
		Log.i(TAG,sampledImage.size().toString());
		return sampledImage;
	}
	
	/*private static double calculateSubSampleSize(Mat srcImage, int reqWidth,int reqHeight) {
		// Raw height and width of image
		final int height = srcImage.height();
		final int width = srcImage.width();
		double inSampleSize = 1;
		if (height > reqHeight || width > reqWidth) {
			// Calculate ratios of requested height and width to the raw
			//height and width
			final double heightRatio = (double) reqHeight / (double) height;
			final double widthRatio = (double) reqWidth / (double) width;
			// Choose the smallest ratio as inSampleSize value, this will
			//guarantee final image with both dimensions larger than or
			//equal to the requested height and width.
			inSampleSize = heightRatio<widthRatio ? heightRatio :widthRatio;
		}
		return inSampleSize;
	}*/
	
	private void drawPointsOnImage(Mat im, double x[], double y[],int radius ,int zoom){
		int i = 0;
		Bitmap bitmap = Bitmap.createBitmap(im.cols(), im.rows(),Bitmap.Config.RGB_565);
		Utils.matToBitmap(im, bitmap);
		Canvas canvas = new Canvas(bitmap);
		canvas.drawBitmap(bitmap, 0, 0, null);
		Paint paint = new Paint();
		for(i = 0; i < x.length; i++){
			canvas.drawCircle((float)x[i]*2, (float)y[i]*2, radius, paint);
		}
		ImageView iv = (ImageView) findViewById(R.id.ImageView);
		iv.setImageBitmap(bitmap);
	}	
	
	/*private void showImage(Mat image){
		// create a bitMap
		Bitmap bitMap = Bitmap.createBitmap(image.cols(),
				image.rows(),Bitmap.Config.RGB_565);
		// convert to bitmap:
		Utils.matToBitmap(image, bitMap);
		// find the imageview and draw it!
		ImageView iv = (ImageView) findViewById(R.id.ImageView);
		iv.setImageBitmap(bitMap);
	}*/
	
	private Mat warpDelaunay(Mat sourceimg, List<Point> sap, List<Point> tap){
		Log.i("WARPING","COMEÇO DO WARPING");
		
		int width = sourceimg.cols();
		int height = sourceimg.rows();
        

		
		//Log.i(TAG,"sap "+sap.toString());
		//Log.i(TAG,"tap "+tap.toString());
		
		//Equivalente a rodar a triangulção Delaunay
		Point[][][] src_triangles = {{{sap.get(0)},
				                      {sap.get(3)},
		                              {sap.get(1)}},
		                             
		                             {{sap.get(12)},
					                  {sap.get(3)},
			                          {sap.get(7)}},
			                          
			                         {{sap.get(3)},
						              {sap.get(6)},
				                      {sap.get(7)}},
				                     
				                     {{sap.get(3)},
						              {sap.get(0)},
				                      {sap.get(6)}},
				                      
				                     {{sap.get(2)},
						              {sap.get(0)},
				                      {sap.get(1)}},
				                      
				                     {{sap.get(11)},
						              {sap.get(2)},
				                      {sap.get(8)}},
				                      
				                     {{sap.get(2)},
						              {sap.get(11)},
				                      {sap.get(5)}},
				                      
				                     {{sap.get(6)},
						              {sap.get(2)},
				                      {sap.get(5)}},
				                      
				                     {{sap.get(0)},
						              {sap.get(2)},
				                      {sap.get(6)}},
				                      
				                     {{sap.get(3)},
						              {sap.get(4)},
				                      {sap.get(1)}},
				                      
				                     {{sap.get(4)},
						              {sap.get(2)},
				                      {sap.get(1)}},
				                      
				                     {{sap.get(9)},
						              {sap.get(4)},
				                      {sap.get(10)}},
				                      
				                     {{sap.get(4)},
						              {sap.get(9)},
				                      {sap.get(8)}},
				                          
				                     {{sap.get(2)},
							          {sap.get(4)},
					                  {sap.get(8)}},
					                          
					                 {{sap.get(4)},
							          {sap.get(12)},
						              {sap.get(10)}},
						                           
						             {{sap.get(4)},
					   			      {sap.get(3)},
						              {sap.get(12)}}
   						         };
		
		Point[][][] dst_triangles = {{{tap.get(0)},
            {tap.get(3)},
            {tap.get(1)}},
           
           {{tap.get(12)},
            {tap.get(3)},
            {tap.get(7)}},
            
           {{tap.get(3)},
            {tap.get(6)},
            {tap.get(7)}},
           
           {{tap.get(3)},
            {tap.get(0)},
            {tap.get(6)}},
            
           {{tap.get(2)},
            {tap.get(0)},
            {tap.get(1)}},
            
           {{tap.get(11)},
            {tap.get(2)},
            {tap.get(8)}},
            
           {{tap.get(2)},
            {tap.get(11)},
            {tap.get(5)}},
            
           {{tap.get(6)},
            {tap.get(2)},
            {tap.get(5)}},
            
           {{tap.get(0)},
            {tap.get(2)},
            {tap.get(6)}},
            
           {{tap.get(3)},
            {tap.get(4)},
            {tap.get(1)}},
            
           {{tap.get(4)},
            {tap.get(2)},
            {tap.get(1)}},
            
           {{tap.get(9)},
            {tap.get(4)},
            {tap.get(10)}},
            
           {{tap.get(4)},
            {tap.get(9)},
            {tap.get(8)}},
                
           {{tap.get(2)},
	        {tap.get(4)},
            {tap.get(8)}},
                    
           {{tap.get(4)},
	        {tap.get(12)},
            {tap.get(10)}},
                         
           {{tap.get(4)},
	        {tap.get(3)},
            {tap.get(12)}}
	      };
		
		//Matrizes auxiliares
		//Mat partial = Mat.zeros(height, width, CvType.CV_8UC3);
		Mat warpedpiece1 = new Mat(height,width,CvType.CV_8UC3);
		Mat warpedpiece = new Mat(height,width,CvType.CV_8UC3);
		Size s = new Size(width,height);
		
		//Cria listas de pontos para passar para as MatOfPoint2f
		List<Point> src_tri = new ArrayList<Point>(); 
		List<Point> dst_tri = new ArrayList<Point>(); 
		MatOfPoint2f src = new MatOfPoint2f();
		MatOfPoint2f dst = new MatOfPoint2f();
		
		//incia a parte que faz o warping
		for (int i=0;i<16;i++){ //sao 16 tirangulos
			//coloca os elementos dos vetores com os vertices dos triangulos nas listas
			for(int j=0;j<3;j++){
				src_tri.add(src_triangles[i][j][0]);
				dst_tri.add(dst_triangles[i][j][0]);
			}
			
			//coloca os valores das listas nas MatOfPoint2f, 
			src.fromList(src_tri); //src.fromArray(); nao funcionou :(
			dst.fromList(dst_tri);
			
			//Log.i(TAG,"src_tri "+src_tri.toString());
			//Log.i(TAG,"dst_tri "+dst_tri.toString());
			
			//encontra a transformação
			Mat transform = Imgproc.getAffineTransform(src, dst);
			 
			
			//Log.i(TAG,"Passou");
			
			//realiza o warp
			Imgproc.warpAffine(sourceimg, warpedpiece, transform, s );
			
			//Log.i(TAG,"Passou de novo");
			
			//cria uma mascara
			Mat mask = Mat.zeros(height, width, CvType.CV_8UC1);
			MatOfPoint fill = new MatOfPoint();
			fill.fromList(dst_tri);
			Imgproc.fillConvexPoly(mask, fill, new Scalar(1) );//255 ou 1
		
		
			//separa os canais da matriz resultado da transformação
			//List<Mat> warped = new ArrayList<Mat>();
			//Core.split(warpedpiece, warped);
			
			//matrizes q formam a warpedpiece1 
			//List<Mat> warped1 = new ArrayList<Mat>();
			
			//List<Mat> part = new ArrayList<Mat>();
			
			//realiza a operação canal por canal, não funcionou utilizar a matriz warped como operando e destino por isso tem a warped1
			
			//Core.multiply(warped.get(0), mask, warped1.get(0),1);
			/*for(int j=0; j<3; j++){
				warped1.add(warped.get(j).mul(mask));
			}*/
			
			//junta os canais da warpedpiece1 que é warpedpiece vezes a mascara
			//Core.merge(warped1, warpedpiece1);
			
			//Core.add(warpedpiece ,partial, partial);;
			
			//A funçaõ copyTo não limpa a matriz destino, de modo que não preciso de uma matrix auxiliar para guardar o resultado a cada iteração
			warpedpiece.copyTo(warpedpiece1,mask);
			
			
			
			src_tri.clear();
			dst_tri.clear();
			
			
		}
		Log.i("WARPING","FIM do warping");
		return warpedpiece1;
	}

	AnimationDrawable animation = new AnimationDrawable();  
    
	public void crossdissolve(Mat img1, Mat img2){
		Mat img3 = new Mat(img1.rows(),img1.cols(), img1.type());
		
		if (img1.empty()){
			 Log.e(TAG, "img1 não lida");
		} 
		if (img2.empty()){
			 Log.e(TAG, "img3 não lida");
		} 
		for(int i = 300; i >=0 ; i--){
			double alpha =  (1.0/300)*i;
			double beta = 1 - alpha;
			Log.i(TAG, "alpha vale " + alpha);
			Log.i(TAG, "beta vale " + beta);
			
			Core.addWeighted(img1, alpha, img2, beta, 0, img3);
			
			Bitmap mBitmap = Bitmap.createBitmap(img3.cols(),
					img3.rows(),Bitmap.Config.RGB_565);
			Utils.matToBitmap(img3, mBitmap);
			
			Log.i(TAG, img3.toString());
			
			Drawable frame = new BitmapDrawable(getResources(), mBitmap);
			
			animation.addFrame(frame, 33);	
		}
		Log.i(TAG, "a animação tem " + animation.getNumberOfFrames() + " frames com duração de " + animation.getDuration(1));	
		
		animation.setOneShot(false);
		ImageView ImageView = (ImageView) findViewById(R.id.ImageView);
		
		ImageView.setBackground(animation);
		animation.setVisible(true, true);

	}
	
	private double[] getftvector(double currft, double nextkeypose_t, double animationtick){
		double q =  ((nextkeypose_t-currft)/animationtick);
		double r =  ((nextkeypose_t-currft)%animationtick);
		
		Log.i(TAG," q : " + q);
		Log.i(TAG," r : " + r);
		
		int kframes = 0;
		
		if((animationtick - r)<0.0001)
			kframes = (int) (q+1);
		else
			kframes = (int) q;
		
		Log.i(TAG," kframes : " + kframes);
		
		double[] ftvector = new double[kframes];
		
		for(int i = 0; i < kframes; i++){
			ftvector[i] = currft+animationtick*(i+1);
		}
		Log.i(TAG,"ftvector: " + ftvector.length);

		return ftvector;
	}

	private List<List<Point>> getTrajectory(double currft, double[] ftvector, List<Point> sap, List<Point> tap){
		int t = ftvector.length;
		//Log.i(TAG," q q ta con t seno: " + ftvector.length);
		List<List<Point>> trajectory = new ArrayList<List<Point>>();
		
		double[] sapx = new double[sap.size()];
		double[] sapy = new double[sap.size()];
		double[] tapx = new double[tap.size()];
		double[] tapy = new double[tap.size()];
	
		for(int i = 0; i < sap.size(); i++){
			sapx[i] = sap.get(i).x;
			sapy[i] = sap.get(i).y;
			tapx[i] = tap.get(i).x;
			tapy[i] = tap.get(i).y;
		}
		
		for(int i = 0; i<t; i++){
			trajectory.add(new ArrayList<Point>());
			for(int j = 0; j<sap.size(); j++){
				trajectory.get(i).add(new Point((((tapx[j]-sapx[j])/t)*i)+sapx[j],(((tapy[j]-sapy[j])/t)*i)+sapy[j]));
				//Log.i(TAG," q q ta con t seno: " + trajectory.get(i).get(j));
			}
			
		}
		Log.i(TAG," tamanho de trajectory na get trajectory: " + trajectory.size());
		return trajectory;
	}
		
	private void morphTrajectory(Mat sourceimg, Mat targetimg, List<Point> sap, List<Point> tap, List<List<Point>> trajectory){
		Log.i("MORPHING","COMEÇÕU O MORPHING NORMAL");
		Mat fowardWarp;
		Mat backwardWarp;
		Mat frame = new Mat();
		double[] alpha = new double[trajectory.size()];
		double[] beta = new double[trajectory.size()];
		Log.i(TAG," tamanho dos vetores alpha e beta: " + trajectory.size());
		for(int i = trajectory.size()-1; i >=0 ; i--){
			alpha[i] =  (1.0/trajectory.size())*i;
			beta[i] = 1 - alpha[i];
		}
		
		for(int i = 0; i < trajectory.size(); i++){
			fowardWarp = warpDelaunay(sourceimg, sap, trajectory.get(i));
			backwardWarp = warpDelaunay(targetimg, tap, trajectory.get(i));
			
			Core.addWeighted(fowardWarp, beta[i], backwardWarp, alpha[i], 0, frame);
			
			Bitmap mBitmap = Bitmap.createBitmap(frame.cols(),
					frame.rows(),Bitmap.Config.RGB_565);
			Utils.matToBitmap(frame, mBitmap);
			
			Log.i(TAG, frame.toString());
			Log.i(TAG," alpha[i] " + alpha[i]);
			Log.i(TAG," beta[i] " + beta[i]);
			
			Drawable animationFrame = new BitmapDrawable(getResources(), mBitmap);
			
			animation.addFrame(animationFrame, 33);	
		}
		
		Log.i(TAG, "a animação tem " + animation.getNumberOfFrames() + " frames com duração de " + animation.getDuration(1));	
		
		animation.setOneShot(false);
		ImageView ImageView = (ImageView) findViewById(R.id.ImageView);
		
		ImageView.setBackground(animation);
		animation.setVisible(true, true);
		Log.i("MORPHING","TERMINOU O MORPHING NORMAL");
	}

	private void morphTrajectoryAndSave(Mat sourceimg, Mat targetimg, List<Point> sap, List<Point> tap, List<List<Point>> trajectory) throws IOException{
		Log.i("COM JCODEC", "Começou");
		Mat fowardWarp;
		Mat backwardWarp;
		Mat frame = new Mat();
		double[] alpha = new double[trajectory.size()];
		double[] beta = new double[trajectory.size()];
		Log.i(TAG," tamanho dos vetores alpha e beta: " + trajectory.size());
		for(int i = trajectory.size()-1; i >=0 ; i--){
			alpha[i] =  (1.0/trajectory.size())*i;
			beta[i] = 1 - alpha[i];
		}
		
		Bitmap mBitmap = Bitmap.createBitmap(M.cols(),
				M.rows(),Bitmap.Config.RGB_565);
		File file = new File(getExternalFilesDir(null), "Morphing.mp4"); 		 
		SequenceEncoder encoder = new SequenceEncoder(file);
		
		for(int i = 0; i < trajectory.size(); i++){
			fowardWarp = warpDelaunay(sourceimg, sap, trajectory.get(i));
			backwardWarp = warpDelaunay(targetimg, tap, trajectory.get(i));
			
			Core.addWeighted(fowardWarp, beta[i], backwardWarp, alpha[i], 0, frame);
			
			
			Utils.matToBitmap(frame, mBitmap);
			
			Log.i(TAG, frame.toString());
			Log.i(TAG," alpha[i] " + alpha[i]);
			Log.i(TAG," beta[i] " + beta[i]);
			
			encoder.encodeNativeFrame(this.fromBitmap(mBitmap));	
		}
		
		Log.i("COM JCODEC", "Terminou");
		
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
	
	


