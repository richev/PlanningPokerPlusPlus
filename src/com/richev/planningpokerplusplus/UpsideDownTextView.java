// Planning Poker++ for Android, copyright (c) Richard Everett 2012
// Full source available on GitHub at http://richev.github.com/PlanningPokerPlusPlus

package com.richev.planningpokerplusplus;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * A TextView that has been rotated 180 degrees
 * 
 * @author Rich
 * 
 */
public class UpsideDownTextView extends TextView
{

    // The below two constructors appear to be required
    public UpsideDownTextView(Context context) {
        super(context);
    }

    public UpsideDownTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onDraw(Canvas canvas)
    {
        Utils.RotateCanvasUpsideDown(this, canvas);

        // draw the text with the matrix applied.
        super.onDraw(canvas);

        // restore the old matrix.
        canvas.restore();
    }
}
