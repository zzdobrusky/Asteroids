package com.studiorur.games.asteroids.Helpers;

import android.graphics.PointF;
import android.util.FloatMath;

/**
 * Created by zbynek on 11/27/2014.
 */
public class Circle
{
    protected float _radius;
    protected PointF _center;

    public float getRadius()
    {
        return _radius;
    }

    public void setRadius(float radius)
    {
        _radius = radius;
    }

    public PointF getCenter()
    {
        return _center;
    }

    public void setCenter(PointF center)
    {
        _center = center;
    }

    public Circle()
    {
        _radius = 0.5f;
        _center = new PointF(0.0f, 0.0f);
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
        float distance = FloatMath.sqrt(XX + YY);

        return (distance < (_radius + circle.getRadius()));
    }

    // this is assuming that the rectangle is not rotating!
    public boolean contains(Rectangle rect)
    {
        float XX = (_center.x - rect.getCenter().x) * (_center.x - rect.getCenter().x);
        float YY = (_center.y - rect.getCenter().y) * (_center.y - rect.getCenter().y);
        float distance = FloatMath.sqrt(XX + YY);

        float H2H2 = (rect.getHeight()/2.0f) * (rect.getHeight()/2.0f);
        float W2W2 = (rect.getWidth()/2.0f) * (rect.getWidth()/2.0f);
        float diagCornerRectCircleDistance = FloatMath.sqrt(H2H2 + W2W2) + _radius;

        boolean isContaining = false;

        if(_center.x >= rect.getLeft() && _center.x <= rect.getRight() &&
           rect.getHeight()/2.0f + _radius >= Math.abs(rect.getCenter().y - _center.y))
        {
            // top or bottom hit
            isContaining = true;
        }
        else if(_center.y <= rect.getTop() && _center.y >= rect.getBottom() &&
                rect.getWidth()/2.0f + _radius >= Math.abs(rect.getCenter().x - _center.x))
        {
            // left or right hit
            isContaining = true;
        }
        else if(distance <= diagCornerRectCircleDistance)
        {
            // everything else is being approximated as corner collisions
            // % of this kind of impacts should be low
            isContaining = true;
        }

        return isContaining;
    }
}
