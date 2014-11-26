package com.studiorur.games.asteroids.Sprites;

import android.graphics.PointF;

import com.studiorur.games.asteroids.Interfaces.Updatable;

/**
 * Created by zbynek on 11/24/2014.
 */
public class PhysicsSprite extends Sprite implements Updatable
{
    PointF _velocity;
    Float _rotationVelocity;


    public PhysicsSprite()
    {
        _velocity = new PointF(0.0f, 0.0f);
        _rotationVelocity = 0.0f;
    }

    @Override
    public void update(float time)
    {
        // physics here
        _center.x = _center.x + _velocity.x * time;
        _center.y = _center.y + _velocity.y * time;

        _rotation = _rotation + _rotationVelocity * time;
    }
}
