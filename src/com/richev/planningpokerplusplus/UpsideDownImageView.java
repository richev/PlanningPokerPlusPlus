// Planning Poker++ for Android, copyright © Richard Everett 2011
// Full source available on Github at http://richev.github.com/PlanningPokerPlusPlus/

package com.richev.planningpokerplusplus;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * An ImageView that has been rotated 180 degrees
 * 
 * @author Rich
 */
public class UpsideDownImageView extends ImageView
{

    // The below two constructors appear to be required
    public UpsideDownImageView(Context context) {
        super(context);
    }

    public UpsideDownImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onDraw(Canvas canvas)
    {
        Utils.RotateCanvasUpsideDown(this, canvas);

        // draw the image with the matrix applied.
        super.onDraw(canvas);

        // restore the old matrix.
        canvas.restore();
    }
}
