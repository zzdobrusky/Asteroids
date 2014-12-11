package com.studiorur.games.asteroids.Sprites;

import android.content.Context;
import android.graphics.PointF;

import com.studiorur.games.asteroids.GameManagement.GameEngine;
import com.studiorur.games.asteroids.Helpers.Boundary;
import com.studiorur.games.asteroids.Helpers.Rectangle;
import com.studiorur.games.asteroids.Helpers.SoundFX;
import com.studiorur.games.asteroids.Interfaces.CollidableType;
import com.studiorur.games.asteroids.Interfaces.ICollidable;
import com.studiorur.games.asteroids.R;

/**
 * Created by zbynek on 12/3/2014.
 */
public class LaserPowerUp extends AnimatedSprite implements ICollidable
{
    PointF _velocity = new PointF(0.0f, 0.0f);
    Boundary _boundary;
    Rectangle _worldRect;
    float _screenOffset;
    CollidableType _collidableType = CollidableType.POWER_UP;
    SoundFX _powerupSound;

    public LaserPowerUp(Context context, int resourceIdentifier, PointF center, Rectangle worldRect, float screenOffset)
    {
        _velocity = new PointF(0.0f, 0.0f);
        _center = new PointF(center.x, center.y);
        _worldRect = worldRect;
        _screenOffset = screenOffset;
        _powerupSound = new SoundFX(context, R.raw.power_up);

        // load sprite sheet
        loadSpritesheet(context.getResources(), resourceIdentifier, 1, 1);
        _worldRect = worldRect;
        _screenOffset = screenOffset;

        // create boundary
        _boundary = new Boundary(false); // make it a rectangle
    }

    public void setVelocity(PointF velocity)
    {
        _velocity = velocity;
    }

    public CollidableType getCollidableType()
    {
        return _collidableType;
    }

    public void playPickUpSound()
    {
        _powerupSound.play();
    }

    @Override
    public Boundary getBoundery()
    {
        _boundary.updateBoundary(_width, _height, _center);
        return _boundary;
    }

    @Override
    public boolean isColliding(ICollidable object)
    {
        return getBoundery().contains(object.getBoundery());
    }

    @Override
    public void update(float time)
    {
        // physics here
        _center.x = _center.x + _velocity.x * time;
        _center.y = _center.y + _velocity.y * time;

        // remove strait projectiles
        if((_center.x - _width/2.0f) < (_worldRect.getLeft() - _screenOffset) ||
                (_center.x + _width/2.0f) > (_worldRect.getRight() + _screenOffset) ||
                (_center.y + _height/2.0f) > (_worldRect.getTop() + _screenOffset) ||
                (_center.y - _height/2.0f) < (_worldRect.getBottom() - _screenOffset))
        {
            GameEngine.getInstance().removeUpdateable(this);
            GameEngine.getInstance().removeCollidable(this);
        }
    }
}
