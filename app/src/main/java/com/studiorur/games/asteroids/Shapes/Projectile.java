package com.studiorur.games.asteroids.Shapes;

import android.graphics.PointF;

import com.studiorur.games.asteroids.Helpers.Boundary;
import com.studiorur.games.asteroids.Interfaces.CollidableType;
import com.studiorur.games.asteroids.Interfaces.ICollidable;
import com.studiorur.games.asteroids.Interfaces.IUpdatable;

/**
 * Created by zbynek on 12/8/2014.
 */
public class Projectile extends RectangleShape implements IUpdatable, ICollidable
{
    PointF _velocity = new PointF(0.0f, 0.0f);;

    public Projectile(PointF center)
    {
        _velocity = new PointF(0.0f, 0.0f);
        _center = new PointF(center.x, center.y);
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

    @Override
    public Boundary getBoundery()
    {
        return null;
    }

    @Override
    public CollidableType getCollidableType()
    {
        return null;
    }

    @Override
    public boolean isColliding(ICollidable object)
    {
        return false;
    }
}
