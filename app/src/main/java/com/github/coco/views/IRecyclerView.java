package com.github.coco.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.github.coco.R;

/**
 * Created on 2022/1/1.
 *
 * @author wy
 */
public class IRecyclerView extends LinearLayout {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private BaseQuickAdapter<?, BaseViewHolder> adapter;

    public IRecyclerView(Context context) {
        super(context);
    }

    public IRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public IRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public IRecyclerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void initView(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.view_recycler_view, this, true);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.IRecyclerView);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        recyclerView = findViewById(R.id.recycler_view);
        swipeRefreshLayout.setColorSchemeResources(R.color.primary);
        boolean aBoolean = typedArray.getBoolean(R.styleable.IRecyclerView_default_refreshing, true);
        if (aBoolean) {
            swipeRefreshLayout.setRefreshing(true);
        }
        if (typedArray.hasValue(R.styleable.IRecyclerView_scroll_mode)) {
            int anInt = typedArray.getInt(R.styleable.IRecyclerView_scroll_mode, View.OVER_SCROLL_ALWAYS);
            recyclerView.setOverScrollMode(anInt);
        }
        typedArray.recycle();
    }

    public void setRefreshing(Boolean refreshing) {
        swipeRefreshLayout.setRefreshing(refreshing);
    }

    public void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        recyclerView.setLayoutManager(layoutManager);
    }

    public void setAdapter(BaseQuickAdapter<?, BaseViewHolder> adapter) {
        this.adapter = adapter;
        recyclerView.setAdapter(adapter);
    }

    public void setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener listener) {
        swipeRefreshLayout.setOnRefreshListener(listener);
    }

    public void setSwipeRefreshLayoutEnabled(Boolean enabled) {
        swipeRefreshLayout.setEnabled(enabled);
    }

    public BaseQuickAdapter<?, BaseViewHolder> getAdapter() {
        return this.adapter;
    }
}
