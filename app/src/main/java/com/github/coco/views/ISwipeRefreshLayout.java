package com.github.coco.views;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.github.coco.R;

/**
 * Created on 2022/1/9.
 *
 * @author wy
 */
public class ISwipeRefreshLayout extends SwipeRefreshLayout {
    public ISwipeRefreshLayout(@NonNull Context context) {
        super(context);
    }

    public ISwipeRefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setColorSchemeResources(R.color.primary);
    }
}
