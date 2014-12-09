package com.studiorur.games.asteroids.GameManagement;

import com.studiorur.games.asteroids.Helpers.Rectangle;
import com.studiorur.games.asteroids.Interfaces.IUpdatable;

/**
 * Created by zbynek on 11/25/2014.
 */
public class PowerupsGenerator implements IUpdatable
{
    float _screenOffset = 0.4f;
    float _timeInterval;
    Rectangle _worldRect;
    float _passedTime = 0.0f;
    boolean _isRunning = false;

    public PowerupsGenerator(GameEngine gameEngine)
    {

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


                //Log.i("power-up_generator", "power-up added");

                // reset time
                _passedTime = 0.0f;
            }
        }
    }

    @Override
    public void draw()
    {

    }
}
