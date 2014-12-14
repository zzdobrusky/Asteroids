package com.studiorur.games.asteroids.AdaptersViews;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.PointF;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.studiorur.games.asteroids.GameManagement.AsteroidGenerator;
import com.studiorur.games.asteroids.GameManagement.DataModel;
import com.studiorur.games.asteroids.GameManagement.GameEngine;
import com.studiorur.games.asteroids.GameManagement.PowerUpGenerator;
import com.studiorur.games.asteroids.GameManagement.LevelManager;
import com.studiorur.games.asteroids.GameManagement.StarGenerator;
import com.studiorur.games.asteroids.Helpers.Rectangle;
import com.studiorur.games.asteroids.Interfaces.CollidableType;
import com.studiorur.games.asteroids.R;
import com.studiorur.games.asteroids.Sprites.SpaceShip;

import java.io.File;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


public class GameScreenAdapter extends Activity implements GLSurfaceView.Renderer
{
    int _height;
    int _width;
    float _displayScaleX;
    float _displayScaleY;
    Context _context = null;
    GLSurfaceView _surfaceView = null;
    PauseMenuView _pauseMenuView = null;
    GameOverView _gameOverView = null;
    RelativeLayout _rootLayout = null;
    File _scoreFile = null;
    LevelManager _levelManager = null;

    // set up touch listener
    private OnTouchScreenListener _onTouchScreenListener = null;
    public interface OnTouchScreenListener
    {
        public void onTouchScreen(PointF worldLoc, MotionEvent motionEvent);
    }
    public OnTouchScreenListener getOnTouchScreenListener()
    {
        return _onTouchScreenListener;
    }
    public void setOnTouchScreenListener(OnTouchScreenListener onTouchScreenListener)
    {
        _onTouchScreenListener = onTouchScreenListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        _context = this;

        _scoreFile = new File(getFilesDir(), "game_score.txt");

        // lock screen orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        _rootLayout = new RelativeLayout(this);

        _surfaceView = new GLSurfaceView(this);
        _surfaceView.setEGLContextClientVersion(2);
        _surfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        _surfaceView.setRenderer(this);
        LinearLayout.LayoutParams surfaceViewLP = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        _rootLayout.addView(_surfaceView, surfaceViewLP);

        setContentView(_rootLayout);
    }

