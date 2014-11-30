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

    public Boundary(boolean isCircle)
    {
        _isCircle = isCircle;
        _rect = new Rectangle();
        _circle = new Circle();
    }

    public void updateBoundary(float objectWidth, float objectHeight, PointF center)
    {
        if(_isCircle)
        {
            float radius = Math.min(objectHeight, objectWidth);
            _circle.setCenter(center);
            _circle.setRadius(radius);
        }
        else
        {
            _rect.setCenter(center);
            _rect.setWidth(objectWidth);
            _rect.setHeight(objectHeight);
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
