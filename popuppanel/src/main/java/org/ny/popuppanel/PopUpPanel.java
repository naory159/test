package org.ny.popuppanel;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PopUpPanel extends RelativeLayout {

    private final int DELTA_TOUCH = 50;

    private RelativeLayout mainLayout;
    private RelativeLayout header;

    private TextView headerTextView;

    private ImageButton closeButton;
    private ImageButton dragButton;

    private int marginTop;
    private int headerHeight;
    private Drawable headerBackground;
    private String headerTitle;
    private int titleColor;
    private boolean isPopupDraggable;
    private boolean hideDraggableIcon;
    private boolean wrapContentHeight;
    private boolean isFirstLayout = true;

    private Drawable headerCloseIcon;
    private Drawable headerDragIcon;

    private Animation slide_down;
    private Animation slide_up;

    public PopUpPanel(Context context) {
        super(context);
        init(null);
    }

    public PopUpPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public PopUpPanel(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(
                    attrs,
                    R.styleable.Card,
                    0, 0);

            try {
                marginTop = (int) typedArray.getDimension(R.styleable.Card_margin_top, getResources().getDimension(R.dimen.margin_top));
                headerHeight = (int) typedArray.getDimension(R.styleable.Card_header_height, getResources().getDimension(R.dimen.header_height));
                headerBackground = typedArray.getDrawable(R.styleable.Card_header_background);
                headerTitle = typedArray.getString(R.styleable.Card_header_title);
                titleColor = typedArray.getColor(R.styleable.Card_title_color, getResources().getColor(R.color.titleColor));
                headerCloseIcon = typedArray.getDrawable(R.styleable.Card_header_close_icon);
                headerDragIcon = typedArray.getDrawable(R.styleable.Card_header_drag_icon);
                isPopupDraggable = typedArray.getBoolean(R.styleable.Card_is_popup_draggable, true);
                hideDraggableIcon = typedArray.getBoolean(R.styleable.Card_hide_draggable_icon, false);
                wrapContentHeight = typedArray.getBoolean(R.styleable.Card_wrap_content_height, false);
            } finally {
                typedArray.recycle();
                typedArray = null;
            }
        }

        inflate(getContext(), R.layout.popup_panel, this);
        this.mainLayout = findViewById(this.getId());
        this.header = findViewById(R.id.header);

        this.headerTextView = findViewById(R.id.header_text_view);
        this.headerTextView.setText(headerTitle);
        this.headerTextView.setTextColor(titleColor);

        this.closeButton = findViewById(R.id.close);
        this.dragButton = findViewById(R.id.drag);

        if (closeButton != null && headerCloseIcon != null)
            closeButton.setImageDrawable(headerCloseIcon);
        if (dragButton != null && headerDragIcon != null)
            dragButton.setImageDrawable(headerDragIcon);

        LayoutParams headerParams = (LayoutParams) header.getLayoutParams();
        headerParams.height = headerHeight;
        if (headerBackground != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                this.header.setBackground(headerBackground);
            } else {
                this.header.setBackgroundDrawable(headerBackground);
            }
        }
        this.header.setLayoutParams(headerParams);

        if (isPopupDraggable)
            this.mainLayout.setOnTouchListener(new CardTouchListener(this.getContext()));
        this.closeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                hideCard();
            }
        });

        if (dragButton != null && hideDraggableIcon)
            this.dragButton.setVisibility(INVISIBLE);

        //Load animation
        slide_down = AnimationUtils.loadAnimation(this.getContext(),
                R.anim.slide_down);

        slide_up = AnimationUtils.loadAnimation(this.getContext(),
                R.anim.slide_up);

        this.hideCard();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        View v;
        String tag;

        int childCount = getChildCount();
        for(int i = 0; i < childCount; i++) {
            v = getChildAt(i);
            if (v != null) {
                tag = (String)v.getTag();
                if (tag != null && tag.equals("content")) {
                    v.setPadding(0, this.headerHeight, 0, 0);

                    if (isFirstLayout && wrapContentHeight) {
                        isFirstLayout = !isFirstLayout;
                        ViewTreeObserver vto = v.getViewTreeObserver();
                        final View finalV = v;
                        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                            @Override
                            public void onGlobalLayout() {

                                if (finalV.getMeasuredHeight() > 0){
                                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                                        finalV.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                                    } else {
                                        finalV.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                                    }
                                    int height = finalV.getMeasuredHeight();
                                    LayoutParams layoutParams = (LayoutParams) mainLayout.getLayoutParams();
                                    layoutParams.height = height + headerHeight;
                                    mainLayout.setLayoutParams(layoutParams);
                                }
                            }
                        });
                    }
                }
            }
        }
    }

    public void hideCard() {
        this.mainLayout.startAnimation(this.slide_down);
        this.mainLayout.setVisibility(GONE);
    }

    public void showCard() {
//        this.mainLayout.bringToFront();
//        this.mainLayout.invalidate();
        this.mainLayout.startAnimation(this.slide_up);
        this.mainLayout.setVisibility(VISIBLE);
    }

    public void setTitle(String title) {
        this.headerTextView.setText(title);
    }

    public void setTitle(int titleRes) {
        this.headerTextView.setText(titleRes);
    }

    public void setTitleTypeface(Typeface font) {
        this.headerTextView.setTypeface(font);
    }

    public void setTitleColor(int colorRes) {
        this.headerTextView.setTextColor(colorRes);
    }

    public int getPanelHeight() {
        return this.getMeasuredHeight();
    }

    public void setOpenAnimation(Animation animation) {
        this.slide_up = animation;
    }

    public void setCloseAnimation(Animation animation) {
        this.slide_down = animation;
    }

    public void setSlideUpDuration(long duration) {
        this.slide_up.setDuration(duration);
    }

    public void setSlideDownDuration(long duration) {
        this.slide_down.setDuration(duration);
    }

    public void setSlideUpInterpolator(Interpolator interpolator) {
        this.slide_up.setInterpolator(interpolator);
    }

    public void setSlideDownInterpolator(Interpolator interpolator) {
        this.slide_down.setInterpolator(interpolator);
    }

    public boolean isHide() {
        return this.mainLayout.getVisibility() == GONE;
    }

    private boolean isDragButtonTouched(MotionEvent motionEvent) {
        return motionEvent.getX() >= dragButton.getLeft() - DELTA_TOUCH && motionEvent.getX() < dragButton.getRight() + DELTA_TOUCH
                && motionEvent.getY() < dragButton.getBottom() + DELTA_TOUCH;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isDragButtonTouched(ev)) {
            return true;
        }
        return super.onInterceptTouchEvent(ev);
    }

    private class CardTouchListener implements OnTouchListener {
        private Context context;
        private int screenHeight = 0;

        CardTouchListener(Context context) {
            this.context = context;

            DisplayMetrics displayMetrics = new DisplayMetrics();
            ((Activity)this.context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            this.screenHeight = displayMetrics.heightPixels;
        }

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            view.performClick();
            if (isDragButtonTouched(motionEvent)) {
                final int Y = (int) motionEvent.getRawY();

                switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                    case MotionEvent.ACTION_POINTER_DOWN:
                        break;
                    case MotionEvent.ACTION_POINTER_UP:
                        break;
                    case MotionEvent.ACTION_MOVE:
                        LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
                        layoutParams.height = Math.max(Math.min(this.screenHeight - Y, this.screenHeight - marginTop), header.getHeight());
                        view.setLayoutParams(layoutParams);
                        break;
                    default:
                        return false;
                }
                view.invalidate();
            }
            return true;
        }
    }
}
