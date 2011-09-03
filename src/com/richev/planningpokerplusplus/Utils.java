package com.richev.planningpokerplusplus;

import java.util.ArrayList;
import java.util.Collections;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Canvas;

/**
 * General static utility functions
 * @author Rich
 *
 */
public class Utils {
	/**
	 * Gets the sequence of card values, based on user settings
	 * @param res use getResources()
	 * @param prefs use PreferenceManager.getDefaultSharedPreferences(this)
	 */
    public static String[] getCardValues(Resources res, SharedPreferences prefs) {
        ArrayList<String> cardValues = new ArrayList<String>();
        
        if (prefs.getBoolean("includeZero", true)) {
        	cardValues.add("0");
        }
        
        if (prefs.getBoolean("includeHalf", true)) {
        	cardValues.add("½");
        }
        
        cardValues.addAll(getCardSequence(res, prefs));
    	
        if (prefs.getBoolean("includeInfinity", true)) {
        	cardValues.add("∞");
        }
        
        if (prefs.getBoolean("includeUnknown", true)) {
        	cardValues.add("?");
        }
        
        if (prefs.getBoolean("includeCoffee", true)) {
        	cardValues.add("coffee");
        }
        
        return cardValues.toArray(new String[cardValues.size()]);
    }
    
    private static ArrayList<String> getCardSequence(Resources res, SharedPreferences prefs)
    {
        String[] cardSequenceNames = res.getStringArray(R.array.cardSequenceNames);
        String[] cardSequenceValues = res.getStringArray(R.array.cardSequenceValues);
        String cardSequenceName = prefs.getString("cardSequence", "fibonacci21");
        
        String cardSequenceValue = cardSequenceValues[1]; // set to a default value (fibonacci21), in case we don't find one
        
        for (int i = 0; i < cardSequenceNames.length; i++)
        {
        	if (cardSequenceNames[i].equals(cardSequenceName))
        	{
        		cardSequenceValue = cardSequenceValues[i];
        		break;
        	}
        }
        
        // cardSequenceValue is a space-delimited string containing the actual values to use
        ArrayList<String> cardSequenceValueList = new ArrayList<String>();
        Collections.addAll(cardSequenceValueList, cardSequenceValue.split(" ")); 
        
        return cardSequenceValueList;
    }
    
   
    /**
     * Rotates a canvas by 180 degrees
     */
    public static void RotateCanvasUpsideDown(android.view.View view, Canvas canvas)
    {
        //This saves off the matrix that the canvas applies to draws, so it can be restored later. 
        canvas.save(); 

        //now we change the matrix
        //We need to rotate around the centre of our text
        //Otherwise it rotates around the origin, and that's bad. 
        float py = view.getHeight()/2.0f;
        float px = view.getWidth()/2.0f;
        canvas.rotate(180, px, py); 
    }
}
