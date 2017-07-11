package com.example.salvar_imagem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {

	
	void createExternalStoragePrivateFile() {
		Log.v("olha eu", "aqui eeeee");
	    // Create a path where we will place our private file on external
	    // storage.
	    File file = new File(getExternalFilesDir(null), "DemoFile.jpg");

	    try {
	        // Very simple code to copy a picture from the application's
	        // resource into the external file.  Note that this code does
	        // no error checking, and assumes the picture is small (does not
	        // try to copy it in chunks).  Note that if external storage is
	        // not currently mounted this will silently fail.
	        InputStream is = getResources().openRawResource(R.drawable.imagem1);
	        OutputStream os = new FileOutputStream(file);
	        byte[] data = new byte[is.available()];
	        is.read(data);
	        os.write(data);
	        is.close();
	        os.close();
	        
	    } catch (IOException e) {
	        // Unable to create file, likely because external storage is
	        // not currently mounted.
	        Log.w("ExternalStorage", "Error writing " + file, e);
	    }
	}

	void deleteExternalStoragePrivateFile() {
	    // Get path for the file on external storage.  If external
	    // storage is not currently mounted this will fail.
	    File file = new File(getExternalFilesDir(null), "DemoFile.jpg");
	    if (file != null) {
	        file.delete();
	    }
	}

	boolean hasExternalStoragePrivateFile() {
	    // Get path for the file on external storage.  If external
	    // storage is not currently mounted this will fail.
	    File file = new File(getExternalFilesDir(null), "DemoFile.jpg");
	    if (file != null) {
	        return file.exists();
	    }
	    return false;
	}
}
