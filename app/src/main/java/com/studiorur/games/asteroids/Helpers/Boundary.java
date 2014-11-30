package com.studiorur.games.asteroids.Helpers;

import android.graphics.PointF;

/**
 * Created by zbynek on 11/28/2014.
 */
public class Boundary
{
    boolean _isCircle = false;
    Rectangle _rect = null;
    Circle _circle = null;
    float _isRectThreshold = 10; // if either width or height is bigger by this % it is a rect
    boolean _isInitialized = false;


    public Rectangle getRect()
    {
        return _rect;
    }

    public Circle getCircle()
    {
        return _circle;
    }

    public boolean isCircle()
    {
        return  _isCircle;
    }

    public Boundary()
    {

    }

    private void init(float objectWidth, float objectHeight, PointF center)
    {
        // assuming that if width and height differ in more then some threshold in % make it a rectangle
        // otherwise it is a circle
        float minSize = Math.min(objectWidth, objectHeight);
        if (Math.abs(objectHeight - objectWidth)/minSize > _isRectThreshold)
        {
            // make the rectangle boundary
            _isCircle = false;
            _rect = new Rectangle(objectWidth, objectHeight, center);
        }
        else
        {
            // make the circle boundary
            _isCircle = true;
            _circle = new Circle(minSize, center);
        }

        _isInitialized = true;
    }

    public void updateBoundary(float objectWidth, float objectHeight, PointF center)
    {
        if(!_isInitialized)
            init(objectWidth, objectHeight, center);

        if(_isCircle)
        {
            _circle.setCenter(center);
        }
        else
        {
            _rect.setCenter(center);
        }
    }

    public boolean contains(Boundary boundary)
    {
        boolean isContaining = false;

        if(_isCircle && boundary.isCircle()) // circle with circle collision
        {
            if(_circle.contains(boundary.getCircle()))
                isContaining = true;
        }
        else if(_isCircle && !boundary.isCircle()) // circle with rectangle collision
        {
            if(_circle.contains(boundary.getRect()))
                isContaining = true;
        }
        else if(!_isCircle && boundary.isCircle()) // rectangle with circle collision
        {
            if(boundary.getCircle().contains(_rect))
                isContaining = true;
        }
        else if(!isContaining && !boundary.isCircle()) // rectangle with rectangle collision
        {
            if(_rect.contains(boundary.getRect()))
                isContaining = true;
        }

        return isContaining;
    }
}
