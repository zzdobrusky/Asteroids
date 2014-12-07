package com.studiorur.games.asteroids.AdaptersViews;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.studiorur.games.asteroids.R;

/**
 * Created by zbynek on 12/6/2014.
 */
public class PauseMenuView extends RelativeLayout
{
    // all in pd
    final static float BUTTON_PADDING_WIDTH = 6.0f;
    final static float BUTTON_MARGIN_Y = 10.0f;
    final static float BUTTON_WIDTH = 180.0f;
    final static float BUTTON_HEIGHT = 40.0f;
    final static float BUTTON_FONT_SIZE = 10.0f;

    Button _resumeButton;
    Button _mainMenuButton;

    public PauseMenuView(Context context)
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

        _resumeButton = new Button(context);
        _resumeButton.setBackgroundResource(R.drawable.rounded_button_background_up);
        LinearLayout.LayoutParams resumeButtonLP = new LinearLayout.LayoutParams(Math.round(BUTTON_WIDTH * scale), Math.round(BUTTON_HEIGHT * scale));
        resumeButtonLP.setMargins(0, Math.round(BUTTON_MARGIN_Y * scale), 0, Math.round(BUTTON_MARGIN_Y * scale));
        _resumeButton.setPadding(Math.round(BUTTON_PADDING_WIDTH * scale), 0, Math.round(BUTTON_PADDING_WIDTH * scale), 0);
        _resumeButton.setText(R.string.resume_button_label);
        _resumeButton.setTextSize(Math.round(BUTTON_FONT_SIZE * scale));
        linearLayout.addView(_resumeButton, resumeButtonLP);

        _mainMenuButton = new Button(context);
        _mainMenuButton.setBackgroundResource(R.drawable.rounded_button_background_up);
        LinearLayout.LayoutParams mainMenuButtonLP = new LinearLayout.LayoutParams(Math.round(BUTTON_WIDTH * scale), Math.round(BUTTON_HEIGHT * scale));
        mainMenuButtonLP.setMargins(0, Math.round(BUTTON_MARGIN_Y * scale), 0, Math.round(BUTTON_MARGIN_Y * scale));
        _mainMenuButton.setPadding(Math.round(BUTTON_PADDING_WIDTH * scale), 0, Math.round(BUTTON_PADDING_WIDTH * scale), 0);
        _mainMenuButton.setText(R.string.mainmenu_button_label);
        _mainMenuButton.setTextSize(Math.round(BUTTON_FONT_SIZE * scale));
        linearLayout.addView(_mainMenuButton, mainMenuButtonLP);
    }

    public Button getResumeButton()
    {
        return _resumeButton;
    }

    public Button getMainMenuButton()
    {
        return _mainMenuButton;
    }
}
