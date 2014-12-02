package com.studiorur.games.asteroids.Sprites;

import android.content.res.Resources;
import android.graphics.RectF;

import com.studiorur.games.asteroids.Helpers.LoopTimer;

/**
 * Created by zbynek on 12/2/2014.
 */
public class AnimatedSprite extends Sprite
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
    // Default is no repetitions, -1 means repeats endlessly till stop animation called
    private int _numOfRepetitions = 0;
    private int _count = _numOfRepetitions;

    public void loadSpritesheet(Resources resources, int resourceIdentifier)
    {
        loadSpritesheet(resources, resourceIdentifier, _numOfRows, _numOfCols, _animationInterval);
    }

    public void loadSpritesheet(
            Resources resources,
            int resourceIdentifier,
            int numOfRows,
            int numOfCols,
            float animationInterval)
    {
        loadTexture(resources, resourceIdentifier);
        _numOfRows = numOfRows;
        _numOfCols = numOfCols;
        _frameWidth = 1.0f/_numOfCols;
        _frameHeight = 1.0f/_numOfRows;
        _animationInterval = animationInterval;
    }

    public void startAnimation(int animatedRow, int numOfRepetitions)
    {
        startAnimation(animatedRow, _startCol, _endCol, numOfRepetitions);
    }

    public void startAnimation(int animatedRow, int startCol, int endCol, int numOfRepetions)
    {
        _animatedRow = animatedRow;
        _startCol = startCol;
        _endCol = endCol;

        // TODO: start timer and call each frame with animation interval till the end
        // and repeat if required
    }

    public void stopAnimation(int stopCol)
    {
        // TODO: stop and destroy timer
    }


    private void setFrame(int row, int col)
    {
        if((row < 0 && row >= _numOfRows) || (col < 0 && col >= _numOfCols))
            return;

        RectF newFrameRect = new RectF(
                row * _frameWidth,
                col * _frameHeight,
                (row + 1) * _frameWidth,
                (col + 1) * _frameWidth);
        setTextureRect(newFrameRect);
    }
}
