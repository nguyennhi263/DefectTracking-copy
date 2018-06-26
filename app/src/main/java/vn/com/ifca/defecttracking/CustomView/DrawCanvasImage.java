package vn.com.ifca.defecttracking.CustomView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Nhi on 1/18/2018.
 */

public class DrawCanvasImage  extends android.support.v7.widget.AppCompatImageView {
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Path mPath;
    private Paint mPaint;
    private float mX, mY;
    private static final float TOLERANCE = 5;
    private Context context;

    //
    public DrawCanvasImage(Context context)
    {
        super(context);
        // TODO Auto-generated constructor stub
        setBackgroundColor(Color.BLUE);

    }



    public DrawCanvasImage(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        this.context = context;
        mPath = new Path();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeWidth(8f);
    }


    public DrawCanvasImage(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        canvas.drawPath(mPath,mPaint);
        //zooming


    }

    private void startTouch(float x, float y){
        mPath.moveTo(x,y);
        mX=x;
        mY=y;

    }
    private void moveTouch(float x, float y){
        float dx= Math.abs(x-mX);
        float dy= Math.abs(x-mY);

        if (dx >= TOLERANCE || dy >= TOLERANCE){
            mPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
            mX=x;
            mY=y;
        }
    }

    private void upTouch(){
        mPath.lineTo(mX,mY);
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mBitmap = Bitmap.createBitmap(w,h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        // return super.dispatchTouchEvent(event);
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                startTouch(x,y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                //  Toast.makeText(context,"test",Toast.LENGTH_SHORT).show();
                moveTouch(x,y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                upTouch();
                invalidate();
                break;
            case MotionEvent.ACTION_POINTER_DOWN:

                break;

        }
        return true;
    }


    public void clear(){
        mPath.reset();
        invalidate();
    }
}
