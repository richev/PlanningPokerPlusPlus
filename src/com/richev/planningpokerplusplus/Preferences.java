// Planning Poker++ for Android, copyright (c) Richard Everett 2011
// Full source available on GitHub at http://richev.github.com/PlanningPokerPlusPlus

package com.richev.planningpokerplusplus;

import android.content.SharedPreferences;

/**
 * Wrapper class for strongly-typed access to SharedPreferences
 * @author Rich
 *
 */
public class Preferences
{
    private final SharedPreferences _prefs;
    
    /**
     * Wrapper class for strongly-typed access to SharedPreferences
     * @param prefs use PreferenceManager.getDefaultSharedPreferences(this)
     */
    public Preferences(SharedPreferences prefs)
    {
        _prefs = prefs;
    }
    
    public boolean getIncludeZero()
    {
        return _prefs.getBoolean("includeZero", true);
    }

    public boolean getIncludeHalf()
    {
        return _prefs.getBoolean("includeHalf", true);
    }

    public boolean getIncludeInfinity()
    {
        return _prefs.getBoolean("includeInfinity", true);
    }

    public boolean getIncludeUnknown()
    {
        return _prefs.getBoolean("includeUnknown", true);
    }

    public boolean getIncludeCoffee()
    {
        return _prefs.getBoolean("includeCoffee", true);
    }
    
    public String getCardSequenceName()
    {
        return _prefs.getString("cardSequence", "fibonacci21");
    }
}
