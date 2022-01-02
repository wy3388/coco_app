package com.github.coco.ui.search;

import android.os.Bundle;

import com.github.coco.R;
import com.github.coco.base.BaseActivity;
import com.github.coco.common.BundleBuilder;
import com.github.coco.databinding.ActivitySearchBinding;
import com.github.coco.utils.ActivityUtil;
import com.github.coco.utils.ToastUtil;

/**
 * Created on 2022/1/2.
 *
 * @author wy
 */
public class SearchActivity extends BaseActivity<ActivitySearchBinding> {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    protected void init() {
        binding.backBtn.setOnClickListener(view -> finish());
        binding.searchBtn.setOnClickListener(view -> {
            String value = binding.searchEt.getText().toString();
            if ("".equals(value)) {
                ToastUtil.show(this, "请输入搜索内容");
                return;
            }
            Bundle bundle = BundleBuilder.builder()
                    .putString("key", value)
                    .build();
            ActivityUtil.start(this, SearchInfoActivity.class, bundle);
        });
    }
}
