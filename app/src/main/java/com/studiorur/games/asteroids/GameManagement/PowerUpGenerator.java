package com.studiorur.games.asteroids.GameManagement;

import android.content.Context;
import android.graphics.PointF;

import com.studiorur.games.asteroids.Helpers.Rectangle;
import com.studiorur.games.asteroids.Helpers.Utils;
import com.studiorur.games.asteroids.Interfaces.CollidableType;
import com.studiorur.games.asteroids.Interfaces.IUpdatable;
import com.studiorur.games.asteroids.Sprites.PowerUp;

/**
 * Created by zbynek on 11/25/2014.
 */
public class PowerUpGenerator implements IUpdatable
{
    float _screenOffset = 0.4f;
    Rectangle _worldRect;
    CollidableType _collidableType;
    float _timeInterval = 4000.0f;
    float _passedTime = 2000.0f; // first powerup shows in (4000 - 2000), constructor can change
    boolean _isRunning = false;
    Context _context;
    int _textureIdentifier;
    int _soundIdentifier;
    float _powerupWidth;
    float _powerupHeight;
    float _powerupVelocityY;

    public PowerUpGenerator(
            Context context,
            CollidableType collidableType,
            Rectangle worldRect,
            int textureIdentifier,
            int soundIdentifier,
            float powerupWidth,
            float powerupHeight,
            float powerupVelocityY,
            float timeInterval,
            float firstPowerupAfter)
    {
        _context = context;
        _collidableType = collidableType;
        _soundIdentifier = soundIdentifier;
        _worldRect = worldRect;
        _textureIdentifier = textureIdentifier;
        _powerupWidth = powerupWidth;
        _powerupHeight = powerupHeight;
        _powerupVelocityY = powerupVelocityY; // and 0 speed at x direction
        _timeInterval = timeInterval;
        _passedTime = _timeInterval - firstPowerupAfter;
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
                float randX = Utils.randomInRange(_worldRect.getLeft() + _powerupWidth/2.0f, _worldRect.getRight() - _powerupWidth/2.0f);

                //Log.i("powerupX", "powerupX: " + Float.toString(randX));

                PowerUp newPowerUp = new PowerUp(
                        _context,
                        _collidableType,
                        _textureIdentifier,
                        _soundIdentifier,
                        new PointF(randX, _worldRect.getTop() + _screenOffset/3.0f),
                        _worldRect,
                        _screenOffset);
                newPowerUp.setWidth(_powerupWidth);
                newPowerUp.setHeight(_powerupHeight);
                newPowerUp.setVelocity(new PointF(0.0f, _powerupVelocityY));

                GameEngine.getInstance().addDrawable(newPowerUp);
                GameEngine.getInstance().addUpdateable(newPowerUp);
                GameEngine.getInstance().addCollidable(newPowerUp);

                //Log.i("power-up_generator", "power-up added");

                // reset time
                _passedTime = 0.0f;
            }
        }
    }
}
