// Planning Poker++ for Android, copyright (c) Richard Everett 2012
// Full source available on GitHub at http://richev.github.com/PlanningPokerPlusPlus

package com.richev.planningpokerplusplus;

import com.richev.planningpokerplusplus.R;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * For the settings page. Automatically refreshes the buttons on MainActivity when this activity ends (pauses)
 * 
 * @author Rich
 * 
 */
public class SettingsActivity extends PreferenceActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        if (mainActivity != null) // It should never be null, but check just to be sure
        {
            // In case card settings have changed, refresh them
            mainActivity.showCardButtons();
        }
    }

    public static MainActivity mainActivity;
}