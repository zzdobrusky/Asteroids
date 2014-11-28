package com.studiorur.games.asteroids.Sprites;

import android.graphics.RectF;

import com.studiorur.games.asteroids.Interfaces.Collidable;
import com.studiorur.games.asteroids.Helpers.Circle;

/**
 * Created by zbynek on 11/27/2014.
 */
public class Asteroid extends PhysicsSprite implements Collidable
{
    @Override
    public Circle getBoundingCircle()
    {
        float radius = _width > _height ? _width : _height;

        return new Circle(radius, _center);
    }

    @Override
    public RectF getBoundingRectangle()
    {
        return null;
    }

    @Override
    public void onCollision(Circle circle1, Circle circle2)
    {
        if(circle1.contains(circle2))
        {
            // TODO: do some cool explosion
        }
    }

    @Override
    public void onCollision(Circle circle, RectF rectangle)
    {

    }
}
