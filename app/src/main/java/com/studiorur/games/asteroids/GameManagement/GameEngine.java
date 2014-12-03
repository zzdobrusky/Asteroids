package com.studiorur.games.asteroids.GameManagement;

import com.studiorur.games.asteroids.Interfaces.ICollidable;
import com.studiorur.games.asteroids.Interfaces.IUpdatable;

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

    private ArrayList<IUpdatable> _IUpdatables;
    private ArrayList<ICollidable> _ICollidables;

    //for consistent rendering
    private long _sleepTime;
    //amount of time to sleep for (in milliseconds)
    private long _delay = 50;

    public GameEngine()
    {
        _IUpdatables = new ArrayList<IUpdatable>();
        _ICollidables = new ArrayList<ICollidable>();
    }

    public void addUpdateable(IUpdatable IUpdatable)
    {
        _IUpdatables.add(IUpdatable);
    }

    public void addCollidable(ICollidable ICollidable)
    {
        _ICollidables.add(ICollidable);
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
        for (IUpdatable IUpdatable : _IUpdatables)
            IUpdatable.update(time);

        // do collisions
        for(int i=0; i< _ICollidables.size(); i++)
            for(int j=i+1; j< _ICollidables.size(); j++)
                _ICollidables.get(i).collide(_ICollidables.get(j));
    }

    public void draw()
    {
        for (IUpdatable IUpdatable : _IUpdatables)
            IUpdatable.draw();
    }
}
