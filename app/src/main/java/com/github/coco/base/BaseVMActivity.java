package com.github.coco.base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

/**
 * Created on 2022/1/1.
 *
 * @author wy
 */
public abstract class BaseVMActivity<V extends ViewDataBinding, M extends ViewModel> extends AppCompatActivity {
    protected V binding;
    protected M model;

    protected abstract int getLayoutId();

    protected abstract Class<M> mClass();

    protected void init() {

    }

    protected void observer() {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, getLayoutId());
        model = new ViewModelProvider(this).get(mClass());
        init();
        observer();
    }
}
