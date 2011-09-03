// Planning Poker++ for Android, copyright © Richard Everett 2011
// Full source available on Github at http://richev.github.com/PlanningPokerPlusPlus/

package com.richev.planningpokerplusplus;

import com.richev.planningpokerplusplus.R;

import android.os.Bundle;
import android.text.util.Linkify;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.app.Activity;
import android.content.pm.PackageManager.NameNotFoundException;

/**
 * An about dialog for the app
 * @author Rich
 *
 */
public class AboutActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.about);
	    
	    linkifyTextView(R.id.feedback, Linkify.EMAIL_ADDRESSES);
	    linkifyTextView(R.id.planningPoker, Linkify.WEB_URLS);
	    linkifyTextView(R.id.iconCredits, Linkify.WEB_URLS);

	    
	    this.setTitle(this.getTitle() + " v" + getVersionName());
	    
        final Button button = (Button)findViewById(R.id.closeButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
        		finish();
            }
        });
	}
	
	private void linkifyTextView(int textViewId, int mask)
	{
	    TextView textView = (TextView)findViewById(textViewId);
	    Linkify.addLinks(textView, mask);
	}
	
	private String getVersionName()
	{
	    String versionName = "";
	    try
	    {
	    	versionName = this.getPackageManager().getPackageInfo(getApplicationInfo().packageName, 0).versionName;
	    }
	    catch (NameNotFoundException ex)
	    {
	    	// Required otherwise the compiler complains
	    }
	    
	    return versionName;
	}
}
