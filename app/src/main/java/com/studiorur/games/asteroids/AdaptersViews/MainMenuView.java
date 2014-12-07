package com.studiorur.games.asteroids.AdaptersViews;

import android.content.Context;
import android.view.Gravity;
import android.view.ViewGroup;
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
    // all in pd
    final static float VIEW_PADDING = 20.0f;
    final static float BUTTON_PADDING_WIDTH = 6.0f;
    final static float BUTTON_PADDING_HEIGHT = 20.0f;
    final static float BUTTON_WIDTH = 180.0f;
    final static float BUTTON_HEIGHT = 40.0f;
    final static float BUTTON_FONT_SIZE = 10.0f;

    Button _playButton;
    TextView _scoreTextView;

    public MainMenuView(Context context)
    {
        super(context);
        float scale = getContext().getResources().getDisplayMetrics().density;

        setOrientation(VERTICAL);
        setPadding(Math.round(VIEW_PADDING * scale), Math.round(VIEW_PADDING * scale), Math.round(VIEW_PADDING * scale), Math.round(VIEW_PADDING * scale));
        setGravity(Gravity.CENTER_HORIZONTAL);
        setBackgroundResource(R.drawable.asteroids_background);

        ImageView splashImageView = new ImageView(context);
        splashImageView.setImageResource(R.drawable.asteroids_splash);
        LayoutParams splashImageViewLP = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1);
        addView(splashImageView, splashImageViewLP);

        _playButton = new Button(context);
        _playButton.setBackgroundResource(R.drawable.rounded_button_background_up);
        LinearLayout.LayoutParams playGameButtonLP = new LayoutParams(Math.round(BUTTON_WIDTH * scale), Math.round(BUTTON_HEIGHT * scale));
        _playButton.setPadding(Math.round(BUTTON_PADDING_WIDTH * scale), 0, Math.round(BUTTON_PADDING_WIDTH * scale), 0);
        _playButton.setText(R.string.play_button_label);
        _playButton.setTextSize(Math.round(BUTTON_FONT_SIZE * scale));
        addView(_playButton, playGameButtonLP);

        _scoreTextView = new TextView(context);
        LayoutParams highestScoreTextViewLP = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        _scoreTextView.setPadding(Math.round(BUTTON_PADDING_WIDTH * scale), Math.round(BUTTON_PADDING_HEIGHT * scale), Math.round(BUTTON_PADDING_WIDTH * scale), Math.round(BUTTON_PADDING_HEIGHT * scale));
        _scoreTextView.setText("Your Highest Score: 12345");
        _scoreTextView.setTextSize(Math.round(BUTTON_FONT_SIZE * scale));
        addView(_scoreTextView, highestScoreTextViewLP);
    }

    public Button getPlayButton()
    {
        return _playButton;
    }

    public TextView getScoreTextView()
    {
        return _scoreTextView;
    }

}
