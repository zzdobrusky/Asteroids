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

    public boolean contains(RectF rectangle)
    {
        float XX = (_center.x - rectangle.centerX()) * (_center.x - rectangle.centerX());
        float YY = (_center.y - rectangle.centerY()) * (_center.y - rectangle.centerY());
        float distance = FloatMath.sqrt(XX - YY);

        // TODO: need to figure out circle x rectangle collision
        boolean isContaining = (distance < (_radius + rectangle.width()/2.0f));

        return isContaining;
    }
}
