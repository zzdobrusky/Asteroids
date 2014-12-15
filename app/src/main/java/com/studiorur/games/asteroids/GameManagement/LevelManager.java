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
    float _torpedoPowerupIntervalDecrease;
    float _currentLaserPowerUpInterval;
    float _currentTorpedoPowerUpInterval;
    LoopTimer _timer;
    int _numOfLevels;
    AsteroidGenerator _asteroidGenerator = null;
    PowerUpGenerator _laserPowerUpGenerator = null;
    PowerUpGenerator _torpedoPowerUpGenerator = null;

    public LevelManager(
            int numOfLevels,
            float levelTimeInterval,
            AsteroidGenerator asteroidGenerator,
            float currentAsteroidInterval,
            float minAsteroidInterval,
            PowerUpGenerator laserPowerUpGenerator,
            float currentLaserPowerUpInterval,
            float minLaserPowerUpInterval,
            PowerUpGenerator torpedoPowerUpGenerator,
            float currentTorpedoPowerUpInterval,
            float minTorpedoPowerUpInterval)
    {
        _numOfLevels = numOfLevels;
        _levelTimeInterval = levelTimeInterval;
        _asteroidGenerator = asteroidGenerator;
        _currentAsteroidInterval = currentAsteroidInterval;
        _laserPowerUpGenerator = laserPowerUpGenerator;
        _currentLaserPowerUpInterval = currentLaserPowerUpInterval;
        _torpedoPowerUpGenerator = torpedoPowerUpGenerator;
        _currentTorpedoPowerUpInterval = currentTorpedoPowerUpInterval;

        // calculate decrease in intervals for each level
        _asteroidIntervalDecrease = (_currentAsteroidInterval - minAsteroidInterval)/(float)_numOfLevels;
        _laserPowerupIntervalDecrease = (_currentLaserPowerUpInterval - minLaserPowerUpInterval)/(float)_numOfLevels;
        _torpedoPowerupIntervalDecrease = (_currentTorpedoPowerUpInterval - minTorpedoPowerUpInterval)/(float)_numOfLevels;

        // start level manager
        _timer = new LoopTimer();
        _timer.setOnTimePassedListener(new LoopTimer.OnTimePassedListener()
        {
            @Override
            public void onTimePassed()
            {
                // Increase level difficulty
                // Increase number of asteroids
                _currentAsteroidInterval -= _asteroidIntervalDecrease;
                _asteroidGenerator.setAsteroidInterval(_currentAsteroidInterval);
                // Increase number of laser powerups
                _currentLaserPowerUpInterval -= _laserPowerupIntervalDecrease;
                _laserPowerUpGenerator.setPowerupFrequency(_currentLaserPowerUpInterval);
                // Increase number of torpedo powerups
                _currentTorpedoPowerUpInterval -= _torpedoPowerupIntervalDecrease;
                _torpedoPowerUpGenerator.setPowerupFrequency(_currentTorpedoPowerUpInterval);

                //Log.i("level", "Asteroid interval: " + _currentAsteroidInterval + " Laser interval: " + _currentLaserPowerUpInterval);
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
}
