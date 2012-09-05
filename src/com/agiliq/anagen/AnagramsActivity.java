package com.agiliq.anagen;

import java.util.ArrayList;
import java.util.TreeSet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class AnagramsActivity extends Activity {

	private static String TAG = "AnagramsActivity.", inputWord;
	private TextView inputWordField;
	private ListView anagramsList;
	private TreeSet<String> anagramsSet;
		
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anagrams);
        
        String localTAG = TAG.concat("onCreate");
        
        inputWordField = (TextView) findViewById(R.id.inputWordField);
        
        anagramsList=(ListView)findViewById(R.id.anagramslist);
	        TextView listViewHeader=new TextView(this);
	        listViewHeader.setText("  Anagrams:");
        anagramsList.addHeaderView(listViewHeader);
        
        Intent intent=getIntent();
        	Log.d(localTAG, intent.toString());
        ArrayList<String> recievedAnagrams=intent.getStringArrayListExtra("com.agiliq.anagen."+MainActivity.TAG+"-anagrams");
        	Log.d(localTAG+"-recievedAnagrams", recievedAnagrams.toString());
        
        ArrayAdapter<String> anagramsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, recievedAnagrams);
        	Log.d(localTAG+"-anagramsAdapter", anagramsAdapter.toString());
        
    	anagramsList.setAdapter(anagramsAdapter);
    }
    
    public void getAnagramsButtonHandler(View getAnagramsButton){
    	String localTAG = TAG.concat("getAnagramsButtonHandler");
			Log.d(localTAG, "getAnagramsButton Pressed");
		inputWord = inputWordField.getText().toString().toLowerCase();
			Log.d(localTAG, "inputWord = " + inputWord);
			
			if(inputWord.equals("")){		//Empty Field Validation
				Toast.makeText(this, "Please enter a word", Toast.LENGTH_LONG).show();
				return;
			}
			
		String joinedPhrase=inputWord.replaceAll("[^a-z ]+", "");	//Eliminated special characters except space
			Log.d(localTAG, "joinedPhrase = " + joinedPhrase);
			
		anagramsSet=new TreeSet<String>();
		getAnagrams("", joinedPhrase);
			Log.d(localTAG, anagramsSet.toString());
			
		ArrayList<String> newAnagrams= new ArrayList<String>(anagramsSet);
			Log.d(localTAG, newAnagrams.toString());
			
		ArrayAdapter<String> anagramsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, newAnagrams);
        	Log.d(localTAG+"-anagramsAdapter", anagramsAdapter.toString());
        
    	anagramsList.setAdapter(anagramsAdapter);
    }
    
//	getAnagrams() performs permutations of the inputWord and checks each result to the wordListSet.
	private void getAnagrams(String prefix, String str) {
		int n = str.length();
		if (n == 0){
			if(MainActivity.wordListSet.contains(prefix)){
				anagramsSet.add(prefix);
					Log.d(TAG+"getAnagrams", prefix);
			}
		}
		else {
			for (int i = 0; i < n; i++)
				getAnagrams(prefix + str.charAt(i),	str.substring(0, i) + str.substring(i + 1, n));
		}
	}
}