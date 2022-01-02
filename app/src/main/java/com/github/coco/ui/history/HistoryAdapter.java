package com.github.coco.ui.history;

import com.github.coco.R;
import com.github.coco.base.BaseAdapter;
import com.github.coco.databinding.ItemHistoryBinding;
import com.github.coco.entity.History;

/**
 * Created on 2022/1/2.
 *
 * @author wy
 */
public class HistoryAdapter extends BaseAdapter<History, ItemHistoryBinding> {
    public HistoryAdapter() {
        super(R.layout.item_history);
    }

    @Override
    protected void convert(ItemHistoryBinding binding, History item) {
        binding.setHistory(item);
    }
}
