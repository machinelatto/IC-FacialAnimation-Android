package com.example.grava_arquivo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
	
        String fileName = "deuCerto.txt";//like 2016_01_12.txt


        try
        {
            File root = new File(getFilesDir(), "Report_Files.txt");
            //File root = new File(Environment.getExternalStorageDirectory(), "Notes");
            if (!root.exists())
            {
                root.mkdirs();
            }
            File gpxfile = new File(root, fileName);


            FileWriter writer = new FileWriter(gpxfile,true);
            writer.append("\nExemplo!!!\n");
            writer.flush();
            writer.close();
            Toast.makeText(this, "Data has been written to Report File\n" + getFilesDir().toString(), Toast.LENGTH_LONG).show();
        }
        catch(IOException e)
        {
            e.printStackTrace();

        }
	}
}
