package com.agiliq.anagen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class AnagramsActivity extends Activity {

	private static String TAG = "AnagramsActivity.";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anagrams);
        
        String localTAG = TAG.concat("onCreate");
        
        Intent intent=getIntent();
        	Log.d(localTAG, intent.toString());
    }
}