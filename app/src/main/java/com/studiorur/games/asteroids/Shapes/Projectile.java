package com.studiorur.games.asteroids.Shapes;

import android.graphics.PointF;

import com.studiorur.games.asteroids.GameManagement.GameEngine;
import com.studiorur.games.asteroids.Helpers.Boundary;
import com.studiorur.games.asteroids.Helpers.Rectangle;
import com.studiorur.games.asteroids.Interfaces.CollidableType;
import com.studiorur.games.asteroids.Interfaces.ICollidable;
import com.studiorur.games.asteroids.Interfaces.IUpdatable;

/**
 * Created by zbynek on 12/8/2014.
 */
public class Projectile extends RectangleShape implements IUpdatable, ICollidable
{
    PointF _velocity = new PointF(0.0f, 0.0f);
    Boundary _boundary;
    CollidableType _collidableType = CollidableType.PROJECTILE;
    Rectangle _worldRect;

    public Projectile(PointF center, Rectangle worldRect)
    {
        _velocity = new PointF(0.0f, 0.0f);
        _center = new PointF(center.x, center.y);
        _worldRect = worldRect;

        // create boundary
        _boundary = new Boundary(false); // make it a rectangle
    }

    @Override
    public Boundary getBoundery()
    {
        _boundary.updateBoundary(_width, _height, _center);

        return _boundary;
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

        // remove strait projectiles
        if(_center.x < _worldRect.getLeft() || _center.x > _worldRect.getRight() ||
           _center.y < _worldRect.getBottom() || _center.y > _worldRect.getTop() )
        {
            GameEngine.getInstance().removeUpdateable(this);
            GameEngine.getInstance().removeCollidable(this);
        }
    }

    public CollidableType getCollidableType()
    {
        return _collidableType;
    }

    @Override
    public boolean isColliding(ICollidable object)
    {
        return getBoundery().contains(object.getBoundery());
    }
}
