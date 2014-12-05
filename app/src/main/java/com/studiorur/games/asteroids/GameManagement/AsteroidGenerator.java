package com.studiorur.games.asteroids.GameManagement;

import android.content.Context;
import android.graphics.PointF;

import com.studiorur.games.asteroids.Helpers.Utils;
import com.studiorur.games.asteroids.Interfaces.IUpdatable;
import com.studiorur.games.asteroids.Sprites.Asteroid;

import java.util.ArrayList;

/**
 * Created by zbynek on 11/25/2014.
 */
public class AsteroidGenerator implements IUpdatable
{
    Context _context = null;
    GameEngine _gameEngine = null;
    int _textureIdentifier;
    ArrayList<Asteroid> _asteroids;
    float _width;
    float _height;
    float _minSize;
    float _maxSize;
    float _maxVelocity;
    float _maxRotationVelocity;
    float _screenOffset;
    float _timeInterval;
    float _passedTime = 0.0f;
    boolean _isRunning = false;

    public AsteroidGenerator(
            Context context,
            GameEngine gameEngine,
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
        _gameEngine = gameEngine;
        _textureIdentifier = textureIdentifier;
        _asteroids = new ArrayList<Asteroid>();
        _width = width;
        _height = height;
        _minSize = minSize;
        _maxSize = maxSize;
        _maxVelocity = maxVelocity;
        _maxRotationVelocity = maxRotationVelocity;
        _timeInterval = timeInterval;
        _screenOffset = 0.2f;

        // add itself to a game engine
        _gameEngine.addUpdateable(this);
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

    private boolean isOutOfBoundaries(Asteroid asteroid)
    {
        return asteroid.getCenter().y < -_height / 2.0f - _screenOffset ||
                asteroid.getCenter().y > _height / 2.0f + _screenOffset ||
                asteroid.getCenter().x < -_width / 2.0f - _screenOffset ||
                asteroid.getCenter().y > _width / 2.0f + _screenOffset;
    }

    @Override
    public void update(float time)
    {
        // this is just for a timer and to check if any asteroids out of boundaries
        if(_passedTime > _timeInterval && _isRunning)
        {
            // randomly throw asteroids with random velocity, rotation, size and shape
            Asteroid newAsteroid = new Asteroid(_context);
            newAsteroid.loadSpritesheet(_context.getResources(), _textureIdentifier);
            randomizeAsteroid(newAsteroid);
            _asteroids.add(newAsteroid);

            // register with the game engine
            _gameEngine.addCollidable(newAsteroid);

            // reset time
            _passedTime = 0.0f;
        }

        // at the same time check if any asteroids out of boundary and remove if yes
        for(int i=_asteroids.size()-1; i>=0; i--)
        {
            if(isOutOfBoundaries(_asteroids.get(i)))
            {
                // remove from the game engine
                Asteroid asteroid = _asteroids.get(i);
                _gameEngine.removeCollidable(asteroid);
                // remove from the internal array - don't know better now
                _asteroids.remove(asteroid);
                asteroid = null; // ready it for the garbage collector - not sure if necessary
            }
        }

        _passedTime += time;
    }

    @Override
    public void draw()
    {
        for(int i=_asteroids.size()-1; i>=0; i--)
            _asteroids.get(i).draw();
    }
}