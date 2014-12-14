package com.studiorur.games.asteroids.GameManagement;

import com.studiorur.games.asteroids.Interfaces.CollidableType;
import com.studiorur.games.asteroids.Interfaces.ICollidable;
import com.studiorur.games.asteroids.Interfaces.IDrawable;
import com.studiorur.games.asteroids.Interfaces.IUpdatable;
import com.studiorur.games.asteroids.Shapes.LaserRay;
import com.studiorur.games.asteroids.Sprites.AnimatedSprite;
import com.studiorur.games.asteroids.Sprites.Asteroid;
import com.studiorur.games.asteroids.Sprites.PowerUp;
import com.studiorur.games.asteroids.Sprites.SpaceShip;
import com.studiorur.games.asteroids.Sprites.Torpedo;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by zbynek on 11/24/2014.
 */
public class GameEngine extends Thread
{
    // Game loop related - variable loop for consistent rendering
    private long _sleepTime;
    // Base amount of time to sleep for (in milliseconds)
    private long _delay = 50;

    // Game related
    public enum GameState
    { RUNNING, PAUSED, NEVER_RUN };
    private GameState _gameState = GameState.NEVER_RUN;
    private boolean _isGameOver = false;
    private ArrayList<IDrawable> _drawables;
    private ArrayList<IUpdatable> _updatables;
    private ArrayList<ICollidable> _collidables;
    private int _countSpaceshipCollissions = 0;
    private int _score = 0;

    // singleton related
    private static GameEngine _instance = null;
    public static GameEngine getInstance()
    {
        if(_instance == null)
        {
            // Thread safe
            synchronized (GameEngine.class)
            {
                if (_instance == null)
                    _instance = new GameEngine();
            }
        }
        return _instance;
    }

    // on game over listener
    private OnGameOverListener _onGameOverListener = null;
    public interface OnGameOverListener
    {
        public void onGameOver();
    }
    public OnGameOverListener getOnGameOverListener()
    {
        return _onGameOverListener;
    }
    public void setOnGameOverListener(OnGameOverListener onGameOverListener)
    {
        _onGameOverListener = onGameOverListener;
    }

    // on score change listener
    private OnScoreChangeListener _onScoreChangeListener = null;
    public interface OnScoreChangeListener
    {
        public void onScoreChange(int score);
    }
    public OnScoreChangeListener getOnScoreChangeListener()
    {
        return _onScoreChangeListener;
    }
    public void setOnScoreChangeListener(OnScoreChangeListener onScoreChangeListener)
    {
        _onScoreChangeListener = onScoreChangeListener;
    }

    // CONSTRUCTOR
    private GameEngine()
    {
        _drawables = new ArrayList<IDrawable>();
        _updatables = new ArrayList<IUpdatable>();
        _collidables = new ArrayList<ICollidable>();
    }

    public GameState getGameState()
    {
        return _gameState;
    }

    public void addDrawable(IDrawable drawable)
    {
        _drawables.add(0, drawable);
    }

    public void removeDrawable(IDrawable drawable)
    {
        _drawables.remove(drawable);
    }

    public void addUpdateable(IUpdatable IUpdatable)
    {
        _updatables.add(IUpdatable);
    }

    public void removeUpdateable(IUpdatable updatable)
    {
        _updatables.remove(updatable);
    }

    public void addCollidable(ICollidable ICollidable)
    {
        _collidables.add(ICollidable);
    }

    public void removeCollidable(ICollidable collidable)
    {
        _collidables.remove(collidable);
    }

    public synchronized  void startGameEngine()
    {
        if(_gameState == GameState.NEVER_RUN)
        {
            _gameState = GameState.RUNNING;
            start();
        }
    }

    public void resetGameEngine()
    {
        _isGameOver = false;

        // Reset score and fire up the event
        _score = 0;
        if(_onScoreChangeListener != null)
            _onScoreChangeListener.onScoreChange(_score);

        // Remove all items
        _drawables.clear();
        _updatables.clear();
        _collidables.clear();

        // TODO: reset level

        // TODO: reset gene
    }

    public synchronized void pauseGameEngine()
    {
        _gameState = GameState.PAUSED;
    }

    public synchronized void resumeGameEngine()
    {
        _gameState = GameState.RUNNING;
    }

    public boolean isGameOver()
    {
        return _isGameOver;
    }

