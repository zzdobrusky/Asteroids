package com.studiorur.games.asteroids.AdaptersViews;

import android.content.Context;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.studiorur.games.asteroids.R;

/**
 * Created by zbynek on 12/6/2014.
 */
public class GameOverView extends RelativeLayout
{
    // all in pd
    final static float BUTTON_PADDING_WIDTH = 6.0f;
    final static float BUTTON_PADDING_HEIGHT = 20.0f;
    final static float BUTTON_MARGIN_Y = 10.0f;
    final static float BUTTON_WIDTH = 180.0f;
    final static float BUTTON_HEIGHT = 40.0f;
    final static float BUTTON_FONT_SIZE = 10.0f;

    Button _mainMenuButton;
    TextView _scoreTextView;

    public GameOverView(Context context)
    {
        super(context);
        float scale = getContext().getResources().getDisplayMetrics().density;

        setBackgroundColor(R.drawable.pause_background);

        RelativeLayout.LayoutParams LP = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        setLayoutParams(LP);

        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        RelativeLayout.LayoutParams linearLayoutLP = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linearLayoutLP.addRule(CENTER_IN_PARENT);
        addView(linearLayout, linearLayoutLP);

        ImageView splashImageView = new ImageView(context);
        splashImageView.setImageResource(R.drawable.game_over_splash);
        LinearLayout.LayoutParams splashImageViewLP = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        splashImageViewLP.gravity = Gravity.CENTER_HORIZONTAL;
        linearLayout.addView(splashImageView, splashImageViewLP);

        _mainMenuButton = new Button(context);
        _mainMenuButton.setBackgroundResource(R.drawable.rounded_button_background_up);
        //_mainMenuButton.setGravity(Gravity.CENTER_HORIZONTAL);
        LinearLayout.LayoutParams mainMenuButtonLP = new LinearLayout.LayoutParams(Math.round(BUTTON_WIDTH * scale), Math.round(BUTTON_HEIGHT * scale));
        mainMenuButtonLP.gravity = Gravity.CENTER_HORIZONTAL;
        mainMenuButtonLP.setMargins(0, Math.round(BUTTON_MARGIN_Y * scale), 0, Math.round(BUTTON_MARGIN_Y * scale));
        _mainMenuButton.setPadding(Math.round(BUTTON_PADDING_WIDTH * scale), 0, Math.round(BUTTON_PADDING_WIDTH * scale), 0);
        _mainMenuButton.setText(R.string.mainmenu_button_label);
        _mainMenuButton.setTextSize(Math.round(BUTTON_FONT_SIZE * scale));
        linearLayout.addView(_mainMenuButton, mainMenuButtonLP);

        _scoreTextView = new TextView(context);
        LinearLayout.LayoutParams highestScoreTextViewLP = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        highestScoreTextViewLP.gravity = Gravity.CENTER_HORIZONTAL;
        _scoreTextView.setPadding(Math.round(BUTTON_PADDING_WIDTH * scale), Math.round(BUTTON_PADDING_HEIGHT * scale), Math.round(BUTTON_PADDING_WIDTH * scale), Math.round(BUTTON_PADDING_HEIGHT * scale));
        _scoreTextView.setText("testing");
        _scoreTextView.setTextSize(Math.round(BUTTON_FONT_SIZE * scale));
        linearLayout.addView(_scoreTextView, highestScoreTextViewLP);
    }

    public Button getMainMenuButton()
    {
        return _mainMenuButton;
    }

    public TextView getScoreTextView()
    {
        return _scoreTextView;
    }
}
