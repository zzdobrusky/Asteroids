package com.studiorur.games.asteroids.GameManagement;

import android.graphics.Color;
import android.graphics.PointF;
import android.util.FloatMath;

import com.studiorur.games.asteroids.DataModel.Utils;
import com.studiorur.games.asteroids.Interfaces.Updatable;
import com.studiorur.games.asteroids.Shapes.Star;

/**
 * Created by zbynek on 11/25/2014.
 */
public class StarGenerator implements Updatable
{
    int _numOfStars;
    Star[] _stars;

    public StarGenerator(int numOfStars, float width, float height, float minRadius, float maxRadius, float constVelocityY)
    {
        _numOfStars = numOfStars;
        _stars = new Star[_numOfStars];

        PointF constVelocity = new PointF(0.0f, constVelocityY);

        // randomly throw stars with random color and size
        for(int i=0; i<_numOfStars; i++)
        {
            float randX = Utils.randomInRange(-width/2, width/2);
            float randY = Utils.randomInRange(-height/2, height/2);
            float randRadius = Utils.randomInRange(minRadius, maxRadius);

            _stars[i] = new Star(randX, randY);
            _stars[i].setRadius(randRadius);
            _stars[i].setVelocity(constVelocity);
            _stars[i].setColor(Color.WHITE);
        }

        // if they are out of bounds just move them at the beginning and randomly change x coordinate

    }

    @Override
    public void update(float time)
    {
        for(Star star: _stars)
            star.update(time);
    }

    @Override
    public void draw()
    {
        for(Star star: _stars)
            star.draw();
    }
}
