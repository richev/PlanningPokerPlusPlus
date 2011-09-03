package com.richev.planningpokerplusplus;

import android.os.Bundle;
import android.preference.PreferenceManager;

/**
 * The activity for showing the coffee card (similar to CardActivity)
 * @author Rich
 *
 */
public class CoffeeCardActivity extends MenuedActivity {
	private final String _cardValue = "coffee";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.coffeecard);
	}
	
	@Override
	public void onResume() {
		super.onResume();

		// The settings dialog may have been invoked, in which case this card
		// value may no longer be allowed.
        String[] cardValues = Utils.getCardValues(getResources(), PreferenceManager.getDefaultSharedPreferences(this));
        
        Boolean cardValueAllowed = false;
        
        for (int i = 0; i < cardValues.length; i++) {
        	if (cardValues[i].equals(_cardValue)) {
        		cardValueAllowed = true;
        		break;
        	}
        }
        
        if (!cardValueAllowed) {
        	// Close this activity, which will return us to the main activity 
        	this.finish();
        }
	}
}
