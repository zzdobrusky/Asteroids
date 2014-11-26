package com.studiorur.games.asteroids.Interfaces;

import android.graphics.RectF;

/**
 * Created by zbynek on 11/24/2014.
 */
public interface Collidable
{
    RectF getBox();
    boolean isColliding(RectF box1, RectF box2);
}
