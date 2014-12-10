package com.studiorur.games.asteroids.Sprites;

import android.graphics.Color;
import android.graphics.PointF;
import android.view.MotionEvent;

import com.studiorur.games.asteroids.AdaptersViews.GameScreenAdapter;
import com.studiorur.games.asteroids.GameManagement.GameEngine;
import com.studiorur.games.asteroids.Helpers.Boundary;
import com.studiorur.games.asteroids.Helpers.LoopTimer;
import com.studiorur.games.asteroids.Helpers.Rectangle;
import com.studiorur.games.asteroids.Interfaces.CollidableType;
import com.studiorur.games.asteroids.Interfaces.ICollidable;
import com.studiorur.games.asteroids.Shapes.Projectile;

/**
 * Created by zbynek on 11/25/2014.
 */
public class SpaceShip extends AnimatedSprite implements ICollidable
{
    PointF _velocity = new PointF(0.0f, 0.0f);;
    Float _rotationVelocity  = 0.0f;;
    float _invertedMass;
    PointF _externalForce;
    PointF _rocketForce;
    GameScreenAdapter _gameScreenActivity = null;
    Rectangle _worldRect;
    float _forceCoefficient = 0.000017f;
    float _frictionCoefficient = 0.89f;
    Boundary _boundary;
    CollidableType _collidableType = CollidableType.SPACESHIP;
    float _originalLaserInterval = 500.0f; // in milliseconds
    float _currentLaserInterval = _originalLaserInterval;
    float _passedTime = _currentLaserInterval; // shoots first
    boolean _isShooting = false;
    LoopTimer _laserUpgradeTimer;

    public PointF getVelocity()
    {
        return _velocity;
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

    public CollidableType getCollidableType()
    {
        return _collidableType;
    }

    public SpaceShip(
            GameScreenAdapter gameScreenActivity,
            Rectangle worldRect,
            float mass,
            int resourceIdentifier,
            int numOfRows,
            int numOfCols,
            float animationInterval)
    {
        _gameScreenActivity = gameScreenActivity;
        _invertedMass = 1.0f/mass;
        _worldRect = worldRect;

        _laserUpgradeTimer = new LoopTimer();

        // load sprite sheet
        loadSpritesheet(gameScreenActivity.getResources(), resourceIdentifier, numOfRows, numOfCols);

        // create boundary
        _boundary = new Boundary(false); // make it a rectangle

        initAnimation(0, 0, 3, animationInterval, 0);
        startAnimation();

        resetForces();

        // start listening for touch events (will propel the spaceship)
        _gameScreenActivity.setOnTouchScreenListener(new GameScreenAdapter.OnTouchScreenListener()
        {
            @Override
            public void onTouchScreen(PointF worldLoc, MotionEvent motionEvent)
            {
                // update force
                float forceX = (worldLoc.x - _center.x) * _forceCoefficient;
                float forceY = (worldLoc.y - _center.y) * _forceCoefficient;
                _rocketForce = new PointF(forceX, forceY);

                // Start shooting projectiles with some frequency
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN)
                {
                    _isShooting = true;
                }
                else if(motionEvent.getAction() == MotionEvent.ACTION_UP)
                {
                    _isShooting = false;
                    _passedTime = _currentLaserInterval;
                }
            }
        });
    }

    public float getOriginalLaserInterval()
    {
        return _originalLaserInterval;
    }

    public void startLaserUpgrade(float laserInterval, float laserUpgradeInterval)
    {
        _currentLaserInterval = laserInterval;
        _passedTime = _currentLaserInterval;
        // start one time timer and set up listener
        _laserUpgradeTimer.setOnTimePassedListener(new LoopTimer.OnTimePassedListener()
        {
            @Override
            public void onTimePassed()
            {
                // go back to original laser frequency
                _currentLaserInterval = _originalLaserInterval;
            }
        });
        _laserUpgradeTimer.start(laserUpgradeInterval, 1); // do it just once
    }

    private void shootLaser()
    {
        Projectile newProjectile = new Projectile(new PointF(_center.x, _center.y + _height/1.5f), _worldRect, 0.2f);
        newProjectile.setVelocity(new PointF(0.0f, 0.001f));
        newProjectile.setWidth(0.01f);
        newProjectile.setHeight(0.05f);
        newProjectile.setColor(Color.MAGENTA);
        //SoundFX.getInstance().play(R.raw.shot, 1.0f); doesn't work
        GameEngine.getInstance().addUpdateable(newProjectile);
        GameEngine.getInstance().addCollidable(newProjectile);
    }

    public void setLaserFrequence(float laserInterval)
    {
        _currentLaserInterval = laserInterval;
        _passedTime = laserInterval;
    }

    public void addExternalForce(PointF force)
    {
        _externalForce = new PointF(_externalForce.x + force.x, _externalForce.y + force.y);
    }

    public void resetForces()
    {
        _externalForce = new PointF(0.0f, 0.0f);
        _rocketForce = new PointF(0.0f, 0.0f);
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
    public void update(float time)
    {
        super.update(time);

        PointF force = new PointF(_rocketForce.x + _externalForce.x, _rocketForce.y + _externalForce.y);

        _velocity.x = _frictionCoefficient * _velocity.x + force.x * _invertedMass * time;
        _velocity.y = _frictionCoefficient * _velocity.y + force.y * _invertedMass * time;

        _center.x = _center.x + _velocity.x * time;
        _center.y = _center.y + _velocity.y * time;

        _rotation = _rotation + _rotationVelocity * time;

        // reset forces each update
        resetForces();

        // laser shooting
        if(_isShooting)
        {
            _passedTime += time;
            if(_passedTime >= _currentLaserInterval)
            {
                shootLaser();
                _passedTime = 0.0f;
            }
        }


        _laserUpgradeTimer.update(time);
    }
}
