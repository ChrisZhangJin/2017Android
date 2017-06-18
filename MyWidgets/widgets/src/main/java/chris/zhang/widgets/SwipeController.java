package chris.zhang.widgets;

import android.content.Context;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.OverScroller;

/**
 * Created by Administrator on 2017/6/18.
 */

class SwipeController {
    private final View mScrollView;
    private final int mDirection;
    private final OverScroller mScroller;
    private final View mSwipeView;
    private VelocityTracker mVelocityTracker;
    private SwipeState mState;

    SwipeController(Context context, View scrollView, View swipeView, int direction) {
        mScrollView = scrollView;
        mSwipeView = swipeView;
        mDirection = direction;
        mScroller = new OverScroller(context);
    }

    void trackVelocity(MotionEvent event) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }

    void clearVelocity() {
        if (mVelocityTracker == null) {
            return;
        }

        mVelocityTracker.clear();
        mVelocityTracker.recycle();
        mVelocityTracker = null;
    }

    void trackMovement(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mState = new SwipeState(event.getX(), event.getY(), mDirection);
                break;
            case MotionEvent.ACTION_MOVE:
                mState.setLastMovement(event.getX(), event.getY());
                break;
        }
    }

    void abortScrolling() {
        if (!mScroller.isFinished()) {
            mScroller.abortAnimation();
        }
    }

    boolean isOutOfScope(MotionEvent event) {
        if (mState == null || mSwipeView == null) {
            return false;
        }
        return mState.isOutOfScope(event.getX(), event.getY(), mScrollView, mSwipeView.getWidth(), mSwipeView.getHeight());
    }

    void scrollBy(MotionEvent event) {
        if (mState == null) {
            return;
        }
        // positive means moving left or top
        final int distance = mState.getLastDistance(event.getX(), event.getY());
        if (SwipeState.isHorizontal(mDirection)) {
            mScrollView.scrollBy(distance, 0);
        } else {
            mScrollView.scrollBy(0, distance);
        }
    }

    void startScroll() {
        if (SwipeState.isHorizontal(mDirection)) {
            final int scrollX = mScrollView.getScrollX();
            final int absScrollX = Math.abs(scrollX);
            if (absScrollX > mSwipeView.getWidth() / 2) {
                mScroller.startScroll(scrollX, 0, mSwipeView.getWidth() - absScrollX, 0, 300);
            } else {
                mScroller.startScroll(scrollX, 0, -scrollX, 100);
            }
        } else {
            final int scrollY = mScrollView.getScrollY();
            final int absScrollY = Math.abs(scrollY);
            if (absScrollY > mSwipeView.getHeight() / 2) {
                mScroller.startScroll(0, scrollY, 0, mSwipeView.getHeight() - absScrollY, 300);
            } else {
                mScroller.startScroll(0, scrollY, 0, -scrollY, 100);
            }
        }
        mSwipeView.invalidate();
    }

    void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            if (SwipeState.isHorizontal(mDirection)) {
                mSwipeView.scrollTo(mScroller.getCurrX(), 0);
            } else {
                mSwipeView.scrollTo(0, mScroller.getCurrY());
            }
            mScrollView.postInvalidate();
        }
    }
}
