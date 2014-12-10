package com.studiorur.games.asteroids.GameManagement;

import android.content.Context;
import android.graphics.PointF;

import com.studiorur.games.asteroids.Helpers.Rectangle;
import com.studiorur.games.asteroids.Helpers.Utils;
import com.studiorur.games.asteroids.Interfaces.IUpdatable;
import com.studiorur.games.asteroids.Sprites.PowerUp;

/**
 * Created by zbynek on 11/25/2014.
 */
public class PowerupsGenerator implements IUpdatable
{
    float _screenOffset = 0.4f;
    Rectangle _worldRect;
    float _timeInterval = 4000.0f;
    float _passedTime = 0.0f;
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
                PowerUp newPowerUp = new PowerUp(
                        _context,
                        _textureIdentifier,
                        new PointF(randX, _worldRect.getTop() + _screenOffset/2.0f),
                        _worldRect,
                        _screenOffset);
                newPowerUp.setWidth(_powerupWidth);
                newPowerUp.setHeight(_powerupHeight);
                newPowerUp.setVelocity(new PointF(0.0f, _powerupVelocityY));

                GameEngine.getInstance().addUpdateable(newPowerUp);
                GameEngine.getInstance().addCollidable(newPowerUp);

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
