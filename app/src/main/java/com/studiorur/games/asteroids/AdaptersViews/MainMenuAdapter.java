package com.studiorur.games.asteroids.AdaptersViews;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.studiorur.games.asteroids.R;

/**
 * Created by zbynek on 12/1/2014.
 */
public class MainMenuAdapter extends Activity
{
    Context _context;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        _context = this;

        // lock screen orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        final MainMenuView mainMenuView = new MainMenuView(this);
        setContentView(mainMenuView);

        mainMenuView.getPlayButton().setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if(event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    mainMenuView.getPlayButton().setBackgroundResource(R.drawable.rounded_button_background_down);

                }
                else if(event.getAction() == MotionEvent.ACTION_UP)
                {
                    mainMenuView.getPlayButton().setBackgroundResource(R.drawable.rounded_button_background_up);

                    // TODO: call game screen activity
                    Intent gameScreenIntent = new Intent();
                    gameScreenIntent.setClass(_context, GameScreenActivity.class);
                    gameScreenIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(gameScreenIntent);
                }

                return true;
            }
        });
    }
}
