package com.github.coco.ui.search.adapter;

import com.github.coco.R;
import com.github.coco.base.BaseAdapter;
import com.github.coco.databinding.ItemSearchHistoryBinding;
import com.github.coco.entity.SearchHistory;

/**
 * Created on 2022/1/3.
 *
 * @author wy
 */
public class SearchHistoryAdapter extends BaseAdapter<SearchHistory, ItemSearchHistoryBinding> {
    public SearchHistoryAdapter() {
        super(R.layout.item_search_history);
    }

    @Override
    protected void convert(ItemSearchHistoryBinding binding, SearchHistory item) {
        binding.setSearchHistory(item);
    }
}
