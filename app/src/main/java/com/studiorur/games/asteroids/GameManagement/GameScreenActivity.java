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
        // background color
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        // TESTING
        _gameEngine = new GameEngine();

        // TODO: determine the actual width and height
        DisplayMetrics dm = getResources().getDisplayMetrics();
        _width = dm.widthPixels;
        _height = dm.heightPixels;

        float topBorder = deviceToWorldCoord(new PointF(0.0f, 0.0f)).y;
        float bottomBorder = deviceToWorldCoord(new PointF(0.0f, _height)).y;
        //StarGenerator starGenerator = new StarGenerator(60, 2.0f, 2.0f, topBorder, bottomBorder, -0.0001f);
        StarGenerator starGenerator = new StarGenerator(60, 2.0f, 2.0f, -1.0f, 1.0f, -0.0001f);
        _gameEngine.addComponent(starGenerator);

        SpaceShip ship = new SpaceShip(1.0f, this);
        ship.setTextureIdentifier(Sprite.loadTexture(getResources(), R.drawable.spaceship));
        ship.setCenterX(0.0f);
        ship.setCenterY(0.0f);
        ship.setWidth(0.15f);
        ship.setHeight(0.25f);
        _gameEngine.addComponent(ship);

        // TODO: needs some interface
        _gameEngine.setGameState(GameEngine.GameState.RUNNING);
        _gameEngine.start();
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height)
    {
        int offsetX;
        int offsetY;
        int size;
        _width = width;
        _height = height;

        if (width < height)
        {
            size = height;
            offsetX = -(height - width) / 2;
            offsetY = 0;
            _displayScaleX = (float)width/(float)height;
            _displayScaleY = 1.0f;
        }
        else
        {
            size = width;
            offsetX = 0;
            offsetY = -(width - height) / 2;
            _displayScaleX = 1.0f;
            _displayScaleY = (float)height/(float)width;
        }

        GLES20.glViewport(offsetX, offsetY, size, size);
    }

    @Override
    public void onDrawFrame(GL10 gl10)
    {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

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
