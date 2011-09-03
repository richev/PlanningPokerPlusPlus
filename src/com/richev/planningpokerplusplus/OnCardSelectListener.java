package com.richev.planningpokerplusplus;

import android.view.View;
import android.view.View.OnClickListener;

/**
 * Definition for the callback that is invoked when a user selects a card
 * @author Rich
 *
 */
public class OnCardSelectListener implements OnClickListener {

    private String _cardValue;

    public OnCardSelectListener(String cardValue) {
       this._cardValue = cardValue;
    }

    public void onClick(View v) {
    }

    /**
     * Gets the value of the card that was selected by the user
     */
    public String getCardValue() {
		return this._cardValue;
    }
 }
