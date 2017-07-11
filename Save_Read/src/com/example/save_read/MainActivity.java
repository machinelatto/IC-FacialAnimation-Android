package com.example.save_read;



import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private static final String TAG = "Check";
    private static final String TAG2 = "Aqui";
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
		saveRead1();
		saveRead2();
		saveRead3();
	}
	
	public void saveRead1(){
		Log.i(TAG2, "Entrou na primeira função");
		Bitmap mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.imagem1);
		File file = new File(getExternalFilesDir(null), "Imagem.png");
		OutputStream os = null;
		try {
			os = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		Boolean bool = mBitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
		try {
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if (bool){
			Log.i(TAG, "Imagem salva");
			Toast.makeText(this, "Imagem salva em\n" + getExternalFilesDir(null).toString(), Toast.LENGTH_LONG).show();
		}	
		else
			Log.e(TAG, "Erro na escrita da imagem");
		
		ImageView ImageView;
		ImageView = (ImageView) findViewById(R.id.imageView1);
		ImageView.setImageBitmap(BitmapFactory.decodeFile(getExternalFilesDir(null).toString()+"/Imagem.png"));
		Log.i(TAG2, "Terminou a primeira função");
	}
	
	public void saveRead2(){
		Log.i(TAG2, "Entrou na segunda função");
		Bitmap mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.imagem150);
		File file = new File(getFilesDir(), "Imagem.png");
		OutputStream os = null;
		try {
			os = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		Boolean bool = mBitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
		try {
			os.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (bool){
			Log.i(TAG, "Imagem salva");
			Toast.makeText(this, "Imagem salva em\n" + getFilesDir().toString(), Toast.LENGTH_LONG).show();
		}	
		else
			Log.e(TAG, "Erro na escrita da imagem");
		ImageView ImageView;
		ImageView = (ImageView) findViewById(R.id.imageView3);
		ImageView.setImageBitmap(BitmapFactory.decodeFile(getFilesDir() + "/Imagem.png"));
		Log.i(TAG2, "Terminou a segunda função");
	}
	
	public void saveRead3(){
		Log.i(TAG2, "Entrou na terceira função");
		Bitmap mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.imagem300);
		File file = new File(getCacheDir(), "Imagem.png");
		OutputStream os = null;
		try {
			os = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		Boolean bool = mBitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
		try {
			os.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (bool){
			Log.i(TAG, "Imagem salva");
			Toast.makeText(this, "Imagem salva em\n" + getCacheDir().toString(), Toast.LENGTH_LONG).show();
		}	
		else
			Log.e(TAG, "Erro na escrita da imagem");
		ImageView ImageView;
		ImageView = (ImageView) findViewById(R.id.imageView2);
		ImageView.setImageBitmap(BitmapFactory.decodeFile(getCacheDir() + "/Imagem.png"));
		Log.i(TAG2, "Terminou a terceira função");
	}
}

