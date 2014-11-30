package com.studiorur.games.asteroids.Helpers;

import android.graphics.PointF;

/**
 * Created by zbynek on 11/29/2014.
 */
public class Rectangle
{
    protected PointF _center;
    protected float _width;
    protected float _height;

    public PointF getCenter()
    {
        return _center;
    }

    public void setCenter(PointF center)
    {
        _center = center;
    }

    public float getWidth()
    {
        return _width;
    }

    public void setWidth(float width)
    {
        _width = width;
    }

    public float getHeight()
    {
        return _height;
    }

    public void setHeight(float height)
    {
        _height = height;
    }

    public float getLeft()
    {
        return _center.x - _width/2.0f;
    }

    public float getTop()
    {
        return _center.y + _height/2.0f;
    }

    public float getRight()
    {
        return _center.x + _width/2.0f;
    }

    public float getBottom()
    {
        return _center.y - _height/2.0f;
    }

    public Rectangle()
    {
        _width = 0.5f;
        _height = 0.5f;
        _center = new PointF(0.0f, 0.0f);
    }

    public Rectangle(float width, float height, PointF center)
    {
        _width = width;
        _height = height;
        _center = center;
    }

    public boolean contains(Rectangle rect)
    {
        return (Math.abs(_center.x - rect.getCenter().x) <= (_width/2.0f + rect.getWidth()/2.0f)) &&
               (Math.abs(_center.y - rect.getCenter().y) <= (_height/2.0f + rect.getHeight()/2.0f));
    }
}
