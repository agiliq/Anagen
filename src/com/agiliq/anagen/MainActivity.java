package com.agiliq.anagen;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity {
	private static String TAG = "MainActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    public void getAnagramsButtonHandler(View getAnagramsButton){
    	TAG= TAG.concat(".getAnagramsButtonHandler");
    	Log.d(TAG, "getAnagramsButton Pressed");
    }
}