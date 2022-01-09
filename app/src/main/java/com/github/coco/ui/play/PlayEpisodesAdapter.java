package com.github.coco.ui.play;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.github.coco.R;
import com.github.coco.databinding.ItemEpisodesBinding;
import com.github.coco.entity.Episodes;

/**
 * Created on 2022/1/8.
 *
 * @author wy
 */
public class PlayEpisodesAdapter extends BaseQuickAdapter<Episodes, BaseViewHolder> {

    private OnSelectedListener listener;

    public PlayEpisodesAdapter() {
        super(R.layout.item_episodes);
    }

    @Override
    protected void onItemViewHolderCreated(@NonNull BaseViewHolder viewHolder, int viewType) {
        DataBindingUtil.bind(viewHolder.itemView);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, Episodes episodes) {
        ItemEpisodesBinding binding = DataBindingUtil.getBinding(holder.itemView);
        if (binding != null) {
            binding.setEpisodes(episodes);
        }
        if (listener != null) {
            listener.onSelected(holder, holder.getLayoutPosition());
        }
    }


    public void setOnSelectedListener(OnSelectedListener listener) {
        this.listener = listener;
    }

    public interface OnSelectedListener {
        void onSelected(BaseViewHolder holder, int position);
    }
}
