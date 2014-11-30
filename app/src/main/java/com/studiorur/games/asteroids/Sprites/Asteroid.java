package com.studiorur.games.asteroids.Sprites;

import android.content.Context;
import android.util.Log;

import com.studiorur.games.asteroids.Helpers.Boundary;
import com.studiorur.games.asteroids.Helpers.SoundFX;
import com.studiorur.games.asteroids.Interfaces.Collidable;
import com.studiorur.games.asteroids.R;

/**
 * Created by zbynek on 11/27/2014.
 */
public class Asteroid extends PhysicsSprite implements Collidable
{
    Boundary _boundary;

    public Asteroid(Context context)
    {
        _boundary = new Boundary(_width, _height, _center);
        // load sounds
        SoundFX.getInstance().addSound(context, R.raw.explosion);
    }

    @Override
    public String toString()
    {
        return super.toString();
    }

    @Override
    public Boundary getBoundery()
    {
        _boundary.updateBoundary(_center);

        return _boundary;
    }

    @Override
    public void collide(Collidable object)
    {
        if(_boundary.contains(object.getBoundery()))
        {
            // TODO: do some awesome explosion and sound effect
            Log.i("collision", "asteroid: " + toString() + " collided with " + object.toString());
            //SoundFX.getInstance().play(R.raw.explosion, 1.0f);
        }
    }
}
