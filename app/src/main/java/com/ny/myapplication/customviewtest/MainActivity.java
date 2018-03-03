package com.ny.myapplication.customviewtest;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {

    TextView headerTitle = null;

    Button button = null;
    Button bookmarkSaveButton = null;

    Card card;

    EditText bookmarkEditText = null;
    TextView bookmarkTextView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            // If the Android version is lower than Jellybean, use this call to hide
            // the status bar.
            if (Build.VERSION.SDK_INT < 16) {
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                        WindowManager.LayoutParams.FLAG_FULLSCREEN);
            } else {
                View decorView = getWindow().getDecorView();
                // Hide the status bar.
                int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
                decorView.setSystemUiVisibility(uiOptions);
                // Remember that you should never show the action bar if the
                // status bar is hidden, so hide that too if necessary.
                ActionBar actionBar = getActionBar();
                actionBar.hide();
            }
        } catch(Exception ex) {

        }

        headerTitle = findViewById(R.id.header_title);

        button = findViewById(R.id.button3);

        card = findViewById(R.id.card);

        bookmarkSaveButton = findViewById(R.id.bookmark_save_button);
        bookmarkEditText = findViewById(R.id.bookmark_edit_text);
        bookmarkTextView = findViewById(R.id.bookmark_text_view);

        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Alef-regular.ttf");
        card.setTitleTypeface(font);
        bookmarkSaveButton.setTypeface(font);
        bookmarkEditText.setTypeface(font);
        bookmarkTextView.setTypeface(font);
        headerTitle.setTypeface(font);
        button.setTypeface(font);

        card.SetSlideUpInterpolator(new AccelerateInterpolator());
        card.SetSlideDownInterpolator(new AccelerateInterpolator());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (card.isHide())
                    card.ShowCard();
                else
                    card.HideCard();
            }
        });

    }
}
