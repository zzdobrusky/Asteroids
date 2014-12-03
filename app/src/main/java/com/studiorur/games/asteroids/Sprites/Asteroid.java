package com.studiorur.games.asteroids.Sprites;

import android.content.Context;
import android.graphics.PointF;

import com.studiorur.games.asteroids.Helpers.Boundary;
import com.studiorur.games.asteroids.Helpers.SoundFX;
import com.studiorur.games.asteroids.Interfaces.ICollidable;
import com.studiorur.games.asteroids.R;

/**
 * Created by zbynek on 11/27/2014.
 */
public class Asteroid extends AnimatedSprite implements ICollidable
{
    Boundary _boundary;
    PointF _velocity = new PointF(0.0f, 0.0f);;
    Float _rotationVelocity  = 0.0f;;

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

    public Asteroid(Context context)
    {
        _boundary = new Boundary(true);
    }

    @Override
    public String toString()
    {
        return super.toString();
    }

    @Override
    public Boundary getBoundery()
    {
        _boundary.updateBoundary(_width, _height, _center);

        return _boundary;
    }

    @Override
    public void collide(ICollidable object)
    {
        if(getBoundery().contains(object.getBoundery()))
        {
            // TODO: do some awesome explosion and sound effect
            //Log.i("collision", "asteroid: " + toString() + " collided with " + object.toString());
            SoundFX.getInstance().play(R.raw.explosion, 1.0f);
        }
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
