package com.github.coco.ui.history;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.github.coco.R;
import com.github.coco.databinding.ItemHistoryBinding;
import com.github.coco.entity.History;

/**
 * Created on 2022/1/2.
 *
 * @author wy
 */
public class HistoryAdapter extends BaseQuickAdapter<History, BaseViewHolder> {

    private boolean isSelect = false;
    private boolean selected = false;

    public HistoryAdapter() {
        super(R.layout.item_history);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setSelect(boolean isSelect) {
        this.isSelect = isSelect;
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void selectedAll(boolean selected) {
        this.selected = selected;
        notifyDataSetChanged();
    }

    public void selected(boolean selected) {
        this.selected = selected;
    }

    @Override
    protected void onItemViewHolderCreated(@NonNull BaseViewHolder viewHolder, int viewType) {
        DataBindingUtil.bind(viewHolder.itemView);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, History history) {
        ItemHistoryBinding binding = DataBindingUtil.getBinding(holder.itemView);
        if (binding != null) {
            binding.setHistory(history);
        }
        ImageView imageView = holder.itemView.findViewById(R.id.select_icon_iv);
        if (isSelect) {
            imageView.setVisibility(View.VISIBLE);
            if (selected) {
                imageView.setImageResource(R.drawable.ic_select);
            } else {
                imageView.setImageResource(R.drawable.ic_un_select);
            }
        } else {
            imageView.setVisibility(View.GONE);
        }
    }
}
