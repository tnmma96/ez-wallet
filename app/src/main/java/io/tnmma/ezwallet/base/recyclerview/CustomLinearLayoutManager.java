package io.tnmma.ezwallet.base.recyclerview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;

/**
 * Custom LinearLayoutManager with a method to scroll to a position and center it inside parent
 * RecyclerView
 *
 * @see <a
 *     href="https://stackoverflow.com/questions/32241948/how-can-i-control-the-scrolling-speed-of-recyclerview-smoothscrolltopositionpos">
 *     How can I control the scrolling speed of recyclerView.smoothScrollToPosition(position)? </a>
 *     <p><a
 *     href="https://stackoverflow.com/questions/38416146/recyclerview-smoothscroll-to-position-in-the-center-android">
 *     RecyclerView smoothScroll to position in the center. android </a>
 */
public class CustomLinearLayoutManager extends LinearLayoutManager {

    private static final String TAG = CustomLinearLayoutManager.class.getSimpleName();

    private static final float MILLISECONDS_PER_INCH = 25f; // Scroll speed, the smaller the faster

    private final LinearSmoothScroller centerSmoothScroller;

    public CustomLinearLayoutManager(Context context) {
        super(context);
        centerSmoothScroller = getCenterSmoothScroller(context);
    }

    public CustomLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
        centerSmoothScroller = getCenterSmoothScroller(context);
    }

    public CustomLinearLayoutManager(
            Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        centerSmoothScroller = getCenterSmoothScroller(context);
    }

    private LinearSmoothScroller getCenterSmoothScroller(Context context) {
        return new LinearSmoothScroller(context) {

            // Calculate the time needed to scroll a pixel
            @Override
            protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                return MILLISECONDS_PER_INCH / displayMetrics.densityDpi;
            }

            // Center the selected item inside parent RecyclerView
            @Override
            public int calculateDtToFit(
                    int viewStart, int viewEnd, int boxStart, int boxEnd, int snapPreference) {
                return (boxStart + (boxEnd - boxStart) / 2)
                        - (viewStart + (viewEnd - viewStart) / 2);
            }
        };
    }

    public void smoothScrollAndCenter(int position) {
        centerSmoothScroller.setTargetPosition(position);
        startSmoothScroll(centerSmoothScroller);
    }
}
