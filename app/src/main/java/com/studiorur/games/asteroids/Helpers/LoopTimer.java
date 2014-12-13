package com.studiorur.games.asteroids.Helpers;


import com.studiorur.games.asteroids.Interfaces.IUpdatable;

/**
 * Created by zbynek on 11/26/2014.
 */
public class LoopTimer implements IUpdatable
{
    float _timeInterval;
    float _passedTime;
    int _repeatNumber;
    int _count;
    boolean _isRunning;

    public interface OnTimePassedListener
    {
        public void onTimePassed();
    }
    private OnTimePassedListener _onTimePassedListener = null;
    public OnTimePassedListener getOnTimePassedListener()
    {
        return _onTimePassedListener;
    }
    public void setOnTimePassedListener(OnTimePassedListener onTimePassedListener)
    {
        _onTimePassedListener = onTimePassedListener;
    }

    public LoopTimer()
    {
        _passedTime = 0.0f;
        _count = 0;
        _isRunning = false;
    }

    public void update(float time)
    {
        if(_isRunning)
        {
            _passedTime += time;

            if (_passedTime >= _timeInterval)
            {
                // fire up the event
                if (_onTimePassedListener != null)
                    _onTimePassedListener.onTimePassed();

                // reset interval
                _passedTime = 0.0f;

                // take care of repetitions
                if(_repeatNumber > 0)
                {
                    _count++;
                    if (_count >= _repeatNumber)
                        stop(); // stop and reset timer
                }
            }
        }
    }

    public void start(float timeInterval, int repeatNumber)
    {
        _isRunning = true;
        _count = 0;
        _passedTime = 0.0f;
        _repeatNumber = repeatNumber; // 0 means infinite
        _timeInterval = timeInterval;
    }

    public void stop()
    {
        _isRunning = false;
        _count = 0;
        _passedTime = 0.0f;
        _repeatNumber = 0;
    }
}
