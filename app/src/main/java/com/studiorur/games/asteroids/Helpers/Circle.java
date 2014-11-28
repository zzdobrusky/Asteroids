package com.studiorur.games.asteroids.Helpers;

import android.graphics.PointF;
import android.graphics.RectF;
import android.util.FloatMath;

/**
 * Created by zbynek on 11/27/2014.
 */
public class Circle
{
    float _radius;
    PointF _center;

    public float getRadius()
    {
        return _radius;
    }

    public PointF getCenter()
    {
        return _center;
    }


    public Circle(float radius, PointF center)
    {
        _radius = radius;
        _center = center;
    }

    public boolean contains(Circle circle)
    {
        float XX = (_center.x - circle.getCenter().x) * (_center.x - circle.getCenter().x);
        float YY = (_center.y - circle.getCenter().y) * (_center.y - circle.getCenter().y);
        float distance = FloatMath.sqrt(XX - YY);

        return (distance < (_radius + circle.getRadius()));
    }

    public boolean contains(RectF rect)
    {
        float XX = (_center.x - rect.centerX()) * (_center.x - rect.centerX());
        float YY = (_center.y - rect.centerY()) * (_center.y - rect.centerY());
        float distance = FloatMath.sqrt(XX - YY);

        float diagCornerRectCircleDistance = FloatMath.sqrt((rect.height()/2.0f)*(rect.height()/2.0f) + (rect.width()/2.0f)*(rect.width()/2.0f))
                + _radius;

        // TODO: need to figure out circle x rect collision
        boolean isContaining = false;


        if(_center.x > rect.left && _center.x < rect.right &&
           rect.height()/2.0f + _radius > Math.abs(rect.centerY() - _center.y))
        {
            // top or bottom hit
            isContaining = true;
        }
        else if(_center.y < rect.top && _center.y > rect.bottom &&
                rect.width()/2.0f + _radius > Math.abs(rect.centerX() - _center.x))
        {
            // left or right hit
            isContaining = true;
        }
        else if(distance < diagCornerRectCircleDistance)
        {
            // everything else is being approximated as corner collisions
            // % of this kind of impacts should be low
            isContaining = true;
        }

        return isContaining;
    }
}
