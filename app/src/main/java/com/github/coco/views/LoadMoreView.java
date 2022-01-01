package com.github.coco.views;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.loadmore.BaseLoadMoreView;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.github.coco.R;

/**
 * Created on 2021/12/12.
 *
 * @author wy
 */
public class LoadMoreView extends BaseLoadMoreView {
    @NonNull
    @Override
    public View getLoadComplete(@NonNull BaseViewHolder baseViewHolder) {
        return baseViewHolder.getView(R.id.load_more_load_complete_view);
    }

    @NonNull
    @Override
    public View getLoadEndView(@NonNull BaseViewHolder baseViewHolder) {
        return baseViewHolder.getView(R.id.load_more_load_end_view);
    }

    @NonNull
    @Override
    public View getLoadFailView(@NonNull BaseViewHolder baseViewHolder) {
        return baseViewHolder.getView(R.id.load_more_load_fail_view);
    }

    @NonNull
    @Override
    public View getLoadingView(@NonNull BaseViewHolder baseViewHolder) {
        return baseViewHolder.getView(R.id.load_more_loading_view);
    }

    @NonNull
    @Override
    public View getRootView(@NonNull ViewGroup viewGroup) {
        return LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_load_more, viewGroup, false);
    }
}