    @Override
    public void run()
    {
        //UPDATE - tweaked version of http://blorb.tumblr.com/post/236799414/simple-java-android-game-loop
        while (true)
        {
            // Time before update
            long beforeTime = System.nanoTime();

            // This is where we update the game engine
            synchronized (GameEngine.class)
            {
                if(_gameState == GameState.RUNNING && !_isGameOver)
                    update(_sleepTime);
            }

            // SLEEP
            // Sleep time. Time required to sleep to keep game consistent
            _sleepTime = _delay + ((System.nanoTime()-beforeTime)/1000000L); // converting nano to milliseconds

            try
            {
                //actual sleep code
                if(_sleepTime >0)
                {
                    sleep(_sleepTime);
                }
            }
            catch (InterruptedException ex)
            {
                Logger.getLogger(GameEngine.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        // otherwise reset time
    }

    private void doFilteredCollision()
    {
        for (int i = _collidables.size() - 1; i >= 1; i--)
            for (int j = _collidables.size() - 2; j >= 0; j--)
            {
                ICollidable object1 = _collidables.get(i);
                ICollidable object2 = _collidables.get(j);
                if (object1.isColliding(object2))
                {
                    if (object1.getCollidableType() == CollidableType.SPACESHIP &&
                            object2.getCollidableType() == CollidableType.ASTEROID)
                    {
                        // Break up asteroid make a ship more damaged + sound effect
                        asteroidBreakup((Asteroid) object2);
                        spaceshipCracked((SpaceShip) object1);
                        return;
                    }
                    else if (object1.getCollidableType() == CollidableType.ASTEROID &&
                            object2.getCollidableType() == CollidableType.SPACESHIP)
                    {
                        // Break up asteroid make a ship more damaged + sound effect
                        asteroidBreakup((Asteroid) object1);
                        spaceshipCracked((SpaceShip) object2);
                        return;
                    }
                    else if (object1.getCollidableType() == CollidableType.SPACESHIP &&
                            object2.getCollidableType() == CollidableType.LASER_POWER_UP)
                    {
                        // Remove power-up add weaponry to spaceship, start a timer, so power-up SFX
                        laserPowerupPickup((PowerUp) object2, (SpaceShip) object1);
                        return;
                    }
                    else if (object1.getCollidableType() == CollidableType.LASER_POWER_UP &&
                            object2.getCollidableType() == CollidableType.SPACESHIP)
                    {
                        // Remove power-up add weaponry to spaceship, start a timer, so power-up SFX
                        laserPowerupPickup((PowerUp) object1, (SpaceShip) object2);
                        return;
                    }
                    else if (object1.getCollidableType() == CollidableType.SPACESHIP &&
                            object2.getCollidableType() == CollidableType.TORPEDO_POWER_UP)
                    {
                        // Remove power-up add weaponry to spaceship, start a timer, so power-up SFX
                        torpedoPowerupPickup((PowerUp) object2, (SpaceShip) object1);
                        return;
                    }
                    else if (object1.getCollidableType() == CollidableType.TORPEDO_POWER_UP &&
                            object2.getCollidableType() == CollidableType.SPACESHIP)
                    {
                        // Remove power-up add weaponry to spaceship, start a timer, so power-up SFX
                        torpedoPowerupPickup((PowerUp) object1, (SpaceShip) object2);
                        return;
                    }
                    else if (object1.getCollidableType() == CollidableType.LASER_RAY &&
                            object2.getCollidableType() == CollidableType.ASTEROID)
                    {
                        // Break up asteroid + remove projectile + sound effect
                        asteroidBreakup((Asteroid) object2);
                        projectileImpact((LaserRay) object1);
                        return;
                    }
                    else if (object1.getCollidableType() == CollidableType.ASTEROID &&
                            object2.getCollidableType() == CollidableType.LASER_RAY)
                    {
                        // Break up asteroid + remove projectile + sound effect
                        asteroidBreakup((Asteroid) object1);
                        projectileImpact((LaserRay) object2);
                        return;
                    }
                    else if (object1.getCollidableType() == CollidableType.TORPEDO &&
                            object2.getCollidableType() == CollidableType.ASTEROID)
                    {
                        // Break up asteroid + explode and remove torpedo + sound effect
                        asteroidBreakup((Asteroid) object2);
                        torpedoImpact((Torpedo) object1);
                        return;
                    }
                    else if (object1.getCollidableType() == CollidableType.ASTEROID &&
                            object2.getCollidableType() == CollidableType.TORPEDO)
                    {
                        // Break up asteroid + explode and remove torpedo + sound effect
                        asteroidBreakup((Asteroid) object1);
                        torpedoImpact((Torpedo) object2);
                        return;
                    }

                    // else do nothing, for example asteroid and power-up collisions
                }
            }
    }

    private void asteroidBreakup(final Asteroid asteroid)
    {
        //Log.i("breakup", "asteroid break up");
        asteroid.playExplosionSound();
        GameEngine.getInstance().removeCollidable(asteroid);

        // get ready animation
        asteroid.startAnimation(0, 0, 3, 60.0f, 1);
        asteroid.setOnAnimationStopListener(new AnimatedSprite.OnAnimationStopListener()
        {
            @Override
            public void onAnimationStop()
            {
                removeDrawable(asteroid);
                removeUpdateable(asteroid);
            }
        });

        //Log.i("numOfNew", Integer.toString(numOfNew));

        // add some points
        _score += 1;
        // fire up on change score event
        if(_onScoreChangeListener != null)
            _onScoreChangeListener.onScoreChange(_score);
    }

    private void spaceshipCracked(SpaceShip spaceShip)
    {
        _countSpaceshipCollissions++;
        if(_countSpaceshipCollissions >= 3)
        {
            // Play awesome explosion audio
            spaceShip.playFinalExplosionSound();

            // change the number of animation from endless to 1
            spaceShip.setNumOfRepetitions(1);

            // on the last frame of explosion is Game over
            spaceShip.setOnAnimationStopListener(new AnimatedSprite.OnAnimationStopListener()
            {
                @Override
                public void onAnimationStop()
                {
                    // last frame of explosion - Game over!
                    _isGameOver = true; // set game over flag

                    // fire up the game over event
                    if(_onGameOverListener != null)
                        _onGameOverListener.onGameOver();
                }
            });
        }

        // update animation
        spaceShip.setAnimatedRow(_countSpaceshipCollissions);
    }

    private void laserPowerupPickup(PowerUp laserPowerUp, SpaceShip spaceShip)
    {
        // Play power up pickup SFX
        laserPowerUp.playPickUpSound();

        spaceShip.upgradeWeapon(SpaceShip.Weapon.UPGRADED_LASER, 9000.0f);

        // Remove from the game engine
        removeDrawable(laserPowerUp);
        removeCollidable(laserPowerUp);
        removeUpdateable(laserPowerUp);

        // add extra points
        _score += 5;
        // fire up on change score event
        if(_onScoreChangeListener != null)
            _onScoreChangeListener.onScoreChange(_score);
    }

    private void torpedoPowerupPickup(PowerUp torpedoPowerUp, SpaceShip spaceShip)
    {
        // Play power up pickup SFX
        torpedoPowerUp.playPickUpSound();

        // Upgrade weapon
        spaceShip.upgradeWeapon(SpaceShip.Weapon.TORPEDO, 6000.0f);

        // Remove from the game engine
        removeDrawable(torpedoPowerUp);
        removeCollidable(torpedoPowerUp);
        removeUpdateable(torpedoPowerUp);

        // add extra points
        _score += 5;
        // fire up on change score event
        if(_onScoreChangeListener != null)
            _onScoreChangeListener.onScoreChange(_score);
    }

    private void projectileImpact(LaserRay laserRay)
    {
        // remove projectile
        removeDrawable(laserRay);
        removeUpdateable(laserRay);
        removeCollidable(laserRay);
    }

    private void torpedoImpact(final Torpedo torpedo)
    {
        //Log.i("breakup", "asteroid break up");
        torpedo.playExplosionSound();

        // do explosion animation (means image scale)
        torpedo.startExplosionAnimation(1000.0f);
        torpedo.setOnExplosionEndListener(new Torpedo.OnExplosionEndListener()
        {
            @Override
            public void onExplosionEnd()
            {
                removeDrawable(torpedo);
                removeUpdateable(torpedo);
                removeCollidable(torpedo);
            }
        });
    }

    public void update(float time)
    {
        // do filtered collisions - we need at least 2 objects to do collisions
        if(_collidables.size() >= 2)
            doFilteredCollision();

        int lastIndex = _updatables.size() - 1;
        for (int i = lastIndex; i >= 0; i--)
            _updatables.get(i).update(time);
    }

    public void draw()
    {
        synchronized (GameEngine.class)
        {
            int lastIndex = _drawables.size() - 1;
            for (int i = lastIndex; i >= 0; i--)
                _drawables.get(i).draw();
        }
    }
}
