package com.agiliq.anagen;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Set;
import java.util.TreeSet;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {

	private static String TAG = "MainActivity.", inputWord;
	private TextView inputWordField;
	private Set<String> wordListSet;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		inputWordField = (TextView) findViewById(R.id.inputWordField);
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
		
		getWordListSet();
        	Log.d("wordlist", wordListSet.toString());

		getAnagrams("", inputWord);
	}
	
//	getWordListSet() accesses wordlist.txt and retrieves its contents into wordListSet.
	private void getWordListSet(){
		wordListSet = new TreeSet<String>();
		InputStream is = getResources().openRawResource(R.raw.wordlist);
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String readLine = null;
        try {
            while ((readLine = br.readLine()) != null) {
            	wordListSet.add(readLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

//	getAnagrams() performs permutations of the inputWord and checks each result to the wordListSet.
	private void getAnagrams(String prefix, String str) {
		int n = str.length();
		String localTAG=TAG.concat("permute");
		if (n == 0){
			if(wordListSet.contains(prefix)){
				Log.d(localTAG, prefix);
			}
		}
		else {
			for (int i = 0; i < n; i++)
				getAnagrams(prefix + str.charAt(i),	str.substring(0, i) + str.substring(i + 1, n));
		}
	}
}
