package com.studiorur.games.asteroids.GameManagement;

import com.studiorur.games.asteroids.Interfaces.Collidable;
import com.studiorur.games.asteroids.Interfaces.Updatable;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by zbynek on 11/24/2014.
 */
public class GameEngine extends Thread
{
    public enum GameState
    { RUNNING, PAUSED };
    private GameState _gameState;

    private ArrayList<Updatable> _updatables;
    private ArrayList<Collidable> _collidables;

    //for consistent rendering
    private long _sleepTime;
    //amount of time to sleep for (in milliseconds)
    private long _delay = 40;

    public GameEngine()
    {
        _updatables = new ArrayList<Updatable>();
        _collidables = new ArrayList<Collidable>();
    }

    public void addUpdateable(Updatable updatable)
    {
        _updatables.add(updatable);
    }

    public void addCOllidable(Collidable collidable)
    {
        _collidables.add(collidable);
    }

    @Override
    public synchronized void start()
    {
        _gameState = GameState.RUNNING;
        super.start();
    }

    public synchronized void pause()
    {
        _gameState = GameState.PAUSED;
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
            synchronized (this)
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
        for (Updatable updatable: _updatables)
            updatable.update(time);

        // do collisions
        for(int i=0; i<_collidables.size(); i++)
            for(int j=i+1; j<_collidables.size(); j++)
                _collidables.get(i).collide(_collidables.get(j));
    }

    public void draw()
    {
        for (Updatable updatable: _updatables)
            updatable.draw();
    }
}
