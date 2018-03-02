package com.ny.myapplication.customviewtest;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button button = null;
    Button bookmarkSaveButton = null;

    Card card;

    EditText bookmarkEditText = null;
    TextView bookmarkTextView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
