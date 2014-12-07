package com.studiorur.games.asteroids.GameManagement;

import com.studiorur.games.asteroids.Helpers.SoundFX;
import com.studiorur.games.asteroids.Interfaces.CollidableType;
import com.studiorur.games.asteroids.Interfaces.ICollidable;
import com.studiorur.games.asteroids.Interfaces.IUpdatable;
import com.studiorur.games.asteroids.R;

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

    public GameState getGameState()
    {
        return _gameState;
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

    @Override
    public synchronized void start()
    {
        if(_gameState == GameState.NEVER_RUN)
        {
            _gameState = GameState.RUNNING;
            super.start();
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

        //UPDATE - http://blorb.tumblr.com/post/236799414/simple-java-android-game-loop
        while (_gameState == GameState.RUNNING)
        {
            //time before update
            long beforeTime = System.nanoTime();

            //This is where we update the game engine
            synchronized (GameEngine.class)
            {
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
                    this.sleep(_sleepTime);
                }
            }
            catch (InterruptedException ex)
            {
                Logger.getLogger(GameEngine.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void update(float time)
    {
        for (int i = _updatables.size() - 1; i >= 0; i--)
            _updatables.get(i).update(time);

        // do filtered collisions - we need at least 2 objects to do collisions
        if(_collidables.size() >= 2)
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
                        } else if ((object1.getCollidableType() == CollidableType.SPACESHIP &&
                                object2.getCollidableType() == CollidableType.ASTEROID))
                        {
                            // TODO: break up asteroid make a ship more damaged + sound effect
                            SoundFX.getInstance().play(R.raw.explosion, 1.0f);
                        } else if ((object1.getCollidableType() == CollidableType.ASTEROID &&
                                object2.getCollidableType() == CollidableType.SPACESHIP))
                        {
                            // TODO: break up asteroid make a ship more damaged + sound effect
                            SoundFX.getInstance().play(R.raw.explosion, 1.0f);
                        } else if ((object1.getCollidableType() == CollidableType.SPACESHIP &&
                                object2.getCollidableType() == CollidableType.POWER_UP))
                        {
                            // TODO: remove power-up, add weaponry to spaceship, start a timer
                        } else if ((object1.getCollidableType() == CollidableType.POWER_UP &&
                                object2.getCollidableType() == CollidableType.SPACESHIP))
                        {
                            // TODO: remove power-up, add weaponry to spaceship, start a timer
                        }

                        // else do nothing, for example asteroid and power-up collisions

                    }
                }
        }
    }

    public void draw()
    {
        for (int i = _updatables.size() - 1; i >= 0; i--)
            _updatables.get(i).draw();
    }
}
