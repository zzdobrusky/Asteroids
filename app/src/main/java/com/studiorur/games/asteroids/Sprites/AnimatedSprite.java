package com.studiorur.games.asteroids.Sprites;

import android.content.res.Resources;
import android.graphics.RectF;
import android.util.Log;

import com.studiorur.games.asteroids.Interfaces.IUpdatable;

/**
 * Created by zbynek on 12/2/2014.
 */
public class AnimatedSprite extends Sprite implements IUpdatable
{

    /*
        Assuming each animation has its own row and it animates from left to right. Animates by
        changing column. Rows and columns start at 0.
    */

    private float _animationInterval = 40.0f; // in milliseconds
    private float _passedTime = 0.0f;
    // Default is 0 repetition, it means repeats endlessly till stop animation called
    // if start column and end column are the same value means no animation at all
    private int _numOfRepetitions = 0;
    private int _countRepetitions = 0;
    private boolean _isAnimating = false;

    private OnAnimationStopListener _onAnimationStopListener = null;
    // Default is just one frame
    private int _numOfRows = 1;
    private int _numOfCols = 1;
    private float _frameWidth;
    private float _frameHeight;
    // Default is assuming that you want to show all animation from start to end
    // and it is the top one (index = 0)
    private int _startCol = 0;
    private int _endCol = _numOfCols - 1;
    private int _animatedRow;
    private int _currentCol = _startCol;

    public interface OnAnimationStopListener
    {
        public void onAnimationStop();
    }

    public OnAnimationStopListener getOnAnimationStopListener()
    {
        return _onAnimationStopListener;
    }

    public void setOnAnimationStopListener(OnAnimationStopListener onAnimationStopListener)
    {
        _onAnimationStopListener = onAnimationStopListener;
    }

    protected void loadSpritesheet(
            Resources resources,
            int resourceIdentifier,
            int numOfRows,
            int numOfCols)
    {
        _numOfRows = numOfRows;
        _numOfCols = numOfCols;
        _frameWidth = 1.0f/_numOfCols;
        _frameHeight = 1.0f/_numOfRows;

        // move to the first frame at top left location as default (and no animation)
        loadTexture(resources, resourceIdentifier);
        setFrame(0, 0);
    }

    public void startAnimation(int animatedRow, int startCol, int endCol, float animationInterval, int numOfRepetitions)
    {
        _animatedRow = animatedRow;
        _startCol = startCol;
        _endCol = endCol;
        _animationInterval = animationInterval;
        _numOfRepetitions = numOfRepetitions;

        // Start timer and call each frame with animation interval till the end and repeat if required
        _currentCol = _startCol;
        setFrame(_animatedRow, _currentCol);
        _isAnimating = true;
    }

    public void stopAnimation()
    {
        // Stop and destroy timer
        _isAnimating = false;
        _countRepetitions = 0;

        Log.i("stop_animation", "currCol: " + _currentCol);

        // register with listener
        if(_onAnimationStopListener != null)
            _onAnimationStopListener.onAnimationStop();
    }

    private void setFrame(int row, int col)
    {
        if((row < 0 && row >= _numOfRows) || (col < 0 && col >= _numOfCols))
            return;

        RectF newFrameRect = new RectF(
                col * _frameWidth,
                row * _frameHeight,
                (col + 1) * _frameWidth,
                (row + 1) * _frameHeight);
        setTextureRect(newFrameRect);
    }

    public void setAnimatedRow(int newAnimatedRow)
    {
        _animatedRow = newAnimatedRow;
    }

    public void setNumOfRepetitions(int numOfRepetitions)
    {
        _numOfRepetitions = numOfRepetitions;
    }


    @Override
    public void update(float time)
    {
        if(_isAnimating)
        {
            _passedTime += time;
            if(_passedTime >= _animationInterval)
            {
                _currentCol++;
                if (_currentCol > _endCol)
                    _currentCol = _startCol;

                setFrame(_animatedRow, _currentCol);

                //Log.i("frame", "frame :" + _currentCol);

                // check if to stop the animation, 0 is infinite repetitions
                if (_numOfRepetitions != 0 && _currentCol == _endCol)
                {
                    _countRepetitions++;
                    if (_countRepetitions >= _numOfRepetitions)
                        stopAnimation();
                }

                _passedTime = 0.0f;
            }
        }
    }
}
