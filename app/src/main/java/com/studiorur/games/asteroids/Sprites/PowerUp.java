package com.studiorur.games.asteroids.Sprites;

import com.studiorur.games.asteroids.Helpers.Boundary;
import com.studiorur.games.asteroids.Interfaces.CollidableType;
import com.studiorur.games.asteroids.Interfaces.ICollidable;

/**
 * Created by zbynek on 12/3/2014.
 */
public class PowerUp extends AnimatedSprite implements ICollidable
{
    CollidableType _collidableType = CollidableType.POWER_UP;

    public CollidableType getCollidableType()
    {
        return null;
    }

    @Override
    public Boundary getBoundery()
    {
        // TODO: is it a circle or a rectangle
        return null;
    }

    @Override
    public boolean isColliding(ICollidable object)
    {
        return getBoundery().contains(object.getBoundery());
    }

    public CollidableType getCollidable()
    {
        return _collidableType;
    }
}
