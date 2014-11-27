package com.studiorur.games.asteroids.GameManagement;

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

    public GameState getGameState()
    {
        return _gameState;
    }

    public void setGameState(GameState state)
    {
        _gameState = state;
    }

    private ArrayList<Updatable> _components;

    //for consistent rendering
    private long sleepTime;
    //amount of time to sleep for (in milliseconds)
    private long delay = 50;

    public GameEngine()
    {
        _components = new ArrayList<Updatable>();
    }

    public void addComponent(Updatable component)
    {
        _components.add(component);
    }

    public void removeComponent(Updatable component)
    {
        _components.remove(component);
    }


    public void update(float time)
    {
        for (Updatable component: _components)
            component.update(time);
    }

    public void draw()
    {
        for (Updatable component: _components)
            component.draw();
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
                update(sleepTime);
            }

            //SLEEP
            //Sleep time. Time required to sleep to keep game consistent
            //This starts with the specified delay time (in milliseconds) then subtracts from that the
            //actual time it took to update and render the game. This allows our game to render smoothly.
            this.sleepTime = delay-((System.nanoTime()-beforeTime)/1000000L);

            try
            {
                //actual sleep code
                if(sleepTime>0)
                {
                    this.sleep(sleepTime);
                }
            }
            catch (InterruptedException ex)
            {
                Logger.getLogger(GameEngine.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

}
