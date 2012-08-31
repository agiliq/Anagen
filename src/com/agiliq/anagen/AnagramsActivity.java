package com.agiliq.anagen;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class AnagramsActivity extends Activity {

	private static String TAG = "AnagramsActivity.";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anagrams);
        
        String localTAG = TAG.concat("onCreate");
        
        Intent intent=getIntent();
        	Log.d(localTAG, intent.toString());
        	
        ArrayList<String> recievedAnagrams=intent.getStringArrayListExtra("MainActivity.-anagrams");
        	Log.d(localTAG+"-recievedAnagrams", recievedAnagrams.toString());
        
        ArrayAdapter<String> anagramsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, recievedAnagrams);
        	Log.d(localTAG+"-anagramsAdapter", anagramsAdapter.toString());
        ListView anagramsList=(ListView)findViewById(R.id.anagramslist);
        	anagramsList.setAdapter(anagramsAdapter);
    }
}