    private void init()
    {
        // set up on touch listener registered with opengl view (not activity)
        _surfaceView.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                float x = event.getX();
                float y = event.getY();

                PointF worldLoc = deviceToWorldCoord(new PointF(x, y));

                if (_onTouchScreenListener != null)
                    _onTouchScreenListener.onTouchScreen(worldLoc, event);

                return true;
            }
        });

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

        float topBorder = deviceToWorldCoord(new PointF(0.0f, 0.0f)).y;
        float bottomBorder = deviceToWorldCoord(new PointF(0.0f, _height)).y;
        float heightInWorld = topBorder - bottomBorder;
        float leftBorder = deviceToWorldCoord(new PointF(0.0f, 0.0f)).x;
        float rightBorder = deviceToWorldCoord(new PointF(_width, 0.0f)).x;
        float widthInWorld = rightBorder - leftBorder;
        Rectangle worldRect = new Rectangle(widthInWorld, heightInWorld, new PointF(0.0f, 0.0f));

        // *********************** GAME SETUP ***********************

        // set up on game over listener
        GameEngine.getInstance().setOnGameOverListener(new GameEngine.OnGameOverListener()
        {
            @Override
            public void onGameOver()
            {
                DataModel.getInstance(_scoreFile).saveScore();
                gameOver();
            }
        });

        // Background stars - two layers of stars with different speeds will create a parallax effect
        StarGenerator starGeneratorSlower = new StarGenerator(worldRect, 70, 0.001f, 0.01f,  -0.00006f);
        starGeneratorSlower.init();
        GameEngine.getInstance().addDrawable(starGeneratorSlower);
        GameEngine.getInstance().addUpdateable(starGeneratorSlower);
        StarGenerator starGeneratorFaster = new StarGenerator(worldRect, 30, 0.001f, 0.011f, -0.0001f);
        starGeneratorFaster.init();
        GameEngine.getInstance().addDrawable(starGeneratorFaster);
        GameEngine.getInstance().addUpdateable(starGeneratorFaster);

        // Your spaceship
        SpaceShip spaceShip = new SpaceShip(
                this,
                worldRect,
                1.0f,
                R.drawable.spaceship_spreadsheet,
                4,
                4,
                70.0f);
        spaceShip.setCenterX(0.0f);
        spaceShip.setCenterY(0.0f);
        spaceShip.setWidth(0.15f);
        spaceShip.setHeight(0.25f);
        GameEngine.getInstance().addDrawable(spaceShip);
        GameEngine.getInstance().addUpdateable(spaceShip);
        GameEngine.getInstance().addCollidable(spaceShip);

        // Laser Power-ups
        float currentLaserPowerUpInterval = 12000.0f;
        final PowerUpGenerator laserPowerUpGenerator = new PowerUpGenerator(
                _context,
                CollidableType.LASER_POWER_UP,
                worldRect,
                R.drawable.laser_power_up,
                R.raw.laser_recharge,
                0.1f,
                0.1f,
                -0.0003f,
                currentLaserPowerUpInterval,
                2000.0f);
        GameEngine.getInstance().addUpdateable(laserPowerUpGenerator);
        laserPowerUpGenerator.start();

        // Torpedo Power-ups
        float currentTorpedoPowerUpInterval = 16000.0f;
        final PowerUpGenerator torpedoPowerUpGenerator = new PowerUpGenerator(
                _context,
                CollidableType.TORPEDO_POWER_UP,
                worldRect,
                R.drawable.torpedo_power_up,
                R.raw.torpedo_recharge,
                0.1f,
                0.1f,
                -0.0003f,
                currentTorpedoPowerUpInterval,
                8000.0f);
        GameEngine.getInstance().addUpdateable(torpedoPowerUpGenerator);
        torpedoPowerUpGenerator.start();

        // Asteroids
        float currentAsteroidInterval = 300.0f;
        final AsteroidGenerator asteroidGenerator = new AsteroidGenerator(
                _context,
                worldRect,
                0.08f,
                0.5f,
                0.0003f,
                0.001f,
                0.0002f,
                0.004f,
                currentAsteroidInterval);
        asteroidGenerator.start();
        // add to game engine
        GameEngine.getInstance().addUpdateable(asteroidGenerator);

        // set up levels
        _levelManager = new LevelManager(
                15,
                15000.0f,
                asteroidGenerator,
                currentAsteroidInterval,
                100.0f,
                laserPowerUpGenerator,
                currentLaserPowerUpInterval,
                5000.0f,
                torpedoPowerUpGenerator,
                currentTorpedoPowerUpInterval,
                7000.0f);
        _levelManager.start();
        GameEngine.getInstance().addUpdateable(_levelManager);

        // start the game
        GameEngine.getInstance().startGameEngine();
    }

    @Override
    public void onBackPressed()
    {
        if(!GameEngine.getInstance().isGameOver())
        {
            if (GameEngine.getInstance().getGameState() == GameEngine.GameState.RUNNING)
                pauseGame();
            else if (GameEngine.getInstance().getGameState() == GameEngine.GameState.PAUSED)
                resumeGame();
        }
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        _surfaceView.onPause();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        _surfaceView.onResume();
    }

    private void resumeGame()
    {
        // remove pause pop up menu if any
        if(_pauseMenuView != null)
            _rootLayout.removeView(_pauseMenuView);

        GameEngine.getInstance().resumeGameEngine();
    }

    private void resetGame()
    {
        // remove game over pop up menu if any
        if(_gameOverView != null)
            _rootLayout.removeView(_gameOverView);

        GameEngine.getInstance().resetGameEngine();
    }

    private void pauseGame()
    {
        GameEngine.getInstance().pauseGameEngine();
        DataModel.getInstance(_scoreFile).saveScore();

        // create pause popup menu
        _pauseMenuView = new PauseMenuView(_context);
        _pauseMenuView.getResumeButton().setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if(event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    _pauseMenuView.getResumeButton().setBackgroundResource(R.drawable.rounded_button_background_down);

                }
                else if(event.getAction() == MotionEvent.ACTION_UP)
                {
                    _pauseMenuView.getResumeButton().setBackgroundResource(R.drawable.rounded_button_background_up);

                    // Pause game
                    resumeGame();
                }

                return true;
            }
        });

        _pauseMenuView.getMainMenuButton().setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if(event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    _pauseMenuView.getMainMenuButton().setBackgroundResource(R.drawable.rounded_button_background_down);

                }
                else if(event.getAction() == MotionEvent.ACTION_UP)
                {
                    _pauseMenuView.getMainMenuButton().setBackgroundResource(R.drawable.rounded_button_background_up);
                    openMainMenu();
                }

                return true;
            }
        });

        _rootLayout.addView(_pauseMenuView);
    }

    private void gameOver()
    {
        GameEngine.getInstance().pauseGameEngine();
        DataModel.getInstance(_scoreFile).saveScore();

        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                // create game over menu
                _gameOverView = new GameOverView(_context);
                _gameOverView.getMainMenuButton().setOnTouchListener(new View.OnTouchListener()
                {
                    @Override
                    public boolean onTouch(View v, MotionEvent event)
                    {
                        if (event.getAction() == MotionEvent.ACTION_DOWN)
                        {
                            _gameOverView.getMainMenuButton().setBackgroundResource(R.drawable.rounded_button_background_down);

                        } else if (event.getAction() == MotionEvent.ACTION_UP)
                        {
                            _gameOverView.getMainMenuButton().setBackgroundResource(R.drawable.rounded_button_background_up);
                            openMainMenu();
                        }

                        return true;
                    }
                });

                _gameOverView.getPlayAgainButton().setOnTouchListener(new View.OnTouchListener()
                {
                    @Override
                    public boolean onTouch(View v, MotionEvent event)
                    {
                        if (event.getAction() == MotionEvent.ACTION_DOWN)
                        {
                            _gameOverView.getPlayAgainButton().setBackgroundResource(R.drawable.rounded_button_background_down);

                        } else if (event.getAction() == MotionEvent.ACTION_UP)
                        {
                            _gameOverView.getPlayAgainButton().setBackgroundResource(R.drawable.rounded_button_background_up);
                            resetGame();
                        }

                        return true;
                    }
                });

                // update score
                String currentScore = Integer.toString(DataModel.getInstance(_scoreFile).getCurrentScore());
                _gameOverView.getScoreTextView().setText("Your score is: " + currentScore);

                _rootLayout.addView(_gameOverView);
            }
        });
    }

    private void openMainMenu()
    {
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                // Call main menu activity and destroy the current one
                Intent mainMenuIntent = new Intent();
                mainMenuIntent.setClass(_context, MainMenuAdapter.class);
                mainMenuIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(mainMenuIntent);
                ((Activity)_context).finish();
            }
        });

//        Handler handler = new Handler();
//        handler.post( new Runnable()
//        {
//            public void run()
//            {
//                // Call main menu activity and destroy the current one
//                Intent mainMenuIntent = new Intent();
//                mainMenuIntent.setClass(_context, MainMenuAdapter.class);
//                //mainMenuIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(mainMenuIntent);
//                ((Activity)_context).finish();
//            }
//        } );
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig eglConfig)
    {
        init();
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

    @Override
    public void onDrawFrame(GL10 gl10)
    {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        // draw components
        GameEngine.getInstance().draw();
    }

    public PointF deviceToWorldCoord(PointF devLoc)
    {
        PointF worldLoc = new PointF();
        worldLoc.x = (2 * devLoc.x / _width - 1.0f) * _displayScaleX;
        worldLoc.y = (-2 * devLoc.y / _height + 1.0f) * _displayScaleY;

        return worldLoc;
    }
}
