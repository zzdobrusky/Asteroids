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
    int _textureIdentifier;
    float _width;
    float _height;
    float _minSize;
    float _maxSize;
    float _maxVelocity;
    float _maxRotationVelocity;
    float _screenOffset = 0.4f;
    float _timeInterval;
    Rectangle _worldRect;
    float _passedTime = 0.0f;
    boolean _isRunning = false;

    public AsteroidGenerator(
            Context context,
            Rectangle worldRect,
            int textureIdentifier,
            float width,
            float height,
            float minSize,
            float maxSize,
            float maxVelocity,
            float maxRotationVelocity,
            float timeInterval)
    {
        _context = context;
        _worldRect = worldRect;
        _textureIdentifier = textureIdentifier;
        _width = width;
        _height = height;
        _minSize = minSize;
        _maxSize = maxSize;
        _maxVelocity = maxVelocity;
        _maxRotationVelocity = maxRotationVelocity;
        _timeInterval = timeInterval;

        // add itself to a game engine - this will take care of timing
        GameEngine.getInstance().addUpdateable(this);
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

    public void addRandomAsteroid()
    {
            // randomly throw asteroids with random velocity, rotation, size and shape
            Asteroid newAsteroid = new Asteroid(_context, _textureIdentifier, 1, 4, 60.0f, _worldRect, _screenOffset);
            randomizeAsteroid(newAsteroid);
            GameEngine.getInstance().addUpdateable(newAsteroid);
            GameEngine.getInstance().addCollidable(newAsteroid);
    }

    public void removeAsteroid(Asteroid asteroid)
    {
            GameEngine.getInstance().removeUpdateable(asteroid);
            GameEngine.getInstance().removeCollidable(asteroid);
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

                // reset time
                _passedTime = 0.0f;
            }
        }
    }

    @Override
    public void draw()
    {
        // not used
    }
}