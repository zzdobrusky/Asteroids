package com.studiorur.games.asteroids.GameManagement;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by zbynek on 11/27/2014.
 */
public class DataModel
{
    private File _filesDir = null;
    private int _theHighestScore = 0;
    private int _currentScore = 0;
    private static DataModel _Instance = null;

    public static DataModel getInstance(File filesDir)
    {
        if(_Instance == null)
        {
            // Thread safe
            synchronized (DataModel.class)
            {
                if (_Instance == null)
                    _Instance = new DataModel(filesDir);
            }
        }

        return _Instance;
    }

    // CONSTRUCTOR
    private DataModel(File filesDir)
    {
        _filesDir = filesDir;
        if(_filesDir != null)
            loadScore();

        // set up on score change listener from game engine
        GameEngine.getInstance().setOnScoreChangeListener(new GameEngine.OnScoreChangeListener()
        {
            @Override
            public void onScoreChange(int score)
            {
                setCurrentScore(score);
            }
        });
    }

    public int getTheHighestScore()
    {
        return _theHighestScore;
    }

    public int getCurrentScore()
    {
        return _currentScore;
    }

    public void setCurrentScore(int currentScore)
    {
        _currentScore = currentScore;
        _theHighestScore = Math.max(_theHighestScore, _currentScore);
    }

    public void loadScore()
    {
        try
        {
            FileReader textReader = new FileReader(_filesDir);
            BufferedReader bufferedTextReader = new BufferedReader(textReader);
            String jsonScore = bufferedTextReader.readLine();

            Gson gson = new Gson();

            _theHighestScore = gson.fromJson(jsonScore, Integer.TYPE);

            bufferedTextReader.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            return;
        }
    }

    public void saveScore()
    {
        Gson gson = new Gson();

        String jsonSavedScore = gson.toJson(getTheHighestScore(), Integer.TYPE);

        try
        {
            FileWriter textWriter = new FileWriter(_filesDir, false);
            BufferedWriter bufferedWriter = new BufferedWriter(textWriter);
            bufferedWriter.write(jsonSavedScore);
            bufferedWriter.flush();
            bufferedWriter.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

}
