// Planning Poker++ for Android, copyright (c) Richard Everett 2011
// Full source available on GitHub at http://richev.github.com/PlanningPokerPlusPlus

package com.richev.planningpokerplusplus;

import com.richev.planningpokerplusplus.R;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

/**
 * An activity that features a menu
 * 
 * @author Rich
 */
public class MenuedActivity extends Activity
{
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.about:
                showAboutDialog();
                return true;
            case R.id.settings:
                showSettings();
                return true;
            case R.id.share:
                initiateShare();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showAboutDialog()
    {
        Intent aboutActivity = new Intent(getBaseContext(), AboutActivity.class);
        startActivity(aboutActivity);
    }

    private void showSettings()
    {
        Intent settingsActivity = new Intent(getBaseContext(), SettingsActivity.class);
        startActivity(settingsActivity);
    }

    private void initiateShare()
    {
        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.setType("text/plain");
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getResources().getString(R.string.share_subject));
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, getResources().getString(R.string.share_body));
        startActivity(emailIntent);
    }
}
