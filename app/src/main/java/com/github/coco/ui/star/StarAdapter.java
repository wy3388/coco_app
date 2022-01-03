package com.github.coco.ui.star;

import com.github.coco.R;
import com.github.coco.base.BaseAdapter;
import com.github.coco.databinding.ItemStarBinding;
import com.github.coco.entity.Star;

/**
 * Created on 2022/1/3.
 *
 * @author wy
 */
public class StarAdapter extends BaseAdapter<Star, ItemStarBinding> {
    public StarAdapter() {
        super(R.layout.item_star);
    }

    @Override
    protected void convert(ItemStarBinding binding, Star item) {
        binding.setStar(item);
    }
}
