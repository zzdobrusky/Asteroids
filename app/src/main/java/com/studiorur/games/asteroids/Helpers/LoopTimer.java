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
        _passedTime = timeInterval;
        _repeatNumber = repeatNumber; // 0 means infinite
        _count = repeatNumber;
    }

    public void update(float time)
    {
        _passedTime -= time;

        if(_passedTime <= 0.0f && _count >= 0)
        {
            // fire up the event
            if(_onTimePassedListener != null)
                _onTimePassedListener.onTimePassed();

            // reset interval to start over
            _passedTime = _timeInterval;

            if(_repeatNumber > 0)
                _count--;
        }
    }
}
