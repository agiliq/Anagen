package com.agiliq.anagen;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.TreeSet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	static String TAG = "MainActivity.", inputWord;
	private TextView inputWordField;
	static TreeSet<String> wordListSet;
	private TreeSet<String> anagramsSet;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		inputWordField = (TextView) findViewById(R.id.inputWordField);
		getWordListSet();
	}
	
//	getWordListSet() accesses wordlist.txt and retrieves its contents into wordListSet.
	private void getWordListSet(){
		wordListSet = new TreeSet<String>();
		InputStream wordlistStream = getResources().openRawResource(R.raw.wordlist);
		BufferedReader wordlistReader = new BufferedReader(new InputStreamReader(wordlistStream));
        String readLine = null;
        try {
            while ((readLine = wordlistReader.readLine()) != null) {
            	wordListSet.add(readLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	public void getAnagramsButtonHandler(View getAnagramsButton) {
		String localTAG = TAG.concat("getAnagramsButtonHandler");
			Log.d(localTAG, "getAnagramsButton Pressed");
		inputWord = inputWordField.getText().toString().toLowerCase();
			Log.d(localTAG, "inputWord = " + inputWord);
			
			if(inputWord.equals("")){		//Empty Field Validation
				Toast.makeText(this, "Please enter a word", Toast.LENGTH_LONG).show();
				return;
			}

		anagramsSet=new TreeSet<String>();
		getAnagrams("", inputWord);
			Log.d(localTAG, anagramsSet.toString());
		
		ArrayList<String> anagramsList= new ArrayList<String>(anagramsSet);
			Log.d(localTAG, anagramsList.toString());
		
		Intent intent=new Intent(this, AnagramsActivity.class);
		intent.putStringArrayListExtra("com.agiliq.anagen."+TAG+"-anagrams", anagramsList);
			Log.d(localTAG, intent.toString());
		startActivity(intent);
	}

//	getAnagrams() performs permutations of the inputWord and checks each result to the wordListSet.
	private void getAnagrams(String prefix, String str) {
		int n = str.length();
		if (n == 0){
			if(wordListSet.contains(prefix)){
				anagramsSet.add(prefix);
					Log.d(TAG+"getAnagrams", prefix);
			}
		}
		else {
			for (int i = 0; i < n; i++)
				getAnagrams(prefix + str.charAt(i),	str.substring(0, i) + str.substring(i + 1, n));
		}
	}
	
	public void getAnagramsCandidateButtonHandler(View getAnagramsCandidateButton){
		String localTAG= TAG.concat("getAnagramsCandidateButtonHandler");
			Log.d(localTAG, "getAnagramsCandidateButton pressed");
		inputWord = inputWordField.getText().toString().toLowerCase();
			Log.d(localTAG, "inputWord = " + inputWord);
			
			if(inputWord.equals("")){		//Empty Field Validation
				Toast.makeText(this, "Please enter a word", Toast.LENGTH_LONG).show();
				return;
			}
		
		String joinedPhrase=inputWord.replaceAll("[^a-z]+", "");	//Eliminated special characters
			Log.d(localTAG, "joinedPhrase = " + joinedPhrase);
	}
}
