package com.g.e.draganddraw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v4.content.FileProvider;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class BoxDrawingView extends View {
    private static final String TAG = "BoxDrawingView";

    private Box mCurrentBox;
    private List<Box> mBoxen = new ArrayList<>();
    private Paint mBoxPaint;
    private Paint mBackgroundPaint;

    private int mPaintFingerId = -1;
    private int mRotationFingerId = -1;
    private PointF mOriginPoint;
    private PointF mCurrentPoint;


    public BoxDrawingView(Context context) {
        this (context, null);
    }

    public BoxDrawingView (Context context, AttributeSet attrs){
        super(context, attrs);

        mBoxPaint = new Paint();
        mBoxPaint.setColor(0x22ff0000);

        mBackgroundPaint = new Paint();
        mBackgroundPaint.setColor(0xfff8efe0);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPaint(mBackgroundPaint);

        for(Box box : mBoxen){
            float left = Math.min(box.getOrigin().x, box.getCurrent().x);
            float right = Math.max(box.getOrigin().x, box.getCurrent().x);
            float top = Math.min(box.getOrigin().y, box.getCurrent().y);
            float bottom = Math.max(box.getOrigin().y, box.getCurrent().y);
//            canvas.drawRect(left, top, right, bottom, mBoxPaint);

            canvas.save();
            canvas.rotate(box.getDegrees(), box.getCurrent().x, box.getCurrent().y);
            canvas.drawRect(left, top, right, bottom, mBoxPaint);
            canvas.restore();

//            RectF r = new RectF();
//            r.set(left, top, right, bottom);
//            Matrix m = new Matrix();
//            m.setRotate(45, box.getOrigin().x, box.getCurrent().x);
//            m.mapRect(r);

        }


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        String maskedAction = "";
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_POINTER_DOWN:
                maskedAction = "ACTION_POINTER_DOWN";

                if(mPaintFingerId !=-1 && mRotationFingerId == -1){
                    mRotationFingerId = event.getPointerId(event.getActionIndex());
                    mCurrentPoint = new PointF();
                    mCurrentPoint.set(event.getX(event.getActionIndex()), event.getY(event.getActionIndex()));
                    if (mOriginPoint==null) {
                        mOriginPoint = new PointF();
                        mOriginPoint.set(event.getX(event.getActionIndex()), event.getY(event.getActionIndex()));
                    }
                }

                break;
            case MotionEvent.ACTION_POINTER_UP:
                maskedAction = "ACTION_POINTER_UP";

                if (event.findPointerIndex(mRotationFingerId) == event.getActionIndex()){
                    mRotationFingerId = -1;
                    mCurrentPoint = null;
                }

                if (event.findPointerIndex(mPaintFingerId) == event.getActionIndex()){
                    mPaintFingerId = -1;
                    mRotationFingerId = -1;
                    mCurrentPoint = null;
                    mOriginPoint = null;
                    mCurrentBox = null;
                }

                break;
        }

        String action = "";
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                action = "ACTION_DOWN";

                mPaintFingerId = event.getPointerId(0);

                mCurrentBox = new Box(event.getX(0), event.getY(0));
                mBoxen.add(mCurrentBox);
                break;
            case MotionEvent.ACTION_MOVE:
                action = "ACTION_MOVE";
                if (mCurrentBox != null) {
                    int paintFingerIndex = event.findPointerIndex(mPaintFingerId);
                    mCurrentBox.setCurrent(event.getX(paintFingerIndex), event.getY(paintFingerIndex));
                    if(mRotationFingerId>=0) {
                        int rotationFingerIndex = event.findPointerIndex(mRotationFingerId);
                        mCurrentPoint.set(event.getX(rotationFingerIndex), event.getY(rotationFingerIndex));
                        mCurrentBox.setDegrees(mOriginPoint, mCurrentPoint);
                    }

                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                action = "ACTION_UP";

                mPaintFingerId = -1;
                mRotationFingerId = -1;

                mCurrentBox = null;
                break;
            case MotionEvent.ACTION_CANCEL:
                action = "ACTION_CANCEL";

                mPaintFingerId = -1;
                mRotationFingerId = -1;

                mCurrentBox = null;
                break;

        }

//        Log.i(TAG, action + " at x=" + current.x +
//                ", y=" + current.y);
        if (maskedAction!="") {
            Log.i(TAG, maskedAction + " " + event.getPointerCount());
        }
        if(action!=""){
            Log.i(TAG, action + " " + event.getPointerCount());
        }
        for (int i=0; i<event.getPointerCount(); i++){
            Log.i(TAG, "index-" + i + " ID-" + event.getPointerId(i) + " x-" + event.getX(i) + " y-" + event.getY(i));
        }

//        Log.i(TAG, "first - (x,y)");
//        if (isSecondFingerPressed){
//            Log.i(TAG, "second - (x,y)");
//        }


        return true;
    }
}
