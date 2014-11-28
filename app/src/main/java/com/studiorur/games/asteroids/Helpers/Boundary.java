package com.studiorur.games.asteroids.Helpers;

import android.graphics.RectF;

/**
 * Created by zbynek on 11/28/2014.
 */
public class Boundary
{
    boolean _isCircle = false;
    RectF _rect = null;
    Circle _circle = null;

    public RectF getRect()
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

    public Boundary(Circle circle)
    {
        _isCircle = true;
        _circle = circle;
    }

    public Boundary(RectF rect)
    {
        _isCircle = false;
        _rect = rect;
    }

    public boolean contains(Boundary boundary)
    {
        boolean isContaining = false;

        if(_isCircle && boundary.isCircle())
        {
            // circle with circle collision
            if(_circle.contains(boundary.getCircle()))
                isContaining = true;
        }
        else if(_isCircle && !boundary.isCircle())
        {
            // circle with rectangle collision
            if(_circle.contains(boundary.getRect()))
                isContaining = true;
        }
        else if(!_isCircle && boundary.isCircle())
        {
            // rectangle with circle collision
            if(boundary.getCircle().contains(_rect))
                isContaining = true;
        }
        else if(!isContaining && !boundary.isCircle())
        {
            // rectangle with rectengle collision
            if(_rect.contains(boundary.getRect()))
                isContaining = true;
        }

        return isContaining;
    }
}
