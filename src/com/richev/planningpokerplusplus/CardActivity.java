// Planning Poker++ for Android, copyright (c) Richard Everett 2011
// Full source available on Github at http://richev.github.com/PlanningPokerPlusPlus/

package com.richev.planningpokerplusplus;

import com.richev.planningpokerplusplus.R;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

/**
 * The activity for showing a card that the user has selected
 * 
 * @author Rich
 * 
 */
public class CardActivity extends MenuedActivity implements OnClickListener
{
    private String _cardValue;
    private String[] _cardValues;
    
    private static final int TEXT_SIZE_ONE_CHAR = 340;
    private static final int TEXT_SIZE_TWO_CHARS = 260;
    private static final int TEXT_SIZE_THREE_CHARS = 180;

    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    private GestureDetector _gestureDetector;
    View.OnTouchListener _gestureListener;

    /**
     * Based on code from http://stackoverflow.com/questions/937313/android-basic-gesture-detection/938657#938657
     *
     */
    class MyGestureDetector extends SimpleOnGestureListener
    {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
        {
            try
            {
                if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                {
                    return false;
                }
                
                // right to left swipe
                if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY)
                {
                    incrementCardValue();
                }
                else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY)
                {
                    decrementCardValue();
                }
            }
            catch (Exception e)
            {
                // nothing
            }
            return false;
        }

    }

    public void onClick(View v)
    {
        updateCardValue();
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card);

        _cardValues = Utils.getCardValues(getResources(), PreferenceManager.getDefaultSharedPreferences(this));        
        _cardValue = (String)getIntent().getExtras().get("cardValue"); // set in MainActivity

        _gestureDetector = new GestureDetector(new MyGestureDetector());
        _gestureListener = new View.OnTouchListener()
        {
            public boolean onTouch(View v, MotionEvent event)
            {
                return _gestureDetector.onTouchEvent(event);
            }
        };

        addListeners(R.id.cardLayout);
        addListeners(R.id.coffeeLayout);

        updateCardValue();
    }
    
    private void addListeners(int viewId)
    {
        View view = findViewById(viewId);
        view.setOnClickListener(CardActivity.this); 
        view.setOnTouchListener(_gestureListener);
    }

    private void incrementCardValue()
    {
        for (int i = 0; i < _cardValues.length; i++)
        {
            if (_cardValue.equals(_cardValues[i]))
            {
                if (i <= _cardValues.length - 1)
                {
                    _cardValue = _cardValues[i + 1];
                }
                break;
            }
        }
    }
    
    private void decrementCardValue()
    {
        for (int i = 0; i < _cardValues.length; i++)
        {
            if (_cardValue.equals(_cardValues[i]))
            {
                if (i >= 1)
                {
                    _cardValue = _cardValues[i - 1];
                }
                break;
            }
        }
    }

    private void updateCardValue()
    {
        if (_cardValue.equals("coffee"))
        {
            findViewById(R.id.cardLayout).setVisibility(View.INVISIBLE);
            findViewById(R.id.coffeeLayout).setVisibility(View.VISIBLE);
        }
        else
        {
            findViewById(R.id.cardLayout).setVisibility(View.VISIBLE);
            findViewById(R.id.coffeeLayout).setVisibility(View.INVISIBLE);

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
    
            float centerCardValueTextSize;
    
            switch (_cardValue.length())
            {
                case 1:
                    centerCardValueTextSize = TEXT_SIZE_ONE_CHAR;
                    break;
                case 2:
                    centerCardValueTextSize = TEXT_SIZE_TWO_CHARS;
                    break;
                default:
                    centerCardValueTextSize = TEXT_SIZE_THREE_CHARS;
            }
    
            ((TextView)findViewById(R.id.cardValueCenter)).setTextSize(TypedValue.COMPLEX_UNIT_DIP, centerCardValueTextSize);
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();

        // The settings dialog may have been invoked, in which case this card
        // value may no longer be allowed.
        _cardValues = Utils.getCardValues(getResources(), PreferenceManager.getDefaultSharedPreferences(this));

        Boolean cardValueAllowed = false;

        for (int i = 0; i < _cardValues.length; i++)
        {
            if (_cardValues[i].equals(_cardValue))
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
