package com.studiorur.games.asteroids.Sprites;

import android.content.Context;
import android.graphics.PointF;

import com.studiorur.games.asteroids.GameManagement.GameEngine;
import com.studiorur.games.asteroids.Helpers.Boundary;
import com.studiorur.games.asteroids.Helpers.Rectangle;
import com.studiorur.games.asteroids.Interfaces.CollidableType;
import com.studiorur.games.asteroids.Interfaces.ICollidable;

/**
 * Created by zbynek on 11/27/2014.
 */
public class Asteroid extends AnimatedSprite implements ICollidable
{
    Boundary _boundary;
    PointF _velocity = new PointF(0.0f, 0.0f);
    Float _rotationVelocity  = 0.0f;
    CollidableType _collidableType = CollidableType.ASTEROID;
    Rectangle _worldRect;
    float _screenOffset;

    public PointF getVelocity()
    {
        return _velocity;
    }

    public void setVelocity(PointF velocity)
    {
        _velocity = velocity;
    }

    public Float getRotationVelocity()
    {
        return _rotationVelocity;
    }

    public void setRotationVelocity(Float rotationVelocity)
    {
        _rotationVelocity = rotationVelocity;
    }

    public CollidableType getCollidableType()
    {
        return _collidableType;
    }

    public Asteroid(
            Context context,
            int resourceIdentifier,
            int numOfRows,
            int numOfCols,
            float animationInterval,
            Rectangle worldRect,
            float screenOffset)
    {
        // load sprite sheet
        loadSpritesheet(context.getResources(), resourceIdentifier, numOfRows, numOfCols);
        _worldRect = worldRect;
        _screenOffset = screenOffset;
        _boundary = new Boundary(true); // make it a circle

        // get ready animation
        initAnimation(0, 0, 3, animationInterval, 1);
    }

    @Override
    public String toString()
    {
        return super.toString();
    }

    public Boundary getBoundery()
    {
        _boundary.updateBoundary(_width, _height, _center);
        return _boundary;
    }

    public boolean isColliding(ICollidable object)
    {
        return getBoundery().contains(object.getBoundery());
    }

    @Override
    public void update(float time)
    {
        super.update(time);

        // physics here
        _center.x = _center.x + _velocity.x * time;
        _center.y = _center.y + _velocity.y * time;

        _rotation = _rotation + _rotationVelocity * time;

        // check if out of boundaries, remove from game engine if yes, do it 2x screen offset so it has time to disappear
        if((_center.x - _width/2.0f) < (_worldRect.getLeft() - 2.0f * _screenOffset) ||
           (_center.x + _width/2.0f) > (_worldRect.getRight() + 2.0f * _screenOffset) ||
           (_center.y + _height/2.0f) > (_worldRect.getTop() + 2.0f * _screenOffset) ||
           (_center.y - _height/2.0f) < (_worldRect.getBottom() - 2.0f * _screenOffset))
        {
            GameEngine.getInstance().removeUpdateable(this);
            GameEngine.getInstance().removeCollidable(this);
        }
    }
}
