package com.richev.planningpokerplusplus;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * An ImageView that has been rotated 180 degrees
 * @author Rich
 */
public class UpsideDownImageView extends ImageView {

    //The below two constructors appear to be required
    public UpsideDownImageView(Context context) {
        super(context);
    }

    public UpsideDownImageView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    @Override
    public void onDraw(Canvas canvas) {
    	Utils.RotateCanvasUpsideDown(this, canvas);

    	/*        //This saves off the matrix that the canvas applies to draws, so it can be restored later. 
        canvas.save(); 

        //now we change the matrix
        //We need to rotate around the centre of our image
        //Otherwise it rotates around the origin, and that's bad. 
        float py = this.getHeight()/2.0f;
        float px = this.getWidth()/2.0f;
        canvas.rotate(180, px, py);*/ 

        //draw the image with the matrix applied. 
        super.onDraw(canvas); 

        //restore the old matrix. 
        canvas.restore();
    }
}

