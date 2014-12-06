package com.studiorur.games.asteroids.AdaptersViews;

import android.content.Context;
import android.view.Gravity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.studiorur.games.asteroids.R;

/**
 * Created by zbynek on 12/6/2014.
 */
public class MainMenuView extends LinearLayout
{
    final static float VIEW_PADDING = 40.0f;
    final static float BUTTON_PADDING = 6.0f;
    final static float BUTTON_WIDTH = 200.0f;

    Button _newGameButton;
    TextView _highestScoreTextView;

    public MainMenuView(Context context)
    {
        super(context);
        float scale = getContext().getResources().getDisplayMetrics().density;

        setOrientation(VERTICAL);
        setPadding(
                Math.round(VIEW_PADDING * scale),
                Math.round(VIEW_PADDING * scale),
                Math.round(VIEW_PADDING * scale),
                Math.round(VIEW_PADDING * scale));
        setGravity(Gravity.CENTER_HORIZONTAL);
        setBackgroundResource(R.drawable.asteroids_background);

        ImageView splashImageView = new ImageView(context);
    }
}
