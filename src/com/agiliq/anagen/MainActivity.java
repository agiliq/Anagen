package com.agiliq.anagen;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;
import java.util.TreeSet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	static String TAG = "MainActivity.", inputWord;
	private TextView inputWordField;
	private Spinner max_words;
	private Spinner min_characters;
	static TreeSet<String> wordListSet;
	private TreeSet<String> anagramsSet, permutationSet;
	private Iterator<String> itr;
	Stack<String> correct;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		inputWordField = (TextView) findViewById(R.id.inputWordField);
		max_words= (Spinner) findViewById(R.id.maximum_words);
		min_characters= (Spinner) findViewById(R.id.minimum_characters);
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
			
		String phrase=inputWord.replaceAll("[^a-z ]+", "");	//Eliminated special characters except space
			Log.d(localTAG, "phrase = " + phrase);

		anagramsSet=new TreeSet<String>();
		getAnagrams("", phrase);
			Log.d(localTAG, anagramsSet.toString());
		
		ArrayList<String> anagramsList= new ArrayList<String>(anagramsSet);
			Log.d(localTAG, anagramsList.toString());
		
		Intent intent=new Intent(this, AnagramsActivity.class);
		intent.putStringArrayListExtra("com.agiliq.anagen."+TAG+"-anagrams", anagramsList);
			Log.d(localTAG, intent.toString());
		startActivity(intent);
	}

//	getAnagrams() performs permutations of the phrase and checks each result to the wordListSet.
	private void getAnagrams(String prefix, String str) {
		int n = str.length();
		if (n == 0){
			if(wordListSet.contains(prefix)){
				anagramsSet.add(prefix);
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
		
		try{
			Integer maximumWords= Integer.parseInt(max_words.getSelectedItem().toString());
				Log.d(localTAG+".maximumWords", maximumWords.toString());
			Integer minimumCharacters= Integer.parseInt(min_characters.getSelectedItem().toString());
				Log.d(localTAG+".minimumCharacters", minimumCharacters.toString());
		} catch(NumberFormatException e){
			Toast.makeText(this, "Select a value for the characters and words", Toast.LENGTH_LONG).show();
			return;
		}
		
		String joinedPhrase=inputWord.replaceAll("[^a-z]+", "");	//Eliminated special characters
			Log.d(localTAG, "joinedPhrase = " + joinedPhrase);
		
		permutationSet= new TreeSet<String>();
		getAllPermutations("", joinedPhrase);
			Log.d(localTAG, permutationSet.toString());
		
		String poppedoutString;
		int length_of_stack_contents;
		Stack<String> temp;
		itr=permutationSet.iterator();
		while(itr.hasNext()){
			correct=new Stack<String>();
			length_of_stack_contents=0;
			getCandidate(itr.next());
//				Log.d(localTAG+"-stack", correct.toString());
			temp=new Stack<String>();
			while(!correct.empty()){
				poppedoutString=correct.pop();
				length_of_stack_contents+=poppedoutString.length();
				temp.push(poppedoutString);
			}
			if(length_of_stack_contents==joinedPhrase.length()){
				Log.d(localTAG+"-stack", temp.toString());
			}
		}
	}
	
//	getAllPermutations() performs permutations of the joinedPhrase.
	private void getAllPermutations(String prefix, String str) {
		int n = str.length();
		if (n == 0){
			permutationSet.add(prefix);
		}
		else {
			for (int i = 0; i < n; i++)
				getAllPermutations(prefix + str.charAt(i),	str.substring(0, i) + str.substring(i + 1, n));
		}
	}
	
	private void getCandidate(String str){
		String first, last=null;
		int stringLength=str.length();
		
		for(int i=0; i<str.length(); i++, stringLength--){
			first=str.substring(0, stringLength);
			if(stringLength!=str.length())
				last=str.substring(stringLength);
			
			if(wordListSet.contains(first)){
				correct.push(first);
				if(stringLength!=str.length())
					getCandidate(last);
				break;
			}
		}
	}
}