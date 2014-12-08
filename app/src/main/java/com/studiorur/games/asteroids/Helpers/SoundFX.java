package com.studiorur.games.asteroids.Helpers;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import java.util.HashMap;

/**
 * Created by zbynek on 11/29/2014.
 */
public class SoundFX
{
    private SoundPool _soundPool;
    private HashMap<Integer, Integer> _soundIds;
    private HashMap<Integer, Boolean> _soundsLoaded;
    private static SoundFX _instance = null;

    public static SoundFX getInstance()
    {
        if(_instance == null)
        {
            // Thread safe
            synchronized (SoundFX.class)
            {
                if(_instance == null)
                    _instance = new SoundFX();
            }
        }

        return _instance;
    }

    private SoundFX()
    {
        _soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        _soundIds = new HashMap<Integer, Integer>();
        _soundsLoaded = new HashMap<Integer, Boolean>();
    }

    public void addSound(Context context, final int soundIdentifier)
    {
        // load the sound
        _soundPool.load(context, soundIdentifier, 1);
        _soundsLoaded.put(soundIdentifier, false);
        _soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener()
        {
            @Override
            public void onLoadComplete(SoundPool soundPool, int soundId, int status)
            {
                _soundIds.put(soundIdentifier, soundId);
                _soundsLoaded.put(soundIdentifier, true);
            }
        });
    }

    public void play(int soundIdentifier, float volume)
    {
        if(_soundIds.get(soundIdentifier) > 0 && _soundPool != null && _soundsLoaded.get(soundIdentifier) == true)
            _soundPool.play(_soundIds.get(soundIdentifier), volume, volume, 1, 0, 1f);
    }

    public void stop(int soundIdentifier)
    {
        // not used

    }
}
