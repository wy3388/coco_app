package com.github.coco.ui.info;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.github.coco.R;
import com.github.coco.databinding.ItemInfoBinding;
import com.github.coco.entity.Episodes;

/**
 * Created on 2022/1/2.
 *
 * @author wy
 */
public class InfoAdapter extends BaseQuickAdapter<Episodes, BaseViewHolder> {

    private OnSelectedListener listener;

    public InfoAdapter() {
        super(R.layout.item_info);
    }

    @Override
    protected void onItemViewHolderCreated(@NonNull BaseViewHolder viewHolder, int viewType) {
        DataBindingUtil.bind(viewHolder.itemView);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, Episodes episodes) {
        ItemInfoBinding binding = DataBindingUtil.getBinding(holder.itemView);
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
