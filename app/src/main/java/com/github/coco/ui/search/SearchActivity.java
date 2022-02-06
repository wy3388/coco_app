package com.github.coco.ui.search;

import android.os.Bundle;

import com.github.coco.R;
import com.github.coco.base.BaseVMActivity;
import com.github.coco.common.AppDatabase;
import com.github.coco.common.BundleBuilder;
import com.github.coco.databinding.ActivitySearchBinding;
import com.github.coco.entity.SearchHistory;
import com.github.coco.ui.search.adapter.SearchHistoryAdapter;
import com.github.coco.ui.search.vm.SearchViewModel;
import com.github.coco.utils.ActivityUtil;
import com.github.coco.utils.ToastUtil;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

/**
 * Created on 2022/1/2.
 *
 * @author wy
 */
public class SearchActivity extends BaseVMActivity<ActivitySearchBinding, SearchViewModel> {

    private SearchHistoryAdapter adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    protected Class<SearchViewModel> mClass() {
        return SearchViewModel.class;
    }

    @Override
    protected void init() {
        // 输入框获取焦点
        binding.searchEt.requestFocus();
        adapter = new SearchHistoryAdapter();
        FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(this);
        flexboxLayoutManager.setFlexWrap(FlexWrap.WRAP);
        flexboxLayoutManager.setFlexDirection(FlexDirection.ROW);
        flexboxLayoutManager.setAlignItems(AlignItems.STRETCH);
        flexboxLayoutManager.setJustifyContent(JustifyContent.FLEX_START);
        binding.searchHistoryRv.setAdapter(adapter);
        binding.searchHistoryRv.setLayoutManager(flexboxLayoutManager);
        binding.clearSearchHistoryBtn.setOnClickListener(view -> model.clearSearchHistory());
        binding.backBtn.setOnClickListener(view -> finish());
        binding.searchBtn.setOnClickListener(view -> {
            String key = binding.searchEt.getText().toString();
            if ("".equals(key)) {
                ToastUtil.show(this, "请输入搜索内容");
                return;
            }
            // 写入搜索记录
            model.insertSearchHistory(key);
            Bundle bundle = BundleBuilder.builder()
                    .putString("key", key)
                    .build();
            ActivityUtil.start(this, SearchInfoActivity.class, bundle);
        });
        adapter.setOnItemClickListener((baseQuickAdapter, view, position) -> {
            SearchHistory searchHistory = adapter.getData().get(position);
            Bundle bundle = BundleBuilder.builder()
                    .putString("key", searchHistory.getKey())
                    .build();
            ActivityUtil.start(this, SearchInfoActivity.class, bundle);
        });
    }

    @Override
    protected void observer() {
        AppDatabase.getInstance().searchHistoryDao().findAll().observe(this, searchHistories -> adapter.setNewInstance(searchHistories));
    }
}
