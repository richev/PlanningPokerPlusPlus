// Planning Poker++ for Android, copyright (c) Richard Everett 2012
// Full source available on GitHub at http://richev.github.com/PlanningPokerPlusPlus

package com.richev.planningpokerplusplus;

import com.richev.planningpokerplusplus.R;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.util.TypedValue;
import android.view.Display;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
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
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    private GestureDetector _gestureDetector;
    private View.OnTouchListener _gestureListener;

    private static final float CARD_SHADOW_LIGHTEST = 1f;
    private static final float CARD_SHADOW_DARKEST_HIDING = 0.25f;
    private static final float CARD_SHADOW_DARKEST_SHOWING = 0f;

    private static final int CARD_ANIMATION_DURATION = 400;
    
    private Preferences _prefs;

    private enum CardView
    {
        /**
         * The card view used for normally displaying the card
         */
        Static,
        
        /**
         * The card view used for showing the animated card transition
         */
        Animated
    }
    
    private enum CardTransition
    {
        Increment,
        Decrement
    }
    
    /**
     * Based on code from http://stackoverflow.com/questions/937313/android-basic-gesture-detection/938657#938657
     *
     */
    private class MyGestureDetector extends SimpleOnGestureListener
    {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
        {
            if (!getCardBackShown() && (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE) && (Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY))
            {
                // right to left swipe
                decrementCard();
            }
            else if (!getCardBackShown() && (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE) && (Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY))
            {
                // left to right swipe
                incrementCard();
            }
            else if ((e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE) && (Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY))
            {
                // vertical swipe
                toggleCardBack();
            }
            return false;
        }
    }

    public void onClick(View v)
    {
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card);

        _prefs = new Preferences(PreferenceManager.getDefaultSharedPreferences(this));
        
        _cardValues = Utils.getCardValues(getResources(), _prefs);        
        _cardValue = (String)getIntent().getExtras().get("cardValue"); // set in MainActivity

        _gestureDetector = new GestureDetector(new MyGestureDetector());
        _gestureListener = new View.OnTouchListener()
        {
            public boolean onTouch(View v, MotionEvent event)
            {
                return _gestureDetector.onTouchEvent(event);
            }
        };

        View cardContainer = findViewById(R.id.cardContainer);
        cardContainer.setOnClickListener(CardActivity.this); 
        cardContainer.setOnTouchListener(_gestureListener);

        findViewById(R.id.coffeeLayout).setVisibility(View.INVISIBLE); // temporary
        
        refreshCard(CardView.Static);
    }

    @Override
    public void onResume()
    {
        super.onResume();

        // The settings dialog may have been invoked...
        
        // This card value may no longer be allowed.
        _cardValues = Utils.getCardValues(getResources(), _prefs);

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
    
    private void animateCard(CardTransition cardTransition)
    {
        Display display = getWindowManager().getDefaultDisplay(); 
        final int pivotX = display.getWidth() / 2;
        final int pivotY = display.getHeight() + (display.getHeight() / 2); // somewhere off the bottom of the screen        
        final int cardMaxAngle = 60; // so the card starts/ends off-screen

        AlphaAnimation alphaAnimation;
        RotateAnimation rotateAnimation;
        
        if (cardTransition == CardTransition.Increment)
        {
            alphaAnimation = new AlphaAnimation(CARD_SHADOW_DARKEST_SHOWING, CARD_SHADOW_LIGHTEST);
            rotateAnimation = new RotateAnimation(0, cardMaxAngle, pivotX, pivotY);
        }
        else
        {
            alphaAnimation = new AlphaAnimation(CARD_SHADOW_LIGHTEST, CARD_SHADOW_DARKEST_HIDING);
            rotateAnimation = new RotateAnimation(cardMaxAngle, 0, pivotX, pivotY);
        }
        
        rotateAnimation.setDuration(CARD_ANIMATION_DURATION);
        alphaAnimation.setDuration(CARD_ANIMATION_DURATION);

        rotateAnimation.setAnimationListener(new AnimationListener()
        {
            public void onAnimationEnd(Animation animation)
            {
                refreshCard(CardView.Static);
            }
            public void onAnimationRepeat(Animation animation) {}
            public void onAnimationStart(Animation animation) {}
        });
        
        View animatedCard = findViewById(R.id.cardLayoutAni);
        animatedCard.setVisibility(View.VISIBLE);
        animatedCard.bringToFront();

        getCurrentCardView().startAnimation(alphaAnimation);

        animatedCard.startAnimation(rotateAnimation);
    }
    
    private boolean getCardBackShown()
    {
        return findViewById(R.id.backLayout).getVisibility() == View.VISIBLE;
    }
    
    private void toggleCardBack()
    {
        View backLayout = findViewById(R.id.backLayout);
        Display display = getWindowManager().getDefaultDisplay();
        
        AlphaAnimation alphaAnimation;
        TranslateAnimation cardBackAnimation;
        
        if (getCardBackShown())
        {
            alphaAnimation = new AlphaAnimation(CARD_SHADOW_DARKEST_SHOWING, CARD_SHADOW_LIGHTEST);
            cardBackAnimation = new TranslateAnimation(0, 0, 0, -display.getHeight());
            cardBackAnimation.setAnimationListener(new AnimationListener()
            {
                public void onAnimationEnd(Animation animation)
                {
                    findViewById(R.id.backLayout).setVisibility(View.INVISIBLE);
                }
                public void onAnimationRepeat(Animation animation) {}
                public void onAnimationStart(Animation animation) {}
            });    
            findViewById(R.id.cardContainer).setKeepScreenOn(true);
        }
        else
        {
            alphaAnimation = new AlphaAnimation(CARD_SHADOW_LIGHTEST, CARD_SHADOW_DARKEST_HIDING);
            cardBackAnimation = new TranslateAnimation(0, 0, display.getHeight(), 0);
            
            backLayout.setVisibility(View.VISIBLE);
            backLayout.bringToFront();
            findViewById(R.id.cardContainer).setKeepScreenOn(false);
        }
        
        alphaAnimation.setDuration(CARD_ANIMATION_DURATION);
        cardBackAnimation.setDuration(CARD_ANIMATION_DURATION);

        getCurrentCardView().startAnimation(alphaAnimation);
        backLayout.startAnimation(cardBackAnimation);
    }

    private void incrementCard()
    {
        for (int i = 0; i < _cardValues.length; i++)
        {
            if (_cardValue.equals(_cardValues[i]))
            {
                int nextCard = i + 1;
                
                if (nextCard <= _cardValues.length - 1)
                {
                    refreshCard(CardView.Animated);
                    _cardValue = _cardValues[nextCard];
                    refreshCard(CardView.Static);
                    animateCard(CardTransition.Increment);
                }
                else
                {
                    vibrate(); // to hint to the user that there is no lower card
                }
                break;
            }
        }
    }
    
    private void decrementCard()
    {
        for (int i = 0; i < _cardValues.length; i++)
        {
            if (_cardValue.equals(_cardValues[i]))
            {
                int prevCard = i - 1;
                
                if (prevCard >= 0)
                {
                    _cardValue = _cardValues[prevCard];
                    refreshCard(CardView.Animated);
                    animateCard(CardTransition.Decrement);
                }
                else
                {
                    vibrate(); // to hint to the user that there is no higher card
                }
                break;
            }
        }
    }
    
    private void vibrate()
    {
        Vibrator vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);

        vibrator.vibrate(50); // Just a short buzz
    }
    
    private Boolean currentCardIsCoffeeCard()
    {
        return _cardValue.equals(Utils.COFFEE_CARD);
    }
    
    private View getCurrentCardView()
    {
        return currentCardIsCoffeeCard() ?
            findViewById(R.id.coffeeLayout) :
            findViewById(R.id.cardLayout);
    }

    private void refreshCard(CardView cardView)
    {
        findViewById(R.id.backLayout).setVisibility(View.INVISIBLE);
        
        View coffeeCard = findViewById(R.id.coffeeLayout);
        View normalCard = findViewById(R.id.cardLayout);
        View animatedCard = findViewById(R.id.cardLayoutAni);

        if (currentCardIsCoffeeCard())
        {
            coffeeCard.setVisibility(View.VISIBLE);
            coffeeCard.bringToFront();
            normalCard.setVisibility(View.INVISIBLE);
            
            if (cardView == CardView.Static)
            {
                animatedCard.setVisibility(View.INVISIBLE);
            }
        }
        else
        {
            TextView[] cardValueViews; 
            
            if (cardView == CardView.Static)
            {
                normalCard.setVisibility(View.VISIBLE);
                normalCard.bringToFront();
                coffeeCard.setVisibility(View.INVISIBLE);
                animatedCard.setVisibility(View.INVISIBLE);

                TextView[] cardValueViewsStatic =
                {
                    (TextView)findViewById(R.id.cardValueTopLeft),
                    (TextView)findViewById(R.id.cardValueTopRight),
                    (TextView)findViewById(R.id.cardValueCenter),
                    (TextView)findViewById(R.id.cardValueBottomLeft),
                    (TextView)findViewById(R.id.cardValueBottomRight)
                };
                
                cardValueViews = cardValueViewsStatic;
            }
            else
            {
                animatedCard.bringToFront();
                animatedCard.setVisibility(View.VISIBLE);

                TextView[] cardValueViewsAni =
                {
                    (TextView)findViewById(R.id.cardValueTopLeftAni),
                    (TextView)findViewById(R.id.cardValueTopRightAni),
                    (TextView)findViewById(R.id.cardValueCenterAni),
                    (TextView)findViewById(R.id.cardValueBottomLeftAni),
                    (TextView)findViewById(R.id.cardValueBottomRightAni)
                };
                
                cardValueViews = cardValueViewsAni;
            }
    
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
    
            ((TextView)findViewById(cardView == CardView.Static ? R.id.cardValueCenter : R.id.cardValueCenterAni))
                .setTextSize(TypedValue.COMPLEX_UNIT_DIP, centerCardValueTextSize);
        }
    }
}
