package com.mateus.morphing;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
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
//import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import com.opencsv.*;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	protected static final String TAG = "DEBUG";

	Mat images[] = new Mat[36];
	Mat images_rgb[] = new Mat[36];
	List<String[]> list = new ArrayList<String[]>();
	List<List<Point>> points = new ArrayList<List<Point>>();
	List<String> images_visemes = new ArrayList<String>();
	List<String> textgrid_visemes = new ArrayList<String>();
	double visemes_times[] = new double[9]; 
	double keypose_times[] = new double[8]; 
	int viseme1 = 0;
	int viseme2 = 0;
	//inicia o opencv manager
	private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
		@Override
		public void onManagerConnected(int status) {
			switch (status) {
				case LoaderCallbackInterface.SUCCESS:{
					Log.i("OpenCV", "OpenCV loaded successfully");
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

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_csv) {
			load_images();
			read_csv();
			processTextGrid();
		}else if(id == R.id.action_animation){
			File file = new File(getExternalFilesDir(null), "Animation.mp4"); 		 
			try {
				SequenceEncoder encoder = new SequenceEncoder(file);
				for(int i=0; i < keypose_times.length - 1; i++){
					List<List<Point>> trajectory = new ArrayList<List<Point>>();
					
					viseme1 = findIndex(textgrid_visemes.get(i));
					//textgrid_visemes.remove(i);
					viseme2 = findIndex(textgrid_visemes.get(i+1));
					//textgrid_visemes.remove(i+1);
					
					Log.i("Menu"," viseme 1: " + viseme1);
					Log.i("Menu"," viseme 2: " + viseme2);
					
					double[] ftvector = getftvector(keypose_times[i], keypose_times[i+1], 1.0/30);
					//if(viseme1!=viseme2){	
						Log.i("Menu"," points.get(viseme1): " + points.get(viseme1).toString());
						Log.i("Menu"," points.get(viseme2) " + points.get(viseme2).toString());
						trajectory = getTrajectory(ftvector, points.get(viseme1), points.get(viseme2));
						Log.i("Menu"," trajectory: " + trajectory.toString());
						morphTrajectoryAndSave(images[viseme1], images[viseme2], points.get(viseme1), points.get(viseme2), trajectory, encoder);
					//}
				}
				encoder.finish();
			} catch (IOException e) {
				e.printStackTrace();
			}
			Toast.makeText(this, "O vídeo será salvo em mp4 no diretório\n" + getExternalFilesDir(null).toString(), Toast.LENGTH_LONG).show();
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void load_images(){ //carrega todas as imagens da db num vetor
		try {
			images[0] = Utils.loadResource(this, R.drawable.proc_papi_frame_20);
			images[1] = Utils.loadResource(this, R.drawable.proc_pipa_frame_36);
			images[2] = Utils.loadResource(this, R.drawable.proc_pupi_frame_42);
			images[3] = Utils.loadResource(this, R.drawable.proc_fafi_frame_44);
			images[4] = Utils.loadResource(this, R.drawable.proc_fufi_frame_42);
			images[5] = Utils.loadResource(this, R.drawable.proc_tita_frame_41);
			images[6] = Utils.loadResource(this, R.drawable.proc_tata_frame_37);
			images[7] = Utils.loadResource(this, R.drawable.proc_sissa_frame_38);
			images[8] = Utils.loadResource(this, R.drawable.proc_sussi_frame_24);
			images[9] = Utils.loadResource(this, R.drawable.proc_lalu_frame_18);
			images[10] = Utils.loadResource(this, R.drawable.proc_lila_frame_20);
			images[11] = Utils.loadResource(this, R.drawable.proc_lula_frame_13);
			images[12] = Utils.loadResource(this, R.drawable.proc_lulu_frame_22);
			images[13] = Utils.loadResource(this, R.drawable.proc_chicha_frame_31);
			images[14] = Utils.loadResource(this, R.drawable.proc_chuchu_frame_22);
			images[15] = Utils.loadResource(this, R.drawable.proc_lhilha_frame_21);
			images[16] = Utils.loadResource(this, R.drawable.proc_lhulha_frame_24);
			images[17] = Utils.loadResource(this, R.drawable.proc_lhilhu_frame_20);
			images[18] = Utils.loadResource(this, R.drawable.proc_kika_frame_37);
			images[19] = Utils.loadResource(this, R.drawable.proc_kaka_frame_44);
			images[20] = Utils.loadResource(this, R.drawable.proc_kuku_frame_23);
			images[21] = Utils.loadResource(this, R.drawable.proc_rira_frame_19);
			images[22] = Utils.loadResource(this, R.drawable.proc_ruri_frame_21);
			images[23] = Utils.loadResource(this, R.drawable.proc_pipa_frame_32);
			images[24] = Utils.loadResource(this, R.drawable.proc_tita_frame_37);
			images[25] = Utils.loadResource(this, R.drawable.proc_papa_frame_33);
			images[26] = Utils.loadResource(this, R.drawable.proc_pupa_frame_35);
			images[27] = Utils.loadResource(this, R.drawable.proc_papi_frame_42);
			images[28] = Utils.loadResource(this, R.drawable.proc_papa_frame_41);
			images[29] = Utils.loadResource(this, R.drawable.proc_papu_frame_43);
			images[30] = Utils.loadResource(this, R.drawable.proc_eu_frame_26);
			images[31] = Utils.loadResource(this, R.drawable.proc_eu_frame_18);
			images[32] = Utils.loadResource(this, R.drawable.proc_oa_frame_16);
			images[33] = Utils.loadResource(this, R.drawable.proc_oa_frame_34);
			images[34] = Utils.loadResource(this, R.drawable.final_mask_reduced);
			images[35] = Utils.loadResource(this, R.drawable.baseface_masked_original);
			
			//Log.i("MASCARA"," numero de cnais da mascara: " + images[34].channels());
			//int i=0;
			//for(i=0; i< 34; i++)
				//Imgproc.cvtColor(images[i], images[i], Imgproc.COLOR_BGR2RGB);
			
			//Imgproc.cvtColor(images[35], images[35], Imgproc.COLOR_BGR2RGB);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private void read_csv(){ //processa o arquivo csv para obter os landmarks e os visemas
        String next[] = {};
        int i =0;
        try {
            @SuppressWarnings("resource") 
			CSVReader reader = new CSVReader(new InputStreamReader(getAssets().open("imagedbMateus.csv")));
            while(true) {
                next = reader.readNext(); //le todas as linhas 
                if(next != null) {
                    list.add(next);
                    Log.i("read_csv", list.get(i)[0]);
                    i++;
                }else{
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        for(i=0; i < list.size(); i++){
        	points.add(new ArrayList<Point>()); //cria uma lista de pontos
        	for(int j=9; j < 19; j=j+2){
        		points.get(i).add(new Point(Integer.parseInt(list.get(i)[j]), Integer.parseInt(list.get(i)[j+1]))); //separa os pontos em uma lista de lista de pontos
        		
        	}
        	images_visemes.add(list.get(i)[3]);
        	Log.i("read_csv", points.get(i).toString());
        }
      Log.i("read_csv", "images visemes " + images_visemes.toString());
	}
	
	private Mat warpDelaunay(Mat sourceimg, List<Point> sap, List<Point> tap){ //realiza o warping utilizando triangulação de Delaunay
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
			
			//realiza o warp
			Imgproc.warpAffine(sourceimg, warpedpiece, transform, s );
			
			//cria uma mascara
			Mat mask = Mat.zeros(height, width, CvType.CV_8UC1);
			MatOfPoint fill = new MatOfPoint();
			fill.fromList(dst_tri);
			Imgproc.fillConvexPoly(mask, fill, new Scalar(1) );//255 ou 1
			
			//A funçaõ copyTo não limpa a matriz destino, de modo que não preciso de uma matrix auxiliar para guardar o resultado a cada iteração
			warpedpiece.copyTo(warpedpiece1,mask);
			
			src_tri.clear();
			dst_tri.clear();
		}
		return warpedpiece1;
	}
		
	private double[] getftvector(double currft, double nextkeypose_t, double animationtick){ 
		double q =  ((nextkeypose_t-currft)/animationtick);
		double r =  ((nextkeypose_t-currft)%animationtick);
		
		//Log.i(TAG," q : " + q);
		//Log.i(TAG," r : " + r);
		
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
		Log.i(TAG," tamanho do ftvector na getftvector: " + ftvector.length);

		return ftvector;
	}

	
	private List<List<Point>> getTrajectory(double[] ftvector, List<Point> sapi, List<Point> tapi){
		int t = ftvector.length;
		Log.i(TAG," tamanho do ftvector na gettrajectory: " + ftvector.length);
		List<List<Point>> trajectory = new ArrayList<List<Point>>();
		List<Point> sap = new ArrayList<Point>();
		List<Point> tap = new ArrayList<Point>();
		int width = images[0].cols();
		int height = images[0].rows();
		
		
		sap.addAll(sapi);
		tap.addAll(tapi);
		
		Log.i(TAG," tapi na get trajectory: " + tapi.toString());
		Log.i(TAG," sapi na get trajectory: " + sapi.toString());
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
		
		Log.i(TAG," tapi na get trajectory: " + tapi.toString());
		Log.i(TAG," sapi na get trajectory: " + sapi.toString());
		
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
		sap.clear();
		tap.clear();
		Log.i(TAG," tamanho de trajectory na get trajectory: " + trajectory.size());
		return trajectory;
	}

	
	private void morphTrajectoryAndSave(Mat sourceimg, Mat targetimg, List<Point> sap_in, List<Point> tap_in, List<List<Point>> trajectory, SequenceEncoder encoder) throws IOException{
		//dimensões das imagens
		int width = sourceimg.cols();
		int height = sourceimg.rows();
		
		//novas listas para não alterar as originais
		List<Point> sap = new ArrayList<Point>();
		List<Point> tap = new ArrayList<Point>();
		sap.addAll(sap_in);
		tap.addAll(tap_in);
		
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
		
		//iniciando as variáveis
		Log.i("COM JCODEC", "Começou");
		Mat fowardWarp;
		Mat backwardWarp;
		
		
		//Rect roi = new Rect(261, 220, width, height);
		
		//vetores com os valores de alpha e beta da addWeighted
		double[] alpha = new double[trajectory.size()];
		double[] beta = new double[trajectory.size()];
		Log.i(TAG," tamanho dos vetores alpha e beta: " + trajectory.size());
		for(int i = trajectory.size()-1; i >=0 ; i--){
			alpha[i] =  (1.0/trajectory.size())*i;
			beta[i] = 1 - alpha[i];
		}
		
		//para criar e salvar um videoem formato mp4
		Bitmap mBitmap = Bitmap.createBitmap(images[34].cols(),
				images[34].rows(),Bitmap.Config.RGB_565);
		//Mat newmask = images[34].clone();
		//Mat multi = new Mat( images[34].rows(), images[34].cols(), CvType.CV_32FC1 );
		//multi.setTo(new Scalar(1/255));
		//Core.multiply(images[34],multi, newmask);
		for(int i = 0; i < trajectory.size(); i++){
			Mat frame = new Mat();
			fowardWarp = warpDelaunay(sourceimg, sap, trajectory.get(i)); //warping
			backwardWarp = warpDelaunay(targetimg, tap, trajectory.get(i)); //warping inverso
			Core.addWeighted(fowardWarp, beta[i], backwardWarp, alpha[i], 0, frame); //realiza a soma ponderada
			
			Mat masked = new Mat();
			frame.copyTo(masked, images[34]); //aplica a mascara
			
			//separa os canais da matriz resultado da transformação
			//List<Mat> framelist = new ArrayList<Mat>();
			//Core.split(frame, framelist);
			
			//List<Mat> maskedlist = new ArrayList<Mat>();
			//Core.multiply(warped.get(0), mask, warped1.get(0),1);
			//for(int j=0; j<3; j++){
			//	maskedlist.add(framelist.get(j).mul(newmask));
			//}
			
			//junta os canais da warpedpiece1 que é warpedpiece vezes a mascara
			//Core.merge(maskedlist, masked);
			Log.i("olh aqui caralho", masked.toString());
						
			//Mat base_face = Utils.loadResource(this, R.drawable.baseface_masked_original); //carregaa face base
			
			//Core.addWeighted(images[35].submat(roi), 1, masked, 1, 0, base_face.submat(roi)); //soma a imagem mascarada na face base
			
			Imgproc.cvtColor(masked, masked, Imgproc.COLOR_BGR2RGB);
			
			Utils.matToBitmap(masked, mBitmap); 
			
			//Log.i(TAG, frame.toString());
			//Log.i(TAG," alpha[i] " + alpha[i]);
			//Log.i(TAG," beta[i] " + beta[i]);
			
			encoder.encodeNativeFrame(this.fromBitmap(mBitmap));	
		}
		sap.clear();
		tap.clear();
		Log.i("COM JCODEC", "Terminou");
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

	/*Thread thread = new Thread(new Runnable(){
	  @Override
	  public void run(){
		  try {
			morphTrajectoryAndSave(images[0], images[27], points.get(0), points.get(27), trajectory);
			//handler_.sendMessage(Message.obtain(handler_));
		} catch (IOException e) {
			e.printStackTrace();
		}
	  }
	});
	
	/*private static Handler handler_ = new Handler(){
	    @Override
	    public void handleMessage(Message msg){
	    	return;
	    }

	};*/
	

	private void processTextGrid(){
		textgrid_visemes.add("#");
		textgrid_visemes.add("#");
		textgrid_visemes.add("p1");
		textgrid_visemes.add("a");
		textgrid_visemes.add("p1");
		textgrid_visemes.add("at");
		textgrid_visemes.add("#");
		textgrid_visemes.add("#");
		
		visemes_times[0] = 0;
		visemes_times[1] = 1.65264237;
		visemes_times[2] = 1.69519887;
		visemes_times[3] = 1.72015304;
		visemes_times[4] = 1.99926503;
		visemes_times[5] = 2.02152417;
		visemes_times[6] = 2.09625129;
		visemes_times[7] = 2.15227183;
		visemes_times[8] = 2.36668750;
		
		for(int i = 0; i<8; i++){
			keypose_times[i] = visemes_times[i] + (visemes_times[i+1] - visemes_times[i])/2;
			Log.d("Keypose", "Tempo " + i + keypose_times[i]);	
		}
	}
	
	int findIndex(String textGridViseme){
		return images_visemes.indexOf(textGridViseme);			
	}
}
