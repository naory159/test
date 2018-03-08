package com.ny.myapplication.customviewtest;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.ny.popuppanel.PopUpPanel;

public class MainActivity extends Activity {

    TextView headerTitle = null;

    Button button = null;
    Button changeAnimationButton = null;
    Button changeFontButton = null;
    Button bookmarkSaveButton = null;

    PopUpPanel card;

    EditText bookmarkEditText = null;
    TextView bookmarkTextView = null;

    boolean animationChanged = false;
    boolean fontChanged = false;

    // Threshold for minimal keyboard height.
    final int MIN_KEYBOARD_HEIGHT_PX = 150;

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
        changeAnimationButton = findViewById(R.id.button4);
        changeFontButton = findViewById(R.id.button5);

        card = findViewById(R.id.card);

        bookmarkSaveButton = findViewById(R.id.bookmark_save_button);
        bookmarkEditText = findViewById(R.id.bookmark_edit_text);
        bookmarkTextView = findViewById(R.id.bookmark_text_view);

        Typeface alefFont = Typeface.createFromAsset(getAssets(), "fonts/Alef-regular.ttf");
        card.setTitleTypeface(alefFont);
        bookmarkSaveButton.setTypeface(alefFont);
        bookmarkEditText.setTypeface(alefFont);
        bookmarkTextView.setTypeface(alefFont);
        headerTitle.setTypeface(alefFont);
        button.setTypeface(alefFont);
        changeAnimationButton.setTypeface(alefFont);

        card.setSlideUpInterpolator(new AccelerateInterpolator());
        card.setSlideDownInterpolator(new AccelerateInterpolator());

        handleKeyboard();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (card.isHide())
                    card.showCard();
                else
                    card.hideCard();
            }
        });

        changeAnimationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!animationChanged) {
                    card.setOpenAnimation(AnimationUtils.loadAnimation(getApplicationContext(),
                            R.anim.slide_right));
                    card.setCloseAnimation(AnimationUtils.loadAnimation(getApplicationContext(),
                            R.anim.slide_left));
                } else {
                    card.setOpenAnimation(AnimationUtils.loadAnimation(getApplicationContext(),
                            R.anim.slide_up));
                    card.setCloseAnimation(AnimationUtils.loadAnimation(getApplicationContext(),
                            R.anim.slide_down));
                }
                animationChanged = !animationChanged;
            }
        });

        changeFontButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!fontChanged) {
                    Typeface assistantFont = Typeface.createFromAsset(getAssets(), "fonts/Assistant-Regular.ttf");
                    card.setTitleTypeface(assistantFont);
                    bookmarkSaveButton.setTypeface(assistantFont);
                    bookmarkEditText.setTypeface(assistantFont);
                    bookmarkTextView.setTypeface(assistantFont);
                    headerTitle.setTypeface(assistantFont);
                    button.setTypeface(assistantFont);
                    changeAnimationButton.setTypeface(assistantFont);
                    changeFontButton.setTypeface(assistantFont);
                    Toast.makeText(MainActivity.this, "Assistant", Toast.LENGTH_SHORT).show();
                } else {
                    Typeface alefFont = Typeface.createFromAsset(getAssets(), "fonts/Alef-regular.ttf");
                    card.setTitleTypeface(alefFont);
                    bookmarkSaveButton.setTypeface(alefFont);
                    bookmarkEditText.setTypeface(alefFont);
                    bookmarkTextView.setTypeface(alefFont);
                    headerTitle.setTypeface(alefFont);
                    button.setTypeface(alefFont);
                    changeAnimationButton.setTypeface(alefFont);
                    changeFontButton.setTypeface(alefFont);
                    Toast.makeText(MainActivity.this, "Alef", Toast.LENGTH_SHORT).show();
                }
                fontChanged = !fontChanged;
            }
        });

    }

    private void handleKeyboard() {
        // Top-level window decor view.
        final View decorView = this.getWindow().getDecorView();

        // Register global layout listener.
        decorView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            private final Rect windowVisibleDisplayFrame = new Rect();
            private int lastVisibleDecorViewHeight;

            @Override
            public void onGlobalLayout() {
                // Retrieve visible rectangle inside window.
                decorView.getWindowVisibleDisplayFrame(windowVisibleDisplayFrame);
                final int visibleDecorViewHeight = windowVisibleDisplayFrame.height();

                // Decide whether keyboard is visible from changing decor view height.
                if (lastVisibleDecorViewHeight != 0) {
                    if (lastVisibleDecorViewHeight > visibleDecorViewHeight + MIN_KEYBOARD_HEIGHT_PX) {
                        // Calculate current keyboard height (this includes also navigation bar height when in fullscreen mode).
                        int currentKeyboardHeight = decorView.getHeight() - windowVisibleDisplayFrame.bottom;
                        // Notify listener about keyboard being shown.
                        //listener.onKeyboardShown(currentKeyboardHeight);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN, WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
                    } else if (lastVisibleDecorViewHeight + MIN_KEYBOARD_HEIGHT_PX < visibleDecorViewHeight) {
                        // Notify listener about keyboard being hidden.
                        //listener.onKeyboardHidden();
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
                        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    }
                }
                // Save current decor view height for the next call.
                lastVisibleDecorViewHeight = visibleDecorViewHeight;
            }
        });
    }
}
