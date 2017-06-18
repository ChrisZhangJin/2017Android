package chris.zhang.widgets;

import android.view.View;

/**
 * Created by Administrator on 2017/6/18.
 */

class SwipeState {
    static final int DIRECTION_LEFT = 1;
    static final int DIRECTION_RIGHT = 2;
    static final int DIRECTION_UP = 3;
    static final int DIRECTION_DOWN = 4;

    private final float mDownX;
    private final float mDownY;
    private final int mDirection;
    private float mLastX;
    private float mLastY;

    SwipeState(float x, float y, int dir) {
        mDownX = x;
        mDownY = y;
        mDirection = dir;
    }

    static boolean isHorizontal(int dir) {
        return dir == DIRECTION_LEFT || dir == DIRECTION_RIGHT;
    }

    void setLastMovement(float x, float y) {
        mLastX = x;
        mLastY = y;
    }

    int getLastDistance(float x, float y) {
        return (int) (isHorizontal(mDirection) ? mLastX - x : mLastY - y);
    }

    int getDistance(float x, float y) {
        return (int) (isHorizontal(mDirection) ? mDownX - x : mDownY - y);
    }

    boolean isOutOfScope(float x, float y, View scrollView, int width, int height) {
        if (isHorizontal(mDirection)) {
            final int move = (int) (mDownX - x);
            if (move == 0) {
                return false;
            }
            final int distance = (int) (mLastX - x);
            final int scrollX = scrollView.getScrollX() + distance; // add the coming distance
            if (mDirection == DIRECTION_LEFT) {
                return move > 0 ? scrollX > width : scrollX < 0;
            } else {
                return move > 0 ? scrollX > 0 : Math.abs(scrollX) > width;
            }
        } else {
            final int move = (int) (mDownY - y);
            if (move == 0) {
                return false;
            }
            final int distance = (int) (mLastY - y);
            final int scrollY = scrollView.getScrollY() + distance;
            if (mDirection == DIRECTION_UP) {
                return move > 0 ? scrollY > height : scrollY < 0;
            } else {
                return move > 0 ? scrollY < 0 : Math.abs(scrollY) > height;
            }
        }
    }
}
