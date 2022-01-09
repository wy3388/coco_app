package com.github.coco.ui.play;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.github.coco.R;
import com.github.coco.databinding.ItemSourceBinding;
import com.github.coco.entity.Play;
import com.github.coco.ui.info.InfoAdapter;

/**
 * Created on 2022/1/2.
 *
 * @author wy
 */
public class PlaySourceAdapter extends BaseQuickAdapter<Play, BaseViewHolder> {

    private InfoAdapter.OnSelectedListener listener;

    public PlaySourceAdapter() {
        super(R.layout.item_source);
    }

    @Override
    protected void onItemViewHolderCreated(@NonNull BaseViewHolder viewHolder, int viewType) {
        DataBindingUtil.bind(viewHolder.itemView);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, Play play) {
        ItemSourceBinding binding = DataBindingUtil.getBinding(holder.itemView);
        if (binding != null) {
            binding.setPlay(play);
        }
        if (listener != null) {
            listener.onSelected(holder, holder.getLayoutPosition());
        }
    }

    public void setOnSelectedListener(InfoAdapter.OnSelectedListener listener) {
        this.listener = listener;
    }

    public interface OnSelectedListener {
        void onSelected(BaseViewHolder holder, int position);
    }
}
