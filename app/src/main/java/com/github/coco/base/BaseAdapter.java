package com.github.coco.base;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

/**
 * Created on 2022/1/1.
 *
 * @author wy
 */
public abstract class BaseAdapter<T, V extends ViewDataBinding> extends BaseQuickAdapter<T, BaseViewHolder> implements LoadMoreModule {
    public BaseAdapter(int layoutResId) {
        super(layoutResId);
    }

    protected abstract void convert(V binding, T item);

    @Override
    protected void onItemViewHolderCreated(@NonNull BaseViewHolder viewHolder, int viewType) {
        DataBindingUtil.bind(viewHolder.itemView);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, T t) {
        convert(DataBindingUtil.getBinding(holder.itemView), t);
    }
}
