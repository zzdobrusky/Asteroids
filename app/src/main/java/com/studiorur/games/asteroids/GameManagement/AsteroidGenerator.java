package com.studiorur.games.asteroids.GameManagement;

import android.content.Context;
import android.graphics.PointF;
import android.util.Log;

import com.studiorur.games.asteroids.Helpers.Utils;
import com.studiorur.games.asteroids.Interfaces.IUpdatable;
import com.studiorur.games.asteroids.Sprites.Asteroid;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by zbynek on 11/25/2014.
 */
public class AsteroidGenerator implements IUpdatable
{
    Context _context = null;
    int _textureIdentifier;
    int _numOfAsteroids;
    ArrayList<Asteroid> _unusedAsteroids;
    ArrayList<Asteroid> _activeAsteroids;
    float _width;
    float _height;
    float _minSize;
    float _maxSize;
    float _maxVelocity;
    float _maxRotationVelocity;
    float _screenOffset = 0.4f;
    float _timeInterval;
    float _passedTime = 0.0f;
    boolean _isRunning = false;

    public AsteroidGenerator(
            Context context,
            int textureIdentifier,
            int numOfAsteroids,
            float width,
            float height,
            float minSize,
            float maxSize,
            float maxVelocity,
            float maxRotationVelocity,
            float timeInterval)
    {
        _context = context;
        _textureIdentifier = textureIdentifier;
        _numOfAsteroids = numOfAsteroids;
        _unusedAsteroids = new ArrayList<Asteroid>();
        _activeAsteroids = new ArrayList<Asteroid>();
        _width = width;
        _height = height;
        _minSize = minSize;
        _maxSize = maxSize;
        _maxVelocity = maxVelocity;
        _maxRotationVelocity = maxRotationVelocity;
        _timeInterval = timeInterval;

        // create all asteroids first, active list is empty now - couldn't figure out the other way
        for(int i=0; i<_numOfAsteroids; i++)
            _unusedAsteroids.add(new Asteroid(_context, _textureIdentifier, 1, 4, 60.0f));

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

        randomizeAsteroid(asteroid, new PointF(randX, randY), randSize);
    }

    public void randomizeAsteroid(Asteroid asteroid, PointF center, float size)
    {
        float randVelocityX = Utils.randomInRange(-_maxVelocity, _maxVelocity);
        float randVelocityY = Utils.randomInRange(_maxVelocity / 2.0f, _maxVelocity);
        float randRotationVelocity = Utils.randomInRange(0.0f, _maxRotationVelocity);

        asteroid.setCenter(center);
        asteroid.setWidth(size);
        asteroid.setHeight(size);
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
        if(_passedTime > _timeInterval && _isRunning)
        {
            addAsteroid();

            // reset time
            _passedTime = 0.0f;
        }

        // at the same time check if any asteroids out of boundary and remove from stage if yes
        for(int i=_activeAsteroids.size()-1; i>=0; i--)
            if(isOutOfBoundaries(_activeAsteroids.get(i)))
                removeAsteroid(_activeAsteroids.get(i));

//        Iterator<Asteroid> asteroidIterator = _activeAsteroids.iterator();
//        while(asteroidIterator.hasNext())
//        {
//            Asteroid asteroid = asteroidIterator.next();
//            if(isOutOfBoundaries(asteroid))
//                removeAsteroid(asteroid);
//        }

        _passedTime += time;
    }

    public void addAsteroid()
    {
        if(_unusedAsteroids.size() > 0)
        {
            // randomly throw asteroids with random velocity, rotation, size and shape
            Asteroid newAsteroid = _unusedAsteroids.get(_unusedAsteroids.size() - 1);
            randomizeAsteroid(newAsteroid);
            GameEngine.getInstance().addUpdateable(newAsteroid);
            GameEngine.getInstance().addCollidable(newAsteroid);

            // add to active asteroids
            _activeAsteroids.add(newAsteroid);
            // remove from unused asteroids
            _unusedAsteroids.remove(newAsteroid);
        }
    }

    public void removeAsteroid(Asteroid asteroid)
    {
        if(_activeAsteroids.size() > 0)
        {
            GameEngine.getInstance().removeUpdateable(asteroid);
            GameEngine.getInstance().removeCollidable(asteroid);
            // remove from the active asteroid array list
            _activeAsteroids.remove(asteroid);
            // put it back to stored asteroids
            _unusedAsteroids.add(asteroid);
        }
    }

    @Override
    public void draw()
    {
        // not used
    }
}