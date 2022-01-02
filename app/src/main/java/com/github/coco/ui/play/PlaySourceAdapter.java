package com.github.coco.ui.play;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.github.coco.R;
import com.github.coco.databinding.ItemSourceBinding;
import com.github.coco.ui.info.InfoAdapter;
import com.github.lib.bean.VideoPlay;

/**
 * Created on 2022/1/2.
 *
 * @author wy
 */
public class PlaySourceAdapter extends BaseQuickAdapter<VideoPlay.Source, BaseViewHolder> {

    private InfoAdapter.OnSelectedListener listener;

    public PlaySourceAdapter() {
        super(R.layout.item_source);
    }

    @Override
    protected void onItemViewHolderCreated(@NonNull BaseViewHolder viewHolder, int viewType) {
        DataBindingUtil.bind(viewHolder.itemView);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, VideoPlay.Source source) {
        ItemSourceBinding binding = DataBindingUtil.getBinding(holder.itemView);
        if (binding != null) {
            binding.setSource(source);
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
