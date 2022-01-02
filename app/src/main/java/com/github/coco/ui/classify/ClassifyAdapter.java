package com.github.coco.ui.classify;

import com.github.coco.R;
import com.github.coco.base.BaseAdapter;
import com.github.coco.databinding.ItemClassifyBinding;
import com.github.coco.entity.Classify;

/**
 * Created on 2022/1/2.
 *
 * @author wy
 */
public class ClassifyAdapter extends BaseAdapter<Classify, ItemClassifyBinding> {
    public ClassifyAdapter() {
        super(R.layout.item_classify);
    }

    @Override
    protected void convert(ItemClassifyBinding binding, Classify item) {
        binding.setClassify(item);
    }
}
