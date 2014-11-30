package com.studiorur.games.asteroids.Interfaces;

import com.studiorur.games.asteroids.Helpers.Boundary;

/**
 * Created by zbynek on 11/24/2014.
 */
public interface Collidable
{
    public Boundary getBoundery();

    public String toString();

    public void collide(Collidable object);
}
