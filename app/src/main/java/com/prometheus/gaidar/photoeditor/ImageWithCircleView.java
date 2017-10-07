package com.prometheus.gaidar.photoeditor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;


public class ImageWithCircleView extends AppCompatImageView {

    private Paint mCirclePaint;
    private int mImageCenterX = 0;
    private int mImageCenterY = 0;
    private int mBitmapX;
    private int mBitmapY;
    private int mRadius = 50;


    private static String TAG = "OnTouch";


    private BitmapCropListener bitmapCropListener = new BitmapCropListener() {
        @Override
        public void showImage(Bitmap bitmap) {

        }

        @Override
        public void hideImage() {

        }
    };

    public interface BitmapCropListener {

        void showImage(Bitmap bitmap);

        void hideImage();
    }

    public void setBitmapCropListener(BitmapCropListener bitmapCropListener) {
        this.bitmapCropListener = bitmapCropListener;
    }

    public ImageWithCircleView(Context context) {
        super(context);
        init();
    }

    public ImageWithCircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }



    private void init() {

        mCirclePaint = new Paint();
        mCirclePaint.setColor(getContext().getResources().getColor(R.color.colorAccent));
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setStrokeWidth(4);
        setScaleType(ScaleType.FIT_XY);


    }

    public void setRadius(float radius) {

        mRadius =(int) (Math.min(getBitmap().getHeight()
                ,getBitmap().getWidth())*radius);

        invalidate();
    }


    public float getRadius() {
        return mRadius;
    }


    @Override
    public boolean onTouchEvent(final MotionEvent event) {

        mImageCenterX = (int) event.getX();
        mImageCenterY = (int) event.getY();
        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:

                break;

            case MotionEvent.ACTION_MOVE:
                 mBitmapX = (int) (calcWidthRatio() * (event.getX()));
                 mBitmapY = (int) (calcHeightRatio() * (event.getY()));
                int height = (int) (mRadius * 2 * calcHeightRatio());
                int width = (int) (2 * mRadius * calcWidthRatio());

                if (mBitmapX < 0 || mBitmapY < 0)
                    return true;
                if ((mBitmapX + width > getBitmap().getWidth()) || (mBitmapY + height > getBitmap().getHeight()))
                    return true;

                Bitmap bitmap = Bitmap.createBitmap(getBitmap(), mBitmapX, mBitmapY, width, height);
                bitmapCropListener.showImage(bitmap);

                Log.i(TAG, "Bitmap pos " + mBitmapX + " " + mBitmapY);
                Log.i(TAG, "Image pos " + mImageCenterX + " " + mImageCenterY);
                invalidate();
                break;

            case MotionEvent.ACTION_UP:
                bitmapCropListener.hideImage();
                invalidate();
                break;

            default:
                break;
        }

        return true;
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(mImageCenterX, mImageCenterY, mRadius, mCirclePaint);

    }

    private double calcHeightRatio() {
        return getBitmap().getHeight()/getHeight();
    }

    private double calcWidthRatio() {
        return getBitmap().getWidth()/getWidth();
    }

private Bitmap getBitmap(){
        return ((BitmapDrawable)getDrawable()).getBitmap();
}

}

