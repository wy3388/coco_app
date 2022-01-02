package com.github.coco.ui.search.adapter;

import com.github.coco.R;
import com.github.coco.base.BaseAdapter;
import com.github.coco.databinding.ItemSearchInfoBinding;
import com.github.lib.bean.Video;

/**
 * Created on 2022/1/2.
 *
 * @author wy
 */
public class SearchInfoAdapter extends BaseAdapter<Video, ItemSearchInfoBinding> {
    public SearchInfoAdapter() {
        super(R.layout.item_search_info);
    }

    @Override
    protected void convert(ItemSearchInfoBinding binding, Video item) {
        binding.setVideo(item);
    }
}
