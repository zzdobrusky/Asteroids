package com.studiorur.games.asteroids.Sprites;

import android.content.res.Resources;
import android.graphics.RectF;

import com.studiorur.games.asteroids.Helpers.LoopTimer;
import com.studiorur.games.asteroids.Interfaces.IUpdatable;

/**
 * Created by zbynek on 12/2/2014.
 */
public class AnimatedSprite extends Sprite implements IUpdatable
{
    private float _animationInterval = 40.0f; // in milliseconds
    private LoopTimer _timer = null;

    /*
        Assuming each animation has its own row and it animates from left to right. Animates by
        changing column. Rows and columns start at 0.
    */

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
    // Default is 1 repetition, 0 means repeats endlessly till stop animation called
    private int _numOfRepetitions = 1;
    private int _count = _numOfRepetitions;

    public void loadSpritesheet(Resources resources, int resourceIdentifier)
    {
        loadSpritesheet(resources, resourceIdentifier, 1, 1, _animationInterval);
    }

    public void loadSpritesheet(
            Resources resources,
            int resourceIdentifier,
            int numOfRows,
            int numOfCols,
            float animationInterval)
    {
        _numOfRows = numOfRows;
        _numOfCols = numOfCols;
        _frameWidth = 1.0f/_numOfCols;
        _frameHeight = 1.0f/_numOfRows;
        _animationInterval = animationInterval;

        // move to the first frame as default
        loadTexture(resources, resourceIdentifier);
        setFrame(0, 0);
    }

    public void initAnimation(int animatedRow, int numOfRepetitions)
    {
        // mostly default
        initAnimation(animatedRow, 0, 0, _animationInterval, numOfRepetitions);
    }

    public void initAnimation(int animatedRow, int startCol, int endCol, float animationInterval, int numOfRepetitions)
    {
        _animatedRow = animatedRow;
        _startCol = startCol;
        _endCol = endCol;
        _animationInterval = animationInterval;


        // Start timer and call each frame with animation interval till the end and repeat if required
        _currentCol = _startCol;
        int numOfFrames = _endCol - _startCol;
        _timer = new LoopTimer(_animationInterval, (numOfFrames + 1) * numOfRepetitions);
    }

    public void startAnimation()
    {
        _timer.setOnTimePassedListener(new LoopTimer.OnTimePassedListener()
        {
            @Override
            public void onTimePassed()
            {
                setFrame(_animatedRow, _currentCol);
                _currentCol++;

                if(_currentCol > _endCol)
                    _currentCol = _startCol;
            }
        });
    }

    public void stopAnimation()
    {
        stopAnimation(_startCol);
    }

    public void stopAnimation(int stopFrame)
    {
        // Stop and destroy timer
        _timer.stop();
        _timer = null;

        // set frame
        setFrame(_animatedRow, stopFrame);
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


    @Override
    public void update(float time)
    {
        if(_timer != null)
            _timer.update(time);
    }
}
