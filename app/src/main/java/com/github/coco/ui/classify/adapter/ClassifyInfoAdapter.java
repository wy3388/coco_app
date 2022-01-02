package com.github.coco.ui.classify.adapter;

import com.github.coco.R;
import com.github.coco.base.BaseAdapter;
import com.github.coco.databinding.ItemClassifyInfoBinding;
import com.github.lib.bean.Video;

/**
 * Created on 2022/1/2.
 *
 * @author wy
 */
public class ClassifyInfoAdapter extends BaseAdapter<Video, ItemClassifyInfoBinding> {
    public ClassifyInfoAdapter() {
        super(R.layout.item_classify_info);
    }

    @Override
    protected void convert(ItemClassifyInfoBinding binding, Video item) {
        binding.setVideo(item);
    }
}
