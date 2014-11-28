package com.studiorur.games.asteroids.GameManagement;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.PointF;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;

import com.studiorur.games.asteroids.R;
import com.studiorur.games.asteroids.Sprites.SpaceShip;
import com.studiorur.games.asteroids.Sprites.Sprite;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


public class GameScreenActivity extends Activity implements GLSurfaceView.Renderer
{
    int _height;
    int _width;
    float _displayScaleX;
    float _displayScaleY;

    GameEngine _gameEngine;

    public interface OnTouchScreenListener
    {
        public void onTouchScreen(PointF worldLoc);
    }
    private OnTouchScreenListener _onTouchScreenListener = null;

    public OnTouchScreenListener getOnTouchScreenListener()
    {
        return _onTouchScreenListener;
    }

    public void setOnTouchScreenListener(OnTouchScreenListener _onTouchScreenListener)
    {
        this._onTouchScreenListener = _onTouchScreenListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // lock screen orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        GLSurfaceView surfaceView = new GLSurfaceView(this);
        surfaceView.setEGLContextClientVersion(2);
        surfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        surfaceView.setRenderer(this);
        setContentView(surfaceView);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        float x = event.getX();
        float y = event.getY();

        PointF worldLoc = deviceToWorldCoord(new PointF(x, y));

        if (_onTouchScreenListener != null)
            _onTouchScreenListener.onTouchScreen(worldLoc);

        return true;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig eglConfig)
    {
        // set background color
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height)
    {
        int offsetX;
        int offsetY;
        int size;

        if (width < height)
        {
            size = height;
            offsetX = -(height - width) / 2;
            offsetY = 0;
        }
        else
        {
            size = width;
            offsetX = 0;
            offsetY = -(width - height) / 2;
        }

        GLES20.glViewport(offsetX, offsetY, size, size);
    }

    private void init()
    {
        // extract the screen size
        DisplayMetrics dm = getResources().getDisplayMetrics();
        _width = dm.widthPixels;
        _height = dm.heightPixels;

        if(_width < _height)
        {
            _displayScaleX = (float)_width/(float)_height;
            _displayScaleY = 1.0f;
        }
        else
        {
            _displayScaleX = 1.0f;
            _displayScaleY = (float)_height/(float)_width;
        }

        // set up the game
        _gameEngine = new GameEngine();

        float topBorder = deviceToWorldCoord(new PointF(0.0f, 0.0f)).y;
        float bottomBorder = deviceToWorldCoord(new PointF(0.0f, _height)).y;
        float heightInWorld = topBorder - bottomBorder;
        // Two layers of stars with different speeds will create a parallax effect
        StarGenerator starGeneratorSlower = new StarGenerator(50, 2.0f, heightInWorld, 0.001f, 0.01f, -0.00003f);
        starGeneratorSlower.init();
        _gameEngine.addComponent(starGeneratorSlower);
        StarGenerator starGeneratorFaster = new StarGenerator(20, 2.0f, heightInWorld, 0.008f, 0.015f, -0.0001f);
        starGeneratorFaster.init();
        _gameEngine.addComponent(starGeneratorFaster);

        // Asteroids
        AsteroidGenerator asteroidGenerator = new AsteroidGenerator(10, 2.0f, heightInWorld, 0.05f, 0.5f, 0.0001f, 0.001f);
        asteroidGenerator.init(getResources(), R.drawable.asteroid);
        _gameEngine.addComponent(asteroidGenerator);

        // You
        SpaceShip ship = new SpaceShip(1.0f, this);
        ship.loadTexture(getResources(), R.drawable.spaceship);
        ship.setCenterX(0.0f);
        ship.setCenterY(0.0f);
        ship.setWidth(0.15f);
        ship.setHeight(0.25f);
        _gameEngine.addComponent(ship);



        // TODO: needs some interface
        _gameEngine.start();
    }

    @Override
    public void onDrawFrame(GL10 gl10)
    {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        if(_width <= 0 || _height <= 0)
            init();

        // draw components
        _gameEngine.draw();
    }

    public PointF deviceToWorldCoord(PointF devLoc)
    {
        PointF worldLoc = new PointF();
        worldLoc.x = (2 * devLoc.x / _width - 1.0f) * _displayScaleX;
        worldLoc.y = (-2 * devLoc.y / _height + 1.0f) * _displayScaleY;

        return worldLoc;
    }
}
