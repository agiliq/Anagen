package com.agiliq.anagen;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;

public class AnagramsActivity extends Activity {

	private static String TAG = "AnagramsActivity.", inputWord;
	Integer maximumWords, minimumCharacters;
	private EditText inputWordField;
	private Spinner max_words;
	private Spinner min_characters;
	private ListView anagramsList;
	static TreeSet<String> anagramsSet;

	static ProgressDialog waiting;
	static Object anagrams_activity_object;
	boolean timercancel=false;
			
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anagrams);
        
        anagrams_activity_object=this;
        
        String localTAG = TAG.concat("onCreate");
        
        inputWordField = (EditText) findViewById(R.id.inputWordField);
        max_words= (Spinner) findViewById(R.id.maximum_words);
        min_characters= (Spinner) findViewById(R.id.minimum_characters);
        
        anagramsList=(ListView)findViewById(R.id.anagramslist);
	        TextView listViewHeader=new TextView(this);
	        listViewHeader.setText("  Anagrams:");
        anagramsList.addHeaderView(listViewHeader);
        
        Intent intent=getIntent();
        	Log.d(localTAG, intent.toString());
    	inputWordField.setText(intent.getStringExtra("com.agiliq.anagen."+MainActivity.TAG+"-inputword"));
    	max_words.setSelection(intent.getIntExtra("com.agiliq.anagen."+MainActivity.TAG+"-maximumWords", 2) - 1);
    	min_characters.setSelection(intent.getIntExtra("com.agiliq.anagen."+MainActivity.TAG+"-minimumCharacters", 2) - 1);
        ArrayList<String> recievedAnagrams=intent.getStringArrayListExtra("com.agiliq.anagen."+MainActivity.TAG+"-anagrams");
        	Log.d(localTAG+"-recievedAnagrams", recievedAnagrams.toString());
        
        ArrayAdapter<String> anagramsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, recievedAnagrams);
        	Log.d(localTAG+"-anagramsAdapter", anagramsAdapter.toString());
        
    	anagramsList.setAdapter(anagramsAdapter);
    	
    	inputWordField.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(inputWordField.getWindowToken(), 0);
				return true;
			}
		});
    	
    	InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(inputWordField.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}
    
    public void onStart()
	{
		super.onStart();
		FlurryAgent.onStartSession(this, "KZDW2M8ZY79VFHSWH69V");
	}
	
	public void getAnagramsButtonHandler(View getAnagramsCandidateButton){
		
		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(inputWordField.getWindowToken(), 0);
		
		final String localTAG= TAG.concat("getAnagramsCandidateButtonHandler");
			Log.d(localTAG, "getAnagramsCandidateButton pressed");
			
		final AnagramsActivityBackgroundProcessing bg= new AnagramsActivityBackgroundProcessing();
		
		waiting=new ProgressDialog(this);
		waiting.setIndeterminate(true);
		waiting.setMessage("Finding Anagrams...");
		waiting.setOnCancelListener(new OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
				bg.cancel(true);
				if(timercancel){
					Toast.makeText((Context) anagrams_activity_object, "The calculation for anagrams took too long. Please try with a smaller phrase.", Toast.LENGTH_LONG).show();
					Log.d(localTAG+"onCancel", "toast");
					timercancel=false;
				}
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
		
//		This timer task is scheduled for one minute after the getAnagramsButton is pressed and then checks is the ProgressDialog is still active, if it is then
//		cancel() is called on it.
		class WaitForSomeTime extends TimerTask{
			@Override
			public void run() {
				Log.d("timertask", "timer out");
				if(waiting.isShowing()){
					Log.d("timertask", "cancelling");
					timercancel=true;
					waiting.cancel();
				}
			}
		}
		Timer timer= new Timer();       Log.d("timer", timer.toString());
		timer.schedule(new WaitForSomeTime(), 60000);   Log.d("timer", timer.toString());
	}
	
	void change(){
		String localTAG= TAG.concat("change");
		
		ArrayList<String> newAnagrams= new ArrayList<String>(anagramsSet);
			Log.d(localTAG, newAnagrams.toString());
	
		ArrayAdapter<String> anagramsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, newAnagrams);
			Log.d(localTAG+"-anagramsAdapter", anagramsAdapter.toString());
			
		if(anagramsAdapter.isEmpty()){
			anagramsList.setVisibility(View.INVISIBLE);
			Toast.makeText(this, "Sorry. No Anagrams found.", Toast.LENGTH_LONG).show();
		} else{
			anagramsList.setVisibility(View.VISIBLE);
			anagramsList.setAdapter(anagramsAdapter);
		}
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