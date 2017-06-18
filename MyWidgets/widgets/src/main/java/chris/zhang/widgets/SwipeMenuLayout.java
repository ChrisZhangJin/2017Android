package chris.zhang.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.AttrRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by Administrator on 2017/6/18.
 */

public class SwipeMenuLayout extends LinearLayout {
    private static final String TAG = "SwipeMenuLayout";
    private static int INVALID_RESOURCE_ID = 0;
    @LayoutRes
    private final int mLayoutId;
    private final int mDirection;
    private ViewGroup mLayout;
    private SwipeController mController;
    private int mContentIndex;
    private View mContent;

    public SwipeMenuLayout(@NonNull Context context) {
        this(context, null);

    }

    public SwipeMenuLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeMenuLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public SwipeMenuLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.SwipeMenuLayout, defStyleAttr, defStyleRes);
        mLayoutId = array.getResourceId(R.styleable.SwipeMenuLayout_swipe_layout, INVALID_RESOURCE_ID);
        mDirection = array.getInt(R.styleable.SwipeMenuLayout_swipe_direction, SwipeState.DIRECTION_LEFT);
        array.recycle();

        if (mLayoutId != INVALID_RESOURCE_ID) {
            addSwipeLayout(mLayoutId);
        }

        mController = new SwipeController(context, this, mLayout, mDirection);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mContent = getChildAt(mContentIndex);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mController.trackVelocity(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mController.abortScrolling();
                mController.trackMovement(event);
                break;
            case MotionEvent.ACTION_MOVE:
                if (mController.isOutOfScope(event)) {
                    break;
                }
                mController.scrollBy(event);
                mController.trackMovement(event);
                break;
            case MotionEvent.ACTION_UP:
                mController.startScroll();
                mController.clearVelocity();
                break;
            case MotionEvent.ACTION_CANCEL:
                mController.abortScrolling();
                mController.clearVelocity();
                break;
        }
        return true;
    }

    @Override
    public void computeScroll() {
        mController.computeScroll();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (mLayout == null || mContent == null) {
            return;
        }

        int l = 0, t = 0, r = 0, b = 0;
        switch (mDirection) {
            case SwipeState.DIRECTION_LEFT:
                l = getMeasuredHeight();
                t = getPaddingTop();
                r = l + mLayout.getMeasuredWidth();
                b = t + mLayout.getMeasuredHeight();
                break;
            case SwipeState.DIRECTION_RIGHT:
                l = -mLayout.getMeasuredWidth();
                t = getPaddingTop();
                r = 0;
                b = t + mLayout.getMeasuredHeight();
                break;
            case SwipeState.DIRECTION_UP:
                l = 0;
                t = bottom;
                r = right;
                b = t + mLayout.getMeasuredHeight();
                break;
            case SwipeState.DIRECTION_DOWN:
                l = 0;
                t = -mLayout.getMeasuredHeight();
                r = right;
                b = 0;
                break;
        }
        mLayout.layout(l, t, r, b);
        mContent.layout(left, top, right, bottom);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (SwipeState.isHorizontal(mDirection)) {
            super.onMeasure(widthMeasureSpec + mLayout.getWidth(), heightMeasureSpec);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec + mLayout.getHeight());
        }
    }

    public void addSwipeLayout(@LayoutRes int layoutId) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        mLayout = (ViewGroup) layoutInflater.inflate(layoutId, null);
        ViewGroup.LayoutParams layoutParams = SwipeState.isHorizontal(mDirection)
                ? new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT)
                : new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        addView(mLayout, layoutParams);
        mContentIndex++;
    }
}
