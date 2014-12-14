package com.studiorur.games.asteroids.Sprites;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PointF;
import android.view.MotionEvent;

import com.studiorur.games.asteroids.AdaptersViews.GameScreenAdapter;
import com.studiorur.games.asteroids.GameManagement.GameEngine;
import com.studiorur.games.asteroids.Helpers.Boundary;
import com.studiorur.games.asteroids.Helpers.LoopTimer;
import com.studiorur.games.asteroids.Helpers.Rectangle;
import com.studiorur.games.asteroids.Helpers.SoundFX;
import com.studiorur.games.asteroids.Interfaces.CollidableType;
import com.studiorur.games.asteroids.Interfaces.ICollidable;
import com.studiorur.games.asteroids.R;
import com.studiorur.games.asteroids.Shapes.LaserRay;

/**
 * Created by zbynek on 11/25/2014.
 */
public class SpaceShip extends AnimatedSprite implements ICollidable
{
    // Physics related
    PointF _velocity = new PointF(0.0f, 0.0f);;
    Float _rotationVelocity  = 0.0f;;
    float _invertedMass;
    PointF _externalForce;
    PointF _rocketForce;
    Rectangle _worldRect;
    float _forceCoefficient = 0.000017f;
    float _frictionCoefficient = 0.89f;
    // Collision related
    Boundary _boundary;
    CollidableType _collidableType = CollidableType.SPACESHIP;
    // Weaponry
    float _originalLaserInterval = 500.0f; // in milliseconds
    float _upgradedLaserInterval = 130.0f;
    float _torpedoShootInterval = 1000.0f;
    float _currentShootInterval = _originalLaserInterval;
    float _passedTime = _currentShootInterval; // shoots first
    boolean _isShooting = false;
    LoopTimer _weaponUpgradeTimer;
    // Sounds
    SoundFX _laserShootSound;
    SoundFX _torpedoShootSound;
    SoundFX _finalExplosionSound;
    private Weapon _currentWeapon = Weapon.LASER; // default
    private GameScreenAdapter _gameScreenActivity = null;


    public enum Weapon {LASER, UPGRADED_LASER, TORPEDO};

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
        _laserShootSound = new SoundFX(gameScreenActivity, R.raw.laser);
        _torpedoShootSound = new SoundFX(gameScreenActivity, R.raw.detonator);
        _finalExplosionSound = new SoundFX(gameScreenActivity, R.raw.spaceship_final);

        // set up timer for going back from weapon upgrade to regular laser
        _weaponUpgradeTimer = new LoopTimer();
        _weaponUpgradeTimer.setOnTimePassedListener(new LoopTimer.OnTimePassedListener()
        {
            @Override
            public void onTimePassed()
            {
                // go back to original laser
                setWeaponShootInterval(_originalLaserInterval);
                _currentWeapon = Weapon.LASER;
            }
        });

        // load sprite sheet
        loadSpritesheet(gameScreenActivity.getResources(), resourceIdentifier, numOfRows, numOfCols);

        // create boundary
        _boundary = new Boundary(false); // make it a rectangle

        // engine flame animation
        startAnimation(0, 0, 3, animationInterval, 0);

        // null forces
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
                    _isShooting = true;
                else if(motionEvent.getAction() == MotionEvent.ACTION_UP)
                    _isShooting = false;
            }
        });
    }

    public void upgradeWeapon(Weapon upgradedWeapon, float weaponInterval)
    {
        switch (upgradedWeapon)
        {
            case UPGRADED_LASER:
                _currentWeapon = Weapon.LASER;
                // to upgrade laser just change the speed of shooting
                setWeaponShootInterval(_upgradedLaserInterval);
                break;

            case TORPEDO:
                _currentWeapon = Weapon.TORPEDO;
                setWeaponShootInterval(_torpedoShootInterval);
        }

        // return to original laser after the timer is done
        _weaponUpgradeTimer.start(weaponInterval, 1); // do it just once
    }

    public void playFinalExplosionSound()
    {
        _finalExplosionSound.play();
    }

    private void setWeaponShootInterval(float shootInterval)
    {
        _currentShootInterval = shootInterval;
        _passedTime = shootInterval;
    }

    private void shootLaser()
    {
        LaserRay newLaserRay = new LaserRay(new PointF(_center.x, _center.y + _height/1.5f), _worldRect);
        newLaserRay.setVelocity(new PointF(0.0f, 0.001f));
        newLaserRay.setWidth(0.008f);
        newLaserRay.setHeight(0.05f);
        newLaserRay.setColor(Color.GREEN);
        _laserShootSound.play();
        GameEngine.getInstance().addDrawable(newLaserRay);
        GameEngine.getInstance().addUpdateable(newLaserRay);
        GameEngine.getInstance().addCollidable(newLaserRay);
    }

    private void shootTorpedo()
    {
        Torpedo newTorpedo = new Torpedo(_gameScreenActivity, new PointF(_center.x, _center.y + _height/1.5f),_worldRect);
        newTorpedo.setVelocity(new PointF(_velocity.x/10.0f , 0.001f));
        newTorpedo.setRotationVelocity(0.01f);
        newTorpedo.setWidth(0.13f);
        newTorpedo.setHeight(0.13f);
        _torpedoShootSound.play();
        GameEngine.getInstance().addDrawable(newTorpedo);
        GameEngine.getInstance().addUpdateable(newTorpedo);
        GameEngine.getInstance().addCollidable(newTorpedo);
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
            if(_passedTime >= _currentShootInterval)
            {
                switch(_currentWeapon)
                {
                    case LASER:
                        shootLaser();
                        break;

                    case TORPEDO:
                        shootTorpedo();
                        break;
                }
                _passedTime = 0.0f;
            }
        }

        _weaponUpgradeTimer.update(time);

        // make sure the spaceship is not out of boundaries
        if(_center.x < _worldRect.getLeft() - _worldRect.getWidth()/6.0f)
        {
            _center.x = _worldRect.getLeft() - _worldRect.getWidth()/6.0f;
        }
        else if(_center.x > _worldRect.getRight() + _worldRect.getWidth()/6.0f)
        {
            _center.x = _worldRect.getRight() + _worldRect.getWidth()/6.0f;
        }
        else if(_center.y > _worldRect.getTop() + _worldRect.getHeight()/6.0f)
        {
            _center.y = _worldRect.getTop() + _worldRect.getHeight()/6.0f;
        }
        else if(_center.y < _worldRect.getBottom() - _worldRect.getHeight()/6.0f)
        {
            _center.y = _worldRect.getBottom() - _worldRect.getHeight()/6.0f;
        }
    }
}
