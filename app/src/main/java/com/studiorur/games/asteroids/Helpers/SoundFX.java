package com.studiorur.games.asteroids.Helpers;

import android.app.Activity;
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
    float _volume;

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

    public void initializeAudioManager(Activity activity)
    {
        AudioManager audioManager = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
        float actVolume = (float) audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        float maxVolume = (float) audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        _volume = actVolume / maxVolume;

        activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
    }

    public void addSound(Context context, final int soundIdentifier)
    {
        // load the soound
        _soundsLoaded.put(soundIdentifier, false);
        _soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener()
        {
            @Override
            public void onLoadComplete(SoundPool soundPool, int soundId, int status)
            {
                _soundsLoaded.put(soundIdentifier, true);
            }
        });
        int soundId = _soundPool.load(context, soundIdentifier, 1);
        _soundIds.put(soundIdentifier, soundId);
    }

    public void play(int soundIdentifier)
    {
        if(_soundIds.get(soundIdentifier) > 0 && _soundPool != null && _soundsLoaded.get(soundIdentifier) == true)
            _soundPool.play(_soundIds.get(soundIdentifier), _volume, _volume, 1, 0, 1f);
    }

    public void stop(int soundIdentifier)
    {
        // not used
    }
}
