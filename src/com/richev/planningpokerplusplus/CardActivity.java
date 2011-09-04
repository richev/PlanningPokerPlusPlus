// Planning Poker++ for Android, copyright � Richard Everett 2011
// Full source available on Github at http://richev.github.com/PlanningPokerPlusPlus/

package com.richev.planningpokerplusplus;

import com.richev.planningpokerplusplus.R;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.TypedValue;
import android.widget.TextView;

/**
 * The activity for showing a card that the user has selected
 * 
 * @author Rich
 * 
 */
public class CardActivity extends MenuedActivity
{
    private String _cardValue;

    private final int TEXT_SIZE_ONE_CHAR = 340;
    private final int TEXT_SIZE_TWO_CHARS = 260;
    private final int TEXT_SIZE_THREE_CHARS = 180;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card);

        _cardValue = (String) getIntent().getExtras().get("cardValue"); // set in MainActivity

        TextView[] cardValueViews =
        {
            (TextView)findViewById(R.id.cardValueTopLeft),
            (TextView)findViewById(R.id.cardValueTopRight),
            (TextView)findViewById(R.id.cardValueCenter),
            (TextView)findViewById(R.id.cardValueBottomLeft),
            (TextView)findViewById(R.id.cardValueBottomRight)
        };

        for (int i = 0; i < cardValueViews.length; i++)
        {
            cardValueViews[i].setText(_cardValue);
        }

        float centerCardValueTextSize = _cardValue.length() == 1 ? TEXT_SIZE_ONE_CHAR : _cardValue.length() == 2 ? TEXT_SIZE_TWO_CHARS : TEXT_SIZE_THREE_CHARS;
        ((TextView)findViewById(R.id.cardValueCenter)).setTextSize(TypedValue.COMPLEX_UNIT_DIP, centerCardValueTextSize);
    }

    @Override
    public void onResume()
    {
        super.onResume();

        // The settings dialog may have been invoked, in which case this card
        // value may no longer be allowed.
        String[] cardValues = Utils.getCardValues(getResources(), PreferenceManager.getDefaultSharedPreferences(this));

        Boolean cardValueAllowed = false;

        for (int i = 0; i < cardValues.length; i++)
        {
            if (cardValues[i].equals(_cardValue))
            {
                cardValueAllowed = true;
                break;
            }
        }

        if (!cardValueAllowed)
        {
            // Close this activity, which will return us to the main activity
            this.finish();
        }
    }
}
