package com.agiliq.anagen;

import com.flurry.android.FlurryAgent;

import android.app.Activity;
import android.os.Bundle;

public class HelpActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
    }
    
    public void onStart()
	{
		super.onStart();
		FlurryAgent.onStartSession(this, "KZDW2M8ZY79VFHSWH69V");
	}
    
    public void onStop()
	{
		super.onStop();
		FlurryAgent.onEndSession(this);
	}
}
