package com.github.coco.ui.home;

import com.github.coco.R;
import com.github.coco.base.BaseAdapter;
import com.github.coco.databinding.ItemHomeBinding;
import com.github.lib.bean.Video;

/**
 * Created on 2022/1/1.
 *
 * @author wy
 */
public class HomeAdapter extends BaseAdapter<Video, ItemHomeBinding> {

    public HomeAdapter() {
        super(R.layout.item_home);
    }

    @Override
    protected void convert(ItemHomeBinding binding, Video item) {
        binding.setVideo(item);
    }
}
