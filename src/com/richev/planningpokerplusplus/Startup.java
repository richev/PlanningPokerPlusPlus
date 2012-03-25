// Planning Poker++ for Android, copyright (c) Richard Everett 2012
// Full source available on GitHub at http://richev.github.com/PlanningPokerPlusPlus

package com.richev.planningpokerplusplus;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager.NameNotFoundException;
import android.preference.PreferenceManager;

/**
 * Useful application startup-related items
 * @author Rich
 *
 */
public class Startup
{
    private Activity _activity;
    
    public Startup(Activity activity)
    {
        _activity = activity;
    }
    
    /**
     * Whether this version of this application is being run on this device for the first time
     * @return
     */
    public Boolean getHasRun()
    {
        return PreferenceManager.getDefaultSharedPreferences(_activity).getBoolean("hasRun" + getVersionCode(), false);
    }
    
    /**
     * Sets that this version of this application has been run on this device
     */
    public void setHasRun()
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(_activity);
        Editor editor = prefs.edit();
        editor.putBoolean("hasRun" + getVersionCode(), true);
        editor.commit();
    }

    private int getVersionCode()
    {
        int versionCode = 0;
        
        try
        {
            versionCode = _activity.getPackageManager().getPackageInfo(
                    _activity.getApplicationInfo().packageName, 0).versionCode;
        }
        catch (NameNotFoundException ex)
        {
            // Required otherwise the compiler complains
        }

        return versionCode;
    } 
}
