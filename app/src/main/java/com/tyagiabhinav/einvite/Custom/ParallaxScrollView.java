package com.tyagiabhinav.einvite.Custom;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by abhinavtyagi on 24/03/16.
 */
public class ParallaxScrollView extends ScrollView {

    public interface OnScrollChangedListener {
        public void onScrollChanged(int deltaX, int deltaY);
    }

    private OnScrollChangedListener mOnScrollChangedListener;

    public ParallaxScrollView(Context context) {
        super(context);
    }

    public ParallaxScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ParallaxScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if(mOnScrollChangedListener != null) {
            mOnScrollChangedListener.onScrollChanged(l - oldl, t - oldt);
        }
    }

    public void setOnScrollChangedListener(OnScrollChangedListener listener) {
        mOnScrollChangedListener = listener;
    }
}