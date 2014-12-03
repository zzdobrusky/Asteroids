package com.studiorur.games.asteroids.Sprites;

import android.graphics.PointF;

import com.studiorur.games.asteroids.Helpers.Boundary;
import com.studiorur.games.asteroids.Helpers.SoundFX;
import com.studiorur.games.asteroids.Interfaces.ICollidable;
import com.studiorur.games.asteroids.Adapters.GameScreenActivity;
import com.studiorur.games.asteroids.R;

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
    GameScreenActivity _gameScreenActivity = null;
    float _forceCoefficient = 0.000019f;
    float _frictionCoefficient = 0.88f;
    Boundary _boundary;


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

    public SpaceShip(float mass, GameScreenActivity gameScreenActivity)
    {
        _gameScreenActivity = gameScreenActivity;
        _invertedMass = 1.0f/mass;

        // create boundary
        _boundary = new Boundary(false);

        startAnimation(0, 1, 3, 25.0f, 0);

        resetForces();

        // start listening for touch events (will propel the spaceship)
        _gameScreenActivity.setOnTouchScreenListener(new GameScreenActivity.OnTouchScreenListener()
        {
            @Override
            public void onTouchScreen(PointF worldLoc)
            {
                // update force
                float forceX = (worldLoc.x - _center.x) * _forceCoefficient;
                float forceY = (worldLoc.y - _center.y) * _forceCoefficient;
                _rocketForce = new PointF(forceX, forceY);
            }
        });
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

    @Override
    public Boundary getBoundery()
    {
        _boundary.updateBoundary(_width, _height, _center);

        return _boundary;
    }

    @Override
    public void collide(ICollidable object)
    {
        if(getBoundery().contains(object.getBoundery()))
        {
            // TODO: do some awesome explosion and sound effect
            //Log.i("collision", "ship collided");
            SoundFX.getInstance().play(R.raw.shot, 1.0f);
        }
    }
}
