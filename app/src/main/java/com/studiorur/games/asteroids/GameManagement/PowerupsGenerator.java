package com.studiorur.games.asteroids.GameManagement;

import android.content.Context;
import android.graphics.PointF;

import com.studiorur.games.asteroids.Helpers.Rectangle;
import com.studiorur.games.asteroids.Helpers.Utils;
import com.studiorur.games.asteroids.Interfaces.IUpdatable;
import com.studiorur.games.asteroids.Sprites.LaserPowerUp;

/**
 * Created by zbynek on 11/25/2014.
 */
public class PowerupsGenerator implements IUpdatable
{
    float _screenOffset = 0.4f;
    Rectangle _worldRect;
    float _timeInterval = 4000.0f;
    float _passedTime = 2000.0f; // first powerup shows in (4000 - 2000)
    boolean _isRunning = false;
    Context _context;
    int _textureIdentifier;
    float _powerupWidth;
    float _powerupHeight;
    float _powerupVelocityY;


    public PowerupsGenerator(Context context,
                             Rectangle worldRect,
                             int textureIdentifier,
                             float powerupWidth,
                             float powerupHeight,
                             float powerupVelocityY)
    {
        _context = context;
        _worldRect = worldRect;
        _textureIdentifier = textureIdentifier;
        _powerupWidth = powerupWidth;
        _powerupHeight = powerupHeight;
        _powerupVelocityY = powerupVelocityY; // and 0 speed at x direction
    }

    public void start()
    {
        _isRunning = true;
    }

    public void stop()
    {
        _isRunning = false;
    }

    public void setPowerupFrequency(float timeInterval)
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
                // add power-up
                float randX = Utils.randomInRange(-2.0f, 2.0f);
                LaserPowerUp newLaserPowerUp = new LaserPowerUp(
                        _context,
                        _textureIdentifier,
                        new PointF(randX, _worldRect.getTop() + _screenOffset/2.0f),
                        _worldRect,
                        _screenOffset);
                newLaserPowerUp.setWidth(_powerupWidth);
                newLaserPowerUp.setHeight(_powerupHeight);
                newLaserPowerUp.setVelocity(new PointF(0.0f, _powerupVelocityY));

                GameEngine.getInstance().addUpdateable(newLaserPowerUp);
                GameEngine.getInstance().addCollidable(newLaserPowerUp);

                //Log.i("power-up_generator", "power-up added");

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
