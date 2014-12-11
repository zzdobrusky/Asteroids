package com.studiorur.games.asteroids.GameManagement;

import android.util.Log;

import com.studiorur.games.asteroids.Helpers.LoopTimer;
import com.studiorur.games.asteroids.Interfaces.IUpdatable;

/**
 * Created by zbynek on 12/11/2014.
 */
public class LevelManager implements IUpdatable
{
    // Game level related (all in milliseconds)
    float _levelTimeInterval; // after this time the game difficulty get increased
    float _asteroidIntervalDecrease;
    float _currentAsteroidInterval;
    float _laserPowerupIntervalDecrease;
    float _currentPowerUpInterval;
    LoopTimer _timer;
    int _numOfLevels;
    AsteroidGenerator _asteroidGenerator = null;
    LaserPowerUpsGenerator _laserPowerUpsGenerator = null;

    public LevelManager(
            int numOfLevels,
            float levelTimeInterval,
            AsteroidGenerator asteroidGenerator,
            float currentAsteroidInterval,
            float minAsteroidInterval,
            LaserPowerUpsGenerator laserPowerUpsGenerator,
            float currentPowerUpInterval,
            float minLaserPowerUpInterval)
    {
        _numOfLevels = numOfLevels;
        _levelTimeInterval = levelTimeInterval;
        _asteroidGenerator = asteroidGenerator;
        _currentAsteroidInterval = currentAsteroidInterval;
        _laserPowerUpsGenerator = laserPowerUpsGenerator;
        _currentPowerUpInterval = currentPowerUpInterval;

        // calculate decrease in intervals for each level
        _asteroidIntervalDecrease = (_currentAsteroidInterval - minAsteroidInterval)/(float)_numOfLevels;
        _laserPowerupIntervalDecrease = (_currentPowerUpInterval - minLaserPowerUpInterval)/(float)_numOfLevels;

        // start level manager
        _timer = new LoopTimer();
        _timer.setOnTimePassedListener(new LoopTimer.OnTimePassedListener()
        {
            @Override
            public void onTimePassed()
            {
                // increase level difficulty
                _currentAsteroidInterval -= _asteroidIntervalDecrease;
                _asteroidGenerator.setAsteroidInterval(_currentAsteroidInterval);

                _currentPowerUpInterval -= _laserPowerupIntervalDecrease;
                _laserPowerUpsGenerator.setPowerupFrequency(_currentPowerUpInterval);

                Log.i("level", "Asteroid interval: " + _currentAsteroidInterval + " Laser interval: " + _currentPowerUpInterval);
            }
        });
    }

    public void start()
    {
        _timer.start(_levelTimeInterval, _numOfLevels);
    }

    @Override
    public void update(float time)
    {
       _timer.update(time);
    }

    @Override
    public void draw()
    {
        // not used
    }
}
