package com.g.e.draganddraw;

import android.graphics.PointF;

public class Box {
    private PointF mOrigin = new PointF();
    private PointF mCurrent = new PointF();


    private float mDegrees;

//    public Box (PointF origin){
//        mOrigin = origin;
//        mCurrent = origin;
//        mDegrees = 0;
//    }

    public Box (float originX, float originY) {
        mOrigin.set(originX, originY);
        mCurrent.set(originX, originY);
        mDegrees = 0;
    }

    public PointF getCurrent() {
        return mCurrent;
    }

    public void setCurrent(PointF current) {
        mCurrent = current;
    }

    public void setCurrent (float x, float y){
        mCurrent.set(x, y);
    }

    public PointF getOrigin() {
        return mOrigin;
    }

    public float getDegrees() {
        return mDegrees;
    }

    public void setDegrees(PointF startPosition, PointF endPosition) {
        double side1 = Math.sqrt(Math.pow((mCurrent.x - startPosition.x), 2)
        + Math.pow((mCurrent.y - startPosition.y), 2));
        double side2 =  Math.sqrt(Math.pow((mCurrent.x - endPosition.x), 2)
                + Math.pow((mCurrent.y - endPosition.y), 2));
        double side3 = Math.sqrt(Math.pow((startPosition.x - endPosition.x), 2)
                + Math.pow((startPosition.y - endPosition.y), 2));

        double arccos = Math.acos((Math.pow(side1, 2) + Math.pow(side2, 2) - Math.pow(side3, 2))/
                (2*side1*side2));

        float degrees = (float)(arccos*180/Math.PI);
        mDegrees = degrees;
    }
}
