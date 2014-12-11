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
    private int _soundId;
    private int _streamId;
    private int _soundIdentifier;
    private boolean _soundLoaded;

    public SoundFX(Context context, int soundIdentifier)
    {
        _soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        _soundIdentifier = soundIdentifier;
        addSound(context);
    }

    private void addSound(Context context)
    {
        // load the sound
        _soundId = _soundPool.load(context, _soundIdentifier, 1);
        _soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener()
        {
            @Override
            public void onLoadComplete(SoundPool soundPool, int soundId, int status)
            {
                _soundLoaded = true;
            }
        });

    }

    public void play()
    {
        if(_soundId > 0 && _soundLoaded == true)
            _streamId = _soundPool.play(_soundId, 1.0f, 1.0f, 1, 0, 1f);
    }

    public void stop()
    {
        if(_streamId > 0)
            _soundPool.stop(_streamId);
    }
}
