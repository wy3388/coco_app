package com.github.coco.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.coco.R;

/**
 * Created on 2022/1/2.
 *
 * @author wy
 */
public class IToolbar extends LinearLayout {
    public IToolbar(Context context) {
        super(context);
    }

    public IToolbar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public IToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public IToolbar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void initView(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.view_tool_bar, this, true);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.IToolbar);
        boolean aBoolean = typedArray.getBoolean(R.styleable.IToolbar_show_back, true);
        LinearLayout backBtn = findViewById(R.id.back_btn);
        if (aBoolean) {
            backBtn.setOnClickListener(view -> ((AppCompatActivity) context).finish());
        } else {
            backBtn.setVisibility(View.GONE);
        }
        boolean aBoolean1 = typedArray.getBoolean(R.styleable.IToolbar_show_search, false);
        LinearLayout searchBtn = findViewById(R.id.search_btn);
        if (aBoolean1) {
            searchBtn.setVisibility(View.VISIBLE);
            searchBtn.setOnClickListener(view -> {
                // todo 跳转搜索页面
            });
        }
        typedArray.recycle();
    }
}
