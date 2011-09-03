package com.richev.planningpokerplusplus;

import com.richev.planningpokerplusplus.R;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * The main entry point activity for the application, which shows a list of card value buttons
 * @author Rich
 *
 */
public class MainActivity extends MenuedActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        showCardButtons();
        
    	SettingsActivity.mainActivity = this;
    }
    
    /**
     * Displays the buttons on the activity.
     * Needs to be called when the activity is first started, and also if the buttons ever change
     * (due to user settings changes invoked from SettingsActivity, for example)
     */
    public void showCardButtons() {
        final LinearLayout buttonsLayout = (LinearLayout)findViewById(R.id.buttonsLayout);
        
        String[] cardValues = Utils.getCardValues(getResources(), PreferenceManager.getDefaultSharedPreferences(this));
        
        buttonsLayout.removeAllViews();
        
        for (int i = 0; i < cardValues.length; i++)
        {
        	Button button = new Button(this);
        	button.setText(cardValues[i]);
        	button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
        	button.setPadding(0, 0, 0, 2);
        	button.setOnClickListener(new OnCardSelectListener(cardValues[i]) {
                public void onClick(View v) {
                	Intent myIntent = new Intent(v.getContext(), this.getCardValue() == "coffee" ? CoffeeCardActivity.class : CardActivity.class);
                	myIntent.putExtra("cardValue", this.getCardValue()); // this will be read in CardActivity
                    startActivityForResult(myIntent, 0);
                }
            });
        	
        	buttonsLayout.addView(button);
        }
    }
}