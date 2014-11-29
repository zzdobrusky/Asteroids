package com.studiorur.games.asteroids.Sprites;

import android.util.Log;

import com.studiorur.games.asteroids.Helpers.Boundary;
import com.studiorur.games.asteroids.Interfaces.Collidable;

/**
 * Created by zbynek on 11/27/2014.
 */
public class Asteroid extends PhysicsSprite implements Collidable
{
    Boundary _boundary;

    public Asteroid()
    {
        _boundary = new Boundary(_width, _height, _center);
    }

    @Override
    public Boundary getBoundery()
    {
        return _boundary;
    }

    @Override
    public void collide(Collidable object)
    {
        if(_boundary.contains(object.getBoundery()))
        {
            // TODO: do some awesome explosion and sound effect
            Log.i("collision", "asteroid collided");
        }
    }
}
