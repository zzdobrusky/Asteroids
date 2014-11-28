package com.studiorur.games.asteroids.Interfaces;

import android.graphics.RectF;

import com.studiorur.games.asteroids.Helpers.Boundary;
import com.studiorur.games.asteroids.Helpers.Circle;

/**
 * Created by zbynek on 11/24/2014.
 */
public interface Collidable
{
    public Boundary getBoundery();

    public void collide(Collidable object);
}
