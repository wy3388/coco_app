package com.github.coco.ui.search;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.github.coco.R;
import com.github.coco.base.BaseVMActivity;
import com.github.coco.databinding.ActivitySearchInfoBinding;
import com.github.coco.ui.search.vm.SearchInfoViewModel;

/**
 * Created on 2022/1/2.
 *
 * @author wy
 */
public class SearchInfoActivity extends BaseVMActivity<ActivitySearchInfoBinding, SearchInfoViewModel> {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_search_info;
    }

    @Override
    protected Class<SearchInfoViewModel> mClass() {
        return SearchInfoViewModel.class;
    }

    @Override
    protected void init() {
        binding.setLifecycleOwner(this);
        binding.setViewModel(model);
        binding.searchRv.setAdapter(model.getAdapter());
        binding.searchRv.setLayoutManager(new LinearLayoutManager(this));
        String key = getIntent().getExtras().getString("key");
        if (key != null && !"".equals(key)) {
            model.loadData(key);
        }
    }

    @Override
    protected void observer() {
        model.getVideos().observe(this, videos -> model.getAdapter().setNewInstance(videos));
    }
}
