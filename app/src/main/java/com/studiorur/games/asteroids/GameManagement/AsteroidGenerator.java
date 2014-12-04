package com.studiorur.games.asteroids.GameManagement;

import android.content.Context;
import android.graphics.PointF;

import com.studiorur.games.asteroids.Helpers.Utils;
import com.studiorur.games.asteroids.Interfaces.ICollidable;
import com.studiorur.games.asteroids.Interfaces.IUpdatable;
import com.studiorur.games.asteroids.Sprites.Asteroid;

import java.util.ArrayList;

/**
 * Created by zbynek on 11/25/2014.
 */
public class AsteroidGenerator implements IUpdatable
{
    int _numOfAsteroids;
    ArrayList<ICollidable> _asteroids;
    float _width;
    float _height;
    float _minSize;
    float _maxSize;
    float _maxVelocity;
    float _maxRotationVelocity;
    float _screenOffset;
    float _timeInterval = 1000.0f; // testing frequency of creating asteroids
    float _passedTime;

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
        _asteroids = new ArrayList<ICollidable>(_numOfAsteroids);
        _width = width;
        _height = height;
        _minSize = minSize;
        _maxSize = maxSize;
        _maxVelocity = maxVelocity;
        _maxRotationVelocity = maxRotationVelocity;
        _screenOffset = 0.2f;
    }

    public ArrayList<ICollidable> getCollidables()
    {
        return _asteroids;
    }

    public void init(Context context, int textureIdentifier)
    {
        // randomly throw asteroids with random velocity, rotation, size and shape
        for (int i = 0; i < _numOfAsteroids; i++)
        {
            Asteroid newAsteroid = new Asteroid(context);
            newAsteroid.loadSpritesheet(context.getResources(), textureIdentifier);
            randomizeAsteroid(newAsteroid);
            _asteroids.add(newAsteroid);
        }
    }

    private void randomizeAsteroid(Asteroid asteroid)
    {
        float randX = Utils.randomInRange(-_width / 2.0f, _width / 2.0f);
        float randY = Utils.randomInRange(_height / 2.0f, _height / 2.0f + _screenOffset); // do it offscreen only
        float randSize = Utils.randomInRange(_minSize, _maxSize);
        float randVelocityX = Utils.randomInRange(-_maxVelocity, _maxVelocity);
        float randVelocityY = Utils.randomInRange(_maxVelocity / 2.0f, _maxVelocity);
        float randRotationVelocity = Utils.randomInRange(0.0f, _maxRotationVelocity);

        asteroid.setCenter(new PointF(randX, randY));
        asteroid.setWidth(randSize);
        asteroid.setHeight(randSize);
        asteroid.setVelocity(new PointF(randVelocityX, -randVelocityY));
        asteroid.setRotationVelocity(randRotationVelocity);
    }

    private boolean isOutOfBoundaries(Asteroid asteroid)
    {
        return asteroid.getCenter().y < -_height / 2.0f - _screenOffset ||
                asteroid.getCenter().y > _height / 2.0f + _screenOffset ||
                asteroid.getCenter().x < -_width / 2.0f - _screenOffset ||
                asteroid.getCenter().y > _width / 2.0f + _screenOffset;
    }

    @Override
    public void draw()
    {
        for (ICollidable asteroid : _asteroids)
            ((Asteroid)asteroid).draw();
    }

    @Override
    public void update(float time)
    {
        for (ICollidable asteroid : _asteroids)
        {

            // if they are out of bounds destroy them
            if (isOutOfBoundaries(((Asteroid)asteroid)))
            {
                // move them to the beginning
                ((Asteroid)asteroid).setCenterY(_height / 2.0f + _screenOffset);
                // and randomize
                randomizeAsteroid(((Asteroid)asteroid));
            }

            ((Asteroid)asteroid).update(time);
        }
    }
}