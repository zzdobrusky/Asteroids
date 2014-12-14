package com.studiorur.games.asteroids.GameManagement;

import android.graphics.Color;
import android.graphics.PointF;

import com.studiorur.games.asteroids.Helpers.Rectangle;
import com.studiorur.games.asteroids.Helpers.Utils;
import com.studiorur.games.asteroids.Interfaces.IDrawable;
import com.studiorur.games.asteroids.Interfaces.IUpdatable;
import com.studiorur.games.asteroids.Shapes.Star;

/**
 * Created by zbynek on 11/25/2014.
 */
public class StarGenerator implements IDrawable, IUpdatable
{
    Rectangle _worldRect;
    int _numOfStars;
    Star[] _stars;
    float _minRadius;
    float _maxRadius;
    PointF _constVelocity;
    float _screenOffset;

    public StarGenerator(
            Rectangle worldRect,
            int numOfStars,
            float minRadius,
            float maxRadius,
            float constVelocityY)
    {
        _worldRect = worldRect;
        _numOfStars = numOfStars;
        _stars = new Star[_numOfStars];
        _minRadius = minRadius;
        _maxRadius = maxRadius;
        _screenOffset = 0.02f;
        _constVelocity = new PointF(0.0f, constVelocityY);
    }

    public void init()
    {
        // randomly throw stars with random color and size
        for(int i=0; i<_numOfStars; i++)
        {
            float randX = Utils.randomInRange(-_worldRect.getWidth()/2.0f, _worldRect.getWidth()/2.0f);
            float randY = Utils.randomInRange(-_worldRect.getHeight()/2.0f - _screenOffset, _worldRect.getHeight()/2.0f + _screenOffset); // a little bit above and below
            float randRadius = Utils.randomInRange(_minRadius, _maxRadius);

            _stars[i] = new Star(randX, randY);
            _stars[i].setRadius(randRadius);
            _stars[i].setVelocity(_constVelocity);
            _stars[i].setColor(Color.WHITE);
        }
    }

    private boolean isOutOfBoundaries(Star star)
    {
        return star.getCenterY() < -_worldRect.getHeight()/2.0f - _screenOffset;
    }

    @Override
    public void update(float time)
    {
        for(Star star: _stars)
        {
            // if they are out of bounds
            if(isOutOfBoundaries(star))
            {
                // move them to the beginning
                star.setCenterY(_worldRect.getHeight()/2.0f + _screenOffset);
                // and randomly change x coordinate
                star.setCenterX(Utils.randomInRange(-_worldRect.getWidth()/2.0f, _worldRect.getHeight()/2.0f));
            }

            star.update(time);
        }
    }

    @Override
    public void draw()
    {
        for(Star star: _stars)
            star.draw();
    }
}
