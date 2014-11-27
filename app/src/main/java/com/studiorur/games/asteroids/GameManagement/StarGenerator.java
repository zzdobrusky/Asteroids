package com.studiorur.games.asteroids.GameManagement;

import android.graphics.Color;
import android.graphics.PointF;

import com.studiorur.games.asteroids.Helpers.Utils;
import com.studiorur.games.asteroids.Interfaces.Updatable;
import com.studiorur.games.asteroids.Shapes.Star;

/**
 * Created by zbynek on 11/25/2014.
 */
public class StarGenerator implements Updatable
{
    int _numOfStars;
    Star[] _stars;
    float _width;
    float _height;
    float _offsetY;

    public StarGenerator(int numOfStars, float width, float height, float minRadius, float maxRadius, float constVelocityY)
    {
        _numOfStars = numOfStars;
        _stars = new Star[_numOfStars];
        _width = width;
        _height = height;
        _offsetY = 0.02f;

        PointF constVelocity = new PointF(0.0f, constVelocityY);

        // randomly throw stars with random color and size
        for(int i=0; i<_numOfStars; i++)
        {
            float randX = Utils.randomInRange(-_width/2.0f, _width/2.0f);
            float randY = Utils.randomInRange(-_height/2.0f - _offsetY, _height/2.0f + _offsetY); // a little bit above and below
            float randRadius = Utils.randomInRange(minRadius, maxRadius);

            _stars[i] = new Star(randX, randY);
            _stars[i].setRadius(randRadius);
            _stars[i].setVelocity(constVelocity);
            _stars[i].setColor(Color.WHITE);
        }
    }

    @Override
    public void update(float time)
    {
        for(Star star: _stars)
        {
            // if they are out of bounds
            if(star.getCenterY() < -_height/2.0f - _offsetY)
            {
                // move them to the beginning
                star.setCenterY(_height/2.0f + _offsetY);
                // and randomly change x coordinate
                star.setCenterX(Utils.randomInRange(-_width/2.0f, _width/2.0f));
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
