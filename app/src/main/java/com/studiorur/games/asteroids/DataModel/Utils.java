package com.studiorur.games.asteroids.DataModel;

/**
 * Created by zbynek on 11/25/2014.
 */
public class Utils
{
    static public float randomInRange(float min, float max)
    {
        return (float)Math.random() * (max-min) + min;
    }
}
