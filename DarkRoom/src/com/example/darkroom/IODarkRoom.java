package com.example.darkroom;

import java.io.IOException;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.*;
import org.opencv.imgcodecs.*;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

public class IODarkRoom extends Activity {

	protected static final String TAG = null;
	
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
		OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_1_0, this,
				mLoaderCallback);
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_iodark_room);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.iodark_room, menu);
		return true;
	}

	private static final int SELECT_PICTURE = 1;
	private String selectedImagePath;
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_openGallary) {
			Intent intent = new Intent();
			intent.setType("image/*");
			intent.setAction(Intent.ACTION_GET_CONTENT);
			startActivityForResult(Intent.createChooser(intent,"Select Picture"),
			SELECT_PICTURE);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		if (resultCode == RESULT_OK) {
			if (requestCode == SELECT_PICTURE) {
				Uri selectedImageUri = data.getData();
				selectedImagePath = getPath(selectedImageUri);
				Log.i(TAG, "selectedImagePath: " + selectedImagePath);
				Mat sampledImage = loadImage(selectedImagePath);
				displayImage(sampledImage);
			}
		}
	}
	
	//helper methods
	private String getPath(Uri uri) { //nao é necessário 
		// just some safety built in
		if(uri == null ) {
			return null;
		}
		// try to retrieve the image from the media store first
		// this will only work for images selected from gallery
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = getContentResolver().query(uri, projection, null, null,
		null);
		if(cursor != null ){
			int column_index =
					cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		}
		return uri.getPath();
	}
	
	private Mat loadImage(String path){
		Mat originalImage = Imgcodecs.imread(path);
		Mat rgbImage=new Mat();
		Imgproc.cvtColor(originalImage, rgbImage, Imgproc.COLOR_BGR2RGB);
		Display display = getWindowManager().getDefaultDisplay();
		//This is "android graphics Point" class
		Point size = new Point();
		display.getSize(size);
		int width = size.x;
		int height = size.y;
		Mat sampledImage = new Mat();
		double downSampleRatio= calculateSubSampleSize(rgbImage,width,height);
		Imgproc.resize(rgbImage, sampledImage, new
				Size(),downSampleRatio,downSampleRatio,Imgproc.INTER_AREA);
		try {
			ExifInterface exif = new ExifInterface(selectedImagePath);
			int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
					1);
			switch (orientation){
				case ExifInterface.ORIENTATION_ROTATE_90:
					//get the mirrored image
					sampledImage=sampledImage.t();
					//flip on the y-axis
					Core.flip(sampledImage, sampledImage, 1);
					break;
				case ExifInterface.ORIENTATION_ROTATE_270:
					//get up side down image
					sampledImage=sampledImage.t();
					//Flip on the x-axis
					Core.flip(sampledImage, sampledImage, 0);
					break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sampledImage;
	}
	
	private static double calculateSubSampleSize(Mat srcImage, int reqWidth,int reqHeight) {
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
	}
	
	private void displayImage(Mat image){
		// create a bitMap
		Bitmap bitMap = Bitmap.createBitmap(image.cols(),
				image.rows(),Bitmap.Config.RGB_565);
		// convert to bitmap:
		Utils.matToBitmap(image, bitMap);
		// find the imageview and draw it!
		ImageView iv = (ImageView) findViewById(R.id.IODarkRoomImageView);
		iv.setImageBitmap(bitMap);
	}
	
}
