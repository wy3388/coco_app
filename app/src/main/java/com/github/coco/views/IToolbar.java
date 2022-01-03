package com.github.coco.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.coco.R;
import com.github.coco.ui.search.SearchActivity;
import com.github.coco.utils.ActivityUtil;

/**
 * Created on 2022/1/2.
 *
 * @author wy
 */
public class IToolbar extends LinearLayout {

    private ImageView starIv;
    private LinearLayout starBtn;

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
        if (aBoolean1) {
            LinearLayout searchBtn = findViewById(R.id.search_btn);
            searchBtn.setVisibility(View.VISIBLE);
            searchBtn.setOnClickListener(view -> ActivityUtil.start(context, SearchActivity.class));
        }
        boolean aBoolean2 = typedArray.getBoolean(R.styleable.IToolbar_show_star, false);
        if (aBoolean2) {
            starBtn = findViewById(R.id.star_btn);
            starIv = findViewById(R.id.star_iv);
            starBtn.setVisibility(View.VISIBLE);
        }
        typedArray.recycle();
    }

    public void setStarClickListener(View.OnClickListener listener) {
        if (starBtn != null) {
            starBtn.setOnClickListener(listener);
        }
    }

    public void setImageResource(@DrawableRes int redId) {
        if (starIv != null) {
            starIv.setImageResource(redId);
        }
    }
}
