package com.studiorur.games.asteroids.Helpers;

import android.graphics.PointF;
import android.graphics.RectF;

/**
 * Created by zbynek on 11/28/2014.
 */
public class Boundary
{
    boolean _isCircle = false;
    RectF _rect = null;
    Circle _circle = null;
    float _isRectThreshold = 10; // if either width or height is bigger by this % it is a rect

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

    public Boundary(float objectWidth, float objectHeight, PointF center)
    {
        // assuming that if width and height differ in more then some threshold in % make it a rectangle
        // otherwise it is a circle
        if (Math.abs(objectHeight - objectWidth)/objectWidth > _isRectThreshold)
        {
            // make a rectangle the boundary
            _isCircle = false;
            _rect = new RectF(
                    center.x - objectWidth/2.0f,
                    center.y + objectHeight/2.0f,
                    center.x + objectWidth/2.0f,
                    center.y - objectHeight/2.0f);
        }
        else
        {
            // make a circle the boundary
            _isCircle = true;
            float radius = Math.min(objectHeight, objectWidth);
            _circle = new Circle(radius, center);
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
