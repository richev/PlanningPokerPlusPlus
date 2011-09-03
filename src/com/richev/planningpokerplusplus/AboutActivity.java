package com.richev.planningpokerplusplus;

import com.richev.planningpokerplusplus.R;

import android.os.Bundle;
import android.text.util.Linkify;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.app.Activity;

public class AboutActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.about);
	    
	    TextView feedbackText = (TextView)findViewById(R.id.feedback);
	    Linkify.addLinks(feedbackText, Linkify.EMAIL_ADDRESSES);
	    
	    TextView planningPokerText = (TextView)findViewById(R.id.planningPoker);
	    Linkify.addLinks(planningPokerText, Linkify.WEB_URLS);
	    
	    TextView iconCreditsText = (TextView)findViewById(R.id.iconCredits);
	    Linkify.addLinks(iconCreditsText, Linkify.WEB_URLS);
	    
	    //PackageInfo manager = getPackageManager().getPackageInfo(getPackageName(), 0);
	    //this.setTitle(this.getTitle() + "v" + manager.versionName);
	    
        final Button button = (Button)findViewById(R.id.closeButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
        		finish();
            }
        });
	}
}
