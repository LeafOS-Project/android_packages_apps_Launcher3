package com.android.quickstep.views;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.FloatProperty;
import android.view.MotionEvent;
import android.view.View;

import com.android.launcher3.InsettableFrameLayout;
import com.android.launcher3.R;

public class RecentsViewContainer extends InsettableFrameLayout {
    public static final FloatProperty<RecentsViewContainer> CONTENT_ALPHA =
            new FloatProperty<RecentsViewContainer>("contentAlpha") {
                @Override
                public void setValue(RecentsViewContainer view, float v) {
                    view.setContentAlpha(v);
                }

                @Override
                public Float get(RecentsViewContainer view) {
                    return view.mRecentsView.getContentAlpha();
                }
            };

    private final Rect mTempRect = new Rect();

    private RecentsView mRecentsView;
    private View mClearAllButton;

    public RecentsViewContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mClearAllButton = findViewById(R.id.clear_all_button);
        mClearAllButton.setOnClickListener((v) -> {
            mRecentsView.dismissAllTasks();
        });

        mRecentsView = (RecentsView) findViewById(R.id.overview_panel);
        mRecentsView.setContainerView(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mRecentsView.getTaskSize(mTempRect);

        mClearAllButton.setTranslationX(
                (mClearAllButton.getMeasuredWidth() - getResources().getDimension(
                        R.dimen.clear_all_container_width)) / 2);
        mClearAllButton.setTranslationY(
                mTempRect.top + (mTempRect.height() - mClearAllButton.getMeasuredHeight()) / 2);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        super.onTouchEvent(ev);
        // Do not let touch escape to siblings below this view. This prevents scrolling of the
        // workspace while in Recents.
        return true;
    }

    public void setContentAlpha(float alpha) {
        if (alpha == mRecentsView.getContentAlpha()) {
            return;
        }
        mRecentsView.setContentAlpha(alpha);
        setVisibility(alpha > 0 ? VISIBLE : GONE);
    }

    public void onEmptyStateChanged(boolean isEmpty) {
        mClearAllButton.setVisibility(isEmpty ? GONE : VISIBLE);
    }
}