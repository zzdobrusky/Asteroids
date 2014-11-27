package com.studiorur.games.asteroids.Interfaces;

import android.graphics.RectF;

import com.studiorur.games.asteroids.Helpers.Circle;

/**
 * Created by zbynek on 11/24/2014.
 */
public interface Collidable
{
    public Circle getBoundingCircle();

    public RectF getBoundingRectangle();

    public void onCollision(Circle circle1, Circle circle2);

    public void onCollision(Circle circle, RectF rectangle);
}
