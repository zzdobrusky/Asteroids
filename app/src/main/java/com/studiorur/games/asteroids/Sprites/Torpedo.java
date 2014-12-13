package com.studiorur.games.asteroids.Sprites;

import com.studiorur.games.asteroids.Helpers.Boundary;
import com.studiorur.games.asteroids.Interfaces.CollidableType;
import com.studiorur.games.asteroids.Interfaces.ICollidable;
import com.studiorur.games.asteroids.Interfaces.IUpdatable;

/**
 * Created by zbynek on 12/13/2014.
 */
public class Torpedo extends Sprite implements IUpdatable, ICollidable
{
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

    @Override
    public void update(float time)
    {

    }
}
