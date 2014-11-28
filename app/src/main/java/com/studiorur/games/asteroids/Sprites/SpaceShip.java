package com.studiorur.games.asteroids.Sprites;

import android.graphics.PointF;
import android.graphics.RectF;

import com.studiorur.games.asteroids.Interfaces.Collidable;
import com.studiorur.games.asteroids.Helpers.Circle;
import com.studiorur.games.asteroids.GameManagement.GameScreenActivity;
import com.studiorur.games.asteroids.R;

/**
 * Created by zbynek on 11/25/2014.
 */
public class SpaceShip extends PhysicsSprite implements Collidable
{
    float _invertedMass;
    PointF _externalForce;
    PointF _rocketForce;
    GameScreenActivity _gameScreenActivity = null;
    float _forceCoefficient = 0.000019f;
    float _frictionCoefficient = 0.88f;

    public SpaceShip(float mass, GameScreenActivity gameScreenActivity)
    {
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

    @Override
    public Circle getBoundingCircle()
    {
        float radius = _width > _height ? _width : _height;

        return new Circle(radius, _center);
    }

    @Override
    public RectF getBoundingRectangle()
    {
        return new RectF(
                _center.x - _width/2.0f,
                _center.y - _height/2.0f,
                _center.x + _width/2.0f,
                _center.y + _height/2.0f);
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
        if(circle.contains(rectangle))
        {
            // TODO: do some cool explosion
        }
    }
}
