package com.studiorur.games.asteroids.GameManagement;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.PointF;

import com.studiorur.games.asteroids.Helpers.Boundary;
import com.studiorur.games.asteroids.Helpers.Utils;
import com.studiorur.games.asteroids.Interfaces.Collidable;
import com.studiorur.games.asteroids.Interfaces.Updatable;
import com.studiorur.games.asteroids.R;
import com.studiorur.games.asteroids.Sprites.Asteroid;
import com.studiorur.games.asteroids.Sprites.Sprite;

/**
 * Created by zbynek on 11/25/2014.
 */
public class AsteroidGenerator implements Updatable, Collidable
{
    int _numOfAsteroids;
    Asteroid[] _asteroids;
    float _width;
    float _height;
    float _minSize;
    float _maxSize;
    float _maxVelocity;
    float _maxRotationVelocity;
    float _screenOffset;

    public AsteroidGenerator(
            int numOfAsteroids,
            float width,
            float height,
            float minSize,
            float maxSize,
            float maxVelocity,
            float maxRotationVelocity)
    {
        _numOfAsteroids = numOfAsteroids;
        _asteroids = new Asteroid[_numOfAsteroids];
        _width = width;
        _height = height;
        _minSize = minSize;
        _maxSize = maxSize;
        _maxVelocity = maxVelocity;
        _maxRotationVelocity = maxRotationVelocity;
        _screenOffset = 0.2f;
    }

    public void init(Context context, int textureIdentifier)
    {
        // randomly throw asteroids with random velocity, rotation, size and shape
        for(int i=0; i<_numOfAsteroids; i++)
        {
            _asteroids[i] = new Asteroid(context);
            _asteroids[i].loadTexture(context.getResources(), textureIdentifier);
            randomizeAsteroid(_asteroids[i]);
        }
    }

    private void  randomizeAsteroid(Asteroid asteroid)
    {
        float randX = Utils.randomInRange(-_width / 2.0f, _width / 2.0f);
        float randY = Utils.randomInRange(_height/2.0f, _height/2.0f + _screenOffset); // do it offscreen only
        float randWidth = Utils.randomInRange(_minSize, _maxSize);
        float randHeight = Utils.randomInRange(_minSize, _maxSize);
        float randVelocityX = Utils.randomInRange(-_maxVelocity, _maxVelocity);
        float randVelocityY = Utils.randomInRange(_maxVelocity/2.0f, _maxVelocity);
        float randRotationVelocity = Utils.randomInRange(0.0f, _maxRotationVelocity);

        asteroid.setCenter(new PointF(randX, randY));
        asteroid.setWidth(randWidth);
        asteroid.setHeight(randHeight);
        asteroid.setVelocity(new PointF(randVelocityX, -randVelocityY));
        asteroid.setRotationVelocity(randRotationVelocity);
    }

    private boolean isOutOfBoundaries(Asteroid asteroid)
    {
        return asteroid.getCenter().y < -_height/2.0f - _screenOffset ||
               asteroid.getCenter().y > _height/2.0f + _screenOffset ||
               asteroid.getCenter().x < -_width/2.0f - _screenOffset ||
               asteroid.getCenter().y > _width/2.0f + _screenOffset;
    }


    @Override
    public void update(float time)
    {
        for(Asteroid asteroid: _asteroids)
        {
            // if they are out of bounds destroy them
            if(isOutOfBoundaries(asteroid))
            {
                // move them to the beginning
                asteroid.setCenterY(_height / 2.0f + _screenOffset);
                // and randomize
                randomizeAsteroid(asteroid);
            }

            asteroid.update(time);
        }
    }

    @Override
    public void draw()
    {
        for(Asteroid asteroid: _asteroids)
            asteroid.draw();
    }

    @Override
    public Boundary getBoundery()
    {
        // not used
        return null;
    }

    @Override
    public void collide(Collidable object)
    {
        for(Asteroid asteroid: _asteroids)
            asteroid.collide(object);
    }
}
