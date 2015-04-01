package com.ultimasquare.pinview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.view.View;

import java.util.InputMismatchException;

/**
 * A drawable that draws the target view as blurred using fast blur
 * <p/>
 * <p/>
 * TODO:we might use setBounds() to draw only part a of the target view
 * <p/>
 * Created by 10uR on 24.5.2014.
 */
public class BlurDrawable extends Drawable {
    private View targetRef;
    private Bitmap blurred;
    private Paint paint;
    private int radius;
    private Context mContext;


    public BlurDrawable(Context context, View target) {
        this(context,target,10);
    }

    public BlurDrawable(Context context, View target, int radius) {
        this.targetRef = target;
        setRadius(radius);
        target.setDrawingCacheEnabled(true);
        target.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_AUTO);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        mContext = context;
    }

    @Override
    public void draw(Canvas canvas) {
        if (blurred == null) {
            View target = targetRef;
            if (target != null) {
                Bitmap bitmap = target.getDrawingCache(true);
                if (bitmap == null) return;
                blurred = blurBitmap(bitmap);
            }
        }
        if (blurred != null && !blurred.isRecycled())
            canvas.drawBitmap(blurred, 0, 0, paint);
        canvas.drawColor(mContext.getResources().getColor(R.color.signin_fade));
    }

    /**
     * Set the bluring radius that will be applied to target view's bitmap
     *
     * @param radius should be 0-100
     */
    public void setRadius(int radius) {
        if (radius < 0 || radius > 100)
            throw new InputMismatchException("Radius must be 0 <= radius <= 100 !");
        this.radius = radius;
        if (blurred != null) {
            blurred.recycle();
            blurred = null;
        }
        invalidateSelf();
    }

    @Override
    public void setAlpha(int alpha) {
    }

    @Override
    public void setColorFilter(ColorFilter cf) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    public Bitmap blurBitmap(Bitmap bitmap) {


        //Let's create an empty bitmap with the same size of the bitmap we want to blur
        Bitmap outBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);


        //Instantiate a new Renderscript
        RenderScript rs = RenderScript.create(mContext);


        //Create an Intrinsic Blur Script using the Renderscript
        ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));


        //Create the in/out Allocations with the Renderscript and the in/out bitmaps
        Allocation allIn = Allocation.createFromBitmap(rs, bitmap);
        Allocation allOut = Allocation.createFromBitmap(rs, outBitmap);


        //Set the radius of the blur
        blurScript.setRadius(25.f);


        //Perform the Renderscript
        blurScript.setInput(allIn);
        blurScript.forEach(allOut);


        //Copy the final bitmap created by the out Allocation to the outBitmap
        allOut.copyTo(outBitmap);


        //recycle the original bitmap
        //TODO: CHECK why this has to be commented to work
        //bitmap.recycle();

        //After finishing everything, we destroy the Renderscript.
        rs.destroy();

        return outBitmap;

    }
}