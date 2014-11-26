package com.studiorur.games.asteroids.Sprites;

import android.graphics.PointF;

import com.studiorur.games.asteroids.GameManagement.GameScreenActivity;

/**
 * Created by zbynek on 11/25/2014.
 */
public class SpaceShip extends PhysicsSprite
{
    float _invertedMass;
    PointF _externalForce;
    PointF _rocketForce;
    GameScreenActivity _gameScreenActivity = null;
    float _forceCoefficient = 0.000019f;
    float _frictionCoefficient = 0.88f;

    public SpaceShip(float mass, GameScreenActivity gameScreenActivity)
    {
        super();
        _gameScreenActivity = gameScreenActivity;
        _invertedMass = 1.0f/mass;

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
}
