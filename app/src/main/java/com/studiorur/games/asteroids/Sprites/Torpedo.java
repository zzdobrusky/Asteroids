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
 * Created by zbynek on 12/13/2014.
 */
public class Torpedo extends AnimatedSprite implements ICollidable
{
    private Boundary _boundary;
    private PointF _velocity = new PointF(0.0f, 0.0f);
    private Float _rotationVelocity  = 0.0f;
    private CollidableType _collidableType = CollidableType.TORPEDO;
    private Rectangle _worldRect;
    private SoundFX _torpedoImpactSound;
    private float _explosionInterval;
    private float _passedTime = 0.0f;
    private float _explosionSpeed = 0.1f;
    private boolean _isExploding = false;

    // declare on explosion end listener
    private OnExplosionEndListener _onExplosionEndListener = null;
    public interface OnExplosionEndListener
    {
        public void onExplosionEnd();
    }
    public OnExplosionEndListener getOnExplosionEndListener()
    {
        return _onExplosionEndListener;
    }
    public void setOnExplosionEndListener(OnExplosionEndListener onExplosionEndListener)
    {
        _onExplosionEndListener = onExplosionEndListener;
    }

    public Torpedo(Context context, PointF center, Rectangle worldRect)
    {
        _center = new PointF(center.x, center.y);
        // load sprite sheet
        loadSpritesheet(context.getResources(), R.drawable.torpedo_spreadsheet, 1, 2);
        _worldRect = worldRect;
        _boundary = new Boundary(true); // make it a circle

        _torpedoImpactSound = new SoundFX(context, R.raw.asteroid_explosion);
    }

    public PointF getVelocity()
    {
        return new PointF(_velocity.x, _velocity.y);
    }

    public void setVelocity(PointF velocity)
    {
        _velocity = velocity;
    }

    public Float getRotationVelocity()
    {
        return _rotationVelocity;
    }

    public void setRotationVelocity(Float rotationVelocity)
    {
        _rotationVelocity = rotationVelocity;
    }

    public void startExplosionAnimation(float explosionInterval)
    {
        _isExploding = true;
        _explosionInterval = explosionInterval;
        // swap textures
        setFrame(0, 1);
    }

    public void playExplosionSound()
    {
        _torpedoImpactSound.play();
    }

    public Boundary getBoundery()
    {
        _boundary.updateBoundary(_width, _height, _center);
        return _boundary;
    }

    public boolean isColliding(ICollidable object)
    {
        return getBoundery().contains(object.getBoundery());
    }

    @Override
    public CollidableType getCollidableType()
    {
        return _collidableType;
    }

    @Override
    public void update(float time)
    {
        // physics here
        _center.x = _center.x + _velocity.x * time;
        _center.y = _center.y + _velocity.y * time;
        _rotation = _rotation + _rotationVelocity * time;

        // explosion animation if any
        if(_isExploding)
        {
            _width += _explosionSpeed;
            _height += _explosionSpeed;

            _passedTime += time;
            if(_passedTime >= _explosionInterval)
            {
                _isExploding = false;
                //setRotationVelocity(0.0f); // stop rotation
                _passedTime = 0.0f;
                // send out event
                if(_onExplosionEndListener != null)
                    _onExplosionEndListener.onExplosionEnd();
            }
        }

        // remove strait torpedoes
        if(_center.x < _worldRect.getLeft() - _worldRect.getWidth()/6.0f ||
           _center.x > _worldRect.getRight() + _worldRect.getWidth()/6.0f ||
           _center.y > _worldRect.getTop() + _worldRect.getHeight()/6.0f ||
           _center.y < _worldRect.getBottom() - _worldRect.getHeight()/6.0f)
        {
            GameEngine.getInstance().removeDrawable(this);
            GameEngine.getInstance().removeUpdateable(this);
            GameEngine.getInstance().removeCollidable(this);
        }
    }
}
