package com.github.coco.base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

/**
 * Created on 2022/1/1.
 *
 * @author wy
 */
public abstract class BaseActivity<V extends ViewDataBinding> extends AppCompatActivity {
    protected V binding;

    protected abstract int getLayoutId();

    protected void init() {

    }

    protected void observer() {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, getLayoutId());
        init();
        observer();
    }
}
