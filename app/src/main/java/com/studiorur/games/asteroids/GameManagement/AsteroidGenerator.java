package com.studiorur.games.asteroids.GameManagement;

import android.content.Context;
import android.graphics.PointF;

import com.studiorur.games.asteroids.Helpers.Rectangle;
import com.studiorur.games.asteroids.Helpers.Utils;
import com.studiorur.games.asteroids.Interfaces.IUpdatable;
import com.studiorur.games.asteroids.Sprites.Asteroid;

/**
 * Created by zbynek on 11/25/2014.
 */
public class AsteroidGenerator implements IUpdatable
{
    Context _context = null;
    float _minSize;
    float _maxSize;
    float _minVelocityY;
    float _maxVelocityY;
    float _maxVelocityX;
    float _maxRotationVelocity;
    float _screenOffset = 0.4f;
    float _timeInterval = 4000.0f; // in milliseconds
    Rectangle _worldRect;
    float _passedTime = 0.0f;
    boolean _isRunning = false;

    public AsteroidGenerator(
            Context context,
            Rectangle worldRect,
            float minSize,
            float maxSize,
            float minVelocityY,
            float maxVelocityY,
            float maxVelocityX,
            float maxRotationVelocity,
            float timeInterval)
    {
        _context = context;
        _worldRect = worldRect;
        _minSize = minSize;
        _maxSize = maxSize;
        _minVelocityY = minVelocityY;
        _maxVelocityY = maxVelocityY;
        _maxVelocityX = maxVelocityX;
        _maxRotationVelocity = maxRotationVelocity;
        _timeInterval = timeInterval;
    }

    public void start()
    {
        _isRunning = true;
    }

    public void stop()
    {
        _isRunning = false;
    }

    private void randomizeAsteroid(Asteroid asteroid)
    {
        float randX = Utils.randomInRange(-_worldRect.getWidth() / 2.0f, _worldRect.getWidth() / 2.0f);
        float centerY = _worldRect.getHeight() / 2.0f + _screenOffset; // do it offscreen only
        float randSize = Utils.randomInRange(_minSize, _maxSize);
        float randVelocityX = Utils.randomInRange(-_maxVelocityX, _maxVelocityX);
        float randVelocityY = Utils.randomInRange(_minVelocityY, _maxVelocityY);
        float randRotationVelocity = Utils.randomInRange(0.0f, _maxRotationVelocity);

        asteroid.setCenter(new PointF(randX, centerY));
        asteroid.setWidth(randSize);
        asteroid.setHeight(randSize);
        asteroid.setVelocity(new PointF(randVelocityX, -randVelocityY));
        asteroid.setRotationVelocity(randRotationVelocity);
    }

    public void addRandomAsteroid()
    {
        // randomly throw asteroids with random velocity, rotation, size and shape
        Asteroid newAsteroid = new Asteroid(_context, _worldRect, _screenOffset);
        randomizeAsteroid(newAsteroid);
        GameEngine.getInstance().addDrawable(newAsteroid);
        GameEngine.getInstance().addUpdateable(newAsteroid);
        GameEngine.getInstance().addCollidable(newAsteroid);
    }

    public void setAsteroidInterval(float timeInterval)
    {
        _timeInterval = timeInterval;
    }

    @Override
    public void update(float time)
    {
        if(_isRunning)
        {
            _passedTime += time;
            if (_passedTime > _timeInterval)
            {
                addRandomAsteroid();

                //Log.i("asteroid_generator", "asteroid added");

                // reset time
                _passedTime = 0.0f;
            }
        }
    }
}