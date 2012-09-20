package com.agiliq.anagen;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.TreeSet;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;

public class MainActivity extends Activity {

	static String TAG = "MainActivity.", inputWord;
	Integer maximumWords, minimumCharacters;
	private EditText inputWordField;
	private Spinner max_words;
	private Spinner min_characters;
	static TreeSet<String> wordListSet;
	static TreeSet<String> anagramsSet;
	
	static ProgressDialog waiting;
	static Object main_activity_object;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		main_activity_object=this;

		inputWordField = (EditText) findViewById(R.id.inputWordField);
		max_words= (Spinner) findViewById(R.id.maximum_words);
			max_words.setSelection(1);
		min_characters= (Spinner) findViewById(R.id.minimum_characters);
			min_characters.setSelection(1);
			
		inputWordField.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(inputWordField.getWindowToken(), 0);
				return true;
			}
		});
		
		getWordListSet();
	}
	
	public void onStart()
	{
		super.onStart();
		FlurryAgent.onStartSession(this, "KZDW2M8ZY79VFHSWH69V");
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
	
	public void getAnagramsButtonHandler(View getAnagramsCandidateButton){
		
		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(inputWordField.getWindowToken(), 0);
		
		String localTAG= TAG.concat("getAnagramsCandidateButtonHandler");
			Log.d(localTAG, "getAnagramsCandidateButton pressed");
		
		final MainActivityBackgroundProcessing bg= new MainActivityBackgroundProcessing();
			
		waiting=new ProgressDialog(this);
		waiting.setIndeterminate(true);
		waiting.setMessage("Finding Anagrams...");
		waiting.setOnCancelListener(new OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
				bg.cancel(true);
			}
		});
		
		inputWord = inputWordField.getText().toString().toLowerCase();
			Log.d(localTAG, "inputWord = " + inputWord);
			
			if(inputWord.equals("")){		//Empty Field Validation
				Toast.makeText(this, "Please enter a word", Toast.LENGTH_LONG).show();
				return;
			}
			
		try{
			maximumWords= Integer.parseInt(max_words.getSelectedItem().toString());
				Log.d(localTAG+".maximumWords", maximumWords.toString());
			minimumCharacters= Integer.parseInt(min_characters.getSelectedItem().toString());
				Log.d(localTAG+".minimumCharacters", minimumCharacters.toString());
		} catch(NumberFormatException e){
			Toast.makeText(this, "Select a value for the characters and words", Toast.LENGTH_LONG).show();
			return;
		}
		
		bg.execute(inputWord, maximumWords, minimumCharacters);
	}
	
	void doIntent(){
		String localTAG= TAG.concat("getAnagramsCandidateButtonHandler");
		ArrayList<String> anagramsList= new ArrayList<String>(anagramsSet);
		Log.d(localTAG, anagramsList.toString());
		Intent intent=new Intent(this, AnagramsActivity.class);
		intent.putStringArrayListExtra("com.agiliq.anagen."+TAG+"-anagrams", anagramsList);
		intent.putExtra("com.agiliq.anagen."+TAG+"-inputword", inputWord);
		intent.putExtra("com.agiliq.anagen."+TAG+"-maximumWords",maximumWords);
		intent.putExtra("com.agiliq.anagen."+TAG+"-minimumCharacters",minimumCharacters);
			Log.d(localTAG, intent.toString());
		startActivity(intent);
	}
	
	public void gotoHelpActivity(View v){
		Intent i= new Intent(this, HelpActivity.class);
		startActivity(i);
	}
	
	public void onStop()
	{
		super.onStop();
		FlurryAgent.onEndSession(this);
	}
}