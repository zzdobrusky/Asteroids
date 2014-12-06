package com.studiorur.games.asteroids.Sprites;

import android.content.Context;
import android.graphics.PointF;

import com.studiorur.games.asteroids.Helpers.Boundary;
import com.studiorur.games.asteroids.Helpers.SoundFX;
import com.studiorur.games.asteroids.Interfaces.CollidableType;
import com.studiorur.games.asteroids.Interfaces.ICollidable;
import com.studiorur.games.asteroids.R;

/**
 * Created by zbynek on 11/27/2014.
 */
public class Asteroid extends AnimatedSprite implements ICollidable
{
    Boundary _boundary;
    PointF _velocity = new PointF(0.0f, 0.0f);
    Float _rotationVelocity  = 0.0f;
    CollidableType _collidableType = CollidableType.ASTEROID;

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

    public Asteroid(Context context, int resourceIdentifier)
    {
        // no animations
        // load sprite sheet
        loadSpritesheet(context.getResources(), resourceIdentifier);

        _boundary = new Boundary(true); // make it a circle

    }

    public Asteroid(Context context, int resourceIdentifier, int numOfRows, int numOfCols, float animationInterval)
    {
        // load sprite sheet
        loadSpritesheet(context.getResources(), resourceIdentifier, numOfRows, numOfCols, animationInterval);

        _boundary = new Boundary(true); // make it a circle
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
    }
}
