package com.studiorur.games.asteroids.GameManagement;

import com.studiorur.games.asteroids.Helpers.SoundFX;
import com.studiorur.games.asteroids.Interfaces.CollidableType;
import com.studiorur.games.asteroids.Interfaces.ICollidable;
import com.studiorur.games.asteroids.Interfaces.IUpdatable;
import com.studiorur.games.asteroids.R;
import com.studiorur.games.asteroids.Sprites.AnimatedSprite;
import com.studiorur.games.asteroids.Sprites.Asteroid;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by zbynek on 11/24/2014.
 */
public class GameEngine extends Thread
{
    public enum GameState
    { RUNNING, PAUSED, NEVER_RUN };

    private GameState _gameState = GameState.NEVER_RUN;
    private ArrayList<IUpdatable> _updatables;
    private ArrayList<ICollidable> _collidables;
    private AsteroidGenerator _asteroidGenerator;
    private float _breakUpInterval = 500.0f; // in milliseconds
    private float _passedTime = 0.0f;
    private boolean _isAllowedToBreak = true;


    //for consistent rendering
    private long _sleepTime;
    //amount of time to sleep for (in milliseconds)
    private long _delay = 50;

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

    private GameEngine()
    {
        _updatables = new ArrayList<IUpdatable>();
        _collidables = new ArrayList<ICollidable>();
    }

    public void registerAsteroidGenerator(AsteroidGenerator asteroidGenerator)
    {
        _asteroidGenerator = asteroidGenerator;
    }

    public GameState getGameState()
    {
        return _gameState;
    }

    public void addUpdateable(IUpdatable IUpdatable)
    {
        _updatables.add(0, IUpdatable);
    }

    public void removeUpdateable(IUpdatable updatable)
    {
        _updatables.remove(updatable);
    }

    public void addCollidable(ICollidable ICollidable)
    {
        _collidables.add(0, ICollidable);
    }

    public void removeCollidable(ICollidable collidable)
    {
        _collidables.remove(collidable);
    }

    public synchronized  void startGame()
    {
        if(_gameState == GameState.NEVER_RUN)
        {
            _gameState = GameState.RUNNING;
            start();
        }
    }

    public synchronized void pauseGame()
    {
        _gameState = GameState.PAUSED;
    }

    public synchronized void resumeGame()
    {
        _gameState = GameState.RUNNING;

    }

    @Override
    public void run()
    {

        //UPDATE - tweaked version of http://blorb.tumblr.com/post/236799414/simple-java-android-game-loop
        while (true)
        {
            //time before update
            long beforeTime = System.nanoTime();

            //This is where we update the game engine
            synchronized (GameEngine.class)
            {
                if(_gameState == GameState.RUNNING)
                    update(_sleepTime);
            }

            //SLEEP
            //Sleep time. Time required to sleep to keep game consistent
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

    public void update(float time)
    {
        _passedTime += time;

        // do filtered collisions - we need at least 2 objects to do collisions
        if(_collidables.size() >= 2)
            doFilteredCollision();

        for (int i = _updatables.size() - 1; i >= 0; i--)
            _updatables.get(i).update(time);

        if(_passedTime > _breakUpInterval)
            _isAllowedToBreak = true;
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
                    if (object1.getCollidableType() == CollidableType.ASTEROID &&
                            object2.getCollidableType() == CollidableType.ASTEROID)
                    {
                        // TODO: do random break up of two asteroids + sound effect
                    }
                    else if (object1.getCollidableType() == CollidableType.SPACESHIP &&
                            object2.getCollidableType() == CollidableType.ASTEROID)
                    {
                        // Break up asteroid make a ship more damaged + sound effect
                        if(_isAllowedToBreak)
                            asteroidBreakup((Asteroid) object2);

                        return;
                    }
                    else if (object1.getCollidableType() == CollidableType.ASTEROID &&
                            object2.getCollidableType() == CollidableType.SPACESHIP)
                    {
                        // Break up asteroid make a ship more damaged + sound effect
                        if(_isAllowedToBreak)
                            asteroidBreakup((Asteroid) object1);

                        return;
                    }
                    else if (object1.getCollidableType() == CollidableType.SPACESHIP &&
                            object2.getCollidableType() == CollidableType.POWER_UP)
                    {
                        // TODO: remove power-up, add weaponry to spaceship, start a timer
                        return;
                    }
                    else if (object1.getCollidableType() == CollidableType.POWER_UP &&
                            object2.getCollidableType() == CollidableType.SPACESHIP)
                    {
                        // TODO: remove power-up, add weaponry to spaceship, start a timer
                        return;
                    }
                    else if (object1.getCollidableType() == CollidableType.PROJECTILE &&
                            object2.getCollidableType() == CollidableType.ASTEROID)
                    {
                        // Break up asteroid + remove projectile + sound effect
                        if(_isAllowedToBreak)
                        {
                            asteroidBreakup((Asteroid) object2);
                            // remove projectile
                            GameEngine.getInstance().removeUpdateable((IUpdatable)object1);
                            GameEngine.getInstance().removeCollidable(object1);
                        }
                        return;
                    }
                    else if (object1.getCollidableType() == CollidableType.ASTEROID &&
                            object2.getCollidableType() == CollidableType.PROJECTILE)
                    {
                        // Break up asteroid + remove projectile + sound effect
                        if(_isAllowedToBreak)
                        {
                            asteroidBreakup((Asteroid) object1);
                            // remove projectile
                            GameEngine.getInstance().removeUpdateable((IUpdatable)object2);
                            GameEngine.getInstance().removeCollidable(object2);
                        }
                        return;
                    }

                    // else do nothing, for example asteroid and power-up collisions

                }
            }
    }

    private void asteroidBreakup(final Asteroid asteroid)
    {
        _isAllowedToBreak = false;
        _passedTime = 0.0f;

        //Log.i("breakup", "asteroid break up");

        asteroid.startAnimation();
        asteroid.setOnAnimationStopListener(new AnimatedSprite.OnAnimationStopListener()
        {
            @Override
            public void onAnimationStop()
            {
                _asteroidGenerator.removeAsteroid(asteroid);
            }
        });

        //Log.i("numOfNew", Integer.toString(numOfNew));
        SoundFX.getInstance().play(R.raw.explosion, 1.0f);
    }

    public void draw()
    {
        synchronized (GameEngine.class)
        {
            for (int i = _updatables.size() - 1; i >= 0; i--)
                _updatables.get(i).draw();
        }

//        Iterator<IUpdatable> updatableIterator = _updatables.iterator();
//        while(updatableIterator.hasNext())
//        {
//            IUpdatable updatable = updatableIterator.next();
//            updatable.draw();
//        }
    }
}
