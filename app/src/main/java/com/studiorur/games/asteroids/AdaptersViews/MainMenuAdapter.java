package com.studiorur.games.asteroids.AdaptersViews;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

/**
 * Created by zbynek on 12/1/2014.
 */
public class MainMenuAdapter extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // lock screen orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);


    }
}
