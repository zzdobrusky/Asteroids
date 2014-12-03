package com.studiorur.games.asteroids.Shapes;

import android.graphics.PointF;

import com.studiorur.games.asteroids.Interfaces.IUpdatable;

/**
 * Created by zbynek on 11/25/2014.
 */
public class Star extends CircleShape implements IUpdatable
{
    PointF _velocity = new PointF(0.0f, 0.0f);;

    public Star(float centerX, float centerY)
    {
        _velocity = new PointF(0.0f, 0.0f);
        _center = new PointF(centerX, centerY);
    }

    public PointF getVelocity()
    {
        return _velocity;
    }

    public void setVelocity(PointF velocity)
    {
        _velocity = velocity;
    }

    @Override
    public void update(float time)
    {
        // physics here
        _center.x = _center.x + _velocity.x * time;
        _center.y = _center.y + _velocity.y * time;
    }
}
