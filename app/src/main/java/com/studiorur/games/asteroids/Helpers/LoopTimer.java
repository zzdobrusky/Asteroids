package com.studiorur.games.asteroids.Helpers;

/**
 * Created by zbynek on 11/26/2014.
 */
public class LoopTimer
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

    public LoopTimer(float timeInterval, int repeatNumber)
    {
        _timeInterval = timeInterval;
        _passedTime = 0.0f;
        _repeatNumber = repeatNumber; // 0 means infinite
        _count = 0;
        _isRunning = false;
    }

    public void update(float time)
    {
        if(_isRunning && _count <= _repeatNumber)
        {
            _passedTime += time;

            if (_passedTime >= _timeInterval)
            {
                // fire up the event
                if (_onTimePassedListener != null)
                    _onTimePassedListener.onTimePassed();

                // reset interval to start over
                _passedTime = 0.0f;

                if (_repeatNumber > 0)
                    _count++;
            }
        }
    }

    public void start()
    {
        _isRunning = true;
        _count = 0;
        _passedTime = 0.0f;
    }

    public void stop()
    {
        _isRunning = false;
    }
}
